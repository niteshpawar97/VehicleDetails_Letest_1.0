package com.niketit.vehicleinfo.utils.database;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class EventEntity {

    @PrimaryKey(autoGenerate = true)
    private int vehicleID;

    @ColumnInfo(name = "vehicleNumber")
    private String vehicleNumber;

    @ColumnInfo(name = "ownerName")
    private String ownerName;

    public int getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(int vehicleID) {
        this.vehicleID = vehicleID;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}