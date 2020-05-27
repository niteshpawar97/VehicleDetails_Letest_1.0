package com.niketit.vehicleinfo.controller;

import com.niketit.vehicleinfo.utils.database.EventEntity;
import com.niketit.vehicleinfo.view.RTOApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

public class SetResponseData {

    private String numberInfo;
    private SimpleDateFormat simpleDateFormat;
    private String currentDate = "";


    public SetResponseData(String numberVehicle) {
        this.numberInfo = numberVehicle;
    }

    //"09-Sep-2015"
    public void setDataResponse(String result) {
        simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        currentDate = simpleDateFormat.format(Calendar.getInstance().getTime());

        try {
            JSONObject jsonObject = new JSONObject(result);
            LinkedHashMap<String, String> numberDetailsTable = new LinkedHashMap<>();
            JSONObject jsonObjectInfo = jsonObject.optJSONObject("info");

            //set sequence to arrange data and save data Room Persistence
            List<EventEntity> entityList = new ArrayList<>();
            EventEntity eventEntity = new EventEntity();

            //for 2
            JSONObject jsonObject2 = jsonObjectInfo.optJSONObject("2");
            String ownerName = jsonObject2.optString("Owner Name: ");
            numberDetailsTable.put("Owner Name:", ownerName);
            eventEntity.setOwnerName(ownerName);
            eventEntity.setVehicleNumber(numberInfo);
            entityList.add(eventEntity);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean isDataAlreadyExist = checkNumberAlreadyExist(numberInfo);
                    if (isDataAlreadyExist) {
                        RTOApplication.getsInstance().getDatabase().rtoEventDao().update(eventEntity);
                    } else {
                        RTOApplication.getsInstance().getDatabase().rtoEventDao().insertAll(entityList);
                    }
                }
            });
            thread.start();

            //for 4
            JSONObject jsonObject4 = jsonObjectInfo.optJSONObject("4");
            String makerModel = jsonObject4.optString("Maker / Model:");
            numberDetailsTable.put("Vehicle Name:", makerModel);

            //for 0
            JSONObject jsonObject0 = jsonObjectInfo.optJSONObject("0");
            String registrationNo = jsonObject0.optString("Registration No:");
            String registrationDate = jsonObject0.optString("Registration Date:");


            try {
                Date previousTime = simpleDateFormat.parse(registrationDate);
                Date currentTime = simpleDateFormat.parse(currentDate);
                String difference=getDateDifferenceInDDMMYYYY(previousTime,currentTime);
                numberDetailsTable.put("Vehicle Age:", difference);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            numberDetailsTable.put("Registration No:", registrationNo);

            //rto name
            String rtoName = jsonObjectInfo.optString("rto_name");
            String rtoRemoveLineBreaks = rtoName.replace("\n", "").replace("\r", "").trim();
            if (rtoRemoveLineBreaks.contains("Registering Authority:")) {
                String updatedRTO = rtoRemoveLineBreaks.replace("Registering Authority:", "");
                numberDetailsTable.put("Registering Authority:", updatedRTO);
            } else numberDetailsTable.put("Registering Authority:", rtoRemoveLineBreaks);
            numberDetailsTable.put("Registration Date:", registrationDate);

            //for 3
            JSONObject jsonObject3 = jsonObjectInfo.optJSONObject("3");
            String vehicleClass = jsonObject3.optString("Vehicle Class: ");
            String fuelType = jsonObject3.optString("Fuel Type:");
            numberDetailsTable.put("Vehicle Class:", vehicleClass);
            numberDetailsTable.put("Fuel Type", fuelType);

            //for 1
            JSONObject jsonObject1 = jsonObjectInfo.optJSONObject("1");
            String chassisNo = jsonObject1.optString("Chassis No:");
            String engineNo = jsonObject1.optString("Engine No:");
            numberDetailsTable.put("Chassis No:", chassisNo);
            numberDetailsTable.put("Engine No:", engineNo);

            //for 5
            JSONObject jsonObject5 = jsonObjectInfo.optJSONObject("5");
            //String fitnessUpto = jsonObject5.optString("Fitness Upto:");
            String insuranceUpto = jsonObject5.optString("Insurance Upto:");
            //numberDetailsTable.put("Fitness Upto:", fitnessUpto);
            numberDetailsTable.put("Insurance Upto:", insuranceUpto);

            //for 6
            JSONObject jsonObject6 = jsonObjectInfo.optJSONObject("6");
            String fuelNorms = jsonObject6.optString("Fuel Norms: ");
            String roadTaxPaidUpto = jsonObject6.optString("Road Tax Paid Upto:");
            numberDetailsTable.put("Fuel Norms:", fuelNorms);
            numberDetailsTable.put("Road Tax Paid Upto:", roadTaxPaidUpto);

            RTOApplication.getsInstance().setNumberDetails(numberDetailsTable);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String getDateDifferenceInDDMMYYYY(Date from, Date to) {
        Calendar fromDate = Calendar.getInstance();
        Calendar toDate = Calendar.getInstance();
        fromDate.setTime(from);
        toDate.setTime(to);
        int increment = 0;
        int year, month, day;
        System.out.println(fromDate.getActualMaximum(Calendar.DAY_OF_MONTH));
        if (fromDate.get(Calendar.DAY_OF_MONTH) > toDate.get(Calendar.DAY_OF_MONTH)) {
            increment = fromDate.getActualMaximum(Calendar.DAY_OF_MONTH);
        }
        // DAY CALCULATION
        if (increment != 0) {
            day = (toDate.get(Calendar.DAY_OF_MONTH) + increment) - fromDate.get(Calendar.DAY_OF_MONTH);
            increment = 1;
        } else {
            day = toDate.get(Calendar.DAY_OF_MONTH) - fromDate.get(Calendar.DAY_OF_MONTH);
        }

        // MONTH CALCULATION
        if ((fromDate.get(Calendar.MONTH) + increment) > toDate.get(Calendar.MONTH)) {
            month = (toDate.get(Calendar.MONTH) + 12) - (fromDate.get(Calendar.MONTH) + increment);
            increment = 1;
        } else {
            month = (toDate.get(Calendar.MONTH)) - (fromDate.get(Calendar.MONTH) + increment);
            increment = 0;
        }

        // YEAR CALCULATION
        year = toDate.get(Calendar.YEAR) - (fromDate.get(Calendar.YEAR) + increment);
        return year +" Years "+month+" Months " + day + " Days";
    }

    private boolean checkNumberAlreadyExist(String numberInfo) {
        boolean isDataAvailable = false;
        List<EventEntity> list = RTOApplication.getsInstance().getDatabase().rtoEventDao().getEventSavedAlready(numberInfo);
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                String savedNumberOfVehicle = list.get(i).getVehicleNumber();
                if (savedNumberOfVehicle.equals(numberInfo)) {
                    isDataAvailable = true;
                }
            }
        }
        return isDataAvailable;
    }
}
