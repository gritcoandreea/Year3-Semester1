package com.example.andreeagritco.exam26.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.andreeagritco.exam26.R;
import com.example.andreeagritco.exam26.model.Project;

import java.util.List;

/**
 * Created by Andreea Gritco on 31-Jan-18.
 */

public class ProjectsListAdapter  extends BaseAdapter {

    private List<Project> projectsList;
    private LayoutInflater inflater;

    public ProjectsListAdapter(List<Project> projectsList, LayoutInflater inflater) {
        this.projectsList = projectsList;
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return projectsList.size();
    }

    @Override
    public Object getItem(int position) {
        return projectsList.get(position);
    }

    @Override
    public long getItemId(int position) {
         return projectsList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView= inflater.inflate(R.layout.project_layout,null);

        TextView nameText = convertView.findViewById(R.id.projectNameText);
        TextView budgetText = convertView.findViewById(R.id.projectBudgetText);
        TextView typeText =  convertView.findViewById(R.id.projectTypeText);
        TextView statusText  = convertView.findViewById(R.id.projectStatusText);

        nameText.setText(projectsList.get(position).getName());
        budgetText.setText(projectsList.get(position).getBudget() + "");
        typeText.setText(projectsList.get(position).getType().toString());
        statusText.setText(projectsList.get(position).getStatus().toString());


        return convertView;
    }
}
