package com.android.github.souravbera.e_commerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.github.souravbera.e_commerce.Model.Users;
import com.android.github.souravbera.e_commerce.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button joinNowButton, loginButton;
    private ProgressDialog loadingBar;
    private String parentDbName= "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        joinNowButton= (Button) findViewById(R.id.main_join_now_button);
        loginButton= (Button) findViewById(R.id.main_login_button);


        loadingBar= new ProgressDialog(this);

        Paper.init(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent= new Intent(MainActivity.this, LoginActivity.class );
                startActivity(loginIntent);
            }
        });

        joinNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent RegisterIntent= new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(RegisterIntent);


            }
        });

        String UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey= Paper.book().read(Prevalent.UserPasswordKey);
        if (UserPhoneKey!= "" && UserPasswordKey!= "")
        {
            if(!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey))
            {
                loadingBar.setTitle("Already Logged In");
                loadingBar.setMessage("Please wait, you are already logged In .....");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                AllowAccess(UserPhoneKey, UserPasswordKey);


            }
        }



    }

    private void AllowAccess(final String phone, final String password)
    {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapShot) {
                if(dataSnapShot.child("Users").child(phone).exists())
                {
                    Users userData= dataSnapShot.child("Users").child(phone).getValue(Users.class);

                    if(userData.getPhone().equals(phone))
                    {
                        if(userData.getPassword().equals(password)){
                            Toast.makeText(MainActivity.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            Intent HomeIntent= new Intent(MainActivity.this, HomeActivity.class);
                            Prevalent.currentOnlineUser= userData;
                            startActivity(HomeIntent);

                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "Password is incorrect ", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }

                    }
                }else
                {
                    loadingBar.dismiss();
                    Toast.makeText(MainActivity.this, "you need to create a new account ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
