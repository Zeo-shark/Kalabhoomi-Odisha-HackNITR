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

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.github.souravbera.e_commerce.Admin.AdminCategoryActivity;
import com.android.github.souravbera.e_commerce.Model.Users;
import com.android.github.souravbera.e_commerce.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private Button LoginButton;
    private EditText InputPhoneNumber, InputPassword;

    private ProgressDialog loadingBar;
    private String parentDbName= "Users";

    private CheckBox chkBoxRememberMe;
    private TextView AdminLink, NotAdminLink, ForgotPasswordLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginButton = findViewById(R.id.login_button);
        InputPhoneNumber = findViewById(R.id.login_phone_number_input);
        InputPassword = findViewById(R.id.login_password_input);

        loadingBar = new ProgressDialog(this);

        chkBoxRememberMe =findViewById(R.id.remember_me_chkb);
        Paper.init(this);

        AdminLink= findViewById(R.id.login_admin_login);
        NotAdminLink= findViewById(R.id.login_not_admin_login);
        ForgotPasswordLink= findViewById(R.id.forgot_password_link);


        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        });

        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginButton.setText("Login Admin");
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdminLink.setVisibility(View.VISIBLE);
                parentDbName= "Admins";
            }
        });

        NotAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                LoginButton.setText("Login ");
                AdminLink.setVisibility(View.VISIBLE);
                NotAdminLink.setVisibility(View.INVISIBLE);
                parentDbName= "Users";
            }
        });

        ForgotPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(LoginActivity.this, ResetPasswordActivity.class);
                intent.putExtra("check","login");
                startActivity(intent);
            }
        });
    }

    private void LoginUser()
    {
        String phone = InputPhoneNumber.getText().toString();
        String password = InputPassword.getText().toString();

        if (TextUtils.isEmpty(phone))
            {
            Toast.makeText(this, "Please write your phone Number.....", Toast.LENGTH_SHORT).show();
            }
        else if (TextUtils.isEmpty(password))
            {
                Toast.makeText(this, "Please write your password.....", Toast.LENGTH_SHORT).show();
            }
        else
            {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAccount(phone, password);

        }
    }

    private void AllowAccessToAccount(final String phone, final String password)
    {
        if(chkBoxRememberMe.isChecked())
        {
            Paper.book().write(Prevalent.UserPhoneKey, phone);
            Paper.book().write(Prevalent.UserPasswordKey, password);
        }

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapShot) {
                if(dataSnapShot.child(parentDbName).child(phone).exists())
                {
                    Users userData= dataSnapShot.child(parentDbName).child(phone).getValue(Users.class);

                    if(userData.getPhone().equals(phone))
                    {
                        if(userData.getPassword().equals(password))
                        {
                            if(parentDbName.equals("Admins")) {
                                Toast.makeText(LoginActivity.this, "Welcome Admin, you have logged in successfully", Toast.LENGTH_SHORT).show();

                                Intent HomeIntent = new Intent(LoginActivity.this, com.android.github.souravbera.e_commerce.Admin.AdminCategoryActivity.class);
                                startActivity(HomeIntent);
                                loadingBar.dismiss();
                            }
                            else if(parentDbName.equals("Users")){
                                Toast.makeText(LoginActivity.this, "You have successfully logged in ", Toast.LENGTH_SHORT).show();

                                Intent HomeIntent = new Intent(LoginActivity.this, HomeActivity.class);
                                Prevalent.currentOnlineUser = userData;
                                startActivity(HomeIntent);
                                loadingBar.dismiss();
                            }
                        }
                        else
                            {
                                Toast.makeText(LoginActivity.this, "Password is incorrect ", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }

                    }
                }else
                {
                    Toast.makeText(LoginActivity.this, "Account with this phone Number does not exists ", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(LoginActivity.this, "you need to create a new account ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
