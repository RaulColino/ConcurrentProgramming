# ConcurrentProgramming

Concurrent Programming exercises
## Final project: Simulation of a _Formula One_ race

### Clase:

#### •	Main

Crea las instancias de las clases con los distintos parámetros de la carrera (número de coches, número de tramos, número de carriles del circuito, numero de vueltas y eventos climáticos). Crea los hilos correspondientes: hilo de la carrera, hilo del juez e hilo de entrada por teclado. Por último, pone en marcha los hilos y espera su finalización. Los hilos del juez y de la carrera deben terminar siempre, tanto si se interrumpe el proceso de la carrera como si no.

#### •	RaceJudge

Espera a que se creen y se lancen las tareas de cada vehículo al ScheduledThreadPool. 
Una vez hecho lo anterior se inicia una cuenta atrás tras la cual todos los coches empiezan a correr. Tras esto, espera a la clasificación y finalización de los coches. Esta lógica esta implementada con CountDownLatch;

#### •	Race

Tras crear e inicializar los vehículos y tramos del circuito, esta clase, mediante un ScheduledThreadPool, lanza tareas programadas cada 5s para cada vehículo, una tarea para cada impresión del ranking cada 4s y un evento climático en el instante indicado en el programa principal (si es de lluvia o lluvia torrencial).  Después de ello espera a que los vehículos terminen la carrera antes de hacer shutdown() al ejecutor.



#### •	Score

Muestra las posiciones de los vehículos. Por cada vehículo se imprime su posición, su nombre, si ha adelantado o ha sido adelantado (representado por un triángulo hacia arriba ▲ y triangulo hacia abajo ▼ respectivamente), vuelta por la que va, distancia recorrida, distancia que le queda para terminar la carrera (exceptuando la vuelta extra) o si ya paso la meta, se indica la distancia restante de la vuelta extra. También se indica la velocidad, tramo por el que va, combustible y si está en boxes.

Para mostrar los resultados se utiliza un stream creado a partir de una copia estática de la colección de vehículos de la clase Race. En el stream ordenamos los vehículos (en función de su posición en la carrera) de tal manera que los vehículos que van por delante en la carrera son los primeros en ser procesados. El stream es secuencial y no paralelo ya que con tan pocos datos a procesar no resulta eficiente.
 

#### •	Vehicle

Cada vehículo tiene una velocidad en recta y una velocidad en curva, un valor máximo de combustible y un nombre fijos. A lo largo de la carrera cambia su distancia recorrida, su combustible, su velocidad, el tramo en el que esta y su espera restante en boxes (0 si no está en boxes).
Antes de correr, los vehículos esperan a la señal del juez. Una vez empezada la carrera los vehículos se mueven “por turnos” y deciden si pueden avanzar (dependiendo de si tienen obstruido el paso por vehículos de delante o no), si tienen que ir a boxes o si ya han terminado la carrera. Las posiciones finales se obtienen a partir de un CountDownLatch del juez.
Los vehículos utilizan un método sincronizado de la clase Race al avanzar de tal manera que cuando un vehículo decide si puede avanzar x unidades de distancia o no, los demás vehículos no se mueven, evitando así posibles errores.

#### •	Racetrack

Los tramos tienen una longitud (x unidades de distancia) y un tipo (0 recta, 1 curva).

#### •	ClimaticEvents

Tienen un tipo (0 soleado, 1 lluvia, 2 lluvia torrencial) y la instancia de la clase Race que nos permite llamar el método rainEvent() que simplemente obliga a cada vehiculo a parar en boxes para cambiar sus neumáticos. Cuando se produce una lluvia torrencial se llama al método interrumpirCarrera() del Main para que se interrumpa la carrera de forma ordenada.



Raúl Colino Singh – Programación Concurrente 2019-2020

