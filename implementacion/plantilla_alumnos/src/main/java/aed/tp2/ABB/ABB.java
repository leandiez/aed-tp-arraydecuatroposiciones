
import java.util.ArrayList;
import java.util.Queue;

// Todos los tipos de datos "Comparables" tienen el método compareTo()
// elem1.compareTo(elem2) devuelve un entero. Si es mayor a 0, entonces elem1 > elem2
public class ABB<T extends Comparable<T>> {
    private Nodo raiz;
    private int size;
    private int altura;

    private class Nodo {
        private T valor;
        private Nodo padre;
        private Nodo izq;
        private Nodo der;
        
        public Nodo(T elem){
            this.valor = elem;
            this.padre = null;
            this.izq = null;
            this.der = null;
        }
    }

    public ABB() {
        this.raiz = null;
        this.size = 0;
        this.altura = 0;
    }

    public int cardinal() {
        return size;
    }

    public T minimo(){
        Nodo referencia = raiz;
        T minimo = null;
        while (referencia != null){
            minimo = referencia.valor;
            referencia = referencia.izq;
        }
        return minimo;
    }

    public T maximo(){
        Nodo referencia = raiz;
        T maximo = null;
        while (referencia != null){
            maximo = referencia.valor;
            referencia = referencia.der;
        }
        return maximo;
    }

    public void insertar(T elem){
        // Empiezo desde la raiz
        // Caso 1, si la raiz es null entonces inserto el elemento
        Nodo nodo = new Nodo(elem);
        Nodo temporal = raiz;
        Nodo padre = raiz;
        if (temporal == null){
            raiz = nodo;
            size += 1;
            altura += 1;
            return;
        }
        // Caso 2, Si tengo elementos en el arbol entonces voy comparando
        // Si es mayor a cero entonces el elemento tiene que estar a la derecha del arbol
        // Si es menor a cero entonces esta a la izquierda del arbol
        while (temporal != null){
            // En algun momento al nodo cuando lo coloque le voy a tener que poner quien es el padre asi que lo asigno aca
            padre = temporal;
            // Rama derecha sería esto
            if (elem.compareTo(temporal.valor) > 0){
                temporal = temporal.der;
            // El else seria la rama izquierda
            } else if (elem.compareTo(temporal.valor) < 0) {
                temporal = temporal.izq;
            } else{
                // Si los elementos son iguales no deberia insertarlo segun los tests
                return;
            }
        }
        // Despues de hacer el recorrido tengo que meter el nodo donde corresponde.
        nodo.padre = padre;
        if (elem.compareTo(padre.valor) > 0){
            padre.der = nodo;
        } else {
            padre.izq = nodo;
        }
        size += 1;
        // Aca aumentar la altura al azar podria ser medio mentiroso, en realidad tendria que calcular la altura con algun algortimo de busqueda por profundidad o algo asi?
        
    }

    public boolean pertenece(T elem){
        Nodo inicio = raiz;
        if (inicio == null){
            return false;
        }
        while (inicio != null){
            if (elem.compareTo(inicio.valor) == 0){
                return true;
            } else if (elem.compareTo(inicio.valor) > 0){
                inicio = inicio.der;
            } else {
                inicio = inicio.izq;
            }
        }
        return false;
    }

