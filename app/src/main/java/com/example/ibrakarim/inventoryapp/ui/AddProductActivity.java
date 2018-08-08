package com.example.ibrakarim.inventoryapp.ui;


import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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
import com.example.ibrakarim.inventoryapp.model.Product;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class AddProductActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>{

    private static final String TAG = AddProductActivity.class.getSimpleName();
    private static final int LOADER_ID = 17;
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
    private Product mProduct;
    private String status,name,price,desc,quantity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        ButterKnife.bind(this);

        status = "new";

        // setup toolbar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getString(R.string.create_item_activity_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(status.equals("new")) insertIntoDB();
                else if (status.equals("update")) updateInDb();

            }
        });

        Intent intent = getIntent();
        if(intent != null && intent.hasExtra(ProductAdapter.PRODUCT_ID_EXTRA) ){
            mProductId = intent.getIntExtra(ProductAdapter.PRODUCT_ID_EXTRA,0);
            status = "update";
            getSupportLoaderManager().initLoader(LOADER_ID,null,this);
        }
    }

    private void updateInDb() {

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

        Uri uri = ContentUris.withAppendedId(Contract.ProductEntry.CONTENT_URI,mProductId);
        getContentResolver().update(uri,cv,null,null);

        Intent returnIntent = new Intent(this,ProductDetailActivity.class);
        returnIntent.putExtra(ProductAdapter.PRODUCT_ID_EXTRA,mProductId);
        finish();
    }

    private void updateUI() {
        mNameText.setText(name);
        mDescText.setText(desc);
        mPriceText.setText(price);
        mQuantityText.setText(quantity);
    }

    private void insertIntoDB() {

        String name = mNameText.getText().toString();
        String desc = mDescText.getText().toString();
        String price = mPriceText.getText().toString();
        String quantity = mQuantityText.getText().toString();

        ContentValues cv = new ContentValues();
        cv.put(Contract.ProductEntry.NAME_COL,name);
        cv.put(Contract.ProductEntry.PRICE_COL,price);
        cv.put(Contract.ProductEntry.DESCRIPTION_COL,desc);
        cv.put(Contract.ProductEntry.QUANTITY_COL,quantity);
        getContentResolver().insert(Contract.ProductEntry.CONTENT_URI,cv);

        Intent returnIntent = new Intent(this,MainActivity.class);
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
            name = data.getString(data.getColumnIndex(Contract.ProductEntry.NAME_COL));
            desc = data.getString(data.getColumnIndex(Contract.ProductEntry.DESCRIPTION_COL));
            price = data.getString(data.getColumnIndex(Contract.ProductEntry.PRICE_COL));
            quantity = data.getString(data.getColumnIndex(Contract.ProductEntry.QUANTITY_COL));

            updateUI();


        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
