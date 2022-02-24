package com.sooft_sales.uis.activity_invoice;

import static android.content.ContentValues.TAG;
import static android.os.Build.VERSION_CODES.KITKAT;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ahmedelsayed.sunmiprinterutill.PrintMe;
import com.google.android.exoplayer2.util.Log;

import com.sooft_sales.R;
import com.sooft_sales.adapter.ProductBillAdapter;
import com.sooft_sales.databinding.ActivityInvoiceBinding;
import com.sooft_sales.language.Language;
import com.sooft_sales.model.CreateOrderModel;
import com.sooft_sales.model.ItemCartModel;
import com.sooft_sales.model.UserModel;
import com.sooft_sales.model.ZatcaQRCodeGeneration;
import com.sooft_sales.preferences.Preferences;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import io.paperdb.Paper;
import pl.allegro.finance.tradukisto.ValueConverters;

public class InvoiceActivity extends AppCompatActivity {
    private ActivityInvoiceBinding binding;
    private Preferences preferences;
    private String lang;
    private CreateOrderModel createOrderModel;
    private UserModel userModel;
    private List<ItemCartModel> limsProductSaleDataList;
    private ProductBillAdapter productBillAdapter;
    private final String write_perm = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final int write_req = 100;

    //    private final String bluthoos_perm = Manifest.permission.BLUETOOTH;
//    private final String bluthoosadmin_perm = Manifest.permission.BLUETOOTH_ADMIN;
//
//    private final int bluthoos_req = 200;
//
    private boolean isPermissionGranted = false;
    private Context context;
    private SimpleDateFormat dateFormat;

    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        this.context = newBase;
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_invoice);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        createOrderModel = (CreateOrderModel) intent.getSerializableExtra("data");
    }

    private void checkWritePermission() {

        if (ContextCompat.checkSelfPermission(this, write_perm) != PackageManager.PERMISSION_GRANTED) {


            isPermissionGranted = false;

            ActivityCompat.requestPermissions(this, new String[]{write_perm}, write_req);


        } else {
            isPermissionGranted = true;
takeScreenshot(2);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == write_req && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        takeScreenshot(2);
        }
    }


    private void initView() {
        limsProductSaleDataList = new ArrayList<>();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        productBillAdapter = new ProductBillAdapter(limsProductSaleDataList, this);
        binding.recView.setNestedScrollingEnabled(false);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        binding.recView.setAdapter(productBillAdapter);
        // getlastInvoice();
        binding.btnConfirm3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PrintMe printMe =  new PrintMe(InvoiceActivity.this);

                // Print a text

                // Print an image
                printMe.sendViewToPrinter(binding.scrollView);
            }
        });
        updateData();
    }

    private void updateData() {
//        ValueConverters converter;
//        converter = ValueConverters.ENGLISH_INTEGER;
//        String valueAsWords = converter.asWords((int) (createOrderModel.getTotal() + createOrderModel.getTax() - createOrderModel.getDiscount()));
//        binding.tvTotal.setText(valueAsWords);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
        binding.tvTime.setText(dateFormat.format(new Date(createOrderModel.getOrder_date_time())));

        ZatcaQRCodeGeneration.Builder builder = new ZatcaQRCodeGeneration.Builder();
        builder.sellerName(userModel.getData().getUser().getName()) // Shawrma House
                .taxNumber("123456789012345") // 1234567890
                .invoiceDate(dateFormat.format(new Date(createOrderModel.getOrder_date_time()))) //..> 22/11/2021 03:00 am
                .totalAmount((createOrderModel.getTotal() + createOrderModel.getTax() - createOrderModel.getDiscount()) + "") // 100
                .taxAmount(createOrderModel.getTax() + "");
        binding.setModel(createOrderModel);
        binding.setUsermodel(userModel);

        binding.setImage(builder.getBase64());

        if (createOrderModel.getDetails() != null && createOrderModel.getDetails().size() > 0) {
            limsProductSaleDataList.addAll(createOrderModel.getDetails());
            productBillAdapter.notifyDataSetChanged();
            Log.e("dkdkdk", limsProductSaleDataList.size() + "");

//      if(limsProductSaleDataList.size()>3){
//          LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, 100);
//          lp.weight = 1;
//          binding.fl.setLayoutParams(lp);
//
//      }
        }
    }

//    public void getlastInvoice() {
//        Log.e("kdkkd", salid);
//        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
//        dialog.setCancelable(false);
//        dialog.show();
//
//
//        Api.getService(Tags.base_url)
//                .getInv(salid, userModel.getUser().getId() + "")
//                .enqueue(new Callback<InvoiceDataModel>() {
//                    @Override
//                    public void onResponse(Call<InvoiceDataModel> call, Response<InvoiceDataModel> response) {
//                        dialog.dismiss();
//                        if (response.isSuccessful() && response.body() != null) {
//                            if (response.body().getStatus() == 200) {
//                                if (response.body() != null) {
//                                    updateData(response.body());
//
////                                    Intent intent = new Intent(HomeActivity.this, InvoiceActivity.class);
////                                    intent.putExtra("data", response.body().getData());
////                                    startActivity(intent);
//                                } else if (response.body().getStatus() == 400) {
//                                    Toast.makeText(InvoiceActivity.this, getResources().getString(R.string.no_invoice), Toast.LENGTH_SHORT).show();
//
//                                }
//
//                            }
//
//                        } else {
//                            if (response.code() == 500) {
//                                Toast.makeText(InvoiceActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Log.e("ERROR", response.message() + "");
//
//                                //     Toast.makeText(HomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
//                            }
//
//
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<InvoiceDataModel> call, Throwable t) {
//                        try {
//                            dialog.dismiss();
//                            if (t.getMessage() != null) {
//                                Log.e("msg_category_error", t.getMessage() + "__");
//
//                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
//                                    // Toast.makeText(SubscriptionActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
//                                } else {
//                                    //Toast.makeText(SubscriptionActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        } catch (Exception e) {
//                            Log.e("Error", e.getMessage() + "__");
//                        }
//                    }
//                });
//    }


    private void takeScreenshot(int mode) {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
                mPath = getExternalFilesDir(Environment.DIRECTORY_DCIM) + "/" + now + ".jpeg";
            } else {
                mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpeg";
            }
            Log.e("kdkkdkd", mPath);
            // create bitmap screen capture
            NestedScrollView v1 = getWindow().getDecorView().findViewById(R.id.scrollView);
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = getBitmapFromView(v1, v1.getChildAt(0).getHeight(), v1.getChildAt(0).getWidth());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            PrintMe printMe =  new PrintMe(this);

            // Print a text

            // Print an image
            printMe.sendImageToPrinter(bitmap);
            //setting screenshot in imageview
            //android.util.Log.e("ddlldld", filePath);


            //sendData(filePath);
            //printPhoto(FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider",new File(filePath)));

//   Bitmap ssbitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());

        } catch (Exception e) {
            // Several error may come out with file handling or DOM
            android.util.Log.e("ddlldld", e.toString());
        }
    }

    private Bitmap getBitmapFromView(View view, int height, int width) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return bitmap;
    }


}
