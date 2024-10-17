package backend.academy.maze.corruptor;

import backend.academy.maze.data.Cell;
import backend.academy.maze.data.Maze;
import backend.academy.maze.data.Surface;
import java.security.SecureRandom;

public class MazeCorruptor {
    private final static int PROBABILITY_SCALE = 100;
    private final static int CORRUPTION_RATE = 20;

    private static final int PASSAGE_WEIGHT = 95;
    private static final int SAND_WEIGHT = 2;
    private static final int SWAMP_WEIGHT = 1;
    private static final int COIN_WEIGHT = 2;

    private final SecureRandom random = new SecureRandom();

    // Метод для порчи лабиринта, удаляя случайные стены
    public Maze corruptMaze(Maze maze) {
        int height = maze.getHeight();
        int width = maze.getWidth();
        Cell[][] grid = maze.getGrid();

        // Проходим по каждой клетке и удаляем случайные стены с вероятностью corruptionRate
        for (int row = 1; row < height - 1; row++) {
            for (int col = 1; col < width - 1; col++) {
                // Проверяем, является ли клетка стеной
                if (grid[row][col].surface() == Surface.WALL) {
                    // Удаляем стену с заданной вероятностью
                    if (random.nextInt(PROBABILITY_SCALE) < CORRUPTION_RATE) {
                        grid[row][col] = new Cell(row, col, getRandomNonWallSurface());
                    }
                }
            }
        }
        return new Maze(height, width, grid);
    }

    private Surface getRandomNonWallSurface() {
        int totalWeight = PASSAGE_WEIGHT + SAND_WEIGHT + SWAMP_WEIGHT + COIN_WEIGHT;
        int randomNum = random.nextInt(totalWeight);
        if (randomNum < PASSAGE_WEIGHT) {
            return Surface.PASSAGE;  // Проход появляется чаще
        } else if (randomNum < PASSAGE_WEIGHT + SAND_WEIGHT) {
            return Surface.SAND;
        } else if (randomNum < PASSAGE_WEIGHT + SAND_WEIGHT + SWAMP_WEIGHT) {
            return Surface.SWAMP;
        } else {
            return Surface.COIN;
        }
    }
}
