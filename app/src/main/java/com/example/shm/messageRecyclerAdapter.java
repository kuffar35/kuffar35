package com.example.shm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class messageRecyclerAdapter extends RecyclerView.Adapter<messageRecyclerAdapter.PostHolder> {


    ArrayList<String> messagesArrayList;

    public messageRecyclerAdapter(ArrayList<String> messagesArrayList) {
        this.messagesArrayList = messagesArrayList;
    }


    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.message_row,parent,false);
        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
      holder.messages.setText(messagesArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return messagesArrayList.size();
    }

    class PostHolder extends RecyclerView.ViewHolder{
        TextView messages;
        public PostHolder(@NonNull View itemView) {
            super(itemView);
            messages=itemView.findViewById(R.id.messagesRecycler_message_label);
        }
    }
}
