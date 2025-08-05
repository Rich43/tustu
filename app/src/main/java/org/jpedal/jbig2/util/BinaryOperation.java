package org.jpedal.jbig2.util;

/* loaded from: icepdf-core.jar:org/jpedal/jbig2/util/BinaryOperation.class */
public class BinaryOperation {
    public static final int LEFT_SHIFT = 0;
    public static final int RIGHT_SHIFT = 1;

    public static int getInt32(short[] number) {
        return (number[0] << 24) | (number[1] << 16) | (number[2] << 8) | number[3];
    }

    public static int getInt16(short[] number) {
        return (number[0] << 8) | number[1];
    }

    public static long bit32Shift(long number, int shift, int direction) {
        long number2;
        if (direction == 0) {
            number2 = number << shift;
        } else {
            number2 = number >> shift;
        }
        return number2 & 4294967295L;
    }

    public static int bit8Shift(int number, int shift, int direction) {
        int number2;
        if (direction == 0) {
            number2 = number << shift;
        } else {
            number2 = number >> shift;
        }
        return number2 & 255;
    }

    public static int getInt32(byte[] number) {
        return (number[0] << 24) | (number[1] << 16) | (number[2] << 8) | number[3];
    }
}
