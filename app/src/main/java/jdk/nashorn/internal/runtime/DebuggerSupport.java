package jdk.nashorn.internal.runtime;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import javafx.fxml.FXMLLoader;
import jdk.nashorn.internal.codegen.CompilerConstants;
import jdk.nashorn.internal.scripts.JS;
import org.icepdf.core.util.PdfOps;

/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/DebuggerSupport.class */
final class DebuggerSupport {
    static boolean FORCELOAD;
    static final /* synthetic */ boolean $assertionsDisabled;

    DebuggerSupport() {
    }

    static {
        $assertionsDisabled = !DebuggerSupport.class.desiredAssertionStatus();
        FORCELOAD = true;
        new DebuggerValueDesc(null, false, null, null);
        new SourceInfo(null, 0, null, null);
    }

    /* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/DebuggerSupport$DebuggerValueDesc.class */
    static class DebuggerValueDesc {
        final String key;
        final boolean expandable;
        final Object valueAsObject;
        final String valueAsString;

        DebuggerValueDesc(String key, boolean expandable, Object valueAsObject, String valueAsString) {
            this.key = key;
            this.expandable = expandable;
            this.valueAsObject = valueAsObject;
            this.valueAsString = valueAsString;
        }
    }

    /* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/DebuggerSupport$SourceInfo.class */
    static class SourceInfo {
        final String name;
        final URL url;
        final int hash;
        final char[] content;

        SourceInfo(String name, int hash, URL url, char[] content) {
            this.name = name;
            this.hash = hash;
            this.url = url;
            this.content = content;
        }
    }

    static void notifyInvoke(MethodHandle mh) {
    }

    static SourceInfo getSourceInfo(Class<?> clazz) {
        if (JS.class.isAssignableFrom(clazz)) {
            try {
                Field sourceField = clazz.getDeclaredField(CompilerConstants.SOURCE.symbolName());
                sourceField.setAccessible(true);
                Source src = (Source) sourceField.get(null);
                return src.getSourceInfo();
            } catch (IllegalAccessException | NoSuchFieldException e2) {
                return null;
            }
        }
        return null;
    }

    static Object getGlobal() {
        return Context.getGlobal();
    }

    static Object eval(ScriptObject scope, Object self, String string, boolean returnException) {
        ScriptObject global = Context.getGlobal();
        ScriptObject initialScope = scope != null ? scope : global;
        Object callThis = self != null ? self : global;
        Context context = global.getContext();
        try {
            return context.eval(initialScope, string, callThis, ScriptRuntime.UNDEFINED);
        } catch (Throwable ex) {
            if (returnException) {
                return ex;
            }
            return null;
        }
    }

    static DebuggerValueDesc[] valueInfos(Object object, boolean all) {
        if ($assertionsDisabled || (object instanceof ScriptObject)) {
            return getDebuggerValueDescs((ScriptObject) object, all, new HashSet());
        }
        throw new AssertionError();
    }

    static DebuggerValueDesc valueInfo(String name, Object value, boolean all) {
        return valueInfo(name, value, all, new HashSet());
    }

    private static DebuggerValueDesc valueInfo(String name, Object value, boolean all, Set<Object> duplicates) {
        if ((value instanceof ScriptObject) && !(value instanceof ScriptFunction)) {
            ScriptObject object = (ScriptObject) value;
            return new DebuggerValueDesc(name, !object.isEmpty(), value, objectAsString(object, all, duplicates));
        }
        return new DebuggerValueDesc(name, false, value, valueAsString(value));
    }

    private static DebuggerValueDesc[] getDebuggerValueDescs(ScriptObject object, boolean all, Set<Object> duplicates) {
        if (duplicates.contains(object)) {
            return null;
        }
        duplicates.add(object);
        String[] keys = object.getOwnKeys(all);
        DebuggerValueDesc[] descs = new DebuggerValueDesc[keys.length];
        for (int i2 = 0; i2 < keys.length; i2++) {
            String key = keys[i2];
            descs[i2] = valueInfo(key, object.get(key), all, duplicates);
        }
        duplicates.remove(object);
        return descs;
    }

    private static String objectAsString(ScriptObject object, boolean all, Set<Object> duplicates) {
        StringBuilder sb = new StringBuilder();
        if (ScriptObject.isArray(object)) {
            sb.append('[');
            long length = (long) object.getDouble("length", -1);
            long j2 = 0;
            while (true) {
                long i2 = j2;
                if (i2 >= length) {
                    break;
                }
                if (object.has(i2)) {
                    Object valueAsObject = object.get(i2);
                    boolean isUndefined = valueAsObject == ScriptRuntime.UNDEFINED;
                    if (!isUndefined) {
                        if (i2 != 0) {
                            sb.append(", ");
                        }
                        if ((valueAsObject instanceof ScriptObject) && !(valueAsObject instanceof ScriptFunction)) {
                            String objectString = objectAsString((ScriptObject) valueAsObject, all, duplicates);
                            sb.append(objectString != null ? objectString : "{...}");
                        } else {
                            sb.append(valueAsString(valueAsObject));
                        }
                    } else if (i2 != 0) {
                        sb.append(",");
                    }
                } else if (i2 != 0) {
                    sb.append(',');
                }
                j2 = i2 + 1;
            }
            sb.append(']');
        } else {
            sb.append('{');
            DebuggerValueDesc[] descs = getDebuggerValueDescs(object, all, duplicates);
            if (descs != null) {
                for (int i3 = 0; i3 < descs.length; i3++) {
                    if (i3 != 0) {
                        sb.append(", ");
                    }
                    String valueAsString = descs[i3].valueAsString;
                    sb.append(descs[i3].key);
                    sb.append(": ");
                    sb.append(valueAsString);
                }
            }
            sb.append('}');
        }
        return sb.toString();
    }

    static String valueAsString(Object value) {
        JSType type = JSType.of(value);
        switch (type) {
            case BOOLEAN:
                return value.toString();
            case STRING:
                return escape(value.toString());
            case NUMBER:
                return JSType.toString(((Number) value).doubleValue());
            case NULL:
                return FXMLLoader.NULL_KEYWORD;
            case UNDEFINED:
                return "undefined";
            case OBJECT:
                return ScriptRuntime.safeToString(value);
            case FUNCTION:
                if (value instanceof ScriptFunction) {
                    return ((ScriptFunction) value).toSource();
                }
                return value.toString();
            default:
                return value.toString();
        }
    }

    private static String escape(String value) {
        StringBuilder sb = new StringBuilder();
        sb.append(PdfOps.DOUBLE_QUOTE__TOKEN);
        for (char ch : value.toCharArray()) {
            switch (ch) {
                case '\b':
                    sb.append("\\b");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\"':
                    sb.append("\\\"");
                    break;
                case '\'':
                    sb.append("\\'");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                default:
                    if (ch < ' ' || ch >= 255) {
                        sb.append("\\u");
                        String hex = Integer.toHexString(ch);
                        for (int i2 = hex.length(); i2 < 4; i2++) {
                            sb.append('0');
                        }
                        sb.append(hex);
                        break;
                    } else {
                        sb.append(ch);
                        break;
                    }
                    break;
            }
        }
        sb.append(PdfOps.DOUBLE_QUOTE__TOKEN);
        return sb.toString();
    }
}
