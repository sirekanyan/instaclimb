package me.vadik.instaclimb.routes.dummy;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.vadik.instaclimb.R;
import me.vadik.instaclimb.routes.RoutesContentProvider;
import me.vadik.instaclimb.routes.SectorFragment;

/**
 * User: vadik
 * Date: 4/7/16
 */
public class DummyItemsHelper {

    public static Map<String, Integer> COLORS;

    static {
        COLORS = new HashMap<>();
        COLORS.put("черный", R.drawable.rect_black);
        COLORS.put("синий", R.drawable.rect_blue);
        COLORS.put("голубой", R.drawable.rect_blue_light);
        COLORS.put("коричневый", R.drawable.rect_brown);
        COLORS.put("серый", R.drawable.rect_gray);
        COLORS.put("зеленый", R.drawable.rect_green);
        COLORS.put("оранжевый", R.drawable.rect_orange);
        COLORS.put("розовый", R.drawable.rect_pink);
        COLORS.put("фиолетовый", R.drawable.rect_purple);
        COLORS.put("красный", R.drawable.rect_red);
        COLORS.put("белый", R.drawable.rect_white);
        COLORS.put("желтый", R.drawable.rect_yellow);
    }

    public static List<DummyContent.DummyItem> getDummyItems(Context context, String[] statusFilterArgs, String[] gradeFilterArgs, Integer mSectorId) {
        DummyContent.clear();

        Uri myUri = Uri.withAppendedPath(RoutesContentProvider.CONTENT_URI, "routes");

        String[] statusPlaceHolders = new String[statusFilterArgs.length];
        String[] gradePlaceHolders = new String[gradeFilterArgs.length];

        for (int i = 0; i < statusPlaceHolders.length; i++) {
            statusPlaceHolders[i] = "?";
        }

        for (int i = 0; i < gradePlaceHolders.length; i++) {
            gradePlaceHolders[i] = "?";
        }

        String[] args = new String[statusFilterArgs.length + gradeFilterArgs.length];
        System.arraycopy(statusFilterArgs, 0, args, 0, statusFilterArgs.length);
        System.arraycopy(gradeFilterArgs, 0, args, statusFilterArgs.length, gradeFilterArgs.length);

        String statusInClause = "status in (" + TextUtils.join(",", statusPlaceHolders) + ") ";

        String gradeInClause = "";
        if (gradeFilterArgs.length > 0) {
            gradeInClause = "and lower(grade) in (" + TextUtils.join(",", gradePlaceHolders) + ") ";
        }

        String sectorClause = "";
        if (mSectorId != null && mSectorId != SectorFragment.ALL_SECTORS) {
            String[] argsWithSector = new String[args.length + 1];
            System.arraycopy(args, 0, argsWithSector, 0, args.length);
            args = argsWithSector;
            args[args.length - 1] = String.valueOf(mSectorId);
            sectorClause = "and sector_id = ?";
        }

        String sqlSelection = statusInClause + gradeInClause + sectorClause;

        Cursor cursor = context.getContentResolver().query(
                myUri,
                null,
                sqlSelection,
                args,
                "id desc");

        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Integer id = cursor.getInt(cursor.getColumnIndex("id"));
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    DummyContent.addItem(new DummyContent.DummyItem(id.toString(), name, cursor));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return DummyContent.ITEMS;
    }
}
