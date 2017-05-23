package com.ambujadev.myapplication;

import android.content.Intent;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Other extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private Button mguest, mforgot,msearch;
    private EditText muid5, mclass5, msection5;
    private TextView mpassword5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);

        final String email = "guest@ambuja.com";
        final String password = "ambujaguest";

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Students");

        msearch = (Button) findViewById(R.id.searchf);
        msearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String uid = muid5.getText().toString().trim();

                mDatabase.child(uid).child("Password").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                       String pass = dataSnapshot.getValue(String.class);

                        mpassword5.setText("Your Password is "+pass);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        mpassword5 = (TextView) findViewById(R.id.textViewf);

        muid5 = (EditText) findViewById(R.id.Uid);
        mclass5 = (EditText) findViewById(R.id.classf);
        msection5  =(EditText) findViewById(R.id.sectionf);

        mforgot = (Button) findViewById(R.id.Forgotbtn);
        mforgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                muid5.setVisibility(View.VISIBLE);
                mclass5.setVisibility(View.VISIBLE);
                msection5.setVisibility(View.VISIBLE);
                mpassword5.setVisibility(View.VISIBLE);
                msearch.setVisibility(View.VISIBLE);

            }
        });

        mAuth = FirebaseAuth.getInstance();
        mguest = (Button) findViewById(R.id.guest);
        mguest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            startActivity(new Intent(Other.this, MainActivity.class));

                        }
                    }
                });


            }
        });
    }


}
