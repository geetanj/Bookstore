package com.example.admin.bookstore;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Admin on 2017-10-13.
 */

public class SpinAdapter extends BaseAdapter{ Context c;
    LayoutInflater layoutInflater;

    public SpinAdapter(Context c, ArrayList<String> catnm) {
        this.c = c;
        this.catnm = catnm;
    }

    ArrayList catnm;
    @Override
    public int getCount() {
        return catnm.size();
    }

    @Override
    public Object getItem(int i) {
        return catnm.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        if (layoutInflater ==null){
            layoutInflater=(LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }
        if(view==null)
        {
            view=layoutInflater.inflate(R.layout.conntentlist,viewGroup,false);
        }
        TextView textView=(TextView)view.findViewById(R.id.text_view);

        textView.setText(catnm.get(i).toString());

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String key = catnm.get(i).toString();

                Intent i=new Intent(c,Main2Activity.class);
                i.putExtra("cat",key);
                c.startActivity(i);

            }
        });
        return view;
    }
}
