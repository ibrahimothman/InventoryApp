package com.example.ibrakarim.inventoryapp.ui;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ibrakarim.inventoryapp.R;
import com.example.ibrakarim.inventoryapp.data.Contract;
import com.example.ibrakarim.inventoryapp.model.Product;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.FileNotFoundException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class AddProductActivity extends AppCompatActivity{

    private static final String TAG = AddProductActivity.class.getSimpleName();

    private static final int REQUEST_GALLERY_CODE = 20;
    private static final int REQUEST_CAMERA_CODE = 30;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.product_name_edittext)
    AppCompatEditText mNameText;
    @BindView(R.id.product_price_edittext)
    EditText mPriceText;
    @BindView(R.id.product_desc_edittext)
    AppCompatEditText mDescText;
    @BindView(R.id.product_quantity_edittext)
    EditText mQuantityText;
    @BindView(R.id.save_btn)
    Button saveBtn;
    @BindView(R.id.product_image)
    CircleImageView mProductImage;
    @BindView(R.id.change_image_fab)
    FloatingActionButton mChangeImageFAB;

    private Product mProduct;
    private static final int REQUEST_PERMISSION_CODE = 18;
    private Bitmap bitmap;
    private ProgressDialog mProgressDialog;
    private byte[] imageArray;
    private String imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        ButterKnife.bind(this);

        // setup dialog
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Processing...");


        // setup toolbar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getString(R.string.create_item_activity_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertIntoDB();
            }
        });



        mChangeImageFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayDialog();

            }
        });
    }

    private void displayDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[]options = new String[]{"pick up from camera","pick up from gallery"};
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i == 0){
                    // from camera
                    pickupFromCamera();
                }else if(i == 1){
                    // pickupFromGallery
                    pickupFromGallery();
                }
            }
        });

        builder.setTitle("choose an option");
        builder.show();
    }

    private void pickupFromCamera() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, REQUEST_CAMERA_CODE);
    }

    private void pickupFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_GALLERY_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if((requestCode == REQUEST_GALLERY_CODE || requestCode == REQUEST_CAMERA_CODE) && resultCode == RESULT_OK && data != null){
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .start(this);
        }if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                imageUri = result.getUri().toString();
                Picasso.get().load(resultUri).into(mProductImage);
            }


        }
    }



    private void insertIntoDB() {

        ContentValues cv = new ContentValues();
        String name = mNameText.getText().toString();
        String desc = mDescText.getText().toString();
        String price = mPriceText.getText().toString();
        String quantity = mQuantityText.getText().toString();

        if(!name.isEmpty() && !price.isEmpty() && !desc.isEmpty() && !quantity.isEmpty() &&
                imageUri != null) {
            cv.put(Contract.ProductEntry.NAME_COL, name);
            cv.put(Contract.ProductEntry.PRICE_COL, price);
            cv.put(Contract.ProductEntry.DESCRIPTION_COL, desc);
            cv.put(Contract.ProductEntry.QUANTITY_COL, quantity);
            cv.put(Contract.ProductEntry.IMAGE, imageUri);
            getContentResolver().insert(Contract.ProductEntry.CONTENT_URI, cv);

            Intent returnIntent = new Intent(this, MainActivity.class);
            startActivity(returnIntent);
            finish();
        }else Toast.makeText(this, "Please fill out all information", Toast.LENGTH_LONG).show();

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }




}
