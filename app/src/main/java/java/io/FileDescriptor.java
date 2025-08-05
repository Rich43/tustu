package java.io;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import sun.misc.JavaIOFileDescriptorAccess;
import sun.misc.SharedSecrets;

/*  JADX ERROR: NullPointerException in pass: ClassModifier
    java.lang.NullPointerException: Cannot invoke "java.util.List.forEach(java.util.function.Consumer)" because "blocks" is null
    	at jadx.core.utils.BlockUtils.collectAllInsns(BlockUtils.java:1029)
    	at jadx.core.dex.visitors.ClassModifier.removeBridgeMethod(ClassModifier.java:245)
    	at jadx.core.dex.visitors.ClassModifier.removeSyntheticMethods(ClassModifier.java:160)
    	at java.base/java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.ClassModifier.visit(ClassModifier.java:65)
    */
/* loaded from: rt.jar:java/io/FileDescriptor.class */
public final class FileDescriptor {
    private int fd = -1;
    private long handle = -1;
    private Closeable parent;
    private List<Closeable> otherParents;
    private boolean closed;
    public static final FileDescriptor in;
    public static final FileDescriptor out;
    public static final FileDescriptor err;

    public native void sync() throws SyncFailedException;

    private static native void initIDs();

    private static native long set(int i2);

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
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:117)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    static /* synthetic */ long access$102(java.io.FileDescriptor r6, long r7) {
        /*
            r0 = r6
            r1 = r7
            // decode failed: arraycopy: source index -1 out of bounds for object array[6]
            r0.handle = r1
            return r-1
        */
        throw new UnsupportedOperationException("Method not decompiled: java.io.FileDescriptor.access$102(java.io.FileDescriptor, long):long");
    }

    public FileDescriptor() {
    }

    static {
        initIDs();
        SharedSecrets.setJavaIOFileDescriptorAccess(new JavaIOFileDescriptorAccess() { // from class: java.io.FileDescriptor.1
            @Override // sun.misc.JavaIOFileDescriptorAccess
            public void set(FileDescriptor fileDescriptor, int i2) {
                fileDescriptor.fd = i2;
            }

            @Override // sun.misc.JavaIOFileDescriptorAccess
            public int get(FileDescriptor fileDescriptor) {
                return fileDescriptor.fd;
            }

            /* JADX WARN: Failed to check method for inline after forced processjava.io.FileDescriptor.access$102(java.io.FileDescriptor, long):long */
            @Override // sun.misc.JavaIOFileDescriptorAccess
            public void setHandle(FileDescriptor fileDescriptor, long j2) {
                FileDescriptor.access$102(fileDescriptor, j2);
            }

            @Override // sun.misc.JavaIOFileDescriptorAccess
            public long getHandle(FileDescriptor fileDescriptor) {
                return fileDescriptor.handle;
            }
        });
        in = standardStream(0);
        out = standardStream(1);
        err = standardStream(2);
    }

    public boolean valid() {
        return (this.handle == -1 && this.fd == -1) ? false : true;
    }

    private static FileDescriptor standardStream(int i2) {
        FileDescriptor fileDescriptor = new FileDescriptor();
        fileDescriptor.handle = set(i2);
        return fileDescriptor;
    }

    synchronized void attach(Closeable closeable) {
        if (this.parent == null) {
            this.parent = closeable;
        } else {
            if (this.otherParents == null) {
                this.otherParents = new ArrayList();
                this.otherParents.add(this.parent);
                this.otherParents.add(closeable);
                return;
            }
            this.otherParents.add(closeable);
        }
    }

    synchronized void closeAll(Closeable closeable) throws IOException {
        if (!this.closed) {
            this.closed = true;
            IOException iOException = null;
            try {
                Throwable th = null;
                try {
                    try {
                        try {
                            if (this.otherParents != null) {
                                Iterator<Closeable> it = this.otherParents.iterator();
                                while (it.hasNext()) {
                                    try {
                                        it.next().close();
                                    } catch (IOException e2) {
                                        if (iOException == null) {
                                            iOException = e2;
                                        } else {
                                            iOException.addSuppressed(e2);
                                        }
                                    }
                                }
                            }
                            if (closeable != null) {
                                if (0 != 0) {
                                    try {
                                        closeable.close();
                                    } catch (Throwable th2) {
                                        th.addSuppressed(th2);
                                    }
                                } else {
                                    closeable.close();
                                }
                            }
                            if (iOException != null) {
                                throw iOException;
                            }
                        } catch (Throwable th3) {
                            if (closeable != null) {
                                if (th != null) {
                                    try {
                                        closeable.close();
                                    } catch (Throwable th4) {
                                        th.addSuppressed(th4);
                                    }
                                } else {
                                    closeable.close();
                                }
                            }
                            throw th3;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        throw th5;
                    }
                } catch (IOException e3) {
                    if (0 != 0) {
                        e3.addSuppressed(null);
                    }
                    if (e3 != null) {
                        throw e3;
                    }
                }
            } catch (Throwable th6) {
                if (0 != 0) {
                    throw null;
                }
                throw th6;
            }
        }
    }
}
