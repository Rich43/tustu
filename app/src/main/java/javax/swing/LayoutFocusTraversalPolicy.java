package javax.swing;

import java.awt.Component;
import java.awt.Container;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Comparator;
import sun.awt.SunToolkit;

/* loaded from: rt.jar:javax/swing/LayoutFocusTraversalPolicy.class */
public class LayoutFocusTraversalPolicy extends SortingFocusTraversalPolicy implements Serializable {
    private static final SwingDefaultFocusTraversalPolicy fitnessTestPolicy = new SwingDefaultFocusTraversalPolicy();

    public LayoutFocusTraversalPolicy() {
        super(new LayoutComparator());
    }

    LayoutFocusTraversalPolicy(Comparator<? super Component> comparator) {
        super(comparator);
    }

    @Override // javax.swing.SortingFocusTraversalPolicy, java.awt.FocusTraversalPolicy
    public Component getComponentAfter(Container container, Component component) {
        if (container == null || component == null) {
            throw new IllegalArgumentException("aContainer and aComponent cannot be null");
        }
        Comparator<? super Component> comparator = getComparator();
        if (comparator instanceof LayoutComparator) {
            ((LayoutComparator) comparator).setComponentOrientation(container.getComponentOrientation());
        }
        return super.getComponentAfter(container, component);
    }

    @Override // javax.swing.SortingFocusTraversalPolicy, java.awt.FocusTraversalPolicy
    public Component getComponentBefore(Container container, Component component) {
        if (container == null || component == null) {
            throw new IllegalArgumentException("aContainer and aComponent cannot be null");
        }
        Comparator<? super Component> comparator = getComparator();
        if (comparator instanceof LayoutComparator) {
            ((LayoutComparator) comparator).setComponentOrientation(container.getComponentOrientation());
        }
        return super.getComponentBefore(container, component);
    }

    @Override // javax.swing.SortingFocusTraversalPolicy, java.awt.FocusTraversalPolicy
    public Component getFirstComponent(Container container) {
        if (container == null) {
            throw new IllegalArgumentException("aContainer cannot be null");
        }
        Comparator<? super Component> comparator = getComparator();
        if (comparator instanceof LayoutComparator) {
            ((LayoutComparator) comparator).setComponentOrientation(container.getComponentOrientation());
        }
        return super.getFirstComponent(container);
    }

    @Override // javax.swing.SortingFocusTraversalPolicy, java.awt.FocusTraversalPolicy
    public Component getLastComponent(Container container) {
        if (container == null) {
            throw new IllegalArgumentException("aContainer cannot be null");
        }
        Comparator<? super Component> comparator = getComparator();
        if (comparator instanceof LayoutComparator) {
            ((LayoutComparator) comparator).setComponentOrientation(container.getComponentOrientation());
        }
        return super.getLastComponent(container);
    }

    @Override // javax.swing.SortingFocusTraversalPolicy
    protected boolean accept(Component component) {
        InputMap inputMap;
        if (!super.accept(component)) {
            return false;
        }
        if (SunToolkit.isInstanceOf(component, "javax.swing.JTable")) {
            return true;
        }
        if (SunToolkit.isInstanceOf(component, "javax.swing.JComboBox")) {
            JComboBox jComboBox = (JComboBox) component;
            return jComboBox.getUI().isFocusTraversable(jComboBox);
        }
        if (component instanceof JComponent) {
            InputMap inputMap2 = ((JComponent) component).getInputMap(0, false);
            while (true) {
                inputMap = inputMap2;
                if (inputMap == null || inputMap.size() != 0) {
                    break;
                }
                inputMap2 = inputMap.getParent();
            }
            if (inputMap != null) {
                return true;
            }
        }
        return fitnessTestPolicy.accept(component);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeObject(getComparator());
        objectOutputStream.writeBoolean(getImplicitDownCycleTraversal());
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        setComparator((Comparator) objectInputStream.readObject());
        setImplicitDownCycleTraversal(objectInputStream.readBoolean());
    }
}
