package com.codedance.boom.activities.forgotPassword;

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
import android.widget.Toast;

import com.codedance.boom.R;

public class ResetPassword extends AppCompatActivity {

    EditText editTextVerificationCode;
    Button SendCodeAndResetAccount;
    ImageView goBackHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.blue));
        }
        goBackHome = findViewById(R.id.goBackHome);
        SendCodeAndResetAccount = findViewById(R.id.SendCodeAndResetAccount);
        editTextVerificationCode = findViewById(R.id.editTextVerificationCode);

        SendCodeAndResetAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editTextVerificationCode.getText().toString().isEmpty())
                    editTextVerificationCode.setError("Required");
                    //Toast.makeText(ResetPassword.this, "Enter Email", Toast.LENGTH_SHORT).show();
                else {
                    Intent it = new Intent(ResetPassword.this, emailSent.class);
                    Log.d("SIMRAN",""+editTextVerificationCode.getText().toString().trim());
                    it.putExtra("email", editTextVerificationCode.getText().toString().trim());
                    startActivity(it);
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
}