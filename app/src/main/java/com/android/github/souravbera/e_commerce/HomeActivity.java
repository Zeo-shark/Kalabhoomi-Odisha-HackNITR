package com.android.github.souravbera.e_commerce;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.github.souravbera.e_commerce.Admin.AdminMaintainProductsActivity;
import com.android.github.souravbera.e_commerce.Admin.AdminMaintainProductsActivity;
import com.android.github.souravbera.e_commerce.Model.Products;
import com.android.github.souravbera.e_commerce.Prevalent.Prevalent;
import com.android.github.souravbera.e_commerce.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {



    private DatabaseReference ProductsRef;
    private String type= "";
    private RecyclerView ProductsList;
    RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Paper.init(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        Intent intent= getIntent();
        Bundle  bundle= intent.getExtras();
        if(bundle != null)
        {
            type = getIntent().getExtras().get("Admin").toString();
        }

        //initialisation
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!type.equals("Admin"))
                {
                    Intent intent= new Intent(HomeActivity.this, CartActivity.class);
                    startActivity(intent);

                }

            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(this,
                drawer,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView= navigationView.getHeaderView(0);
        TextView userNameTextView= headerView.findViewById(R.id.user_profile_name);
        CircleImageView profileImageView= headerView.findViewById(R.id.user_profile_image);
        //setting Username
        if(!type.equals("Admin"))
        {
            userNameTextView.setText(Prevalent.currentOnlineUser.getName());

            Picasso.get().load(Prevalent.currentOnlineUser.getImage()).placeholder(R.drawable.profile).into(profileImageView);
        }


        ProductsList = findViewById(R.id.recycler_menu);
        ProductsList.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(this);
        ProductsList.setLayoutManager(layoutManager);

    }

    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<Products> options=
                new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(ProductsRef, Products.class)
                .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter=
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int i, @NonNull final Products model) {

                        holder.txtProductName.setText(model.getProductname());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText(String.format("Price = Rs.%s", model.getPrice()));
                        Picasso.get().load(model.getImage()).placeholder(R.drawable.profile).into(holder.imageView);


                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(type.equals("Admin"))
                                {
                                    Intent intent= new Intent(HomeActivity.this, AdminMaintainProductsActivity.class);
                                    intent.putExtra("Pid", model.getPid());
                                    startActivity(intent);

                                }
                                else
                                {
                                        Intent intent= new Intent(HomeActivity.this, ProductsDetailsActivity.class);
                                        intent.putExtra("pid", model.getPid());
                                        startActivity(intent);
                                }
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                     View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.products_items_layout ,parent , false);
                     ProductViewHolder holder= new ProductViewHolder(view);
                     return holder;

                    }
                };
        ProductsList.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id= item.getItemId();

        if(id==R.id.nav_cart){
            if(!type.equals("Admin"))
            {
                Intent intent= new Intent(HomeActivity.this, CartActivity.class);
                startActivity(intent);

            }
        }
        else if(id==R.id.nav_search){
            if(!type.equals("Admin"))
            {
                Intent intent= new Intent(HomeActivity.this, SearchProductActivity.class);
                startActivity(intent);

            }


        }
        else if(id==R.id.nav_categories){

        }

        else if(id==R.id.nav_settings){
            if(!type.equals("Admin"))
            {
                Intent intent= new Intent(HomeActivity.this, ActivitySettings.class);
                startActivity(intent);

            }

        }
        else if(id==R.id.nav_logout)
        {
            Paper.book().destroy();

            Intent intent= new Intent(HomeActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer= findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}




