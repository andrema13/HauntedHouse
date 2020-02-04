package libs;

import interfaces.UnorderedListADT;

public class ArrayUnorderedList<T> extends ArrayList<T> implements UnorderedListADT<T> {

    public ArrayUnorderedList() {
        setList(getList());
    }

    @Override
    public void addToFront(T element) {

        if (element != null) {

            if (getLength() > size())
                expandCapacity();

            if (!isEmpty()) {
                T[] temp = (T[]) new Object[getDefaultCapacity()];
                for (int i = 0; i < getLength(); i++) {
                    temp[i] = getList()[i];
                }

                for (int k = 0; k < getLength(); k++) {
                    getList()[k + 1] = temp[k];
                }
            }
            getList()[0] = element;
            increaseLength();
            increaseModCount();
        } else {
            throw new UnsupportedOperationException();
        }
    }


    @Override
    public void addToRear(T element) {

        if (element != null) {

            if (getLength() > size())
                expandCapacity();

            if (isEmpty()) {
                getList()[0] = element;
                increaseLength();
                increaseModCount();
            } else {
                increaseLength();
                increaseModCount();
                getList()[getLength() - 1] = element;
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public void addAfter(T element, T target) {

        if (element != null) {

            if (getLength() > size())
                expandCapacity();

            if (isEmpty()) {
                System.out.println("Empty List");
            } else {
                boolean inserted = false;

                for (int i = 0; i < getLength(); i++) {
                    // verifica se existe o target no array
                    if (target.equals(getList()[i]) && !inserted) {
                        //manda os elementos a seguir ao target para um array temp
                        T[] temp = (T[]) new Object[getDefaultCapacity()];
                        int pos = 0;//Posicao onde vao ser guardados os dados no array temp
                        for (int j = i; j < getLength(); j++) {
                            temp[pos] = getList()[j + 1];
                            pos++;
                        }
                        //adiciona o elemento na posicao a seguir ao target
                        getList()[i + 1] = element;
                        //
                        int posTemp = 0;
                        for (int k = i + 1; k < getLength(); k++) {
                            getList()[k + 1] = temp[posTemp];
                            posTemp++;
                        }
                        //aumenta o tamanho
                        increaseLength();
                        increaseModCount();
                        //assegura que só insere uma vez, no caso de haver "target" com o mesmo conteudo no elemento
                        inserted = true;
                    }
                }
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }

}
