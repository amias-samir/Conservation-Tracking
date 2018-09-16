package com.naxa.conservationtrackingapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.naxa.conservationtrackingapp.application.ApplicationClass;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import Utls.SharedPreferenceUtils;

/**
 * Created by ramaan on 1/30/2016.
 */
public class DataBaseConserVationTracking extends ODKSQLiteOpenHelper {
    private final static String db_name = "_db_digitalNepal.db";
    private final static int db_version = (int) 2;

    public final static String ID_TABLE = "_id_table";
    public final static String TABLE_ID = "_table_id";
    public final static String TABLE_NAME = "_table_Name";
    public final static String TABLE_DATE = "_table_date";
    public final static String TABLE_JSON = "_table_json";
    public final static String TABLE_STATUS = "_table_status";
    public final static String TABLE_GPS = "_table_Gps";
    public final static String TABLE_PHOTO = "_table_photo";
    public final static String DELETE_FLAG = "_delete_flag";
    public final static String IMEI = "imei";
    public final static String TABLE_MAIN = "_table_main";

    //    public final static String[] COLS_TABLE_MAIN = new String[]{ID_TABLE, TABLE_ID, TABLE_NAME, TABLE_DATE, TABLE_JSON, TABLE_GPS, TABLE_PHOTO, TABLE_STATUS, DELETE_FLAG};
    public final static String[] COLS_TABLE_MAIN = new String[]{ID_TABLE, TABLE_ID, TABLE_NAME, TABLE_DATE, TABLE_JSON, TABLE_GPS, TABLE_PHOTO, TABLE_STATUS, DELETE_FLAG, IMEI};
//    static String CREATE_TABLE_MAIN = "Create table if not exists " + TABLE_MAIN + "("
//            + ID_TABLE + " INTEGER PRIMARY KEY AUTOINCREMENT " +
//            "," + TABLE_ID + " Text not null " +
//            "," + TABLE_NAME + " Text not null " +
//            "," + TABLE_DATE + " Text not null " +
//            "," + TABLE_JSON + " Text not null " +
//            "," + TABLE_GPS + " Text not null " +
//            "," + TABLE_PHOTO + " Text not null " +
//            "," + TABLE_STATUS + " Text not null " +
//            "," + DELETE_FLAG + " Text not null )";


    static String CREATE_TABLE_MAIN = "Create table if not exists " + TABLE_MAIN + "("
            + ID_TABLE + " INTEGER PRIMARY KEY AUTOINCREMENT " +
            "," + TABLE_ID + " Text not null " +
            "," + TABLE_NAME + " Text not null " +
            "," + TABLE_DATE + " Text not null " +
            "," + TABLE_JSON + " Text not null " +
            "," + TABLE_GPS + " Text not null " +
            "," + TABLE_PHOTO + " Text not null " +
            "," + TABLE_STATUS + " Text not null " +
            "," + DELETE_FLAG + " Text not null " +
            "," + IMEI + " Text not null )";

    static final String DROP_TABLE_MAIN = "DROP TABLE IF EXISTS " + TABLE_MAIN + ";";

    SQLiteDatabase db = null;
    long id;
    Context con;

    public DataBaseConserVationTracking(Context context) {
        super(ApplicationClass.extSdcard + ApplicationClass.mainFolder + ApplicationClass.dataFolder, db_name, null, db_version);
        this.con = context;
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(CREATE_TABLE_MAIN);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

        switch (newVersion) {
            case 2:
                String alterSql = " ALTER TABLE " + TABLE_MAIN + " ADD " + IMEI + " TEXT ";
                db.execSQL(alterSql);

                //update imei in old data
                ContentValues contentValues = new ContentValues();
                contentValues.put(IMEI, SharedPreferenceUtils.getInstance(con).getStringValue(IMEI, ""));
                db.update(TABLE_MAIN, contentValues, null, null);


                break;

            default:
                db.execSQL(DROP_TABLE_MAIN);
                onCreate(db);
                break;
        }

    }

    public void open() throws SQLException {
        db = this.getWritableDatabase();
    }

    public void close() {
        db.close();
    }


    public long insertIntoTable_Main(String[] list) {


        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd HH:MM:SS", Locale.US);
        String formatedDate = format.format(date);

        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_ID, list[0]);
        contentValues.put(TABLE_NAME, list[1]);
        contentValues.put(TABLE_DATE, formatedDate);
        contentValues.put(TABLE_JSON, list[3]);
        contentValues.put(TABLE_GPS, list[4]);
        contentValues.put(TABLE_PHOTO, list[5]);
        contentValues.put(TABLE_STATUS, list[6]);
        contentValues.put(DELETE_FLAG, list[7]);
        contentValues.put(IMEI, SharedPreferenceUtils.getInstance(con).getStringValue(IMEI, ""));

