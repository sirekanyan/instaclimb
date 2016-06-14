package me.vadik.instaclimb.view;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.vadik.instaclimb.R;

/**
 * User: vadik
 * Date: 6/14/16
 */
public class RouteFilter {

    public static List<String> getGradeFilterArgs(Context ctx, int menuId) {

        List<String> gradeFilterArgs = new ArrayList<>();

        int res = 0;

        switch (menuId) {
            case R.id.grade_very_easy:
                res = R.array.very_easy_grades;
                break;
            case R.id.grade_easy:
                res = R.array.easy_grades;
                break;
            case R.id.grade_medium:
                res = R.array.medium_grades;
                break;
            case R.id.grade_advanced:
                res = R.array.advanced_grades;
                break;
            case R.id.grade_hard:
                res = R.array.hard_grades;
                break;
            case R.id.grade_very_hard:
                res = R.array.very_hard_grades;
                break;
        }

        if (res != 0) {
            String[] grades = ctx.getResources().getStringArray(res);
            gradeFilterArgs.addAll(Arrays.asList(grades));
        }

        return gradeFilterArgs;
    }
}
