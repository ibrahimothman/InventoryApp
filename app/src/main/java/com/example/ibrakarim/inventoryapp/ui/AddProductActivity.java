package com.example.ibrakarim.inventoryapp.ui;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
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

public class AddProductActivity extends AppCompatActivity {

    private static final String TAG = AddProductActivity.class.getSimpleName();
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
    private String status;


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
        if(intent != null && intent.getParcelableExtra(ProductDetailActivity.PRODUCT_EXTRA) != null){
            mProduct = intent.getParcelableExtra(ProductDetailActivity.PRODUCT_EXTRA);
            status = "update";
            updateUI();
        }
    }

    private void updateInDb() {

        String name = mNameText.getText().toString();
        String desc = mDescText.getText().toString();
        String price = mPriceText.getText().toString();
        String quantity = mQuantityText.getText().toString();

        ContentValues cv = new ContentValues();
        cv.put(Contract.ProductEntry.NAME_COL,name);
        cv.put(Contract.ProductEntry.PRICE_COL,price);
        cv.put(Contract.ProductEntry.DESCRIPTION_COL,desc);
        cv.put(Contract.ProductEntry.QUANTITY_COL,quantity);

        int productId = mProduct.getId();
        Uri uri = ContentUris.withAppendedId(Contract.ProductEntry.CONTENT_URI,productId);
        getContentResolver().update(uri,cv,null,null);

        Intent returnIntent = new Intent(this,ProductDetailActivity.class);
        returnIntent.putExtra(ProductAdapter.PRODUCT_ID_EXTRA,productId);
        finish();
    }

    private void updateUI() {
        mNameText.setText(mProduct.getName());
        mDescText.setText(mProduct.getDesc());
        mPriceText.setText(mProduct.getPrice());
        mQuantityText.setText(mProduct.getQuantity());
    }

    private void insertIntoDB() {
        String name = mNameText.getText().toString();
        String desc = mDescText.getText().toString();
        String price = mPriceText.getText().toString();
        String quantity = mQuantityText.getText().toString();

        Log.d(TAG,name);
        Log.d(TAG,desc);
        Log.d(TAG,price);
        Log.d(TAG,quantity);

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
}
