package com.example.andreeagritco.exam.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.andreeagritco.exam.R;
import com.example.andreeagritco.exam.utils.NetworkUtils;

import java.util.concurrent.ThreadPoolExecutor;

public class MainActivity extends AppCompatActivity {

    private Button clientButton;
    private Button theaterButton;
    private Button adminButton;
    private View.OnClickListener listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clientButton = findViewById(R.id.clientButton);
        theaterButton = findViewById(R.id.theatherButton);
        adminButton = findViewById(R.id.adminButton);

        listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.clientButton) {

                    Log.i("clicks", "You clicked CLIENT button.");
                    Intent i = new Intent(MainActivity.this, ClientActivity.class);
                    startActivity(i);

                } else if (v.getId() == R.id.theatherButton) {
                    Log.i("clicks", "You clicked THEATER button.");
                    if (NetworkUtils.isNetworkAvailable(getSystemService(Context.CONNECTIVITY_SERVICE))) {
                        Intent i = new Intent(MainActivity.this, TheaterActivity.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(getApplicationContext(), "Network not available!", Toast.LENGTH_LONG).show();

                    }


                } else {
                    Log.i("clicks", "You clicked ADMIN button.");

                    if (NetworkUtils.isNetworkAvailable(getSystemService(Context.CONNECTIVITY_SERVICE))) {
                        Intent i = new Intent(MainActivity.this, AdminActivity.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(getApplicationContext(), "Network not available!", Toast.LENGTH_LONG).show();

                    }


                }


            }
        };
        clientButton.setOnClickListener(listener);
        theaterButton.setOnClickListener(listener);
        adminButton.setOnClickListener(listener);

    }
}
