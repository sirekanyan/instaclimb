package me.vadik.instaclimb.model;

import android.database.Cursor;

import me.vadik.instaclimb.model.common.CommonObject;
import me.vadik.instaclimb.model.common.ObjectBuilder;
import me.vadik.instaclimb.model.contract.RouteViewContract;
import me.vadik.instaclimb.model.contract.UsersRoutesContract;

import static me.vadik.instaclimb.model.contract.RouteContract.*;

/**
 * User: vadik
 * Date: 4/13/16
 */
public class Route extends CommonObject {

    public final String grade;
    public final int color1;
    public final int color2;
    public final int color3;
    public final int userId;
    public final String userName;
    public final String comment;
    public final String createdWhen;
    public final String destroyedWhen;
    public final boolean isActive;
    public final int pictureId;
    public final int sectorId;
    public final int done;
    public final String climbedWhen;

    public Route(Builder builder) {
        super(builder);
        grade = builder.getString(GRADE);
        color1 = builder.getInt(COLOR1);
        color2 = builder.getInt(COLOR2);
        color3 = builder.getInt(COLOR3);
        userId = builder.getInt(USER_ID);
        userName = builder.getString(RouteViewContract.USER_NAME); // todo is it ok?
        comment = builder.getString(COMMENT);
        createdWhen = builder.getString(CREATED_WHEN);
        destroyedWhen = builder.getString(DESTROYED_WHEN);
        isActive = builder.getBoolean(IS_ACTIVE);
        pictureId = builder.getInt(PICTURE_ID);
        sectorId = builder.getInt(SECTOR_ID);
        done = builder.getInt(DONE);
        climbedWhen = builder.getString(UsersRoutesContract.DATE);
    }

    public static class Builder extends ObjectBuilder<Route> {

        public Builder(Cursor cursor) {
            super(cursor);
        }

        public Builder(int id, String name) {
            super(id, name);
        }

        @Override
        public Route build() {
            return new Route(this);
        }
    }
}
