package aed.tp2;

public class Promocion {
    private String NombrePromo;
    private double FactorPromo;
    private boolean TieneTope;
    private double TopePromo;
    private double millasRestantes;
    
    public Promocion(String NombrePromo, double FactorPromo, boolean TieneTope, double TopePromo){
        this.NombrePromo = NombrePromo;
        this.FactorPromo = FactorPromo;
        this.TieneTope = TieneTope;
        this.TopePromo = TopePromo;
        this.millasRestantes = TopePromo;
    }

    public boolean tieneTope(){
        return TieneTope;
    }
    public double topPromo(){
        return TopePromo;
    }

    public String nombre(){
        return NombrePromo;
    }
   public double factor(){
        return FactorPromo;
    }
    public double millasRestantes(){
        return millasRestantes;
    }

    public boolean seConsumio(){
        return millasRestantes <= 0;
    }

    public void restarMillasPromo(double millas){
        this.millasRestantes -= millas;
    }
}
