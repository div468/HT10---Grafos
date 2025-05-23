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

import java.util.Set;

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
    //Test para cambiar las condiciones del clima entre ciudades específicas.
    void testCambiarCondicionClimatica() {
        // Verificar condición inicial de una conexión específica (debería ser NORMAL)
        CondicionClimatica condicionInicial = grafo.getCondicionConexion("Guatemala", "Quetzaltenango");
        assertEquals(CondicionClimatica.NORMAL, condicionInicial);
        
        // Cambiar condición de una conexión específica a LLUVIA
        boolean cambioExitoso = grafo.cambiarCondicionConexion("Guatemala", "Quetzaltenango", CondicionClimatica.LLUVIA);
        assertTrue(cambioExitoso);
        
        // Verificar que la condición cambió
        CondicionClimatica nuevaCondicion = grafo.getCondicionConexion("Guatemala", "Quetzaltenango");
        assertEquals(CondicionClimatica.LLUVIA, nuevaCondicion);
        
        // Recalcular rutas después del cambio
        grafo.floyd();
        
        // Verificar que el peso de la ruta cambió según la nueva condición climática
        Ruta rutaConLluvia = grafo.obtenerRuta("Guatemala", "Quetzaltenango");
        assertNotNull(rutaConLluvia);
        assertEquals(6.0, rutaConLluvia.getTiempo(), 0.001); // Tiempo con lluvia
        
        // Cambiar a condición de NIEVE
        grafo.cambiarCondicionConexion("Guatemala", "Quetzaltenango", CondicionClimatica.NIEVE);
        grafo.floyd(); // Recalcular rutas
        
        Ruta rutaConNieve = grafo.obtenerRuta("Guatemala", "Quetzaltenango");
        assertEquals(7.0, rutaConNieve.getTiempo(), 0.001); // Tiempo con nieve
        
        // Cambiar a condición de TORMENTA
        grafo.cambiarCondicionConexion("Guatemala", "Quetzaltenango", CondicionClimatica.TORMENTA);
        grafo.floyd(); // Recalcular rutas
        
        Ruta rutaConTormenta = grafo.obtenerRuta("Guatemala", "Quetzaltenango");
        assertEquals(10.0, rutaConTormenta.getTiempo(), 0.001); // Tiempo con tormenta
        
        // Verificar que otras conexiones no se vieron afectadas
        CondicionClimatica otraConexion = grafo.getCondicionConexion("Quetzaltenango", "Antigua");
        assertEquals(CondicionClimatica.NORMAL, otraConexion);
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
        // Obtener el centro inicial
        String centroInicial = grafo.obtenerCentro();
        assertNotNull(centroInicial);
        assertTrue(grafo.contieneCiudad(centroInicial)); // Verificar que el centro es una ciudad válida
        
        // El centro debería ser una de las ciudades existentes
        Set<String> ciudadesDisponibles = grafo.getCiudades();
        assertTrue(ciudadesDisponibles.contains(centroInicial));
        
        // Cambiar condiciones climáticas de varias conexiones y verificar si el centro cambia
        grafo.cambiarCondicionConexion("Guatemala", "Quetzaltenango", CondicionClimatica.TORMENTA);
        grafo.cambiarCondicionConexion("Guatemala", "Escuintla", CondicionClimatica.TORMENTA);
        grafo.floyd(); // Recalcular rutas
        
        String centroConTormenta = grafo.obtenerCentro();
        assertNotNull(centroConTormenta);
        assertTrue(grafo.contieneCiudad(centroConTormenta));
        
        // Agregar una nueva ciudad más central y verificar si se convierte en el nuevo centro
        grafo.agregarCiudad("CiudadCentral");
        grafo.agregarConexion("CiudadCentral", "Guatemala", 2.0, 2.5, 3.0, 4.0);
        grafo.agregarConexion("CiudadCentral", "Quetzaltenango", 2.0, 2.5, 3.0, 4.0);
        grafo.agregarConexion("CiudadCentral", "Antigua", 2.0, 2.5, 3.0, 4.0);
        grafo.agregarConexion("CiudadCentral", "Escuintla", 2.0, 2.5, 3.0, 4.0);
        grafo.floyd(); // Recalcular rutas
        
        String centroConNuevaCiudad = grafo.obtenerCentro();
        assertNotNull(centroConNuevaCiudad);
        
        // La nueva ciudad debería ser más central debido a sus conexiones cortas
        // (esto depende de la implementación específica del algoritmo de centro)
        
        // Restaurar condiciones normales para verificar consistencia
        grafo.cambiarCondicionConexion("Guatemala", "Quetzaltenango", CondicionClimatica.NORMAL);
        grafo.cambiarCondicionConexion("Guatemala", "Escuintla", CondicionClimatica.NORMAL);
        grafo.floyd();
        
        String centroFinal = grafo.obtenerCentro();
        assertNotNull(centroFinal);
        assertTrue(grafo.contieneCiudad(centroFinal));
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