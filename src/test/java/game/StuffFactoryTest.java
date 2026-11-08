package game;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class StuffFactoryTest {

    @Test
    void createsAllCreatureAndItemKinds() {
        World world = new World(WorldTest.tiles(80, 80, 2, Tile.FLOOR));
        StuffFactory factory = new StuffFactory(world);
        FieldOfView fov = new FieldOfView(world);
        List<String> messages = new ArrayList<>();
        Creature player = factory.newPlayer(messages, fov);

        assertEquals("player", player.name());
        assertEquals("fungus", factory.newFungus(0).name());
        assertEquals("bat", factory.newBat(0).name());
        assertEquals("zombie", factory.newZombie(0, player).name());
        assertEquals("goblin", factory.newGoblin(0, player).name());

        assertItem(factory.newRock(0), "rock");
        assertItem(factory.newVictoryItem(0), "teddy bear");
        assertEquals(400, factory.newBread(0).foodValue());
        assertEquals(100, factory.newFruit(0).foodValue());
        assertEquals(5, factory.newDagger(0).attackValue());
        assertEquals(10, factory.newSword(0).attackValue());
        assertEquals(5, factory.newStaff(0).attackValue());
        assertEquals(5, factory.newBow(0).rangedAttackValue());
        assertEquals(100, factory.newEdibleWeapon(0).foodValue());
        assertEquals(2, factory.newLightArmor(0).defenseValue());
        assertEquals(4, factory.newMediumArmor(0).defenseValue());
        assertEquals(6, factory.newHeavyArmor(0).defenseValue());

        assertItem(factory.newPotionOfHealth(0), "health potion");
        assertItem(factory.newPotionOfMana(0), "mana potion");
        assertItem(factory.newPotionOfSlowHealth(0), "slow health potion");
        assertItem(factory.newPotionOfPoison(0), "poison potion");
        assertItem(factory.newPotionOfWarrior(0), "warrior's potion");
        assertItem(factory.newPotionOfArcher(0), "archers potion");
        assertItem(factory.newPotionOfExperience(0), "experience potion");
        assertEquals(4, factory.newWhiteMagesSpellbook(0).writtenSpells().size());
        assertEquals(4, factory.newBlueMagesSpellbook(0).writtenSpells().size());
        assertNotNull(factory.randomArmor(0));
        assertNotNull(factory.randomWeapon(0));
        assertNotNull(factory.randomPotion(0));
        assertNotNull(factory.randomSpellBook(0));
    }

    @Test
    void potionEffectsApplyAndExpire() {
        World world = new World(WorldTest.tiles(80, 80, 1, Tile.FLOOR));
        StuffFactory factory = new StuffFactory(world);
        Creature creature = WorldTest.creature(world, 40, 40, 0, "hero");
        world.add(creature);

        Item health = factory.newPotionOfHealth(0);
        creature.modifyHp(-20, "test");
        health.quaffEffect().start(creature);
        assertTrue(creature.hp() > 80);
        health.quaffEffect().start(creature);

        Item mana = factory.newPotionOfMana(0);
        creature.modifyMana(-3);
        mana.quaffEffect().start(creature);
        assertEquals(creature.maxMana(), creature.mana());
        mana.quaffEffect().start(creature);

        Item slowHealth = factory.newPotionOfSlowHealth(0);
        slowHealth.quaffEffect().start(creature);
        slowHealth.quaffEffect().update(creature);
        slowHealth.quaffEffect().end(creature);

        Item poison = factory.newPotionOfPoison(0);
        int beforePoison = creature.hp();
        poison.quaffEffect().start(creature);
        poison.quaffEffect().update(creature);
        assertEquals(beforePoison - 1, creature.hp());
        poison.quaffEffect().end(creature);

        Item warrior = factory.newPotionOfWarrior(0);
        int oldAttack = creature.attackValue();
        int oldDefense = creature.defenseValue();
        warrior.quaffEffect().start(creature);
        assertEquals(oldAttack + 5, creature.attackValue());
        assertEquals(oldDefense + 5, creature.defenseValue());
        warrior.quaffEffect().end(creature);
        assertEquals(oldAttack, creature.attackValue());
        assertEquals(oldDefense, creature.defenseValue());

        Item archer = factory.newPotionOfArcher(0);
        int oldVision = creature.visionRadius();
        archer.quaffEffect().start(creature);
        assertEquals(oldVision + 3, creature.visionRadius());
        archer.quaffEffect().end(creature);
        assertEquals(oldVision, creature.visionRadius());

        int oldXp = creature.xp();
        factory.newPotionOfExperience(0).quaffEffect().start(creature);
        assertTrue(creature.xp() > oldXp);
    }

    @Test
    void spellEffectsCanBeAppliedAndEnded() {
        World world = new World(WorldTest.tiles(80, 80, 1, Tile.FLOOR));
        StuffFactory factory = new StuffFactory(world);
        Creature creature = WorldTest.creature(world, 40, 40, 0, "hero");
        world.add(creature);
        creature.modifyHp(-50, "test");

        Item whiteBook = factory.newWhiteMagesSpellbook(0);
        List<Spell> whiteSpells = whiteBook.writtenSpells();
        whiteSpells.get(0).effect().start(creature);
        whiteSpells.get(1).effect().start(creature);
        whiteSpells.get(2).effect().update(creature);
        int attack = creature.attackValue();
        int defense = creature.defenseValue();
        int vision = creature.visionRadius();
        Effect strength = whiteSpells.get(3).effect();
        strength.start(creature);
        strength.update(creature);
        strength.end(creature);
        assertEquals(attack, creature.attackValue());
        assertEquals(defense, creature.defenseValue());
        assertEquals(vision, creature.visionRadius());

        Item blueBook = factory.newBlueMagesSpellbook(0);
        blueBook.writtenSpells().get(0).effect().start(creature);
        blueBook.writtenSpells().get(1).effect().start(creature);
        blueBook.writtenSpells().get(2).effect().start(creature);
        blueBook.writtenSpells().get(3).effect().end(creature);
    }

    private static void assertItem(Item item, String name) {
        assertEquals(name, item.name());
    }
}
