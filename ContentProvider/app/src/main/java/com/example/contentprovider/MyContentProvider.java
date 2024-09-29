package com.example.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class MyContentProvider extends ContentProvider {
    public MyContentProvider() {
    }
    SQLiteDatabase myDb;

    public static final String DB_NAME = "company";
    public static final String DB_TABLE = "emp";
    public static final int DB_VER = 1;

    public static final String AUTHORITY = "com.example.contentprovider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/emp");

    static int EMP = 1;
    static int EMP_ID = 2;

    static UriMatcher myUri = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        myUri.addURI(AUTHORITY, "emp", EMP);
        myUri.addURI(AUTHORITY, "emp/#", EMP_ID);
    }

    private class MyOwnDatabase extends SQLiteOpenHelper {
        public MyOwnDatabase(Context ct) {
            super(ct, DB_NAME, null, DB_VER);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL("create table " + DB_TABLE + " (_id integer primary key autoincrement, emp_name text, profile text);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("drop table if exists " + DB_TABLE);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long row = myDb.insert(DB_TABLE, null, values);

        if(row > 0) {
            uri = ContentUris.withAppendedId(CONTENT_URI, row);
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return uri;
    }

    @Override
    public boolean onCreate() {
        MyOwnDatabase myHelper = new MyOwnDatabase(getContext());

        myDb = myHelper.getWritableDatabase();

        if(myDb != null) {
            return true;
        }else {
            return false;
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder myQuery = new SQLiteQueryBuilder();
        myQuery.setTables(DB_TABLE);

        Cursor cr = myQuery.query(myDb, null, null, null, null, null, "_id");

        cr.setNotificationUri(getContext().getContentResolver(), uri);

        return cr;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}