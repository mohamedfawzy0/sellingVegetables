package com.sellingvegetables.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.sellingvegetables.R;
import com.sellingvegetables.databinding.CartItemRowBinding;
import com.sellingvegetables.databinding.ProductItemRowBinding;
import com.sellingvegetables.model.ItemCartModel;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<ItemCartModel> list;
    private Context context;
    private LayoutInflater inflater;

    public CartAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CartItemRowBinding binding= DataBindingUtil.inflate(inflater, R.layout.cart_item_row,parent,false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder=(MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        } else {
            return 0;
        }
    }

    public void updateList(List<ItemCartModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public CartItemRowBinding binding;

        public MyHolder(CartItemRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}
