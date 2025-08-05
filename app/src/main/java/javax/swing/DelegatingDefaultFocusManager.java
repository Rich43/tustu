package javax.swing;

import java.awt.AWTEvent;
import java.awt.AWTKeyStroke;
import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import java.util.Set;

/* loaded from: rt.jar:javax/swing/DelegatingDefaultFocusManager.class */
final class DelegatingDefaultFocusManager extends DefaultFocusManager {
    private final KeyboardFocusManager delegate;

    DelegatingDefaultFocusManager(KeyboardFocusManager keyboardFocusManager) {
        this.delegate = keyboardFocusManager;
        setDefaultFocusTraversalPolicy(this.gluePolicy);
    }

    KeyboardFocusManager getDelegate() {
        return this.delegate;
    }

    @Override // java.awt.DefaultKeyboardFocusManager, java.awt.KeyboardFocusManager
    public void processKeyEvent(Component component, KeyEvent keyEvent) {
        this.delegate.processKeyEvent(component, keyEvent);
    }

    @Override // java.awt.DefaultKeyboardFocusManager, java.awt.KeyboardFocusManager
    public void focusNextComponent(Component component) {
        this.delegate.focusNextComponent(component);
    }

    @Override // java.awt.DefaultKeyboardFocusManager, java.awt.KeyboardFocusManager
    public void focusPreviousComponent(Component component) {
        this.delegate.focusPreviousComponent(component);
    }

    @Override // java.awt.KeyboardFocusManager
    public Component getFocusOwner() {
        return this.delegate.getFocusOwner();
    }

    @Override // java.awt.KeyboardFocusManager
    public void clearGlobalFocusOwner() throws SecurityException {
        this.delegate.clearGlobalFocusOwner();
    }

    @Override // java.awt.KeyboardFocusManager
    public Component getPermanentFocusOwner() {
        return this.delegate.getPermanentFocusOwner();
    }

    @Override // java.awt.KeyboardFocusManager
    public Window getFocusedWindow() {
        return this.delegate.getFocusedWindow();
    }

    @Override // java.awt.KeyboardFocusManager
    public Window getActiveWindow() {
        return this.delegate.getActiveWindow();
    }

    @Override // java.awt.KeyboardFocusManager
    public FocusTraversalPolicy getDefaultFocusTraversalPolicy() {
        return this.delegate.getDefaultFocusTraversalPolicy();
    }

    @Override // java.awt.KeyboardFocusManager
    public void setDefaultFocusTraversalPolicy(FocusTraversalPolicy focusTraversalPolicy) {
        if (this.delegate != null) {
            this.delegate.setDefaultFocusTraversalPolicy(focusTraversalPolicy);
        }
    }

    @Override // java.awt.KeyboardFocusManager
    public void setDefaultFocusTraversalKeys(int i2, Set<? extends AWTKeyStroke> set) {
        this.delegate.setDefaultFocusTraversalKeys(i2, set);
    }

    @Override // java.awt.KeyboardFocusManager
    public Set<AWTKeyStroke> getDefaultFocusTraversalKeys(int i2) {
        return this.delegate.getDefaultFocusTraversalKeys(i2);
    }

    @Override // java.awt.KeyboardFocusManager
    public Container getCurrentFocusCycleRoot() {
        return this.delegate.getCurrentFocusCycleRoot();
    }

    @Override // java.awt.KeyboardFocusManager
    public void setGlobalCurrentFocusCycleRoot(Container container) throws SecurityException {
        this.delegate.setGlobalCurrentFocusCycleRoot(container);
    }

    @Override // java.awt.KeyboardFocusManager
    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        this.delegate.addPropertyChangeListener(propertyChangeListener);
    }

    @Override // java.awt.KeyboardFocusManager
    public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        this.delegate.removePropertyChangeListener(propertyChangeListener);
    }

    @Override // java.awt.KeyboardFocusManager
    public void addPropertyChangeListener(String str, PropertyChangeListener propertyChangeListener) {
        this.delegate.addPropertyChangeListener(str, propertyChangeListener);
    }

    @Override // java.awt.KeyboardFocusManager
    public void removePropertyChangeListener(String str, PropertyChangeListener propertyChangeListener) {
        this.delegate.removePropertyChangeListener(str, propertyChangeListener);
    }

    @Override // java.awt.KeyboardFocusManager
    public void addVetoableChangeListener(VetoableChangeListener vetoableChangeListener) {
        this.delegate.addVetoableChangeListener(vetoableChangeListener);
    }

    @Override // java.awt.KeyboardFocusManager
    public void removeVetoableChangeListener(VetoableChangeListener vetoableChangeListener) {
        this.delegate.removeVetoableChangeListener(vetoableChangeListener);
    }

    @Override // java.awt.KeyboardFocusManager
    public void addVetoableChangeListener(String str, VetoableChangeListener vetoableChangeListener) {
        this.delegate.addVetoableChangeListener(str, vetoableChangeListener);
    }

    @Override // java.awt.KeyboardFocusManager
    public void removeVetoableChangeListener(String str, VetoableChangeListener vetoableChangeListener) {
        this.delegate.removeVetoableChangeListener(str, vetoableChangeListener);
    }

    @Override // java.awt.KeyboardFocusManager
    public void addKeyEventDispatcher(KeyEventDispatcher keyEventDispatcher) {
        this.delegate.addKeyEventDispatcher(keyEventDispatcher);
    }

    @Override // java.awt.KeyboardFocusManager
    public void removeKeyEventDispatcher(KeyEventDispatcher keyEventDispatcher) {
        this.delegate.removeKeyEventDispatcher(keyEventDispatcher);
    }

    @Override // java.awt.DefaultKeyboardFocusManager, java.awt.KeyboardFocusManager
    public boolean dispatchEvent(AWTEvent aWTEvent) {
        return this.delegate.dispatchEvent(aWTEvent);
    }

    @Override // java.awt.DefaultKeyboardFocusManager, java.awt.KeyboardFocusManager, java.awt.KeyEventDispatcher
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        return this.delegate.dispatchKeyEvent(keyEvent);
    }

    @Override // java.awt.DefaultKeyboardFocusManager, java.awt.KeyboardFocusManager
    public void upFocusCycle(Component component) {
        this.delegate.upFocusCycle(component);
    }

    @Override // java.awt.DefaultKeyboardFocusManager, java.awt.KeyboardFocusManager
    public void downFocusCycle(Container container) {
        this.delegate.downFocusCycle(container);
    }
}
