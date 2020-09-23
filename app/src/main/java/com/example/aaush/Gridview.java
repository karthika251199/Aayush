package com.example.aaush;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class Gridview extends AppCompatActivity {

    ImageButton reminders,reports,camara,share;
    TextView Name;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_gridview);

        reminders = findViewById(R.id.reminders);
        reports = findViewById(R.id.reports);
        camara = findViewById(R.id.camara);
        share = findViewById(R.id.share);
        Name = findViewById(R.id.Name);

        fAuth = FirebaseAuth.getInstance();
        fStore =FirebaseFirestore.getInstance();

        userID = fAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                Name.setText(documentSnapshot.getString("fName"));

            }
        });

        reminders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Appointments.class));
            }
        });

        reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Appointments.class));
            }
        });

        camara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Appointments.class));
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Appointments.class));
            }
        });


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("profile");
        menu.add("share");
        menu.add("logout");


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getTitle() == "profile") {
            //Toast.makeText(this,"profile is Selected", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), Profile.class));
        } else if (item.getTitle() == "share") {
            Toast.makeText(this, "share is Selected", Toast.LENGTH_SHORT).show();
        } else if (item.getTitle() == "logout") {
            //Toast.makeText( this,"logout is Selected", Toast.LENGTH_SHORT).show();
               /* FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),Login.class));
                finish();*/
            startActivity(new Intent(getApplicationContext(), Login.class));

        }


        return super.onOptionsItemSelected(item);

    }

}