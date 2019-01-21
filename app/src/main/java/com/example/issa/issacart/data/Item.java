package com.example.issa.issacart.data;

import android.database.Cursor;

/**
 * Created issa
 */
public class Item {

    public int id;

    public String name;
    public String description;
    public String imageUrl;
    public Double price;
    public int userRating;


    public Item(Cursor cursor) {
        this.name = cursor.getString(cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_NAME));
        this.description = cursor.getString(cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_DESCRIPTION));
        this.imageUrl = cursor.getString(cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_IMAGE));
        this.price = cursor.getDouble(cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_PRICE));
        this.userRating = cursor.getInt(cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_USERRATING));
    }

}
