package com.example.androidassignments;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MessageDetails extends AppCompatActivity {

    Button deleteButton;
    Bundle bundle;
    int id;
    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);

        deleteButton = findViewById(R.id.deleteButton);
        bundle = getIntent().getExtras();
        id = bundle.getInt("id", 0);
        message = bundle.getString("message", "");

        MessageFragment fragment = new MessageFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        fragment.setArguments(bundle);
        ft.addToBackStack(null);
        ft.add(R.id.fragmentFrame, fragment);
        ft.commit();

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(id);
                finish();
            }
        });
    }

    static class MessageFragment extends Fragment {
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.message_details_fragment, parent, false);
        }
    }


}