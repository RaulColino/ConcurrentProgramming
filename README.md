# Concurrent programming

Concurrent programming exercises and final project.
## Final project: _Formula 1_ race simulator
### Description
The simulation's logic would be as follows: a race circuit is generated from separate parts. Each part has N units of distance (1 unit of distance > vehicle length). Then, each vehicle with its technical specifications is created. The race judge checks that everyone on the starting grid is ready, and if it's all OK, he gives the starting signal. The vehicles are then invoked every 5 seconds, making it's moves concurrently.

Vehicles will have to decide whether or not to move to the next unit of distance or, in the case of an overtaking, whether to advance one unit further than their opponent. The control panel and the competitors are provided with "real-time" data such as the current location, the meters to the finish line, distance between cars, etc. Naturally, the advancement of each segment would require the vehicles to use fuel, having to go to the pits to refuel when they are in reserve.

The race finishes when all the vehicles cross the finish line, make an extra lap and end in the pits. The race judge will be responsible for ensuring that all of the above situations has been reached.

### Program classes

#### • Main

Creates the instances of the classes with the different parameters of the race (number of cars, number of racetracks, number of lanes of the raceway, number of laps, and weather events). Creates the corresponding threads: race thread, judge thread, and keyboard input thread. Finally, starts the threads and waits for them to complete. The threads of the judge and the race must always end, whether or not the process of the race is interrupted.

#### • RaceJudge

Waits for each vehicle's tasks to be created and started in ScheduledThreadPool.
Once this is done, a countdown begins, after which all the cars start to run. After this, it waits for the ranking and completion of the cars. This logic is implemented with CountDownLatch.

#### • Race

After creating and initializing vehicles and racetracks, this class, using a scheduled thread, starts scheduled tasks every 5 seconds for each vehicle, a task for each printout of the ranking every 4 seconds and a weather event at the time indicated in the main program (if it is rain or pouring rain). After that, it waits for the vehicles to finish the race before turning off the executor.



#### • Score

Shows the positions of the vehicles. For each vehicle the program prints its position, its name, whether it has overtaken someone or has been overtaken (shown with a triangle up ▲ and a triangle down ▼ respectively), lap, distance traveled, distance left to finish the race (except when the extra lap) or if the finish line has already passed, the remaining distance of the extra lap is indicated. It also indicates the speed, the racetrack that is going through, the fuel, and whether it's in the pit.

To show the results, a stream created from a static copy of the Race class vehicle collection is used. In the stream, we order the vehicles (according to their position in the race) in such a way that the vehicles that go ahead in the race are the first to be processed. The stream is sequential and not parallel since with so little data to process it is not efficient.


#### • Vehicle

Each vehicle has straight track speed and curved track speed, a fixed maximum fuel value and a name. Throughout the race, the distance traveled, fuel, speed, the racetrack that is going through and the remaining time in the pits change (0 if it's not in the pits).
Before running, the vehicles wait for the judge's signal. Once the race has started, the vehicles move “in turns” and decide if they can advance (depending on whether they have the passage obstructed by vehicles or not), if they have to go to the pits or if they have already finished the race. Final positions are obtained from a CountDownLatch of the judge.
Vehicles use a synchronized method of the Race class when advancing in such a way that when a vehicle decides whether it can advance x units of distance or not, the other vehicles do not move, thus avoiding possible errors.

#### • Racetrack

The sections have a length (x distance units) and a type (0 lines, 1 curve).

#### • ClimaticEvents

They have a type (0 sunny, 1 rain, 2 torrential rain) and the instance of the Race class that allows it to call the rainEvent() method that simply forces each vehicle to stop at the pit to change its tires. When pouring rain occurs, the _interrumpirCarrera()_ Main method is called to interrupt the race in an appropriate manner.

## License

This project is licensed under the MIT License.




