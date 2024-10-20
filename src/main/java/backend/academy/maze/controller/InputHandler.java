package backend.academy.maze.controller;

import backend.academy.maze.data.Coordinate;
import backend.academy.maze.data.Maze;
import backend.academy.maze.data.Surface;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class InputHandler {
    private final Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
    private final PrintStream printStream = System.out;

    // Ввод размеров лабиринта
    public int[] getMazeDimensions() {

        printStream.println(
            "Введите размер лабиринта. Для более красивого результата введите нечетные значения. (ширина высота):");
        int width = scanner.nextInt();
        int height = scanner.nextInt();
        if (width <= 0 || height <= 0) {
            printStream.println("Неверный размер. Повторите ввод.");
            return getMazeDimensions();
        }
        return new int[] {width, height};
    }

    // Ввод начальной точки
    public Coordinate getStartPoint(int width, int height, Maze maze) {
        printStream.println("Введите начальную точку. Пожалуйста, не вводите координаты стен (строка, колонка):");
        int row = scanner.nextInt();
        int col = scanner.nextInt();
        if (row >= height || col >= width || row < 0 || col < 0 || maze.getGrid()[row][col].surface() == Surface.WALL) {
            printStream.println("Неверная начальная точка. Повторите ввод.");
            return getStartPoint(width, height, maze);
        }
        return new Coordinate(row, col);
    }

    // Ввод конечной точки
    public Coordinate getEndPoint(int width, int height, Maze maze) {
        printStream.println("Введите конечную точку. Пожалуйста, не вводите координаты стен (строка, колонка):");
        int row = scanner.nextInt();
        int col = scanner.nextInt();
        if (row >= height || col >= width || row < 0 || col < 0 || maze.getGrid()[row][col].surface() == Surface.WALL) {
            printStream.println("Неверная конечная точка. Повторите ввод.");
            return getEndPoint(width, height, maze);
        }
        return new Coordinate(row, col);
    }

    // Выбор алгоритма генерации
    public int getGenerationAlgorithm() {
        printStream.println("Выберите алгоритм генерации:");
        printStream.println("1 - Алгоритм Прима");
        printStream.println("2 - Алгоритм Краскала");
        return scanner.nextInt();
    }

    // Выбор алгоритма поиска пути
    public int getSolverAlgorithm() {
        printStream.println("Выберите алгоритм поиска пути:");
        printStream.println("1 - Поиск в ширину (BFS)");
        printStream.println("2 - A-star (A*)");
        return scanner.nextInt();
    }

    // Выбор правильности лабиринта
    public int getCorruptionChoice() {
        printStream.println("Вы хотите сделать лабиринт не идеальным? Так вы сможете"
            + " более наглядно увидеть работу алгоритма нахождения пути в лабиринте:");
        printStream.println("1 - Да");
        printStream.println("2 - Нет");
        return scanner.nextInt();
    }
}
