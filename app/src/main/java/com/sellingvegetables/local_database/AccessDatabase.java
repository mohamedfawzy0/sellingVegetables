package com.sellingvegetables.local_database;

import android.content.Context;
import android.os.AsyncTask;


import com.sellingvegetables.model.DepartmentModel;
import com.sellingvegetables.model.ProductModel;

import java.util.List;

public class AccessDatabase {
    private LocalDatabase localDatabase;
    private DAOInterface daoInterface;

    public AccessDatabase(Context context) {
        localDatabase = LocalDatabase.newInstance(context);
        daoInterface = localDatabase.daoInterface();
    }

    public void clear() {
        localDatabase.clearAllTables();
    }

    public void insertCategory(List<DepartmentModel> departmentModelList, DataBaseInterfaces.CategoryInsertInterface retrieveInsertInterface) {
        new InsertCategoryTask(retrieveInsertInterface).execute(departmentModelList);
    }

    public void insertProduct(List<ProductModel> productModelList, DataBaseInterfaces.ProductInsertInterface retrieveInsertInterface) {
        new InsertProductTask(retrieveInsertInterface).execute(productModelList);
    }

    public void getCategory(DataBaseInterfaces.CategoryInterface categoryInterface) {
        new CategoryTask(categoryInterface).execute();
    }


    public void getProduct(DataBaseInterfaces.ProductInterface productInterface, String id) {
        new ProductTask(productInterface).execute(id);
    }
    public void getLocalProduct(DataBaseInterfaces.ProductInterface productInterface, String type) {
        new LocalroductTask(productInterface).execute(type);
    }
    public void getlastProduct(DataBaseInterfaces.LastProductInterface productInterface) {
        new LastProductTask(productInterface).execute();
    }
    public void insertSingleProduct(ProductModel productModel, DataBaseInterfaces.ProductInsertInterface retrieveInsertInterface) {
        new InsertSingleProductTask(retrieveInsertInterface).execute(productModel);
    }

    public class InsertCategoryTask extends AsyncTask<List<DepartmentModel>, Void, Boolean> {
        private DataBaseInterfaces.CategoryInsertInterface retrieveInsertInterface;

        public InsertCategoryTask(DataBaseInterfaces.CategoryInsertInterface retrieveInsertInterface) {
            this.retrieveInsertInterface = retrieveInsertInterface;
        }

        @Override
        protected Boolean doInBackground(List<DepartmentModel>... lists) {
            boolean isInserted = false;
            long[] data = daoInterface.insertCategoryData(lists[0]);
            if (data != null && data.length > 0) {
                isInserted = true;
            }
            return isInserted;
        }

        @Override
        protected void onPostExecute(Boolean bol) {

            retrieveInsertInterface.onCategoryDataInsertedSuccess(bol);

        }
    }

    public class InsertProductTask extends AsyncTask<List<ProductModel>, Void, Boolean> {
        private DataBaseInterfaces.ProductInsertInterface retrieveInsertInterface;

        public InsertProductTask(DataBaseInterfaces.ProductInsertInterface retrieveInsertInterface) {
            this.retrieveInsertInterface = retrieveInsertInterface;
        }

        @Override
        protected Boolean doInBackground(List<ProductModel>... lists) {
            boolean isInserted = false;
            long[] data = daoInterface.insertProductData(lists[0]);
            if (data != null && data.length > 0) {
                isInserted = true;
            }
            return isInserted;
        }

        @Override
        protected void onPostExecute(Boolean bol) {

            retrieveInsertInterface.onProductDataInsertedSuccess(bol);

        }
    }

    public class CategoryTask extends AsyncTask<Void, Void, List<DepartmentModel>> {
        private DataBaseInterfaces.CategoryInterface categoryInterface;

        public CategoryTask(DataBaseInterfaces.CategoryInterface categoryInterface) {
            this.categoryInterface = categoryInterface;
        }

        @Override
        protected List<DepartmentModel> doInBackground(Void... voids) {
            return daoInterface.getCategory();
        }

        @Override
        protected void onPostExecute(List<DepartmentModel> DepartmentModelList) {
            categoryInterface.onCategoryDataSuccess(DepartmentModelList);
        }
    }

    public class ProductTask extends AsyncTask<String, Void, List<ProductModel>> {
        private DataBaseInterfaces.ProductInterface productInterface;

        public ProductTask(DataBaseInterfaces.ProductInterface productInterface) {
            this.productInterface = productInterface;
        }

        @Override
        protected List<ProductModel> doInBackground(String... strings) {


            return daoInterface.getProductByCategory(strings[0]);

        }

        @Override
        protected void onPostExecute(List<ProductModel> productModelList) {

            productInterface.onProductDataSuccess(productModelList);
        }

    }
    public class LocalroductTask extends AsyncTask<String, Void, List<ProductModel>> {
        private DataBaseInterfaces.ProductInterface productInterface;

        public LocalroductTask(DataBaseInterfaces.ProductInterface productInterface) {
            this.productInterface = productInterface;
        }

        @Override
        protected List<ProductModel> doInBackground(String... strings) {


            return daoInterface.getLocalProduct(strings[0]);

        }

        @Override
        protected void onPostExecute(List<ProductModel> productModelList) {

            productInterface.onProductDataSuccess(productModelList);
        }

    }


    //
    public class LastProductTask extends AsyncTask<String, Void, ProductModel> {
        private DataBaseInterfaces.LastProductInterface lastProductInterface;

        public LastProductTask(DataBaseInterfaces.LastProductInterface lastProductInterface) {
            this.lastProductInterface = lastProductInterface;
        }

        @Override
        protected ProductModel doInBackground(String... strings) {


            return daoInterface.getlastOrder();

        }

        @Override
        protected void onPostExecute(ProductModel productModel) {

            lastProductInterface.onLastProductDataSuccess(productModel);
        }

    }
    public class InsertSingleProductTask extends AsyncTask<ProductModel, Void, Boolean> {
        private DataBaseInterfaces.ProductInsertInterface retrieveInsertInterface;

        public InsertSingleProductTask(DataBaseInterfaces.ProductInsertInterface retrieveInsertInterface) {
            this.retrieveInsertInterface = retrieveInsertInterface;
        }

        @Override
        protected Boolean doInBackground(ProductModel... lists) {
            boolean isInserted = false;
            long data = daoInterface.insertProductData(lists[0]);
            if ( data > 0) {
                isInserted = true;
            }
            return isInserted;
        }

        @Override
        protected void onPostExecute(Boolean bol) {

            retrieveInsertInterface.onProductDataInsertedSuccess(bol);

        }
    }

}
