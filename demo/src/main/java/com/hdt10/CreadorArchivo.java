/*
 * Universidad del Valle de Guatemala
 * Algoritmos y Estructuras de Datos
 * Ing. Douglas Barrios
 * Creación: 18/05/2025
 * File Name: CreadorArchivo.java
 * Descripción: Clase auxiliar para crear el archivo de conexiones si no existe
 */

package com.hdt10;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CreadorArchivo {
    
    /**
     * Crea un archivo de conexiones de ejemplo si no existe
     * 
     * @param rutaArchivo Ruta donde se creará el archivo
     * @return true si se creó exitosamente o ya existía, false en caso contrario
     */
    public static boolean crearArchivoEjemplo(String rutaArchivo) {
        File archivo = new File(rutaArchivo);
        
        // Si el archivo ya existe, no hacer nada
        if (archivo.exists() && archivo.isFile()) {
            System.out.println("El archivo " + rutaArchivo + " ya existe.");
            return true;
        }
        
        // Asegurarse de que el directorio existe
        File directorio = archivo.getParentFile();
        if (directorio != null && !directorio.exists()) {
            boolean dirCreado = directorio.mkdirs();
            if (!dirCreado) {
                System.out.println("No se pudo crear el directorio: " + directorio.getAbsolutePath());
                
                // Intentar usar el directorio actual
                rutaArchivo = archivo.getName();
                archivo = new File(rutaArchivo);
                System.out.println("Intentando crear archivo en el directorio actual: " + archivo.getAbsolutePath());
            }
        }
        
        // Contenido de ejemplo
        String contenido = 
                "BuenosAires SaoPaulo 10 15 20 50\n"
                + "BuenosAires Lima 15 20 30 70\n"
                + "Lima Quito 10 12 15 20\n"
                + "Quito Bogota 8 10 14 25\n"
                + "Bogota Caracas 6 8 12 18\n"
                + "Caracas Miami 15 18 25 35\n"
                + "Miami LosAngeles 12 15 20 40\n"
                + "LosAngeles SanFrancisco 5 7 10 15\n"
                + "SanFrancisco Seattle 8 10 15 22\n"
                + "Seattle Vancouver 3 5 9 12\n"
                + "Vancouver Calgary 7 12 18 25\n"
                + "Calgary Toronto 15 18 28 35\n"
                + "Toronto Montreal 5 8 14 18\n"
                + "Montreal Chicago 10 12 20 30\n"
                + "Chicago Dallas 8 10 15 25\n"
                + "Dallas Houston 4 5 8 12\n"
                + "Houston Miami 10 12 18 30\n"
                + "Miami LaHabana 5 8 10 16\n"
                + "LaHabana CiudadDeMexico 12 15 20 35\n"
                + "CiudadDeMexico Monterrey 5 7 10 15\n"
                + "Monterrey Dallas 8 10 15 20\n";
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
            writer.write(contenido);
            System.out.println("Archivo de ejemplo creado exitosamente en: " + archivo.getAbsolutePath());
            return true;
        } catch (IOException e) {
            System.out.println("Error al crear el archivo: " + e.getMessage());
            return false;
        }
    }
}