package com.smartreminder.app.module;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import java.sql.SQLException;

/**
 * Created by LiJunxing on 6/16/14.
 */
public class EventProvider extends ContentProvider {

    public EventProvider() {
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        dbOpenrHelper = new DBOpenHelper(context, DB_NAME, null, DB_VERSION);
        db = dbOpenrHelper.getWritableDatabase();

        if(db == null)
            return false;
        else
            return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(DB_TABLE);
        qb.appendWhere(KEY_ID + "=" + uri.getPathSegments().get(1));

        Cursor cursor = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {

        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = db.insert(DB_TABLE, null, values);
        if(id > 0) {
            Uri newUri = ContentUris.withAppendedId(CONTENT_URI, id);
            getContext().getContentResolver().notifyChange(newUri, null);
            return newUri;
        }

        try {
            throw new SQLException("Fail to insert row into " + uri);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;

        String segment = uri.getPathSegments().get(1);
        count = db.delete(DB_TABLE, KEY_ID + "=" + segment, selectionArgs);

        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;

        String segment = uri.getPathSegments().get(1);
        count = db.update(DB_TABLE, values, selection, selectionArgs);

        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }

    public static final String DIR_PREFIX = "vnd.android.cursor.dir";
    public static final String ITEM_PREFIX = "vnd.android.cursor.item";

    private static final String DB_NAME = "SmartReminder.db";
    private static final String DB_TABLE = "EventTable";
    private static final int DB_VERSION = 1;

    public static final String KEY_ID = "_id";
    public static final String KEY_TITLE = "_title";
    public static final String KEY_DESCRIPTION = "_description";
    public static final String KEY_ALERTTIME = "_alerttime";
    public static final String KEY_ALERTON = "_alerton";

    public static final String AUTHORITY = "com.smartreminder.EventProvider";
    public static final String PATH_SINGLE = "Event/#";
    public static final String PATH_MULTIPLE = "EVENT";
    public static final String CONTENT_URI_STRING = "content://" + AUTHORITY + "/" + PATH_MULTIPLE;
    public static final Uri CONTENT_URI = Uri.parse(CONTENT_URI_STRING);
//
//    private static final int MULTIPLE_EVENT = 1;
//    private static final int SINGLE_EVENT = 2;
//    private static final UriMatcher uriMatcher;
//
//    static {
//        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
//        uriMatcher.addURI(AUTHORITY, PATH_MULTIPLE, MULTIPLE_EVENT);
//        uriMatcher.addURI(AUTHORITY, PATH_SINGLE, SINGLE_EVENT);
//    }

    private SQLiteDatabase db;
    private DBOpenHelper dbOpenrHelper;

    private static class DBOpenHelper extends SQLiteOpenHelper {
        public DBOpenHelper (Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super (context, name, factory, version);
        }

        private static final String DB_CREATE = "create table " + DB_TABLE + "(" + KEY_ID + " long primary key, " + KEY_TITLE + " text not null, " + KEY_DESCRIPTION + " text, " + KEY_ALERTTIME + " long, " + KEY_ALERTON + " boolean not null);";

        @Override
        public void onCreate(SQLiteDatabase _db) {
            _db.execSQL(DB_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion) {
            _db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
            onCreate(_db);
        }
    }
}
