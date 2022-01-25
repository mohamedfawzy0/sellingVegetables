package com.sellingvegetables.uis.activity_product;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.sellingvegetables.R;
import com.sellingvegetables.adapter.ProductAdapter;
import com.sellingvegetables.adapter.ProductCategoryAdapter;
import com.sellingvegetables.databinding.ActivityProductBinding;
import com.sellingvegetables.databinding.DialogAlertBinding;
import com.sellingvegetables.uis.activity_base.BaseActivity;

public class ProductActivity extends BaseActivity {
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
    public static void CreateDialogAlertProfile(Context context) {
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .create();

        DialogAlertBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_alert, null, false);


        binding.btnAdd.setOnClickListener(v -> {
                    Intent intent = new Intent(context, ProductActivity.class);
                    ((AppCompatActivity) context).startActivityForResult(intent, 2);

                    dialog.dismiss();
                }

        );
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }
}