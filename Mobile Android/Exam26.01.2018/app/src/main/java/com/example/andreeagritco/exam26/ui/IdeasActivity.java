package com.example.andreeagritco.exam26.ui;

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
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.andreeagritco.exam26.R;
import com.example.andreeagritco.exam26.api.ApiClient;
import com.example.andreeagritco.exam26.api.ApiInterface;
import com.example.andreeagritco.exam26.db.AppDatabase;
import com.example.andreeagritco.exam26.model.Project;
import com.example.andreeagritco.exam26.utils.IdeasListAdapter;
import com.example.andreeagritco.exam26.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IdeasActivity extends AppCompatActivity {

    private Button addIdeaButton;
    private ListView ideasListView;
    private ProgressDialog progressDialog;


    private static ApiInterface apiInterface;
    private static AppDatabase appDatabase;
    private List<Project> projects = new ArrayList<>();

    private IdeasListAdapter ideasListAdapter;
    private Handler handler = new Handler();
    private SearchForInternet searchForInternet = new SearchForInternet();
    private AdapterView.OnItemLongClickListener deleteListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ideas);

        addIdeaButton = findViewById(R.id.addIdeaButton);
        ideasListView = findViewById(R.id.ideasListView);

        ideasListAdapter = new IdeasListAdapter(projects, getLayoutInflater());
        ideasListView.setAdapter(ideasListAdapter);


        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        appDatabase = AppDatabase.getInstance(getApplicationContext());
        progressDialog = new ProgressDialog(this);

        AdapterView.OnItemClickListener promoteListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (NetworkUtils.isNetworkAvailable(getSystemService(Context.CONNECTIVITY_SERVICE))) {
                    AlertDialog alertDialog = new AlertDialog.Builder(IdeasActivity.this).create();
                    alertDialog.setTitle("Warning");
                    alertDialog.setMessage("Do you want to promote this idea?");
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            progressDialog.setIndeterminate(true);
                            progressDialog.setMessage("Loading...");
                            progressDialog.show();

                            //PROMOTE ITEM
                            if (NetworkUtils.isNetworkAvailable(getSystemService(Context.CONNECTIVITY_SERVICE))) {

                                Call<Project> call = apiInterface.promoteProject(projects.get(position));

                                call.enqueue(new Callback<Project>() {
                                    @Override
                                    public void onResponse(Call<Project> call, Response<Project> response) {

                                        if (progressDialog.isShowing()) {
                                            progressDialog.dismiss();
                                        }

                                        if (response.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), "SUCCESSFULLY PROMOTED", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Error when promoting this idea!", Toast.LENGTH_LONG).show();
                                        }


                                        getAll();

                                    }

                                    @Override
                                    public void onFailure(Call<Project> call, Throwable t) {

                                        if (progressDialog.isShowing()) {
                                            progressDialog.dismiss();
                                        }
                                        Toast.makeText(getApplicationContext(), "Error when promoting this idea!", Toast.LENGTH_LONG).show();

                                    }
                                });

                            } else {
                                Toast.makeText(getApplicationContext(), "NETWORK IS NOT AVAILABLE!", Toast.LENGTH_LONG).show();
                            }

                        }
                    });

                    alertDialog.show();


                } else {
                    Toast.makeText(getApplicationContext(), "NO INTERNET CONNECTION!", Toast.LENGTH_LONG).show();
                }
            }
        };


        deleteListener = new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog alertDialog = new AlertDialog.Builder(IdeasActivity.this).create();
                alertDialog.setTitle("Warning");
                alertDialog.setMessage("Do you want to delete this idea?");
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        progressDialog.setIndeterminate(true);
                        progressDialog.setMessage("Loading...");
                        progressDialog.show();

                        //DELETE IDEA
                        if (NetworkUtils.isNetworkAvailable(getSystemService(Context.CONNECTIVITY_SERVICE))) {
                            Call<Project> call = apiInterface.deleteIdea(projects.get(position).getId());

                            call.enqueue(new Callback<Project>() {
                                @Override
                                public void onResponse(Call<Project> call, Response<Project> response) {
                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }


                                    if (response.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "SUCCESSFULLY DELETED", Toast.LENGTH_LONG).show();
                                        getAll();

                                    } else {
                                        Toast.makeText(getApplicationContext(), "Error when deleting this idea!", Toast.LENGTH_LONG).show();
                                    }


                                }

                                @Override
                                public void onFailure(Call<Project> call, Throwable t) {

                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }
                                    Toast.makeText(getApplicationContext(), "Error when deleting this idea!", Toast.LENGTH_LONG).show();

                                }
                            });

                        } else {
                            Toast.makeText(getApplicationContext(), "NETWORK IS NOT AVAILABLE!", Toast.LENGTH_LONG).show();
                        }

                    }
                });


                alertDialog.show();

                return true;
            }
        };


        addIdeaButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("clicks", "You clicked add idea button");
                if (NetworkUtils.isNetworkAvailable(getSystemService(Context.CONNECTIVITY_SERVICE))) {
                    Intent i = new Intent(IdeasActivity.this, AddActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "NO INTERNET CONNECTION!", Toast.LENGTH_LONG).show();
                }
            }
        });


        ideasListView.setOnItemLongClickListener(deleteListener);

        ideasListView.setOnItemClickListener(promoteListener);

        //getAll();

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

            Call<List<Project>> call = apiInterface.getIdeas();

            call.enqueue(new Callback<List<Project>>() {
                @SuppressLint("StaticFieldLeak")
                @Override
                public void onResponse(Call<List<Project>> call, Response<List<Project>> response) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }

                    handler.removeCallbacks(searchForInternet);

                    projects.clear();
                    projects.addAll(response.body());

                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            appDatabase.projectDao().deleteAll();

                            for (Project project : projects) {
                                appDatabase.projectDao().add(project);
                            }


                            return null;
                        }

                    }.execute();

                    ideasListAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<List<Project>> call, Throwable t) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            });


        } else {
            Toast.makeText(getApplicationContext(), "NO INTERNET CONNECTION", Toast.LENGTH_LONG).show();
            Log.i("info", "Check the connection");

            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    projects.clear();
                    projects.addAll(appDatabase.projectDao().getAll());

                    return null;
                }

            }.execute();


            ideasListAdapter.notifyDataSetChanged();

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
