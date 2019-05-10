package com.ibrahim.secimtahmin;

import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    private String[] partiler =
            {"AKP", "BBP", "CHP", "HDP", "MHP", "SP"};
    private String[] partilersonuc =
            {"AKP", "BBP", "CHP", "HDP", "MHP", "SP"};
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Partiler");
    DatabaseReference myRef2 = database.getReference("Users");
    private String android_id;
    ListView sonuclar;
    int b=0;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sonuclar = findViewById(R.id.sonuc);
        listView = findViewById(R.id.listview);
        android_id = Secure.getString(getApplicationContext().getContentResolver(),
                Secure.ANDROID_ID);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("SecimTahmin");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));
        setSupportActionBar(toolbar);




             final ArrayAdapter<String> veriAdaptoru = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, android.R.id.text1, partiler);



        listView.setAdapter(veriAdaptoru);




        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position,
                                    long id) {


                Query query = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("kullanıcı").equalTo(android_id);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() > 0) {
                            Toast.makeText(MainActivity.this, "Zaten bir seçim yapmıssınız ", Toast.LENGTH_LONG).show();

                        }else{
                            myRef = database.getReference("Partiler").child(partiler[position]).child(android_id);

                            HashMap<String,String> hashMap=new HashMap<>();
                            hashMap.put("kullanıcılar",android_id);
                            hashMap.put("oy","1");

                            myRef2.child(android_id).child("oy").setValue(partiler[position]);
                            myRef2.child(android_id).child("kullanıcı").setValue(android_id);


                            myRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Toast.makeText(MainActivity.this, "okay", Toast.LENGTH_LONG).show();

                                    }
                                    else{

                                        Toast.makeText(MainActivity.this,"Bir hata oluştu tekrar deneyiniz",Toast.LENGTH_LONG).show();

                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.getdata:
                getData();


            default:
                return super.onOptionsItemSelected(item);
        }
    }



    public  void getData(){

        DatabaseReference reference=database.getReference("Partiler");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){

                    long a=ds.getChildrenCount();
                    partilersonuc[b]=(partiler[b]+"   "+a);
                    b++;
                    if(b>=6){
                        b=0;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final ArrayAdapter<String> veriAdaptoru2 = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, android.R.id.text1, partilersonuc);
        sonuclar.setAdapter(veriAdaptoru2);

    }

}