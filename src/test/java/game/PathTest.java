package game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

public class PathTest {

    @Test
    void pointsCannotBeChangedThroughReturnedListOrPoints() {
        Tile[][][] tiles = new Tile[3][3][1];
        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[x].length; y++)
                tiles[x][y][0] = Tile.FLOOR;
        }

        Creature creature = new Creature(new World(tiles), '@', java.awt.Color.WHITE, "player", 10, 1, 1);
        new CreatureAi(creature);
        creature.x = 0;
        creature.y = 0;
        creature.z = 0;

        Path path = new Path(creature, 2, 2);
        List<Point> points = path.points();
        Point expectedEnd = points.get(points.size() - 1);

        assertThrows(UnsupportedOperationException.class, points::clear);
        points.get(points.size() - 1).x = 99;

        Point actualEnd = path.points().get(path.points().size() - 1);
        assertEquals(expectedEnd.y, actualEnd.y);
        assertEquals(2, actualEnd.x);
    }
}
