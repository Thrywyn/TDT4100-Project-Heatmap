package Heatmap;

public class Player implements ChoiceBoxToStringInterface {

    private String name;

    public Player(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        if (!Contains.onlyOneorMoreAlphaNumericSpace(name)) {
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
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Player other = (Player) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Player [name=" + name + "]";
    }

}
