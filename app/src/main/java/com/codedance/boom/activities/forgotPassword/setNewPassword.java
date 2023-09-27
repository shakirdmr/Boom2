package com.codedance.boom.activities.forgotPassword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codedance.boom.R;
import com.codedance.boom.activities.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class setNewPassword extends AppCompatActivity {
EditText newPasswordET;
Button newPasswordDone;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.blue));
        }

        newPasswordET  =findViewById(R.id.newPassword);
        newPasswordDone  =findViewById(R.id.newPasswordDone);



        newPasswordDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newPassword  =newPasswordET.getText().toString().trim();

                updatePasswordAndGotoHome(newPassword);
            }
        });


    }

    private void updatePasswordAndGotoHome(String newPassword) {

        user.updatePassword(newPassword)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Password updated successfully
                        Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show();

                        //CREATE A SHARED PREF

                        startActivity(new Intent(setNewPassword.this, MainActivity.class));


                    } else {
                        // Password update failed
                        Toast.makeText(this, "Password update failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}