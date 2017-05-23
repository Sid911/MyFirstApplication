package com.ambujadev.myapplication;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.system.Os;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Register extends AppCompatActivity{
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private EditText mNameField;
    private EditText mEmailField, mPasswordField , mClass , mSection;
    private EditText mEnglishGarde, mEnglishSA1, mEnglishSA2;
    private EditText mMathGrade;
    private EditText mScienceGrade;
    private EditText mHindiGrade;

    private Button mRegisterbtn;

    private ProgressDialog mProgress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        mProgress = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Students");

        mNameField = (EditText) findViewById(R.id.nameField);
        mClass = (EditText) findViewById(R.id.classField);
        mSection = (EditText) findViewById(R.id.sectionField);

        mEmailField = (EditText) findViewById(R.id.emailField);
        mPasswordField = (EditText) findViewById(R.id.PasswordField);

        mEnglishGarde = (EditText) findViewById(R.id.English);
        mEnglishSA1 = (EditText) findViewById(R.id.EnglishSA1);
        mEnglishSA2 = (EditText) findViewById(R.id.EnglishSA2);

        mMathGrade = (EditText) findViewById(R.id.Maths);

        mScienceGrade = (EditText) findViewById(R.id.Science);

        mHindiGrade = (EditText) findViewById(R.id.Hindi);

        mRegisterbtn = (Button) findViewById(R.id.Registerbtn);
        mRegisterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegister();
            }
        });
    }



    private void startRegister() {

        final String name = mNameField.getText().toString().trim();
        final String sclass = mClass.getText().toString().trim();
        final String section = mSection.getText().toString().trim();

        String email = mEmailField.getText().toString().trim();
        final String password = mPasswordField.getText().toString().trim();

        final String englishg = mEnglishGarde.getText().toString().trim();
        final String englishs1 = mEnglishSA1.getText().toString().trim();
        final String englishs2 = mEnglishSA2.getText().toString().trim();

        final String mathg = mMathGrade.getText().toString().trim();

        final String scienceg = mScienceGrade.getText().toString().trim();

        final String hindig = mHindiGrade.getText().toString().trim();

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) &&!TextUtils.isEmpty(sclass) && !TextUtils.isEmpty(section)){

            mProgress.show();
           mAuth.createUserWithEmailAndPassword(email , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
               @Override
               public void onComplete(@NonNull Task<AuthResult> task) {
                   if (task.isSuccessful()){
                       //This is user id
                       String user_id = mAuth.getCurrentUser().getUid();

                       //This is "NON-ORGANISED DATABASE
                       DatabaseReference overall = mDatabase;
                       DatabaseReference user = overall.child(user_id);

                       user.child("Name").setValue(name);
                       user.child("Password").setValue(password);
                       user.child("Class").setValue(sclass);
                       user.child("Section").setValue(section);

                       DatabaseReference Oenglish = user.child("English");
                       Oenglish.child("Grade").setValue(englishg);
                       Oenglish.child("SA 1").setValue(englishs1);
                       Oenglish.child("SA 2").setValue(englishs2);

                       DatabaseReference Oscience = user.child("Science");
                       Oscience.child("Grade").setValue(scienceg);

                       DatabaseReference Omaths = user.child("Maths");
                       Omaths.child("Grade").setValue(mathg);

                       DatabaseReference Ohindi = user.child("Hindi");
                       Ohindi.child("Grade").setValue(hindig);


                       //This is "ORGANISED DATABASE"
                       DatabaseReference tclass = mDatabase.child(sclass);
                       DatabaseReference tsection = tclass.child(section);

                       DatabaseReference current_user_prof = tsection.child(user_id);
                       current_user_prof.child("Name").setValue(name);
                       current_user_prof.child("Password").setValue(password);

                       DatabaseReference english = current_user_prof.child("English");
                       english.child("Grade").setValue(englishg);
                       english.child("SA 1").setValue(englishs1);
                       english.child("SA 2").setValue(englishs2);

                       DatabaseReference science = current_user_prof.child("Science");
                       science.child("Grade").setValue(scienceg);

                       DatabaseReference maths = current_user_prof.child("Maths");
                       maths.child("Grade").setValue(mathg);

                       DatabaseReference hindi = current_user_prof.child("Hindi");
                       hindi.child("Grade").setValue(hindig);

                       mProgress.dismiss();
                   }
               }
           });
        }
    }
}
