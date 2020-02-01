package libs;

public class MapConnection<T> implements Comparable<MapConnection<T>> {

    private LinearNode<T> destinyDivision;
    private int ghostTakenPoints;

    MapConnection(LinearNode<T> destinyDivision, int ghostTakenPoints) {
        this.destinyDivision = destinyDivision;
        this.ghostTakenPoints = ghostTakenPoints;
    }

    //region get-set

    public LinearNode<T> getDestinyDivision() {
        return destinyDivision;
    }

    public void setDestinyDivision(LinearNode<T> destinyDivision) {
        this.destinyDivision = destinyDivision;
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
        return "Connection Division:  " + this.destinyDivision + ", ghostTakenPoints=" + this.ghostTakenPoints;
    }

    @Override
    public int compareTo(MapConnection o) {
        int connectionGhostTakenPoints = this.ghostTakenPoints;
        int result = -1;

        if (o.getGhostTakenPoints() >= connectionGhostTakenPoints) {
            result = 1;
        }

        return result;
    }
}
