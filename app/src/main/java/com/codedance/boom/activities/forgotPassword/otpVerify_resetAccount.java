package com.codedance.boom.activities.forgotPassword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codedance.boom.R;
import com.codedance.boom.activities.MainActivity;
import com.codedance.boom.activities.otpVerify_newAccount;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class otpVerify_resetAccount extends AppCompatActivity {

    EditText editTextVerificationCode;
    TextView otpEMAIL;
    Button verifyCODE;
    public String verificationCode;
    private static final String EMAIL_ADDRESS = "aly66416@gmail.com"; // Replace with your email
    private static final String EMAIL_PASSWORD = "hrju wfww yclj xpou"; // Replace with your email password


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verify_reset_account);

        Intent it = getIntent();
        String email = it.getStringExtra("email");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.blue));
        }

        editTextVerificationCode = findViewById(R.id.editTextVerificationCode);
        otpEMAIL = findViewById(R.id.otpEMAIL);
        verifyCODE  =findViewById(R.id.verifyCODE);

        otpEMAIL.setText("enter otp sent to email "+email);
        sendVerificationEmail(email);

        verifyCODE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = editTextVerificationCode.getText().toString().trim();
                if (!code.isEmpty()) {
                    verifyCode(code);
                } else {
                    Toast.makeText(otpVerify_resetAccount.this, "Please enter the verification code.", Toast.LENGTH_SHORT).show();
                }
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
            message.setText("Hii 👋👋 \n Your verification to reset your account for BOOM app is :  \n " + verificationCode);

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
                Toast.makeText(otpVerify_resetAccount.this, "Email sent successfully", Toast.LENGTH_SHORT).show();


            } else {
                Toast.makeText(otpVerify_resetAccount.this, "Failed to send email", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void verifyCode(String code) {

        Log.d("SIMRAN","__"+verificationCode+"__"+code);
        if(code.equals(verificationCode))

        {
            Toast.makeText(this, "OTP verified. Please wait.", Toast.LENGTH_SHORT).show();

            createNewPassword();
            //MAKE A SHARED PREF
              finishAffinity();
        }
        else {
            Toast.makeText(this, "Wrong OTP given. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private void createNewPassword() {
        startActivity(new Intent(otpVerify_resetAccount.this,setNewPassword.class));
   finish();
    }


}