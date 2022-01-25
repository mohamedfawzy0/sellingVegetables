package com.sellingvegetables.uis.activity_login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.sellingvegetables.R;
import com.sellingvegetables.databinding.ActivityLoginBinding;
import com.sellingvegetables.uis.activity_base.BaseActivity;
import com.sellingvegetables.uis.activity_home.HomeActivity;

public class LoginActivity extends BaseActivity {
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_login);
        initView();
        
    }

    private void initView() {
binding.btLogin.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        navigateToHomeActivity();
    }
});
    }

    private void navigateToHomeActivity() {
        Intent intent;


        intent = new Intent(this, HomeActivity.class);

        startActivity(intent);
        finish();

    }
}