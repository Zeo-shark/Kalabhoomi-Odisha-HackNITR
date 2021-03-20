package com.android.github.souravbera.e_commerce;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.github.souravbera.e_commerce.Model.Cart;
import com.android.github.souravbera.e_commerce.Prevalent.Prevalent;
import com.android.github.souravbera.e_commerce.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private Button NextProcessBtn;
    private TextView txtTotalAmount, msg1;

    private double overTotalPrice= 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView= findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        NextProcessBtn = findViewById(R.id.next_process_btn);
        txtTotalAmount= findViewById(R.id.total_price);



        msg1= findViewById(R.id.msg1);
        NextProcessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
             Intent intent= new Intent(CartActivity.this, ConfirmFinalOrderActivity.class);
             intent.putExtra("Total Price", String.valueOf(overTotalPrice));
             startActivity(intent);
             finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        CheckOrderState();

        final DatabaseReference cartListRef= FirebaseDatabase.getInstance().getReference().child("Cart List");

        FirebaseRecyclerOptions<Cart> options=
                new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef.child("User View")
                        .child(Prevalent.currentOnlineUser.getPhone()).child("Products"), Cart.class)
                        .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter
                = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int i, @NonNull final Cart model)
            {
                holder.txtProductQuatity.setText(String.format("Product Quantity: %s", model.getQuantity()));
                holder.txtProductPrice.setText(String.format("Product Price: Rs.%s", model.getPrice()));
                holder.txtProductName.setText(String.format("Product: %s", model.getProductname()));





                double oneTypeProductTPrice= ((Double.parseDouble(model.getPrice())) *(Double.parseDouble(model.getQuantity())));
                overTotalPrice += oneTypeProductTPrice;

                txtTotalAmount.setText(String.format("Total Price: Rs.%s", String.valueOf(overTotalPrice)));

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                            CharSequence  options[]= new CharSequence[]
                                    {
                                            "Edit",
                                            "Remove"
                                    };
                        AlertDialog.Builder builder=  new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Cart Options:");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i)
                            {
                                if(i==0){
                                    Intent intent= new Intent(CartActivity.this, ProductsDetailsActivity.class);
                                    intent.putExtra("pid",model.getPid());
                                    startActivity(intent);
                                }
                                if(i==1)
                                {
                                    cartListRef.child("User View")
                                            .child(Prevalent.currentOnlineUser.getPhone())
                                            .child("Products")
                                            .child(model.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task)
                                                {
                                                    if(task.isSuccessful())
                                                    {
                                                        Toast.makeText(CartActivity.this,"Item removed successfully",Toast.LENGTH_SHORT).show();

                                                        Intent intent= new Intent(CartActivity.this, HomeActivity.class);
                                                        startActivity(intent);
                                                    }

                                                }
                                            });
                                }
                            }
                        });
                        builder.show();
                    }
                });

            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent, false);
                CartViewHolder holder= new CartViewHolder(view);
                return holder;

            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
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
                    String username= dataSnapshot.child("name").getValue().toString();

                    if(shippingState.equals("shipped"))
                    {
                        txtTotalAmount.setText(String.format("Dear %s, \n order is shipped successfully.", username));
                        recyclerView.setVisibility(View.GONE);
                        msg1.setText("Congratulations your final order has been placed successfully, soon you will recieve your order at your doorsteps");
                        msg1.setVisibility(View.VISIBLE);
                        NextProcessBtn.setVisibility(View.GONE);

                        Toast.makeText(CartActivity.this, "you can purchase more products once you recieved your first final order",Toast.LENGTH_SHORT).show();
                    }
                    else if(shippingState.equals("not shipped"))
                    {
                        txtTotalAmount.setText(String.format("Dear %s, \n order is not shipped yet.", username));
                        recyclerView.setVisibility(View.GONE);
                        msg1.setText("Your Order is in process, soon it will be shipped...... ");
                        msg1.setVisibility(View.VISIBLE);
                        NextProcessBtn.setVisibility(View.GONE);

                        Toast.makeText(CartActivity.this, "you can purchase more products once you recieved your first final order",Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
