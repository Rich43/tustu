package javax.swing.plaf.basic;

import java.lang.reflect.InvocationTargetException;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.ActionMapUIResource;

/* loaded from: rt.jar:javax/swing/plaf/basic/LazyActionMap.class */
class LazyActionMap extends ActionMapUIResource {
    private transient Object _loader;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !LazyActionMap.class.desiredAssertionStatus();
    }

    static void installLazyActionMap(JComponent jComponent, Class cls, String str) {
        ActionMap lazyActionMap = (ActionMap) UIManager.get(str);
        if (lazyActionMap == null) {
            lazyActionMap = new LazyActionMap(cls);
            UIManager.getLookAndFeelDefaults().put(str, lazyActionMap);
        }
        SwingUtilities.replaceUIActionMap(jComponent, lazyActionMap);
    }

    static ActionMap getActionMap(Class cls, String str) {
        ActionMap lazyActionMap = (ActionMap) UIManager.get(str);
        if (lazyActionMap == null) {
            lazyActionMap = new LazyActionMap(cls);
            UIManager.getLookAndFeelDefaults().put(str, lazyActionMap);
        }
        return lazyActionMap;
    }

    private LazyActionMap(Class cls) {
        this._loader = cls;
    }

    public void put(Action action) {
        put(action.getValue("Name"), action);
    }

    @Override // javax.swing.ActionMap
    public void put(Object obj, Action action) {
        loadIfNecessary();
        super.put(obj, action);
    }

    @Override // javax.swing.ActionMap
    public Action get(Object obj) {
        loadIfNecessary();
        return super.get(obj);
    }

    @Override // javax.swing.ActionMap
    public void remove(Object obj) {
        loadIfNecessary();
        super.remove(obj);
    }

    @Override // javax.swing.ActionMap
    public void clear() {
        loadIfNecessary();
        super.clear();
    }

    @Override // javax.swing.ActionMap
    public Object[] keys() {
        loadIfNecessary();
        return super.keys();
    }

    @Override // javax.swing.ActionMap
    public int size() {
        loadIfNecessary();
        return super.size();
    }

    @Override // javax.swing.ActionMap
    public Object[] allKeys() {
        loadIfNecessary();
        return super.allKeys();
    }

    @Override // javax.swing.ActionMap
    public void setParent(ActionMap actionMap) {
        loadIfNecessary();
        super.setParent(actionMap);
    }

    private void loadIfNecessary() {
        if (this._loader != null) {
            Object obj = this._loader;
            this._loader = null;
            Class cls = (Class) obj;
            try {
                cls.getDeclaredMethod("loadActionMap", LazyActionMap.class).invoke(cls, this);
            } catch (IllegalAccessException e2) {
                if (!$assertionsDisabled) {
                    throw new AssertionError((Object) ("LazyActionMap unable to load actions " + ((Object) e2)));
                }
            } catch (IllegalArgumentException e3) {
                if (!$assertionsDisabled) {
                    throw new AssertionError((Object) ("LazyActionMap unable to load actions " + ((Object) e3)));
                }
            } catch (NoSuchMethodException e4) {
                if (!$assertionsDisabled) {
                    throw new AssertionError((Object) ("LazyActionMap unable to load actions " + ((Object) cls)));
                }
            } catch (InvocationTargetException e5) {
                if (!$assertionsDisabled) {
                    throw new AssertionError((Object) ("LazyActionMap unable to load actions " + ((Object) e5)));
                }
            }
        }
    }
}
