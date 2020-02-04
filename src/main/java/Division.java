public class Division implements Comparable<Division> {

    private int id;
    private String name;
    private boolean visited;

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

    boolean isVisited() {
        return this.visited;
    }

    void visit() {
        this.visited = true;
    }

    void unVisit() {
        this.visited = false;
    }

    @Override
    public String toString() {
        return String.format("%-15s %-20s", "     ID = " + this.id, "Name = " + this.name);
    }

    @Override
    public int compareTo(Division o) {
        return this.id - o.id;
    }
}
