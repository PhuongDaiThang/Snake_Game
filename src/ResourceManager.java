public class ResourceManager {
    private static ResourceManager instance;

    // Private constructor to prevent instantiation from outside
    private ResourceManager() {
        // Initialize resource manager here (e.g., load images, sounds)
    }

    // Method to get the singleton instance
    public static ResourceManager getInstance() {
        if (instance == null) {
            instance = new ResourceManager();
        }
        return instance;
    }

    // Other methods to manage resources (e.g., loadImage, playSound)
}
