package com.invofreaks.www.boostup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by goura on 06-10-2015.
 */
public class NavListCustomAdapter extends ArrayAdapter<String> {

    private Context context;
    private String[] titles;
    int [] images;

    public NavListCustomAdapter(Context context, int[] images, String[] titles) {
        super(context,R.layout.nav_list_layout ,titles);

        this.context = context;
        this.titles = titles;
        this.images = images;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.nav_list_layout, parent,false);

        TextView textViewNav = (TextView) rowView.findViewById(R.id.textView);
        ImageView imageViewNav = (ImageView) rowView.findViewById(R.id.imageView);

        imageViewNav.setImageResource(images[position]);
        textViewNav.setText(titles[position]);


        return rowView;

    }
}
