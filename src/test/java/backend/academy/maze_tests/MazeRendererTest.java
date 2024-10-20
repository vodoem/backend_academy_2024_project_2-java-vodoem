package backend.academy.maze_tests;

import backend.academy.maze.data.Cell;
import backend.academy.maze.data.Maze;
import backend.academy.maze.data.Surface;
import backend.academy.maze.renderer.ConsoleRenderer;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MazeRendererTest {
    @Test
    public void testRenderMaze() {
        //Arrange
        Maze maze = createSimpleMaze();
        ConsoleRenderer cr = new ConsoleRenderer();
        String expectedMaze =
            """
                ⬛⬜⬛⬛⬛
                ⬛⬜⬛⬜⬛
                ⬛⬜⬛⬜⬛
                ⬛⬜⬛⬜⬛
                ⬛⬛⬛⬛⬛
                """;
        //Act
        String rendererMaze = cr.render(maze);

        //Assert
        assertThat(rendererMaze).isEqualTo(expectedMaze);
    }

    private Maze createSimpleMaze() {
        // Простой лабиринт с проходами и стенами
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
