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
import com.example.andreeagritco.exam25.utils.GameListAdapter;

import java.util.ArrayList;
import java.util.List;

public class PurchasedActivity extends AppCompatActivity {

    private ListView purchasedListView;

    private static AppDatabase appDatabase;
    private List<Game> purchasedGames = new ArrayList<>();

    private GameListAdapter gameListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchased);

        purchasedListView = findViewById(R.id.purhasedGamesListView);
        gameListAdapter = new GameListAdapter(purchasedGames, getLayoutInflater(), true);

        purchasedListView.setAdapter(gameListAdapter);

        appDatabase = AppDatabase.getInstancce(getApplicationContext());


    }

    @Override
    protected void onResume() {
        super.onResume();
        getAll();
    }

    @SuppressLint("StaticFieldLeak")
    private void getAll() {

        Log.i("info", "Get purchased items from database");

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                purchasedGames.clear();

                List<Game> databaseGames = appDatabase.gameDao().getAll();

                for (Game g : databaseGames) {
                    if (g.getStatus().toString().equals(com.example.andreeagritco.exam25.model.Status.sold.toString())) {
                        purchasedGames.add(g);
                    }
                }

                return null;
            }

        }.execute();


        gameListAdapter.notifyDataSetChanged();


    }
}
