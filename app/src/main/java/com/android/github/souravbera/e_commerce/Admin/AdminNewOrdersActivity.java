package com.android.github.souravbera.e_commerce.Admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.github.souravbera.e_commerce.Admin.AdminUserProductsActivity;
import com.android.github.souravbera.e_commerce.Model.AdminOrders;
import com.android.github.souravbera.e_commerce.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.android.github.souravbera.e_commerce.R.layout.orders_layout;

public class AdminNewOrdersActivity extends AppCompatActivity {

    private DatabaseReference ordersRef;
    private RecyclerView orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);

        ordersRef= FirebaseDatabase.getInstance().getReference().child("Orders");

        orderList= findViewById(R.id.orders_list);
        orderList.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<AdminOrders> options=
                new FirebaseRecyclerOptions.Builder<AdminOrders>()
                .setQuery(ordersRef, AdminOrders.class)
                .build();

        FirebaseRecyclerAdapter<AdminOrders,AdminOrdersViewHolder> adapter=
                new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AdminOrdersViewHolder holder, final int i, @NonNull final AdminOrders model)
                    {

                        holder.userName.setText(String.format("Name: %s", model.getName()));
                        holder.userPhoneNumber.setText(String.format("Phone Number: %s", model.getPhone()));
                        holder.userTotalPrice.setText(String.format("Total Amount: %s", model.getTotalAmount()));
                        holder.userDateTime.setText(String.format("OrderedAt: %s  %s", model.getDate(), model.getTime()));
                        holder.userShippingAddress.setText(String.format("ShippingAddress: %s  %s", model.getAddress(), model.getCity()));

                        holder.ShowOrderBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                String UID= getRef(i).getKey();

                                Intent intent= new Intent(AdminNewOrdersActivity.this, AdminUserProductsActivity.class);
                                intent.putExtra("uid",model.getPhone());
                                startActivity(intent);
                            }
                        });
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CharSequence options[] = new CharSequence[]
                                        {
                                                "Yes",
                                                "No"
                                        };

                                AlertDialog.Builder builder= new AlertDialog.Builder(AdminNewOrdersActivity.this);
                                builder.setTitle("Have you shipped this order products ?");

                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int pos)
                                    {
                                        if(i==0){
                                            String UID= getRef(pos).getKey();

                                            RemoverOrder(UID);
                                        }else{
                                            finish();
                                        }
                                    }
                                });
                                builder.show();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout, parent);
                        return new AdminOrdersViewHolder(view);
                    }
                };

        orderList.setAdapter(adapter);
        adapter.startListening();
    }


    public static class AdminOrdersViewHolder extends RecyclerView.ViewHolder
    {
        public TextView userName,userPhoneNumber, userTotalPrice, userDateTime, userShippingAddress;
        public Button ShowOrderBtn;

        public AdminOrdersViewHolder(@NonNull View itemView)
        {
            super(itemView);

            userName= itemView.findViewById(R.id.orders_user_name);
            userPhoneNumber= itemView.findViewById(R.id.orders_phone);
            userTotalPrice= itemView.findViewById(R.id.orders_total_price);
            userDateTime= itemView.findViewById(R.id.orders_date_time);
            userShippingAddress= itemView.findViewById(R.id.orders_address_city);
            ShowOrderBtn= itemView.findViewById(R.id.show_all_products_btn);

        }


    }
    private void RemoverOrder(String UID)
    {
        ordersRef.child(UID).removeValue();
    }

}
