/*
 * Universidad del Valle de Guatemala
 * Algoritmos y Estructuras de Datos
 * Ing. Douglas Barrios
 * @author: Marcelo Detlefsen, Julián Divas
 * Creación: 17/05/2025
 * última modificación: 17/05/2025
 * File Name: Grafo.java
 * Descripción: Clase que representa un grafo dirigido con pesos, implementando el algoritmo de Floyd-Warshall para encontrar caminos más cortos entre todas las ciudades.
 * Esta clase permite agregar ciudades, conexiones entre ellas, eliminar conexiones, calcular rutas más cortas y encontrar el centro del grafo.
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
    private double[][] matriz;
    private int[][] next;
    private final static double INF = Double.POSITIVE_INFINITY;

    public Grafo(int capacidadInicial) {
        matriz = new double[capacidadInicial][capacidadInicial];
        next = new int[capacidadInicial][capacidadInicial];
        for (double[] fila : matriz) Arrays.fill(fila, INF);
        for (int i = 0; i < capacidadInicial; i++)
            Arrays.fill(next[i], -1);
    }

    public void agregarCiudad(String nombre) {

    }

    public void agregarConexion(String ciudad1, String ciudad2, double peso) {

    }

    public void eliminarConexion(String ciudad1, String ciudad2) {

    }

    public void floyd() {
        int n = ciudades.size();
        for (int k = 0; k < n; k++)
            for (int i = 0; i < n; i++)
                for (int j = 0; j < n; j++)
                    if (matriz[i][k] + matriz[k][j] < matriz[i][j]) {
                        matriz[i][j] = matriz[i][k] + matriz[k][j];
                        next[i][j] = next[i][k];
                    }
    }

    /// Devuelve la ruta más corta entre dos ciudades
    public Ruta obtenerRuta(String origen, String destino) {
        return null;
    }

    public String obtenerCentro() {
        return null;
    }

    public void imprimirMatriz() {

    }

    public boolean contieneCiudad(String ciudad) {
        return ciudades.containsKey(ciudad);
    }

    public Set<String> getCiudades() {
        return ciudades.keySet();
    }
}
