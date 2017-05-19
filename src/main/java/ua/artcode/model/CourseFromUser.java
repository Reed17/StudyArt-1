package ua.artcode.model;

import javax.validation.constraints.Pattern;

/**
 * Created by v21k on 15.04.17.
 */
public class CourseFromUser {
    private int id;
    @Pattern(regexp = "\\w{2,}", message = "Invalid course name")
    private String name;
    @Pattern(regexp = "^http(s?):.+\\.git$", message = "Invalid git URL")
    private String url;

    public CourseFromUser() {
    }

    public CourseFromUser(int id, String name, String url) {
        this.id = id;
        this.name = name;
        this.url = url;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CourseFromUser course = (CourseFromUser) o;

        if (name != null ? !name.equals(course.name) : course.name != null) return false;
        return url != null ? url.equals(course.url) : course.url == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}

