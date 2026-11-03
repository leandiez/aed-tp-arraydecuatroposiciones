package aed.tp2;

public class Trie {
    private Nodo raiz;

    private class Nodo{
        // Lo que tiene el trie es un array de hijos (nodos que apuntan a donde este la letra)
        Nodo[] hijos; 
        boolean esFinDePalabra;

        public Nodo(){
            this.hijos = new Nodo[52]; // Porque cuento minusculas y mayusculas del alfabeto
            this.esFinDePalabra = false; 
        }
    }

    public void insertar(String palabra){
        // Me creo mi puntero en la raiz
        Nodo actual = raiz;
        for (int i = 0; i < palabra.length(); i++) {
            char letra = palabra.charAt(i);
            if (raiz.hijos[indice(letra)] == null){
                raiz.hijos[indice(letra)] = new Nodo();
            }

            // Me meto en esa letra (bajo un nivel)
            actual = actual.hijos[indice(letra)];

        }

        actual.esFinDePalabra = true;
    }
    public boolean buscar(String palabra){
        Nodo actual = raiz;
        for (int i = 0; i < palabra.length(); i++) {
            char letra = palabra.charAt(i);
            if (raiz.hijos[indice(letra)] == null){
                return false;
            }

            // Me meto en esa letra (bajo un nivel)
            actual = actual.hijos[indice(letra)];
        }

        return actual.esFinDePalabra;
    }
    public void eliminar(String palabra){
        // Me gustaria no implementarlo o no usarlo :D
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
