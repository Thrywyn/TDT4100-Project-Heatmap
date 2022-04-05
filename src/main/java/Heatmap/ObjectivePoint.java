package Heatmap;

public class ObjectivePoint extends Point implements ChoiceBoxToStringInterface {

    String name;

    public ObjectivePoint(Map map, Double x, Double y, String name) {
        super(map, x, y);
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
