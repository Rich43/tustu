package javax.swing;

import java.applet.Applet;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.IllegalComponentStateException;
import java.awt.Image;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.lang.reflect.InvocationTargetException;
import java.security.AccessController;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleComponent;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleStateSet;
import javax.swing.JComponent;
import javax.swing.TransferHandler;
import javax.swing.event.MenuDragMouseEvent;
import javax.swing.plaf.UIResource;
import javax.swing.text.View;
import sun.awt.AWTAccessor;
import sun.awt.AppContext;
import sun.reflect.misc.ReflectUtil;
import sun.security.action.GetPropertyAction;
import sun.swing.SwingUtilities2;
import sun.swing.UIAction;

/* loaded from: rt.jar:javax/swing/SwingUtilities.class */
public class SwingUtilities implements SwingConstants {
    private static boolean suppressDropSupport;
    private static boolean checkedSuppressDropSupport;
    private static boolean canAccessEventQueue = false;
    private static boolean eventQueueTested = false;
    private static final Object sharedOwnerFrameKey = new StringBuffer("SwingUtilities.sharedOwnerFrame");

    private static boolean getSuppressDropTarget() {
        if (!checkedSuppressDropSupport) {
            suppressDropSupport = Boolean.valueOf((String) AccessController.doPrivileged(new GetPropertyAction("suppressSwingDropSupport"))).booleanValue();
            checkedSuppressDropSupport = true;
        }
        return suppressDropSupport;
    }

    static void installSwingDropTargetAsNecessary(Component component, TransferHandler transferHandler) {
        if (!getSuppressDropTarget()) {
            DropTarget dropTarget = component.getDropTarget();
            if (dropTarget == null || (dropTarget instanceof UIResource)) {
                if (transferHandler == null) {
                    component.setDropTarget(null);
                } else if (!GraphicsEnvironment.isHeadless()) {
                    component.setDropTarget(new TransferHandler.SwingDropTarget(component));
                }
            }
        }
    }

    public static final boolean isRectangleContainingRectangle(Rectangle rectangle, Rectangle rectangle2) {
        return rectangle2.f12372x >= rectangle.f12372x && rectangle2.f12372x + rectangle2.width <= rectangle.f12372x + rectangle.width && rectangle2.f12373y >= rectangle.f12373y && rectangle2.f12373y + rectangle2.height <= rectangle.f12373y + rectangle.height;
    }

    public static Rectangle getLocalBounds(Component component) {
        Rectangle rectangle = new Rectangle(component.getBounds());
        rectangle.f12373y = 0;
        rectangle.f12372x = 0;
        return rectangle;
    }

    public static Window getWindowAncestor(Component component) {
        Container parent = component.getParent();
        while (true) {
            Container container = parent;
            if (container != null) {
                if (!(container instanceof Window)) {
                    parent = container.getParent();
                } else {
                    return (Window) container;
                }
            } else {
                return null;
            }
        }
    }

    static Point convertScreenLocationToParent(Container container, int i2, int i3) {
        Container parent = container;
        while (true) {
            Container container2 = parent;
            if (container2 != null) {
                if (!(container2 instanceof Window)) {
                    parent = container2.getParent();
                } else {
                    Point point = new Point(i2, i3);
                    convertPointFromScreen(point, container);
                    return point;
                }
            } else {
                throw new Error("convertScreenLocationToParent: no window ancestor");
            }
        }
    }

    public static Point convertPoint(Component component, Point point, Component component2) {
        if (component == null && component2 == null) {
            return point;
        }
        if (component == null) {
            component = getWindowAncestor(component2);
            if (component == null) {
                throw new Error("Source component not connected to component tree hierarchy");
            }
        }
        Point point2 = new Point(point);
        convertPointToScreen(point2, component);
        if (component2 == null) {
            component2 = getWindowAncestor(component);
            if (component2 == null) {
                throw new Error("Destination component not connected to component tree hierarchy");
            }
        }
        convertPointFromScreen(point2, component2);
        return point2;
    }

    public static Point convertPoint(Component component, int i2, int i3, Component component2) {
        return convertPoint(component, new Point(i2, i3), component2);
    }

    public static Rectangle convertRectangle(Component component, Rectangle rectangle, Component component2) {
        Point pointConvertPoint = convertPoint(component, new Point(rectangle.f12372x, rectangle.f12373y), component2);
        return new Rectangle(pointConvertPoint.f12370x, pointConvertPoint.f12371y, rectangle.width, rectangle.height);
    }

    public static Container getAncestorOfClass(Class<?> cls, Component component) {
        Container container;
        if (component == null || cls == null) {
            return null;
        }
        Container parent = component.getParent();
        while (true) {
            container = parent;
            if (container == null || cls.isInstance(container)) {
                break;
            }
            parent = container.getParent();
        }
        return container;
    }

    public static Container getAncestorNamed(String str, Component component) {
        Container container;
        if (component == null || str == null) {
            return null;
        }
        Container parent = component.getParent();
        while (true) {
            container = parent;
            if (container == null || str.equals(container.getName())) {
                break;
            }
            parent = container.getParent();
        }
        return container;
    }

    public static Component getDeepestComponentAt(Component component, int i2, int i3) {
        Component componentAt;
        if (!component.contains(i2, i3)) {
            return null;
        }
        if (component instanceof Container) {
            for (Component component2 : ((Container) component).getComponents()) {
                if (component2 != null && component2.isVisible()) {
                    Point location = component2.getLocation();
                    if (component2 instanceof Container) {
                        componentAt = getDeepestComponentAt(component2, i2 - location.f12370x, i3 - location.f12371y);
                    } else {
                        componentAt = component2.getComponentAt(i2 - location.f12370x, i3 - location.f12371y);
                    }
                    if (componentAt != null && componentAt.isVisible()) {
                        return componentAt;
                    }
                }
            }
        }
        return component;
    }

