package com.sun.media.sound;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import javax.sound.sampled.AudioFormat;
import sun.misc.FloatConsts;

/* loaded from: rt.jar:com/sun/media/sound/AudioFloatConverter.class */
public abstract class AudioFloatConverter {
    private AudioFormat format;

    public abstract float[] toFloatArray(byte[] bArr, int i2, float[] fArr, int i3, int i4);

    public abstract byte[] toByteArray(float[] fArr, int i2, int i3, byte[] bArr, int i4);

    /* loaded from: rt.jar:com/sun/media/sound/AudioFloatConverter$AudioFloatLSBFilter.class */
    private static class AudioFloatLSBFilter extends AudioFloatConverter {
        private final AudioFloatConverter converter;
        private final int offset;
        private final int stepsize;
        private final byte mask;
        private byte[] mask_buffer;

        AudioFloatLSBFilter(AudioFloatConverter audioFloatConverter, AudioFormat audioFormat) {
            int sampleSizeInBits = audioFormat.getSampleSizeInBits();
            boolean zIsBigEndian = audioFormat.isBigEndian();
            this.converter = audioFloatConverter;
            this.stepsize = (sampleSizeInBits + 7) / 8;
            this.offset = zIsBigEndian ? this.stepsize - 1 : 0;
            int i2 = sampleSizeInBits % 8;
            if (i2 == 0) {
                this.mask = (byte) 0;
                return;
            }
            if (i2 == 1) {
                this.mask = Byte.MIN_VALUE;
                return;
            }
            if (i2 == 2) {
                this.mask = (byte) -64;
                return;
            }
            if (i2 == 3) {
                this.mask = (byte) -32;
                return;
            }
            if (i2 == 4) {
                this.mask = (byte) -16;
                return;
            }
            if (i2 == 5) {
                this.mask = (byte) -8;
                return;
            }
            if (i2 == 6) {
                this.mask = (byte) -4;
            } else if (i2 == 7) {
                this.mask = (byte) -2;
            } else {
                this.mask = (byte) -1;
            }
        }

        @Override // com.sun.media.sound.AudioFloatConverter
        public byte[] toByteArray(float[] fArr, int i2, int i3, byte[] bArr, int i4) {
            byte[] byteArray = this.converter.toByteArray(fArr, i2, i3, bArr, i4);
            int i5 = i3 * this.stepsize;
            int i6 = i4;
            int i7 = this.offset;
            while (true) {
                int i8 = i6 + i7;
                if (i8 < i5) {
                    bArr[i8] = (byte) (bArr[i8] & this.mask);
                    i6 = i8;
                    i7 = this.stepsize;
                } else {
                    return byteArray;
                }
            }
        }

        @Override // com.sun.media.sound.AudioFloatConverter
        public float[] toFloatArray(byte[] bArr, int i2, float[] fArr, int i3, int i4) {
            if (this.mask_buffer == null || this.mask_buffer.length < bArr.length) {
                this.mask_buffer = new byte[bArr.length];
            }
            System.arraycopy(bArr, 0, this.mask_buffer, 0, bArr.length);
            int i5 = i4 * this.stepsize;
            int i6 = i2;
            int i7 = this.offset;
            while (true) {
                int i8 = i6 + i7;
                if (i8 < i5) {
                    this.mask_buffer[i8] = (byte) (this.mask_buffer[i8] & this.mask);
                    i6 = i8;
                    i7 = this.stepsize;
                } else {
                    return this.converter.toFloatArray(this.mask_buffer, i2, fArr, i3, i4);
                }
            }
        }
    }

    /* loaded from: rt.jar:com/sun/media/sound/AudioFloatConverter$AudioFloatConversion64L.class */
    private static class AudioFloatConversion64L extends AudioFloatConverter {
        ByteBuffer bytebuffer;
        DoubleBuffer floatbuffer;
        double[] double_buff;

        private AudioFloatConversion64L() {
            this.bytebuffer = null;
            this.floatbuffer = null;
            this.double_buff = null;
        }

        @Override // com.sun.media.sound.AudioFloatConverter
        public float[] toFloatArray(byte[] bArr, int i2, float[] fArr, int i3, int i4) {
            int i5 = i4 * 8;
            if (this.bytebuffer == null || this.bytebuffer.capacity() < i5) {
                this.bytebuffer = ByteBuffer.allocate(i5).order(ByteOrder.LITTLE_ENDIAN);
                this.floatbuffer = this.bytebuffer.asDoubleBuffer();
            }
            this.bytebuffer.position(0);
            this.floatbuffer.position(0);
            this.bytebuffer.put(bArr, i2, i5);
            if (this.double_buff == null || this.double_buff.length < i4 + i3) {
                this.double_buff = new double[i4 + i3];
            }
            this.floatbuffer.get(this.double_buff, i3, i4);
            int i6 = i3 + i4;
            for (int i7 = i3; i7 < i6; i7++) {
                fArr[i7] = (float) this.double_buff[i7];
            }
            return fArr;
        }

        @Override // com.sun.media.sound.AudioFloatConverter
        public byte[] toByteArray(float[] fArr, int i2, int i3, byte[] bArr, int i4) {
            int i5 = i3 * 8;
            if (this.bytebuffer == null || this.bytebuffer.capacity() < i5) {
                this.bytebuffer = ByteBuffer.allocate(i5).order(ByteOrder.LITTLE_ENDIAN);
                this.floatbuffer = this.bytebuffer.asDoubleBuffer();
            }
            this.floatbuffer.position(0);
            this.bytebuffer.position(0);
            if (this.double_buff == null || this.double_buff.length < i2 + i3) {
                this.double_buff = new double[i2 + i3];
            }
            int i6 = i2 + i3;
            for (int i7 = i2; i7 < i6; i7++) {
                this.double_buff[i7] = fArr[i7];
            }
            this.floatbuffer.put(this.double_buff, i2, i3);
            this.bytebuffer.get(bArr, i4, i5);
            return bArr;
        }
    }

    /* loaded from: rt.jar:com/sun/media/sound/AudioFloatConverter$AudioFloatConversion64B.class */
    private static class AudioFloatConversion64B extends AudioFloatConverter {
        ByteBuffer bytebuffer;
        DoubleBuffer floatbuffer;
        double[] double_buff;

