package com.sun.beans.editors;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditor;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXMLLoader;

/* loaded from: rt.jar:com/sun/beans/editors/EnumEditor.class */
public final class EnumEditor implements PropertyEditor {
    private final List<PropertyChangeListener> listeners = new ArrayList();
    private final Class type;
    private final String[] tags;
    private Object value;

    public EnumEditor(Class cls) {
        Object[] enumConstants = cls.getEnumConstants();
        if (enumConstants == null) {
            throw new IllegalArgumentException("Unsupported " + ((Object) cls));
        }
        this.type = cls;
        this.tags = new String[enumConstants.length];
        for (int i2 = 0; i2 < enumConstants.length; i2++) {
            this.tags[i2] = ((Enum) enumConstants[i2]).name();
        }
    }

    @Override // java.beans.PropertyEditor
    public Object getValue() {
        return this.value;
    }

    /* JADX WARN: Code restructure failed: missing block: B:16:0x004c, code lost:
    
        if (r8.equals(r0) != false) goto L17;
     */
    @Override // java.beans.PropertyEditor
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void setValue(java.lang.Object r8) {
        /*
            r7 = this;
            r0 = r8
            if (r0 == 0) goto L2a
            r0 = r7
            java.lang.Class r0 = r0.type
            r1 = r8
            boolean r0 = r0.isInstance(r1)
            if (r0 != 0) goto L2a
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            r1 = r0
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r3 = r2
            r3.<init>()
            java.lang.String r3 = "Unsupported value: "
            java.lang.StringBuilder r2 = r2.append(r3)
            r3 = r8
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r2 = r2.toString()
            r1.<init>(r2)
            throw r0
        L2a:
            r0 = r7
            java.util.List<java.beans.PropertyChangeListener> r0 = r0.listeners
            r1 = r0
            r11 = r1
            monitor-enter(r0)
            r0 = r7
            java.lang.Object r0 = r0.value     // Catch: java.lang.Throwable -> L7f
            r9 = r0
            r0 = r7
            r1 = r8
            r0.value = r1     // Catch: java.lang.Throwable -> L7f
            r0 = r8
            if (r0 != 0) goto L47
            r0 = r9
            if (r0 != 0) goto L53
            goto L4f
        L47:
            r0 = r8
            r1 = r9
            boolean r0 = r0.equals(r1)     // Catch: java.lang.Throwable -> L7f
            if (r0 == 0) goto L53
        L4f:
            r0 = r11
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L7f
            return
        L53:
            r0 = r7
            java.util.List<java.beans.PropertyChangeListener> r0 = r0.listeners     // Catch: java.lang.Throwable -> L7f
            int r0 = r0.size()     // Catch: java.lang.Throwable -> L7f
            r12 = r0
            r0 = r12
            if (r0 != 0) goto L67
            r0 = r11
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L7f
            return
        L67:
            r0 = r7
            java.util.List<java.beans.PropertyChangeListener> r0 = r0.listeners     // Catch: java.lang.Throwable -> L7f
            r1 = r12
            java.beans.PropertyChangeListener[] r1 = new java.beans.PropertyChangeListener[r1]     // Catch: java.lang.Throwable -> L7f
            java.lang.Object[] r0 = r0.toArray(r1)     // Catch: java.lang.Throwable -> L7f
            java.beans.PropertyChangeListener[] r0 = (java.beans.PropertyChangeListener[]) r0     // Catch: java.lang.Throwable -> L7f
            r10 = r0
            r0 = r11
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L7f
            goto L87
        L7f:
            r13 = move-exception
            r0 = r11
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L7f
            r0 = r13
            throw r0
        L87:
            java.beans.PropertyChangeEvent r0 = new java.beans.PropertyChangeEvent
            r1 = r0
            r2 = r7
            r3 = 0
            r4 = r9
            r5 = r8
            r1.<init>(r2, r3, r4, r5)
            r11 = r0
            r0 = r10
            r12 = r0
            r0 = r12
            int r0 = r0.length
            r13 = r0
            r0 = 0
            r14 = r0
        L9f:
            r0 = r14
            r1 = r13
            if (r0 >= r1) goto Lbc
            r0 = r12
            r1 = r14
            r0 = r0[r1]
            r15 = r0
            r0 = r15
            r1 = r11
            r0.propertyChange(r1)
            int r14 = r14 + 1
            goto L9f
        Lbc:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.beans.editors.EnumEditor.setValue(java.lang.Object):void");
    }

    @Override // java.beans.PropertyEditor
    public String getAsText() {
        if (this.value != null) {
            return ((Enum) this.value).name();
        }
        return null;
    }

    @Override // java.beans.PropertyEditor
    public void setAsText(String str) {
        setValue(str != null ? Enum.valueOf(this.type, str) : null);
    }

    @Override // java.beans.PropertyEditor
    public String[] getTags() {
        return (String[]) this.tags.clone();
    }

    @Override // java.beans.PropertyEditor
    public String getJavaInitializationString() {
        String asText = getAsText();
        return asText != null ? this.type.getName() + '.' + asText : FXMLLoader.NULL_KEYWORD;
    }

    @Override // java.beans.PropertyEditor
    public boolean isPaintable() {
        return false;
    }

    @Override // java.beans.PropertyEditor
    public void paintValue(Graphics graphics, Rectangle rectangle) {
    }

    @Override // java.beans.PropertyEditor
    public boolean supportsCustomEditor() {
        return false;
    }

    @Override // java.beans.PropertyEditor
    public Component getCustomEditor() {
        return null;
    }

    @Override // java.beans.PropertyEditor
    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        synchronized (this.listeners) {
            this.listeners.add(propertyChangeListener);
        }
    }

    @Override // java.beans.PropertyEditor
    public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        synchronized (this.listeners) {
            this.listeners.remove(propertyChangeListener);
        }
    }
}
