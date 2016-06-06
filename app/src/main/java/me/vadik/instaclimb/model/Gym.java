package me.vadik.instaclimb.model;

import android.database.Cursor;

import me.vadik.instaclimb.model.common.CommonObject;
import me.vadik.instaclimb.model.common.ObjectBuilder;

/**
 * User: vadik
 * Date: 5/8/16
 */
public class Gym extends CommonObject {

    public Gym(ObjectBuilder builder) {
        super(builder);
    }

    public static class Builder extends ObjectBuilder<Gym> {

        public Builder(Cursor cursor) {
            super(cursor);
        }

        @Override
        public Gym build() {
            return new Gym(this);
        }
    }
}
