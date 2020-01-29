import libs.DoubleLinkedList;
import libs.DoubleLinkedUnorderedList;

public class Division implements Comparable<Division> {

    private int id;
    private String name;
    private int ghostPoints;
    private DoubleLinkedUnorderedList<Division> connections;

    Division(int id, String name, int ghostPoints) {
        this.id = id;
        this.name = name;
        this.ghostPoints = ghostPoints;
        this.connections = new DoubleLinkedUnorderedList<>();
    }

    //region get-set

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGhostPoints() {
        return ghostPoints;
    }

    public void setGhostPoints(int ghostPoints) {
        this.ghostPoints = ghostPoints;
    }
    //endregion

    protected void changeGhostPoints(int ghostPoints, GameLevel gameLevel) {
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

    protected void addConnection(Division division){
        this.connections.addToRear(division);
    }

    protected void printConnections(){

        DoubleLinkedList<Division>.DoubleIterator iterator = this.connections.iterator();
        while (iterator.hasNext()){
            Division division = iterator.next();
            System.out.println(division.toString());
        }
    }

    @Override
    public String toString() {
        return "Id = " + this.id + "\t\t" + "Name = " + this.name + "\t\t" + "GhostPoints = " + this.ghostPoints + '\n';
    }

    @Override
    public int compareTo(Division o) {
        //TODO
        return 0;
    }
}
