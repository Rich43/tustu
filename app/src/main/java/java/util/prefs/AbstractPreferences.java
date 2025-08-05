package java.util.prefs;

import com.sun.javafx.font.LogicalFont;
import java.io.IOException;
import java.io.OutputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.TreeSet;
import javax.xml.transform.TransformerFactoryConfigurationError;
import org.w3c.dom.DOMException;

/* loaded from: rt.jar:java/util/prefs/AbstractPreferences.class */
public abstract class AbstractPreferences extends Preferences {
    private final String name;
    private final String absolutePath;
    final AbstractPreferences parent;
    private final AbstractPreferences root;
    protected boolean newNode = false;
    private Map<String, AbstractPreferences> kidCache = new HashMap();
    private boolean removed = false;
    private PreferenceChangeListener[] prefListeners = new PreferenceChangeListener[0];
    private NodeChangeListener[] nodeListeners = new NodeChangeListener[0];
    protected final Object lock = new Object();
    private static final String[] EMPTY_STRING_ARRAY = new String[0];
    private static final AbstractPreferences[] EMPTY_ABSTRACT_PREFS_ARRAY = new AbstractPreferences[0];
    private static final List<EventObject> eventQueue = new LinkedList();
    private static Thread eventDispatchThread = null;

    protected abstract void putSpi(String str, String str2);

    protected abstract String getSpi(String str);

    protected abstract void removeSpi(String str);

    protected abstract void removeNodeSpi() throws BackingStoreException;

    protected abstract String[] keysSpi() throws BackingStoreException;

    protected abstract String[] childrenNamesSpi() throws BackingStoreException;

    protected abstract AbstractPreferences childSpi(String str);

    protected abstract void syncSpi() throws BackingStoreException;

    protected abstract void flushSpi() throws BackingStoreException;

    protected AbstractPreferences(AbstractPreferences abstractPreferences, String str) {
        if (abstractPreferences == null) {
            if (!str.equals("")) {
                throw new IllegalArgumentException("Root name '" + str + "' must be \"\"");
            }
            this.absolutePath = "/";
            this.root = this;
        } else {
            if (str.indexOf(47) != -1) {
                throw new IllegalArgumentException("Name '" + str + "' contains '/'");
            }
            if (str.equals("")) {
                throw new IllegalArgumentException("Illegal name: empty string");
            }
            this.root = abstractPreferences.root;
            this.absolutePath = abstractPreferences == this.root ? "/" + str : abstractPreferences.absolutePath() + "/" + str;
        }
        this.name = str;
        this.parent = abstractPreferences;
    }

    @Override // java.util.prefs.Preferences
    public void put(String str, String str2) {
        if (str == null || str2 == null) {
            throw new NullPointerException();
        }
        if (str.length() > 80) {
            throw new IllegalArgumentException("Key too long: " + str);
        }
        if (str2.length() > 8192) {
            throw new IllegalArgumentException("Value too long: " + str2);
        }
        synchronized (this.lock) {
            if (this.removed) {
                throw new IllegalStateException("Node has been removed.");
            }
            putSpi(str, str2);
            enqueuePreferenceChangeEvent(str, str2);
        }
    }

    @Override // java.util.prefs.Preferences
    public String get(String str, String str2) {
        String str3;
        if (str == null) {
            throw new NullPointerException("Null key");
        }
        synchronized (this.lock) {
            if (this.removed) {
                throw new IllegalStateException("Node has been removed.");
            }
            String spi = null;
            try {
                spi = getSpi(str);
            } catch (Exception e2) {
            }
            str3 = spi == null ? str2 : spi;
        }
        return str3;
    }

    @Override // java.util.prefs.Preferences
    public void remove(String str) {
        Objects.requireNonNull(str, "Specified key cannot be null");
        synchronized (this.lock) {
            if (this.removed) {
                throw new IllegalStateException("Node has been removed.");
            }
            removeSpi(str);
            enqueuePreferenceChangeEvent(str, null);
        }
    }

