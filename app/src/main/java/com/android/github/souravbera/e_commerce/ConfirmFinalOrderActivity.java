package com.android.github.souravbera.e_commerce;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.github.souravbera.e_commerce.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rey.material.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity {

    private EditText nameEditText, phoneEditText, addressEditText, cityEditText;
    private Button confirmOrderbtn;
    private String totalAmount= "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        totalAmount= getIntent().getStringExtra("Total Price");
        Toast.makeText(this,"Total Price= Rs."+totalAmount,Toast.LENGTH_SHORT).show();

        confirmOrderbtn= findViewById(R.id.confirm_final_btn);
        nameEditText= findViewById(R.id.shippment_name);
        addressEditText= findViewById(R.id.shippment_address);
        cityEditText= findViewById(R.id.shippment_city);
        phoneEditText= findViewById(R.id.shippment_phone);

        confirmOrderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check();
            }
        });





    }

    private void Check()
    {
        if(TextUtils.isEmpty(nameEditText.getText().toString()))
        {
            Toast.makeText(this, "Please Provide your full Name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(addressEditText.getText().toString()))
        {
            Toast.makeText(this, "Please Provide your Address", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(cityEditText.getText().toString()))
        {
            Toast.makeText(this, "Please Provide your City Name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phoneEditText.getText().toString()))
        {
            Toast.makeText(this, "Please Confirm your Phone Number", Toast.LENGTH_SHORT).show();
        }

        else{
            ConfirmOrder();
        }
    }

    private void ConfirmOrder()
    {
        final String saveCurrentDate, saveCurrentTime;
        Calendar calForData= Calendar.getInstance();
        SimpleDateFormat currentDate= new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate= currentDate.format(calForData.getTime());

        SimpleDateFormat currentTime= new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime= currentTime.format(calForData.getTime());

        final DatabaseReference ordersRef= FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(Prevalent.currentOnlineUser.getPhone());

        HashMap<String, Object> orderMap= new HashMap<>();
        orderMap.put("totalAmount", totalAmount);
        orderMap.put("name",nameEditText.getText().toString());
        orderMap.put("phone",phoneEditText.getText().toString());
        orderMap.put("address",addressEditText.getText().toString());
        orderMap.put("city",cityEditText.getText().toString());
        orderMap.put("date",saveCurrentDate);
        orderMap.put("time",saveCurrentTime);
        orderMap.put("state","not shipped");



        ordersRef.updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    FirebaseDatabase.getInstance().getReference()
                    .child("Cart List")
                    .child("User View")
                    .child(Prevalent.currentOnlineUser.getPhone())
                    .removeValue()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(ConfirmFinalOrderActivity.this, "your final Order has been placed successfully",Toast.LENGTH_SHORT).show();
                                Intent intent= new Intent(ConfirmFinalOrderActivity.this, HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

                            }
                        }
                    });
                }
            }
        });
    }


}
