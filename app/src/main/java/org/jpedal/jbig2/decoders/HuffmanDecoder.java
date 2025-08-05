package org.jpedal.jbig2.decoders;

import com.sun.glass.events.WindowEvent;
import com.sun.media.sound.DLSModulator;
import java.io.IOException;
import jdk.nashorn.internal.runtime.regexp.joni.encoding.CharacterType;
import org.jpedal.jbig2.io.StreamReader;
import sun.nio.fs.WindowsConstants;

/* loaded from: icepdf-core.jar:org/jpedal/jbig2/decoders/HuffmanDecoder.class */
public class HuffmanDecoder {
    private StreamReader reader;
    private static HuffmanDecoder ref;
    public static int jbig2HuffmanLOW = -3;
    public static int jbig2HuffmanOOB = -2;
    public static int jbig2HuffmanEOT = -1;
    public static int[][] huffmanTableA = {new int[]{0, 1, 4, 0}, new int[]{16, 2, 8, 2}, new int[]{272, 3, 16, 6}, new int[]{65808, 3, 32, 7}, new int[]{0, 0, jbig2HuffmanEOT, 0}};
    public static int[][] huffmanTableB = {new int[]{0, 1, 0, 0}, new int[]{1, 2, 0, 2}, new int[]{2, 3, 0, 6}, new int[]{3, 4, 3, 14}, new int[]{11, 5, 6, 30}, new int[]{75, 6, 32, 62}, new int[]{0, 6, jbig2HuffmanOOB, 63}, new int[]{0, 0, jbig2HuffmanEOT, 0}};
    public static int[][] huffmanTableC = {new int[]{0, 1, 0, 0}, new int[]{1, 2, 0, 2}, new int[]{2, 3, 0, 6}, new int[]{3, 4, 3, 14}, new int[]{11, 5, 6, 30}, new int[]{0, 6, jbig2HuffmanOOB, 62}, new int[]{75, 7, 32, 254}, new int[]{-256, 8, 8, 254}, new int[]{-257, 8, jbig2HuffmanLOW, 255}, new int[]{0, 0, jbig2HuffmanEOT, 0}};
    public static int[][] huffmanTableD = {new int[]{1, 1, 0, 0}, new int[]{2, 2, 0, 2}, new int[]{3, 3, 0, 6}, new int[]{4, 4, 3, 14}, new int[]{12, 5, 6, 30}, new int[]{76, 5, 32, 31}, new int[]{0, 0, jbig2HuffmanEOT, 0}};
    public static int[][] huffmanTableE = {new int[]{1, 1, 0, 0}, new int[]{2, 2, 0, 2}, new int[]{3, 3, 0, 6}, new int[]{4, 4, 3, 14}, new int[]{12, 5, 6, 30}, new int[]{76, 6, 32, 62}, new int[]{-255, 7, 8, 126}, new int[]{-256, 7, jbig2HuffmanLOW, 127}, new int[]{0, 0, jbig2HuffmanEOT, 0}};
    public static int[][] huffmanTableF = {new int[]{0, 2, 7, 0}, new int[]{128, 3, 7, 2}, new int[]{256, 3, 8, 3}, new int[]{-1024, 4, 9, 8}, new int[]{-512, 4, 8, 9}, new int[]{-256, 4, 7, 10}, new int[]{-32, 4, 5, 11}, new int[]{512, 4, 9, 12}, new int[]{1024, 4, 10, 13}, new int[]{-2048, 5, 10, 28}, new int[]{-128, 5, 6, 29}, new int[]{-64, 5, 5, 30}, new int[]{-2049, 6, jbig2HuffmanLOW, 62}, new int[]{2048, 6, 32, 63}, new int[]{0, 0, jbig2HuffmanEOT, 0}};
    public static int[][] huffmanTableG = {new int[]{-512, 3, 8, 0}, new int[]{256, 3, 8, 1}, new int[]{512, 3, 9, 2}, new int[]{1024, 3, 10, 3}, new int[]{-1024, 4, 9, 8}, new int[]{-256, 4, 7, 9}, new int[]{-32, 4, 5, 10}, new int[]{0, 4, 5, 11}, new int[]{128, 4, 7, 12}, new int[]{-128, 5, 6, 26}, new int[]{-64, 5, 5, 27}, new int[]{32, 5, 5, 28}, new int[]{64, 5, 6, 29}, new int[]{-1025, 5, jbig2HuffmanLOW, 30}, new int[]{2048, 5, 32, 31}, new int[]{0, 0, jbig2HuffmanEOT, 0}};
    public static int[][] huffmanTableH = {new int[]{0, 2, 1, 0}, new int[]{0, 2, jbig2HuffmanOOB, 1}, new int[]{4, 3, 4, 4}, new int[]{-1, 4, 0, 10}, new int[]{22, 4, 4, 11}, new int[]{38, 4, 5, 12}, new int[]{2, 5, 0, 26}, new int[]{70, 5, 6, 27}, new int[]{134, 5, 7, 28}, new int[]{3, 6, 0, 58}, new int[]{20, 6, 1, 59}, new int[]{262, 6, 7, 60}, new int[]{646, 6, 10, 61}, new int[]{-2, 7, 0, 124}, new int[]{390, 7, 8, 125}, new int[]{-15, 8, 3, 252}, new int[]{-5, 8, 1, 253}, new int[]{-7, 9, 1, 508}, new int[]{-3, 9, 0, 509}, new int[]{-16, 9, jbig2HuffmanLOW, CharacterType.ALPHA_MASK}, new int[]{1670, 9, 32, WindowEvent.RESIZE}, new int[]{0, 0, jbig2HuffmanEOT, 0}};
    public static int[][] huffmanTableI = {new int[]{0, 2, jbig2HuffmanOOB, 0}, new int[]{-1, 3, 1, 2}, new int[]{1, 3, 1, 3}, new int[]{7, 3, 5, 4}, new int[]{-3, 4, 1, 10}, new int[]{43, 4, 5, 11}, new int[]{75, 4, 6, 12}, new int[]{3, 5, 1, 26}, new int[]{139, 5, 7, 27}, new int[]{WindowsConstants.ERROR_DIRECTORY, 5, 8, 28}, new int[]{5, 6, 1, 58}, new int[]{39, 6, 2, 59}, new int[]{523, 6, 8, 60}, new int[]{1291, 6, 11, 61}, new int[]{-5, 7, 1, 124}, new int[]{DLSModulator.CONN_DST_EG2_DECAYTIME, 7, 9, 125}, new int[]{-31, 8, 4, 252}, new int[]{-11, 8, 2, 253}, new int[]{-15, 9, 2, 508}, new int[]{-7, 9, 1, 509}, new int[]{-32, 9, jbig2HuffmanLOW, CharacterType.ALPHA_MASK}, new int[]{3339, 9, 32, WindowEvent.RESIZE}, new int[]{0, 0, jbig2HuffmanEOT, 0}};
    public static int[][] huffmanTableJ = {new int[]{-2, 2, 2, 0}, new int[]{6, 2, 6, 1}, new int[]{0, 2, jbig2HuffmanOOB, 2}, new int[]{-3, 5, 0, 24}, new int[]{2, 5, 0, 25}, new int[]{70, 5, 5, 26}, new int[]{3, 6, 0, 54}, new int[]{102, 6, 5, 55}, new int[]{134, 6, 6, 56}, new int[]{198, 6, 7, 57}, new int[]{326, 6, 8, 58}, new int[]{582, 6, 9, 59}, new int[]{1094, 6, 10, 60}, new int[]{-21, 7, 4, 122}, new int[]{-4, 7, 0, 123}, new int[]{4, 7, 0, 124}, new int[]{2118, 7, 11, 125}, new int[]{-5, 8, 0, 252}, new int[]{5, 8, 0, 253}, new int[]{-22, 8, jbig2HuffmanLOW, 254}, new int[]{4166, 8, 32, 255}, new int[]{0, 0, jbig2HuffmanEOT, 0}};
    public static int[][] huffmanTableK = {new int[]{1, 1, 0, 0}, new int[]{2, 2, 1, 2}, new int[]{4, 4, 0, 12}, new int[]{5, 4, 1, 13}, new int[]{7, 5, 1, 28}, new int[]{9, 5, 2, 29}, new int[]{13, 6, 2, 60}, new int[]{17, 7, 2, 122}, new int[]{21, 7, 3, 123}, new int[]{29, 7, 4, 124}, new int[]{45, 7, 5, 125}, new int[]{77, 7, 6, 126}, new int[]{141, 7, 32, 127}, new int[]{0, 0, jbig2HuffmanEOT, 0}};
    public static int[][] huffmanTableL = {new int[]{1, 1, 0, 0}, new int[]{2, 2, 0, 2}, new int[]{3, 3, 1, 6}, new int[]{5, 5, 0, 28}, new int[]{6, 5, 1, 29}, new int[]{8, 6, 1, 60}, new int[]{10, 7, 0, 122}, new int[]{11, 7, 1, 123}, new int[]{13, 7, 2, 124}, new int[]{17, 7, 3, 125}, new int[]{25, 7, 4, 126}, new int[]{41, 8, 5, 254}, new int[]{73, 8, 32, 255}, new int[]{0, 0, jbig2HuffmanEOT, 0}};
    public static int[][] huffmanTableM = {new int[]{1, 1, 0, 0}, new int[]{2, 3, 0, 4}, new int[]{7, 3, 3, 5}, new int[]{3, 4, 0, 12}, new int[]{5, 4, 1, 13}, new int[]{4, 5, 0, 28}, new int[]{15, 6, 1, 58}, new int[]{17, 6, 2, 59}, new int[]{21, 6, 3, 60}, new int[]{29, 6, 4, 61}, new int[]{45, 6, 5, 62}, new int[]{77, 7, 6, 126}, new int[]{141, 7, 32, 127}, new int[]{0, 0, jbig2HuffmanEOT, 0}};
    public static int[][] huffmanTableN = {new int[]{0, 1, 0, 0}, new int[]{-2, 3, 0, 4}, new int[]{-1, 3, 0, 5}, new int[]{1, 3, 0, 6}, new int[]{2, 3, 0, 7}, new int[]{0, 0, jbig2HuffmanEOT, 0}};
    public static int[][] huffmanTableO = {new int[]{0, 1, 0, 0}, new int[]{-1, 3, 0, 4}, new int[]{1, 3, 0, 5}, new int[]{-2, 4, 0, 12}, new int[]{2, 4, 0, 13}, new int[]{-4, 5, 1, 28}, new int[]{3, 5, 1, 29}, new int[]{-8, 6, 2, 60}, new int[]{5, 6, 2, 61}, new int[]{-24, 7, 4, 124}, new int[]{9, 7, 4, 125}, new int[]{-25, 7, jbig2HuffmanLOW, 126}, new int[]{25, 7, 32, 127}, new int[]{0, 0, jbig2HuffmanEOT, 0}};