        long id = db.insert(TABLE_MAIN, null, contentValues);
        return id;
    }



    private String formatImageName(String s) {
        String imei = SharedPreferenceUtils.getInstance(con).getStringValue(IMEI, "");
        return s + "_" + imei + ".jpeg";
    }

    public boolean is_TABLE_MAIN_Empty() {
        boolean b = true;
        Cursor cursor = db.query(TABLE_MAIN, COLS_TABLE_MAIN, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            b = false;
        } else {
            b = true;
        }
        return b;
    }

    public String[] return_Data_TABLE_MAIN(int id) {
        Cursor cursor = db.query(TABLE_MAIN, COLS_TABLE_MAIN, "_table_id=? and _delete_flag=?", new String[]{String.valueOf(id), "0"}, null, null, null);
        String[] name = new String[8];
        try {
            while (cursor.moveToNext()) {
                name[0] = cursor.getString(0);
                name[1] = cursor.getString(1);
                name[2] = cursor.getString(2);
                name[3] = cursor.getString(3);
                name[4] = cursor.getString(4);
                name[5] = cursor.getString(5);
                name[6] = cursor.getString(6);
                name[7] = cursor.getString(7);
            }
        } finally {
            cursor.close();
        }
        return name;
    }

    public int returnTotalNoOf_TABLE_MAIN_NUM() {
        Cursor cursor = db.query(TABLE_MAIN, COLS_TABLE_MAIN, null, null, null,
                null, null);
        int count = cursor.getCount();
        return count;
    }

    public String[] return_Data_ID(int id) {
        Cursor cursor = db.query(TABLE_MAIN, COLS_TABLE_MAIN, ID_TABLE + "='" + id + " ' ", null, null, null, null);
        String[] name = new String[9];
        try {
            while (cursor.moveToNext()) {
                name[0] = cursor.getString(1);
                name[1] = cursor.getString(2);
                name[2] = cursor.getString(3);
                name[3] = cursor.getString(4);
                name[4] = cursor.getString(5);
                name[5] = cursor.getString(6);
                name[6] = cursor.getString(7);
                name[7] = cursor.getString(8);
                name[8] = cursor.getString(0);

//                name[0] = cursor.getString(0);
//                name[1] = cursor.getString(1);
//                name[2] = cursor.getString(2);
//                name[3] = cursor.getString(3);
//                name[4] = cursor.getString(4);
//                name[5] = cursor.getString(5);
//                name[6] = cursor.getString(6);
//                name[7] = cursor.getString(7);

            }
        } finally {
            cursor.close();
        }
        return name;
    }

    public long updateTable_DeleteFlag(String id) {
        ContentValues values = new ContentValues();
        values.put(DELETE_FLAG, "1");
        int rowsUpdated = db.update(TABLE_MAIN, values, ID_TABLE + "='" + id + " ' ", null);
        return rowsUpdated;
    }



//        public void alterTableMAIN() {
//            db.execSQL(DROP_TABLE_MAIN);
//            db.execSQL(CREATE_TABLE_MAIN);
//        }


//        public long insertIntoTABLE(String tableName ,String[] COLS , String[] list) {
//
//            ContentValues contentValues = new ContentValues();
//            int length = COLS.length;
//            for(int i= 0 ; i<length ;i++ ) {
//                contentValues.put(COLS[i], list[i]);
//            }
//            long id = db.insert(tableName, null, contentValues);
//            return id;
//        }
//        public boolean is_TABLE_Empty(String tableName , String[] COLS){
//            boolean b=true;
//            Cursor cursor = db.query(tableName, COLS , null, null, null, null, null);
//            if( cursor != null && cursor.moveToFirst() ){
//                b=false;
//            }else{
//                b=true;
//            }
//            return b;
//        }
//        public String[] return_Data_TABLE(String tableName ,String[] COLS , String colsId , int id , int nosOfColumns ) {
//            Cursor cursor = db.query( tableName , COLS , colsId + "='" + id + " ' ", null , null, null, null);
//            String[] name = new String[nosOfColumns];
//            try {
//                while (cursor.moveToNext()) {
//                    for(int i = 0 ; i <nosOfColumns ; i++){
//                        name[i] = cursor.getString(i+1);
//                    }
//                }
//            } finally {
//                cursor.close();
//            }
//            return name;
//        }
//        public int returnTotalNoOf_CF_DATA_NUM(String tableName,String[] COLS) {
//            Cursor cursor = db.query( tableName , COLS , null, null, null,
//                    null, null);
//            int count = cursor.getCount();
//            return count;
//        }
//        public void alterTable(String dropTable , String createTable) {
//            db.execSQL(dropTable);
//            db.execSQL(createTable);
//        }


}
