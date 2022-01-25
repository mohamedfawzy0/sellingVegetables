package com.sellingvegetables.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.sellingvegetables.R;
import com.sellingvegetables.databinding.CategoryItemRowBinding;

import java.util.List;

public class ProductCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<Object> list;
    private Context context;
    private LayoutInflater inflater;

    public ProductCategoryAdapter( Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CategoryItemRowBinding binding= DataBindingUtil.inflate(inflater, R.layout.category_item_row,parent,false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder=(MyHolder) holder;
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();

        } else {
            return 3;
        }
    }
    public void updateList(List<Object> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public CategoryItemRowBinding binding;

        public MyHolder(CategoryItemRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}
