package com.cst2335.chua0012;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    public static final String TAG = "PROFILE_ACTIVITY";
    ImageButton imgbtn;
    Button button2;

    ActivityResultLauncher<Intent> myPictureTakerLauncher =
            registerForActivityResult( new ActivityResultContracts.StartActivityForResult()
                    ,new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == Activity.RESULT_OK)
                            { Intent data = result.getData();
                                Bitmap imgbitmap = (Bitmap) data.getExtras().get("data");
                                imgbtn.setImageBitmap(imgbitmap); // the imageButton
                            }
                            else if(result.getResultCode() == Activity.RESULT_CANCELED)
                                Log.i(TAG, "User refused to capture a picture.");
                        }
                    } );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextView editText = findViewById(R.id.editTextTextPersonName2);

        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREFS, Context.MODE_PRIVATE);
        String previous = sharedPreferences.getString(MainActivity.EMAIL, "");
        editText.setText(previous);
        Intent fromPrevious = getIntent();
        String input = fromPrevious.getStringExtra(MainActivity.EMAIL);
        editText.setText(input);


        SharedPreferences.Editor writer = sharedPreferences.edit();
        writer.putString(MainActivity.EMAIL, editText.getText().toString());
        writer.apply();

        button2 = findViewById(R.id.button2);
        ImageButton cam = findViewById( R.id.imageButton);
        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    myPictureTakerLauncher.launch(takePictureIntent);

                }
            }
        });

       imgbtn = findViewById( R.id.imageButton);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {

                    Intent nextPage = new Intent(ProfileActivity.this,   ChatRoomActivity.class  );

                    startActivity(    nextPage  );
                }
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "In onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "In onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "In onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "In onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "In onDestroy");
    }


}