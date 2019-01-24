package com.example.linhan.pictest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    Button btnSend;
    ImageView imageView;
    Button btnCamera;
    Button btnGallery;
    Button btnRotate;
    String imageString;
    TextView textView;
    Intent outputIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSend = findViewById(R.id.btnSend);
        btnCamera = findViewById(R.id.btnCamera);
        btnGallery = findViewById(R.id.btnGallery);
        btnRotate = findViewById(R.id.btnRotate);
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.txtTest);
        outputIntent = new Intent(this, OutputActivity.class);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, 100);
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                outputIntent.putExtra("image", imageString);
                startActivity(outputIntent);
            }
        });

        btnRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BitmapDrawable bd = (BitmapDrawable) imageView.getDrawable();
                Bitmap bitmap = bd.getBitmap();
                Bitmap rotated = rotateBitmap(bitmap, 200, 200);
                imageView.setImageBitmap(rotated);
                imageString = convertBitmapToString(rotated);
            }
        });
    }

    private Bitmap rotateBitmap(Bitmap bitmap, int width, int height) {
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        return rotatedBitmap;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0){
            if(resultCode == RESULT_OK){
                imageString = "";
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imageString = convertBitmapToString(bitmap);
                outputIntent.putExtra("image", imageString);
                imageView.setImageBitmap(bitmap);

            }
        }

        if(requestCode == 100){
            if(resultCode == RESULT_OK){
                Bitmap bitmap;
                imageString = "";
                Uri image = data.getData();
                try (InputStream is = getContentResolver().openInputStream(image)){
                    bitmap = BitmapFactory.decodeStream(is);
                    bitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, true);

                }catch(FileNotFoundException e){
                    bitmap = null;
                    e.printStackTrace();
                }catch (IOException e){
                    bitmap = null;
                    e.printStackTrace();
                }

                if(bitmap != null){
                    imageString = convertBitmapToString(bitmap);
                    imageView.setImageBitmap(bitmap);
                }else{
                    Toast.makeText(this, "Error: error loading image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private String convertBitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bs);
        byte [] bytes = bs.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
}
