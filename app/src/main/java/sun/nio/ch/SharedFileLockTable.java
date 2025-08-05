package sun.nio.ch;

import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.nio.channels.Channel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/* compiled from: FileLockTable.java */
/* loaded from: rt.jar:sun/nio/ch/SharedFileLockTable.class */
class SharedFileLockTable extends FileLockTable {
    private static ConcurrentHashMap<FileKey, List<FileLockReference>> lockMap;
    private static ReferenceQueue<FileLock> queue;
    private final Channel channel;
    private final FileKey fileKey;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !SharedFileLockTable.class.desiredAssertionStatus();
        lockMap = new ConcurrentHashMap<>();
        queue = new ReferenceQueue<>();
    }

    /* compiled from: FileLockTable.java */
    /* loaded from: rt.jar:sun/nio/ch/SharedFileLockTable$FileLockReference.class */
    private static class FileLockReference extends WeakReference<FileLock> {
        private FileKey fileKey;

        FileLockReference(FileLock fileLock, ReferenceQueue<FileLock> referenceQueue, FileKey fileKey) {
            super(fileLock, referenceQueue);
            this.fileKey = fileKey;
        }

        FileKey fileKey() {
            return this.fileKey;
        }
    }

    SharedFileLockTable(Channel channel, FileDescriptor fileDescriptor) throws IOException {
        this.channel = channel;
        this.fileKey = FileKey.create(fileDescriptor);
    }

    /* JADX WARN: Code restructure failed: missing block: B:25:0x0078, code lost:
    
        checkList(r9, r8.position(), r8.size());
        r9.add(new sun.nio.ch.SharedFileLockTable.FileLockReference(r8, sun.nio.ch.SharedFileLockTable.queue, r7.fileKey));
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x0033, code lost:
    
        r0.add(new sun.nio.ch.SharedFileLockTable.FileLockReference(r8, sun.nio.ch.SharedFileLockTable.queue, r7.fileKey));
     */
    /* JADX WARN: Removed duplicated region for block: B:41:0x0063 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    @Override // sun.nio.ch.FileLockTable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void add(java.nio.channels.FileLock r8) throws java.nio.channels.OverlappingFileLockException {
        /*
            r7 = this;
            java.util.concurrent.ConcurrentHashMap<sun.nio.ch.FileKey, java.util.List<sun.nio.ch.SharedFileLockTable$FileLockReference>> r0 = sun.nio.ch.SharedFileLockTable.lockMap
            r1 = r7
            sun.nio.ch.FileKey r1 = r1.fileKey
            java.lang.Object r0 = r0.get(r1)
            java.util.List r0 = (java.util.List) r0
            r9 = r0
        Le:
            r0 = r9
            if (r0 != 0) goto L5f
            java.util.ArrayList r0 = new java.util.ArrayList
            r1 = r0
            r2 = 2
            r1.<init>(r2)
            r9 = r0
            r0 = r9
            r1 = r0
            r11 = r1
            monitor-enter(r0)
            java.util.concurrent.ConcurrentHashMap<sun.nio.ch.FileKey, java.util.List<sun.nio.ch.SharedFileLockTable$FileLockReference>> r0 = sun.nio.ch.SharedFileLockTable.lockMap     // Catch: java.lang.Throwable -> L55
            r1 = r7
            sun.nio.ch.FileKey r1 = r1.fileKey     // Catch: java.lang.Throwable -> L55
            r2 = r9
            java.lang.Object r0 = r0.putIfAbsent(r1, r2)     // Catch: java.lang.Throwable -> L55
            java.util.List r0 = (java.util.List) r0     // Catch: java.lang.Throwable -> L55
            r10 = r0
            r0 = r10
            if (r0 != 0) goto L4f
            r0 = r9
            sun.nio.ch.SharedFileLockTable$FileLockReference r1 = new sun.nio.ch.SharedFileLockTable$FileLockReference     // Catch: java.lang.Throwable -> L55
            r2 = r1
            r3 = r8
            java.lang.ref.ReferenceQueue<java.nio.channels.FileLock> r4 = sun.nio.ch.SharedFileLockTable.queue     // Catch: java.lang.Throwable -> L55
            r5 = r7
            sun.nio.ch.FileKey r5 = r5.fileKey     // Catch: java.lang.Throwable -> L55
            r2.<init>(r3, r4, r5)     // Catch: java.lang.Throwable -> L55
            boolean r0 = r0.add(r1)     // Catch: java.lang.Throwable -> L55
            r0 = r11
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L55
            goto Lb2
        L4f:
            r0 = r11
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L55
            goto L5d
        L55:
            r12 = move-exception
            r0 = r11
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L55
            r0 = r12
            throw r0
        L5d:
            r0 = r10
            r9 = r0
        L5f:
            r0 = r9
            r1 = r0
            r10 = r1
            monitor-enter(r0)
            java.util.concurrent.ConcurrentHashMap<sun.nio.ch.FileKey, java.util.List<sun.nio.ch.SharedFileLockTable$FileLockReference>> r0 = sun.nio.ch.SharedFileLockTable.lockMap     // Catch: java.lang.Throwable -> La8
            r1 = r7
            sun.nio.ch.FileKey r1 = r1.fileKey     // Catch: java.lang.Throwable -> La8
            java.lang.Object r0 = r0.get(r1)     // Catch: java.lang.Throwable -> La8
            java.util.List r0 = (java.util.List) r0     // Catch: java.lang.Throwable -> La8
            r11 = r0
            r0 = r9
            r1 = r11
            if (r0 != r1) goto La0
            r0 = r7
            r1 = r9
            r2 = r8
            long r2 = r2.position()     // Catch: java.lang.Throwable -> La8
            r3 = r8
            long r3 = r3.size()     // Catch: java.lang.Throwable -> La8
            r0.checkList(r1, r2, r3)     // Catch: java.lang.Throwable -> La8
            r0 = r9
            sun.nio.ch.SharedFileLockTable$FileLockReference r1 = new sun.nio.ch.SharedFileLockTable$FileLockReference     // Catch: java.lang.Throwable -> La8
            r2 = r1
            r3 = r8
            java.lang.ref.ReferenceQueue<java.nio.channels.FileLock> r4 = sun.nio.ch.SharedFileLockTable.queue     // Catch: java.lang.Throwable -> La8
            r5 = r7
            sun.nio.ch.FileKey r5 = r5.fileKey     // Catch: java.lang.Throwable -> La8
            r2.<init>(r3, r4, r5)     // Catch: java.lang.Throwable -> La8
            boolean r0 = r0.add(r1)     // Catch: java.lang.Throwable -> La8
            r0 = r10
            monitor-exit(r0)     // Catch: java.lang.Throwable -> La8
            goto Lb2
        La0:
            r0 = r11
            r9 = r0
            r0 = r10
            monitor-exit(r0)     // Catch: java.lang.Throwable -> La8
            goto Laf
        La8:
            r13 = move-exception
            r0 = r10
            monitor-exit(r0)     // Catch: java.lang.Throwable -> La8
            r0 = r13
            throw r0
        Laf:
            goto Le
        Lb2:
            r0 = r7
            r0.removeStaleEntries()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.ch.SharedFileLockTable.add(java.nio.channels.FileLock):void");
    }

    private void removeKeyIfEmpty(FileKey fileKey, List<FileLockReference> list) {
        if (!$assertionsDisabled && !Thread.holdsLock(list)) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && lockMap.get(fileKey) != list) {
            throw new AssertionError();
        }
        if (list.isEmpty()) {
            lockMap.remove(fileKey);
        }
    }

    @Override // sun.nio.ch.FileLockTable
    public void remove(FileLock fileLock) {
        if (!$assertionsDisabled && fileLock == null) {
            throw new AssertionError();
        }
        List<FileLockReference> list = lockMap.get(this.fileKey);
        if (list == null) {
            return;
        }
        synchronized (list) {
            int i2 = 0;
            while (true) {
                if (i2 >= list.size()) {
                    break;
                }
                FileLockReference fileLockReference = list.get(i2);
                FileLock fileLock2 = fileLockReference.get();
                if (fileLock2 == fileLock) {
                    if (!$assertionsDisabled && (fileLock2 == null || fileLock2.acquiredBy() != this.channel)) {
                        throw new AssertionError();
                    }
                    fileLockReference.clear();
                    list.remove(i2);
                } else {
                    i2++;
                }
            }
        }
    }

    @Override // sun.nio.ch.FileLockTable
    public List<FileLock> removeAll() {
        ArrayList arrayList = new ArrayList();
        List<FileLockReference> list = lockMap.get(this.fileKey);
        if (list != null) {
            synchronized (list) {
                int i2 = 0;
                while (i2 < list.size()) {
                    FileLockReference fileLockReference = list.get(i2);
                    FileLock fileLock = fileLockReference.get();
                    if (fileLock != null && fileLock.acquiredBy() == this.channel) {
                        fileLockReference.clear();
                        list.remove(i2);
                        arrayList.add(fileLock);
                    } else {
                        i2++;
                    }
                }
                removeKeyIfEmpty(this.fileKey, list);
            }
        }
        return arrayList;
    }

    @Override // sun.nio.ch.FileLockTable
    public void replace(FileLock fileLock, FileLock fileLock2) {
        List<FileLockReference> list = lockMap.get(this.fileKey);
        if (!$assertionsDisabled && list == null) {
            throw new AssertionError();
        }
        synchronized (list) {
            int i2 = 0;
            while (true) {
                if (i2 >= list.size()) {
                    break;
                }
                FileLockReference fileLockReference = list.get(i2);
                if (fileLockReference.get() != fileLock) {
                    i2++;
                } else {
                    fileLockReference.clear();
                    list.set(i2, new FileLockReference(fileLock2, queue, this.fileKey));
                    break;
                }
            }
        }
    }

    private void checkList(List<FileLockReference> list, long j2, long j3) throws OverlappingFileLockException {
        if (!$assertionsDisabled && !Thread.holdsLock(list)) {
            throw new AssertionError();
        }
        Iterator<FileLockReference> it = list.iterator();
        while (it.hasNext()) {
            FileLock fileLock = it.next().get();
            if (fileLock != null && fileLock.overlaps(j2, j3)) {
                throw new OverlappingFileLockException();
            }
        }
    }

    private void removeStaleEntries() {
        while (true) {
            FileLockReference fileLockReference = (FileLockReference) queue.poll();
            if (fileLockReference != null) {
                FileKey fileKey = fileLockReference.fileKey();
                List<FileLockReference> list = lockMap.get(fileKey);
                if (list != null) {
                    synchronized (list) {
                        list.remove(fileLockReference);
                        removeKeyIfEmpty(fileKey, list);
                    }
                }
            } else {
                return;
            }
        }
    }
}
