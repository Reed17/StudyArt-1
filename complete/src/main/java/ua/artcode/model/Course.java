package ua.artcode.model;

import java.util.List;

/**
 * Created by v21k on 09.04.17.
 */
public class Course {
    private String gitURL;
    private String author;
    private List<Lesson> lessons;

    public Course() {
    }

    public Course(String gitURL, String author, List<Lesson> lessons) {
        this.gitURL = gitURL;
        this.author = author;
        this.lessons = lessons;
    }

    public String getGitURL() {
        return gitURL;
    }

    public void setGitURL(String gitURL) {
        this.gitURL = gitURL;
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

        if (gitURL != null ? !gitURL.equals(course.gitURL) : course.gitURL != null) return false;
        return author != null ? author.equals(course.author) : course.author == null;
    }

    @Override
    public int hashCode() {
        int result = gitURL != null ? gitURL.hashCode() : 0;
        result = 31 * result + (author != null ? author.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Course{" +
                "gitURL='" + gitURL + '\'' +
                ", author='" + author + '\'' +
                ", lessons=" + lessons +
                '}';
    }
}
