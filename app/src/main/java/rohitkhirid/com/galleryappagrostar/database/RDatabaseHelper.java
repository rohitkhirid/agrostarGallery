package rohitkhirid.com.galleryappagrostar.database;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import rohitkhirid.com.galleryappagrostar.services.UploadService;
import rohitkhirid.com.galleryappagrostar.utils.DebugLog;

/**
 * Created by rohitkhirid on 7/3/17.
 *
 * database abstraction for table to store filePaths and Url in database
 */
public class RDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "images";
    private static final int DATABASE_VERSION = 1;

    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_FILE_PATH = "file_path";
    private static final String COLUMN_REMOTE_URL = "remote_url";
    private static final String COLUMN_SUCCESS_BIT = "success_bit";
    private static final String COLUMN_TIMESTAMP = "timestamp";

    private static final int BIT_SUCCESS = 1;
    private static final int BIT_FAILURE = 0;

    private SQLiteDatabase mDatabase;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + DATABASE_NAME
            + "( " + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_FILE_PATH + " text not null, "
            + COLUMN_REMOTE_URL + " text, "
            + COLUMN_SUCCESS_BIT + " integer default " + BIT_FAILURE + ", "
            + COLUMN_TIMESTAMP + " datetime, "
            + "UNIQUE (" + COLUMN_FILE_PATH + "));";


    public RDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mDatabase = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        DebugLog.d("upgrading from " + oldVersion + " to new version : " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        onCreate(db);
    }

    public class DataBaseEntry {
        public int id;
        public String filePath;
        public String remoteUrl;
        public int successBit;
        public long currentTimestamp;

        public DataBaseEntry(Cursor cursor) {
            this.id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
            this.filePath = cursor.getString(cursor.getColumnIndex(COLUMN_FILE_PATH));
            this.remoteUrl = cursor.getString(cursor.getColumnIndex(COLUMN_REMOTE_URL));
            this.successBit = cursor.getInt(cursor.getColumnIndex(COLUMN_SUCCESS_BIT));
            this.currentTimestamp = cursor.getLong(cursor.getColumnIndex(COLUMN_TIMESTAMP));
        }
    }

    public void put(Activity activity, String filePath, String remoteUrl) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_FILE_PATH, filePath);
        contentValues.put(COLUMN_REMOTE_URL, remoteUrl);
        contentValues.put(COLUMN_SUCCESS_BIT, BIT_FAILURE);
        contentValues.put(COLUMN_TIMESTAMP, System.currentTimeMillis());
        mDatabase.insert(DATABASE_NAME, null, contentValues);
        UploadService.startMe(activity);
    }

    public void update(DataBaseEntry dataBaseEntry) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, dataBaseEntry.id);
        contentValues.put(COLUMN_FILE_PATH, dataBaseEntry.filePath);
        contentValues.put(COLUMN_REMOTE_URL, dataBaseEntry.remoteUrl);
        contentValues.put(COLUMN_SUCCESS_BIT, BIT_SUCCESS);
        contentValues.put(COLUMN_TIMESTAMP, System.currentTimeMillis());
        mDatabase.update(DATABASE_NAME, contentValues, "_id = " + dataBaseEntry.id, null);
    }

    public ArrayList<DataBaseEntry> getAll() {
        Cursor cursor = mDatabase.query(DATABASE_NAME, null, null, null, null, null, null);
        DebugLog.d("cursor has : " + cursor.getCount());
        ArrayList<DataBaseEntry> dataBaseEntries = new ArrayList<>(cursor.getCount());
        while (cursor.moveToNext()) {
            DataBaseEntry dataBaseEntry = new DataBaseEntry(cursor);
            dataBaseEntries.add(dataBaseEntry);
        }
        return dataBaseEntries;
    }

    public ArrayList<DataBaseEntry> getAllPendingFiles() {
        Cursor cursor = mDatabase.query(DATABASE_NAME, null, COLUMN_SUCCESS_BIT + " = " + BIT_FAILURE, null, null, null, null);
        DebugLog.d("cursor has : " + cursor.getCount());
        ArrayList<DataBaseEntry> dataBaseEntries = new ArrayList<>(cursor.getCount());
        while (cursor.moveToNext()) {
            DataBaseEntry dataBaseEntry = new DataBaseEntry(cursor);
            dataBaseEntries.add(dataBaseEntry);
        }
        return dataBaseEntries;
    }

    public ArrayList<DataBaseEntry> getAllUrls() {
        Cursor cursor = mDatabase.query(DATABASE_NAME, null, COLUMN_SUCCESS_BIT + " = " + BIT_SUCCESS, null, null, null, null);
        DebugLog.d("cursor has : " + cursor.getCount());
        ArrayList<DataBaseEntry> dataBaseEntries = new ArrayList<>(cursor.getCount());
        while (cursor.moveToNext()) {
            DataBaseEntry dataBaseEntry = new DataBaseEntry(cursor);
            dataBaseEntries.add(dataBaseEntry);
        }
        return dataBaseEntries;
    }

    public void printTable() {
        Cursor cursor = mDatabase.query(DATABASE_NAME, null, null, null, null, null, null);
        DebugLog.d("entries in table : " + cursor.getCount());
        while (cursor.moveToNext()) {
            DataBaseEntry dataBaseEntry = new DataBaseEntry(cursor);
            DebugLog.d(dataBaseEntry.filePath + " : " + dataBaseEntry.remoteUrl + " : " + dataBaseEntry.successBit);
        }
    }
}
