package com.example.shm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class accountSaveActivity extends AppCompatActivity {
private FirebaseAuth firebaseAuth;
EditText email_txt,username_txt,password_txt,retryPassword_txt;

 FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_save);
        firebaseAuth = FirebaseAuth.getInstance();
        email_txt = findViewById(R.id.usermail_txt);
        username_txt = findViewById(R.id.username_txt);
        password_txt = findViewById(R.id.userpassword_txt);
        retryPassword_txt = findViewById(R.id.retrypassword_txt);

        firebaseFirestore = FirebaseFirestore.getInstance();

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser != null ){
            Intent intent = new Intent(accountSaveActivity.this,streamingActivity.class);
            startActivity(intent);
            finish();
        }


    }
    public void saveButton(View view){
          String mail = email_txt.getText().toString();
          String retryPassword = retryPassword_txt.getText().toString();
          String password = password_txt.getText().toString();


          if(mail.matches("") || password.matches("") || retryPassword.matches("")){
              Toast.makeText(accountSaveActivity.this,"mail adresinizi veya parolanızı eksiksiz bir şekilde giriniz",Toast.LENGTH_LONG).show();

          }
          else if(password.equals(retryPassword)){


              firebaseAuth.createUserWithEmailAndPassword(mail,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                  @Override
                  public void onSuccess(AuthResult authResult) {


                      Toast.makeText(accountSaveActivity.this,"kullanıcı oluşturuldu",Toast.LENGTH_LONG).show();
                      savePersonal();
                      Intent intent = new Intent(accountSaveActivity.this, streamingActivity.class);
                      startActivity(intent);
                      finish();
                  }
              }).addOnFailureListener(new OnFailureListener() {
                  @Override
                  public void onFailure(@NonNull Exception e) {
                      Toast.makeText(accountSaveActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                  }
              });

          }
          else {
              Toast.makeText(accountSaveActivity.this,"parolanız eşleşmiyor eksiksiz bir şekilde giriniz",Toast.LENGTH_LONG).show();
    }
    }
    public void savePersonal(){


String usermail= email_txt.getText().toString();
String username = username_txt.getText().toString();
String password = password_txt.getText().toString();
        HashMap<String,Object> postData = new HashMap<>();

        postData.put("usermail",usermail);
        postData.put("username",username);
        postData.put("password",password);

        firebaseFirestore.collection("personal").add(postData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(accountSaveActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            }
        });



        Toast.makeText(accountSaveActivity.this,"kişisel bilgiler kaydetildi",Toast.LENGTH_LONG).show();


    }
}
