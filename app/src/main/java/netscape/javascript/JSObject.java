package netscape.javascript;

import java.applet.Applet;

/* loaded from: jfxrt.jar:netscape/javascript/JSObject.class */
public abstract class JSObject {
    public abstract Object call(String str, Object... objArr) throws JSException;

    public abstract Object eval(String str) throws JSException;

    public abstract Object getMember(String str) throws JSException;

    public abstract void setMember(String str, Object obj) throws JSException;

    public abstract void removeMember(String str) throws JSException;

    public abstract Object getSlot(int i2) throws JSException;

    public abstract void setSlot(int i2, Object obj) throws JSException;

    protected JSObject() {
    }

    public static JSObject getWindow(Applet applet) throws JSException {
        throw new JSException("Unexpected error: This method should not be used unless loaded from plugin.jar");
    }
}
