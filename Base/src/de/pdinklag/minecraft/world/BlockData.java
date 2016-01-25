package de.pdinklag.minecraft.world;

/**
 * Block data associated to different blocks as of Minecraft version {@code 14w33c}.
 */
public final class BlockData {
	public final static byte NONE = 0x00;
	
	public final class WOODPLANKS {
		public final static byte OAK = 0x00;
		public final static byte SPRUCE = 0x01;
		public final static byte BIRCH = 0x02;
		public final static byte JUNGLE = 0x03;
		public final static byte ACACIA = 0x04;
		public final static byte DARKOAK = 0x05;
	}
	public final class STONE {
		public final static byte STONE = 0x00;
		public final static byte GRANITE = 0x01;
		public final static byte POLISHED_GRANITE = 0x02;
		public final static byte DIORITE = 0x03;
		public final static byte POLISHED_DIORITE = 0x04;
		public final static byte ANDESITE = 0x05;
		public final static byte POLISHED_ANDESITE = 0x06;
	}
	public final class DIRT {
		public final static byte DIRT = 0x00;
		public final static byte COARSE_DIRT = 0x01;
		public final static byte PODZOL = 0x02;
	}
	public final class SAPLINGS {
		public final static byte OAK = 0x00;
		public final static byte SPRUCE = 0x01;
		public final static byte BIRCH = 0x02;
		public final static byte JUNGLE = 0x03;
		public final static byte ACACIA = 0x04;
		public final static byte DARKOAK = 0x05;
		public final static byte READY_TO_GROW = 0x08;
	}
	public final class FLOWING { //for FLOWING_WATER and FLOWING_LAVA
		public final static byte LEVEL_HIGHEST = 0x00;
		public final static byte LEVEL_7 = 0x01;
		public final static byte LEVEL_6 = 0x02;
		public final static byte LEVEL_5 = 0x03;
		public final static byte LEVEL_4 = 0x04;
		public final static byte LEVEL_3 = 0x05;
		public final static byte LEVEL_2 = 0x06;
		public final static byte LEVEL_1 = 0x07;
		public final static byte LEVEL_FALLING = 0x08;
	}
	public final class SAND {
		public final static byte SAND = 0x00;
		public final static byte RED_SAND = 0x01;
	}
	public final class LOG {
		public final static byte OAK = 0x00;
		public final static byte SPRUCE = 0x01;
		public final static byte BIRCH = 0x02;
		public final static byte JUNGLE = 0x03;
		public final static byte ORIENTATION_UD = 0x00;
		public final static byte ORIENTATION_EW = 0x04;
		public final static byte ORIENTATION_NS = 0x08;
		public final static byte ORIENTATION_BARK = 0x0C;
	}
	public final class LOG2 {
		public final static byte ACACIA = 0x00;
		public final static byte DARKOAK = 0x01;
		public final static byte ORIENTATION_UD = 0x00;
		public final static byte ORIENTATION_EW = 0x04;
		public final static byte ORIENTATION_NS = 0x08;
		public final static byte ORIENTATION_BARK = 0x0C;
	}
	public final class LEAVES {
		public final static byte OAK = 0x00;
		public final static byte SPRUCE = 0x01;
		public final static byte BIRCH = 0x02;
		public final static byte JUNGLE = 0x03;
		public final static byte NO_DECAY = 0x04;
		public final static byte CHECK_DECAY = 0x08;
	}
	public final class LEAVES2 {
		public final static byte ACACIA = 0x00;
		public final static byte DARKOAK = 0x01;
		public final static byte NO_DECAY = 0x04;
		public final static byte CHECK_DECAY = 0x08;
	}

