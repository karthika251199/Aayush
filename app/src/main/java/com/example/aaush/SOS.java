package com.example.aaush;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class SOS extends AppCompatActivity {
    private static final int REQUEST_CALL=1;
    private EditText mEditTextNumber,message;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String userID;
    private Button send;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_o_s);

        mEditTextNumber = findViewById(R.id.phone);
        message = findViewById(R.id.msg);
        send = findViewById(R.id.bt_msg);
        Button bt_call = findViewById(R.id.bt_call);

        fAuth = FirebaseAuth.getInstance();
        fStore =FirebaseFirestore.getInstance();

        userID = fAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                        mEditTextNumber.setText(documentSnapshot.getString("phone"));
                    }
                });


        bt_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makePhoneCall();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                        sendSMS();
                    }else {
                        requestPermissions(new String[] {Manifest.permission.SEND_SMS},1);
                    }
                }
            }
        });

    }

        private void makePhoneCall(){
            String number = mEditTextNumber.getText().toString();
            if(number.trim().length() > 0){

                if(ContextCompat.checkSelfPermission(SOS.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){

                    ActivityCompat.requestPermissions(SOS.this,new String[] {Manifest.permission.CALL_PHONE},REQUEST_CALL);

                }else{
                    String dial = "tel:" +number;
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                }

            }else{
                Toast.makeText(SOS.this, "please enter number", Toast.LENGTH_SHORT).show();
            }
        }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CALL){
            if(grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                makePhoneCall();
            }else{
                Toast.makeText(this, "permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendSMS () {
        String phoneNo = mEditTextNumber.getText().toString().trim();
        String sms = message.getText().toString().trim();

        try {
            SmsManager smsManager=SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo,null, sms,null,null);
            Toast.makeText(this, "Message is sent",Toast.LENGTH_SHORT).show();
        } catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(this, "Faied to send msg",Toast.LENGTH_SHORT).show();
        }


    }
}