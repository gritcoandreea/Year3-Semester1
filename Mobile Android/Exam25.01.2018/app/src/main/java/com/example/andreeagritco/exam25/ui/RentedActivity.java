package com.example.andreeagritco.exam25.ui;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.andreeagritco.exam25.R;
import com.example.andreeagritco.exam25.db.AppDatabase;
import com.example.andreeagritco.exam25.model.Game;
import com.example.andreeagritco.exam25.model.Status;
import com.example.andreeagritco.exam25.utils.GameListAdapter;

import java.util.ArrayList;
import java.util.List;

public class RentedActivity extends AppCompatActivity {

    private ListView rentedListView;

    private static AppDatabase appDatabase;
    private List<Game> rentedGames = new ArrayList<>();

    private GameListAdapter gameListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rented);

        rentedListView = findViewById(R.id.rentedGamesListView);
        gameListAdapter = new GameListAdapter(rentedGames, getLayoutInflater(), true);

        rentedListView.setAdapter(gameListAdapter);

        appDatabase = AppDatabase.getInstancce(getApplicationContext());


    }

    @Override
    protected void onResume() {
        super.onResume();
        getAll();
    }

    @SuppressLint("StaticFieldLeak")
    private void getAll() {

        Log.i("info", "Get rented items from database");

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                rentedGames.clear();

                List<Game> databaseGames = appDatabase.gameDao().getAll();

                for (Game g : databaseGames) {
                    if (g.getStatus().toString().equals(com.example.andreeagritco.exam25.model.Status.rent.toString())) {
                        rentedGames.add(g);
                    }
                }

                return null;
            }

        }.execute();


        gameListAdapter.notifyDataSetChanged();


    }
}
