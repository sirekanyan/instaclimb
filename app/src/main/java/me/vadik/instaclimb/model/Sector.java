package me.vadik.instaclimb.model;

import android.database.Cursor;

import me.vadik.instaclimb.model.common.CommonObject;
import me.vadik.instaclimb.model.common.ObjectBuilder;

import static me.vadik.instaclimb.model.contract.SectorContract.*;

/**
 * User: vadik
 * Date: 5/7/16
 */
public class Sector extends CommonObject {

    public final int gymId;

    public Sector(ObjectBuilder builder) {
        super(builder);
        gymId = builder.getInt(GYM_ID);
    }

    public static class Builder extends ObjectBuilder<Sector> {

        public Builder(Cursor cursor) {
            super(cursor);
        }

        public Builder(int id, String name) {
            super(id, name);
        }

        @Override
        public Sector build() {
            return new Sector(this);
        }
    }
}