    // Me quedo espantosa esta solucion pero parece funcionar :D
    public void eliminar(T elem){
        // Preparo mi nodo para recorrer
        Nodo inicio = raiz;
        Nodo padre = null;
        while (inicio != null){
            // Si es mayor que 0 entonces sigo buscando en la rama derecha
            if (elem.compareTo(inicio.valor) > 0){
                inicio = inicio.der;
            } else if (elem.compareTo(inicio.valor) < 0){
                inicio = inicio.izq;
            } else {
                // Aca significa que lo encontre, pero tengo que tener cuidado
                // Caso 1) Tengo que eliminar una hoja
                if (inicio.izq == null && inicio.der == null){
                    // Aca creo que se subdivide en otro caso porque podria ser hijo de izq o derecha
                    // Si es hijo izquierdo
                    if (inicio.padre == null){
                        raiz = null;
                    } else {
                        if (inicio == inicio.padre.izq){
                            inicio.padre.izq = null;
                        } else if (inicio == inicio.padre.der){
                            inicio.padre.der = null;
                        }
                    }
                    size -= 1;
                    return;
                }
                // Caso 2) Tengo que eliminar una rama con un hijo (esto sería un o exclusivo)
                else if (inicio.izq == null || inicio.der == null){
                    // Si el hijo de la izquierda es distino de null, ese es el que le tengo que asignar al padre
                    padre = inicio.padre;
                    // Si padre es null es porque es la raiz
                    if (padre == null){
                        // Separo en 4 casos
                        // Raiz con hijo izquierdo
                        Nodo hijo;
                        if (inicio.izq != null) {
                            hijo = inicio.izq;
                        } else {
                            hijo = inicio.der;
                        }
                        raiz = hijo;
                        if (hijo != null) {
                            hijo.padre = null;
                        }
                    } else {
                        Nodo hijo;
                        if (inicio.izq != null) {
                            hijo = inicio.izq;
                        } else {
                            hijo = inicio.der;
                        }
                        if (padre.izq == inicio) {
                            padre.izq = hijo;
                        } else {
                            padre.der = hijo;
                        }
                        if (hijo != null) {
                            hijo.padre = padre;
                        }
                    }
                    size -= 1;
                    return;
                }
                // Caso 3) Tengo que eliminar una rama con 2 o mas hijos 
                // Este seria el caso mas dificil porque tengo que buscar el predecesor o el sucesor
                else if (inicio.izq != null && inicio.der != null){
                    // Para que tenga sucesor tiene que haber un subarbol derecho
                    padre = inicio.padre;
                    // Si el padre es null es porque estoy en la raiz
                    if (padre == null){
                        Nodo sucesor = this.Sucesor(inicio);
                        inicio.valor = sucesor.valor;
                        // Si sucesor.der es distinto de null es porque tengo un subarbol derecho
                        if (sucesor.padre.izq == sucesor) {
                            sucesor.padre.izq = sucesor.der;
                        } else {
                            sucesor.padre.der = sucesor.der;
                        }
                        if (sucesor.der != null) {
                            sucesor.der.padre = sucesor.padre;
                        }
                        size -= 1;
                    // Si no es una raiz 
                    } else {
                        if (inicio.der != null){
                            Nodo sucesor = this.Sucesor(inicio);
                            inicio.valor = sucesor.valor;
                            // ahora tengo que eliminar el sucesor de "abajo", que solo puede tener una hoja o un sub arbol derecho, no puede tener uno izquierdo porque si no no seria sucesor
                            // Mismo caso que el caso 2
                            if (sucesor.padre.izq == sucesor) {
                                sucesor.padre.izq = sucesor.der;
                            } else {
                                sucesor.padre.der = sucesor.der;
                            }
                            if (sucesor.der != null) {
                                sucesor.der.padre = sucesor.padre;
                            }
                        } else if (inicio.izq != null){
                            Nodo predecesor = this.Predecesor(inicio);
                            inicio.valor = predecesor.valor;
                        }
                        size -= 1;
                    }
                    return;
                } 
            }
        }
    }

    public Nodo Sucesor(Nodo nodo){
        Nodo res = null;
        // Me muevo uno a la derecha y despues todo hasta la izquierda para encontrar el minimo
        // Si tiene sub arbol derecho, me muevo uno hacia la derecha y despues todo hacia la izquierda
        if (nodo.der != null){
            nodo = nodo.der;
            while (nodo != null){
                res = nodo;
                nodo = nodo.izq;
            }
            // Si no tiene un subarbol, entonces el sucesor es el primer ancestro cuyo hijo sea izquierdo
        } else {
            // mientras sea el hijo derecho voy subiendo
            while(nodo.padre != null && nodo.padre.der == nodo){
                nodo = nodo.padre; // Voy subiendo en el arbol
            }
            res = nodo.padre;

        }
        return res;
    }

    public Nodo Predecesor(Nodo nodo){
        Nodo res = null;
        // Me muevo uno hacia la izquierda y todo lo que pueda hacia la derecha
        nodo = nodo.izq;
        while (nodo != null){
            res = nodo;
            nodo = nodo.der;
        }
        return res;
    }

    public String toString(){
        ABB_Iterador iterador = iterador();
        String res = "{";
        T max = maximo();
        
        while(iterador.haySiguiente()){
            T valor = iterador.siguiente();
            res += valor; 
            if (valor != max){
                res += ",";
            }
        }
        res += "}";
        return res;
    }

    public class ABB_Iterador {
        private Nodo actual;

        public boolean haySiguiente() {
            return actual != null;            
        }
    
        public T siguiente() {
            T valor = actual.valor;
            actual = Sucesor(actual);
            return valor;
        }

        public void BuscarNodoMinimo(){
            Nodo minimo = null;
            while (actual != null){
                minimo = actual;
                actual = actual.izq;
            }
            actual = minimo;
        }

        public ABB_Iterador(){
            this.actual = raiz;
            BuscarNodoMinimo();

        }
    }

    public ABB_Iterador iterador() {
        return new ABB_Iterador();
    }

}
