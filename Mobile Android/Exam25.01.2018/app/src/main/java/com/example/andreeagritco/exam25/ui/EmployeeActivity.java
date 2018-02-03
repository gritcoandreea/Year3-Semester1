package com.example.andreeagritco.exam25.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.andreeagritco.exam25.R;
import com.example.andreeagritco.exam25.api.ApiClient;
import com.example.andreeagritco.exam25.api.ApiInterface;
import com.example.andreeagritco.exam25.db.AppDatabase;
import com.example.andreeagritco.exam25.model.Game;
import com.example.andreeagritco.exam25.model.IdObject;
import com.example.andreeagritco.exam25.utils.GameListAdapter;
import com.example.andreeagritco.exam25.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeActivity extends AppCompatActivity {

    private Button addGameButton;
    private ListView gamesListView;
    private ProgressDialog progressDialog;

    private static ApiInterface apiInterface;
    private List<Game> games = new ArrayList<>();
    private GameListAdapter gamesListAdapter;
    private View.OnClickListener addGameListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (NetworkUtils.isNetworkAvailable(getSystemService(Context.CONNECTIVITY_SERVICE))) {
                Intent i = new Intent(EmployeeActivity.this, AddActivity.class);
                startActivity(i);


            } else {
                Toast.makeText(getApplicationContext(), "NETWORK NOT AVAILABLE!", Toast.LENGTH_LONG);
                Log.i("networkProblem", "There is no internet connection for adding a game!");
                getAll();
            }
        }
    };
    private AdapterView.OnItemLongClickListener removeListener = new AdapterView.OnItemLongClickListener() {

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

            AlertDialog alertDialog = new AlertDialog.Builder(EmployeeActivity.this).create();
            alertDialog.setTitle("Warning");
            alertDialog.setMessage("Do you want to delete this game?");
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    //DELETE MOVIE
                    if (NetworkUtils.isNetworkAvailable(getSystemService(Context.CONNECTIVITY_SERVICE))) {

                        progressDialog.setIndeterminate(true);
                        progressDialog.setMessage("Loading...");
                        progressDialog.show();


                        Call<Game> call = apiInterface.removeGame(new IdObject(games.get(position).getId()));

                        call.enqueue(new Callback<Game>() {
                            @SuppressLint("StaticFieldLeak")
                            @Override
                            public void onResponse(Call<Game> call, Response<Game> response) {
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }

                                if (response.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "SUCCESSFULLY DELETED!", Toast.LENGTH_LONG).show();
                                    getAll();

                                } else {
                                    Toast.makeText(getApplicationContext(), "Error when deleting this movie!", Toast.LENGTH_LONG).show();
                                }


                            }

                            @Override
                            public void onFailure(Call<Game> call, Throwable t) {

                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                                Toast.makeText(getApplicationContext(), "Error when trying to establosh a connection with the server!", Toast.LENGTH_LONG).show();

                            }
                        });

                    } else {
                        Toast.makeText(getApplicationContext(), "NETWORK IS NOT AVAILABLE!YOU CAN'T DELETE A MOVIE!", Toast.LENGTH_LONG).show();
                    }

                }
            });

            alertDialog.show();


            return true;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        addGameButton = findViewById(R.id.addGameButton);
        gamesListView = findViewById(R.id.employeeListView);

        gamesListAdapter = new GameListAdapter(games, getLayoutInflater(), false);
        gamesListView.setAdapter(gamesListAdapter);

        gamesListView.setOnItemLongClickListener(removeListener);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        progressDialog = new ProgressDialog(this);

        addGameButton.setOnClickListener(addGameListener);


    }

    @Override
    protected void onResume() {
        super.onResume();
        getAll();
    }

    private void getAll() {

        if (NetworkUtils.isNetworkAvailable(getSystemService(Context.CONNECTIVITY_SERVICE))) {
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

            Call<List<Game>> call = apiInterface.getAll();

            call.enqueue(new Callback<List<Game>>() {
                @SuppressLint("StaticFieldLeak")
                @Override
                public void onResponse(Call<List<Game>> call, Response<List<Game>> response) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }

                    if (response.isSuccessful()) {

                        games.clear();
                        games.addAll(response.body());

                        gamesListAdapter.notifyDataSetChanged();

                    } else {

                        Toast.makeText(getApplicationContext(), "Error when trying to get list of games!", Toast.LENGTH_LONG).show();
                        Log.i("info", "Check the connection");
                    }


                }

                @Override
                public void onFailure(Call<List<Game>> call, Throwable t) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }

                    Toast.makeText(getApplicationContext(), "Error when trying to connect with the server!", Toast.LENGTH_LONG).show();
                    Log.i("info", "Check the connection");

                }
            });


        } else {
            Toast.makeText(getApplicationContext(), "NO INTERNET CONNECTION", Toast.LENGTH_LONG).show();
            Log.i("info", "Check the connection");

            games.clear();
            gamesListAdapter.notifyDataSetChanged();
        }

    }

}
