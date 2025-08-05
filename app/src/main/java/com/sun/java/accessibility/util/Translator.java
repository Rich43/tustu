package com.sun.java.accessibility.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.IllegalComponentStateException;
import java.awt.MenuComponent;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeListener;
import java.util.Locale;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleComponent;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleState;
import javax.accessibility.AccessibleStateSet;
import jdk.Exported;

@Exported
/* loaded from: jaccess.jar:com/sun/java/accessibility/util/Translator.class */
public class Translator extends AccessibleContext implements Accessible, AccessibleComponent {
    protected Object source;

    protected static Class getTranslatorClass(Class cls) {
        if (cls == null) {
            return null;
        }
        try {
            return Class.forName("com.sun.java.accessibility.util." + cls.getName() + "Translator");
        } catch (Exception e2) {
            return getTranslatorClass(cls.getSuperclass());
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v17, types: [javax.accessibility.Accessible] */
    public static Accessible getAccessible(Object obj) {
        Translator translator = null;
        if (obj == null) {
            return null;
        }
        if (obj instanceof Accessible) {
            translator = (Accessible) obj;
        } else {
            Class translatorClass = getTranslatorClass(obj.getClass());
            if (translatorClass != null) {
                try {
                    Translator translator2 = (Translator) translatorClass.newInstance();
                    translator2.setSource(obj);
                    translator = translator2;
                } catch (Exception e2) {
                }
            }
        }
        if (translator == null) {
            translator = new Translator(obj);
        }
        return translator;
    }

    public Translator() {
    }

    public Translator(Object obj) {
        this.source = obj;
    }

    public Object getSource() {
        return this.source;
    }

    public void setSource(Object obj) {
        this.source = obj;
    }

    public boolean equals(Object obj) {
        return this.source.equals(obj);
    }

    @Override // javax.accessibility.Accessible
    public AccessibleContext getAccessibleContext() {
        return this;
    }

    @Override // javax.accessibility.AccessibleContext
    public String getAccessibleName() {
        if (this.source instanceof MenuItem) {
            return ((MenuItem) this.source).getLabel();
        }
        if (this.source instanceof Component) {
            return ((Component) this.source).getName();
        }
        return null;
    }

    @Override // javax.accessibility.AccessibleContext
    public void setAccessibleName(String str) {
        if (this.source instanceof MenuItem) {
            ((MenuItem) this.source).setLabel(str);
        } else if (this.source instanceof Component) {
            ((Component) this.source).setName(str);
        }
    }

    @Override // javax.accessibility.AccessibleContext
    public String getAccessibleDescription() {
        return null;
    }

    @Override // javax.accessibility.AccessibleContext
    public void setAccessibleDescription(String str) {
    }

    @Override // javax.accessibility.AccessibleContext
    public AccessibleRole getAccessibleRole() {
        return AccessibleRole.UNKNOWN;
    }

    @Override // javax.accessibility.AccessibleContext
    public AccessibleStateSet getAccessibleStateSet() {
        AccessibleStateSet accessibleStateSet = new AccessibleStateSet();
        if (this.source instanceof Component) {
            Component component = (Component) this.source;
            Container parent = component.getParent();
            while (true) {
                Container container = parent;
                if (container == null) {
                    break;
                }
                if ((container instanceof Window) && ((Window) container).getFocusOwner() == component) {
                    accessibleStateSet.add(AccessibleState.FOCUSED);
                }
                parent = container.getParent();
            }
        }
        if (isEnabled()) {
            accessibleStateSet.add(AccessibleState.ENABLED);
        }
        if (isFocusTraversable()) {
            accessibleStateSet.add(AccessibleState.FOCUSABLE);
        }
        if (this.source instanceof MenuItem) {
            accessibleStateSet.add(AccessibleState.FOCUSABLE);
        }
        return accessibleStateSet;
    }

    @Override // javax.accessibility.AccessibleContext
    public Accessible getAccessibleParent() {
        if (this.accessibleParent != null) {
            return this.accessibleParent;
        }
        if (this.source instanceof Component) {
            return getAccessible(((Component) this.source).getParent());
        }
        return null;
    }

    @Override // javax.accessibility.AccessibleContext
    public int getAccessibleIndexInParent() {
        Container parent;
        if ((this.source instanceof Component) && (parent = ((Component) this.source).getParent()) != null) {
            Component[] components = parent.getComponents();
            for (int i2 = 0; i2 < components.length; i2++) {
                if (this.source.equals(components[i2])) {
                    return i2;
                }
            }
            return -1;
        }
        return -1;
    }

    @Override // javax.accessibility.AccessibleContext
    public int getAccessibleChildrenCount() {
        if (this.source instanceof Container) {
            int i2 = 0;
            for (Component component : ((Container) this.source).getComponents()) {
                if (getAccessible(component) != null) {
                    i2++;
                }
            }
            return i2;
        }
        return 0;
    }

    @Override // javax.accessibility.AccessibleContext
    public Accessible getAccessibleChild(int i2) {
        if (this.source instanceof Container) {
            int i3 = 0;
            for (Component component : ((Container) this.source).getComponents()) {
                Accessible accessible = getAccessible(component);
                if (accessible != null) {
                    if (i3 == i2) {
                        AccessibleContext accessibleContext = accessible.getAccessibleContext();
                        if (accessibleContext != null) {
                            accessibleContext.setAccessibleParent(this);
                        }
                        return accessible;
                    }
                    i3++;
                }
            }
            return null;
        }
        return null;
    }

    @Override // javax.accessibility.AccessibleContext
    public Locale getLocale() throws IllegalComponentStateException {
        if (this.source instanceof Component) {
            return ((Component) this.source).getLocale();
        }
        return null;
    }

    @Override // javax.accessibility.AccessibleContext
    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
    }

    @Override // javax.accessibility.AccessibleContext
    public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
    }

