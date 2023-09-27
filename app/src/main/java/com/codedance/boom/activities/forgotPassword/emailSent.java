package com.codedance.boom.activities.forgotPassword;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codedance.boom.R;
import com.codedance.boom.activities.login_signup;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class emailSent extends AppCompatActivity {

    TextView recipientEmail;
    private FirebaseAuth auth;
    Button loginActivity;
    ImageView goBackHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_sent);

        loginActivity  = findViewById(R.id.loginActivity);
        goBackHome  = findViewById(R.id.goBackHome);
        recipientEmail  = findViewById(R.id.recipientEmail);

        auth = FirebaseAuth.getInstance();

        Intent it = getIntent();
        String email = it.getStringExtra("email");

        recipientEmail.setText(email);
        sendEmail_resetPassword(email);

        loginActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(emailSent.this, login_signup.class));
                finishAffinity();
            }
        });

        goBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void sendEmail_resetPassword(String email) {

        // Send a password reset email
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Password reset email sent successfully
                            Toast.makeText(emailSent.this, "Password reset email sent.", Toast.LENGTH_SHORT).show();
                        } else {
                            // Password reset email failed
                            Toast.makeText(emailSent.this, "Failed to send password reset email.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        }
}