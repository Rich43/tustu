package javax.swing;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Vector;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import sun.awt.AWTAccessor;
import sun.awt.AppContext;
import sun.security.pkcs11.wrapper.Constants;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:javax/swing/MenuSelectionManager.class */
public class MenuSelectionManager {
    private static final boolean TRACE = false;
    private static final boolean VERBOSE = false;
    private static final boolean DEBUG = false;
    private static final StringBuilder MENU_SELECTION_MANAGER_KEY = new StringBuilder("javax.swing.MenuSelectionManager");
    private Vector<MenuElement> selection = new Vector<>();
    protected transient ChangeEvent changeEvent = null;
    protected EventListenerList listenerList = new EventListenerList();

    public static MenuSelectionManager defaultManager() {
        MenuSelectionManager menuSelectionManager;
        synchronized (MENU_SELECTION_MANAGER_KEY) {
            AppContext appContext = AppContext.getAppContext();
            MenuSelectionManager menuSelectionManager2 = (MenuSelectionManager) appContext.get(MENU_SELECTION_MANAGER_KEY);
            if (menuSelectionManager2 == null) {
                menuSelectionManager2 = new MenuSelectionManager();
                appContext.put(MENU_SELECTION_MANAGER_KEY, menuSelectionManager2);
                Object obj = appContext.get(SwingUtilities2.MENU_SELECTION_MANAGER_LISTENER_KEY);
                if (obj != null && (obj instanceof ChangeListener)) {
                    menuSelectionManager2.addChangeListener((ChangeListener) obj);
                }
            }
            menuSelectionManager = menuSelectionManager2;
        }
        return menuSelectionManager;
    }

    public void setSelectedPath(MenuElement[] menuElementArr) {
        int size = this.selection.size();
        int i2 = 0;
        if (menuElementArr == null) {
            menuElementArr = new MenuElement[0];
        }
        int length = menuElementArr.length;
        for (int i3 = 0; i3 < length && i3 < size && this.selection.elementAt(i3) == menuElementArr[i3]; i3++) {
            i2++;
        }
        for (int i4 = size - 1; i4 >= i2; i4--) {
            MenuElement menuElementElementAt = this.selection.elementAt(i4);
            this.selection.removeElementAt(i4);
            menuElementElementAt.menuSelectionChanged(false);
        }
        int length2 = menuElementArr.length;
        for (int i5 = i2; i5 < length2; i5++) {
            if (menuElementArr[i5] != null) {
                this.selection.addElement(menuElementArr[i5]);
                menuElementArr[i5].menuSelectionChanged(true);
            }
        }
        fireStateChanged();
    }

    public MenuElement[] getSelectedPath() {
        MenuElement[] menuElementArr = new MenuElement[this.selection.size()];
        int size = this.selection.size();
        for (int i2 = 0; i2 < size; i2++) {
            menuElementArr[i2] = this.selection.elementAt(i2);
        }
        return menuElementArr;
    }

    public void clearSelectedPath() {
        if (this.selection.size() > 0) {
            setSelectedPath(null);
        }
    }

    public void addChangeListener(ChangeListener changeListener) {
        this.listenerList.add(ChangeListener.class, changeListener);
    }

    public void removeChangeListener(ChangeListener changeListener) {
        this.listenerList.remove(ChangeListener.class, changeListener);
    }

    public ChangeListener[] getChangeListeners() {
        return (ChangeListener[]) this.listenerList.getListeners(ChangeListener.class);
    }

