package me.vadik.instaclimb.routes.dummy;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DummyContent {

    public static final List<DummyItem> ITEMS = new ArrayList<>();
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<>();

    public static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static void clear() {
        ITEMS.clear();
        ITEM_MAP.clear();
    }

    public static class DummyItem {
        public final String id;
        public String name;
        public final String grade;
        public final Integer pictureId;

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
            return "http://instaclimb.ru/maps/" + pictureId + "/small.jpg";
        }

        public String getPictureUrl() {
            return "http://instaclimb.ru/maps/" + pictureId + "/full.jpg";
        }
    }
}
