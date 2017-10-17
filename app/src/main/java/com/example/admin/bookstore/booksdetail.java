package com.example.admin.bookstore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class booksdetail extends AppCompatActivity {

    TextView bknm,bkpub,bkqty,bkaut;

    FirebaseDatabase database,database2;
    DatabaseReference myRef1,myaut;

    Button btn_rent;

    ImageView bookdis;

    String autid,coverpath;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booksdetail);

        bknm = (TextView) findViewById(R.id.dis_title);
        bkpub = (TextView) findViewById(R.id.dis_pub);
        bkqty = (TextView) findViewById(R.id.dis_qty);
        bkaut = (TextView) findViewById(R.id.dis_auth);
        bookdis = (ImageView) findViewById(R.id.img_book);
        btn_rent = (Button) findViewById(R.id.butn_rent);

        Intent i = getIntent();
        String path = i.getStringExtra("key");

        database = FirebaseDatabase.getInstance();
        myRef1 = database.getReferenceFromUrl(path);


        myRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                bknm.setText(dataSnapshot.child("title").getValue().toString());
                bkpub.setText(dataSnapshot.child("publisher").getValue().toString());
                bkqty.setText(dataSnapshot.child("quantity").getValue().toString());
                autid =  dataSnapshot.child("author_id").getValue().toString();
                coverpath = dataSnapshot.child("cover").getValue().toString();

                System.out.println("Cover Path   "+coverpath);



                Picasso.with(booksdetail.this).load(coverpath).into(bookdis);

                final int qty = Integer.parseInt(dataSnapshot.child("quantity").getValue().toString());


                btn_rent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(qty<=0)
                        {
                            Toast.makeText(getApplicationContext(),"No More Books Available!",Toast.LENGTH_LONG).show();
                        }else
                        {
                            Toast.makeText(getApplicationContext(),"Thank you for rent book",Toast.LENGTH_LONG).show();
                        }


                    }
                });


                System.out.println("Quantity in Integer  "+qty);


                database2 = FirebaseDatabase.getInstance();
                myaut = database2.getReference("authors");

                myaut.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds : dataSnapshot.getChildren())
                        {

                            if(ds.getKey().equals(autid))
                            {
                                bkaut.setText(ds.child("name").getValue().toString());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}


