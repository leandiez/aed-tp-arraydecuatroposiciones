package aed.tp2.Heaps;
import java.util.Comparator; // Este comparador es de la interfaz de java
import aed.tp2.Usuario; // importo el usuario aca

public class PorMillasTotales implements Comparator<Usuario>{
    // Hago una comparación entre 2 usuarios para saber quien es el que mas millas acumuló
    /*  
    Entiendo que la clase Comparator debe estar pisando el metodo Compare (que es distinto de CompareTo)
    el metodo Compare se usa para hacer comparaciones personalizadas entre clases. No se como esta implementado
    ni me interesa. 
    */
    @Override 
    public int compare(Usuario U1, Usuario U2){
        // Me interesa ver cuanto vale la resta entre estos dos numeros
        // Creo que el tema del orden tambien importa, lo podemos ver despues, no se si es Integer o Double.
        return Integer.compare(U2.millasTotales(), U1.millasTotales());
    }
}
