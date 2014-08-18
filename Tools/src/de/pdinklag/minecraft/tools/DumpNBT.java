package de.pdinklag.minecraft.tools;

import de.pdinklag.minecraft.nbt.NBT;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Very simple utility to dump an NBT file into the standard output as readable text.
 */
public class DumpNBT {
    /**
     * Runs the application.
     *
     * @param args the first and only argument is the NBT file to read (e.g. a player data file).
     */
    public static void main(String[] args) throws Throwable {
        if (args.length == 1) {
            dumpNBT(Paths.get(args[0]));
        } else {
            System.out.println("Usage: DumpNBT <filename>");
        }
    }

    private static void dumpNBT(Path path) throws IOException {
        System.out.println(NBT.load(path).toString());
    }
}
