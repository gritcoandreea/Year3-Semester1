package com.example.andreeagritco.exam.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.andreeagritco.exam.R;
import com.example.andreeagritco.exam.api.ApiClient;
import com.example.andreeagritco.exam.api.ApiInterface;
import com.example.andreeagritco.exam.db.AppDatabase;
import com.example.andreeagritco.exam.model.IdObject;
import com.example.andreeagritco.exam.model.Seat;
import com.example.andreeagritco.exam.utils.NetworkUtils;
import com.example.andreeagritco.exam.utils.SeatListAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TheaterActivity extends AppCompatActivity {

    Button markAvailable;
    ListView seatsListView;


    private ProgressDialog progressDialog;
    private static ApiInterface apiInterface;
    private static AppDatabase appDatabase;
    private List<Seat> seats = new ArrayList<>();

    private SeatListAdapter seatListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theater);

        markAvailable = findViewById(R.id.markAvailableButton);
        seatsListView = findViewById(R.id.theaterListView);

        seatListAdapter = new SeatListAdapter(seats, getLayoutInflater(), false);
        seatsListView.setAdapter(seatListAdapter);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        appDatabase = AppDatabase.getInstancce(getApplicationContext());
        progressDialog = new ProgressDialog(this);

        seatsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (NetworkUtils.isNetworkAvailable(getSystemService(Context.CONNECTIVITY_SERVICE))) {
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();

                    Call<Seat> call = apiInterface.confirmSeat(new IdObject(seats.get(position).getId()));

                    call.enqueue(new Callback<Seat>() {
                        @SuppressLint("StaticFieldLeak")
                        @Override
                        public void onResponse(Call<Seat> call, Response<Seat> response) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            if (response.isSuccessful()) {

                                final Seat seat = response.body();
                                new AsyncTask<Void, Void, Void>() {
                                    @Override
                                    protected Void doInBackground(Void... voids) {
                                        boolean check = false;
                                        List<Seat> seats = appDatabase.seatDao().getAll();
//
                                        for (Seat s : seats) {
                                            if (s.getId() == seat.getId()) {
                                                s.setStatus(seat.getStatus());
                                                appDatabase.seatDao().update(s);
                                                check = true;
                                                break;
                                            }
                                        }



                                        return null;
                                    }

                                }.execute();



                                seatListAdapter.notifyDataSetChanged();
                                getAll();

                            } else {
                                Toast.makeText(getApplicationContext(), "Error when getting available seats!", Toast.LENGTH_LONG).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<Seat> call, Throwable t) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            Toast.makeText(getApplicationContext(), "Error when trying to establish connection with the server!", Toast.LENGTH_LONG).show();

                        }
                    });

                } else {
                    Toast.makeText(getApplicationContext(), "NO INTERNET CONNECTION", Toast.LENGTH_LONG).show();
                    Log.i("info", "Check the connection");

                }
            }
        });


        markAvailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtils.isNetworkAvailable(getSystemService(Context.CONNECTIVITY_SERVICE))) {
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();

                    Call<Void> call = apiInterface.deleteSeatsMarkAvailable();

                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            if (response.isSuccessful()) {
                                seatListAdapter.notifyDataSetChanged();

                            } else {
                                Toast.makeText(getApplicationContext(), "Error when getting available seats!", Toast.LENGTH_LONG).show();
                            }

                            getAll();

                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            Toast.makeText(getApplicationContext(), "Error when trying to establish connection with the server!", Toast.LENGTH_LONG).show();

                        }
                    });

                } else {
                    Toast.makeText(getApplicationContext(), "NO INTERNET CONNECTION", Toast.LENGTH_LONG).show();
                    Log.i("info", "Check the connection");

                }
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

            Call<List<Seat>> call = apiInterface.getAll();

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

        }
    }

}
