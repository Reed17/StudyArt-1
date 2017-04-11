package ua.artcode.model;

import java.util.List;

/**
 * Created by v21k on 09.04.17.
 */
public class Lesson {
    private String name;
    private List<String> activeClasses;
    private List<String> activeTests;

    public Lesson() {
    }

    public Lesson(String name, List<String> activeClasses, List<String> activeTests) {
        this.name = name;
        this.activeClasses = activeClasses;
        this.activeTests = activeTests;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getActiveClasses() {
        return activeClasses;
    }

    public void setActiveClasses(List<String> activeClasses) {
        this.activeClasses = activeClasses;
    }

    public List<String> getActiveTests() {
        return activeTests;
    }

    public void setActiveTests(List<String> activeTests) {
        this.activeTests = activeTests;
    }
}
