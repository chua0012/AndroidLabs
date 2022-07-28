package com.cst2335.chua0012;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private EditText editText;
    private Button button;

    public static final String SHARED_PREFS = "SP";
    public static final String EMAIL = "email";

    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editTextTextEmailAddress_input);
        button = findViewById(R.id.button);

        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        //Read preferences
        String previous = prefs.getString(EMAIL, "");
        editText.setText(previous);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {

                    Intent nextPage = new Intent(MainActivity.this,   ProfileActivity.class  );
                    String userTyped = editText.getText().toString();
                    nextPage.putExtra(EMAIL, userTyped);

                    startActivity(    nextPage  );
                }
            }
        });

    }
}