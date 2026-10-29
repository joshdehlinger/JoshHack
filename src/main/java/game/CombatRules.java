package game;

public class CombatRules {

    /**
     * Calculates the new health points after applying a change, ensuring the result
     * stays between zero and the maximum health points.
     * 
     * @param currentHp the current health points of the character
     * @param maxHp     the maximum health points the character can have
     * @param amount    the amount to change the current health points by, positive
     *                  or negative
     * @return the adjusted health points constrained between zero and maximum
     *         health points inclusive
     */
    public static int calculateNewHp(int currentHp, int maxHp, int amount) {
        int newHp = currentHp + amount;

        if (newHp > maxHp)
            return maxHp;

        if (newHp < 0)
            return 0;

        return newHp;
    }

    /**
     * Determines whether a character is dead based on their health points.
     * 
     * @param hp the current health points of the character
     * @return true if health points are less than one, indicating death; false
     *         otherwise
     */
    public static boolean isDead(int hp) {
        return hp < 1;
    }
}
