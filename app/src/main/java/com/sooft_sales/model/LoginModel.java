package com.sooft_sales.model;

import android.content.Context;
import android.util.Log;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.sooft_sales.BR;
import com.sooft_sales.R;

import java.io.Serializable;

public class LoginModel extends BaseObservable implements Serializable {
    private String username;
    private String password;
    private String lang;
    public ObservableField<String> error_user_name = new ObservableField<>();
    public ObservableField<String> error_password = new ObservableField<>();

    public LoginModel() {
        username = "";
        password = "";
        lang="";
    }

    public boolean isDataValid(Context context) {
        if (!username.isEmpty() &&
                !password.isEmpty()) {
            error_user_name.set(null);
            error_password.set(null);


            return true;
        } else {

            if (username.isEmpty()) {
                error_user_name.set(context.getString(R.string.field_required));

            } else {
                error_user_name.set(null);

            }

            if (password.isEmpty()) {
                error_password.set(context.getString(R.string.field_required));

            }  else {
                error_password.set(null);

            }


            return false;
        }
    }

    @Bindable
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        notifyPropertyChanged(BR.username);
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);

    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}