package game;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class CombatRulesTest {

    @Test
    void testNormalHealing() {
        assertEquals(70, CombatRules.calculateNewHp(50, 100, 20));
    }

    @Test
    void testHealingExceedsMaxHp() {
        assertEquals(100, CombatRules.calculateNewHp(90, 100, 30));
    }

    @Test
    void testHealingExactlyToMaxHp() {
        assertEquals(100, CombatRules.calculateNewHp(80, 100, 20));
    }

    @Test
    void testIsDead() {
        assertTrue(CombatRules.isDead(0));
        assertTrue(CombatRules.isDead(-5));
        assertFalse(CombatRules.isDead(1));
        assertFalse(CombatRules.isDead(50));
    }
}