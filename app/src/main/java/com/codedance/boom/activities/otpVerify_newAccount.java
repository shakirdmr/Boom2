package com.codedance.boom.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.codedance.boom.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.os.AsyncTask;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class otpVerify_newAccount extends AppCompatActivity {

    TextView otpEMAIL;
    DatabaseReference databaseReference;
    ProgressBar   progressCreatingnewAccount ;
    FirebaseAuth firebaseAuth;
    public String verificationCode;
    Button verifyCODE;
    EditText editTextVerificationCode;
    private static final String EMAIL_ADDRESS = "aly66416@gmail.com"; // Replace with your email
    private static final String EMAIL_PASSWORD = "hrju wfww yclj xpou"; // Replace with your email password

    String email;
    String fullName ;
    String password;
    ImageView goBackHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verify);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.orange));
        }

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Retrieve the HashMap from the Intent extras
        Intent intent = getIntent();
        
            HashMap<String, String> receivedMap = (HashMap<String, String>) intent.getSerializableExtra("mapOfDetails");
            
                // Now you have the HashMap containing the details
                 email = receivedMap.get("email");
                 fullName = receivedMap.get("fullName");
                 password = receivedMap.get("password");

                // Use these values as needed in this activity





        otpEMAIL = findViewById(R.id.otpEMAIL);
        goBackHome = findViewById(R.id.goBackHome);
        progressCreatingnewAccount = findViewById(R.id.progressCreatingnewAccount);
        editTextVerificationCode = findViewById(R.id.editTextVerificationCode);
        verifyCODE = findViewById(R.id.verifyCODE);


        otpEMAIL.setText("enter otp sent to email "+email);
        sendVerificationEmail(email);
        
        

        verifyCODE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = editTextVerificationCode.getText().toString().trim();
                if (!code.isEmpty()) {
                    verifyCode(code);
                } else {
                    Toast.makeText(otpVerify_newAccount.this, "Please enter the verification code.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        goBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void sendVerificationEmail(String email) {
                // Generate a new verification code
                 verificationCode = generateVerificationCode();
        
                // Email properties
                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", "smtp.gmail.com"); // Use the appropriate SMTP server
                props.put("mail.smtp.port", "587"); // Use the appropriate port
        
                Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(EMAIL_ADDRESS, EMAIL_PASSWORD);
                    }
                });
        
                try {
                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(EMAIL_ADDRESS));
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email)); // Replace with the recipient's email
                    message.setSubject("Verification Code - BOOM");
                    message.setText("Hii 👋👋 \n Your verification code for BOOM app is :  \n " + verificationCode);
        
                    new SendEmailTask().execute(message);
                } catch (MessagingException e) {
                    Log.d("SIMRAN",""+e.getMessage());
                    Toast.makeText(this, "error 84-otpVerify "+e.getMessage(), Toast.LENGTH_LONG).show();
                }
    }

    private String generateVerificationCode() {
            // Generate a 6-digit random verification code
            int min = 100000;
            int max = 999999;
            return String.valueOf(min + (int) (Math.random() * (max - min + 1)));
        }

    private class SendEmailTask extends AsyncTask<Message, Void, Boolean> {
                @Override
                protected Boolean doInBackground(Message... messages) {
                    try {
                        Transport.send(messages[0]);
                        return true;
                    } catch (MessagingException e) {
                        Log.d("SIMRAN",""+e.getMessage());
                        //Toast.makeText(otpVerify.this, "HELLO____"+e.getMessage(), Toast.LENGTH_LONG).show();
        
                        return false;
                    }
                }
        
                @Override
                protected void onPostExecute(Boolean result) {
                    super.onPostExecute(result);
                    if (result) {
                        Toast.makeText(otpVerify_newAccount.this, "Email sent successfully", Toast.LENGTH_SHORT).show();
        
        
                    } else {
                        Toast.makeText(otpVerify_newAccount.this, "Failed to send email", Toast.LENGTH_SHORT).show();
                    }
                }
    }

    private void verifyCode(String code) {

        Log.d("SIMRAN","__"+verificationCode+"__"+code);
            if(code.equals(verificationCode))

            {
                Toast.makeText(this, "OTP verified. Please wait.", Toast.LENGTH_SHORT).show();

                createNewAccount(fullName,email,password);
                //MAKE A SHARED PREF
                startActivity(new Intent(otpVerify_newAccount.this,MainActivity.class));
                finishAffinity();
            }
            else {
                Toast.makeText(this, "Wrong OTP given. Please try again.", Toast.LENGTH_SHORT).show();
            }
    }

    private void createNewAccount(String name, String email, String pass) {

        progressCreatingnewAccount.setVisibility(View.VISIBLE);
        verifyCODE.setVisibility(View.GONE);

        firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                Toast.makeText(otpVerify_newAccount.this, "authenticartion sucess", Toast.LENGTH_SHORT).show();

                //NOW USER IS CREATED ADD VALUES TO DATABSE
                HashMap<String, Object> map = new HashMap<>();
                map.put("name",name);
                map.put("email",email);
                map.put("id",firebaseAuth.getCurrentUser().getUid());

                databaseReference.child("users").child(firebaseAuth
                                .getCurrentUser().getUid()).setValue(map)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                progressCreatingnewAccount.setVisibility(View.GONE);
                                verifyCODE.setVisibility(View.VISIBLE);

                                if(task.isSuccessful())
                                {
                                    //CREATE SHARED PREFF FIRST
                                    Toast.makeText(otpVerify_newAccount.this, "User Registered", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent( otpVerify_newAccount.this,MainActivity.class ));
                                    finish();
                                }
                                else {
                                    Toast.makeText(otpVerify_newAccount.this, "Failed. User Registered", Toast.LENGTH_SHORT).show();

                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {


                                progressCreatingnewAccount.setVisibility(View.GONE);
                                verifyCODE.setVisibility(View.VISIBLE);
                                Toast.makeText(otpVerify_newAccount.this, "Failed."+e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });



            }
        });
    }
}