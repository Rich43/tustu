package bX;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/* loaded from: TunerStudioMS.jar:bX/b.class */
public final class b {

    /* renamed from: a, reason: collision with root package name */
    public static final InetAddress f7662a = a();

    /* renamed from: b, reason: collision with root package name */
    public static final InetAddress f7663b = b();

    /* renamed from: c, reason: collision with root package name */
    static final Map f7664c;

    /* renamed from: d, reason: collision with root package name */
    static final Map f7665d;

    /* renamed from: e, reason: collision with root package name */
    static final Map f7666e;

    /* renamed from: f, reason: collision with root package name */
    static final Map f7667f;

    /* renamed from: g, reason: collision with root package name */
    static final Map f7668g;

    private b() {
        throw new UnsupportedOperationException();
    }

    private static final InetAddress a() {
        try {
            return InetAddress.getByAddress(new byte[]{0, 0, 0, 0});
        } catch (UnknownHostException e2) {
            throw new IllegalStateException("Unable to generate INADDR_ANY");
        }
    }

    private static final InetAddress b() {
        try {
            return InetAddress.getByAddress(new byte[]{-1, -1, -1, -1});
        } catch (UnknownHostException e2) {
            throw new IllegalStateException("Unable to generate INADDR_BROADCAST");
        }
    }

    static {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        LinkedHashMap linkedHashMap2 = new LinkedHashMap();
        LinkedHashMap linkedHashMap3 = new LinkedHashMap();
        LinkedHashMap linkedHashMap4 = new LinkedHashMap();
        LinkedHashMap linkedHashMap5 = new LinkedHashMap();
        try {
            for (Field field : b.class.getDeclaredFields()) {
                int modifiers = field.getModifiers();
                String name = field.getName();
                if (Modifier.isFinal(modifiers) && Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && field.getType().equals(Byte.TYPE)) {
                    byte b2 = field.getByte(null);
                    if (name.startsWith("BOOT")) {
                        linkedHashMap.put(Byte.valueOf(b2), name);
                    } else if (name.startsWith("HTYPE_")) {
                        linkedHashMap2.put(Byte.valueOf(b2), name);
                    } else if (name.startsWith("DHCP")) {
                        linkedHashMap3.put(Byte.valueOf(b2), name);
                    } else if (name.startsWith("DHO_")) {
                        linkedHashMap4.put(Byte.valueOf(b2), name);
                        linkedHashMap5.put(name, Byte.valueOf(b2));
                    }
                }
            }
            f7664c = Collections.unmodifiableMap(linkedHashMap);
            f7665d = Collections.unmodifiableMap(linkedHashMap2);
            f7666e = Collections.unmodifiableMap(linkedHashMap3);
            f7667f = Collections.unmodifiableMap(linkedHashMap4);
            f7668g = Collections.unmodifiableMap(linkedHashMap5);
        } catch (IllegalAccessException e2) {
            throw new IllegalStateException("Fatal error while parsing internal fields");
        }
    }
}
