package com.example.ibrakarim.inventoryapp.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ibrakarim.inventoryapp.R;
import com.example.ibrakarim.inventoryapp.adapter.ProductAdapter;
import com.example.ibrakarim.inventoryapp.model.Product;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductDetailActivity extends AppCompatActivity {

    private static final String TAG = ProductDetailActivity.class.getSimpleName();
    private static final String PRODUCT_EXTRA = "PRODUCT_EXTRA";
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

    private Product mProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // setup views
        ButterKnife.bind(this);



        // get product detail
        Intent intent = getIntent();
        if(intent != null && intent.getParcelableExtra(ProductAdapter.PRODUCT_DETAIL_EXTRA) != null){
            mProduct = intent.getParcelableExtra(ProductAdapter.PRODUCT_DETAIL_EXTRA);
        }

        // setup toolbar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(mProduct.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // setup UI
        mProductName.setText(mProduct.getName());
        mProductPrice.setText("$ "+mProduct.getPrice());
        mProductQuantity.setText(mProduct.getQuantity()+" pieces");
        mProductDetail.setText(mProduct.getDesc());

        // edit product
        mEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                updateProduct();
            }
        });
        builder.setNegativeButton("No",null);
        builder.show();
    }

    private void updateProduct() {
        Intent editIntent = new Intent(this,AddProductActivity.class);
        editIntent.putExtra(PRODUCT_EXTRA,mProduct);
        startActivity(editIntent);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
