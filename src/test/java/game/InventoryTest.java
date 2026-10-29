package game;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;

import org.junit.jupiter.api.Test;

public class InventoryTest {

    private Item makeItem(String name) {
        return new Item('!', Color.WHITE, name, null);
    }

    @Test
    void addPlacesItemInFirstAvailableSlot() {
        // Arrange
        Inventory inventory = new Inventory(3);
        Item item = makeItem("Sword");

        // Act
        inventory.add(item);

        // Assert
        assertSame(item, inventory.get(0));
    }

    @Test
    void addUsesNextAvailableSlot() {
        // Arrange
        Inventory inventory = new Inventory(3);
        Item first = makeItem("Sword");
        Item second = makeItem("Potion");

        inventory.add(first);

        // Act
        inventory.add(second);

        // Assert
        assertSame(first, inventory.get(0));
        assertSame(second, inventory.get(1));
    }

    @Test
    void addDoesNotOverwriteExistingItem() {
        // Arrange
        Inventory inventory = new Inventory(2);
        Item first = makeItem("Sword");
        Item second = makeItem("Potion");

        inventory.add(first);

        // Act
        inventory.add(second);

        // Assert
        assertSame(first, inventory.get(0));
        assertSame(second, inventory.get(1));
    }

    @Test
    void addDoesNothingWhenInventoryIsFull() {
        // Arrange
        Inventory inventory = new Inventory(2);
        Item first = makeItem("Sword");
        Item second = makeItem("Potion");
        Item third = makeItem("Armor");

        inventory.add(first);
        inventory.add(second);

        // Act
        inventory.add(third);

        // Assert
        assertSame(first, inventory.get(0));
        assertSame(second, inventory.get(1));
        assertFalse(inventory.contains(third));
    }

    @Test
    void addFillsPreviouslyEmptySlotAfterRemoval() {
        // Arrange
        Inventory inventory = new Inventory(3);
        Item first = makeItem("Sword");
        Item second = makeItem("Potion");
        Item replacement = makeItem("Armor");

        inventory.add(first);
        inventory.add(second);
        inventory.remove(first);

        // Act
        inventory.add(replacement);

        // Assert
        assertSame(replacement, inventory.get(0));
        assertSame(second, inventory.get(1));
    }
}
