package game;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class WorldBuilderTest {

    @Test
    void makeCavesBuildsWalkableLevelsAndConnectsThemWithStairs() {
        World world = new WorldBuilder(80, 80, 3).makeCaves().build();

        assertEquals(80, world.width());
        assertEquals(80, world.height());
        assertEquals(3, world.depth());
        assertTrue(count(world, Tile.STAIRS_UP) >= 1);
        assertTrue(count(world, Tile.STAIRS_DOWN) >= 1);
        assertTrue(count(world, Tile.FLOOR) > 0);
    }

    @Test
    void anUnbuiltBuilderHasItsRequestedDimensions() {
        World world = new WorldBuilder(2, 3, 1).build();
        assertEquals(2, world.width());
        assertEquals(3, world.height());
        assertEquals(1, world.depth());
    }

    private static int count(World world, Tile tile) {
        int count = 0;
        for (int x = 0; x < world.width(); x++)
            for (int y = 0; y < world.height(); y++)
                for (int z = 0; z < world.depth(); z++)
                    if (world.tile(x, y, z) == tile)
                        count++;
        return count;
    }
}
