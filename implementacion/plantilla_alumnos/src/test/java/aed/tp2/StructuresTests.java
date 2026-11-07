package aed.tp2;

import aed.tp2.Heaps.Heap;

import org.junit.jupiter.api.Test;

import aed.tp2.ABB.ABB;

import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Comparator; // Este comparador es de la interfaz de java

public class StructuresTests {

    private class PorOrden implements Comparator<Integer> {
        // Creo este Comparador para probar el HEAP
        @Override
        public int compare(Integer N1, Integer N2) {
            return Integer.compare(N1, N2);
        }
    }

    private AirMiles am;

    @BeforeEach
    void setUp() {
        ABB abb = new ABB<>();
        Heap heap = new Heap<Integer>(new PorOrden());
        Trie trie = new Trie<>();
    }

    @Test
    void insertarABB() {
        assertNotNull(am);
        assertEquals(0, am.paresTransferidoresExclusivos().length);
    }

    @Test
    void insertarHeap() {
        assertNotNull(am);
        assertEquals(0, am.paresTransferidoresExclusivos().length);
    }

    @Test
    void insertarTrie() {
        assertNotNull(am);
        assertEquals(0, am.paresTransferidoresExclusivos().length);
    }

}