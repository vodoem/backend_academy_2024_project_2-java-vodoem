package backend.academy.maze.renderer;

import backend.academy.maze.data.Cell;
import backend.academy.maze.data.Coordinate;
import backend.academy.maze.data.Maze;
import backend.academy.maze.data.Surface;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConsoleRenderer implements Renderer {
    @Override
    public String render(Maze maze) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < maze.getHeight(); i++) {
            for (int j = 0; j < maze.getWidth(); j++) {
                Cell cell = maze.getCell(i, j);
                sb.append(renderSurface(cell.surface()));
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    @Override
    public String render(Maze maze, List<Coordinate> path) {
        int height = maze.getHeight();
        int width = maze.getWidth();
        Set<Coordinate> pathSet = new HashSet<>(path);

        StringBuilder sb = new StringBuilder();

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Cell cell = maze.getCell(row, col);
                Coordinate current = new Coordinate(row, col);
                if (pathSet.contains(current)) {
                    // Если клетка является частью пути, отображаем её символом '*'
                    sb.append("\uD83D\uDFE5");
                } else {
                    // Иначе отображаем клетку в зависимости от её поверхности
                    sb.append(renderSurface(cell.surface()));
                }
            }
            sb.append('\n'); // Переход на новую строку после каждой строки лабиринта
        }

        return sb.toString();
    }

    private String renderSurface(Surface surface) {
        return switch (surface) {
            case WALL -> "⬜"; // Стена
            case PASSAGE -> "⬛"; // Проход
            case SWAMP -> "\uD83D\uDFEB"; // Болото
            case SAND -> "\uD83D\uDFE8"; // Песок
            case COIN -> "\uD83E\uDE99"; // Монетка
        };
    }

}
