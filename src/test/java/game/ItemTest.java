package game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.awt.Color;

import org.junit.jupiter.api.Test;

public class ItemTest {

    @Test
    void writtenSpellsCanBeAddedButNotModifiedThroughGetter() {
        Item item = new Item('?', Color.WHITE, "spellbook", null);
        item.addWrittenSpell("heal", 1, new Effect(1));

        assertEquals(1, item.writtenSpells().size());
        assertThrows(UnsupportedOperationException.class, () -> item.writtenSpells().clear());
    }
}
