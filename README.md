# HDT10 - Grafos
https://github.com/div468/HT10---Grafos.git

# 📊 Implementación del Algoritmos
## Algoritmo de Floyd
El algoritmo de Floyd en este sistema se implementa para encontrar las rutas más cortas entre todas las ciudades del grafo. La implementación:
1. Inicializa matrices de distancia y de caminos
2. Utiliza un enfoque de programación dinámica con tres bucles anidados para:
   - Evaluar todas las posibles rutas entre cada par de vértices
   - Actualizar la matriz de distancias cuando se encuentra un camino más corto
   - Mantener la matriz de predecesores para reconstruir las rutas
El algoritmo considera las condiciones climáticas actuales (normal, lluvia, nieve, tormenta) para calcular los tiempos de viaje entre ciudades.

## Algoritmo del Centro del Grafo
La implementación para encontrar el centro del grafo:
1. Calcula la excentricidad de cada ciudad (la distancia máxima a cualquier otra ciudad)
2. Identifica la ciudad con la excentricidad mínima

El proceso incluye:
   - Utilizar la matriz de distancias generada por el algoritmo de Floyd
   - Evaluar la distancia máxima desde cada ciudad a todas las demás
   - Seleccionar la ciudad que minimiza esta distancia máxima
El centro representa la ciudad más accesible desde cualquier punto del grafo, considerando las condiciones climáticas actuales.

## NetworkX
Para la implementación del algoritmo del centro del grafo y Floyd se trabajó con el módulo NetworkX, una biblioteca de Python utilizada para el estudio de grados y análisis de redes.

# 🛠️ Instalación y Ejecución en Java
1. Clonar el repositorio:
    ```bash
    git clone https://github.com/div468/HT10---Grafos.git
    cd cd HT10---Grafos
    ```

2. Compilar el programa:
    ```bash 
    cd demo
    javac -d out src/main/java/com/hdt10/*.java
    ```

3. Ejecutar el programa de Java:
    ```bash
    cd out
    java com.hdt10.Main
    ```
# 🐍 Instalación y Ejecución en Python
1. Instalar las dependencias necesarias
    '''bash
    pip 
    install networkx numpy
    '''

2. Ejecutar el programa de Python
    '''bash
    python Grafo.py
    '''
    
# 📚 Contenido de "conexiones.txt"
Este es el contenido de las conexiones establecidas por defecto.
```lisp
BuenosAires -----> SaoPaulo
BuenosAires -----> Lima
Lima ------------> Quito
Quito -----------> Bogota
Bogota ----------> Caracas
Caracas ---------> Miami
Miami -----------> LosAngeles
LosAngeles ------> SanFrancisco
SanFrancisco ----> Seattle
Seattle ---------> Vancouver
Vancouver -------> Calgary
Calgary ---------> Toronto
Toronto ---------> Montreal
Montreal --------> Chicago
Chicago ---------> Dallas
Dallas ----------> Houston
Houston ---------> Miami
Miami -----------> LaHabana
LaHabana --------> CiudadDeMexico
CiudadDeMexico --> Monterrey
Monterrey -------> Dallas
```

# Autores
👨‍💻 Marcelo Detlefsen
👨‍💻 Julián Divas