public class Division {

    /**
     * Id of this map
     */
    private int id;
    /**
     * Name of this map
     */
    private String name;

    /**
     * Constructor that initializes this division
     *
     * @param id   to this division
     * @param name to this division
     */
    Division(int id, String name) {
        this.id = id;
        this.name = name;
    }

    //region get-set
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    //endregion

    @Override
    public String toString() {
        return String.format("%-15s %-20s", "     ID = " + this.id, "Name = " + this.name);
    }

}
