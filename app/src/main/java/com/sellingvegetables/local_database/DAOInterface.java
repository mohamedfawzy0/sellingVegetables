package com.sellingvegetables.local_database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


import com.sellingvegetables.model.DepartmentModel;
import com.sellingvegetables.model.ProductModel;
import com.sellingvegetables.tags.Tags;

import java.util.List;

@Dao
public interface DAOInterface {



    @Insert(entity = DepartmentModel.class, onConflict = OnConflictStrategy.IGNORE)
    long[] insertCategoryData(List<DepartmentModel> retrieveModel);
    @Insert(entity = ProductModel.class, onConflict = OnConflictStrategy.IGNORE)
    long[] insertProductData(List<ProductModel> retrieveModel);

    @Query("SELECT * FROM " + Tags.table_category)
    List<DepartmentModel> getCategory();



    @Query("SELECT * FROM " + Tags.table_products + " where category_id=:id")
    List<ProductModel> getProductByCategory(String id);
    @Query("SELECT * FROM " + Tags.table_products + " where type=:type")
    List<ProductModel> getLocalProduct(String type);
    @Query("SELECT * FROM " + Tags.table_products +" where id=(SELECT MAX(id) from products)")
    ProductModel getlastOrder();
    @Insert(entity = ProductModel.class, onConflict = OnConflictStrategy.IGNORE)
    long insertProductData(ProductModel retrieveModel);







}
