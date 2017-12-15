package com.example.integration;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by I328028 on 12/13/2017.
 */

public class GridViewAdapter extends ArrayAdapter<Product> {

    public GridViewAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Product> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;

        if(null == v){
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.grid_item, null);
        }

        Product product = getItem(position);
        ImageView imageView = (ImageView) v.findViewById(R.id.imageView);
        TextView textTitle = (TextView) v.findViewById(R.id.textTitle);
        TextView textDescription = (TextView) v.findViewById(R.id.textDescription);

        imageView.setImageResource(product.getImageId());
        textTitle.setText(product.getTitle());
        textDescription.setText(product.getDescription());


        return v;
    }
}
