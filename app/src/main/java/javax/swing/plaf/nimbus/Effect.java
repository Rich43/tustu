package javax.swing.plaf.nimbus;

import java.awt.image.BufferedImage;
import java.lang.ref.SoftReference;
import sun.awt.AppContext;

/* loaded from: rt.jar:javax/swing/plaf/nimbus/Effect.class */
abstract class Effect {

    /* loaded from: rt.jar:javax/swing/plaf/nimbus/Effect$EffectType.class */
    enum EffectType {
        UNDER,
        BLENDED,
        OVER
    }

    abstract EffectType getEffectType();

    abstract float getOpacity();

    abstract BufferedImage applyEffect(BufferedImage bufferedImage, BufferedImage bufferedImage2, int i2, int i3);

    Effect() {
    }

    protected static ArrayCache getArrayCache() {
        ArrayCache arrayCache = (ArrayCache) AppContext.getAppContext().get(ArrayCache.class);
        if (arrayCache == null) {
            arrayCache = new ArrayCache();
            AppContext.getAppContext().put(ArrayCache.class, arrayCache);
        }
        return arrayCache;
    }

    /* loaded from: rt.jar:javax/swing/plaf/nimbus/Effect$ArrayCache.class */
    protected static class ArrayCache {
        private SoftReference<int[]> tmpIntArray = null;
        private SoftReference<byte[]> tmpByteArray1 = null;
        private SoftReference<byte[]> tmpByteArray2 = null;
        private SoftReference<byte[]> tmpByteArray3 = null;

        protected ArrayCache() {
        }

        /* JADX WARN: Removed duplicated region for block: B:8:0x001c  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        protected int[] getTmpIntArray(int r6) {
            /*
                r5 = this;
                r0 = r5
                java.lang.ref.SoftReference<int[]> r0 = r0.tmpIntArray
                if (r0 == 0) goto L1c
                r0 = r5
                java.lang.ref.SoftReference<int[]> r0 = r0.tmpIntArray
                java.lang.Object r0 = r0.get()
                int[] r0 = (int[]) r0
                r1 = r0
                r7 = r1
                if (r0 == 0) goto L1c
                r0 = r7
                int r0 = r0.length
                r1 = r6
                if (r0 >= r1) goto L2c
            L1c:
                r0 = r6
                int[] r0 = new int[r0]
                r7 = r0
                r0 = r5
                java.lang.ref.SoftReference r1 = new java.lang.ref.SoftReference
                r2 = r1
                r3 = r7
                r2.<init>(r3)
                r0.tmpIntArray = r1
            L2c:
                r0 = r7
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: javax.swing.plaf.nimbus.Effect.ArrayCache.getTmpIntArray(int):int[]");
        }

        /* JADX WARN: Removed duplicated region for block: B:8:0x001c  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        protected byte[] getTmpByteArray1(int r6) {
            /*
                r5 = this;
                r0 = r5
                java.lang.ref.SoftReference<byte[]> r0 = r0.tmpByteArray1
                if (r0 == 0) goto L1c
                r0 = r5
                java.lang.ref.SoftReference<byte[]> r0 = r0.tmpByteArray1
                java.lang.Object r0 = r0.get()
                byte[] r0 = (byte[]) r0
                r1 = r0
                r7 = r1
                if (r0 == 0) goto L1c
                r0 = r7
                int r0 = r0.length
                r1 = r6
                if (r0 >= r1) goto L2c
            L1c:
                r0 = r6
                byte[] r0 = new byte[r0]
                r7 = r0
                r0 = r5
                java.lang.ref.SoftReference r1 = new java.lang.ref.SoftReference
                r2 = r1
                r3 = r7
                r2.<init>(r3)
                r0.tmpByteArray1 = r1
            L2c:
                r0 = r7
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: javax.swing.plaf.nimbus.Effect.ArrayCache.getTmpByteArray1(int):byte[]");
        }

        /* JADX WARN: Removed duplicated region for block: B:8:0x001c  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        protected byte[] getTmpByteArray2(int r6) {
            /*
                r5 = this;
                r0 = r5
                java.lang.ref.SoftReference<byte[]> r0 = r0.tmpByteArray2
                if (r0 == 0) goto L1c
                r0 = r5
                java.lang.ref.SoftReference<byte[]> r0 = r0.tmpByteArray2
                java.lang.Object r0 = r0.get()
                byte[] r0 = (byte[]) r0
                r1 = r0
                r7 = r1
                if (r0 == 0) goto L1c
                r0 = r7
                int r0 = r0.length
                r1 = r6
                if (r0 >= r1) goto L2c
            L1c:
                r0 = r6
                byte[] r0 = new byte[r0]
                r7 = r0
                r0 = r5
                java.lang.ref.SoftReference r1 = new java.lang.ref.SoftReference
                r2 = r1
                r3 = r7
                r2.<init>(r3)
                r0.tmpByteArray2 = r1
            L2c:
                r0 = r7
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: javax.swing.plaf.nimbus.Effect.ArrayCache.getTmpByteArray2(int):byte[]");
        }

        /* JADX WARN: Removed duplicated region for block: B:8:0x001c  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        protected byte[] getTmpByteArray3(int r6) {
            /*
                r5 = this;
                r0 = r5
                java.lang.ref.SoftReference<byte[]> r0 = r0.tmpByteArray3
                if (r0 == 0) goto L1c
                r0 = r5
                java.lang.ref.SoftReference<byte[]> r0 = r0.tmpByteArray3
                java.lang.Object r0 = r0.get()
                byte[] r0 = (byte[]) r0
                r1 = r0
                r7 = r1
                if (r0 == 0) goto L1c
                r0 = r7
                int r0 = r0.length
                r1 = r6
                if (r0 >= r1) goto L2c
            L1c:
                r0 = r6
                byte[] r0 = new byte[r0]
                r7 = r0
                r0 = r5
                java.lang.ref.SoftReference r1 = new java.lang.ref.SoftReference
                r2 = r1
                r3 = r7
                r2.<init>(r3)
                r0.tmpByteArray3 = r1
            L2c:
                r0 = r7
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: javax.swing.plaf.nimbus.Effect.ArrayCache.getTmpByteArray3(int):byte[]");
        }
    }
}
