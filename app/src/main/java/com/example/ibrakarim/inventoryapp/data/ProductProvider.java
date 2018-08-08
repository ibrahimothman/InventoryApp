package com.example.ibrakarim.inventoryapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class ProductProvider extends ContentProvider {


    private static final int ALL_TASKS = 100;
    private static final int TASK_WITH_ID = 101;

    public static final UriMatcher sMatcher = getUriMatcher();
    DbHelper dbHelper;

    public static UriMatcher getUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(Contract.AUTHORITY, Contract.PATH, ALL_TASKS);
        matcher.addURI(Contract.AUTHORITY, Contract.PATH + "/#", TASK_WITH_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int match = getUriMatcher().match(uri);
        Cursor cursor;
        switch (match) {
            case ALL_TASKS:
                cursor = db.query(Contract.ProductEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;

            case TASK_WITH_ID:
                String id = uri.getPathSegments().get(1);
                String mSelection = "_id=?";
                String[] mArgs = new String[]{id};
                cursor = db.query(Contract.ProductEntry.TABLE_NAME, projection, mSelection, mArgs, null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("invalid_uri");
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = getUriMatcher().match(uri);
        Uri mUri;
        switch (match) {
            case ALL_TASKS:
                long id = db.insert(Contract.ProductEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    mUri = ContentUris.withAppendedId(Contract.ProductEntry.CONTENT_URI, id);
                    Toast.makeText(getContext(), "successfully inserted", Toast.LENGTH_LONG).show();
                } else {
                    throw new SQLException();
                }
                break;
            default:
                throw new UnsupportedOperationException("invalid uri");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return mUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = getUriMatcher().match(uri);
        int mId;
        switch (match) {
            case TASK_WITH_ID:
                String id = uri.getPathSegments().get(1);
                mId = db.delete(Contract.ProductEntry.TABLE_NAME, "_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("invalid uri");
        }

        // if deleted record exist
        if (mId != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
            return mId;
        }
        // if record not exist
        else return -1;

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = getUriMatcher().match(uri);
        int mId;
        switch (match) {
            case TASK_WITH_ID:
                String id = uri.getPathSegments().get(1);
                mId = db.update(Contract.ProductEntry.TABLE_NAME,values, "_id=?", new String[]{id});
                Toast.makeText(getContext(), "successfully updated", Toast.LENGTH_LONG).show();

                break;
            default:
                throw new UnsupportedOperationException("invalid uri");
        }

        getContext().getContentResolver().notifyChange(uri,null);
        return mId;
    }

}
