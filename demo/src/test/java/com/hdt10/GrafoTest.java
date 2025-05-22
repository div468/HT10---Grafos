/*
 * Universidad del Valle de Guatemala
 * Algoritmos y Estructuras de Datos
 * Ing. Douglas Barrios
 * @author: Marcelo Detlefsen, Julián Divas
 * Creación: 19/05/2025
 * última modificación: 19/05/2025
 * File Name: GrafoTest.java
 * Descripción: Clase con las pruebas unitarias de los métodos de la clase Grafo
 */

package com.hdt10;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.hdt10.Grafo.CondicionClimatica;

class GrafoTest {
    private Grafo grafo;

    //Valores iniciales a trabajar
    @BeforeEach
    void setUp() {
        grafo = new Grafo(10);
        
        // Creamos un grafo con 4 ciudades iniciales
        grafo.agregarCiudad("Guatemala");
        grafo.agregarCiudad("Quetzaltenango");
        grafo.agregarCiudad("Antigua");
        grafo.agregarCiudad("Escuintla");
        
        // Conexiones con diferentes pesos según condiciones climáticas
        grafo.agregarConexion("Guatemala", "Quetzaltenango", 5.0, 6.0, 7.0, 10.0);
        grafo.agregarConexion("Quetzaltenango", "Antigua", 3.0, 4.0, 5.0, 8.0);
        grafo.agregarConexion("Antigua", "Escuintla", 2.0, 3.0, 4.0, 6.0);
        grafo.agregarConexion("Guatemala", "Escuintla", 15.0, 18.0, 20.0, 25.0);
        
        //Se calculan los caminos más cortos
        grafo.floyd();
    }

    @Test
    //Test para verificar que se agregan correctamente ciudades
    void testAgregarCiudad() {
        assertTrue(grafo.contieneCiudad("Guatemala"));
        assertTrue(grafo.contieneCiudad("Quetzaltenango"));
        assertTrue(grafo.contieneCiudad("Antigua"));
        assertTrue(grafo.contieneCiudad("Escuintla"));
        assertFalse(grafo.contieneCiudad("Peten"));
        
        grafo.agregarCiudad("Flores");
        assertTrue(grafo.contieneCiudad("Flores"));
    }

    @Test
    //Test para verificar que se agregan caminos correctamente
    void testAgregarConexion() {
        // Verificar conexiones existentes
        assertTrue(grafo.getCiudades().contains("Guatemala"));
        assertTrue(grafo.getCiudades().contains("Quetzaltenango"));
        
        // Agregar nueva conexión
        grafo.agregarConexion("Guatemala", "Antigua", 7.0, 8.0, 9.0, 12.0);
        
        // Verificar que la conexión fue agregada
        Ruta ruta = grafo.obtenerRuta("Guatemala", "Antigua");
        assertNotNull(ruta);
        assertEquals(7.0, ruta.getTiempo(), 0.001);
    }

    @Test
    //Test para verificar que se eliminan conexiones correctamente
    void testEliminarConexion() {
        // Verificar que la conexión existe inicialmente
        assertNotNull(grafo.obtenerRuta("Guatemala", "Quetzaltenango"));
        
        // Eliminar conexión
        grafo.eliminarConexion("Guatemala", "Quetzaltenango");
        
        // Verificar que la conexión fue eliminada
        assertNull(grafo.obtenerRuta("Guatemala", "Quetzaltenango"));
    }

    @Test
    //Test para cambiar las condiciones del clima entre ciudades.
    void testCambiarCondicionClimatica() {
        // Condición inicial es NORMAL
        assertEquals(CondicionClimatica.NORMAL, grafo.getCondicionActual());
        
        // Cambiar a LLUVIA
        grafo.cambiarCondicionClimatica(CondicionClimatica.LLUVIA);
        assertEquals(CondicionClimatica.LLUVIA, grafo.getCondicionActual());
        
        // Verificar que los pesos han cambiado
        Ruta rutaLluvia = grafo.obtenerRuta("Guatemala", "Quetzaltenango");
        assertEquals(6.0, rutaLluvia.getTiempo(), 0.001);
        
        // Cambiar a clima nevado
        grafo.cambiarCondicionClimatica(CondicionClimatica.NIEVE);
        Ruta rutaNieve = grafo.obtenerRuta("Guatemala", "Quetzaltenango");
        assertEquals(7.0, rutaNieve.getTiempo(), 0.001);
    }

    @Test
    //Test para obtener las rutas de un punto a otro
    void testObtenerRuta() {
        // Ruta directa
        Ruta rutaAB = grafo.obtenerRuta("Guatemala", "Quetzaltenango");
        assertNotNull(rutaAB);
        assertEquals(5.0, rutaAB.getTiempo(), 0.001);
        assertEquals(2, rutaAB.getCamino().size());
        assertEquals("Guatemala", rutaAB.getCamino().get(0));
        assertEquals("Quetzaltenango", rutaAB.getCamino().get(1));
        
        // Ruta con múltiples saltos (A -> B -> C -> D)
        Ruta rutaAD = grafo.obtenerRuta("Guatemala", "Escuintla");
        assertNotNull(rutaAD);
        assertEquals(10.0, rutaAD.getTiempo(), 0.001); // 5 + 3 + 2
        assertEquals(4, rutaAD.getCamino().size());
        
        // Ruta que no existe
        assertNull(grafo.obtenerRuta("Quetzaltenango", "Guatemala"));
    }

    @Test
    //test para mostrar el centro del grafo, quetzaltenango teóricamente
    void testObtenerCentro() {
        String centro = grafo.obtenerCentro();
        assertEquals("Guatemala", centro);
        
        //Al cambiar el clima el centro puede cambiar
        grafo.cambiarCondicionClimatica(CondicionClimatica.TORMENTA);
        String centroTormenta = grafo.obtenerCentro();
        assertNotNull(centroTormenta);
    }

    @Test
    //Aumentar el tamaño de las ciudades que pueden existir
    void testRedimensionarMatrices() {
        grafo.agregarCiudad("Flores");
        for (int i = 0; i < 15; i++) {
            grafo.agregarCiudad("Quiche" + i);
        }
        
        // Verificar que podemos obtener rutas con las nuevas ciudades
        grafo.agregarConexion("Quiche", "Flores", 1.0, 1.5, 2.0, 3.0);
        Ruta rutaExtra = grafo.obtenerRuta("Quiche", "Flores");
        assertNotNull(rutaExtra);
        assertEquals(1.0, rutaExtra.getTiempo(), 0.001);
    }

    @Test
    //Verificar que el algoritma brinda las distancias minimas correctamente
    void testFloydWarshall() {
        // De Guatemala a antigua  = 8 por (5 + 3)
        Ruta rutaAC = grafo.obtenerRuta("Guatemala", "Antigua");
        assertEquals(8.0, rutaAC.getTiempo(), 0.001);
        
        // De Guatemala a escuintla = 10 (5 + 3 + 2)
        Ruta rutaAD = grafo.obtenerRuta("Guatemala", "Escuintla");
        assertEquals(10.0, rutaAD.getTiempo(), 0.001);
        
        // La ruta directa Guatemala a Escuintla tiene distancia 15, pero es más larga que Guatemala - Quetzaltenango -  Antigua - Escuintla
        assertTrue(rutaAD.getTiempo() < 15.0);
    }
}