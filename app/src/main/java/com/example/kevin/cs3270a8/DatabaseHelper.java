package com.example.kevin.cs3270a8;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {

    private SQLiteDatabase database;

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }

    public SQLiteDatabase open()
    {
        database = getWritableDatabase();
        return database;
    }

    public void close()
    {
        if (database != null)
            database.close();
    }

    public long addCourse(String id, String name, String code, String start, String end){

        long rowID = -1;

        ContentValues newCourse = new ContentValues();
        newCourse.put("id", id);
        newCourse.put("name", name);
        newCourse.put("course_code", code);
        newCourse.put("start_at", start);
        newCourse.put("end_at", end);

        if (open() != null){
            rowID = database.insert("courses", null, newCourse);
            close();
        }
        return rowID;
    }

    public long updateCourse(long _id, String id, String name, String code, String start, String end){

        long rowID = -1;

        ContentValues editCourse = new ContentValues();
        editCourse.put("id", id);
        editCourse.put("name", name);
        editCourse.put("course_code", code);
        editCourse.put("start_at", start);
        editCourse.put("end_at", end);

        if (open() != null){
            rowID = database.update("courses", editCourse, "_id=" + _id, null);
            close();
        }
        return rowID;
    }

    public Cursor getOneCourse(long id){

        String[] params = new String[1];
        params[0] = "" + id;
        Cursor cursor = null;
        if (open() != null){
            cursor = database.rawQuery("SELECT * FROM courses WHERE _id = ?", params);
    }
        return cursor;
    }

    public void deleteAllCourses(){
        if (open() != null) {
            database.delete("courses", null, null);
        }
    }

    public void deleteCourse(long id){

        if (open() != null) {
            database.delete("courses", "_id" + "=?", new String[]{String.valueOf(id)});
            close();
        }

    }

    public Cursor getAllCourses(){

        Cursor cursor = null;
        if (open() != null){
            cursor = database.rawQuery("SELECT * FROM courses ORDER BY name", null);
        }
        return cursor;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createQuery = "CREATE TABLE courses " +
                "(_id integer primary key autoincrement," +
                "id TEXT, name TEXT, course_code TEXT," +
                "start_at TEXT, end_at TEXT);";

        db.execSQL(createQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }


}
