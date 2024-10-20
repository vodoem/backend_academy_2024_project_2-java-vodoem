package backend.academy.maze.solver;

import backend.academy.maze.data.Coordinate;
import backend.academy.maze.data.Maze;
import backend.academy.maze.data.Surface;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class MazeUtils {
    private MazeUtils() {
    }

    // Проверяем, можно ли сделать ход в указанную клетку
    public static boolean isValidMove(Maze maze, int row, int col) {
        int height = maze.getHeight();
        int width = maze.getWidth();

        // Клетка должна быть в пределах лабиринта и не быть стеной
        return row >= 0 && row < height
            && col >= 0 && col < width
            && maze.getGrid()[row][col].surface() != Surface.WALL;
    }

    // Новый метод для обновления координат
    public static Coordinate updateCoordinates(Coordinate current, int[] direction) {
        List<Integer> directionsList = Arrays.stream(direction)
            .boxed()
            .toList();
        Iterator<Integer> iterator = directionsList.iterator();
        int newRow = current.row() + iterator.next();
        int newCol = current.col() + iterator.next();

        return new Coordinate(newRow, newCol);
    }
}
