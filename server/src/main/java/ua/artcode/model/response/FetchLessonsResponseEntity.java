package ua.artcode.model.response;

/**
 * Created by zhenia on 06.09.17.
 */
public class FetchLessonsResponseEntity {
    private String name;
    private Integer id;

    public FetchLessonsResponseEntity(String name, Integer id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
