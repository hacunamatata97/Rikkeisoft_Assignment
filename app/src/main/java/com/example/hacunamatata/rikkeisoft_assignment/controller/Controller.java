package com.example.hacunamatata.rikkeisoft_assignment.controller;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.example.hacunamatata.rikkeisoft_assignment.R;
import com.example.hacunamatata.rikkeisoft_assignment.adapter.NoteAdapter;
import com.example.hacunamatata.rikkeisoft_assignment.app.Prefs;
import com.example.hacunamatata.rikkeisoft_assignment.model.Note;
import io.realm.Realm;
import io.realm.RealmResults;

public class Controller {

    // CardView removeItem to remove item
    public static void removeItem(final Context context, final NoteAdapter noteAdapter,
            NoteAdapter.CardViewHolder holder, final Realm realm, final int position) {
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                RealmResults<Note> results = realm.where(Note.class).findAll();
                // Get note title to show in toast
                Note n = results.get(position);
                String title = n.getTitle();

                // Transaction
                realm.beginTransaction();
                // Remove single item
                results.remove(position);
                realm.commitTransaction();
                // End Transaction

                if (results.size() == 0) {
                    Prefs.with(context).setPreLoad(false);
                }

                noteAdapter.notifyDataSetChanged();

                Toast.makeText(context, title + " has been removed!", Toast.LENGTH_SHORT).show();

                return false;
            }
        });
    }

    // CardView onClick to edit item
    public static void editItem(final Context context, final NoteAdapter noteAdapter,
            NoteAdapter.CardViewHolder holder, final Note note, final Realm realm,
            final int position) {
        holder.cardView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Get layout
                LayoutInflater inflater =
                        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View content = inflater.inflate(R.layout.edit_item, null);

                // Initialize components
                final EditText etTitle = content.findViewById(R.id.et_title);

                // Alarm
                final TimePicker tpAlarm = content.findViewById(R.id.tc_alarm);
                tpAlarm.setIs24HourView(true);
                final ToggleButton checkedButton = content.findViewById(R.id.tg_btn_alarm_status);
                final EditText etImageUrl = content.findViewById(R.id.et_image_url);
                final EditText etContent = content.findViewById(R.id.et_content);

                // Set data into components
                etTitle.setText(note.getTitle());

                int hour = Integer.parseInt(note.getAlarm().substring(0, note.getAlarm().indexOf(':')));
                int minute = Integer.parseInt(note.getAlarm().substring(note.getAlarm().indexOf(':') + 1));
                tpAlarm.setHour(hour);
                tpAlarm.setMinute(minute);

                checkedButton.setChecked(note.isChecked());
                etImageUrl.setText(note.getImageUrl());
                etContent.setText(note.getContent());

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(content)
                        .setTitle("Edit Note")
                        .setPositiveButton("Save",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        RealmResults<Note> results =
                                                realm.where(Note.class).findAll();

                                        // Transaction
                                        realm.beginTransaction();

                                        results.get(position)
                                                .setTitle(etTitle.getText().toString());
                                        results.get(position)
                                                .setAlarm(tpAlarm.getHour()
                                                        + ":"
                                                        + tpAlarm.getMinute());
                                        results.get(position)
                                                .setChecked(checkedButton.isChecked());
                                        results.get(position)
                                                .setImageUrl(etImageUrl.getText().toString());
                                        results.get(position)
                                                .setContent(etContent.getText().toString());

                                        realm.commitTransaction();
                                        // End Transaction

                                        noteAdapter.notifyDataSetChanged();
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
}
