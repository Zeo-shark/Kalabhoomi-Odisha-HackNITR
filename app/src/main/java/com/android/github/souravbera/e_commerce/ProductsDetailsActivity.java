package com.android.github.souravbera.e_commerce;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.github.souravbera.e_commerce.Model.Products;
import com.android.github.souravbera.e_commerce.Prevalent.Prevalent;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductsDetailsActivity extends AppCompatActivity {

//    private FloatingActionButton addToCart;
    private Button AddToCartbtn;
    private PhotoView productImage;
    private ElegantNumberButton numberButton;
    private TextView productPrice, productDescription, productName;
    
    private String productId= "" , state="normal";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_details);
        
        productId = getIntent().getStringExtra("pid");
        
        
        
//        addToCart= findViewById(R.id.add_product_to_cart_btn);
        numberButton= findViewById(R.id.number_btn);
        productImage= findViewById(R.id.product_image_details);
        productDescription= findViewById(R.id.product_description_details);
        productPrice= findViewById(R.id.product_price_details);
        productName= findViewById(R.id.product_name_details);
        AddToCartbtn= findViewById(R.id.prod_add_to_cart);
        
        getProductDetails(productId);

        AddToCartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(state.equals("Order Placed")||  state.equals("Order Shipped"))
                {
                    Toast.makeText(ProductsDetailsActivity.this, "You can add purchase more products , once your order is shipped or confirmed",Toast.LENGTH_SHORT ).show();
                }
                else{
                    addingToCartList();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        CheckOrderState();
    }

    private void addingToCartList() {
        String saveCurrentTime, saveCurrentDate;

        Calendar calForData= Calendar.getInstance();
        SimpleDateFormat currentDate= new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate= currentDate.format(calForData.getTime());

        SimpleDateFormat currentTime= new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime= currentTime.format(calForData.getTime());

        final DatabaseReference cartListRef=  FirebaseDatabase.getInstance().getReference().child("Cart List");

        final HashMap<String , Object> cartMap= new HashMap<>();
        cartMap.put("pid",productId);
        cartMap.put("productname",productName.getText().toString());
        cartMap.put("price",productPrice.getText().toString());
        cartMap.put("date",saveCurrentDate);
        cartMap.put("time",saveCurrentTime);
        cartMap.put("quantity",numberButton.getNumber());
        cartMap.put("discount","");

        cartListRef.child("User View").child(Prevalent.currentOnlineUser.getPhone())
                .child("Products").child(productId)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful()){
                            cartListRef.child("Admin View").child(Prevalent.currentOnlineUser.getPhone())
                                    .child("Products").child(productId)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(ProductsDetailsActivity.this, "Added to Cart List",Toast.LENGTH_SHORT).show();
                                                Intent intent= new Intent(ProductsDetailsActivity.this, HomeActivity.class);
                                                startActivity(intent);
                                            }
                                        }
                                    });
                        }
                    }
                });

    }

    private void getProductDetails(String productId) {
        DatabaseReference productsRef= FirebaseDatabase.getInstance().getReference().child("Products");

        productsRef.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Products products= dataSnapshot.getValue(Products.class);


//                    productName.setText(products.getProductname());
                    productDescription.setText(products.getDescription());
                    productPrice.setText(products.getPrice());
                    productName.setText(products.getProductname());
                    Picasso.get().load(products.getImage()).placeholder(R.drawable.profile).into(productImage);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void CheckOrderState()
    {
        DatabaseReference ordersRef;
        ordersRef= FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone());

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String shippingState = dataSnapshot.child("state").getValue().toString();


                    if(shippingState.equals("shipped"))
                    {
                        state= "Order Shipped";
                    }
                    else if(shippingState.equals("not shipped"))
                    {
                        state= "Order Placed";
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
