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

    public boolean tieneTope(Promocion promo){
        return promo.TieneTope;
    }
    public float topPromo(){
        return TopePromo;
    }

    public String nombre(){
        return NombrePromo;
    }
   public float factor(){
        return FactorPromo;
    }
    public float millasRestantes(){
        return millasRestantes;
    }
}