    public static MouseEvent convertMouseEvent(Component component, MouseEvent mouseEvent, Component component2) {
        Component component3;
        MouseEvent mouseEvent2;
        Point pointConvertPoint = convertPoint(component, new Point(mouseEvent.getX(), mouseEvent.getY()), component2);
        if (component2 != null) {
            component3 = component2;
        } else {
            component3 = component;
        }
        if (mouseEvent instanceof MouseWheelEvent) {
            MouseWheelEvent mouseWheelEvent = (MouseWheelEvent) mouseEvent;
            mouseEvent2 = new MouseWheelEvent(component3, mouseWheelEvent.getID(), mouseWheelEvent.getWhen(), mouseWheelEvent.getModifiers() | mouseWheelEvent.getModifiersEx(), pointConvertPoint.f12370x, pointConvertPoint.f12371y, mouseWheelEvent.getXOnScreen(), mouseWheelEvent.getYOnScreen(), mouseWheelEvent.getClickCount(), mouseWheelEvent.isPopupTrigger(), mouseWheelEvent.getScrollType(), mouseWheelEvent.getScrollAmount(), mouseWheelEvent.getWheelRotation());
        } else if (mouseEvent instanceof MenuDragMouseEvent) {
            MenuDragMouseEvent menuDragMouseEvent = (MenuDragMouseEvent) mouseEvent;
            mouseEvent2 = new MenuDragMouseEvent(component3, menuDragMouseEvent.getID(), menuDragMouseEvent.getWhen(), menuDragMouseEvent.getModifiers() | menuDragMouseEvent.getModifiersEx(), pointConvertPoint.f12370x, pointConvertPoint.f12371y, menuDragMouseEvent.getXOnScreen(), menuDragMouseEvent.getYOnScreen(), menuDragMouseEvent.getClickCount(), menuDragMouseEvent.isPopupTrigger(), menuDragMouseEvent.getPath(), menuDragMouseEvent.getMenuSelectionManager());
        } else {
            mouseEvent2 = new MouseEvent(component3, mouseEvent.getID(), mouseEvent.getWhen(), mouseEvent.getModifiers() | mouseEvent.getModifiersEx(), pointConvertPoint.f12370x, pointConvertPoint.f12371y, mouseEvent.getXOnScreen(), mouseEvent.getYOnScreen(), mouseEvent.getClickCount(), mouseEvent.isPopupTrigger(), mouseEvent.getButton());
            AWTAccessor.MouseEventAccessor mouseEventAccessor = AWTAccessor.getMouseEventAccessor();
            mouseEventAccessor.setCausedByTouchEvent(mouseEvent2, mouseEventAccessor.isCausedByTouchEvent(mouseEvent));
        }
        return mouseEvent2;
    }

    public static void convertPointToScreen(Point point, Component component) {
        int x2;
        int y2;
        do {
            if (component instanceof JComponent) {
                x2 = component.getX();
                y2 = component.getY();
            } else if ((component instanceof Applet) || (component instanceof Window)) {
                try {
                    Point locationOnScreen = component.getLocationOnScreen();
                    x2 = locationOnScreen.f12370x;
                    y2 = locationOnScreen.f12371y;
                } catch (IllegalComponentStateException e2) {
                    x2 = component.getX();
                    y2 = component.getY();
                }
            } else {
                x2 = component.getX();
                y2 = component.getY();
            }
            point.f12370x += x2;
            point.f12371y += y2;
            if (!(component instanceof Window) && !(component instanceof Applet)) {
                component = component.getParent();
            } else {
                return;
            }
        } while (component != null);
    }

    public static void convertPointFromScreen(Point point, Component component) {
        int x2;
        int y2;
        do {
            if (component instanceof JComponent) {
                x2 = component.getX();
                y2 = component.getY();
            } else if ((component instanceof Applet) || (component instanceof Window)) {
                try {
                    Point locationOnScreen = component.getLocationOnScreen();
                    x2 = locationOnScreen.f12370x;
                    y2 = locationOnScreen.f12371y;
                } catch (IllegalComponentStateException e2) {
                    x2 = component.getX();
                    y2 = component.getY();
                }
            } else {
                x2 = component.getX();
                y2 = component.getY();
            }
            point.f12370x -= x2;
            point.f12371y -= y2;
            if (!(component instanceof Window) && !(component instanceof Applet)) {
                component = component.getParent();
            } else {
                return;
            }
        } while (component != null);
    }

    public static Window windowForComponent(Component component) {
        return getWindowAncestor(component);
    }

