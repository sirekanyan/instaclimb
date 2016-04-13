package me.vadik.instaclimb.model;

import android.database.Cursor;

/**
 * User: vadik
 * Date: 4/8/16
 */
public class RouteDetail {
    public final String id;
    private final String author;
    private final Integer author_id;
    public final String name;
    public final String grade;
    public final Integer pictureId;

    public String details;

    public RouteDetail(String id, String name, Cursor cursor) {
        this.id = id;
        this.name = name;

        this.author = cursor.getString(cursor.getColumnIndex("user_name"));
        this.author_id = cursor.getInt(cursor.getColumnIndex("user_id"));

        this.details = "";

        this.pictureId = cursor.getInt(cursor.getColumnIndex("picture_id"));
        this.grade = cursor.getString(cursor.getColumnIndex("grade"));

        int color1 = cursor.getInt(cursor.getColumnIndex("color1"));
        int color2 = cursor.getInt(cursor.getColumnIndex("color2"));
        int color3 = cursor.getInt(cursor.getColumnIndex("color3"));
        String comment = "";//TODO cursor.getString(cursor.getColumnIndex("comment"));
        String created_when = cursor.getString(cursor.getColumnIndex("created_when"));
//        Integer status = cursor.getInt(cursor.getColumnIndex("status"));
        Integer sector_id = cursor.getInt(cursor.getColumnIndex("sector_id"));

        if (!grade.isEmpty())
            details += "Сложность: " + grade + "\n";

        details += "Цвет меток: " + String.valueOf(color1) + " " + String.valueOf(color2) + " " + String.valueOf(color3) + "\n";

        if (!comment.isEmpty())
            details += "Комментарий: " + comment + "\n";
        if (!created_when.isEmpty())
            details += "Дата накрутки: " + created_when + "\n";

        if (!sector_id.toString().isEmpty())
            details += "Сектор: " + sector_id + "\n";

        details += "Скалодром: ?\n";
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

    public String getAuthor() {
        return author;
    }

    public Integer getAuthorId() {
        return author_id;
    }
}