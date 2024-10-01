package backend.academy.maze.solver;

import backend.academy.maze.data.Coordinate;
import backend.academy.maze.data.Maze;

import java.util.List;

public interface Solver {
    List<Coordinate> solve(Maze maze, Coordinate start, Coordinate end);
}
