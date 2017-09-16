package ua.artcode.model.response;

import java.util.List;

/**
 * Created by zhenia on 16.09.17.
 */
public class LessonResponse {
    private Integer id;
    private String name;
    private List<String> classes;
    private String description;
    private String theory;

    public LessonResponse(Integer id, String name, List<String> classes, String description, String theory) {
        this.id = id;
        this.name = name;
        this.classes = classes;
        this.description = description;
        this.theory = theory;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getClasses() {
        return classes;
    }

    public void setClasses(List<String> classes) {
        this.classes = classes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTheory() {
        return theory;
    }

    public void setTheory(String theory) {
        this.theory = theory;
    }
}
