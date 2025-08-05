package com.sun.glass.ui;

import com.sun.javafx.scene.NodeHelper;
import com.sun.javafx.scene.SceneHelper;
import com.sun.javafx.tk.quantum.QuantumToolkit;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.Scene;

/* loaded from: jfxrt.jar:com/sun/glass/ui/Accessible.class */
public abstract class Accessible {
    private EventHandler eventHandler;
    private View view;
    private GetAttribute getAttribute = new GetAttribute();
    private ExecuteAction executeAction = new ExecuteAction();

    protected abstract long getNativeAccessible();

    public abstract void sendNotification(AccessibleAttribute accessibleAttribute);

    /* loaded from: jfxrt.jar:com/sun/glass/ui/Accessible$EventHandler.class */
    public static abstract class EventHandler {
        public abstract AccessControlContext getAccessControlContext();

        public Object getAttribute(AccessibleAttribute attribute, Object... parameters) {
            return null;
        }

        public void executeAction(AccessibleAction action, Object... parameters) {
        }
    }

    public EventHandler getEventHandler() {
        return this.eventHandler;
    }

    public void setEventHandler(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    public void setView(View view) {
        this.view = view;
    }

    public View getView() {
        return this.view;
    }

    public void dispose() {
        this.eventHandler = null;
        this.view = null;
    }

    public boolean isDisposed() {
        return getNativeAccessible() == 0;
    }

    public String toString() {
        return getClass().getSimpleName() + " (" + ((Object) this.eventHandler) + ")";
    }

    protected boolean isIgnored() {
        AccessibleRole role = (AccessibleRole) getAttribute(AccessibleAttribute.ROLE, new Object[0]);
        return role == null || role == AccessibleRole.NODE || role == AccessibleRole.PARENT;
    }

    protected Accessible getAccessible(Scene scene) {
        if (scene == null) {
            return null;
        }
        return SceneHelper.getAccessible(scene);
    }

    protected Accessible getAccessible(Node node) {
        if (node == null) {
            return null;
        }
        return NodeHelper.getAccessible(node);
    }

    protected long getNativeAccessible(Node node) {
        Accessible acc;
        if (node == null || (acc = getAccessible(node)) == null) {
            return 0L;
        }
        return acc.getNativeAccessible();
    }

    protected Accessible getContainerAccessible(AccessibleRole targetRole) {
        Object attribute = getAttribute(AccessibleAttribute.PARENT, new Object[0]);
        while (true) {
            Node node = (Node) attribute;
            if (node != null) {
                Accessible acc = getAccessible(node);
                AccessibleRole role = (AccessibleRole) acc.getAttribute(AccessibleAttribute.ROLE, new Object[0]);
                if (role == targetRole) {
                    return acc;
                }
                attribute = acc.getAttribute(AccessibleAttribute.PARENT, new Object[0]);
            } else {
                return null;
            }
        }
    }

    private final AccessControlContext getAccessControlContext() {
        AccessControlContext acc = null;
        try {
            acc = this.eventHandler.getAccessControlContext();
        } catch (Exception e2) {
        }
        return acc;
    }

    /* loaded from: jfxrt.jar:com/sun/glass/ui/Accessible$GetAttribute.class */
    private class GetAttribute implements PrivilegedAction<Object> {
        AccessibleAttribute attribute;
        Object[] parameters;

        private GetAttribute() {
        }

        @Override // java.security.PrivilegedAction
        /* renamed from: run */
        public Object run2() {
            Class<?> clazz;
            Object result = Accessible.this.eventHandler.getAttribute(this.attribute, this.parameters);
            if (result != null && (clazz = this.attribute.getReturnType()) != null) {
                try {
                    clazz.cast(result);
                } catch (Exception e2) {
                    String msg = "The expected return type for the " + ((Object) this.attribute) + " attribute is " + clazz.getSimpleName() + " but found " + result.getClass().getSimpleName();
                    System.err.println(msg);
                    return null;
                }
            }
            return result;
        }
    }

    public Object getAttribute(AccessibleAttribute attribute, Object... parameters) {
        AccessControlContext acc = getAccessControlContext();
        if (acc == null) {
            return null;
        }
        return QuantumToolkit.runWithoutRenderLock(() -> {
            this.getAttribute.attribute = attribute;
            this.getAttribute.parameters = parameters;
            return AccessController.doPrivileged(this.getAttribute, acc);
        });
    }

    /* loaded from: jfxrt.jar:com/sun/glass/ui/Accessible$ExecuteAction.class */
    private class ExecuteAction implements PrivilegedAction<Void> {
        AccessibleAction action;
        Object[] parameters;

        private ExecuteAction() {
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.security.PrivilegedAction
        /* renamed from: run */
        public Void run2() {
            Accessible.this.eventHandler.executeAction(this.action, this.parameters);
            return null;
        }
    }

    public void executeAction(AccessibleAction action, Object... parameters) {
        AccessControlContext acc = getAccessControlContext();
        if (acc == null) {
            return;
        }
        QuantumToolkit.runWithoutRenderLock(() -> {
            this.executeAction.action = action;
            this.executeAction.parameters = parameters;
            return (Void) AccessController.doPrivileged(this.executeAction, acc);
        });
    }
}
