package com.example.andreeagritco.exam.ui;

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
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservedSeatsAcivity extends AppCompatActivity {

    private Button deleteButton;

    private ListView reservedSeatsListView;
    private static AppDatabase appDatabase;
    private List<Seat> reservedSeats = new ArrayList<>();

    private ProgressDialog progressDialog;
    private static ApiInterface apiInterface;

    private SeatListAdapter seatListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserved_seats_acivity);

        reservedSeatsListView = findViewById(R.id.reservedSeatsListView);
        deleteButton = findViewById(R.id.deleteAllButton);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {

                Log.i("delete local", "DELETING LOCAL DATA");
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        for (Seat s : reservedSeats) {
                            appDatabase.seatDao().delete(s);
                        }


                        return null;
                    }

                }.execute();

                getAll();

            }


        });


        seatListAdapter = new SeatListAdapter(reservedSeats, getLayoutInflater(), false);
        reservedSeatsListView.setAdapter(seatListAdapter);

        appDatabase = AppDatabase.getInstancce(getApplicationContext());

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        progressDialog = new ProgressDialog(this);


        reservedSeatsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog alertDialog = new AlertDialog.Builder(ReservedSeatsAcivity.this).create();
                alertDialog.setTitle("Warning");
                alertDialog.setMessage("Do you want to refresh this seat's status?");
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //RENT MOVIE
                        if (NetworkUtils.isNetworkAvailable(getSystemService(Context.CONNECTIVITY_SERVICE))) {

                            progressDialog.setIndeterminate(true);
                            progressDialog.setMessage("Loading...");
                            progressDialog.show();


                            Call<Status> call = apiInterface.refreshSeatStatus(reservedSeats.get(position).getId());

                            call.enqueue(new Callback<Status>() {
                                @SuppressLint("StaticFieldLeak")
                                @Override
                                public void onResponse(Call<Status> call, Response<Status> response) {
                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }

                                    if (response.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "SUCCESSFULLY REFRESHED!", Toast.LENGTH_LONG).show();

                                        final Status status = response.body();

                                        final Seat seatList = reservedSeats.get(position);

                                        if (!reservedSeats.get(position).getStatus().toString().equals(status.toString())) {

                                            new AsyncTask<Void, Void, Void>() {
                                                @Override
                                                protected Void doInBackground(Void... voids) {
                                                    List<Seat> seats = appDatabase.seatDao().getAll();
//
                                                    for (Seat s : seats) {
                                                        if (s.getId() == seatList.getId()) {
                                                            s.setStatus(status);
                                                            appDatabase.seatDao().update(s);
                                                            break;
                                                        }
                                                    }


                                                    return null;
                                                }

                                            }.execute();
                                        }


                                    } else {
                                        //   Toast.makeText(getApplicationContext(), "Error when refrshing this seat!", Toast.LENGTH_LONG).show();
                                    }

                                    getAll();

                                }

                                @Override
                                public void onFailure(Call<Status> call, Throwable t) {

                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }
                                    //   Toast.makeText(getApplicationContext(), "Error when trying to establish a connection with the server!", Toast.LENGTH_LONG).show();

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


        reservedSeatsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog alertDialog = new AlertDialog.Builder(ReservedSeatsAcivity.this).create();
                alertDialog.setTitle("Warning");
                alertDialog.setMessage("Do you want to buy this seat?");
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (NetworkUtils.isNetworkAvailable(getSystemService(Context.CONNECTIVITY_SERVICE))) {

                            progressDialog.setIndeterminate(true);
                            progressDialog.setMessage("Loading...");
                            progressDialog.show();


                            Call<Seat> call = apiInterface.buySeat(new IdObject(reservedSeats.get(position).getId()));

                            call.enqueue(new Callback<Seat>() {
                                @SuppressLint("StaticFieldLeak")
                                @Override
                                public void onResponse(Call<Seat> call, Response<Seat> response) {
                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }

                                    if (response.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "SUCCESSFULLY REFRESHED!", Toast.LENGTH_LONG).show();

                                        final Seat seatBody = response.body();

                                        final Seat seatList = reservedSeats.get(position);

                                        if (!reservedSeats.get(position).getStatus().toString().equals(seatBody.getStatus().toString())) {

                                            new AsyncTask<Void, Void, Void>() {
                                                @Override
                                                protected Void doInBackground(Void... voids) {
                                                    List<Seat> seats = appDatabase.seatDao().getAll();
//
                                                    for (Seat s : seats) {
                                                        if (s.getId() == seatList.getId()) {
                                                            s.setStatus(seatBody.getStatus());
                                                            appDatabase.seatDao().update(s);
                                                            break;
                                                        }
                                                    }


                                                    return null;
                                                }

                                            }.execute();
                                        }


                                    } else {
                                        Toast.makeText(getApplicationContext(), "Error when refrshing this seat!", Toast.LENGTH_LONG).show();
                                    }

                                    getAll();

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


                return true;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getAll();
    }

    @SuppressLint("StaticFieldLeak")
    private void getAll() {

        Log.i("info", "Get reserved items from database");

        try {
            new AsyncTask<Void, Void, Integer>() {
                @Override
                protected Integer doInBackground(Void... voids) {
                    reservedSeats.clear();

                    List<Seat> databaseSeats = appDatabase.seatDao().getAll();

                    reservedSeats.clear();
                    reservedSeats.addAll(databaseSeats);

                    return null;
                }

            }.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        seatListAdapter.notifyDataSetChanged();

    }


}
