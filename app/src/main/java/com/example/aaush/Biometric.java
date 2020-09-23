package com.example.aaush;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import java.util.concurrent.Executor;

public class Biometric extends AppCompatActivity implements View.OnClickListener {

    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biometric);

        login=findViewById(R.id.login);
        login.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

        if(isBiometricAvailable()){
            showBiometricPrompt();
        }

    }

    public void showBiometricPrompt() {

        Executor executor= ContextCompat.getMainExecutor(this);
        BiometricPrompt biometricPrompt=new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);

                Toast.makeText(com.example.aaush.Biometric.this, errString,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result){
                super.onAuthenticationSucceeded(result);
                Toast.makeText(com.example.aaush.Biometric.this, "You have successfully logged in.",Toast.LENGTH_SHORT).show();

                startActivity(new Intent(getApplicationContext(), Gridview.class));

            }

            @Override
            public void onAuthenticationFailed(){
                super.onAuthenticationFailed();
                Toast.makeText(com.example.aaush.Biometric.this, "Authentication Failed",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });
        BiometricPrompt.PromptInfo promptInfo=new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Authentication")
                .setSubtitle("Authenticate with biometric")
                .setNegativeButtonText("Cancel")
                .setConfirmationRequired(true)
                .build();

        biometricPrompt.authenticate(promptInfo);
    }

    private boolean isBiometricAvailable() {
        BiometricManager biometricManager= BiometricManager.from(this);
        int can=biometricManager.canAuthenticate();

        if(can==BiometricManager.BIOMETRIC_SUCCESS){
            Toast.makeText(this,"Biometric is available.",Toast.LENGTH_SHORT).show();
            return true;
        }

        else if(can==BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE){
            Toast.makeText(this,"Biometric hardware is not available.",Toast.LENGTH_SHORT).show();
            return false;
        }

        else if(can==BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE){
            Toast.makeText(this,"No Biometric hardware.",Toast.LENGTH_SHORT).show();
            return false;
        }

        else if(can==BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED){
            Toast.makeText(this,"Biometric not enrolled.",Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
}

