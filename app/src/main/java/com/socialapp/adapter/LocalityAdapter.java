package com.socialapp.adapter;

import android.content.Context;
import android.location.Address;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.socialapp.R;

public class LocalityAdapter extends ArrayAdapter<Address>
{
    private Context context;
    private Address[] categories;
    public LocalityAdapter(Context context, int resource, Address[] objects) {
        super(context, resource, objects);
        this.context=context;
        categories=objects;
    }
    public View getDropDownView(int position, View view, ViewGroup parent)
    {
        return getCustomView(position, view, parent);
    }
     public View getView(int pos, View view, ViewGroup parent)
     {
         return getCustomView(pos, view, parent);
     }

    public View getCustomView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View spinner = inflater.inflate(R.layout.category_adapter_layout, parent, false);
        TextView main_text = (TextView) spinner .findViewById(R.id.category_name);
        main_text.setText(categories[position].getLocality());
        return spinner;
    }

}
