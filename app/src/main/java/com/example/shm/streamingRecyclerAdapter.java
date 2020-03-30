package com.example.shm;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.base.MoreObjects;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class streamingRecyclerAdapter extends RecyclerView.Adapter<streamingRecyclerAdapter.PostHolder> {

    private ArrayList<String> userName;
    private ArrayList<String>  userComment;
    private ArrayList<String>  userImage;
    private FirebaseFirestore firebaseFirestore;




    public streamingRecyclerAdapter(ArrayList<String> userName, ArrayList<String> userComment, ArrayList<String> userImage) {
        this.userName = userName;
        this.userComment = userComment;
        this.userImage = userImage;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recyler_row,parent,false);
        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PostHolder holder, final int position) {

        holder.usernameTXT.setText("*** "+userName.get(position) + " ***");
        holder.commentTXT.setText(userComment.get(position));
        holder.sendComment.getText();
        Picasso.get().load(userImage.get(position)).into(holder.imageview);
        holder.buttonComent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(holder.usernameTXT.getText()+userImage.get(position)+holder.sendComment.getText());
                HashMap <String,Object>postData = new HashMap<>();
                postData.put("username",holder.usernameTXT.getText().toString());
                postData.put("userImage",userImage.get(position));
                postData.put("comment",holder.sendComment.getText().toString());
                firebaseFirestore.collection("Comment").add(postData);


            }

        });


    }

    @Override
    public int getItemCount() {
        return userName.size();
    }



    class  PostHolder extends RecyclerView.ViewHolder {


        ImageView imageview;
        TextView usernameTXT;
        TextView commentTXT;
        TextView sendComment;
        ImageButton buttonComent;



        @SuppressLint("WrongViewCast")
        public PostHolder(@NonNull View itemView) {
            super(itemView);
                imageview = itemView.findViewById(R.id.recyclervie_row_imageView);
                usernameTXT = itemView.findViewById(R.id.recyclervie_row_username_text);
                commentTXT = itemView.findViewById(R.id.recyclervie_row_userComment_text);
                sendComment = itemView.findViewById(R.id.comment_txt);
            buttonComent =  itemView.findViewById(R.id.recyclervie_row_userButton_text);

            firebaseFirestore = FirebaseFirestore.getInstance();


        }
    }
}
