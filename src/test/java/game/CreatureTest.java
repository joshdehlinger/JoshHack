package game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Color;

public class CreatureTest {

    private World world;
    private Creature creature;

    @BeforeEach
    void setUp() {
        Tile[][][] tiles = new Tile[3][3][1];

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                tiles[x][y][0] = Tile.FLOOR;
            }
        }

        world = new World(tiles);

        creature = new Creature(world, '@', Color.GRAY, "josh", 100, 10, 5);

        creature.x = 1;
        creature.y = 1;
        creature.z = 0;
    }

    @Test
    void testModifyHpDamage() {

        creature.modifyHp(-30, "Killed in combat");

        assertEquals(70, creature.hp());
    }

    @Test
    void testModifyHpHealingMax() {

        creature.modifyHp(-20, "Damage");
        creature.modifyHp(50, "Healing");

        assertEquals(100, creature.hp());
    }

    @Test
    void testModifyHpDeath() {

        creature.modifyHp(-100, "Killed in combat");

        assertEquals(0, creature.hp());
        assertEquals("Killed in combat", creature.causeOfDeath());

    }
}
