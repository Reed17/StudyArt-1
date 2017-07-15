package ua.artcode.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by v21k on 15.04.17.
 */
@Entity
public class Lesson implements Comparable<Lesson> {

    // todo remove fields, describe schema
    @Id
    @GeneratedValue
    @Column(name = "LESSON_ID")
    private Integer id;

    private int courseID;

    @NotNull
    private String name;
    @NotNull
    private String lessonPackage;

    @ElementCollection
    private List<String> baseClasses;
    @ElementCollection
    private List<String> testsClasses;

    private String sourcesRoot;
    private String testsRoot;
    private String description;
    @Lob
    @Column( length = 100000 )
    private String theory;



    // todo date
    public Lesson() {
    }

    public Lesson(String name, String localPath) {
        this.name = name;
        this.lessonPackage = localPath;
    }


    public Lesson(String name,
                  String localPath,
                  List<String> baseClasses,
                  List<String> testsClasses,
                  String sourcesRoot,
                  String testsRoot,
                  String description,
                  String theory) {
        this.name = name;
        this.lessonPackage = localPath;
        this.baseClasses = baseClasses;
        this.testsClasses = testsClasses;
        this.sourcesRoot = sourcesRoot;
        this.testsRoot = testsRoot;
        this.description = description;
        this.theory = theory;
    }

    public Lesson(String name,
                  String localPath,
                  List<String> baseClasses,
                  List<String> testsClasses,
                  String sourcesRoot,
                  String testsRoot,
                  String description) {
        this.name = name;
        this.lessonPackage = localPath;
        this.baseClasses = baseClasses;
        this.testsClasses = testsClasses;
        this.sourcesRoot = sourcesRoot;
        this.testsRoot = testsRoot;
        this.description = description;

    }

    public List<String> getBaseClasses() {
        return baseClasses;
    }

    public void setBaseClasses(List<String> baseClasses) {
        this.baseClasses = baseClasses;
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
        return lessonPackage;
    }

    public void setLocalPath(String localPath) {
        this.lessonPackage = localPath;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int course) {
        this.courseID = course;
    }

    public String getTheory() {
        return theory;
    }

    public void setTheory(String theory) {
        this.theory = theory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Lesson lesson = (Lesson) o;

        if (courseID != lesson.courseID) return false;
        return id != null ? id.equals(lesson.id) : lesson.id == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + courseID;
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
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
