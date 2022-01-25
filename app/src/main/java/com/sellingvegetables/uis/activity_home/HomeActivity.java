package com.sellingvegetables.uis.activity_home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.result.ActivityResultLauncher;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.sellingvegetables.R;
import com.sellingvegetables.databinding.ActivityHomeBinding;
import com.sellingvegetables.interfaces.Listeners;
import com.sellingvegetables.language.Language;
import com.sellingvegetables.model.UserModel;
import com.sellingvegetables.mvvm.HomeActivityMvvm;
import com.sellingvegetables.preferences.Preferences;
import com.sellingvegetables.uis.activity_base.BaseActivity;
import com.sellingvegetables.uis.activity_login.LoginActivity;

import io.paperdb.Paper;

public class HomeActivity extends BaseActivity  {
    private ActivityHomeBinding binding;
    private NavController navController;
    private HomeActivityMvvm homeActivityMvvm;
    private Preferences preferences;
    private UserModel userModel;
    private ActivityResultLauncher<Intent> launcher;
    private int req = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        initView();


    }

    private void initView() {

        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);

        homeActivityMvvm = ViewModelProviders.of(this).get(HomeActivityMvvm.class);


        navController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupWithNavController(binding.bottomNav, navController);
       // NavigationUI.setupActionBarWithNavController(this, navController);


//        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
//            if (binding.toolBar.getNavigationIcon() != null) {
//                binding.toolBar.getNavigationIcon().setColorFilter(ContextCompat.getColor(HomeActivity.this, R.color.black), PorterDuff.Mode.SRC_ATOP);
//
//            }
//        });


//
//        toggle.setHomeAsUpIndicator(R.drawable.ic_menu);









    }





    @Override
    public void onBackPressed() {
        int currentFragmentId = navController.getCurrentDestination().getId();
        if (currentFragmentId == R.id.home) {
            finish();

        }  else {
            navController.popBackStack();
        }

    }




}
