package com.sooft_sales.uis.activity_home.fragments_home_navigaion;

import static android.app.Activity.RESULT_OK;

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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.sooft_sales.R;

import com.sooft_sales.databinding.FragmentHomeBinding;

import com.sooft_sales.local_database.AccessDatabase;
import com.sooft_sales.local_database.DataBaseInterfaces;
import com.sooft_sales.model.CreateOrderModel;
import com.sooft_sales.model.DepartmentModel;
import com.sooft_sales.model.ItemCartModel;
import com.sooft_sales.model.ProductModel;
import com.sooft_sales.model.SettingDataModel;
import com.sooft_sales.model.SettingModel;
import com.sooft_sales.model.UserSettingsModel;
import com.sooft_sales.mvvm.FragmentHomeMvvm;
import com.sooft_sales.preferences.Preferences;
import com.sooft_sales.tags.Tags;
import com.sooft_sales.uis.activity_base.BaseFragment;
import com.sooft_sales.uis.activity_home.HomeActivity;
import com.sooft_sales.uis.activity_product.ProductActivity;
import com.sooft_sales.uis.activity_return_invoice.ReturnInvoiceActivity;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

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


public class FragmentHome extends BaseFragment implements DataBaseInterfaces.ProductInsertInterface, DataBaseInterfaces.CategoryInsertInterface, DataBaseInterfaces.OrderInsertInterface, DataBaseInterfaces.ProductOrderInsertInterface, DataBaseInterfaces.ProductInterface, DataBaseInterfaces.ProductupdateInterface, DataBaseInterfaces.AllOrderInterface, DataBaseInterfaces.AllOrderProductInterface {
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
    private Preferences preferences;
    private UserSettingsModel userSettingsModel;
    private List<ProductModel> productModelList;
    private ActivityResultLauncher<Intent> launcher;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Navigation.findNavController(binding.getRoot()).navigate(R.id.cart);
            }
        });
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
        checkpermission();
        accessDatabase = new AccessDatabase(activity);
        preferences = Preferences.getInstance();
        userSettingsModel = preferences.getUserSettings(activity);

        binding.setLang(getLang());
        fragmentHomeMvvm = ViewModelProviders.of(this).get(FragmentHomeMvvm.class);
        fragmentHomeMvvm.getSetting().observe(this, new androidx.lifecycle.Observer<SettingDataModel>() {
            @Override
            public void onChanged(SettingDataModel settingDataModel) {
                if (settingDataModel != null) {
                    Picasso.get()
                            .load(Tags.base_url+settingDataModel.getData().getLogo())
                            .into(new Target() {
                                @Override
                                public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                                    /* Save the bitmap or do something with it here */
                                    SettingModel settingModel = settingDataModel.getData();

                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 10, stream);
                                    settingModel.setImageBitmap(stream.toByteArray());
                                    settingDataModel.setData(settingModel);
                                    preferences.createUpdateUserDataSetting(activity, settingDataModel);

                                }

                                @Override
                                public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                                    preferences.createUpdateUserDataSetting(activity, settingDataModel);

                                }

                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {

                                }
                            });
                }
            }
        });
        fragmentHomeMvvm.getCategoryData().observe(activity, new androidx.lifecycle.Observer<List<DepartmentModel>>() {
            @Override
            public void onChanged(List<DepartmentModel> departmentModels) {
                if (departmentModels.size() > 0) {
                    //Log.e("kkkk", departmentModels.size() + "");
                    try {
                        accessDatabase.insertCategory(departmentModels, FragmentHome.this);

                    } catch (Exception e) {

                    }
                    //binding.cardNoData.setVisibility(View.GONE);
                } else {
                    index = 0;
                    pos = 0;
                    binding.progBar.setVisibility(View.GONE);
                    binding.nested.setVisibility(View.VISIBLE);
                    //   fragmentHomeMvvm.getProducts(getUserModel());
                }
            }
        });

        fragmentHomeMvvm.getProductList().observe(activity, new androidx.lifecycle.Observer<List<ProductModel>>() {
            @Override
            public void onChanged(List<ProductModel> productModels) {
                if (productModels != null && productModels.size() > 0) {
                    Log.e("kkkk", productModels.size() + "");
                    index = 0;
                    FragmentHome.this.productModels = productModels;
                    setImageBitmap();
                } else {
                    index = 0;
                    pos = 0;
                    binding.progBar.setVisibility(View.GONE);
                    binding.nested.setVisibility(View.VISIBLE);
                }
            }
        });
        fragmentHomeMvvm.getOrderList().observe(activity, new androidx.lifecycle.Observer<List<CreateOrderModel>>() {
            @Override
            public void onChanged(List<CreateOrderModel> createOrderModels) {
                if (createOrderModels != null && createOrderModels.size() > 0) {

                    insertOrders(createOrderModels);
                } else {
                    index = 0;
                    pos = 0;
                    binding.progBar.setVisibility(View.GONE);
                    binding.nested.setVisibility(View.VISIBLE);
                }
            }
        });
        fragmentHomeMvvm.getProduct().observe(this, new androidx.lifecycle.Observer<ProductModel>() {
            @Override
            public void onChanged(ProductModel productModel) {
                if (productModel != null && productModel.getId() == productModelList.get(pos).getId()) {
                    pos += 1;
                    if (pos < productModelList.size()) {
                        fragmentHomeMvvm.addproduct(activity, productModelList.get(pos), getUserModel());
                    } else {
                        pos = 0;
                        try {
                            accessDatabase.getallOrder(FragmentHome.this);

                        } catch (Exception e) {

                        }
                    }
                } else {
                    // Log.e("llll","llll");

                    if (productModel != null) {
                        Log.e("llll", "llll");
                        try {
                            accessDatabase.udateProduct(productModelList.get(pos).getId() + "", productModel.getId() + "", FragmentHome.this);

                        } catch (Exception e) {

                        }

                    }
                }
            }
        });
        fragmentHomeMvvm.getSend().observe(activity, new androidx.lifecycle.Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    pos += 1;
                    if (pos < createOrderModels.size()) {
                        if (createOrderModels.get(pos).getLocal() != null && createOrderModels.get(pos).getLocal().equals("local")) {
                            fragmentHomeMvvm.sendOrder(createOrderModels.get(pos), getUserModel());
                        } else {
                            pos += 1;
                            uploadOrders();
                        }
                    } else {
                        pos = 0;
                        backOrder();
                    }
                }
            }
        });
        fragmentHomeMvvm.getBack().

                observe(activity, new androidx.lifecycle.Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if (aBoolean) {
                            pos += 1;
                            backOrder();
                        }
                    }
                });
        binding.cardsales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ProductActivity.class);
                launcher.launch(intent);
            }
        });
        binding.cardReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ReturnInvoiceActivity.class);
                startActivity(intent);
            }
        });
        binding.cardNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index = 0;
                pos = 0;
                binding.progBar.setVisibility(View.VISIBLE);
                binding.nested.setVisibility(View.GONE);
                fragmentHomeMvvm.getSetting(getUserModel());

                try {
                    accessDatabase.getLocalProduct(FragmentHome.this, "local");

                } catch (Exception e) {

                }
            }
        });
        binding.cardLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        accessDatabase.clear();

                    }
                }).start();
                preferences.clearcart_soft(activity);
                preferences.clearUserData(activity);
                activity.logout();
            }
        });
        if (userSettingsModel == null || userSettingsModel.isIs_first()) {
            if (userSettingsModel == null) {
                userSettingsModel = new UserSettingsModel();
            }
            userSettingsModel.setIs_first(false);
            preferences.create_update_user_settings(activity, userSettingsModel);
            fragmentHomeMvvm.getDepartment(getUserModel());
            fragmentHomeMvvm.getSetting(getUserModel());

        } else {
            index = 0;
            pos = 0;
            binding.progBar.setVisibility(View.GONE);
            binding.nested.setVisibility(View.VISIBLE);
        }

    }

    private void checkpermission() {
        if (getUserModel().getData().getPermissions() != null && !getUserModel().getData().getPermissions().contains("ordersBack")) {
            binding.cardReturn.setVisibility(View.GONE);
        }
        if (getUserModel().getData().getPermissions() != null && !getUserModel().getData().getPermissions().contains("productsIndex")) {
            binding.cardsales.setVisibility(View.GONE);
        }
        if (getUserModel().getData().getPermissions() != null && !getUserModel().getData().getPermissions().contains("productsIndex") && !getUserModel().getData().getPermissions().contains("ordersBack")) {
            binding.cardNew.setVisibility(View.GONE);
        }

    }

    private void backOrder() {
        if (pos < createOrderModels.size()) {
            if (createOrderModels.get(pos).isIs_back()) {
                fragmentHomeMvvm.backOrder(createOrderModels.get(pos), getUserModel());
            } else {
                pos += 1;
                backOrder();
            }
        } else {
            pos = 0;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    accessDatabase.clear();

                }
            }).start();
            fragmentHomeMvvm.getDepartment(getUserModel());
        }
    }

    private void insertOrders(List<CreateOrderModel> createOrderModels) {
        this.createOrderModels = createOrderModels;
        try {
            accessDatabase.insertOrder(createOrderModels.get(pos), FragmentHome.this);

        } catch (Exception e) {

        }

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
            index = 0;
            fragmentHomeMvvm.getProducts(getUserModel());
        }
    }

    public void setImageBitmap() {


        try {

            Picasso.get()
                    .load(productModels.get(index).getPhoto())
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                            /* Save the bitmap or do something with it here */

                            //Set it in the ImageView
                            if (index < productModels.size()) {
                                ProductModel productModel = productModels.get(index);
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 10, stream);
                                productModel.setImageBitmap(stream.toByteArray());
                                productModels.set(index, productModel);
                            }
                            index += 1;
                            if (index == productModels.size()) {
                                try {
                                    accessDatabase.insertProduct(productModels, FragmentHome.this);

                                } catch (Exception e) {

                                }
                            } else {
                                setImageBitmap();
                            }
                        }

                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                            index += 1;
                            if (index == productModels.size()) {
                                try {
                                    accessDatabase.insertProduct(productModels, FragmentHome.this);

                                } catch (Exception ex) {

                                }
                            } else {
                                setImageBitmap();
                            }
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });
        } catch (Exception e) {
            index += 1;
            if (index == productModels.size()) {
                try {
                    accessDatabase.insertProduct(productModels, FragmentHome.this);

                } catch (Exception exception) {

                }
            } else {
                setImageBitmap();
            }
        }
    }

    @Override
    public void onOrderDataInsertedSuccess(long bol) {
        Log.e(";lll", createOrderModels.get(pos).getId() + "");
        List<ItemCartModel> list = createOrderModels.get(pos).getDetails();
        for (int i = 0; i < list.size(); i++) {
            ItemCartModel itemCartModel = list.get(i);
            itemCartModel.setOrder_id(createOrderModels.get(pos).getId());
            list.set(i, itemCartModel);
        }
        if (bol > 0) {
            try {
                accessDatabase.insertOrderProduct(list, this);

            } catch (Exception e) {

            }

        }
    }

    @Override
    public void onProductORderDataInsertedSuccess(Boolean bol) {
        pos++;
        if (pos < createOrderModels.size()) {
            try {
                accessDatabase.insertOrder(createOrderModels.get(pos), this);

            } catch (Exception e) {

            }
        } else {
            index = 0;
            pos = 0;
            binding.progBar.setVisibility(View.GONE);
            binding.nested.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onProductDataSuccess(List<ProductModel> productModelList) {
        pos = 0;
        this.productModelList = productModelList;
        if (productModelList.size() > 0) {
            fragmentHomeMvvm.addproduct(activity, productModelList.get(pos), getUserModel());
        } else {
            try {

                accessDatabase.getallOrder(this);

            } catch (Exception e) {

            }
        }
    }

    @Override
    public void onProductUpdateDataSuccess() {
        Log.e("lllll", "kkkkk");

        pos += 1;
        if (pos < productModelList.size()) {
            fragmentHomeMvvm.addproduct(activity, productModelList.get(pos), getUserModel());
        } else {
            pos = 0;

            Log.e("lllll", "kkkkk");
            try {
                accessDatabase.getallOrder(FragmentHome.this);

            } catch (Exception e) {

            }

        }
    }

    @Override
    public void onAllOrderDataSuccess(List<CreateOrderModel> createOrderModels) {
        pos = 0;
        this.createOrderModels = createOrderModels;
        if (createOrderModels.size() > 0) {
            try {
                accessDatabase.getOrderProduct(this, createOrderModels.get(pos).getId() + "");

            } catch (Exception e) {

            }
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    accessDatabase.clear();

                }
            }).start();

            fragmentHomeMvvm.getDepartment(getUserModel());
        }
    }

    @Override
    public void onAllOrderProductDataSuccess(List<ItemCartModel> itemCartModelList) {
        CreateOrderModel createOrderModel = createOrderModels.get(pos);
        createOrderModel.setDetails(itemCartModelList);
        createOrderModels.set(pos, createOrderModel);
        pos += 1;

        if (pos == createOrderModels.size()) {
            pos = 0;
            uploadOrders();
        } else {
            try {
                accessDatabase.getOrderProduct(this, createOrderModels.get(pos).getId() + "");

            } catch (Exception e) {

            }
        }
    }

    private void uploadOrders() {
        if (pos < createOrderModels.size()) {
            if (createOrderModels.get(pos).getLocal() != null && createOrderModels.get(pos).getLocal().equals("local")) {
                fragmentHomeMvvm.sendOrder(createOrderModels.get(pos), getUserModel());
            } else {
                pos += 1;
                uploadOrders();
            }
        } else {
            pos = 0;
            backOrder();
        }
    }

}