package com.example.andreeagritco.exam25.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.andreeagritco.exam25.R;
import com.example.andreeagritco.exam25.utils.NetworkUtils;

public class MainActivity extends AppCompatActivity {

    private Button clientButton;
    private Button employeeButton;

    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.clientButton) {
                Log.i("clicks", "You clicked CLIENT button.");
                Intent i = new Intent(MainActivity.this, ClientActivity.class);
                startActivity(i);
            } else {
                Log.i("clicks", "You clicked EMPLOYEE button.");

                if (NetworkUtils.isNetworkAvailable(getSystemService(Context.CONNECTIVITY_SERVICE))) {
                    Intent i = new Intent(MainActivity.this, EmployeeActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "Network not available!", Toast.LENGTH_LONG).show();
                }

            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clientButton = findViewById(R.id.clientButton);
        employeeButton = findViewById(R.id.employeeButton);


        clientButton.setOnClickListener(clickListener);
        employeeButton.setOnClickListener(clickListener);


    }
}
