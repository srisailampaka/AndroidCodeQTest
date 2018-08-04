package com.quandoo.restaurant.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.quandoo.restaurant.model.Customer;
import com.quandoo.restaurant.model.ReservationTable;

import java.util.ArrayList;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static DataBaseHelper mDatabaseHelper;

    public static DataBaseHelper getInstance(Context context) {
        if (mDatabaseHelper == null) {
            mDatabaseHelper = new DataBaseHelper(context, DBUtils.DATABASE_NAME, null, DBUtils.DATABASE_VERSION);
        }
        return mDatabaseHelper;
    }

    private DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create tableS
        db.execSQL(DBUtils.CREATE_TABLE_CUSTOMER);
        db.execSQL(DBUtils.CREATE_TABLE_RESERVATION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DBUtils.DROP_CUSTOMERS);
        db.execSQL(DBUtils.DROP_TABLE_RESERVATION);
        onCreate(db);
    }

    public void insertCustomer(Customer customer) {
        if (customer != null) {
            // get writable database as we want to write data
            SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DBUtils.COLUMN_ID, customer.getId());
            values.put(DBUtils.COLUMN_FIRST_NAME, customer.getCustomerFirstName());
            values.put(DBUtils.COLUMN_LAST_NAME, customer.getCustomerLastName());
            // insert row
            db.insert(DBUtils.TABLE_NAME_CUSTOMER, null, values);
            // close db connection
            closeDB(db);
        }
    }

    public void insertTableReservation(boolean isReserved) {
        int reserved = 0;
        if (isReserved) {
            reserved = 1;
        }
        // get writable database as we want to write data
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBUtils.COLUMN_RESERVED, reserved);
        values.put(DBUtils.COLUMN_RESERVE_CUSTOMER_NAME, "");
        // insert row
        db.insert(DBUtils.TABLE_NAME_RESERVATION, null, values);
        // close db connection
        closeDB(db);
    }

    public ArrayList<Customer> getAllCustomers() {
        ArrayList<Customer> customerList = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(DBUtils.SELECT_CUSTOMERS, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Customer customer = new Customer();
                customer.setId(cursor.getInt(cursor.getColumnIndex(DBUtils.COLUMN_ID)));
                customer.setCustomerFirstName(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_FIRST_NAME)));
                customer.setCustomerLastName(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_LAST_NAME)));
                customerList.add(customer);
            } while (cursor.moveToNext());

            closeCursor(cursor);
        }

        // close db connection
        closeDB(db);
        // return notes list
        return customerList;
    }

    public ArrayList<ReservationTable> getTableReservations() {
        ArrayList<ReservationTable> reservationTableList = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(DBUtils.SELECT_TABLE_RESERVATION, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ReservationTable reservationTable = new ReservationTable();
                reservationTable.setTableId(cursor.getInt(cursor.getColumnIndex(DBUtils.COLUMN_TABLE_ID)));
                int reserved = cursor.getInt(cursor.getColumnIndex(DBUtils.COLUMN_RESERVED));
                if (reserved == 0) {
                    reservationTable.setReserved(false);
                } else {
                    reservationTable.setReserved(true);
                }
                reservationTable.setTableReserverName(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_RESERVE_CUSTOMER_NAME)));
                reservationTableList.add(reservationTable);
            } while (cursor.moveToNext());

            closeCursor(cursor);
        }

        // close db connection
        closeDB(db);
        // return notes list
        return reservationTableList;
    }


    public int updateReservation(ReservationTable reservationTable) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBUtils.COLUMN_RESERVED, reservationTable.isReserved());
        values.put(DBUtils.COLUMN_RESERVE_CUSTOMER_NAME, reservationTable.getTableReserverName());
        // updating row
        return db.update(DBUtils.TABLE_NAME_RESERVATION, values, DBUtils.COLUMN_TABLE_ID + " = ?",
                new String[]{String.valueOf(reservationTable.getTableId())});
    }

    public int getCustomerCount() {
        return getTableCount(DBUtils.SELECT_CUSTOMERS);
    }

    public int getReservationCount() {
        return getTableCount(DBUtils.SELECT_TABLE_RESERVATION);
    }

    private int getTableCount(String selectQuery) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = cursor.getCount();
        closeCursor(cursor);
        closeDB(db);
        return count;
    }


    private void closeCursor(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }

    private void closeDB(SQLiteDatabase sqLiteDatabase) {
        if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) {
            sqLiteDatabase.close();
        }
    }
}
