package game;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class FieldOfViewTest {

    @Test
    void visibilityTracksRadiusDepthAndOpaqueWalls() {
        Tile[][][] tiles = WorldTest.tiles(7, 7, 2, Tile.FLOOR);
        tiles[3][2][0] = Tile.WALL;
        World world = new World(tiles);
        FieldOfView fov = new FieldOfView(world);

        assertFalse(fov.isVisible(3, 3, 0));
        assertEquals(Tile.UNKNOWN, fov.tile(0, 0, 0));

        fov.update(3, 3, 0, 3);

        assertTrue(fov.isVisible(3, 3, 0));
        assertTrue(fov.isVisible(3, 2, 0));
        assertFalse(fov.isVisible(3, 1, 0));
        assertFalse(fov.isVisible(6, 3, 0));
        assertFalse(fov.isVisible(-1, 0, 0));
        assertFalse(fov.isVisible(3, 3, 1));
        assertEquals(Tile.WALL, fov.tile(3, 2, 0));

        fov.update(2, 2, 1, 2);
        assertTrue(fov.isVisible(2, 2, 1));
        assertFalse(fov.isVisible(3, 3, 0));
    }
}
