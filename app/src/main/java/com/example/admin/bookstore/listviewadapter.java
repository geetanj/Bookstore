package com.example.admin.bookstore;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by Admin on 2017-10-13.
 */

public class listviewadapter extends BaseAdapter {

    LayoutInflater inflater;
    Context c;
    ArrayList<books> arrylst;

    FirebaseDatabase fd;
    DatabaseReference db;

    public listviewadapter(Context c, ArrayList<books> arrylst) {
        this.c = c;
        this.arrylst = arrylst;
    }


    public int getCount() {
        return arrylst.size();
    }

    @Override
    public Object getItem(int i) {
        return arrylst.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }



    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        fd=FirebaseDatabase.getInstance();
        db=fd.getReference();

        if (inflater ==null){
            inflater=(LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }
        if(view==null)
        {
            view=inflater.inflate(R.layout.conntentlist,viewGroup,false);
        }
        TextView textView=(TextView)view.findViewById(R.id.text_view);
        textView.setText(arrylst.get(i).getBookname());

        textView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Toast.makeText(c, arrylst.get(i).getBookname().toString(), Toast.LENGTH_LONG).show();

                db.child("books").orderByChild("title").equalTo(arrylst.get(i).getBookname().toString()).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        fd = FirebaseDatabase.getInstance();
                        db = fd.getReference();
                        String ls=db.child("books").child(dataSnapshot.getKey()).toString();
                        Toast.makeText(c,ls,Toast.LENGTH_LONG);
                        Intent i=new Intent(c,booksdetail.class);
                        i.putExtra("key",ls);
                        c.startActivity(i);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

        return view;
    }
}
