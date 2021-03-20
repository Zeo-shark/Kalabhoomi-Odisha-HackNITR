package com.android.github.souravbera.e_commerce.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.github.souravbera.e_commerce.Interface.ItemClickListener;
import com.android.github.souravbera.e_commerce.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductName, txtProductPrice, txtProductQuatity;
    private ItemClickListener itemClickListener;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        txtProductName= itemView.findViewById(R.id.cart_product_name);
        txtProductPrice= itemView.findViewById(R.id.cart_product_price);
        txtProductQuatity= itemView.findViewById(R.id.cart_product_quantity);

    }




    @Override
    public void onClick(View view) {

        itemClickListener.onClick(view, getAdapterPosition(), false);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
