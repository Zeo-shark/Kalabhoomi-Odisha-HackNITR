package com.android.github.souravbera.e_commerce.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.github.souravbera.e_commerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainProductsActivity extends AppCompatActivity {


    private Button applyChangesBtn;
    private EditText name, price, description;
    private ImageView imageView;

    private String productId= "" ;
    private DatabaseReference productsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_products);

        productId= getIntent().getStringExtra("Pid");



        applyChangesBtn= findViewById(R.id.products_maintain_btn);
        name= findViewById(R.id.maintain_product_name);
        price= findViewById(R.id.maintain_product_price);
        description= findViewById(R.id.maintain_product_description);
        imageView= findViewById(R.id.maintain_product_image);

        productsRef= FirebaseDatabase.getInstance().getReference().child("Products").child(productId);

        displaySpecificProductInfo();

        applyChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyChanges();
            }
        });

    }

    private void applyChanges()
    {
        String pName= name.getText().toString();
        String pDescription= description.getText().toString();
        String pPrice= price.getText().toString();

        if(pName.equals(""))
        {
            Toast.makeText(this, "Write down Product Name.",Toast.LENGTH_SHORT).show();
        }
        else if(pPrice.equals(""))
        {
            Toast.makeText(this, "Write down Product Price.",Toast.LENGTH_SHORT).show();
        }
        else if(pDescription.equals(""))
        {
            Toast.makeText(this, "Write down Product Description.",Toast.LENGTH_SHORT).show();
        }
        else{
            HashMap<String, Object> ProductMap= new HashMap<>();
            ProductMap.put("pid",productId);

            ProductMap.put("description",pDescription);

            ProductMap.put("price",pPrice);
            ProductMap.put("productname",pName);

            productsRef.updateChildren(ProductMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                 if(task.isSuccessful())
                 {
                     Toast.makeText(AdminMaintainProductsActivity.this, "Changes Applied Successfully",Toast.LENGTH_SHORT).show();
                     Intent intent = new Intent(AdminMaintainProductsActivity.this, AdminCategoryActivity.class);
                     startActivity(intent);
                 }
                }
            });
        }
    }

    private void displaySpecificProductInfo()
    {
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String pName= dataSnapshot.child("productname").getValue().toString();
                    String pPrice= dataSnapshot.child("price").getValue().toString();
                    String pDescription= dataSnapshot.child("description").getValue().toString();
                    String pImage= dataSnapshot.child("image").getValue().toString();

                    name.setText(pName);
                    price.setText(pPrice);
                    description.setText(pDescription);
                    Picasso.get().load(pImage).into(imageView);




                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
