package uk.sponte.automation.web.helpers;

/**
 * Created by n450777 on 07/04/15.
 */
public class TestDiInstance {
    private String name;

    public TestDiInstance(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("This instance is called '%s'", this.name);
    }
}
