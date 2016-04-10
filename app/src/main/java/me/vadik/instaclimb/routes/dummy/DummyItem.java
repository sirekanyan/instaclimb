package me.vadik.instaclimb.routes.dummy;

import android.database.Cursor;

import me.vadik.instaclimb.routes.contract.StatusValues;

/**
 * User: vadik
 * Date: 4/8/16
 */
public class DummyItem {
    public final String id;
    public String name;
    public final String grade;
    public final Integer pictureId;

    public String details;

    public DummyItem(String id, String name, Cursor cursor) {
        this.id = id;
        this.name = name;
        this.details = "";

        this.pictureId = cursor.getInt(cursor.getColumnIndex("picture_id"));
        this.grade = cursor.getString(cursor.getColumnIndex("grade"));

        String grade_author = cursor.getString(cursor.getColumnIndex("grade_author"));
        String grade_users = cursor.getString(cursor.getColumnIndex("grade_users"));
        int color1 = cursor.getInt(cursor.getColumnIndex("color1"));
        int color2 = cursor.getInt(cursor.getColumnIndex("color2"));
        int color3 = cursor.getInt(cursor.getColumnIndex("color3"));
        String author = cursor.getString(cursor.getColumnIndex("author"));
        String comment = cursor.getString(cursor.getColumnIndex("comment"));
        String created_when = cursor.getString(cursor.getColumnIndex("created_when"));
        String destroyed_when = cursor.getString(cursor.getColumnIndex("destroyed_when"));
        Integer status = cursor.getInt(cursor.getColumnIndex("status"));
        Integer sector_id = cursor.getInt(cursor.getColumnIndex("sector_id"));

        boolean archived = StatusValues.ARCHIVE.equals(status);
        boolean draft = StatusValues.DRAFT.equals(status);

        if (archived || draft) {
            this.name += " (" + status.toString() + ")";
        }

        if (!grade.isEmpty())
            details += "Сложность: " + grade + "\n";
        if (!grade_author.isEmpty())
            details += "Оценка автора: " + grade_author + "\n";
        if (!grade_users.isEmpty() && !"(нет оценок)".equals(grade_users))
            details += "Оценка юзеров: " + grade_users + "\n";

        details += "Цвет меток: " + String.valueOf(color1) + " " + String.valueOf(color2) + " " + String.valueOf(color3) + "\n";

        if (!author.isEmpty())
            details += "Автор: " + author + "\n";
        if (!comment.isEmpty())
            details += "Комментарий: " + comment + "\n";
        if (!created_when.isEmpty())
            details += "Дата накрутки: " + created_when + "\n";
        if (!destroyed_when.isEmpty() && !"Не задан".equals(destroyed_when))
            details += "Дата скрутки: " + destroyed_when + "\n";

        details += "Статус: " + status.toString() + "\n";

        if (!sector_id.toString().isEmpty())
            details += "Сектор: " + sector_id + "\n";
    }

    @Override
    public String toString() {
        return name;
    }

    public String getThumbnailUrl() {
        return "http://instaclimb.ru/maps/" + pictureId + "/thumb.jpg";
    }

    public String getSmallPictureUrl() {
        return getSmallPictureUrl(pictureId.toString());
    }

    public static String getSmallPictureUrl(String pictureId) {
        return "http://instaclimb.ru/maps/" + pictureId + "/small.jpg";
    }

    public static String getPictureUrl(String pictureId) {
        return "http://instaclimb.ru/maps/" + pictureId + "/full.jpg";
    }

    public Integer getPictureId() {
        return pictureId;
    }
}