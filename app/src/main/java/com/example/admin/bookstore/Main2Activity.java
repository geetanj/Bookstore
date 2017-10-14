package com.example.admin.bookstore;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class Main2Activity extends AppCompatActivity {
    ArrayList<books> arrayList = new ArrayList<>();
    ArrayList<String> catarray=new ArrayList<>();
    SpinAdapter spinadapter;
    listviewadapter listviewadapter;
    FirebaseDatabase fd;
    DatabaseReference db;
    ListView listview;

    Spinner spincat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        spincat=(Spinner)findViewById(R.id.spicat);
        listview = (ListView) findViewById(R.id.list_view);


        Intent i=getIntent();
        String catkey=i.getStringExtra("cat");

        System.out.println("category list "+catkey);
        getcat();
        getcatdata();


        fd = FirebaseDatabase.getInstance();
        db = fd.getReference("books");
        System.out.println("database  " + db.toString());
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot db : dataSnapshot.getChildren()) {

                    Boolean ca = db.child("shipToCanada").getValue(Boolean.class);
                    Boolean us=db.child("shipToUSA").getValue(Boolean.class);

                    if(tocanada(getLoc())){
                        if(ca.equals(true) || us.equals(false)){

                            String bnm=db.child("title").getValue().toString();
                            arrayList.add(new books(bnm));
                            //System.out.println("from canada "+db.toString());
                        }
                    }else if (tous(getLoc())){
                        if (us.equals(true) || ca.equals(false)){
                            String bnm=db.child("title").getValue().toString();
                            arrayList.add(new books(bnm));
                            //System.out.println("from usa "+db.toString());
                        }
                    }

                }

                listviewadapter = new listviewadapter(Main2Activity.this, arrayList);
                listview.setAdapter(listviewadapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {

            case R.id.menu_search :

                Toast.makeText(getApplicationContext(), "search books ", Toast.LENGTH_LONG).show();
                searchdisplay();
            default :

                return super.onOptionsItemSelected(item);

        }

    }

    public void searchdisplay() {
        final Dialog ds = new Dialog(this, R.style.CustomDialog);
        ds.setTitle("Search");
        ds.setContentView(R.layout.searchlayout);

        final EditText edt = (EditText) ds.findViewById(R.id.edt_search);
        final Spinner spin = (Spinner) ds.findViewById(R.id.spin_search);
        Button btn = (Button) ds.findViewById(R.id.btn_search);

        String searchkey=edt.getText().toString();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(), "searching " + edt.getText(), Toast.LENGTH_LONG).show();
                String keyme = edt.getText().toString().toLowerCase();


                if(spin.getSelectedItem().toString().equals("Book Name")){
                    searchbyname(keyme);
                }
                else if(spin.getSelectedItem().toString().equals("Author Name")){
                    searchbyauthor(keyme);
                }
                else {
                    Toast.makeText(getApplicationContext(),"please select the choice",Toast.LENGTH_LONG).show();
                }
                ds.dismiss();
            }
        });

        ds.show();
    }

    public void searchbyname(final String keyme) {
        FirebaseDatabase fd = FirebaseDatabase.getInstance();
        DatabaseReference db = fd.getReference("books");
        arrayList.clear();
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String title = ds.child("title").getValue().toString().toLowerCase();

                    if (title.contains(keyme)) {
                        String test = ds.child("title").getValue().toString();

                        System.out.println("search me " + test);


                        arrayList.add(new books(test));
                    }
                    setAdapater();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setAdapater() {

        listviewadapter = new listviewadapter(Main2Activity.this, arrayList);
        listview.setAdapter(listviewadapter);
        listviewadapter.notifyDataSetChanged();
    }

    public void searchbyauthor(final String autnm){
        FirebaseDatabase fd = FirebaseDatabase.getInstance();
        final DatabaseReference db = fd.getReference("auhtors");
        arrayList.clear();

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {


                    String nm = ds.child("name").getValue().toString().toLowerCase();
                    if (nm.contains(autnm)) {

                        final String authid=ds.getKey();
                        System.out.println("author id " + ds.getKey());
                        FirebaseDatabase fd2 = FirebaseDatabase.getInstance();
                        DatabaseReference db2 = fd2.getReference("books");
                        db2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    String id = ds.child("author_id").getValue().toString();
                                    if (id.equals(authid)) {

                                        arrayList.add(new books(ds.child("title").getValue().toString()));

                                    }
                                    setAdapater();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                    }
                }




            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public boolean tocanada(String cn){
        if(cn.equals("CA")){
            return true;
        }else {
            return false;
        }
    }
    public boolean tous(String cn){
        if(cn.equals("US")){
            return true;
        }else {
            return false;
        }
    }
    public String getLoc(){
        return Locale.getDefault().getCountry();
    }

    public void getcat(){

        FirebaseDatabase fd = FirebaseDatabase.getInstance();
        DatabaseReference db = fd.getReference("category");

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    System.out.println("books category"+ds.getValue().toString());
                    catarray.add(ds.getValue(String.class));
                }
                spinadapter=new SpinAdapter(Main2Activity.this,catarray);
                spincat.setAdapter(spinadapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void getcatdata(){
        FirebaseDatabase fd = FirebaseDatabase.getInstance();
        DatabaseReference db = fd.getReference("books");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
