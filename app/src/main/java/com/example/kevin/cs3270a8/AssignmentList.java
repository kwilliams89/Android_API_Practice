package com.example.kevin.cs3270a8;


import android.database.Cursor;
import com.example.kevin.cs3270a8.CanvasObjects.Assignment;
import com.example.kevin.cs3270a8.Authorization;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class AssignmentList extends ListFragment {

    View rootView;
    protected ArrayAdapter<String> assignmentAdapter;
    protected long courseID;
    protected String course_id;

    public AssignmentList() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = super.onCreateView(inflater, container, savedInstanceState);

        setHasOptionsMenu(true);
        MainActivity ma = (MainActivity) getActivity();
        course_id = ma.getCourse_ID();
        new getCanvasAssignments().execute("");
        return rootView;
    }


    public class getCanvasAssignments extends AsyncTask<String, Integer, String> {

        String AUTH_TOKEN = Authorization.AUTH_TOKEN;
        String rawJson = "";

        @Override
        protected String doInBackground(String... params) {

            try {
                URL url = new URL("https://weber.instructure.com/api/v1/courses/" + course_id + "/assignments");
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Authorization", "Bearer " + AUTH_TOKEN);
                conn.connect();
                int status = conn.getResponseCode();
                switch (status) {
                    case 200:
                    case 201:
                        BufferedReader br =
                                new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        rawJson = br.readLine();
                }
            } catch (MalformedURLException e) {

            } catch (IOException e) {
            }
            return rawJson;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            assignmentAdapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1);

            try {
                Assignment[] assignments = jsonParse(s);
                for (Assignment assignment : assignments) {
                    assignmentAdapter.add(assignment.name);
                }
                setListAdapter(assignmentAdapter);
            } catch (Exception e) {
            }
        }

        private Assignment[] jsonParse(String rawJson) {
            GsonBuilder gsonb = new GsonBuilder();
            Gson gson = gsonb.create();

            Assignment[] assignments = null;

            try {
                assignments = gson.fromJson(rawJson, Assignment[].class);
            } catch (Exception e) {

            }
            return assignments;
        }

    } // end of getCanvasAssignments class
}// end of AssignmentList class