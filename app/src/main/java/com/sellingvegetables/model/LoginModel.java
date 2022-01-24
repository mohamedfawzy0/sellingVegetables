package com.sellingvegetables.model;



import androidx.databinding.BaseObservable;

import androidx.databinding.ObservableField;



import java.io.Serializable;

public class LoginModel extends BaseObservable implements Serializable {
    private String phone_code;
    private String phone;
    public ObservableField<String> error_phone = new ObservableField<>();

    public LoginModel() {
        phone_code ="+20";
        phone ="";
    }


}