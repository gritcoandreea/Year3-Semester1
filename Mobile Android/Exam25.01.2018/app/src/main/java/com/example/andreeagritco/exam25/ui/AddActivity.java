package com.example.andreeagritco.exam25.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.andreeagritco.exam25.R;
import com.example.andreeagritco.exam25.api.ApiClient;
import com.example.andreeagritco.exam25.api.ApiInterface;
import com.example.andreeagritco.exam25.model.Game;
import com.example.andreeagritco.exam25.model.Status;
import com.example.andreeagritco.exam25.model.Type;
import com.example.andreeagritco.exam25.utils.NetworkUtils;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddActivity extends AppCompatActivity {

    private EditText nameText;
    private Spinner typeSpinner;
    private Button submitButton;
    private ProgressDialog progressDialog;

    private static ApiInterface apiInterface;

    private View.OnClickListener submitListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Game game = new Game(nameText.getText().toString(), 1, Type.valueOf(typeSpinner.getSelectedItem().toString()), Status.available);

            if (NetworkUtils.isNetworkAvailable(getSystemService(Context.CONNECTIVITY_SERVICE))) {
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Loading.....");
                progressDialog.show();

                Call<Game> call = apiInterface.addGame(game);

                call.enqueue(new Callback<Game>() {
                    @Override
                    public void onResponse(Call<Game> call, Response<Game> response) {

                        if (response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Successfully added!", Toast.LENGTH_LONG).show();
                            onBackPressed();
                        } else {
                            Toast.makeText(getApplicationContext(), "Error when trying to add the game!", Toast.LENGTH_LONG).show();
                            onBackPressed();
                        }
                    }

                    @Override
                    public void onFailure(Call<Game> call, Throwable t) {

                        Toast.makeText(getApplicationContext(), "Error when trying to establish connection with the server!", Toast.LENGTH_LONG).show();
                        onBackPressed();

                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "NO INTERNET CONNECTION", Toast.LENGTH_LONG).show();
                Log.i("info", "Check the connection");
                onBackPressed();
            }


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        nameText = findViewById(R.id.addGameNameText);
        typeSpinner = findViewById(R.id.typeSpinner);
        submitButton = findViewById(R.id.submitGameButton);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        progressDialog = new ProgressDialog(this);

        populateSpinner();

        submitButton.setOnClickListener(submitListener);
    }

    private void populateSpinner() {
        List<Type> types = Arrays.asList(Type.values());
        ArrayAdapter<Type> adapter = new ArrayAdapter<Type>(this, android.R.layout.simple_spinner_item, types);

        typeSpinner.setAdapter(adapter);

    }
}