	public final class COLORS { // for WOOL, STAINED CLAY, STAINED GLASS and CARPET
		public final static byte WHITE = 0x00;
		public final static byte ORANGE = 0x01;
		public final static byte MAGENTA = 0x02;
		public final static byte LIGHT_BLUE = 0x03;
		public final static byte YELLOW = 0x04;
		public final static byte LIME = 0x05;
		public final static byte PINK = 0x06;
		public final static byte GRAY = 0x07;
		public final static byte LIGHT_GRAY = 0x08;
		public final static byte CYAN = 0x09;
		public final static byte PURPLE = 0x0A;
		public final static byte BLUE = 0x0B;
		public final static byte BROWN = 0x0C;
		public final static byte GREEN = 0x0D;
		public final static byte RED = 0x0E;
		public final static byte BLACK = 0x0F;
	}
	public final class TORCHES { // also for REDSTONE TORCHES
		public final static byte FACING_EAST = 0x01;
		public final static byte FACING_WEST = 0x02;
		public final static byte FACING_SOUTH = 0x03;
		public final static byte FACING_NORTH = 0x04;
		public final static byte FACING_UP = 0x05;
	}
	public final class SLABS {
		public final static byte RIGHT_SIDE_UP = 0x00;
		public final static byte UPSIDE_DOWN = 0x08;
	}
	public final class STONESLABS { /* common to stoneslab and double stoneslab */
		public final static byte STONE = 0x00;
		public final static byte SANDSTONE = 0x01;
		public final static byte WOODEN = 0x02;
		public final static byte COBBLESTONE = 0x03;
		public final static byte BRICKS = 0x04;
		public final static byte STONE_BRICK = 0x05;
		public final static byte NETHER_BRICK = 0x06;
		public final static byte QUARTZ = 0x07;
		public final static byte SMOOTH_STONE_DOUBLEONLY = 0x08;
		public final static byte SMOOTH_SANDSTONE_DOUBLEONLY = 0x09;
		public final static byte TILE_QUARTZ_DOUBLEONLY = 0x07;
	}
	public final class STONESLABS2 { /* common to stoneslab2 and double stoneslab2 */
		public final static byte REDSTONE = 0x00;
		public final static byte SMOOTH_REDSTONE_DOUBLEONLY = 0x08;
	}
	public final class WOODENSLABS { /* common to woodenslab and double woodenslab */
		public final static byte OAK = 0x00;
		public final static byte SPRUCE = 0x01;
		public final static byte BIRCH = 0x02;
		public final static byte JUNGLE = 0x03;
		public final static byte ACACIA = 0x04;
		public final static byte DARK_OAK = 0x05;
	}
	public final class SANDSTONES { /* common to sandstone and red sandstone */
		public final static byte SANDSTONE = 0x00;
		public final static byte CHISELED_SANDSTONE = 0x01;
		public final static byte SMOOTH_SANDSTONE = 0x02;
	}
	public final class BED {
		public final static byte FACING_SOUTH = 0x00;
		public final static byte FACING_WEST = 0x01;
		public final static byte FACING_NORTH = 0x02;
		public final static byte FACING_EAST = 0x03;
		public final static byte EMPTY = 0x00;
		public final static byte OCCUPIED = 0x04;
		public final static byte FOOT = 0x00;
		public final static byte HEAD = 0x08;
	}
	public final class TALLGRASS {
		public final static byte SHRUB = 0x00;
		public final static byte TALL_GRASS = 0x01;
		public final static byte FERN = 0x02;
	}
	public final class REDFLOWER {
		public final static byte POPPY = 0x00;
		public final static byte BLUE_ORCHID = 0x01;
		public final static byte ALLIUM = 0x02;
		public final static byte AZURE_BLUET = 0x03;
		public final static byte RED_TULIP = 0x04;
		public final static byte ORANGE_TULIP = 0x05;
		public final static byte WHITE_TULIP = 0x06;
		public final static byte PINK_TULIP = 0x07;
		public final static byte OXEYE_DAISY = 0x08;
	}
	public final class DOUBLEPLANT {
		public final static byte SUNFLOWER = 0x00;
		public final static byte LILAC = 0x01;
		public final static byte DOUBLE_TALLGRASS = 0x02;
		public final static byte LARGE_FERN = 0x03;
		public final static byte ROSE_BUSH = 0x04;
		public final static byte PEONY = 0x05;
		public final static byte TOP_HALF = 0x08;
	}
	public final class PISTONS { /* common to piston and piston extension */
		public final static byte HEAD_DOWN = 0x00;
		public final static byte HEAD_UP = 0x01;
		public final static byte HEAD_NORTH = 0x02;
		public final static byte HEAD_SOUTH = 0x03;
		public final static byte HEAD_WEST = 0x04;
		public final static byte HEAD_EAST = 0x05;
	}
	public final class PISTON {
		public final static byte RETRACTED = 0x00;
		public final static byte PUSHED = 0x08;
	}
	public final class PISTONEXTENSION {
		public final static byte REGULAR = 0x00;
		public final static byte STICKY = 0x08;
	}
	public final class STAIRS {
		public final static byte FULLBLOCK_SIDE_EAST = 0x00;
		public final static byte FULLBLOCK_SIDE_WEST = 0x01;
		public final static byte FULLBLOCK_SIDE_SOUTH = 0x02;
		public final static byte FULLBLOCK_SIDE_NORTH = 0x03;
		public final static byte UPSIDE_DOWN = 0x04;
	}
	public final class REDSTONEPOWER { /* common to redstone wire, daylight sensor */
		public final static byte REDSTONEPOWER_NONE = 0x00;
		public final static byte REDSTONEPOWER_1 = 0x01;
		public final static byte REDSTONEPOWER_2 = 0x02;
		public final static byte REDSTONEPOWER_3 = 0x03;
		public final static byte REDSTONEPOWER_4 = 0x04;
		public final static byte REDSTONEPOWER_5 = 0x05;
		public final static byte REDSTONEPOWER_6 = 0x06;
		public final static byte REDSTONEPOWER_7 = 0x07;
		public final static byte REDSTONEPOWER_8 = 0x08;
		public final static byte REDSTONEPOWER_9 = 0x09;
		public final static byte REDSTONEPOWER_10 = 0x0A;
		public final static byte REDSTONEPOWER_11 = 0x0B;
		public final static byte REDSTONEPOWER_12 = 0x0C;
		public final static byte REDSTONEPOWER_13 = 0x0D;
		public final static byte REDSTONEPOWER_14 = 0x0E;
		public final static byte REDSTONEPOWER_MAX = 0x0F;
	}
	public final class CROPSGENERIC { /* common to wheat, carrot and potato */
		public final static byte CROPGROW_0 = 0x00;
		public final static byte CROPGROW_1 = 0x01;
		public final static byte CROPGROW_2 = 0x02;
		public final static byte CROPGROW_3 = 0x03;
		public final static byte CROPGROW_4 = 0x04;
		public final static byte CROPGROW_5 = 0x05;
		public final static byte CROPGROW_6 = 0x06;
		public final static byte CROPGROW_MAX = 0x07;
	}
	public final class BEETROOT {
		public final static byte CROPGROW_0 = 0x00;
		public final static byte CROPGROW_1 = 0x01;
		public final static byte CROPGROW_2 = 0x02;
		public final static byte CROPGROW_MAX = 0x03;
	}
	public final class FARMLAND {
		public final static byte FARMLAND_DRY = 0x00;
		public final static byte FARMLAND_WET_1 = 0x01;
		public final static byte FARMLAND_WET_2 = 0x02;
		public final static byte FARMLAND_WET_3 = 0x03;
		public final static byte FARMLAND_WET_4 = 0x04;
		public final static byte FARMLAND_WET_5 = 0x05;
		public final static byte FARMLAND_WET_6 = 0x06;
		public final static byte FARMLAND_WET_MAX = 0x07;
	}
	/**
	 * 
	 * ... TODO
	 *
	 */
}