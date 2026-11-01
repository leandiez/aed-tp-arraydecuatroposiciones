package aed.tp2;

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
    private Categoria categoria;

    public enum Categoria {
        BRONCE, 
        PLATA, 
        ORO, 
        PLATINO
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

    public Categoria darCategoria(){
        if (millasTotales < 10000){
            return categoria.BRONCE;
        } else if (millasTotales >= 10000 && millasTotales < 49999){
            return categoria.PLATA;
        } else if (millasTotales >= 50000 && millasTotales < 1000000){
            return categoria.ORO;
        } else {
            return categoria.PLATINO;
        }
    }

    @Override 
    public int compareTo(Usuario otro){
        return Integer.compare(this.id, otro.id);
    }
}
