package com.example.andreeagritco.exam26.ui;

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

import com.example.andreeagritco.exam26.R;
import com.example.andreeagritco.exam26.api.ApiClient;
import com.example.andreeagritco.exam26.api.ApiInterface;
import com.example.andreeagritco.exam26.model.Project;
import com.example.andreeagritco.exam26.utils.NetworkUtils;
import com.example.andreeagritco.exam26.utils.ProjectsListAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProjectsActivity extends AppCompatActivity {

    private Button promoteIdeaButton;
    private ListView projectListView;
    private ProgressDialog progressDialog;

    private static ApiInterface apiInterface;
    private List<Project> projects = new ArrayList<>();

    private ProjectsListAdapter projectsListAdapter;
    private AdapterView.OnItemClickListener acceptDiscardListener;
    private AdapterView.OnItemLongClickListener deleteListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);

        promoteIdeaButton = findViewById(R.id.promoteIdeaButton);
        projectListView = findViewById(R.id.projectsListView);

        projectsListAdapter = new ProjectsListAdapter(projects, getLayoutInflater());
        projectListView.setAdapter(projectsListAdapter);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        progressDialog = new ProgressDialog(this);

        acceptDiscardListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog alertDialog = new AlertDialog.Builder(ProjectsActivity.this).create();
                alertDialog.setTitle("Warning");
                alertDialog.setMessage("Approve/Discard Idea?");

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "APPROVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        progressDialog.setIndeterminate(true);
                        progressDialog.setMessage("Loading...");
                        progressDialog.show();

                        //APPROVE ITEM
                        if (NetworkUtils.isNetworkAvailable(getSystemService(Context.CONNECTIVITY_SERVICE))) {
                            Call<Project> call = apiInterface.approveProject(projects.get(position));

                            call.enqueue(new Callback<Project>() {
                                @Override
                                public void onResponse(Call<Project> call, Response<Project> response) {
                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }

                                    if (response.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "SUCCESSFULLY APPROVED", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Error when approving this idea!", Toast.LENGTH_LONG).show();
                                    }

                                    getAll();

                                }

                                @Override
                                public void onFailure(Call<Project> call, Throwable t) {

                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }
                                    Toast.makeText(getApplicationContext(), "Error when approving this idea!", Toast.LENGTH_LONG).show();

                                }
                            });

                        } else {
                            Toast.makeText(getApplicationContext(), "NETWORK IS NOT AVAILABLE!", Toast.LENGTH_LONG).show();
                        }

                    }
                });

                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "DISCARD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        progressDialog.setIndeterminate(true);
                        progressDialog.setMessage("Loading...");
                        progressDialog.show();

                        //DISCARD ITEM
                        if (NetworkUtils.isNetworkAvailable(getSystemService(Context.CONNECTIVITY_SERVICE))) {
                            Call<Project> call = apiInterface.discardProject(projects.get(position));

                            call.enqueue(new Callback<Project>() {
                                @Override
                                public void onResponse(Call<Project> call, Response<Project> response) {
                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }

                                    if (response.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "SUCCESSFULLY DISCARDED", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Error when discarding this idea!", Toast.LENGTH_LONG).show();
                                    }

                                    getAll();

                                }

                                @Override
                                public void onFailure(Call<Project> call, Throwable t) {

                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }
                                    Toast.makeText(getApplicationContext(), "Error when discarding this idea!", Toast.LENGTH_LONG).show();

                                }
                            });

                        } else {
                            Toast.makeText(getApplicationContext(), "NETWORK IS NOT AVAILABLE!", Toast.LENGTH_LONG).show();
                        }

                    }
                });


                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


                alertDialog.show();


            }
        };


        deleteListener = new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog alertDialog = new AlertDialog.Builder(ProjectsActivity.this).create();
                alertDialog.setTitle("Warning");
                alertDialog.setMessage("Do you want to delete this project?");
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

                        //DELETE PROJECT
                        if (NetworkUtils.isNetworkAvailable(getSystemService(Context.CONNECTIVITY_SERVICE))) {
                            Call<Project> call = apiInterface.removeProject(projects.get(position).getId());

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
                                        Toast.makeText(getApplicationContext(), "Error when deleting this project!", Toast.LENGTH_LONG).show();
                                    }


                                }

                                @Override
                                public void onFailure(Call<Project> call, Throwable t) {

                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }
                                    Toast.makeText(getApplicationContext(), "Error when deleting this project!", Toast.LENGTH_LONG).show();

                                }
                            });

                        } else {
                            Toast.makeText(getApplicationContext(), "NETWORK IS NOT AVAILABLE!", Toast.LENGTH_LONG).show();
                        }

                    }
                });


                alertDialog.show();


                return false;
            }
        };


        projectListView.setOnItemClickListener(acceptDiscardListener);
        projectListView.setOnItemLongClickListener(deleteListener);
        promoteIdeaButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (NetworkUtils.isNetworkAvailable(getSystemService(Context.CONNECTIVITY_SERVICE))) {
                    Intent i = new Intent(ProjectsActivity.this, IdeasActivity.class);
                    startActivity(i);

                } else {
                    Toast.makeText(getApplicationContext(), "NO INTERNET CONNECTION!", Toast.LENGTH_LONG).show();
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

            Call<List<Project>> call = apiInterface.getProjects();

            call.enqueue(new Callback<List<Project>>() {
                @SuppressLint("StaticFieldLeak")
                @Override
                public void onResponse(Call<List<Project>> call, Response<List<Project>> response) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }


                    projects.clear();
                    projects.addAll(response.body());

                    projectsListAdapter.notifyDataSetChanged();
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

            projects.clear();
            projectsListAdapter.notifyDataSetChanged();
        }

    }

}
