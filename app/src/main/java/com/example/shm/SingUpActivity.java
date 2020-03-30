package com.example.shm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
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

public class SingUpActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    EditText userMail_txt,userPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);
        ActionBar actionBar =getSupportActionBar();
        actionBar.hide();

        firebaseAuth = FirebaseAuth.getInstance();

        userMail_txt = findViewById(R.id.userMail_txt);
        userPassword = findViewById(R.id.userPassword_txt);

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser != null ){
            Intent intent = new Intent(SingUpActivity.this,streamingActivity.class);
            startActivity(intent);
            finish();
        }


    }

    public void login(View view){
          String mail = userMail_txt.getText().toString();
          String password = userPassword.getText().toString();
          if(mail.matches("") || password.matches("")){
              Toast.makeText(SingUpActivity.this,"mail adresinizi veya parolanızı eksiksiz bir şekilde giriniz",Toast.LENGTH_LONG).show();
          }else {
          firebaseAuth.signInWithEmailAndPassword(mail,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
              @Override
              public void onSuccess(AuthResult authResult) {

                  Toast.makeText(SingUpActivity.this,"kullanıcı giriş yapıldı",Toast.LENGTH_LONG).show();
                   Intent intent = new Intent(SingUpActivity.this,streamingActivity.class);
                   startActivity(intent);
                   finish();
              }
          }).addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                  Toast.makeText(SingUpActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
              }
          });
    }}
    public void singin(View view){
        Intent intent = new Intent(SingUpActivity.this,accountSaveActivity.class);
        startActivity(intent);
        finish();
    }
}
