package com.niketit.vehicleinfo.utils.database;

import com.niketit.vehicleinfo.controller.interfaces.RTOEventDao;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {EventEntity.class}, exportSchema = false,version = 1)
public abstract class RTODatabase extends RoomDatabase {
    public abstract RTOEventDao rtoEventDao();
}