package game;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;

import org.junit.jupiter.api.Test;

class WorldTest {

    @Test
    void tileAndOccupantQueriesRespectWorldState() {
        Tile[][][] tiles = tiles(3, 3, 1, Tile.FLOOR);
        tiles[1][1][0] = Tile.WALL;
        World world = new World(tiles);
        Creature creature = creature(world, 1, 1, 0, "test");
        world.add(creature);

        assertEquals(3, world.width());
        assertEquals(3, world.height());
        assertEquals(1, world.depth());
        assertSame(Tile.WALL, world.tile(1, 1, 0));
        assertSame(Tile.BOUNDS, world.tile(-1, 0, 0));
        assertSame(creature, world.creature(1, 1, 0));
        assertEquals('@', world.glyph(1, 1, 0));
        assertEquals(Color.WHITE, world.color(1, 1, 0));

        world.remove(creature);
        assertNull(world.creature(1, 1, 0));
    }

    @Test
    void itemsCanBeAddedRemovedAndPlacedNearOccupiedLocations() {
        World world = new World(tiles(5, 5, 1, Tile.FLOOR));
        Item first = new Item('!', Color.WHITE, "first", null);
        Item second = new Item('?', Color.YELLOW, "second", null);

        world.addAtEmptyLocation(first, 0);
        assertSame(first, itemAt(first, world));
        world.remove(first);
        assertNull(itemAt(first, world));

        assertTrue(world.addAtEmptySpace(null, 0, 0, 0));
        assertTrue(world.addAtEmptySpace(second, 2, 2, 0));
        assertSame(second, world.item(2, 2, 0));
        assertEquals('?', world.glyph(2, 2, 0));
        assertEquals(Color.YELLOW, world.color(2, 2, 0));
        world.remove(2, 2, 0);
        assertNull(world.item(2, 2, 0));
    }

    @Test
    void addAtEmptySpaceSearchesNearbyAndReturnsFalseWhenNoFloorExists() {
        World world = new World(tiles(3, 3, 1, Tile.FLOOR));
        Item first = new Item('!', Color.WHITE, "first", null);
        Item second = new Item('?', Color.WHITE, "second", null);

        assertTrue(world.addAtEmptySpace(first, 1, 1, 0));
        assertTrue(world.addAtEmptySpace(second, 1, 1, 0));
        assertSame(first, world.item(1, 1, 0));
        assertNotNull(findItem(second, world));

        World blockedWorld = new World(tiles(3, 3, 1, Tile.WALL));
        assertFalse(blockedWorld.addAtEmptySpace(new Item('x', Color.WHITE, "blocked", null), 0, 0, 0));
    }

    @Test
    void diggingChangesWallsAndUpdatingRunsCreatures() {
        Tile[][][] tiles = tiles(3, 3, 1, Tile.FLOOR);
        tiles[1][0][0] = Tile.WALL;
        World world = new World(tiles);
        Creature creature = creature(world, 1, 1, 0, "test");
        world.add(creature);

        world.dig(1, 0, 0);
        assertSame(Tile.FLOOR, world.tile(1, 0, 0));
        world.dig(1, 0, 0);
        world.update();
        assertSame(creature, world.creature(1, 1, 0));
    }

    static Tile[][][] tiles(int width, int height, int depth, Tile tile) {
        Tile[][][] result = new Tile[width][height][depth];
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                for (int z = 0; z < depth; z++)
                    result[x][y][z] = tile;
        return result;
    }

    static Creature creature(World world, int x, int y, int z, String name) {
        Creature creature = new Creature(world, '@', Color.WHITE, name, 100, 20, 5);
        new CreatureAi(creature);
        creature.x = x;
        creature.y = y;
        creature.z = z;
        return creature;
    }

    private Item itemAt(Item item, World world) {
        return findItem(item, world);
    }

    private Item findItem(Item item, World world) {
        for (int x = 0; x < world.width(); x++)
            for (int y = 0; y < world.height(); y++)
                for (int z = 0; z < world.depth(); z++)
                    if (world.item(x, y, z) == item)
                        return item;
        return null;
    }
}
