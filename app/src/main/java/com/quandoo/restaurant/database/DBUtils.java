package com.quandoo.restaurant.database;

class DBUtils {

    // DATABASE VERSION
    static final int DATABASE_VERSION = 1;
    // DATABASE NAME
    static final String DATABASE_NAME = "RESTAURANT_BOOKING_DB";
    //TABLE NAMES
    static final String TABLE_NAME_CUSTOMER = "TABLE_NAME_CUSTOMER";
    static final String TABLE_NAME_RESERVATION = "TABLE_NAME_RESERVATION";

    //COLUMN NAMES
    static final String COLUMN_ID = "COLUMN_ID";
    static final String COLUMN_FIRST_NAME = "COLUMN_FIRST_NAME";
    static final String COLUMN_LAST_NAME = "COLUMN_LAST_NAME";

    static final String COLUMN_TABLE_ID = "COLUMN_TABLE_ID";
    static final String COLUMN_RESERVED = "COLUMN_RESERVED";
    static final String COLUMN_RESERVE_CUSTOMER_NAME = "COLUMN_RESERVE_CUSTOMER_NAME";

    //CREATE TABLE
    static final String CREATE_TABLE_CUSTOMER =
            "CREATE TABLE " + TABLE_NAME_CUSTOMER + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY,"
                    + COLUMN_FIRST_NAME + " TEXT,"
                    + COLUMN_LAST_NAME + " TEXT"
                    + ")";
    static final String CREATE_TABLE_RESERVATION =
            "CREATE TABLE " + TABLE_NAME_RESERVATION + "("
                    + COLUMN_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_RESERVED + " INTEGER,"
                    + COLUMN_RESERVE_CUSTOMER_NAME + " TEXT"
                    + ")";

    // SELECT QUERIES
    static String SELECT_CUSTOMERS = "SELECT  * FROM " + TABLE_NAME_CUSTOMER;
    static String SELECT_TABLE_RESERVATION = "SELECT  * FROM " + TABLE_NAME_RESERVATION;

    static String DROP_CUSTOMERS = "DROP TABLE IF EXISTS " + TABLE_NAME_CUSTOMER;
    static String DROP_TABLE_RESERVATION = "DROP TABLE IF EXISTS " + TABLE_NAME_RESERVATION;
}
