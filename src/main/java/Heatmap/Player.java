package Heatmap;

import java.util.ArrayList;

public class Player implements ChoiceBoxToStringInterface {

    private String name;

    public Player(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        this.name = name;
    }

    @Override
    public String getChoiceBoxString() {
        return name;
    }

    public String getName() {
        return name;
    }

}
