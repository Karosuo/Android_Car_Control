package com.example.karosuo.carcontrol;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by karosuo on 28/07/16.
 */
public class MyDBAccess {
    private MyDBHelper dbHelper;

    private SQLiteDatabase database;

    public final static String TABLE_NAME="MyTrips"; // name of table

    /** All the column names to acces the data or put */
    public final static String T_NAME="tripName";
    public final static String T_IMGURI="imgUri";
    public final static String T_FECHA="fecha";
    public final static String T_DURAC="duracion";
    public final static String T_TRAYECT="trayectoria";
    /**
     *
     * @param context
     */
    public MyDBAccess(Context context){
        dbHelper = new MyDBHelper(context);
    }

    public void addTrip(Trip myTrip){
        this.database = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(T_NAME, myTrip.getName());
        values.put(T_IMGURI, myTrip.getImgUri());
        values.put(T_FECHA, myTrip.getFecha());
        values.put(T_DURAC, myTrip.getDuracion());
        values.put(T_TRAYECT, myTrip.getTrayectoria());
        database.insert(TABLE_NAME, null, values);

        this.database.close();
    }

    public ArrayList<Trip> getTripByName(String name2find){
        this.database = this.dbHelper.getReadableDatabase();
        ArrayList<Trip> myTripList = new ArrayList<Trip>();
        Cursor cursor = this.database.query(TABLE_NAME, new String[]{"_id",T_NAME,T_IMGURI,T_FECHA,T_DURAC,T_TRAYECT},T_NAME+"=?", new String[]{name2find},null,null,null,null);
        if (cursor != null)
        {
            if (cursor.moveToFirst()){
                do{
                    Trip myTrip = new Trip();
                    myTrip.setId(cursor.getInt(cursor.getColumnIndex("_id")));
                    myTrip.setName(cursor.getString(cursor.getColumnIndex(T_NAME)));
                    myTrip.setImgUri(cursor.getString(cursor.getColumnIndex(T_IMGURI)));
                    myTrip.setFecha(cursor.getString(cursor.getColumnIndex(T_FECHA)));
                    myTrip.setDuracion(cursor.getInt(cursor.getColumnIndex(T_DURAC)));
                    myTrip.setTrayectoria(cursor.getString(cursor.getColumnIndex(T_TRAYECT)));
                    myTripList.add(myTrip);
                }while(cursor.moveToNext());
            }
        }
        this.database.close();
        return myTripList;
    }

    public ArrayList<Trip> getTripByDate(String fechaInf, String fechaSup){
        this.database = this.dbHelper.getReadableDatabase();
        ArrayList<Trip> myTripList = new ArrayList<Trip>();
        Cursor cursor = this.database.query(TABLE_NAME, new String[]{"_id",T_NAME,T_IMGURI,T_FECHA,T_DURAC,T_TRAYECT},T_FECHA + " BETWEEN ? AND ?", new String[]{fechaInf,fechaSup},null,null,null,null);
        if (cursor != null)
        {
            if (cursor.moveToFirst()){
                do{
                    Trip myTrip = new Trip();
                    myTrip.setId(cursor.getInt(cursor.getColumnIndex("_id")));
                    myTrip.setName(cursor.getString(cursor.getColumnIndex(T_NAME)));
                    myTrip.setImgUri(cursor.getString(cursor.getColumnIndex(T_IMGURI)));
                    myTrip.setFecha(cursor.getString(cursor.getColumnIndex(T_FECHA)));
                    myTrip.setDuracion(cursor.getInt(cursor.getColumnIndex(T_DURAC)));
                    myTrip.setTrayectoria(cursor.getString(cursor.getColumnIndex(T_TRAYECT)));
                    myTripList.add(myTrip);
                }while(cursor.moveToNext());
            }
        }
        this.database.close();
        return myTripList;
    }

    public ArrayList<Trip> getTripByDate(String fecha){
        this.database = this.dbHelper.getReadableDatabase();
        ArrayList<Trip> myTripList = new ArrayList<Trip>();
        Cursor cursor = this.database.query(TABLE_NAME, new String[]{"_id",T_NAME,T_IMGURI,T_FECHA,T_DURAC,T_TRAYECT},T_FECHA + "<=?", new String[]{fecha},null,null,null,null);
        if (cursor != null)
        {
            if (cursor.moveToFirst()){
                do{
                    Trip myTrip = new Trip();
                    myTrip.setId(cursor.getInt(cursor.getColumnIndex("_id")));
                    myTrip.setName(cursor.getString(cursor.getColumnIndex(T_NAME)));
                    myTrip.setImgUri(cursor.getString(cursor.getColumnIndex(T_IMGURI)));
                    myTrip.setFecha(cursor.getString(cursor.getColumnIndex(T_FECHA)));
                    myTrip.setDuracion(cursor.getInt(cursor.getColumnIndex(T_DURAC)));
                    myTrip.setTrayectoria(cursor.getString(cursor.getColumnIndex(T_TRAYECT)));
                    myTripList.add(myTrip);
                }while(cursor.moveToNext());
            }
        }
        this.database.close();
        return myTripList;
    }

