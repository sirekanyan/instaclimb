package me.vadik.instaclimb.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * User: vadik
 * Date: 4/1/16
 */
//TODO: use this class when copying of db will be unnecessary
public class RoutesDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "instaclimb.db";

    public RoutesDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL("create table example (id int, name varchar(50))");
//        db.execSQL("insert into example (id, name) values (1, 'one')");
//        db.execSQL("insert into example (id, name) values (2, 'two')");
//        db.execSQL("insert into example (id, name) values (3, 'three')");

        //Create routes_view
        db.execSQL("create view routes_view as\n" +
                "select\n" +
                "  r._id,\n" +
                "  r.name,\n" +
                "  r.grade,\n" +
                "  r.color1,\n" +
                "  r.color2,\n" +
                "  r.color3,\n" +
                "  r.user_id,\n" +
                "  u.name user_name,\n" +
                "  r.comment,\n" +
                "  r.created_when,\n" +
                "  r.destroyed_when,\n" +
                "  r.is_active,\n" +
                "  r.sector_id,\n" +
                "  r.picture_id\n" +
                "from\n" +
                "  routes r,\n" +
                "  users u\n" +
                "where\n" +
                "  r.user_id = u._id;");

        //Create users_routes_view
        db.execSQL("create view users_routes_view as\n" +
                "select\n" +
                "  ur.user_id,\n" +
                "  u.name user_name,\n" +
                "  ur.route_id,\n" +
                "  ur.is_flash,\n" +
                "  ur.date\n" +
                "from\n" +
                "  users_routes ur,\n" +
                "  users u\n" +
                "where\n" +
                "  ur.user_id = u._id;");

        //Create routes_users_view
        db.execSQL("create view routes_users_view as\n" +
                "select\n" +
                "  ur.user_id,\n" +
                "  ur.route_id,\n" +
                "  r.name route_name,\n" +
                "  ur.is_flash,\n" +
                "  ur.date\n" +
                "from\n" +
                "  users_routes ur,\n" +
                "  routes r\n" +
                "where\n" +
                "  ur.route_id = r._id;");
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
