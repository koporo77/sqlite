package com.example.sqlite_prac;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {


    public static final String CUSTOMER_TABLE = "CUSTOMER_TABLE";
    public static final String COLUMN_CUSTOMER_NAME = "CUSTOMER_NAME";
    public static final String COLUMN_CUTOMER_AGE = "CUTOMER_AGE";
    public static final String COLUMN_ACTIVE_CUSTOMER = "ACTIVE_CUSTOMER";
    public static final String COLUMN_ID = "ID";

    //hard coding
    public DatabaseHelper(@Nullable Context context) {
        super(context, "customer.db", null, 1);
    }

    //ths is called the first time a database is accessed. There should be code in here to create a new datbase
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + CUSTOMER_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_CUSTOMER_NAME + " TEXT, " + COLUMN_CUTOMER_AGE + " INT, " + COLUMN_ACTIVE_CUSTOMER + " BOOL)";
        db.execSQL(createTableStatement);
    }

    //this is called if the database version number changes. it prevents previous users apps from breaking when you change the database design
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //select = read, / add,update,delete = writable
    public boolean addOne(CustomerModel customerModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_CUSTOMER_NAME, customerModel.getName());
        cv.put(COLUMN_CUTOMER_AGE, customerModel.getAge());
        cv.put(COLUMN_ACTIVE_CUSTOMER, customerModel.isActive());

        long insert = db.insert(CUSTOMER_TABLE, null, cv);
        if (insert == -1)
            return false;
        else
            return true;
    }

    public boolean deleteOne(CustomerModel customerModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        String stringQuery = "DELETE FROM " + CUSTOMER_TABLE + " WHERE " + COLUMN_ID + " = " + customerModel.getId();

        Cursor cursor = db.rawQuery(stringQuery, null);

        if (cursor.moveToFirst()) {
            return true;
        } else {
            return false;
        }
    }

    public List<CustomerModel> getEveryone() {

        List<CustomerModel> returnList = new ArrayList<>();

        String QueryString = "SELECT * FROM " + CUSTOMER_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(QueryString, null);
        //cursor is like result(set of data)

        if (cursor.moveToFirst()) {
            do {
                int customerId = cursor.getInt(0);
                String customerName = cursor.getString(1);
                int customerAge = cursor.getInt(2);
                boolean customerActive = cursor.getInt(3) == 1 ? true : false;

                CustomerModel newCustomer = new CustomerModel(customerId, customerName, customerAge, customerActive);
                returnList.add(newCustomer);

            } while (cursor.moveToNext());

        } else {
            //nothing is in list
        }
        //after using sql, we have to close it because when it first accessed it get locked
        cursor.close();
        db.close();

        return returnList;

    }
}
