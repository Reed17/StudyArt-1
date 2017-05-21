package ua.artcode.model;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by v21k on 15.04.17.
 */
@Entity
public class Lesson implements Comparable<Lesson> {

    @Id
    @GeneratedValue
    private Integer id;

    @NotNull
    private Integer courseID;
    @NotNull
    private String name;
    @NotNull
    private String localPath;

    @ElementCollection
    private List<String> baseClasses;
    @ElementCollection
    private List<String> requiredClasses;
    @ElementCollection
    private List<String> testsClasses;

    // todo sourcesRoot here - is only directory for lesson with .java files. For RunCore we need PROJECT sources root (src/main/java), not path to lesson dir
    private String sourcesRoot;
    private String testsRoot;
    private String description;

    //    @Column(name="timestamp", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
//    private LocalDateTime date;
// todo date
    public Lesson() {
    }

    public Lesson(String name, String localPath) {
        this.name = name;
        this.localPath = localPath;
    }

    public List<String> getBaseClasses() {
        return baseClasses;
    }

    public void setBaseClasses(List<String> baseClasses) {
        this.baseClasses = baseClasses;
    }

    public List<String> getRequiredClasses() {
        return requiredClasses;
    }

    public void setRequiredClasses(List<String> requiredClasses) {
        this.requiredClasses = requiredClasses;
    }

    public List<String> getTestsClasses() {
        return testsClasses;
    }

    public void setTestsClasses(List<String> testsClasses) {
        this.testsClasses = testsClasses;
    }

    public String getSourcesRoot() {
        return sourcesRoot;
    }

    public void setSourcesRoot(String sourcesRoot) {
        this.sourcesRoot = sourcesRoot;
    }

    public String getTestsRoot() {
        return testsRoot;
    }

    public void setTestsRoot(String testsRoot) {
        this.testsRoot = testsRoot;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//    public LocalDateTime getDate() {
//        return date;
//    }
//
//    public void setDate(LocalDateTime date) {
//        this.date = date;
//    }

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

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public Integer getCourseID() {
        return courseID;
    }

    public void setCourseID(Integer courseID) {
        this.courseID = courseID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Lesson lesson = (Lesson) o;

        if (courseID != null ? !courseID.equals(lesson.courseID) : lesson.courseID != null) return false;
        if (name != null ? !name.equals(lesson.name) : lesson.name != null) return false;
        return description != null ? description.equals(lesson.description) : lesson.description == null;
    }

    @Override
    public int hashCode() {
        int result = courseID != null ? courseID.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(Lesson o) {
        return this.name.compareTo(o.name);
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "id=" + id +
                ", courseID=" + courseID +
                ", name='" + name + '\'' +
                ", baseClasses=" + baseClasses +
                ", requiredClasses=" + requiredClasses +
                ", testsClasses=" + testsClasses +
                ", description='" + description + '\'' +
                ", date=" + /*date**/ +
                '}';
    }
}
