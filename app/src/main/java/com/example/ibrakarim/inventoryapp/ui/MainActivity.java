package com.example.ibrakarim.inventoryapp.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.ibrakarim.inventoryapp.R;
import com.example.ibrakarim.inventoryapp.adapter.ProductAdapter;
import com.example.ibrakarim.inventoryapp.model.Product;
import com.example.ibrakarim.inventoryapp.data.Contract;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    private static final int LOADER_ID = 15;
    @BindView(R.id.recycler_list)
    RecyclerView mRecyclerView;

    ProductAdapter productAdapter;
    List<Product> productList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        productAdapter = new ProductAdapter(productList,this);
        mRecyclerView.setAdapter(productAdapter);


        getSupportLoaderManager().initLoader(LOADER_ID,null,this);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addProductIntent = new Intent(MainActivity.this,AddProductActivity.class);
                startActivity(addProductIntent);
            }
        });
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(this, Contract.ProductEntry.CONTENT_URI,null,null,null, Contract.ProductEntry.TIME);

    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if(data.getCount() != 0){
            productList.clear();
            data.moveToFirst();
            do{
                Product product = new Product();
                product.setId(data.getInt(data.getColumnIndex(Contract.ProductEntry._ID)));
                product.setName(data.getString(data.getColumnIndex(Contract.ProductEntry.NAME_COL)));
                product.setDesc(data.getString(data.getColumnIndex(Contract.ProductEntry.DESCRIPTION_COL)));
                product.setPrice(data.getString(data.getColumnIndex(Contract.ProductEntry.PRICE_COL)));
                product.setQuantity(data.getString(data.getColumnIndex(Contract.ProductEntry.QUANTITY_COL)));
                product.setImage(data.getBlob(data.getColumnIndex(Contract.ProductEntry.IMAGE)));

                productList.add(product);
            }while (data.moveToNext());
            productAdapter.swip(productList);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
