package jdk.jfr.internal;

import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;
import java.util.function.Supplier;
import jdk.jfr.Event;

/*  JADX ERROR: NullPointerException in pass: ClassModifier
    java.lang.NullPointerException: Cannot invoke "java.util.List.forEach(java.util.function.Consumer)" because "blocks" is null
    	at jadx.core.utils.BlockUtils.collectAllInsns(BlockUtils.java:1029)
    	at jadx.core.dex.visitors.ClassModifier.removeBridgeMethod(ClassModifier.java:245)
    	at jadx.core.dex.visitors.ClassModifier.removeSyntheticMethods(ClassModifier.java:160)
    	at java.base/java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.ClassModifier.visit(ClassModifier.java:65)
    	at jadx.core.dex.visitors.ClassModifier.visit(ClassModifier.java:58)
    */
/* loaded from: jfr.jar:jdk/jfr/internal/RequestEngine.class */
public final class RequestEngine {
    private static final JVM jvm = JVM.getJVM();
    private static final List<RequestHook> entries = new CopyOnWriteArrayList();
    private static long lastTimeMillis;

    public RequestEngine() {
    }

    static {
    }

    /* loaded from: jfr.jar:jdk/jfr/internal/RequestEngine$RequestHook.class */
    static final class RequestHook {
        private final Runnable hook;
        private final PlatformEventType type;
        private final AccessControlContext accessControllerContext;
        private long delta;

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
        static /* synthetic */ long access$502(jdk.jfr.internal.RequestEngine.RequestHook r6, long r7) {
            /*
                r0 = r6
                r1 = r7
                // decode failed: arraycopy: source index -1 out of bounds for object array[6]
                r0.delta = r1
                return r-1
            */
            throw new UnsupportedOperationException("Method not decompiled: jdk.jfr.internal.RequestEngine.RequestHook.access$502(jdk.jfr.internal.RequestEngine$RequestHook, long):long");
        }

        private RequestHook(AccessControlContext accessControlContext, PlatformEventType platformEventType, Runnable runnable) {
            this.hook = runnable;
            this.type = platformEventType;
            this.accessControllerContext = accessControlContext;
        }

