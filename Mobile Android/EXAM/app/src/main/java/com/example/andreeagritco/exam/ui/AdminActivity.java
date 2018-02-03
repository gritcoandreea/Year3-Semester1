package com.example.andreeagritco.exam.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.andreeagritco.exam.R;
import com.example.andreeagritco.exam.api.ApiClient;
import com.example.andreeagritco.exam.api.ApiInterface;
import com.example.andreeagritco.exam.db.AppDatabase;
import com.example.andreeagritco.exam.model.Seat;
import com.example.andreeagritco.exam.utils.NetworkUtils;
import com.example.andreeagritco.exam.utils.SeatListAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminActivity extends AppCompatActivity {

    private Button deleteAll;
    private Button getPurchasedButton;
    private Button getConfirmedSeats;
    private ListView seatsListView;

    private ProgressDialog progressDialog;
    private static ApiInterface apiInterface;
    private static AppDatabase appDatabase;
    private List<Seat> seats = new ArrayList<>();

    private SeatListAdapter seatListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        deleteAll = findViewById(R.id.deleteAllSeatsAdmin);
        getPurchasedButton = findViewById(R.id.getPurchasedSeats);
        getConfirmedSeats = findViewById(R.id.getConfirmedSeats);

        seatsListView = findViewById(R.id.adminListView);

        seatListAdapter = new SeatListAdapter(seats, getLayoutInflater(), false);
        seatsListView.setAdapter(seatListAdapter);


        getPurchasedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getAll(2);

            }
        });

        getConfirmedSeats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAll(1);

            }
        });

        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtils.isNetworkAvailable(getSystemService(Context.CONNECTIVITY_SERVICE))) {
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();

                    Call<Void> call = apiInterface.deleteAllSeats();


                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            if (response.isSuccessful()) {
                                seats.clear();


                                seatListAdapter.notifyDataSetChanged();

                            } else {
                                Toast.makeText(getApplicationContext(), "Error when getting available seats!", Toast.LENGTH_LONG).show();
                            }

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


        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        appDatabase = AppDatabase.getInstancce(getApplicationContext());
        progressDialog = new ProgressDialog(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //   getAll();
    }

    private void getAll(int button) {


        if (NetworkUtils.isNetworkAvailable(getSystemService(Context.CONNECTIVITY_SERVICE))) {
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

            Call<List<Seat>> call = null;
            if (button == 1) {
                call = apiInterface.getConfirmedSeats();
            } else {
                call = apiInterface.getPurchasedSeats();
            }


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
