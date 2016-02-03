package com.benzino.fiveminworkout;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Anas on 1/2/16.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    //Database Handler
    private static DatabaseHandler instance = null;

    //Database version
    private static final int DATABASE_VERSION = 4;

    //Database name
    private static final String DATABASE_NAME = "workout";

    //Table name
    private static final String TABLE_TESTS = "tests";

    //Column names
    private static final String KEY_ID = "id";
    private static final String KEY_TYPE = "type";
    private static final String KEY_CREATED_AT = "created_at";

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    //Using the singleton design pattern to connect with the database
    public synchronized static DatabaseHandler getInstance(Context context){
        // Use the application context, which will ensure that you
        // don't accidentally leak on the Activity's context
        if(instance == null){
            instance = new DatabaseHandler(context);
        }
        return instance;
    }

    private DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    //Creating database and tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_TESTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TYPE + " TEXT,"
                + KEY_CREATED_AT + " DATETIME" + ");";

        db.execSQL(CREATE_USERS_TABLE);
    }

    //Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TESTS);

        // Create tables again
        onCreate(db);
    }


    //Create a test according to a user
    public void createTest(Test test){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //Test type
        values.put(KEY_TYPE, test.getType());

        //Test created at
        values.put(KEY_CREATED_AT, getDateTime());

        //Inserting row in the test table
        db.insert(TABLE_TESTS, null, values);

        //Close database connection
        db.close();
    }


    //Getting  tests count
    public int getTestsCount(){
        String query = "SELECT * FROM "+TABLE_TESTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.close();
        db.close();
        return cursor.getCount();
    }

    //Get test count for the last 30days
    public int getTestsInMonth(Calendar calendar){
        SQLiteDatabase db = this.getReadableDatabase();

        String date = dateFormat.format(calendar.getTime());
        calendar.add(Calendar.DATE, -30);
        String dateBeforeMonth = dateFormat.format(calendar.getTime());
        String query = "SELECT * FROM "
                        +TABLE_TESTS
                        +" WHERE "
                        + KEY_CREATED_AT
                        +" BETWEEN '"
                        + dateBeforeMonth
                        + "' AND '"
                        + date
                        +"'; ";
        Log.d("ANAS", query);
        calendar.setTime(Calendar.getInstance().getTime());
        calendar = Calendar.getInstance();
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    //Find all tests
    public List<Test> findAllTests(){
        SQLiteDatabase db = this.getWritableDatabase();

        List<Test> tests = new ArrayList<Test>();

        String query = "SELECT * FROM "
                + TABLE_TESTS;

        Cursor cursor = db.rawQuery(query, null);

        //Looping through the table and add all the tests
        if(cursor.moveToFirst()){
            do{
                Test test = new Test();
                test.setId(Integer.parseInt(cursor.getString(0)));
                test.setType(cursor.getString(1));
                test.setCreatedAt(cursor.getString(2));

                //Adding test to the list
                tests.add(test);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return tests;
    }

    //Get a dates where the user has worked out
    public List<CalendarDay> getDates(){
        SQLiteDatabase db = this.getWritableDatabase();

        List<CalendarDay> dates = new ArrayList<CalendarDay>();
        Calendar cal = Calendar.getInstance();


        String query = "SELECT * FROM "
                        + TABLE_TESTS +
                        " GROUP BY "
                        + KEY_CREATED_AT;

        Cursor cursor = db.rawQuery(query, null);

        //Looping through the table and add all the dates
        if(cursor.moveToFirst()){
            do{
                try {
                    cal.setTime(dateFormat.parse(cursor.getString(2)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                CalendarDay calendarDay = CalendarDay.from(cal);
                dates.add(calendarDay);
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return dates ;
    }

    //Created_at value current datetime
    private String getDateTime() {
        Date date = new Date();
        return dateFormat.format(date);
    }
}
