package me.vadik.instaclimb.model.common;

import static me.vadik.instaclimb.model.contract.CommonContract.*;

/**
 * User: vadik
 * Date: 5/6/16
 */
public class CommonObject extends BaseObject {

    public final int id;
    public final String name;

    public CommonObject(ObjectBuilder builder) {
        super(builder);
        id = builder.getInt(_ID);
        name = builder.getString(NAME);
    }
}
