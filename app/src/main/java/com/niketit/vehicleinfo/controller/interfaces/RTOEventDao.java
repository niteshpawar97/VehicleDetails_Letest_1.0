package com.niketit.vehicleinfo.controller.interfaces;

import com.niketit.vehicleinfo.utils.database.EventEntity;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface RTOEventDao {

    @Query("SELECT * FROM evententity")
    List<EventEntity> getAllSavedVehicles();

    @Insert
    void insertAll(List<EventEntity> eventEntities);

    @Update
    void update(EventEntity eventEntities);

    @Delete
    void delete(EventEntity eventEntities);

    @Query("SELECT * FROM evententity where vehicleNumber=:numberOfVehicle")
    List<EventEntity> getEventSavedAlready(String numberOfVehicle);
}