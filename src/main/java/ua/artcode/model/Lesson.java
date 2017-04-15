package ua.artcode.model;

import java.util.Arrays;
import java.util.List;

/**
 * Created by v21k on 09.04.17.
 */
public class Lesson {
    private String name;
    private String path;
    private String[] classes;

    public Lesson() {
    }

    public Lesson(String name, String path, String[] classes) {
        this.name = name;
        this.path = path;
        this.classes = classes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String[] getClasses() {
        return classes;
    }

    public void setClasses(String[] classes) {
        this.classes = classes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Lesson lesson = (Lesson) o;

        if (name != null ? !name.equals(lesson.name) : lesson.name != null) return false;
        if (path != null ? !path.equals(lesson.path) : lesson.path != null) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(classes, lesson.classes);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (path != null ? path.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(classes);
        return result;
    }


    @Override
    public String toString() {
        return "Lesson{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", classes=" + Arrays.toString(classes) +
                '}';
    }
}
