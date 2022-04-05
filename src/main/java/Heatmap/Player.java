package Heatmap;

import java.util.ArrayList;

public class Player implements ChoiceBoxToStringInterface {

    private ArrayList<Point> points = new ArrayList<Point>();

    private String name;

    public Player(String name) {
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
