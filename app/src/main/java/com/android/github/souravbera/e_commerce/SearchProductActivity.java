package com.android.github.souravbera.e_commerce;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.github.souravbera.e_commerce.Model.Products;
import com.android.github.souravbera.e_commerce.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SearchProductActivity extends AppCompatActivity {

    private Button SearchBtn;
    private EditText inputText;
    private RecyclerView searchList;

    String SearchInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);

        inputText= findViewById(R.id.search_product_name);
        SearchBtn= findViewById(R.id.search_btn);
        searchList= findViewById(R.id.search_list);
        searchList.setLayoutManager(new LinearLayoutManager((SearchProductActivity.this)));

        SearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchInput= inputText.getText().toString();
            }
        });

    }

    @Override
    protected void onStart()
    {
        super.onStart();

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Products");

        FirebaseRecyclerOptions<Products> options=
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(reference.orderByChild("productname").startAt(SearchInput).endAt(SearchInput),Products.class).build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter=
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int i, @NonNull final Products model)
                    {
                        holder.txtProductName.setText(model.getProductname());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText("Price = Rs."+model.getPrice());
                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent= new Intent(SearchProductActivity.this, ProductsDetailsActivity.class);
                                intent.putExtra("pid",model.getPid());
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.products_items_layout ,parent , false);
                        ProductViewHolder holder= new ProductViewHolder(view);
                        return holder;
                    }
                };
        searchList.setAdapter(adapter);
        adapter.startListening();

    }
}
