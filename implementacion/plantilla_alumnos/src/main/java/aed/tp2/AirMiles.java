package aed.tp2;
import java.util.ArrayList;

import aed.tp2.ABB.ABB;
import aed.tp2.Heaps.Heap;
import aed.tp2.Heaps.PorMasCanjeos;
import aed.tp2.Heaps.PorMillasDisponibles;
import aed.tp2.Heaps.PorMillasTotales;

public class AirMiles implements SistemaAirMiles {
    
    ABB<Usuario> Usuarios;
    Trie<Promocion> Promociones; // No implementado aun
    /* 
    Le pasamos al constructor del Heap la clase PorMasCanjeos que es la clase que implementa el Comparator (interfaz de java) que tiene
    un metodo override al compare para hacer la comparacion que necesitamos.
    */
    // ArrayList<Transaccion> Transacciones; No se si va aca o directamente todo dentro desde usuarios al momento de crearse.
    // En realidad por dentro el heap esta creando los Handles! Esto es importante de mencionar. Es similar al taller 5 lo que estoy haciendo aca.
    Heap<Usuario> elMasAcumulador; // Algo asi esta hecho en la filmina del campus 
    Heap<Usuario> elMasCanjeador;
    Heap<Usuario> rankingMillas;
    ArrayList<int[]> paresTransferidoresExclusivos; //[[1,2],[6,3], etc]

    public AirMiles() {
        Usuarios = new ABB<Usuario>();
        // Se crea el sistema con la promocion NOPROMO --IMPLEMENTAR--
        elMasAcumulador = new Heap<Usuario>(new PorMillasTotales());
        elMasCanjeador = new Heap<Usuario>(new PorMasCanjeos());
        rankingMillas = new Heap<Usuario>(new PorMillasDisponibles());
        paresTransferidoresExclusivos = new ArrayList<int[]>();
        Promociones =  new Trie<Promocion>();
        Promociones.insertar("NoPromo", new Promocion("NoPromo", 1.0f, false, -1.0));
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
        /* 
            Posible implementacion
        
            // busco la promo
            Promocion.buscarPromo(nombrePromo)
            
            // Busco al usuario
            Usuario usuario = new Usuario(id);
            Usuario usuarioDelArbol = Usuarios.buscarNodo(usuario);

            // Caso 1, la promocion alcanza para recorrer lo que tiene que viajar
            millasARecorrer = usuario.calcularMillasViaje(distancia, usuario.darFactorCategoria(), Promocion.darFactorPromo(nombrePromo));


        */
       /* 
       // logica y busco el usuario
        double cantMillas = distancia;
        boolean promocionTieneTope = true;
        Usuario usuario = new Usuario(id);
        Usuario usuarioDelArbol = Usuarios.buscarNodo(usuario);
        //calculo las millas
        // Caso 1: promocion sin tope
        if (promocionTieneTope == false){
            cantMillas = cantMillas*nombrePromo.darFactorPromocion*usuarioDelArbol.darCategoria()
            usuarioDestinoArbol.sumaMillasDisponibles(cantMillas)
            usuarioDestinoArbol.sumaMillas(cantMillas)
        }
        //caso 2:
        if (promocionTieneTope == false){
        
        }
        //actualizo el heap millas totales
        int indiceHeap = elMasAcumulador.buscarPorUsuario(usuario); 
        elMasAcumulador.siftUp(indiceHeap);
        //actualizo el heap millas disponibles
        int indiceHeap = rankingMillas.buscarPorUsuario(usuario);
        rankingMillas.siftUp(indiceHeap); 
        */

        // Primero obtengo la promocion
        Promocion promocion = Promociones.obtener(nombrePromo);
        if (promocion == null){
            promocion = Promociones.obtener("NoPromo");
        }
        // Calculo las millas que tiene que recorrer el usuario
        Usuario usuario = new Usuario(id);
        Usuario usuarioDelArbol = Usuarios.buscarNodo(usuario);
        double millasPromo = usuarioDelArbol.calcularMillasViaje(distancia,usuarioDelArbol.darFactorCategoria(),promocion.factor());
        // caso 1 si las millas calculadas que tiene que hacer el usuario son menores a las que tiene el tope de la promo
        if (!promocion.tieneTope() || (promocion.tieneTope() && millasPromo <= promocion.millasRestantes())){
            // Se le agregan las millas al usuario
            usuarioDelArbol.sumaMillasDisponibles(millasPromo);
            usuarioDelArbol.sumaMillasTotales(millasPromo);
        }
        // Caso 1.1 Si las millasPromo superan a las millas restantes solo guardo todo con factor 1.
        else if (promocion.tieneTope() && millasPromo > promocion.millasRestantes()){
            // Tal cual a como esta hecho en el tp1
            double millasNoPromo = usuarioDelArbol.calcularMillasViaje(distancia, usuarioDelArbol.darFactorCategoria(), 1.0);
            usuarioDelArbol.sumaMillasDisponibles(millasNoPromo);
            usuarioDelArbol.sumaMillasTotales(millasNoPromo);
        }
        // Tengo que viajar 2000 millas, la promo tiene 500 -> viajo 500 millas -> me restan 2000 (millas calculadas) - restantesDePromo (500) = 1500 total con NoPromo?
        // Actualizo las millas restantes de la promo y si se consumio la saco.
        if (promocion.tieneTope() && millasPromo <= promocion.millasRestantes()){
            promocion.restarMillasPromo(millasPromo);
            if (promocion.seConsumio()){
                // Si se consumio la promo la quito del Trie
                Promociones.eliminar(nombrePromo);
            }
        }

        //Tengo que actualizar los heaps de elMasAcumulador y rankingMillas
        int indiceHeap = elMasAcumulador.buscarPorUsuario(usuarioDelArbol); 
        elMasAcumulador.siftUp(indiceHeap);
        int indiceHeapRanking = rankingMillas.buscarPorUsuario(usuarioDelArbol);
        rankingMillas.siftUp(indiceHeapRanking); 
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
        usuarioDelArbol.restarMillas(cantidad);
        usuarioDelArbol.sumarMillasCanjeadas(cantidad);

        // Despues de canjear las millas del usuario tengo que actualizar el heap.
        int indiceHeap = elMasCanjeador.buscarPorUsuario(usuario); // No me gusta esta forma, la complejidad de buscarlo en el arbol es en el peor caso O(U)
        elMasCanjeador.siftUp(indiceHeap);
    }

