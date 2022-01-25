package com.sellingvegetables.mvvm;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.sellingvegetables.model.UserModel;

import io.reactivex.disposables.CompositeDisposable;

public class ActivityLoginMvvm extends AndroidViewModel {
    private Context context;
    private FirebaseAuth mAuth;

    public MutableLiveData<UserModel> userModelMutableLiveData = new MutableLiveData<>();
    private CompositeDisposable disposable = new CompositeDisposable();
    public ActivityLoginMvvm(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        mAuth = FirebaseAuth.getInstance();
    }
    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
