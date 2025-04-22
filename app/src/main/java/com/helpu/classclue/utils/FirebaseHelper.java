package com.helpu.classclue.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHelper {
    private static FirebaseHelper instance;
    private final FirebaseAuth auth;
    private final DatabaseReference database;

    private FirebaseHelper() {
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
    }

    public static synchronized FirebaseHelper getInstance() {
        if (instance == null) {
            instance = new FirebaseHelper();
        }
        return instance;
    }

    public FirebaseAuth getAuth() {
        return auth;
    }

    public DatabaseReference getDatabase() {
        return database;
    }

    public DatabaseReference getUsersRef() {
        return database.child("users");
    }

    public DatabaseReference getSubjectsRef() {
        return database.child("subjects");
    }

    public DatabaseReference getEventsRef() {
        return database.child("events");
    }
}