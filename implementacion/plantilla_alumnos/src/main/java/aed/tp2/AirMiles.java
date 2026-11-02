package aed.tp2;
import aed.tp2.ABB.ABB;
import aed.tp2.Usuario;
import aed.tp2.Heaps.Heap;
import aed.tp2.Heaps.Heap.HandleHeap;
import aed.tp2.Heaps.PorMasCanjeos;
import aed.tp2.Heaps.PorMillasDisponibles;
import aed.tp2.Heaps.PorMillasTotales;

public class AirMiles implements SistemaAirMiles {
    
    ABB<Usuario> Usuarios;
    // Trie Promociones = new Trie<Promocion>(); // No implementado aun
    /* 
    Le pasamos al constructor del Heap la clase PorMasCanjeos que es la clase que implementa el Comparator (interfaz de java) que tiene
    un metodo override al compare para hacer la comparacion que necesitamos.
    */
    // ArrayList<Transaccion> Transacciones; No se si va aca o directamente todo dentro desde usuarios al momento de crearse.
    // En realidad por dentro el heap esta creando los Handles! Esto es importante de mencionar. Es similar al taller 5 lo que estoy haciendo aca.
    Heap<Usuario> elMasAcumulador; // Algo asi esta hecho en la filmina del campus 
    Heap<Usuario> elMasCanjeador;
    Heap<Usuario> rankingMillas;

    public AirMiles() {
        Usuarios = new ABB<Usuario>();
        // Se crea el sistema con la promocion NOPROMO --IMPLEMENTAR--
        elMasAcumulador = new Heap(new PorMillasTotales());
        elMasCanjeador = new Heap(new PorMasCanjeos());
        rankingMillas = new Heap(new PorMillasDisponibles());
    }

    public void registrarUsuario(int id) {
        // Creo el usuario y lo meto en el arbol
        Usuario usuario = new Usuario(id); 
        Usuarios.insertar(usuario);

        // Ahora todos esos usuarios tienen que pertenecer a todos los heaps (Complejidad es O(log U))
        // Lo que va en el heap es un handle.
        elMasAcumulador.agregar(usuario);
        elMasCanjeador.agregar(usuario);
        rankingMillas.agregar(usuario);
    }

    public void registrarUsuarioConReferidor(int id, int ref) {
        throw new UnsupportedOperationException("Sin implementar");
    }

    public void acumularMillas(int id, double distancia, String nombrePromo) {
        throw new UnsupportedOperationException("Sin implementar");
    }

    public void canjearMillas(int id, double cantidad) {
        throw new UnsupportedOperationException("Sin implementar");
    }

    public void transferirMillas(int origen, int destino, double cantidad) {
        throw new UnsupportedOperationException("Sin implementar");
    }

    public double consultarSaldo(int id) {
        // Creo el usuario y lo busco (Esto me suena muy raro igual) Esto habría que consultarlo...
        Usuario usuario = new Usuario(id);
        Usuario nodoUsuario = Usuarios.buscarNodo(usuario); 
        return nodoUsuario.millasDisponibles();

    }

    public String consultarCategoria(int id) {
        Usuario usuario = new Usuario(id);
        Usuario nodoUsuario = Usuarios.buscarNodo(usuario);
        return nodoUsuario.darCategoria().toString();
    }

    public int[] rankingMillas(int n) {
        int i = 0;
        int[] res = new int[n];
        while(i <= n){
            Usuario usuario = rankingMillas.get();
            res[i] = usuario.id();
        }
        return res;
    }

    public void iniciarPromocion(String nombre, double factor) {
        throw new UnsupportedOperationException("Sin implementar");
    }

    public void iniciarPromocionConTope(String nombre, double factor, double tope) {
        throw new UnsupportedOperationException("Sin implementar");
    }

    public void finalizarPromocion(String nombre) {
        throw new UnsupportedOperationException("Sin implementar");
    }

    public int elMasAcumulador() {
        // Lo obtengo de mi heap
        Usuario usuario = elMasAcumulador.get();
        return usuario.id();
    }

    public int elMasCanjeador() {
        throw new UnsupportedOperationException("Sin implementar");
    }

    public int[][] paresTransferidoresExclusivos() {
        throw new UnsupportedOperationException("Sin implementar");
    }
}
