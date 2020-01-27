public class Division implements Comparable<Division> {

    private String name;
    private int ghostPoints;

    Division(String name, int ghostPoints) {
        this.name = name;
        this.ghostPoints = ghostPoints;
    }

    //region get-set
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGhostPoints() {
        return ghostPoints;
    }

    public void setGhostPoints(int ghostPoints, GameLevel gameLevel) {

        switch (gameLevel) {
            case BASIC:
                this.ghostPoints = ghostPoints;
                break;
            case NORMAL:
                this.ghostPoints = ghostPoints * 2;
                break;
            case HARD:
                this.ghostPoints = ghostPoints * 3;
                break;
            default:
                break;
        }
    }
    //endregion

    @Override
    public String toString() {
        return "Name = " + this.name + "\t\t" +
                "GhostPoints = " + this.ghostPoints + '\n';
    }

    @Override
    public int compareTo(Division o) {
        return 0;
    }
}