    public void transferirMillas(int origen, int destino, double cantidad) {
        // Posible idea de implementacion
        /* 
            Analisis de complejidad
            BuscarUsuarios -> Promedio O(ln U), peor caso O(U)
            restarMillas/SumarMillas -> O(1)
            crearTransferencia -> O(1)

            Complejidad Total: Promedio O(ln U), peor caso O(U)
        */

        //Se crea a ambos usuarios.
        Usuario usuarioOrigen = new Usuario(origen);
        Usuario usuarioDestino = new Usuario(destino);

        // Se los busca en el arbol
        Usuario usuarioOrigenArbol = Usuarios.buscarNodo(usuarioOrigen);
        Usuario usuarioDestinoArbol = Usuarios.buscarNodo(usuarioDestino);

        // Se asume que tienen las millas disponibles. Se le resta unicamente de las millas disponibles
        usuarioOrigenArbol.restarMillas(cantidad); // se le resta la cantidad de millas al usuario
        usuarioDestinoArbol.sumaMillasDisponibles(cantidad); // Se le agregan las millas al usuario de destino y no se modifican las totales.

        // Se crea la relacion en algun lugar
        usuarioOrigenArbol.crearTransferencia(destino); // Solo le agrega la transferencia al usuario de origen 
        //hay que hacer un sift en el heap rankingMillas con el que recibio se le suman a las disponibles
        int indiceHeap = rankingMillas.buscarPorUsuario(usuarioDestinoArbol);
        rankingMillas.siftUp(indiceHeap); 
        // y tambien hay que actualizar el heap del mas canjeador (lo dice en aclaraciones)
        int indiceHeap0 = elMasCanjeador.buscarPorUsuario(usuarioOrigenArbol);
        elMasCanjeador.siftUp(indiceHeap0);
    }

