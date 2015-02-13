package de.pdinklag.minecraft.world;

import de.pdinklag.minecraft.nbt.CompoundTag;
import de.pdinklag.minecraft.nbt.NBT;
import de.pdinklag.minecraft.nbt.marshal.NBTMarshal;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

/**
 * Represents a region, consisting of 32x32 chunks.
 */
class Region {
    private static final Logger LOGGER = Logger.getLogger("Region");

    static final int CHUNKS = 32;
    static final int BLOCKS = CHUNKS * Chunk.BLOCKS;

    private static int blockToChunk(int b) {
        b %= BLOCKS;
        if (b < 0) {
            b += BLOCKS;
        }

        return b / CHUNKS;
    }

    private final Path file;

    private long[][] chunkFileOffsets = new long[CHUNKS][CHUNKS];
    private Chunk[][] chunks = new Chunk[CHUNKS][CHUNKS];

    private transient boolean dirty = false;

    /**
     * Constructs a new, empty region.
     */
    Region(Path file) throws IOException {
        this.file = file;

        if (Files.exists(file)) {
            try (DataInputStream in = new DataInputStream(Files.newInputStream(file))) {
                for (int z = 0; z < CHUNKS; z++) {
                    for (int x = 0; x < CHUNKS; x++) {
                        final int location = in.readInt();
                        chunkFileOffsets[x][z] = (location >> 8) << 12;
                    }
                }
            }

            dirty = false;
        } else {
            dirty = true;
        }
    }

    boolean isDirty() {
        return dirty;
    }

    void save() throws IOException {
        throw new UnsupportedOperationException("not yet implemented");
    }

    private Chunk getChunkAt(int x, int z) {
        x = blockToChunk(x);
        z = blockToChunk(z);

        Chunk chunk = chunks[x][z];
        if (chunk == null) {

            if (file != null && chunkFileOffsets[x][z] > 0) {
                //load chunk
                LOGGER.log(Level.INFO, "Loading relative chunk (" + x + ", " + z + ")");
                try (final RandomAccessFile raf = new RandomAccessFile(file.toString(), "r")) {
                    raf.seek(chunkFileOffsets[x][z]);
                    final int size = raf.readInt();
                    final int compression = raf.readByte();

                    final byte[] data = new byte[size - 1];
                    raf.read(data);

                    final CompoundTag chunkNbt;
                    try (final ByteArrayInputStream bis = new ByteArrayInputStream(data)) {
                        switch (compression) {
                            case 1:
                                chunkNbt = NBT.loadDirect(new GZIPInputStream(bis));
                                break;

                            case 2:
                                chunkNbt = NBT.loadDirect(new InflaterInputStream(bis));
                                break;

                            default:
                                throw new UnsupportedOperationException(
                                        "Unsupported compression type " + compression);
                        }
                    }

                    chunk = NBTMarshal.unmarshal(Chunk.class, chunkNbt.getCompound("Level"));
                } catch (IOException ex) {
                    throw new WorldException("Failed to load chunk", ex);
                }
            } else {
                chunk = new Chunk();
            }

            chunks[x][z] = chunk;
        }

        return chunk;
    }

    Block getBlock(int x, int y, int z) {
        return getChunkAt(x, z).getBlock(x, y, z);
    }

    void setBlock(int x, int y, int z, Block block) {
        final Chunk chunk = getChunkAt(x, z);
        chunk.setBlock(x, y, z, block);

        dirty = (dirty || chunk.isDirty());
    }
}
