package com.sun.javafx.css;

import com.sun.javafx.css.FontFace;
import com.sun.javafx.css.StyleCache;
import com.sun.javafx.css.parser.CSSParser;
import com.sun.javafx.util.Logging;
import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.io.FileNotFoundException;
import java.io.FilePermission;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.AccessControlContext;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PermissionCollection;
import java.security.PrivilegedActionException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.css.StyleOrigin;
import javafx.css.Styleable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.stage.Window;
import sun.util.logging.PlatformLogger;

/* loaded from: jfxrt.jar:com/sun/javafx/css/StyleManager.class */
public final class StyleManager {
    private static final Object styleLock;
    private static PlatformLogger LOGGER;
    static final Map<Parent, CacheContainer> cacheContainerMap;
    final List<StylesheetContainer> userAgentStylesheetContainers;
    final List<StylesheetContainer> platformUserAgentStylesheetContainers;
    boolean hasDefaultUserAgentStylesheet;
    final Map<String, StylesheetContainer> stylesheetContainerMap;
    private final Map<String, Image> imageCache;
    private static final String skinPrefix = "com/sun/javafx/scene/control/skin/";
    private static final String skinUtilsClassName = "com.sun.javafx.scene.control.skin.Utils";
    private Key key;
    private static ObservableList<CssError> errors;
    private static List<String> cacheMapKey;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !StyleManager.class.desiredAssertionStatus();
        styleLock = new Object();
        cacheContainerMap = new WeakHashMap();
        errors = null;
    }

    private static PlatformLogger getLogger() {
        if (LOGGER == null) {
            LOGGER = Logging.getCSSLogger();
        }
        return LOGGER;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/css/StyleManager$InstanceHolder.class */
    private static class InstanceHolder {
        static final StyleManager INSTANCE = new StyleManager();

        private InstanceHolder() {
        }
    }

    public static StyleManager getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private StyleManager() {
        this.userAgentStylesheetContainers = new ArrayList();
        this.platformUserAgentStylesheetContainers = new ArrayList();
        this.hasDefaultUserAgentStylesheet = false;
        this.stylesheetContainerMap = new HashMap();
        this.imageCache = new HashMap();
        this.key = null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    CacheContainer getCacheContainer(Styleable styleable, SubScene subScene) {
        Scene scene;
        CacheContainer cacheContainer;
        if (styleable == 0 && subScene == null) {
            return null;
        }
        Parent root = null;
        if (subScene != null) {
            root = subScene.getRoot();
        } else if (styleable instanceof Node) {
            Node node = (Node) styleable;
            Scene scene2 = node.getScene();
            if (scene2 != null) {
                root = scene2.getRoot();
            }
        } else if ((styleable instanceof Window) && (scene = ((Window) styleable).getScene()) != null) {
            root = scene.getRoot();
        }
        if (root == null) {
            return null;
        }
        synchronized (styleLock) {
            CacheContainer container = cacheContainerMap.get(root);
            if (container == null) {
                container = new CacheContainer();
                cacheContainerMap.put(root, container);
            }
            cacheContainer = container;
        }
        return cacheContainer;
    }

    public StyleCache getSharedCache(Styleable styleable, SubScene subScene, StyleCache.Key key) {
        Map<StyleCache.Key, StyleCache> styleCache;
        CacheContainer container = getCacheContainer(styleable, subScene);
        if (container == null || (styleCache = container.getStyleCache()) == null) {
            return null;
        }
        StyleCache sharedCache = styleCache.get(key);
        if (sharedCache == null) {
            sharedCache = new StyleCache();
            styleCache.put(new StyleCache.Key(key), sharedCache);
        }
        return sharedCache;
    }

    public StyleMap getStyleMap(Styleable styleable, SubScene subScene, int smapId) {
        CacheContainer container;
        if (smapId != -1 && (container = getCacheContainer(styleable, subScene)) != null) {
            return container.getStyleMap(smapId);
        }
        return StyleMap.EMPTY_MAP;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/css/StyleManager$StylesheetContainer.class */
    static class StylesheetContainer {
        final String fname;
        final Stylesheet stylesheet;
        final SelectorPartitioning selectorPartitioning;
        final RefList<Parent> parentUsers;
        final List<Image> imageCache;
        final int hash;
        final byte[] checksum;
        boolean checksumInvalid;

        StylesheetContainer(String fname, Stylesheet stylesheet) {
            this(fname, stylesheet, stylesheet != null ? StyleManager.calculateCheckSum(stylesheet.getUrl()) : new byte[0]);
        }

        StylesheetContainer(String fname, Stylesheet stylesheet, byte[] checksum) {
            this.checksumInvalid = false;
            this.fname = fname;
            this.hash = fname != null ? fname.hashCode() : 127;
            this.stylesheet = stylesheet;
            if (stylesheet != null) {
                this.selectorPartitioning = new SelectorPartitioning();
                List<Rule> rules = stylesheet.getRules();
                int rMax = (rules == null || rules.isEmpty()) ? 0 : rules.size();
                for (int r2 = 0; r2 < rMax; r2++) {
                    Rule rule = rules.get(r2);
                    List<Selector> selectors = rule.getUnobservedSelectorList();
                    int sMax = (selectors == null || selectors.isEmpty()) ? 0 : selectors.size();
                    for (int s2 = 0; s2 < sMax; s2++) {
                        Selector selector = selectors.get(s2);
                        this.selectorPartitioning.partition(selector);
                    }
                }
            } else {
                this.selectorPartitioning = null;
            }
            this.parentUsers = new RefList<>();
            this.imageCache = new ArrayList();
            this.checksum = checksum;
        }

        void invalidateChecksum() {
            this.checksumInvalid = this.checksum.length > 0;
        }

        public int hashCode() {
            return this.hash;
        }

        public boolean equals(Object obj) {
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            StylesheetContainer other = (StylesheetContainer) obj;
            if (this.fname == null) {
                if (other.fname != null) {
                    return false;
                }
                return true;
            }
            if (!this.fname.equals(other.fname)) {
                return false;
            }
            return true;
        }

        public String toString() {
            return this.fname;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/css/StyleManager$RefList.class */
    static class RefList<K> {
        final List<Reference<K>> list = new ArrayList();

        RefList() {
        }

        void add(K key) {
            for (int n2 = this.list.size() - 1; 0 <= n2; n2--) {
                Reference<K> ref = this.list.get(n2);
                K k2 = ref.get();
                if (k2 == null) {
                    this.list.remove(n2);
                } else if (k2 == key) {
                    return;
                }
            }
            this.list.add(new WeakReference(key));
        }

        void remove(K key) {
            for (int n2 = this.list.size() - 1; 0 <= n2; n2--) {
                Reference<K> ref = this.list.get(n2);
                K k2 = ref.get();
                if (k2 == null) {
                    this.list.remove(n2);
                } else if (k2 == key) {
                    this.list.remove(n2);
                    return;
                }
            }
        }

        boolean contains(K key) {
            for (int n2 = this.list.size() - 1; 0 <= n2; n2--) {
                Reference<K> ref = this.list.get(n2);
                K k2 = ref.get();
                if (k2 == key) {
                    return true;
                }
            }
            return false;
        }
    }

    public void forget(Scene scene) {
        if (scene == null) {
            return;
        }
        forget(scene.getRoot());
        synchronized (styleLock) {
            if (scene.getUserAgentStylesheet() != null) {
                String sceneUserAgentStylesheet = scene.getUserAgentStylesheet().trim();
                if (!sceneUserAgentStylesheet.isEmpty()) {
                    for (int n2 = this.userAgentStylesheetContainers.size() - 1; 0 <= n2; n2--) {
                        StylesheetContainer container = this.userAgentStylesheetContainers.get(n2);
                        if (sceneUserAgentStylesheet.equals(container.fname)) {
                            container.parentUsers.remove(scene.getRoot());
                            if (container.parentUsers.list.size() == 0) {
                                this.userAgentStylesheetContainers.remove(n2);
                            }
                        }
                    }
                }
            }
            Set<Map.Entry<String, StylesheetContainer>> stylesheetContainers = this.stylesheetContainerMap.entrySet();
            Iterator<Map.Entry<String, StylesheetContainer>> iter = stylesheetContainers.iterator();
            while (iter.hasNext()) {
                Map.Entry<String, StylesheetContainer> entry = iter.next();
                StylesheetContainer container2 = entry.getValue();
                Iterator<Reference<Parent>> parentIter = container2.parentUsers.list.iterator();
                while (parentIter.hasNext()) {
                    Reference<Parent> ref = parentIter.next();
                    Parent _parent = ref.get();
                    if (_parent == null || _parent.getScene() == scene || _parent.getScene() == null) {
                        ref.clear();
                        parentIter.remove();
                    }
                }
                if (container2.parentUsers.list.isEmpty()) {
                    iter.remove();
                }
            }
        }
    }

    public void stylesheetsChanged(Scene scene, ListChangeListener.Change<String> c2) {
        synchronized (styleLock) {
            Set<Map.Entry<Parent, CacheContainer>> entrySet = cacheContainerMap.entrySet();
            for (Map.Entry<Parent, CacheContainer> entry : entrySet) {
                Parent parent = entry.getKey();
                CacheContainer container = entry.getValue();
                if (parent.getScene() == scene) {
                    container.clearCache();
                }
            }
            c2.reset();
            while (c2.next()) {
                if (c2.wasRemoved()) {
                    for (String fname : c2.getRemoved()) {
                        stylesheetRemoved(scene, fname);
                        StylesheetContainer stylesheetContainer = this.stylesheetContainerMap.get(fname);
                        if (stylesheetContainer != null) {
                            stylesheetContainer.invalidateChecksum();
                        }
                    }
                }
            }
        }
    }

    private void stylesheetRemoved(Scene scene, String fname) {
        stylesheetRemoved(scene.getRoot(), fname);
    }

    public void forget(Parent parent) {
        if (parent == null) {
            return;
        }
        synchronized (styleLock) {
            CacheContainer removedContainer = cacheContainerMap.remove(parent);
            if (removedContainer != null) {
                removedContainer.clearCache();
            }
            List<String> stylesheets = parent.getStylesheets();
            if (stylesheets != null && !stylesheets.isEmpty()) {
                for (String fname : stylesheets) {
                    stylesheetRemoved(parent, fname);
                }
            }
            Iterator<Map.Entry<String, StylesheetContainer>> containerIterator = this.stylesheetContainerMap.entrySet().iterator();
            while (containerIterator.hasNext()) {
                Map.Entry<String, StylesheetContainer> entry = containerIterator.next();
                StylesheetContainer container = entry.getValue();
                container.parentUsers.remove(parent);
                if (container.parentUsers.list.isEmpty()) {
                    containerIterator.remove();
                    if (container.selectorPartitioning != null) {
                        container.selectorPartitioning.reset();
                    }
                    String fname2 = container.fname;
                    cleanUpImageCache(fname2);
                }
            }
        }
    }

    public void stylesheetsChanged(Parent parent, ListChangeListener.Change<String> c2) {
        synchronized (styleLock) {
            c2.reset();
            while (c2.next()) {
                if (c2.wasRemoved()) {
                    for (String fname : c2.getRemoved()) {
                        stylesheetRemoved(parent, fname);
                        StylesheetContainer stylesheetContainer = this.stylesheetContainerMap.get(fname);
                        if (stylesheetContainer != null) {
                            stylesheetContainer.invalidateChecksum();
                        }
                    }
                }
            }
        }
    }

    private void stylesheetRemoved(Parent parent, String fname) {
        synchronized (styleLock) {
            StylesheetContainer stylesheetContainer = this.stylesheetContainerMap.get(fname);
            if (stylesheetContainer == null) {
                return;
            }
            stylesheetContainer.parentUsers.remove(parent);
            if (stylesheetContainer.parentUsers.list.isEmpty()) {
                removeStylesheetContainer(stylesheetContainer);
            }
        }
    }

    public void forget(SubScene subScene) {
        Parent subSceneRoot;
        if (subScene == null || (subSceneRoot = subScene.getRoot()) == null) {
            return;
        }
        forget(subSceneRoot);
        synchronized (styleLock) {
            if (subScene.getUserAgentStylesheet() != null) {
                String sceneUserAgentStylesheet = subScene.getUserAgentStylesheet().trim();
                if (!sceneUserAgentStylesheet.isEmpty()) {
                    Iterator<StylesheetContainer> iterator = this.userAgentStylesheetContainers.iterator();
                    while (iterator.hasNext()) {
                        StylesheetContainer container = iterator.next();
                        if (sceneUserAgentStylesheet.equals(container.fname)) {
                            container.parentUsers.remove(subScene.getRoot());
                            if (container.parentUsers.list.size() == 0) {
                                iterator.remove();
                            }
                        }
                    }
                }
            }
            List<StylesheetContainer> stylesheetContainers = new ArrayList<>(this.stylesheetContainerMap.values());
            Iterator<StylesheetContainer> iter = stylesheetContainers.iterator();
            while (iter.hasNext()) {
                Iterator<Reference<Parent>> parentIter = iter.next().parentUsers.list.iterator();
                while (parentIter.hasNext()) {
                    Reference<Parent> ref = parentIter.next();
                    Parent _parent = ref.get();
                    if (_parent != null) {
                        Parent p2 = _parent;
                        while (true) {
                            if (p2 == null) {
                                break;
                            }
                            if (subSceneRoot == p2.getParent()) {
                                ref.clear();
                                parentIter.remove();
                                forget(_parent);
                                break;
                            }
                            p2 = p2.getParent();
                        }
                    }
                }
            }
        }
    }

    private void removeStylesheetContainer(StylesheetContainer stylesheetContainer) {
        if (stylesheetContainer == null) {
            return;
        }
        synchronized (styleLock) {
            String fname = stylesheetContainer.fname;
            this.stylesheetContainerMap.remove(fname);
            if (stylesheetContainer.selectorPartitioning != null) {
                stylesheetContainer.selectorPartitioning.reset();
            }
            for (Map.Entry<Parent, CacheContainer> entry : cacheContainerMap.entrySet()) {
                CacheContainer container = entry.getValue();
                if (container != null && container.cacheMap != null && !container.cacheMap.isEmpty()) {
                    List<List<String>> entriesToRemove = new ArrayList<>();
                    for (Map.Entry<List<String>, Map<Key, Cache>> cacheMapEntry : container.cacheMap.entrySet()) {
                        List<String> cacheMapKey2 = cacheMapEntry.getKey();
                        if (cacheMapKey2 != null) {
                            if (cacheMapKey2.contains(fname)) {
                                entriesToRemove.add(cacheMapKey2);
                            }
                        } else if (fname == null) {
                            entriesToRemove.add(cacheMapKey2);
                        }
                    }
                    if (!entriesToRemove.isEmpty()) {
                        Iterator<List<String>> it = entriesToRemove.iterator();
                        while (it.hasNext()) {
                            Map<Key, Cache> cacheEntry = (Map) container.cacheMap.remove(it.next());
                            if (cacheEntry != null) {
                                cacheEntry.clear();
                            }
                        }
                    }
                }
            }
            cleanUpImageCache(fname);
            List<Reference<Parent>> parentList = stylesheetContainer.parentUsers.list;
            for (int n2 = parentList.size() - 1; 0 <= n2; n2--) {
                Reference<Parent> ref = parentList.remove(n2);
                Parent parent = ref.get();
                ref.clear();
                if (parent != null && parent.getScene() != null) {
                    parent.impl_reapplyCSS();
                }
            }
        }
    }

    public Image getCachedImage(String url) {
        Image image;
        synchronized (styleLock) {
            Image image2 = null;
            if (this.imageCache.containsKey(url)) {
                image2 = this.imageCache.get(url);
            } else {
                try {
                    image2 = new Image(url);
                    if (image2.isError()) {
                        PlatformLogger logger = getLogger();
                        if (logger != null && logger.isLoggable(PlatformLogger.Level.WARNING)) {
                            logger.warning("Error loading image: " + url);
                        }
                        image2 = null;
                    }
                    this.imageCache.put(url, image2);
                } catch (IllegalArgumentException iae) {
                    PlatformLogger logger2 = getLogger();
                    if (logger2 != null && logger2.isLoggable(PlatformLogger.Level.WARNING)) {
                        logger2.warning(iae.getLocalizedMessage());
                    }
                } catch (NullPointerException npe) {
                    PlatformLogger logger3 = getLogger();
                    if (logger3 != null && logger3.isLoggable(PlatformLogger.Level.WARNING)) {
                        logger3.warning(npe.getLocalizedMessage());
                    }
                }
            }
            image = image2;
        }
        return image;
    }

    private void cleanUpImageCache(String imgFname) {
        synchronized (styleLock) {
            if (imgFname == null) {
                if (this.imageCache.isEmpty()) {
                    return;
                }
            }
            String fname = imgFname.trim();
            if (fname.isEmpty()) {
                return;
            }
            int len = fname.lastIndexOf(47);
            String path = len > 0 ? fname.substring(0, len) : fname;
            int plen = path.length();
            String[] entriesToRemove = new String[this.imageCache.size()];
            int count = 0;
            Set<Map.Entry<String, Image>> entrySet = this.imageCache.entrySet();
            for (Map.Entry<String, Image> entry : entrySet) {
                String key = entry.getKey();
                int len2 = key.lastIndexOf(47);
                String kpath = len2 > 0 ? key.substring(0, len2) : key;
                int klen = kpath.length();
                boolean match = klen > plen ? kpath.startsWith(path) : path.startsWith(kpath);
                if (match) {
                    int i2 = count;
                    count++;
                    entriesToRemove[i2] = key;
                }
            }
            for (int n2 = 0; n2 < count; n2++) {
                this.imageCache.remove(entriesToRemove[n2]);
            }
        }
    }

    private static URL getURL(String str) {
        if (str == null || str.trim().isEmpty()) {
            return null;
        }
        try {
            URI uri = new URI(str.trim());
            if (!uri.isAbsolute()) {
                if (str.startsWith(skinPrefix) && (str.endsWith(".css") || str.endsWith(".bss"))) {
                    try {
                        ClassLoader cl = StyleManager.class.getClassLoader();
                        Class<?> clz = Class.forName(skinUtilsClassName, true, cl);
                        Method m_getResource = clz.getMethod("getResource", String.class);
                        return (URL) m_getResource.invoke(null, str.substring(skinPrefix.length()));
                    } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
                        ex.printStackTrace();
                        return null;
                    }
                }
                ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
                String path = uri.getPath();
                URL resource = null;
                if (contextClassLoader != null) {
                    if (path.startsWith("/")) {
                        resource = contextClassLoader.getResource(path.substring(1));
                    } else {
                        resource = contextClassLoader.getResource(path);
                    }
                }
                return resource;
            }
            return uri.toURL();
        } catch (MalformedURLException e2) {
            return null;
        } catch (URISyntaxException e3) {
            return null;
        }
    }

    /* JADX WARN: Failed to apply debug info
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r7v0 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r8v0 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Multi-variable type inference failed. Error: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.RegisterArg.getSVar()" because the return value of "jadx.core.dex.nodes.InsnNode.getResult()" is null
    	at jadx.core.dex.visitors.typeinference.AbstractTypeConstraint.collectRelatedVars(AbstractTypeConstraint.java:31)
    	at jadx.core.dex.visitors.typeinference.AbstractTypeConstraint.<init>(AbstractTypeConstraint.java:19)
    	at jadx.core.dex.visitors.typeinference.TypeSearch$1.<init>(TypeSearch.java:376)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.makeMoveConstraint(TypeSearch.java:376)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.makeConstraint(TypeSearch.java:361)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.collectConstraints(TypeSearch.java:341)
    	at java.base/java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:60)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.runMultiVariableSearch(FixTypesVisitor.java:116)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:91)
     */
    /* JADX WARN: Not initialized variable reg: 7, insn: 0x00da: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r7 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY] A[D('stream' java.io.InputStream)]) A[TRY_LEAVE], block:B:54:0x00da */
    /* JADX WARN: Not initialized variable reg: 8, insn: 0x00de: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r8 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]), block:B:56:0x00de */
    /* JADX WARN: Type inference failed for: r7v0, names: [stream], types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r8v0, types: [java.lang.Throwable] */
    static byte[] calculateCheckSum(String fname) {
        URL url;
        if (fname == null || fname.isEmpty()) {
            return new byte[0];
        }
        try {
            url = getURL(fname);
        } catch (IOException | IllegalArgumentException | SecurityException | NoSuchAlgorithmException e2) {
        }
        if (url == null || !DeploymentDescriptorParser.ATTR_FILE.equals(url.getProtocol())) {
            return new byte[0];
        }
        try {
            InputStream inputStreamOpenStream = url.openStream();
            Throwable th = null;
            DigestInputStream digestInputStream = new DigestInputStream(inputStreamOpenStream, MessageDigest.getInstance("MD5"));
            Throwable th2 = null;
            try {
                try {
                    digestInputStream.getMessageDigest().reset();
                    while (digestInputStream.read() != -1) {
                    }
                    byte[] bArrDigest = digestInputStream.getMessageDigest().digest();
                    if (digestInputStream != null) {
                        if (0 != 0) {
                            try {
                                digestInputStream.close();
                            } catch (Throwable th3) {
                                th2.addSuppressed(th3);
                            }
                        } else {
                            digestInputStream.close();
                        }
                    }
                    if (inputStreamOpenStream != null) {
                        if (0 != 0) {
                            try {
                                inputStreamOpenStream.close();
                            } catch (Throwable th4) {
                                th.addSuppressed(th4);
                            }
                        } else {
                            inputStreamOpenStream.close();
                        }
                    }
                    return bArrDigest;
                } catch (Throwable th5) {
                    if (digestInputStream != null) {
                        if (th2 != null) {
                            try {
                                digestInputStream.close();
                            } catch (Throwable th6) {
                                th2.addSuppressed(th6);
                            }
                        } else {
                            digestInputStream.close();
                        }
                    }
                    throw th5;
                }
            } finally {
            }
        } finally {
        }
    }

    public static Stylesheet loadStylesheet(String fname) {
        try {
            return loadStylesheetUnPrivileged(fname);
        } catch (AccessControlException e2) {
            if (fname.length() < 7 && fname.indexOf("!/") < fname.length() - 7) {
                return null;
            }
            try {
                URI requestedFileUrI = new URI(fname);
                if ("jar".equals(requestedFileUrI.getScheme())) {
                    URI styleManagerJarURI = (URI) AccessController.doPrivileged(() -> {
                        return StyleManager.class.getProtectionDomain().getCodeSource().getLocation().toURI();
                    });
                    String styleManagerJarPath = styleManagerJarURI.getSchemeSpecificPart();
                    String requestedFilePath = requestedFileUrI.getSchemeSpecificPart();
                    String requestedFileJarPart = requestedFilePath.substring(requestedFilePath.indexOf(47), requestedFilePath.indexOf("!/"));
                    if (styleManagerJarPath.equals(requestedFileJarPart)) {
                        String requestedFileJarPathNoLeadingSlash = fname.substring(fname.indexOf("!/") + 2);
                        if (fname.endsWith(".css") || fname.endsWith(".bss")) {
                            FilePermission perm = new FilePermission(styleManagerJarPath, "read");
                            PermissionCollection perms = perm.newPermissionCollection();
                            perms.add(perm);
                            AccessControlContext permsAcc = new AccessControlContext(new ProtectionDomain[]{new ProtectionDomain(null, perms)});
                            try {
                                JarFile jar = (JarFile) AccessController.doPrivileged(() -> {
                                    return new JarFile(styleManagerJarPath);
                                }, permsAcc);
                                if (jar != null) {
                                    JarEntry entry = jar.getJarEntry(requestedFileJarPathNoLeadingSlash);
                                    if (entry != null) {
                                        return (Stylesheet) AccessController.doPrivileged(() -> {
                                            return loadStylesheetUnPrivileged(fname);
                                        }, permsAcc);
                                    }
                                    return null;
                                }
                                return null;
                            } catch (PrivilegedActionException e3) {
                                return null;
                            }
                        }
                        return null;
                    }
                    return null;
                }
                return null;
            } catch (URISyntaxException e4) {
                return null;
            } catch (PrivilegedActionException e5) {
                return null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Stylesheet loadStylesheetUnPrivileged(String fname) {
        URL url;
        synchronized (styleLock) {
            Boolean parse = (Boolean) AccessController.doPrivileged(() -> {
                boolean zBooleanValue;
                String bss = System.getProperty("binary.css");
                if (fname.endsWith(".bss") || bss == null) {
                    zBooleanValue = Boolean.FALSE.booleanValue();
                } else {
                    zBooleanValue = !Boolean.valueOf(bss).booleanValue();
                }
                return Boolean.valueOf(zBooleanValue);
            });
            try {
                String ext = parse.booleanValue() ? ".css" : ".bss";
                Stylesheet stylesheet = null;
                if (!fname.endsWith(".css") && !fname.endsWith(".bss")) {
                    url = getURL(fname);
                    parse = true;
                } else {
                    String name = fname.substring(0, fname.length() - 4);
                    url = getURL(name + ext);
                    if (url == null) {
                        Boolean boolValueOf = Boolean.valueOf(!parse.booleanValue());
                        parse = boolValueOf;
                        if (boolValueOf.booleanValue()) {
                            url = getURL(name + ".css");
                        }
                    }
                    if (url != null && !parse.booleanValue()) {
                        try {
                            stylesheet = Stylesheet.loadBinary(url);
                        } catch (IOException e2) {
                            stylesheet = null;
                        }
                        if (stylesheet == null) {
                            Boolean boolValueOf2 = Boolean.valueOf(!parse.booleanValue());
                            parse = boolValueOf2;
                            if (boolValueOf2.booleanValue()) {
                                url = getURL(fname);
                            }
                        }
                    }
                }
                if (url != null && parse.booleanValue()) {
                    stylesheet = new CSSParser().parse(url);
                }
                if (stylesheet == null) {
                    if (errors != null) {
                        CssError error = new CssError("Resource \"" + fname + "\" not found.");
                        errors.add(error);
                    }
                    if (getLogger().isLoggable(PlatformLogger.Level.WARNING)) {
                        getLogger().warning(String.format("Resource \"%s\" not found.", fname));
                    }
                }
                if (stylesheet != null) {
                    for (FontFace fontFace : stylesheet.getFontFaces()) {
                        Iterator<FontFace.FontFaceSrc> it = fontFace.getSources().iterator();
                        while (true) {
                            if (it.hasNext()) {
                                FontFace.FontFaceSrc src = it.next();
                                if (src.getType() == FontFace.FontFaceSrcType.URL) {
                                    Font loadedFont = Font.loadFont(src.getSrc(), 10.0d);
                                    if (loadedFont == null) {
                                        getLogger().info("Could not load @font-face font [" + src.getSrc() + "]");
                                    }
                                }
                            }
                        }
                    }
                }
                return stylesheet;
            } catch (FileNotFoundException e3) {
                if (errors != null) {
                    CssError error2 = new CssError("Stylesheet \"" + fname + "\" not found.");
                    errors.add(error2);
                }
                if (getLogger().isLoggable(PlatformLogger.Level.INFO)) {
                    getLogger().info("Could not find stylesheet: " + fname);
                }
                return null;
            } catch (IOException e4) {
                if (errors != null) {
                    CssError error3 = new CssError("Could not load stylesheet: " + fname);
                    errors.add(error3);
                }
                if (getLogger().isLoggable(PlatformLogger.Level.INFO)) {
                    getLogger().info("Could not load stylesheet: " + fname);
                }
                return null;
            }
        }
    }

    public void setUserAgentStylesheets(List<String> urls) {
        if (urls == null || urls.size() == 0) {
            return;
        }
        synchronized (styleLock) {
            if (urls.size() == this.platformUserAgentStylesheetContainers.size()) {
                boolean isSame = true;
                int nMax = urls.size();
                for (int n2 = 0; n2 < nMax && isSame; n2++) {
                    String url = urls.get(n2);
                    String fname = url != null ? url.trim() : null;
                    if (fname == null || fname.isEmpty()) {
                        break;
                    }
                    StylesheetContainer container = this.platformUserAgentStylesheetContainers.get(n2);
                    boolean zEquals = fname.equals(container.fname);
                    isSame = zEquals;
                    if (zEquals) {
                        String stylesheetUrl = container.stylesheet.getUrl();
                        byte[] checksum = calculateCheckSum(stylesheetUrl);
                        isSame = Arrays.equals(checksum, container.checksum);
                    }
                }
                if (isSame) {
                    return;
                }
            }
            boolean modified = false;
            int nMax2 = urls.size();
            for (int n3 = 0; n3 < nMax2; n3++) {
                String url2 = urls.get(n3);
                String fname2 = url2 != null ? url2.trim() : null;
                if (fname2 != null && !fname2.isEmpty()) {
                    if (!modified) {
                        this.platformUserAgentStylesheetContainers.clear();
                        modified = true;
                    }
                    if (n3 == 0) {
                        _setDefaultUserAgentStylesheet(fname2);
                    } else {
                        _addUserAgentStylesheet(fname2);
                    }
                }
            }
            if (modified) {
                userAgentStylesheetsChanged();
            }
        }
    }

    public void addUserAgentStylesheet(String fname) {
        addUserAgentStylesheet((Scene) null, fname);
    }

    public void addUserAgentStylesheet(Scene scene, String url) {
        String fname = url != null ? url.trim() : null;
        if (fname == null || fname.isEmpty()) {
            return;
        }
        synchronized (styleLock) {
            CssError.setCurrentScene(scene);
            if (_addUserAgentStylesheet(fname)) {
                userAgentStylesheetsChanged();
            }
            CssError.setCurrentScene(null);
        }
    }

    private boolean _addUserAgentStylesheet(String fname) {
        synchronized (styleLock) {
            int nMax = this.platformUserAgentStylesheetContainers.size();
            for (int n2 = 0; n2 < nMax; n2++) {
                StylesheetContainer container = this.platformUserAgentStylesheetContainers.get(n2);
                if (fname.equals(container.fname)) {
                    return false;
                }
            }
            Stylesheet ua_stylesheet = loadStylesheet(fname);
            if (ua_stylesheet == null) {
                return false;
            }
            ua_stylesheet.setOrigin(StyleOrigin.USER_AGENT);
            this.platformUserAgentStylesheetContainers.add(new StylesheetContainer(fname, ua_stylesheet));
            return true;
        }
    }

    public void addUserAgentStylesheet(Scene scene, Stylesheet ua_stylesheet) {
        if (ua_stylesheet == null) {
            throw new IllegalArgumentException("null arg ua_stylesheet");
        }
        String url = ua_stylesheet.getUrl();
        String fname = url != null ? url.trim() : "";
        synchronized (styleLock) {
            int nMax = this.platformUserAgentStylesheetContainers.size();
            for (int n2 = 0; n2 < nMax; n2++) {
                StylesheetContainer container = this.platformUserAgentStylesheetContainers.get(n2);
                if (fname.equals(container.fname)) {
                    return;
                }
            }
            CssError.setCurrentScene(scene);
            this.platformUserAgentStylesheetContainers.add(new StylesheetContainer(fname, ua_stylesheet));
            if (ua_stylesheet != null) {
                ua_stylesheet.setOrigin(StyleOrigin.USER_AGENT);
            }
            userAgentStylesheetsChanged();
            CssError.setCurrentScene(null);
        }
    }

    public void setDefaultUserAgentStylesheet(String fname) {
        setDefaultUserAgentStylesheet(null, fname);
    }

    public void setDefaultUserAgentStylesheet(Scene scene, String url) {
        String fname = url != null ? url.trim() : null;
        if (fname == null || fname.isEmpty()) {
            return;
        }
        synchronized (styleLock) {
            CssError.setCurrentScene(scene);
            if (_setDefaultUserAgentStylesheet(fname)) {
                userAgentStylesheetsChanged();
            }
            CssError.setCurrentScene(null);
        }
    }

    private boolean _setDefaultUserAgentStylesheet(String fname) {
        synchronized (styleLock) {
            int n2 = 0;
            int nMax = this.platformUserAgentStylesheetContainers.size();
            while (n2 < nMax) {
                StylesheetContainer container = this.platformUserAgentStylesheetContainers.get(n2);
                if (!fname.equals(container.fname)) {
                    n2++;
                } else {
                    if (n2 > 0) {
                        this.platformUserAgentStylesheetContainers.remove(n2);
                        if (this.hasDefaultUserAgentStylesheet) {
                            this.platformUserAgentStylesheetContainers.set(0, container);
                        } else {
                            this.platformUserAgentStylesheetContainers.add(0, container);
                        }
                    }
                    return n2 > 0;
                }
            }
            Stylesheet ua_stylesheet = loadStylesheet(fname);
            if (ua_stylesheet == null) {
                return false;
            }
            ua_stylesheet.setOrigin(StyleOrigin.USER_AGENT);
            StylesheetContainer sc = new StylesheetContainer(fname, ua_stylesheet);
            if (this.platformUserAgentStylesheetContainers.size() == 0) {
                this.platformUserAgentStylesheetContainers.add(sc);
            } else if (this.hasDefaultUserAgentStylesheet) {
                this.platformUserAgentStylesheetContainers.set(0, sc);
            } else {
                this.platformUserAgentStylesheetContainers.add(0, sc);
            }
            this.hasDefaultUserAgentStylesheet = true;
            return true;
        }
    }

    public void removeUserAgentStylesheet(String url) {
        String fname = url != null ? url.trim() : null;
        if (fname == null || fname.isEmpty()) {
            return;
        }
        synchronized (styleLock) {
            boolean removed = false;
            for (int n2 = this.platformUserAgentStylesheetContainers.size() - 1; n2 >= 0; n2--) {
                if (!fname.equals(Application.getUserAgentStylesheet())) {
                    StylesheetContainer container = this.platformUserAgentStylesheetContainers.get(n2);
                    if (fname.equals(container.fname)) {
                        this.platformUserAgentStylesheetContainers.remove(n2);
                        removed = true;
                    }
                }
            }
            if (removed) {
                userAgentStylesheetsChanged();
            }
        }
    }

    public void setDefaultUserAgentStylesheet(Stylesheet ua_stylesheet) {
        if (ua_stylesheet == null) {
            return;
        }
        String url = ua_stylesheet.getUrl();
        String fname = url != null ? url.trim() : "";
        synchronized (styleLock) {
            int nMax = this.platformUserAgentStylesheetContainers.size();
            for (int n2 = 0; n2 < nMax; n2++) {
                StylesheetContainer container = this.platformUserAgentStylesheetContainers.get(n2);
                if (fname.equals(container.fname)) {
                    if (n2 > 0) {
                        this.platformUserAgentStylesheetContainers.remove(n2);
                        if (this.hasDefaultUserAgentStylesheet) {
                            this.platformUserAgentStylesheetContainers.set(0, container);
                        } else {
                            this.platformUserAgentStylesheetContainers.add(0, container);
                        }
                    }
                    return;
                }
            }
            StylesheetContainer sc = new StylesheetContainer(fname, ua_stylesheet);
            if (this.platformUserAgentStylesheetContainers.size() == 0) {
                this.platformUserAgentStylesheetContainers.add(sc);
            } else if (this.hasDefaultUserAgentStylesheet) {
                this.platformUserAgentStylesheetContainers.set(0, sc);
            } else {
                this.platformUserAgentStylesheetContainers.add(0, sc);
            }
            this.hasDefaultUserAgentStylesheet = true;
            ua_stylesheet.setOrigin(StyleOrigin.USER_AGENT);
            userAgentStylesheetsChanged();
        }
    }

    private void userAgentStylesheetsChanged() {
        List<Parent> parents = new ArrayList<>();
        synchronized (styleLock) {
            for (CacheContainer container : cacheContainerMap.values()) {
                container.clearCache();
            }
            StyleConverterImpl.clearCache();
            for (Parent root : cacheContainerMap.keySet()) {
                if (root != null) {
                    parents.add(root);
                }
            }
        }
        Iterator<Parent> it = parents.iterator();
        while (it.hasNext()) {
            it.next().impl_reapplyCSS();
        }
    }

    private List<StylesheetContainer> processStylesheets(List<String> stylesheets, Parent parent) {
        List<StylesheetContainer> list;
        synchronized (styleLock) {
            list = new ArrayList<>();
            int nMax = stylesheets.size();
            for (int n2 = 0; n2 < nMax; n2++) {
                String fname = stylesheets.get(n2);
                if (this.stylesheetContainerMap.containsKey(fname)) {
                    StylesheetContainer container = this.stylesheetContainerMap.get(fname);
                    if (!list.contains(container)) {
                        if (container.checksumInvalid) {
                            byte[] checksum = calculateCheckSum(fname);
                            if (!Arrays.equals(checksum, container.checksum)) {
                                removeStylesheetContainer(container);
                                Stylesheet stylesheet = loadStylesheet(fname);
                                container = new StylesheetContainer(fname, stylesheet, checksum);
                                this.stylesheetContainerMap.put(fname, container);
                            } else {
                                container.checksumInvalid = false;
                            }
                        }
                        list.add(container);
                    }
                    container.parentUsers.add(parent);
                } else {
                    Stylesheet stylesheet2 = loadStylesheet(fname);
                    StylesheetContainer container2 = new StylesheetContainer(fname, stylesheet2);
                    container2.parentUsers.add(parent);
                    this.stylesheetContainerMap.put(fname, container2);
                    list.add(container2);
                }
            }
        }
        return list;
    }

    private List<StylesheetContainer> gatherParentStylesheets(Parent parent) {
        List<StylesheetContainer> list;
        if (parent == null) {
            return Collections.emptyList();
        }
        List<String> parentStylesheets = parent.impl_getAllParentStylesheets();
        if (parentStylesheets == null || parentStylesheets.isEmpty()) {
            return Collections.emptyList();
        }
        synchronized (styleLock) {
            CssError.setCurrentScene(parent.getScene());
            list = processStylesheets(parentStylesheets, parent);
            CssError.setCurrentScene(null);
        }
        return list;
    }

    private List<StylesheetContainer> gatherSceneStylesheets(Scene scene) {
        List<StylesheetContainer> list;
        if (scene == null) {
            return Collections.emptyList();
        }
        List<String> sceneStylesheets = scene.getStylesheets();
        if (sceneStylesheets == null || sceneStylesheets.isEmpty()) {
            return Collections.emptyList();
        }
        synchronized (styleLock) {
            CssError.setCurrentScene(scene);
            list = processStylesheets(sceneStylesheets, scene.getRoot());
            CssError.setCurrentScene(null);
        }
        return list;
    }

    public StyleMap findMatchingStyles(Node node, SubScene subScene, Set<PseudoClass>[] triggerStates) {
        String strTrim;
        Scene scene = node.getScene();
        if (scene == null) {
            return StyleMap.EMPTY_MAP;
        }
        CacheContainer cacheContainer = getCacheContainer(node, subScene);
        if (cacheContainer == null) {
            if ($assertionsDisabled) {
                return StyleMap.EMPTY_MAP;
            }
            throw new AssertionError((Object) node.toString());
        }
        synchronized (styleLock) {
            Parent parent = node instanceof Parent ? (Parent) node : node.getParent();
            List<StylesheetContainer> parentStylesheets = gatherParentStylesheets(parent);
            boolean hasParentStylesheets = !parentStylesheets.isEmpty();
            List<StylesheetContainer> sceneStylesheets = gatherSceneStylesheets(scene);
            boolean hasSceneStylesheets = !sceneStylesheets.isEmpty();
            String inlineStyle = node.getStyle();
            boolean hasInlineStyles = (inlineStyle == null || inlineStyle.trim().isEmpty()) ? false : true;
            String sceneUserAgentStylesheet = scene.getUserAgentStylesheet();
            boolean hasSceneUserAgentStylesheet = (sceneUserAgentStylesheet == null || sceneUserAgentStylesheet.trim().isEmpty()) ? false : true;
            String subSceneUserAgentStylesheet = subScene != null ? subScene.getUserAgentStylesheet() : null;
            boolean hasSubSceneUserAgentStylesheet = (subSceneUserAgentStylesheet == null || subSceneUserAgentStylesheet.trim().isEmpty()) ? false : true;
            String regionUserAgentStylesheet = null;
            Node region = node;
            while (region != null) {
                regionUserAgentStylesheet = region instanceof Region ? ((Region) region).getUserAgentStylesheet() : null;
                if (regionUserAgentStylesheet != null) {
                    break;
                }
                region = region.getParent();
            }
            boolean hasRegionUserAgentStylesheet = (regionUserAgentStylesheet == null || regionUserAgentStylesheet.trim().isEmpty()) ? false : true;
            if (!hasInlineStyles && !hasParentStylesheets && !hasSceneStylesheets && !hasSceneUserAgentStylesheet && !hasSubSceneUserAgentStylesheet && !hasRegionUserAgentStylesheet && this.platformUserAgentStylesheetContainers.isEmpty()) {
                return StyleMap.EMPTY_MAP;
            }
            String cname = node.getTypeSelector();
            String id = node.getId();
            List<String> styleClasses = node.getStyleClass();
            if (this.key == null) {
                this.key = new Key();
            }
            this.key.className = cname;
            this.key.id = id;
            int nMax = styleClasses.size();
            for (int n2 = 0; n2 < nMax; n2++) {
                String styleClass = styleClasses.get(n2);
                if (styleClass != null && !styleClass.isEmpty()) {
                    this.key.styleClasses.add(StyleClassSet.getStyleClass(styleClass));
                }
            }
            Map<Key, Cache> cacheMap = cacheContainer.getCacheMap(parentStylesheets, regionUserAgentStylesheet);
            Cache cache = cacheMap.get(this.key);
            if (cache != null) {
                this.key.styleClasses.clear();
            } else {
                List<Selector> selectorData = new ArrayList<>();
                if (hasSubSceneUserAgentStylesheet || hasSceneUserAgentStylesheet) {
                    if (hasSubSceneUserAgentStylesheet) {
                        strTrim = subScene.getUserAgentStylesheet().trim();
                    } else {
                        strTrim = scene.getUserAgentStylesheet().trim();
                    }
                    String uaFileName = strTrim;
                    StylesheetContainer container = null;
                    int nMax2 = this.userAgentStylesheetContainers.size();
                    for (int n3 = 0; n3 < nMax2; n3++) {
                        container = this.userAgentStylesheetContainers.get(n3);
                        if (uaFileName.equals(container.fname)) {
                            break;
                        }
                        container = null;
                    }
                    if (container == null) {
                        Stylesheet stylesheet = loadStylesheet(uaFileName);
                        if (stylesheet != null) {
                            stylesheet.setOrigin(StyleOrigin.USER_AGENT);
                        }
                        container = new StylesheetContainer(uaFileName, stylesheet);
                        this.userAgentStylesheetContainers.add(container);
                    }
                    if (container.selectorPartitioning != null) {
                        Parent root = hasSubSceneUserAgentStylesheet ? subScene.getRoot() : scene.getRoot();
                        container.parentUsers.add(root);
                        List<Selector> matchingRules = container.selectorPartitioning.match(id, cname, this.key.styleClasses);
                        selectorData.addAll(matchingRules);
                    }
                } else if (!this.platformUserAgentStylesheetContainers.isEmpty()) {
                    int nMax3 = this.platformUserAgentStylesheetContainers.size();
                    for (int n4 = 0; n4 < nMax3; n4++) {
                        StylesheetContainer container2 = this.platformUserAgentStylesheetContainers.get(n4);
                        if (container2 != null && container2.selectorPartitioning != null) {
                            List<Selector> matchingRules2 = container2.selectorPartitioning.match(id, cname, this.key.styleClasses);
                            selectorData.addAll(matchingRules2);
                        }
                    }
                }
                if (hasRegionUserAgentStylesheet) {
                    StylesheetContainer container3 = null;
                    int nMax4 = this.userAgentStylesheetContainers.size();
                    for (int n5 = 0; n5 < nMax4; n5++) {
                        container3 = this.userAgentStylesheetContainers.get(n5);
                        if (regionUserAgentStylesheet.equals(container3.fname)) {
                            break;
                        }
                        container3 = null;
                    }
                    if (container3 == null) {
                        Stylesheet stylesheet2 = loadStylesheet(regionUserAgentStylesheet);
                        if (stylesheet2 != null) {
                            stylesheet2.setOrigin(StyleOrigin.USER_AGENT);
                        }
                        container3 = new StylesheetContainer(regionUserAgentStylesheet, stylesheet2);
                        this.userAgentStylesheetContainers.add(container3);
                    }
                    if (container3.selectorPartitioning != null) {
                        container3.parentUsers.add((Parent) region);
                        List<Selector> matchingRules3 = container3.selectorPartitioning.match(id, cname, this.key.styleClasses);
                        selectorData.addAll(matchingRules3);
                    }
                }
                if (!sceneStylesheets.isEmpty()) {
                    int nMax5 = sceneStylesheets.size();
                    for (int n6 = 0; n6 < nMax5; n6++) {
                        StylesheetContainer container4 = sceneStylesheets.get(n6);
                        if (container4 != null && container4.selectorPartitioning != null) {
                            List<Selector> matchingRules4 = container4.selectorPartitioning.match(id, cname, this.key.styleClasses);
                            selectorData.addAll(matchingRules4);
                        }
                    }
                }
                if (hasParentStylesheets) {
                    int nMax6 = parentStylesheets == null ? 0 : parentStylesheets.size();
                    for (int n7 = 0; n7 < nMax6; n7++) {
                        StylesheetContainer container5 = parentStylesheets.get(n7);
                        if (container5.selectorPartitioning != null) {
                            List<Selector> matchingRules5 = container5.selectorPartitioning.match(id, cname, this.key.styleClasses);
                            selectorData.addAll(matchingRules5);
                        }
                    }
                }
                cache = new Cache(selectorData);
                cacheMap.put(this.key, cache);
                this.key = null;
            }
            StyleMap smap = cache.getStyleMap(cacheContainer, node, triggerStates, hasInlineStyles);
            return smap;
        }
    }

    public static ObservableList<CssError> errorsProperty() {
        if (errors == null) {
            errors = FXCollections.observableArrayList();
        }
        return errors;
    }

    public static ObservableList<CssError> getErrors() {
        return errors;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/css/StyleManager$CacheContainer.class */
    static class CacheContainer {
        private Map<StyleCache.Key, StyleCache> styleCache;
        private Map<List<String>, Map<Key, Cache>> cacheMap;
        private List<StyleMap> styleMapList;
        private Map<String, Selector> inlineStylesCache;
        private int styleMapId = 0;
        private int baseStyleMapId = 0;

        CacheContainer() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Map<StyleCache.Key, StyleCache> getStyleCache() {
            if (this.styleCache == null) {
                this.styleCache = new HashMap();
            }
            return this.styleCache;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Removed duplicated region for block: B:11:0x0025  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public java.util.Map<com.sun.javafx.css.StyleManager.Key, com.sun.javafx.css.StyleManager.Cache> getCacheMap(java.util.List<com.sun.javafx.css.StyleManager.StylesheetContainer> r5, java.lang.String r6) {
            /*
                Method dump skipped, instructions count: 280
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.sun.javafx.css.StyleManager.CacheContainer.getCacheMap(java.util.List, java.lang.String):java.util.Map");
        }

        private List<StyleMap> getStyleMapList() {
            if (this.styleMapList == null) {
                this.styleMapList = new ArrayList();
            }
            return this.styleMapList;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int nextSmapId() {
            this.styleMapId = this.baseStyleMapId + getStyleMapList().size();
            return this.styleMapId;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addStyleMap(StyleMap smap) {
            getStyleMapList().add(smap);
        }

        public StyleMap getStyleMap(int smapId) {
            int correctedId = smapId - this.baseStyleMapId;
            if (0 <= correctedId && correctedId < getStyleMapList().size()) {
                return getStyleMapList().get(correctedId);
            }
            return StyleMap.EMPTY_MAP;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearCache() {
            if (this.cacheMap != null) {
                this.cacheMap.clear();
            }
            if (this.styleCache != null) {
                this.styleCache.clear();
            }
            if (this.styleMapList != null) {
                this.styleMapList.clear();
            }
            this.baseStyleMapId = this.styleMapId;
            if (this.baseStyleMapId > 1879048185) {
                this.styleMapId = 0;
                this.baseStyleMapId = 0;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Selector getInlineStyleSelector(String inlineStyle) {
            if (inlineStyle == null || inlineStyle.trim().isEmpty()) {
                return null;
            }
            if (this.inlineStylesCache != null && this.inlineStylesCache.containsKey(inlineStyle)) {
                return this.inlineStylesCache.get(inlineStyle);
            }
            if (this.inlineStylesCache == null) {
                this.inlineStylesCache = new HashMap();
            }
            Stylesheet inlineStylesheet = new CSSParser().parse("*{" + inlineStyle + "}");
            if (inlineStylesheet != null) {
                inlineStylesheet.setOrigin(StyleOrigin.INLINE);
                List<Rule> rules = inlineStylesheet.getRules();
                Rule rule = (rules == null || rules.isEmpty()) ? null : rules.get(0);
                List<Selector> selectors = rule != null ? rule.getUnobservedSelectorList() : null;
                Selector selector = (selectors == null || selectors.isEmpty()) ? null : selectors.get(0);
                if (selector != null) {
                    selector.setOrdinal(-1);
                    this.inlineStylesCache.put(inlineStyle, selector);
                    return selector;
                }
            }
            this.inlineStylesCache.put(inlineStyle, null);
            return null;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/css/StyleManager$Cache.class */
    private static class Cache {
        private final List<Selector> selectors;
        private final Map<Key, Integer> cache = new HashMap();

        /* loaded from: jfxrt.jar:com/sun/javafx/css/StyleManager$Cache$Key.class */
        private static class Key {
            final long[] key;
            final String inlineStyle;

            Key(long[] key, String inlineStyle) {
                this.key = key;
                this.inlineStyle = (inlineStyle == null || !inlineStyle.trim().isEmpty()) ? inlineStyle : null;
            }

            public String toString() {
                return Arrays.toString(this.key) + (this.inlineStyle != null ? "* {" + this.inlineStyle + "}" : "");
            }

            public int hashCode() {
                int hash = (17 * 3) + Arrays.hashCode(this.key);
                if (this.inlineStyle != null) {
                    hash = (17 * hash) + this.inlineStyle.hashCode();
                }
                return hash;
            }

            public boolean equals(Object obj) {
                if (obj == null || getClass() != obj.getClass()) {
                    return false;
                }
                Key other = (Key) obj;
                if (this.inlineStyle == null) {
                    if (other.inlineStyle != null) {
                        return false;
                    }
                } else if (!this.inlineStyle.equals(other.inlineStyle)) {
                    return false;
                }
                if (!Arrays.equals(this.key, other.key)) {
                    return false;
                }
                return true;
            }
        }

        Cache(List<Selector> selectors) {
            this.selectors = selectors;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public StyleMap getStyleMap(CacheContainer cacheContainer, Node node, Set<PseudoClass>[] triggerStates, boolean hasInlineStyle) {
            Selector selector;
            if ((this.selectors == null || this.selectors.isEmpty()) && !hasInlineStyle) {
                return StyleMap.EMPTY_MAP;
            }
            int selectorDataSize = this.selectors.size();
            long[] key = new long[(selectorDataSize / 64) + 1];
            boolean nothingMatched = true;
            for (int s2 = 0; s2 < selectorDataSize; s2++) {
                Selector sel = this.selectors.get(s2);
                if (sel.applies(node, triggerStates, 0)) {
                    int index = s2 / 64;
                    key[index] = key[index] | (1 << s2);
                    nothingMatched = false;
                }
            }
            if (nothingMatched && !hasInlineStyle) {
                return StyleMap.EMPTY_MAP;
            }
            String inlineStyle = node.getStyle();
            Key keyObj = new Key(key, inlineStyle);
            if (this.cache.containsKey(keyObj)) {
                Integer styleMapId = this.cache.get(keyObj);
                return styleMapId != null ? cacheContainer.getStyleMap(styleMapId.intValue()) : StyleMap.EMPTY_MAP;
            }
            List<Selector> selectors = new ArrayList<>();
            if (hasInlineStyle && (selector = cacheContainer.getInlineStyleSelector(inlineStyle)) != null) {
                selectors.add(selector);
            }
            for (int k2 = 0; k2 < key.length; k2++) {
                if (key[k2] != 0) {
                    int offset = k2 * 64;
                    for (int b2 = 0; b2 < 64; b2++) {
                        long mask = 1 << b2;
                        if ((mask & key[k2]) == mask) {
                            Selector pair = this.selectors.get(offset + b2);
                            selectors.add(pair);
                        }
                    }
                }
            }
            int id = cacheContainer.nextSmapId();
            this.cache.put(keyObj, Integer.valueOf(id));
            StyleMap styleMap = new StyleMap(id, selectors);
            cacheContainer.addStyleMap(styleMap);
            return styleMap;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/css/StyleManager$Key.class */
    private static class Key {
        String className;
        String id;
        final StyleClassSet styleClasses;

        private Key() {
            this.styleClasses = new StyleClassSet();
        }

        public boolean equals(Object o2) {
            if (this != o2 && (o2 instanceof Key)) {
                Key other = (Key) o2;
                if (this.className == null) {
                    if (other.className != null) {
                        return false;
                    }
                } else if (!this.className.equals(other.className)) {
                    return false;
                }
                if (this.id == null) {
                    if (other.id != null) {
                        return false;
                    }
                } else if (!this.id.equals(other.id)) {
                    return false;
                }
                return this.styleClasses.equals(other.styleClasses);
            }
            return true;
        }

        public int hashCode() {
            int hash = (29 * 7) + (this.className != null ? this.className.hashCode() : 0);
            return (29 * ((29 * hash) + (this.id != null ? this.id.hashCode() : 0))) + this.styleClasses.hashCode();
        }
    }
}
