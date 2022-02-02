package com.sellingvegetables.uis.activity_product;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import android.view.View;

import com.sellingvegetables.R;
import com.sellingvegetables.adapter.ProductAdapter;
import com.sellingvegetables.adapter.ProductCategoryAdapter;
import com.sellingvegetables.databinding.ActivityProductBinding;
import com.sellingvegetables.databinding.DialogAlertBinding;
import com.sellingvegetables.local_database.AccessDatabase;
import com.sellingvegetables.local_database.DataBaseInterfaces;
import com.sellingvegetables.model.CreateOrderModel;
import com.sellingvegetables.model.DepartmentModel;
import com.sellingvegetables.model.ItemCartModel;
import com.sellingvegetables.model.ProductModel;
import com.sellingvegetables.preferences.Preferences;
import com.sellingvegetables.uis.activity_add_product.AddProductActivity;
import com.sellingvegetables.uis.activity_base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends BaseActivity implements DataBaseInterfaces.ProductInterface, DataBaseInterfaces.CategoryInterface {
    private ActivityProductBinding binding;
    private ProductCategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;
    private AccessDatabase accessDatabase;
    private ActivityResultLauncher<Intent> launcher;
    private Preferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product);
        initView();
    }

    private void initView() {
        preferences=Preferences.getInstance();
        accessDatabase = new AccessDatabase(this);

        categoryAdapter = new ProductCategoryAdapter(this);
        binding.recyclerCategory.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recyclerCategory.setAdapter(categoryAdapter);

        productAdapter = new ProductAdapter(this);
        binding.recyclerProduct.setLayoutManager(new GridLayoutManager(this, 2));
        binding.recyclerProduct.setAdapter(productAdapter);
        accessDatabase.getCategory(this);
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            accessDatabase.getCategory(this);
        });
        binding.llMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductActivity.this, AddProductActivity.class);
                launcher.launch(intent);
            }
        });
    }

    public static void CreateDialogAlertProfile(Context context, ProductModel productModel) {
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .create();

        DialogAlertBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_alert, null, false);


        binding.btnAdd.setOnClickListener(v -> {
                  String amount=binding.edtAmount.getText().toString();
                  String price=binding.edtprice.getText().toString();
                  if(!amount.isEmpty()&&!price.isEmpty()){
                      binding.edtAmount.setError(null);
                      binding.edtprice.setError(null);

                      dialog.dismiss();

                  }
                  else{
                      if(amount.isEmpty()){
                          binding.edtAmount.setError(context.getResources().getString(R.string.field_required));
                      }
                      else{
                          binding.edtAmount.setError(null);
                      }
                      if(price.isEmpty()){
                          binding.edtprice.setError(context.getResources().getString(R.string.field_required));
                      }
                      else{
                          binding.edtprice.setError(null);
                      }
                  }
                }

        );
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }
    public void addtocart(ProductModel productModel,int amount,double price) {
        List<ItemCartModel> productDetailsList;
        CreateOrderModel add_order_model = preferences.getcart_olivaData(ProductActivity.this);
        if (add_order_model != null) {
            productDetailsList = add_order_model.getDetails();
            if (productDetailsList == null) {
                productDetailsList = new ArrayList<>();
            }
        } else {
            add_order_model = new CreateOrderModel();
            productDetailsList = new ArrayList<>();
        }
        ItemCartModel productDetails = new ItemCartModel();
        productDetails.setQty(amount);
        productDetails.setProduct_id(productModel.getId());
        productDetails.setPrice(price);
        productDetails.setTotal(amount*price);
        productDetails.setName(productModel.getTitle());
        productDetailsList.add(productDetails);
        add_order_model.setDetails(productDetailsList);
        preferences.create_update_cart_oliva(ProductActivity.this, add_order_model);

    }
    @Override
    public void onCategoryDataSuccess(List<DepartmentModel> categoryModelList) {
        categoryAdapter.updateList(categoryModelList);
        if (categoryModelList.size() > 0) {
            accessDatabase.getProduct(this, categoryModelList.get(0).getId() + "");
        }
    }

    @Override
    public void onProductDataSuccess(List<ProductModel> productModelList) {
        if (productModelList.size() > 0) {
            productAdapter.updateList(productModelList);
            binding.frame.setVisibility(View.VISIBLE);
        } else {
            binding.frame.setVisibility(View.GONE);
        }
    }

    public void show(int id) {
        accessDatabase.getProduct(this, id + "");

    }
}