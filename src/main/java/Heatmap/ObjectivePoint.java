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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        ObjectivePoint other = (ObjectivePoint) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

}
