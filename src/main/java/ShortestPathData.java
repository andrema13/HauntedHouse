public class ShortestPathData<T> implements Comparable<ShortestPathData<T>> {
    private T t;
    private double weight;

    ShortestPathData(T t, double weight){
        this.t = t;
        this.weight = weight;
    }

    //region get-set

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    //endregion

    @Override
    public int compareTo(ShortestPathData<T> o) {
        return (int) (this.weight - o.weight);
    }
}
