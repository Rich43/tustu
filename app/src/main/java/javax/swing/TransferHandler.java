package javax.swing;

import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceContext;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.dnd.InvalidDnDOperationException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.TooManyListenersException;
import javax.swing.event.EventListenerList;
import javax.swing.plaf.UIResource;
import javax.swing.text.JTextComponent;
import sun.awt.AWTAccessor;
import sun.awt.AppContext;
import sun.awt.SunToolkit;
import sun.misc.JavaSecurityAccess;
import sun.misc.SharedSecrets;
import sun.reflect.misc.MethodUtil;
import sun.swing.SwingAccessor;
import sun.swing.SwingUtilities2;
import sun.swing.UIAction;

/* loaded from: rt.jar:javax/swing/TransferHandler.class */
public class TransferHandler implements Serializable {
    public static final int NONE = 0;
    public static final int COPY = 1;
    public static final int MOVE = 2;
    public static final int COPY_OR_MOVE = 3;
    public static final int LINK = 1073741824;
    private Image dragImage;
    private Point dragImageOffset;
    private String propertyName;
    private static SwingDragGestureRecognizer recognizer = null;
    static final Action cutAction = new TransferAction("cut");
    static final Action copyAction = new TransferAction("copy");
    static final Action pasteAction = new TransferAction("paste");

    /* loaded from: rt.jar:javax/swing/TransferHandler$HasGetTransferHandler.class */
    interface HasGetTransferHandler {
        TransferHandler getTransferHandler();
    }

    /* loaded from: rt.jar:javax/swing/TransferHandler$DropLocation.class */
    public static class DropLocation {
        private final Point dropPoint;

        protected DropLocation(Point point) {
            if (point == null) {
                throw new IllegalArgumentException("Point cannot be null");
            }
            this.dropPoint = new Point(point);
        }

        public final Point getDropPoint() {
            return new Point(this.dropPoint);
        }

        public String toString() {
            return getClass().getName() + "[dropPoint=" + ((Object) this.dropPoint) + "]";
        }
    }

    /* loaded from: rt.jar:javax/swing/TransferHandler$TransferSupport.class */
    public static final class TransferSupport {
        private boolean isDrop;
        private Component component;
        private boolean showDropLocationIsSet;
        private boolean showDropLocation;
        private int dropAction;
        private Object source;
        private DropLocation dropLocation;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !TransferHandler.class.desiredAssertionStatus();
        }

        private TransferSupport(Component component, DropTargetEvent dropTargetEvent) {
            this.dropAction = -1;
            this.isDrop = true;
            setDNDVariables(component, dropTargetEvent);
        }

