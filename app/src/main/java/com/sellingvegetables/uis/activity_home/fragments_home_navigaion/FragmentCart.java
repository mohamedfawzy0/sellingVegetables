package com.sellingvegetables.uis.activity_home.fragments_home_navigaion;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.sellingvegetables.mvvm.FragmentCartMvvm;
import com.sellingvegetables.uis.activity_base.BaseFragment;
import com.sellingvegetables.uis.activity_home.HomeActivity;

import java.util.List;


public class FragmentCart extends BaseFragment {
    private FragmentCartBinding binding;
    private HomeActivity activity;
    private FragmentCartMvvm fragmentCartMvvm;
    private CartAdapter cartadpter;

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
        fragmentCartMvvm = ViewModelProviders.of(this).get(FragmentCartMvvm.class);

        cartadpter=new CartAdapter(activity);
        binding.recviewcart.setLayoutManager(new GridLayoutManager(activity,1));
        binding.recviewcart.setAdapter(cartadpter);

    }




}