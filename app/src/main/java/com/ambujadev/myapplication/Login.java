package com.ambujadev.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    private EditText mEmail , mPassword , mClass , mSection;
    private Button mLoginbtn, mOther;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mOther = (Button)findViewById(R.id.otherbtn);
        mOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Other.class));
            }
        });


        mProgress = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Students");
        mDatabase.keepSynced(true);

        mEmail = (EditText) findViewById(R.id.EmailLog);
        mPassword = (EditText) findViewById(R.id.PasswordLog);
        mClass = (EditText) findViewById(R.id.ClassLog);
        mSection = (EditText) findViewById(R.id.SectionLog);

        mLoginbtn = (Button) findViewById(R.id.SignInbtn);

        mLoginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin();
            }
        });
    }

    private void checkLogin() {
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        String classy = mClass.getText().toString().trim();
        String section = mSection.getText().toString().trim();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(classy) && !TextUtils.isEmpty(section)){

            mProgress.setMessage("Signing IN...");
            mProgress.show();
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){

                        mProgress.dismiss();

                        checkUserExists();
                    }else {

                        mProgress.dismiss();
                        Toast.makeText(Login.this ,"Error Login", Toast.LENGTH_LONG).show();
                        //Intent register = new Intent(Login.this, Register.class);
                        //startActivity(register);
                    }
                }
            });
        }
    }

    private void checkUserExists() {
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        final String classy = mClass.getText().toString().trim();
        String section = mSection.getText().toString().trim();

        final String uid = mAuth.getCurrentUser().getUid();

        DatabaseReference clast = mDatabase.child(classy);
        DatabaseReference user = clast.child(section);

        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(uid)){

                    Intent mainIntent= new Intent(Login.this , MainActivity.class);
                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);

                }else {
                    /*Intent setupIntent= new Intent(Login.this , SetupActivity.class);
                    setupIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(setupIntent);*/
                    FirebaseCrash.log("KOI KOCHAK RAHA HAI");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
