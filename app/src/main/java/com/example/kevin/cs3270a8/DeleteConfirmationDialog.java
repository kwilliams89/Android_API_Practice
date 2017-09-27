package com.example.kevin.cs3270a8;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;


public class DeleteConfirmationDialog extends DialogFragment {

    long courseID;

    public DeleteConfirmationDialog() {
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Dialog dialog = builder.setMessage("This will permanently delete the course.")
                .setCancelable(false)
                .setTitle("Are You Sure?")
                .setPositiveButton("Delete",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DatabaseHelper dbHelper = new DatabaseHelper(getActivity(), "School", null, 1);
                                MainActivity ma = (MainActivity) getActivity();
                                courseID = ma.getID();
                                if (courseID > 0) {
                                    dbHelper.deleteCourse(courseID);
                                    ma.reloadCourseList();
                                }
                            }
                        }
                )
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        }).create();

        return dialog;
    }

}
