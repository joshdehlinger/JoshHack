package game;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Creature {
	private World world;

	public int x;
	public int y;
	public int z;

	private char glyph;

	/**
	 * Returns the character glyph that represents this Creature.
	 * 
	 * @return The character glyph representing the Creature.
	 */
	public char glyph() {
		return glyph;
	}

	private Color color;

	/**
	 * Returns the color of this Creature.
	 * 
	 * @return The color of the Creature.
	 */
	public Color color() {
		return color;
	}

	private CreatureAi ai;

	/**
	 * Sets the AI controller for this Creature.
	 * 
	 * @param ai The CreatureAi instance to control this Creature.
	 */
	public void setCreatureAi(CreatureAi ai) {
		this.ai = ai;
	}

	private int maxHp;

	/**
	 * Returns the maximum health points of this Creature.
	 * 
	 * @return The maximum health points value.
	 */
	public int maxHp() {
		return maxHp;
	}

	/**
	 * Modifies the maximum health points by a given amount.
	 * 
	 * @param amount The amount to add to the maximum health points.
	 */
	public void modifyMaxHp(int amount) {
		maxHp += amount;
	}

	private int hp;

	/**
	 * Returns the current health points of this Creature.
	 * 
	 * @return The current health points value.
	 */
	public int hp() {
		return hp;
	}

	private int attackValue;

	/**
	 * Modifies the base attack value by a given amount.
	 * 
	 * @param value The amount to add to the base attack value.
	 */
	public void modifyAttackValue(int value) {
		attackValue += value;
	}

	/**
	 * Calculates and returns the total attack value including weapon and armor
	 * bonuses.
	 * 
	 * @return The total attack value of the Creature.
	 */
	public int attackValue() {
		return attackValue
				+ (weapon == null ? 0 : weapon.attackValue())
				+ (armor == null ? 0 : armor.attackValue());
	}

	private int defenseValue;

	/**
	 * Modifies the base defense value by a given amount.
	 * 
	 * @param value The amount to add to the base defense value.
	 */
	public void modifyDefenseValue(int value) {
		defenseValue += value;
	}

	/**
	 * Calculates and returns the total defense value including weapon and armor
	 * bonuses.
	 * 
	 * @return The total defense value of the Creature.
	 */
	public int defenseValue() {
		return defenseValue
				+ (weapon == null ? 0 : weapon.defenseValue())
				+ (armor == null ? 0 : armor.defenseValue());
	}

	private int visionRadius;

	/**
	 * Modifies the vision radius by a given amount.
	 * 
	 * @param value The amount to add to the vision radius.
	 */
	public void modifyVisionRadius(int value) {
		visionRadius += value;
	}

	/**
	 * Returns the vision radius of this Creature.
	 * 
	 * @return The vision radius value.
	 */
	public int visionRadius() {
		return visionRadius;
	}

	private String name;

	/**
	 * Returns the name of this Creature.
	 * 
	 * @return The name string of the Creature.
	 */
	public String name() {
		return name;
	}

	private Inventory inventory;

	/**
	 * Returns the inventory object of this Creature.
	 * 
	 * @return The Inventory instance belonging to the Creature.
	 */
	public Inventory inventory() {
		return inventory;
	}

	private int maxFood;

	/**
	 * Returns the maximum food value (hunger capacity) of this Creature.
	 * 
	 * @return The maximum food value.
	 */
	public int maxFood() {
		return maxFood;
	}

	private int food;

	/**
	 * Returns the current food value (hunger level) of this Creature.
	 * 
	 * @return The current food value.
	 */
	public int food() {
		return food;
	}

	private Item weapon;

	/**
	 * Returns the currently equipped weapon item, or null if none.
	 * 
	 * @return The equipped weapon Item or null.
	 */
	public Item weapon() {
		return weapon;
	}

	private Item armor;

	/**
	 * Returns the currently equipped armor item, or null if none.
	 * 
	 * @return The equipped armor Item or null.
	 */
	public Item armor() {
		return armor;
	}

	private int xp;

	/**
	 * Returns the current experience points of this Creature.
	 * 
	 * @return The current experience points value.
	 */
	public int xp() {
		return xp;
	}

	/**
	 * Modifies the experience points by a given amount and handles level ups.
	 * 
	 * @param amount The amount to add to the experience points.
	 */
	public void modifyXp(int amount) {
		xp += amount;

		notify("You %s %d xp.", amount < 0 ? "lose" : "gain", amount);

		while (xp > (int) (Math.pow(level, 1.75) * 25)) {
			level++;
			doAction("advance to level %d", level);
			ai.onGainLevel();
			modifyHp(level * 2, "Died from having a negative level?");
		}
	}

	private int level;

	/**
	 * Returns the current level of this Creature.
	 * 
	 * @return The level integer value.
	 */
	public int level() {
		return level;
	}

	private int regenHpCooldown;
	private int regenHpPer1000;

	/**
	 * Modifies the health regeneration rate per 1000 time units by a given amount.
	 * 
	 * @param amount The amount to add to the regeneration rate.
	 */
	public void modifyRegenHpPer1000(int amount) {
		regenHpPer1000 += amount;
	}

	private List<Effect> effects;

	/**
	 * Returns the list of active effects currently applied to this Creature.
	 * 
	 * @return A list of Effects affecting the Creature.
	 */
	public List<Effect> effects() {
		return Collections.unmodifiableList(effects);
	}

	private int maxMana;

	/**
	 * Returns the maximum mana points of this Creature.
	 * 
	 * @return The maximum mana points value.
	 */
	public int maxMana() {
		return maxMana;
	}

	/**
	 * Modifies the maximum mana points by a given amount.
	 * 
	 * @param amount The amount to add to the maximum mana points.
	 */
	public void modifyMaxMana(int amount) {
		maxMana += amount;
	}

	private int mana;

	/**
	 * Returns the current mana points of this Creature.
	 * 
	 * @return The current mana points value.
	 */
	public int mana() {
		return mana;
	}

	/**
	 * Modifies the current mana points by a given amount, bounded by zero and
	 * maximum mana.
	 * 
	 * @param amount The amount to add to the current mana points.
	 */
	public void modifyMana(int amount) {
		mana = Math.max(0, Math.min(mana + amount, maxMana));
	}

	private int regenManaCooldown;
	private int regenManaPer1000;

	/**
	 * Modifies the mana regeneration rate per 1000 time units by a given amount.
	 * 
	 * @param amount The amount to add to the mana regeneration rate.
	 */
	public void modifyRegenManaPer1000(int amount) {
		regenManaPer1000 += amount;
	}

	private String causeOfDeath;

	/**
	 * Returns the cause of death string if this Creature is dead.
	 * 
	 * @return The cause of death string or null if alive.
	 */
	public String causeOfDeath() {
		return causeOfDeath;
	}

	/**
	 * Constructs a Creature with specified characteristics including appearance and
	 * stats.
	 * 
	 * @param world   The game World the Creature is part of.
	 * @param glyph   The character glyph representing the Creature visually.
	 * @param color   The color used to render the Creature.
	 * @param name    The name of the Creature.
	 * @param maxHp   The maximum health points.
	 * @param attack  The base attack value.
	 * @param defense The base defense value.
	 */
	public Creature(World world, char glyph, Color color, String name, int maxHp, int attack, int defense) {
		this.world = world;
		this.glyph = glyph;
		this.color = color;
		this.maxHp = maxHp;
		this.hp = maxHp;
		this.attackValue = attack;
		this.defenseValue = defense;
		this.visionRadius = 9;
		this.name = name;
		this.inventory = new Inventory(20);
		this.maxFood = 1000;
		this.food = maxFood / 3 * 2;
		this.level = 1;
		this.regenHpPer1000 = 10;
		this.effects = new ArrayList<Effect>();
		this.maxMana = 5;
		this.mana = maxMana;
		this.regenManaPer1000 = 20;
	}

	/**
	 * Attempts to move the Creature by specified offsets, handling interactions
	 * like stairs and combat.
	 * 
	 * @param mx Horizontal x-axis movement delta.
	 * @param my Horizontal y-axis movement delta.
	 * @param mz Vertical z-axis movement delta (level changes).
	 */
	public void moveBy(int mx, int my, int mz) {
		if (mx == 0 && my == 0 && mz == 0)
			return;

		Tile tile = world.tile(x + mx, y + my, z + mz);

		if (mz == -1) {
			if (tile == Tile.STAIRS_DOWN) {
				doAction("walk up the stairs to level %d", z + mz + 1);
			} else {
				doAction("try to go up but are stopped by the cave ceiling");
				return;
			}
		} else if (mz == 1) {
			if (tile == Tile.STAIRS_UP) {
				doAction("walk down the stairs to level %d", z + mz + 1);
			} else {
				doAction("try to go down but are stopped by the cave floor");
				return;
			}
		}

		Creature other = world.creature(x + mx, y + my, z + mz);

		modifyFood(-1);

		if (other == null)
			ai.onEnter(x + mx, y + my, z + mz, tile);
		else
			meleeAttack(other);
	}

	/**
	 * Performs a melee attack on another Creature.
	 * 
	 * @param other The target Creature to attack.
	 */
	public void meleeAttack(Creature other) {
		commonAttack(other, attackValue(), "attack the %s for %d damage", other.name);
	}

	/**
	 * Performs a throw attack using an item against a target Creature.
	 * 
	 * @param item  The Item to throw.
	 * @param other The target Creature to hit.
	 */
	private void throwAttack(Item item, Creature other) {
		commonAttack(other, attackValue / 2 + item.thrownAttackValue(), "throw a %s at the %s for %d damage",
				nameOf(item), other.name);
		other.addEffect(item.quaffEffect());
	}

	/**
	 * Performs an attack using the Creature's ranged weapon on a target Creature.
	 * 
	 * @param other The target Creature to attack.
	 */
	public void rangedWeaponAttack(Creature other) {
		commonAttack(other, attackValue / 2 + weapon.rangedAttackValue(), "fire a %s at the %s for %d damage",
				nameOf(weapon), other.name);
	}

	/**
	 * Executes a common attack sequence applying damage and notifying actions.
	 * 
	 * @param other  The target Creature to attack.
	 * @param attack The calculated attack value for damage computation.
	 * @param action The action message format string describing the attack.
	 * @param params Additional parameters referenced by the action message format
	 *               string.
	 */
	private void commonAttack(Creature other, int attack, String action, Object... params) {
		modifyFood(-2);

		int amount = Math.max(0, attack - other.defenseValue());

		amount = (int) (Math.random() * amount) + 1;

		Object[] params2 = new Object[params.length + 1];
		for (int i = 0; i < params.length; i++) {
			params2[i] = params[i];
		}
		params2[params2.length - 1] = amount;

		doAction(action, params2);

		other.modifyHp(-amount, "Killed by a " + name);

		if (other.hp < 1)
			gainXp(other);
	}

	/**
	 * Gains experience points based on another Creature's attributes after
	 * defeating it.
	 * 
	 * @param other The defeated Creature from which to derive experience points.
	 */
	public void gainXp(Creature other) {
		int amount = other.maxHp
				+ other.attackValue()
				+ other.defenseValue()
				- level;

		if (amount > 0)
			modifyXp(amount);
	}

	/**
	 * Modifies the current health points by a given amount and sets cause of death
	 * if applicable.
	 * 
	 * @param amount       The amount to add to the current health points (can be
	 *                     negative).
	 * @param causeOfDeath The cause of death message if this modification kills the
	 *                     Creature.
	 */
	public void modifyHp(int amount, String causeOfDeath) {
		hp = CombatRules.calculateNewHp(hp, maxHp, amount);
		this.causeOfDeath = causeOfDeath;

		if (CombatRules.isDead(hp)) {
			doAction("die");
			leaveCorpse();
			world.remove(this);
		}
	}

	/**
	 * Creates a corpse item at the Creature’s location and drops all inventory
	 * items upon death.
	 */
	private void leaveCorpse() {
		Item corpse = new Item('%', color, name + " corpse", null);
		corpse.modifyFoodValue(maxHp * 5);
		world.addAtEmptySpace(corpse, x, y, z);
		for (Item item : inventory.getItems()) {
			if (item != null)
				drop(item);
		}
	}

	/**
	 * Digs at a specified world coordinate and reduces food due to exertion.
	 * 
	 * @param wx The world x-coordinate where digging occurs.
	 * @param wy The world y-coordinate where digging occurs.
	 * @param wz The world z-coordinate where digging occurs.
	 */
	public void dig(int wx, int wy, int wz) {
		modifyFood(-10);
		world.dig(wx, wy, wz);
		doAction("dig");
	}

	/**
	 * Updates the Creature status each tick including food, health, mana, effects,
	 * and AI actions.
	 */
	public void update() {
		modifyFood(-1);
		regenerateHealth();
		regenerateMana();
		updateEffects();
		ai.onUpdate();
	}

	/**
	 * Updates all active effects on the Creature and removes those that have ended.
	 */
	private void updateEffects() {
		List<Effect> done = new ArrayList<Effect>();

		for (Effect effect : effects) {
			effect.update(this);
			if (effect.isDone()) {
				effect.end(this);
				done.add(effect);
			}
		}

		effects.removeAll(done);
	}

	/**
	 * Handles health regeneration over time if conditions permit.
	 */
	private void regenerateHealth() {
		regenHpCooldown -= regenHpPer1000;
		if (regenHpCooldown < 0) {
			if (hp < maxHp) {
				modifyHp(1, "Died from regenerating health?");
				modifyFood(-1);
			}
			regenHpCooldown += 1000;
		}
	}

	/**
	 * Handles mana regeneration over time if conditions permit.
	 */
	private void regenerateMana() {
		regenManaCooldown -= regenManaPer1000;
		if (regenManaCooldown < 0) {
			if (mana < maxMana) {
				modifyMana(1);
				modifyFood(-1);
			}
			regenManaCooldown += 1000;
		}
	}

	/**
	 * Determines if the Creature can enter a specified coordinate, checking for
	 * ground and absence of other creatures.
	 * 
	 * @param wx Target world x-coordinate.
	 * @param wy Target world y-coordinate.
	 * @param wz Target world z-coordinate.
	 * @return True if the location is enterable; false otherwise.
	 */
	public boolean canEnter(int wx, int wy, int wz) {
		return world.tile(wx, wy, wz).isGround() && world.creature(wx, wy, wz) == null;
	}

	/**
	 * Sends a formatted notification message to the Creature's AI.
	 * 
	 * @param message The message format string.
	 * @param params  Parameters to format into the message.
	 */
	public void notify(String message, Object... params) {
		ai.onNotify(String.format(message, params));
	}

	/**
	 * Notifies nearby creatures of this Creature's action with a descriptive
	 * message.
	 * 
	 * @param message The action message format string.
	 * @param params  Parameters to format into the action message.
	 */
	public void doAction(String message, Object... params) {
		for (Creature other : getCreaturesWhoSeeMe()) {
			if (other == this) {
				other.notify("You " + message + ".", params);
			} else {
				other.notify(String.format("The %s %s.", name, makeSecondPerson(message)), params);
			}
		}
	}

	/**
	 * Notifies nearby creatures of an action involving an item and updates the AI's
	 * knowledge of the item.
	 * 
	 * @param item    The involved Item.
	 * @param message The action message format string.
	 * @param params  Parameters to format into the action message.
	 */
	public void doAction(Item item, String message, Object... params) {
		if (hp < 1)
			return;

		for (Creature other : getCreaturesWhoSeeMe()) {
			if (other == this) {
				other.notify("You " + message + ".", params);
			} else {
				other.notify(String.format("The %s %s.", name, makeSecondPerson(message)), params);
			}
			other.learnName(item);
		}
	}

	/**
	 * Returns a list of creatures within vision radius who can observe this
	 * Creature.
	 * 
	 * @return A list of Creatures that can see this Creature.
	 */
	private List<Creature> getCreaturesWhoSeeMe() {
		List<Creature> others = new ArrayList<Creature>();
		int r = 9;
		for (int ox = -r; ox < r + 1; ox++) {
			for (int oy = -r; oy < r + 1; oy++) {
				if (ox * ox + oy * oy > r * r)
					continue;

				Creature other = world.creature(x + ox, y + oy, z);

				if (other == null)
					continue;

				others.add(other);
			}
		}
		return others;
	}

	/**
	 * Converts a message string into second person singular form for others'
	 * perception.
	 * 
	 * @param text The message text to convert.
	 * @return The converted second-person verb phrase string.
	 */
	private String makeSecondPerson(String text) {
		String[] words = text.split(" ");
		words[0] = words[0] + "s";

		StringBuilder builder = new StringBuilder();
		for (String word : words) {
			builder.append(" ");
			builder.append(word);
		}

		return builder.toString().trim();
	}

	/**
	 * Determines if this Creature can see a specific coordinate in the world.
	 * 
	 * @param wx World x-coordinate to check visibility.
	 * @param wy World y-coordinate to check visibility.
	 * @param wz World z-coordinate to check visibility.
	 * @return True if the coordinate is visible; false otherwise.
	 */
	public boolean canSee(int wx, int wy, int wz) {
		return (detectCreatures > 0 && world.creature(wx, wy, wz) != null
				|| ai.canSee(wx, wy, wz));
	}

	/**
	 * Returns the actual tile at specified world coordinates without visibility
	 * restrictions.
	 * 
	 * @param wx World x-coordinate.
	 * @param wy World y-coordinate.
	 * @param wz World z-coordinate.
	 * @return The Tile present at the given coordinates.
	 */
	public Tile realTile(int wx, int wy, int wz) {
		return world.tile(wx, wy, wz);
	}

	/**
	 * Returns the tile at specified world coordinates considering visibility and
	 * memory.
	 * 
	 * @param wx World x-coordinate.
	 * @param wy World y-coordinate.
	 * @param wz World z-coordinate.
	 * @return The Tile known or remembered at the given coordinates.
	 */
	public Tile tile(int wx, int wy, int wz) {
		if (canSee(wx, wy, wz))
			return world.tile(wx, wy, wz);
		else
			return ai.rememberedTile(wx, wy, wz);
	}

	/**
	 * Returns the Creature at specified coordinates if visible, otherwise null.
	 * 
	 * @param wx World x-coordinate.
	 * @param wy World y-coordinate.
	 * @param wz World z-coordinate.
	 * @return The Creature at the location if visible; null otherwise.
	 */
	public Creature creature(int wx, int wy, int wz) {
		if (canSee(wx, wy, wz))
			return world.creature(wx, wy, wz);
		else
			return null;
	}

	/**
	 * Attempts to pick up an item at the Creature's location into its inventory.
	 */
	public void pickup() {
		Item item = world.item(x, y, z);

		if (inventory.isFull() || item == null) {
			doAction("grab at the ground");
		} else {
			doAction("pickup a %s", nameOf(item));
			world.remove(x, y, z);
			inventory.add(item);
		}
	}

	/**
	 * Drops a specified item from inventory at the Creature's location or nearby
	 * empty space.
	 * 
	 * @param item The item to drop.
	 */
	public void drop(Item item) {
		if (world.addAtEmptySpace(item, x, y, z)) {
			doAction("drop a " + nameOf(item));
			inventory.remove(item);
			unequip(item);
		} else {
			notify("There's nowhere to drop the %s.", nameOf(item));
		}
	}

	/**
	 * Modifies the Creature's food value by an amount and handles consequences like
	 * overeating or starvation.
	 * 
	 * @param amount The amount to add to the current food value.
	 */
	public void modifyFood(int amount) {
		food += amount;

		if (food > maxFood) {
			maxFood = (maxFood + food) / 2;
			food = maxFood;
			notify("You can't belive your stomach can hold that much!");
			modifyHp(-1, "Killed by overeating.");
		} else if (food < 1 && isPlayer()) {
			modifyHp(-1000, "Starved to death.");
		}
	}

	/**
	 * Determines whether this Creature is the player character.
	 * 
	 * @return True if the Creature is the player; false otherwise.
	 */
	public boolean isPlayer() {
		return glyph == '@';
	}

	/**
	 * Consumes a food item and applies its effects.
	 * 
	 * @param item The food item to eat.
	 */
	public void eat(Item item) {
		doAction("eat a " + nameOf(item));
		consume(item);
	}

	/**
	 * Consumes a potion or drink item and applies its effects.
	 * 
	 * @param item The potion or drink item to quaff.
	 */
	public void quaff(Item item) {
		doAction("quaff a " + nameOf(item));
		consume(item);
	}

	/**
	 * Consumes an item applying its food value and effects, then removes it from
	 * inventory.
	 * 
	 * @param item The item to consume.
	 */
	private void consume(Item item) {
		if (item.foodValue() < 0)
			notify("Gross!");

		addEffect(item.quaffEffect());

		modifyFood(item.foodValue());
		getRidOf(item);
	}

	/**
	 * Adds a new effect to this Creature and starts it.
	 * 
	 * @param effect The effect to add and start.
	 */
	private void addEffect(Effect effect) {
		if (effect == null)
			return;

		effect.start(this);
		effects.add(effect);
	}

	/**
	 * Removes an item from inventory and unequips it if equipped.
	 * 
	 * @param item The item to remove.
	 */
	private void getRidOf(Item item) {
		inventory.remove(item);
		unequip(item);
	}

	/**
	 * Places an item at specified world coordinates, removing it from inventory and
	 * unequipping it.
	 * 
	 * @param item The item to place.
	 * @param wx   World x-coordinate where to place the item.
	 * @param wy   World y-coordinate where to place the item.
	 * @param wz   World z-coordinate where to place the item.
	 */
	private void putAt(Item item, int wx, int wy, int wz) {
		inventory.remove(item);
		unequip(item);
		world.addAtEmptySpace(item, wx, wy, wz);
	}

	/**
	 * Unequips the specified item if it is currently equipped as weapon or armor.
	 * 
	 * @param item The item to unequip.
	 */
	public void unequip(Item item) {
		if (item == null)
			return;

		if (item == armor) {
			if (hp > 0)
				doAction("remove a " + nameOf(item));
			armor = null;
		} else if (item == weapon) {
			if (hp > 0)
				doAction("put away a " + nameOf(item));
			weapon = null;
		}
	}

	/**
	 * Equips an item from inventory as weapon or armor based on its attributes.
	 * 
	 * @param item The item to equip.
	 */
	public void equip(Item item) {
		if (!inventory.contains(item)) {
			if (inventory.isFull()) {
				notify("Can't equip %s since you're holding too much stuff.", nameOf(item));
				return;
			} else {
				world.remove(item);
				inventory.add(item);
			}
		}

		if (item.attackValue() == 0 && item.rangedAttackValue() == 0 && item.defenseValue() == 0)
			return;

		if (item.attackValue() + item.rangedAttackValue() >= item.defenseValue()) {
			unequip(weapon);
			doAction("wield a " + nameOf(item));
			weapon = item;
		} else {
			unequip(armor);
			doAction("put on a " + nameOf(item));
			armor = item;
		}
	}

	/**
	 * Returns the item at specified coordinates if visible; otherwise null.
	 * 
	 * @param wx World x-coordinate to query.
	 * @param wy World y-coordinate to query.
	 * @param wz World z-coordinate to query.
	 * @return The Item at the specified location or null if not visible.
	 */
	public Item item(int wx, int wy, int wz) {
		if (canSee(wx, wy, wz))
			return world.item(wx, wy, wz);
		else
			return null;
	}

	/**
	 * Returns a formatted string summarizing the Creature's level, attack, defense,
	 * and current health.
	 * 
	 * @return A string containing the Creature's detailed status.
	 */
	public String details() {
		return String.format("  level:%d  attack:%d  defense:%d  hp:%d", level, attackValue(), defenseValue(), hp);
	}

	/**
	 * Throws an item towards specified coordinates, potentially hitting a Creature
	 * or landing on the ground.
	 * 
	 * @param item The item to throw.
	 * @param wx   Target world x-coordinate.
	 * @param wy   Target world y-coordinate.
	 * @param wz   Target world z-coordinate.
	 */
	public void throwItem(Item item, int wx, int wy, int wz) {
		Point end = new Point(x, y, 0);

		for (Point p : new Line(x, y, wx, wy)) {
			if (!realTile(p.x, p.y, z).isGround())
				break;
			end = p;
		}

		wx = end.x;
		wy = end.y;

		Creature c = creature(wx, wy, wz);

		if (c != null)
			throwAttack(item, c);
		else
			doAction("throw a %s", nameOf(item));

		if (item.quaffEffect() != null && c != null)
			getRidOf(item);
		else
			putAt(item, wx, wy, wz);
	}

	/**
	 * Adds another Creature into the world at its current location.
	 * 
	 * @param other The Creature to summon into the world.
	 */
	public void summon(Creature other) {
		world.add(other);
	}

	private int detectCreatures;

	/**
	 * Modifies the detection radius or ability for creatures by a given amount.
	 * 
	 * @param amount The amount to add to the detection ability.
	 */
	public void modifyDetectCreatures(int amount) {
		detectCreatures += amount;
	}

	/**
	 * Casts a spell at specified coordinates if enough mana is available and a
	 * creature is present.
	 * 
	 * @param spell The spell to cast.
	 * @param x2    Target x-coordinate.
	 * @param y2    Target y-coordinate.
	 */
	public void castSpell(Spell spell, int x2, int y2) {
		Creature other = creature(x2, y2, z);

		if (spell.manaCost() > mana) {
			doAction("point and mumble but nothing happens");
			return;
		} else if (other == null) {
			doAction("point and mumble at nothing");
			return;
		}

		other.addEffect(spell.effect());
		modifyMana(-spell.manaCost());
	}

	/**
	 * Returns the name of an item as known by the Creature's AI.
	 * 
	 * @param item The item for which to get the name.
	 * @return The known name string of the item.
	 */
	public String nameOf(Item item) {
		return ai.getName(item);
	}

	/**
	 * Updates the Creature's AI with the actual name of an item and notifies the
	 * Creature.
	 * 
	 * @param item The item whose name is learned.
	 */
	public void learnName(Item item) {
		notify("The " + item.appearance() + " is a " + item.name() + "!");
		ai.setName(item, item.name());
	}
}
