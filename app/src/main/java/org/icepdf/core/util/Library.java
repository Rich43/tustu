package org.icepdf.core.util;

import java.awt.geom.Rectangle2D;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import org.icepdf.core.pobjects.Catalog;
import org.icepdf.core.pobjects.Dictionary;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.pobjects.PObject;
import org.icepdf.core.pobjects.PRectangle;
import org.icepdf.core.pobjects.PTrailer;
import org.icepdf.core.pobjects.Reference;
import org.icepdf.core.pobjects.Resources;
import org.icepdf.core.pobjects.StateManager;
import org.icepdf.core.pobjects.Stream;
import org.icepdf.core.pobjects.StringObject;
import org.icepdf.core.pobjects.fonts.Font;
import org.icepdf.core.pobjects.fonts.FontDescriptor;
import org.icepdf.core.pobjects.graphics.ICCBased;
import org.icepdf.core.pobjects.graphics.ImagePool;
import org.icepdf.core.pobjects.security.SecurityManager;

/* loaded from: icepdf-core.jar:org/icepdf/core/util/Library.class */
public class Library {
    private static final Logger log = Logger.getLogger(Library.class.toString());
    protected static ThreadPoolExecutor commonThreadPool;
    protected static ThreadPoolExecutor painterThreadPool;
    protected static ThreadPoolExecutor imageThreadPool;
    protected static int commonPoolThreads;
    protected static int painterPoolThreads;
    protected static int imagePoolThreads;
    private static final long KEEP_ALIVE_TIME = 10;
    private LazyObjectLoader lazyObjectLoader;
    private Catalog catalog;
    public SecurityManager securityManager;
    public StateManager stateManager;
    private boolean isEncrypted;
    private boolean isLinearTraversal;
    private ConcurrentHashMap<Reference, WeakReference<Object>> refs = new ConcurrentHashMap<>(1024);
    private ConcurrentHashMap<Reference, WeakReference<ICCBased>> lookupReference2ICCBased = new ConcurrentHashMap<>(256);
    private ImagePool imagePool = new ImagePool();

    static {
        try {
            commonPoolThreads = Defs.intProperty("org.icepdf.core.library.threadPoolSize", 5);
            if (commonPoolThreads < 1) {
                commonPoolThreads = 5;
            }
        } catch (NumberFormatException e2) {
            log.warning("Error reading buffered scale factor");
        }
        try {
            painterPoolThreads = Defs.intProperty("org.icepdf.core.library.painterThreadPoolSize", 2);
            if (painterPoolThreads < 1) {
                painterPoolThreads = 2;
            }
        } catch (NumberFormatException e3) {
            log.warning("Error reading buffered scale factor");
        }
        try {
            imagePoolThreads = Defs.intProperty("org.icepdf.core.library.imageThreadPoolSize", 10);
            if (imagePoolThreads < 1) {
                imagePoolThreads = 10;
            }
        } catch (NumberFormatException e4) {
            log.warning("Error reading buffered scale factor");
        }
        log.fine("Starting ICEpdf Thread Pools: " + (commonPoolThreads + painterPoolThreads + imagePoolThreads) + " threads.");
        initializeThreadPool();
    }

    public void setLazyObjectLoader(LazyObjectLoader lol) {
        this.lazyObjectLoader = lol;
    }

    public PTrailer getTrailerByFilePosition(long position) {
        if (this.lazyObjectLoader == null) {
            return null;
        }
        return this.lazyObjectLoader.loadTrailer(position);
    }

    public Object getObject(Reference reference) {
        while (true) {
            WeakReference<Object> obRef = this.refs.get(reference);
            Object ob = obRef != null ? obRef.get() : null;
            if (ob == null && this.stateManager != null && this.stateManager.contains(reference)) {
                return this.stateManager.getChange(reference);
            }
            if (ob == null && this.lazyObjectLoader != null) {
                ob = this.lazyObjectLoader.loadObject(reference);
            }
            if (ob instanceof PObject) {
                return ((PObject) ob).getObject();
            }
            if (ob instanceof Reference) {
                reference = (Reference) ob;
            } else {
                return ob;
            }
        }
    }

    private void printObjectDebug(Object ob) {
        if (ob == null) {
            log.finer("null object found");
            return;
        }
        if (ob instanceof PObject) {
            PObject tmp = (PObject) ob;
            log.finer(((Object) tmp.getReference()) + " " + tmp.toString());
        } else if (ob instanceof Dictionary) {
            Dictionary tmp2 = (Dictionary) ob;
            log.finer(((Object) tmp2.getPObjectReference()) + " " + tmp2.toString());
        } else {
            log.finer(((Object) ob.getClass()) + " " + ob.toString());
        }
    }

    public Object getObject(HashMap dictionaryEntries, Name key) {
        if (dictionaryEntries == null) {
            return null;
        }
        Object o2 = dictionaryEntries.get(key);
        if (o2 == null) {
            return null;
        }
        if (o2 instanceof Reference) {
            o2 = getObject((Reference) o2);
        }
        return o2;
    }