        private AudioFloatConversion64B() {
            this.bytebuffer = null;
            this.floatbuffer = null;
            this.double_buff = null;
        }

        @Override // com.sun.media.sound.AudioFloatConverter
        public float[] toFloatArray(byte[] bArr, int i2, float[] fArr, int i3, int i4) {
            int i5 = i4 * 8;
            if (this.bytebuffer == null || this.bytebuffer.capacity() < i5) {
                this.bytebuffer = ByteBuffer.allocate(i5).order(ByteOrder.BIG_ENDIAN);
                this.floatbuffer = this.bytebuffer.asDoubleBuffer();
            }
            this.bytebuffer.position(0);
            this.floatbuffer.position(0);
            this.bytebuffer.put(bArr, i2, i5);
            if (this.double_buff == null || this.double_buff.length < i4 + i3) {
                this.double_buff = new double[i4 + i3];
            }
            this.floatbuffer.get(this.double_buff, i3, i4);
            int i6 = i3 + i4;
            for (int i7 = i3; i7 < i6; i7++) {
                fArr[i7] = (float) this.double_buff[i7];
            }
            return fArr;
        }

        @Override // com.sun.media.sound.AudioFloatConverter
        public byte[] toByteArray(float[] fArr, int i2, int i3, byte[] bArr, int i4) {
            int i5 = i3 * 8;
            if (this.bytebuffer == null || this.bytebuffer.capacity() < i5) {
                this.bytebuffer = ByteBuffer.allocate(i5).order(ByteOrder.BIG_ENDIAN);
                this.floatbuffer = this.bytebuffer.asDoubleBuffer();
            }
            this.floatbuffer.position(0);
            this.bytebuffer.position(0);
            if (this.double_buff == null || this.double_buff.length < i2 + i3) {
                this.double_buff = new double[i2 + i3];
            }
            int i6 = i2 + i3;
            for (int i7 = i2; i7 < i6; i7++) {
                this.double_buff[i7] = fArr[i7];
            }
            this.floatbuffer.put(this.double_buff, i2, i3);
            this.bytebuffer.get(bArr, i4, i5);
            return bArr;
        }
    }

    /* loaded from: rt.jar:com/sun/media/sound/AudioFloatConverter$AudioFloatConversion32L.class */
    private static class AudioFloatConversion32L extends AudioFloatConverter {
        ByteBuffer bytebuffer;
        FloatBuffer floatbuffer;

        private AudioFloatConversion32L() {
            this.bytebuffer = null;
            this.floatbuffer = null;
        }

        @Override // com.sun.media.sound.AudioFloatConverter
        public float[] toFloatArray(byte[] bArr, int i2, float[] fArr, int i3, int i4) {
            int i5 = i4 * 4;
            if (this.bytebuffer == null || this.bytebuffer.capacity() < i5) {
                this.bytebuffer = ByteBuffer.allocate(i5).order(ByteOrder.LITTLE_ENDIAN);
                this.floatbuffer = this.bytebuffer.asFloatBuffer();
            }
            this.bytebuffer.position(0);
            this.floatbuffer.position(0);
            this.bytebuffer.put(bArr, i2, i5);
            this.floatbuffer.get(fArr, i3, i4);
            return fArr;
        }

        @Override // com.sun.media.sound.AudioFloatConverter
        public byte[] toByteArray(float[] fArr, int i2, int i3, byte[] bArr, int i4) {
            int i5 = i3 * 4;
            if (this.bytebuffer == null || this.bytebuffer.capacity() < i5) {
                this.bytebuffer = ByteBuffer.allocate(i5).order(ByteOrder.LITTLE_ENDIAN);
                this.floatbuffer = this.bytebuffer.asFloatBuffer();
            }
            this.floatbuffer.position(0);
            this.bytebuffer.position(0);
            this.floatbuffer.put(fArr, i2, i3);
            this.bytebuffer.get(bArr, i4, i5);
            return bArr;
        }
    }

    /* loaded from: rt.jar:com/sun/media/sound/AudioFloatConverter$AudioFloatConversion32B.class */
    private static class AudioFloatConversion32B extends AudioFloatConverter {
        ByteBuffer bytebuffer;
        FloatBuffer floatbuffer;

        private AudioFloatConversion32B() {
            this.bytebuffer = null;
            this.floatbuffer = null;
        }

        @Override // com.sun.media.sound.AudioFloatConverter
        public float[] toFloatArray(byte[] bArr, int i2, float[] fArr, int i3, int i4) {
            int i5 = i4 * 4;
            if (this.bytebuffer == null || this.bytebuffer.capacity() < i5) {
                this.bytebuffer = ByteBuffer.allocate(i5).order(ByteOrder.BIG_ENDIAN);
                this.floatbuffer = this.bytebuffer.asFloatBuffer();
            }
            this.bytebuffer.position(0);
            this.floatbuffer.position(0);
            this.bytebuffer.put(bArr, i2, i5);
            this.floatbuffer.get(fArr, i3, i4);
            return fArr;
        }

        @Override // com.sun.media.sound.AudioFloatConverter
        public byte[] toByteArray(float[] fArr, int i2, int i3, byte[] bArr, int i4) {
            int i5 = i3 * 4;
            if (this.bytebuffer == null || this.bytebuffer.capacity() < i5) {
                this.bytebuffer = ByteBuffer.allocate(i5).order(ByteOrder.BIG_ENDIAN);
                this.floatbuffer = this.bytebuffer.asFloatBuffer();
            }
            this.floatbuffer.position(0);
            this.bytebuffer.position(0);
            this.floatbuffer.put(fArr, i2, i3);
            this.bytebuffer.get(bArr, i4, i5);
            return bArr;
        }
    }

    /* loaded from: rt.jar:com/sun/media/sound/AudioFloatConverter$AudioFloatConversion8S.class */
    private static class AudioFloatConversion8S extends AudioFloatConverter {
        private AudioFloatConversion8S() {
        }

        @Override // com.sun.media.sound.AudioFloatConverter
        public float[] toFloatArray(byte[] bArr, int i2, float[] fArr, int i3, int i4) {
            int i5 = i2;
            int i6 = i3;
            for (int i7 = 0; i7 < i4; i7++) {
                int i8 = i6;
                i6++;
                int i9 = i5;
                i5++;
                fArr[i8] = bArr[i9] * 0.007874016f;
            }
            return fArr;
        }

