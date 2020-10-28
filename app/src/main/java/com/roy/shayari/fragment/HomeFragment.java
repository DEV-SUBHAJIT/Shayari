package com.roy.shayari.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.roy.shayari.R;

public class HomeFragment extends Fragment {
    private ImageView love, sad, hate, goodMorning, goodNight, sorry, friendship, heartTouching;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        clickEvent();
        return view;
    }

    private void initView(View view) {
        love = view.findViewById(R.id.iv_love);
        sad = view.findViewById(R.id.iv_sad);
        hate = view.findViewById(R.id.iv_hate);
        goodMorning = view.findViewById(R.id.iv_good_morning);
        goodNight = view.findViewById(R.id.iv_goodnight);
        sorry = view.findViewById(R.id.iv_sorry);
        friendship = view.findViewById(R.id.iv_friendship);
        heartTouching = view.findViewById(R.id.iv_heart_touching);
    }

    private void clickEvent() {
        love.setOnClickListener(v -> getFragmentManager().beginTransaction().replace(R.id.frame_layout, new LoveFragment()).commit());
        sad.setOnClickListener(v -> getFragmentManager().beginTransaction().replace(R.id.frame_layout, new SadFragment()).commit());
        hate.setOnClickListener(v -> getFragmentManager().beginTransaction().replace(R.id.frame_layout, new HateFragment()).commit());
        goodMorning.setOnClickListener(v -> getFragmentManager().beginTransaction().replace(R.id.frame_layout, new MorningFragment()).commit());
        goodNight.setOnClickListener(v -> getFragmentManager().beginTransaction().replace(R.id.frame_layout, new NightFragment()).commit());
        sorry.setOnClickListener(v -> getFragmentManager().beginTransaction().replace(R.id.frame_layout, new SorryFragment()).commit());
        heartTouching.setOnClickListener(v -> getFragmentManager().beginTransaction().replace(R.id.frame_layout, new HeartFragment()).commit());
        friendship.setOnClickListener(v -> getFragmentManager().beginTransaction().replace(R.id.frame_layout, new FriendshipFragment()).commit());
    }
}