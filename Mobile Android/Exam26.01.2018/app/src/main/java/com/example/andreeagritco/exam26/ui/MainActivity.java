package com.example.andreeagritco.exam26.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.andreeagritco.exam26.R;
import com.example.andreeagritco.exam26.utils.NetworkUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button ideasButton;
    private Button projectsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ideasButton = findViewById(R.id.ideasButton);
        projectsButton = findViewById(R.id.projectsButton);

        ideasButton.setOnClickListener(this);
        projectsButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.ideasButton: {
                Log.i("clicks", "You clicked ideas button.");
                Intent i = new Intent(MainActivity.this,IdeasActivity.class);
                startActivity(i);
                break;
            }

            case R.id.projectsButton: {
                Log.i("clicks","You clicked projects button.");

                if(NetworkUtils.isNetworkAvailable(getSystemService(Context.CONNECTIVITY_SERVICE))) {
                    Intent i = new Intent(MainActivity.this, ProjectsActivity.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Network not available!", Toast.LENGTH_LONG).show();
                }
                break;
            }

        }
    }

}
