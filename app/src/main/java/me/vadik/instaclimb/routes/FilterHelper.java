package me.vadik.instaclimb.routes;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.vadik.instaclimb.R;

/**
 * User: vadik
 * Date: 4/7/16
 */
public class FilterHelper {

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

    public static List<String> getStatusFilterArgs(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        List<String> statusFilterArgs = new ArrayList<>();

        statusFilterArgs.add("Активна");

        if (preferences.getBoolean("show_archived", false)) {
            statusFilterArgs.add("Архив");
        }
        if (preferences.getBoolean("show_draft", false)) {
            statusFilterArgs.add("Черновик");
        }

        return statusFilterArgs;
    }

    public static List<String> getGradeFilterArgs(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        List<String> gradeFilterArgs = new ArrayList<>();
        switch (preferences.getInt("grade", -1)) {
            case 0:
                gradeFilterArgs.addAll(Arrays.asList(
                        "5a", "5a+", "5b", "5b+", "5c", "5c+",
                        "6a", "6a+"
                ));
                break;
            case 1:
                gradeFilterArgs.addAll(Arrays.asList(
                        "6a", "6a+",
                        "6b", "6b+"
                ));
                break;
            case 2:
                gradeFilterArgs.addAll(Arrays.asList(
                        "6b", "6b+",
                        "6c", "6c+"
                ));
                break;
            case 3:
                gradeFilterArgs.addAll(Arrays.asList(
                        "6c", "6c+",
                        "7a", "7a+"
                ));
                break;
            case 4:
                gradeFilterArgs.addAll(Arrays.asList(
                        "7a", "7a+",
                        "7b", "7b+"
                ));
                break;
            case 5:
                gradeFilterArgs.addAll(Arrays.asList(
                        "7b", "7b+",
                        "7c", "7c+"
                ));
                break;
            case 6:
                gradeFilterArgs.addAll(Arrays.asList(
                        "7c", "7c+",
                        "8a", "8a+", "8b", "8b+", "8c", "8c+", "9a"
                ));
                break;
        }

        return gradeFilterArgs;
    }
}
