package Heatmap;

public class ObjectivePoint extends Point implements ChoiceBoxToStringInterface {

    private String name;

    public ObjectivePoint(Map map, Double x, Double y, String name) {
        super(map, x, y);
        if (name == null) {
            throw new IllegalArgumentException("Objective name cannot be null");
        }
        if (!Contains.onlyOneorMoreAlphaNumericSpaceAndNoWhitespaceAtStartAndEnd(name)) {
            throw new IllegalArgumentException("Name must be only one or more alphanumeric characters");
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (getClass() != obj.getClass())
            return false;
        ObjectivePoint other = (ObjectivePoint) obj;

        // Custom code
        if (getX() == null) {
            if (other.getX() != null)
                return false;
        } else if (!getX().equals(other.getX()))
            return false;
        if (getY() == null) {
            if (other.getY() != null)
                return false;
        } else if (!getY().equals(other.getY()))
            return false;

        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ObjectivePoint [name=" + name + "]";
    }

}
