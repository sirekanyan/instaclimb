package me.vadik.instaclimb.model.common;

import android.database.Cursor;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import me.vadik.instaclimb.helper.CursorHelper;

import static me.vadik.instaclimb.model.contract.BaseObjectContract.NAME;
import static me.vadik.instaclimb.model.contract.BaseObjectContract._ID;

/**
 * User: vadik
 * Date: 5/8/16
 */
public abstract class CursorBuilder<T> {

    private final Map<String, String> stringParams = new HashMap<>();
    private final Map<String, Integer> intParams = new HashMap<>();

    public CursorBuilder(Cursor cursor) {
        CursorHelper helper = new CursorHelper(cursor);
        for (String columnName : cursor.getColumnNames()) {
            int type = helper.getType(columnName);
            switch (type) {
                case Cursor.FIELD_TYPE_STRING:
                    stringParams.put(columnName, helper.getString(columnName));
                    break;
                case Cursor.FIELD_TYPE_INTEGER:
                    intParams.put(columnName, helper.getInt(columnName));
                    break;
                case Cursor.FIELD_TYPE_NULL:
                    // do nothing
                    break;
                default:
                    Log.e("me", "Unsupported type: " + type);
                    break;
            }
        }
    }

    public CursorBuilder(int id, String name) {
        intParams.put(_ID, id);
        stringParams.put(NAME, name);
    }

    public abstract T build();

    public String getString(String columnName) {
        return stringParams.get(columnName);
    }

    public int getInt(String columnName) {
        if (intParams.containsKey(columnName)) {
            return intParams.get(columnName);
        }
        return 0;
    }

    public boolean getBoolean(String columnName) {
        return 1 == getInt(columnName);
    }
}
