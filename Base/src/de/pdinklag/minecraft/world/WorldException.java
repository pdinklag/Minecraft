package de.pdinklag.minecraft.world;

/**
 * Exception type for world related issues.
 */
public class WorldException extends RuntimeException {
    public WorldException(String message) {
        super(message);
    }

    public WorldException(String message, Throwable cause) {
        super(message, cause);
    }

    public WorldException(Throwable cause) {
        super(cause);
    }
}
