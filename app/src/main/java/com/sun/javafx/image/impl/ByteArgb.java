package com.sun.javafx.image.impl;

import com.sun.javafx.image.AlphaType;
import com.sun.javafx.image.BytePixelAccessor;
import com.sun.javafx.image.BytePixelGetter;
import com.sun.javafx.image.BytePixelSetter;
import com.sun.javafx.image.PixelUtils;
import java.nio.ByteBuffer;

/* loaded from: jfxrt.jar:com/sun/javafx/image/impl/ByteArgb.class */
public class ByteArgb {
    public static final BytePixelGetter getter = Accessor.instance;
    public static final BytePixelSetter setter = Accessor.instance;
    public static final BytePixelAccessor accessor = Accessor.instance;

    /* loaded from: jfxrt.jar:com/sun/javafx/image/impl/ByteArgb$Accessor.class */
    static class Accessor implements BytePixelAccessor {
        static final BytePixelAccessor instance = new Accessor();

        private Accessor() {
        }

        @Override // com.sun.javafx.image.PixelGetter, com.sun.javafx.image.PixelSetter
        public AlphaType getAlphaType() {
            return AlphaType.NONPREMULTIPLIED;
        }

        @Override // com.sun.javafx.image.PixelGetter, com.sun.javafx.image.PixelSetter
        public int getNumElements() {
            return 4;
        }

        @Override // com.sun.javafx.image.BytePixelGetter
        public int getArgb(byte[] arr, int offset) {
            return (arr[offset] << 24) | ((arr[offset + 1] & 255) << 16) | ((arr[offset + 2] & 255) << 8) | (arr[offset + 3] & 255);
        }

        @Override // com.sun.javafx.image.BytePixelGetter
        public int getArgbPre(byte[] arr, int offset) {
            return PixelUtils.NonPretoPre(getArgb(arr, offset));
        }

        @Override // com.sun.javafx.image.PixelGetter
        public int getArgb(ByteBuffer buf, int offset) {
            return (buf.get(offset) << 24) | ((buf.get(offset + 1) & 255) << 16) | ((buf.get(offset + 2) & 255) << 8) | (buf.get(offset + 3) & 255);
        }

        @Override // com.sun.javafx.image.PixelGetter
        public int getArgbPre(ByteBuffer buf, int offset) {
            return PixelUtils.NonPretoPre(getArgb(buf, offset));
        }

        @Override // com.sun.javafx.image.BytePixelSetter
        public void setArgb(byte[] arr, int offset, int argb) {
            arr[offset] = (byte) (argb >> 24);
            arr[offset + 1] = (byte) (argb >> 16);
            arr[offset + 2] = (byte) (argb >> 8);
            arr[offset + 3] = (byte) argb;
        }

        @Override // com.sun.javafx.image.BytePixelSetter
        public void setArgbPre(byte[] arr, int offset, int argbpre) {
            setArgb(arr, offset, PixelUtils.PretoNonPre(argbpre));
        }

        @Override // com.sun.javafx.image.PixelSetter
        public void setArgb(ByteBuffer buf, int offset, int argb) {
            buf.put(offset, (byte) (argb >> 24));
            buf.put(offset + 1, (byte) (argb >> 16));
            buf.put(offset + 2, (byte) (argb >> 8));
            buf.put(offset + 3, (byte) argb);
        }

        @Override // com.sun.javafx.image.PixelSetter
        public void setArgbPre(ByteBuffer buf, int offset, int argbpre) {
            setArgb(buf, offset, PixelUtils.PretoNonPre(argbpre));
        }
    }
}
