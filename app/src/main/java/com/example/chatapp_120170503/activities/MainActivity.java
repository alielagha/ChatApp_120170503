package com.example.chatapp_120170503.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.chatapp_120170503.adapters.UsersListAdapter;
import com.example.chatapp_120170503.R;
import com.example.chatapp_120170503.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView usersRecyclerView;
    private UsersListAdapter usersListAdapter;
    private List<User> users = new ArrayList<>();
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseFirestore = FirebaseFirestore.getInstance();
        usersRecyclerView = findViewById(R.id.usersRecyclerView);

        usersRecyclerViewSetup();

        getUsersFromFirestore();

    }

    private void usersRecyclerViewSetup() {

        usersListAdapter = new UsersListAdapter(getApplicationContext() ,users);
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false));
        usersRecyclerView.setAdapter(usersListAdapter);

    }

    private void getUsersFromFirestore() {

        CollectionReference collectionReference = firebaseFirestore.collection("users");
        Task<QuerySnapshot> querySnapshotTask = collectionReference.get();
        querySnapshotTask.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

               Iterator<QueryDocumentSnapshot> snapshotIterator  =  queryDocumentSnapshots.iterator();
               while (snapshotIterator.hasNext()){
                   User user = snapshotIterator.next().toObject(User.class);

                   Toast.makeText(MainActivity.this, user.getName(), Toast.LENGTH_SHORT).show();
                   users.add(user);

               }

               usersListAdapter.notifyDataSetChanged();

            }
        });
    }


}
