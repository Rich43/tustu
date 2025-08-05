package sun.nio.ch;

import java.awt.Event;
import java.io.IOException;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.Pipe;
import java.nio.channels.SelectableChannel;
import java.nio.channels.Selector;
import java.nio.channels.spi.SelectorProvider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import sun.misc.Unsafe;

/*  JADX ERROR: NullPointerException in pass: ClassModifier
    java.lang.NullPointerException: Cannot invoke "java.util.List.forEach(java.util.function.Consumer)" because "blocks" is null
    	at jadx.core.utils.BlockUtils.collectAllInsns(BlockUtils.java:1029)
    	at jadx.core.dex.visitors.ClassModifier.removeBridgeMethod(ClassModifier.java:245)
    	at jadx.core.dex.visitors.ClassModifier.removeSyntheticMethods(ClassModifier.java:160)
    	at java.base/java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.ClassModifier.visit(ClassModifier.java:65)
    	at jadx.core.dex.visitors.ClassModifier.visit(ClassModifier.java:58)
    */
/* loaded from: rt.jar:sun/nio/ch/WindowsSelectorImpl.class */
final class WindowsSelectorImpl extends SelectorImpl {
    private static final Unsafe unsafe;
    private static int addressSize;
    private final int INIT_CAP = 8;
    private static final int MAX_SELECTABLE_FDS = 1024;
    private static final long SIZEOF_FD_SET;
    private SelectionKeyImpl[] channelArray;
    private PollArrayWrapper pollWrapper;
    private int totalChannels;
    private int threadsCount;
    private final List<SelectThread> threads;
    private final Pipe wakeupPipe;
    private final int wakeupSourceFd;
    private final int wakeupSinkFd;
    private Object closeLock;
    private final FdMap fdMap;
    private final SubSelector subSelector;
    private long timeout;
    private final Object interruptLock;
    private volatile boolean interruptTriggered;
    private final StartLock startLock;
    private final FinishLock finishLock;
    private long updateCount;
    static final /* synthetic */ boolean $assertionsDisabled;

    private native void setWakeupSocket0(int i2);

    private native void resetWakeupSocket0(int i2);

    /* JADX INFO: Access modifiers changed from: private */
    public native boolean discardUrgentData(int i2);

    static {
        $assertionsDisabled = !WindowsSelectorImpl.class.desiredAssertionStatus();
        unsafe = Unsafe.getUnsafe();
        addressSize = unsafe.addressSize();
        SIZEOF_FD_SET = dependsArch(4100, 8200);
        IOUtil.load();
    }

    private static int dependsArch(int i2, int i3) {
        return addressSize == 4 ? i2 : i3;
    }

    /* loaded from: rt.jar:sun/nio/ch/WindowsSelectorImpl$FdMap.class */
    private static final class FdMap extends HashMap<Integer, MapEntry> {
        static final long serialVersionUID = 0;