    @Override // javax.accessibility.AccessibleComponent
    public Color getBackground() {
        if (this.source instanceof Component) {
            return ((Component) this.source).getBackground();
        }
        return null;
    }

    @Override // javax.accessibility.AccessibleComponent
    public void setBackground(Color color) {
        if (this.source instanceof Component) {
            ((Component) this.source).setBackground(color);
        }
    }

    @Override // javax.accessibility.AccessibleComponent
    public Color getForeground() {
        if (this.source instanceof Component) {
            return ((Component) this.source).getForeground();
        }
        return null;
    }

    @Override // javax.accessibility.AccessibleComponent
    public void setForeground(Color color) {
        if (this.source instanceof Component) {
            ((Component) this.source).setForeground(color);
        }
    }

    @Override // javax.accessibility.AccessibleComponent
    public Cursor getCursor() {
        if (this.source instanceof Component) {
            return ((Component) this.source).getCursor();
        }
        return null;
    }

    @Override // javax.accessibility.AccessibleComponent
    public void setCursor(Cursor cursor) {
        if (this.source instanceof Component) {
            ((Component) this.source).setCursor(cursor);
        }
    }

    @Override // javax.accessibility.AccessibleComponent
    public Font getFont() {
        if (this.source instanceof Component) {
            return ((Component) this.source).getFont();
        }
        if (this.source instanceof MenuComponent) {
            return ((MenuComponent) this.source).getFont();
        }
        return null;
    }

    @Override // javax.accessibility.AccessibleComponent
    public void setFont(Font font) {
        if (this.source instanceof Component) {
            ((Component) this.source).setFont(font);
        } else if (this.source instanceof MenuComponent) {
            ((MenuComponent) this.source).setFont(font);
        }
    }

    @Override // javax.accessibility.AccessibleComponent
    public FontMetrics getFontMetrics(Font font) {
        if (this.source instanceof Component) {
            return ((Component) this.source).getFontMetrics(font);
        }
        return null;
    }

