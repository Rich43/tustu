package javax.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.swing.border.Border;
import javax.swing.event.SwingPropertyChangeSupport;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.ComponentUI;
import org.apache.commons.math3.geometry.VectorFormat;
import sun.font.Font2D;
import sun.reflect.misc.MethodUtil;
import sun.reflect.misc.ReflectUtil;
import sun.util.CoreResourceBundleControl;

/* loaded from: rt.jar:javax/swing/UIDefaults.class */
public class UIDefaults extends Hashtable<Object, Object> {
    private static final Object PENDING = new Object();
    private SwingPropertyChangeSupport changeSupport;
    private Vector<String> resourceBundles;
    private Locale defaultLocale;
    private Map<Locale, Map<String, Object>> resourceCache;

    /* loaded from: rt.jar:javax/swing/UIDefaults$ActiveValue.class */
    public interface ActiveValue {
        Object createValue(UIDefaults uIDefaults);
    }

    /* loaded from: rt.jar:javax/swing/UIDefaults$LazyValue.class */
    public interface LazyValue {
        Object createValue(UIDefaults uIDefaults);
    }

    public UIDefaults() {
        this(Font2D.FWEIGHT_BOLD, 0.75f);
    }

    public UIDefaults(int i2, float f2) {
        super(i2, f2);
        this.defaultLocale = Locale.getDefault();
        this.resourceCache = new HashMap();
    }

    public UIDefaults(Object[] objArr) {
        super(objArr.length / 2);
        this.defaultLocale = Locale.getDefault();
        for (int i2 = 0; i2 < objArr.length; i2 += 2) {
            super.put(objArr[i2], objArr[i2 + 1]);
        }
    }

    @Override // java.util.Hashtable, java.util.Dictionary
    public Object get(Object obj) {
        Object fromHashtable = getFromHashtable(obj);
        return fromHashtable != null ? fromHashtable : getFromResourceBundle(obj, null);
    }

    private Object getFromHashtable(Object obj) {
        Object obj2;
        Object obj3 = super.get(obj);
        if (obj3 != PENDING && !(obj3 instanceof ActiveValue) && !(obj3 instanceof LazyValue)) {
            return obj3;
        }
        synchronized (this) {
            Object objCreateValue = super.get(obj);
            if (objCreateValue == PENDING) {
                do {
                    try {
                        wait();
                    } catch (InterruptedException e2) {
                    }
                    obj2 = super.get(obj);
                } while (obj2 == PENDING);
                return obj2;
            }
            if (objCreateValue instanceof LazyValue) {
                super.put(obj, PENDING);
            } else if (!(objCreateValue instanceof ActiveValue)) {
                return objCreateValue;
            }
            if (objCreateValue instanceof LazyValue) {
                try {
                    objCreateValue = ((LazyValue) objCreateValue).createValue(this);
                    synchronized (this) {
                        if (objCreateValue == null) {
                            super.remove(obj);
                        } else {
                            super.put(obj, objCreateValue);
                        }
                        notifyAll();
                    }
                } catch (Throwable th) {
                    synchronized (this) {
                        if (objCreateValue == null) {
                            super.remove(obj);
                        } else {
                            super.put(obj, objCreateValue);
                        }
                        notifyAll();
                        throw th;
                    }
                }
            } else {
                objCreateValue = ((ActiveValue) objCreateValue).createValue(this);
            }
            return objCreateValue;
        }
    }

    public Object get(Object obj, Locale locale) {
        Object fromHashtable = getFromHashtable(obj);
        return fromHashtable != null ? fromHashtable : getFromResourceBundle(obj, locale);
    }

    private Object getFromResourceBundle(Object obj, Locale locale) {
        Object obj2;
        if (this.resourceBundles == null || this.resourceBundles.isEmpty() || !(obj instanceof String)) {
            return null;
        }
        if (locale == null) {
            if (this.defaultLocale == null) {
                return null;
            }
            locale = this.defaultLocale;
        }
        synchronized (this) {
            obj2 = getResourceCache(locale).get(obj);
        }
        return obj2;
    }