    protected void fireStateChanged() {
        Object[] listenerList = this.listenerList.getListenerList();
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == ChangeListener.class) {
                if (this.changeEvent == null) {
                    this.changeEvent = new ChangeEvent(this);
                }
                ((ChangeListener) listenerList[length + 1]).stateChanged(this.changeEvent);
            }
        }
    }

    public void processMouseEvent(MouseEvent mouseEvent) {
        int width;
        int height;
        Point point = mouseEvent.getPoint();
        Component component = mouseEvent.getComponent();
        if (component != null && !component.isShowing()) {
            return;
        }
        int id = mouseEvent.getID();
        int modifiers = mouseEvent.getModifiers();
        if ((id == 504 || id == 505) && (modifiers & 28) != 0) {
            return;
        }
        if (component != null) {
            SwingUtilities.convertPointToScreen(point, component);
        }
        int i2 = point.f12370x;
        int i3 = point.f12371y;
        Vector vector = (Vector) this.selection.clone();
        boolean z2 = false;
        for (int size = vector.size() - 1; size >= 0 && !z2; size--) {
            MenuElement[] subElements = ((MenuElement) vector.elementAt(size)).getSubElements();
            MenuElement[] menuElementArr = null;
            int length = subElements.length;
            for (int i4 = 0; i4 < length && !z2; i4++) {
                if (subElements[i4] != null) {
                    Component component2 = subElements[i4].getComponent();
                    if (component2.isShowing()) {
                        if (component2 instanceof JComponent) {
                            width = component2.getWidth();
                            height = component2.getHeight();
                        } else {
                            Rectangle bounds = component2.getBounds();
                            width = bounds.width;
                            height = bounds.height;
                        }
                        point.f12370x = i2;
                        point.f12371y = i3;
                        SwingUtilities.convertPointFromScreen(point, component2);
                        if (point.f12370x >= 0 && point.f12370x < width && point.f12371y >= 0 && point.f12371y < height) {
                            if (menuElementArr == null) {
                                menuElementArr = new MenuElement[size + 2];
                                for (int i5 = 0; i5 <= size; i5++) {
                                    menuElementArr[i5] = (MenuElement) vector.elementAt(i5);
                                }
                            }
                            menuElementArr[size + 1] = subElements[i4];
                            MenuElement[] selectedPath = getSelectedPath();
                            if (selectedPath[selectedPath.length - 1] != menuElementArr[size + 1] && (selectedPath.length < 2 || selectedPath[selectedPath.length - 2] != menuElementArr[size + 1])) {
                                MouseEvent mouseEvent2 = new MouseEvent(selectedPath[selectedPath.length - 1].getComponent(), 505, mouseEvent.getWhen(), mouseEvent.getModifiers(), point.f12370x, point.f12371y, mouseEvent.getXOnScreen(), mouseEvent.getYOnScreen(), mouseEvent.getClickCount(), mouseEvent.isPopupTrigger(), 0);
                                AWTAccessor.MouseEventAccessor mouseEventAccessor = AWTAccessor.getMouseEventAccessor();
                                mouseEventAccessor.setCausedByTouchEvent(mouseEvent2, mouseEventAccessor.isCausedByTouchEvent(mouseEvent));
                                selectedPath[selectedPath.length - 1].processMouseEvent(mouseEvent2, menuElementArr, this);
                                MouseEvent mouseEvent3 = new MouseEvent(component2, 504, mouseEvent.getWhen(), mouseEvent.getModifiers(), point.f12370x, point.f12371y, mouseEvent.getXOnScreen(), mouseEvent.getYOnScreen(), mouseEvent.getClickCount(), mouseEvent.isPopupTrigger(), 0);
                                mouseEventAccessor.setCausedByTouchEvent(mouseEvent3, mouseEventAccessor.isCausedByTouchEvent(mouseEvent));
                                subElements[i4].processMouseEvent(mouseEvent3, menuElementArr, this);
                            }
                            MouseEvent mouseEvent4 = new MouseEvent(component2, mouseEvent.getID(), mouseEvent.getWhen(), mouseEvent.getModifiers(), point.f12370x, point.f12371y, mouseEvent.getXOnScreen(), mouseEvent.getYOnScreen(), mouseEvent.getClickCount(), mouseEvent.isPopupTrigger(), 0);
                            AWTAccessor.MouseEventAccessor mouseEventAccessor2 = AWTAccessor.getMouseEventAccessor();
                            mouseEventAccessor2.setCausedByTouchEvent(mouseEvent4, mouseEventAccessor2.isCausedByTouchEvent(mouseEvent));
                            subElements[i4].processMouseEvent(mouseEvent4, menuElementArr, this);
                            z2 = true;
                            mouseEvent.consume();
                        }
                    }
                }
            }
        }
    }

    private void printMenuElementArray(MenuElement[] menuElementArr) {
        printMenuElementArray(menuElementArr, false);
    }

    private void printMenuElementArray(MenuElement[] menuElementArr, boolean z2) {
        System.out.println("Path is(");
        int length = menuElementArr.length;
        for (int i2 = 0; i2 < length; i2++) {
            for (int i3 = 0; i3 <= i2; i3++) {
                System.out.print(Constants.INDENT);
            }
            MenuElement menuElement = menuElementArr[i2];
            if (menuElement instanceof JMenuItem) {
                System.out.println(((JMenuItem) menuElement).getText() + ", ");
            } else if (menuElement instanceof JMenuBar) {
                System.out.println("JMenuBar, ");
            } else if (menuElement instanceof JPopupMenu) {
                System.out.println("JPopupMenu, ");
            } else if (menuElement == null) {
                System.out.println("NULL , ");
            } else {
                System.out.println("" + ((Object) menuElement) + ", ");
            }
        }
        System.out.println(")");
        if (z2) {
            Thread.dumpStack();
        }
    }

    public Component componentForPoint(Component component, Point point) {
        int width;
        int height;
        SwingUtilities.convertPointToScreen(point, component);
        int i2 = point.f12370x;
        int i3 = point.f12371y;
        Vector vector = (Vector) this.selection.clone();
        for (int size = vector.size() - 1; size >= 0; size--) {
            MenuElement[] subElements = ((MenuElement) vector.elementAt(size)).getSubElements();
            int length = subElements.length;
            for (int i4 = 0; i4 < length; i4++) {
                if (subElements[i4] != null) {
                    Component component2 = subElements[i4].getComponent();
                    if (component2.isShowing()) {
                        if (component2 instanceof JComponent) {
                            width = component2.getWidth();
                            height = component2.getHeight();
                        } else {
                            Rectangle bounds = component2.getBounds();
                            width = bounds.width;
                            height = bounds.height;
                        }
                        point.f12370x = i2;
                        point.f12371y = i3;
                        SwingUtilities.convertPointFromScreen(point, component2);
                        if (point.f12370x >= 0 && point.f12370x < width && point.f12371y >= 0 && point.f12371y < height) {
                            return component2;
                        }
                    } else {
                        continue;
                    }
                }
            }
        }
        return null;
    }

    public void processKeyEvent(KeyEvent keyEvent) {
        MenuElement[] menuElementArr = (MenuElement[]) this.selection.toArray(new MenuElement[0]);
        int length = menuElementArr.length;
        if (length < 1) {
            return;
        }
        for (int i2 = length - 1; i2 >= 0; i2--) {
            MenuElement[] subElements = menuElementArr[i2].getSubElements();
            MenuElement[] menuElementArr2 = null;
            for (int i3 = 0; i3 < subElements.length; i3++) {
                if (subElements[i3] != null && subElements[i3].getComponent().isShowing() && subElements[i3].getComponent().isEnabled()) {
                    if (menuElementArr2 == null) {
                        menuElementArr2 = new MenuElement[i2 + 2];
                        System.arraycopy(menuElementArr, 0, menuElementArr2, 0, i2 + 1);
                    }
                    menuElementArr2[i2 + 1] = subElements[i3];
                    subElements[i3].processKeyEvent(keyEvent, menuElementArr2, this);
                    if (keyEvent.isConsumed()) {
                        return;
                    }
                }
            }
        }
        MenuElement[] menuElementArr3 = {menuElementArr[0]};
        menuElementArr3[0].processKeyEvent(keyEvent, menuElementArr3, this);
        if (keyEvent.isConsumed()) {
        }
    }

    public boolean isComponentPartOfCurrentMenu(Component component) {
        if (this.selection.size() > 0) {
            return isComponentPartOfCurrentMenu(this.selection.elementAt(0), component);
        }
        return false;
    }

    private boolean isComponentPartOfCurrentMenu(MenuElement menuElement, Component component) {
        if (menuElement == null) {
            return false;
        }
        if (menuElement.getComponent() == component) {
            return true;
        }
        for (MenuElement menuElement2 : menuElement.getSubElements()) {
            if (isComponentPartOfCurrentMenu(menuElement2, component)) {
                return true;
            }
        }
        return false;
    }
}
