package com.example.ibrakarim.inventoryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "products.db";
    public static final int DATABASE_VERSION = 4;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_PRODUCT_TABLE_QUERY = "CREATE TABLE "+ Contract.ProductEntry.TABLE_NAME+
                "("+ Contract.ProductEntry._ID+" INTEGER PRIMARY KEY , "+
                Contract.ProductEntry.NAME_COL+" TEXT NOT NULL , "+
                Contract.ProductEntry.PRICE_COL+" TEXT NOT NULL , "+
                Contract.ProductEntry.DESCRIPTION_COL+" TEXT NOT NULL , "+
                Contract.ProductEntry.TIME+" DATETIME DEFAULT CURRENT_TIMESTAMP , "+
                Contract.ProductEntry.QUANTITY_COL+" TEXT NOT NULL ); ";

        db.execSQL(CREATE_PRODUCT_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        final String DROP_FAV_TABLE_QUERY = "DROP TABLE IF EXISTS "+ Contract.ProductEntry.TABLE_NAME;
        db.execSQL(DROP_FAV_TABLE_QUERY);
        onCreate(db);
    }
}
