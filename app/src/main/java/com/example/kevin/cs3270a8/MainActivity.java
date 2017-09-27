package com.example.kevin.cs3270a8;

import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fabAddCourse;
    long courseID;
    Boolean pressedEdit;
    String course_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().replace(R.id.containerFragments, new CourseList(), "CL").addToBackStack(null).commit();

        fabAddCourse = (FloatingActionButton) findViewById(R.id.fabAddCourse);

        fabAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getSupportFragmentManager().beginTransaction().replace(R.id.containerFragments, new CourseEdit(), "CE").addToBackStack(null).commit();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Fragment fr = getSupportFragmentManager().findFragmentById(R.id.containerFragments);


        if (fr != null){
            if (fr.getTag() == "CL") {
                fabAddCourse.show();
                resetCourseID();
            }
        }

        else{
            finish();
        }
    }

    public void hideActionButton(){
        fabAddCourse.hide();
    }

    public void reloadCourseList(){

        resetCourseID();
        getSupportFragmentManager().beginTransaction().replace(R.id.containerFragments, new CourseList(), "CL").addToBackStack(null).commit();
        fabAddCourse.show();
    }

    public void resetCourseID(){
        courseID = 0;
    }

    public void populateCourse(long id){

        courseID = id;
        fabAddCourse.hide();
        getSupportFragmentManager().beginTransaction().replace(R.id.containerFragments, new CourseView(), "CV").addToBackStack(null).commit();

    }

    public void loadAssignments(String courseID){

        course_id = courseID;
        fabAddCourse.hide();
        getSupportFragmentManager().beginTransaction().replace(R.id.containerFragments, new AssignmentList(), "AL").addToBackStack(null).commit();
    }

    public void setPressedEdit(Boolean pressed){

        if (pressed){
            pressedEdit = pressed;
        }
    }

    public Boolean getPressedEdit(){
        return pressedEdit;
    }

    public void populateEditCourse(long id){

        courseID = id;
        getSupportFragmentManager().beginTransaction().replace(R.id.containerFragments, new CourseEdit(), "CE").addToBackStack(null).commit();

    }

    public String getCourse_ID(){
        return course_id;
    }

    public long getID(){
        return courseID;
    }




}