    @Override // java.util.prefs.Preferences
    public void clear() throws BackingStoreException {
        synchronized (this.lock) {
            for (String str : keys()) {
                remove(str);
            }
        }
    }

    @Override // java.util.prefs.Preferences
    public void putInt(String str, int i2) {
        put(str, Integer.toString(i2));
    }

    @Override // java.util.prefs.Preferences
    public int getInt(String str, int i2) {
        int i3 = i2;
        try {
            String str2 = get(str, null);
            if (str2 != null) {
                i3 = Integer.parseInt(str2);
            }
        } catch (NumberFormatException e2) {
        }
        return i3;
    }

    @Override // java.util.prefs.Preferences
    public void putLong(String str, long j2) {
        put(str, Long.toString(j2));
    }

    @Override // java.util.prefs.Preferences
    public long getLong(String str, long j2) {
        long j3 = j2;
        try {
            String str2 = get(str, null);
            if (str2 != null) {
                j3 = Long.parseLong(str2);
            }
        } catch (NumberFormatException e2) {
        }
        return j3;
    }

    @Override // java.util.prefs.Preferences
    public void putBoolean(String str, boolean z2) {
        put(str, String.valueOf(z2));
    }

    @Override // java.util.prefs.Preferences
    public boolean getBoolean(String str, boolean z2) {
        boolean z3 = z2;
        String str2 = get(str, null);
        if (str2 != null) {
            if (str2.equalsIgnoreCase("true")) {
                z3 = true;
            } else if (str2.equalsIgnoreCase("false")) {
                z3 = false;
            }
        }
        return z3;
    }

    @Override // java.util.prefs.Preferences
    public void putFloat(String str, float f2) {
        put(str, Float.toString(f2));
    }

    @Override // java.util.prefs.Preferences
    public float getFloat(String str, float f2) {
        float f3 = f2;
        try {
            String str2 = get(str, null);
            if (str2 != null) {
                f3 = Float.parseFloat(str2);
            }
        } catch (NumberFormatException e2) {
        }
        return f3;
    }

    @Override // java.util.prefs.Preferences
    public void putDouble(String str, double d2) {
        put(str, Double.toString(d2));
    }

    @Override // java.util.prefs.Preferences
    public double getDouble(String str, double d2) {
        double d3 = d2;
        try {
            String str2 = get(str, null);
            if (str2 != null) {
                d3 = Double.parseDouble(str2);
            }
        } catch (NumberFormatException e2) {
        }
        return d3;
    }

    @Override // java.util.prefs.Preferences
    public void putByteArray(String str, byte[] bArr) {
        put(str, Base64.byteArrayToBase64(bArr));
    }

    @Override // java.util.prefs.Preferences
    public byte[] getByteArray(String str, byte[] bArr) {
        byte[] bArrBase64ToByteArray = bArr;
        String str2 = get(str, null);
        if (str2 != null) {
            try {
                bArrBase64ToByteArray = Base64.base64ToByteArray(str2);
            } catch (RuntimeException e2) {
            }
        }
        return bArrBase64ToByteArray;
    }

    @Override // java.util.prefs.Preferences
    public String[] keys() throws BackingStoreException {
        String[] strArrKeysSpi;
        synchronized (this.lock) {
            if (this.removed) {
                throw new IllegalStateException("Node has been removed.");
            }
            strArrKeysSpi = keysSpi();
        }
        return strArrKeysSpi;
    }

    @Override // java.util.prefs.Preferences
    public String[] childrenNames() throws BackingStoreException {
        String[] strArr;
        synchronized (this.lock) {
            if (this.removed) {
                throw new IllegalStateException("Node has been removed.");
            }
            TreeSet treeSet = new TreeSet(this.kidCache.keySet());
            for (String str : childrenNamesSpi()) {
                treeSet.add(str);
            }
            strArr = (String[]) treeSet.toArray(EMPTY_STRING_ARRAY);
        }
        return strArr;
    }

