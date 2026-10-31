package aed.tp2;

public class Usuario {
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
}
