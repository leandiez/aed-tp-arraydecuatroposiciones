package aed.tp2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class AirMilesTests {
    private AirMiles am;

    @BeforeEach
    void setUp() {
        am = new AirMiles();
    }

    @Test
    void crearSistema() {
        assertNotNull(am);
        assertEquals(0, am.paresTransferidoresExclusivos().length);
    }

    @Test
    void registrarUsuario() {
        am.registrarUsuario(10);
        assertEquals(0.0, am.consultarSaldo(10));
        assertEquals("Bronce", am.consultarCategoria(10));
        assertEquals(10, am.elMasAcumulador());
    }

    @Test
    void registrarUsuarioConReferidor() {
        am.registrarUsuario(1);
        am.registrarUsuarioConReferidor(2, 1);
        
        assertEquals(500.0, am.consultarSaldo(1));
        assertEquals(500.0, am.consultarSaldo(2));
        
        // El mas acumulador es ambiguo (ambos 500) pero ambos están en el sistema
        assertTrue(am.elMasAcumulador() == 1 || am.elMasAcumulador() == 2);
    }

    @Test
    void acumularMillasYRanking() {
        am.registrarUsuario(1);
        am.registrarUsuario(2);
        am.registrarUsuario(3);
        
        am.iniciarPromocion("PromoVerano", 2.0);
        
        // 100 * 1.0 (bronce) * 2.0 (promo) = 200
        am.acumularMillas(1, 100, "PromoVerano"); 
        
        // 50 * 1.0 * 1.0 = 50
        am.acumularMillas(2, 50, "NoPromo");
        
        // 300 * 1.0 * 2.0 = 600
        am.acumularMillas(3, 300, "PromoVerano");
        
        assertEquals(200.0, am.consultarSaldo(1));
        assertEquals(50.0, am.consultarSaldo(2));
        assertEquals(600.0, am.consultarSaldo(3));
        
        int[] top = am.rankingMillas(3);
        assertEquals(3, top[0]);
        assertEquals(1, top[1]);
        assertEquals(2, top[2]);
        
        assertEquals(3, am.elMasAcumulador());
    }

    @Test
    void canjearYElMasCanjeador() {
        am.registrarUsuario(1);
        am.acumularMillas(1, 100, "NoPromo"); // Tiene 100
        
        am.canjearMillas(1, 40); // Le quedan 60
        
        assertEquals(60.0, am.consultarSaldo(1));
        assertEquals(1, am.elMasCanjeador());
        
        am.registrarUsuario(2);
        am.acumularMillas(2, 200, "NoPromo"); // Tiene 200
        am.canjearMillas(2, 50); // Le quedan 150
        
        assertEquals(150.0, am.consultarSaldo(2));
        assertEquals(2, am.elMasCanjeador()); // Gastó 50 vs los 40 de usuario 1
    }

    @Test
    void transferirMillasYParesExclusivos() {
        am.registrarUsuario(1);
        am.registrarUsuario(2);
        am.registrarUsuario(3);
        
        am.acumularMillas(1, 500, "NoPromo");
        
        am.transferirMillas(1, 2, 100);
        assertEquals(400.0, am.consultarSaldo(1));
        assertEquals(100.0, am.consultarSaldo(2));
        
        int[][] exclusivos = am.paresTransferidoresExclusivos();
        assertEquals(1, exclusivos.length);
        assertTrue((exclusivos[0][0] == 1 && exclusivos[0][1] == 2) || (exclusivos[0][0] == 2 && exclusivos[0][1] == 1));
        
        am.transferirMillas(1, 3, 50); // Se rompe la exclusividad de 1
        
        assertEquals(0, am.paresTransferidoresExclusivos().length);
    }

    @Test
    void topePromocion() {
        am.registrarUsuario(1);
        am.iniciarPromocionConTope("Flash", 3.0, 300);
        
        am.acumularMillas(1, 100, "Flash"); 
        // Gana 100 * 3.0 = 300. Tope alcanzado.
        assertEquals(300.0, am.consultarSaldo(1));
        
        am.acumularMillas(1, 50, "Flash");
        // Promo ya superó tope, asume factor 1.0 (y no hay error)
        // 50 * 1.0 = 50. Total = 350.
        assertEquals(350.0, am.consultarSaldo(1));
    }

    //lean

@Test
    void transicionesCategoriaYFactores() {
        am.registrarUsuario(1);
        
        // 1. Bronce a Plata
        // Vuela 10000 con factor 1.0 (Bronce). Llega a 10000 exactos.
        am.acumularMillas(1, 10000, "NoPromo");
        assertEquals("Plata", am.consultarCategoria(1));
        
        // Vuela 1000 extra. Como es Plata (factor 1.5), suma 1500.
        // Total histórico y disponible = 10000 + 1500 = 11500.
        am.acumularMillas(1, 1000, "NoPromo");
        assertEquals(11500.0, am.consultarSaldo(1));
        
        // 2. Plata a Oro
        // Necesita llegar a 50000. Le faltan 38500. 
        // Como es Plata, vuela 38500 / 1.5 = 25666.66... usaremos un número redondo.
        am.acumularMillas(1, 26000, "NoPromo"); // 26000 * 1.5 = 39000
        // Total = 11500 + 39000 = 50500. Alcanza Oro.
        assertEquals("Oro", am.consultarCategoria(1));
        
        // Vuela 1000 extra. Como es Oro (factor 2.0), suma 2000.
        // Total = 50500 + 2000 = 52500.
        am.acumularMillas(1, 1000, "NoPromo");
        assertEquals(52500.0, am.consultarSaldo(1));
    }

@Test
    void categoriaPlatinoNoSumaMillas() {
        am.registrarUsuario(1);
        
        // Lo llevamos a Platino (1.000.000 millas)
        // Volamos 1.000.000 siendo Bronce
        am.acumularMillas(1, 1000000, "NoPromo");
        assertEquals("Platino", am.consultarCategoria(1));
        
        // Intenta acumular más millas siendo Platino (factor 0.0)
        am.acumularMillas(1, 5000, "NoPromo");
        
        // El saldo debe mantenerse intacto porque el factor es 0
        assertEquals(1000000.0, am.consultarSaldo(1));
    }

@Test
    void rankingMillasMenosUsuariosQueN() {
        am.registrarUsuario(1);
        am.registrarUsuario(2);
        
        am.acumularMillas(1, 100, "NoPromo");
        am.acumularMillas(2, 300, "NoPromo");
        
        // Pedimos un ranking de los top 5, pero solo hay 2 usuarios en el sistema
        int[] top = am.rankingMillas(5);
        
        // Debe devolver solo los que existen, ordenados de mayor a menor saldo
        assertEquals(2, top.length);
        assertEquals(2, top[0]);
        assertEquals(1, top[1]);
    }


@Test
    void testIntegracionCompleja() {
        // 1. Registro en cadena con referidores
        am.registrarUsuario(1); // U1: 0 totales, 0 disp.
        am.registrarUsuarioConReferidor(2, 1); // U2 referido por U1. Ambos ganan 500.
        am.registrarUsuarioConReferidor(3, 2); // U3 referido por U2. Ambos ganan 500.
        am.registrarUsuarioConReferidor(4, 3); // U4 referido por U3. Ambos ganan 500.
        
        // Verificación de saldos iniciales por bonos
        assertEquals(500.0, am.consultarSaldo(1));  // Ganó 500 por referir a U2
        assertEquals(1000.0, am.consultarSaldo(2)); // Ganó 500 (referido) + 500 (referidor de U3)
        assertEquals(1000.0, am.consultarSaldo(3)); // Ganó 500 (referido) + 500 (referidor de U4)
        assertEquals(500.0, am.consultarSaldo(4));  // Ganó 500 por ser referido

        // 2. Creación de promociones
        am.iniciarPromocion("PromoVerano", 2.0);
        am.iniciarPromocionConTope("PromoRelampago", 3.0, 900.0);

        // 3. Acumulación masiva y cambios de categoría
        // U1 vuela 9500 millas sin promo. Total = 500 + 9500 = 10000. Sube a Plata
        am.acumularMillas(1, 9500, "NoPromo");
        assertEquals("Plata", am.consultarCategoria(1));
        assertEquals(10000.0, am.consultarSaldo(1));

        // U1 vuela 1000 millas con PromoVerano. Factor Plata (1.5) * Promo (2.0) = 3.0
        // Gana 3000 millas. Total U1 = 13000. Disp = 13000.
        am.acumularMillas(1, 1000, "PromoVerano");
        assertEquals(13000.0, am.consultarSaldo(1));

        // 4. Consumo de promoción con tope
        // U2 vuela 200 millas con PromoRelampago. Factor Bronce (1.0) * Promo (3.0) = 3.0
        // Gana 600 millas. Tope restante = 300. Disp U2 = 1600.
        am.acumularMillas(2, 200, "PromoRelampago");
        assertEquals(1600.0, am.consultarSaldo(2));

        // U3 vuela 100 millas con PromoRelampago. Factor Bronce (1.0) * Promo (3.0) = 3.0
        // Gana 300 millas. Tope restante = 0 (Se desactiva la promo). Disp U3 = 1300
        am.acumularMillas(3, 100, "PromoRelampago");
        assertEquals(1300.0, am.consultarSaldo(3));

        // 5. Transferencias y Canjes
        // U1 transfiere 3000 millas a U4. 
        // U1 Disp = 10000. U4 Disp = 3500. Las totales no cambian
        am.transferirMillas(1, 4, 3000);
        assertEquals(10000.0, am.consultarSaldo(1));
        assertEquals(3500.0, am.consultarSaldo(4));

        // U2 canjea 600 millas. U2 Disp = 1000. Las totales no cambian
        am.canjearMillas(2, 600);
        assertEquals(1000.0, am.consultarSaldo(2));

        // 6. Verificaciones Finales de Estadísticas y Ranking
        // Totales históricos (calculados manualmente):
        // U1 = 13000
        // U2 = 1600
        // U3 = 1300
        // U4 = 500

        // Mas acumulador debe ser U1
        assertEquals(1, am.elMasAcumulador());

        // Mas canjeador: 
        // U1 perdió 3000 (transferidas)
        // U2 perdió 600 (canjeadas)
        // El mas canjeador debe ser U1 porque suma canjes + transferencias enviadas
        assertEquals(1, am.elMasCanjeador());

        // Ranking de disponibles (Top 4)
        // U1: 10000
        // U4: 3500
        // U3: 1300
        // U2: 1000
        int[] top = am.rankingMillas(4);
        assertArrayEquals(new int[]{1, 4, 3, 2}, top);
        
        // Pares transferidores exclusivos: solo hubo una transferencia (1 a 4)
        int[][] exclusivos = am.paresTransferidoresExclusivos();
        assertEquals(1, exclusivos.length);
        assertTrue((exclusivos[0][0] == 1 && exclusivos[0][1] == 4) || 
                   (exclusivos[0][0] == 4 && exclusivos[0][1] == 1));
    }

@Test
    void testRankingInmutableAnteMultiplesConsultas() {
        am.registrarUsuario(1);
        am.registrarUsuario(2);
        am.registrarUsuario(3);
        
        am.acumularMillas(1, 100, "NoPromo");
        am.acumularMillas(2, 500, "NoPromo");
        am.acumularMillas(3, 300, "NoPromo");
        
        // Primera consulta
        int[] top1 = am.rankingMillas(3);
        assertArrayEquals(new int[]{2, 3, 1}, top1);
        
        // Segunda consulta inmediata: debe devolver EXACTAMENTE lo mismo
        // Si la reinserción en el MaxHeap falló, este test se rompe.
        int[] top2 = am.rankingMillas(3);
        assertArrayEquals(new int[]{2, 3, 1}, top2);
    }



@Test
    void testRupturaDeHandleParesExclusivos() {
        am.registrarUsuario(1); am.registrarUsuario(2);
        am.registrarUsuario(3); am.registrarUsuario(4);
        
        am.acumularMillas(1, 1000, "NoPromo");
        am.acumularMillas(3, 1000, "NoPromo");
        
        // Se forma par (1, 2)
        am.transferirMillas(1, 2, 100); 
        // Se forma par (3, 4)
        am.transferirMillas(3, 4, 100); 
        
        assertEquals(2, am.paresTransferidoresExclusivos().length);
        
        // Usuario 2 transfiere a 3. Esto rompe la exclusividad de 2 (y elimina el par 1-2).
        // Pero el par (3, 4) también se rompe porque 3 ahora interactuó con 2.
        am.transferirMillas(2, 3, 50);
        
        // Ambos handles (el del par 1-2 y el del 3-4) deberían haber llamado a eliminar()
        assertEquals(0, am.paresTransferidoresExclusivos().length);
    }

    @Test
    void rankingDespuesDeRegistroConReferidor() {
        am.registrarUsuario(1);
        am.registrarUsuario(2);
        am.registrarUsuario(3);
        // 2 refiere a 4: ambos quedan con 500, el resto con 0
        am.registrarUsuarioConReferidor(4, 2);
        am.registrarUsuario(5);

        int[] top = am.rankingMillas(5);
        assertEquals(5, top.length);
        assertTrue((top[0] == 2 && top[1] == 4) || (top[0] == 4 && top[1] == 2));
    }

    @Test
    void variosParesExclusivos() {
        for (int id = 1; id <= 6; id++) am.registrarUsuario(id);
        am.acumularMillas(1, 1000, "NoPromo");
        am.acumularMillas(3, 1000, "NoPromo");
        am.acumularMillas(5, 1000, "NoPromo");

        am.transferirMillas(1, 2, 100);
        am.transferirMillas(3, 4, 100);
        am.transferirMillas(5, 6, 100);

        int[][] pares = am.paresTransferidoresExclusivos();
        assertEquals(3, pares.length);
        boolean par12 = false, par34 = false, par56 = false;
        for (int[] par : pares) {
            assertNotNull(par);
            int a = Math.min(par[0], par[1]);
            int b = Math.max(par[0], par[1]);
            if (a == 1 && b == 2) par12 = true;
            if (a == 3 && b == 4) par34 = true;
            if (a == 5 && b == 6) par56 = true;
        }
        assertTrue(par12 && par34 && par56);
    }

    @Test
    void rankingSeActualizaDespuesDeConsultarlo() {
        am.registrarUsuario(1);
        am.acumularMillas(1, 300, "NoPromo");
        am.registrarUsuario(2);
        am.acumularMillas(2, 500, "NoPromo");
        am.registrarUsuario(3);
        am.acumularMillas(3, 100, "NoPromo");
        am.registrarUsuario(4);
        am.acumularMillas(4, 100, "NoPromo");
        am.rankingMillas(3);

        // Después de consultar el ranking, los cambios de saldo lo tienen que seguir actualizando
        am.acumularMillas(3, 500, "NoPromo"); // 3 queda con 600
        assertArrayEquals(new int[]{3, 2, 1, 4}, am.rankingMillas(4));
    }

    @Test
    void rankingConSaldosIgualesYCanje() {
        // Para cada usuario: todos empiezan con el mismo saldo, se consulta el ranking,
        // ese usuario canjea y tiene que quedar último
        for (int canjeador = 1; canjeador <= 4; canjeador++) {
            am = new AirMiles();
            for (int id = 1; id <= 4; id++) {
                am.registrarUsuario(id);
                am.acumularMillas(id, 100, "NoPromo");
            }
            am.rankingMillas(1);
            am.canjearMillas(canjeador, 50);

            int[] top = am.rankingMillas(4);
            assertEquals(4, top.length);
            assertEquals(canjeador, top[3]);
        }
    }

    @Test
    void transferenciaRecibidaNoSumaATotales() {
        am.registrarUsuario(1);
        am.registrarUsuario(2);
        am.acumularMillas(1, 9000, "NoPromo");
        am.acumularMillas(2, 5000, "NoPromo");

        // 2 recibe 6000: se suman a sus disponibles, pero no a sus totales (siguen en 5000)
        am.transferirMillas(1, 2, 6000);
        assertEquals(11000.0, am.consultarSaldo(2));
        assertEquals("Bronce", am.consultarCategoria(2));
        assertEquals(1, am.elMasAcumulador());
        // Las millas transferidas sí cuentan para elMasCanjeador
        assertEquals(1, am.elMasCanjeador());

        // Sigue siendo Bronce: vuela 100 con factor 1.0
        am.acumularMillas(2, 100, "NoPromo");
        assertEquals(11100.0, am.consultarSaldo(2));
    }

    @Test
    void finalizarPromocion() {
        am.registrarUsuario(1);
        am.iniciarPromocion("PromoVerano", 2.0);
        am.acumularMillas(1, 100, "PromoVerano"); // 200
        am.finalizarPromocion("PromoVerano");
        am.acumularMillas(1, 100, "PromoVerano"); // ya no está activa: factor 1.0
        assertEquals(300.0, am.consultarSaldo(1));
    }

    // Modelo simple del sistema para el stress test (usuarios con ids 1..maxId)
    private static class Modelo {
        int registrados = 0;
        double[] totales, disponibles, gastadas; // gastadas = canjeadas + enviadas
        int[] contraparte;                      // única contraparte en transferencias (0 si no transfirió)
        boolean[] variasContrapartes;
        boolean[][] transfirieron;

        Modelo(int maxId) {
            totales = new double[maxId + 1];
            disponibles = new double[maxId + 1];
            gastadas = new double[maxId + 1];
            contraparte = new int[maxId + 1];
            variasContrapartes = new boolean[maxId + 1];
            transfirieron = new boolean[maxId + 1][maxId + 1];
        }

        String categoria(int id) {
            if (totales[id] < 10000) return "Bronce";
            if (totales[id] < 50000) return "Plata";
            if (totales[id] < 1000000) return "Oro";
            return "Platino";
        }

        double factorCategoria(int id) {
            if (totales[id] < 10000) return 1.0;
            if (totales[id] < 50000) return 1.5;
            if (totales[id] < 1000000) return 2.0;
            return 0.0;
        }

        void sumar(int id, double millas) {
            totales[id] += millas;
            disponibles[id] += millas;
        }

        void gastar(int id, double millas) {
            disponibles[id] -= millas;
            gastadas[id] += millas;
        }

        void transferir(int origen, int destino, double cantidad) {
            gastar(origen, cantidad);
            disponibles[destino] += cantidad;
            transfirieron[origen][destino] = transfirieron[destino][origen] = true;
            for (int[] par : new int[][]{{origen, destino}, {destino, origen}}) {
                if (contraparte[par[0]] == 0) contraparte[par[0]] = par[1];
                else if (contraparte[par[0]] != par[1]) variasContrapartes[par[0]] = true;
            }
        }

        boolean esPar(int a, int b) {
            return a != b && !variasContrapartes[a] && !variasContrapartes[b]
                    && contraparte[a] == b && contraparte[b] == a;
        }

        double maximo(double[] valores) {
            double max = 0;
            for (int id = 1; id <= registrados; id++) max = Math.max(max, valores[id]);
            return max;
        }
    }

    @Test
    void stressTest() {
        final int USUARIOS = 500;
        final int OPERACIONES = 20000;
        java.util.Random rnd = new java.util.Random(1234);
        Modelo m = new Modelo(USUARIOS);
        String[] promos = {"PromoDoble", "PromoTriple"};
        double[] factores = {2.0, 3.0};
        am.iniciarPromocion(promos[0], factores[0]);
        am.iniciarPromocion(promos[1], factores[1]);
        am.registrarUsuario(++m.registrados);

        for (int op = 0; op < OPERACIONES; op++) {
            // Unos pocos usuarios concentran muchas operaciones, para que lleguen a categorías altas
            int id = rnd.nextInt(5) == 0 ? 1 + rnd.nextInt(Math.min(10, m.registrados)) : 1 + rnd.nextInt(m.registrados);
            int r = rnd.nextInt(100);

            if (r < 10 && m.registrados < USUARIOS) {
                int nuevo = ++m.registrados;
                // Como en los tests, los referidores son Bronce y lo siguen siendo después del bono
                if (rnd.nextBoolean() && m.totales[id] + 500 < 10000) {
                    am.registrarUsuarioConReferidor(nuevo, id);
                    m.sumar(nuevo, 500);
                    m.sumar(id, 500);
                } else {
                    am.registrarUsuario(nuevo);
                }
                assertEquals(m.disponibles[nuevo], am.consultarSaldo(nuevo));
            } else if (r < 50) {
                double distancia = 1 + rnd.nextInt(rnd.nextInt(20) == 0 ? 100000 : 1000);
                // Como en los tests, los usuarios Platino solo acumulan con NoPromo
                int p = m.categoria(id).equals("Platino") ? -1 : rnd.nextInt(3) - 1;
                am.acumularMillas(id, distancia, p < 0 ? "NoPromo" : promos[p]);
                m.sumar(id, distancia * m.factorCategoria(id) * (p < 0 ? 1.0 : factores[p]));
            } else if (r < 65 && m.disponibles[id] >= 2) {
                double cantidad = 1 + rnd.nextInt((int) Math.min(m.disponibles[id] / 2, 5000));
                am.canjearMillas(id, cantidad);
                m.gastar(id, cantidad);
            } else if (r < 85) {
                // Se forman pares con las parejas (2k-1, 2k) y se rompen transfiriendo a otros usuarios.
                // Nunca se transfiere entre dos usuarios que ya se transfirieron entre sí.
                int pareja = id % 2 == 1 ? id + 1 : id - 1;
                boolean libres = pareja <= m.registrados && m.contraparte[id] == 0 && m.contraparte[pareja] == 0;
                int destino = libres ? pareja : (rnd.nextInt(10) == 0 ? 1 + rnd.nextInt(m.registrados) : id);
                if (destino != id && !m.transfirieron[id][destino] && m.disponibles[id] >= 2) {
                    double cantidad = 1 + rnd.nextInt((int) Math.min(m.disponibles[id] / 2, 5000));
                    am.transferirMillas(id, destino, cantidad);
                    m.transferir(id, destino, cantidad);
                    assertEquals(m.disponibles[destino], am.consultarSaldo(destino));
                    verificarPares(m);
                }
            } else if (r < 92) {
                verificarRanking(m, 1 + rnd.nextInt(m.registrados + 5));
            }

            assertEquals(m.disponibles[id], am.consultarSaldo(id));
            assertEquals(m.categoria(id), am.consultarCategoria(id));
            // Los máximos se comparan por valor, para no depender de cómo se desempata
            assertEquals(m.maximo(m.totales), m.totales[am.elMasAcumulador()]);
            if (m.maximo(m.gastadas) > 0) assertEquals(m.maximo(m.gastadas), m.gastadas[am.elMasCanjeador()]);
        }

        for (int id = 1; id <= m.registrados; id++) assertEquals(m.disponibles[id], am.consultarSaldo(id));
        verificarRanking(m, m.registrados);
        verificarPares(m);
    }

    // Compara los saldos de los usuarios devueltos (no los ids, para no depender de cómo se desempata)
    private void verificarRanking(Modelo m, int n) {
        int[] top = am.rankingMillas(n);
        assertEquals(Math.min(n, m.registrados), top.length);
        double[] saldos = java.util.Arrays.copyOfRange(m.disponibles, 1, m.registrados + 1);
        java.util.Arrays.sort(saldos);
        for (int i = 0; i < top.length; i++) {
            assertEquals(saldos[saldos.length - 1 - i], m.disponibles[top[i]]);
        }
    }

    private void verificarPares(Modelo m) {
        int[][] pares = am.paresTransferidoresExclusivos();
        int esperados = 0;
        for (int id = 1; id <= m.registrados; id++) if (m.contraparte[id] > id && m.esPar(id, m.contraparte[id])) esperados++;
        assertEquals(esperados, pares.length);
        boolean[] visto = new boolean[m.registrados + 1];
        for (int[] par : pares) {
            assertNotNull(par);
            assertTrue(m.esPar(par[0], par[1]));
            assertFalse(visto[par[0]]); // sin pares repetidos
            visto[par[0]] = visto[par[1]] = true;
        }
    }
}