        @Override // com.sun.media.sound.AudioFloatConverter
        public byte[] toByteArray(float[] fArr, int i2, int i3, byte[] bArr, int i4) {
            int i5 = i2;
            int i6 = i4;
            for (int i7 = 0; i7 < i3; i7++) {
                int i8 = i6;
                i6++;
                int i9 = i5;
                i5++;
                bArr[i8] = (byte) (fArr[i9] * 127.0f);
            }
            return bArr;
        }
    }

    /* loaded from: rt.jar:com/sun/media/sound/AudioFloatConverter$AudioFloatConversion8U.class */
    private static class AudioFloatConversion8U extends AudioFloatConverter {
        private AudioFloatConversion8U() {
        }

        @Override // com.sun.media.sound.AudioFloatConverter
        public float[] toFloatArray(byte[] bArr, int i2, float[] fArr, int i3, int i4) {
            int i5 = i2;
            int i6 = i3;
            for (int i7 = 0; i7 < i4; i7++) {
                int i8 = i6;
                i6++;
                int i9 = i5;
                i5++;
                fArr[i8] = ((bArr[i9] & 255) - 127) * 0.007874016f;
            }
            return fArr;
        }

        @Override // com.sun.media.sound.AudioFloatConverter
        public byte[] toByteArray(float[] fArr, int i2, int i3, byte[] bArr, int i4) {
            int i5 = i2;
            int i6 = i4;
            for (int i7 = 0; i7 < i3; i7++) {
                int i8 = i6;
                i6++;
                int i9 = i5;
                i5++;
                bArr[i8] = (byte) (127.0f + (fArr[i9] * 127.0f));
            }
            return bArr;
        }
    }

    /* loaded from: rt.jar:com/sun/media/sound/AudioFloatConverter$AudioFloatConversion16SL.class */
    private static class AudioFloatConversion16SL extends AudioFloatConverter {
        private AudioFloatConversion16SL() {
        }

        @Override // com.sun.media.sound.AudioFloatConverter
        public float[] toFloatArray(byte[] bArr, int i2, float[] fArr, int i3, int i4) {
            int i5 = i2;
            int i6 = i3 + i4;
            for (int i7 = i3; i7 < i6; i7++) {
                int i8 = i5;
                i5 = i5 + 1 + 1;
                fArr[i7] = ((short) ((bArr[i8] & 255) | (bArr[r12] << 8))) * 3.051851E-5f;
            }
            return fArr;
        }

        @Override // com.sun.media.sound.AudioFloatConverter
        public byte[] toByteArray(float[] fArr, int i2, int i3, byte[] bArr, int i4) {
            int i5 = i4;
            int i6 = i2 + i3;
            for (int i7 = i2; i7 < i6; i7++) {
                int i8 = (int) (fArr[i7] * 32767.0d);
                int i9 = i5;
                int i10 = i5 + 1;
                bArr[i9] = (byte) i8;
                i5 = i10 + 1;
                bArr[i10] = (byte) (i8 >>> 8);
            }
            return bArr;
        }
    }

    /* loaded from: rt.jar:com/sun/media/sound/AudioFloatConverter$AudioFloatConversion16SB.class */
    private static class AudioFloatConversion16SB extends AudioFloatConverter {
        private AudioFloatConversion16SB() {
        }

        @Override // com.sun.media.sound.AudioFloatConverter
        public float[] toFloatArray(byte[] bArr, int i2, float[] fArr, int i3, int i4) {
            int i5 = i2;
            int i6 = i3;
            for (int i7 = 0; i7 < i4; i7++) {
                int i8 = i6;
                i6++;
                int i9 = i5;
                i5 = i5 + 1 + 1;
                fArr[i8] = ((short) ((bArr[i9] << 8) | (bArr[r12] & 255))) * 3.051851E-5f;
            }
            return fArr;
        }

        @Override // com.sun.media.sound.AudioFloatConverter
        public byte[] toByteArray(float[] fArr, int i2, int i3, byte[] bArr, int i4) {
            int i5 = i2;
            int i6 = i4;
            for (int i7 = 0; i7 < i3; i7++) {
                int i8 = i5;
                i5++;
                int i9 = (int) (fArr[i8] * 32767.0d);
                int i10 = i6;
                int i11 = i6 + 1;
                bArr[i10] = (byte) (i9 >>> 8);
                i6 = i11 + 1;
                bArr[i11] = (byte) i9;
            }
            return bArr;
        }
    }

    /* loaded from: rt.jar:com/sun/media/sound/AudioFloatConverter$AudioFloatConversion16UL.class */
    private static class AudioFloatConversion16UL extends AudioFloatConverter {
        private AudioFloatConversion16UL() {
        }

        @Override // com.sun.media.sound.AudioFloatConverter
        public float[] toFloatArray(byte[] bArr, int i2, float[] fArr, int i3, int i4) {
            int i5 = i2;
            int i6 = i3;
            for (int i7 = 0; i7 < i4; i7++) {
                int i8 = i5;
                int i9 = i5 + 1;
                i5 = i9 + 1;
                int i10 = (bArr[i8] & 255) | ((bArr[i9] & 255) << 8);
                int i11 = i6;
                i6++;
                fArr[i11] = (i10 - Short.MAX_VALUE) * 3.051851E-5f;
            }
            return fArr;
        }

        @Override // com.sun.media.sound.AudioFloatConverter
        public byte[] toByteArray(float[] fArr, int i2, int i3, byte[] bArr, int i4) {
            int i5 = i2;
            int i6 = i4;
            for (int i7 = 0; i7 < i3; i7++) {
                int i8 = i5;
                i5++;
                int i9 = Short.MAX_VALUE + ((int) (fArr[i8] * 32767.0d));
                int i10 = i6;
                int i11 = i6 + 1;
                bArr[i10] = (byte) i9;
                i6 = i11 + 1;
                bArr[i11] = (byte) (i9 >>> 8);
            }
            return bArr;
        }
    }

