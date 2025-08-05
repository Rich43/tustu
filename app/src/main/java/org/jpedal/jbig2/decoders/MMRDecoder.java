package org.jpedal.jbig2.decoders;

import java.io.IOException;
import jdk.nashorn.internal.runtime.regexp.joni.constants.StackType;
import org.jpedal.jbig2.io.StreamReader;
import org.jpedal.jbig2.util.BinaryOperation;

/* loaded from: icepdf-core.jar:org/jpedal/jbig2/decoders/MMRDecoder.class */
public class MMRDecoder {
    private StreamReader reader;
    public static int ccittEndOfLine = -2;
    public static final int twoDimensionalPass = 0;
    public static final int twoDimensionalHorizontal = 1;
    public static final int twoDimensionalVertical0 = 2;
    public static final int twoDimensionalVerticalR1 = 3;
    public static final int twoDimensionalVerticalL1 = 4;
    public static final int twoDimensionalVerticalR2 = 5;
    public static final int twoDimensionalVerticalL2 = 6;
    public static final int twoDimensionalVerticalR3 = 7;
    public static final int twoDimensionalVerticalL3 = 8;
    private long bufferLength = 0;
    private long buffer = 0;
    private long noOfBytesRead = 0;
    private int[][] twoDimensionalTable1 = {new int[]{-1, -1}, new int[]{-1, -1}, new int[]{7, 8}, new int[]{7, 7}, new int[]{6, 6}, new int[]{6, 6}, new int[]{6, 5}, new int[]{6, 5}, new int[]{4, 0}, new int[]{4, 0}, new int[]{4, 0}, new int[]{4, 0}, new int[]{4, 0}, new int[]{4, 0}, new int[]{4, 0}, new int[]{4, 0}, new int[]{3, 1}, new int[]{3, 1}, new int[]{3, 1}, new int[]{3, 1}, new int[]{3, 1}, new int[]{3, 1}, new int[]{3, 1}, new int[]{3, 1}, new int[]{3, 1}, new int[]{3, 1}, new int[]{3, 1}, new int[]{3, 1}, new int[]{3, 1}, new int[]{3, 1}, new int[]{3, 1}, new int[]{3, 1}, new int[]{3, 4}, new int[]{3, 4}, new int[]{3, 4}, new int[]{3, 4}, new int[]{3, 4}, new int[]{3, 4}, new int[]{3, 4}, new int[]{3, 4}, new int[]{3, 4}, new int[]{3, 4}, new int[]{3, 4}, new int[]{3, 4}, new int[]{3, 4}, new int[]{3, 4}, new int[]{3, 4}, new int[]{3, 4}, new int[]{3, 3}, new int[]{3, 3}, new int[]{3, 3}, new int[]{3, 3}, new int[]{3, 3}, new int[]{3, 3}, new int[]{3, 3}, new int[]{3, 3}, new int[]{3, 3}, new int[]{3, 3}, new int[]{3, 3}, new int[]{3, 3}, new int[]{3, 3}, new int[]{3, 3}, new int[]{3, 3}, new int[]{3, 3}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}, new int[]{1, 2}};
    private int[][] whiteTable1 = {new int[]{-1, -1}, new int[]{12, ccittEndOfLine}, new int[]{-1, -1}, new int[]{-1, -1}, new int[]{-1, -1}, new int[]{-1, -1}, new int[]{-1, -1}, new int[]{-1, -1}, new int[]{-1, -1}, new int[]{-1, -1}, new int[]{-1, -1}, new int[]{-1, -1}, new int[]{-1, -1}, new int[]{-1, -1}, new int[]{-1, -1}, new int[]{-1, -1}, new int[]{11, 1792}, new int[]{11, 1792}, new int[]{12, 1984}, new int[]{12, 2048}, new int[]{12, 2112}, new int[]{12, 2176}, new int[]{12, 2240}, new int[]{12, 2304}, new int[]{11, 1856}, new int[]{11, 1856}, new int[]{11, 1920}, new int[]{11, 1920}, new int[]{12, 2368}, new int[]{12, 2432}, new int[]{12, 2496}, new int[]{12, StackType.VOID}};
    private int[][] whiteTable2 = {new int[]{-1, -1}, new int[]{-1, -1}, new int[]{-1, -1}, new int[]{-1, -1}, new int[]{8, 29}, new int[]{8, 29}, new int[]{8, 30}, new int[]{8, 30}, new int[]{8, 45}, new int[]{8, 45}, new int[]{8, 46}, new int[]{8, 46}, new int[]{7, 22}, new int[]{7, 22}, new int[]{7, 22}, new int[]{7, 22}, new int[]{7, 23}, new int[]{7, 23}, new int[]{7, 23}, new int[]{7, 23}, new int[]{8, 47}, new int[]{8, 47}, new int[]{8, 48}, new int[]{8, 48}, new int[]{6, 13}, new int[]{6, 13}, new int[]{6, 13}, new int[]{6, 13}, new int[]{6, 13}, new int[]{6, 13}, new int[]{6, 13}, new int[]{6, 13}, new int[]{7, 20}, new int[]{7, 20}, new int[]{7, 20}, new int[]{7, 20}, new int[]{8, 33}, new int[]{8, 33}, new int[]{8, 34}, new int[]{8, 34}, new int[]{8, 35}, new int[]{8, 35}, new int[]{8, 36}, new int[]{8, 36}, new int[]{8, 37}, new int[]{8, 37}, new int[]{8, 38}, new int[]{8, 38}, new int[]{7, 19}, new int[]{7, 19}, new int[]{7, 19}, new int[]{7, 19}, new int[]{8, 31}, new int[]{8, 31}, new int[]{8, 32}, new int[]{8, 32}, new int[]{6, 1}, new int[]{6, 1}, new int[]{6, 1}, new int[]{6, 1}, new int[]{6, 1}, new int[]{6, 1}, new int[]{6, 1}, new int[]{6, 1}, new int[]{6, 12}, new int[]{6, 12}, new int[]{6, 12}, new int[]{6, 12}, new int[]{6, 12}, new int[]{6, 12}, new int[]{6, 12}, new int[]{6, 12}, new int[]{8, 53}, new int[]{8, 53}, new int[]{8, 54}, new int[]{8, 54}, new int[]{7, 26}, new int[]{7, 26}, new int[]{7, 26}, new int[]{7, 26}, new int[]{8, 39}, new int[]{8, 39}, new int[]{8, 40}, new int[]{8, 40}, new int[]{8, 41}, new int[]{8, 41}, new int[]{8, 42}, new int[]{8, 42}, new int[]{8, 43}, new int[]{8, 43}, new int[]{8, 44}, new int[]{8, 44}, new int[]{7, 21}, new int[]{7, 21}, new int[]{7, 21}, new int[]{7, 21}, new int[]{7, 28}, new int[]{7, 28}, new int[]{7, 28}, new int[]{7, 28}, new int[]{8, 61}, new int[]{8, 61}, new int[]{8, 62}, new int[]{8, 62}, new int[]{8, 63}, new int[]{8, 63}, new int[]{8, 0}, new int[]{8, 0}, new int[]{8, 320}, new int[]{8, 320}, new int[]{8, 384}, new int[]{8, 384}, new int[]{5, 10}, new int[]{5, 10}, new int[]{5, 10}, new int[]{5, 10}, new int[]{5, 10}, new int[]{5, 10}, new int[]{5, 10}, new int[]{5, 10}, new int[]{5, 10}, new int[]{5, 10}, new int[]{5, 10}, new int[]{5, 10}, new int[]{5, 10}, new int[]{5, 10}, new int[]{5, 10}, new int[]{5, 10}, new int[]{5, 11}, new int[]{5, 11}, new int[]{5, 11}, new int[]{5, 11}, new int[]{5, 11}, new int[]{5, 11}, new int[]{5, 11}, new int[]{5, 11}, new int[]{5, 11}, new int[]{5, 11}, new int[]{5, 11}, new int[]{5, 11}, new int[]{5, 11}, new int[]{5, 11}, new int[]{5, 11}, new int[]{5, 11}, new int[]{7, 27}, new int[]{7, 27}, new int[]{7, 27}, new int[]{7, 27}, new int[]{8, 59}, new int[]{8, 59}, new int[]{8, 60}, new int[]{8, 60}, new int[]{9, 1472}, new int[]{9, 1536}, new int[]{9, 1600}, new int[]{9, 1728}, new int[]{7, 18}, new int[]{7, 18}, new int[]{7, 18}, new int[]{7, 18}, new int[]{7, 24}, new int[]{7, 24}, new int[]{7, 24}, new int[]{7, 24}, new int[]{8, 49}, new int[]{8, 49}, new int[]{8, 50}, new int[]{8, 50}, new int[]{8, 51}, new int[]{8, 51}, new int[]{8, 52}, new int[]{8, 52}, new int[]{7, 25}, new int[]{7, 25}, new int[]{7, 25}, new int[]{7, 25}, new int[]{8, 55}, new int[]{8, 55}, new int[]{8, 56}, new int[]{8, 56}, new int[]{8, 57}, new int[]{8, 57}, new int[]{8, 58}, new int[]{8, 58}, new int[]{6, 192}, new int[]{6, 192}, new int[]{6, 192}, new int[]{6, 192}, new int[]{6, 192}, new int[]{6, 192}, new int[]{6, 192}, new int[]{6, 192}, new int[]{6, 1664}, new int[]{6, 1664}, new int[]{6, 1664}, new int[]{6, 1664}, new int[]{6, 1664}, new int[]{6, 1664}, new int[]{6, 1664}, new int[]{6, 1664}, new int[]{8, 448}, new int[]{8, 448}, new int[]{8, 512}, new int[]{8, 512}, new int[]{9, 704}, new int[]{9, 768}, new int[]{8, 640}, new int[]{8, 640}, new int[]{8, 576}, new int[]{8, 576}, new int[]{9, 832}, new int[]{9, 896}, new int[]{9, 960}, new int[]{9, 1024}, new int[]{9, 1088}, new int[]{9, 1152}, new int[]{9, 1216}, new int[]{9, 1280}, new int[]{9, 1344}, new int[]{9, 1408}, new int[]{7, 256}, new int[]{7, 256}, new int[]{7, 256}, new int[]{7, 256}, new int[]{4, 2}, new int[]{4, 2}, new int[]{4, 2}, new int[]{4, 2}, new int[]{4, 2}, new int[]{4, 2}, new int[]{4, 2}, new int[]{4, 2}, new int[]{4, 2}, new int[]{4, 2}, new int[]{4, 2}, new int[]{4, 2}, new int[]{4, 2}, new int[]{4, 2}, new int[]{4, 2}, new int[]{4, 2}, new int[]{4, 2}, new int[]{4, 2}, new int[]{4, 2}, new int[]{4, 2}, new int[]{4, 2}, new int[]{4, 2}, new int[]{4, 2}, new int[]{4, 2}, new int[]{4, 2}, new int[]{4, 2}, new int[]{4, 2}, new int[]{4, 2}, new int[]{4, 2}, new int[]{4, 2}, new int[]{4, 2}, new int[]{4, 2}, new int[]{4, 3}, new int[]{4, 3}, new int[]{4, 3}, new int[]{4, 3}, new int[]{4, 3}, new int[]{4, 3}, new int[]{4, 3}, new int[]{4, 3}, new int[]{4, 3}, new int[]{4, 3}, new int[]{4, 3}, new int[]{4, 3}, new int[]{4, 3}, new int[]{4, 3}, new int[]{4, 3}, new int[]{4, 3}, new int[]{4, 3}, new int[]{4, 3}, new int[]{4, 3}, new int[]{4, 3}, new int[]{4, 3}, new int[]{4, 3}, new int[]{4, 3}, new int[]{4, 3}, new int[]{4, 3}, new int[]{4, 3}, new int[]{4, 3}, new int[]{4, 3}, new int[]{4, 3}, new int[]{4, 3}, new int[]{4, 3}, new int[]{4, 3}, new int[]{5, 128}, new int[]{5, 128}, new int[]{5, 128}, new int[]{5, 128}, new int[]{5, 128}, new int[]{5, 128}, new int[]{5, 128}, new int[]{5, 128}, new int[]{5, 128}, new int[]{5, 128}, new int[]{5, 128}, new int[]{5, 128}, new int[]{5, 128}, new int[]{5, 128}, new int[]{5, 128}, new int[]{5, 128}, new int[]{5, 8}, new int[]{5, 8}, new int[]{5, 8}, new int[]{5, 8}, new int[]{5, 8}, new int[]{5, 8}, new int[]{5, 8}, new int[]{5, 8}, new int[]{5, 8}, new int[]{5, 8}, new int[]{5, 8}, new int[]{5, 8}, new int[]{5, 8}, new int[]{5, 8}, new int[]{5, 8}, new int[]{5, 8}, new int[]{5, 9}, new int[]{5, 9}, new int[]{5, 9}, new int[]{5, 9}, new int[]{5, 9}, new int[]{5, 9}, new int[]{5, 9}, new int[]{5, 9}, new int[]{5, 9}, new int[]{5, 9}, new int[]{5, 9}, new int[]{5, 9}, new int[]{5, 9}, new int[]{5, 9}, new int[]{5, 9}, new int[]{5, 9}, new int[]{6, 16}, new int[]{6, 16}, new int[]{6, 16}, new int[]{6, 16}, new int[]{6, 16}, new int[]{6, 16}, new int[]{6, 16}, new int[]{6, 16}, new int[]{6, 17}, new int[]{6, 17}, new int[]{6, 17}, new int[]{6, 17}, new int[]{6, 17}, new int[]{6, 17}, new int[]{6, 17}, new int[]{6, 17}, new int[]{4, 4}, new int[]{4, 4}, new int[]{4, 4}, new int[]{4, 4}, new int[]{4, 4}, new int[]{4, 4}, new int[]{4, 4}, new int[]{4, 4}, new int[]{4, 4}, new int[]{4, 4}, new int[]{4, 4}, new int[]{4, 4}, new int[]{4, 4}, new int[]{4, 4}, new int[]{4, 4}, new int[]{4, 4}, new int[]{4, 4}, new int[]{4, 4}, new int[]{4, 4}, new int[]{4, 4}, new int[]{4, 4}, new int[]{4, 4}, new int[]{4, 4}, new int[]{4, 4}, new int[]{4, 4}, new int[]{4, 4}, new int[]{4, 4}, new int[]{4, 4}, new int[]{4, 4}, new int[]{4, 4}, new int[]{4, 4}, new int[]{4, 4}, new int[]{4, 5}, new int[]{4, 5}, new int[]{4, 5}, new int[]{4, 5}, new int[]{4, 5}, new int[]{4, 5}, new int[]{4, 5}, new int[]{4, 5}, new int[]{4, 5}, new int[]{4, 5}, new int[]{4, 5}, new int[]{4, 5}, new int[]{4, 5}, new int[]{4, 5}, new int[]{4, 5}, new int[]{4, 5}, new int[]{4, 5}, new int[]{4, 5}, new int[]{4, 5}, new int[]{4, 5}, new int[]{4, 5}, new int[]{4, 5}, new int[]{4, 5}, new int[]{4, 5}, new int[]{4, 5}, new int[]{4, 5}, new int[]{4, 5}, new int[]{4, 5}, new int[]{4, 5}, new int[]{4, 5}, new int[]{4, 5}, new int[]{4, 5}, new int[]{6, 14}, new int[]{6, 14}, new int[]{6, 14}, new int[]{6, 14}, new int[]{6, 14}, new int[]{6, 14}, new int[]{6, 14}, new int[]{6, 14}, new int[]{6, 15}, new int[]{6, 15}, new int[]{6, 15}, new int[]{6, 15}, new int[]{6, 15}, new int[]{6, 15}, new int[]{6, 15}, new int[]{6, 15}, new int[]{5, 64}, new int[]{5, 64}, new int[]{5, 64}, new int[]{5, 64}, new int[]{5, 64}, new int[]{5, 64}, new int[]{5, 64}, new int[]{5, 64}, new int[]{5, 64}, new int[]{5, 64}, new int[]{5, 64}, new int[]{5, 64}, new int[]{5, 64}, new int[]{5, 64}, new int[]{5, 64}, new int[]{5, 64}, new int[]{4, 6}, new int[]{4, 6}, new int[]{4, 6}, new int[]{4, 6}, new int[]{4, 6}, new int[]{4, 6}, new int[]{4, 6}, new int[]{4, 6}, new int[]{4, 6}, new int[]{4, 6}, new int[]{4, 6}, new int[]{4, 6}, new int[]{4, 6}, new int[]{4, 6}, new int[]{4, 6}, new int[]{4, 6}, new int[]{4, 6}, new int[]{4, 6}, new int[]{4, 6}, new int[]{4, 6}, new int[]{4, 6}, new int[]{4, 6}, new int[]{4, 6}, new int[]{4, 6}, new int[]{4, 6}, new int[]{4, 6}, new int[]{4, 6}, new int[]{4, 6}, new int[]{4, 6}, new int[]{4, 6}, new int[]{4, 6}, new int[]{4, 6}, new int[]{4, 7}, new int[]{4, 7}, new int[]{4, 7}, new int[]{4, 7}, new int[]{4, 7}, new int[]{4, 7}, new int[]{4, 7}, new int[]{4, 7}, new int[]{4, 7}, new int[]{4, 7}, new int[]{4, 7}, new int[]{4, 7}, new int[]{4, 7}, new int[]{4, 7}, new int[]{4, 7}, new int[]{4, 7}, new int[]{4, 7}, new int[]{4, 7}, new int[]{4, 7}, new int[]{4, 7}, new int[]{4, 7}, new int[]{4, 7}, new int[]{4, 7}, new int[]{4, 7}, new int[]{4, 7}, new int[]{4, 7}, new int[]{4, 7}, new int[]{4, 7}, new int[]{4, 7}, new int[]{4, 7}, new int[]{4, 7}, new int[]{4, 7}};
    int[][] blackTable1 = {new int[]{-1, -1}, new int[]{-1, -1}, new int[]{12, ccittEndOfLine}, new int[]{12, ccittEndOfLine}, new int[]{-1, -1}, new int[]{-1, -1}, new int[]{-1, -1}, new int[]{-1, -1}, new int[]{-1, -1}, new int[]{-1, -1}, new int[]{-1, -1}, new int[]{-1, -1}, new int[]{-1, -1}, new int[]{-1, -1}, new int[]{-1, -1}, new int[]{-1, -1}, new int[]{-1, -1}, new int[]{-1, -1}, new int[]{-1, -1}, new int[]{-1, -1}, new int[]{-1, -1}, new int[]{-1, -1}, new int[]{-1, -1}, new int[]{-1, -1}, new int[]{-1, -1}, new int[]{-1, -1}, new int[]{-1, -1}, new int[]{-1, -1}, new int[]{-1, -1}, new int[]{-1, -1}, new int[]{-1, -1}, new int[]{-1, -1}, new int[]{11, 1792}, new int[]{11, 1792}, new int[]{11, 1792}, new int[]{11, 1792}, new int[]{12, 1984}, new int[]{12, 1984}, new int[]{12, 2048}, new int[]{12, 2048}, new int[]{12, 2112}, new int[]{12, 2112}, new int[]{12, 2176}, new int[]{12, 2176}, new int[]{12, 2240}, new int[]{12, 2240}, new int[]{12, 2304}, new int[]{12, 2304}, new int[]{11, 1856}, new int[]{11, 1856}, new int[]{11, 1856}, new int[]{11, 1856}, new int[]{11, 1920}, new int[]{11, 1920}, new int[]{11, 1920}, new int[]{11, 1920}, new int[]{12, 2368}, new int[]{12, 2368}, new int[]{12, 2432}, new int[]{12, 2432}, new int[]{12, 2496}, new int[]{12, 2496}, new int[]{12, StackType.VOID}, new int[]{12, StackType.VOID}, new int[]{10, 18}, new int[]{10, 18}, new int[]{10, 18}, new int[]{10, 18}, new int[]{10, 18}, new int[]{10, 18}, new int[]{10, 18}, new int[]{10, 18}, new int[]{12, 52}, new int[]{12, 52}, new int[]{13, 640}, new int[]{13, 704}, new int[]{13, 768}, new int[]{13, 832}, new int[]{12, 55}, new int[]{12, 55}, new int[]{12, 56}, new int[]{12, 56}, new int[]{13, 1280}, new int[]{13, 1344}, new int[]{13, 1408}, new int[]{13, 1472}, new int[]{12, 59}, new int[]{12, 59}, new int[]{12, 60}, new int[]{12, 60}, new int[]{13, 1536}, new int[]{13, 1600}, new int[]{11, 24}, new int[]{11, 24}, new int[]{11, 24}, new int[]{11, 24}, new int[]{11, 25}, new int[]{11, 25}, new int[]{11, 25}, new int[]{11, 25}, new int[]{13, 1664}, new int[]{13, 1728}, new int[]{12, 320}, new int[]{12, 320}, new int[]{12, 384}, new int[]{12, 384}, new int[]{12, 448}, new int[]{12, 448}, new int[]{13, 512}, new int[]{13, 576}, new int[]{12, 53}, new int[]{12, 53}, new int[]{12, 54}, new int[]{12, 54}, new int[]{13, 896}, new int[]{13, 960}, new int[]{13, 1024}, new int[]{13, 1088}, new int[]{13, 1152}, new int[]{13, 1216}, new int[]{10, 64}, new int[]{10, 64}, new int[]{10, 64}, new int[]{10, 64}, new int[]{10, 64}, new int[]{10, 64}, new int[]{10, 64}, new int[]{10, 64}};
    int[][] blackTable2 = {new int[]{8, 13}, new int[]{8, 13}, new int[]{8, 13}, new int[]{8, 13}, new int[]{8, 13}, new int[]{8, 13}, new int[]{8, 13}, new int[]{8, 13}, new int[]{8, 13}, new int[]{8, 13}, new int[]{8, 13}, new int[]{8, 13}, new int[]{8, 13}, new int[]{8, 13}, new int[]{8, 13}, new int[]{8, 13}, new int[]{11, 23}, new int[]{11, 23}, new int[]{12, 50}, new int[]{12, 51}, new int[]{12, 44}, new int[]{12, 45}, new int[]{12, 46}, new int[]{12, 47}, new int[]{12, 57}, new int[]{12, 58}, new int[]{12, 61}, new int[]{12, 256}, new int[]{10, 16}, new int[]{10, 16}, new int[]{10, 16}, new int[]{10, 16}, new int[]{10, 17}, new int[]{10, 17}, new int[]{10, 17}, new int[]{10, 17}, new int[]{12, 48}, new int[]{12, 49}, new int[]{12, 62}, new int[]{12, 63}, new int[]{12, 30}, new int[]{12, 31}, new int[]{12, 32}, new int[]{12, 33}, new int[]{12, 40}, new int[]{12, 41}, new int[]{11, 22}, new int[]{11, 22}, new int[]{8, 14}, new int[]{8, 14}, new int[]{8, 14}, new int[]{8, 14}, new int[]{8, 14}, new int[]{8, 14}, new int[]{8, 14}, new int[]{8, 14}, new int[]{8, 14}, new int[]{8, 14}, new int[]{8, 14}, new int[]{8, 14}, new int[]{8, 14}, new int[]{8, 14}, new int[]{8, 14}, new int[]{8, 14}, new int[]{7, 10}, new int[]{7, 10}, new int[]{7, 10}, new int[]{7, 10}, new int[]{7, 10}, new int[]{7, 10}, new int[]{7, 10}, new int[]{7, 10}, new int[]{7, 10}, new int[]{7, 10}, new int[]{7, 10}, new int[]{7, 10}, new int[]{7, 10}, new int[]{7, 10}, new int[]{7, 10}, new int[]{7, 10}, new int[]{7, 10}, new int[]{7, 10}, new int[]{7, 10}, new int[]{7, 10}, new int[]{7, 10}, new int[]{7, 10}, new int[]{7, 10}, new int[]{7, 10}, new int[]{7, 10}, new int[]{7, 10}, new int[]{7, 10}, new int[]{7, 10}, new int[]{7, 10}, new int[]{7, 10}, new int[]{7, 10}, new int[]{7, 10}, new int[]{7, 11}, new int[]{7, 11}, new int[]{7, 11}, new int[]{7, 11}, new int[]{7, 11}, new int[]{7, 11}, new int[]{7, 11}, new int[]{7, 11}, new int[]{7, 11}, new int[]{7, 11}, new int[]{7, 11}, new int[]{7, 11}, new int[]{7, 11}, new int[]{7, 11}, new int[]{7, 11}, new int[]{7, 11}, new int[]{7, 11}, new int[]{7, 11}, new int[]{7, 11}, new int[]{7, 11}, new int[]{7, 11}, new int[]{7, 11}, new int[]{7, 11}, new int[]{7, 11}, new int[]{7, 11}, new int[]{7, 11}, new int[]{7, 11}, new int[]{7, 11}, new int[]{7, 11}, new int[]{7, 11}, new int[]{7, 11}, new int[]{7, 11}, new int[]{9, 15}, new int[]{9, 15}, new int[]{9, 15}, new int[]{9, 15}, new int[]{9, 15}, new int[]{9, 15}, new int[]{9, 15}, new int[]{9, 15}, new int[]{12, 128}, new int[]{12, 192}, new int[]{12, 26}, new int[]{12, 27}, new int[]{12, 28}, new int[]{12, 29}, new int[]{11, 19}, new int[]{11, 19}, new int[]{11, 20}, new int[]{11, 20}, new int[]{12, 34}, new int[]{12, 35}, new int[]{12, 36}, new int[]{12, 37}, new int[]{12, 38}, new int[]{12, 39}, new int[]{11, 21}, new int[]{11, 21}, new int[]{12, 42}, new int[]{12, 43}, new int[]{10, 0}, new int[]{10, 0}, new int[]{10, 0}, new int[]{10, 0}, new int[]{7, 12}, new int[]{7, 12}, new int[]{7, 12}, new int[]{7, 12}, new int[]{7, 12}, new int[]{7, 12}, new int[]{7, 12}, new int[]{7, 12}, new int[]{7, 12}, new int[]{7, 12}, new int[]{7, 12}, new int[]{7, 12}, new int[]{7, 12}, new int[]{7, 12}, new int[]{7, 12}, new int[]{7, 12}, new int[]{7, 12}, new int[]{7, 12}, new int[]{7, 12}, new int[]{7, 12}, new int[]{7, 12}, new int[]{7, 12}, new int[]{7, 12}, new int[]{7, 12}, new int[]{7, 12}, new int[]{7, 12}, new int[]{7, 12}, new int[]{7, 12}, new int[]{7, 12}, new int[]{7, 12}, new int[]{7, 12}, new int[]{7, 12}};
    int[][] blackTable3 = {new int[]{-1, -1}, new int[]{-1, -1}, new int[]{-1, -1}, new int[]{-1, -1}, new int[]{6, 9}, new int[]{6, 8}, new int[]{5, 7}, new int[]{5, 7}, new int[]{4, 6}, new int[]{4, 6}, new int[]{4, 6}, new int[]{4, 6}, new int[]{4, 5}, new int[]{4, 5}, new int[]{4, 5}, new int[]{4, 5}, new int[]{3, 1}, new int[]{3, 1}, new int[]{3, 1}, new int[]{3, 1}, new int[]{3, 1}, new int[]{3, 1}, new int[]{3, 1}, new int[]{3, 1}, new int[]{3, 4}, new int[]{3, 4}, new int[]{3, 4}, new int[]{3, 4}, new int[]{3, 4}, new int[]{3, 4}, new int[]{3, 4}, new int[]{3, 4}, new int[]{2, 3}, new int[]{2, 3}, new int[]{2, 3}, new int[]{2, 3}, new int[]{2, 3}, new int[]{2, 3}, new int[]{2, 3}, new int[]{2, 3}, new int[]{2, 3}, new int[]{2, 3}, new int[]{2, 3}, new int[]{2, 3}, new int[]{2, 3}, new int[]{2, 3}, new int[]{2, 3}, new int[]{2, 3}, new int[]{2, 2}, new int[]{2, 2}, new int[]{2, 2}, new int[]{2, 2}, new int[]{2, 2}, new int[]{2, 2}, new int[]{2, 2}, new int[]{2, 2}, new int[]{2, 2}, new int[]{2, 2}, new int[]{2, 2}, new int[]{2, 2}, new int[]{2, 2}, new int[]{2, 2}, new int[]{2, 2}, new int[]{2, 2}};

    /* JADX WARN: Type inference failed for: r1v10, types: [int[], int[][]] */
    /* JADX WARN: Type inference failed for: r1v12, types: [int[], int[][]] */
    /* JADX WARN: Type inference failed for: r1v14, types: [int[], int[][]] */
    /* JADX WARN: Type inference failed for: r1v4, types: [int[], int[][]] */
    /* JADX WARN: Type inference failed for: r1v6, types: [int[], int[][]] */
    /* JADX WARN: Type inference failed for: r1v8, types: [int[], int[][]] */
    private MMRDecoder() {
    }

    /* JADX WARN: Type inference failed for: r1v10, types: [int[], int[][]] */
    /* JADX WARN: Type inference failed for: r1v12, types: [int[], int[][]] */
    /* JADX WARN: Type inference failed for: r1v14, types: [int[], int[][]] */
    /* JADX WARN: Type inference failed for: r1v4, types: [int[], int[][]] */
    /* JADX WARN: Type inference failed for: r1v6, types: [int[], int[][]] */
    /* JADX WARN: Type inference failed for: r1v8, types: [int[], int[][]] */
    public MMRDecoder(StreamReader reader) {
        this.reader = reader;
    }

    public void reset() {
        this.bufferLength = 0L;
        this.noOfBytesRead = 0L;
        this.buffer = 0L;
    }

    public void skipTo(int length) throws IOException {
        while (this.noOfBytesRead < length) {
            this.reader.readByte();
            this.noOfBytesRead++;
        }
    }

    public long get24Bits() throws IOException {
        while (this.bufferLength < 24) {
            this.buffer = BinaryOperation.bit32Shift(this.buffer, 8, 0) | (this.reader.readByte() & 255);
            this.bufferLength += 8;
            this.noOfBytesRead++;
        }
        return BinaryOperation.bit32Shift(this.buffer, (int) (this.bufferLength - 24), 1) & 16777215;
    }

    public int get2DCode() throws IOException {
        int[] tuple;
        if (this.bufferLength == 0) {
            this.buffer = this.reader.readByte() & 255;
            this.bufferLength = 8L;
            this.noOfBytesRead++;
            int lookup = (int) (BinaryOperation.bit32Shift(this.buffer, 1, 1) & 127);
            tuple = this.twoDimensionalTable1[lookup];
        } else if (this.bufferLength == 8) {
            int lookup2 = (int) (BinaryOperation.bit32Shift(this.buffer, 1, 1) & 127);
            tuple = this.twoDimensionalTable1[lookup2];
        } else {
            int lookup3 = (int) (BinaryOperation.bit32Shift(this.buffer, (int) (7 - this.bufferLength), 0) & 127);
            tuple = this.twoDimensionalTable1[lookup3];
            if (tuple[0] < 0 || tuple[0] > ((int) this.bufferLength)) {
                int right = this.reader.readByte() & 255;
                long left = BinaryOperation.bit32Shift(this.buffer, 8, 0);
                this.buffer = left | right;
                this.bufferLength += 8;
                this.noOfBytesRead++;
                int look = (int) (BinaryOperation.bit32Shift(this.buffer, (int) (this.bufferLength - 7), 1) & 127);
                tuple = this.twoDimensionalTable1[look];
            }
        }
        if (tuple[0] < 0) {
            if (JBIG2StreamDecoder.debug) {
                System.out.println("Bad two dim code in JBIG2 MMR stream");
                return 0;
            }
            return 0;
        }
        this.bufferLength -= tuple[0];
        return tuple[1];
    }

    public int getWhiteCode() throws IOException {
        long code;
        int[] tuple;
        long code2;
        if (this.bufferLength == 0) {
            this.buffer = this.reader.readByte() & 255;
            this.bufferLength = 8L;
            this.noOfBytesRead++;
        }
        while (true) {
            if (this.bufferLength >= 7 && (BinaryOperation.bit32Shift(this.buffer, (int) (this.bufferLength - 7), 1) & 127) == 0) {
                if (this.bufferLength <= 12) {
                    code2 = BinaryOperation.bit32Shift(this.buffer, (int) (12 - this.bufferLength), 0);
                } else {
                    code2 = BinaryOperation.bit32Shift(this.buffer, (int) (this.bufferLength - 12), 1);
                }
                tuple = this.whiteTable1[(int) (code2 & 31)];
            } else {
                if (this.bufferLength <= 9) {
                    code = BinaryOperation.bit32Shift(this.buffer, (int) (9 - this.bufferLength), 0);
                } else {
                    code = BinaryOperation.bit32Shift(this.buffer, (int) (this.bufferLength - 9), 1);
                }
                int lookup = (int) (code & 511);
                if (lookup >= 0) {
                    tuple = this.whiteTable2[lookup];
                } else {
                    tuple = this.whiteTable2[this.whiteTable2.length + lookup];
                }
            }
            if (tuple[0] > 0 && tuple[0] <= ((int) this.bufferLength)) {
                this.bufferLength -= tuple[0];
                return tuple[1];
            }
            if (this.bufferLength < 12) {
                this.buffer = BinaryOperation.bit32Shift(this.buffer, 8, 0) | (this.reader.readByte() & 255);
                this.bufferLength += 8;
                this.noOfBytesRead++;
            } else {
                if (JBIG2StreamDecoder.debug) {
                    System.out.println("Bad white code in JBIG2 MMR stream");
                }
                this.bufferLength--;
                return 1;
            }
        }
    }

    public int getBlackCode() throws IOException {
        long code;
        int[] tuple;
        long code2;
        long code3;
        if (this.bufferLength == 0) {
            this.buffer = this.reader.readByte() & 255;
            this.bufferLength = 8L;
            this.noOfBytesRead++;
        }
        while (true) {
            if (this.bufferLength >= 6 && (BinaryOperation.bit32Shift(this.buffer, (int) (this.bufferLength - 6), 1) & 63) == 0) {
                if (this.bufferLength <= 13) {
                    code3 = BinaryOperation.bit32Shift(this.buffer, (int) (13 - this.bufferLength), 0);
                } else {
                    code3 = BinaryOperation.bit32Shift(this.buffer, (int) (this.bufferLength - 13), 1);
                }
                tuple = this.blackTable1[(int) (code3 & 127)];
            } else if (this.bufferLength >= 4 && ((this.buffer >> ((int) (this.bufferLength - 4))) & 15) == 0) {
                if (this.bufferLength <= 12) {
                    code2 = BinaryOperation.bit32Shift(this.buffer, (int) (12 - this.bufferLength), 0);
                } else {
                    code2 = BinaryOperation.bit32Shift(this.buffer, (int) (this.bufferLength - 12), 1);
                }
                int lookup = (int) ((code2 & 255) - 64);
                if (lookup >= 0) {
                    tuple = this.blackTable2[lookup];
                } else {
                    tuple = this.blackTable1[this.blackTable1.length + lookup];
                }
            } else {
                if (this.bufferLength <= 6) {
                    code = BinaryOperation.bit32Shift(this.buffer, (int) (6 - this.bufferLength), 0);
                } else {
                    code = BinaryOperation.bit32Shift(this.buffer, (int) (this.bufferLength - 6), 1);
                }
                int lookup2 = (int) (code & 63);
                if (lookup2 >= 0) {
                    tuple = this.blackTable3[lookup2];
                } else {
                    tuple = this.blackTable2[this.blackTable2.length + lookup2];
                }
            }
            if (tuple[0] > 0 && tuple[0] <= ((int) this.bufferLength)) {
                this.bufferLength -= tuple[0];
                return tuple[1];
            }
            if (this.bufferLength < 13) {
                this.buffer = BinaryOperation.bit32Shift(this.buffer, 8, 0) | (this.reader.readByte() & 255);
                this.bufferLength += 8;
                this.noOfBytesRead++;
            } else {
                if (JBIG2StreamDecoder.debug) {
                    System.out.println("Bad black code in JBIG2 MMR stream");
                }
                this.bufferLength--;
                return 1;
            }
        }
    }
}
