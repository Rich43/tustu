package sun.java2d.marlin;

/* loaded from: rt.jar:sun/java2d/marlin/MarlinConst.class */
interface MarlinConst {
    public static final boolean enableLogs = MarlinProperties.isLoggingEnabled();
    public static final boolean useLogger;
    public static final boolean logCreateContext;
    public static final boolean logUnsafeMalloc;
    public static final boolean doCheckUnsafe = false;
    public static final boolean doStats;
    public static final boolean doMonitors = false;
    public static final boolean doChecks;
    public static final boolean DO_AA_RANGE_CHECK = false;
    public static final boolean doLogWidenArray;
    public static final boolean doLogOverSize;
    public static final boolean doTrace;
    public static final boolean doFlushMonitors = true;
    public static final boolean useDumpThread = false;
    public static final long statDump = 5000;
    public static final boolean doCleanDirty = false;
    public static final boolean useSimplifier;
    public static final boolean doLogBounds;
    public static final int INITIAL_PIXEL_DIM;
    public static final int INITIAL_ARRAY = 256;
    public static final int INITIAL_SMALL_ARRAY = 1024;
    public static final int INITIAL_MEDIUM_ARRAY = 4096;
    public static final int INITIAL_LARGE_ARRAY = 8192;
    public static final int INITIAL_ARRAY_16K = 16384;
    public static final int INITIAL_ARRAY_32K = 32768;
    public static final int INITIAL_AA_ARRAY;
    public static final int INITIAL_EDGES_CAPACITY = 98304;
    public static final byte BYTE_0 = 0;
    public static final int SUBPIXEL_LG_POSITIONS_X;
    public static final int SUBPIXEL_LG_POSITIONS_Y;
    public static final int SUBPIXEL_POSITIONS_X;
    public static final int SUBPIXEL_POSITIONS_Y;
    public static final float NORM_SUBPIXELS;
    public static final int MAX_AA_ALPHA;
    public static final int TILE_SIZE_LG;
    public static final int TILE_SIZE;
    public static final int BLOCK_SIZE_LG;
    public static final int BLOCK_SIZE;

    static {
        useLogger = enableLogs && MarlinProperties.isUseLogger();
        logCreateContext = enableLogs && MarlinProperties.isLogCreateContext();
        logUnsafeMalloc = enableLogs && MarlinProperties.isLogUnsafeMalloc();
        doStats = enableLogs && MarlinProperties.isDoStats();
        doChecks = enableLogs && MarlinProperties.isDoChecks();
        if (enableLogs) {
        }
        doLogWidenArray = false;
        if (enableLogs) {
        }
        doLogOverSize = false;
        if (enableLogs) {
        }
        doTrace = false;
        useSimplifier = MarlinProperties.isUseSimplifier();
        if (enableLogs) {
        }
        doLogBounds = false;
        INITIAL_PIXEL_DIM = MarlinProperties.getInitialImageSize();
        INITIAL_AA_ARRAY = INITIAL_PIXEL_DIM;
        SUBPIXEL_LG_POSITIONS_X = MarlinProperties.getSubPixel_Log2_X();
        SUBPIXEL_LG_POSITIONS_Y = MarlinProperties.getSubPixel_Log2_Y();
        SUBPIXEL_POSITIONS_X = 1 << SUBPIXEL_LG_POSITIONS_X;
        SUBPIXEL_POSITIONS_Y = 1 << SUBPIXEL_LG_POSITIONS_Y;
        NORM_SUBPIXELS = (float) Math.sqrt(((SUBPIXEL_POSITIONS_X * SUBPIXEL_POSITIONS_X) + (SUBPIXEL_POSITIONS_Y * SUBPIXEL_POSITIONS_Y)) / 2.0d);
        MAX_AA_ALPHA = SUBPIXEL_POSITIONS_X * SUBPIXEL_POSITIONS_Y;
        TILE_SIZE_LG = MarlinProperties.getTileSize_Log2();
        TILE_SIZE = 1 << TILE_SIZE_LG;
        BLOCK_SIZE_LG = MarlinProperties.getBlockSize_Log2();
        BLOCK_SIZE = 1 << BLOCK_SIZE_LG;
    }
}
