package Heatmap;

public class MatchType implements ChoiceBoxToStringInterface {

    private String name;

    public MatchType(String type) {
        if (type == null) {
            throw new IllegalArgumentException("MatchType cannot be null");
        }
        if (type.isEmpty()) {
            throw new IllegalArgumentException("MatchType cannot be empty");
        }
        this.name = type;
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
        MatchType other = (MatchType) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

}
