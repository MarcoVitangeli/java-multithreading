package org.marcovitangeli.fileprocess;

import java.util.Objects;

public class BadFileInfo {
    final private String filePath;
    final private String badWords;

    public BadFileInfo(String fileName, String badWords) {
        this.badWords = badWords;
        this.filePath = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getBadWords() {
        return badWords;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BadFileInfo that = (BadFileInfo) o;
        return filePath.equals(that.filePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filePath);
    }

    @Override
    public String toString() {
        return "BadFileInfo{" +
                "filePath='" + filePath + '\'' +
                ", badWords='" + badWords + '\'' +
                '}';
    }
}
