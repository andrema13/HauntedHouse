public class Player implements Comparable<Player> {

    private String name;
    private int points;

    Player(String playerName, int playerPoints) {
        this.points = playerPoints;
        this.name = playerName;
    }
    //region get-set

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
    //endregion

    @Override
    public String toString() {
        return  "Name = " + this.name + "\t\t" +
                "Points = " + this.points + '\n';
    }

    @Override
    public int compareTo(Player o) {
        
        int bestPlayer = this.points;
        int result = 0;

        if (o.getPoints() >= bestPlayer) {
            result = 1;
        } else if (o.getPoints() < bestPlayer) {
            result = -1;
        }

        return result;
    }
}
