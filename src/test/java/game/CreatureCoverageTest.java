package game;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;

import org.junit.jupiter.api.Test;

class CreatureCoverageTest {

    @Test
    void movementHandlesWallsStairsAndNoOpMoves() {
        Tile[][][] tiles = WorldTest.tiles(5, 5, 2, Tile.FLOOR);
        tiles[2][1][0] = Tile.WALL;
        tiles[2][2][1] = Tile.STAIRS_UP;
        tiles[2][2][0] = Tile.STAIRS_DOWN;
        World world = new World(tiles);
        Creature creature = WorldTest.creature(world, 1, 1, 0, "walker");
        world.add(creature);

        creature.moveBy(0, 0, 0);
        creature.moveBy(1, 0, 0);
        assertEquals(1, creature.x);
        creature.moveBy(0, 1, 0);
        assertEquals(2, creature.y);
        creature.moveBy(1, 0, 0);
        assertEquals(2, creature.x);
        creature.moveBy(0, 0, 1);
        assertEquals(1, creature.z);
        creature.moveBy(0, 0, -1);
        assertEquals(0, creature.z);
    }

    @Test
    void combatEquipmentAndExperienceWorkAcrossBranches() {
        World world = new World(WorldTest.tiles(20, 20, 1, Tile.FLOOR));
        Creature hero = WorldTest.creature(world, 5, 5, 0, "hero");
        Creature enemy = new Creature(world, 'e', Color.RED, "enemy", 1, 1, 0);
        new CreatureAi(enemy);
        enemy.x = 6;
        enemy.y = 5;
        enemy.z = 0;
        world.add(hero);
        world.add(enemy);

        Item sword = new Item(')', Color.WHITE, "sword", null);
        sword.modifyAttackValue(20);
        hero.equip(sword);
        assertSame(sword, hero.weapon());
        assertTrue(hero.attackValue() >= 40);

        Item armor = new Item('[', Color.WHITE, "armor", null);
        armor.modifyDefenseValue(5);
        hero.equip(armor);
        assertSame(armor, hero.armor());
        assertTrue(hero.defenseValue() >= 10);

        Item empty = new Item('x', Color.WHITE, "empty", null);
        hero.equip(empty);
        assertSame(sword, hero.weapon());
        hero.unequip(sword);
        hero.unequip(armor);
        hero.unequip(null);

        hero.meleeAttack(enemy);
        assertEquals(0, enemy.hp());
        assertTrue(hero.xp() > 0);
        assertNotNull(enemy.causeOfDeath());
    }

    @Test
    void rangedAndThrownAttacksConsumeItemsAndApplyEffects() {
        World world = new World(WorldTest.tiles(15, 15, 1, Tile.FLOOR));
        Creature hero = WorldTest.creature(world, 5, 5, 0, "hero");
        Creature target = new Creature(world, 't', Color.RED, "target", 100, 1, 0);
        new CreatureAi(target);
        target.x = 6;
        target.y = 5;
        target.z = 0;
        world.add(hero);
        world.add(target);

        Item bow = new Item(')', Color.WHITE, "bow", null);
        bow.modifyRangedAttackValue(10);
        hero.equip(bow);
        int before = target.hp();
        hero.rangedWeaponAttack(target);
        assertTrue(target.hp() < before);

        Item throwingPotion = new Item('!', Color.WHITE, "throwing potion", null);
        throwingPotion.setQuaffEffect(new Effect(2));
        hero.inventory().add(throwingPotion);
        before = target.hp();
        hero.throwItem(throwingPotion, target.x, target.y, target.z);
        assertTrue(target.hp() < before);
        assertFalse(hero.inventory().contains(throwingPotion));
        assertEquals(1, target.effects().size());

        Item rock = new Item(',', Color.WHITE, "rock", null);
        hero.inventory().add(rock);
        hero.throwItem(rock, 10, 10, 0);
        assertFalse(hero.inventory().contains(rock));
    }

