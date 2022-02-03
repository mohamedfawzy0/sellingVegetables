package com.sellingvegetables.uis.activity_home.fragments_home_navigaion;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.sellingvegetables.R;

import com.sellingvegetables.databinding.FragmentHomeBinding;

import com.sellingvegetables.local_database.AccessDatabase;
import com.sellingvegetables.local_database.DataBaseInterfaces;
import com.sellingvegetables.model.CreateOrderModel;
import com.sellingvegetables.model.DepartmentModel;
import com.sellingvegetables.model.ItemCartModel;
import com.sellingvegetables.model.ProductModel;
import com.sellingvegetables.mvvm.FragmentHomeMvvm;
import com.sellingvegetables.uis.activity_base.BaseFragment;
import com.sellingvegetables.uis.activity_home.HomeActivity;
import com.sellingvegetables.uis.activity_product.ProductActivity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class FragmentHome extends BaseFragment implements DataBaseInterfaces.ProductInsertInterface, DataBaseInterfaces.CategoryInsertInterface, DataBaseInterfaces.OrderInsertInterface, DataBaseInterfaces.ProductOrderInsertInterface {
    private static final String TAG = FragmentHome.class.getName();
    private HomeActivity activity;
    private FragmentHomeBinding binding;
    private FragmentHomeMvvm fragmentHomeMvvm;
    private AccessDatabase accessDatabase;
    private int index = 0;
    private CompositeDisposable disposable = new CompositeDisposable();
    private List<ProductModel> productModels;
    private List<CreateOrderModel> createOrderModels;
    private int pos = 0;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Observable.timer(130, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull Long aLong) {
                        initView();

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void initView() {
        accessDatabase = new AccessDatabase(activity);

        binding.setLang(getLang());
        fragmentHomeMvvm = ViewModelProviders.of(this).get(FragmentHomeMvvm.class);
        fragmentHomeMvvm.getCategoryData().observe(activity, new androidx.lifecycle.Observer<List<DepartmentModel>>() {
            @Override
            public void onChanged(List<DepartmentModel> departmentModels) {
                if (departmentModels.size() > 0) {
                    Log.e("kkkk", departmentModels.size() + "");
                    accessDatabase.insertCategory(departmentModels, FragmentHome.this);
                    //binding.cardNoData.setVisibility(View.GONE);
                }
            }
        });

        fragmentHomeMvvm.getProductList().observe(activity, new androidx.lifecycle.Observer<List<ProductModel>>() {
            @Override
            public void onChanged(List<ProductModel> productModels) {
                if (productModels != null && productModels.size() > 0) {
                    Log.e("kkkk", productModels.size() + "");

                    FragmentHome.this.productModels = productModels;
                    setImageBitmap();
                }
            }
        });
        fragmentHomeMvvm.getOrderList().observe(activity, new androidx.lifecycle.Observer<List<CreateOrderModel>>() {
            @Override
            public void onChanged(List<CreateOrderModel> createOrderModels) {
                if (createOrderModels != null && createOrderModels.size() > 0) {
                    insertOrders(createOrderModels);
                } else {
                    binding.progBar.setVisibility(View.GONE);
                    binding.nested.setVisibility(View.VISIBLE);
                }
            }
        });
        binding.cardsales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ProductActivity.class);
                startActivity(intent);
            }
        });
        fragmentHomeMvvm.getDepartment(getUserModel());

    }

    private void insertOrders(List<CreateOrderModel> createOrderModels) {
        this.createOrderModels = createOrderModels;
        accessDatabase.insertOrder(createOrderModels.get(pos), FragmentHome.this);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposable.clear();
    }


    @Override
    public void onProductDataInsertedSuccess(Boolean bol) {
        fragmentHomeMvvm.getOrders(getUserModel());
    }

    @Override
    public void onCategoryDataInsertedSuccess(Boolean bol) {
        if (bol) {
            fragmentHomeMvvm.getProducts(getUserModel());
        }
    }

    public void setImageBitmap() {


        Glide.with(this)
                .asBitmap()
                .load(productModels.get(index).getPhoto())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        ProductModel productModel = productModels.get(index);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        resource.compress(Bitmap.CompressFormat.JPEG, 10, stream);
                        productModel.setImageBitmap(stream.toByteArray());
                        productModels.set(index, productModel);

                        index += 1;
                        if (index == productModels.size()) {
                            accessDatabase.insertProduct(productModels, FragmentHome.this);
                        } else {
                            setImageBitmap();
                        }


                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        // super.onLoadFailed(errorDrawable);
                        index += 1;
                        if (index == productModels.size()) {
                            accessDatabase.insertProduct(productModels, FragmentHome.this);
                        } else {
                            setImageBitmap();
                        }
                    }
                });
    }

    @Override
    public void onOrderDataInsertedSuccess(long bol) {
        List<ItemCartModel> list = createOrderModels.get(pos).getDetails();
        for (int i = 0; i < list.size(); i++) {
            ItemCartModel itemCartModel = list.get(i);
            itemCartModel.setCreate_id((int) bol);
            list.set(i, itemCartModel);
        }
        if (bol > 0) {
            accessDatabase.insertOrderProduct(list, this);

        }
    }

    @Override
    public void onProductORderDataInsertedSuccess(Boolean bol) {
        pos++;
        if (pos < createOrderModels.size()) {
            accessDatabase.insertOrder(createOrderModels.get(pos), this);
        } else {
            binding.progBar.setVisibility(View.GONE);
            binding.nested.setVisibility(View.VISIBLE);
        }
    }
}