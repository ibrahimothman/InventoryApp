package com.example.ibrakarim.inventoryapp.ui;

import android.content.ContentValues;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.ibrakarim.inventoryapp.R;
import com.example.ibrakarim.inventoryapp.data.Contract;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class AddProductActivity extends AppCompatActivity {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        ButterKnife.bind(this);

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
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
