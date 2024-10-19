package backend.academy.maze.controller;

import backend.academy.maze.corruptor.MazeCorruptor;
import backend.academy.maze.data.Coordinate;
import backend.academy.maze.data.Maze;
import backend.academy.maze.generator.KruskalMazeGenerator;
import backend.academy.maze.generator.PrimMazeGenerator;
import backend.academy.maze.solver.AStarSolver;
import backend.academy.maze.solver.BFSSolver;
import java.util.List;

public class MazeManager {
    public Maze generateMaze(int width, int height, int algorithmChoice) {
        Maze maze;
        if (algorithmChoice == 1) {
            maze = new PrimMazeGenerator(height, width).generate();  // Реализация алгоритма Прима
        } else {
            maze = new KruskalMazeGenerator(height, width).generate();  // Реализация алгоритма Краскала
        }
        return maze;
    }

    // Решение лабиринта
    public List<Coordinate> solveMaze(Maze maze, Coordinate start, Coordinate end, int algorithmChoice) {
        if (algorithmChoice == 1) {
            return new BFSSolver().solve(maze, start, end);  // Реализация BFS
        } else {
            return new AStarSolver().solve(maze, start, end);  // Реализация A*
        }
    }

    // Выбор испорченного лабиринта
    public Maze promptForCorruptedMaze(Maze maze, int corruptionChoice) {
        if (corruptionChoice == 1) {
            return new MazeCorruptor().corruptMaze(maze);
        }
        return maze;
    }
}
