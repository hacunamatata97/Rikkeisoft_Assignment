package com.example.hacunamatata.rikkeisoft_assignment.realm;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;
import com.example.hacunamatata.rikkeisoft_assignment.model.Note;
import io.realm.Realm;
import io.realm.RealmResults;

public class RealmController {

    private static RealmController instance;
    private final Realm realm;

    public RealmController(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController with(Fragment fragment) {

        if (instance == null) {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmController with(Activity activity) {

        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController with(Application application) {

        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }

    public static RealmController getInstance() {
        return instance;
    }

    public Realm getRealm() {
        return realm;
    }

    //Refresh the realm instance
    public void refresh() {
        realm.refresh();
    }

    //clear all objects from Book.class
    public void clearAll() {
        realm.beginTransaction();
        realm.clear(Note.class);
        realm.commitTransaction();
    }

    //find all objects in the Book.class
    public RealmResults<Note> getNotes() {
        return realm.where(Note.class).findAll();
    }

    //query a single item with the given id
    public Note getNote(String id) {
        return realm.where(Note.class).equalTo("id", id).findFirst();
    }

    //check if Book.class is empty
    public boolean hasNotes() {
        return !realm.allObjects(Note.class).isEmpty();
    }
}
