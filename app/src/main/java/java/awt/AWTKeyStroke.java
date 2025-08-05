package java.awt;

import java.awt.event.KeyEvent;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import sun.awt.AppContext;

/* loaded from: rt.jar:java/awt/AWTKeyStroke.class */
public class AWTKeyStroke implements Serializable {
    static final long serialVersionUID = -6430539691155161871L;
    private static Map<String, Integer> modifierKeywords;
    private static VKCollection vks;
    private static Object APP_CONTEXT_CACHE_KEY;
    private static AWTKeyStroke APP_CONTEXT_KEYSTROKE_KEY;
    private char keyChar;
    private int keyCode;
    private int modifiers;
    private boolean onKeyRelease;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !AWTKeyStroke.class.desiredAssertionStatus();
        APP_CONTEXT_CACHE_KEY = new Object();
        APP_CONTEXT_KEYSTROKE_KEY = new AWTKeyStroke();
        Toolkit.loadLibraries();
    }

    private static Class<AWTKeyStroke> getAWTKeyStrokeClass() {
        Class<AWTKeyStroke> cls = (Class) AppContext.getAppContext().get(AWTKeyStroke.class);
        if (cls == null) {
            cls = AWTKeyStroke.class;
            AppContext.getAppContext().put(AWTKeyStroke.class, AWTKeyStroke.class);
        }
        return cls;
    }

    protected AWTKeyStroke() {
        this.keyChar = (char) 65535;
        this.keyCode = 0;
    }

    protected AWTKeyStroke(char c2, int i2, int i3, boolean z2) {
        this.keyChar = (char) 65535;
        this.keyCode = 0;
        this.keyChar = c2;
        this.keyCode = i2;
        this.modifiers = i3;
        this.onKeyRelease = z2;
    }

    protected static void registerSubclass(Class<?> cls) {
        if (cls == null) {
            throw new IllegalArgumentException("subclass cannot be null");
        }
        synchronized (AWTKeyStroke.class) {
            Class cls2 = (Class) AppContext.getAppContext().get(AWTKeyStroke.class);
            if (cls2 == null || !cls2.equals(cls)) {
                if (!AWTKeyStroke.class.isAssignableFrom(cls)) {
                    throw new ClassCastException("subclass is not derived from AWTKeyStroke");
                }
                Constructor ctor = getCtor(cls);
                if (ctor == null) {
                    throw new IllegalArgumentException("subclass could not be instantiated");
                }
                try {
                    if (((AWTKeyStroke) ctor.newInstance((Object[]) null)) == null) {
                        throw new IllegalArgumentException("subclass could not be instantiated");
                    }
                    synchronized (AWTKeyStroke.class) {
                        AppContext.getAppContext().put(AWTKeyStroke.class, cls);
                        AppContext.getAppContext().remove(APP_CONTEXT_CACHE_KEY);
                        AppContext.getAppContext().remove(APP_CONTEXT_KEYSTROKE_KEY);
                    }
                } catch (ExceptionInInitializerError e2) {
                    throw new IllegalArgumentException("subclass could not be instantiated");
                } catch (IllegalAccessException e3) {
                    throw new IllegalArgumentException("subclass could not be instantiated");
                } catch (InstantiationException e4) {
                    throw new IllegalArgumentException("subclass could not be instantiated");
                } catch (NoSuchMethodError e5) {
                    throw new IllegalArgumentException("subclass could not be instantiated");
                } catch (InvocationTargetException e6) {
                    throw new IllegalArgumentException("subclass could not be instantiated");
                }
            }
        }
    }

    private static Constructor getCtor(final Class cls) {
        return (Constructor) AccessController.doPrivileged(new PrivilegedAction<Constructor>() { // from class: java.awt.AWTKeyStroke.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Constructor run2() {
                try {
                    Constructor declaredConstructor = cls.getDeclaredConstructor((Class[]) null);
                    if (declaredConstructor != null) {
                        declaredConstructor.setAccessible(true);
                    }
                    return declaredConstructor;
                } catch (NoSuchMethodException | SecurityException e2) {
                    return null;
                }
            }
        });
    }

    private static synchronized AWTKeyStroke getCachedStroke(char c2, int i2, int i3, boolean z2) {
        Map map = (Map) AppContext.getAppContext().get(APP_CONTEXT_CACHE_KEY);
        AWTKeyStroke aWTKeyStroke = (AWTKeyStroke) AppContext.getAppContext().get(APP_CONTEXT_KEYSTROKE_KEY);
        if (map == null) {
            map = new HashMap();
            AppContext.getAppContext().put(APP_CONTEXT_CACHE_KEY, map);
        }
        if (aWTKeyStroke == null) {
            try {
                aWTKeyStroke = (AWTKeyStroke) getCtor(getAWTKeyStrokeClass()).newInstance((Object[]) null);
                AppContext.getAppContext().put(APP_CONTEXT_KEYSTROKE_KEY, aWTKeyStroke);
            } catch (IllegalAccessException e2) {
                if (!$assertionsDisabled) {
                    throw new AssertionError();
                }
            } catch (InstantiationException e3) {
                if (!$assertionsDisabled) {
                    throw new AssertionError();
                }
            } catch (InvocationTargetException e4) {
                if (!$assertionsDisabled) {
                    throw new AssertionError();
                }
            }
        }
        aWTKeyStroke.keyChar = c2;
        aWTKeyStroke.keyCode = i2;
        aWTKeyStroke.modifiers = mapNewModifiers(mapOldModifiers(i3));
        aWTKeyStroke.onKeyRelease = z2;
        AWTKeyStroke aWTKeyStroke2 = (AWTKeyStroke) map.get(aWTKeyStroke);
        if (aWTKeyStroke2 == null) {
            aWTKeyStroke2 = aWTKeyStroke;
            map.put(aWTKeyStroke2, aWTKeyStroke2);
            AppContext.getAppContext().remove(APP_CONTEXT_KEYSTROKE_KEY);
        }
        return aWTKeyStroke2;
    }

    public static AWTKeyStroke getAWTKeyStroke(char c2) {
        return getCachedStroke(c2, 0, 0, false);
    }

    public static AWTKeyStroke getAWTKeyStroke(Character ch, int i2) {
        if (ch == null) {
            throw new IllegalArgumentException("keyChar cannot be null");
        }
        return getCachedStroke(ch.charValue(), 0, i2, false);
    }

    public static AWTKeyStroke getAWTKeyStroke(int i2, int i3, boolean z2) {
        return getCachedStroke((char) 65535, i2, i3, z2);
    }

    public static AWTKeyStroke getAWTKeyStroke(int i2, int i3) {
        return getCachedStroke((char) 65535, i2, i3, false);
    }

    public static AWTKeyStroke getAWTKeyStrokeForEvent(KeyEvent keyEvent) {
        int id = keyEvent.getID();
        switch (id) {
            case 400:
                return getCachedStroke(keyEvent.getKeyChar(), 0, keyEvent.getModifiers(), false);
            case 401:
            case 402:
                return getCachedStroke((char) 65535, keyEvent.getKeyCode(), keyEvent.getModifiers(), id == 402);
            default:
                return null;
        }
    }

    public static AWTKeyStroke getAWTKeyStroke(String str) {
        if (str == null) {
            throw new IllegalArgumentException("String cannot be null");
        }
        StringTokenizer stringTokenizer = new StringTokenizer(str, " ");
        int iIntValue = 0;
        boolean z2 = false;
        boolean z3 = false;
        boolean z4 = false;
        synchronized (AWTKeyStroke.class) {
            if (modifierKeywords == null) {
                HashMap map = new HashMap(8, 1.0f);
                map.put("shift", 65);
                map.put("control", 130);
                map.put("ctrl", 130);
                map.put("meta", 260);
                map.put("alt", 520);
                map.put("altGraph", 8224);
                map.put("button1", 1024);
                map.put("button2", 2048);
                map.put("button3", 4096);
                modifierKeywords = Collections.synchronizedMap(map);
            }
        }
        int iCountTokens = stringTokenizer.countTokens();
        for (int i2 = 1; i2 <= iCountTokens; i2++) {
            String strNextToken = stringTokenizer.nextToken();
            if (z3) {
                if (strNextToken.length() != 1 || i2 != iCountTokens) {
                    throw new IllegalArgumentException("String formatted incorrectly");
                }
                return getCachedStroke(strNextToken.charAt(0), 0, iIntValue, false);
            }
            if (z4 || z2 || i2 == iCountTokens) {
                if (i2 != iCountTokens) {
                    throw new IllegalArgumentException("String formatted incorrectly");
                }
                return getCachedStroke((char) 65535, getVKValue("VK_" + strNextToken), iIntValue, z2);
            }
            if (strNextToken.equals("released")) {
                z2 = true;
            } else if (strNextToken.equals("pressed")) {
                z4 = true;
            } else if (strNextToken.equals("typed")) {
                z3 = true;
            } else {
                Integer num = modifierKeywords.get(strNextToken);
                if (num != null) {
                    iIntValue |= num.intValue();
                } else {
                    throw new IllegalArgumentException("String formatted incorrectly");
                }
            }
        }
        throw new IllegalArgumentException("String formatted incorrectly");
    }

    private static VKCollection getVKCollection() {
        if (vks == null) {
            vks = new VKCollection();
        }
        return vks;
    }

    private static int getVKValue(String str) {
        VKCollection vKCollection = getVKCollection();
        Integer numFindCode = vKCollection.findCode(str);
        if (numFindCode == null) {
            try {
                numFindCode = Integer.valueOf(KeyEvent.class.getField(str).getInt(KeyEvent.class));
                vKCollection.put(str, numFindCode);
            } catch (IllegalAccessException e2) {
                throw new IllegalArgumentException("String formatted incorrectly");
            } catch (NoSuchFieldException e3) {
                throw new IllegalArgumentException("String formatted incorrectly");
            }
        }
        return numFindCode.intValue();
    }

    public final char getKeyChar() {
        return this.keyChar;
    }

    public final int getKeyCode() {
        return this.keyCode;
    }

    public final int getModifiers() {
        return this.modifiers;
    }

    public final boolean isOnKeyRelease() {
        return this.onKeyRelease;
    }

    public final int getKeyEventType() {
        if (this.keyCode == 0) {
            return 400;
        }
        return this.onKeyRelease ? 402 : 401;
    }

    public int hashCode() {
        return ((this.keyChar + 1) * 2 * (this.keyCode + 1) * (this.modifiers + 1)) + (this.onKeyRelease ? 1 : 2);
    }

    public final boolean equals(Object obj) {
        if (obj instanceof AWTKeyStroke) {
            AWTKeyStroke aWTKeyStroke = (AWTKeyStroke) obj;
            return aWTKeyStroke.keyChar == this.keyChar && aWTKeyStroke.keyCode == this.keyCode && aWTKeyStroke.onKeyRelease == this.onKeyRelease && aWTKeyStroke.modifiers == this.modifiers;
        }
        return false;
    }

    public String toString() {
        if (this.keyCode == 0) {
            return getModifiersText(this.modifiers) + "typed " + this.keyChar;
        }
        return getModifiersText(this.modifiers) + (this.onKeyRelease ? "released" : "pressed") + " " + getVKText(this.keyCode);
    }

    static String getModifiersText(int i2) {
        StringBuilder sb = new StringBuilder();
        if ((i2 & 64) != 0) {
            sb.append("shift ");
        }
        if ((i2 & 128) != 0) {
            sb.append("ctrl ");
        }
        if ((i2 & 256) != 0) {
            sb.append("meta ");
        }
        if ((i2 & 512) != 0) {
            sb.append("alt ");
        }
        if ((i2 & 8192) != 0) {
            sb.append("altGraph ");
        }
        if ((i2 & 1024) != 0) {
            sb.append("button1 ");
        }
        if ((i2 & 2048) != 0) {
            sb.append("button2 ");
        }
        if ((i2 & 4096) != 0) {
            sb.append("button3 ");
        }
        return sb.toString();
    }

    static String getVKText(int i2) throws SecurityException {
        VKCollection vKCollection = getVKCollection();
        Integer numValueOf = Integer.valueOf(i2);
        String strFindName = vKCollection.findName(numValueOf);
        if (strFindName != null) {
            return strFindName.substring(3);
        }
        Field[] declaredFields = KeyEvent.class.getDeclaredFields();
        for (int i3 = 0; i3 < declaredFields.length; i3++) {
            try {
            } catch (IllegalAccessException e2) {
                if (!$assertionsDisabled) {
                    throw new AssertionError();
                }
            }
            if (declaredFields[i3].getModifiers() == 25 && declaredFields[i3].getType() == Integer.TYPE && declaredFields[i3].getName().startsWith("VK_") && declaredFields[i3].getInt(KeyEvent.class) == i2) {
                String name = declaredFields[i3].getName();
                vKCollection.put(name, numValueOf);
                return name.substring(3);
            }
        }
        return "UNKNOWN";
    }

    protected Object readResolve() throws ObjectStreamException {
        synchronized (AWTKeyStroke.class) {
            if (getClass().equals(getAWTKeyStrokeClass())) {
                return getCachedStroke(this.keyChar, this.keyCode, this.modifiers, this.onKeyRelease);
            }
            return this;
        }
    }

    private static int mapOldModifiers(int i2) {
        if ((i2 & 1) != 0) {
            i2 |= 64;
        }
        if ((i2 & 8) != 0) {
            i2 |= 512;
        }
        if ((i2 & 32) != 0) {
            i2 |= 8192;
        }
        if ((i2 & 2) != 0) {
            i2 |= 128;
        }
        if ((i2 & 4) != 0) {
            i2 |= 256;
        }
        return i2 & 16320;
    }

    private static int mapNewModifiers(int i2) {
        if ((i2 & 64) != 0) {
            i2 |= 1;
        }
        if ((i2 & 512) != 0) {
            i2 |= 8;
        }
        if ((i2 & 8192) != 0) {
            i2 |= 32;
        }
        if ((i2 & 128) != 0) {
            i2 |= 2;
        }
        if ((i2 & 256) != 0) {
            i2 |= 4;
        }
        return i2;
    }
}
