package com.sellingvegetables.local_database;



import com.sellingvegetables.model.DepartmentModel;
import com.sellingvegetables.model.ProductModel;

import java.util.List;

public class DataBaseInterfaces {


    public interface ProductInsertInterface {
        void onProductDataInsertedSuccess(Boolean bol);
    }

    public interface CategoryInsertInterface {
        void onCategoryDataInsertedSuccess(Boolean bol);
    }

    public interface CategoryInterface {
        void onCategoryDataSuccess(List<DepartmentModel> categoryModelList);
    }

    public interface ProductInterface {
        void onProductDataSuccess(List<ProductModel> productModelList);
    }
    public interface LastProductInterface {
        void onLastProductDataSuccess(ProductModel productModel);
    }

}
