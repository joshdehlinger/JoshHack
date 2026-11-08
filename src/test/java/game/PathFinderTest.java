package game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

public class PathFinderTest {

    @Test
    void findsShortPathAroundBlockedTile() {
        Tile[][][] tiles = filledTiles(5, 5, Tile.FLOOR);
        tiles[1][0][0] = Tile.WALL;
        Creature creature = creatureIn(new World(tiles), 0, 0);

        List<Point> path = new PathFinder().findPath(creature, new Point(0, 0, 0), new Point(2, 2, 0), 300);

        assertNotNull(path);
        assertEquals(2, path.size());
        assertEquals(new Point(2, 2, 0), path.get(path.size() - 1));
        assertFalse(path.contains(new Point(1, 0, 0)));
        assertTrue(isAdjacentPath(new Point(0, 0, 0), path));
    }

    @Test
    void returnsEmptyPathWhenStartIsDestination() {
        Tile[][][] tiles = filledTiles(3, 3, Tile.FLOOR);
        Creature creature = creatureIn(new World(tiles), 1, 1);
        Point start = new Point(1, 1, 0);

        List<Point> path = new PathFinder().findPath(creature, start, start, 10);

        assertNotNull(path);
        assertTrue(path.isEmpty());
    }

    @Test
    void returnsNullWhenDestinationIsUnreachable() {
        Tile[][][] tiles = filledTiles(3, 3, Tile.WALL);
        tiles[0][0][0] = Tile.FLOOR;
        tiles[2][2][0] = Tile.FLOOR;
        Creature creature = creatureIn(new World(tiles), 0, 0);

        List<Point> path = new PathFinder().findPath(creature, new Point(0, 0, 0), new Point(2, 2, 0), 30);

        assertNull(path);
    }

    @Test
    void clearsSearchStateBetweenCalls() {
        Tile[][][] tiles = filledTiles(3, 3, Tile.FLOOR);
        Creature creature = creatureIn(new World(tiles), 0, 0);
        PathFinder pathFinder = new PathFinder();

        assertNotNull(pathFinder.findPath(creature, new Point(0, 0, 0), new Point(2, 2, 0), 30));

        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[x].length; y++)
                tiles[x][y][0] = Tile.WALL;
        }
        tiles[0][0][0] = Tile.FLOOR;
        tiles[2][2][0] = Tile.FLOOR;

        assertNull(pathFinder.findPath(creature, new Point(0, 0, 0), new Point(2, 2, 0), 30));
    }

    private static Creature creatureIn(World world, int x, int y) {
        Creature creature = new Creature(world, '@', java.awt.Color.WHITE, "player", 10, 1, 1);
        new CreatureAi(creature);
        creature.x = x;
        creature.y = y;
        creature.z = 0;
        return creature;
    }

    private static Tile[][][] filledTiles(int width, int height, Tile tile) {
        Tile[][][] tiles = new Tile[width][height][1];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++)
                tiles[x][y][0] = tile;
        }
        return tiles;
    }

    private static boolean isAdjacentPath(Point start, List<Point> path) {
        Point previous = start;
        for (Point point : path) {
            if (Math.max(Math.abs(previous.x - point.x), Math.abs(previous.y - point.y)) != 1)
                return false;
            previous = point;
        }
        return true;
    }
}
