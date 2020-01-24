import interfaces.OrderedListADT;

public class ArrayOrderedList<T extends Comparable<T>> extends ArrayList<T> implements OrderedListADT<T> {

    ArrayOrderedList() {
        setList(getList());
    }

    @Override
    public void add(T element) {

        if (element != null) {

            if (getLength() > size())
                expandCapacity();

            if (isEmpty()) {
                getList()[0] = element;
                setLength(getLength() + 1);
                setModCount(getModCount() + 1);
            } else {
                // Insere na ultima posição disponivel quando é superior ao ultimo elemento do array
                if (element.compareTo(getList()[getLength() - 1]) > 0) {
                    getList()[getLength()] = element;
                    setLength(getLength() + 1);
                    setModCount(getModCount() + 1);
                }
                //se o elemento for inferior compara-se com a lista e insere-se na posicao adequada
                else {
                    boolean inserted = false;
                    for (int i = 0; i < getLength(); i++) {
                        if (element.compareTo(getList()[i]) < 0 && !inserted) {
                            //array temporario para ficar com os elementos ordenados da direita do elemento a adicionar
                            T[] temp = (T[]) new Comparable[getDefaultCapacity()];
                            for (int j = i; j < getLength(); j++) {
                                temp[j] = getList()[j];
                            }
                            //adiciona o elemento na posiçao correta
                            getList()[i] = element;
                            //copia para o array original os elementos que estavam no array temp
                            for(int k = i; k < getLength(); k++){
                                getList()[k+1] = temp[k];
                            }
                            //aumenta o tamanho
                            setLength(getLength() + 1);
                            setModCount(getModCount() + 1);
                            inserted = true;
                        }
                    }
                }
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }

}