    /* loaded from: rt.jar:com/sun/media/sound/AudioFloatConverter$AudioFloatConversion16UB.class */
    private static class AudioFloatConversion16UB extends AudioFloatConverter {
        private AudioFloatConversion16UB() {
        }

        @Override // com.sun.media.sound.AudioFloatConverter
        public float[] toFloatArray(byte[] bArr, int i2, float[] fArr, int i3, int i4) {
            int i5 = i2;
            int i6 = i3;
            for (int i7 = 0; i7 < i4; i7++) {
                int i8 = i5;
                int i9 = i5 + 1;
                i5 = i9 + 1;
                int i10 = ((bArr[i8] & 255) << 8) | (bArr[i9] & 255);
                int i11 = i6;
                i6++;
                fArr[i11] = (i10 - Short.MAX_VALUE) * 3.051851E-5f;
            }
            return fArr;
        }

        @Override // com.sun.media.sound.AudioFloatConverter
        public byte[] toByteArray(float[] fArr, int i2, int i3, byte[] bArr, int i4) {
            int i5 = i2;
            int i6 = i4;
            for (int i7 = 0; i7 < i3; i7++) {
                int i8 = i5;
                i5++;
                int i9 = Short.MAX_VALUE + ((int) (fArr[i8] * 32767.0d));
                int i10 = i6;
                int i11 = i6 + 1;
                bArr[i10] = (byte) (i9 >>> 8);
                i6 = i11 + 1;
                bArr[i11] = (byte) i9;
            }
            return bArr;
        }
    }

    /* loaded from: rt.jar:com/sun/media/sound/AudioFloatConverter$AudioFloatConversion24SL.class */
    private static class AudioFloatConversion24SL extends AudioFloatConverter {
        private AudioFloatConversion24SL() {
        }

        @Override // com.sun.media.sound.AudioFloatConverter
        public float[] toFloatArray(byte[] bArr, int i2, float[] fArr, int i3, int i4) {
            int i5 = i2;
            int i6 = i3;
            for (int i7 = 0; i7 < i4; i7++) {
                int i8 = i5;
                int i9 = i5 + 1;
                int i10 = i9 + 1;
                int i11 = (bArr[i8] & 255) | ((bArr[i9] & 255) << 8);
                i5 = i10 + 1;
                int i12 = i11 | ((bArr[i10] & 255) << 16);
                if (i12 > 8388607) {
                    i12 -= 16777216;
                }
                int i13 = i6;
                i6++;
                fArr[i13] = i12 * 1.192093E-7f;
            }
            return fArr;
        }

        @Override // com.sun.media.sound.AudioFloatConverter
        public byte[] toByteArray(float[] fArr, int i2, int i3, byte[] bArr, int i4) {
            int i5 = i2;
            int i6 = i4;
            for (int i7 = 0; i7 < i3; i7++) {
                int i8 = i5;
                i5++;
                int i9 = (int) (fArr[i8] * 8388607.0f);
                if (i9 < 0) {
                    i9 += 16777216;
                }
                int i10 = i6;
                int i11 = i6 + 1;
                bArr[i10] = (byte) i9;
                int i12 = i11 + 1;
                bArr[i11] = (byte) (i9 >>> 8);
                i6 = i12 + 1;
                bArr[i12] = (byte) (i9 >>> 16);
            }
            return bArr;
        }
    }

    /* loaded from: rt.jar:com/sun/media/sound/AudioFloatConverter$AudioFloatConversion24SB.class */
    private static class AudioFloatConversion24SB extends AudioFloatConverter {
        private AudioFloatConversion24SB() {
        }

        @Override // com.sun.media.sound.AudioFloatConverter
        public float[] toFloatArray(byte[] bArr, int i2, float[] fArr, int i3, int i4) {
            int i5 = i2;
            int i6 = i3;
            for (int i7 = 0; i7 < i4; i7++) {
                int i8 = i5;
                int i9 = i5 + 1;
                int i10 = i9 + 1;
                int i11 = ((bArr[i8] & 255) << 16) | ((bArr[i9] & 255) << 8);
                i5 = i10 + 1;
                int i12 = i11 | (bArr[i10] & 255);
                if (i12 > 8388607) {
                    i12 -= 16777216;
                }
                int i13 = i6;
                i6++;
                fArr[i13] = i12 * 1.192093E-7f;
            }
            return fArr;
        }

        @Override // com.sun.media.sound.AudioFloatConverter
        public byte[] toByteArray(float[] fArr, int i2, int i3, byte[] bArr, int i4) {
            int i5 = i2;
            int i6 = i4;
            for (int i7 = 0; i7 < i3; i7++) {
                int i8 = i5;
                i5++;
                int i9 = (int) (fArr[i8] * 8388607.0f);
                if (i9 < 0) {
                    i9 += 16777216;
                }
                int i10 = i6;
                int i11 = i6 + 1;
                bArr[i10] = (byte) (i9 >>> 16);
                int i12 = i11 + 1;
                bArr[i11] = (byte) (i9 >>> 8);
                i6 = i12 + 1;
                bArr[i12] = (byte) i9;
            }
            return bArr;
        }
    }

    /* loaded from: rt.jar:com/sun/media/sound/AudioFloatConverter$AudioFloatConversion24UL.class */
    private static class AudioFloatConversion24UL extends AudioFloatConverter {
        private AudioFloatConversion24UL() {
        }

        @Override // com.sun.media.sound.AudioFloatConverter
        public float[] toFloatArray(byte[] bArr, int i2, float[] fArr, int i3, int i4) {
            int i5 = i2;
            int i6 = i3;
            for (int i7 = 0; i7 < i4; i7++) {
                int i8 = i5;
                int i9 = i5 + 1;
                int i10 = (bArr[i8] & 255) | ((bArr[i9] & 255) << 8);
                i5 = i9 + 1 + 1;
                int i11 = i6;
                i6++;
                fArr[i11] = ((i10 | ((bArr[r11] & 255) << 16)) - FloatConsts.SIGNIF_BIT_MASK) * 1.192093E-7f;
            }
            return fArr;
        }

