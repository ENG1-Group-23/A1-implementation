package main.java.bytemusketeers.heslingtonhustle.Player;

public class Metrics {
    private int happinessLevel;

    public Metrics() {
        // Initialize the happiness level
        happinessLevel = 100; // Set an initial value
    }

    public void itemPickedUp(int num) {
        System.out.println("You've picked up an item!!!");
        // Adjust the happiness level based on the type of item
        if (num == 0) {
            happinessLevel -= 10; // Adjust as needed
            // Add more cases for other item types
        } else {// Default adjustment for unknown items
            happinessLevel -= 5; // Adjust as needed
        }

        // Print the current happiness level
        System.out.println("Player's Happiness Level: " + happinessLevel);
    }

    public int getHappinessLevel() {
        return happinessLevel;
    }

}

