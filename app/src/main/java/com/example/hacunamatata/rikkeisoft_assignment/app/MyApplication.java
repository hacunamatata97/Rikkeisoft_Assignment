package com.example.hacunamatata.rikkeisoft_assignment.app;

import android.app.Application;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        RealmConfiguration realmConfiguration =
                new RealmConfiguration.Builder(this).name("MyRealm.realm")
                        .schemaVersion(0)
                        .deleteRealmIfMigrationNeeded()
                        .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
