package com.example.aaush;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Calendar extends AppCompatActivity {

    EditText title;
   // EditText location;
    EditText descreption;
    Button addevent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        addevent =  findViewById(R.id.btnAdd);
        descreption = findViewById(R.id.etDesc);
        //location = findViewById(R.id.etLocation);
        title = findViewById(R.id.etTitle);

        addevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!title.getText().toString().isEmpty()){

                    Intent intent = new Intent(Intent.ACTION_INSERT);
                    intent.setData(CalendarContract.Events.CONTENT_URI);
                    intent.putExtra(CalendarContract.Events.TITLE,title.getText().toString());
                    intent.putExtra(CalendarContract.Reminders.DESCRIPTION,descreption.getText().toString());
                   // intent.putExtra(CalendarContract.Reminders.EVENT_END_TIMEZONE,location.getText().toString());
                    intent.putExtra(CalendarContract.Events.ALL_DAY, "true");
                    intent.putExtra(Intent.EXTRA_EMAIL,"ponnada1999@gmail.com");

                    if(intent.resolveActivity(getPackageManager())!=null){

                        startActivity(intent);

                    }else{
                        Toast.makeText(Calendar.this, "The action cannot be supported!!", Toast.LENGTH_SHORT).show();
                    }



                } else{
                    Toast.makeText(Calendar.this, "please fill all the fields ", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
}