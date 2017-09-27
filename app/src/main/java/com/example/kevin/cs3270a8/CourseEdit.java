package com.example.kevin.cs3270a8;


import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


public class CourseEdit extends Fragment {

View rootView;

    public CourseEdit() {
    }

    EditText edtCourseID;
    EditText edtName;
    EditText edtCourseCode;
    EditText edtCourseStart;
    EditText edtCourseEnd;
    FloatingActionButton fabSaveCourse;
    long courseID;
    Boolean pressedEdit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_course_edit, container, false);

        MainActivity ma = (MainActivity) getActivity();
        ma.hideActionButton();

        edtCourseID = (EditText) rootView.findViewById(R.id.edtCourseID);
        edtName = (EditText) rootView.findViewById(R.id.edtName);
        edtCourseCode = (EditText) rootView.findViewById(R.id.edtCourseCode);
        edtCourseStart = (EditText) rootView.findViewById(R.id.edtCourseStart);
        edtCourseEnd = (EditText) rootView.findViewById(R.id.edtCourseEnd);

        fabSaveCourse = (FloatingActionButton) rootView.findViewById(R.id.fabSaveCourse);

        fabSaveCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper dbHelper = new DatabaseHelper(getActivity(), "School", null, 1);
                if (courseID > 0){
                long rowID = dbHelper.updateCourse( courseID,
                        edtCourseID.getText().toString(),
                        edtName.getText().toString(),
                        edtCourseCode.getText().toString(),
                        edtCourseStart.getText().toString(),
                        edtCourseEnd.getText().toString());}
                else{
                    long rowID = dbHelper.addCourse(
                            edtCourseID.getText().toString(),
                            edtName.getText().toString(),
                            edtCourseCode.getText().toString(),
                            edtCourseStart.getText().toString(),
                            edtCourseEnd.getText().toString());

                }

                MainActivity ma = (MainActivity) getActivity();
                ma.reloadCourseList();
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity ma = (MainActivity) getActivity();
        courseID = ma.getID();
        pressedEdit = ma.getPressedEdit();
        if (courseID > 0) {
            populateCourse(courseID);
        }
    }

    public void populateCourse(long id){

        DatabaseHelper dbHelper = new DatabaseHelper(getActivity(), "School",null,1);
        Cursor cursor = dbHelper.getOneCourse(id);
        cursor.moveToFirst();

        String course_id = cursor.getString(cursor.getColumnIndex("id"));
        String name = cursor.getString(cursor.getColumnIndex("name"));
        String course_code = cursor.getString(cursor.getColumnIndex("course_code"));
        String start_at = cursor.getString(cursor.getColumnIndex("start_at"));
        String end_at = cursor.getString(cursor.getColumnIndex("end_at"));

        edtCourseID.setText(course_id);
        edtName.setText(name);
        edtCourseCode.setText(course_code);
        edtCourseStart.setText(start_at);
        edtCourseEnd.setText(end_at);

    }


}
