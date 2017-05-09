package ua.artcode.model;

/**
 * Created by v21k on 15.04.17.
 */
public class Lesson implements Comparable<Lesson> {
    private int id;
    private String name;
    private String localPath;


    public Lesson() {
    }

    public Lesson(String name, String localPath) {
        this.name = name;
        this.localPath = localPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Lesson lesson = (Lesson) o;

        if (name != null ? !name.equals(lesson.name) : lesson.name != null) return false;
        return localPath != null ? localPath.equals(lesson.localPath) : lesson.localPath == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (localPath != null ? localPath.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(Lesson o) {
        return this.name.compareTo(o.name);
    }
}
