public class Player implements Comparable<Player> {

    /**
     * Name of this player
     */
    private String name;
    /**
     * Points of this player
     */
    private double points;

    /**
     * Initialize a player with a name and his points
     *
     * @param playerName   name
     * @param playerPoints points
     */
    Player(String playerName, int playerPoints) {
        this.points = playerPoints;
        this.name = playerName;
    }
    //region get-set

    public String getName() {
        return name;
    }

    public double getPoints() {
        return this.points;
    }

    public void setPoints(double points) {
        this.points = points;
    }
    //endregion

    @Override
    public String toString() {
        return "Name = " + this.name + "\t\t" +
                "Points = " + this.points + '\n';
    }

    @Override
    public int compareTo(Player o) {
        return (int) (o.getPoints() - points);
    }
}
