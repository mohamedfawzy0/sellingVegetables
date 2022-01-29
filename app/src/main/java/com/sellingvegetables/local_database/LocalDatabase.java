package com.sellingvegetables.local_database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.sellingvegetables.model.DepartmentModel;
import com.sellingvegetables.model.ProductModel;
import com.sellingvegetables.tags.Tags;

@Database(version = 1, entities = {DepartmentModel.class, ProductModel.class}, exportSchema = false)
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
