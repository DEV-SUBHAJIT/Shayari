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
import com.roy.shayari.model.Text;

import java.util.ArrayList;

public class TextAdapter extends RecyclerView.Adapter<TextAdapter.MyViewHolder> {

    public ArrayList<Text> textList;
    public ItemClick mItemClick;

    public TextAdapter(ArrayList<Text> textList, ItemClick itemClick) {
        this.textList = textList;
        this.mItemClick = itemClick;
    }

    @NonNull
    @Override
    public TextAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_item, parent, false);
        return new MyViewHolder(itemView);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final MaterialTextView story;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            story = itemView.findViewById(R.id.tv_story);
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
    public void onBindViewHolder(@NonNull TextAdapter.MyViewHolder holder, int position) {
        holder.story.setText(textList.get(position).getStory());
    }

    @Override
    public int getItemCount() {
        return textList.size();
    }

    public interface ItemClick {
        void onClick(View view, int position);
    }
}
