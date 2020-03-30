package com.example.shm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class streamingActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    streamingRecyclerAdapter _streamingRecyclerAdapter;

    ArrayList <String>  userEmailFromFB;
    ArrayList<String>  userCommentFB;
    ArrayList<String>  userImagesFB;
    ArrayList<String> usernameFB;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.shm_options_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.upload){
            Intent intentToUpload = new Intent(streamingActivity.this,UploadActivity.class);
            startActivity(intentToUpload);
        }
        else if(item.getItemId() == R.id.personal){
              Toast.makeText(streamingActivity.this,"yapım aşamasında üzgünüz",Toast.LENGTH_LONG).show();
        }
        else  if (item.getItemId() == R.id.logout){
             firebaseAuth.signOut();
             Intent intentToSingUp = new Intent(streamingActivity.this,SingUpActivity.class);
             startActivity(intentToSingUp);
             finish();
        }
        else if(item.getItemId() == R.id.messages){
            Intent messageActivities = new Intent(streamingActivity.this,messagesActivity.class);
            startActivity(messageActivities);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streaming);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        userCommentFB = new ArrayList<>();
        userEmailFromFB = new ArrayList<>();
        userImagesFB = new ArrayList<>();
        usernameFB = new ArrayList<>();

        getDataFromFireStore();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        _streamingRecyclerAdapter = new streamingRecyclerAdapter(usernameFB,userCommentFB,userImagesFB);

        recyclerView.setAdapter(_streamingRecyclerAdapter);
    }


    public void getDataFromFireStore(){

        CollectionReference collectionReference = firebaseFirestore.collection("Posts");
        collectionReference.orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e != null){
                    Toast.makeText(streamingActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }
                if(queryDocumentSnapshots != null){
                    for(DocumentSnapshot snapshot :queryDocumentSnapshots.getDocuments() ){
                        Map<String, Object> data = snapshot.getData();
                        String userMail = (String) data.get("usermail");
                        String comment = (String) data.get("comment");
                        String downloandUrl = (String) data.get("downloadUrl");
                        String username = (String) data.get("username");

                        userEmailFromFB.add(userMail);
                        userCommentFB.add(comment);
                        userImagesFB.add(downloandUrl);
                        usernameFB.add(username);

                        _streamingRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}
