package com.example.issa.issacart;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.issa.issacart.data.ItemContract;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by issa
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    // Class variables for the Cursor that holds task data and the Context
    private Cursor mCursor;
    private Context mContext;


    /**
     * Constructor for the CustomCursorAdapter that initializes the Context.
     *
     * @param mContext the current Context
     */
    public CartAdapter(Context mContext) {
        this.mContext = mContext;
    }



    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {

        // Indices for the _id, description, and priority columns
        int idIndex = mCursor.getColumnIndex(ItemContract.ItemEntry._CARTID);
        int itemName = mCursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_CART_NAME);
        int image = mCursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_CART_IMAGE);
        int quantity = mCursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_CART_QUANTITY);
        int price = mCursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_CART_TOTAL_PRICE);


        mCursor.moveToPosition(position); // get to the right location in the cursor

        // Determine the values of the wanted data
        final int id = mCursor.getInt(idIndex);
        String name = mCursor.getString(itemName);
        String itemImage = mCursor.getString(image);
        int itemQuantity = mCursor.getInt(quantity);
        Double itemPrice = mCursor.getDouble(price);

        DecimalFormat precision = new DecimalFormat("0.00");



        //Set values
        holder.itemView.setTag(id);
        holder.itemName.setText(name);
        holder.fragQuantity.setText("Quantity ordering: " + String.valueOf(itemQuantity));
        holder.fragPrice.setText("$" + precision.format(itemPrice));

        String poster = "http://muteero.com/items/" + itemImage;

       Glide.with(mContext)
                .load(poster)
                .placeholder(R.drawable.load)
                .into(holder.image);

    }


    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    public Cursor swapCursor(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (mCursor == c) {
            return null; // bc nothing has changed
        }
        Cursor temp = mCursor;
        this.mCursor = c; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, fragQuantity, fragPrice;
        ImageView image;
        public CartViewHolder(View itemView) {
            super(itemView);

            itemName = (TextView) itemView.findViewById(R.id.itemName);
            fragQuantity = (TextView) itemView.findViewById(R.id.quantity);
            fragPrice = (TextView) itemView.findViewById(R.id.price);
            image = (ImageView) itemView.findViewById(R.id.cartImage);
        }

    }
}