    protected final AbstractPreferences[] cachedChildren() {
        return (AbstractPreferences[]) this.kidCache.values().toArray(EMPTY_ABSTRACT_PREFS_ARRAY);
    }

    @Override // java.util.prefs.Preferences
    public Preferences parent() {
        AbstractPreferences abstractPreferences;
        synchronized (this.lock) {
            if (this.removed) {
                throw new IllegalStateException("Node has been removed.");
            }
            abstractPreferences = this.parent;
        }
        return abstractPreferences;
    }

    @Override // java.util.prefs.Preferences
    public Preferences node(String str) {
        synchronized (this.lock) {
            if (this.removed) {
                throw new IllegalStateException("Node has been removed.");
            }
            if (str.equals("")) {
                return this;
            }
            if (str.equals("/")) {
                return this.root;
            }
            if (str.charAt(0) != '/') {
                return node(new StringTokenizer(str, "/", true));
            }
            return this.root.node(new StringTokenizer(str.substring(1), "/", true));
        }
    }

    private Preferences node(StringTokenizer stringTokenizer) {
        String strNextToken = stringTokenizer.nextToken();
        if (strNextToken.equals("/")) {
            throw new IllegalArgumentException("Consecutive slashes in path");
        }
        synchronized (this.lock) {
            AbstractPreferences abstractPreferencesChildSpi = this.kidCache.get(strNextToken);
            if (abstractPreferencesChildSpi == null) {
                if (strNextToken.length() > 80) {
                    throw new IllegalArgumentException("Node name " + strNextToken + " too long");
                }
                abstractPreferencesChildSpi = childSpi(strNextToken);
                if (abstractPreferencesChildSpi.newNode) {
                    enqueueNodeAddedEvent(abstractPreferencesChildSpi);
                }
                this.kidCache.put(strNextToken, abstractPreferencesChildSpi);
            }
            if (!stringTokenizer.hasMoreTokens()) {
                return abstractPreferencesChildSpi;
            }
            stringTokenizer.nextToken();
            if (!stringTokenizer.hasMoreTokens()) {
                throw new IllegalArgumentException("Path ends with slash");
            }
            return abstractPreferencesChildSpi.node(stringTokenizer);
        }
    }

    @Override // java.util.prefs.Preferences
    public boolean nodeExists(String str) throws BackingStoreException {
        synchronized (this.lock) {
            if (str.equals("")) {
                return !this.removed;
            }
            if (this.removed) {
                throw new IllegalStateException("Node has been removed.");
            }
            if (str.equals("/")) {
                return true;
            }
            if (str.charAt(0) != '/') {
                return nodeExists(new StringTokenizer(str, "/", true));
            }
            return this.root.nodeExists(new StringTokenizer(str.substring(1), "/", true));
        }
    }

    private boolean nodeExists(StringTokenizer stringTokenizer) throws BackingStoreException {
        String strNextToken = stringTokenizer.nextToken();
        if (strNextToken.equals("/")) {
            throw new IllegalArgumentException("Consecutive slashes in path");
        }
        synchronized (this.lock) {
            AbstractPreferences child = this.kidCache.get(strNextToken);
            if (child == null) {
                child = getChild(strNextToken);
            }
            if (child == null) {
                return false;
            }
            if (!stringTokenizer.hasMoreTokens()) {
                return true;
            }
            stringTokenizer.nextToken();
            if (!stringTokenizer.hasMoreTokens()) {
                throw new IllegalArgumentException("Path ends with slash");
            }
            return child.nodeExists(stringTokenizer);
        }
    }

    @Override // java.util.prefs.Preferences
    public void removeNode() throws BackingStoreException {
        if (this == this.root) {
            throw new UnsupportedOperationException("Can't remove the root!");
        }
        synchronized (this.parent.lock) {
            removeNode2();
            this.parent.kidCache.remove(this.name);
        }
    }