        public TransferSupport(Component component, Transferable transferable) {
            this.dropAction = -1;
            if (component == null) {
                throw new NullPointerException("component is null");
            }
            if (transferable == null) {
                throw new NullPointerException("transferable is null");
            }
            this.isDrop = false;
            this.component = component;
            this.source = transferable;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setDNDVariables(Component component, DropTargetEvent dropTargetEvent) {
            Point location;
            if (!$assertionsDisabled && !this.isDrop) {
                throw new AssertionError();
            }
            this.component = component;
            this.source = dropTargetEvent;
            this.dropLocation = null;
            this.dropAction = -1;
            this.showDropLocationIsSet = false;
            if (this.source == null) {
                return;
            }
            if (!$assertionsDisabled && !(this.source instanceof DropTargetDragEvent) && !(this.source instanceof DropTargetDropEvent)) {
                throw new AssertionError();
            }
            if (this.source instanceof DropTargetDragEvent) {
                location = ((DropTargetDragEvent) this.source).getLocation();
            } else {
                location = ((DropTargetDropEvent) this.source).getLocation();
            }
            Point point = location;
            if (SunToolkit.isInstanceOf(component, "javax.swing.text.JTextComponent")) {
                this.dropLocation = SwingAccessor.getJTextComponentAccessor().dropLocationForPoint((JTextComponent) component, point);
            } else if (component instanceof JComponent) {
                this.dropLocation = ((JComponent) component).dropLocationForPoint(point);
            }
        }

        public boolean isDrop() {
            return this.isDrop;
        }

        public Component getComponent() {
            return this.component;
        }

        private void assureIsDrop() {
            if (!this.isDrop) {
                throw new IllegalStateException("Not a drop");
            }
        }

        public DropLocation getDropLocation() {
            Point location;
            assureIsDrop();
            if (this.dropLocation == null) {
                if (this.source instanceof DropTargetDragEvent) {
                    location = ((DropTargetDragEvent) this.source).getLocation();
                } else {
                    location = ((DropTargetDropEvent) this.source).getLocation();
                }
                this.dropLocation = new DropLocation(location);
            }
            return this.dropLocation;
        }

        public void setShowDropLocation(boolean z2) {
            assureIsDrop();
            this.showDropLocation = z2;
            this.showDropLocationIsSet = true;
        }

        public void setDropAction(int i2) {
            assureIsDrop();
            int sourceDropActions = i2 & getSourceDropActions();
            if (sourceDropActions != 1 && sourceDropActions != 2 && sourceDropActions != 1073741824) {
                throw new IllegalArgumentException("unsupported drop action: " + i2);
            }
            this.dropAction = i2;
        }

        public int getDropAction() {
            return this.dropAction == -1 ? getUserDropAction() : this.dropAction;
        }

        public int getUserDropAction() {
            assureIsDrop();
            if (this.source instanceof DropTargetDragEvent) {
                return ((DropTargetDragEvent) this.source).getDropAction();
            }
            return ((DropTargetDropEvent) this.source).getDropAction();
        }

        public int getSourceDropActions() {
            assureIsDrop();
            if (this.source instanceof DropTargetDragEvent) {
                return ((DropTargetDragEvent) this.source).getSourceActions();
            }
            return ((DropTargetDropEvent) this.source).getSourceActions();
        }

        public DataFlavor[] getDataFlavors() {
            if (this.isDrop) {
                if (this.source instanceof DropTargetDragEvent) {
                    return ((DropTargetDragEvent) this.source).getCurrentDataFlavors();
                }
                return ((DropTargetDropEvent) this.source).getCurrentDataFlavors();
            }
            return ((Transferable) this.source).getTransferDataFlavors();
        }

        public boolean isDataFlavorSupported(DataFlavor dataFlavor) {
            if (this.isDrop) {
                if (this.source instanceof DropTargetDragEvent) {
                    return ((DropTargetDragEvent) this.source).isDataFlavorSupported(dataFlavor);
                }
                return ((DropTargetDropEvent) this.source).isDataFlavorSupported(dataFlavor);
            }
            return ((Transferable) this.source).isDataFlavorSupported(dataFlavor);
        }

        public Transferable getTransferable() {
            if (this.isDrop) {
                if (this.source instanceof DropTargetDragEvent) {
                    return ((DropTargetDragEvent) this.source).getTransferable();
                }
                return ((DropTargetDropEvent) this.source).getTransferable();
            }
            return (Transferable) this.source;
        }
    }

    public static Action getCutAction() {
        return cutAction;
    }

    public static Action getCopyAction() {
        return copyAction;
    }

    public static Action getPasteAction() {
        return pasteAction;
    }

    public TransferHandler(String str) {
        this.propertyName = str;
    }

    protected TransferHandler() {
        this(null);
    }

    public void setDragImage(Image image) {
        this.dragImage = image;
    }

    public Image getDragImage() {
        return this.dragImage;
    }

    public void setDragImageOffset(Point point) {
        this.dragImageOffset = new Point(point);
    }

    public Point getDragImageOffset() {
        if (this.dragImageOffset == null) {
            return new Point(0, 0);
        }
        return new Point(this.dragImageOffset);
    }

    public void exportAsDrag(JComponent jComponent, InputEvent inputEvent, int i2) {
        int sourceActions = getSourceActions(jComponent);
        if (!(inputEvent instanceof MouseEvent) || ((i2 != 1 && i2 != 2 && i2 != 1073741824) || (sourceActions & i2) == 0)) {
            i2 = 0;
        }
        if (i2 != 0 && !GraphicsEnvironment.isHeadless()) {
            if (recognizer == null) {
                recognizer = new SwingDragGestureRecognizer(new DragHandler());
            }
            recognizer.gestured(jComponent, (MouseEvent) inputEvent, sourceActions, i2);
            return;
        }
        exportDone(jComponent, null, 0);
    }

