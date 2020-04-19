# CrazyPutting

Latest changes:

- Updated to work with Java 11 (openjdk version "11.0.6" 2020-01-14).
- Gradle updated from 2.14.1 to 5.4.1 (Latest compatible gradle with libgdx).
- LibGDX updated to 1.9.10.
- References to JavaFX erased.
- Configuration file for VSCode (1.44) added.
- Configuration file for Intellij (2020.1) added.
- Minor changes in folder structure. 
- README.md updated.

## Requirements

- Java 11.

## Run Executable

Run `Mini Golf Executable 2020.4.19.jar` 

## Intellij & VSCode
Run DesktopLauncher.main file to launch the program

To build a .jar file use  `gradle desktop:dist`. The .jar will be located at *../desktop/build/libs*.  

A .jar (Mini Golf Executable 2020.4.19) is provided in the root of this repository for accessibility. 

## Features
 
- 3D Real Time Map Editor
    - 3D Spline Editor 
    - Add and remove obstacles
    - Custom Raycasting
- Custom built physics
- Three AI solvers
    - Basic Genetic Algorithm.
    - Genetic Algorithm with path finding.
    - Dantzig's simplex algorithm.

## Images


![Realtime 3D Splines map editor](https://raw.githubusercontent.com/Noixas/CrazyPutting/master/gifs/mini-golf-splines.gif)
![Obstacles and physics example](https://raw.githubusercontent.com/Noixas/CrazyPutting/master/gifs/mini-golf-obstacles.gif)


