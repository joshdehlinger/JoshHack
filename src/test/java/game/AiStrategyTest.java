package game;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class AiStrategyTest {

    @Test
    void baseAiNamesItemsFindsEquipmentAndHunts() {
        World world = new World(WorldTest.tiles(20, 20, 1, Tile.FLOOR));
        Creature creature = WorldTest.creature(world, 2, 2, 0, "hunter");
        Creature target = WorldTest.creature(world, 8, 8, 0, "target");
        world.add(creature);
        world.add(target);
        TestAi ai = new TestAi(creature);
        Item potion = new Item('!', Color.WHITE, "health potion", "blue potion");

        assertEquals("blue potion", ai.getName(potion));
        ai.setName(potion, "health potion");
        assertEquals("health potion", ai.getName(potion));
        assertTrue(ai.canSee(target.x, target.y, target.z));
        assertFalse(ai.canSee(target.x, target.y, 1));
        assertEquals(Tile.UNKNOWN, ai.rememberedTile(0, 0, 0));

        Item bow = new Item(')', Color.WHITE, "bow", null);
        bow.modifyRangedAttackValue(5);
        creature.inventory().add(bow);
        assertTrue(ai.canUseBetterEquipment());
        ai.useBetterEquipment();
        assertSame(bow, creature.weapon());
        assertTrue(ai.canUseRangedOn(target));

        Item rock = new Item(',', Color.WHITE, "rock", null);
        rock.modifyThrownAttackValue(10);
        creature.inventory().add(rock);
        assertSame(rock, ai.weaponToThrow());
        assertTrue(ai.canThrowAtTarget(target));
        ai.hunt(target);
        assertNotEquals(2, creature.x);

        ai.onGainLevel();
        assertTrue(creature.level() >= 1);
    }

    @Test
    void pickupAvailabilityTracksWorldItemsAndInventoryCapacity() {
        World world = new World(WorldTest.tiles(4, 4, 1, Tile.FLOOR));
        Creature creature = WorldTest.creature(world, 1, 1, 0, "picker");
        TestAi ai = new TestAi(creature);
        Item item = new Item('!', Color.WHITE, "potion", null);

        assertFalse(ai.canPickUpItem());
        assertTrue(world.addAtEmptySpace(item, creature.x, creature.y, creature.z));
        assertTrue(ai.canPickUpItem());
        creature.inventory().add(item);
        creature.pickup();
        assertFalse(ai.canPickUpItem());
    }

    @Test
    void playerAiMovesDigsReportsAndUsesRememberedTiles() {
        Tile[][][] tiles = WorldTest.tiles(5, 5, 1, Tile.FLOOR);
        tiles[2][1][0] = Tile.WALL;
        World world = new World(tiles);
        Creature player = WorldTest.creature(world, 1, 1, 0, "player");
        FieldOfView fov = new FieldOfView(world);
        fov.update(1, 1, 0, 3);
        List<String> messages = new ArrayList<>();
        PlayerAi ai = new PlayerAi(player, messages, fov);
        world.add(player);

        ai.onNotify("hello");
        assertEquals(List.of("hello"), messages);
        assertTrue(ai.canSee(1, 1, 0));
        assertEquals(Tile.WALL, ai.rememberedTile(2, 1, 0));

        Item item = new Item('!', Color.WHITE, "potion", "green potion");
        assertTrue(world.addAtEmptySpace(item, 1, 3, 0));
        ai.onEnter(1, 3, 0, Tile.FLOOR);
        assertEquals(3, player.y);
        assertTrue(messages.stream().anyMatch(m -> m.contains("green potion")));

        ai.onEnter(2, 1, 0, Tile.WALL);
        assertEquals(Tile.FLOOR, world.tile(2, 1, 0));
        ai.onGainLevel();
    }

    @Test
    void goblinAndZombieAisEquipAttackAndPursue() {
        World world = new World(WorldTest.tiles(20, 20, 1, Tile.FLOOR));
        Creature player = WorldTest.creature(world, 8, 8, 0, "player");
        Creature goblin = new Creature(world, 'g', Color.GREEN, "goblin", 50, 5, 2);
        goblin.x = 2;
        goblin.y = 2;
        goblin.z = 0;
        GoblinAi goblinAi = new GoblinAi(goblin, player);
        Item sword = new Item(')', Color.WHITE, "sword", null);
        sword.modifyAttackValue(5);
        goblin.inventory().add(sword);
        world.add(player);
        world.add(goblin);

        goblinAi.onUpdate();
        assertSame(sword, goblin.weapon());
        goblinAi.onUpdate();
        assertTrue(goblin.x > 2 || goblin.y > 2);

        Creature zombie = new Creature(world, 'z', Color.WHITE, "zombie", 30, 5, 1);
        zombie.x = 2;
        zombie.y = 12;
        zombie.z = 0;
        ZombieAi zombieAi = new ZombieAi(zombie, player);
        world.add(zombie);
        for (int i = 0; i < 100 && zombie.x == 2 && zombie.y == 12; i++)
            zombieAi.onUpdate();
        assertTrue(zombie.x != 2 || zombie.y != 12);
    }

    @Test
    void batWandersAndFungusEventuallySpreads() {
        World world = new World(WorldTest.tiles(80, 80, 1, Tile.FLOOR));
        Creature bat = new Creature(world, 'b', Color.YELLOW, "bat", 15, 5, 0);
        bat.x = 40;
        bat.y = 40;
        bat.z = 0;
        new BatAi(bat);
        world.add(bat);
        for (int i = 0; i < 20; i++)
            bat.update();
        assertTrue(bat.x >= 0 && bat.x < world.width());
        assertTrue(bat.y >= 0 && bat.y < world.height());

        StuffFactory factory = new StuffFactory(world);
        Creature fungus = new Creature(world, 'f', Color.GREEN, "fungus", 10, 0, 0);
        fungus.x = 40;
        fungus.y = 40;
        fungus.z = 0;
        new FungusAi(fungus, factory);
        world.add(fungus);
        int before = countCreatures(world);
        for (int i = 0; i < 3000 && countCreatures(world) == before; i++)
            fungus.update();
        assertTrue(countCreatures(world) > before);
    }

    private static int countCreatures(World world) {
        int count = 0;
        for (int x = 0; x < world.width(); x++)
            for (int y = 0; y < world.height(); y++)
                if (world.creature(x, y, 0) != null)
                    count++;
        return count;
    }

    private static final class TestAi extends CreatureAi {
        TestAi(Creature creature) {
            super(creature);
        }

        boolean canThrowAtTarget(Creature other) {
            return super.canThrowAt(other);
        }

        boolean canUseRangedOn(Creature other) {
            return super.canRangedWeaponAttack(other);
        }

        boolean canPickUpItem() {
            return super.canPickup();
        }

        boolean hasBetterEquipment() {
            return super.canUseBetterEquipment();
        }

        Item weaponToThrow() {
            return super.getWeaponToThrow();
        }

        void equipBetterItems() {
            super.useBetterEquipment();
        }
    }
}
