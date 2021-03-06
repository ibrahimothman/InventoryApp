package com.example.ibrakarim.inventoryapp.ui;


import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.ibrakarim.inventoryapp.R;
import com.example.ibrakarim.inventoryapp.adapter.ProductAdapter;
import com.example.ibrakarim.inventoryapp.data.Contract;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.FileNotFoundException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class EditProductActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID = 22;
    private static final String TAG = EditProductActivity.class.getSimpleName();
    private static final int REQUEST_IMAGE_CODE = 120;
    private static final int REQUEST_CAMERA_CODE = 40;
    private static final int REQUEST_GALLERY_CODE = 50;


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


    private int mProductId;
    private Bitmap bitmap;
    private String imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        ButterKnife.bind(this);

        // setup toolbar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getString(R.string.edit_item_activity_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if(intent != null && intent.hasExtra(ProductDetailActivity.PRODUCT_ID_EXTRA) ){
            mProductId = intent.getIntExtra(ProductAdapter.PRODUCT_ID_EXTRA,0);
            getSupportLoaderManager().initLoader(LOADER_ID,null,this);
        }


        mChangeImageFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayDialog();

            }
        });


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatDb();
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


    private void pickupImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,REQUEST_IMAGE_CODE);
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
                imageUri = result.getUri().toString();
                // use picasso to display updated image immediately
                Picasso.get().load(Uri.parse(imageUri)).into(mProductImage);
            }


        }
    }


    private void updatDb() {

        Log.d(TAG,"update");
        String name = mNameText.getText().toString();
        String desc = mDescText.getText().toString();
        String price = mPriceText.getText().toString();
        String quantity = mQuantityText.getText().toString();

        ContentValues cv = new ContentValues();
        cv.put(Contract.ProductEntry.NAME_COL,name);
        cv.put(Contract.ProductEntry.PRICE_COL,price);
        cv.put(Contract.ProductEntry.DESCRIPTION_COL,desc);
        cv.put(Contract.ProductEntry.QUANTITY_COL,quantity);
        cv.put(Contract.ProductEntry.IMAGE, imageUri);

        Uri uri = ContentUris.withAppendedId(Contract.ProductEntry.CONTENT_URI,mProductId);
        getContentResolver().update(uri,cv,null,null);

        Intent returnIntent = new Intent(this,ProductDetailActivity.class);
        returnIntent.putExtra(ProductDetailActivity.PRODUCT_ID_EXTRA,mProductId);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Uri uri = ContentUris.withAppendedId(Contract.ProductEntry.CONTENT_URI,mProductId);
        return new CursorLoader(this,uri ,null,null,null, Contract.ProductEntry.TIME);

    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if(data.getCount() != 0){
            data.moveToFirst();
            String name = data.getString(data.getColumnIndex(Contract.ProductEntry.NAME_COL));
            String desc = data.getString(data.getColumnIndex(Contract.ProductEntry.DESCRIPTION_COL));
            String price = data.getString(data.getColumnIndex(Contract.ProductEntry.PRICE_COL));
            String quantity = data.getString(data.getColumnIndex(Contract.ProductEntry.QUANTITY_COL));
            String image = data.getString(data.getColumnIndex(Contract.ProductEntry.IMAGE));
            updateUI(name,desc,price,quantity,image);


        }
    }

    private void updateUI(String name, String desc, String price, String quantity,String image) {
        mNameText.setText(name);
        mDescText.setText(desc);
        mPriceText.setText(price);
        mQuantityText.setText(quantity);
        Picasso.get().load(image).into(mProductImage);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
