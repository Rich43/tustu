package com.sun.webkit.network;

import java.nio.ByteBuffer;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

/* loaded from: jfxrt.jar:com/sun/webkit/network/ByteBufferPool.class */
final class ByteBufferPool {
    private final Queue<ByteBuffer> byteBuffers = new ConcurrentLinkedQueue();
    private final int bufferSize;

    private ByteBufferPool(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    static ByteBufferPool newInstance(int bufferSize) {
        return new ByteBufferPool(bufferSize);
    }

    ByteBufferAllocator newAllocator(int maxBufferCount) {
        return new ByteBufferAllocatorImpl(maxBufferCount);
    }

    /* loaded from: jfxrt.jar:com/sun/webkit/network/ByteBufferPool$ByteBufferAllocatorImpl.class */
    private final class ByteBufferAllocatorImpl implements ByteBufferAllocator {
        private final Semaphore semaphore;

        private ByteBufferAllocatorImpl(int maxBufferCount) {
            this.semaphore = new Semaphore(maxBufferCount);
        }

        @Override // com.sun.webkit.network.ByteBufferAllocator
        public ByteBuffer allocate() throws InterruptedException {
            this.semaphore.acquire();
            ByteBuffer byteBuffer = (ByteBuffer) ByteBufferPool.this.byteBuffers.poll();
            if (byteBuffer == null) {
                byteBuffer = ByteBuffer.allocateDirect(ByteBufferPool.this.bufferSize);
            }
            return byteBuffer;
        }

        @Override // com.sun.webkit.network.ByteBufferAllocator
        public void release(ByteBuffer byteBuffer) {
            ByteBufferPool.this.byteBuffers.add(byteBuffer);
            this.semaphore.release();
        }
    }
}
