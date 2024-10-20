package backend.academy.maze.solver;

import backend.academy.maze.data.Cell;
import backend.academy.maze.data.Coordinate;
import backend.academy.maze.data.Maze;
import backend.academy.maze.data.Surface;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class BFSSolver implements Solver {

    private static final int[][] DIRECTIONS = {
        {-1, 0}, // вверх
        {1, 0},  // вниз
        {0, -1}, // влево
        {0, 1}   // вправо
    };

    @Override
    public List<Coordinate> solve(Maze maze, Coordinate start, Coordinate end) {
        Cell[][] grid = maze.getGrid();

        // Приоритетная очередь для взвешенного BFS (алгоритм Дейкстры)
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(node -> node.cost));

        // Карта для отслеживания минимальной стоимости достижения каждой клетки
        Map<Coordinate, Integer> costMap = new HashMap<>();
        Map<Coordinate, Coordinate> cameFrom = new HashMap<>(); // Храним путь

        // Начальная точка
        queue.add(new Node(start, 0)); // 0 - начальная стоимость
        costMap.put(start, 0);

        // Алгоритм поиска
        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();
            Coordinate current = currentNode.coordinate;

            // Если достигли конечной точки, восстанавливаем путь
            if (current.equals(end)) {
                return reconstructPath(cameFrom, start, end);
            }

            // Проверяем соседей (вверх, вниз, вправо, влево)
            for (int[] direction : DIRECTIONS) {
                // Чтоб чекстайл не ругался
                List<Integer> directionsList = Arrays.stream(direction)
                    .boxed()
                    .toList();
                Iterator<Integer> iterator = directionsList.iterator();
                int newRow = current.row() + iterator.next();
                int newCol = current.col() + iterator.next();

                if (isValidMove(maze, newRow, newCol)) {
                    Coordinate neighbor = new Coordinate(newRow, newCol);
                    int newCost
                        = currentNode.cost + grid[newRow][newCol].surface().getWeight(); // Добавляем вес поверхности

                    // Если новый путь до соседа дешевле или сосед ещё не был посещён
                    int currentCost = costMap.getOrDefault(neighbor, Integer.MAX_VALUE);
                    if (newCost < currentCost) {
                        costMap.put(neighbor, newCost);
                        queue.add(new Node(neighbor, newCost)); // Добавляем в очередь с новой стоимостью
                        cameFrom.put(neighbor, current); // Запоминаем путь
                    }
                }
            }
        }

        // Если путь не найден, возвращаем пустой список
        return List.of();
    }

    // Проверяем, можно ли сделать ход в указанную клетку
    private boolean isValidMove(Maze maze, int row, int col) {
        int height = maze.getHeight();
        int width = maze.getWidth();

        // Клетка должна быть в пределах лабиринта и не быть стеной
        return row >= 0 && row < height
            && col >= 0 && col < width
            && maze.getGrid()[row][col].surface() != Surface.WALL;
    }

    // Восстанавливаем путь от конечной до начальной точки
    private List<Coordinate> reconstructPath(Map<Coordinate, Coordinate> cameFrom, Coordinate start, Coordinate end) {
        List<Coordinate> path = new ArrayList<>(cameFrom.size());
        Coordinate current = end;

        while (!current.equals(start)) {
            path.add(current);
            current = cameFrom.get(current); // Переходим к предыдущей клетке
        }

        path.add(start); // Добавляем начальную точку
        Collections.reverse(path); // Переворачиваем путь, чтобы идти от начала к концу
        return path;
    }

    // Вспомогательный класс для узла с координатой и стоимостью
    private static class Node {
        Coordinate coordinate;
        int cost;

        Node(Coordinate coordinate, int cost) {
            this.coordinate = coordinate;
            this.cost = cost;
        }
    }
}