    private void removeNode2() throws BackingStoreException {
        synchronized (this.lock) {
            if (this.removed) {
                throw new IllegalStateException("Node already removed.");
            }
            String[] strArrChildrenNamesSpi = childrenNamesSpi();
            for (int i2 = 0; i2 < strArrChildrenNamesSpi.length; i2++) {
                if (!this.kidCache.containsKey(strArrChildrenNamesSpi[i2])) {
                    this.kidCache.put(strArrChildrenNamesSpi[i2], childSpi(strArrChildrenNamesSpi[i2]));
                }
            }
            Iterator<AbstractPreferences> it = this.kidCache.values().iterator();
            while (it.hasNext()) {
                try {
                    it.next().removeNode2();
                    it.remove();
                } catch (BackingStoreException e2) {
                }
            }
            removeNodeSpi();
            this.removed = true;
            this.parent.enqueueNodeRemovedEvent(this);
        }
    }

    @Override // java.util.prefs.Preferences
    public String name() {
        return this.name;
    }

    @Override // java.util.prefs.Preferences
    public String absolutePath() {
        return this.absolutePath;
    }

    @Override // java.util.prefs.Preferences
    public boolean isUserNode() {
        return ((Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() { // from class: java.util.prefs.AbstractPreferences.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Boolean run2() {
                return Boolean.valueOf(AbstractPreferences.this.root == Preferences.userRoot());
            }
        })).booleanValue();
    }

    @Override // java.util.prefs.Preferences
    public void addPreferenceChangeListener(PreferenceChangeListener preferenceChangeListener) {
        if (preferenceChangeListener == null) {
            throw new NullPointerException("Change listener is null.");
        }
        synchronized (this.lock) {
            if (this.removed) {
                throw new IllegalStateException("Node has been removed.");
            }
            PreferenceChangeListener[] preferenceChangeListenerArr = this.prefListeners;
            this.prefListeners = new PreferenceChangeListener[preferenceChangeListenerArr.length + 1];
            System.arraycopy(preferenceChangeListenerArr, 0, this.prefListeners, 0, preferenceChangeListenerArr.length);
            this.prefListeners[preferenceChangeListenerArr.length] = preferenceChangeListener;
        }
        startEventDispatchThreadIfNecessary();
    }

    @Override // java.util.prefs.Preferences
    public void removePreferenceChangeListener(PreferenceChangeListener preferenceChangeListener) {
        synchronized (this.lock) {
            if (this.removed) {
                throw new IllegalStateException("Node has been removed.");
            }
            if (this.prefListeners == null || this.prefListeners.length == 0) {
                throw new IllegalArgumentException("Listener not registered.");
            }
            PreferenceChangeListener[] preferenceChangeListenerArr = new PreferenceChangeListener[this.prefListeners.length - 1];
            int i2 = 0;
            while (i2 < preferenceChangeListenerArr.length && this.prefListeners[i2] != preferenceChangeListener) {
                int i3 = i2;
                int i4 = i2;
                i2++;
                preferenceChangeListenerArr[i3] = this.prefListeners[i4];
            }
            if (i2 == preferenceChangeListenerArr.length && this.prefListeners[i2] != preferenceChangeListener) {
                throw new IllegalArgumentException("Listener not registered.");
            }
            while (i2 < preferenceChangeListenerArr.length) {
                int i5 = i2;
                i2++;
                preferenceChangeListenerArr[i5] = this.prefListeners[i2];
            }
            this.prefListeners = preferenceChangeListenerArr;
        }
    }

    @Override // java.util.prefs.Preferences
    public void addNodeChangeListener(NodeChangeListener nodeChangeListener) {
        if (nodeChangeListener == null) {
            throw new NullPointerException("Change listener is null.");
        }
        synchronized (this.lock) {
            if (this.removed) {
                throw new IllegalStateException("Node has been removed.");
            }
            if (this.nodeListeners == null) {
                this.nodeListeners = new NodeChangeListener[1];
                this.nodeListeners[0] = nodeChangeListener;
            } else {
                NodeChangeListener[] nodeChangeListenerArr = this.nodeListeners;
                this.nodeListeners = new NodeChangeListener[nodeChangeListenerArr.length + 1];
                System.arraycopy(nodeChangeListenerArr, 0, this.nodeListeners, 0, nodeChangeListenerArr.length);
                this.nodeListeners[nodeChangeListenerArr.length] = nodeChangeListener;
            }
        }
        startEventDispatchThreadIfNecessary();
    }

    @Override // java.util.prefs.Preferences
    public void removeNodeChangeListener(NodeChangeListener nodeChangeListener) {
        synchronized (this.lock) {
            if (this.removed) {
                throw new IllegalStateException("Node has been removed.");
            }
            if (this.nodeListeners == null || this.nodeListeners.length == 0) {
                throw new IllegalArgumentException("Listener not registered.");
            }
            int i2 = 0;
            while (i2 < this.nodeListeners.length && this.nodeListeners[i2] != nodeChangeListener) {
                i2++;
            }
            if (i2 == this.nodeListeners.length) {
                throw new IllegalArgumentException("Listener not registered.");
            }
            NodeChangeListener[] nodeChangeListenerArr = new NodeChangeListener[this.nodeListeners.length - 1];
            if (i2 != 0) {
                System.arraycopy(this.nodeListeners, 0, nodeChangeListenerArr, 0, i2);
            }
            if (i2 != nodeChangeListenerArr.length) {
                System.arraycopy(this.nodeListeners, i2 + 1, nodeChangeListenerArr, i2, nodeChangeListenerArr.length - i2);
            }
            this.nodeListeners = nodeChangeListenerArr;
        }
    }

    protected AbstractPreferences getChild(String str) throws BackingStoreException {
        synchronized (this.lock) {
            String[] strArrChildrenNames = childrenNames();
            for (int i2 = 0; i2 < strArrChildrenNames.length; i2++) {
                if (strArrChildrenNames[i2].equals(str)) {
                    return childSpi(strArrChildrenNames[i2]);
                }
            }
            return null;
        }
    }

    @Override // java.util.prefs.Preferences
    public String toString() {
        return (isUserNode() ? "User" : LogicalFont.SYSTEM) + " Preference Node: " + absolutePath();
    }

    @Override // java.util.prefs.Preferences
    public void sync() throws BackingStoreException {
        sync2();
    }

    private void sync2() throws BackingStoreException {
        AbstractPreferences[] abstractPreferencesArrCachedChildren;
        synchronized (this.lock) {
            if (this.removed) {
                throw new IllegalStateException("Node has been removed");
            }
            syncSpi();
            abstractPreferencesArrCachedChildren = cachedChildren();
        }
        for (AbstractPreferences abstractPreferences : abstractPreferencesArrCachedChildren) {
            abstractPreferences.sync2();
        }
    }

    @Override // java.util.prefs.Preferences
    public void flush() throws BackingStoreException {
        flush2();
    }

    private void flush2() throws BackingStoreException {
        synchronized (this.lock) {
            flushSpi();
            if (this.removed) {
                return;
            }
            for (AbstractPreferences abstractPreferences : cachedChildren()) {
                abstractPreferences.flush2();
            }
        }
    }

    protected boolean isRemoved() {
        boolean z2;
        synchronized (this.lock) {
            z2 = this.removed;
        }
        return z2;
    }

    /* loaded from: rt.jar:java/util/prefs/AbstractPreferences$NodeAddedEvent.class */
    private class NodeAddedEvent extends NodeChangeEvent {
        private static final long serialVersionUID = -6743557530157328528L;

        NodeAddedEvent(Preferences preferences, Preferences preferences2) {
            super(preferences, preferences2);
        }
    }

    /* loaded from: rt.jar:java/util/prefs/AbstractPreferences$NodeRemovedEvent.class */
    private class NodeRemovedEvent extends NodeChangeEvent {
        private static final long serialVersionUID = 8735497392918824837L;

        NodeRemovedEvent(Preferences preferences, Preferences preferences2) {
            super(preferences, preferences2);
        }
    }

    /* loaded from: rt.jar:java/util/prefs/AbstractPreferences$EventDispatchThread.class */
    private static class EventDispatchThread extends Thread {
        private EventDispatchThread() {
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            EventObject eventObject;
            while (true) {
                synchronized (AbstractPreferences.eventQueue) {
                    while (AbstractPreferences.eventQueue.isEmpty()) {
                        try {
                            AbstractPreferences.eventQueue.wait();
                        } catch (InterruptedException e2) {
                            return;
                        }
                    }
                    eventObject = (EventObject) AbstractPreferences.eventQueue.remove(0);
                }
                AbstractPreferences abstractPreferences = (AbstractPreferences) eventObject.getSource();
                if (eventObject instanceof PreferenceChangeEvent) {
                    PreferenceChangeEvent preferenceChangeEvent = (PreferenceChangeEvent) eventObject;
                    for (PreferenceChangeListener preferenceChangeListener : abstractPreferences.prefListeners()) {
                        preferenceChangeListener.preferenceChange(preferenceChangeEvent);
                    }
                } else {
                    NodeChangeEvent nodeChangeEvent = (NodeChangeEvent) eventObject;
                    NodeChangeListener[] nodeChangeListenerArrNodeListeners = abstractPreferences.nodeListeners();
                    if (nodeChangeEvent instanceof NodeAddedEvent) {
                        for (NodeChangeListener nodeChangeListener : nodeChangeListenerArrNodeListeners) {
                            nodeChangeListener.childAdded(nodeChangeEvent);
                        }
                    } else {
                        for (NodeChangeListener nodeChangeListener2 : nodeChangeListenerArrNodeListeners) {
                            nodeChangeListener2.childRemoved(nodeChangeEvent);
                        }
                    }
                }
            }
        }
    }

    private static synchronized void startEventDispatchThreadIfNecessary() {
        if (eventDispatchThread == null) {
            eventDispatchThread = new EventDispatchThread();
            eventDispatchThread.setDaemon(true);
            eventDispatchThread.start();
        }
    }

    PreferenceChangeListener[] prefListeners() {
        PreferenceChangeListener[] preferenceChangeListenerArr;
        synchronized (this.lock) {
            preferenceChangeListenerArr = this.prefListeners;
        }
        return preferenceChangeListenerArr;
    }

    NodeChangeListener[] nodeListeners() {
        NodeChangeListener[] nodeChangeListenerArr;
        synchronized (this.lock) {
            nodeChangeListenerArr = this.nodeListeners;
        }
        return nodeChangeListenerArr;
    }

    private void enqueuePreferenceChangeEvent(String str, String str2) {
        if (this.prefListeners.length != 0) {
            synchronized (eventQueue) {
                eventQueue.add(new PreferenceChangeEvent(this, str, str2));
                eventQueue.notify();
            }
        }
    }

    private void enqueueNodeAddedEvent(Preferences preferences) {
        if (this.nodeListeners.length != 0) {
            synchronized (eventQueue) {
                eventQueue.add(new NodeAddedEvent(this, preferences));
                eventQueue.notify();
            }
        }
    }

    private void enqueueNodeRemovedEvent(Preferences preferences) {
        if (this.nodeListeners.length != 0) {
            synchronized (eventQueue) {
                eventQueue.add(new NodeRemovedEvent(this, preferences));
                eventQueue.notify();
            }
        }
    }

    @Override // java.util.prefs.Preferences
    public void exportNode(OutputStream outputStream) throws DOMException, TransformerFactoryConfigurationError, BackingStoreException, IOException, IllegalArgumentException {
        XmlSupport.export(outputStream, this, false);
    }

    @Override // java.util.prefs.Preferences
    public void exportSubtree(OutputStream outputStream) throws DOMException, TransformerFactoryConfigurationError, BackingStoreException, IOException, IllegalArgumentException {
        XmlSupport.export(outputStream, this, true);
    }
}
