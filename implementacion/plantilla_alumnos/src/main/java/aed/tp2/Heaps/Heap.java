package aed.tp2.Heaps;
import java.util.ArrayList;
import java.util.Comparator;
import aed.tp2.Handle;
import aed.tp2.Usuario;

public class Heap<T> {
    // No especificaron nada acerca de si se puede usar esta estructura o no. No tenemos tiempo para implementar tantas cosas asi que opto por usar esto.
    private ArrayList<HandleHeap> data;
    private Comparator<T> comparator; // La clase del heap utiliza el comparator del tipo que le corresponda, ejemplo PorMillasTotales, PorMillasDisponibles, etc 
 
    /*
    No tenemos muy seguro la complejidad de hacer la extension del arrayList ¿se puede no considerar?
    */
    public Heap(Comparator<T> comparator){
        // Creo el array vacio
        data = new ArrayList<HandleHeap>();
        this.comparator = comparator; 
    }

    // Tampoco estoy del todo seguro si esto deberia ir aca. Entiendo que si igual
    public class HandleHeap implements Handle<T>{
        public T nodoReferencia; // Aca estoy guardando la referencia a un objeto cualquiera de tipo T, en particular un usuario
        public int indice; // Este sería el indice donde estaria guardado mi Usuario en el Heap¿?

        public HandleHeap(T usuario, int indice){
            this.nodoReferencia = usuario;
            this.indice = indice;
        }
        public T valor(){
            return this.nodoReferencia;
        };
        public void eliminar(){
            this.nodoReferencia = null;
        };
        public void modificar(T v){};
        public boolean equals(Handle<T> h){
            return true;
        }; // Ni la mas pálida idea de como implementar el equals aca

    }
    // Algunos metodos comunes
    // Si tengo que implementar un sift up lo que hago es lo pongo al final y voy subiendo
    public void siftUp(){
        // Recordar:
        // HijoIzq = 2*i+1
        // HijoDer = 2*i+2
        // Padre = [(i-1)/2], es la funcion piso

        // [24,34,45,29,35,43,60,15] -> i = 8, indicePadre = 3
        int i = data.size() -1 ; // Empiezo desde el final de array y hago subir al elemento
        while (i > 0){
            int padreIndice = (i-1)/2;
            T padre = data.get(padreIndice).valor();
            T hijo = data.get(i).valor();

            // El compare devuelve negativo si o1 < o2 
            // El compare devuelve 0 si o1 = o2
            // El compare devuelve positivo si o1 > o2  
            if (comparator.compare(hijo,padre) > 0){
                // Si entro aca es porque hay que hacer un swap
                swap(i, padreIndice);
                i = padreIndice;
            } else {
                return;
            }
        }
    }

    public void siftUp(int indice){
        /* Misma idea que el siftUp pero solo que este es para un indice en especifico.*/
        int i = indice ; // Empiezo desde el final de array y hago subir al elemento
        while (i > 0){
            int padreIndice = (i-1)/2;
            T padre = data.get(padreIndice).valor();
            T hijo = data.get(i).valor();

            if (comparator.compare(hijo,padre) > 0){
                // Si entro aca es porque hay que hacer un swap
                swap(i, padreIndice);
                i = padreIndice;
            } else {
                return;
            }
        }
    }

    private void swap(int i, int j) {
        HandleHeap temp = data.get(i);
        HandleHeap otro = data.get(j);
        data.set(i, otro);
        data.set(j, temp);
        // Tengo que tambien permutar los indices de los handles
        temp.indice = j;
        otro.indice = i;
    }

    public void siftDown(){
        int i = 0;
        int longitud = data.size(); // no se si es size o size - 1 
        while (i < longitud){
            int hijoIzqIndice = 2*i+1;
            int hijoDerIndice = 2*i+2;
            int hijoMayorIndice = i;
 
            // Esto debería funciona porque se supone que el invariante del heap es "izquierdista", entonces deberia estar lleno del lado izquierdo
            // Esto se va a pisar si o si con el mayor
            // Hay que ver bien el signo, si es > 0 o < 0
            if(hijoIzqIndice < longitud && comparator.compare(data.get(hijoIzqIndice).valor(), data.get(hijoMayorIndice).valor()) > 0){
                hijoMayorIndice = hijoIzqIndice;
            } 
            
            if(hijoDerIndice < longitud && comparator.compare(data.get(hijoDerIndice).valor(), data.get(hijoMayorIndice).valor()) > 0){
                hijoMayorIndice = hijoDerIndice;
            } 
            
            if(hijoMayorIndice == i){
                return;
            } else {
                swap(i, hijoMayorIndice);
                i = hijoMayorIndice;
            }
        }
    };

    public void siftDown(int indice){
        /* Misma idea que el siftdown pero solo que este es para un indice en especifico.*/
        int i = indice;
        int longitud = data.size(); // no se si es size o size - 1 
        while (i < longitud){
            int hijoIzqIndice = 2*i+1;
            int hijoDerIndice = 2*i+2;
            int hijoMayorIndice = i;
 
            // Esto debería funciona porque se supone que el invariante del heap es "izquierdista", entonces deberia estar lleno del lado izquierdo
            // Esto se va a pisar si o si con el mayor
            // Hay que ver bien el signo, si es > 0 o < 0
            if(hijoIzqIndice < longitud && comparator.compare(data.get(hijoIzqIndice).valor(), data.get(hijoMayorIndice).valor()) > 0){
                hijoMayorIndice = hijoIzqIndice;
            } 
            
            if(hijoDerIndice < longitud && comparator.compare(data.get(hijoDerIndice).valor(), data.get(hijoMayorIndice).valor()) > 0){
                hijoMayorIndice = hijoDerIndice;
            } 
            
            if(hijoMayorIndice == i){
                return;
            } else {
                swap(i, hijoMayorIndice);
                i = hijoMayorIndice;
            }

        }
    };

    public T get(){
        // Este metodo tambien tiene que sacar efectivamente el elemento del heap.
        // Retorna la raiz y hace sift-down para no romper el invariante
        if (data.size() > 0){
            HandleHeap valor = data.get(0); // Obtengo la raiz
            HandleHeap ultimo = data.remove(data.size() - 1); // Borro el ultimo elemento, creo que la complejidad de esto es O(1)
            if (!data.isEmpty()) {                                // si quedó algo, reubica el último
                ultimo.indice = 0; // Me tengo que acordar de cambiarle el indice al heap
                data.set(0, ultimo); // Pongo el ultimo al inicio
                siftDown(0); // heapify  
            }
            return valor.valor();
        } else {
            return null;
        }    
    }

    public T getSinQuitar(){
        if (data.size() > 0){
            HandleHeap valor = data.get(0); // Obtengo la raiz
            return valor.valor();
        } else {
            return null;
        }  
    }

    public int buscarPorUsuario(T usuario){
        for (HandleHeap handleHeap : data) {
            if (handleHeap.valor().equals(usuario)){
                return handleHeap.indice;
            }
        }
        return -1; // No deberia llegar nunca aca
    }

    public void agregar(T elem){
        // Recordar que lo que tengo que almacenar es un handle! asi que lo tengo que crear aca
        HandleHeap h = new HandleHeap(elem,data.size()); // Hay que ver si el tema del indice molesta o no. Al agregarlo lo agrego siempre al final
        data.add(h);
        siftUp();
    }
}
