package com.sellingvegetables.mvvm;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.sellingvegetables.R;
import com.sellingvegetables.model.LoginModel;
import com.sellingvegetables.model.UserModel;
import com.sellingvegetables.remote.Api;
import com.sellingvegetables.share.Common;
import com.sellingvegetables.tags.Tags;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class ActivityLoginMvvm extends AndroidViewModel {
    private static final String TAG = "ActivityLoginMvvm";
    private MutableLiveData<UserModel> onLoginSuccess = new MutableLiveData<>();
    private CompositeDisposable disposable = new CompositeDisposable();

    public ActivityLoginMvvm(@NonNull Application application) {
        super(application);


    }

    public LiveData<UserModel> onLoginSuccess(){
        if (onLoginSuccess==null){
            onLoginSuccess = new MutableLiveData<>();
        }
        return onLoginSuccess;
    }


    public void login(Context context, LoginModel model) {
        ProgressDialog dialog = Common.createProgressDialog(context, context.getResources().getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url).login(model.getUsername(),model.getLang(), model.getPassword())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<UserModel>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposable.add(d);
            }

            @Override
            public void onSuccess(@NonNull Response<UserModel> userModelResponse) {
                dialog.dismiss();
Log.e("jjjj",userModelResponse.code()+"");
                if (userModelResponse.isSuccessful()) {
                    if (userModelResponse.body().getCode() == 200) {

                        onLoginSuccess.setValue(userModelResponse.body());
                    }
                }

            }

            @Override
            public void onError(@NonNull Throwable e) {
                dialog.dismiss();
                Log.e("jjjj",e.toString()+"");

            }
        });
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