    public static boolean isDescendingFrom(Component component, Component component2) {
        if (component == component2) {
            return true;
        }
        Container parent = component.getParent();
        while (true) {
            Container container = parent;
            if (container != null) {
                if (container != component2) {
                    parent = container.getParent();
                } else {
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    public static Rectangle computeIntersection(int i2, int i3, int i4, int i5, Rectangle rectangle) {
        int i6 = i2 > rectangle.f12372x ? i2 : rectangle.f12372x;
        int i7 = i2 + i4 < rectangle.f12372x + rectangle.width ? i2 + i4 : rectangle.f12372x + rectangle.width;
        int i8 = i3 > rectangle.f12373y ? i3 : rectangle.f12373y;
        int i9 = i3 + i5 < rectangle.f12373y + rectangle.height ? i3 + i5 : rectangle.f12373y + rectangle.height;
        rectangle.f12372x = i6;
        rectangle.f12373y = i8;
        rectangle.width = i7 - i6;
        rectangle.height = i9 - i8;
        if (rectangle.width < 0 || rectangle.height < 0) {
            rectangle.height = 0;
            rectangle.width = 0;
            rectangle.f12373y = 0;
            rectangle.f12372x = 0;
        }
        return rectangle;
    }

    public static Rectangle computeUnion(int i2, int i3, int i4, int i5, Rectangle rectangle) {
        int i6 = i2 < rectangle.f12372x ? i2 : rectangle.f12372x;
        int i7 = i2 + i4 > rectangle.f12372x + rectangle.width ? i2 + i4 : rectangle.f12372x + rectangle.width;
        int i8 = i3 < rectangle.f12373y ? i3 : rectangle.f12373y;
        int i9 = i3 + i5 > rectangle.f12373y + rectangle.height ? i3 + i5 : rectangle.f12373y + rectangle.height;
        rectangle.f12372x = i6;
        rectangle.f12373y = i8;
        rectangle.width = i7 - i6;
        rectangle.height = i9 - i8;
        return rectangle;
    }

    public static Rectangle[] computeDifference(Rectangle rectangle, Rectangle rectangle2) {
        if (rectangle2 == null || !rectangle.intersects(rectangle2) || isRectangleContainingRectangle(rectangle2, rectangle)) {
            return new Rectangle[0];
        }
        Rectangle rectangle3 = new Rectangle();
        Rectangle rectangle4 = null;
        Rectangle rectangle5 = null;
        Rectangle rectangle6 = null;
        Rectangle rectangle7 = null;
        int i2 = 0;
        if (isRectangleContainingRectangle(rectangle, rectangle2)) {
            rectangle3.f12372x = rectangle.f12372x;
            rectangle3.f12373y = rectangle.f12373y;
            rectangle3.width = rectangle2.f12372x - rectangle.f12372x;
            rectangle3.height = rectangle.height;
            if (rectangle3.width > 0 && rectangle3.height > 0) {
                rectangle4 = new Rectangle(rectangle3);
                i2 = 0 + 1;
            }
            rectangle3.f12372x = rectangle2.f12372x;
            rectangle3.f12373y = rectangle.f12373y;
            rectangle3.width = rectangle2.width;
            rectangle3.height = rectangle2.f12373y - rectangle.f12373y;
            if (rectangle3.width > 0 && rectangle3.height > 0) {
                rectangle5 = new Rectangle(rectangle3);
                i2++;
            }
            rectangle3.f12372x = rectangle2.f12372x;
            rectangle3.f12373y = rectangle2.f12373y + rectangle2.height;
            rectangle3.width = rectangle2.width;
            rectangle3.height = (rectangle.f12373y + rectangle.height) - (rectangle2.f12373y + rectangle2.height);
            if (rectangle3.width > 0 && rectangle3.height > 0) {
                rectangle6 = new Rectangle(rectangle3);
                i2++;
            }
            rectangle3.f12372x = rectangle2.f12372x + rectangle2.width;
            rectangle3.f12373y = rectangle.f12373y;
            rectangle3.width = (rectangle.f12372x + rectangle.width) - (rectangle2.f12372x + rectangle2.width);
            rectangle3.height = rectangle.height;
            if (rectangle3.width > 0 && rectangle3.height > 0) {
                rectangle7 = new Rectangle(rectangle3);
                i2++;
            }
        } else if (rectangle2.f12372x <= rectangle.f12372x && rectangle2.f12373y <= rectangle.f12373y) {
            if (rectangle2.f12372x + rectangle2.width > rectangle.f12372x + rectangle.width) {
                rectangle3.f12372x = rectangle.f12372x;
                rectangle3.f12373y = rectangle2.f12373y + rectangle2.height;
                rectangle3.width = rectangle.width;
                rectangle3.height = (rectangle.f12373y + rectangle.height) - (rectangle2.f12373y + rectangle2.height);
                if (rectangle3.width > 0 && rectangle3.height > 0) {
                    rectangle4 = rectangle3;
                    i2 = 0 + 1;
                }
            } else if (rectangle2.f12373y + rectangle2.height > rectangle.f12373y + rectangle.height) {
                rectangle3.setBounds(rectangle2.f12372x + rectangle2.width, rectangle.f12373y, (rectangle.f12372x + rectangle.width) - (rectangle2.f12372x + rectangle2.width), rectangle.height);
                if (rectangle3.width > 0 && rectangle3.height > 0) {
                    rectangle4 = rectangle3;
                    i2 = 0 + 1;
                }
            } else {
                rectangle3.setBounds(rectangle2.f12372x + rectangle2.width, rectangle.f12373y, (rectangle.f12372x + rectangle.width) - (rectangle2.f12372x + rectangle2.width), (rectangle2.f12373y + rectangle2.height) - rectangle.f12373y);
                if (rectangle3.width > 0 && rectangle3.height > 0) {
                    rectangle4 = new Rectangle(rectangle3);
                    i2 = 0 + 1;
                }
                rectangle3.setBounds(rectangle.f12372x, rectangle2.f12373y + rectangle2.height, rectangle.width, (rectangle.f12373y + rectangle.height) - (rectangle2.f12373y + rectangle2.height));
                if (rectangle3.width > 0 && rectangle3.height > 0) {
                    rectangle5 = new Rectangle(rectangle3);
                    i2++;
                }
            }
        } else if (rectangle2.f12372x <= rectangle.f12372x && rectangle2.f12373y + rectangle2.height >= rectangle.f12373y + rectangle.height) {
            if (rectangle2.f12372x + rectangle2.width > rectangle.f12372x + rectangle.width) {
                rectangle3.setBounds(rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle2.f12373y - rectangle.f12373y);
                if (rectangle3.width > 0 && rectangle3.height > 0) {
                    rectangle4 = rectangle3;
                    i2 = 0 + 1;
                }
            } else {
                rectangle3.setBounds(rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle2.f12373y - rectangle.f12373y);
                if (rectangle3.width > 0 && rectangle3.height > 0) {
                    rectangle4 = new Rectangle(rectangle3);
                    i2 = 0 + 1;
                }
                rectangle3.setBounds(rectangle2.f12372x + rectangle2.width, rectangle2.f12373y, (rectangle.f12372x + rectangle.width) - (rectangle2.f12372x + rectangle2.width), (rectangle.f12373y + rectangle.height) - rectangle2.f12373y);
                if (rectangle3.width > 0 && rectangle3.height > 0) {
                    rectangle5 = new Rectangle(rectangle3);
                    i2++;
                }
            }
        } else if (rectangle2.f12372x <= rectangle.f12372x) {
            if (rectangle2.f12372x + rectangle2.width >= rectangle.f12372x + rectangle.width) {
                rectangle3.setBounds(rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle2.f12373y - rectangle.f12373y);
                if (rectangle3.width > 0 && rectangle3.height > 0) {
                    rectangle4 = new Rectangle(rectangle3);
                    i2 = 0 + 1;
                }
                rectangle3.setBounds(rectangle.f12372x, rectangle2.f12373y + rectangle2.height, rectangle.width, (rectangle.f12373y + rectangle.height) - (rectangle2.f12373y + rectangle2.height));
                if (rectangle3.width > 0 && rectangle3.height > 0) {
                    rectangle5 = new Rectangle(rectangle3);
                    i2++;
                }
            } else {
                rectangle3.setBounds(rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle2.f12373y - rectangle.f12373y);
                if (rectangle3.width > 0 && rectangle3.height > 0) {
                    rectangle4 = new Rectangle(rectangle3);
                    i2 = 0 + 1;
                }
                rectangle3.setBounds(rectangle2.f12372x + rectangle2.width, rectangle2.f12373y, (rectangle.f12372x + rectangle.width) - (rectangle2.f12372x + rectangle2.width), rectangle2.height);
                if (rectangle3.width > 0 && rectangle3.height > 0) {
                    rectangle5 = new Rectangle(rectangle3);
                    i2++;
                }
                rectangle3.setBounds(rectangle.f12372x, rectangle2.f12373y + rectangle2.height, rectangle.width, (rectangle.f12373y + rectangle.height) - (rectangle2.f12373y + rectangle2.height));
                if (rectangle3.width > 0 && rectangle3.height > 0) {
                    rectangle6 = new Rectangle(rectangle3);
                    i2++;
                }
            }
        } else if (rectangle2.f12372x <= rectangle.f12372x + rectangle.width && rectangle2.f12372x + rectangle2.width > rectangle.f12372x + rectangle.width) {
            if (rectangle2.f12373y <= rectangle.f12373y && rectangle2.f12373y + rectangle2.height > rectangle.f12373y + rectangle.height) {
                rectangle3.setBounds(rectangle.f12372x, rectangle.f12373y, rectangle2.f12372x - rectangle.f12372x, rectangle.height);
                if (rectangle3.width > 0 && rectangle3.height > 0) {
                    rectangle4 = rectangle3;
                    i2 = 0 + 1;
                }
            } else if (rectangle2.f12373y <= rectangle.f12373y) {
                rectangle3.setBounds(rectangle.f12372x, rectangle.f12373y, rectangle2.f12372x - rectangle.f12372x, (rectangle2.f12373y + rectangle2.height) - rectangle.f12373y);
                if (rectangle3.width > 0 && rectangle3.height > 0) {
                    rectangle4 = new Rectangle(rectangle3);
                    i2 = 0 + 1;
                }
                rectangle3.setBounds(rectangle.f12372x, rectangle2.f12373y + rectangle2.height, rectangle.width, (rectangle.f12373y + rectangle.height) - (rectangle2.f12373y + rectangle2.height));
                if (rectangle3.width > 0 && rectangle3.height > 0) {
                    rectangle5 = new Rectangle(rectangle3);
                    i2++;
                }
            } else if (rectangle2.f12373y + rectangle2.height > rectangle.f12373y + rectangle.height) {
                rectangle3.setBounds(rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle2.f12373y - rectangle.f12373y);
                if (rectangle3.width > 0 && rectangle3.height > 0) {
                    rectangle4 = new Rectangle(rectangle3);
                    i2 = 0 + 1;
                }
                rectangle3.setBounds(rectangle.f12372x, rectangle2.f12373y, rectangle2.f12372x - rectangle.f12372x, (rectangle.f12373y + rectangle.height) - rectangle2.f12373y);
                if (rectangle3.width > 0 && rectangle3.height > 0) {
                    rectangle5 = new Rectangle(rectangle3);
                    i2++;
                }
            } else {
                rectangle3.setBounds(rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle2.f12373y - rectangle.f12373y);
                if (rectangle3.width > 0 && rectangle3.height > 0) {
                    rectangle4 = new Rectangle(rectangle3);
                    i2 = 0 + 1;
                }
                rectangle3.setBounds(rectangle.f12372x, rectangle2.f12373y, rectangle2.f12372x - rectangle.f12372x, rectangle2.height);
                if (rectangle3.width > 0 && rectangle3.height > 0) {
                    rectangle5 = new Rectangle(rectangle3);
                    i2++;
                }
                rectangle3.setBounds(rectangle.f12372x, rectangle2.f12373y + rectangle2.height, rectangle.width, (rectangle.f12373y + rectangle.height) - (rectangle2.f12373y + rectangle2.height));
                if (rectangle3.width > 0 && rectangle3.height > 0) {
                    rectangle6 = new Rectangle(rectangle3);
                    i2++;
                }
            }
        } else if (rectangle2.f12372x >= rectangle.f12372x && rectangle2.f12372x + rectangle2.width <= rectangle.f12372x + rectangle.width) {
            if (rectangle2.f12373y <= rectangle.f12373y && rectangle2.f12373y + rectangle2.height > rectangle.f12373y + rectangle.height) {
                rectangle3.setBounds(rectangle.f12372x, rectangle.f12373y, rectangle2.f12372x - rectangle.f12372x, rectangle.height);
                if (rectangle3.width > 0 && rectangle3.height > 0) {
                    rectangle4 = new Rectangle(rectangle3);
                    i2 = 0 + 1;
                }
                rectangle3.setBounds(rectangle2.f12372x + rectangle2.width, rectangle.f12373y, (rectangle.f12372x + rectangle.width) - (rectangle2.f12372x + rectangle2.width), rectangle.height);
                if (rectangle3.width > 0 && rectangle3.height > 0) {
                    rectangle5 = new Rectangle(rectangle3);
                    i2++;
                }
            } else if (rectangle2.f12373y <= rectangle.f12373y) {
                rectangle3.setBounds(rectangle.f12372x, rectangle.f12373y, rectangle2.f12372x - rectangle.f12372x, rectangle.height);
                if (rectangle3.width > 0 && rectangle3.height > 0) {
                    rectangle4 = new Rectangle(rectangle3);
                    i2 = 0 + 1;
                }
                rectangle3.setBounds(rectangle2.f12372x, rectangle2.f12373y + rectangle2.height, rectangle2.width, (rectangle.f12373y + rectangle.height) - (rectangle2.f12373y + rectangle2.height));
                if (rectangle3.width > 0 && rectangle3.height > 0) {
                    rectangle5 = new Rectangle(rectangle3);
                    i2++;
                }
                rectangle3.setBounds(rectangle2.f12372x + rectangle2.width, rectangle.f12373y, (rectangle.f12372x + rectangle.width) - (rectangle2.f12372x + rectangle2.width), rectangle.height);
                if (rectangle3.width > 0 && rectangle3.height > 0) {
                    rectangle6 = new Rectangle(rectangle3);
                    i2++;
                }
            } else {
                rectangle3.setBounds(rectangle.f12372x, rectangle.f12373y, rectangle2.f12372x - rectangle.f12372x, rectangle.height);
                if (rectangle3.width > 0 && rectangle3.height > 0) {
                    rectangle4 = new Rectangle(rectangle3);
                    i2 = 0 + 1;
                }
                rectangle3.setBounds(rectangle2.f12372x, rectangle.f12373y, rectangle2.width, rectangle2.f12373y - rectangle.f12373y);
                if (rectangle3.width > 0 && rectangle3.height > 0) {
                    rectangle5 = new Rectangle(rectangle3);
                    i2++;
                }
                rectangle3.setBounds(rectangle2.f12372x + rectangle2.width, rectangle.f12373y, (rectangle.f12372x + rectangle.width) - (rectangle2.f12372x + rectangle2.width), rectangle.height);
                if (rectangle3.width > 0 && rectangle3.height > 0) {
                    rectangle6 = new Rectangle(rectangle3);
                    i2++;
                }
            }
        }
        Rectangle[] rectangleArr = new Rectangle[i2];
        int i3 = 0;
        if (rectangle4 != null) {
            i3 = 0 + 1;
            rectangleArr[0] = rectangle4;
        }
        if (rectangle5 != null) {
            int i4 = i3;
            i3++;
            rectangleArr[i4] = rectangle5;
        }
        if (rectangle6 != null) {
            int i5 = i3;
            i3++;
            rectangleArr[i5] = rectangle6;
        }
        if (rectangle7 != null) {
            int i6 = i3;
            int i7 = i3 + 1;
            rectangleArr[i6] = rectangle7;
        }
        return rectangleArr;
    }

    public static boolean isLeftMouseButton(MouseEvent mouseEvent) {
        return (mouseEvent.getModifiersEx() & 1024) != 0 || mouseEvent.getButton() == 1;
    }

    public static boolean isMiddleMouseButton(MouseEvent mouseEvent) {
        return (mouseEvent.getModifiersEx() & 2048) != 0 || mouseEvent.getButton() == 2;
    }

    public static boolean isRightMouseButton(MouseEvent mouseEvent) {
        return (mouseEvent.getModifiersEx() & 4096) != 0 || mouseEvent.getButton() == 3;
    }

    public static int computeStringWidth(FontMetrics fontMetrics, String str) {
        return SwingUtilities2.stringWidth(null, fontMetrics, str);
    }

    public static String layoutCompoundLabel(JComponent jComponent, FontMetrics fontMetrics, String str, Icon icon, int i2, int i3, int i4, int i5, Rectangle rectangle, Rectangle rectangle2, Rectangle rectangle3, int i6) {
        boolean z2 = true;
        int i7 = i3;
        int i8 = i5;
        if (jComponent != null && !jComponent.getComponentOrientation().isLeftToRight()) {
            z2 = false;
        }
        switch (i3) {
            case 10:
                i7 = z2 ? 2 : 4;
                break;
            case 11:
                i7 = z2 ? 4 : 2;
                break;
        }
        switch (i5) {
            case 10:
                i8 = z2 ? 2 : 4;
                break;
            case 11:
                i8 = z2 ? 4 : 2;
                break;
        }
        return layoutCompoundLabelImpl(jComponent, fontMetrics, str, icon, i2, i7, i4, i8, rectangle, rectangle2, rectangle3, i6);
    }

    public static String layoutCompoundLabel(FontMetrics fontMetrics, String str, Icon icon, int i2, int i3, int i4, int i5, Rectangle rectangle, Rectangle rectangle2, Rectangle rectangle3, int i6) {
        return layoutCompoundLabelImpl(null, fontMetrics, str, icon, i2, i3, i4, i5, rectangle, rectangle2, rectangle3, i6);
    }

    private static String layoutCompoundLabelImpl(JComponent jComponent, FontMetrics fontMetrics, String str, Icon icon, int i2, int i3, int i4, int i5, Rectangle rectangle, Rectangle rectangle2, Rectangle rectangle3, int i6) {
        int i7;
        int i8;
        int i9;
        int i10;
        if (icon != null) {
            rectangle2.width = icon.getIconWidth();
            rectangle2.height = icon.getIconHeight();
        } else {
            rectangle2.height = 0;
            rectangle2.width = 0;
        }
        int leftSideBearing = 0;
        if (str == null || str.equals("")) {
            rectangle3.height = 0;
            rectangle3.width = 0;
            str = "";
            i7 = 0;
        } else {
            i7 = icon == null ? 0 : i6;
            if (i5 == 0) {
                i8 = rectangle.width;
            } else {
                i8 = rectangle.width - (rectangle2.width + i7);
            }
            View view = jComponent != null ? (View) jComponent.getClientProperty("html") : null;
            if (view != null) {
                rectangle3.width = Math.min(i8, (int) view.getPreferredSpan(0));
                rectangle3.height = (int) view.getPreferredSpan(1);
            } else {
                rectangle3.width = SwingUtilities2.stringWidth(jComponent, fontMetrics, str);
                leftSideBearing = SwingUtilities2.getLeftSideBearing(jComponent, fontMetrics, str);
                if (leftSideBearing < 0) {
                    rectangle3.width -= leftSideBearing;
                }
                if (rectangle3.width > i8) {
                    str = SwingUtilities2.clipString(jComponent, fontMetrics, str, i8);
                    rectangle3.width = SwingUtilities2.stringWidth(jComponent, fontMetrics, str);
                }
                rectangle3.height = fontMetrics.getHeight();
            }
        }
        if (i4 == 1) {
            if (i5 != 0) {
                rectangle3.f12373y = 0;
            } else {
                rectangle3.f12373y = -(rectangle3.height + i7);
            }
        } else if (i4 == 0) {
            rectangle3.f12373y = (rectangle2.height / 2) - (rectangle3.height / 2);
        } else if (i5 != 0) {
            rectangle3.f12373y = rectangle2.height - rectangle3.height;
        } else {
            rectangle3.f12373y = rectangle2.height + i7;
        }
        if (i5 == 2) {
            rectangle3.f12372x = -(rectangle3.width + i7);
        } else if (i5 == 0) {
            rectangle3.f12372x = (rectangle2.width / 2) - (rectangle3.width / 2);
        } else {
            rectangle3.f12372x = rectangle2.width + i7;
        }
        int iMin = Math.min(rectangle2.f12372x, rectangle3.f12372x);
        int iMax = Math.max(rectangle2.f12372x + rectangle2.width, rectangle3.f12372x + rectangle3.width) - iMin;
        int iMin2 = Math.min(rectangle2.f12373y, rectangle3.f12373y);
        int iMax2 = Math.max(rectangle2.f12373y + rectangle2.height, rectangle3.f12373y + rectangle3.height) - iMin2;
        if (i2 == 1) {
            i9 = rectangle.f12373y - iMin2;
        } else if (i2 == 0) {
            i9 = (rectangle.f12373y + (rectangle.height / 2)) - (iMin2 + (iMax2 / 2));
        } else {
            i9 = (rectangle.f12373y + rectangle.height) - (iMin2 + iMax2);
        }
        if (i3 == 2) {
            i10 = rectangle.f12372x - iMin;
        } else if (i3 == 4) {
            i10 = (rectangle.f12372x + rectangle.width) - (iMin + iMax);
        } else {
            i10 = (rectangle.f12372x + (rectangle.width / 2)) - (iMin + (iMax / 2));
        }
        rectangle3.f12372x += i10;
        rectangle3.f12373y += i9;
        rectangle2.f12372x += i10;
        rectangle2.f12373y += i9;
        if (leftSideBearing < 0) {
            rectangle3.f12372x -= leftSideBearing;
            rectangle3.width += leftSideBearing;
        }
        if (0 > 0) {
            rectangle3.width -= 0;
        }
        return str;
    }

    public static void paintComponent(Graphics graphics, Component component, Container container, int i2, int i3, int i4, int i5) {
        getCellRendererPane(component, container).paintComponent(graphics, component, container, i2, i3, i4, i5, false);
    }

    public static void paintComponent(Graphics graphics, Component component, Container container, Rectangle rectangle) {
        paintComponent(graphics, component, container, rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height);
    }

    private static CellRendererPane getCellRendererPane(Component component, Container container) {
        Container parent = component.getParent();
        if (parent instanceof CellRendererPane) {
            if (parent.getParent() != container) {
                container.add(parent);
            }
        } else {
            parent = new CellRendererPane();
            parent.add(component);
            container.add(parent);
        }
        return (CellRendererPane) parent;
    }

    public static void updateComponentTreeUI(Component component) {
        updateComponentTreeUI0(component);
        component.invalidate();
        component.validate();
        component.repaint();
    }

    private static void updateComponentTreeUI0(Component component) {
        if (component instanceof JComponent) {
            JComponent jComponent = (JComponent) component;
            jComponent.updateUI();
            JPopupMenu componentPopupMenu = jComponent.getComponentPopupMenu();
            if (componentPopupMenu != null) {
                updateComponentTreeUI(componentPopupMenu);
            }
        }
        Component[] components = null;
        if (component instanceof JMenu) {
            components = ((JMenu) component).getMenuComponents();
        } else if (component instanceof Container) {
            components = ((Container) component).getComponents();
        }
        if (components != null) {
            for (Component component2 : components) {
                updateComponentTreeUI0(component2);
            }
        }
    }

    public static void invokeLater(Runnable runnable) {
        EventQueue.invokeLater(runnable);
    }

    public static void invokeAndWait(Runnable runnable) throws InterruptedException, InvocationTargetException {
        EventQueue.invokeAndWait(runnable);
    }

    public static boolean isEventDispatchThread() {
        return EventQueue.isDispatchThread();
    }

    public static int getAccessibleIndexInParent(Component component) {
        return component.getAccessibleContext().getAccessibleIndexInParent();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static Accessible getAccessibleAt(Component component, Point point) {
        AccessibleComponent accessibleComponent;
        if (component instanceof Container) {
            return component.getAccessibleContext().getAccessibleComponent().getAccessibleAt(point);
        }
        if (component instanceof Accessible) {
            Accessible accessible = (Accessible) component;
            if (accessible != null) {
                AccessibleContext accessibleContext = accessible.getAccessibleContext();
                if (accessibleContext != null) {
                    int accessibleChildrenCount = accessibleContext.getAccessibleChildrenCount();
                    for (int i2 = 0; i2 < accessibleChildrenCount; i2++) {
                        Accessible accessibleChild = accessibleContext.getAccessibleChild(i2);
                        if (accessibleChild != null) {
                            accessibleContext = accessibleChild.getAccessibleContext();
                            if (accessibleContext != null && (accessibleComponent = accessibleContext.getAccessibleComponent()) != null && accessibleComponent.isShowing()) {
                                Point location = accessibleComponent.getLocation();
                                if (accessibleComponent.contains(new Point(point.f12370x - location.f12370x, point.f12371y - location.f12371y))) {
                                    return accessibleChild;
                                }
                            }
                        }
                    }
                }
            }
            return (Accessible) component;
        }
        return null;
    }

    public static AccessibleStateSet getAccessibleStateSet(Component component) {
        return component.getAccessibleContext().getAccessibleStateSet();
    }

    public static int getAccessibleChildrenCount(Component component) {
        return component.getAccessibleContext().getAccessibleChildrenCount();
    }

    public static Accessible getAccessibleChild(Component component, int i2) {
        return component.getAccessibleContext().getAccessibleChild(i2);
    }

    @Deprecated
    public static Component findFocusOwner(Component component) {
        Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
        Component parent = focusOwner;
        while (true) {
            Component component2 = parent;
            if (component2 != null) {
                if (component2 != component) {
                    parent = component2 instanceof Window ? null : component2.getParent();
                } else {
                    return focusOwner;
                }
            } else {
                return null;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static JRootPane getRootPane(Component component) {
        boolean z2 = component instanceof RootPaneContainer;
        if (z2) {
            return ((RootPaneContainer) component).getRootPane();
        }
        for (JRootPane parent = component; parent != false; parent = parent.getParent()) {
            if (parent instanceof JRootPane) {
                return parent;
            }
        }
        return null;
    }

    public static Component getRoot(Component component) {
        Component component2 = null;
        Component parent = component;
        while (true) {
            Component component3 = parent;
            if (component3 != null) {
                if (component3 instanceof Window) {
                    return component3;
                }
                if (component3 instanceof Applet) {
                    component2 = component3;
                }
                parent = component3.getParent();
            } else {
                return component2;
            }
        }
    }

    static JComponent getPaintingOrigin(JComponent jComponent) {
        JComponent jComponent2;
        JComponent jComponent3 = jComponent;
        do {
            Container parent = jComponent3.getParent();
            jComponent3 = parent;
            if (parent instanceof JComponent) {
                jComponent2 = jComponent3;
            } else {
                return null;
            }
        } while (!jComponent2.isPaintingOrigin());
        return jComponent2;
    }

    public static boolean processKeyBindings(KeyEvent keyEvent) {
        if (keyEvent == null || keyEvent.isConsumed()) {
            return false;
        }
        boolean z2 = keyEvent.getID() == 401;
        if (!isValidKeyEventForKeyBindings(keyEvent)) {
            return false;
        }
        for (Component component = keyEvent.getComponent(); component != null; component = component.getParent()) {
            if (component instanceof JComponent) {
                return ((JComponent) component).processKeyBindings(keyEvent, z2);
            }
            if ((component instanceof Applet) || (component instanceof Window)) {
                return JComponent.processKeyBindingsForAllComponents(keyEvent, (Container) component, z2);
            }
        }
        return false;
    }

    static boolean isValidKeyEventForKeyBindings(KeyEvent keyEvent) {
        return true;
    }

    public static boolean notifyAction(Action action, KeyStroke keyStroke, KeyEvent keyEvent, Object obj, int i2) {
        boolean z2;
        String strValueOf;
        if (action == null) {
            return false;
        }
        if (action instanceof UIAction) {
            if (!((UIAction) action).isEnabled(obj)) {
                return false;
            }
        } else if (!action.isEnabled()) {
            return false;
        }
        Object value = action.getValue(Action.ACTION_COMMAND_KEY);
        if (value == null && (action instanceof JComponent.ActionStandin)) {
            z2 = true;
        } else {
            z2 = false;
        }
        if (value != null) {
            strValueOf = value.toString();
        } else if (!z2 && keyEvent.getKeyChar() != 65535) {
            strValueOf = String.valueOf(keyEvent.getKeyChar());
        } else {
            strValueOf = null;
        }
        action.actionPerformed(new ActionEvent(obj, 1001, strValueOf, keyEvent.getWhen(), i2));
        return true;
    }

    public static void replaceUIInputMap(JComponent jComponent, int i2, InputMap inputMap) {
        InputMap inputMap2;
        InputMap inputMap3 = jComponent.getInputMap(i2, inputMap != null);
        while (true) {
            inputMap2 = inputMap3;
            if (inputMap2 != null) {
                InputMap parent = inputMap2.getParent();
                if (parent == null || (parent instanceof UIResource)) {
                    break;
                } else {
                    inputMap3 = parent;
                }
            } else {
                return;
            }
        }
        inputMap2.setParent(inputMap);
    }

    public static void replaceUIActionMap(JComponent jComponent, ActionMap actionMap) {
        ActionMap actionMap2;
        ActionMap actionMap3 = jComponent.getActionMap(actionMap != null);
        while (true) {
            actionMap2 = actionMap3;
            if (actionMap2 != null) {
                ActionMap parent = actionMap2.getParent();
                if (parent == null || (parent instanceof UIResource)) {
                    break;
                } else {
                    actionMap3 = parent;
                }
            } else {
                return;
            }
        }
        actionMap2.setParent(actionMap);
    }

    public static InputMap getUIInputMap(JComponent jComponent, int i2) {
        InputMap inputMap = jComponent.getInputMap(i2, false);
        while (true) {
            InputMap inputMap2 = inputMap;
            if (inputMap2 != null) {
                InputMap parent = inputMap2.getParent();
                if (parent instanceof UIResource) {
                    return parent;
                }
                inputMap = parent;
            } else {
                return null;
            }
        }
    }

    public static ActionMap getUIActionMap(JComponent jComponent) {
        ActionMap actionMap = jComponent.getActionMap(false);
        while (true) {
            ActionMap actionMap2 = actionMap;
            if (actionMap2 != null) {
                ActionMap parent = actionMap2.getParent();
                if (parent instanceof UIResource) {
                    return parent;
                }
                actionMap = parent;
            } else {
                return null;
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/SwingUtilities$SharedOwnerFrame.class */
    static class SharedOwnerFrame extends Frame implements WindowListener {
        SharedOwnerFrame() {
        }

        @Override // java.awt.Frame, java.awt.Window, java.awt.Container, java.awt.Component
        public void addNotify() {
            super.addNotify();
            installListeners();
        }

        void installListeners() {
            for (Window window : getOwnedWindows()) {
                if (window != null) {
                    window.removeWindowListener(this);
                    window.addWindowListener(this);
                }
            }
        }

        @Override // java.awt.event.WindowListener
        public void windowClosed(WindowEvent windowEvent) {
            synchronized (getTreeLock()) {
                for (Window window : getOwnedWindows()) {
                    if (window != null) {
                        if (window.isDisplayable()) {
                            return;
                        } else {
                            window.removeWindowListener(this);
                        }
                    }
                }
                dispose();
            }
        }

        @Override // java.awt.event.WindowListener
        public void windowOpened(WindowEvent windowEvent) {
        }

        @Override // java.awt.event.WindowListener
        public void windowClosing(WindowEvent windowEvent) {
        }

        @Override // java.awt.event.WindowListener
        public void windowIconified(WindowEvent windowEvent) {
        }

        @Override // java.awt.event.WindowListener
        public void windowDeiconified(WindowEvent windowEvent) {
        }

        @Override // java.awt.event.WindowListener
        public void windowActivated(WindowEvent windowEvent) {
        }

        @Override // java.awt.event.WindowListener
        public void windowDeactivated(WindowEvent windowEvent) {
        }

        @Override // java.awt.Window, java.awt.Component
        public void show() {
        }

        @Override // java.awt.Window
        public void dispose() {
            try {
                getToolkit().getSystemEventQueue();
                super.dispose();
            } catch (Exception e2) {
            }
        }
    }

    static Frame getSharedOwnerFrame() throws HeadlessException {
        Frame sharedOwnerFrame = (Frame) appContextGet(sharedOwnerFrameKey);
        if (sharedOwnerFrame == null) {
            sharedOwnerFrame = new SharedOwnerFrame();
            appContextPut(sharedOwnerFrameKey, sharedOwnerFrame);
        }
        return sharedOwnerFrame;
    }

    static WindowListener getSharedOwnerFrameShutdownListener() throws HeadlessException {
        return (WindowListener) getSharedOwnerFrame();
    }

    static Object appContextGet(Object obj) {
        return AppContext.getAppContext().get(obj);
    }

    static void appContextPut(Object obj, Object obj2) {
        AppContext.getAppContext().put(obj, obj2);
    }

    static void appContextRemove(Object obj) {
        AppContext.getAppContext().remove(obj);
    }

    static Class<?> loadSystemClass(String str) throws ClassNotFoundException {
        ReflectUtil.checkPackageAccess(str);
        return Class.forName(str, true, Thread.currentThread().getContextClassLoader());
    }

    static boolean isLeftToRight(Component component) {
        return component.getComponentOrientation().isLeftToRight();
    }

    private SwingUtilities() {
        throw new Error("SwingUtilities is just a container for static methods");
    }

    static boolean doesIconReferenceImage(Icon icon, Image image) {
        return ((icon == null || !(icon instanceof ImageIcon)) ? null : ((ImageIcon) icon).getImage()) == image;
    }

    static int findDisplayedMnemonicIndex(String str, int i2) {
        if (str == null || i2 == 0) {
            return -1;
        }
        char upperCase = Character.toUpperCase((char) i2);
        char lowerCase = Character.toLowerCase((char) i2);
        int iIndexOf = str.indexOf(upperCase);
        int iIndexOf2 = str.indexOf(lowerCase);
        if (iIndexOf == -1) {
            return iIndexOf2;
        }
        if (iIndexOf2 == -1) {
            return iIndexOf;
        }
        return iIndexOf2 < iIndexOf ? iIndexOf2 : iIndexOf;
    }

    public static Rectangle calculateInnerArea(JComponent jComponent, Rectangle rectangle) {
        if (jComponent == null) {
            return null;
        }
        Rectangle rectangle2 = rectangle;
        Insets insets = jComponent.getInsets();
        if (rectangle2 == null) {
            rectangle2 = new Rectangle();
        }
        rectangle2.f12372x = insets.left;
        rectangle2.f12373y = insets.top;
        rectangle2.width = (jComponent.getWidth() - insets.left) - insets.right;
        rectangle2.height = (jComponent.getHeight() - insets.top) - insets.bottom;
        return rectangle2;
    }

    static void updateRendererOrEditorUI(Object obj) {
        if (obj == null) {
            return;
        }
        Component component = null;
        if (obj instanceof Component) {
            component = (Component) obj;
        }
        if (obj instanceof DefaultCellEditor) {
            component = ((DefaultCellEditor) obj).getComponent();
        }
        if (component != null) {
            updateComponentTreeUI(component);
        }
    }

    public static Container getUnwrappedParent(Component component) {
        Container parent = component.getParent();
        while (true) {
            Container container = parent;
            if (container instanceof JLayer) {
                parent = container.getParent();
            } else {
                return container;
            }
        }
    }

    public static Component getUnwrappedView(JViewport jViewport) {
        Component view = jViewport.getView();
        while (true) {
            Component component = view;
            if (component instanceof JLayer) {
                view = ((JLayer) component).getView();
            } else {
                return component;
            }
        }
    }

    static Container getValidateRoot(Container container, boolean z2) {
        Container container2 = null;
        while (true) {
            if (container == null) {
                break;
            }
            if (!container.isDisplayable() || (container instanceof CellRendererPane)) {
                return null;
            }
            if (!container.isValidateRoot()) {
                container = container.getParent();
            } else {
                container2 = container;
                break;
            }
        }
        if (container2 == null) {
            return null;
        }
        while (container != null && container.isDisplayable()) {
            if (z2 && !container.isVisible()) {
                return null;
            }
            if (!(container instanceof Window) && !(container instanceof Applet)) {
                container = container.getParent();
            } else {
                return container2;
            }
        }
        return null;
    }
}
