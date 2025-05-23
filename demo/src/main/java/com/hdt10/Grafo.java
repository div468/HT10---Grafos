/*
 * Universidad del Valle de Guatemala
 * Algoritmos y Estructuras de Datos
 * Ing. Douglas Barrios
 * @author: Marcelo Detlefsen, Julián Divas
 * Creación: 17/05/2025
 * última modificación: 17/05/2025
 * File Name: Grafo.java
 * Descripción: Clase que representa un grafo dirigido con pesos, implementando el algoritmo de Floyd-Warshall para encontrar caminos más cortos entre todas las ciudades.
 *              Esta clase permite agregar ciudades, conexiones entre ellas, eliminar conexiones, calcular rutas más cortas y encontrar el centro del grafo.
 *              Ahora soporta diferentes pesos según condiciones climáticas (normal, lluvia, nieve, tormenta).
 */

package com.hdt10;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Grafo {
    private final Map<String, Integer> ciudades = new HashMap<>();
    private final List<String> nombresCiudades = new ArrayList<>();
    
    // Matrices para cada condición climática
    private double[][] matrizNormal;
    private double[][] matrizLluvia;
    private double[][] matrizNieve;
    private double[][] matrizTormenta;
    
    private double[][] matrizActual; // Matriz que se usa para los cálculos actuales
    private int[][] next;
    private final static double INF = Double.POSITIVE_INFINITY;
    
    /**
     * Enumeración que representa las posibles condiciones climáticas que afectan los pesos de las conexiones.
     */
    public enum CondicionClimatica {
        NORMAL, LLUVIA, NIEVE, TORMENTA
    }
    
    private CondicionClimatica condicionActual = CondicionClimatica.NORMAL;

    /**
     * Constructor del grafo.
     * 
     * @param capacidadInicial Capacidad inicial para las matrices de adyacencia.
     */
    public Grafo(int capacidadInicial) {
        // Inicializar todas las matrices
        matrizNormal = new double[capacidadInicial][capacidadInicial];
        matrizLluvia = new double[capacidadInicial][capacidadInicial];
        matrizNieve = new double[capacidadInicial][capacidadInicial];
        matrizTormenta = new double[capacidadInicial][capacidadInicial];
        next = new int[capacidadInicial][capacidadInicial];
        
        // Inicializar todas las matrices con infinito
        for (double[] fila : matrizNormal) Arrays.fill(fila, INF);
        for (double[] fila : matrizLluvia) Arrays.fill(fila, INF);
        for (double[] fila : matrizNieve) Arrays.fill(fila, INF);
        for (double[] fila : matrizTormenta) Arrays.fill(fila, INF);
        
        // Inicializar next con -1
        for (int i = 0; i < capacidadInicial; i++)
            Arrays.fill(next[i], -1);
        
        // Por defecto, trabajamos con la matriz normal
        matrizActual = matrizNormal;
    }

    /**
     * Agrega una ciudad al grafo si no existe.
     * 
     * @param nombre Nombre de la ciudad a agregar.
     */
    public void agregarCiudad(String nombre) {
        if (!ciudades.containsKey(nombre)) {
            int index = ciudades.size();
            ciudades.put(nombre, index);
            nombresCiudades.add(nombre);
            
            // Si hemos excedido la capacidad inicial, redimensionar
            if (index >= matrizNormal.length) {
                redimensionarMatrices(index + 10); // Aumentar en 10 por seguridad
            }
        }
    }
    
    /**
     * Redimensiona las matrices del grafo para aumentar su capacidad.
     * 
     * @param nuevaCapacidad Nueva capacidad deseada.
     */
    private void redimensionarMatrices(int nuevaCapacidad)   {
        double[][] nuevaMatrizNormal = new double[nuevaCapacidad][nuevaCapacidad];
        double[][] nuevaMatrizLluvia = new double[nuevaCapacidad][nuevaCapacidad];
        double[][] nuevaMatrizNieve = new double[nuevaCapacidad][nuevaCapacidad];
        double[][] nuevaMatrizTormenta = new double[nuevaCapacidad][nuevaCapacidad];
        int[][] nuevoNext = new int[nuevaCapacidad][nuevaCapacidad];
        
        // Inicializar todas las matrices nuevas con infinito
        for (double[] fila : nuevaMatrizNormal) Arrays.fill(fila, INF);
        for (double[] fila : nuevaMatrizLluvia) Arrays.fill(fila, INF);
        for (double[] fila : nuevaMatrizNieve) Arrays.fill(fila, INF);
        for (double[] fila : nuevaMatrizTormenta) Arrays.fill(fila, INF);
        
        // Inicializar next con -1
        for (int i = 0; i < nuevaCapacidad; i++)
            Arrays.fill(nuevoNext[i], -1);
        
        // Copiar datos antiguos
        int viejaCapacidad = matrizNormal.length;
        for (int i = 0; i < viejaCapacidad; i++) {
            for (int j = 0; j < viejaCapacidad; j++) {
                nuevaMatrizNormal[i][j] = matrizNormal[i][j];
                nuevaMatrizLluvia[i][j] = matrizLluvia[i][j];
                nuevaMatrizNieve[i][j] = matrizNieve[i][j];
                nuevaMatrizTormenta[i][j] = matrizTormenta[i][j];
                nuevoNext[i][j] = next[i][j];
            }
        }
        
        // Reemplazar matrices
        matrizNormal = nuevaMatrizNormal;
        matrizLluvia = nuevaMatrizLluvia;
        matrizNieve = nuevaMatrizNieve;
        matrizTormenta = nuevaMatrizTormenta;
        next = nuevoNext;
        
        // Actualizar la matriz actual
        switch (condicionActual) {
            case NORMAL: matrizActual = matrizNormal; break;
            case LLUVIA: matrizActual = matrizLluvia; break;
            case NIEVE: matrizActual = matrizNieve; break;
            case TORMENTA: matrizActual = matrizTormenta; break;
        }
    }

    /**
     * Agrega una conexión entre dos ciudades con diferentes tiempos según condiciones climáticas.
     * 
     * @param ciudad1 Ciudad origen.
     * @param ciudad2 Ciudad destino.
     * @param tiempoNormal Tiempo en condiciones normales.
     * @param tiempoLluvia Tiempo con lluvia.
     * @param tiempoNieve Tiempo con nieve.
     * @param tiempoTormenta Tiempo con tormenta.
     */
    public void agregarConexion(String ciudad1, String ciudad2, double tiempoNormal, double tiempoLluvia, 
                                double tiempoNieve, double tiempoTormenta) {
        agregarCiudad(ciudad1);
        agregarCiudad(ciudad2);
        int i = ciudades.get(ciudad1);
        int j = ciudades.get(ciudad2);
        
        matrizNormal[i][j] = tiempoNormal;
        matrizLluvia[i][j] = tiempoLluvia;
        matrizNieve[i][j] = tiempoNieve;
        matrizTormenta[i][j] = tiempoTormenta;
        
        // Actualizar next para todas las condiciones
        next[i][j] = j;
    }

    /**
     * Elimina una conexión entre dos ciudades.
     * 
     * @param ciudad1 Ciudad origen.
     * @param ciudad2 Ciudad destino.
     */
    public void eliminarConexion(String ciudad1, String ciudad2) {
        int i = ciudades.get(ciudad1);
        int j = ciudades.get(ciudad2);
        
        matrizNormal[i][j] = INF;
        matrizLluvia[i][j] = INF;
        matrizNieve[i][j] = INF;
        matrizTormenta[i][j] = INF;
        
        next[i][j] = -1;
    }
    
    /**
     * Cambia la condición climática actual y recalcula las rutas.
     * 
     * @param condicion Nueva condición climática.
     */
    public void cambiarCondicionClimatica(CondicionClimatica condicion) {
        this.condicionActual = condicion;
        switch (condicion) {
            case NORMAL: matrizActual = matrizNormal; break;
            case LLUVIA: matrizActual = matrizLluvia; break;
            case NIEVE: matrizActual = matrizNieve; break;
            case TORMENTA: matrizActual = matrizTormenta; break;
        }
        
        // Recalcular Floyd-Warshall con la nueva matriz
        floyd();
    }

    /**
     * Ejecuta el algoritmo de Floyd-Warshall para calcular las rutas más cortas.
     */
    public void floyd() {
        int n = ciudades.size();
        // Resetear la matriz next
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (matrizActual[i][j] != INF) {
                    next[i][j] = j;
                } else {
                    next[i][j] = -1;
                }
            }
        }
        // Algoritmo de Floyd-Warshall
        for (int k = 0; k < n; k++)
            for (int i = 0; i < n; i++)
                for (int j = 0; j < n; j++)
                    if (matrizActual[i][k] + matrizActual[k][j] < matrizActual[i][j]) {
                        matrizActual[i][j] = matrizActual[i][k] + matrizActual[k][j];
                        next[i][j] = next[i][k];
                    }
    }

    /**
     * Obtiene la ruta más corta entre dos ciudades.
     * 
     * @param origen Ciudad origen.
     * @param destino Ciudad destino.
     * @return Objeto Ruta con el camino y tiempo, o null si no hay ruta.
     */
    public Ruta obtenerRuta(String origen, String destino) {
        if (!ciudades.containsKey(origen) || !ciudades.containsKey(destino)) {
            return null;
        }
        
        int i = ciudades.get(origen);
        int j = ciudades.get(destino);
        
        if (matrizActual[i][j] == INF) return null;
        
        List<String> camino = new ArrayList<>();
        for (int at = i; at != j; at = next[at][j]) {
            if (at == -1) return null; // Si no hay camino
            camino.add(nombresCiudades.get(at));
        }
        camino.add(destino);
        return new Ruta(camino, matrizActual[i][j]);
    }

    /**
     * Calcula la ciudad que está en el centro del grafo (menor excentricidad máxima).
     * 
     * @return Nombre de la ciudad centro, o mensaje de error si no se puede determinar.
     */
    public String obtenerCentro() {
        int centro = -1;
        double menorExcentricidad = Double.POSITIVE_INFINITY;
        
        for (int i = 0; i < ciudades.size(); i++) {
            double excentricidad = 0;
            boolean esValido = true;
            
            for (int j = 0; j < ciudades.size(); j++) {
                if (i == j) continue;
                if (matrizActual[i][j] == INF) {
                    esValido = false;
                    break;
                }
                excentricidad = Math.max(excentricidad, matrizActual[i][j]);
            }
            
            if (esValido && excentricidad < menorExcentricidad) {
                menorExcentricidad = excentricidad;
                centro = i;
            }
        }
        
        return (centro == -1) ? "No hay centro" : nombresCiudades.get(centro);
    }

    /**
     * Imprime la matriz de adyacencia actual según la condición climática.
     */
    public void imprimirMatriz() {
        System.out.println("Matriz de adyacencia (" + condicionActual + "):");
        for (int i = 0; i < ciudades.size(); i++) {
            for (int j = 0; j < ciudades.size(); j++) {
                double d = matrizActual[i][j];
                System.out.printf("%10s", d == INF ? "INF" : String.format("%.2f", d));
            }
            System.out.println();
        }
    }

    /**
     * Verifica si una ciudad existe en el grafo.
     * 
     * @param ciudad Nombre de la ciudad.
     * @return true si existe, false en caso contrario.
     */
    public boolean contieneCiudad(String ciudad) {
        return ciudades.containsKey(ciudad);
    }

    /**
     * Obtiene el conjunto de ciudades en el grafo.
     * 
     * @return Conjunto de nombres de ciudades.
     */
    public Set<String> getCiudades() {
        return ciudades.keySet();
    }
    
    /**
     * Obtiene la condición climática actual.
     * 
     * @return Condición climática actual.
     */
    public CondicionClimatica getCondicionActual() {
        return condicionActual;
    }
}