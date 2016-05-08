package me.vadik.instaclimb.model;

import android.database.Cursor;

import me.vadik.instaclimb.model.common.CommonObject;
import me.vadik.instaclimb.model.common.ObjectBuilder;
import me.vadik.instaclimb.model.contract.UsersRoutesViewContract;

import static me.vadik.instaclimb.model.contract.UserContract.*;

/**
 * User: vadik
 * Date: 4/13/16
 */
public class User extends CommonObject {

    public final String date;
    public final int rating;
    public final int height;
    public final int weight;
    public final int sex;
    public final String flash_boulder;
    public final String redpoint_boulder;
    public final String flash_lead;
    public final String redpoint_lead;
    public final String about;
    public final boolean hasPicture;

    public User(ObjectBuilder builder) {
        super(builder);
        date = builder.getString(UsersRoutesViewContract.DATE); // todo is it ok?
        rating = builder.getInt(RATING);
        height = builder.getInt(HEIGHT);
        weight = builder.getInt(WEIGHT);
        sex = builder.getInt(SEX);
        flash_boulder = builder.getString(FLASH_BOULDERING);
        redpoint_boulder = builder.getString(REDPOINT_BOULDERING);
        flash_lead = builder.getString(FLASH_LEAD);
        redpoint_lead = builder.getString(REDPOINT_LEAD);
        about = builder.getString(ABOUT);
        hasPicture = builder.getBoolean(HAS_PICTURE);
    }

    public static class Builder extends ObjectBuilder<User> {

        public Builder(Cursor cursor) {
            super(cursor);
        }

        public Builder(int id, String name) {
            super(id, name);
        }

        @Override
        public User build() {
            return new User(this);
        }
    }
}