    public boolean isReference(HashMap dictionaryEntries, Name key) {
        return dictionaryEntries != null && (dictionaryEntries.get(key) instanceof Reference);
    }

    public Reference getReference(HashMap dictionaryEntries, Name key) {
        Object ref = dictionaryEntries.get(key);
        if (ref instanceof Reference) {
            return (Reference) ref;
        }
        return null;
    }

    public StateManager getStateManager() {
        return this.stateManager;
    }

    public void setStateManager(StateManager stateManager) {
        this.stateManager = stateManager;
    }

    public Object getObject(Object referenceObject) {
        if (referenceObject instanceof Reference) {
            return getObject((Reference) referenceObject);
        }
        return referenceObject;
    }

    public boolean isValidEntry(HashMap dictionaryEntries, Name key) {
        Object o2;
        return (dictionaryEntries == null || (o2 = dictionaryEntries.get(key)) == null || ((o2 instanceof Reference) && !isValidEntry((Reference) o2))) ? false : true;
    }

    public boolean isValidEntry(Reference reference) {
        WeakReference<Object> ob = this.refs.get(reference);
        return !(ob == null || ob.get() == null) || (this.lazyObjectLoader != null && this.lazyObjectLoader.haveEntry(reference));
    }

    public Number getNumber(HashMap dictionaryEntries, Name key) {
        Object o2 = getObject(dictionaryEntries, key);
        if (o2 instanceof Number) {
            return (Number) o2;
        }
        return null;
    }

    public Boolean getBoolean(HashMap dictionaryEntries, Name key) {
        Object o2 = getObject(dictionaryEntries, key);
        if (o2 instanceof String) {
            return Boolean.valueOf((String) o2);
        }
        return Boolean.valueOf((o2 instanceof Boolean) && ((Boolean) o2).booleanValue());
    }

    public float getFloat(HashMap dictionaryEntries, Name key) {
        Number n2 = getNumber(dictionaryEntries, key);
        if (n2 != null) {
            return n2.floatValue();
        }
        return 0.0f;
    }

    public int getInt(HashMap dictionaryEntries, Name key) {
        Number n2 = getNumber(dictionaryEntries, key);
        if (n2 != null) {
            return n2.intValue();
        }
        return 0;
    }

    public long getLong(HashMap dictionaryEntries, Name key) {
        Number n2 = getNumber(dictionaryEntries, key);
        if (n2 != null) {
            return n2.longValue();
        }
        return 0L;
    }

    public Name getName(HashMap dictionaryEntries, Name key) {
        Object o2 = getObject(dictionaryEntries, key);
        if (o2 != null && (o2 instanceof Name)) {
            return (Name) o2;
        }
        return null;
    }

    public String getString(HashMap dictionaryEntries, Name key) {
        Object o2 = getObject(dictionaryEntries, key);
        if (o2 != null) {
            if (o2 instanceof String) {
                return (String) o2;
            }
            if (o2 instanceof StringObject) {
                return ((StringObject) o2).getDecryptedLiteralString(this.securityManager);
            }
            if (o2 instanceof Name) {
                return ((Name) o2).getName();
            }
            return null;
        }
        return null;
    }

    public HashMap getDictionary(HashMap dictionaryEntries, Name key) {
        Object o2 = getObject(dictionaryEntries, key);
        if (o2 instanceof HashMap) {
            return (HashMap) o2;
        }
        if (o2 instanceof List) {
            List v2 = (List) o2;
            HashMap h1 = new HashMap();
            for (Object o1 : v2) {
                if (o1 instanceof HashMap) {
                    h1.putAll((HashMap) o1);
                }
            }
            return h1;
        }
        return null;
    }

    public List getArray(HashMap dictionaryEntries, Name key) {
        Object o2 = getObject(dictionaryEntries, key);
        if (o2 instanceof List) {
            return (List) o2;
        }
        return null;
    }

    public Rectangle2D.Float getRectangle(HashMap dictionaryEntries, Name key) {
        List v2 = (List) getObject(dictionaryEntries, key);
        if (v2 != null) {
            return new PRectangle(v2).toJava2dCoordinates();
        }
        return null;
    }

    public ICCBased getICCBased(Reference ref) {
        ICCBased cs = null;
        WeakReference<ICCBased> csRef = this.lookupReference2ICCBased.get(ref);
        if (csRef != null) {
            cs = csRef.get();
        }
        if (cs == null) {
            Object obj = getObject(ref);
            if (obj instanceof Stream) {
                Stream stream = (Stream) obj;
                cs = new ICCBased(this, stream);
                this.lookupReference2ICCBased.put(ref, new WeakReference<>(cs));
            }
        }
        return cs;
    }

    public Resources getResources(HashMap dictionaryEntries, Name key) {
        Object ob;
        if (dictionaryEntries == null || (ob = dictionaryEntries.get(key)) == null) {
            return null;
        }
        if (ob instanceof Resources) {
            return (Resources) ob;
        }
        if (ob instanceof Reference) {
            Reference reference = (Reference) ob;
            return getResources(reference);
        }
        if (ob instanceof HashMap) {
            HashMap ht = (HashMap) ob;
            Resources resources = new Resources(this, ht);
            dictionaryEntries.put(key, resources);
            return resources;
        }
        return null;
    }