        @Override // com.sun.media.sound.AudioFloatConverter
        public byte[] toByteArray(float[] fArr, int i2, int i3, byte[] bArr, int i4) {
            int i5 = i2;
            int i6 = i4;
            for (int i7 = 0; i7 < i3; i7++) {
                int i8 = i5;
                i5++;
                int i9 = ((int) (fArr[i8] * 8388607.0f)) + FloatConsts.SIGNIF_BIT_MASK;
                int i10 = i6;
                int i11 = i6 + 1;
                bArr[i10] = (byte) i9;
                int i12 = i11 + 1;
                bArr[i11] = (byte) (i9 >>> 8);
                i6 = i12 + 1;
                bArr[i12] = (byte) (i9 >>> 16);
            }
            return bArr;
        }
    }

    /* loaded from: rt.jar:com/sun/media/sound/AudioFloatConverter$AudioFloatConversion24UB.class */
    private static class AudioFloatConversion24UB extends AudioFloatConverter {
        private AudioFloatConversion24UB() {
        }

        @Override // com.sun.media.sound.AudioFloatConverter
        public float[] toFloatArray(byte[] bArr, int i2, float[] fArr, int i3, int i4) {
            int i5 = i2;
            int i6 = i3;
            for (int i7 = 0; i7 < i4; i7++) {
                int i8 = i5;
                int i9 = i5 + 1;
                int i10 = ((bArr[i8] & 255) << 16) | ((bArr[i9] & 255) << 8);
                i5 = i9 + 1 + 1;
                int i11 = i6;
                i6++;
                fArr[i11] = ((i10 | (bArr[r11] & 255)) - FloatConsts.SIGNIF_BIT_MASK) * 1.192093E-7f;
            }
            return fArr;
        }

        @Override // com.sun.media.sound.AudioFloatConverter
        public byte[] toByteArray(float[] fArr, int i2, int i3, byte[] bArr, int i4) {
            int i5 = i2;
            int i6 = i4;
            for (int i7 = 0; i7 < i3; i7++) {
                int i8 = i5;
                i5++;
                int i9 = ((int) (fArr[i8] * 8388607.0f)) + FloatConsts.SIGNIF_BIT_MASK;
                int i10 = i6;
                int i11 = i6 + 1;
                bArr[i10] = (byte) (i9 >>> 16);
                int i12 = i11 + 1;
                bArr[i11] = (byte) (i9 >>> 8);
                i6 = i12 + 1;
                bArr[i12] = (byte) i9;
            }
            return bArr;
        }
    }

    /* loaded from: rt.jar:com/sun/media/sound/AudioFloatConverter$AudioFloatConversion32SL.class */
    private static class AudioFloatConversion32SL extends AudioFloatConverter {
        private AudioFloatConversion32SL() {
        }

        @Override // com.sun.media.sound.AudioFloatConverter
        public float[] toFloatArray(byte[] bArr, int i2, float[] fArr, int i3, int i4) {
            int i5 = i2;
            int i6 = i3;
            for (int i7 = 0; i7 < i4; i7++) {
                int i8 = i5;
                int i9 = i5 + 1;
                int i10 = i9 + 1;
                int i11 = (bArr[i8] & 255) | ((bArr[i9] & 255) << 8);
                int i12 = i11 | ((bArr[i10] & 255) << 16);
                i5 = i10 + 1 + 1;
                int i13 = i6;
                i6++;
                fArr[i13] = (i12 | ((bArr[r11] & 255) << 24)) * 4.656613E-10f;
            }
            return fArr;
        }

        @Override // com.sun.media.sound.AudioFloatConverter
        public byte[] toByteArray(float[] fArr, int i2, int i3, byte[] bArr, int i4) {
            int i5 = i2;
            int i6 = i4;
            for (int i7 = 0; i7 < i3; i7++) {
                int i8 = i5;
                i5++;
                int i9 = (int) (fArr[i8] * 2.1474836E9f);
                int i10 = i6;
                int i11 = i6 + 1;
                bArr[i10] = (byte) i9;
                int i12 = i11 + 1;
                bArr[i11] = (byte) (i9 >>> 8);
                int i13 = i12 + 1;
                bArr[i12] = (byte) (i9 >>> 16);
                i6 = i13 + 1;
                bArr[i13] = (byte) (i9 >>> 24);
            }
            return bArr;
        }
    }

    /* loaded from: rt.jar:com/sun/media/sound/AudioFloatConverter$AudioFloatConversion32SB.class */
    private static class AudioFloatConversion32SB extends AudioFloatConverter {
        private AudioFloatConversion32SB() {
        }

        @Override // com.sun.media.sound.AudioFloatConverter
        public float[] toFloatArray(byte[] bArr, int i2, float[] fArr, int i3, int i4) {
            int i5 = i2;
            int i6 = i3;
            for (int i7 = 0; i7 < i4; i7++) {
                int i8 = i5;
                int i9 = i5 + 1;
                int i10 = i9 + 1;
                int i11 = ((bArr[i8] & 255) << 24) | ((bArr[i9] & 255) << 16);
                int i12 = i11 | ((bArr[i10] & 255) << 8);
                i5 = i10 + 1 + 1;
                int i13 = i6;
                i6++;
                fArr[i13] = (i12 | (bArr[r11] & 255)) * 4.656613E-10f;
            }
            return fArr;
        }

        @Override // com.sun.media.sound.AudioFloatConverter
        public byte[] toByteArray(float[] fArr, int i2, int i3, byte[] bArr, int i4) {
            int i5 = i2;
            int i6 = i4;
            for (int i7 = 0; i7 < i3; i7++) {
                int i8 = i5;
                i5++;
                int i9 = (int) (fArr[i8] * 2.1474836E9f);
                int i10 = i6;
                int i11 = i6 + 1;
                bArr[i10] = (byte) (i9 >>> 24);
                int i12 = i11 + 1;
                bArr[i11] = (byte) (i9 >>> 16);
                int i13 = i12 + 1;
                bArr[i12] = (byte) (i9 >>> 8);
                i6 = i13 + 1;
                bArr[i13] = (byte) i9;
            }
            return bArr;
        }
    }

    /* loaded from: rt.jar:com/sun/media/sound/AudioFloatConverter$AudioFloatConversion32UL.class */
    private static class AudioFloatConversion32UL extends AudioFloatConverter {
        private AudioFloatConversion32UL() {
        }

