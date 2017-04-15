package ua.artcode.model;

import java.util.List;

/**
 * Created by v21k on 15.04.17.
 */
public class Lesson implements Comparable<Lesson> {
    private String name;
    private String localPath;
    private List<String> classPaths;

    public Lesson() {
    }

    public Lesson(String name, String localPath, List<String> classPaths) {
        this.name = name;
        this.localPath = localPath;
        this.classPaths = classPaths;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public List<String> getClassPaths() {
        return classPaths;
    }

    public void setClassPaths(List<String> classPaths) {
        this.classPaths = classPaths;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Lesson lesson = (Lesson) o;

        if (name != null ? !name.equals(lesson.name) : lesson.name != null) return false;
        if (localPath != null ? !localPath.equals(lesson.localPath) : lesson.localPath != null) return false;
        return classPaths != null ? classPaths.equals(lesson.classPaths) : lesson.classPaths == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (localPath != null ? localPath.hashCode() : 0);
        result = 31 * result + (classPaths != null ? classPaths.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "name='" + name + '\'' +
                ", localPath='" + localPath + '\'' +
                ", classPaths=" + classPaths +
                '}';
    }

    @Override
    public int compareTo(Lesson o) {
        return this.name.compareTo(o.name);
    }
}
