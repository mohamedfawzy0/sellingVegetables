package com.ragm_sales.local_database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


import com.ragm_sales.model.CreateOrderModel;
import com.ragm_sales.model.DepartmentModel;
import com.ragm_sales.model.ItemCartModel;
import com.ragm_sales.model.ProductModel;
import com.ragm_sales.tags.Tags;

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
    ProductModel getlastProduct();
    @Insert(entity = ProductModel.class, onConflict = OnConflictStrategy.IGNORE)
    long insertProductData(ProductModel retrieveModel);
    @Insert(entity = ItemCartModel.class, onConflict = OnConflictStrategy.IGNORE)
    long[] insertOrderProducts(List<ItemCartModel> itemCartModelList);
    @Insert(entity = CreateOrderModel.class, onConflict = OnConflictStrategy.IGNORE)
    long insertOrderData(CreateOrderModel createOrderModel);
    @Query("SELECT * FROM " + Tags.table_order + " where id=:id")
    CreateOrderModel search(double id);
    @Query("Update " + Tags.table_order + " set  is_back=:is_back where id=:id")
    int updateOrder(double id, boolean is_back);

    @Query("Update " + Tags.table_products + " set  id=:newid where id=:id")
    int updateProduct(double id, double newid);
    @Query("SELECT * FROM " + Tags.table_order +" where local=:local ")
    List<CreateOrderModel> getallOrders(String local);
    @Query("SELECT * FROM " + Tags.table_order_products + " where order_id=:id   ")
    List<ItemCartModel> getOrderProducts(double id);

}