    private Map<String, Object> getResourceCache(Locale locale) {
        ResourceBundle bundle;
        Map<String, Object> textAndMnemonicHashMap = this.resourceCache.get(locale);
        if (textAndMnemonicHashMap == null) {
            textAndMnemonicHashMap = new TextAndMnemonicHashMap();
            for (int size = this.resourceBundles.size() - 1; size >= 0; size--) {
                String str = this.resourceBundles.get(size);
                try {
                    CoreResourceBundleControl rBControlInstance = CoreResourceBundleControl.getRBControlInstance(str);
                    if (rBControlInstance != null) {
                        bundle = ResourceBundle.getBundle(str, locale, rBControlInstance);
                    } else {
                        bundle = ResourceBundle.getBundle(str, locale, ClassLoader.getSystemClassLoader());
                    }
                    Enumeration<String> keys = bundle.getKeys();
                    while (keys.hasMoreElements()) {
                        String strNextElement = keys.nextElement();
                        if (textAndMnemonicHashMap.get(strNextElement) == null) {
                            textAndMnemonicHashMap.put(strNextElement, bundle.getObject(strNextElement));
                        }
                    }
                } catch (MissingResourceException e2) {
                }
            }
            this.resourceCache.put(locale, textAndMnemonicHashMap);
        }
        return textAndMnemonicHashMap;
    }

    @Override // java.util.Hashtable, java.util.Dictionary
    public Object put(Object obj, Object obj2) {
        Object objRemove = obj2 == null ? super.remove(obj) : super.put(obj, obj2);
        if (obj instanceof String) {
            firePropertyChange((String) obj, objRemove, obj2);
        }
        return objRemove;
    }

    public void putDefaults(Object[] objArr) {
        int length = objArr.length;
        for (int i2 = 0; i2 < length; i2 += 2) {
            Object obj = objArr[i2 + 1];
            if (obj == null) {
                super.remove(objArr[i2]);
            } else {
                super.put(objArr[i2], obj);
            }
        }
        firePropertyChange("UIDefaults", null, null);
    }

    public Font getFont(Object obj) {
        Object obj2 = get(obj);
        if (obj2 instanceof Font) {
            return (Font) obj2;
        }
        return null;
    }

    public Font getFont(Object obj, Locale locale) {
        Object obj2 = get(obj, locale);
        if (obj2 instanceof Font) {
            return (Font) obj2;
        }
        return null;
    }

    public Color getColor(Object obj) {
        Object obj2 = get(obj);
        if (obj2 instanceof Color) {
            return (Color) obj2;
        }
        return null;
    }

    public Color getColor(Object obj, Locale locale) {
        Object obj2 = get(obj, locale);
        if (obj2 instanceof Color) {
            return (Color) obj2;
        }
        return null;
    }

    public Icon getIcon(Object obj) {
        Object obj2 = get(obj);
        if (obj2 instanceof Icon) {
            return (Icon) obj2;
        }
        return null;
    }

    public Icon getIcon(Object obj, Locale locale) {
        Object obj2 = get(obj, locale);
        if (obj2 instanceof Icon) {
            return (Icon) obj2;
        }
        return null;
    }

    public Border getBorder(Object obj) {
        Object obj2 = get(obj);
        if (obj2 instanceof Border) {
            return (Border) obj2;
        }
        return null;
    }

    public Border getBorder(Object obj, Locale locale) {
        Object obj2 = get(obj, locale);
        if (obj2 instanceof Border) {
            return (Border) obj2;
        }
        return null;
    }

    public String getString(Object obj) {
        Object obj2 = get(obj);
        if (obj2 instanceof String) {
            return (String) obj2;
        }
        return null;
    }

    public String getString(Object obj, Locale locale) {
        Object obj2 = get(obj, locale);
        if (obj2 instanceof String) {
            return (String) obj2;
        }
        return null;
    }

    public int getInt(Object obj) {
        Object obj2 = get(obj);
        if (obj2 instanceof Integer) {
            return ((Integer) obj2).intValue();
        }
        return 0;
    }

    public int getInt(Object obj, Locale locale) {
        Object obj2 = get(obj, locale);
        if (obj2 instanceof Integer) {
            return ((Integer) obj2).intValue();
        }
        return 0;
    }

    public boolean getBoolean(Object obj) {
        Object obj2 = get(obj);
        if (obj2 instanceof Boolean) {
            return ((Boolean) obj2).booleanValue();
        }
        return false;
    }

    public boolean getBoolean(Object obj, Locale locale) {
        Object obj2 = get(obj, locale);
        if (obj2 instanceof Boolean) {
            return ((Boolean) obj2).booleanValue();
        }
        return false;
    }

