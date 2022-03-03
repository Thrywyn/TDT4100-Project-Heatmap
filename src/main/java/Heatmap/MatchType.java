package Heatmap;

public class MatchType implements ChoiceBoxToStringInterface {

    private String name;

    public MatchType(String type) {
        this.name = type.toUpperCase();
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String type) {
        this.name = type;
    }

    @Override
    public String getChoiceBoxString() {
        return name;
    }

}