        private FdMap() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public MapEntry get(int i2) {
            return get(new Integer(i2));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public MapEntry put(SelectionKeyImpl selectionKeyImpl) {
            return put(new Integer(selectionKeyImpl.channel.getFDVal()), new MapEntry(selectionKeyImpl));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public MapEntry remove(SelectionKeyImpl selectionKeyImpl) {
            Integer num = new Integer(selectionKeyImpl.channel.getFDVal());
            MapEntry mapEntry = get(num);
            if (mapEntry != null && mapEntry.ski.channel == selectionKeyImpl.channel) {
                return remove(num);
            }
            return null;
        }
    }

    /* loaded from: rt.jar:sun/nio/ch/WindowsSelectorImpl$MapEntry.class */
    private static final class MapEntry {
        SelectionKeyImpl ski;
        long updateCount = 0;
        long clearedCount = 0;

        MapEntry(SelectionKeyImpl selectionKeyImpl) {
            this.ski = selectionKeyImpl;
        }
    }

    WindowsSelectorImpl(SelectorProvider selectorProvider) throws IOException {
        super(selectorProvider);
        this.INIT_CAP = 8;
        this.channelArray = new SelectionKeyImpl[8];
        this.totalChannels = 1;
        this.threadsCount = 0;
        this.threads = new ArrayList();
        this.closeLock = new Object();
        this.fdMap = new FdMap();
        this.subSelector = new SubSelector(this, (AnonymousClass1) null);
        this.interruptLock = new Object();
        this.interruptTriggered = false;
        this.startLock = new StartLock(this, null);
        this.finishLock = new FinishLock();
        this.updateCount = 0L;
        this.pollWrapper = new PollArrayWrapper(8);
        this.wakeupPipe = Pipe.open();
        this.wakeupSourceFd = ((SelChImpl) this.wakeupPipe.source()).getFDVal();
        SinkChannelImpl sinkChannelImpl = (SinkChannelImpl) this.wakeupPipe.sink();
        sinkChannelImpl.sc.socket().setTcpNoDelay(true);
        this.wakeupSinkFd = sinkChannelImpl.getFDVal();
        this.pollWrapper.addWakeupSocket(this.wakeupSourceFd, 0);
    }

    @Override // sun.nio.ch.SelectorImpl
    protected int doSelect(long j2) throws IOException {
        if (this.channelArray == null) {
            throw new ClosedSelectorException();
        }
        this.timeout = j2;
        processDeregisterQueue();
        if (this.interruptTriggered) {
            resetWakeupSocket();
            return 0;
        }
        adjustThreadsCount();
        this.finishLock.reset();
        this.startLock.startThreads();
        try {
            begin();
            try {
                this.subSelector.poll();
            } catch (IOException e2) {
                this.finishLock.setException(e2);
            }
            if (this.threads.size() > 0) {
                this.finishLock.waitForHelperThreads();
            }
            this.finishLock.checkForException();
            processDeregisterQueue();
            int iUpdateSelectedKeys = updateSelectedKeys();
            resetWakeupSocket();
            return iUpdateSelectedKeys;
        } finally {
            end();
        }
    }

    /* loaded from: rt.jar:sun/nio/ch/WindowsSelectorImpl$StartLock.class */
    private final class StartLock {
        private long runsCounter;
        final /* synthetic */ WindowsSelectorImpl this$0;

        private StartLock(WindowsSelectorImpl windowsSelectorImpl) {
            this.this$0 = windowsSelectorImpl;
        }

        /* synthetic */ StartLock(WindowsSelectorImpl windowsSelectorImpl, AnonymousClass1 anonymousClass1) {
            this(windowsSelectorImpl);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void startThreads() {
            this.runsCounter++;
            notifyAll();
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Failed to check method for inline after forced processsun.nio.ch.WindowsSelectorImpl.SelectThread.access$902(sun.nio.ch.WindowsSelectorImpl$SelectThread, long):long */
        public synchronized boolean waitForStart(SelectThread selectThread) {
            while (this.runsCounter == selectThread.lastRun) {
                try {
                    this.this$0.startLock.wait();
                } catch (InterruptedException e2) {
                    Thread.currentThread().interrupt();
                }
            }
            if (selectThread.isZombie()) {
                return true;
            }
            SelectThread.access$902(selectThread, this.runsCounter);
            return false;
        }
    }

    /* loaded from: rt.jar:sun/nio/ch/WindowsSelectorImpl$FinishLock.class */
    private final class FinishLock {
        private int threadsToFinish;
        IOException exception;

        private FinishLock() {
            this.exception = null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void reset() {
            this.threadsToFinish = WindowsSelectorImpl.this.threads.size();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void threadFinished() {
            if (this.threadsToFinish == WindowsSelectorImpl.this.threads.size()) {
                WindowsSelectorImpl.this.wakeup();
            }
            this.threadsToFinish--;
            if (this.threadsToFinish == 0) {
                notify();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void waitForHelperThreads() {
            if (this.threadsToFinish == WindowsSelectorImpl.this.threads.size()) {
                WindowsSelectorImpl.this.wakeup();
            }
            while (this.threadsToFinish != 0) {
                try {
                    WindowsSelectorImpl.this.finishLock.wait();
                } catch (InterruptedException e2) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void setException(IOException iOException) {
            this.exception = iOException;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void checkForException() throws IOException {
            if (this.exception == null) {
                return;
            }
            StringBuffer stringBuffer = new StringBuffer("An exception occurred during the execution of select(): \n");
            stringBuffer.append((Object) this.exception);
            stringBuffer.append('\n');
            this.exception = null;
            throw new IOException(stringBuffer.toString());
        }
    }

    /* loaded from: rt.jar:sun/nio/ch/WindowsSelectorImpl$SubSelector.class */
    private final class SubSelector {
        private final int pollArrayIndex;
        private final int[] readFds;
        private final int[] writeFds;
        private final int[] exceptFds;
        private final long fdsBuffer;
        final /* synthetic */ WindowsSelectorImpl this$0;

        private native int poll0(long j2, int i2, int[] iArr, int[] iArr2, int[] iArr3, long j3, long j4);

        /* synthetic */ SubSelector(WindowsSelectorImpl windowsSelectorImpl, AnonymousClass1 anonymousClass1) {
            this(windowsSelectorImpl);
        }

        /* synthetic */ SubSelector(WindowsSelectorImpl windowsSelectorImpl, int i2, AnonymousClass1 anonymousClass1) {
            this(windowsSelectorImpl, i2);
        }

        private SubSelector(WindowsSelectorImpl windowsSelectorImpl) {
            this.this$0 = windowsSelectorImpl;
            this.readFds = new int[Event.INSERT];
            this.writeFds = new int[Event.INSERT];
            this.exceptFds = new int[Event.INSERT];
            this.fdsBuffer = WindowsSelectorImpl.unsafe.allocateMemory(WindowsSelectorImpl.SIZEOF_FD_SET * 6);
            this.pollArrayIndex = 0;
        }

        private SubSelector(WindowsSelectorImpl windowsSelectorImpl, int i2) {
            this.this$0 = windowsSelectorImpl;
            this.readFds = new int[Event.INSERT];
            this.writeFds = new int[Event.INSERT];
            this.exceptFds = new int[Event.INSERT];
            this.fdsBuffer = WindowsSelectorImpl.unsafe.allocateMemory(WindowsSelectorImpl.SIZEOF_FD_SET * 6);
            this.pollArrayIndex = (i2 + 1) * 1024;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int poll() throws IOException {
            return poll0(this.this$0.pollWrapper.pollArrayAddress, Math.min(this.this$0.totalChannels, 1024), this.readFds, this.writeFds, this.exceptFds, this.this$0.timeout, this.fdsBuffer);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int poll(int i2) throws IOException {
            return poll0(this.this$0.pollWrapper.pollArrayAddress + (this.pollArrayIndex * PollArrayWrapper.SIZE_POLLFD), Math.min(1024, this.this$0.totalChannels - ((i2 + 1) * 1024)), this.readFds, this.writeFds, this.exceptFds, this.this$0.timeout, this.fdsBuffer);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int processSelectedKeys(long j2) {
            return 0 + processFDSet(j2, this.readFds, Net.POLLIN, false) + processFDSet(j2, this.writeFds, Net.POLLCONN | Net.POLLOUT, false) + processFDSet(j2, this.exceptFds, Net.POLLIN | Net.POLLCONN | Net.POLLOUT, true);
        }

        private int processFDSet(long j2, int[] iArr, int i2, boolean z2) {
            int i3 = 0;
            for (int i4 = 1; i4 <= iArr[0]; i4++) {
                int i5 = iArr[i4];
                if (i5 == this.this$0.wakeupSourceFd) {
                    synchronized (this.this$0.interruptLock) {
                        this.this$0.interruptTriggered = true;
                    }
                } else {
                    MapEntry mapEntry = this.this$0.fdMap.get(i5);
                    if (mapEntry != null) {
                        SelectionKeyImpl selectionKeyImpl = mapEntry.ski;
                        if (!z2 || !(selectionKeyImpl.channel() instanceof SocketChannelImpl) || !this.this$0.discardUrgentData(i5)) {
                            if (this.this$0.selectedKeys.contains(selectionKeyImpl)) {
                                if (mapEntry.clearedCount != j2) {
                                    if (selectionKeyImpl.channel.translateAndSetReadyOps(i2, selectionKeyImpl) && mapEntry.updateCount != j2) {
                                        mapEntry.updateCount = j2;
                                        i3++;
                                    }
                                } else if (selectionKeyImpl.channel.translateAndUpdateReadyOps(i2, selectionKeyImpl) && mapEntry.updateCount != j2) {
                                    mapEntry.updateCount = j2;
                                    i3++;
                                }
                                mapEntry.clearedCount = j2;
                            } else {
                                if (mapEntry.clearedCount != j2) {
                                    selectionKeyImpl.channel.translateAndSetReadyOps(i2, selectionKeyImpl);
                                    if ((selectionKeyImpl.nioReadyOps() & selectionKeyImpl.nioInterestOps()) != 0) {
                                        this.this$0.selectedKeys.add(selectionKeyImpl);
                                        mapEntry.updateCount = j2;
                                        i3++;
                                    }
                                } else {
                                    selectionKeyImpl.channel.translateAndUpdateReadyOps(i2, selectionKeyImpl);
                                    if ((selectionKeyImpl.nioReadyOps() & selectionKeyImpl.nioInterestOps()) != 0) {
                                        this.this$0.selectedKeys.add(selectionKeyImpl);
                                        mapEntry.updateCount = j2;
                                        i3++;
                                    }
                                }
                                mapEntry.clearedCount = j2;
                            }
                        }
                    }
                }
            }
            return i3;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void freeFDSetBuffer() {
            WindowsSelectorImpl.unsafe.freeMemory(this.fdsBuffer);
        }
    }

    /* loaded from: rt.jar:sun/nio/ch/WindowsSelectorImpl$SelectThread.class */
    private final class SelectThread extends Thread {
        private final int index;
        final SubSelector subSelector;
        private long lastRun;
        private volatile boolean zombie;

        /*  JADX ERROR: Failed to decode insn: 0x0002: MOVE_MULTI
            java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[6]
            	at java.base/java.lang.System.arraycopy(Native Method)
            	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
            	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
            	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
            	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
            	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
            	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
            	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:464)
            	at jadx.core.ProcessClass.process(ProcessClass.java:69)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
            	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
            	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
            */
        static /* synthetic */ long access$902(sun.nio.ch.WindowsSelectorImpl.SelectThread r6, long r7) {
            /*
                r0 = r6
                r1 = r7
                // decode failed: arraycopy: source index -1 out of bounds for object array[6]
                r0.lastRun = r1
                return r-1
            */
            throw new UnsupportedOperationException("Method not decompiled: sun.nio.ch.WindowsSelectorImpl.SelectThread.access$902(sun.nio.ch.WindowsSelectorImpl$SelectThread, long):long");
        }

        /* synthetic */ SelectThread(WindowsSelectorImpl windowsSelectorImpl, int i2, AnonymousClass1 anonymousClass1) {
            this(i2);
        }

        private SelectThread(int i2) {
            this.lastRun = 0L;
            this.index = i2;
            this.subSelector = new SubSelector(WindowsSelectorImpl.this, i2, null);
            this.lastRun = WindowsSelectorImpl.this.startLock.runsCounter;
        }

        void makeZombie() {
            this.zombie = true;
        }

        boolean isZombie() {
            return this.zombie;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            while (!WindowsSelectorImpl.this.startLock.waitForStart(this)) {
                try {
                    this.subSelector.poll(this.index);
                } catch (IOException e2) {
                    WindowsSelectorImpl.this.finishLock.setException(e2);
                }
                WindowsSelectorImpl.this.finishLock.threadFinished();
            }
            this.subSelector.freeFDSetBuffer();
        }
    }

    private void adjustThreadsCount() {
        if (this.threadsCount > this.threads.size()) {
            for (int size = this.threads.size(); size < this.threadsCount; size++) {
                SelectThread selectThread = new SelectThread(this, size, null);
                this.threads.add(selectThread);
                selectThread.setDaemon(true);
                selectThread.start();
            }
            return;
        }
        if (this.threadsCount < this.threads.size()) {
            for (int size2 = this.threads.size() - 1; size2 >= this.threadsCount; size2--) {
                this.threads.remove(size2).makeZombie();
            }
        }
    }

    private void setWakeupSocket() {
        setWakeupSocket0(this.wakeupSinkFd);
    }

    private void resetWakeupSocket() {
        synchronized (this.interruptLock) {
            if (this.interruptTriggered) {
                resetWakeupSocket0(this.wakeupSourceFd);
                this.interruptTriggered = false;
            }
        }
    }

    private int updateSelectedKeys() {
        this.updateCount++;
        int iProcessSelectedKeys = 0 + this.subSelector.processSelectedKeys(this.updateCount);
        Iterator<SelectThread> it = this.threads.iterator();
        while (it.hasNext()) {
            iProcessSelectedKeys += it.next().subSelector.processSelectedKeys(this.updateCount);
        }
        return iProcessSelectedKeys;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // sun.nio.ch.SelectorImpl
    protected void implClose() throws IOException {
        synchronized (this.closeLock) {
            if (this.channelArray != null && this.pollWrapper != null) {
                synchronized (this.interruptLock) {
                    this.interruptTriggered = true;
                }
                this.wakeupPipe.sink().close();
                this.wakeupPipe.source().close();
                for (int i2 = 1; i2 < this.totalChannels; i2++) {
                    if (i2 % 1024 != 0) {
                        deregister(this.channelArray[i2]);
                        SelectableChannel selectableChannelChannel = this.channelArray[i2].channel();
                        if (!selectableChannelChannel.isOpen() && !selectableChannelChannel.isRegistered()) {
                            ((SelChImpl) selectableChannelChannel).kill();
                        }
                    }
                }
                this.pollWrapper.free();
                this.pollWrapper = null;
                this.selectedKeys = null;
                this.channelArray = null;
                Iterator<SelectThread> it = this.threads.iterator();
                while (it.hasNext()) {
                    it.next().makeZombie();
                }
                this.startLock.startThreads();
                this.subSelector.freeFDSetBuffer();
            }
        }
    }

    @Override // sun.nio.ch.SelectorImpl
    protected void implRegister(SelectionKeyImpl selectionKeyImpl) {
        synchronized (this.closeLock) {
            if (this.pollWrapper == null) {
                throw new ClosedSelectorException();
            }
            growIfNeeded();
            this.channelArray[this.totalChannels] = selectionKeyImpl;
            selectionKeyImpl.setIndex(this.totalChannels);
            this.fdMap.put(selectionKeyImpl);
            this.keys.add(selectionKeyImpl);
            this.pollWrapper.addEntry(this.totalChannels, selectionKeyImpl);
            this.totalChannels++;
        }
    }

    private void growIfNeeded() {
        if (this.channelArray.length == this.totalChannels) {
            int i2 = this.totalChannels * 2;
            SelectionKeyImpl[] selectionKeyImplArr = new SelectionKeyImpl[i2];
            System.arraycopy(this.channelArray, 1, selectionKeyImplArr, 1, this.totalChannels - 1);
            this.channelArray = selectionKeyImplArr;
            this.pollWrapper.grow(i2);
        }
        if (this.totalChannels % 1024 == 0) {
            this.pollWrapper.addWakeupSocket(this.wakeupSourceFd, this.totalChannels);
            this.totalChannels++;
            this.threadsCount++;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // sun.nio.ch.SelectorImpl
    protected void implDereg(SelectionKeyImpl selectionKeyImpl) throws IOException {
        int index = selectionKeyImpl.getIndex();
        if (!$assertionsDisabled && index < 0) {
            throw new AssertionError();
        }
        synchronized (this.closeLock) {
            if (index != this.totalChannels - 1) {
                SelectionKeyImpl selectionKeyImpl2 = this.channelArray[this.totalChannels - 1];
                this.channelArray[index] = selectionKeyImpl2;
                selectionKeyImpl2.setIndex(index);
                this.pollWrapper.replaceEntry(this.pollWrapper, this.totalChannels - 1, this.pollWrapper, index);
            }
            selectionKeyImpl.setIndex(-1);
        }
        this.channelArray[this.totalChannels - 1] = null;
        this.totalChannels--;
        if (this.totalChannels != 1 && this.totalChannels % 1024 == 1) {
            this.totalChannels--;
            this.threadsCount--;
        }
        this.fdMap.remove(selectionKeyImpl);
        this.keys.remove(selectionKeyImpl);
        this.selectedKeys.remove(selectionKeyImpl);
        deregister(selectionKeyImpl);
        SelectableChannel selectableChannelChannel = selectionKeyImpl.channel();
        if (!selectableChannelChannel.isOpen() && !selectableChannelChannel.isRegistered()) {
            ((SelChImpl) selectableChannelChannel).kill();
        }
    }

    @Override // sun.nio.ch.SelectorImpl
    public void putEventOps(SelectionKeyImpl selectionKeyImpl, int i2) {
        synchronized (this.closeLock) {
            if (this.pollWrapper == null) {
                throw new ClosedSelectorException();
            }
            int index = selectionKeyImpl.getIndex();
            if (index == -1) {
                throw new CancelledKeyException();
            }
            this.pollWrapper.putEventOps(index, i2);
        }
    }

    @Override // sun.nio.ch.SelectorImpl, java.nio.channels.Selector
    public Selector wakeup() {
        synchronized (this.interruptLock) {
            if (!this.interruptTriggered) {
                setWakeupSocket();
                this.interruptTriggered = true;
            }
        }
        return this;
    }
}
