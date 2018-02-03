package com.example.andreeagritco.exam26.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.andreeagritco.exam26.R;
import com.example.andreeagritco.exam26.api.ApiClient;
import com.example.andreeagritco.exam26.api.ApiInterface;
import com.example.andreeagritco.exam26.db.AppDatabase;
import com.example.andreeagritco.exam26.model.Project;
import com.example.andreeagritco.exam26.model.Type;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddActivity extends AppCompatActivity {

    private EditText nameText;
    private EditText budgetText;
    private Spinner typeSpinner;
    private Button submitButton;

    private ProgressDialog progressDialog;
    private static ApiInterface apiInterface;


    final View.OnClickListener l = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Log.i("clicks", "You clicked submit button.");
            Project project = new Project(nameText.getText().toString(), Integer.valueOf(budgetText.getText().toString()), Type.valueOf(typeSpinner.getSelectedItem().toString()));

            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Loading.....");
            progressDialog.show();

            Call<Project> call = apiInterface.addIdea(project);

            call.enqueue(new Callback<Project>() {
                @Override
                public void onResponse(Call<Project> call, Response<Project> response) {
                    Toast.makeText(getApplicationContext(), "Successfully added!", Toast.LENGTH_LONG).show();
                    onBackPressed();
                }

                @Override
                public void onFailure(Call<Project> call, Throwable t) {

                    Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_LONG).show();
                    //   Intent i = new Intent(AddActivity.this,IdeasActivity.class);
                    //  startActivity(i);
                    // finish();
                    onBackPressed();

                }
            });


        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        progressDialog = new ProgressDialog(this);

        nameText = findViewById(R.id.addNameText);
        budgetText = findViewById(R.id.addBudgetText);
        typeSpinner = findViewById(R.id.addTypeSpinner);
        submitButton = findViewById(R.id.addSubmitButton);

        populateSpinner();

        submitButton.setOnClickListener(l);


    }

    private void populateSpinner() {
        List<Type> types = Arrays.asList(Type.values());
        ArrayAdapter<Type> adapter = new ArrayAdapter<Type>(this, android.R.layout.simple_spinner_item, types);

        typeSpinner.setAdapter(adapter);
    }
}
