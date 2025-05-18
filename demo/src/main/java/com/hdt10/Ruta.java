/*
 * Universidad del Valle de Guatemala
 * Algoritmos y Estructuras de Datos
 * Ing. Douglas Barrios
 * @author: Marcelo Detlefsen, Julián Divas
 * Creación: 17/05/2025
 * última modificación: 17/05/2025
 * File Name: Ruta.java
 * Descripción: Clase que representa una ruta entre dos ciudades en un grafo dirigido con pesos.
 * Esta clase almacena el camino y el costo total de la ruta.
 */

package com.hdt10;

import java.util.List;

public class Ruta {
    private final List<String> camino;
    private final double costo;

    public Ruta(List<String> camino, double costo) {
        this.camino = camino;
        this.costo = costo;
    }

    public void imprimirRuta() {
        System.out.println("Ruta: " + String.join(" -> ", camino));
        System.out.printf("Costo total: %.2f horas\n", costo);
    }
}