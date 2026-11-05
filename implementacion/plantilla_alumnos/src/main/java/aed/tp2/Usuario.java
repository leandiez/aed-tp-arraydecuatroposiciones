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
    private double millasTotales;
    private double millasDisponibles;
    private double millasCanjeadas;
    private int idReferidor;
    private ArrayList<Integer> transferidores; // Esta sería la lista de personas que transfirieron o le transfirio el usuario
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
        this.millasCanjeadas = 0;
        this.categoria = categoria.Bronce;
        this.transferidores = new ArrayList<Integer>();
        this.idReferidor = 0; // Si es 0 significaria que nadie lo refirio, no se si es la mejor forma...
    }

    // Si el usuario es creado por referido se llama a este constructor.
    public Usuario(int id, int idReferido){
        this.id = id;
        this.millasTotales = 0;
        this.millasDisponibles = 0;
        this.millasCanjeadas = 0;
        this.categoria = categoria.Bronce;
        this.transferidores = new ArrayList<Integer>();
        this.idReferidor = idReferido;
    }

    public double millasTotales(){
        return millasTotales;
    }

    public double millasDisponibles(){
        return millasDisponibles;
    }

    public double millasCanjeadas(){
        return millasCanjeadas;
    }

    public int id(){
        return id;
    }

    public void sumaMillasDisponibles(double puntos){
        millasDisponibles += puntos;
    }

    public void sumaMillasTotales(double puntos){
        millasTotales += puntos;
    }

    public void restarMillas(double cantidad){
        millasDisponibles -= cantidad;
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

    public double darFactorCategoria(){
        if (millasTotales < 10000){
            return 1;
        } else if (millasTotales >= 10000 && millasTotales < 49999){
            return 1.5;
        } else if (millasTotales >= 50000 && millasTotales < 1000000){
            return 2.0;
        } else {
            return 0;
        }
    }
    public void crearUsuario(int id){
        Usuario usuario = new Usuario(id);
    }

    public boolean esExclusivo(){
        // Por la aclaracion en el tp, esta es valida como una unica transferencia para ambas partes asi que cuenta como par.
        return transferidores.size() == 1;
    }

    @Override 
    public int compareTo(Usuario otro){
        return Integer.compare(this.id, otro.id);
    }

    public boolean equals(Object otro){
        // Defino la igualdad de usuario solo por su id.
        boolean otroEsNull = (otro == null);
        if(otroEsNull){
            return false;
        }
        boolean claseDistinta = otro.getClass() != this.getClass();
        if (claseDistinta) {
            return false;
        }
        Usuario usuario = (Usuario) otro; // Casteo

        return this.id() == usuario.id();
    }

    public void crearTransferencia(int destino){
        transferidores.add(destino);
    }

    public double calcularMillasViaje(double distancia, double factorCategoria, double factorPromo){
        return distancia*factorCategoria*factorPromo;
    }
}
