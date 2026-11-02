package aed.tp2;
import java.util.ArrayList;
/*  
La posta es que esto habría que consultarlo... Tengo que implementarle este Comparable porque es la clase que usa el Arbol del taller 6. 
Si no lo tenemos no se puede usar el Comparator pero creo que no es necesario, la comparacion entre usuarios para el arbol basta unicamente con el ID
Asi que creo que esto es suficiente.
*/
public class Usuario implements Comparable<Usuario>{
    // Los atributos son privados pero creo los getter para obtenerlos y usarlos en otra clase
    private int id;
    private int millasTotales;
    private int millasDisponibles;
    private int canjeosRealizados;
    private int idReferidor;
    private ArrayList<Usuario> transferidores; // Esta sería la lista de personas que transfirieron o le transfirio el usuario
    private Categoria categoria;
    // private ALGUNAESTRUCTURARANDOM transferidores; Aca en teoria deberíamos meter algo para ver como 

    public enum Categoria {
        Bronce, 
        Plata, 
        Oro, 
        Platino
    }

    // Este sería el primer usuario que creo, que no es referido
    public Usuario(int id){
        this.id = id;
        this.millasTotales = 0;
        this.millasDisponibles = 0;
        this.canjeosRealizados = 0;
        this.categoria = categoria.Bronce;
        this.transferidores = new ArrayList<Usuario>();
        this.idReferidor = 0; // Si es 0 significaria que nadie lo refirio, no se si es la mejor forma...
    }

    // Si el usuario es creado por referido se llama a este constructor.
    public Usuario(int id, int idReferido){
        this.id = id;
        this.millasTotales = 0;
        this.millasDisponibles = 0;
        this.canjeosRealizados = 0;
        this.categoria = categoria.Bronce;
        this.transferidores = new ArrayList<Usuario>();
        this.idReferidor = idReferido;
    }

    public int millasTotales(){
        return millasTotales;
    }

    public int millasDisponibles(){
        return millasDisponibles;
    }

    public int canjeosRealizados(){
        return canjeosRealizados;
    }

    public int id(){
        return id;
    }

    public void sumaMillasDisponibles(int puntos){
        millasDisponibles += puntos;
    }

    public Categoria darCategoria(){
        if (millasTotales < 10000){
            return categoria.Bronce;
        } else if (millasTotales >= 10000 && millasTotales < 49999){
            return categoria.Plata;
        } else if (millasTotales >= 50000 && millasTotales < 1000000){
            return categoria.Oro;
        } else {
            return categoria.Platino;
        }
    }

    public void crearUsuario(int id){
        Usuario usuario = new Usuario(id);
    }

    @Override 
    public int compareTo(Usuario otro){
        return Integer.compare(this.id, otro.id);
    }
}
