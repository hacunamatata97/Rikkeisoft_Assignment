package com.example.hacunamatata.rikkeisoft_assignment.adapter;

import android.content.Context;
import com.example.hacunamatata.rikkeisoft_assignment.model.Note;
import io.realm.RealmResults;

public class RealmNotesAdapter extends RealmModelAdapter<Note> {

    public RealmNotesAdapter(Context context, RealmResults<Note> realmResults,
            boolean automaticUpdate) {

        super(context, realmResults, automaticUpdate);
    }
}