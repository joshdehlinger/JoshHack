package game;

public class Inventory {

	private Item[] items;

	/**
	 * Returns the array of items stored in the inventory.
	 * 
	 * @return The array of items currently in the inventory.
	 */
	public Item[] getItems() {
		return items;
	}

	/**
	 * Returns the item at the specified index in the inventory.
	 * 
	 * @param i The index of the desired item.
	 * @return The item located at the specified index in the inventory.
	 */
	public Item get(int i) {
		return items[i];
	}

	/**
	 * Constructs an inventory with a specified maximum capacity.
	 * 
	 * @param max The maximum number of items the inventory can hold.
	 */
	public Inventory(int max) {
		items = new Item[max];
	}

	/**
	 * Adds an item to the first available slot in the inventory.
	 * 
	 * @param item The item to be added to the inventory.
	 */
	public void add(Item item) {
		for (int i = 0; i < items.length; i++) {
			if (items[i] == null) {
				items[i] = item;
				break;
			}
		}
	}

	/**
	 * Removes the specified item from the inventory if it exists.
	 * 
	 * @param item The item to be removed from the inventory.
	 */
	public void remove(Item item) {
		for (int i = 0; i < items.length; i++) {
			if (items[i] == item) {
				items[i] = null;
				return;
			}
		}
	}

	/**
	 * Determines whether the inventory has no empty slots remaining.
	 * 
	 * @return True if the inventory is full; false otherwise.
	 */
	public boolean isFull() {
		int size = 0;
		for (int i = 0; i < items.length; i++) {
			if (items[i] != null)
				size++;
		}
		return size == items.length;
	}

	/**
	 * Checks if the inventory contains the specified item.
	 * 
	 * @param item The item to check for presence in the inventory.
	 * @return True if the item is found in the inventory; false otherwise.
	 */
	public boolean contains(Item item) {
		for (Item i : items) {
			if (i == item)
				return true;
		}
		return false;
	}
}
