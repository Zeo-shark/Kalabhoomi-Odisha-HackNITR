package com.android.github.souravbera.e_commerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.tv.TvContract;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText input_username, input_phoneNumber, input_password;
    private Button createAccountButton;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        createAccountButton = findViewById(R.id.register_button);
        input_username = findViewById(R.id.register_username_input);
        input_phoneNumber = findViewById(R.id.register_phone_number_input);
        input_password = findViewById(R.id.register_password_input);

        loadingBar = new ProgressDialog(this);
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });

    }

    private void CreateAccount() {
        String name = input_username.getText().toString();
        String phone = input_phoneNumber.getText().toString();
        String password = input_password.getText().toString();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please write your name .....", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Please write your phone Number.....", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please write your password.....", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidatePhoneNumber(name, phone, password);
        }
    }


        private void ValidatePhoneNumber (final String name, final String phone, final String password){
            final DatabaseReference RootRef;
            RootRef = FirebaseDatabase.getInstance().getReference();

            RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapShot) {
                    if (!(dataSnapShot.child("Users").child(phone).exists()))
                    {
                        HashMap<String, Object> userdataMap= new HashMap<>();
                        userdataMap.put("phone",phone);
                        userdataMap.put("password",password);
                        userdataMap.put("name", name);


                        RootRef.child("Users").child(phone).updateChildren(userdataMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task)
                                    {
                                        if(task.isSuccessful()){
                                            Toast.makeText(RegisterActivity.this, "Cogratulations, your account has been created", Toast.LENGTH_SHORT).show();
                                            loadingBar.dismiss();

                                            Intent LoginIntent= new Intent(RegisterActivity.this, LoginActivity.class);
                                            startActivity(LoginIntent);

                                        }
                                        else{
                                            loadingBar.dismiss();
                                            Toast.makeText(RegisterActivity.this, "Network Error, check your Internet Connections", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });

                    } else {
                        Toast.makeText(RegisterActivity.this, "This " + phone + " already exists.... ", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                        Toast.makeText( RegisterActivity.this, "Please try again using another phone number", Toast.LENGTH_LONG).show();

                        Intent MainIntent= new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(MainIntent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
