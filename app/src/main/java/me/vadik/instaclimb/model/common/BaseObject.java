package me.vadik.instaclimb.model.common;

import static me.vadik.instaclimb.model.contract.BaseContract.TS;

/**
 * User: vadik
 * Date: 5/7/16
 */
public class BaseObject {

    public final int ts;

    public BaseObject(CursorBuilder builder) {
        ts = builder.getInt(TS); // wait 2038 :)
    }
}
