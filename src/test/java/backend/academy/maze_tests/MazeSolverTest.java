package backend.academy.maze_tests;

import backend.academy.maze.data.Cell;
import backend.academy.maze.data.Coordinate;
import backend.academy.maze.data.Maze;
import backend.academy.maze.data.Surface;
import backend.academy.maze.solver.AStarSolver;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MazeSolverTest {
    @Test
    public void testSolveKnownMaze() {
        // Arrange
        Maze maze = createKnownMaze();
        AStarSolver as = new AStarSolver();
        Coordinate start = new Coordinate(0, 0);
        Coordinate end = new Coordinate(4, 4);
        List<Coordinate> expectedPath = List.of(
            new Coordinate(0, 0),
            new Coordinate(1, 0),
            new Coordinate(2, 0),
            new Coordinate(3, 0),
            new Coordinate(4, 0),
            new Coordinate(4, 1),
            new Coordinate(4, 2),
            new Coordinate(4, 3),
            new Coordinate(4, 4)
        );

        // Act
        List<Coordinate> path = as.solve(maze, start, end);

        // Assert
        assertThat(path).isEqualTo(expectedPath);
    }

    private Maze createKnownMaze() {
        // Создание заранее известного лабиринта
        Cell[][] grid = {
            {new Cell(0, 0, Surface.PASSAGE), new Cell(0, 1, Surface.WALL), new Cell(0, 2, Surface.PASSAGE),
                new Cell(0, 3, Surface.PASSAGE), new Cell(0, 4, Surface.PASSAGE)},
            {new Cell(1, 0, Surface.PASSAGE), new Cell(1, 1, Surface.WALL), new Cell(1, 2, Surface.PASSAGE),
                new Cell(1, 3, Surface.WALL), new Cell(1, 4, Surface.PASSAGE)},
            {new Cell(2, 0, Surface.PASSAGE), new Cell(2, 1, Surface.WALL), new Cell(2, 2, Surface.PASSAGE),
                new Cell(2, 3, Surface.WALL), new Cell(2, 4, Surface.PASSAGE)},
            {new Cell(3, 0, Surface.PASSAGE), new Cell(3, 1, Surface.WALL), new Cell(3, 2, Surface.PASSAGE),
                new Cell(3, 3, Surface.WALL), new Cell(3, 4, Surface.PASSAGE)},
            {new Cell(4, 0, Surface.PASSAGE), new Cell(4, 1, Surface.PASSAGE), new Cell(4, 2, Surface.PASSAGE),
                new Cell(4, 3, Surface.PASSAGE), new Cell(4, 4, Surface.PASSAGE)}
        };
        return new Maze(5, 5, grid);
    }
}