        RequestHook(PlatformEventType platformEventType) {
            this(null, platformEventType, null);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void execute() {
            try {
                if (this.accessControllerContext == null) {
                    if (!this.type.isJDK()) {
                        RequestEngine.jvm.emitEvent(this.type.getId(), JVM.counterTime(), 0L);
                    } else {
                        this.hook.run();
                    }
                    if (Logger.shouldLog(LogTag.JFR_EVENT, LogLevel.DEBUG)) {
                        Logger.log(LogTag.JFR_SYSTEM_EVENT, LogLevel.DEBUG, (Supplier<String>) () -> {
                            return "Executed periodic hook for " + this.type.getLogName();
                        });
                    }
                } else {
                    executeSecure();
                }
            } catch (Throwable th) {
                Logger.log(LogTag.JFR_SYSTEM_EVENT, LogLevel.WARN, "Exception occured during execution of period hook for " + this.type.getLogName());
            }
        }

        private void executeSecure() {
            AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: jdk.jfr.internal.RequestEngine.RequestHook.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Void run2() {
                    try {
                        RequestHook.this.hook.run();
                        if (Logger.shouldLog(LogTag.JFR_EVENT, LogLevel.DEBUG)) {
                            Logger.log(LogTag.JFR_EVENT, LogLevel.DEBUG, (Supplier<String>) () -> {
                                return "Executed periodic hook for " + RequestHook.this.type.getLogName();
                            });
                        }
                        return null;
                    } catch (Throwable th) {
                        Logger.log(LogTag.JFR_EVENT, LogLevel.WARN, "Exception occured during execution of period hook for " + RequestHook.this.type.getLogName());
                        return null;
                    }
                }
            }, this.accessControllerContext);
        }
    }

    public static void addHook(AccessControlContext accessControlContext, PlatformEventType platformEventType, Runnable runnable) {
        Objects.requireNonNull(accessControlContext);
        addHookInternal(accessControlContext, platformEventType, runnable);
    }

    private static void addHookInternal(AccessControlContext accessControlContext, PlatformEventType platformEventType, Runnable runnable) {
        RequestHook requestHook = new RequestHook(accessControlContext, platformEventType, runnable);
        Iterator<RequestHook> it = entries.iterator();
        while (it.hasNext()) {
            if (it.next().hook == runnable) {
                throw new IllegalArgumentException("Hook has already been added");
            }
        }
        requestHook.type.setEventHook(true);
        entries.add(requestHook);
        logHook("Added", platformEventType);
    }

    public static void addTrustedJDKHook(Class<? extends Event> cls, Runnable runnable) {
        if (cls.getClassLoader() != null) {
            throw new SecurityException("Hook can only be registered for event classes that are loaded by the bootstrap class loader");
        }
        if (runnable.getClass().getClassLoader() != null) {
            throw new SecurityException("Runnable hook class must be loaded by the bootstrap class loader");
        }
        addHookInternal(null, PrivateAccess.getInstance().getPlatformEventType(MetadataRepository.getInstance().getEventType(cls)), runnable);
    }

    private static void logHook(String str, PlatformEventType platformEventType) {
        if (platformEventType.isJDK() || platformEventType.isJVM()) {
            Logger.log(LogTag.JFR_SYSTEM_EVENT, LogLevel.INFO, str + " periodic hook for " + platformEventType.getLogName());
        } else {
            Logger.log(LogTag.JFR_EVENT, LogLevel.INFO, str + " periodic hook for " + platformEventType.getLogName());
        }
    }

    public static boolean removeHook(Runnable runnable) {
        for (RequestHook requestHook : entries) {
            if (requestHook.hook == runnable) {
                entries.remove(requestHook);
                requestHook.type.setEventHook(false);
                logHook("Removed", requestHook.type);
                return true;
            }
        }
        return false;
    }

    static void addHooks(List<RequestHook> list) {
        ArrayList arrayList = new ArrayList();
        for (RequestHook requestHook : list) {
            requestHook.type.setEventHook(true);
            arrayList.add(requestHook);
            logHook("Added", requestHook.type);
        }
        entries.addAll(list);
    }

    static void doChunkEnd() {
        doChunk(platformEventType -> {
            return platformEventType.isEndChunk();
        });
    }

    static void doChunkBegin() {
        doChunk(platformEventType -> {
            return platformEventType.isBeginChunk();
        });
    }

    private static void doChunk(Predicate<PlatformEventType> predicate) {
        for (RequestHook requestHook : entries) {
            PlatformEventType platformEventType = requestHook.type;
            if (platformEventType.isEnabled() && predicate.test(platformEventType)) {
                requestHook.execute();
            }
        }
    }

    static long doPeriodic() {
        return run_requests(entries);
    }

    /* JADX WARN: Failed to check method for inline after forced processjdk.jfr.internal.RequestEngine.RequestHook.access$502(jdk.jfr.internal.RequestEngine$RequestHook, long):long */
    private static long run_requests(Collection<RequestHook> collection) {
        long j2 = lastTimeMillis;
        long jCurrentTimeMillis = System.currentTimeMillis();
        long j3 = 0;
        if (j2 == 0) {
            j2 = jCurrentTimeMillis;
        }
        long j4 = jCurrentTimeMillis - j2;
        if (j4 < 0) {
            lastTimeMillis = jCurrentTimeMillis;
            return 0L;
        }
        for (RequestHook requestHook : collection) {
            PlatformEventType platformEventType = requestHook.type;
            if (platformEventType.isEnabled() && !platformEventType.isEveryChunk()) {
                long period = platformEventType.getPeriod();
                long j5 = requestHook.delta + j4;
                if (j5 >= period) {
                    j5 = 0;
                    requestHook.execute();
                }
                long j6 = period - j5;
                if (j6 < 0) {
                    j6 = 0;
                }
                RequestHook.access$502(requestHook, j5);
                if (j3 == 0 || j6 < j3) {
                    j3 = j6;
                }
            }
        }
        lastTimeMillis = jCurrentTimeMillis;
        return j3;
    }
}
