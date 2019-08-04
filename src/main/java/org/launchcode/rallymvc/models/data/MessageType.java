package org.launchcode.rallymvc.models.data;

public enum MessageType {
    MOTIVATE ("Motivate"),
    BERATE ("Berate"),
    CONGRATULATE ("Congratulate");

    private final String name;

    MessageType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}