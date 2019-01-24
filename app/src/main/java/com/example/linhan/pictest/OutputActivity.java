package com.example.linhan.pictest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;

public class OutputActivity extends Activity {
    ImageView imgOutput;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_output);
        imgOutput = findViewById(R.id.imageView2);
        Intent intent = getIntent();
        imgOutput.setImageBitmap(convertStringToBitmap(intent.getStringExtra("image")));
    }

    private Bitmap convertStringToBitmap(String image) {
        try{
            byte[] bytes = Base64.decode(image, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bytes, 0 ,bytes.length);
            return bitmap;
        }catch (Exception e){
            e.getMessage();
            return null;
        }
    }
}
