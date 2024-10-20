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

public class AStarSolver implements Solver {
    private static final int[][] DIRECTIONS = {
        {-1, 0},  // вверх
        {1, 0},   // вниз
        {0, -1},  // влево
        {0, 1}    // вправо
    };

    @Override
    public List<Coordinate> solve(Maze maze, Coordinate start, Coordinate end) {
        Cell[][] grid = maze.getGrid();

        // Приоритетная очередь для узлов, сортируем по f(n) = g(n) + h(n)
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(node -> node.f));

        // Карта для отслеживания минимальной стоимости достижения каждой клетки
        Map<Coordinate, Double> gScore = new HashMap<>();
        Map<Coordinate, Coordinate> cameFrom = new HashMap<>(); // Храним путь

        gScore.put(start, 0.0); // Начальная точка
        priorityQueue.add(new Node(start, heuristic(start, end)));

        // Пока есть узлы для обработки
        while (!priorityQueue.isEmpty()) {
            Node currentNode = priorityQueue.poll();
            Coordinate current = currentNode.coordinate;

            // Если достигли цели, восстанавливаем путь
            if (current.equals(end)) {
                return reconstructPath(cameFrom, start, end);
            }

            // Проверяем соседей (вверх, вниз, влево, вправо)
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

                    // Стоимость перехода в соседа
                    double tentativeGScore = gScore.get(current) + grid[newRow][newCol].surface().getWeight();

                    // Если путь через текущую клетку дешевле, обновляем путь
                    if (tentativeGScore < gScore.getOrDefault(neighbor, Double.MAX_VALUE)) {
                        cameFrom.put(neighbor, current);
                        gScore.put(neighbor, tentativeGScore);

                        double fScore = tentativeGScore + heuristic(neighbor, end); // f(n) = g(n) + h(n)
                        priorityQueue.add(new Node(neighbor, fScore));
                    }
                }
            }
        }

        // Если путь не найден, возвращаем пустой список
        return List.of();
    }

    // Эвристика для A* (Манхэттенское расстояние)
    private double heuristic(Coordinate a, Coordinate b) {
        return Math.abs(a.row() - b.row()) + Math.abs(a.col() - b.col());
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

    // Вспомогательный класс для узла
    private static class Node {
        Coordinate coordinate;
        double f; // f(n) = g(n) + h(n)

        Node(Coordinate coordinate, double f) {
            this.coordinate = coordinate;
            this.f = f;
        }

    }
}
