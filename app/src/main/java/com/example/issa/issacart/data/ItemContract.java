package com.example.issa.issacart.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by issa
 */

public class ItemContract {

    private ItemContract() {}

    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "com.example.issa.issacart";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final String PATH_ITEM = "item-path";

    public static final String PATH_CART= "cart-path";


    /**
     * Inner class that defines constant values for the items database table.
     * Each entry in the table represents a single items.
     */
    public static final class ItemEntry implements BaseColumns {

        /** The content URI to access the items data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ITEM);

        public static final Uri CONTENT_URI_CART = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_CART);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of items.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEM;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single single.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEM;

        /** Name of database table for item */
        public final static String TABLE_NAME = "items";

        //cart table name
        public final static String CART_TABLE = "cart";

        /**
         * Unique ID number for the item (only for use in the database table).
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        public final static String _CARTID = BaseColumns._ID;

        /**
         * Name of the item.
         *
         * Type: TEXT
         */
        public final static String COLUMN_NAME = "itemName";
        public final static String COLUMN_DESCRIPTION = "description";
        public final static String COLUMN_IMAGE = "imageUrl";
        public final static String COLUMN_PRICE = "price";
        public final static String COLUMN_USERRATING = "userRating";


        public final static String COLUMN_CART_NAME = "cartitemname";
        public final static String COLUMN_CART_IMAGE = "cartImageUrl";
        public final static String COLUMN_CART_QUANTITY = "cartQuantity";
        public final static String COLUMN_CART_TOTAL_PRICE = "cartotalprice";

    }

}

