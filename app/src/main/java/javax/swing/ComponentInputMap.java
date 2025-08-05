package javax.swing;

/* loaded from: rt.jar:javax/swing/ComponentInputMap.class */
public class ComponentInputMap extends InputMap {
    private JComponent component;

    public ComponentInputMap(JComponent jComponent) {
        this.component = jComponent;
        if (jComponent == null) {
            throw new IllegalArgumentException("ComponentInputMaps must be associated with a non-null JComponent");
        }
    }

    @Override // javax.swing.InputMap
    public void setParent(InputMap inputMap) {
        if (getParent() == inputMap) {
            return;
        }
        if (inputMap != null && (!(inputMap instanceof ComponentInputMap) || ((ComponentInputMap) inputMap).getComponent() != getComponent())) {
            throw new IllegalArgumentException("ComponentInputMaps must have a parent ComponentInputMap associated with the same component");
        }
        super.setParent(inputMap);
        getComponent().componentInputMapChanged(this);
    }

    public JComponent getComponent() {
        return this.component;
    }

    @Override // javax.swing.InputMap
    public void put(KeyStroke keyStroke, Object obj) {
        super.put(keyStroke, obj);
        if (getComponent() != null) {
            getComponent().componentInputMapChanged(this);
        }
    }

    @Override // javax.swing.InputMap
    public void remove(KeyStroke keyStroke) {
        super.remove(keyStroke);
        if (getComponent() != null) {
            getComponent().componentInputMapChanged(this);
        }
    }

    @Override // javax.swing.InputMap
    public void clear() {
        int size = size();
        super.clear();
        if (size > 0 && getComponent() != null) {
            getComponent().componentInputMapChanged(this);
        }
    }
}
