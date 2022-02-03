package com.sellingvegetables.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


import com.sellingvegetables.tags.Tags;

import java.io.Serializable;
import java.util.List;

@Entity(tableName = Tags.table_order

)
public class CreateOrderModel implements Serializable {
    @PrimaryKey
    double id;
    private String customer_name;
    private double total;
    private double discount;
    private double tax;
    private long date;
    @Ignore
    private List<ItemCartModel> details;

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public List<ItemCartModel> getDetails() {
        return details;
    }

    public void setDetails(List<ItemCartModel> details) {
        this.details = details;
    }
}
