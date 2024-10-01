package backend.academy.maze.renderer;

import backend.academy.maze.data.Cell;
import backend.academy.maze.data.Coordinate;
import backend.academy.maze.data.Maze;
import backend.academy.maze.data.Surface;
import java.util.List;

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
        // Отображение пути символами '*'
        //return sb.toString();
        return "";
    }

    private char renderSurface(Surface surface) {
        return switch (surface) {
            case WALL -> '█';       // Стена
            case PASSAGE -> ' ';    // Проход
            case SWAMP -> '▒';      // Болото
            case SAND -> '░';       // Песок
            case COIN -> '✪';       // Монетка
        };
    }

}
