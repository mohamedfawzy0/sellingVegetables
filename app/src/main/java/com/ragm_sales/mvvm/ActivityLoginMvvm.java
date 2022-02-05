package com.ragm_sales.mvvm;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ragm_sales.R;
import com.ragm_sales.model.LoginModel;
import com.ragm_sales.model.UserModel;
import com.ragm_sales.remote.Api;
import com.ragm_sales.share.Common;
import com.ragm_sales.tags.Tags;

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
                    if (userModelResponse.body().getCode() == 200&&userModelResponse.body().getData()!=null) {

                        onLoginSuccess.setValue(userModelResponse.body());
                    }
                    else {
                        Toast.makeText(context, context.getResources().getString(R.string.incorrect),Toast.LENGTH_LONG).show();
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
