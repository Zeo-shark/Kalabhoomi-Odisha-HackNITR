package com.android.github.souravbera.e_commerce.Admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.github.souravbera.e_commerce.Model.Cart;
import com.android.github.souravbera.e_commerce.R;
import com.android.github.souravbera.e_commerce.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdminUserProductsActivity extends AppCompatActivity {

    private RecyclerView productsList;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference cartListRef;

    String UserId= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_products);

          UserId= getIntent().getStringExtra("uid");
          cartListRef = FirebaseDatabase.getInstance().getReference().child("Products");

        productsList = findViewById(R.id.orders_list);
        productsList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        productsList.setLayoutManager(layoutManager);


        cartListRef= FirebaseDatabase.getInstance().getReference()
                .child("Cart List").child("Admin View").child(UserId).child("Products");
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<Cart> options=
                new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef, Cart.class)
                .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter= new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int i, @NonNull Cart model)
            {
                holder.txtProductQuatity.setText(String.format("Product Quantity: %s", model.getQuantity()));
                holder.txtProductPrice.setText(String.format("Product Price: Rs.%s", model.getPrice()));
                holder.txtProductName.setText(String.format("Product: %s", model.getProductname()));
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent, false);
                CartViewHolder holder= new CartViewHolder(view);
                return holder;
            }
        };
        productsList.setAdapter(adapter);
        adapter.startListening();
    }
}
