package com.example.aaush;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class HomePage extends AppCompatActivity {
    Button AddAppointments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home_page);

        AddAppointments = findViewById(R.id.addAppointment);
        AddAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AddAppointment.class));
            }
        });
    }
   /* public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();*/

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("profile");
        menu.add("share");
        menu.add("logout");


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getTitle()=="profile") {
            //Toast.makeText(this,"profile is Selected", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),Profile.class));
        }

        else if(item.getTitle()=="share") {
            Toast.makeText(this ,"share is Selected", Toast.LENGTH_SHORT).show();}

        else if(item.getTitle()=="logout") {
            //Toast.makeText( this,"logout is Selected", Toast.LENGTH_SHORT).show();
               /* FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),Login.class));
                finish();*/
            startActivity(new Intent(getApplicationContext(),Login.class));

        }


        return super.onOptionsItemSelected(item);
    }



}