    public void exportToClipboard(JComponent jComponent, Clipboard clipboard, int i2) throws IllegalStateException {
        Transferable transferableCreateTransferable;
        if ((i2 == 1 || i2 == 2) && (getSourceActions(jComponent) & i2) != 0 && (transferableCreateTransferable = createTransferable(jComponent)) != null) {
            try {
                clipboard.setContents(transferableCreateTransferable, null);
                exportDone(jComponent, transferableCreateTransferable, i2);
                return;
            } catch (IllegalStateException e2) {
                exportDone(jComponent, transferableCreateTransferable, 0);
                throw e2;
            }
        }
        exportDone(jComponent, null, 0);
    }

    public boolean importData(TransferSupport transferSupport) {
        if (transferSupport.getComponent() instanceof JComponent) {
            return importData((JComponent) transferSupport.getComponent(), transferSupport.getTransferable());
        }
        return false;
    }

    public boolean importData(JComponent jComponent, Transferable transferable) {
        Method writeMethod;
        DataFlavor propertyDataFlavor;
        PropertyDescriptor propertyDescriptor = getPropertyDescriptor(jComponent);
        if (propertyDescriptor == null || (writeMethod = propertyDescriptor.getWriteMethod()) == null) {
            return false;
        }
        Class<?>[] parameterTypes = writeMethod.getParameterTypes();
        if (parameterTypes.length == 1 && (propertyDataFlavor = getPropertyDataFlavor(parameterTypes[0], transferable.getTransferDataFlavors())) != null) {
            try {
                MethodUtil.invoke(writeMethod, jComponent, new Object[]{transferable.getTransferData(propertyDataFlavor)});
                return true;
            } catch (Exception e2) {
                System.err.println("Invocation failed");
                return false;
            }
        }
        return false;
    }

    public boolean canImport(TransferSupport transferSupport) {
        if (transferSupport.getComponent() instanceof JComponent) {
            return canImport((JComponent) transferSupport.getComponent(), transferSupport.getDataFlavors());
        }
        return false;
    }

    public boolean canImport(JComponent jComponent, DataFlavor[] dataFlavorArr) {
        Method writeMethod;
        PropertyDescriptor propertyDescriptor = getPropertyDescriptor(jComponent);
        if (propertyDescriptor == null || (writeMethod = propertyDescriptor.getWriteMethod()) == null) {
            return false;
        }
        Class<?>[] parameterTypes = writeMethod.getParameterTypes();
        if (parameterTypes.length == 1 && getPropertyDataFlavor(parameterTypes[0], dataFlavorArr) != null) {
            return true;
        }
        return false;
    }

    public int getSourceActions(JComponent jComponent) {
        if (getPropertyDescriptor(jComponent) != null) {
            return 1;
        }
        return 0;
    }

    public Icon getVisualRepresentation(Transferable transferable) {
        return null;
    }

    protected Transferable createTransferable(JComponent jComponent) {
        PropertyDescriptor propertyDescriptor = getPropertyDescriptor(jComponent);
        if (propertyDescriptor != null) {
            return new PropertyTransferable(propertyDescriptor, jComponent);
        }
        return null;
    }

    protected void exportDone(JComponent jComponent, Transferable transferable, int i2) {
    }

    private PropertyDescriptor getPropertyDescriptor(JComponent jComponent) {
        Method readMethod;
        Class<?>[] parameterTypes;
        if (this.propertyName == null) {
            return null;
        }
        try {
            PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(jComponent.getClass()).getPropertyDescriptors();
            for (int i2 = 0; i2 < propertyDescriptors.length; i2++) {
                if (this.propertyName.equals(propertyDescriptors[i2].getName()) && (readMethod = propertyDescriptors[i2].getReadMethod()) != null && ((parameterTypes = readMethod.getParameterTypes()) == null || parameterTypes.length == 0)) {
                    return propertyDescriptors[i2];
                }
            }
            return null;
        } catch (IntrospectionException e2) {
            return null;
        }
    }

