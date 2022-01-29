package com.sellingvegetables.mvvm;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.sellingvegetables.model.DepartmentDataModel;
import com.sellingvegetables.model.DepartmentModel;
import com.sellingvegetables.model.ProductDataModel;
import com.sellingvegetables.model.ProductModel;
import com.sellingvegetables.model.SingleProductDataModel;
import com.sellingvegetables.model.UserModel;
import com.sellingvegetables.remote.Api;
import com.sellingvegetables.share.Common;
import com.sellingvegetables.tags.Tags;

import java.io.ByteArrayOutputStream;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

public class FragmentHomeMvvm extends AndroidViewModel {
    private static final String TAG = "FragmentHomeMvvm";
    private Context context;

    private CompositeDisposable disposable = new CompositeDisposable();
    private MutableLiveData<List<DepartmentModel>> departmentLivData;
    private MutableLiveData<List<ProductModel>> listMutableLiveData;
    private MutableLiveData<ProductModel> productMutableLiveData;



    public FragmentHomeMvvm(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public MutableLiveData<List<ProductModel>> getProductList() {
        if (listMutableLiveData == null) {
            listMutableLiveData = new MutableLiveData<>();
        }
        return listMutableLiveData;
    }

    public MutableLiveData<ProductModel> getProduct() {
        if (productMutableLiveData == null) {
            productMutableLiveData = new MutableLiveData<>();
        }
        return productMutableLiveData;
    }




    public LiveData<List<DepartmentModel>> getCategoryData() {
        if (departmentLivData == null) {
            departmentLivData = new MutableLiveData<>();

        }
        return departmentLivData;
    }




    public void getDepartment(UserModel userModel) {
        Api.getService(Tags.base_url)
                .getDepartments(userModel.getData().getAccess_token())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe(new SingleObserver<Response<DepartmentDataModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<DepartmentDataModel> response) {

                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getCode() == 200) {
                                List<DepartmentModel> list = response.body().getData();
                                if (list.size() > 0) {
                                    departmentLivData.setValue(list);
                                }


                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "onError: ", e);
                    }
                });

    }

    public void getProducts(UserModel userModel) {


        Api.getService(Tags.base_url)
                .getProducts(userModel.getData().getAccess_token())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<ProductDataModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }
                    public void onSuccess(@NonNull Response<ProductDataModel> response) {
                        Log.e("dddd",response.code()+"");

                        if (response.isSuccessful() && response.body() != null) {
                            Log.e("dddd",response.body().getCode()+"");
                            if (response.body().getCode() == 200) {
                                // List<ProductModel> list = response.body().getData();
                                listMutableLiveData.setValue(response.body().getData());
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
Log.e("kkkk",e.toString());
                    }
                });
    }
    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
    public void addproduct(Context context, ProductModel model,UserModel userModel) {
        RequestBody ar_part,en_part;
       if(userModel.getData().getUser().getLang().equals("ar")){
            ar_part = Common.getRequestBodyText(model.getTitle());
         en_part = Common.getRequestBodyText(model.getTitle2());}
       else{
           en_part = Common.getRequestBodyText(model.getTitle());
           ar_part = Common.getRequestBodyText(model.getTitle2());
       }
        RequestBody cat_part = Common.getRequestBodyText(model.getCategory_id()+"");


        MultipartBody.Part image = Common.getMultiPart(context, getUriFromBitmap(model.getImageBitmap()), "logo");


        Api.getService(Tags.base_url).addPeoduct(userModel.getData().getAccess_token(),ar_part, en_part, cat_part,  image).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).unsubscribeOn(Schedulers.io()).subscribe(new SingleObserver<Response<SingleProductDataModel>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposable.add(d);
            }

            @Override
            public void onSuccess(@NonNull Response<SingleProductDataModel> singleProductDataModelResponse) {
                if (singleProductDataModelResponse.isSuccessful()) {
                    if (singleProductDataModelResponse.body().getCode() == 200) {

                        productMutableLiveData.postValue(singleProductDataModelResponse.body().getData());
                    }
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }
        });

    }

    private Uri getUriFromBitmap(byte[] endPoint) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(endPoint, 0, endPoint.length);

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        return Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "", ""));
    }

}
