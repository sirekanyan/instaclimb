package me.vadik.instaclimb.routes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * User: vadik
 * Date: 4/1/16
 */
//TODO: use this class when copying of db will be unnecessary
public class RoutesDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "routes.db";

    public RoutesDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL("create table example (id int, name varchar(50))");
//        db.execSQL("insert into example (id, name) values (1, 'one')");
//        db.execSQL("insert into example (id, name) values (2, 'two')");
//        db.execSQL("insert into example (id, name) values (3, 'three')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("drop table example");
//        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("drop table example");
//        onCreate(db);
    }
}