    private DataFlavor getPropertyDataFlavor(Class<?> cls, DataFlavor[] dataFlavorArr) {
        for (DataFlavor dataFlavor : dataFlavorArr) {
            if ("application".equals(dataFlavor.getPrimaryType()) && "x-java-jvm-local-objectref".equals(dataFlavor.getSubType()) && cls.isAssignableFrom(dataFlavor.getRepresentationClass())) {
                return dataFlavor;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static DropTargetListener getDropTargetListener() {
        DropHandler dropHandler;
        synchronized (DropHandler.class) {
            DropHandler dropHandler2 = (DropHandler) AppContext.getAppContext().get(DropHandler.class);
            if (dropHandler2 == null) {
                dropHandler2 = new DropHandler();
                AppContext.getAppContext().put(DropHandler.class, dropHandler2);
            }
            dropHandler = dropHandler2;
        }
        return dropHandler;
    }

    /* loaded from: rt.jar:javax/swing/TransferHandler$PropertyTransferable.class */
    static class PropertyTransferable implements Transferable {
        JComponent component;
        PropertyDescriptor property;

        PropertyTransferable(PropertyDescriptor propertyDescriptor, JComponent jComponent) {
            this.property = propertyDescriptor;
            this.component = jComponent;
        }

        @Override // java.awt.datatransfer.Transferable
        public DataFlavor[] getTransferDataFlavors() {
            DataFlavor[] dataFlavorArr = new DataFlavor[1];
            try {
                dataFlavorArr[0] = new DataFlavor("application/x-java-jvm-local-objectref;class=" + this.property.getPropertyType().getName());
            } catch (ClassNotFoundException e2) {
                dataFlavorArr = new DataFlavor[0];
            }
            return dataFlavorArr;
        }

        @Override // java.awt.datatransfer.Transferable
        public boolean isDataFlavorSupported(DataFlavor dataFlavor) {
            Class<?> propertyType = this.property.getPropertyType();
            if ("application".equals(dataFlavor.getPrimaryType()) && "x-java-jvm-local-objectref".equals(dataFlavor.getSubType()) && dataFlavor.getRepresentationClass().isAssignableFrom(propertyType)) {
                return true;
            }
            return false;
        }

        @Override // java.awt.datatransfer.Transferable
        public Object getTransferData(DataFlavor dataFlavor) throws UnsupportedFlavorException, IOException {
            if (!isDataFlavorSupported(dataFlavor)) {
                throw new UnsupportedFlavorException(dataFlavor);
            }
            try {
                return MethodUtil.invoke(this.property.getReadMethod(), this.component, (Object[]) null);
            } catch (Exception e2) {
                throw new IOException("Property read failed: " + this.property.getName());
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/TransferHandler$SwingDropTarget.class */
    static class SwingDropTarget extends DropTarget implements UIResource {
        private EventListenerList listenerList;

        SwingDropTarget(Component component) {
            super(component, 1073741827, null);
            try {
                super.addDropTargetListener(TransferHandler.getDropTargetListener());
            } catch (TooManyListenersException e2) {
            }
        }

        @Override // java.awt.dnd.DropTarget
        public void addDropTargetListener(DropTargetListener dropTargetListener) throws TooManyListenersException {
            if (this.listenerList == null) {
                this.listenerList = new EventListenerList();
            }
            this.listenerList.add(DropTargetListener.class, dropTargetListener);
        }

        @Override // java.awt.dnd.DropTarget
        public void removeDropTargetListener(DropTargetListener dropTargetListener) {
            if (this.listenerList != null) {
                this.listenerList.remove(DropTargetListener.class, dropTargetListener);
            }
        }

        @Override // java.awt.dnd.DropTarget, java.awt.dnd.DropTargetListener
        public void dragEnter(DropTargetDragEvent dropTargetDragEvent) {
            super.dragEnter(dropTargetDragEvent);
            if (this.listenerList != null) {
                Object[] listenerList = this.listenerList.getListenerList();
                for (int length = listenerList.length - 2; length >= 0; length -= 2) {
                    if (listenerList[length] == DropTargetListener.class) {
                        ((DropTargetListener) listenerList[length + 1]).dragEnter(dropTargetDragEvent);
                    }
                }
            }
        }

        @Override // java.awt.dnd.DropTarget, java.awt.dnd.DropTargetListener
        public void dragOver(DropTargetDragEvent dropTargetDragEvent) {
            super.dragOver(dropTargetDragEvent);
            if (this.listenerList != null) {
                Object[] listenerList = this.listenerList.getListenerList();
                for (int length = listenerList.length - 2; length >= 0; length -= 2) {
                    if (listenerList[length] == DropTargetListener.class) {
                        ((DropTargetListener) listenerList[length + 1]).dragOver(dropTargetDragEvent);
                    }
                }
            }
        }

        @Override // java.awt.dnd.DropTarget, java.awt.dnd.DropTargetListener
        public void dragExit(DropTargetEvent dropTargetEvent) {
            DropTargetListener dropTargetListener;
            super.dragExit(dropTargetEvent);
            if (this.listenerList != null) {
                Object[] listenerList = this.listenerList.getListenerList();
                for (int length = listenerList.length - 2; length >= 0; length -= 2) {
                    if (listenerList[length] == DropTargetListener.class) {
                        ((DropTargetListener) listenerList[length + 1]).dragExit(dropTargetEvent);
                    }
                }
            }
            if (isActive() || (dropTargetListener = TransferHandler.getDropTargetListener()) == null || !(dropTargetListener instanceof DropHandler)) {
                return;
            }
            ((DropHandler) dropTargetListener).cleanup(false);
        }

        @Override // java.awt.dnd.DropTarget, java.awt.dnd.DropTargetListener
        public void drop(DropTargetDropEvent dropTargetDropEvent) {
            super.drop(dropTargetDropEvent);
            if (this.listenerList != null) {
                Object[] listenerList = this.listenerList.getListenerList();
                for (int length = listenerList.length - 2; length >= 0; length -= 2) {
                    if (listenerList[length] == DropTargetListener.class) {
                        ((DropTargetListener) listenerList[length + 1]).drop(dropTargetDropEvent);
                    }
                }
            }
        }

        @Override // java.awt.dnd.DropTarget, java.awt.dnd.DropTargetListener
        public void dropActionChanged(DropTargetDragEvent dropTargetDragEvent) {
            super.dropActionChanged(dropTargetDragEvent);
            if (this.listenerList != null) {
                Object[] listenerList = this.listenerList.getListenerList();
                for (int length = listenerList.length - 2; length >= 0; length -= 2) {
                    if (listenerList[length] == DropTargetListener.class) {
                        ((DropTargetListener) listenerList[length + 1]).dropActionChanged(dropTargetDragEvent);
                    }
                }
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/TransferHandler$DropHandler.class */
    private static class DropHandler implements DropTargetListener, Serializable, ActionListener {
        private Timer timer;
        private Point lastPosition;
        private Rectangle outer;
        private Rectangle inner;
        private int hysteresis;
        private Component component;
        private Object state;
        private TransferSupport support;
        private static final int AUTOSCROLL_INSET = 10;

        private DropHandler() {
            this.outer = new Rectangle();
            this.inner = new Rectangle();
            this.hysteresis = 10;
            this.support = new TransferSupport(null, (DropTargetEvent) null);
        }

        private void updateAutoscrollRegion(JComponent jComponent) {
            Rectangle visibleRect = jComponent.getVisibleRect();
            this.outer.setBounds(visibleRect.f12372x, visibleRect.f12373y, visibleRect.width, visibleRect.height);
            Insets insets = new Insets(0, 0, 0, 0);
            if (jComponent instanceof Scrollable) {
                if (visibleRect.width >= 20) {
                    insets.right = 10;
                    insets.left = 10;
                }
                if (visibleRect.height >= 20) {
                    insets.bottom = 10;
                    insets.top = 10;
                }
            }
            this.inner.setBounds(visibleRect.f12372x + insets.left, visibleRect.f12373y + insets.top, visibleRect.width - (insets.left + insets.right), visibleRect.height - (insets.top + insets.bottom));
        }

        /* JADX WARN: Multi-variable type inference failed */
        private void autoscroll(JComponent jComponent, Point point) {
            if (jComponent instanceof Scrollable) {
                Scrollable scrollable = (Scrollable) jComponent;
                if (point.f12371y < this.inner.f12373y) {
                    int scrollableUnitIncrement = scrollable.getScrollableUnitIncrement(this.outer, 1, -1);
                    jComponent.scrollRectToVisible(new Rectangle(this.inner.f12372x, this.outer.f12373y - scrollableUnitIncrement, this.inner.width, scrollableUnitIncrement));
                } else if (point.f12371y > this.inner.f12373y + this.inner.height) {
                    jComponent.scrollRectToVisible(new Rectangle(this.inner.f12372x, this.outer.f12373y + this.outer.height, this.inner.width, scrollable.getScrollableUnitIncrement(this.outer, 1, 1)));
                }
                if (point.f12370x < this.inner.f12372x) {
                    int scrollableUnitIncrement2 = scrollable.getScrollableUnitIncrement(this.outer, 0, -1);
                    jComponent.scrollRectToVisible(new Rectangle(this.outer.f12372x - scrollableUnitIncrement2, this.inner.f12373y, scrollableUnitIncrement2, this.inner.height));
                } else if (point.f12370x > this.inner.f12372x + this.inner.width) {
                    jComponent.scrollRectToVisible(new Rectangle(this.outer.f12372x + this.outer.width, this.inner.f12373y, scrollable.getScrollableUnitIncrement(this.outer, 0, 1), this.inner.height));
                }
            }
        }

        private void initPropertiesIfNecessary() {
            if (this.timer == null) {
                Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
                Integer num = (Integer) defaultToolkit.getDesktopProperty("DnD.Autoscroll.interval");
                this.timer = new Timer(num == null ? 100 : num.intValue(), this);
                Integer num2 = (Integer) defaultToolkit.getDesktopProperty("DnD.Autoscroll.initialDelay");
                this.timer.setInitialDelay(num2 == null ? 100 : num2.intValue());
                Integer num3 = (Integer) defaultToolkit.getDesktopProperty("DnD.Autoscroll.cursorHysteresis");
                if (num3 != null) {
                    this.hysteresis = num3.intValue();
                }
            }
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            updateAutoscrollRegion((JComponent) this.component);
            if (this.outer.contains(this.lastPosition) && !this.inner.contains(this.lastPosition)) {
                autoscroll((JComponent) this.component, this.lastPosition);
            }
        }

        private void setComponentDropLocation(TransferSupport transferSupport, boolean z2) {
            DropLocation dropLocation = transferSupport == null ? null : transferSupport.getDropLocation();
            if (SunToolkit.isInstanceOf(this.component, "javax.swing.text.JTextComponent")) {
                this.state = SwingAccessor.getJTextComponentAccessor().setDropLocation((JTextComponent) this.component, dropLocation, this.state, z2);
            } else if (this.component instanceof JComponent) {
                this.state = ((JComponent) this.component).setDropLocation(dropLocation, this.state, z2);
            }
        }

        private void handleDrag(DropTargetDragEvent dropTargetDragEvent) {
            TransferHandler transferHandler = ((HasGetTransferHandler) this.component).getTransferHandler();
            if (transferHandler != null) {
                this.support.setDNDVariables(this.component, dropTargetDragEvent);
                boolean zCanImport = transferHandler.canImport(this.support);
                if (zCanImport) {
                    dropTargetDragEvent.acceptDrag(this.support.getDropAction());
                } else {
                    dropTargetDragEvent.rejectDrag();
                }
                setComponentDropLocation(this.support.showDropLocationIsSet ? this.support.showDropLocation : zCanImport ? this.support : null, false);
                return;
            }
            dropTargetDragEvent.rejectDrag();
            setComponentDropLocation(null, false);
        }

        @Override // java.awt.dnd.DropTargetListener
        public void dragEnter(DropTargetDragEvent dropTargetDragEvent) {
            this.state = null;
            this.component = dropTargetDragEvent.getDropTargetContext().getComponent();
            handleDrag(dropTargetDragEvent);
            if (this.component instanceof JComponent) {
                this.lastPosition = dropTargetDragEvent.getLocation();
                updateAutoscrollRegion((JComponent) this.component);
                initPropertiesIfNecessary();
            }
        }

        @Override // java.awt.dnd.DropTargetListener
        public void dragOver(DropTargetDragEvent dropTargetDragEvent) {
            handleDrag(dropTargetDragEvent);
            if (!(this.component instanceof JComponent)) {
                return;
            }
            Point location = dropTargetDragEvent.getLocation();
            if (Math.abs(location.f12370x - this.lastPosition.f12370x) > this.hysteresis || Math.abs(location.f12371y - this.lastPosition.f12371y) > this.hysteresis) {
                if (this.timer.isRunning()) {
                    this.timer.stop();
                }
            } else if (!this.timer.isRunning()) {
                this.timer.start();
            }
            this.lastPosition = location;
        }

        @Override // java.awt.dnd.DropTargetListener
        public void dragExit(DropTargetEvent dropTargetEvent) {
            cleanup(false);
        }

        @Override // java.awt.dnd.DropTargetListener
        public void drop(DropTargetDropEvent dropTargetDropEvent) throws InvalidDnDOperationException {
            boolean zImportData;
            TransferHandler transferHandler = ((HasGetTransferHandler) this.component).getTransferHandler();
            if (transferHandler != null) {
                this.support.setDNDVariables(this.component, dropTargetDropEvent);
                boolean zCanImport = transferHandler.canImport(this.support);
                if (zCanImport) {
                    dropTargetDropEvent.acceptDrop(this.support.getDropAction());
                    setComponentDropLocation(this.support.showDropLocationIsSet ? this.support.showDropLocation : zCanImport ? this.support : null, false);
                    try {
                        zImportData = transferHandler.importData(this.support);
                    } catch (RuntimeException e2) {
                        zImportData = false;
                    }
                    dropTargetDropEvent.dropComplete(zImportData);
                    cleanup(zImportData);
                    return;
                }
                dropTargetDropEvent.rejectDrop();
                cleanup(false);
                return;
            }
            dropTargetDropEvent.rejectDrop();
            cleanup(false);
        }

        @Override // java.awt.dnd.DropTargetListener
        public void dropActionChanged(DropTargetDragEvent dropTargetDragEvent) {
            if (this.component == null) {
                return;
            }
            handleDrag(dropTargetDragEvent);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void cleanup(boolean z2) {
            setComponentDropLocation(null, z2);
            if (this.component instanceof JComponent) {
                ((JComponent) this.component).dndDone();
            }
            if (this.timer != null) {
                this.timer.stop();
            }
            this.state = null;
            this.component = null;
            this.lastPosition = null;
        }
    }

    /* loaded from: rt.jar:javax/swing/TransferHandler$DragHandler.class */
    private static class DragHandler implements DragGestureListener, DragSourceListener {
        private boolean scrolls;

        private DragHandler() {
        }

        @Override // java.awt.dnd.DragGestureListener
        public void dragGestureRecognized(DragGestureEvent dragGestureEvent) {
            JComponent jComponent = (JComponent) dragGestureEvent.getComponent();
            TransferHandler transferHandler = jComponent.getTransferHandler();
            Transferable transferableCreateTransferable = transferHandler.createTransferable(jComponent);
            if (transferableCreateTransferable != null) {
                this.scrolls = jComponent.getAutoscrolls();
                jComponent.setAutoscrolls(false);
                try {
                    Image dragImage = transferHandler.getDragImage();
                    if (dragImage == null) {
                        dragGestureEvent.startDrag(null, transferableCreateTransferable, this);
                        return;
                    } else {
                        dragGestureEvent.startDrag(null, dragImage, transferHandler.getDragImageOffset(), transferableCreateTransferable, this);
                        return;
                    }
                } catch (RuntimeException e2) {
                    jComponent.setAutoscrolls(this.scrolls);
                }
            }
            transferHandler.exportDone(jComponent, transferableCreateTransferable, 0);
        }

        @Override // java.awt.dnd.DragSourceListener
        public void dragEnter(DragSourceDragEvent dragSourceDragEvent) {
        }

        @Override // java.awt.dnd.DragSourceListener
        public void dragOver(DragSourceDragEvent dragSourceDragEvent) {
        }

        @Override // java.awt.dnd.DragSourceListener
        public void dragExit(DragSourceEvent dragSourceEvent) {
        }

        @Override // java.awt.dnd.DragSourceListener
        public void dragDropEnd(DragSourceDropEvent dragSourceDropEvent) {
            DragSourceContext dragSourceContext = dragSourceDropEvent.getDragSourceContext();
            JComponent jComponent = (JComponent) dragSourceContext.getComponent();
            if (dragSourceDropEvent.getDropSuccess()) {
                jComponent.getTransferHandler().exportDone(jComponent, dragSourceContext.getTransferable(), dragSourceDropEvent.getDropAction());
            } else {
                jComponent.getTransferHandler().exportDone(jComponent, dragSourceContext.getTransferable(), 0);
            }
            jComponent.setAutoscrolls(this.scrolls);
        }

        @Override // java.awt.dnd.DragSourceListener
        public void dropActionChanged(DragSourceDragEvent dragSourceDragEvent) {
        }
    }

    /* loaded from: rt.jar:javax/swing/TransferHandler$SwingDragGestureRecognizer.class */
    private static class SwingDragGestureRecognizer extends DragGestureRecognizer {
        SwingDragGestureRecognizer(DragGestureListener dragGestureListener) {
            super(DragSource.getDefaultDragSource(), null, 0, dragGestureListener);
        }

        void gestured(JComponent jComponent, MouseEvent mouseEvent, int i2, int i3) {
            setComponent(jComponent);
            setSourceActions(i2);
            appendEvent(mouseEvent);
            fireDragGestureRecognized(i3, mouseEvent.getPoint());
        }

        @Override // java.awt.dnd.DragGestureRecognizer
        protected void registerListeners() {
        }

        @Override // java.awt.dnd.DragGestureRecognizer
        protected void unregisterListeners() {
        }
    }

    /* loaded from: rt.jar:javax/swing/TransferHandler$TransferAction.class */
    static class TransferAction extends UIAction implements UIResource {
        private static final JavaSecurityAccess javaSecurityAccess = SharedSecrets.getJavaSecurityAccess();
        private static Object SandboxClipboardKey = new Object();

        TransferAction(String str) {
            super(str);
        }

        @Override // sun.swing.UIAction
        public boolean isEnabled(Object obj) {
            if ((obj instanceof JComponent) && ((JComponent) obj).getTransferHandler() == null) {
                return false;
            }
            return true;
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(final ActionEvent actionEvent) {
            Object source = actionEvent.getSource();
            final PrivilegedAction<Void> privilegedAction = new PrivilegedAction<Void>() { // from class: javax.swing.TransferHandler.TransferAction.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Void run2() {
                    TransferAction.this.actionPerformedImpl(actionEvent);
                    return null;
                }
            };
            AccessControlContext context = AccessController.getContext();
            AccessControlContext accessControlContext = AWTAccessor.getComponentAccessor().getAccessControlContext((Component) source);
            final AccessControlContext accessControlContext2 = AWTAccessor.getAWTEventAccessor().getAccessControlContext(actionEvent);
            if (accessControlContext == null) {
                javaSecurityAccess.doIntersectionPrivilege(privilegedAction, context, accessControlContext2);
            } else {
                javaSecurityAccess.doIntersectionPrivilege(new PrivilegedAction<Void>() { // from class: javax.swing.TransferHandler.TransferAction.2
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedAction
                    /* renamed from: run */
                    public Void run2() {
                        TransferAction.javaSecurityAccess.doIntersectionPrivilege(privilegedAction, accessControlContext2);
                        return null;
                    }
                }, context, accessControlContext);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void actionPerformedImpl(ActionEvent actionEvent) {
            Object source = actionEvent.getSource();
            if (source instanceof JComponent) {
                JComponent jComponent = (JComponent) source;
                TransferHandler transferHandler = jComponent.getTransferHandler();
                Clipboard clipboard = getClipboard(jComponent);
                String str = (String) getValue("Name");
                Transferable contents = null;
                if (clipboard != null && transferHandler != null && str != null) {
                    try {
                        if ("cut".equals(str)) {
                            transferHandler.exportToClipboard(jComponent, clipboard, 2);
                        } else if ("copy".equals(str)) {
                            transferHandler.exportToClipboard(jComponent, clipboard, 1);
                        } else if ("paste".equals(str)) {
                            contents = clipboard.getContents(null);
                        }
                    } catch (IllegalStateException e2) {
                        UIManager.getLookAndFeel().provideErrorFeedback(jComponent);
                        return;
                    }
                }
                if (contents != null) {
                    transferHandler.importData(new TransferSupport(jComponent, contents));
                }
            }
        }

        private Clipboard getClipboard(JComponent jComponent) {
            if (SwingUtilities2.canAccessSystemClipboard()) {
                return jComponent.getToolkit().getSystemClipboard();
            }
            Clipboard clipboard = (Clipboard) AppContext.getAppContext().get(SandboxClipboardKey);
            if (clipboard == null) {
                clipboard = new Clipboard("Sandboxed Component Clipboard");
                AppContext.getAppContext().put(SandboxClipboardKey, clipboard);
            }
            return clipboard;
        }
    }
}
