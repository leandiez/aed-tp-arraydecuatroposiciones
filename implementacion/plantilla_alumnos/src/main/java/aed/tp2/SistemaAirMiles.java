package aed.tp2;

public interface SistemaAirMiles {
    void registrarUsuario(int id);
    void registrarUsuarioConReferidor(int id, int ref);
    void acumularMillas(int id, double distancia, String nombrePromo);
    void canjearMillas(int id, double cantidad);
    void transferirMillas(int origen, int destino, double cantidad);
    
    double consultarSaldo(int id);
    String consultarCategoria(int id);
    int[] rankingMillas(int n);
    
    void iniciarPromocion(String nombre, double factor);
    void iniciarPromocionConTope(String nombre, double factor, double tope);
    void finalizarPromocion(String nombre);
    
    int elMasAcumulador();
    int elMasCanjeador();
    int[][] paresTransferidoresExclusivos();
}