        @Override // com.sun.media.sound.AudioFloatConverter
        public float[] toFloatArray(byte[] bArr, int i2, float[] fArr, int i3, int i4) {
            int i5 = i2;
            int i6 = i3;
            for (int i7 = 0; i7 < i4; i7++) {
                int i8 = i5;
                int i9 = i5 + 1;
                int i10 = i9 + 1;
                int i11 = (bArr[i8] & 255) | ((bArr[i9] & 255) << 8);
                int i12 = i11 | ((bArr[i10] & 255) << 16);
                i5 = i10 + 1 + 1;
                int i13 = i6;
                i6++;
                fArr[i13] = ((i12 | ((bArr[r11] & 255) << 24)) - Integer.MAX_VALUE) * 4.656613E-10f;
            }
            return fArr;
        }

        @Override // com.sun.media.sound.AudioFloatConverter
        public byte[] toByteArray(float[] fArr, int i2, int i3, byte[] bArr, int i4) {
            int i5 = i2;
            int i6 = i4;
            for (int i7 = 0; i7 < i3; i7++) {
                int i8 = i5;
                i5++;
                int i9 = ((int) (fArr[i8] * 2.1474836E9f)) + Integer.MAX_VALUE;
                int i10 = i6;
                int i11 = i6 + 1;
                bArr[i10] = (byte) i9;
                int i12 = i11 + 1;
                bArr[i11] = (byte) (i9 >>> 8);
                int i13 = i12 + 1;
                bArr[i12] = (byte) (i9 >>> 16);
                i6 = i13 + 1;
                bArr[i13] = (byte) (i9 >>> 24);
            }
            return bArr;
        }
    }

    /* loaded from: rt.jar:com/sun/media/sound/AudioFloatConverter$AudioFloatConversion32UB.class */
    private static class AudioFloatConversion32UB extends AudioFloatConverter {
        private AudioFloatConversion32UB() {
        }

        @Override // com.sun.media.sound.AudioFloatConverter
        public float[] toFloatArray(byte[] bArr, int i2, float[] fArr, int i3, int i4) {
            int i5 = i2;
            int i6 = i3;
            for (int i7 = 0; i7 < i4; i7++) {
                int i8 = i5;
                int i9 = i5 + 1;
                int i10 = i9 + 1;
                int i11 = ((bArr[i8] & 255) << 24) | ((bArr[i9] & 255) << 16);
                int i12 = i11 | ((bArr[i10] & 255) << 8);
                i5 = i10 + 1 + 1;
                int i13 = i6;
                i6++;
                fArr[i13] = ((i12 | (bArr[r11] & 255)) - Integer.MAX_VALUE) * 4.656613E-10f;
            }
            return fArr;
        }

        @Override // com.sun.media.sound.AudioFloatConverter
        public byte[] toByteArray(float[] fArr, int i2, int i3, byte[] bArr, int i4) {
            int i5 = i2;
            int i6 = i4;
            for (int i7 = 0; i7 < i3; i7++) {
                int i8 = i5;
                i5++;
                int i9 = ((int) (fArr[i8] * 2.1474836E9f)) + Integer.MAX_VALUE;
                int i10 = i6;
                int i11 = i6 + 1;
                bArr[i10] = (byte) (i9 >>> 24);
                int i12 = i11 + 1;
                bArr[i11] = (byte) (i9 >>> 16);
                int i13 = i12 + 1;
                bArr[i12] = (byte) (i9 >>> 8);
                i6 = i13 + 1;
                bArr[i13] = (byte) i9;
            }
            return bArr;
        }
    }

    /* loaded from: rt.jar:com/sun/media/sound/AudioFloatConverter$AudioFloatConversion32xSL.class */
    private static class AudioFloatConversion32xSL extends AudioFloatConverter {
        final int xbytes;

        AudioFloatConversion32xSL(int i2) {
            this.xbytes = i2;
        }

        @Override // com.sun.media.sound.AudioFloatConverter
        public float[] toFloatArray(byte[] bArr, int i2, float[] fArr, int i3, int i4) {
            int i5 = i2;
            int i6 = i3;
            for (int i7 = 0; i7 < i4; i7++) {
                int i8 = i5 + this.xbytes;
                int i9 = i8 + 1;
                int i10 = i9 + 1;
                int i11 = (bArr[i8] & 255) | ((bArr[i9] & 255) << 8);
                int i12 = i11 | ((bArr[i10] & 255) << 16);
                i5 = i10 + 1 + 1;
                int i13 = i6;
                i6++;
                fArr[i13] = (i12 | ((bArr[r11] & 255) << 24)) * 4.656613E-10f;
            }
            return fArr;
        }

        @Override // com.sun.media.sound.AudioFloatConverter
        public byte[] toByteArray(float[] fArr, int i2, int i3, byte[] bArr, int i4) {
            int i5 = i2;
            int i6 = i4;
            for (int i7 = 0; i7 < i3; i7++) {
                int i8 = i5;
                i5++;
                int i9 = (int) (fArr[i8] * 2.1474836E9f);
                for (int i10 = 0; i10 < this.xbytes; i10++) {
                    int i11 = i6;
                    i6++;
                    bArr[i11] = 0;
                }
                int i12 = i6;
                int i13 = i6 + 1;
                bArr[i12] = (byte) i9;
                int i14 = i13 + 1;
                bArr[i13] = (byte) (i9 >>> 8);
                int i15 = i14 + 1;
                bArr[i14] = (byte) (i9 >>> 16);
                i6 = i15 + 1;
                bArr[i15] = (byte) (i9 >>> 24);
            }
            return bArr;
        }
    }

    /* loaded from: rt.jar:com/sun/media/sound/AudioFloatConverter$AudioFloatConversion32xSB.class */
    private static class AudioFloatConversion32xSB extends AudioFloatConverter {
        final int xbytes;

        AudioFloatConversion32xSB(int i2) {
            this.xbytes = i2;
        }

