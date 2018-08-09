package com.example.ibrakarim.inventoryapp.ui;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ibrakarim.inventoryapp.R;
import com.example.ibrakarim.inventoryapp.adapter.ProductAdapter;
import com.example.ibrakarim.inventoryapp.data.Contract;
import com.example.ibrakarim.inventoryapp.helper.ImageHelper;
import com.example.ibrakarim.inventoryapp.model.Product;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class AddProductActivity extends AppCompatActivity{

    private static final String TAG = AddProductActivity.class.getSimpleName();

    private static final int REQUEST_IMAGE_CODE = 20;
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
                pickupImage();
            }
        });
    }

    private void pickupImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,REQUEST_IMAGE_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE_CODE && resultCode == RESULT_OK && data != null){
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .start(this);
        }if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    InputStream inputStream = getContentResolver().openInputStream(resultUri);
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    Log.d(TAG,"bitmab is "+bitmap.toString());
                    mProductImage.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }


        }
    }



    private void insertIntoDB() {

        ContentValues cv = new ContentValues();
        String name = mNameText.getText().toString();
        String desc = mDescText.getText().toString();
        String price = mPriceText.getText().toString();
        String quantity = mQuantityText.getText().toString();



        if(bitmap != null) {
            imageArray = ImageHelper.getImageByteArray(bitmap);
        }else {
            Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),R.drawable.main_default_image);
            imageArray = ImageHelper.getImageByteArray(bitmap);
        }
        if(!name.isEmpty() && !price.isEmpty() && !desc.isEmpty() && !quantity.isEmpty()) {


            cv.put(Contract.ProductEntry.NAME_COL, name);
            cv.put(Contract.ProductEntry.PRICE_COL, price);
            cv.put(Contract.ProductEntry.DESCRIPTION_COL, desc);
            cv.put(Contract.ProductEntry.QUANTITY_COL, quantity);
            cv.put(Contract.ProductEntry.IMAGE, imageArray);
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
