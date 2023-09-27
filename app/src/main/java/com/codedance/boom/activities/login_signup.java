package com.codedance.boom.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codedance.boom.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class login_signup extends AppCompatActivity {


    TextView ResetPassword;
    ProgressBar loginProgress ;
    FirebaseAuth firebaseAuth;
    Button login;
    EditText email, password;
    Button create_new_acc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);

    /*   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.black));
        }*/

        firebaseAuth = FirebaseAuth.getInstance();

        create_new_acc = findViewById(R.id.create_new_acc);
        ResetPassword = findViewById(R.id.ResetPassword);
        login = findViewById(R.id.login);
        email = findViewById(R.id.emailLogin);
        password = findViewById(R.id.passwordLogin);
        loginProgress = findViewById(R.id.loginProgress);

        Log.d("SIMRAN",""+email+" "+password);



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginProgress.setVisibility(View.VISIBLE);
                login.setVisibility(View.GONE);

                String emailText = email.getText().toString();
                String passwordText  = password.getText().toString();

                Log.d("SIMRAN",""+emailText+" "+passwordText);

                if(TextUtils.isEmpty(emailText) || TextUtils.isEmpty(passwordText)) {
                    Toast.makeText(login_signup.this, "Enter credentials", Toast.LENGTH_SHORT).show();

                    loginProgress.setVisibility(View.GONE);
                    login.setVisibility(View.VISIBLE);

                }else {

                    loginTheUserAndCreateSharedPref(email.getText().toString(), password.getText().toString());

                }
            }
        });

        ResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(login_signup.this, com.codedance.boom.activities.forgotPassword.ResetPassword.class));
            }
        });

        create_new_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login_signup.this, create_new_account.class));
            }
        });
    }



    private void loginTheUserAndCreateSharedPref(String email, String password) {



    firebaseAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
        @Override
        public void onSuccess(AuthResult authResult) {
            //create new shared pref to store the user
            startActivity(new Intent(login_signup.this, MainActivity.class));
            finish();

        }

    }).addOnFailureListener(e -> {
            // Sign-in failed
            // Check if it's an email-based failure
            if (e instanceof FirebaseAuthInvalidUserException) {
                // The email is not registered or the account is disabled
                // Show a message to the user indicating that the email is incorrect
                Toast.makeText(login_signup.this, "Email is incorrect", Toast.LENGTH_SHORT).show();
            } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                // The email is registered, but the password is incorrect
                // Show a message to the user indicating that the password is incorrect
                Toast.makeText(login_signup.this, "Password is incorrect", Toast.LENGTH_SHORT).show();
            } else {
                // Other errors, handle them as needed
                showAlertDialog_error();

                loginProgress.setVisibility(View.GONE);
                login.setVisibility(View.VISIBLE);
                //Toast.makeText(login_signup.this, "Sign-in failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void showAlertDialog_error() {
        AlertDialog.Builder AlertDialogError = new AlertDialog.Builder(this); // Use the activity's context

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View errorScreen = layoutInflater.inflate(R.layout.error_indicator_login_signup, null);



        AlertDialogError.setView(errorScreen);

        TextView tryAgain = errorScreen.findViewById(R.id.tryAgain);

        AlertDialog alertDialog = AlertDialogError.create();
        alertDialog.show();

        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }



}