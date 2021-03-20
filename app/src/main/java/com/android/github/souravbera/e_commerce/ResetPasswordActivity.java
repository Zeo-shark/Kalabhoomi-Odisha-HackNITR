package com.android.github.souravbera.e_commerce;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.github.souravbera.e_commerce.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ResetPasswordActivity extends AppCompatActivity {

    private String check = "";

    private TextView PageTitle, titleQuestion;
    private EditText PhoneNumber, question1, question2;
    private Button VerifyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        PageTitle = findViewById(R.id.page_title);
        titleQuestion = findViewById(R.id.question_title);
        question1 = findViewById(R.id.question_1);
        question2 = findViewById(R.id.question_2);
        PhoneNumber = findViewById(R.id.find_phone_number);
        VerifyBtn = findViewById(R.id.verify_btn);


        check = getIntent().getStringExtra("check");
    }

    @Override
    protected void onStart() {
        super.onStart();

        PhoneNumber.setVisibility(View.GONE);


        if (check.equals("settings")) {
            PageTitle.setText("Set Questions");
            titleQuestion.setText("Please set the Answers for the following Security Questions");
            PhoneNumber.setVisibility(View.GONE);
            VerifyBtn.setText("Set");
            displayPreviousAnswers();

            VerifyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setAnswers();

                }
            });

        } else if (check.equals("login")) {
            PhoneNumber.setVisibility(View.VISIBLE);

            VerifyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    verifyUser();
                }
            });
        }
    }

    private void setAnswers() {
        String answer1 = question1.getText().toString().toLowerCase();
        String answer2 = question2.getText().toString().toLowerCase();

        if (answer1.equals("") && answer2.equals("")) {
            Toast.makeText(ResetPasswordActivity.this, "Please answer the questions first", Toast.LENGTH_SHORT).show();
        } else {

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                    .child("Users").child(Prevalent.currentOnlineUser.getPhone());

            HashMap<String, Object> userdataMap = new HashMap<>();
            userdataMap.put("answer1", answer1);
            userdataMap.put("answer2", answer2);

            ref.child("Security Questions").updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ResetPasswordActivity.this, "You have answered the security questions successfully!", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(ResetPasswordActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }
                }
            });

        }
    }

    private void displayPreviousAnswers() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(Prevalent.currentOnlineUser.getPhone());

        ref.child("Security Questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String ans1 = snapshot.child("answer1").getValue().toString();
                    String ans2 = snapshot.child("answer2").getValue().toString();

                    question1.setText(ans1);
                    question2.setText(ans2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void verifyUser() {
        final String phone = PhoneNumber.getText().toString();

        final String answer1 = question1.getText().toString().toLowerCase();
        final String answer2 = question1.getText().toString().toLowerCase();

        if (!phone.equals("") && !answer1.equals("") && !answer2.equals("")) {
            final DatabaseReference ref = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Users")
                    .child(phone);

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String mPhone = snapshot.child("phone").getValue().toString();


                        if (snapshot.hasChild("Security Questions")) {
                            String ans1 = snapshot.child("Security Questions").child("answer1").getValue().toString();
                            String ans2 = snapshot.child("Security Questions").child("answer2").getValue().toString();

                            if (!ans1.equals(answer1)) {
                                Toast.makeText(ResetPasswordActivity.this, "You 1st Answer is wrong Please Enter correct answer!", Toast.LENGTH_SHORT).show();
                            } else if (!ans2.equals(answer2)) {
                                Toast.makeText(ResetPasswordActivity.this, "You 2nd Answer is wrong Please Enter correct answer!", Toast.LENGTH_SHORT).show();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);
                                builder.setTitle("New Password");
                                final EditText newPassword = new EditText(ResetPasswordActivity.this);
                                newPassword.setHint("Write New Passowrd Here");
                                builder.setView(newPassword);

                                builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (!newPassword.getText().toString().equals("")) {
                                            ref.child("password")
                                                    .setValue(newPassword.getText().toString())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(ResetPasswordActivity.this, "New Password set Successfully", Toast.LENGTH_SHORT).show();

                                                                Intent intent= new Intent(ResetPasswordActivity.this, LoginActivity.class);
                                                                startActivity(intent);
                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                builder.show();
                            }
                        } else {
                            Toast.makeText(ResetPasswordActivity.this, " you have'nt security questions", Toast.LENGTH_SHORT)
                                    .show();
                        }

                    }
                    else
                    {
                        Toast.makeText(ResetPasswordActivity.this, "This Phone Number does not exist",Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else{
            Toast.makeText(ResetPasswordActivity.this, "Please Complete the Form",Toast.LENGTH_SHORT).show();
        }

    }
}
