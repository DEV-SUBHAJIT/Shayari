package com.roy.shayari.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.roy.shayari.R;
import com.roy.shayari.model.Post;
import com.roy.shayari.model.Text;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

    public ArrayList<Post> postList;
    public ItemClick mItemClick;

    public PostAdapter(ArrayList<Post> postList, ItemClick itemClick) {
        this.postList = postList;
        this.mItemClick = itemClick;
    }

    @NonNull
    @Override
    public PostAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_item, parent, false);
        return new MyViewHolder(itemView);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final MaterialTextView story;
        private final MaterialTextView name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            story = itemView.findViewById(R.id.tv_story);
            name = itemView.findViewById(R.id.tv_name);
            MaterialButton button = itemView.findViewById(R.id.btn_share);
            ImageView whatsapp = itemView.findViewById(R.id.btn_whatsapp);

            button.setOnClickListener(this);
            whatsapp.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClick != null) mItemClick.onClick(v, getAdapterPosition());
        }
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.MyViewHolder holder, int position) {
        holder.story.setText(postList.get(position).getPost());
        holder.name.setText(postList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public interface ItemClick {
        void onClick(View view, int position);
    }
}
