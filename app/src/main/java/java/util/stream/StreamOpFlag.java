package java.util.stream;

import java.util.EnumMap;
import java.util.Map;
import java.util.Spliterator;

/* loaded from: rt.jar:java/util/stream/StreamOpFlag.class */
enum StreamOpFlag {
    DISTINCT(0, set(Type.SPLITERATOR).set(Type.STREAM).setAndClear(Type.OP)),
    SORTED(1, set(Type.SPLITERATOR).set(Type.STREAM).setAndClear(Type.OP)),
    ORDERED(2, set(Type.SPLITERATOR).set(Type.STREAM).setAndClear(Type.OP).clear(Type.TERMINAL_OP).clear(Type.UPSTREAM_TERMINAL_OP)),
    SIZED(3, set(Type.SPLITERATOR).set(Type.STREAM).clear(Type.OP)),
    SHORT_CIRCUIT(12, set(Type.OP).set(Type.TERMINAL_OP));

    private static final int SET_BITS = 1;
    private static final int CLEAR_BITS = 2;
    private static final int PRESERVE_BITS = 3;
    private final Map<Type, Integer> maskTable;
    private final int bitPosition;
    private final int set;
    private final int clear;
    private final int preserve;
    static final int SPLITERATOR_CHARACTERISTICS_MASK = createMask(Type.SPLITERATOR);
    static final int STREAM_MASK = createMask(Type.STREAM);
    static final int OP_MASK = createMask(Type.OP);
    static final int TERMINAL_OP_MASK = createMask(Type.TERMINAL_OP);
    static final int UPSTREAM_TERMINAL_OP_MASK = createMask(Type.UPSTREAM_TERMINAL_OP);
    private static final int FLAG_MASK = createFlagMask();
    private static final int FLAG_MASK_IS = STREAM_MASK;
    private static final int FLAG_MASK_NOT = STREAM_MASK << 1;
    static final int INITIAL_OPS_VALUE = FLAG_MASK_IS | FLAG_MASK_NOT;
    static final int IS_DISTINCT = DISTINCT.set;
    static final int NOT_DISTINCT = DISTINCT.clear;
    static final int IS_SORTED = SORTED.set;
    static final int NOT_SORTED = SORTED.clear;
    static final int IS_ORDERED = ORDERED.set;
    static final int NOT_ORDERED = ORDERED.clear;
    static final int IS_SIZED = SIZED.set;
    static final int NOT_SIZED = SIZED.clear;
    static final int IS_SHORT_CIRCUIT = SHORT_CIRCUIT.set;

    /* loaded from: rt.jar:java/util/stream/StreamOpFlag$Type.class */
    enum Type {
        SPLITERATOR,
        STREAM,
        OP,
        TERMINAL_OP,
        UPSTREAM_TERMINAL_OP
    }

    private static MaskBuilder set(Type type) {
        return new MaskBuilder(new EnumMap(Type.class)).set(type);
    }

    /* loaded from: rt.jar:java/util/stream/StreamOpFlag$MaskBuilder.class */
    private static class MaskBuilder {
        final Map<Type, Integer> map;

        MaskBuilder(Map<Type, Integer> map) {
            this.map = map;
        }

        MaskBuilder mask(Type type, Integer num) {
            this.map.put(type, num);
            return this;
        }

        MaskBuilder set(Type type) {
            return mask(type, 1);
        }

        MaskBuilder clear(Type type) {
            return mask(type, 2);
        }

        MaskBuilder setAndClear(Type type) {
            return mask(type, 3);
        }

        Map<Type, Integer> build() {
            for (Type type : Type.values()) {
                this.map.putIfAbsent(type, 0);
            }
            return this.map;
        }
    }

    StreamOpFlag(int i2, MaskBuilder maskBuilder) {
        this.maskTable = maskBuilder.build();
        int i3 = i2 * 2;
        this.bitPosition = i3;
        this.set = 1 << i3;
        this.clear = 2 << i3;
        this.preserve = 3 << i3;
    }

    int set() {
        return this.set;
    }

    int clear() {
        return this.clear;
    }

    boolean isStreamFlag() {
        return this.maskTable.get(Type.STREAM).intValue() > 0;
    }

    boolean isKnown(int i2) {
        return (i2 & this.preserve) == this.set;
    }

    boolean isCleared(int i2) {
        return (i2 & this.preserve) == this.clear;
    }

    boolean isPreserved(int i2) {
        return (i2 & this.preserve) == this.preserve;
    }

    boolean canSet(Type type) {
        return (this.maskTable.get(type).intValue() & 1) > 0;
    }

    private static int createMask(Type type) {
        int iIntValue = 0;
        for (StreamOpFlag streamOpFlag : values()) {
            iIntValue |= streamOpFlag.maskTable.get(type).intValue() << streamOpFlag.bitPosition;
        }
        return iIntValue;
    }

    private static int createFlagMask() {
        int i2 = 0;
        for (StreamOpFlag streamOpFlag : values()) {
            i2 |= streamOpFlag.preserve;
        }
        return i2;
    }

    private static int getMask(int i2) {
        return i2 == 0 ? FLAG_MASK : ((i2 | ((FLAG_MASK_IS & i2) << 1)) | ((FLAG_MASK_NOT & i2) >> 1)) ^ (-1);
    }

    static int combineOpFlags(int i2, int i3) {
        return (i3 & getMask(i2)) | i2;
    }

    static int toStreamFlags(int i2) {
        return ((i2 ^ (-1)) >> 1) & FLAG_MASK_IS & i2;
    }

    static int toCharacteristics(int i2) {
        return i2 & SPLITERATOR_CHARACTERISTICS_MASK;
    }

    static int fromCharacteristics(Spliterator<?> spliterator) {
        int iCharacteristics = spliterator.characteristics();
        if ((iCharacteristics & 4) != 0 && spliterator.getComparator() != null) {
            return iCharacteristics & SPLITERATOR_CHARACTERISTICS_MASK & (-5);
        }
        return iCharacteristics & SPLITERATOR_CHARACTERISTICS_MASK;
    }

    static int fromCharacteristics(int i2) {
        return i2 & SPLITERATOR_CHARACTERISTICS_MASK;
    }
}
