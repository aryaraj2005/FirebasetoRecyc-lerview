package com.example.firebasetorecyclerview;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
   RecyclerView recyclerView;
   myAdapter adapter;
   FloatingActionButton fb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView  = findViewById(R.id.main_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
          // from documentation
        // this used to fetch the data from firebase to recycler view
        // this code will send a query to firebase and take all the data from firebase
        FirebaseRecyclerOptions<myModel> options =
                new FirebaseRecyclerOptions.Builder<myModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("students"), myModel.class)
                        .build();
         adapter = new myAdapter(options);
         recyclerView.setAdapter(adapter);
            // for add the new data use a floaticon
         fb = findViewById(R.id.fbtn);
         fb.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                startActivity(new Intent(MainActivity.this , addActivity.class));
             }
         });

    }
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
       MenuInflater inflater = getMenuInflater();
       inflater.inflate(R.menu.mymenu, menu);
       MenuItem item = menu.findItem(R.id.search_menu);
       // both serchView in XML and hear are of androidx other gives runtime error
       SearchView searchView = (SearchView)item.getActionView();
       searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
           @Override
           public boolean onQueryTextSubmit(String query) {
               processearch(query);
               return false;
           }

           @Override
           public boolean onQueryTextChange(String newText) {
               processearch(newText);
               return false;
           }
       });
       return true;
   }

    protected void processearch(String s){
        // from documentation
        // this used to fetch the data from firebase to recycler view
        FirebaseRecyclerOptions<myModel> options =
                new FirebaseRecyclerOptions.Builder<myModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("students").orderByChild("course").startAt(s).endAt(s+"\uf8ff"), myModel.class)
                        .build();

          adapter = new myAdapter(options);
          adapter.startListening();
          recyclerView.setAdapter(adapter);

    }
}