    private HuffmanDecoder() {
    }

    public HuffmanDecoder(StreamReader reader) {
        this.reader = reader;
    }

    public DecodeIntResult decodeInt(int[][] table) throws IOException {
        int decodedInt;
        int length = 0;
        int prefix = 0;
        for (int i2 = 0; table[i2][2] != jbig2HuffmanEOT; i2++) {
            while (length < table[i2][1]) {
                int bit = this.reader.readBit();
                prefix = (prefix << 1) | bit;
                length++;
            }
            if (prefix == table[i2][3]) {
                if (table[i2][2] == jbig2HuffmanOOB) {
                    return new DecodeIntResult(-1, false);
                }
                if (table[i2][2] == jbig2HuffmanLOW) {
                    int readBits = this.reader.readBits(32);
                    decodedInt = table[i2][0] - readBits;
                } else if (table[i2][2] > 0) {
                    int readBits2 = this.reader.readBits(table[i2][2]);
                    decodedInt = table[i2][0] + readBits2;
                } else {
                    decodedInt = table[i2][0];
                }
                return new DecodeIntResult(decodedInt, true);
            }
        }
        return new DecodeIntResult(-1, false);
    }

    public static int[][] buildTable(int[][] table, int length) {
        int i2 = 0;
        while (i2 < length) {
            int j2 = i2;
            while (j2 < length && table[j2][1] == 0) {
                j2++;
            }
            if (j2 == length) {
                break;
            }
            for (int k2 = j2 + 1; k2 < length; k2++) {
                if (table[k2][1] > 0 && table[k2][1] < table[j2][1]) {
                    j2 = k2;
                }
            }
            if (j2 != i2) {
                int[] tab = table[j2];
                for (int k3 = j2; k3 > i2; k3--) {
                    table[k3] = table[k3 - 1];
                }
                table[i2] = tab;
            }
            i2++;
        }
        table[i2] = table[length];
        int prefix = 0 + 1;
        table[0][3] = 0;
        for (int i3 = 0 + 1; table[i3][2] != jbig2HuffmanEOT; i3++) {
            int prefix2 = prefix << (table[i3][1] - table[i3 - 1][1]);
            prefix = prefix2 + 1;
            table[i3][3] = prefix2;
        }
        return table;
    }
}
