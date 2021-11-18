package com.example.mobproject;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText sendEmail;
    private Button newPass;
    private Boolean validSendEmail;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);

        sendEmail = findViewById(R.id.send_email);
        newPass = findViewById(R.id.new_pass);

        newPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailValidation();
                //finish();
            }
        });
    }

    private void emailValidation(){
        //Check login email address
        if(sendEmail.getText().toString().isEmpty()){
            sendEmail.setError(getResources().getString(R.string.email_error));
            validSendEmail = false;
        } else if(!Patterns.EMAIL_ADDRESS.matcher(sendEmail.getText().toString()).matches()){
            sendEmail.setError(getResources().getString(R.string.error_invalid_email));
            validSendEmail = false;
        } else{
            validSendEmail = true;
        }

        if(validSendEmail){
            Toast.makeText(getApplicationContext(),
                    "Password generated!",
                    Toast.LENGTH_SHORT).show();
            finish();
        }

    }
}
