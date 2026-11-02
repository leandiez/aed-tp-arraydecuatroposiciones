package aed.tp2;
import aed.tp2.ABB.ABB;

import aed.tp2.Usuario;
import aed.tp2.Heaps.Heap;
import aed.tp2.Heaps.PorMasCanjeos;
import aed.tp2.Heaps.PorMillasDisponibles;
import aed.tp2.Heaps.PorMillasTotales;
import java.util.ArrayList;

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
    ArrayList<int[][]> ParesTransferidores; //[[1,2],[6,3], etc]

    public AirMiles() {
        Usuarios = new ABB<Usuario>();
        // Se crea el sistema con la promocion NOPROMO --IMPLEMENTAR--
        elMasAcumulador = new Heap(new PorMillasTotales());
        elMasCanjeador = new Heap(new PorMasCanjeos());
        rankingMillas = new Heap(new PorMillasDisponibles());
    }

    public void registrarUsuario(int id) {
        /* 
            Analisis de complejidad:
            Insertar Usuario en arbol: Promedio O(ln U), peor caso O(U)
            Agregar al heap: O(1)

            Complejidad Total: Promedio O(ln U), peor caso O(U)
        */
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
        /* 
            Analisis de complejidad:
            Insertar Usuario en arbol: Promedio O(ln U), peor caso O(U)
            BuscarUsuario en Arbol: Promedio O(ln U), peor caso O(U)
            SumarMillas: O(1)
            Agregar a cualquier heap: O(1)

            Complejidad Total: Promedio O(ln U), peor caso O(U)
        */
        // El usuario referidor ya existe en el sistema.
        Usuario usuarioReferidor = new Usuario(ref); // Este usuario no lo estoy creando y agregando al arbol, solo lo creo para buscarlo en el arbol
        Usuario usuarioReferido = new Usuario(id, ref);

        // El que agrego al arbol es el referido, el referidor ya estaba en el sistema
        Usuarios.insertar(usuarioReferido);

        // Busco al usuario referidor
        Usuario usuarioDelArbol = Usuarios.buscarNodo(usuarioReferidor);

        // Se le asigna a cada uno 500 puntos
        usuarioReferido.sumaMillasDisponibles(500);
        usuarioReferido.sumaMillasTotales(500);
        usuarioDelArbol.sumaMillasDisponibles(500);
        usuarioDelArbol.sumaMillasTotales(500);

        // Agrego el nuevo usuario a todos los heaps. El heap automaticamente se encargara de acomodarlo donde corresponda.
        elMasAcumulador.agregar(usuarioReferido);
        elMasCanjeador.agregar(usuarioReferido);
        rankingMillas.agregar(usuarioReferido);
    }

    public void acumularMillas(int id, double distancia, String nombrePromo) {
        throw new UnsupportedOperationException("Sin implementar");
    }

    public void canjearMillas(int id, double cantidad) {
        /* 
            Analisis de complejidad:
            Crea usuario -> O(1)
            BuscarUsuario en arbol -> Promedio O(ln U), peor caso O(u)
            CanjeoMillas -> O(1)
            buscarPorUsuario en heap -> Peor caso O(U)
            siftUp -> O(ln U)

            Total: O(ln U) en caso promedio y O(U) en peor caso.

            Anotacion Javi:
            No le estamos sacando el jugo al Handle con el indice aca, no supe como corregirlo. No tiene sentido buscarPorUsuario, para eso deberia usarse el Handle y hacerse mas rapido, aun asi no rompe la complejidad pero no lo estamos aprovechando creo.
        */
        // Primero creo el usuario para buscarlo
        Usuario usuario = new Usuario(id);

        Usuario usuarioDelArbol = Usuarios.buscarNodo(usuario);
        usuarioDelArbol.canjeoMillas(cantidad);

        // Despues de canjear las millas del usuario tengo que actualizar el heap.
        int indiceHeap = elMasCanjeador.buscarPorUsuario(usuario); // No me gusta esta forma, la complejidad de buscarlo en el arbol es en el peor caso O(U)
        elMasCanjeador.siftUp(indiceHeap);
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
        // Primero devuelvo los usuarios
        Usuario[] usuarios = new Usuario[n];
        while(i <= n){
            Usuario usuario = rankingMillas.get();
            res[i] = usuario.id();
        }

        // Restauro los usuarios despues de haberlos sacado.
        for (Usuario usuario : usuarios) {
            rankingMillas.agregar(usuario);
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
