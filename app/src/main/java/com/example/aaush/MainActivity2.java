package com.example.aaush;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity2 extends AppCompatActivity {

    private Button uploadBtn, showAllBtn, shareBtn;
    private ImageView imageView;
    private ProgressBar progressBar;
    private Uri imageUri;

    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("Image");
    private StorageReference reference = FirebaseStorage.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        uploadBtn = findViewById(R.id.upload_btn);
        showAllBtn = findViewById(R.id.showall_btn);
        shareBtn = findViewById(R.id.share_btn);

        imageView = findViewById(R.id.imageView);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        showAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity2.this, ShowActivity.class));
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,2 );
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageUri != null){
                      uploadToFirebase(imageUri);
                }else{
                    Toast.makeText(MainActivity2.this, "Please, select an image", Toast.LENGTH_SHORT).show();
                }
            }
        });



        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image ();
            }
        });


    }

    private void image() {
        StrictMode.VmPolicy.Builder builder=new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        BitmapDrawable drawable=(BitmapDrawable)imageView.getDrawable();
        Bitmap bitmap=drawable.getBitmap();
        File f=new File(getExternalCacheDir()+"/"+getResources().getString(R.string.app_name)+".png");
        Intent shareint;


        try {
            FileOutputStream outputStream= new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100,outputStream);

            outputStream.flush();
            outputStream.close();
            shareint= new Intent(Intent.ACTION_SEND);
            shareint.setType("image/*");
            shareint.putExtra(Intent.EXTRA_STREAM, imageUri.fromFile(f));
            shareint.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        }catch (Exception e){
            throw new RuntimeException(e);
        }
        startActivity(Intent.createChooser(shareint,"share image"));



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode ==2 && resultCode == RESULT_OK && data != null ){
                      imageUri = data.getData();
                      imageView.setImageURI(imageUri);
        }

    }

    private void uploadToFirebase(Uri uri){
        final StorageReference fileRef =  reference.child(System.currentTimeMillis() + "." + getFileExtention(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
               fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                   @Override
                   public void onSuccess(Uri uri) {
                       Model model = new Model(uri.toString());
                       String modelId = root.push().getKey();
                       root.child(modelId).setValue(model);
                       progressBar.setVisibility(View.INVISIBLE);
                       Toast.makeText(MainActivity2.this, "Uploaded Successfully!!", Toast.LENGTH_SHORT).show();
                       imageView.setImageResource(R.drawable.ic_baseline_add_photo_alternate_24);
                   }
               });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity2.this, "Uploading Failed!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtention(Uri mUri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }
}



























