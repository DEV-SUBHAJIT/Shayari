package com.roy.shayari.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.roy.shayari.R;
import com.roy.shayari.adapter.TextAdapter;
import com.roy.shayari.model.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SadFragment extends Fragment implements TextAdapter.ItemClick {

    private RecyclerView recyclerView;
    private List<Text> textList;
    private TextAdapter textAdapter;
    private Text text;
    private AdView mAdView;

    private ShimmerFrameLayout shimmerFrameLayout;

    private DatabaseReference mDatabaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sad, container, false);
        initView(view);
        loadData();
        loadAd(view);
        return view;
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.sad_recyclerview);
        shimmerFrameLayout = view.findViewById(R.id.shimmer_layout_sad);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void loadData() {
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmerAnimation();
        textList = new ArrayList<>();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Sad");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    text = postSnapshot.getValue(Text.class);
                    textList.add(text);
                }
                shimmerFrameLayout.stopShimmerAnimation();
                shimmerFrameLayout.setVisibility(View.GONE);

                textAdapter = new TextAdapter((ArrayList<Text>) textList, SadFragment.this);
                recyclerView.setAdapter(textAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                shimmerFrameLayout.stopShimmerAnimation();
                shimmerFrameLayout.setVisibility(View.GONE);
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view, int position) {
        String story = textList.get(position).getStory();
        String shareBody = story + "\n\n Send from " +
                "https://play.google.com/store/apps/details?id=com.roy.shayari";
        switch (view.getId()) {
            case R.id.btn_whatsapp:
                Intent waIntent = new Intent(Intent.ACTION_SEND);
                waIntent.setType("text/plain");
                waIntent.setPackage("com.whatsapp");
                waIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                try {
                    Objects.requireNonNull(getActivity()).startActivity(waIntent);
                } catch (android.content.ActivityNotFoundException exception) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.whatsapp")));
                }
                break;
            case R.id.btn_share:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                break;
        }
    }

    private void loadAd(View view) {
        MobileAds.initialize(getActivity(), initializationStatus -> {
        });
        AdView adView = new AdView(getActivity());
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId("ca-app-pub-4909264441583347/1183196958");

        mAdView = view.findViewById(R.id.adView_sad);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}