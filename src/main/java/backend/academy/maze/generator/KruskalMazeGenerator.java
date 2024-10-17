package backend.academy.maze.generator;

import backend.academy.maze.data.Cell;
import backend.academy.maze.data.Maze;
import backend.academy.maze.data.Surface;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class KruskalMazeGenerator implements Generator {

    private static final int PASSAGE_WEIGHT = 95;
    private static final int SAND_WEIGHT = 5;
    private static final int SWAMP_WEIGHT = 5;
    private static final int COIN_WEIGHT = 15;

    private final int height;
    private final int width;
    private final Cell[][] grid;
    private final SecureRandom random = new SecureRandom();
    private final UnionFind unionFind;
    private final List<Cell> walls;

    public KruskalMazeGenerator(int height, int width) {
        this.height = height;
        this.width = width;
        this.grid = new Cell[height][width];
        this.unionFind = new UnionFind(height * width);  // Используем для объединения ячеек в группы
        this.walls = new ArrayList<>();
        initializeMaze(); // Инициализируем лабиринт
    }

    @Override
    public Maze generate() {
        openEveryOtherCell(); // Открываем каждую вторую клетку
        while (unionFind.getNumberOfSets() > 1) {
            iterateKruskal(); // Выполняем итерации алгоритма Краскала
        }
        return new Maze(height, width, grid);
    }

    private void initializeMaze() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                grid[row][col] = new Cell(row, col, Surface.WALL); // Каждая ячейка сначала является стеной
                if (row % 2 == 0 || col % 2 == 0) {
                    walls.add(grid[row][col]); // Добавляем стены в список
                }
            }
        }
    }

    private void openEveryOtherCell() {

        for (int row = 1; row < height; row += 2) {
            for (int col = 1; col < width; col += 2) {
                grid[row][col] = new Cell(row, col, getRandomNonWallSurface());  // Открываем клетку
                int index = row * width + col;
                unionFind.createSet(index); // Каждая открытая клетка становится отдельным множеством
                unionFind.incrementSetCount(); // Увеличиваем количество групп

            }
        }
    }

    private void iterateKruskal() {
        Cell wall = getRandomClosedCell(); // Получаем случайную стену
        List<Cell> surroundingCells = getSurroundingOpenCells(wall); // Получаем окружающие открытые клетки

        if (surroundingCells.size() == 2) {
            Cell cell1 = surroundingCells.get(0);
            Cell cell2 = surroundingCells.get(1);

            int cell1Index = cell1.row() * width + cell1.col();
            int cell2Index = cell2.row() * width + cell2.col();

            // Проверяем, принадлежат ли клетки к разным группам (множествам)
            if (!unionFind.connected(cell1Index, cell2Index)) {
                // Если клетки в разных группах, открываем стену
                grid[wall.row()][wall.col()] = new Cell(wall.row(), wall.col(), getRandomNonWallSurface());
                // Объединяем группы и новый проход в одну группу
                int wallIndex = wall.row() * width + wall.col();
                unionFind.union(cell1Index, wallIndex);
                unionFind.union(cell1Index, cell2Index);
                unionFind.decrementSetCount();
                walls.remove(grid[wall.row()][wall.col()]);
            }
        }
    }

    private Cell getRandomClosedCell() {
        int randomIndex = random.nextInt(walls.size()); // Выбираем случайную закрытую стену
        return walls.get(randomIndex);
    }

    private List<Cell> getSurroundingOpenCells(Cell wall) {
        List<Cell> openCells = new ArrayList<>();
        int row = wall.row();
        int col = wall.col();

        if (row > 1 && grid[row - 1][col].surface() != Surface.WALL && row < height - 1
            && grid[row + 1][col].surface() != Surface.WALL) {
            openCells.add(grid[row - 1][col]);
            openCells.add(grid[row + 1][col]);
            return openCells;
        }
        if (col > 1 && grid[row][col - 1].surface() != Surface.WALL && col < width - 1
            && grid[row][col + 1].surface() != Surface.WALL) {
            openCells.add(grid[row][col - 1]);
            openCells.add(grid[row][col + 1]);
        }

        return openCells;
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

    private static class UnionFind {
        private final int[] parent;
        private final int[] rank;
        private int numberOfSets;

        UnionFind(int size) {
            parent = new int[size];
            rank = new int[size];
            numberOfSets = 0;
            for (int i = 0; i < size; i++) {
                parent[i] = i;
                rank[i] = 0;
            }
        }

        // Находим корневого родителя
        public int find(int p) {
            if (p != parent[p]) {
                parent[p] = find(parent[p]);
            }
            return parent[p];
        }

        // Объединяем два множества
        public void union(int p, int q) {
            int rootP = find(p);
            int rootQ = find(q);
            if (rootP != rootQ) {
                if (rank[rootP] > rank[rootQ]) {
                    parent[rootQ] = rootP;
                    //rank[rootP]++;
                } else if (rank[rootP] < rank[rootQ]) {
                    parent[rootP] = rootQ;
                    //rank[rootQ]++;
                } else {
                    parent[rootQ] = rootP;
                    rank[rootP]++;
                }
            }
        }

        // Проверка, принадлежат ли два элемента одному множеству
        public boolean connected(int p, int q) {
            return find(p) == find(q);
        }

        // Создание отдельного множества
        public void createSet(int index) {
            parent[index] = index;
        }

        public void incrementSetCount() {
            numberOfSets++;
        }

        public void decrementSetCount() {
            numberOfSets--;
        }

        public int getNumberOfSets() {
            return numberOfSets;
        }
    }
}
