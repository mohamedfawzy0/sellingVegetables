package com.sellingvegetables.uis.activity_product;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.sellingvegetables.R;
import com.sellingvegetables.adapter.ProductAdapter;
import com.sellingvegetables.adapter.ProductCategoryAdapter;
import com.sellingvegetables.databinding.ActivityProductBinding;

public class ProductActivity extends AppCompatActivity {
    private ActivityProductBinding binding;
    private ProductCategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_product);
        initView();
    }

    private void initView() {

        categoryAdapter=new ProductCategoryAdapter(this);
        binding.recyclerCategory.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL,false));
        binding.recyclerCategory.setAdapter(categoryAdapter);

        productAdapter=new ProductAdapter(this);
        binding.recyclerProduct.setLayoutManager(new GridLayoutManager(this,2));
        binding.recyclerProduct.setAdapter(productAdapter);


    }
}