    public Insets getInsets(Object obj) {
        Object obj2 = get(obj);
        if (obj2 instanceof Insets) {
            return (Insets) obj2;
        }
        return null;
    }

    public Insets getInsets(Object obj, Locale locale) {
        Object obj2 = get(obj, locale);
        if (obj2 instanceof Insets) {
            return (Insets) obj2;
        }
        return null;
    }

    public Dimension getDimension(Object obj) {
        Object obj2 = get(obj);
        if (obj2 instanceof Dimension) {
            return (Dimension) obj2;
        }
        return null;
    }

    public Dimension getDimension(Object obj, Locale locale) {
        Object obj2 = get(obj, locale);
        if (obj2 instanceof Dimension) {
            return (Dimension) obj2;
        }
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public Class<? extends ComponentUI> getUIClass(String str, ClassLoader classLoader) {
        try {
            String str2 = (String) get(str);
            if (str2 != null) {
                ReflectUtil.checkPackageAccess(str2);
                Class<?> clsLoadClass = (Class) get(str2);
                if (clsLoadClass == null) {
                    if (classLoader == null) {
                        clsLoadClass = SwingUtilities.loadSystemClass(str2);
                    } else {
                        clsLoadClass = classLoader.loadClass(str2);
                    }
                    if (clsLoadClass != null) {
                        put(str2, clsLoadClass);
                    }
                }
                return clsLoadClass;
            }
            return null;
        } catch (ClassCastException e2) {
            return null;
        } catch (ClassNotFoundException e3) {
            return null;
        }
    }

    public Class<? extends ComponentUI> getUIClass(String str) {
        return getUIClass(str, null);
    }

    protected void getUIError(String str) {
        System.err.println("UIDefaults.getUI() failed: " + str);
        try {
            throw new Error();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public ComponentUI getUI(JComponent jComponent) {
        Object obj = get("ClassLoader");
        Class<? extends ComponentUI> uIClass = getUIClass(jComponent.getUIClassID(), obj != null ? (ClassLoader) obj : jComponent.getClass().getClassLoader());
        Object objInvoke = null;
        if (uIClass == null) {
            getUIError("no ComponentUI class for: " + ((Object) jComponent));
        } else {
            try {
                Method method = (Method) get(uIClass);
                if (method == null) {
                    method = uIClass.getMethod("createUI", JComponent.class);
                    put(uIClass, method);
                }
                objInvoke = MethodUtil.invoke(method, null, new Object[]{jComponent});
            } catch (NoSuchMethodException e2) {
                getUIError("static createUI() method not found in " + ((Object) uIClass));
            } catch (Exception e3) {
                getUIError("createUI() failed for " + ((Object) jComponent) + " " + ((Object) e3));
            }
        }
        return (ComponentUI) objInvoke;
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        if (this.changeSupport == null) {
            this.changeSupport = new SwingPropertyChangeSupport(this);
        }
        this.changeSupport.addPropertyChangeListener(propertyChangeListener);
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        if (this.changeSupport != null) {
            this.changeSupport.removePropertyChangeListener(propertyChangeListener);
        }
    }

    public synchronized PropertyChangeListener[] getPropertyChangeListeners() {
        if (this.changeSupport == null) {
            return new PropertyChangeListener[0];
        }
        return this.changeSupport.getPropertyChangeListeners();
    }

    protected void firePropertyChange(String str, Object obj, Object obj2) {
        if (this.changeSupport != null) {
            this.changeSupport.firePropertyChange(str, obj, obj2);
        }
    }

    public synchronized void addResourceBundle(String str) {
        if (str == null) {
            return;
        }
        if (this.resourceBundles == null) {
            this.resourceBundles = new Vector<>(5);
        }
        if (!this.resourceBundles.contains(str)) {
            this.resourceBundles.add(str);
            this.resourceCache.clear();
        }
    }

    public synchronized void removeResourceBundle(String str) {
        if (this.resourceBundles != null) {
            this.resourceBundles.remove(str);
        }
        this.resourceCache.clear();
    }

    public void setDefaultLocale(Locale locale) {
        this.defaultLocale = locale;
    }

    public Locale getDefaultLocale() {
        return this.defaultLocale;
    }

    /* loaded from: rt.jar:javax/swing/UIDefaults$ProxyLazyValue.class */
    public static class ProxyLazyValue implements LazyValue {
        private AccessControlContext acc;
        private String className;
        private String methodName;
        private Object[] args;

        public ProxyLazyValue(String str) {
            this(str, (String) null);
        }

        public ProxyLazyValue(String str, String str2) {
            this(str, str2, null);
        }

        public ProxyLazyValue(String str, Object[] objArr) {
            this(str, null, objArr);
        }

        public ProxyLazyValue(String str, String str2, Object[] objArr) {
            this.acc = AccessController.getContext();
            this.className = str;
            this.methodName = str2;
            if (objArr != null) {
                this.args = (Object[]) objArr.clone();
            }
        }

        @Override // javax.swing.UIDefaults.LazyValue
        public Object createValue(final UIDefaults uIDefaults) {
            if (this.acc == null && System.getSecurityManager() != null) {
                throw new SecurityException("null AccessControlContext");
            }
            return AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: javax.swing.UIDefaults.ProxyLazyValue.1
                /* JADX WARN: Removed duplicated region for block: B:6:0x0018 A[Catch: Exception -> 0x00a8, TryCatch #0 {Exception -> 0x00a8, blocks: (B:2:0x0000, B:4:0x0007, B:9:0x0027, B:11:0x0052, B:13:0x007d, B:6:0x0018, B:8:0x0023), top: B:18:0x0000 }] */
                @Override // java.security.PrivilegedAction
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                    To view partially-correct code enable 'Show inconsistent code' option in preferences
                */
                public java.lang.Object run() {
                    /*
                        r4 = this;
                        r0 = r4
                        javax.swing.UIDefaults r0 = r5     // Catch: java.lang.Exception -> La8
                        if (r0 == 0) goto L18
                        r0 = r4
                        javax.swing.UIDefaults r0 = r5     // Catch: java.lang.Exception -> La8
                        java.lang.String r1 = "ClassLoader"
                        java.lang.Object r0 = r0.get(r1)     // Catch: java.lang.Exception -> La8
                        r1 = r0
                        r6 = r1
                        boolean r0 = r0 instanceof java.lang.ClassLoader     // Catch: java.lang.Exception -> La8
                        if (r0 != 0) goto L27
                    L18:
                        java.lang.Thread r0 = java.lang.Thread.currentThread()     // Catch: java.lang.Exception -> La8
                        java.lang.ClassLoader r0 = r0.getContextClassLoader()     // Catch: java.lang.Exception -> La8
                        r6 = r0
                        r0 = r6
                        if (r0 != 0) goto L27
                        java.lang.ClassLoader r0 = java.lang.ClassLoader.getSystemClassLoader()     // Catch: java.lang.Exception -> La8
                        r6 = r0
                    L27:
                        r0 = r4
                        javax.swing.UIDefaults$ProxyLazyValue r0 = javax.swing.UIDefaults.ProxyLazyValue.this     // Catch: java.lang.Exception -> La8
                        java.lang.String r0 = javax.swing.UIDefaults.ProxyLazyValue.access$100(r0)     // Catch: java.lang.Exception -> La8
                        sun.reflect.misc.ReflectUtil.checkPackageAccess(r0)     // Catch: java.lang.Exception -> La8
                        r0 = r4
                        javax.swing.UIDefaults$ProxyLazyValue r0 = javax.swing.UIDefaults.ProxyLazyValue.this     // Catch: java.lang.Exception -> La8
                        java.lang.String r0 = javax.swing.UIDefaults.ProxyLazyValue.access$100(r0)     // Catch: java.lang.Exception -> La8
                        r1 = 1
                        r2 = r6
                        java.lang.ClassLoader r2 = (java.lang.ClassLoader) r2     // Catch: java.lang.Exception -> La8
                        java.lang.Class r0 = java.lang.Class.forName(r0, r1, r2)     // Catch: java.lang.Exception -> La8
                        r5 = r0
                        r0 = r5
                        int r0 = r0.getModifiers()     // Catch: java.lang.Exception -> La8
                        sun.swing.SwingUtilities2.checkAccess(r0)     // Catch: java.lang.Exception -> La8
                        r0 = r4
                        javax.swing.UIDefaults$ProxyLazyValue r0 = javax.swing.UIDefaults.ProxyLazyValue.this     // Catch: java.lang.Exception -> La8
                        java.lang.String r0 = javax.swing.UIDefaults.ProxyLazyValue.access$200(r0)     // Catch: java.lang.Exception -> La8
                        if (r0 == 0) goto L7d
                        r0 = r4
                        javax.swing.UIDefaults$ProxyLazyValue r0 = javax.swing.UIDefaults.ProxyLazyValue.this     // Catch: java.lang.Exception -> La8
                        r1 = r4
                        javax.swing.UIDefaults$ProxyLazyValue r1 = javax.swing.UIDefaults.ProxyLazyValue.this     // Catch: java.lang.Exception -> La8
                        java.lang.Object[] r1 = javax.swing.UIDefaults.ProxyLazyValue.access$300(r1)     // Catch: java.lang.Exception -> La8
                        java.lang.Class[] r0 = javax.swing.UIDefaults.ProxyLazyValue.access$400(r0, r1)     // Catch: java.lang.Exception -> La8
                        r7 = r0
                        r0 = r5
                        r1 = r4
                        javax.swing.UIDefaults$ProxyLazyValue r1 = javax.swing.UIDefaults.ProxyLazyValue.this     // Catch: java.lang.Exception -> La8
                        java.lang.String r1 = javax.swing.UIDefaults.ProxyLazyValue.access$200(r1)     // Catch: java.lang.Exception -> La8
                        r2 = r7
                        java.lang.reflect.Method r0 = r0.getMethod(r1, r2)     // Catch: java.lang.Exception -> La8
                        r8 = r0
                        r0 = r8
                        r1 = r5
                        r2 = r4
                        javax.swing.UIDefaults$ProxyLazyValue r2 = javax.swing.UIDefaults.ProxyLazyValue.this     // Catch: java.lang.Exception -> La8
                        java.lang.Object[] r2 = javax.swing.UIDefaults.ProxyLazyValue.access$300(r2)     // Catch: java.lang.Exception -> La8
                        java.lang.Object r0 = sun.reflect.misc.MethodUtil.invoke(r0, r1, r2)     // Catch: java.lang.Exception -> La8
                        return r0
                    L7d:
                        r0 = r4
                        javax.swing.UIDefaults$ProxyLazyValue r0 = javax.swing.UIDefaults.ProxyLazyValue.this     // Catch: java.lang.Exception -> La8
                        r1 = r4
                        javax.swing.UIDefaults$ProxyLazyValue r1 = javax.swing.UIDefaults.ProxyLazyValue.this     // Catch: java.lang.Exception -> La8
                        java.lang.Object[] r1 = javax.swing.UIDefaults.ProxyLazyValue.access$300(r1)     // Catch: java.lang.Exception -> La8
                        java.lang.Class[] r0 = javax.swing.UIDefaults.ProxyLazyValue.access$400(r0, r1)     // Catch: java.lang.Exception -> La8
                        r7 = r0
                        r0 = r5
                        r1 = r7
                        java.lang.reflect.Constructor r0 = r0.getConstructor(r1)     // Catch: java.lang.Exception -> La8
                        r8 = r0
                        r0 = r8
                        int r0 = r0.getModifiers()     // Catch: java.lang.Exception -> La8
                        sun.swing.SwingUtilities2.checkAccess(r0)     // Catch: java.lang.Exception -> La8
                        r0 = r8
                        r1 = r4
                        javax.swing.UIDefaults$ProxyLazyValue r1 = javax.swing.UIDefaults.ProxyLazyValue.this     // Catch: java.lang.Exception -> La8
                        java.lang.Object[] r1 = javax.swing.UIDefaults.ProxyLazyValue.access$300(r1)     // Catch: java.lang.Exception -> La8
                        java.lang.Object r0 = r0.newInstance(r1)     // Catch: java.lang.Exception -> La8
                        return r0
                    La8:
                        r5 = move-exception
                        r0 = 0
                        return r0
                    */
                    throw new UnsupportedOperationException("Method not decompiled: javax.swing.UIDefaults.ProxyLazyValue.AnonymousClass1.run():java.lang.Object");
                }
            }, this.acc);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Class[] getClassArray(Object[] objArr) {
            Class[] clsArr = null;
            if (objArr != null) {
                clsArr = new Class[objArr.length];
                for (int i2 = 0; i2 < objArr.length; i2++) {
                    if (objArr[i2] instanceof Integer) {
                        clsArr[i2] = Integer.TYPE;
                    } else if (objArr[i2] instanceof Boolean) {
                        clsArr[i2] = Boolean.TYPE;
                    } else if (objArr[i2] instanceof ColorUIResource) {
                        clsArr[i2] = Color.class;
                    } else {
                        clsArr[i2] = objArr[i2].getClass();
                    }
                }
            }
            return clsArr;
        }

        private String printArgs(Object[] objArr) {
            String strConcat;
            String strConcat2 = VectorFormat.DEFAULT_PREFIX;
            if (objArr != null) {
                for (int i2 = 0; i2 < objArr.length - 1; i2++) {
                    strConcat2 = strConcat2.concat(objArr[i2] + ",");
                }
                strConcat = strConcat2.concat(objArr[objArr.length - 1] + "}");
            } else {
                strConcat = strConcat2.concat("}");
            }
            return strConcat;
        }
    }

    /* loaded from: rt.jar:javax/swing/UIDefaults$LazyInputMap.class */
    public static class LazyInputMap implements LazyValue {
        private Object[] bindings;

        public LazyInputMap(Object[] objArr) {
            this.bindings = objArr;
        }

        @Override // javax.swing.UIDefaults.LazyValue
        public Object createValue(UIDefaults uIDefaults) {
            if (this.bindings != null) {
                return LookAndFeel.makeInputMap(this.bindings);
            }
            return null;
        }
    }

    /* loaded from: rt.jar:javax/swing/UIDefaults$TextAndMnemonicHashMap.class */
    private static class TextAndMnemonicHashMap extends HashMap<String, Object> {
        static final String AND_MNEMONIC = "AndMnemonic";
        static final String TITLE_SUFFIX = ".titleAndMnemonic";
        static final String TEXT_SUFFIX = ".textAndMnemonic";

        private TextAndMnemonicHashMap() {
        }

        @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
        public Object get(Object obj) {
            Object obj2 = super.get(obj);
            if (obj2 == null) {
                boolean z2 = false;
                String string = obj.toString();
                String strComposeKey = null;
                if (string.endsWith(AND_MNEMONIC)) {
                    return null;
                }
                if (string.endsWith(".mnemonic")) {
                    strComposeKey = composeKey(string, 9, TEXT_SUFFIX);
                } else if (string.endsWith("NameMnemonic")) {
                    strComposeKey = composeKey(string, 12, TEXT_SUFFIX);
                } else if (string.endsWith("Mnemonic")) {
                    strComposeKey = composeKey(string, 8, TEXT_SUFFIX);
                    z2 = true;
                }
                if (strComposeKey != null) {
                    Object obj3 = super.get(strComposeKey);
                    if (obj3 == null && z2) {
                        obj3 = super.get(composeKey(string, 8, TITLE_SUFFIX));
                    }
                    if (obj3 == null) {
                        return null;
                    }
                    return getMnemonicFromProperty(obj3.toString());
                }
                if (string.endsWith("NameText")) {
                    strComposeKey = composeKey(string, 8, TEXT_SUFFIX);
                } else if (string.endsWith(".nameText")) {
                    strComposeKey = composeKey(string, 9, TEXT_SUFFIX);
                } else if (string.endsWith("Text")) {
                    strComposeKey = composeKey(string, 4, TEXT_SUFFIX);
                } else if (string.endsWith("Title")) {
                    strComposeKey = composeKey(string, 5, TITLE_SUFFIX);
                }
                if (strComposeKey != null) {
                    Object obj4 = super.get(strComposeKey);
                    if (obj4 == null) {
                        return null;
                    }
                    return getTextFromProperty(obj4.toString());
                }
                if (string.endsWith("DisplayedMnemonicIndex")) {
                    Object obj5 = super.get(composeKey(string, 22, TEXT_SUFFIX));
                    if (obj5 == null) {
                        obj5 = super.get(composeKey(string, 22, TITLE_SUFFIX));
                    }
                    if (obj5 == null) {
                        return null;
                    }
                    return getIndexFromProperty(obj5.toString());
                }
            }
            return obj2;
        }

        String composeKey(String str, int i2, String str2) {
            return str.substring(0, str.length() - i2) + str2;
        }

        String getTextFromProperty(String str) {
            return str.replace("&", "");
        }

        String getMnemonicFromProperty(String str) {
            int iIndexOf = str.indexOf(38);
            if (0 <= iIndexOf && iIndexOf < str.length() - 1) {
                return Integer.toString(Character.toUpperCase(str.charAt(iIndexOf + 1)));
            }
            return null;
        }

        String getIndexFromProperty(String str) {
            int iIndexOf = str.indexOf(38);
            if (iIndexOf == -1) {
                return null;
            }
            return Integer.toString(iIndexOf);
        }
    }
}
