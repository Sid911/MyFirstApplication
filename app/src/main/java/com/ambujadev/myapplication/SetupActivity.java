package com.ambujadev.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class SetupActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView mText;
    private Button mretry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        mAuth = FirebaseAuth.getInstance();

        String uid = mAuth.getCurrentUser().getUid();
        mText = (TextView) findViewById(R.id.textView4);
        mText.setText(uid);

        mretry = (Button) findViewById(R.id.retrybtn);
        mretry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SetupActivity.this, Login.class));
            }
        });
    }
}
