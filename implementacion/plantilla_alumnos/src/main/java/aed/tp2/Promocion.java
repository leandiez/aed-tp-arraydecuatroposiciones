package aed.tp2;

public class Promocion {
    private String NombrePromo;
    private float FactorPromo;
    private boolean TieneTope;
    private float TopePromo;
    private float millasRestantes;
    
    public Promocion(String NombrePromo, float FactorPromo, boolean TieneTope, float TopePromo){
        this.NombrePromo = NombrePromo;
        this.FactorPromo = FactorPromo;
        this.TieneTope = TieneTope;
        this.TopePromo = TopePromo;
        this.millasRestantes = TopePromo;
    }

    
}
