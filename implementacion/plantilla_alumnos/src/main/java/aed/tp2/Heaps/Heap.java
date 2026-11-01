package aed.tp2.Heaps;
import java.util.ArrayList;
import java.util.Comparator;
import aed.tp2.Handle;

public class Heap<T> {
    // No especificaron nada acerca de si se puede usar esta estructura o no. No tenemos tiempo para implementar tantas cosas asi que opto por usar esto.
    private ArrayList<T> data;
    private Comparator<T> comparator; // La clase del heap utiliza el comparator del tipo que le corresponda, ejemplo PorMillasTotales, PorMillasDisponibles, etc 
 
    /*
    No tenemos muy seguro la complejidad de hacer la extension del arrayList ¿se puede no considerar?
    */
    public Heap(Comparator<T> comparator){
        // Creo el array vacio
        data = new ArrayList<T>();
        this.comparator = comparator; 
    }

    // Tampoco estoy del todo seguro si esto deberia ir aca. Entiendo que si igual
    public class HandleHeap implements Handle<T>{
        public T valor(){
            return null;
        };
        public void eliminar(){};
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
            T padre = data.get(padreIndice);
            T hijo = data.get(i);

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
    private void swap(int i, int j) {
        T temp = data.get(i);
        data.set(i, data.get(j));
        data.set(j, temp);
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
            if(hijoIzqIndice < longitud && comparator.compare(data.get(i), data.get(hijoIzqIndice)) > 0){
                hijoMayorIndice = hijoIzqIndice;
            } 
            
            if(hijoDerIndice < longitud && comparator.compare(data.get(i), data.get(hijoDerIndice)) > 0){
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
    public void Heapify(){
        // Esteban habia comentado que para heapifycar el arbol no habia que darle importancia a las hojas, que se podia comenzar desde el "anteúltimo nivel"
        // Idea: 
        // 1) Empiezo desde el anteultimo nivel
        // 2) Aplico sift down a todos los nodos de ese nivel
        // 3) Termino cuando llego al inicio.
        // No estoy del todo seguro si lo vamos a usar¿?
        int i = data.size() - 1; 
        int inicio = (i-1)/2; // empiezo por el padre

        
    }
    public T get(){
        // Retorna la raiz y hace sift-down para no romper el invariante
        if (data.size()-1 > 1){
            T valor = data.get(0); // Obtengo la raiz
            T ultimo = data.get(data.size()-1); // Tengo el ultimo valor y lo pongo en la raiz
            data.set(0, ultimo);
            siftDown(); // Dejo que el siftdown lo acomode solo y retorno el valor
            return valor;
        } else {
            return data.get(0);
        }
        
    };
}
