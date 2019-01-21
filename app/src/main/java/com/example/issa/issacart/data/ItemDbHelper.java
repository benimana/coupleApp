package com.example.issa.issacart.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.issa.issacart.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.example.issa.issacart.data.ItemContract.ItemEntry.CART_TABLE;

/**
 * Created by issa
 */

public class ItemDbHelper extends SQLiteOpenHelper {
    private static final String TAG = ItemDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "items.db";
    private static final int DATABASE_VERSION = 1;
    Context context;
    SQLiteDatabase db;
    ContentResolver mContentResolver;



    //Used to read data from res/ and assets/
    private Resources mResources;



    public ItemDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        mResources = context.getResources();
        mContentResolver = context.getContentResolver();

        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_ITEM_TABLE = "CREATE TABLE " + ItemContract.ItemEntry.TABLE_NAME + " (" +
                ItemContract.ItemEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ItemContract.ItemEntry.COLUMN_NAME + " TEXT UNIQUE NOT NULL, " +
                ItemContract.ItemEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                ItemContract.ItemEntry.COLUMN_IMAGE + " TEXT NOT NULL, " +
                ItemContract.ItemEntry.COLUMN_PRICE + " REAL NOT NULL, " +
                ItemContract.ItemEntry.COLUMN_USERRATING + " INTEGER NOT NULL " + " );";

        final String SQL_CREATE_CART_ITEM_TABLE = "CREATE TABLE " + ItemContract.ItemEntry.CART_TABLE + " (" +
                ItemContract.ItemEntry._CARTID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ItemContract.ItemEntry.COLUMN_CART_NAME + " TEXT UNIQUE NOT NULL, " +
                ItemContract.ItemEntry.COLUMN_CART_IMAGE + " TEXT NOT NULL, " +
                ItemContract.ItemEntry.COLUMN_CART_QUANTITY + " INTEGER NOT NULL, " +
                ItemContract.ItemEntry.COLUMN_CART_TOTAL_PRICE + " REAL NOT NULL " + " );";


        db.execSQL(SQL_CREATE_ITEM_TABLE);
        db.execSQL(SQL_CREATE_CART_ITEM_TABLE);
        Log.d(TAG, "Database Created Successfully" );


        try {
            readItemsFromResources(db);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO: Handle database version upgrades
        db.execSQL("DROP TABLE IF EXISTS " + ItemContract.ItemEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ItemContract.ItemEntry.CART_TABLE);
        onCreate(db);
    }


    private void readItemsFromResources(SQLiteDatabase db) throws IOException, JSONException {
        //db = this.getWritableDatabase();
        StringBuilder builder = new StringBuilder();
        InputStream in = mResources.openRawResource(R.raw.items);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line + "\n");
        }

        //Parse resource into key/values
        final String rawJson = builder.toString();

        final String BGS_ITEMS = "items";

        final String BGS_NAME = "itemName";
        final String BGS_DESCRIPTION = "description";
        final String BGS_IMAGEURL = "imageUrl";
        final String BGS_PRICE = "price";
        final String BGS_USERRATING = "userRating";

        try {
            JSONObject itemsJson = new JSONObject(rawJson);
            JSONArray itemArray = itemsJson.getJSONArray(BGS_ITEMS);


            for (int i = 0; i < itemArray.length(); i++) {

                String itemName;
                String description;
                String imageUrl;
                Double price;
                int userRating;

                JSONObject itemDetails = itemArray.getJSONObject(i);

                itemName = itemDetails.getString(BGS_NAME);
                description = itemDetails.getString(BGS_DESCRIPTION);
                imageUrl = itemDetails.getString(BGS_IMAGEURL);
                price = itemDetails.getDouble(BGS_PRICE);
                userRating = itemDetails.getInt(BGS_USERRATING);


                ContentValues itemValues = new ContentValues();

                itemValues.put(ItemContract.ItemEntry.COLUMN_NAME, itemName);
                itemValues.put(ItemContract.ItemEntry.COLUMN_DESCRIPTION, description);
                itemValues.put(ItemContract.ItemEntry.COLUMN_IMAGE, imageUrl);
                itemValues.put(ItemContract.ItemEntry.COLUMN_PRICE, price);
                itemValues.put(ItemContract.ItemEntry.COLUMN_USERRATING, userRating);

                 db.insert(ItemContract.ItemEntry.TABLE_NAME, null, itemValues);

                Log.d(TAG, "Inserted Successfully " + itemValues );
            }

            Log.d(TAG, "Inserted Successfully " + itemArray.length() );

        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }


}
