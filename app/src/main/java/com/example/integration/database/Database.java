package com.example.integration.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.integration.models.Student;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

// An Android helper class to manage database creation and version management using an application's raw asset files.
//
//This class provides developers with a simple way to ship their Android app with an existing SQLite database (which may be pre-populated with data) and to manage its initial creation and any upgrades required with subsequent version releases.
//
//It is implemented as an extension to SQLiteOpenHelper, providing an efficient way for ContentProvider implementations to defer opening and upgrading the database until first use.
//
//Rather than implementing the onCreate() and onUpgrade() methods to execute a bunch of SQL statements, developers simply include appropriately named file assets in their project's assets directory. These will include the initial SQLite database file for creation and optionally any SQL upgrade scripts.
public class Database extends SQLiteAssetHelper {
    //external library is used to load the existing database from the db and use directly into the application without any connection of sqlhelper

static final String DB_NAME="stud.db";
static final int DB_ver=1;
    
public Database(Context context) {
        super(context,DB_NAME,null,DB_ver);
    }

    public List<Student> getStudentByHtno(String htno){
        SQLiteDatabase db=getReadableDatabase();
        SQLiteQueryBuilder qb=new SQLiteQueryBuilder();
        //i am building the query here...
        String tableName="fail_list";
        String []sqlSelect ={"htno","branch","subject"};
        //i am setting the table name here to the ...table name variable..
        qb.setTables(tableName);
        Cursor cursor=qb.query(db,sqlSelect,"htno=?",new String[]{htno},null,null,null);
        List<Student> result=new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                Student student=new Student();
                student.setBranch(cursor.getString(cursor.getColumnIndex("branch")));
                student.setHtno(cursor.getString(cursor.getColumnIndex("htno")));
                student.setSubject(cursor.getString(cursor.getColumnIndex("subject")));
                result.add(student);
            }while(cursor.moveToNext());
        }
        return result;
    }

}