    public Resources getResources(Reference reference) {
        Object object = getObject(reference);
        if (object instanceof Resources) {
            return (Resources) object;
        }
        if (object instanceof HashMap) {
            HashMap ht = (HashMap) object;
            Resources resources = new Resources(this, ht);
            addObject(resources, reference);
            return resources;
        }
        return null;
    }

    public void addObject(Object object, Reference objectReference) {
        this.refs.put(objectReference, new WeakReference<>(object));
    }

    public void removeObject(Reference objetReference) {
        if (objetReference != null) {
            this.refs.remove(objetReference);
        }
    }

    public Reference getObjectReference(HashMap dictionaryEntries, Name key) {
        if (dictionaryEntries == null) {
            return null;
        }
        Object o2 = dictionaryEntries.get(key);
        if (o2 == null) {
            return null;
        }
        Reference currentRef = null;
        while (o2 != null && (o2 instanceof Reference)) {
            currentRef = (Reference) o2;
            o2 = getObject(currentRef);
        }
        return currentRef;
    }

    public boolean isEncrypted() {
        return this.isEncrypted;
    }

    public SecurityManager getSecurityManager() {
        return this.securityManager;
    }

    public void setEncrypted(boolean flag) {
        this.isEncrypted = flag;
    }

    public void setLinearTraversal() {
        this.isLinearTraversal = true;
    }

    public boolean isLinearTraversal() {
        return this.isLinearTraversal;
    }

    public Catalog getCatalog() {
        return this.catalog;
    }

    public void setCatalog(Catalog c2) {
        this.catalog = c2;
    }

    public void disposeFontResources() {
        Set<Reference> test = this.refs.keySet();
        for (Reference ref : test) {
            WeakReference<Object> reference = this.refs.get(ref);
            Object tmp = reference != null ? reference.get() : null;
            if ((tmp instanceof Font) || (tmp instanceof FontDescriptor)) {
                this.refs.remove(ref);
            }
        }
    }

    public ImagePool getImagePool() {
        return this.imagePool;
    }

    public static void initializeThreadPool() {
        log.fine("Starting ICEpdf Thread Pool: " + commonPoolThreads + " threads.");
        commonThreadPool = new ThreadPoolExecutor(commonPoolThreads, commonPoolThreads, 10L, TimeUnit.SECONDS, new LinkedBlockingQueue());
        commonThreadPool.setThreadFactory(new ThreadFactory() { // from class: org.icepdf.core.util.Library.1
            @Override // java.util.concurrent.ThreadFactory
            public Thread newThread(Runnable command) {
                Thread newThread = new Thread(command);
                newThread.setName("ICEpdf-thread-pool");
                newThread.setPriority(5);
                newThread.setDaemon(true);
                return newThread;
            }
        });
        imageThreadPool = new ThreadPoolExecutor(imagePoolThreads, imagePoolThreads, 10L, TimeUnit.SECONDS, new LinkedBlockingQueue());
        imageThreadPool.setThreadFactory(new ThreadFactory() { // from class: org.icepdf.core.util.Library.2
            @Override // java.util.concurrent.ThreadFactory
            public Thread newThread(Runnable command) {
                Thread newThread = new Thread(command);
                newThread.setName("ICEpdf-thread-image-pool");
                newThread.setPriority(5);
                newThread.setDaemon(true);
                return newThread;
            }
        });
        painterThreadPool = new ThreadPoolExecutor(painterPoolThreads, painterPoolThreads, 10L, TimeUnit.SECONDS, new LinkedBlockingQueue());
        painterThreadPool.setThreadFactory(new ThreadFactory() { // from class: org.icepdf.core.util.Library.3
            @Override // java.util.concurrent.ThreadFactory
            public Thread newThread(Runnable command) {
                Thread newThread = new Thread(command);
                newThread.setName("ICEpdf-thread-painter-pool");
                newThread.setPriority(5);
                newThread.setDaemon(true);
                return newThread;
            }
        });
    }

    public static void shutdownThreadPool() {
        commonThreadPool.purge();
        commonThreadPool.shutdownNow();
        painterThreadPool.purge();
        painterThreadPool.shutdownNow();
        imageThreadPool.purge();
        imageThreadPool.shutdownNow();
    }

    public static void execute(Runnable runnable) {
        try {
            commonThreadPool.execute(runnable);
        } catch (RejectedExecutionException e2) {
            log.severe("ICEpdf Common Thread Pool was shutdown!");
        }
    }

    public static void executeImage(FutureTask callable) {
        try {
            imageThreadPool.execute(callable);
        } catch (RejectedExecutionException e2) {
            log.severe("ICEpdf Common Thread Pool was shutdown!");
        }
    }

    public static void executePainter(Runnable runnable) {
        try {
            painterThreadPool.execute(runnable);
        } catch (RejectedExecutionException e2) {
            log.severe("ICEpdf Common Thread Pool was shutdown!");
        }
    }
}
