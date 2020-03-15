package com.az.pplayer.Storage.Db.DbModels;

import android.content.Context;

import com.az.pplayer.Storage.Db.phPlayerDb;

import androidx.room.Room;

public class dbBuilder {
    private phPlayerDb database;
    public dbBuilder(Context context) {
        database = Room.databaseBuilder(context, phPlayerDb.class,"phplayerdb").build();
    }

    public phPlayerDb getDatabase() {
        return database;
    }
}
