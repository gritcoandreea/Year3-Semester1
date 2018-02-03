package com.example.andreeagritco.exam25.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.andreeagritco.exam25.R;
import com.example.andreeagritco.exam25.api.ApiClient;
import com.example.andreeagritco.exam25.api.ApiInterface;
import com.example.andreeagritco.exam25.db.AppDatabase;
import com.example.andreeagritco.exam25.model.Game;
import com.example.andreeagritco.exam25.model.IdObject;
import com.example.andreeagritco.exam25.model.Status;
import com.example.andreeagritco.exam25.model.Type;
import com.example.andreeagritco.exam25.utils.GameListAdapter;
import com.example.andreeagritco.exam25.utils.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientActivity extends AppCompatActivity {

    private Button rentedButton;
    private Button purchasedButton;
    private ListView availableGamesListView;
    private ProgressDialog progressDialog;

    private static ApiInterface apiInterface;
    private static AppDatabase appDatabase;
    private List<Game> games = new ArrayList<>();

    private GameListAdapter gamesListAdapter;
    private Handler handler = new Handler();
    private SearchForInternet searchForInternet = new SearchForInternet();

    private AdapterView.OnItemClickListener purchaseListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(ClientActivity.this);
            builder.setTitle("Please enter the quantity you want to buy: ");

            // Set up the input
            final EditText input = new EditText(ClientActivity.this);
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            builder.setView(input);

            // Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final String m_Text = input.getText().toString();

                    if (NetworkUtils.isNetworkAvailable(getSystemService(Context.CONNECTIVITY_SERVICE))) {
                        progressDialog.setIndeterminate(true);
                        progressDialog.setMessage("Loading...");
                        progressDialog.show();

                        Call<Game> call = apiInterface.buyGame(new IdObject(games.get(position).getId(), Integer.parseInt(m_Text)));

                        call.enqueue(new Callback<Game>() {
                            @SuppressLint("StaticFieldLeak")
                            @Override
                            public void onResponse(Call<Game> call, Response<Game> response) {
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                                if (response.isSuccessful()) {

                                    final Game game = response.body();

                                    new AsyncTask<Void, Void, Void>() {
                                        @Override
                                        protected Void doInBackground(Void... voids) {
                                            List<Game> games = appDatabase.gameDao().getAll();
//
                                            boolean check = false;
                                            Game temporaryGame = new Game(game.getId(), game.getName(), Integer.parseInt(m_Text), game.getType(), com.example.andreeagritco.exam25.model.Status.sold);


                                            for (Game g : games) {
                                                if (g.getId() == game.getId() && g.getStatus() == com.example.andreeagritco.exam25.model.Status.sold) {
                                                    temporaryGame.setQuantity(temporaryGame.getQuantity() + g.getQuantity());
                                                    appDatabase.gameDao().update(temporaryGame);
                                                    check = true;
                                                    break;
                                                }
                                            }

                                            if (!check) {
                                                appDatabase.gameDao().add(temporaryGame);
                                            }

                                            return null;
                                        }

                                    }.execute();

                                    Intent i = new Intent(ClientActivity.this, PurchasedActivity.class);
                                    startActivity(i);


                                } else {
                                    Toast.makeText(getApplicationContext(), "Error when buying a game!", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Game> call, Throwable t) {
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                                Toast.makeText(getApplicationContext(), "Error when trying to establish connection with the server!", Toast.LENGTH_LONG).show();

                            }

                        });


                    } else {
                        Toast.makeText(getApplicationContext(), "NO INTERNET CONNECTION", Toast.LENGTH_LONG).show();
                        Log.i("info", "Check the connection");
                        dialog.cancel();
                    }


                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();


        }
    };

    private View.OnClickListener rentedListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Log.i("changeActivity", "Starting RentedActivity.");
            Intent i = new Intent(ClientActivity.this, RentedActivity.class);
            startActivity(i);


        }
    };

    private View.OnClickListener purchasedListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Log.i("changeActivity", "Starting PurchasedActivity.");
            Intent i = new Intent(ClientActivity.this, PurchasedActivity.class);
            startActivity(i);


        }
    };
    private AdapterView.OnItemLongClickListener rentListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

            AlertDialog alertDialog = new AlertDialog.Builder(ClientActivity.this).create();
            alertDialog.setTitle("Warning");
            alertDialog.setMessage("Do you want to rent this game?");
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


                        Call<Game> call = apiInterface.rentGame(new IdObject(games.get(position).getId()));

                        call.enqueue(new Callback<Game>() {
                            @SuppressLint("StaticFieldLeak")
                            @Override
                            public void onResponse(Call<Game> call, Response<Game> response) {
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }

                                if (response.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "SUCCESSFULLY RENTED!", Toast.LENGTH_LONG).show();

                                    final Game game = response.body();

                                    new AsyncTask<Void, Void, Void>() {
                                        @Override
                                        protected Void doInBackground(Void... voids) {
                                            List<Game> games = appDatabase.gameDao().getAll();
//
                                            boolean check = false;
                                            Game temporaryGame = new Game(game.getId(), game.getName(), 1, game.getType(), com.example.andreeagritco.exam25.model.Status.rent);


                                            for (Game g : games) {
                                                if (g.getId() == game.getId() && g.getStatus() == com.example.andreeagritco.exam25.model.Status.rent) {
                                                    temporaryGame.setQuantity(temporaryGame.getQuantity() + g.getQuantity());
                                                    appDatabase.gameDao().update(temporaryGame);
                                                    check = true;
                                                    break;
                                                }
                                            }

                                            if (!check) {
                                                appDatabase.gameDao().add(temporaryGame);
                                            }

                                            return null;
                                        }

                                    }.execute();

                                    Intent i = new Intent(ClientActivity.this, RentedActivity.class);
                                    startActivity(i);


                                } else {
                                    Toast.makeText(getApplicationContext(), "Error when renting this game!", Toast.LENGTH_LONG).show();
                                }

                                getAll();

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
                        Toast.makeText(getApplicationContext(), "NETWORK IS NOT AVAILABLE!YOU CAN'T RENT A GAME!", Toast.LENGTH_LONG).show();
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
        setContentView(R.layout.activity_client);

        rentedButton = findViewById(R.id.rentedGamesButton);
        purchasedButton = findViewById(R.id.purchasedGamesButton);

        availableGamesListView = findViewById(R.id.clientAvailableGamesListView);

        gamesListAdapter = new GameListAdapter(games, getLayoutInflater(), true);
        availableGamesListView.setAdapter(gamesListAdapter);


        availableGamesListView.setOnItemClickListener(purchaseListener);
        availableGamesListView.setOnItemLongClickListener(rentListener);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        appDatabase = AppDatabase.getInstancce(getApplicationContext());
        progressDialog = new ProgressDialog(this);


        rentedButton.setOnClickListener(rentedListener);
        purchasedButton.setOnClickListener(purchasedListener);


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

            Call<List<Game>> call = apiInterface.getGames();

            call.enqueue(new Callback<List<Game>>() {
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
                        Toast.makeText(getApplicationContext(), "Error when getting available games!", Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<List<Game>> call, Throwable t) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    Toast.makeText(getApplicationContext(), "Error when trying to establish connection with the server!", Toast.LENGTH_LONG).show();

                }
            });

        } else {
            Toast.makeText(getApplicationContext(), "NO INTERNET CONNECTION", Toast.LENGTH_LONG).show();
            Log.i("info", "Check the connection");

            games.clear();
            gamesListAdapter.notifyDataSetChanged();
            handler.postDelayed(searchForInternet, 20000);
        }
    }


    @Override
    public void onStart() {
        super.onStart();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("http://192.168.1.5:4001").build();

        EchoWebSocketListener listener = new EchoWebSocketListener();
        WebSocket ws =  client.newWebSocket(request,listener);

    }


    private final class EchoWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;

        @Override
        public void onOpen(WebSocket webSocket, okhttp3.Response response) {
            Log.d("open", "opne");
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            messageReceived(text);
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, okhttp3.Response response) {
            Log.d("SOCKET FAILURE", "I DON'T KNOW WHAT'S HAPPENING........");
        }
    }

    private void messageReceived(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                try {
                    JSONObject object = new JSONObject(text);
                    Game game = new Game(object.getInt("id"), object.getString("name"),
                            object.getInt("quantity"), Type.valueOf(object.getString("type")), Status.valueOf(object.getString("status")));
                    games.add(game);
                    gamesListAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    private class SearchForInternet implements Runnable {
        @Override
        public void run() {
            Log.i("info", "Check the connection AICI");
            getAll();
        }
    }


}