    @Override // javax.accessibility.AccessibleComponent
    public boolean isEnabled() {
        if (this.source instanceof Component) {
            return ((Component) this.source).isEnabled();
        }
        if (this.source instanceof MenuItem) {
            return ((MenuItem) this.source).isEnabled();
        }
        return true;
    }

    @Override // javax.accessibility.AccessibleComponent
    public void setEnabled(boolean z2) {
        if (this.source instanceof Component) {
            ((Component) this.source).setEnabled(z2);
        } else if (this.source instanceof MenuItem) {
            ((MenuItem) this.source).setEnabled(z2);
        }
    }

    @Override // javax.accessibility.AccessibleComponent
    public boolean isVisible() {
        if (this.source instanceof Component) {
            return ((Component) this.source).isVisible();
        }
        return false;
    }

    @Override // javax.accessibility.AccessibleComponent
    public void setVisible(boolean z2) {
        if (this.source instanceof Component) {
            ((Component) this.source).setVisible(z2);
        }
    }

    @Override // javax.accessibility.AccessibleComponent
    public boolean isShowing() {
        if (this.source instanceof Component) {
            return ((Component) this.source).isShowing();
        }
        return false;
    }

    @Override // javax.accessibility.AccessibleComponent
    public boolean contains(Point point) {
        if (this.source instanceof Component) {
            return ((Component) this.source).contains(point);
        }
        return false;
    }

    @Override // javax.accessibility.AccessibleComponent
    public Point getLocationOnScreen() {
        if (this.source instanceof Component) {
            return ((Component) this.source).getLocationOnScreen();
        }
        return null;
    }

    @Override // javax.accessibility.AccessibleComponent
    public Point getLocation() {
        if (this.source instanceof Component) {
            return ((Component) this.source).getLocation();
        }
        return null;
    }

    @Override // javax.accessibility.AccessibleComponent
    public void setLocation(Point point) {
        if (this.source instanceof Component) {
            ((Component) this.source).setLocation(point);
        }
    }

    @Override // javax.accessibility.AccessibleComponent
    public Rectangle getBounds() {
        if (this.source instanceof Component) {
            return ((Component) this.source).getBounds();
        }
        return null;
    }

    @Override // javax.accessibility.AccessibleComponent
    public void setBounds(Rectangle rectangle) {
        if (this.source instanceof Component) {
            ((Component) this.source).setBounds(rectangle);
        }
    }

    @Override // javax.accessibility.AccessibleComponent
    public Dimension getSize() {
        if (this.source instanceof Component) {
            return ((Component) this.source).getSize();
        }
        return null;
    }

    @Override // javax.accessibility.AccessibleComponent
    public void setSize(Dimension dimension) {
        if (this.source instanceof Component) {
            ((Component) this.source).setSize(dimension);
        }
    }

    @Override // javax.accessibility.AccessibleComponent
    public Accessible getAccessibleAt(Point point) {
        Component componentAt;
        if ((this.source instanceof Component) && (componentAt = ((Component) this.source).getComponentAt(point)) != null) {
            return getAccessible(componentAt);
        }
        return null;
    }

    @Override // javax.accessibility.AccessibleComponent
    public boolean isFocusTraversable() {
        if (this.source instanceof Component) {
            return ((Component) this.source).isFocusTraversable();
        }
        return false;
    }

    @Override // javax.accessibility.AccessibleComponent
    public void requestFocus() {
        if (this.source instanceof Component) {
            ((Component) this.source).requestFocus();
        }
    }

    @Override // javax.accessibility.AccessibleComponent
    public synchronized void addFocusListener(FocusListener focusListener) {
        if (this.source instanceof Component) {
            ((Component) this.source).addFocusListener(focusListener);
        }
    }

    @Override // javax.accessibility.AccessibleComponent
    public synchronized void removeFocusListener(FocusListener focusListener) {
        if (this.source instanceof Component) {
            ((Component) this.source).removeFocusListener(focusListener);
        }
    }
}
