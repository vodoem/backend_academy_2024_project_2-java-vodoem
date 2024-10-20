package backend.academy.maze_tests;

import backend.academy.maze.controller.InputHandler;
import backend.academy.maze.data.Cell;
import backend.academy.maze.data.Coordinate;
import backend.academy.maze.data.Maze;
import backend.academy.maze.data.Surface;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class InputHandlerTest {
    @Test
    public void testInvalidMazeDimensions() {
        //Arrange
        // Мокируем ввод: сначала вводим неправильные значения, затем корректные
        String input = "-1 10\n5 5\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        System.setIn(in);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        InputHandler inputHandler = new InputHandler();

        int[] expectedDimensions = new int[]{5, 5};

        // Act
        // Проверяем, что после ввода некорректных данных программа повторяет запрос и возвращает корректные данные
        int[] result = inputHandler.getMazeDimensions();
        assertArrayEquals(expectedDimensions, result);

        // Assert
        String output = out.toString();
        assert (output.contains("Неверный размер. Повторите ввод."));

    }

    @Test
    public void testInvalidStartPoint() {
        // Arrange
        // Вводим 2 раза неверную стартовую точку, а на 3 раз верную
        String input = "10 10\n0 0\n1 1\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        System.setIn(in);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        Maze maze = createMockMaze(5, 5);
        InputHandler inputHandler = new InputHandler();
        Coordinate expected = new Coordinate(1, 1);

        // Act
        // Проверяем, что после ввода некорректных данных программа повторяет запрос и возвращает корректные данные
        Coordinate result = inputHandler.getStartPoint(5, 5, maze);
        assertEquals(expected, result);

        // Assert
        String output = out.toString();
        assert (output.contains("Неверная начальная точка. Повторите ввод."));
    }

    @Test
    public void testInvalidEndPoint(){
        // Arrange
        String input = "10 10\n0 0\n1 1\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        System.setIn(in);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        Maze maze = createMockMaze(5, 5);
        InputHandler inputHandler = new InputHandler();
        Coordinate expected = new Coordinate(1, 1);
        // Act
        Coordinate result = inputHandler.getEndPoint(5, 5, maze);
        assertEquals(result, expected);
        // Assert
        String output = out.toString();
        assert (output.contains("Неверная конечная точка. Повторите ввод."));

    }

    private Maze createMockMaze(int width, int height) {
        Cell[][] grid = new Cell[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                grid[i][j] = new Cell(i, j, Surface.PASSAGE); // Все клетки - проходы
            }
        }
        grid[0][0] = new Cell(0, 0, Surface.WALL); // Стена в точке (0, 0)
        return new Maze(height, width, grid);
    }
}