    @Test
    void pickupEatingDroppingAndFoodBoundariesWork() {
        World world = new World(WorldTest.tiles(10, 10, 1, Tile.FLOOR));
        Creature hero = WorldTest.creature(world, 4, 4, 0, "hero");
        world.add(hero);
        Item apple = new Item('%', Color.RED, "apple", null);
        apple.modifyFoodValue(50);
        assertTrue(world.addAtEmptySpace(apple, hero.x, hero.y, hero.z));
        hero.pickup();
        assertTrue(hero.inventory().contains(apple));
        hero.eat(apple);
        assertFalse(hero.inventory().contains(apple));
        assertEquals(716, hero.food());

        Item potion = new Item('!', Color.WHITE, "bad potion", "unknown potion");
        potion.modifyFoodValue(-1);
        potion.setQuaffEffect(new Effect(1));
        hero.inventory().add(potion);
        hero.quaff(potion);
        assertFalse(hero.inventory().contains(potion));
        assertTrue(hero.food() < 716);

        Item drop = new Item(',', Color.WHITE, "drop", null);
        hero.inventory().add(drop);
        hero.drop(drop);
        assertFalse(hero.inventory().contains(drop));

        Creature nonPlayer = new Creature(world, 'n', Color.WHITE, "npc", 20, 1, 1);
        new CreatureAi(nonPlayer);
        nonPlayer.modifyFood(-1000);
        assertTrue(nonPlayer.hp() > 0);
        hero.modifyFood(1000);
        assertTrue(hero.maxFood() > 1000);
    }

    @Test
    void spellCastingAndDetectionRespectManaAndTargets() {
        World world = new World(WorldTest.tiles(10, 10, 1, Tile.FLOOR));
        Creature caster = WorldTest.creature(world, 2, 2, 0, "caster");
        Creature target = new Creature(world, 't', Color.RED, "target", 20, 1, 1);
        new CreatureAi(target);
        target.x = 3;
        target.y = 2;
        target.z = 0;
        world.add(caster);
        world.add(target);

        Spell costly = new Spell("costly", 20, new Effect(1));
        caster.castSpell(costly, target.x, target.y);
        assertEquals(5, caster.mana());
        caster.castSpell(new Spell("miss", 1, new Effect(1)), 9, 9);
        assertEquals(5, caster.mana());
        caster.castSpell(new Spell("valid", 2, new Effect(1)), target.x, target.y);
        assertEquals(3, caster.mana());
        assertEquals(1, target.effects().size());

        assertTrue(caster.canSee(target.x, target.y, target.z));
        caster.modifyDetectCreatures(1);
        assertTrue(caster.creature(target.x, target.y, target.z) == target);
        assertSame(Tile.FLOOR, caster.realTile(0, 0, 0));
        assertNotNull(caster.tile(0, 0, 0));
        assertEquals("  level:1  attack:20  defense:5  hp:100", caster.details());
    }

    @Test
    void updateRegeneratesStatsAndExpiresEffects() {
        World world = new World(WorldTest.tiles(5, 5, 1, Tile.FLOOR));
        Creature creature = WorldTest.creature(world, 2, 2, 0, "hero");
        creature.modifyHp(-10, "test");
        creature.modifyMana(-2);
        creature.update();
        assertEquals(91, creature.hp());
        assertEquals(4, creature.mana());

        Effect ending = new Effect(1) {
            @Override
            public void end(Creature target) {
                target.modifyAttackValue(3);
            }
        };
        Item potion = new Item('!', Color.WHITE, "potion", null);
        potion.setQuaffEffect(ending);
        creature.inventory().add(potion);
        creature.quaff(potion);
        assertEquals(1, creature.effects().size());
        int attack = creature.attackValue();
        creature.update();
        assertTrue(creature.effects().isEmpty());
        assertEquals(attack + 3, creature.attackValue());

        creature.modifyXp(1000);
        assertTrue(creature.level() > 1);
        assertTrue(creature.xp() >= 1000);
    }

    @Test
    void deathDropsCorpseAndStarvationKillsPlayers() {
        World world = new World(WorldTest.tiles(5, 5, 1, Tile.FLOOR));
        Creature hero = WorldTest.creature(world, 2, 2, 0, "hero");
        world.add(hero);
        Item loot = new Item(',', Color.WHITE, "loot", null);
        hero.inventory().add(loot);

        hero.modifyHp(-1000, "test death");

        assertEquals(0, hero.hp());
        assertNull(world.creature(2, 2, 0));
        assertFalse(hero.inventory().contains(loot));
        assertTrue(world.item(2, 2, 0) != null || findItem(world, loot));

        Creature starving = WorldTest.creature(world, 1, 1, 0, "starving");
        world.add(starving);
        starving.modifyFood(-1000);
        assertEquals(0, starving.hp());
        assertEquals("Starved to death.", starving.causeOfDeath());
    }

    private static boolean findItem(World world, Item expected) {
        for (int x = 0; x < world.width(); x++)
            for (int y = 0; y < world.height(); y++)
                for (int z = 0; z < world.depth(); z++)
                    if (world.item(x, y, z) == expected)
                        return true;
        return false;
    }
}
