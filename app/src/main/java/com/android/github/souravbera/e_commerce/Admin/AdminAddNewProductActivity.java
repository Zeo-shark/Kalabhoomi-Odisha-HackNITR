package com.android.github.souravbera.e_commerce.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.github.souravbera.e_commerce.R;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewProductActivity extends AppCompatActivity {



    private StorageReference ProductImagesRef;
    private DatabaseReference ProductsRef;
    private String CategoryName, Description, Price, Productname, saveCurrentDate, saveCurrentTime;
    private Button AddNewProduct;
    private EditText InputProductName, InputProductDescription, InputProductPrice;
    private ImageView InputProductImage;

    private Uri ImageUri;

    private String productRandomKey, DownloadImageUrl;

    private static final int GalleryPic=1;

    private ProgressDialog loadingBar;
    private Uri downloadUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);


        CategoryName =  getIntent().getExtras().get("category").toString();

        ProductImagesRef= FirebaseStorage.getInstance().getReference().child("Product Images");
        ProductsRef= FirebaseDatabase.getInstance().getReference().child("Products");

        AddNewProduct = findViewById(R.id.add_new_product);
        InputProductName = findViewById(R.id.product_name);
        InputProductDescription = findViewById(R.id.product_description);
        InputProductImage = findViewById(R.id.select_product_Image);
        InputProductPrice = findViewById(R.id.product_price);


        loadingBar= new ProgressDialog(this);
        InputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        AddNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateProductData();
            }
        });

    }
        private void OpenGallery()
        {
            Intent galleryIntent= new Intent();
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent, GalleryPic);


        }


//Intent requestcode
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GalleryPic && resultCode== RESULT_OK && data!= null)
        {
            ImageUri= data.getData();
            InputProductImage.setImageURI(ImageUri);

        }
    }

    private void ValidateProductData()
    {
        Description=InputProductDescription.getText().toString();
        Price= InputProductPrice.getText().toString();
        Productname= InputProductName.getText().toString();


        if(ImageUri== null)
        {
            Toast.makeText(this,"Product Image is Mandatory....", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Description))
        {
            Toast.makeText(this,"Product Description is Mandatory....", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(Price))
        {
            Toast.makeText(this,"Product Price is Mandatory....", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(Productname))
        {
            Toast.makeText(this,"Product Name is Mandatory....", Toast.LENGTH_SHORT).show();
        }
        else{
            storeProductInformation();
        }
    }

    private void storeProductInformation()
    {
        Calendar calendar= Calendar.getInstance();

        SimpleDateFormat currentDate= new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime= new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey= saveCurrentDate+ saveCurrentTime;


        final StorageReference filePath= ProductImagesRef.child(ImageUri.getLastPathSegment()+ productRandomKey+".jpg");

        final UploadTask uploadTask= filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                    String message = e.toString();
                    Toast.makeText(AdminAddNewProductActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminAddNewProductActivity.this, "Product Image stored Successfully ", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask= uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful())
                        {
                            throw task.getException();
                        }

                        DownloadImageUrl= filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(AdminAddNewProductActivity.this, "Product Image set to Database", Toast.LENGTH_SHORT).show();
                            downloadUri = task.getResult();
                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });


    }

    private void SaveProductInfoToDatabase()
    {
        HashMap<String, Object> ProductMap= new HashMap<>();
        ProductMap.put("pid",productRandomKey);
        ProductMap.put("date",saveCurrentDate);
        ProductMap.put("time",saveCurrentTime);
        ProductMap.put("description",Description);
        ProductMap.put("image",downloadUri.toString());
        ProductMap.put("category",CategoryName);
        ProductMap.put("price",Price);
        ProductMap.put("productname",Productname);

        ProductsRef.child(productRandomKey).updateChildren(ProductMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                     if(task.isSuccessful()){
                         Intent ACAintent = new Intent(AdminAddNewProductActivity.this, AdminCategoryActivity.class);
                         startActivity(ACAintent);
                         loadingBar.dismiss();
                         Toast.makeText(AdminAddNewProductActivity.this, "Product is added successfully to Database..... ", Toast.LENGTH_SHORT).show();
                     }
                     else{
                         loadingBar.dismiss();
                         String message= task.getException().toString();
                         Toast.makeText(AdminAddNewProductActivity.this,"Error: "+message,Toast.LENGTH_SHORT).show();
                     }
                    }
                });



    }
}
