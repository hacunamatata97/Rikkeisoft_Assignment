package com.example.hacunamatata.rikkeisoft_assignment.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.example.hacunamatata.rikkeisoft_assignment.R;
import com.example.hacunamatata.rikkeisoft_assignment.adapter.NoteAdapter;
import com.example.hacunamatata.rikkeisoft_assignment.adapter.RealmNotesAdapter;
import com.example.hacunamatata.rikkeisoft_assignment.model.Note;
import com.example.hacunamatata.rikkeisoft_assignment.realm.RealmController;
import com.example.hacunamatata.rikkeisoft_assignment.service.AlarmService;
import com.facebook.stetho.Stetho;
import io.realm.Realm;
import io.realm.RealmResults;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    public static MainActivity instance;

    private NoteAdapter adapter;
    private Realm realm;
    private LayoutInflater inflater;
    private RecyclerView recycler;
    private FloatingActionButton fab;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    public static MainActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Check database
        Stetho.initializeWithDefaults(this);

        // Set toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);
        recycler = findViewById(R.id.recycler);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // get Realm instance
        realm = RealmController.with(this).getRealm();

        // Setup recycler
        setupRecycler();

        // Set Share preference
        //        if (!Prefs.with(this).getPreLoad()) {
        //
        //        }

        // Refresh realm instance
        RealmController.with(this).refresh();

        // Setup RealmAdapter
        setRealmAdapter(RealmController.with(this).getNotes());

        // Check if there is an alarm goes off
        checkToggleStatus();

        // Tutorial
        Toast.makeText(this, "Press note to edit, long press to remove!", Toast.LENGTH_LONG).show();

        // Add new item
        addItem();
    }

    @Override
    protected void onStart() {
        super.onStart();
        instance = this;
    }

    private void addItem() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inflater = MainActivity.this.getLayoutInflater();
                View content = inflater.inflate(R.layout.edit_item, null);

                // Initialize components
                final EditText etTitle = content.findViewById(R.id.et_title);

                // Alarm
                final TimePicker tpAlarm = content.findViewById(R.id.tc_alarm);
                tpAlarm.setIs24HourView(true);
                final ToggleButton checkButton = content.findViewById(R.id.tg_btn_alarm_status);
                final EditText etImageUrl = content.findViewById(R.id.et_image_url);
                final EditText etContent = content.findViewById(R.id.et_content);

                // Dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setView(content)
                        .setTitle("Add Note")
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Note note = new Note();

                                // Set note's properties
                                note.setId(RealmController.getInstance().getNotes().size()
                                        + System.currentTimeMillis());
                                note.setTitle(etTitle.getText().toString());
                                note.setAlarm(tpAlarm.getHour() + ":" + tpAlarm.getMinute());
                                note.setChecked(checkButton.isChecked());
                                note.setImageUrl(etImageUrl.getText().toString());
                                note.setContent(etContent.getText().toString());

                                if (etTitle.getText() == null || etTitle.getText()
                                        .toString()
                                        .trim()
                                        .equals("")) {
                                    Toast.makeText(MainActivity.this,
                                            "Entry not saved, missing title", Toast.LENGTH_SHORT)
                                            .show();
                                } else {
                                    // Transaction
                                    realm.beginTransaction();

                                    realm.copyToRealm(note);

                                    realm.commitTransaction();

                                    adapter.notifyDataSetChanged();
                                    // End Transaction

                                    // Scroll to bottom
                                    recycler.scrollToPosition(
                                            RealmController.getInstance().getNotes().size() - 1);
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.cancel,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    public void checkToggleStatus() {
        RealmResults<Note> results = realm.where(Note.class).findAll();
        for (Note note : results) {
            if (note.isChecked()) {
                Log.d("MainActivity", "Alarm On");
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(
                        note.getAlarm().substring(0, note.getAlarm().indexOf(':'))));
                calendar.set(Calendar.MINUTE, Integer.parseInt(
                        note.getAlarm().substring(note.getAlarm().indexOf(':') + 1)));

                Intent intent = new Intent(MainActivity.this, AlarmService.class);
                pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);

                alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
            } else {
                alarmManager.cancel(pendingIntent);
                Log.d("MainActivity", "Alarm Off");
            }
        }
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void setRealmAdapter(RealmResults<Note> notes) {
        RealmNotesAdapter realmAdapter =
                new RealmNotesAdapter(this.getApplicationContext(), notes, true);
        adapter.setRealmAdapter(realmAdapter);
        adapter.notifyDataSetChanged();
    }

    private void setupRecycler() {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recycler.setHasFixedSize(true);

        // use a linear layout manager since the cards are vertically scrollable
        recycler.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));

        // create an empty adapter and add it to the recycler view
        adapter = new NoteAdapter(this);
        recycler.setAdapter(adapter);
    }
}
