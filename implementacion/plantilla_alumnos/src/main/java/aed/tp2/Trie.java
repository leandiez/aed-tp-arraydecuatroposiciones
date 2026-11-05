package aed.tp2;
import aed.tp2.Promocion;
public class Trie<T> {
    /* 
        No se si es una practica comun, pero suena viable despues de recorrer el arbol meter un objeto de tipo T generico.
        Recorres el arbol por la longitud de nombre de la promo y al llegar al final de la palabra devolves el objeto.

        Explicacion para el casteo de hijos: Java no comprende que se puede crear un nodo generico, entonces hay que hacerle entender que lo puede crear haciendo
        Nodo<T>[] y casteandolo con (). Es decir, le decis que estas creando una lista de nodos de tipo T.
    */
    private Nodo<T> raiz;

    private class Nodo<T>{
        // Lo que tiene el trie es un array de hijos (nodos que apuntan a donde este la letra)
        private Nodo[] hijos; 
        private boolean esFinDePalabra;
        private T valor; // Aca se guardaria el objeto despues de recorrer todo el trie

        public Nodo(){
            this.hijos = (Nodo<T>[]) new Nodo[52]; // Porque cuento minusculas y mayusculas del alfabeto. No me deja hacer esto creo this.hijos = new Nodo<T>[52]; Casteas la lista de Nodos de esta forma y funciona
            this.esFinDePalabra = false; 
        }
    }

    public Trie (){
        this.raiz = new Nodo<T>();
    }

    // Es de tipo T porque es generico, pero lo que estoy poniendo aca es una promocion
    public void insertar(String palabra, T promocion){
        // Me creo mi puntero en la raiz
        Nodo<T> actual = raiz;
        for (int i = 0; i < palabra.length(); i++) {
            char letra = palabra.charAt(i);
            if (actual.hijos[indice(letra)] == null){
                actual.hijos[indice(letra)] = new Nodo();
            }

            // Me meto en esa letra (bajo un nivel)
            actual = actual.hijos[indice(letra)];
        }

        actual.esFinDePalabra = true;
        actual.valor = promocion;
    }

    public boolean buscar(String palabra){
        Nodo<T> actual = raiz;
        for (int i = 0; i < palabra.length(); i++) {
            char letra = palabra.charAt(i);
            if (actual.hijos[indice(letra)] == null){
                return false;
            }

            // Me meto en esa letra (bajo un nivel)
            actual = actual.hijos[indice(letra)];
        }

        return actual.esFinDePalabra;
    }

    public T obtener(String palabra){
        Nodo<T> actual = raiz;
        for (int i = 0; i < palabra.length(); i++) {
            char letra = palabra.charAt(i);
            if (actual.hijos[indice(letra)] == null){
                return null;
            }

            // Me meto en esa letra (bajo un nivel)
            actual = actual.hijos[indice(letra)];
        }
        if (actual.esFinDePalabra){
            return actual.valor; // Devuelvo mi objeto.
        } else{
            return null;
        }
    }

    public void eliminar(String clave) {
        // Muy similar a lo que haciamos en haskell cuando necesitabamos mas parametros de los que podiamos tomar.
        // No creo que sea la forma correcta igual
        eliminarAux(this.raiz, clave, 0); // No retorno nada, solo ejecuto la limpieza
    }

    private boolean eliminarAux(Nodo<T> nodo, String clave, int posicion) {
        // Este es el caso base, si se llega al final de la palabra la palabra se borro correctamente
        if (posicion == clave.length()) {
            if (!nodo.esFinDePalabra) {
                return false; // La palabra no estaba
            }
            nodo.esFinDePalabra = false;
            nodo.valor = null;
            return true; // se borro bien
        }

        int indice = indice(clave.charAt(posicion));
        Nodo<T> hijo = nodo.hijos[indice];
        
        if (hijo == null) {
            return false; // La palabra no existe (no hay camino)
        }


        boolean seElimino = eliminarAux(hijo, clave, posicion + 1);

        // Si el hijo borró la palabra, chequeo si quedó inservible para desconectarlo
        if (seElimino && !hijo.esFinDePalabra && noTieneHijos(hijo)) {
            nodo.hijos[indice] = null;
        }

        // Le paso el resultado hacia arriba
        return seElimino; 
    }

    private boolean noTieneHijos(Nodo<T> nodo) {
        for (Nodo<T> hijo : nodo.hijos) {
            if (hijo != null) {
                return false;
            }
        }
        return true;
    }

    private int indice(char c){
        // Esto es muy similar creo a la funcion nativa de python "ord"
        if (c >= 'a' && c <= 'z') {
            return c - 'a';           // 0..25
        } else if (c >= 'A' && c <= 'Z') {
            return 26 + (c - 'A');    // 26..51
        } else{
            // Se supone que nunca llegaria aca
            return 0;
        }
    }

}
