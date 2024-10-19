package backend.academy.maze;

import backend.academy.maze.controller.InputHandler;
import backend.academy.maze.controller.MazeManager;
import backend.academy.maze.data.Coordinate;
import backend.academy.maze.data.Maze;
import backend.academy.maze.renderer.ConsoleRenderer;
import java.io.PrintStream;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass public class Main {
    private static final PrintStream PRINT_STREAM = System.out;

    public static void main(String[] args) {
        PRINT_STREAM.println("\"⬜\" - Стена, \"⬛\" - Проход, \"\uD83D\uDFEB\" - Болото,"
            + " \"\uD83D\uDFE8\" - Песок, \"\uD83E\uDE99\" - Монетка");
        InputHandler inputHandler = new InputHandler();
        MazeManager mazeManager = new MazeManager();
        ConsoleRenderer cr = new ConsoleRenderer();

        // Получение параметров от пользователя
        int[] dimensions = inputHandler.getMazeDimensions();
        int width = dimensions[0];
        int height = dimensions[1];

        // Выбор алгоритма генерации
        int generationChoice = inputHandler.getGenerationAlgorithm();
        Maze maze = mazeManager.generateMaze(width, height, generationChoice);

        // Печать сгенерированного лабиринта
        PRINT_STREAM.println("Сгенерированный лабиринт:");
        PRINT_STREAM.println(cr.render(maze));

        // Выбор правильности лабиринта
        int corruptionChoice = inputHandler.getCorruptionChoice();
        maze = mazeManager.promptForCorruptedMaze(maze, corruptionChoice);
        PRINT_STREAM.println("Лабиринт для которого будем искать путь:");
        PRINT_STREAM.println(cr.render(maze));

        // Ввод начальной и конечной точки
        Coordinate start = inputHandler.getStartPoint(width, height, maze);
        Coordinate end = inputHandler.getEndPoint(width, height, maze);

        // Выбор алгоритма поиска пути
        int solverChoice = inputHandler.getSolverAlgorithm();
        List<Coordinate> path = mazeManager.solveMaze(maze, start, end, solverChoice);

        // Отображение результата
        if (path.isEmpty()) {
            PRINT_STREAM.println("Путь не найден!");
        } else {
            PRINT_STREAM.println("Путь найден:");
            PRINT_STREAM.println(cr.render(maze, path));
        }

    }
}
