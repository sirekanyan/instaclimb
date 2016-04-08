package me.vadik.instaclimb.routes.dummy;

import android.database.Cursor;

/**
 * User: vadik
 * Date: 4/8/16
 */
public class DummyItem {
    public final String id;
    public String name;
    public final String grade;
    public final Integer pictureId;

    private String firstColor = null;
    private String secondColor = null;
    private String thirdColor = null;

    public String getFirstColor() {
        return firstColor;
    }

    public String getSecondColor() {
        return secondColor;
    }

    public String getThirdColor() {
        return thirdColor;
    }

    public boolean isArchived() {
        return archived;
    }

    public boolean isDraft() {
        return draft;
    }

    private final boolean archived;
    private final boolean draft;
    public String details;

    public DummyItem(String id, String content, Cursor cursor) {
        this.id = id;
        this.name = content.replaceAll(" \\(.*?\\)$", ""); //TODO remove replacement
        this.details = "";

        this.pictureId = cursor.getInt(cursor.getColumnIndex("picture_id"));
        this.grade = cursor.getString(cursor.getColumnIndex("grade"));

        String grade_author = cursor.getString(cursor.getColumnIndex("grade_author"));
        String grade_users = cursor.getString(cursor.getColumnIndex("grade_users"));
        String color = cursor.getString(cursor.getColumnIndex("color"));
        String author = cursor.getString(cursor.getColumnIndex("author"));
        String comment = cursor.getString(cursor.getColumnIndex("comment"));
        String created_when = cursor.getString(cursor.getColumnIndex("created_when"));
        String destroyed_when = cursor.getString(cursor.getColumnIndex("destroyed_when"));
        String status = cursor.getString(cursor.getColumnIndex("status"));
        Integer sector_id = cursor.getInt(cursor.getColumnIndex("sector_id"));

        color = color.replaceAll("^,", ""); //TODO remove replacement

        if (!color.isEmpty()) {
            String[] colors = color.split(",");
            switch (colors.length) {
                case 3:
                    thirdColor = colors[2];
                case 2:
                    secondColor = colors[1];
                case 1:
                    firstColor = colors[0];
            }
        }

        this.archived = "Архив".equals(status);
        this.draft = "Черновик".equals(status);

        if (archived || draft) {
            this.name += " (" + status.toLowerCase() + ")";
        }

        if (!grade.isEmpty())
            details += "Сложность: " + grade + "\n";
        if (!grade_author.isEmpty())
            details += "Оценка автора: " + grade_author + "\n";
        if (!grade_users.isEmpty() && !"(нет оценок)".equals(grade_users))
            details += "Оценка юзеров: " + grade_users + "\n";
        if (!color.isEmpty())
            details += "Цвет меток: " + color + "\n";
        if (!author.isEmpty())
            details += "Автор: " + author + "\n";
        if (!comment.isEmpty())
            details += "Комментарий: " + comment + "\n";
        if (!created_when.isEmpty())
            details += "Дата накрутки: " + created_when + "\n";
        if (!destroyed_when.isEmpty() && !"Не задан".equals(destroyed_when))
            details += "Дата скрутки: " + destroyed_when + "\n";
        if (!status.isEmpty())
            details += "Статус: " + status + "\n";
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