        @Override // com.sun.media.sound.AudioFloatConverter
        public float[] toFloatArray(byte[] bArr, int i2, float[] fArr, int i3, int i4) {
            int i5 = i2;
            int i6 = i3;
            for (int i7 = 0; i7 < i4; i7++) {
                int i8 = i5;
                int i9 = i5 + 1;
                int i10 = i9 + 1;
                int i11 = ((bArr[i8] & 255) << 24) | ((bArr[i9] & 255) << 16);
                int i12 = i10 + 1;
                int i13 = i11 | ((bArr[i10] & 255) << 8) | (bArr[i12] & 255);
                i5 = i12 + 1 + this.xbytes;
                int i14 = i6;
                i6++;
                fArr[i14] = i13 * 4.656613E-10f;
            }
            return fArr;
        }

        @Override // com.sun.media.sound.AudioFloatConverter
        public byte[] toByteArray(float[] fArr, int i2, int i3, byte[] bArr, int i4) {
            int i5 = i2;
            int i6 = i4;
            for (int i7 = 0; i7 < i3; i7++) {
                int i8 = i5;
                i5++;
                int i9 = (int) (fArr[i8] * 2.1474836E9f);
                int i10 = i6;
                int i11 = i6 + 1;
                bArr[i10] = (byte) (i9 >>> 24);
                int i12 = i11 + 1;
                bArr[i11] = (byte) (i9 >>> 16);
                int i13 = i12 + 1;
                bArr[i12] = (byte) (i9 >>> 8);
                i6 = i13 + 1;
                bArr[i13] = (byte) i9;
                for (int i14 = 0; i14 < this.xbytes; i14++) {
                    int i15 = i6;
                    i6++;
                    bArr[i15] = 0;
                }
            }
            return bArr;
        }
    }

    /* loaded from: rt.jar:com/sun/media/sound/AudioFloatConverter$AudioFloatConversion32xUL.class */
    private static class AudioFloatConversion32xUL extends AudioFloatConverter {
        final int xbytes;

        AudioFloatConversion32xUL(int i2) {
            this.xbytes = i2;
        }

        @Override // com.sun.media.sound.AudioFloatConverter
        public float[] toFloatArray(byte[] bArr, int i2, float[] fArr, int i3, int i4) {
            int i5 = i2;
            int i6 = i3;
            for (int i7 = 0; i7 < i4; i7++) {
                int i8 = i5 + this.xbytes;
                int i9 = i8 + 1;
                int i10 = i9 + 1;
                int i11 = (bArr[i8] & 255) | ((bArr[i9] & 255) << 8);
                int i12 = i11 | ((bArr[i10] & 255) << 16);
                i5 = i10 + 1 + 1;
                int i13 = i6;
                i6++;
                fArr[i13] = ((i12 | ((bArr[r11] & 255) << 24)) - Integer.MAX_VALUE) * 4.656613E-10f;
            }
            return fArr;
        }

        @Override // com.sun.media.sound.AudioFloatConverter
        public byte[] toByteArray(float[] fArr, int i2, int i3, byte[] bArr, int i4) {
            int i5 = i2;
            int i6 = i4;
            for (int i7 = 0; i7 < i3; i7++) {
                int i8 = i5;
                i5++;
                int i9 = ((int) (fArr[i8] * 2.1474836E9f)) + Integer.MAX_VALUE;
                for (int i10 = 0; i10 < this.xbytes; i10++) {
                    int i11 = i6;
                    i6++;
                    bArr[i11] = 0;
                }
                int i12 = i6;
                int i13 = i6 + 1;
                bArr[i12] = (byte) i9;
                int i14 = i13 + 1;
                bArr[i13] = (byte) (i9 >>> 8);
                int i15 = i14 + 1;
                bArr[i14] = (byte) (i9 >>> 16);
                i6 = i15 + 1;
                bArr[i15] = (byte) (i9 >>> 24);
            }
            return bArr;
        }
    }

    /* loaded from: rt.jar:com/sun/media/sound/AudioFloatConverter$AudioFloatConversion32xUB.class */
    private static class AudioFloatConversion32xUB extends AudioFloatConverter {
        final int xbytes;

        AudioFloatConversion32xUB(int i2) {
            this.xbytes = i2;
        }

        @Override // com.sun.media.sound.AudioFloatConverter
        public float[] toFloatArray(byte[] bArr, int i2, float[] fArr, int i3, int i4) {
            int i5 = i2;
            int i6 = i3;
            for (int i7 = 0; i7 < i4; i7++) {
                int i8 = i5;
                int i9 = i5 + 1;
                int i10 = i9 + 1;
                int i11 = ((bArr[i8] & 255) << 24) | ((bArr[i9] & 255) << 16);
                int i12 = i10 + 1;
                int i13 = i11 | ((bArr[i10] & 255) << 8) | (bArr[i12] & 255);
                i5 = i12 + 1 + this.xbytes;
                int i14 = i6;
                i6++;
                fArr[i14] = (i13 - Integer.MAX_VALUE) * 4.656613E-10f;
            }
            return fArr;
        }

        @Override // com.sun.media.sound.AudioFloatConverter
        public byte[] toByteArray(float[] fArr, int i2, int i3, byte[] bArr, int i4) {
            int i5 = i2;
            int i6 = i4;
            for (int i7 = 0; i7 < i3; i7++) {
                int i8 = i5;
                i5++;
                int i9 = ((int) (fArr[i8] * 2.147483647E9d)) + Integer.MAX_VALUE;
                int i10 = i6;
                int i11 = i6 + 1;
                bArr[i10] = (byte) (i9 >>> 24);
                int i12 = i11 + 1;
                bArr[i11] = (byte) (i9 >>> 16);
                int i13 = i12 + 1;
                bArr[i12] = (byte) (i9 >>> 8);
                i6 = i13 + 1;
                bArr[i13] = (byte) i9;
                for (int i14 = 0; i14 < this.xbytes; i14++) {
                    int i15 = i6;
                    i6++;
                    bArr[i15] = 0;
                }
            }
            return bArr;
        }
    }

