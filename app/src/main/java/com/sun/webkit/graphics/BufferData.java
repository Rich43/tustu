package com.sun.webkit.graphics;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

/* compiled from: WCRenderQueue.java */
/* loaded from: jfxrt.jar:com/sun/webkit/graphics/BufferData.class */
final class BufferData {
    private final AtomicInteger idCount = new AtomicInteger(0);
    private final HashMap<Integer, String> strMap = new HashMap<>();
    private final HashMap<Integer, int[]> intArrMap = new HashMap<>();
    private final HashMap<Integer, float[]> floatArrMap = new HashMap<>();
    private ByteBuffer buffer;

    BufferData() {
    }

    private int createID() {
        return this.idCount.incrementAndGet();
    }

    int addIntArray(int[] a2) {
        int id = createID();
        this.intArrMap.put(Integer.valueOf(id), a2);
        return id;
    }

    int[] getIntArray(int id) {
        return this.intArrMap.get(Integer.valueOf(id));
    }

    int addFloatArray(float[] a2) {
        int id = createID();
        this.floatArrMap.put(Integer.valueOf(id), a2);
        return id;
    }

    float[] getFloatArray(int id) {
        return this.floatArrMap.get(Integer.valueOf(id));
    }

    int addString(String s2) {
        int id = createID();
        this.strMap.put(Integer.valueOf(id), s2);
        return id;
    }

    String getString(int id) {
        return this.strMap.get(Integer.valueOf(id));
    }

    ByteBuffer getBuffer() {
        return this.buffer;
    }

    void setBuffer(ByteBuffer buffer) {
        this.buffer = buffer;
    }
}
