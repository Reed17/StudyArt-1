package ua.artcode.model;

import java.util.List;

/**
 * Created by v21k on 09.04.17.
 */
public class Course {
    private int id;
    private String gitURL;
    private String courseLocalPath;
    private String author;
    private List<Lesson> lessons;

    public Course() {
    }

    public Course(int id, String gitURL, String coruseLocalPath, String author, List<Lesson> lessons) {
        this.id = id;
        this.gitURL = gitURL;
        this.courseLocalPath = coruseLocalPath;
        this.author = author;
        this.lessons = lessons;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGitURL() {
        return gitURL;
    }

    public void setGitURL(String gitURL) {
        this.gitURL = gitURL;
    }

    public String getCourseLocalPath() {
        return courseLocalPath;
    }

    public void setCourseLocalPath(String courseLocalPath) {
        this.courseLocalPath = courseLocalPath;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Course course = (Course) o;

        if (id != course.id) return false;
        if (gitURL != null ? !gitURL.equals(course.gitURL) : course.gitURL != null) return false;
        if (courseLocalPath != null ? !courseLocalPath.equals(course.courseLocalPath) : course.courseLocalPath != null)
            return false;
        return author != null ? author.equals(course.author) : course.author == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (gitURL != null ? gitURL.hashCode() : 0);
        result = 31 * result + (courseLocalPath != null ? courseLocalPath.hashCode() : 0);
        result = 31 * result + (author != null ? author.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", gitURL='" + gitURL + '\'' +
                ", courseLocalPath='" + courseLocalPath + '\'' +
                ", author='" + author + '\'' +
                ", lessons=" + lessons +
                '}';
    }
}
