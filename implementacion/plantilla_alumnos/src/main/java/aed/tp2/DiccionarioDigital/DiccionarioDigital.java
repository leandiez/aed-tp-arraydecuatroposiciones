package aed.tp2.DiccionarioDigital;

public class DiccionarioDigital<T> {
    private static final int CANTIDAD_CARACTERES = 256;

    private static class Nodo<T> {
        private final Nodo<T>[] hijos;
        private T valor;
        private boolean esFinDeClave;

        @SuppressWarnings("unchecked")
        Nodo() {
            hijos = (Nodo<T>[]) new Nodo[CANTIDAD_CARACTERES];
            valor = null;
            esFinDeClave = false;
        }
    }

    private final Nodo<T> raiz;

    public DiccionarioDigital() {
        raiz = new Nodo<>();
    }

    public void insertar(String clave, T valor) {
        validarClave(clave);

        Nodo<T> actual = raiz;
        for (int i = 0; i < clave.length(); i++) {
            int indice = indiceDe(clave.charAt(i));
            if (actual.hijos[indice] == null) {
                actual.hijos[indice] = new Nodo<>();
            }
            actual = actual.hijos[indice];
        }

        actual.valor = valor;
        actual.esFinDeClave = true;
    }

    public T buscar(String clave) {
        Nodo<T> nodo = nodoDe(clave);
        return nodo != null && nodo.esFinDeClave ? nodo.valor : null;
    }

    public boolean pertenece(String clave) {
        Nodo<T> nodo = nodoDe(clave);
        return nodo != null && nodo.esFinDeClave;
    }

    public void eliminar(String clave) {
        validarClave(clave);
        eliminar(raiz, clave, 0);
    }

    private boolean eliminar(Nodo<T> nodo, String clave, int posicion) {
        if (posicion == clave.length()) {
            if (!nodo.esFinDeClave) {
                return false;
            }
            nodo.esFinDeClave = false;
            nodo.valor = null;
            return noTieneHijos(nodo);
        }

        int indice = indiceDe(clave.charAt(posicion));
        Nodo<T> hijo = nodo.hijos[indice];
        if (hijo == null) {
            return false;
        }

        boolean eliminarHijo = eliminar(hijo, clave, posicion + 1);
        if (eliminarHijo) {
            nodo.hijos[indice] = null;
        }

        return !nodo.esFinDeClave && noTieneHijos(nodo);
    }

    private Nodo<T> nodoDe(String clave) {
        validarClave(clave);

        Nodo<T> actual = raiz;
        for (int i = 0; i < clave.length(); i++) {
            int indice = indiceDe(clave.charAt(i));
            if (actual.hijos[indice] == null) {
                return null;
            }
            actual = actual.hijos[indice];
        }
        return actual;
    }

    private boolean noTieneHijos(Nodo<T> nodo) {
        for (Nodo<T> hijo : nodo.hijos) {
            if (hijo != null) {
                return false;
            }
        }
        return true;
    }

    private int indiceDe(char caracter) {
        if (caracter >= CANTIDAD_CARACTERES) {
            throw new IllegalArgumentException("La clave contiene un caracter no soportado");
        }
        return caracter;
    }

    private void validarClave(String clave) {
        if (clave == null || clave.isEmpty()) {
            throw new IllegalArgumentException("La clave no puede ser vacia");
        }
    }
}
