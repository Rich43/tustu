package com.sun.media.sound;

import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.AudioInputStream;

/* loaded from: rt.jar:com/sun/media/sound/SoftJitterCorrector.class */
public final class SoftJitterCorrector extends AudioInputStream {

    /* loaded from: rt.jar:com/sun/media/sound/SoftJitterCorrector$JitterStream.class */
    private static class JitterStream extends InputStream {
        static int MAX_BUFFER_SIZE = 1048576;
        Thread thread;
        AudioInputStream stream;
        byte[][] buffers;
        int w_count;
        int bbuffer_max;
        boolean active = true;
        int writepos = 0;
        int readpos = 0;
        private final Object buffers_mutex = new Object();
        int w_min_tol = 2;
        int w_max_tol = 10;

        /* renamed from: w, reason: collision with root package name */
        int f11983w = 0;
        int w_min = -1;
        int bbuffer_pos = 0;
        byte[] bbuffer = null;

        public byte[] nextReadBuffer() {
            synchronized (this.buffers_mutex) {
                if (this.writepos > this.readpos) {
                    int i2 = this.writepos - this.readpos;
                    if (i2 < this.w_min) {
                        this.w_min = i2;
                    }
                    int i3 = this.readpos;
                    this.readpos++;
                    return this.buffers[i3 % this.buffers.length];
                }
                this.w_min = -1;
                this.f11983w = this.w_count - 1;
                while (true) {
                    try {
                        Thread.sleep(1L);
                        synchronized (this.buffers_mutex) {
                            if (this.writepos > this.readpos) {
                                this.f11983w = 0;
                                this.w_min = -1;
                                this.f11983w = this.w_count - 1;
                                int i4 = this.readpos;
                                this.readpos++;
                                return this.buffers[i4 % this.buffers.length];
                            }
                        }
                    } catch (InterruptedException e2) {
                        return null;
                    }
                }
            }
        }

        public byte[] nextWriteBuffer() {
            byte[] bArr;
            synchronized (this.buffers_mutex) {
                bArr = this.buffers[this.writepos % this.buffers.length];
            }
            return bArr;
        }

        public void commit() {
            synchronized (this.buffers_mutex) {
                this.writepos++;
                if (this.writepos - this.readpos > this.buffers.length) {
                    this.buffers = new byte[Math.max(this.buffers.length * 2, (this.writepos - this.readpos) + 10)][this.buffers[0].length];
                }
            }
        }

        JitterStream(AudioInputStream audioInputStream, int i2, int i3) {
            this.w_count = 1000;
            this.bbuffer_max = 0;
            this.w_count = 10 * (i2 / i3);
            if (this.w_count < 100) {
                this.w_count = 100;
            }
            this.buffers = new byte[(i2 / i3) + 10][i3];
            this.bbuffer_max = MAX_BUFFER_SIZE / i3;
            this.stream = audioInputStream;
            this.thread = new Thread(new Runnable() { // from class: com.sun.media.sound.SoftJitterCorrector.JitterStream.1
                /* JADX WARN: Code restructure failed: missing block: B:68:0x0182, code lost:
                
                    java.lang.Thread.sleep(1);
                 */
                @Override // java.lang.Runnable
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                    To view partially-correct code enable 'Show inconsistent code' option in preferences
                */
                public void run() throws java.io.EOFException {
                    /*
                        Method dump skipped, instructions count: 553
                        To view this dump change 'Code comments level' option to 'DEBUG'
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.sun.media.sound.SoftJitterCorrector.JitterStream.AnonymousClass1.run():void");
                }
            });
            this.thread.setDaemon(true);
            this.thread.setPriority(10);
            this.thread.start();
        }

        @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            synchronized (this) {
                this.active = false;
            }
            try {
                this.thread.join();
            } catch (InterruptedException e2) {
            }
            this.stream.close();
        }

        @Override // java.io.InputStream
        public int read() throws IOException {
            byte[] bArr = new byte[1];
            if (read(bArr) == -1) {
                return -1;
            }
            return bArr[0] & 255;
        }

        public void fillBuffer() {
            this.bbuffer = nextReadBuffer();
            this.bbuffer_pos = 0;
        }

        @Override // java.io.InputStream
        public int read(byte[] bArr, int i2, int i3) {
            if (this.bbuffer == null) {
                fillBuffer();
            }
            int length = this.bbuffer.length;
            int i4 = i2 + i3;
            while (i2 < i4) {
                if (available() == 0) {
                    fillBuffer();
                } else {
                    byte[] bArr2 = this.bbuffer;
                    int i5 = this.bbuffer_pos;
                    while (i2 < i4 && i5 < length) {
                        int i6 = i2;
                        i2++;
                        int i7 = i5;
                        i5++;
                        bArr[i6] = bArr2[i7];
                    }
                    this.bbuffer_pos = i5;
                }
            }
            return i3;
        }

        @Override // java.io.InputStream
        public int available() {
            return this.bbuffer.length - this.bbuffer_pos;
        }
    }

    public SoftJitterCorrector(AudioInputStream audioInputStream, int i2, int i3) {
        super(new JitterStream(audioInputStream, i2, i3), audioInputStream.getFormat(), audioInputStream.getFrameLength());
    }
}
