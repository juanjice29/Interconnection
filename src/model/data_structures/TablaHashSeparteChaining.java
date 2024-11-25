package model.data_structures;

import java.text.DecimalFormat;

public class TablaHashSeparateChaining<K extends Comparable<K>, V extends Comparable<V>> implements ITablaSimbolos<K, V> {

    private ILista<ILista<NodoTS<K, V>>> listaNodos;
    private int tamanoAct;
    private int tamanoTabla;
    private double minicial;
    private double cantidadRehash;

    public TablaHashSeparateChaining(int tamInicial) {
        tamanoTabla = nextPrime(tamInicial);
        minicial = tamanoTabla;
        listaNodos = inicializarTabla(tamanoTabla);
        tamanoAct = 0;
    }

    /* MÉTODOS PÚBLICOS PRINCIPALES */

    @Override
    public void put(K key, V value) {
        int posicion = hash(key);

        try {
            ILista<NodoTS<K, V>> listaSecundaria = listaNodos.getElement(posicion);
            if (listaSecundaria == null) {
                listaSecundaria = crearListaSecundaria(posicion);
            }

            if (!contains(key)) {
                listaSecundaria.insertElement(new NodoTS<>(key, value), listaSecundaria.size() + 1);
                tamanoAct++;
            }

            if (factorDeCarga() > 5) {
                rehash();
            }
        } catch (Exception e) {
            manejarExcepcion(e);
        }
    }

    @Override
    public V get(K key) {
        try {
            ILista<NodoTS<K, V>> listaSecundaria = listaNodos.getElement(hash(key));
            if (listaSecundaria != null) {
                return buscarValorEnLista(listaSecundaria, key);
            }
        } catch (Exception e) {
            manejarExcepcion(e);
        }
        return null;
    }

    @Override
    public V remove(K key) {
        try {
            ILista<NodoTS<K, V>> listaSecundaria = listaNodos.getElement(hash(key));
            if (listaSecundaria != null) {
                for (int i = 1; i <= listaSecundaria.size(); i++) {
                    NodoTS<K, V> nodo = listaSecundaria.getElement(i);
                    if (nodo.getKey().compareTo(key) == 0) {
                        V valor = nodo.getValue();
                        listaSecundaria.deleteElement(i);
                        tamanoAct--;
                        return valor;
                    }
                }
            }
        } catch (Exception e) {
            manejarExcepcion(e);
        }
        return null;
    }

    @Override
    public boolean contains(K key) {
        return get(key) != null;
    }

    @Override
    public boolean isEmpty() {
        return tamanoAct == 0;
    }

    @Override
    public int size() {
        return tamanoAct;
    }

    @Override
    public ILista<K> keySet() {
        return obtenerListaGeneral(NodoTS::getKey);
    }

    @Override
    public ILista<V> valueSet() {
        return obtenerListaGeneral(NodoTS::getValue);
    }

    @Override
    public ILista<NodoTS<K, V>> darListaNodos() {
        return obtenerListaNodos();
    }

    @Override
    public int hash(K key) {
        return Math.abs(key.hashCode() % tamanoTabla) + 1;
    }

    /* MÉTODOS AUXILIARES PRIVADOS */

    private ILista<ILista<NodoTS<K, V>>> inicializarTabla(int tamano) {
        ILista<ILista<NodoTS<K, V>>> tabla = new ArregloDinamico<>(tamano);
        for (int i = 1; i <= tamano; i++) {
            try {
                tabla.insertElement(null, i);
            } catch (Exception e) {
                manejarExcepcion(e);
            }
        }
        return tabla;
    }

    private ILista<NodoTS<K, V>> crearListaSecundaria(int posicion) throws Exception {
        listaNodos.changeInfo(posicion, new ArregloDinamico<>(1));
        return listaNodos.getElement(posicion);
    }

    private V buscarValorEnLista(ILista<NodoTS<K, V>> lista, K key) throws Exception {
        for (int i = 1; i <= lista.size(); i++) {
            NodoTS<K, V> nodo = lista.getElement(i);
            if (nodo.getKey().compareTo(key) == 0) {
                return nodo.getValue();
            }
        }
        return null;
    }

    private <T> ILista<T> obtenerListaGeneral(java.util.function.Function<NodoTS<K, V>, T> mapper) {
        ILista<T> lista = new ArregloDinamico<>(1);
        try {
            for (ILista<NodoTS<K, V>> subLista : listaNodos) {
                if (subLista != null) {
                    for (int i = 1; i <= subLista.size(); i++) {
                        lista.insertElement(mapper.apply(subLista.getElement(i)), lista.size() + 1);
                    }
                }
            }
        } catch (Exception e) {
            manejarExcepcion(e);
        }
        return lista;
    }

    private ILista<NodoTS<K, V>> obtenerListaNodos() {
        ILista<NodoTS<K, V>> nodos = new ArregloDinamico<>(1);
        try {
            for (ILista<NodoTS<K, V>> subLista : listaNodos) {
                if (subLista != null) {
                    for (int i = 1; i <= subLista.size(); i++) {
                        nodos.insertElement(subLista.getElement(i), nodos.size() + 1);
                    }
                }
            }
        } catch (Exception e) {
            manejarExcepcion(e);
        }
        return nodos;
    }

    private double factorDeCarga() {
        return (double) tamanoAct / tamanoTabla;
    }

    public void rehash() {
        try {
            ILista<NodoTS<K, V>> nodos = darListaNodos();
            tamanoTabla = nextPrime(tamanoTabla * 2);
            listaNodos = inicializarTabla(tamanoTabla);
            tamanoAct = 0;

            for (NodoTS<K, V> nodo : nodos) {
                put(nodo.getKey(), nodo.getValue());
            }

            cantidadRehash++;
        } catch (Exception e) {
            manejarExcepcion(e);
        }
    }

    private static boolean isPrime(int n) {
        if (n <= 1) return false;
        if (n <= 3) return true;
        if (n % 2 == 0 || n % 3 == 0) return false;
        for (int i = 5; i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0) return false;
        }
        return true;
    }

    private static int nextPrime(int n) {
        while (!isPrime(++n)) {}
        return n;
    }

    private void manejarExcepcion(Exception e) {
        e.printStackTrace();
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("###.##");
        return String.format("""
            La cantidad de duplas: %d
            El m inicial es: %.0f
            El m final es: %d
            El factor de carga es: %s
            La cantidad de rehash es: %.0f
            """, keySet().size(), minicial, tamanoTabla, df.format(factorDeCarga()), cantidadRehash);
    }
}

