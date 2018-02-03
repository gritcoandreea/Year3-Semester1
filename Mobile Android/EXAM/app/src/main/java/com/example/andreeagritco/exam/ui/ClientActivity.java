package com.example.andreeagritco.exam.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.andreeagritco.exam.R;
import com.example.andreeagritco.exam.api.ApiClient;
import com.example.andreeagritco.exam.api.ApiInterface;
import com.example.andreeagritco.exam.db.AppDatabase;
import com.example.andreeagritco.exam.model.IdObject;
import com.example.andreeagritco.exam.model.Seat;
import com.example.andreeagritco.exam.model.Status;
import com.example.andreeagritco.exam.utils.NetworkUtils;
import com.example.andreeagritco.exam.utils.SeatListAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientActivity extends AppCompatActivity {

    private ListView seatsListView;
    private ProgressDialog progressDialog;
    private static ApiInterface apiInterface;
    private static AppDatabase appDatabase;
    private List<Seat> seats = new ArrayList<>();

    private SeatListAdapter seatListAdapter;
    private Handler handler = new Handler();
    private SearchForInternet searchForInternet = new SearchForInternet();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        seatsListView = findViewById(R.id.clientSeatsListView);

        seatListAdapter = new SeatListAdapter(seats, getLayoutInflater(), true);
        seatsListView.setAdapter(seatListAdapter);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        appDatabase = AppDatabase.getInstancce(getApplicationContext());
        progressDialog = new ProgressDialog(this);

        seatsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog alertDialog = new AlertDialog.Builder(ClientActivity.this).create();
                alertDialog.setTitle("Warning");
                alertDialog.setMessage("Do you want to reserve this seat?");
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //RESERVE SEAT
                        if (NetworkUtils.isNetworkAvailable(getSystemService(Context.CONNECTIVITY_SERVICE))) {

                            progressDialog.setIndeterminate(true);
                            progressDialog.setMessage("Loading...");
                            progressDialog.show();


                            Call<Seat> call = apiInterface.reserveSeat(new IdObject(seats.get(position).getId()));

                            call.enqueue(new Callback<Seat>() {
                                @SuppressLint("StaticFieldLeak")
                                @Override
                                public void onResponse(Call<Seat> call, Response<Seat> response) {
                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }

                                    if (response.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "SUCCESSFULLY RENTED!", Toast.LENGTH_LONG).show();

                                        final Seat game = response.body();
                                        final Status status = game.getStatus();

                                        if (!seats.get(position).getStatus().toString().equals(status.toString())) {

                                            new AsyncTask<Void, Void, Void>() {
                                                @Override
                                                protected Void doInBackground(Void... voids) {
                                                    boolean check = false;
                                                    List<Seat> seats = appDatabase.seatDao().getAll();
//
                                                    for (Seat s : seats) {
                                                        if (s.getId() == game.getId()) {
                                                            s.setStatus(status);
                                                            appDatabase.seatDao().update(s);
                                                            check = true;
                                                            break;
                                                        }
                                                    }
                                                    if(!check){
                                                        appDatabase.seatDao().add(game);
                                                    }


                                                    return null;
                                                }

                                            }.execute();


                                            Intent i = new Intent(ClientActivity.this, ReservedSeatsAcivity.class);
                                            startActivity(i);


                                        } else {
                                            Toast.makeText(getApplicationContext(), "Error when reserving this seat!", Toast.LENGTH_LONG).show();
                                        }

                                        getAll();

                                    }
                                }
                                @Override
                                public void onFailure(Call<Seat> call, Throwable t) {

                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }
                                    Toast.makeText(getApplicationContext(), "Error when trying to establish a connection with the server!", Toast.LENGTH_LONG).show();

                                }
                            });

                        } else {
                            Toast.makeText(getApplicationContext(), "NETWORK IS NOT AVAILABLE!YOU CAN'T RENT A GAME!", Toast.LENGTH_LONG).show();
                        }

                    }
                });

                alertDialog.show();


            }
        });


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

            Call<List<Seat>> call = apiInterface.getSeats();

            call.enqueue(new Callback<List<Seat>>() {
                @Override
                public void onResponse(Call<List<Seat>> call, Response<List<Seat>> response) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    if (response.isSuccessful()) {
                        seats.clear();
                        seats.addAll(response.body());

                        seatListAdapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(getApplicationContext(), "Error when getting available seats!", Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<List<Seat>> call, Throwable t) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    Toast.makeText(getApplicationContext(), "Error when trying to establish connection with the server!", Toast.LENGTH_LONG).show();

                }
            });

        } else {
            Toast.makeText(getApplicationContext(), "NO INTERNET CONNECTION", Toast.LENGTH_LONG).show();
            Log.i("info", "Check the connection");

            seats.clear();
            seatListAdapter.notifyDataSetChanged();
            handler.postDelayed(searchForInternet, 20000);
        }
    }


    private class SearchForInternet implements Runnable {
        @Override
        public void run() {
            Log.i("info", "Check the connection AICI");
            getAll();
        }
    }


}
