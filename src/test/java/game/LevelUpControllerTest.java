package game;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

class LevelUpControllerTest {

    @Test
    void exposesAndInvokesEveryLevelUpOption() {
        LevelUpController controller = new LevelUpController();
        Creature creature = WorldTest.creature(new World(WorldTest.tiles(3, 3, 1, Tile.FLOOR)), 1, 1, 0, "hero");
        List<String> options = controller.getLevelUpOptions();

        assertEquals(7, options.size());
        for (String name : options) {
            LevelUpOption option = controller.getLevelUpOption(name);
            assertNotNull(option);
            assertEquals(name, option.name());
            option.invoke(creature);
        }

        assertNull(controller.getLevelUpOption("not an option"));
        assertTrue(creature.maxHp() >= 110);
        assertTrue(creature.maxMana() >= 10);
        assertTrue(creature.attackValue() >= 22);
        assertTrue(creature.defenseValue() >= 6);
        assertTrue(creature.visionRadius() >= 10);
    }
}