    public ArrayList<Trip> getTripByLength(int limInf, int limSup){
        this.database = this.dbHelper.getReadableDatabase();
        ArrayList<Trip> myTripList = new ArrayList<Trip>();
        Cursor cursor = this.database.query(TABLE_NAME, new String[]{"_id",T_NAME,T_IMGURI,T_FECHA,T_DURAC,T_TRAYECT},T_DURAC + " BETWEEN ? AND ?", new String[]{String.valueOf(limInf),String.valueOf(limSup)},null,null,null,null);
        if (cursor != null)
        {
            if (cursor.moveToFirst()){
                do{
                    Trip myTrip = new Trip();
                    myTrip.setId(cursor.getInt(cursor.getColumnIndex("_id")));
                    myTrip.setName(cursor.getString(cursor.getColumnIndex(T_NAME)));
                    myTrip.setImgUri(cursor.getString(cursor.getColumnIndex(T_IMGURI)));
                    myTrip.setFecha(cursor.getString(cursor.getColumnIndex(T_FECHA)));
                    myTrip.setDuracion(cursor.getInt(cursor.getColumnIndex(T_DURAC)));
                    myTrip.setTrayectoria(cursor.getString(cursor.getColumnIndex(T_TRAYECT)));
                    myTripList.add(myTrip);
                }while(cursor.moveToNext());
            }
        }
        this.database.close();
        return myTripList;
    }

    public ArrayList<Trip> getTripByLength(int len){
        this.database = this.dbHelper.getReadableDatabase();
        ArrayList<Trip> myTripList = new ArrayList<Trip>();
        Cursor cursor = this.database.query(TABLE_NAME, new String[]{"_id",T_NAME,T_IMGURI,T_FECHA,T_DURAC,T_TRAYECT},T_DURAC + " <=?", new String[]{String.valueOf(len)},null,null,null,null);
        if (cursor != null)
        {
            if (cursor.moveToFirst()){
                do{
                    Trip myTrip = new Trip();
                    myTrip.setId(cursor.getInt(cursor.getColumnIndex("_id")));
                    myTrip.setName(cursor.getString(cursor.getColumnIndex(T_NAME)));
                    myTrip.setImgUri(cursor.getString(cursor.getColumnIndex(T_IMGURI)));
                    myTrip.setFecha(cursor.getString(cursor.getColumnIndex(T_FECHA)));
                    myTrip.setDuracion(cursor.getInt(cursor.getColumnIndex(T_DURAC)));
                    myTrip.setTrayectoria(cursor.getString(cursor.getColumnIndex(T_TRAYECT)));
                    myTripList.add(myTrip);
                }while(cursor.moveToNext());
            }
        }
        this.database.close();
        return myTripList;
    }

    public ArrayList<Trip> getAllTrips(){
        this.database = this.dbHelper.getReadableDatabase();
        ArrayList<Trip> myTripList = new ArrayList<Trip>();
        Cursor cursor = this.database.query(TABLE_NAME, null,null, null,null,null,null,null);
        if (cursor != null)
        {
            if (cursor.moveToFirst()){
                do{
                    Trip myTrip = new Trip();
                    myTrip.setId(cursor.getInt(cursor.getColumnIndex("_id")));
                    myTrip.setName(cursor.getString(cursor.getColumnIndex(T_NAME)));
                    myTrip.setImgUri(cursor.getString(cursor.getColumnIndex(T_IMGURI)));
                    myTrip.setFecha(cursor.getString(cursor.getColumnIndex(T_FECHA)));
                    myTrip.setDuracion(cursor.getInt(cursor.getColumnIndex(T_DURAC)));
                    myTrip.setTrayectoria(cursor.getString(cursor.getColumnIndex(T_TRAYECT)));
                    myTripList.add(myTrip);
                }while(cursor.moveToNext());
            }
        }
        this.database.close();

        return myTripList;
    }

    public int updateTrip(Trip myNewTrip){
        this.database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("_id", myNewTrip.getId());
        values.put(T_NAME, myNewTrip.getName());
        values.put(T_IMGURI, myNewTrip.getImgUri());
        values.put(T_FECHA, myNewTrip.getFecha());
        values.put(T_DURAC, myNewTrip.getDuracion());
        values.put(T_TRAYECT, myNewTrip.getTrayectoria());

        int affected_cols = this.database.update(TABLE_NAME, values, "_id =?", new String[]{String.valueOf(myNewTrip.getId())});
        this.database.close();
        return affected_cols;
    }

    public void deleteTrip(Trip myTrip) {
        this.database = dbHelper.getWritableDatabase();
        this.database.delete(TABLE_NAME,"_id =?",new String[]{String.valueOf(myTrip.getId())});
        this.database.close();
    }

}
