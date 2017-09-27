package com.example.kevin.cs3270a8;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



public class CourseView extends Fragment {


    View rootView;
    TextView txvCourseID;
    TextView txvName;
    TextView txvCourseCode;
    TextView txvCourseStart;
    TextView txvCourseEnd;
    long courseID;
    Boolean pressedEdit;

    public CourseView() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_course_view, container, false);

        setHasOptionsMenu(true);

        txvCourseID = (TextView) rootView.findViewById(R.id.txvCourseID);
        txvName = (TextView) rootView.findViewById(R.id.txvName);
        txvCourseCode = (TextView) rootView.findViewById(R.id.txvCourseCode);
        txvCourseStart = (TextView) rootView.findViewById(R.id.txvCourseStart);
        txvCourseEnd = (TextView) rootView.findViewById(R.id.txvCourseEnd);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        MainActivity ma = (MainActivity) getActivity();
        courseID = ma.getID();
        if (courseID > 0) {
            populateCourse(courseID);
        }
    }


    public void populateCourse(long id){


            DatabaseHelper dbHelper = new DatabaseHelper(getActivity(), "School", null, 1);
            Cursor cursor = dbHelper.getOneCourse(id);
            cursor.moveToFirst();

            String course_id = cursor.getString(cursor.getColumnIndex("id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String course_code = cursor.getString(cursor.getColumnIndex("course_code"));
            String start_at = cursor.getString(cursor.getColumnIndex("start_at"));
            String end_at = cursor.getString(cursor.getColumnIndex("end_at"));

            txvCourseID.setText(course_id);
            txvName.setText(name);
            txvCourseCode.setText(course_code);
            txvCourseStart.setText(start_at);
            txvCourseEnd.setText(end_at);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.viewmenu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mnuEdit:
                MainActivity ma = (MainActivity) getActivity();
                pressedEdit = true;
                ma.setPressedEdit(pressedEdit);
                ma.populateEditCourse(courseID);

                return true;
            case R.id.mnuDelete:
                DeleteConfirmationDialog dialog = new DeleteConfirmationDialog();
                dialog.setCancelable(false);
                dialog.show(getFragmentManager(), "Delete");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