    public double consultarSaldo(int id) {
        /* 
            Analisis de complejidad:
            BuscarUsuario en Nodo -> Promedio O(ln U), peor caso O(u)
            MillasDisponibles -> O(1)

            Complejidad Total: Promedio O(ln U), peor caso O(U)
        */
        // Creo el usuario y lo busco (Esto me suena muy raro igual) Esto habría que consultarlo...
        Usuario usuario = new Usuario(id);
        Usuario nodoUsuario = Usuarios.buscarNodo(usuario); 
        return nodoUsuario.millasDisponibles();

    }

    public String consultarCategoria(int id) {
        /* 
            Analisis de complejidad:
            BuscarUsuario en Nodo -> Promedio O(ln U), peor caso O(u)
            darCategoria -> O(1)

            Complejidad Total: Promedio O(ln U), peor caso O(U)
        */
        Usuario usuario = new Usuario(id);
        Usuario nodoUsuario = Usuarios.buscarNodo(usuario);
        return nodoUsuario.darCategoria().toString();
    }

    public int[] rankingMillas(int n) {
        /* 
            Analisis de complejidad:
            Get de usuarios es O(1)* N -> O(N)
            volver a agregar a los usuarios es O(1) * N, reordenarlos es O(ln U), entonces queda O(N lg U)
        
            Complejidad Total:  O(N ln U)
        */
        // En caso de que me pidan mas elementos de los que realmente tengo en el RankingMillas lo puedo salvar de que explote
        int cantidad = Math.min(n, rankingMillas.size());
        int i = 0;
        int[] res = new int[n];
        // Primero devuelvo los usuarios
        Usuario[] usuarios = new Usuario[cantidad];
        while(i < cantidad){
            Usuario usuario = rankingMillas.get();
            res[i] = usuario.id();
            // Agrego los usuarios a mi lista de usuarios
            usuarios[i] = usuario;
            i ++;
        }

        // Restauro los usuarios despues de haberlos sacado.
        for (Usuario usuario : usuarios) {
            rankingMillas.agregar(usuario);
        }

        return res;
    }

    public void iniciarPromocion(String nombre, double factor) {
        // Posible idea de implementacion (composicion)
        /* 
        Promocion.iniciarPromocion(nombre,factor) -> Le corresponde a la clase Promocion implementada con un Trie
        
        */
        Promociones.insertar(nombre, new Promocion(nombre, factor, false, -1));

    }

    public void iniciarPromocionConTope(String nombre, double factor, double tope) {
        // Posible idea de implementacion (composicion)
        /* 
        Llamaria al constructor pero es lo mismo, tendria dos clases constructoras y es comun
        Promocion.iniciarPromocionConTope(nombre,factor,tope) -> Le corresponde a la clase Promocion implementada con un Trie
        
        */
        Promociones.insertar(nombre, new Promocion(nombre, factor, true, tope));
    }

    public void finalizarPromocion(String nombre) {
        // Posible idea de implementacion (composicion)
        /* 
        Habria que ver si la borra o si modifica una flag. Tendriamos que ver como esta implementado en los tests/ lo que dice la especificacion que dicen cosas distintas.
        Promocion.finalizarPromocion(nombre) -> Le corresponde a la clase Promocion implementada con un Trie
        */

        // Si la promocion primero la obtengo y si se consumio entoncess la saco
        Promociones.eliminar(nombre);
    }

    public int elMasAcumulador() {
        /* 
            Análisis de complejidad
            GetUsuario -> O(1)

            ComplejidadTotal: O(1)
        */
        // Lo obtengo de mi heap
        Usuario usuario = elMasAcumulador.getSinQuitar();
        return usuario.id();
    }

    public int elMasCanjeador() {
        /* 
            Análisis de complejidad
            getSinQuitar -> O(1)

            ComplejidadTotal: O(1)
        */
        Usuario usuario = elMasCanjeador.getSinQuitar();
        return usuario.id();
    }

    public int[][] paresTransferidoresExclusivos() {
        throw new UnsupportedOperationException("Sin implementar");
    }
}
