package sun.rmi.transport;

import java.rmi.server.UID;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import sun.rmi.runtime.RuntimeUtil;
import sun.security.action.GetLongAction;

/* loaded from: rt.jar:sun/rmi/transport/DGCAckHandler.class */
public class DGCAckHandler {
    private static final long dgcAckTimeout;
    private static final ScheduledExecutorService scheduler;
    private static final Map<UID, DGCAckHandler> idTable;
    private final UID id;
    private List<Object> objList = new ArrayList();
    private Future<?> task = null;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !DGCAckHandler.class.desiredAssertionStatus();
        dgcAckTimeout = ((Long) AccessController.doPrivileged(new GetLongAction("sun.rmi.dgc.ackTimeout", 300000L))).longValue();
        scheduler = ((RuntimeUtil) AccessController.doPrivileged(new RuntimeUtil.GetInstanceAction())).getScheduler();
        idTable = Collections.synchronizedMap(new HashMap());
    }

    DGCAckHandler(UID uid) {
        this.id = uid;
        if (uid != null) {
            if (!$assertionsDisabled && idTable.containsKey(uid)) {
                throw new AssertionError();
            }
            idTable.put(uid, this);
        }
    }

    synchronized void add(Object obj) {
        if (this.objList != null) {
            this.objList.add(obj);
        }
    }

    synchronized void startTimer() {
        if (this.objList != null && this.task == null) {
            this.task = scheduler.schedule(new Runnable() { // from class: sun.rmi.transport.DGCAckHandler.1
                @Override // java.lang.Runnable
                public void run() {
                    if (DGCAckHandler.this.id != null) {
                        DGCAckHandler.idTable.remove(DGCAckHandler.this.id);
                    }
                    DGCAckHandler.this.release();
                }
            }, dgcAckTimeout, TimeUnit.MILLISECONDS);
        }
    }

    synchronized void release() {
        if (this.task != null) {
            this.task.cancel(false);
            this.task = null;
        }
        this.objList = null;
    }

    public static void received(UID uid) {
        DGCAckHandler dGCAckHandlerRemove = idTable.remove(uid);
        if (dGCAckHandlerRemove != null) {
            dGCAckHandlerRemove.release();
        }
    }
}
