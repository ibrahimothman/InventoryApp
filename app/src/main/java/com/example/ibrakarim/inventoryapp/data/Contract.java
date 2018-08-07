package com.example.ibrakarim.inventoryapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class Contract {


    public static final String AUTHORITY = "com.ibra.moviesseries";
    public static final Uri BASE_URI = Uri.parse("content://"+AUTHORITY);
    public static final String PATH = "products";

    public static class FavEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_URI.buildUpon().appendPath(PATH).build();
        public static final String TABLE_NAME = "product_table";
            public static final String NAME_COL = "name";
        public static final String DESCRIPTION_COL = "desc";
        public static final String PRICE_COL = "price";
        public static final String QUANTITY_COL = "quantity";
        public static final String STATUS_COL = "status";
        public static final String MODEL_COL = "model";
        public static final String CATEGORY_COL = "category";

    }
}
