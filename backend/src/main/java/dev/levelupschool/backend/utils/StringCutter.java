package dev.levelupschool.backend.utils;

public class StringCutter {
    public static String truncate(String content) {
        int length = content.length();
        return content.substring(0, length/3);
    }
}
