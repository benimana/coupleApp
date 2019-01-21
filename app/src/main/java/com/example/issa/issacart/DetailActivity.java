package com.example.issa.issacart;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.issa.issacart.count.Utils;
import com.example.issa.issacart.data.ItemContract;
import com.example.issa.issacart.data.ItemDbHelper;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import static com.example.issa.issacart.data.ItemContract.ItemEntry.CART_TABLE;

/**
 * Created by issa.
 */

public class DetailActivity extends AppCompatActivity {

    public static final String  ITEM_NAME = "itemName";
    public static final String  ITEM_DESCRIPTION = "itemDescription";
    public static final String  ITEM_RATING = "itemRating";
    public static final String  ITEM_IMAGE = "itemImage";
    public static final String  ITEM_PRICE = "itemPrice";

    private ImageView mImage;


    String itemName, description, itemImage;
    int rating;
    Double price;
    private int mQuantity = 1;
    private double mTotalPrice;
    String imagePath;
    TextView costTextView;
    ContentResolver mContentResolver;
    private SQLiteDatabase mDb;

    private int mNotificationsCount = 0;
    Button addToCartButton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mContentResolver = this.getContentResolver();
        ItemDbHelper dbHelper = new ItemDbHelper(this);
        mDb = dbHelper.getWritableDatabase();


        mImage = (ImageView) findViewById(R.id.itemImage);
        Intent intentThatStartedThisActivity = getIntent();
        addToCartButton = (Button) findViewById(R.id.cart_button);

        costTextView = (TextView) findViewById(
                R.id.cost_text_view);

        if (intentThatStartedThisActivity.hasExtra(ITEM_NAME)) {
            itemName = getIntent().getExtras().getString(ITEM_NAME);
            description = getIntent().getExtras().getString(ITEM_DESCRIPTION);
            rating = getIntent().getExtras().getInt(ITEM_RATING);
            itemImage = getIntent().getExtras().getString(ITEM_IMAGE);
            price = getIntent().getExtras().getDouble(ITEM_PRICE);

            TextView desc = (TextView) findViewById(R.id.description);
            desc.setText(description);

            TextView fragmentPrice = (TextView) findViewById(R.id.price);
            DecimalFormat precision = new DecimalFormat("0.00");
            fragmentPrice.setText("$" + precision.format(price));

            float f = Float.parseFloat(Double.toString(rating));

            setTitle(itemName);

            RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingLevel);
            ratingBar.setRating(f);

            imagePath = "http://muteero.com/items/" + itemImage;

            Glide.with(this)
                    .load(imagePath)
                    .placeholder(R.drawable.load)
                    .into(mImage);


        }


        if (mQuantity == 1){

            mTotalPrice = price;
            displayCost(mTotalPrice);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Get the notifications MenuItem and
        // its LayerDrawable (layer-list)
        MenuItem item = menu.findItem(R.id.action_notifications);
        LayerDrawable icon = (LayerDrawable) item.getIcon();

        // Update LayerDrawable's BadgeDrawable
        Utils.setBadgeCount(this, icon, mNotificationsCount);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_notifications:
                Intent intent = new Intent(this, CartActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        new FetchCountTask().execute();
    }

    public void increment(View view){

        price = getIntent().getExtras().getDouble(ITEM_PRICE);
        mQuantity = mQuantity + 1;
        displayQuantity(mQuantity);
        mTotalPrice = mQuantity * price;
        displayCost(mTotalPrice);
    }

    public void decrement(View view){
        if (mQuantity > 1){

            mQuantity = mQuantity - 1;
            displayQuantity(mQuantity);
            mTotalPrice = mQuantity * price;
            displayCost(mTotalPrice);

        }
    }

    private void displayQuantity(int numberOfItems) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText(String.valueOf(numberOfItems));
    }

    private void displayCost(double totalPrice) {

        String convertPrice = NumberFormat.getCurrencyInstance().format(totalPrice);
        costTextView.setText(convertPrice);
    }

    private void addValuesToCart() {

        ContentValues cartValues = new ContentValues();

        cartValues.put(ItemContract.ItemEntry.COLUMN_CART_NAME, itemName);
        cartValues.put(ItemContract.ItemEntry.COLUMN_CART_IMAGE, itemImage);
        cartValues.put(ItemContract.ItemEntry.COLUMN_CART_QUANTITY, mQuantity);
        cartValues.put(ItemContract.ItemEntry.COLUMN_CART_TOTAL_PRICE, mTotalPrice);



        mContentResolver.insert(ItemContract.ItemEntry.CONTENT_URI, cartValues);

        Toast.makeText(this, "Successfully added to Cart",
                Toast.LENGTH_SHORT).show();


    }

    public void addToCart(View view) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.add_to_cart);
        builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                addValuesToCart();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the items.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void updateNotificationsBadge(int count) {
        mNotificationsCount = count;

        // force the ActionBar to relayout its MenuItems.
        // onCreateOptionsMenu(Menu) will be called again.
        invalidateOptionsMenu();
    }

    /*
Sample AsyncTask to fetch the notifications count
*/
    class FetchCountTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            String countQuery = "SELECT  * FROM " + CART_TABLE;
            Cursor cursor = mDb.rawQuery(countQuery, null);
            int count = cursor.getCount();
            cursor.close();
            return count;

        }

        @Override
        public void onPostExecute(Integer count) {
            updateNotificationsBadge(count);
        }
    }


}
