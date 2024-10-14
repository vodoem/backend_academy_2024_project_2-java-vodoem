package backend.academy.maze.generator;

import backend.academy.maze.data.Cell;
import backend.academy.maze.data.Maze;
import backend.academy.maze.data.Surface;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PrimMazeGenerator implements Generator {
    private static final int PASSAGE_WEIGHT = 95;
    private static final int SAND_WEIGHT = 2;
    private static final int SWAMP_WEIGHT = 1;
    private static final int COIN_WEIGHT = 2;

    private final Random random;
    private final int height;
    private final int width;

    public PrimMazeGenerator(int height, int width) {
        this.height = height;
        this.width = width;
        random = new SecureRandom();
    }

    @Override
    public Maze generate() {
        Cell[][] grid = initializeGrid(height, width);
        int startRow = random.nextInt(height);
        int startCol = random.nextInt(width);
        Cell startCell = grid[startRow][startCol];
        grid[startRow][startCol] = new Cell(startCell.row(), startCell.col(), Surface.PASSAGE);

        List<Cell> borders = new ArrayList<>();
        addBorderCells(grid, startCell, borders);

        while (!borders.isEmpty()) {
            int index = random.nextInt(borders.size());
            Cell cell = borders.get(index);
            if (cell.surface() == Surface.WALL) {
                grid[cell.row()][cell.col()] = new Cell(cell.row(), cell.col(), getRandomNonWallSurface());
                borders.remove(index);

                createPassageInDirection(grid, cell);
                addBorderCells(grid, cell, borders);
            }

        }
        return new Maze(height, width, grid);
    }

    // Очистка прохода к случайной соседней клетке в одном из направлений (север, юг, восток, запад),
    // если соседняя клетка на расстоянии двух клеток доступна.
    private void createPassageInDirection(Cell[][] grid, Cell cell) {
        enum Direction {
            NORTH, SOUTH, EAST, WEST
        }
        // Список доступных направлений

        List<Direction> directions = new ArrayList<>(List.of(Direction.values()));
        int row = cell.row();
        int col = cell.col();
        // Пока есть направления для проверки
        while (!directions.isEmpty()) {

            int dirIndex = random.nextInt(directions.size());
            Direction direction = directions.get(dirIndex);
            directions.remove(dirIndex);
            switch (direction) {
                case NORTH:
                    if (col - 2 >= 0 && grid[row][col - 2].surface() != Surface.WALL) {
                        grid[row][col - 1] = new Cell(row, col - 1, getRandomNonWallSurface());
                        directions.clear();
                    }
                    break;
                case SOUTH:
                    if (col + 2 < grid.length && grid[row][col + 2].surface() != Surface.WALL) {
                        grid[row][col + 1] = new Cell(row, col + 1, getRandomNonWallSurface());
                        directions.clear();
                    }
                    break;
                case EAST:
                    if (row - 2 >= 0 && grid[row - 2][col].surface() != Surface.WALL) {
                        grid[row - 1][col] = new Cell(row - 1, col, getRandomNonWallSurface());
                        directions.clear();
                    }
                    break;
                case WEST:
                    if (row + 2 < grid[0].length && grid[row + 2][col].surface() != Surface.WALL) {
                        grid[row + 1][col] = new Cell(row + 1, col, getRandomNonWallSurface());
                        directions.clear();
                    }
                    break;
                default:
                    break;
            }

        }
    }

    // Добавление ячеек-стен, расположенных на расстоянии двух ортогональных пробелов от выбранной ячейки
    private void addBorderCells(Cell[][] grid, Cell cell, List<Cell> borders) {
        int row = cell.row();
        int col = cell.col();

        if (col - 2 >= 0 && grid[row][col - 2].surface() == Surface.WALL) {
            borders.add(grid[row][col - 2]);
        }
        if (col + 2 < grid.length && grid[row][col + 2].surface() == Surface.WALL) {
            borders.add(grid[row][col + 2]);
        }
        if (row - 2 >= 0 && grid[row - 2][col].surface() == Surface.WALL) {
            borders.add(grid[row - 2][col]);
        }
        if (row + 2 < grid[0].length && grid[row + 2][col].surface() == Surface.WALL) {
            borders.add(grid[row + 2][col]);
        }
    }

    // Инициализация grid из всех стен
    private Cell[][] initializeGrid(int height, int width) {
        Cell[][] grid = new Cell[height][width];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                grid[row][col] = new Cell(row, col, Surface.WALL);  // Все клетки изначально стены
            }
        }
        return grid;
    }

    // Метод для задания случайной поверхности (кроме стены)
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
