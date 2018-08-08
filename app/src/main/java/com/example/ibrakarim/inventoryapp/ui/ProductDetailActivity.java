package com.example.ibrakarim.inventoryapp.ui;


import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;

import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ibrakarim.inventoryapp.R;
import com.example.ibrakarim.inventoryapp.adapter.ProductAdapter;
import com.example.ibrakarim.inventoryapp.data.Contract;
import com.example.ibrakarim.inventoryapp.model.Product;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = ProductDetailActivity.class.getSimpleName();
    public static final String PRODUCT_EXTRA = "PRODUCT_EXTRA";
    private static final int LOADER_ID = 16;
    @BindView(R.id.product_image)
    ImageView mProductImageview;
    @BindView(R.id.product_name)
    TextView mProductName;
    @BindView(R.id.product_price)
    TextView mProductPrice;
    @BindView(R.id.product_quantity)
    TextView mProductQuantity;
    @BindView(R.id.product_detail)
    TextView mProductDetail;
    @BindView(R.id.edit_btn)
    Button mEditBtn;
    @BindView(R.id.delete_btn)
    Button mDeleteBtn;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    private int mProductId;
    private Product mProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // setup views
        ButterKnife.bind(this);

        // get product id
        Intent intent = getIntent();
        if(intent != null ){
            mProductId = intent.getIntExtra(ProductAdapter.PRODUCT_ID_EXTRA,0);
            Log.d(TAG,"id is "+mProductId);
            getSupportLoaderManager().initLoader(LOADER_ID,null,this);
        }

        // edit product
        mEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProduct();
            }
        });

        // delete product
        mDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // show confirm dialog
                showConfirmDialog();
            }
        });


    }

    private void showConfirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm");
        builder.setMessage("Are you sure to delete this produce");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d(TAG,"yes pressed");
                // delete
            }
        });
        builder.setNegativeButton("No",null);
        builder.show();
    }

    private void updateProduct() {
        Intent editIntent = new Intent(this,AddProductActivity.class);
        if(mProduct != null) {
            editIntent.putExtra(PRODUCT_EXTRA, mProduct);
            startActivity(editIntent);
        }else Log.d(TAG,"something wrong");
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        mProgressBar.setVisibility(View.VISIBLE);
        Uri uri = ContentUris.withAppendedId(Contract.ProductEntry.CONTENT_URI,mProductId);
        return new CursorLoader(this,uri ,null,null,null, Contract.ProductEntry.TIME);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if(cursor.getCount() != 0){
            mProgressBar.setVisibility(View.INVISIBLE);
            cursor.moveToFirst();
            mProduct  = new Product();
            mProduct.setName(cursor.getString(cursor.getColumnIndex(Contract.ProductEntry.NAME_COL)));
            mProduct.setDesc(cursor.getString(cursor.getColumnIndex(Contract.ProductEntry.DESCRIPTION_COL)));
            mProduct.setPrice(cursor.getString(cursor.getColumnIndex(Contract.ProductEntry.PRICE_COL)));
            mProduct.setQuantity(cursor.getString(cursor.getColumnIndex(Contract.ProductEntry.QUANTITY_COL)));

            setupToolbar();
            setupUI();
        }
    }




    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void setupToolbar() {
        // setup toolbar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(mProduct.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupUI() {
        // setup UI
        mProductName.setText(mProduct.getName());
        mProductPrice.setText("$ "+mProduct.getPrice());
        mProductQuantity.setText(mProduct.getQuantity()+" pieces");
        mProductDetail.setText(mProduct.getDesc());
    }
}
