package de.pdinklag.minecraft.tools;

import de.pdinklag.minecraft.nbt.CompoundTag;
import de.pdinklag.minecraft.nbt.ListTag;
import de.pdinklag.minecraft.nbt.NBT;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Utility for porting players from one world into another world.
 * <p/>
 * It copies the player data of the source world into the target world and teleports them
 * to the target world's spawn.
 */
public class PortPlayers {
    /**
     * Runs the application.
     *
     * @param args the first argument is the path to the SOURCE world, the second argument is the
     *             path to the TARGET world.
     */
    public static void main(String[] args) throws Throwable {
        if (args.length == 2) {
            retainInventory(Paths.get(args[0]), Paths.get(args[1]));
        } else {
            System.out.println("PortPlayers --  Port players from one world into a new world.\n\n"
                            + "Everything attached to their data (inventory, experience, ender chest content,\n"
                            + "achievements, etc.) will be retained.\n\n"
                            + "They will be teleported to the new world's spawn point and have some internal\n"
                            + "flags reset to make sure they are safe.\n\n"
                            + "Usage: PortPlayers <SOURCE world> <TARGET world>"
            );
        }
    }

    private static void retainInventory(final Path srcWorldPath, final Path dstWorldPath) throws IOException {
        System.out.println("Starting inventory retention from '" + srcWorldPath + "' to '" + dstWorldPath + "' ...");

        final int[] dstSpawn = findWorldSpawn(dstWorldPath);
        System.out.println("Spawn of new world is " + dstSpawn[0] + ", " + dstSpawn[1] + ", " + dstSpawn[2] + ".");
        System.out.println("Processing players of source world ...");

        final Path srcPlayerDataPath = srcWorldPath.resolve("playerdata");
        final Path srcStatsPath = srcWorldPath.resolve("stats");

        final Path dstPlayerDataPath = dstWorldPath.resolve("playerdata");
        final Path dstStatsPath = dstWorldPath.resolve("stats");

        //Make sure the target directories exist
        try {
            Files.createDirectory(dstPlayerDataPath);
        } catch(FileAlreadyExistsException ex) {
            //seriously?
        }

        try {
            Files.createDirectory(dstStatsPath);
        } catch(FileAlreadyExistsException ex) {
            //...
        }

        Files.walkFileTree(srcPlayerDataPath, new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                //apparently, the server sometimes creates player data files with no content...
                if (attrs.size() > 0) {
                    //Load player NBT
                    System.out.println();
                    System.out.println("Loading " + file.toString() + " ...");
                    final CompoundTag player = NBT.load(file);

                    System.out.println("Moving into new world ...");

                    //Get 'em out of any alternate dimension
                    player.put("Dimension", 0);

                    //Teleport to spawn
                    player.put("Pos", new ListTag(
                            (double) dstSpawn[0],
                            (double) dstSpawn[1],
                            (double) dstSpawn[2]));

                    //Force new spawn position
                    player.put("SpawnX", dstSpawn[0]);
                    player.put("SpawnY", dstSpawn[1]);
                    player.put("SpawnZ", dstSpawn[2]);
                    player.put("SpawnForced", true);

                    //Reset some stuff
                    player.put("Motion", new ListTag(0.0, 0.0, 0.0));
                    player.put("Rot", new ListTag(0.0f, 0.0f));
                    player.put("OnGround", true);
                    player.put("FallDistance", 0.0f);
                    player.put("Sleeping", false);
                    player.put("Fire", (short) -20); //-20 is default according to Minecraft Wiki

                    player.remove("Riding");
                    player.remove("ActiveEffects");

                    //Save NBT
                    final String fileName = file.getFileName().toString();
                    final String playerId = fileName.substring(0, fileName.lastIndexOf('.'));
                    final String jsonFileName = playerId + ".json";

                    final Path destFile = dstPlayerDataPath.resolve(file.getFileName().toString());
                    System.out.println("-> " + destFile.toString());
                    NBT.save(destFile, player);

                    final Path srcStatFile = srcStatsPath.resolve(jsonFileName);
                    if (Files.exists(srcStatFile)) {
                        final Path destJson = dstStatsPath.resolve(jsonFileName);
                        Files.copy(srcStatFile, destJson);
                        System.out.println("-> " + destJson.toString());
                    }
                }

                return FileVisitResult.CONTINUE;

            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                if (exc != null) {
                    exc.printStackTrace();
                }

                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                if (exc != null) {
                    exc.printStackTrace();
                }

                return FileVisitResult.CONTINUE;
            }
        });
    }

    private static int[] findWorldSpawn(Path worldPath) throws IOException {
        final CompoundTag level = NBT.load(worldPath.resolve("level.dat"));
        final CompoundTag data = level.getCompound("Data");

        return new int[]{
                data.getInt("SpawnX"),
                data.getInt("SpawnY"),
                data.getInt("SpawnZ")};
    }
}
