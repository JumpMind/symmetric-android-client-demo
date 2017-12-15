package org.jumpmind.symmetric.symmetricandroidclientdemo;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.Nullable;

import org.jumpmind.symmetric.android.SQLiteOpenHelperRegistry;
import org.jumpmind.symmetric.android.SymmetricService;
import org.jumpmind.symmetric.common.ParameterConstants;

import java.util.Properties;

/**
 * Created by Maxwell on 12/14/2017.
 */

public class DbProvider extends ContentProvider {

    //TODO: Update REGISTRATION_URL with Sync URL of corp-000
    private final String REGISTRATION_URL = "http://192.168.42.105:31415/sync/corp-000";
    private final String NODE_ID = "android-003";
    private final String NODE_GROUP = "store";

    final String SQL_CREATE_TABLE_ITEM = "CREATE TABLE IF NOT EXISTS ITEM(\n" +
            "    ITEM_ID INTEGER NOT NULL PRIMARY KEY ,\n" +
            "    NAME VARCHAR\n" +
            ");";

    final String SQL_CREATE_TABLE_ITEM_SELLING_PRICE = "CREATE TABLE IF NOT EXISTS ITEM_SELLING_PRICE(\n" +
            "    ITEM_ID INTEGER NOT NULL,\n" +
            "    STORE_ID VARCHAR NOT NULL,\n" +
            "    PRICE DECIMAL NOT NULL,\n" +
            "    COST DECIMAL,\n" +
            "    PRIMARY KEY (ITEM_ID, STORE_ID)\n" +
            ");";

    final String SQL_CREATE_TABLE_SALE_TRANSACTION = "CREATE TABLE IF NOT EXISTS SALE_TRANSACTION(\n" +
            "    TRAN_ID INTEGER NOT NULL PRIMARY KEY ,\n" +
            "    STORE_ID VARCHAR NOT NULL,\n" +
            "    WORKSTATION VARCHAR NOT NULL,\n" +
            "    DAY VARCHAR NOT NULL,\n" +
            "    SEQ INTEGER NOT NULL\n" +
            ");\n";

    final String SQL_CREATE_TABLE_SALE_RETURN_LINE_ITEM = "CREATE TABLE IF NOT EXISTS SALE_RETURN_LINE_ITEM(\n" +
            "    TRAN_ID INTEGER NOT NULL PRIMARY KEY ,\n" +
            "    ITEM_ID INTEGER NOT NULL,\n" +
            "    PRICE DECIMAL NOT NULL,\n" +
            "    QUANTITY INTEGER NOT NULL,\n" +
            "    RETURNED_QUANTITY INTEGER\n" +
            ");\n";

    public static final String DATABASE_NAME = "symmetric-demo.db";

    // Handle to a new DatabaseHelper.
    private DatabaseHelper mOpenHelper;

    /**
     * This class helps open, create, and upgrade the database file. Set to package visibility
     * for testing purposes.
     */
    static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {

            // calls the super constructor, requesting the default cursor factory.
            super(context, DATABASE_NAME, null, 2);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onCreate(db);
        }
    }

    /**
     * Initializes the provider by creating a new DatabaseHelper. onCreate() is called
     * automatically when Android creates the provider in response to a resolver request from a
     * client.
     */
    @Override
    public boolean onCreate() {

        // Creates a new helper object. Note that the database itself isn't opened until
        // something tries to access it, and it's only created if it doesn't already exist.
        mOpenHelper = new DatabaseHelper(getContext());

        // Init the DB here
        mOpenHelper.getWritableDatabase().execSQL(SQL_CREATE_TABLE_ITEM);
        mOpenHelper.getWritableDatabase().execSQL(SQL_CREATE_TABLE_ITEM_SELLING_PRICE);
        mOpenHelper.getWritableDatabase().execSQL(SQL_CREATE_TABLE_SALE_TRANSACTION);
        mOpenHelper.getWritableDatabase().execSQL(SQL_CREATE_TABLE_SALE_RETURN_LINE_ITEM);

        // Register the database helper, so it can be shared with the SymmetricService
        SQLiteOpenHelperRegistry.register(DATABASE_NAME, mOpenHelper);

        Intent intent = new Intent(getContext(), SymmetricService.class);

        intent.putExtra(SymmetricService.INTENTKEY_SQLITEOPENHELPER_REGISTRY_KEY, DATABASE_NAME);
        intent.putExtra(SymmetricService.INTENTKEY_REGISTRATION_URL, REGISTRATION_URL);
        intent.putExtra(SymmetricService.INTENTKEY_EXTERNAL_ID, NODE_ID);
        intent.putExtra(SymmetricService.INTENTKEY_NODE_GROUP_ID, NODE_GROUP);
        intent.putExtra(SymmetricService.INTENTKEY_START_IN_BACKGROUND, true);


        // TODO: Update properties with the desired Symmetric parameters (e.g. File Sync parameters)
        Properties properties = new Properties();
        //properties.put(ParameterConstants.FILE_SYNC_ENABLE, "true");
        //properties.put("start.file.sync.tracker.job", "true");
        //properties.put("start.file.sync.push.job", "true");
        //properties.put("start.file.sync.pull.job", "true");
        //properties.put("job.file.sync.pull.period.time.ms", "10000");

        intent.putExtra(SymmetricService.INTENTKEY_PROPERTIES, properties);

        getContext().startService(intent);

        // Assumes that any failures will be reported by a thrown exception.
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    public String getType(Uri uri) {

        throw new IllegalArgumentException("Unknown URI " + uri);

    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
