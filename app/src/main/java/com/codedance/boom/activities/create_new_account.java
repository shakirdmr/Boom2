package com.codedance.boom.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.codedance.boom.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class create_new_account extends AppCompatActivity {


    private DatabaseReference usersRef;
    FirebaseAuth firebaseAuth;
    HashMap<String, String> map;
    ImageView backHome;

    Button continueCreateNewAcc;
    EditText fullname, email, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_account);



        backHome  =findViewById(R.id.goBackHome);
        continueCreateNewAcc  =findViewById(R.id.continueCreateNewAcc);
        fullname  =findViewById(R.id.fullname);
        email  =findViewById(R.id.email);
        password  =findViewById(R.id.password);

        firebaseAuth = FirebaseAuth.getInstance();

        usersRef = FirebaseDatabase.getInstance().getReference().child("users");


        continueCreateNewAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                 String fullnameText = fullname.getText().toString();
                 String  emailText = email.getText().toString();
                 String passwordText = password.getText().toString();

                map = new HashMap<>();
                map.put("email",emailText);
                map.put("fullName",fullnameText);
                map.put("password",passwordText);

                 if(TextUtils.isEmpty(fullnameText) || TextUtils.isEmpty(emailText) || TextUtils.isEmpty(passwordText))
                 {
                     Toast.makeText(create_new_account.this, "Empty credentials. Please enter all details", Toast.LENGTH_SHORT).show();

                 }
                 else if(passwordText.length()<6)
                 {
                     Toast.makeText(create_new_account.this, "Password needs to be greater than six digits", Toast.LENGTH_SHORT).show();

                 }
                 else
                 {
                     checkIfEmailExists_and_makeNewRegistration(emailText);
                 }
            }
        });

        backHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
    }

    private void checkIfEmailExists_and_makeNewRegistration(final String email) {
        Log.d("SIMRAN","done "+email);
        usersRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // The email already exists in the database
                    Toast.makeText(create_new_account.this, "Email already registered", Toast.LENGTH_SHORT).show();
                } else {
                    // The email is not registered, proceed with registration


                    Intent it = new Intent(create_new_account.this, otpVerify_newAccount.class);
                    it.putExtra("mapOfDetails",map);
                    startActivity(it);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
                Toast.makeText(create_new_account.this, "Database error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

