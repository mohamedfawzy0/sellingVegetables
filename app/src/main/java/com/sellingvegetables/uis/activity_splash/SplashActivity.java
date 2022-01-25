package com.sellingvegetables.uis.activity_splash;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.sellingvegetables.R;
import com.sellingvegetables.databinding.ActivitySplashBinding;
import com.sellingvegetables.preferences.Preferences;
import com.sellingvegetables.uis.activity_base.BaseActivity;
import com.sellingvegetables.uis.activity_home.HomeActivity;
import com.sellingvegetables.uis.activity_login.LoginActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SplashActivity extends BaseActivity {
    private ActivitySplashBinding binding;
    private CompositeDisposable disposable = new CompositeDisposable();
    private Animation animation;

    private Preferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        initView();

    }

    private void initView() {
        preferences = Preferences.getInstance();
        Observable.timer(2, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull Long aLong) {
                        animateMethod();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    private void animateMethod() {
        animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.lanuch);


        binding.cons.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (getUserModel() != null) {
                    navigateToLoginActivity();
                } else {
                    navigateToHomeActivity();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }

    private void navigateToLoginActivity() {
        Intent intent;


        intent = new Intent(this, LoginActivity.class);

        startActivity(intent);
        finish();
    }


    private void navigateToHomeActivity() {
        Intent intent;


        intent = new Intent(this, HomeActivity.class);

        startActivity(intent);
        finish();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }
}