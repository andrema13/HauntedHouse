public class Connection implements Comparable<Connection> {

    private int id;
    private int ghostTakenPoints;

    Connection(int ghostTakenPoints){
        this.id = 0;
        this.ghostTakenPoints = ghostTakenPoints;
    }

    //region get-set
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGhostTakenPoints() {
        return ghostTakenPoints;
    }

    public void setGhostTakenPoints(int ghostTakenPoints) {
        this.ghostTakenPoints = ghostTakenPoints;
    }
    //endregion


    @Override
    public String toString() {
        return "Connection ID:  " + id + ", ghostTakenPoints=" + ghostTakenPoints;
    }

    @Override
    public int compareTo(Connection o) {
        //TODO
        return 0;
    }
}
