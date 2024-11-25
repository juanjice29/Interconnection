package model.data_structures;

public class ListaEncadenada<T extends Comparable<T>> implements ILista<T> {

    private Nodo<T> first;
    private int size;
    private Nodo<T> last;

    public ListaEncadenada() {
        first = null;
        last = null;
        size = 0;
    }

    public ListaEncadenada(T element) {
        first = new Nodo<>(element);
        last = first;
        size = 1;
    }

    public void addFirst(T element) {
        Nodo<T> actual = new Nodo<>(element);
        actual.setNext(first);
        first = actual;
        if (size == 0) {
            last = actual;
        }
        size++;
    }

    public void addLast(T element) {
        Nodo<T> actual = new Nodo<>(element);
        if (last != null) {
            last.setNext(actual);
        }
        last = actual;
        if (size == 0) {
            first = actual;
        }
        size++;
    }

    public void addLastCola(T element) throws NullException {
        if (element == null) {
            throw new NullException("No es válido el elemento ingresado");
        }
        addLast(element);
    }

    public void insertElement(T elemento, int pos) throws PosException, NullException {
        if (pos < 1 || pos > size + 1) {
            throw new PosException("La posición no es válida");
        }
        if (elemento == null) {
            throw new NullException("No es válido el elemento ingresado");
        }

        if (pos == 1) {
            addFirst(elemento);
        } else if (pos == size + 1) {
            addLast(elemento);
        } else {
            Nodo<T> nuevo = new Nodo<>(elemento);
            Nodo<T> actual = getNodeAt(pos - 1);
            nuevo.setNext(actual.getNext());
            actual.setNext(nuevo);
            size++;
        }
    }

    public T removeFirst() throws VacioException {
        if (isEmpty()) {
            throw new VacioException("La lista está vacía");
        }
        T primero = first.getInfo();
        first = first.getNext();
        size--;
        if (size == 0) {
            last = null;
        }
        return primero;
    }

    public T removeLast() throws VacioException {
        if (isEmpty()) {
            throw new VacioException("La lista está vacía");
        }
        if (size == 1) {
            return removeFirst();
        }
        Nodo<T> penultimo = getNodeAt(size - 1);
        T ultimo = penultimo.getNext().getInfo();
        penultimo.setNext(null);
        last = penultimo;
        size--;
        return ultimo;
    }

    public T removeLastPila() throws VacioException {
        return removeLast();
    }

    public T deleteElement(int pos) throws PosException, VacioException {
        if (pos < 1 || pos > size) {
            throw new PosException("La posición no es válida");
        }
        if (isEmpty()) {
            throw new VacioException("La lista está vacía");
        }

        if (pos == 1) {
            return removeFirst();
        } else if (pos == size) {
            return removeLast();
        } else {
            return removeAtPosition(pos);
        }
    }

    private T removeAtPosition(int pos) {
        Nodo<T> previous = getNodeAt(pos - 1);
        T value = previous.getNext().getInfo();
        previous.setNext(previous.getNext().getNext());
        size--;
        return value;
    }

    public T firstElement() throws VacioException {
        if (isEmpty()) {
            throw new VacioException("La lista está vacía");
        }
        return first.getInfo();
    }

    public T lastElement() throws VacioException {
        if (isEmpty()) {
            throw new VacioException("La lista está vacía");
        }
        return last.getInfo();
    }

    public T getElement(int pos) throws PosException, VacioException {
        if (pos < 1 || pos > size) {
            throw new PosException("La posición no es válida");
        }
        if (isEmpty()) {
            throw new VacioException("La lista está vacía");
        }
        return getNodeAt(pos).getInfo();
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return first == null;
    }

    public int isPresent(T element) throws VacioException, NullException {
        if (element == null) {
            throw new NullException("No es válido el elemento ingresado");
        }
        if (isEmpty()) {
            throw new VacioException("La lista está vacía");
        }
        Nodo<T> actual = first;
        for (int i = 1; i <= size; i++) {
            if (actual.getInfo().equals(element)) {
                return i;
            }
            actual = actual.getNext();
        }
        return -1;
    }

    public void exchange(int pos1, int pos2) throws PosException, VacioException {
        if (pos1 < 1 || pos1 > size || pos2 < 1 || pos2 > size) {
            throw new PosException("La posición no es válida");
        }
        if (isEmpty()) {
            throw new VacioException("La lista está vacía");
        }
        if (pos1 != pos2) {
            T temp = getElement(pos1);
            try {
				changeInfo(pos1, getElement(pos2));
			} catch (PosException | VacioException | NullException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            try {
				changeInfo(pos2, temp);
			} catch (PosException | VacioException | NullException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }

    public void changeInfo(int pos, T element) throws PosException, VacioException, NullException {
        if (pos < 1 || pos > size) {
            throw new PosException("La posición no es válida");
        }
        if (isEmpty()) {
            throw new VacioException("La lista está vacía");
        }
        if (element == null) {
            throw new NullException("No es válido el elemento ingresado");
        }
        Nodo<T> actual = getNodeAt(pos);
        actual.change(element);
    }

    public ILista<T> sublista(int pos, int numElementos) throws PosException, VacioException {
        if (isEmpty()) {
            throw new VacioException("La lista está vacía");
        }
        if (numElementos < 0 || pos < 1 || pos > size) {
            throw new PosException("La cantidad de elementos no es válida");
        }
        if (numElementos >= size) {
            return this;
        }
        ILista<T> copia = new ListaEncadenada<>();
        Nodo<T> actual = getNodeAt(pos);
        for (int i = 0; i < numElementos; i++) {
            copia.addLast(actual.getInfo());
            actual = actual.getNext();
        }
        return copia;
    }

    private Nodo<T> getNodeAt(int pos) {
        Nodo<T> actual = first;
        for (int i = 1; i < pos; i++) {
            actual = actual.getNext();
        }
        return actual;
    }

    @Override
    public int compareTo(ILista<T> o) {
        return 0;
    }
}