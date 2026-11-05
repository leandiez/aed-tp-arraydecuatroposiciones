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

    public AirMiles() {
        Usuarios = new ABB<Usuario>();
        // Se crea el sistema con la promocion NOPROMO --IMPLEMENTAR--
        elMasAcumulador = new Heap<Usuario>(new PorMillasTotales());
        elMasCanjeador = new Heap<Usuario>(new PorMasCanjeos());
        rankingMillas = new Heap<Usuario>(new PorMillasDisponibles());
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
        Usuario usuarioDelArbolReferidor = Usuarios.buscarNodo(usuarioReferidor);
        Usuario usaurioDelArbolReferido = Usuarios.buscarNodo(usuarioReferido);

        // Se le asigna a cada uno 500 puntos
        usaurioDelArbolReferido.sumaMillasDisponibles(500);
        usaurioDelArbolReferido.sumaMillasTotales(500);
        usuarioDelArbolReferidor.sumaMillasDisponibles(500);
        usuarioDelArbolReferidor.sumaMillasTotales(500);

        // Agrego el nuevo usuario a todos los heaps. El heap automaticamente se encargara de acomodarlo donde corresponda.
        elMasAcumulador.agregar(usuarioReferido);
        elMasCanjeador.agregar(usuarioReferido);
        rankingMillas.agregar(usuarioReferido);

        // Tengo que actualizar tambien los heaps porque ganaron 500 millas
        int indiceHeapRanking1 = rankingMillas.buscarPorUsuario(usuarioDelArbolReferidor);
        int indiceHeapRanking2 = rankingMillas.buscarPorUsuario(usaurioDelArbolReferido);
        rankingMillas.siftUp(indiceHeapRanking1);
        rankingMillas.siftUp(indiceHeapRanking2);

    }

    public void acumularMillas(int id, double distancia, String nombrePromo) {
        
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
        int indiceHeap = elMasCanjeador.buscarPorUsuario(usuarioDelArbol); // No me gusta esta forma, la complejidad de buscarlo en el arbol es en el peor caso O(U)
        elMasCanjeador.siftUp(indiceHeap);
        // Actualizo tambien el rankingMillas, aca el saldo del usuario baja, asi que al canjear el saldo lo normal seria que el usuario baje del heap porque perdio millas 
        int indiceHeapRanking = rankingMillas.buscarPorUsuario(usuarioDelArbol);
        rankingMillas.siftDown(indiceHeapRanking); 
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
        usuarioOrigenArbol.sumarMillasCanjeadas(cantidad); // Se le suma a lo que perdio el usuario (esta en el primer punto de la aclaracion)
        usuarioDestinoArbol.sumaMillasDisponibles(cantidad); // Se le agregan las millas al usuario de destino y no se modifican las totales.

        // Creo la relacion reciproca entre los usuarios
        usuarioOrigenArbol.crearReferenciaUsuario(usuarioDestinoArbol);
        usuarioDestinoArbol.crearReferenciaUsuario(usuarioOrigenArbol);
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
        int[] res = new int[cantidad];
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
        //Recorro el arbol de usuarios inorder la complejidad de esto es O(U)
        ABB<Usuario>.ABB_Iterador iterador = Usuarios.iterador();
        ArrayList<int[]> paresTransferidores = new ArrayList<>();
        while(iterador.haySiguiente()){
            Usuario usuario = iterador.siguiente(); // Obtengo el usuario haciendo la busqueda en el arbol inorder
            // Exclusivo significa que unicamente tiene 1 transferencia, hay que chequear que "el otro" sea exclusivo tambien
            if (usuario.esExclusivo()){
                Usuario otro = usuario.getOtroUsuario();
                if( otro != null && usuario.id() < otro.id() && otro.esExclusivo()){
                    paresTransferidores.add(new int[]{usuario.id(), otro.id()});
                }
            }
        }

        // Una vez que termine de armar mi array, lo tengo que pasar a int[][]
        int[][] res = new int[paresTransferidores.size()][2]; // Medio rara esta forma pero funcionar funciona
        // Esto solo porque no me dejan usar seguro nada para hacer la conversion
        for(int i =0; i < paresTransferidores.size(); i++){
            res[i] = paresTransferidores.get(i);
        }

        return res;
    }
}
