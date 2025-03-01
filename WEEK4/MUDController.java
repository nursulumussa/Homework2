import java.util.Scanner;
import com.T.mud.player.Player;
import com.T.mud.room.Room;
import com.T.mud.item.Item;

public class MUDController {
    private final Player player;
    private boolean running;

    public MUDController(Player player) {
        this.player = player;
        this.running = true;
    }

    public void runGameLoop() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to MUD! Type 'help' for a list of commands.");
        while (running) {
            System.out.print("> ");
            String input = scanner.nextLine().trim().toLowerCase();
            handleInput(input);
        }
        scanner.close();
    }

    public void handleInput(String input) {
        String[] parts = input.split(" ", 2);
        String command = parts[0];
        String argument = parts.length > 1 ? parts[1] : "";

        switch (command) {
            case "look":
                lookAround();
                break;
            case "move":
                move(argument);
                break;
            case "pick":
                if (argument.startsWith("up ")) {
                    pickUp(argument.substring(3));
                } else {
                    System.out.println("Invalid command. Did you mean 'pick up <item>'?");
                }
                break;
            case "inventory":
                checkInventory();
                break;
            case "help":
                showHelp();
                break;
            case "quit":
            case "exit":
                System.out.println("Exiting the game. Goodbye!");
                running = false;
                break;
            default:
                System.out.println("Unknown command. Type 'help' for a list of commands.");
                break;
        }
    }

    private void lookAround() {
        Room currentRoom = player.getCurrentRoom();
        if (currentRoom != null) {
            System.out.println(currentRoom.getDescription());
            System.out.println("Items here: " + currentRoom.listItems());
        } else {
            System.out.println("You are in an unknown place.");
        }
    }

    private void move(String direction) {
        if (direction.isEmpty()) {
            System.out.println("Where to move? Use: move <direction>");
            return;
        }
        Room nextRoom = player.getCurrentRoom().getConnectedRoom(direction);
        if (nextRoom != null) {
            player.setCurrentRoom(nextRoom);
            System.out.println("You moved " + direction + ".");
            lookAround();
        } else {
            System.out.println("You cannot go that way!");
        }
    }

    private void pickUp(String itemName) {
        Room currentRoom = player.getCurrentRoom();
        Item item = currentRoom.getItem(itemName);
        if (item != null) {
            player.addItemToInventory(item);
            currentRoom.removeItem(item);
            System.out.println("You picked up " + itemName + ".");
        } else {
            System.out.println("There is no item named '" + itemName + "' here!");
        }
    }

    private void checkInventory() {
        System.out.println("You have in your inventory:");
        System.out.println(player.listInventory());
    }

    private void showHelp() {
        System.out.println("Available commands:");
        System.out.println("look - Describe the current room");
        System.out.println("move <direction> - Move in the specified direction (forward, back, left, right)");
        System.out.println("pick up <item> - Pick up an item");
        System.out.println("inventory - Show your inventory");
        System.out.println("help - Show this message");
        System.out.println("quit / exit - Exit the game");
    }
}
