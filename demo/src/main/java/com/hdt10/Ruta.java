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
    private List<String> camino;
    private double tiempo;
    
    public Ruta(List<String> camino, double tiempo) {
        this.camino = camino;
        this.tiempo = tiempo;
    }
    
    public List<String> getCamino() {
        return camino;
    }
    
    public double getTiempo() {
        return tiempo;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Ruta: ");
        for (int i = 0; i < camino.size(); i++) {
            sb.append(camino.get(i));
            if (i < camino.size() - 1) {
                sb.append(" -> ");
            }
        }
        sb.append("\nTiempo: ").append(String.format("%.2f", tiempo));
        return sb.toString();
    }
}