    public static AudioFloatConverter getConverter(AudioFormat audioFormat) {
        AudioFloatConverter audioFloatConversion64B = null;
        if (audioFormat.getFrameSize() == 0 || audioFormat.getFrameSize() != ((audioFormat.getSampleSizeInBits() + 7) / 8) * audioFormat.getChannels()) {
            return null;
        }
        if (audioFormat.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED)) {
            if (audioFormat.isBigEndian()) {
                if (audioFormat.getSampleSizeInBits() <= 8) {
                    audioFloatConversion64B = new AudioFloatConversion8S();
                } else if (audioFormat.getSampleSizeInBits() > 8 && audioFormat.getSampleSizeInBits() <= 16) {
                    audioFloatConversion64B = new AudioFloatConversion16SB();
                } else if (audioFormat.getSampleSizeInBits() > 16 && audioFormat.getSampleSizeInBits() <= 24) {
                    audioFloatConversion64B = new AudioFloatConversion24SB();
                } else if (audioFormat.getSampleSizeInBits() > 24 && audioFormat.getSampleSizeInBits() <= 32) {
                    audioFloatConversion64B = new AudioFloatConversion32SB();
                } else if (audioFormat.getSampleSizeInBits() > 32) {
                    audioFloatConversion64B = new AudioFloatConversion32xSB(((audioFormat.getSampleSizeInBits() + 7) / 8) - 4);
                }
            } else if (audioFormat.getSampleSizeInBits() <= 8) {
                audioFloatConversion64B = new AudioFloatConversion8S();
            } else if (audioFormat.getSampleSizeInBits() > 8 && audioFormat.getSampleSizeInBits() <= 16) {
                audioFloatConversion64B = new AudioFloatConversion16SL();
            } else if (audioFormat.getSampleSizeInBits() > 16 && audioFormat.getSampleSizeInBits() <= 24) {
                audioFloatConversion64B = new AudioFloatConversion24SL();
            } else if (audioFormat.getSampleSizeInBits() > 24 && audioFormat.getSampleSizeInBits() <= 32) {
                audioFloatConversion64B = new AudioFloatConversion32SL();
            } else if (audioFormat.getSampleSizeInBits() > 32) {
                audioFloatConversion64B = new AudioFloatConversion32xSL(((audioFormat.getSampleSizeInBits() + 7) / 8) - 4);
            }
        } else if (audioFormat.getEncoding().equals(AudioFormat.Encoding.PCM_UNSIGNED)) {
            if (audioFormat.isBigEndian()) {
                if (audioFormat.getSampleSizeInBits() <= 8) {
                    audioFloatConversion64B = new AudioFloatConversion8U();
                } else if (audioFormat.getSampleSizeInBits() > 8 && audioFormat.getSampleSizeInBits() <= 16) {
                    audioFloatConversion64B = new AudioFloatConversion16UB();
                } else if (audioFormat.getSampleSizeInBits() > 16 && audioFormat.getSampleSizeInBits() <= 24) {
                    audioFloatConversion64B = new AudioFloatConversion24UB();
                } else if (audioFormat.getSampleSizeInBits() > 24 && audioFormat.getSampleSizeInBits() <= 32) {
                    audioFloatConversion64B = new AudioFloatConversion32UB();
                } else if (audioFormat.getSampleSizeInBits() > 32) {
                    audioFloatConversion64B = new AudioFloatConversion32xUB(((audioFormat.getSampleSizeInBits() + 7) / 8) - 4);
                }
            } else if (audioFormat.getSampleSizeInBits() <= 8) {
                audioFloatConversion64B = new AudioFloatConversion8U();
            } else if (audioFormat.getSampleSizeInBits() > 8 && audioFormat.getSampleSizeInBits() <= 16) {
                audioFloatConversion64B = new AudioFloatConversion16UL();
            } else if (audioFormat.getSampleSizeInBits() > 16 && audioFormat.getSampleSizeInBits() <= 24) {
                audioFloatConversion64B = new AudioFloatConversion24UL();
            } else if (audioFormat.getSampleSizeInBits() > 24 && audioFormat.getSampleSizeInBits() <= 32) {
                audioFloatConversion64B = new AudioFloatConversion32UL();
            } else if (audioFormat.getSampleSizeInBits() > 32) {
                audioFloatConversion64B = new AudioFloatConversion32xUL(((audioFormat.getSampleSizeInBits() + 7) / 8) - 4);
            }
        } else if (audioFormat.getEncoding().equals(AudioFormat.Encoding.PCM_FLOAT)) {
            if (audioFormat.getSampleSizeInBits() == 32) {
                audioFloatConversion64B = audioFormat.isBigEndian() ? new AudioFloatConversion32B() : new AudioFloatConversion32L();
            } else if (audioFormat.getSampleSizeInBits() == 64) {
                audioFloatConversion64B = audioFormat.isBigEndian() ? new AudioFloatConversion64B() : new AudioFloatConversion64L();
            }
        }
        if ((audioFormat.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED) || audioFormat.getEncoding().equals(AudioFormat.Encoding.PCM_UNSIGNED)) && audioFormat.getSampleSizeInBits() % 8 != 0) {
            audioFloatConversion64B = new AudioFloatLSBFilter(audioFloatConversion64B, audioFormat);
        }
        if (audioFloatConversion64B != null) {
            audioFloatConversion64B.format = audioFormat;
        }
        return audioFloatConversion64B;
    }

    public final AudioFormat getFormat() {
        return this.format;
    }

    public final float[] toFloatArray(byte[] bArr, float[] fArr, int i2, int i3) {
        return toFloatArray(bArr, 0, fArr, i2, i3);
    }

    public final float[] toFloatArray(byte[] bArr, int i2, float[] fArr, int i3) {
        return toFloatArray(bArr, i2, fArr, 0, i3);
    }

    public final float[] toFloatArray(byte[] bArr, float[] fArr, int i2) {
        return toFloatArray(bArr, 0, fArr, 0, i2);
    }

    public final float[] toFloatArray(byte[] bArr, float[] fArr) {
        return toFloatArray(bArr, 0, fArr, 0, fArr.length);
    }

    public final byte[] toByteArray(float[] fArr, int i2, byte[] bArr, int i3) {
        return toByteArray(fArr, 0, i2, bArr, i3);
    }

    public final byte[] toByteArray(float[] fArr, int i2, int i3, byte[] bArr) {
        return toByteArray(fArr, i2, i3, bArr, 0);
    }

    public final byte[] toByteArray(float[] fArr, int i2, byte[] bArr) {
        return toByteArray(fArr, 0, i2, bArr, 0);
    }

    public final byte[] toByteArray(float[] fArr, byte[] bArr) {
        return toByteArray(fArr, 0, fArr.length, bArr, 0);
    }
}
