package com.roy.shayari;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.roy.shayari.fragment.HomeFragment;
import com.roy.shayari.fragment.WriteFragment;

public class DashboardActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Fragment selectFragment = null;
    private String shayari, name;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        toolbarCode();
        bottomNav();
    }

    private void initView() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        mAuth = FirebaseAuth.getInstance();
    }

    private void toolbarCode() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.dash_toolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu);
    }

    private void bottomNav() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.bottomNavigationHomeMenuId:
                    selectFragment = new HomeFragment();
                    break;
                case R.id.bottomNavigationPostMenuId:
                    selectFragment = new WriteFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, selectFragment).commit();
            return true;
        });
    }

    @Override
    public void onBackPressed() {
        try {
            if (selectFragment != null) {
                getSupportFragmentManager().beginTransaction().remove(selectFragment).commit();
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();
        } catch (Exception e) {
            Log.d("TAG", "Exception " + e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.edit_post) {
            dialogCode();
        }
        return true;
    }

    private void dialogCode() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.post_dialog, null);

        AlertDialog.Builder alertDialogFb = new AlertDialog.Builder(this).setView(v);
        final AlertDialog fDialog = alertDialogFb.create();
        fDialog.setCanceledOnTouchOutside(false);

        MaterialButton fSubmit = v.findViewById(R.id.btn_feedback_submit);
        MaterialButton fCancel = v.findViewById(R.id.btn_feedback_cancel);
        TextInputEditText etName = v.findViewById(R.id.et_name);
        TextInputEditText etShayari = v.findViewById(R.id.et_shayari);

        fSubmit.setOnClickListener(view -> {
            shayari = etShayari.getText().toString().trim();
            name = etName.getText().toString().trim();
            if (name.isEmpty()) {
                etName.setError("Enter your name");
                return;
            }
            if (shayari.length() <= 50) {
                etShayari.setError("Write something more");
                Toast.makeText(this, "Your shayari is too short", Toast.LENGTH_SHORT).show();
                return;
            }
            sendData();
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new WriteFragment()).commit();
            fDialog.dismiss();
        });
        fCancel.setOnClickListener(view -> {
            if (fDialog.isShowing()) {
                fDialog.dismiss();
            }
        });
        fDialog.show();
    }

    private void sendData() {
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("UserPost");

        String key = myRef.child("UserPost").push().getKey();
        myRef.child(key).child("name").setValue(name);
        myRef.child(key).child("post").setValue(shayari);
    }
}