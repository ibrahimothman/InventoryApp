package com.example.ibrakarim.inventoryapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ibrakarim.inventoryapp.R;
import com.example.ibrakarim.inventoryapp.adapter.model.Product;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> {

    private List<Product> productList;
    private Context mContext;

    public ProductAdapter(List<Product> productList, Context mContext) {
        this.productList = productList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.item_row,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        Product product = productList.get(position);
        holder.mProductName.setText(product.getName());
        holder.mProductPrice.setText("$"+product.getPrice());
        holder.mProductQuantity.setText(product.getQuantity()+" pieces");
    }

    @Override
    public int getItemCount() {
        if(productList != null) return productList.size();
        else return 0;
    }

    public void swip(List<Product> productList) {
        this.productList = productList;
        this.notifyDataSetChanged();
    }

    class ProductHolder extends RecyclerView.ViewHolder{


        @BindView(R.id.product_image)
        ImageView mProductImage;
        @BindView(R.id.product_name)
        TextView mProductName;
        @BindView(R.id.product_price)
        TextView mProductPrice;
        @BindView(R.id.product_pieces)
        TextView mProductQuantity;
        public ProductHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
