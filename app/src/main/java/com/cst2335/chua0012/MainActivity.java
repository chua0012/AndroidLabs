package com.cst2335.chua0012;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    //androidx.constraintlayout.widget.ConstraintLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = findViewById(R.id.button);
        Switch sw = (Switch) findViewById(R.id.switch1);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), R.string.toast_message, Toast.LENGTH_LONG).show();
            }
        });

        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton cb, boolean b) {
                Snackbar snackbar;
                if (b) {
                    snackbar = Snackbar.make(cb, R.string.SwitchOn, Snackbar.LENGTH_SHORT)
                            .setAction("undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                }
                else {
                    snackbar = Snackbar.make(cb, R.string.SwitchOff, Snackbar.LENGTH_SHORT);
                }
                snackbar.show();
            }
        });
    }
}