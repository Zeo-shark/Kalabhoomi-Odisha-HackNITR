package com.android.github.souravbera.e_commerce.ViewHolder;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.github.souravbera.e_commerce.Interface.ItemClickListener;
import com.android.github.souravbera.e_commerce.R;

import org.w3c.dom.Text;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{

    public TextView txtProductName, txtProductDescription, txtProductPrice;
    public ImageView imageView;

    public ItemClickListener listener;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView= itemView.findViewById(R.id.product_image);
        txtProductName= itemView.findViewById(R.id.product_name);
        txtProductDescription= itemView.findViewById(R.id.product_description);
        txtProductPrice= itemView.findViewById(R.id.product_price);




    }

    public void setItemClickListener(ItemClickListener listener){
        this.listener= listener;
    }
    @Override
    public void onClick(View view) {
        listener.onClick(view, getAdapterPosition(), false);
    }
}
