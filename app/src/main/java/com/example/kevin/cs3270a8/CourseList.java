package com.example.kevin.cs3270a8;


import android.database.Cursor;

import com.example.kevin.cs3270a8.CanvasObjects.Course;
import com.example.kevin.cs3270a8.Authorization;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.Runnable;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class CourseList extends ListFragment {

    View rootView;
    protected SimpleCursorAdapter adapter;
    Cursor cursor;

    public CourseList() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = super.onCreateView(inflater, container, savedInstanceState);

        setHasOptionsMenu(true);



        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new getAllCourses().execute(1);

        AdapterView.OnItemLongClickListener myListener = new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                DatabaseHelper dbHelper = new DatabaseHelper(getActivity(), "School", null, 1);
                Cursor cursor = dbHelper.getOneCourse(id);
                cursor.moveToFirst();

                String course_id = cursor.getString(cursor.getColumnIndex("id"));
                MainActivity ma = (MainActivity) getActivity();
                ma.loadAssignments(course_id);
                return true;
            }
        };
        getListView().setOnItemLongClickListener(myListener);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.listmenu, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnuImport:
                DatabaseHelper dbHelper = new DatabaseHelper(getActivity(), "School", null, 1);
                dbHelper.deleteAllCourses();
                new getCanvasCourses().execute("");
                MainActivity ma = (MainActivity) getActivity();
                ma.reloadCourseList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        MainActivity ma = (MainActivity) getActivity();
        ma.populateCourse(id);
    }

    public class getCanvasCourses extends AsyncTask<String, Integer, String> {

        String AUTH_TOKEN = Authorization.AUTH_TOKEN;
        String rawJson = "";

        @Override
        protected String doInBackground(String... params) {

            try {
                URL url = new URL("https://weber.instructure.com/api/v1/courses");
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

            DatabaseHelper dbHelper = new DatabaseHelper(getActivity(), "School", null, 1);

            try {
                Course[] courses = jsonParse(s);
                for (Course course : courses) {
                    long rowID = dbHelper.addCourse(
                            course.id,
                            course.name,
                            course.course_code,
                            course.start_at,
                            course.end_at);
                }
            } catch (Exception e) {
            }
        }

        private Course[] jsonParse(String rawJson) {
            GsonBuilder gsonb = new GsonBuilder();
            Gson gson = gsonb.create();

            Course[] courses = null;

            try {
                courses = gson.fromJson(rawJson, Course[].class);
            } catch (Exception e) {

            }
            return courses;
        }

    } // end of getCanvasCourse class

    public class getAllCourses extends AsyncTask<Integer, Integer, Cursor> {

        DatabaseHelper dbHelper = new DatabaseHelper(getActivity(), "School", null, 1);

        @Override
        protected Cursor doInBackground(Integer... params) {
            cursor = dbHelper.getAllCourses();
            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            String[] columns = new String[]{"name"};
            int[] views = new int[]{android.R.id.text1};
            adapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_1,
                    cursor, columns, views,
                    CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
            setListAdapter(adapter);

        }
    } // end of deleteAllCourses class
}// end of CourseList class