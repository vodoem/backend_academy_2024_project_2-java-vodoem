package backend.academy.maze.data;

public enum Surface {
    WALL(0),        // Стена, по ней нельзя пройти
    PASSAGE(2),     // Проход, стандартное передвижение
    SWAMP(5),       // Болото, движение замедляется
    SAND(5),        // Песок, движение чуть медленнее
    COIN(1);       // Монетка, может давать бонус

    private final int weight;

    Surface(int weight) {
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }
}
