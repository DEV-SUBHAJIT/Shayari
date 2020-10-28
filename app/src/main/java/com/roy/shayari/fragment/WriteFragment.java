package com.roy.shayari.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.roy.shayari.adapter.PostAdapter;
import com.roy.shayari.adapter.TextAdapter;
import com.roy.shayari.model.Post;
import com.roy.shayari.model.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WriteFragment extends Fragment implements PostAdapter.ItemClick {
    private RecyclerView recyclerView;
    private List<Post> postList;
    private PostAdapter textAdapter;
    private Post text;
    private AdView mAdView;

    private Button button;

    private ShimmerFrameLayout shimmerFrameLayout;

    private DatabaseReference mDatabaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_write, container, false);
        initView(view);
        loadData();
        loadAd(view);
        return view;
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.rv_write);
        shimmerFrameLayout = view.findViewById(R.id.shimmer_layout_write);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        view.findViewById(R.id.button).setOnClickListener(v -> {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("HeartTouching");

            String key = myRef.child("HeartTouching").push().getKey();
            myRef.child(key).child("story").setValue("");
        });
    }

    private void loadData() {
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmerAnimation();
        postList = new ArrayList<>();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("UserPost");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    text = postSnapshot.getValue(Post.class);
                    postList.add(text);
                }
                shimmerFrameLayout.stopShimmerAnimation();
                shimmerFrameLayout.setVisibility(View.GONE);

                textAdapter = new PostAdapter((ArrayList<Post>) postList, WriteFragment.this);
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
        String story = postList.get(position).getPost();
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
        adView.setAdUnitId("ca-app-pub-4909264441583347/5544409526");

        mAdView = view.findViewById(R.id.adView_write);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

}
