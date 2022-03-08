package com.sooft_sales.preferences;

import android.content.Context;
import android.content.SharedPreferences;


import com.google.gson.Gson;
import com.sooft_sales.model.CreateOrderModel;
import com.sooft_sales.model.UserModel;
import com.sooft_sales.model.UserSettingsModel;
import com.sooft_sales.uis.activity_home.fragments_home_navigaion.FragmentCart;

public class Preferences {

    private static Preferences instance = null;

    private Preferences() {
    }

    public static Preferences getInstance() {
        if (instance == null) {
            instance = new Preferences();
        }
        return instance;
    }
    public UserModel getUserData(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String user_data = preferences.getString("user_data", "");
        UserModel userModel = gson.fromJson(user_data, UserModel.class);
        return userModel;
    }

    public void createUpdateUserData(Context context,UserModel userModel) {
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String user_data = gson.toJson(userModel);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_data",user_data);
        editor.apply();

    }

    public void clearUserData(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

    }
    public void create_update_user_settings(Context context, UserSettingsModel model) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("settings_pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String data = new Gson().toJson(model);
        editor.putString("settings", data);
        editor.apply();


    }

    public UserSettingsModel getUserSettings(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("settings_pref", Context.MODE_PRIVATE);
        UserSettingsModel model = new Gson().fromJson(preferences.getString("settings",""),UserSettingsModel.class);
        return model;

    }



    public void create_room_id(Context context, String room_id) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("room", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("room_id", room_id);
        editor.apply();


    }

    public String getRoom_Id(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("room", Context.MODE_PRIVATE);
        String chat_user_id = preferences.getString("room_id","");
        return chat_user_id;
    }


    public void createUpdateAppSetting(Context context, UserSettingsModel settings) {
        SharedPreferences preferences = context.getSharedPreferences("settingsEbsar", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String data = gson.toJson(settings);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("settings", data);
        editor.apply();
    }
    public UserSettingsModel getAppSetting(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("settingsEbsar", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        return gson.fromJson(preferences.getString("settings",""), UserSettingsModel.class);
    }
    public void create_update_cart_soft(Context context , CreateOrderModel model)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("cart_soft", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String cart_soft_data = gson.toJson(model);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("cart_soft_data", cart_soft_data);
        editor.apply();

    }

    public CreateOrderModel getcart_softData(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("cart_soft", Context.MODE_PRIVATE);
        String json_data = sharedPreferences.getString("cart_soft_data","");
        Gson gson = new Gson();
        CreateOrderModel model = gson.fromJson(json_data, CreateOrderModel.class);
        return model;
    }

    public void clearcart_soft(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("cart_soft", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.clear();
        edit.apply();
    }


}
