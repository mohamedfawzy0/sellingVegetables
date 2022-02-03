package com.sellingvegetables.uis.activity_home.fragments_home_navigaion;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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


import com.sellingvegetables.R;

import com.sellingvegetables.adapter.CartAdapter;
import com.sellingvegetables.adapter.ProductAdapter;
import com.sellingvegetables.databinding.FragmentCartBinding;
import com.sellingvegetables.local_database.AccessDatabase;
import com.sellingvegetables.local_database.DataBaseInterfaces;
import com.sellingvegetables.model.CreateOrderModel;
import com.sellingvegetables.model.ItemCartModel;
import com.sellingvegetables.model.UserModel;
import com.sellingvegetables.mvvm.FragmentCartMvvm;
import com.sellingvegetables.preferences.Preferences;
import com.sellingvegetables.share.Common;
import com.sellingvegetables.uis.activity_base.BaseFragment;
import com.sellingvegetables.uis.activity_home.HomeActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class FragmentCart extends BaseFragment implements DataBaseInterfaces.OrderInsertInterface, DataBaseInterfaces.ProductOrderInsertInterface {
    private FragmentCartBinding binding;
    private HomeActivity activity;
    private FragmentCartMvvm fragmentCartMvvm;
    private CartAdapter cartadpter;
    private List<ItemCartModel> list;
    private CreateOrderModel createOrderModel;
    private double total, tax, discount;
    private Preferences preferences;
    private UserModel userModel;
    private AccessDatabase accessDatabase;
    private ProgressDialog dialog;
    private int pos;

    public static FragmentCart newInstance() {
        FragmentCart fragment = new FragmentCart();
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

    }

    private void initView() {
        list = new ArrayList<>();
        fragmentCartMvvm = ViewModelProviders.of(this).get(FragmentCartMvvm.class);
        preferences = Preferences.getInstance();
        accessDatabase = new AccessDatabase(activity);
        dialog = Common.createProgressDialog(activity, activity.getResources().getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        userModel = preferences.getUserData(activity);
        createOrderModel = preferences.getcart_olivaData(activity);
        cartadpter = new CartAdapter(activity);
        binding.recviewcart.setLayoutManager(new GridLayoutManager(activity, 1));
        binding.recviewcart.setAdapter(cartadpter);
        updateUi();
        binding.edTax.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                tax = (total * (Double.parseDouble(editable.toString())));
                calculateTotal();
            }
        });
        binding.edDiscount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                discount = ((total + tax) * (Double.parseDouble(editable.toString())));
                calculateTotal();
            }
        });
        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createOrderModel.setDate(System.currentTimeMillis());
                createOrderModel.setId(Math.random());
                dialog.show();
                accessDatabase.insertOrder(createOrderModel, FragmentCart.this);

            }
        });
    }


    private void updateUi() {
        list.clear();
        if (createOrderModel != null) {
            list.addAll(createOrderModel.getDetails());
            cartadpter.notifyDataSetChanged();
//            binding.llEmptyCart.setVisibility(View.GONE);
            calculateTotal();

        } else {
//            binding.llEmptyCart.setVisibility(View.VISIBLE);
//            binding.fltotal.setVisibility(View.GONE);

        }
    }

    private void calculateTotal() {

        for (ItemCartModel model : list) {

            total += model.getTotal();
        }
        binding.tvDiscount.setText(discount + "");
        binding.tvTax.setText(discount + "");
        binding.tvTotal.setText(total + "");
        binding.tvTotal2.setText((total + tax - discount) + "");
        createOrderModel.setTotal(total);
        createOrderModel.setTax(tax);
        createOrderModel.setDiscount(discount);

    }


    @Override
    public void onOrderDataInsertedSuccess(long bol) {
        for (int i = 0; i < list.size(); i++) {
            ItemCartModel itemCartModel = list.get(i);
            itemCartModel.setCreate_id((int) bol);
            list.set(i, itemCartModel);
        }
        createOrderModel.setDetails(list);
        if (bol > 0) {
            accessDatabase.insertOrderProduct(createOrderModel.getDetails(), this);

        }
    }

    @Override
    public void onProductORderDataInsertedSuccess(Boolean bol) {
        dialog.dismiss();
        list.clear();
        cartadpter.notifyDataSetChanged();
        preferences.clearcart_oliva(activity);
        updateUi();
        Log.e("lllll",bol+"");
    }
}