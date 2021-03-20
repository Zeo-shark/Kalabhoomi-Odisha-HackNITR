package com.android.github.souravbera.e_commerce.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.github.souravbera.e_commerce.Admin.AdminNewOrdersActivity;
import com.android.github.souravbera.e_commerce.HomeActivity;
import com.android.github.souravbera.e_commerce.MainActivity;
import com.android.github.souravbera.e_commerce.R;

public class AdminCategoryActivity extends AppCompatActivity {

    private ImageView t_shirts, t_shirts_sports, female_dresses, sweathers;
    private ImageView glasses, hatsCaps, walletBagsPurses, shoes;
    private ImageView headphonesHandFree, laptops, watches, mobiles;

    private Button logoutBtn, CheckOrdersBtn, MaintainProductsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        t_shirts= findViewById(R.id.t_shirts);
        t_shirts_sports= findViewById(R.id.sports_t_shirts);
        female_dresses= findViewById(R.id.female_dresses);
        sweathers= findViewById(R.id.sweathers);

        glasses= findViewById(R.id.glasses);
        hatsCaps= findViewById(R.id.hats_caps);
        walletBagsPurses= findViewById(R.id.purses_bags_wallets);
        shoes= findViewById(R.id.shoes);

        headphonesHandFree= findViewById(R.id.headphones_handfree);
        laptops= findViewById(R.id.laptops_pc);
        watches= findViewById(R.id.watches);
        mobiles= findViewById(R.id.mobile_phones);

        logoutBtn= findViewById(R.id.admin_logout_btn);
        CheckOrdersBtn= findViewById(R.id.check_orders_btn);
        MaintainProductsBtn= findViewById(R.id.admin_maintain_products);


        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(AdminCategoryActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });


        MaintainProductsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(AdminCategoryActivity.this, HomeActivity.class);
                intent.putExtra("Admin", "Admin");
                startActivity(intent);
                finish();
            }
        });

        CheckOrdersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent= new Intent(AdminCategoryActivity.this, AdminNewOrdersActivity.class);
                startActivity(intent);
                finish();
            }
        });

        t_shirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category","t_shirts");
                startActivity(intent);

            }
        });

        t_shirts_sports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category","sports_t_shirts");
                startActivity(intent);
            }
        });

        female_dresses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category","female_dresses");
                startActivity(intent);
            }
        });
        sweathers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category","sweathers");
                startActivity(intent);
            }
        });
        glasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category","glasses");
                startActivity(intent);
            }
        });
        hatsCaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category","hats_caps");
                startActivity(intent);
            }
        });
        walletBagsPurses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category","purses_bags_wallets");
                startActivity(intent);
            }
        });
       shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category","shoes");
                startActivity(intent);
            }
        });

        headphonesHandFree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category","headphones_handfree");
                startActivity(intent);
            }
        });
        laptops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category","laptops_pcs");
                startActivity(intent);
            }
        });
        watches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category","watches");
                startActivity(intent);
            }
        });
        mobiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category","mobile_phones");
                startActivity(intent);
            }
        });
    }
}
