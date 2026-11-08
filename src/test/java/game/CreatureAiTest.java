package game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;

import org.junit.jupiter.api.Test;

public class CreatureAiTest {

    @Test
    void onEnterMovesCreatureOntoGround() {
        Creature creature = creatureOn(filledTiles(Tile.FLOOR));
        CreatureAi ai = new CreatureAi(creature);

        ai.onEnter(2, 1, 0, Tile.FLOOR);

        assertEquals(2, creature.x);
        assertEquals(1, creature.y);
        assertEquals(0, creature.z);
    }

    @Test
    void onEnterDoesNotMoveCreatureIntoWall() {
        Creature creature = creatureOn(filledTiles(Tile.FLOOR));
        CreatureAi ai = new CreatureAi(creature);

        ai.onEnter(2, 1, 0, Tile.WALL);

        assertEquals(1, creature.x);
        assertEquals(1, creature.y);
    }

    @Test
    void canSeeRespectsObstaclesRadiusAndDepth() {
        Tile[][][] tiles = filledTiles(Tile.FLOOR);
        tiles[2][1][0] = Tile.WALL;
        Creature creature = creatureOn(tiles);
        CreatureAi ai = new CreatureAi(creature);

        assertTrue(ai.canSee(2, 1, 0));
        assertFalse(ai.canSee(3, 1, 0));
        assertFalse(ai.canSee(1, 1, 1));
        assertFalse(ai.canSee(20, 1, 0));
    }

    private static Creature creatureOn(Tile[][][] tiles) {
        Creature creature = new Creature(new World(tiles), '@', Color.WHITE, "player", 10, 1, 1);
        creature.x = 1;
        creature.y = 1;
        creature.z = 0;
        return creature;
    }

    private static Tile[][][] filledTiles(Tile tile) {
        Tile[][][] tiles = new Tile[5][5][1];
        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[x].length; y++)
                tiles[x][y][0] = tile;
        }
        return tiles;
    }
}
