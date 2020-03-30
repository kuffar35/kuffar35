package com.example.shm;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class messagesActivity extends AppCompatActivity {
    EditText message_txt;
    private String messageFB;



    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private ArrayList<String> messagesArrayList;
    FirebaseAuth firebaseAuth;
    messageRecyclerAdapter _messageRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        message_txt = findViewById(R.id.message_txt);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        messagesArrayList = new ArrayList<String>();

        getData();
        RecyclerView recyclerView = findViewById(R.id.message_rcycler_row);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        _messageRecyclerAdapter = new messageRecyclerAdapter(messagesArrayList);
        recyclerView.setAdapter(_messageRecyclerAdapter);



    }

    public void message_btn(View view){

        FirebaseUser firebaseUser= firebaseAuth.getCurrentUser();
        messageFB=message_txt.getText().toString();
        final String userMail = firebaseUser.getEmail();

        CollectionReference collectionReference = firebaseFirestore.collection("personal");
        collectionReference.whereEqualTo("usermail",userMail).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(messagesActivity.this, e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }
                if(queryDocumentSnapshots != null){

                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                        Map<String,Object> data =  snapshot.getData();
                        String username = (String) data.get("username");
                        HashMap<String,Object> postData = new HashMap<>();
                        postData.put("messages",username+"| ---> "+messageFB);
                        postData.put("date", FieldValue.serverTimestamp());

                        firebaseFirestore.collection("messages").add(postData);
                        message_txt.setText("");


                    }
                    Intent intnt = new Intent(messagesActivity.this,streamingActivity.class);
                    intnt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


                    startActivity(intnt);
                    finish();


                }
            }});

    }

    public void  getData(){

       CollectionReference collectionReference = firebaseFirestore.collection("messages");
       collectionReference.orderBy("date", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
           @Override
           public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
               if(e != null){
                   Toast.makeText(messagesActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
               }
                   if(queryDocumentSnapshots != null){
                       String message = "";
                       for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                            Map<String,Object> data =  snapshot.getData();
                            message = (String) data.get("messages");

                           messagesArrayList.add(message);
                           _messageRecyclerAdapter.notifyDataSetChanged();




                   }




                   }
           }
       });

    }
}
