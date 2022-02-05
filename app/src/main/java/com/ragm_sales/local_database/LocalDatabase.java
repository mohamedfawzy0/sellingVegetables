package com.ragm_sales.local_database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.ragm_sales.model.CreateOrderModel;
import com.ragm_sales.model.DepartmentModel;
import com.ragm_sales.model.ItemCartModel;
import com.ragm_sales.model.ProductModel;
import com.ragm_sales.tags.Tags;

@Database(version = 1, entities = {DepartmentModel.class, ProductModel.class, CreateOrderModel.class, ItemCartModel.class}, exportSchema = false)
public abstract class LocalDatabase extends RoomDatabase {

    public static volatile LocalDatabase instance = null;

    public abstract DAOInterface daoInterface();


    public static LocalDatabase newInstance(Context context) {
        if (instance == null) {
            synchronized (LocalDatabase.class) {
                instance = Room.databaseBuilder(context.getApplicationContext(), LocalDatabase.class, Tags.DATABASE_NAME)
                        .build();
            }
        }
        return instance;
    }
}
