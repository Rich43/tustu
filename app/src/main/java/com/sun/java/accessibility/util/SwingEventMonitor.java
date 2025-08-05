package com.sun.java.accessibility.util;

import com.sun.java.accessibility.util.AWTEventMonitor;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ContainerEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.swing.CellEditor;
import javax.swing.JComponent;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.EventListenerList;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeSelectionModel;
import jdk.Exported;

@Exported
/* loaded from: jaccess.jar:com/sun/java/accessibility/util/SwingEventMonitor.class */
public class SwingEventMonitor extends AWTEventMonitor {
    protected static final EventListenerList listenerList = new EventListenerList();
    protected static final SwingEventListener swingListener = new SwingEventListener();

    public static void addAncestorListener(AncestorListener ancestorListener) {
        if (listenerList.getListenerCount(AncestorListener.class) == 0) {
            swingListener.installListeners(12);
        }
        listenerList.add(AncestorListener.class, ancestorListener);
    }

    public static void removeAncestorListener(AncestorListener ancestorListener) {
        listenerList.remove(AncestorListener.class, ancestorListener);
        if (listenerList.getListenerCount(AncestorListener.class) == 0) {
            swingListener.removeListeners(12);
        }
    }

    public static void addCaretListener(CaretListener caretListener) {
        if (listenerList.getListenerCount(CaretListener.class) == 0) {
            swingListener.installListeners(13);
        }
        listenerList.add(CaretListener.class, caretListener);
    }

    public static void removeCaretListener(CaretListener caretListener) {
        listenerList.remove(CaretListener.class, caretListener);
        if (listenerList.getListenerCount(CaretListener.class) == 0) {
            swingListener.removeListeners(13);
        }
    }

    public static void addCellEditorListener(CellEditorListener cellEditorListener) {
        if (listenerList.getListenerCount(CellEditorListener.class) == 0) {
            swingListener.installListeners(14);
        }
        listenerList.add(CellEditorListener.class, cellEditorListener);
    }

    public static void removeCellEditorListener(CellEditorListener cellEditorListener) {
        listenerList.remove(CellEditorListener.class, cellEditorListener);
        if (listenerList.getListenerCount(CellEditorListener.class) == 0) {
            swingListener.removeListeners(14);
        }
    }

    public static void addChangeListener(ChangeListener changeListener) {
        if (listenerList.getListenerCount(ChangeListener.class) == 0) {
            swingListener.installListeners(15);
        }
        listenerList.add(ChangeListener.class, changeListener);
    }

    public static void removeChangeListener(ChangeListener changeListener) {
        listenerList.remove(ChangeListener.class, changeListener);
        if (listenerList.getListenerCount(ChangeListener.class) == 0) {
            swingListener.removeListeners(15);
        }
    }

    public static void addColumnModelListener(TableColumnModelListener tableColumnModelListener) {
        if (listenerList.getListenerCount(TableColumnModelListener.class) == 0) {
            swingListener.installListeners(16);
        }
        listenerList.add(TableColumnModelListener.class, tableColumnModelListener);
    }

    public static void removeColumnModelListener(TableColumnModelListener tableColumnModelListener) {
        listenerList.remove(TableColumnModelListener.class, tableColumnModelListener);
        if (listenerList.getListenerCount(TableColumnModelListener.class) == 0) {
            swingListener.removeListeners(16);
        }
    }

    public static void addDocumentListener(DocumentListener documentListener) {
        if (listenerList.getListenerCount(DocumentListener.class) == 0) {
            swingListener.installListeners(17);
        }
        listenerList.add(DocumentListener.class, documentListener);
    }

    public static void removeDocumentListener(DocumentListener documentListener) {
        listenerList.remove(DocumentListener.class, documentListener);
        if (listenerList.getListenerCount(DocumentListener.class) == 0) {
            swingListener.removeListeners(17);
        }
    }

    public static void addListDataListener(ListDataListener listDataListener) {
        if (listenerList.getListenerCount(ListDataListener.class) == 0) {
            swingListener.installListeners(18);
        }
        listenerList.add(ListDataListener.class, listDataListener);
    }

    public static void removeListDataListener(ListDataListener listDataListener) {
        listenerList.remove(ListDataListener.class, listDataListener);
        if (listenerList.getListenerCount(ListDataListener.class) == 0) {
            swingListener.removeListeners(18);
        }
    }

    public static void addListSelectionListener(ListSelectionListener listSelectionListener) {
        if (listenerList.getListenerCount(ListSelectionListener.class) == 0) {
            swingListener.installListeners(19);
        }
        listenerList.add(ListSelectionListener.class, listSelectionListener);
    }

    public static void removeListSelectionListener(ListSelectionListener listSelectionListener) {
        listenerList.remove(ListSelectionListener.class, listSelectionListener);
        if (listenerList.getListenerCount(ListSelectionListener.class) == 0) {
            swingListener.removeListeners(19);
        }
    }

    public static void addMenuListener(MenuListener menuListener) {
        if (listenerList.getListenerCount(MenuListener.class) == 0) {
            swingListener.installListeners(20);
        }
        listenerList.add(MenuListener.class, menuListener);
    }

    public static void removeMenuListener(MenuListener menuListener) {
        listenerList.remove(MenuListener.class, menuListener);
        if (listenerList.getListenerCount(MenuListener.class) == 0) {
            swingListener.removeListeners(20);
        }
    }

    public static void addPopupMenuListener(PopupMenuListener popupMenuListener) {
        if (listenerList.getListenerCount(PopupMenuListener.class) == 0) {
            swingListener.installListeners(21);
        }
        listenerList.add(PopupMenuListener.class, popupMenuListener);
    }

    public static void removePopupMenuListener(PopupMenuListener popupMenuListener) {
        listenerList.remove(PopupMenuListener.class, popupMenuListener);
        if (listenerList.getListenerCount(PopupMenuListener.class) == 0) {
            swingListener.removeListeners(21);
        }
    }

    public static void addTableModelListener(TableModelListener tableModelListener) {
        if (listenerList.getListenerCount(TableModelListener.class) == 0) {
            swingListener.installListeners(22);
        }
        listenerList.add(TableModelListener.class, tableModelListener);
    }

    public static void removeTableModelListener(TableModelListener tableModelListener) {
        listenerList.remove(TableModelListener.class, tableModelListener);
        if (listenerList.getListenerCount(TableModelListener.class) == 0) {
            swingListener.removeListeners(22);
        }
    }

    public static void addTreeExpansionListener(TreeExpansionListener treeExpansionListener) {
        if (listenerList.getListenerCount(TreeExpansionListener.class) == 0) {
            swingListener.installListeners(23);
        }
        listenerList.add(TreeExpansionListener.class, treeExpansionListener);
    }

    public static void removeTreeExpansionListener(TreeExpansionListener treeExpansionListener) {
        listenerList.remove(TreeExpansionListener.class, treeExpansionListener);
        if (listenerList.getListenerCount(TreeExpansionListener.class) == 0) {
            swingListener.removeListeners(23);
        }
    }

    public static void addTreeModelListener(TreeModelListener treeModelListener) {
        if (listenerList.getListenerCount(TreeModelListener.class) == 0) {
            swingListener.installListeners(24);
        }
        listenerList.add(TreeModelListener.class, treeModelListener);
    }

    public static void removeTreeModelListener(TreeModelListener treeModelListener) {
        listenerList.remove(TreeModelListener.class, treeModelListener);
        if (listenerList.getListenerCount(TreeModelListener.class) == 0) {
            swingListener.removeListeners(24);
        }
    }

    public static void addTreeSelectionListener(TreeSelectionListener treeSelectionListener) {
        if (listenerList.getListenerCount(TreeSelectionListener.class) == 0) {
            swingListener.installListeners(25);
        }
        listenerList.add(TreeSelectionListener.class, treeSelectionListener);
    }

    public static void removeTreeSelectionListener(TreeSelectionListener treeSelectionListener) {
        listenerList.remove(TreeSelectionListener.class, treeSelectionListener);
        if (listenerList.getListenerCount(TreeSelectionListener.class) == 0) {
            swingListener.removeListeners(25);
        }
    }

    public static void addUndoableEditListener(UndoableEditListener undoableEditListener) {
        if (listenerList.getListenerCount(UndoableEditListener.class) == 0) {
            swingListener.installListeners(26);
        }
        listenerList.add(UndoableEditListener.class, undoableEditListener);
    }

    public static void removeUndoableEditListener(UndoableEditListener undoableEditListener) {
        listenerList.remove(UndoableEditListener.class, undoableEditListener);
        if (listenerList.getListenerCount(UndoableEditListener.class) == 0) {
            swingListener.removeListeners(26);
        }
    }

    public static void addInternalFrameListener(InternalFrameListener internalFrameListener) {
        if (listenerList.getListenerCount(InternalFrameListener.class) == 0) {
            swingListener.installListeners(29);
        }
        listenerList.add(InternalFrameListener.class, internalFrameListener);
    }

    public static void removeInternalFrameListener(InternalFrameListener internalFrameListener) {
        listenerList.remove(InternalFrameListener.class, internalFrameListener);
        if (listenerList.getListenerCount(InternalFrameListener.class) == 0) {
            swingListener.removeListeners(29);
        }
    }

    public static void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        if (listenerList.getListenerCount(PropertyChangeListener.class) == 0) {
            swingListener.installListeners(27);
        }
        listenerList.add(PropertyChangeListener.class, propertyChangeListener);
    }

    public static void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        listenerList.remove(PropertyChangeListener.class, propertyChangeListener);
        if (listenerList.getListenerCount(PropertyChangeListener.class) == 0) {
            swingListener.removeListeners(27);
        }
    }

    public static void addVetoableChangeListener(VetoableChangeListener vetoableChangeListener) {
        if (listenerList.getListenerCount(VetoableChangeListener.class) == 0) {
            swingListener.installListeners(28);
        }
        listenerList.add(VetoableChangeListener.class, vetoableChangeListener);
    }

    public static void removeVetoableChangeListener(VetoableChangeListener vetoableChangeListener) {
        listenerList.remove(VetoableChangeListener.class, vetoableChangeListener);
        if (listenerList.getListenerCount(VetoableChangeListener.class) == 0) {
            swingListener.removeListeners(28);
        }
    }

    /* loaded from: jaccess.jar:com/sun/java/accessibility/util/SwingEventMonitor$SwingEventListener.class */
    static class SwingEventListener extends AWTEventMonitor.AWTEventsListener implements AncestorListener, CaretListener, CellEditorListener, ChangeListener, DocumentListener, ListDataListener, ListSelectionListener, MenuListener, PopupMenuListener, TableColumnModelListener, TableModelListener, TreeExpansionListener, TreeModelListener, TreeSelectionListener, UndoableEditListener, InternalFrameListener, PropertyChangeListener, VetoableChangeListener {
        private Class[] caretListeners;
        private Method removeCaretMethod;
        private Method addCaretMethod;
        private Object[] caretArgs;
        private Class[] cellEditorListeners;
        private Method removeCellEditorMethod;
        private Method addCellEditorMethod;
        private Object[] cellEditorArgs;
        private Method getCellEditorMethod;
        private Class[] changeListeners;
        private Method removeChangeMethod;
        private Method addChangeMethod;
        private Object[] changeArgs;
        private Method getColumnModelMethod;
        private Class[] documentListeners;
        private Method removeDocumentMethod;
        private Method addDocumentMethod;
        private Object[] documentArgs;
        private Method getDocumentMethod;
        private Method getModelMethod;
        private Class[] listSelectionListeners;
        private Method removeListSelectionMethod;
        private Method addListSelectionMethod;
        private Object[] listSelectionArgs;
        private Method getSelectionModelMethod;
        private Class[] menuListeners;
        private Method removeMenuMethod;
        private Method addMenuMethod;
        private Object[] menuArgs;
        private Class[] popupMenuListeners;
        private Method removePopupMenuMethod;
        private Method addPopupMenuMethod;
        private Object[] popupMenuArgs;
        private Method getPopupMenuMethod;
        private Class[] treeExpansionListeners;
        private Method removeTreeExpansionMethod;
        private Method addTreeExpansionMethod;
        private Object[] treeExpansionArgs;
        private Class[] treeSelectionListeners;
        private Method removeTreeSelectionMethod;
        private Method addTreeSelectionMethod;
        private Object[] treeSelectionArgs;
        private Class[] undoableEditListeners;
        private Method removeUndoableEditMethod;
        private Method addUndoableEditMethod;
        private Object[] undoableEditArgs;
        private Class[] internalFrameListeners;
        private Method removeInternalFrameMethod;
        private Method addInternalFrameMethod;
        private Object[] internalFrameArgs;
        private Class[] propertyChangeListeners;
        private Method removePropertyChangeMethod;
        private Method addPropertyChangeMethod;
        private Object[] propertyChangeArgs;
        private Class[] nullClass;
        private Object[] nullArgs;

        public SwingEventListener() {
            initializeIntrospection();
            installListeners();
            EventQueueMonitor.addTopLevelWindowListener(this);
        }

        private boolean initializeIntrospection() {
            try {
                this.caretListeners = new Class[1];
                this.caretArgs = new Object[1];
                this.caretListeners[0] = Class.forName("javax.swing.event.CaretListener");
                this.caretArgs[0] = this;
                this.cellEditorListeners = new Class[1];
                this.cellEditorArgs = new Object[1];
                this.cellEditorListeners[0] = Class.forName("javax.swing.event.CellEditorListener");
                this.cellEditorArgs[0] = this;
                this.changeListeners = new Class[1];
                this.changeArgs = new Object[1];
                this.changeListeners[0] = Class.forName("javax.swing.event.ChangeListener");
                this.changeArgs[0] = this;
                this.documentListeners = new Class[1];
                this.documentArgs = new Object[1];
                this.documentListeners[0] = Class.forName("javax.swing.event.DocumentListener");
                this.documentArgs[0] = this;
                this.listSelectionListeners = new Class[1];
                this.listSelectionArgs = new Object[1];
                this.listSelectionListeners[0] = Class.forName("javax.swing.event.ListSelectionListener");
                this.listSelectionArgs[0] = this;
                this.menuListeners = new Class[1];
                this.menuArgs = new Object[1];
                this.menuListeners[0] = Class.forName("javax.swing.event.MenuListener");
                this.menuArgs[0] = this;
                this.popupMenuListeners = new Class[1];
                this.popupMenuArgs = new Object[1];
                this.popupMenuListeners[0] = Class.forName("javax.swing.event.PopupMenuListener");
                this.popupMenuArgs[0] = this;
                this.treeExpansionListeners = new Class[1];
                this.treeExpansionArgs = new Object[1];
                this.treeExpansionListeners[0] = Class.forName("javax.swing.event.TreeExpansionListener");
                this.treeExpansionArgs[0] = this;
                this.treeSelectionListeners = new Class[1];
                this.treeSelectionArgs = new Object[1];
                this.treeSelectionListeners[0] = Class.forName("javax.swing.event.TreeSelectionListener");
                this.treeSelectionArgs[0] = this;
                this.undoableEditListeners = new Class[1];
                this.undoableEditArgs = new Object[1];
                this.undoableEditListeners[0] = Class.forName("javax.swing.event.UndoableEditListener");
                this.undoableEditArgs[0] = this;
                this.internalFrameListeners = new Class[1];
                this.internalFrameArgs = new Object[1];
                this.internalFrameListeners[0] = Class.forName("javax.swing.event.InternalFrameListener");
                this.internalFrameArgs[0] = this;
                this.nullClass = new Class[0];
                this.nullArgs = new Object[0];
                try {
                    this.propertyChangeListeners = new Class[1];
                    this.propertyChangeArgs = new Object[1];
                    this.propertyChangeListeners[0] = Class.forName("java.beans.PropertyChangeListener");
                    this.propertyChangeArgs[0] = this;
                    return true;
                } catch (ClassNotFoundException e2) {
                    System.out.println("EXCEPTION - Class 'java.beans.*' not in CLASSPATH");
                    return false;
                }
            } catch (ClassNotFoundException e3) {
                System.out.println("EXCEPTION - Class 'javax.swing.event.*' not in CLASSPATH");
                return false;
            }
        }

        @Override // com.sun.java.accessibility.util.AWTEventMonitor.AWTEventsListener
        protected void installListeners(Component component) throws IllegalArgumentException {
            installListeners(component, 3);
            if (SwingEventMonitor.listenerList.getListenerCount(AncestorListener.class) > 0) {
                installListeners(component, 12);
            }
            if (SwingEventMonitor.listenerList.getListenerCount(CaretListener.class) > 0) {
                installListeners(component, 13);
            }
            if (SwingEventMonitor.listenerList.getListenerCount(CellEditorListener.class) > 0) {
                installListeners(component, 14);
            }
            if (SwingEventMonitor.listenerList.getListenerCount(ChangeListener.class) > 0) {
                installListeners(component, 15);
            }
            if (SwingEventMonitor.listenerList.getListenerCount(TableColumnModelListener.class) > 0) {
                installListeners(component, 16);
            }
            if (SwingEventMonitor.listenerList.getListenerCount(DocumentListener.class) > 0) {
                installListeners(component, 17);
            }
            if (SwingEventMonitor.listenerList.getListenerCount(ListDataListener.class) > 0) {
                installListeners(component, 18);
            }
            if (SwingEventMonitor.listenerList.getListenerCount(ListSelectionListener.class) > 0) {
                installListeners(component, 19);
            }
            if (SwingEventMonitor.listenerList.getListenerCount(MenuListener.class) > 0) {
                installListeners(component, 20);
            }
            if (SwingEventMonitor.listenerList.getListenerCount(PopupMenuListener.class) > 0) {
                installListeners(component, 21);
            }
            if (SwingEventMonitor.listenerList.getListenerCount(TableModelListener.class) > 0) {
                installListeners(component, 22);
            }
            if (SwingEventMonitor.listenerList.getListenerCount(TreeExpansionListener.class) > 0) {
                installListeners(component, 23);
            }
            if (SwingEventMonitor.listenerList.getListenerCount(TreeModelListener.class) > 0) {
                installListeners(component, 24);
            }
            if (SwingEventMonitor.listenerList.getListenerCount(TreeSelectionListener.class) > 0) {
                installListeners(component, 25);
            }
            if (SwingEventMonitor.listenerList.getListenerCount(UndoableEditListener.class) > 0) {
                installListeners(component, 26);
            }
            if (SwingEventMonitor.listenerList.getListenerCount(InternalFrameListener.class) > 0) {
                installListeners(component, 29);
            }
            if (SwingEventMonitor.listenerList.getListenerCount(PropertyChangeListener.class) > 0) {
                installListeners(component, 27);
            }
            if (SwingEventMonitor.listenerList.getListenerCount(VetoableChangeListener.class) > 0) {
                installListeners(component, 28);
            }
            super.installListeners(component);
        }

        @Override // com.sun.java.accessibility.util.AWTEventMonitor.AWTEventsListener
        protected void installListeners(Component component, int i2) throws IllegalArgumentException {
            switch (i2) {
                case 3:
                    if (component instanceof Container) {
                        ((Container) component).removeContainerListener(this);
                        ((Container) component).addContainerListener(this);
                        break;
                    }
                    break;
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                default:
                    return;
                case 12:
                    if (component instanceof JComponent) {
                        ((JComponent) component).removeAncestorListener(this);
                        ((JComponent) component).addAncestorListener(this);
                        break;
                    }
                    break;
                case 13:
                    try {
                        this.removeCaretMethod = component.getClass().getMethod("removeCaretListener", this.caretListeners);
                        this.addCaretMethod = component.getClass().getMethod("addCaretListener", this.caretListeners);
                        try {
                            this.removeCaretMethod.invoke(component, this.caretArgs);
                            this.addCaretMethod.invoke(component, this.caretArgs);
                        } catch (IllegalAccessException e2) {
                            System.out.println("Exception: " + e2.toString());
                        } catch (InvocationTargetException e3) {
                            System.out.println("Exception: " + e3.toString());
                        }
                        break;
                    } catch (NoSuchMethodException e4) {
                        break;
                    } catch (SecurityException e5) {
                        System.out.println("Exception: " + e5.toString());
                        break;
                    }
                case 14:
                    try {
                        this.getCellEditorMethod = component.getClass().getMethod("getCellEditorMethod", this.nullClass);
                        try {
                            Object objInvoke = this.getCellEditorMethod.invoke(component, this.nullArgs);
                            if (objInvoke != null && (objInvoke instanceof CellEditor)) {
                                ((CellEditor) objInvoke).removeCellEditorListener(this);
                                ((CellEditor) objInvoke).addCellEditorListener(this);
                            }
                        } catch (IllegalAccessException e6) {
                            System.out.println("Exception: " + e6.toString());
                        } catch (InvocationTargetException e7) {
                            System.out.println("Exception: " + e7.toString());
                        }
                    } catch (NoSuchMethodException e8) {
                    } catch (SecurityException e9) {
                        System.out.println("Exception: " + e9.toString());
                    }
                    try {
                        this.removeCellEditorMethod = component.getClass().getMethod("removeCellEditorListener", this.cellEditorListeners);
                        this.addCellEditorMethod = component.getClass().getMethod("addCellEditorListener", this.cellEditorListeners);
                        try {
                            this.removeCellEditorMethod.invoke(component, this.cellEditorArgs);
                            this.addCellEditorMethod.invoke(component, this.cellEditorArgs);
                        } catch (IllegalAccessException e10) {
                            System.out.println("Exception: " + e10.toString());
                        } catch (InvocationTargetException e11) {
                            System.out.println("Exception: " + e11.toString());
                        }
                        break;
                    } catch (NoSuchMethodException e12) {
                        break;
                    } catch (SecurityException e13) {
                        System.out.println("Exception: " + e13.toString());
                        break;
                    }
                    break;
                case 15:
                    try {
                        this.removeChangeMethod = component.getClass().getMethod("removeChangeListener", this.changeListeners);
                        this.addChangeMethod = component.getClass().getMethod("addChangeListener", this.changeListeners);
                        try {
                            this.removeChangeMethod.invoke(component, this.changeArgs);
                            this.addChangeMethod.invoke(component, this.changeArgs);
                        } catch (IllegalAccessException e14) {
                            System.out.println("Exception: " + e14.toString());
                        } catch (InvocationTargetException e15) {
                            System.out.println("Exception: " + e15.toString());
                        }
                    } catch (NoSuchMethodException e16) {
                    } catch (SecurityException e17) {
                        System.out.println("Exception: " + e17.toString());
                    }
                    try {
                        this.getModelMethod = component.getClass().getMethod("getModel", this.nullClass);
                        try {
                            Object objInvoke2 = this.getModelMethod.invoke(component, this.nullArgs);
                            if (objInvoke2 != null) {
                                this.removeChangeMethod = objInvoke2.getClass().getMethod("removeChangeListener", this.changeListeners);
                                this.addChangeMethod = objInvoke2.getClass().getMethod("addChangeListener", this.changeListeners);
                                this.removeChangeMethod.invoke(objInvoke2, this.changeArgs);
                                this.addChangeMethod.invoke(objInvoke2, this.changeArgs);
                            }
                        } catch (IllegalAccessException e18) {
                            System.out.println("Exception: " + e18.toString());
                        } catch (InvocationTargetException e19) {
                            System.out.println("Exception: " + e19.toString());
                        }
                        break;
                    } catch (NoSuchMethodException e20) {
                        break;
                    } catch (SecurityException e21) {
                        System.out.println("Exception: " + e21.toString());
                        break;
                    }
                case 16:
                    try {
                        this.getColumnModelMethod = component.getClass().getMethod("getTableColumnModel", this.nullClass);
                        try {
                            Object objInvoke3 = this.getColumnModelMethod.invoke(component, this.nullArgs);
                            if (objInvoke3 != null && (objInvoke3 instanceof TableColumnModel)) {
                                ((TableColumnModel) objInvoke3).removeColumnModelListener(this);
                                ((TableColumnModel) objInvoke3).addColumnModelListener(this);
                            }
                        } catch (IllegalAccessException e22) {
                            System.out.println("Exception: " + e22.toString());
                        } catch (InvocationTargetException e23) {
                            System.out.println("Exception: " + e23.toString());
                        }
                        break;
                    } catch (NoSuchMethodException e24) {
                        break;
                    } catch (SecurityException e25) {
                        System.out.println("Exception: " + e25.toString());
                        break;
                    }
                    break;
                case 17:
                    try {
                        this.getDocumentMethod = component.getClass().getMethod("getDocument", this.nullClass);
                        try {
                            Object objInvoke4 = this.getDocumentMethod.invoke(component, this.nullArgs);
                            if (objInvoke4 != null && (objInvoke4 instanceof Document)) {
                                ((Document) objInvoke4).removeDocumentListener(this);
                                ((Document) objInvoke4).addDocumentListener(this);
                            }
                        } catch (IllegalAccessException e26) {
                            System.out.println("Exception: " + e26.toString());
                        } catch (InvocationTargetException e27) {
                            System.out.println("Exception: " + e27.toString());
                        }
                    } catch (NoSuchMethodException e28) {
                    } catch (SecurityException e29) {
                        System.out.println("Exception: " + e29.toString());
                    }
                    try {
                        this.removeDocumentMethod = component.getClass().getMethod("removeDocumentListener", this.documentListeners);
                        this.addDocumentMethod = component.getClass().getMethod("addDocumentListener", this.documentListeners);
                        try {
                            this.removeDocumentMethod.invoke(component, this.documentArgs);
                            this.addDocumentMethod.invoke(component, this.documentArgs);
                        } catch (IllegalAccessException e30) {
                            System.out.println("Exception: " + e30.toString());
                        } catch (InvocationTargetException e31) {
                            System.out.println("Exception: " + e31.toString());
                        }
                    } catch (NoSuchMethodException e32) {
                    } catch (SecurityException e33) {
                        System.out.println("Exception: " + e33.toString());
                    }
                    if (component instanceof JTextComponent) {
                        try {
                            this.removePropertyChangeMethod = component.getClass().getMethod("removePropertyChangeListener", this.propertyChangeListeners);
                            this.addPropertyChangeMethod = component.getClass().getMethod("addPropertyChangeListener", this.propertyChangeListeners);
                            try {
                                this.removePropertyChangeMethod.invoke(component, this.propertyChangeArgs);
                                this.addPropertyChangeMethod.invoke(component, this.propertyChangeArgs);
                            } catch (IllegalAccessException e34) {
                                System.out.println("Exception: " + e34.toString());
                            } catch (InvocationTargetException e35) {
                                System.out.println("Exception: " + e35.toString());
                            }
                            break;
                        } catch (NoSuchMethodException e36) {
                            break;
                        } catch (SecurityException e37) {
                            System.out.println("Exception: " + e37.toString());
                            break;
                        }
                    }
                    break;
                case 18:
                case 22:
                case 24:
                    try {
                        this.getModelMethod = component.getClass().getMethod("getModel", this.nullClass);
                        try {
                            Object objInvoke5 = this.getModelMethod.invoke(component, this.nullArgs);
                            if (objInvoke5 != null) {
                                if (i2 == 18 && (objInvoke5 instanceof ListModel)) {
                                    ((ListModel) objInvoke5).removeListDataListener(this);
                                    ((ListModel) objInvoke5).addListDataListener(this);
                                } else if (i2 == 22 && (objInvoke5 instanceof TableModel)) {
                                    ((TableModel) objInvoke5).removeTableModelListener(this);
                                    ((TableModel) objInvoke5).addTableModelListener(this);
                                } else if (objInvoke5 instanceof TreeModel) {
                                    ((TreeModel) objInvoke5).removeTreeModelListener(this);
                                    ((TreeModel) objInvoke5).addTreeModelListener(this);
                                }
                            }
                        } catch (IllegalAccessException e38) {
                            System.out.println("Exception: " + e38.toString());
                        } catch (InvocationTargetException e39) {
                            System.out.println("Exception: " + e39.toString());
                        }
                        break;
                    } catch (NoSuchMethodException e40) {
                        break;
                    } catch (SecurityException e41) {
                        System.out.println("Exception: " + e41.toString());
                        break;
                    }
                    break;
                case 19:
                    try {
                        this.removeListSelectionMethod = component.getClass().getMethod("removeListSelectionListener", this.listSelectionListeners);
                        this.addListSelectionMethod = component.getClass().getMethod("addListSelectionListener", this.listSelectionListeners);
                        try {
                            this.removeListSelectionMethod.invoke(component, this.listSelectionArgs);
                            this.addListSelectionMethod.invoke(component, this.listSelectionArgs);
                        } catch (IllegalAccessException e42) {
                            System.out.println("Exception: " + e42.toString());
                        } catch (InvocationTargetException e43) {
                            System.out.println("Exception: " + e43.toString());
                        }
                    } catch (NoSuchMethodException e44) {
                    } catch (SecurityException e45) {
                        System.out.println("Exception: " + e45.toString());
                    }
                    try {
                        this.getSelectionModelMethod = component.getClass().getMethod("getSelectionModel", this.nullClass);
                        try {
                            Object objInvoke6 = this.getSelectionModelMethod.invoke(component, this.nullArgs);
                            if (objInvoke6 != null && (objInvoke6 instanceof ListSelectionModel)) {
                                ((ListSelectionModel) objInvoke6).removeListSelectionListener(this);
                                ((ListSelectionModel) objInvoke6).addListSelectionListener(this);
                            }
                        } catch (IllegalAccessException e46) {
                            System.out.println("Exception: " + e46.toString());
                        } catch (InvocationTargetException e47) {
                            System.out.println("Exception: " + e47.toString());
                        }
                        break;
                    } catch (NoSuchMethodException e48) {
                        break;
                    } catch (SecurityException e49) {
                        System.out.println("Exception: " + e49.toString());
                        break;
                    }
                    break;
                case 20:
                    try {
                        this.removeMenuMethod = component.getClass().getMethod("removeMenuListener", this.menuListeners);
                        this.addMenuMethod = component.getClass().getMethod("addMenuListener", this.menuListeners);
                        try {
                            this.removeMenuMethod.invoke(component, this.menuArgs);
                            this.addMenuMethod.invoke(component, this.menuArgs);
                        } catch (IllegalAccessException e50) {
                            System.out.println("Exception: " + e50.toString());
                        } catch (InvocationTargetException e51) {
                            System.out.println("Exception: " + e51.toString());
                        }
                        break;
                    } catch (NoSuchMethodException e52) {
                        break;
                    } catch (SecurityException e53) {
                        System.out.println("Exception: " + e53.toString());
                        break;
                    }
                case 21:
                    try {
                        this.removePopupMenuMethod = component.getClass().getMethod("removePopupMenuListener", this.popupMenuListeners);
                        this.addPopupMenuMethod = component.getClass().getMethod("addPopupMenuListener", this.popupMenuListeners);
                        try {
                            this.removePopupMenuMethod.invoke(component, this.popupMenuArgs);
                            this.addPopupMenuMethod.invoke(component, this.popupMenuArgs);
                        } catch (IllegalAccessException e54) {
                            System.out.println("Exception: " + e54.toString());
                        } catch (InvocationTargetException e55) {
                            System.out.println("Exception: " + e55.toString());
                        }
                    } catch (NoSuchMethodException e56) {
                    } catch (SecurityException e57) {
                        System.out.println("Exception: " + e57.toString());
                    }
                    try {
                        this.getPopupMenuMethod = component.getClass().getMethod("getPopupMenu", this.nullClass);
                        try {
                            Object objInvoke7 = this.getPopupMenuMethod.invoke(component, this.nullArgs);
                            if (objInvoke7 != null) {
                                this.removePopupMenuMethod = objInvoke7.getClass().getMethod("removePopupMenuListener", this.popupMenuListeners);
                                this.addPopupMenuMethod = objInvoke7.getClass().getMethod("addPopupMenuListener", this.popupMenuListeners);
                                this.removePopupMenuMethod.invoke(objInvoke7, this.popupMenuArgs);
                                this.addPopupMenuMethod.invoke(objInvoke7, this.popupMenuArgs);
                            }
                        } catch (IllegalAccessException e58) {
                            System.out.println("Exception: " + e58.toString());
                        } catch (InvocationTargetException e59) {
                            System.out.println("Exception: " + e59.toString());
                        }
                        break;
                    } catch (NoSuchMethodException e60) {
                        break;
                    } catch (SecurityException e61) {
                        System.out.println("Exception: " + e61.toString());
                        break;
                    }
                case 23:
                    try {
                        this.removeTreeExpansionMethod = component.getClass().getMethod("removeTreeExpansionListener", this.treeExpansionListeners);
                        this.addTreeExpansionMethod = component.getClass().getMethod("addTreeExpansionListener", this.treeExpansionListeners);
                        try {
                            this.removeTreeExpansionMethod.invoke(component, this.treeExpansionArgs);
                            this.addTreeExpansionMethod.invoke(component, this.treeExpansionArgs);
                        } catch (IllegalAccessException e62) {
                            System.out.println("Exception: " + e62.toString());
                        } catch (InvocationTargetException e63) {
                            System.out.println("Exception: " + e63.toString());
                        }
                        break;
                    } catch (NoSuchMethodException e64) {
                        break;
                    } catch (SecurityException e65) {
                        System.out.println("Exception: " + e65.toString());
                        break;
                    }
                case 25:
                    try {
                        this.removeTreeSelectionMethod = component.getClass().getMethod("removeTreeSelectionListener", this.treeSelectionListeners);
                        this.addTreeSelectionMethod = component.getClass().getMethod("addTreeSelectionListener", this.treeSelectionListeners);
                        try {
                            this.removeTreeSelectionMethod.invoke(component, this.treeSelectionArgs);
                            this.addTreeSelectionMethod.invoke(component, this.treeSelectionArgs);
                        } catch (IllegalAccessException e66) {
                            System.out.println("Exception: " + e66.toString());
                        } catch (InvocationTargetException e67) {
                            System.out.println("Exception: " + e67.toString());
                        }
                        break;
                    } catch (NoSuchMethodException e68) {
                        break;
                    } catch (SecurityException e69) {
                        System.out.println("Exception: " + e69.toString());
                        break;
                    }
                case 26:
                    try {
                        this.getDocumentMethod = component.getClass().getMethod("getDocument", this.nullClass);
                        try {
                            Object objInvoke8 = this.getDocumentMethod.invoke(component, this.nullArgs);
                            if (objInvoke8 != null && (objInvoke8 instanceof Document)) {
                                ((Document) objInvoke8).removeUndoableEditListener(this);
                                ((Document) objInvoke8).addUndoableEditListener(this);
                            }
                        } catch (IllegalAccessException e70) {
                            System.out.println("Exception: " + e70.toString());
                        } catch (InvocationTargetException e71) {
                            System.out.println("Exception: " + e71.toString());
                        }
                    } catch (NoSuchMethodException e72) {
                    } catch (SecurityException e73) {
                        System.out.println("Exception: " + e73.toString());
                    }
                    try {
                        this.removeUndoableEditMethod = component.getClass().getMethod("removeUndoableEditListener", this.undoableEditListeners);
                        this.addUndoableEditMethod = component.getClass().getMethod("addUndoableEditListener", this.undoableEditListeners);
                        try {
                            this.removeUndoableEditMethod.invoke(component, this.undoableEditArgs);
                            this.addUndoableEditMethod.invoke(component, this.undoableEditArgs);
                        } catch (IllegalAccessException e74) {
                            System.out.println("Exception: " + e74.toString());
                        } catch (InvocationTargetException e75) {
                            System.out.println("Exception: " + e75.toString());
                        }
                        break;
                    } catch (NoSuchMethodException e76) {
                        break;
                    } catch (SecurityException e77) {
                        System.out.println("Exception: " + e77.toString());
                        break;
                    }
                case 27:
                    try {
                        this.removePropertyChangeMethod = component.getClass().getMethod("removePropertyChangeListener", this.propertyChangeListeners);
                        this.addPropertyChangeMethod = component.getClass().getMethod("addPropertyChangeListener", this.propertyChangeListeners);
                        try {
                            this.removePropertyChangeMethod.invoke(component, this.propertyChangeArgs);
                            this.addPropertyChangeMethod.invoke(component, this.propertyChangeArgs);
                        } catch (IllegalAccessException e78) {
                            System.out.println("Exception: " + e78.toString());
                        } catch (InvocationTargetException e79) {
                            System.out.println("Exception: " + e79.toString());
                        }
                    } catch (NoSuchMethodException e80) {
                    } catch (SecurityException e81) {
                        System.out.println("Exception: " + e81.toString());
                    }
                    try {
                        this.getSelectionModelMethod = component.getClass().getMethod("getSelectionModel", this.nullClass);
                        try {
                            Object objInvoke9 = this.getSelectionModelMethod.invoke(component, this.nullArgs);
                            if (objInvoke9 != null && (objInvoke9 instanceof TreeSelectionModel)) {
                                ((TreeSelectionModel) objInvoke9).removePropertyChangeListener(this);
                                ((TreeSelectionModel) objInvoke9).addPropertyChangeListener(this);
                            }
                        } catch (IllegalAccessException e82) {
                            System.out.println("Exception: " + e82.toString());
                        } catch (InvocationTargetException e83) {
                            System.out.println("Exception: " + e83.toString());
                        }
                        break;
                    } catch (NoSuchMethodException e84) {
                        break;
                    } catch (SecurityException e85) {
                        System.out.println("Exception: " + e85.toString());
                        break;
                    }
                    break;
                case 28:
                    if (component instanceof JComponent) {
                        ((JComponent) component).removeVetoableChangeListener(this);
                        ((JComponent) component).addVetoableChangeListener(this);
                        break;
                    }
                    break;
                case 29:
                    try {
                        this.removeInternalFrameMethod = component.getClass().getMethod("removeInternalFrameListener", this.internalFrameListeners);
                        this.addInternalFrameMethod = component.getClass().getMethod("addInternalFrameListener", this.internalFrameListeners);
                        try {
                            this.removeInternalFrameMethod.invoke(component, this.internalFrameArgs);
                            this.addInternalFrameMethod.invoke(component, this.internalFrameArgs);
                        } catch (IllegalAccessException e86) {
                            System.out.println("Exception: " + e86.toString());
                        } catch (InvocationTargetException e87) {
                            System.out.println("Exception: " + e87.toString());
                        }
                        break;
                    } catch (NoSuchMethodException e88) {
                        break;
                    } catch (SecurityException e89) {
                        System.out.println("Exception: " + e89.toString());
                        break;
                    }
            }
            if (component instanceof Container) {
                int componentCount = ((Container) component).getComponentCount();
                for (int i3 = 0; i3 < componentCount; i3++) {
                    installListeners(((Container) component).getComponent(i3), i2);
                }
            }
        }

        @Override // com.sun.java.accessibility.util.AWTEventMonitor.AWTEventsListener
        protected void removeListeners(Component component) throws IllegalArgumentException {
            if (SwingEventMonitor.listenerList.getListenerCount(AncestorListener.class) > 0) {
                removeListeners(component, 12);
            }
            if (SwingEventMonitor.listenerList.getListenerCount(CaretListener.class) > 0) {
                removeListeners(component, 13);
            }
            if (SwingEventMonitor.listenerList.getListenerCount(CellEditorListener.class) > 0) {
                removeListeners(component, 14);
            }
            if (SwingEventMonitor.listenerList.getListenerCount(ChangeListener.class) > 0) {
                removeListeners(component, 15);
            }
            if (SwingEventMonitor.listenerList.getListenerCount(TableColumnModelListener.class) > 0) {
                removeListeners(component, 16);
            }
            if (SwingEventMonitor.listenerList.getListenerCount(DocumentListener.class) > 0) {
                removeListeners(component, 17);
            }
            if (SwingEventMonitor.listenerList.getListenerCount(ListDataListener.class) > 0) {
                removeListeners(component, 18);
            }
            if (SwingEventMonitor.listenerList.getListenerCount(ListSelectionListener.class) > 0) {
                removeListeners(component, 19);
            }
            if (SwingEventMonitor.listenerList.getListenerCount(MenuListener.class) > 0) {
                removeListeners(component, 20);
            }
            if (SwingEventMonitor.listenerList.getListenerCount(PopupMenuListener.class) > 0) {
                removeListeners(component, 21);
            }
            if (SwingEventMonitor.listenerList.getListenerCount(TableModelListener.class) > 0) {
                removeListeners(component, 22);
            }
            if (SwingEventMonitor.listenerList.getListenerCount(TreeExpansionListener.class) > 0) {
                removeListeners(component, 23);
            }
            if (SwingEventMonitor.listenerList.getListenerCount(TreeModelListener.class) > 0) {
                removeListeners(component, 24);
            }
            if (SwingEventMonitor.listenerList.getListenerCount(TreeSelectionListener.class) > 0) {
                removeListeners(component, 25);
            }
            if (SwingEventMonitor.listenerList.getListenerCount(UndoableEditListener.class) > 0) {
                removeListeners(component, 26);
            }
            if (SwingEventMonitor.listenerList.getListenerCount(InternalFrameListener.class) > 0) {
                removeListeners(component, 29);
            }
            if (SwingEventMonitor.listenerList.getListenerCount(PropertyChangeListener.class) > 0) {
                removeListeners(component, 27);
            }
            if (SwingEventMonitor.listenerList.getListenerCount(VetoableChangeListener.class) > 0) {
                removeListeners(component, 28);
            }
            super.removeListeners(component);
        }

        @Override // com.sun.java.accessibility.util.AWTEventMonitor.AWTEventsListener
        protected void removeListeners(Component component, int i2) throws IllegalArgumentException {
            switch (i2) {
                case 3:
                    break;
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                default:
                    return;
                case 12:
                    if (component instanceof JComponent) {
                        ((JComponent) component).removeAncestorListener(this);
                        break;
                    }
                    break;
                case 13:
                    try {
                        this.removeCaretMethod = component.getClass().getMethod("removeCaretListener", this.caretListeners);
                        try {
                            this.removeCaretMethod.invoke(component, this.caretArgs);
                        } catch (IllegalAccessException e2) {
                            System.out.println("Exception: " + e2.toString());
                        } catch (InvocationTargetException e3) {
                            System.out.println("Exception: " + e3.toString());
                        }
                        break;
                    } catch (NoSuchMethodException e4) {
                        break;
                    } catch (SecurityException e5) {
                        System.out.println("Exception: " + e5.toString());
                        break;
                    }
                case 14:
                    try {
                        this.getCellEditorMethod = component.getClass().getMethod("getCellEditorMethod", this.nullClass);
                        try {
                            Object objInvoke = this.getCellEditorMethod.invoke(component, this.nullArgs);
                            if (objInvoke != null && (objInvoke instanceof CellEditor)) {
                                ((CellEditor) objInvoke).removeCellEditorListener(this);
                            }
                        } catch (IllegalAccessException e6) {
                            System.out.println("Exception: " + e6.toString());
                        } catch (InvocationTargetException e7) {
                            System.out.println("Exception: " + e7.toString());
                        }
                    } catch (NoSuchMethodException e8) {
                    } catch (SecurityException e9) {
                        System.out.println("Exception: " + e9.toString());
                    }
                    try {
                        this.removeCellEditorMethod = component.getClass().getMethod("removeCellEditorListener", this.cellEditorListeners);
                        try {
                            this.removeCellEditorMethod.invoke(component, this.cellEditorArgs);
                        } catch (IllegalAccessException e10) {
                            System.out.println("Exception: " + e10.toString());
                        } catch (InvocationTargetException e11) {
                            System.out.println("Exception: " + e11.toString());
                        }
                        break;
                    } catch (NoSuchMethodException e12) {
                        break;
                    } catch (SecurityException e13) {
                        System.out.println("Exception: " + e13.toString());
                        break;
                    }
                    break;
                case 15:
                    try {
                        this.removeChangeMethod = component.getClass().getMethod("removeChangeListener", this.changeListeners);
                        try {
                            this.removeChangeMethod.invoke(component, this.changeArgs);
                        } catch (IllegalAccessException e14) {
                            System.out.println("Exception: " + e14.toString());
                        } catch (InvocationTargetException e15) {
                            System.out.println("Exception: " + e15.toString());
                        }
                    } catch (NoSuchMethodException e16) {
                    } catch (SecurityException e17) {
                        System.out.println("Exception: " + e17.toString());
                    }
                    try {
                        this.getModelMethod = component.getClass().getMethod("getModel", this.nullClass);
                        try {
                            Object objInvoke2 = this.getModelMethod.invoke(component, this.nullArgs);
                            if (objInvoke2 != null) {
                                this.removeChangeMethod = objInvoke2.getClass().getMethod("removeChangeListener", this.changeListeners);
                                this.removeChangeMethod.invoke(objInvoke2, this.changeArgs);
                            }
                        } catch (IllegalAccessException e18) {
                            System.out.println("Exception: " + e18.toString());
                        } catch (InvocationTargetException e19) {
                            System.out.println("Exception: " + e19.toString());
                        }
                        break;
                    } catch (NoSuchMethodException e20) {
                        break;
                    } catch (SecurityException e21) {
                        System.out.println("Exception: " + e21.toString());
                        break;
                    }
                case 16:
                    try {
                        this.getColumnModelMethod = component.getClass().getMethod("getTableColumnModel", this.nullClass);
                        try {
                            Object objInvoke3 = this.getColumnModelMethod.invoke(component, this.nullArgs);
                            if (objInvoke3 != null && (objInvoke3 instanceof TableColumnModel)) {
                                ((TableColumnModel) objInvoke3).removeColumnModelListener(this);
                            }
                        } catch (IllegalAccessException e22) {
                            System.out.println("Exception: " + e22.toString());
                        } catch (InvocationTargetException e23) {
                            System.out.println("Exception: " + e23.toString());
                        }
                        break;
                    } catch (NoSuchMethodException e24) {
                        break;
                    } catch (SecurityException e25) {
                        System.out.println("Exception: " + e25.toString());
                        break;
                    }
                    break;
                case 17:
                    try {
                        this.getDocumentMethod = component.getClass().getMethod("getDocument", this.nullClass);
                        try {
                            Object objInvoke4 = this.getDocumentMethod.invoke(component, this.nullArgs);
                            if (objInvoke4 != null && (objInvoke4 instanceof Document)) {
                                ((Document) objInvoke4).removeDocumentListener(this);
                            }
                        } catch (IllegalAccessException e26) {
                            System.out.println("Exception: " + e26.toString());
                        } catch (InvocationTargetException e27) {
                            System.out.println("Exception: " + e27.toString());
                        }
                    } catch (NoSuchMethodException e28) {
                    } catch (SecurityException e29) {
                        System.out.println("Exception: " + e29.toString());
                    }
                    try {
                        this.removeDocumentMethod = component.getClass().getMethod("removeDocumentListener", this.documentListeners);
                        try {
                            this.removeDocumentMethod.invoke(component, this.documentArgs);
                        } catch (IllegalAccessException e30) {
                            System.out.println("Exception: " + e30.toString());
                        } catch (InvocationTargetException e31) {
                            System.out.println("Exception: " + e31.toString());
                        }
                        break;
                    } catch (NoSuchMethodException e32) {
                        break;
                    } catch (SecurityException e33) {
                        System.out.println("Exception: " + e33.toString());
                        break;
                    }
                    break;
                case 18:
                case 22:
                case 24:
                    try {
                        this.getModelMethod = component.getClass().getMethod("getModel", this.nullClass);
                        try {
                            Object objInvoke5 = this.getModelMethod.invoke(component, this.nullArgs);
                            if (objInvoke5 != null) {
                                if (i2 == 18 && (objInvoke5 instanceof ListModel)) {
                                    ((ListModel) objInvoke5).removeListDataListener(this);
                                } else if (i2 == 22 && (objInvoke5 instanceof TableModel)) {
                                    ((TableModel) objInvoke5).removeTableModelListener(this);
                                } else if (objInvoke5 instanceof TreeModel) {
                                    ((TreeModel) objInvoke5).removeTreeModelListener(this);
                                }
                            }
                        } catch (IllegalAccessException e34) {
                            System.out.println("Exception: " + e34.toString());
                        } catch (InvocationTargetException e35) {
                            System.out.println("Exception: " + e35.toString());
                        }
                        break;
                    } catch (NoSuchMethodException e36) {
                        break;
                    } catch (SecurityException e37) {
                        System.out.println("Exception: " + e37.toString());
                        break;
                    }
                case 19:
                    try {
                        this.removeListSelectionMethod = component.getClass().getMethod("removeListSelectionListener", this.listSelectionListeners);
                        try {
                            this.removeListSelectionMethod.invoke(component, this.listSelectionArgs);
                        } catch (IllegalAccessException e38) {
                            System.out.println("Exception: " + e38.toString());
                        } catch (InvocationTargetException e39) {
                            System.out.println("Exception: " + e39.toString());
                        }
                    } catch (NoSuchMethodException e40) {
                    } catch (SecurityException e41) {
                        System.out.println("Exception: " + e41.toString());
                    }
                    try {
                        this.getSelectionModelMethod = component.getClass().getMethod("getSelectionModel", this.nullClass);
                        try {
                            Object objInvoke6 = this.getSelectionModelMethod.invoke(component, this.nullArgs);
                            if (objInvoke6 != null && (objInvoke6 instanceof ListSelectionModel)) {
                                ((ListSelectionModel) objInvoke6).removeListSelectionListener(this);
                            }
                        } catch (IllegalAccessException e42) {
                            System.out.println("Exception: " + e42.toString());
                        } catch (InvocationTargetException e43) {
                            System.out.println("Exception: " + e43.toString());
                        }
                        break;
                    } catch (NoSuchMethodException e44) {
                        break;
                    } catch (SecurityException e45) {
                        System.out.println("Exception: " + e45.toString());
                        break;
                    }
                    break;
                case 20:
                    try {
                        this.removeMenuMethod = component.getClass().getMethod("removeMenuListener", this.menuListeners);
                        try {
                            this.removeMenuMethod.invoke(component, this.menuArgs);
                        } catch (IllegalAccessException e46) {
                            System.out.println("Exception: " + e46.toString());
                        } catch (InvocationTargetException e47) {
                            System.out.println("Exception: " + e47.toString());
                        }
                        break;
                    } catch (NoSuchMethodException e48) {
                        break;
                    } catch (SecurityException e49) {
                        System.out.println("Exception: " + e49.toString());
                        break;
                    }
                case 21:
                    try {
                        this.removePopupMenuMethod = component.getClass().getMethod("removePopupMenuListener", this.popupMenuListeners);
                        try {
                            this.removePopupMenuMethod.invoke(component, this.popupMenuArgs);
                        } catch (IllegalAccessException e50) {
                            System.out.println("Exception: " + e50.toString());
                        } catch (InvocationTargetException e51) {
                            System.out.println("Exception: " + e51.toString());
                        }
                    } catch (NoSuchMethodException e52) {
                    } catch (SecurityException e53) {
                        System.out.println("Exception: " + e53.toString());
                    }
                    try {
                        this.getPopupMenuMethod = component.getClass().getMethod("getPopupMenu", this.nullClass);
                        try {
                            Object objInvoke7 = this.getPopupMenuMethod.invoke(component, this.nullArgs);
                            if (objInvoke7 != null) {
                                this.removePopupMenuMethod = objInvoke7.getClass().getMethod("removePopupMenuListener", this.popupMenuListeners);
                                this.removePopupMenuMethod.invoke(objInvoke7, this.popupMenuArgs);
                            }
                        } catch (IllegalAccessException e54) {
                            System.out.println("Exception: " + e54.toString());
                        } catch (InvocationTargetException e55) {
                            System.out.println("Exception: " + e55.toString());
                        }
                        break;
                    } catch (NoSuchMethodException e56) {
                        break;
                    } catch (SecurityException e57) {
                        System.out.println("Exception: " + e57.toString());
                        break;
                    }
                case 23:
                    try {
                        this.removeTreeExpansionMethod = component.getClass().getMethod("removeTreeExpansionListener", this.treeExpansionListeners);
                        try {
                            this.removeTreeExpansionMethod.invoke(component, this.treeExpansionArgs);
                        } catch (IllegalAccessException e58) {
                            System.out.println("Exception: " + e58.toString());
                        } catch (InvocationTargetException e59) {
                            System.out.println("Exception: " + e59.toString());
                        }
                        break;
                    } catch (NoSuchMethodException e60) {
                        break;
                    } catch (SecurityException e61) {
                        System.out.println("Exception: " + e61.toString());
                        break;
                    }
                case 25:
                    try {
                        this.removeTreeSelectionMethod = component.getClass().getMethod("removeTreeSelectionListener", this.treeSelectionListeners);
                        try {
                            this.removeTreeSelectionMethod.invoke(component, this.treeSelectionArgs);
                        } catch (IllegalAccessException e62) {
                            System.out.println("Exception: " + e62.toString());
                        } catch (InvocationTargetException e63) {
                            System.out.println("Exception: " + e63.toString());
                        }
                        break;
                    } catch (NoSuchMethodException e64) {
                        break;
                    } catch (SecurityException e65) {
                        System.out.println("Exception: " + e65.toString());
                        break;
                    }
                case 26:
                    try {
                        this.getDocumentMethod = component.getClass().getMethod("getDocument", this.nullClass);
                        try {
                            Object objInvoke8 = this.getDocumentMethod.invoke(component, this.nullArgs);
                            if (objInvoke8 != null && (objInvoke8 instanceof Document)) {
                                ((Document) objInvoke8).removeUndoableEditListener(this);
                            }
                        } catch (IllegalAccessException e66) {
                            System.out.println("Exception: " + e66.toString());
                        } catch (InvocationTargetException e67) {
                            System.out.println("Exception: " + e67.toString());
                        }
                    } catch (NoSuchMethodException e68) {
                    } catch (SecurityException e69) {
                        System.out.println("Exception: " + e69.toString());
                    }
                    try {
                        this.removeUndoableEditMethod = component.getClass().getMethod("removeUndoableEditListener", this.undoableEditListeners);
                        try {
                            this.removeUndoableEditMethod.invoke(component, this.undoableEditArgs);
                        } catch (IllegalAccessException e70) {
                            System.out.println("Exception: " + e70.toString());
                        } catch (InvocationTargetException e71) {
                            System.out.println("Exception: " + e71.toString());
                        }
                        break;
                    } catch (NoSuchMethodException e72) {
                        break;
                    } catch (SecurityException e73) {
                        System.out.println("Exception: " + e73.toString());
                        break;
                    }
                    break;
                case 27:
                    try {
                        this.removePropertyChangeMethod = component.getClass().getMethod("removePropertyChangeListener", this.propertyChangeListeners);
                        try {
                            this.removePropertyChangeMethod.invoke(component, this.propertyChangeArgs);
                        } catch (IllegalAccessException e74) {
                            System.out.println("Exception: " + e74.toString());
                        } catch (InvocationTargetException e75) {
                            System.out.println("Exception: " + e75.toString());
                        }
                    } catch (NoSuchMethodException e76) {
                    } catch (SecurityException e77) {
                        System.out.println("Exception: " + e77.toString());
                    }
                    try {
                        this.getSelectionModelMethod = component.getClass().getMethod("getSelectionModel", this.nullClass);
                        try {
                            Object objInvoke9 = this.getSelectionModelMethod.invoke(component, this.nullArgs);
                            if (objInvoke9 != null && (objInvoke9 instanceof TreeSelectionModel)) {
                                ((TreeSelectionModel) objInvoke9).removePropertyChangeListener(this);
                            }
                        } catch (IllegalAccessException e78) {
                            System.out.println("Exception: " + e78.toString());
                        } catch (InvocationTargetException e79) {
                            System.out.println("Exception: " + e79.toString());
                        }
                        break;
                    } catch (NoSuchMethodException e80) {
                        break;
                    } catch (SecurityException e81) {
                        System.out.println("Exception: " + e81.toString());
                        break;
                    }
                    break;
                case 28:
                    if (component instanceof JComponent) {
                        ((JComponent) component).removeVetoableChangeListener(this);
                        break;
                    }
                    break;
                case 29:
                    try {
                        this.removeInternalFrameMethod = component.getClass().getMethod("removeInternalFrameListener", this.internalFrameListeners);
                        try {
                            this.removeInternalFrameMethod.invoke(component, this.internalFrameArgs);
                        } catch (IllegalAccessException e82) {
                            System.out.println("Exception: " + e82.toString());
                        } catch (InvocationTargetException e83) {
                            System.out.println("Exception: " + e83.toString());
                        }
                        break;
                    } catch (NoSuchMethodException e84) {
                        break;
                    } catch (SecurityException e85) {
                        System.out.println("Exception: " + e85.toString());
                        break;
                    }
            }
            if (component instanceof Container) {
                int componentCount = ((Container) component).getComponentCount();
                for (int i3 = 0; i3 < componentCount; i3++) {
                    removeListeners(((Container) component).getComponent(i3), i2);
                }
            }
        }

        @Override // com.sun.java.accessibility.util.AWTEventMonitor.AWTEventsListener, java.awt.event.ContainerListener
        public void componentAdded(ContainerEvent containerEvent) throws IllegalArgumentException {
            installListeners(containerEvent.getChild());
        }

        @Override // com.sun.java.accessibility.util.AWTEventMonitor.AWTEventsListener, java.awt.event.ContainerListener
        public void componentRemoved(ContainerEvent containerEvent) throws IllegalArgumentException {
            removeListeners(containerEvent.getChild());
        }

        @Override // javax.swing.event.AncestorListener
        public void ancestorAdded(AncestorEvent ancestorEvent) {
            Object[] listenerList = SwingEventMonitor.listenerList.getListenerList();
            for (int length = listenerList.length - 2; length >= 0; length -= 2) {
                if (listenerList[length] == AncestorListener.class) {
                    ((AncestorListener) listenerList[length + 1]).ancestorAdded(ancestorEvent);
                }
            }
        }

        @Override // javax.swing.event.AncestorListener
        public void ancestorRemoved(AncestorEvent ancestorEvent) {
            Object[] listenerList = SwingEventMonitor.listenerList.getListenerList();
            for (int length = listenerList.length - 2; length >= 0; length -= 2) {
                if (listenerList[length] == AncestorListener.class) {
                    ((AncestorListener) listenerList[length + 1]).ancestorRemoved(ancestorEvent);
                }
            }
        }

        @Override // javax.swing.event.AncestorListener
        public void ancestorMoved(AncestorEvent ancestorEvent) {
            Object[] listenerList = SwingEventMonitor.listenerList.getListenerList();
            for (int length = listenerList.length - 2; length >= 0; length -= 2) {
                if (listenerList[length] == AncestorListener.class) {
                    ((AncestorListener) listenerList[length + 1]).ancestorMoved(ancestorEvent);
                }
            }
        }

        @Override // javax.swing.event.CaretListener
        public void caretUpdate(CaretEvent caretEvent) {
            Object[] listenerList = SwingEventMonitor.listenerList.getListenerList();
            for (int length = listenerList.length - 2; length >= 0; length -= 2) {
                if (listenerList[length] == CaretListener.class) {
                    ((CaretListener) listenerList[length + 1]).caretUpdate(caretEvent);
                }
            }
        }

        @Override // javax.swing.event.CellEditorListener
        public void editingStopped(ChangeEvent changeEvent) {
            Object[] listenerList = SwingEventMonitor.listenerList.getListenerList();
            for (int length = listenerList.length - 2; length >= 0; length -= 2) {
                if (listenerList[length] == CellEditorListener.class) {
                    ((CellEditorListener) listenerList[length + 1]).editingStopped(changeEvent);
                }
            }
        }

        @Override // javax.swing.event.CellEditorListener
        public void editingCanceled(ChangeEvent changeEvent) {
            Object[] listenerList = SwingEventMonitor.listenerList.getListenerList();
            for (int length = listenerList.length - 2; length >= 0; length -= 2) {
                if (listenerList[length] == CellEditorListener.class) {
                    ((CellEditorListener) listenerList[length + 1]).editingCanceled(changeEvent);
                }
            }
        }

        @Override // com.sun.java.accessibility.util.AWTEventMonitor.AWTEventsListener, javax.swing.event.ChangeListener
        public void stateChanged(ChangeEvent changeEvent) {
            Object[] listenerList = SwingEventMonitor.listenerList.getListenerList();
            for (int length = listenerList.length - 2; length >= 0; length -= 2) {
                if (listenerList[length] == ChangeListener.class) {
                    ((ChangeListener) listenerList[length + 1]).stateChanged(changeEvent);
                }
            }
        }

        @Override // javax.swing.event.TableColumnModelListener
        public void columnAdded(TableColumnModelEvent tableColumnModelEvent) {
            Object[] listenerList = SwingEventMonitor.listenerList.getListenerList();
            for (int length = listenerList.length - 2; length >= 0; length -= 2) {
                if (listenerList[length] == TableColumnModelListener.class) {
                    ((TableColumnModelListener) listenerList[length + 1]).columnAdded(tableColumnModelEvent);
                }
            }
        }

        @Override // javax.swing.event.TableColumnModelListener
        public void columnMarginChanged(ChangeEvent changeEvent) {
            Object[] listenerList = SwingEventMonitor.listenerList.getListenerList();
            for (int length = listenerList.length - 2; length >= 0; length -= 2) {
                if (listenerList[length] == TableColumnModelListener.class) {
                    ((TableColumnModelListener) listenerList[length + 1]).columnMarginChanged(changeEvent);
                }
            }
        }

        @Override // javax.swing.event.TableColumnModelListener
        public void columnMoved(TableColumnModelEvent tableColumnModelEvent) {
            Object[] listenerList = SwingEventMonitor.listenerList.getListenerList();
            for (int length = listenerList.length - 2; length >= 0; length -= 2) {
                if (listenerList[length] == TableColumnModelListener.class) {
                    ((TableColumnModelListener) listenerList[length + 1]).columnMoved(tableColumnModelEvent);
                }
            }
        }

        @Override // javax.swing.event.TableColumnModelListener
        public void columnRemoved(TableColumnModelEvent tableColumnModelEvent) {
            Object[] listenerList = SwingEventMonitor.listenerList.getListenerList();
            for (int length = listenerList.length - 2; length >= 0; length -= 2) {
                if (listenerList[length] == TableColumnModelListener.class) {
                    ((TableColumnModelListener) listenerList[length + 1]).columnRemoved(tableColumnModelEvent);
                }
            }
        }

        @Override // javax.swing.event.TableColumnModelListener
        public void columnSelectionChanged(ListSelectionEvent listSelectionEvent) {
            Object[] listenerList = SwingEventMonitor.listenerList.getListenerList();
            for (int length = listenerList.length - 2; length >= 0; length -= 2) {
                if (listenerList[length] == TableColumnModelListener.class) {
                    ((TableColumnModelListener) listenerList[length + 1]).columnSelectionChanged(listSelectionEvent);
                }
            }
        }

        @Override // javax.swing.event.DocumentListener
        public void changedUpdate(DocumentEvent documentEvent) {
            Object[] listenerList = SwingEventMonitor.listenerList.getListenerList();
            for (int length = listenerList.length - 2; length >= 0; length -= 2) {
                if (listenerList[length] == DocumentListener.class) {
                    ((DocumentListener) listenerList[length + 1]).changedUpdate(documentEvent);
                }
            }
        }

        @Override // javax.swing.event.DocumentListener
        public void insertUpdate(DocumentEvent documentEvent) {
            Object[] listenerList = SwingEventMonitor.listenerList.getListenerList();
            for (int length = listenerList.length - 2; length >= 0; length -= 2) {
                if (listenerList[length] == DocumentListener.class) {
                    ((DocumentListener) listenerList[length + 1]).insertUpdate(documentEvent);
                }
            }
        }

        @Override // javax.swing.event.DocumentListener
        public void removeUpdate(DocumentEvent documentEvent) {
            Object[] listenerList = SwingEventMonitor.listenerList.getListenerList();
            for (int length = listenerList.length - 2; length >= 0; length -= 2) {
                if (listenerList[length] == DocumentListener.class) {
                    ((DocumentListener) listenerList[length + 1]).removeUpdate(documentEvent);
                }
            }
        }

        @Override // javax.swing.event.ListDataListener
        public void contentsChanged(ListDataEvent listDataEvent) {
            Object[] listenerList = SwingEventMonitor.listenerList.getListenerList();
            for (int length = listenerList.length - 2; length >= 0; length -= 2) {
                if (listenerList[length] == ListDataListener.class) {
                    ((ListDataListener) listenerList[length + 1]).contentsChanged(listDataEvent);
                }
            }
        }

        @Override // javax.swing.event.ListDataListener
        public void intervalAdded(ListDataEvent listDataEvent) {
            Object[] listenerList = SwingEventMonitor.listenerList.getListenerList();
            for (int length = listenerList.length - 2; length >= 0; length -= 2) {
                if (listenerList[length] == ListDataListener.class) {
                    ((ListDataListener) listenerList[length + 1]).intervalAdded(listDataEvent);
                }
            }
        }

        @Override // javax.swing.event.ListDataListener
        public void intervalRemoved(ListDataEvent listDataEvent) {
            Object[] listenerList = SwingEventMonitor.listenerList.getListenerList();
            for (int length = listenerList.length - 2; length >= 0; length -= 2) {
                if (listenerList[length] == ListDataListener.class) {
                    ((ListDataListener) listenerList[length + 1]).intervalRemoved(listDataEvent);
                }
            }
        }

        @Override // javax.swing.event.ListSelectionListener
        public void valueChanged(ListSelectionEvent listSelectionEvent) {
            Object[] listenerList = SwingEventMonitor.listenerList.getListenerList();
            for (int length = listenerList.length - 2; length >= 0; length -= 2) {
                if (listenerList[length] == ListSelectionListener.class) {
                    ((ListSelectionListener) listenerList[length + 1]).valueChanged(listSelectionEvent);
                }
            }
        }

        @Override // javax.swing.event.MenuListener
        public void menuCanceled(MenuEvent menuEvent) {
            Object[] listenerList = SwingEventMonitor.listenerList.getListenerList();
            for (int length = listenerList.length - 2; length >= 0; length -= 2) {
                if (listenerList[length] == MenuListener.class) {
                    ((MenuListener) listenerList[length + 1]).menuCanceled(menuEvent);
                }
            }
        }

        @Override // javax.swing.event.MenuListener
        public void menuDeselected(MenuEvent menuEvent) {
            Object[] listenerList = SwingEventMonitor.listenerList.getListenerList();
            for (int length = listenerList.length - 2; length >= 0; length -= 2) {
                if (listenerList[length] == MenuListener.class) {
                    ((MenuListener) listenerList[length + 1]).menuDeselected(menuEvent);
                }
            }
        }

        @Override // javax.swing.event.MenuListener
        public void menuSelected(MenuEvent menuEvent) {
            Object[] listenerList = SwingEventMonitor.listenerList.getListenerList();
            for (int length = listenerList.length - 2; length >= 0; length -= 2) {
                if (listenerList[length] == MenuListener.class) {
                    ((MenuListener) listenerList[length + 1]).menuSelected(menuEvent);
                }
            }
        }

        @Override // javax.swing.event.PopupMenuListener
        public void popupMenuWillBecomeVisible(PopupMenuEvent popupMenuEvent) {
            Object[] listenerList = SwingEventMonitor.listenerList.getListenerList();
            for (int length = listenerList.length - 2; length >= 0; length -= 2) {
                if (listenerList[length] == PopupMenuListener.class) {
                    ((PopupMenuListener) listenerList[length + 1]).popupMenuWillBecomeVisible(popupMenuEvent);
                }
            }
        }

        @Override // javax.swing.event.PopupMenuListener
        public void popupMenuWillBecomeInvisible(PopupMenuEvent popupMenuEvent) {
            Object[] listenerList = SwingEventMonitor.listenerList.getListenerList();
            for (int length = listenerList.length - 2; length >= 0; length -= 2) {
                if (listenerList[length] == PopupMenuListener.class) {
                    ((PopupMenuListener) listenerList[length + 1]).popupMenuWillBecomeInvisible(popupMenuEvent);
                }
            }
        }

        @Override // javax.swing.event.PopupMenuListener
        public void popupMenuCanceled(PopupMenuEvent popupMenuEvent) {
            Object[] listenerList = SwingEventMonitor.listenerList.getListenerList();
            for (int length = listenerList.length - 2; length >= 0; length -= 2) {
                if (listenerList[length] == PopupMenuListener.class) {
                    ((PopupMenuListener) listenerList[length + 1]).popupMenuCanceled(popupMenuEvent);
                }
            }
        }

        @Override // javax.swing.event.TableModelListener
        public void tableChanged(TableModelEvent tableModelEvent) {
            Object[] listenerList = SwingEventMonitor.listenerList.getListenerList();
            for (int length = listenerList.length - 2; length >= 0; length -= 2) {
                if (listenerList[length] == TableModelListener.class) {
                    ((TableModelListener) listenerList[length + 1]).tableChanged(tableModelEvent);
                }
            }
        }

        @Override // javax.swing.event.TreeExpansionListener
        public void treeCollapsed(TreeExpansionEvent treeExpansionEvent) {
            Object[] listenerList = SwingEventMonitor.listenerList.getListenerList();
            for (int length = listenerList.length - 2; length >= 0; length -= 2) {
                if (listenerList[length] == TreeExpansionListener.class) {
                    ((TreeExpansionListener) listenerList[length + 1]).treeCollapsed(treeExpansionEvent);
                }
            }
        }

        @Override // javax.swing.event.TreeExpansionListener
        public void treeExpanded(TreeExpansionEvent treeExpansionEvent) {
            Object[] listenerList = SwingEventMonitor.listenerList.getListenerList();
            for (int length = listenerList.length - 2; length >= 0; length -= 2) {
                if (listenerList[length] == TreeExpansionListener.class) {
                    ((TreeExpansionListener) listenerList[length + 1]).treeExpanded(treeExpansionEvent);
                }
            }
        }

        @Override // javax.swing.event.TreeModelListener
        public void treeNodesChanged(TreeModelEvent treeModelEvent) {
            Object[] listenerList = SwingEventMonitor.listenerList.getListenerList();
            for (int length = listenerList.length - 2; length >= 0; length -= 2) {
                if (listenerList[length] == TreeModelListener.class) {
                    ((TreeModelListener) listenerList[length + 1]).treeNodesChanged(treeModelEvent);
                }
            }
        }

        @Override // javax.swing.event.TreeModelListener
        public void treeNodesInserted(TreeModelEvent treeModelEvent) {
            Object[] listenerList = SwingEventMonitor.listenerList.getListenerList();
            for (int length = listenerList.length - 2; length >= 0; length -= 2) {
                if (listenerList[length] == TreeModelListener.class) {
                    ((TreeModelListener) listenerList[length + 1]).treeNodesInserted(treeModelEvent);
                }
            }
        }

        @Override // javax.swing.event.TreeModelListener
        public void treeNodesRemoved(TreeModelEvent treeModelEvent) {
            Object[] listenerList = SwingEventMonitor.listenerList.getListenerList();
            for (int length = listenerList.length - 2; length >= 0; length -= 2) {
                if (listenerList[length] == TreeModelListener.class) {
                    ((TreeModelListener) listenerList[length + 1]).treeNodesRemoved(treeModelEvent);
                }
            }
        }

        @Override // javax.swing.event.TreeModelListener
        public void treeStructureChanged(TreeModelEvent treeModelEvent) {
            Object[] listenerList = SwingEventMonitor.listenerList.getListenerList();
            for (int length = listenerList.length - 2; length >= 0; length -= 2) {
                if (listenerList[length] == TreeModelListener.class) {
                    ((TreeModelListener) listenerList[length + 1]).treeStructureChanged(treeModelEvent);
                }
            }
        }

        @Override // javax.swing.event.TreeSelectionListener
        public void valueChanged(TreeSelectionEvent treeSelectionEvent) {
            Object[] listenerList = SwingEventMonitor.listenerList.getListenerList();
            for (int length = listenerList.length - 2; length >= 0; length -= 2) {
                if (listenerList[length] == TreeSelectionListener.class) {
                    ((TreeSelectionListener) listenerList[length + 1]).valueChanged(treeSelectionEvent);
                }
            }
        }

        @Override // javax.swing.event.UndoableEditListener
        public void undoableEditHappened(UndoableEditEvent undoableEditEvent) {
            Object[] listenerList = SwingEventMonitor.listenerList.getListenerList();
            for (int length = listenerList.length - 2; length >= 0; length -= 2) {
                if (listenerList[length] == UndoableEditListener.class) {
                    ((UndoableEditListener) listenerList[length + 1]).undoableEditHappened(undoableEditEvent);
                }
            }
        }

        @Override // javax.swing.event.InternalFrameListener
        public void internalFrameOpened(InternalFrameEvent internalFrameEvent) {
            Object[] listenerList = SwingEventMonitor.listenerList.getListenerList();
            for (int length = listenerList.length - 2; length >= 0; length -= 2) {
                if (listenerList[length] == InternalFrameListener.class) {
                    ((InternalFrameListener) listenerList[length + 1]).internalFrameOpened(internalFrameEvent);
                }
            }
        }

        @Override // javax.swing.event.InternalFrameListener
        public void internalFrameActivated(InternalFrameEvent internalFrameEvent) {
            Object[] listenerList = SwingEventMonitor.listenerList.getListenerList();
            for (int length = listenerList.length - 2; length >= 0; length -= 2) {
                if (listenerList[length] == InternalFrameListener.class) {
                    ((InternalFrameListener) listenerList[length + 1]).internalFrameActivated(internalFrameEvent);
                }
            }
        }

        @Override // javax.swing.event.InternalFrameListener
        public void internalFrameDeactivated(InternalFrameEvent internalFrameEvent) {
            Object[] listenerList = SwingEventMonitor.listenerList.getListenerList();
            for (int length = listenerList.length - 2; length >= 0; length -= 2) {
                if (listenerList[length] == InternalFrameListener.class) {
                    ((InternalFrameListener) listenerList[length + 1]).internalFrameDeactivated(internalFrameEvent);
                }
            }
        }

        @Override // javax.swing.event.InternalFrameListener
        public void internalFrameIconified(InternalFrameEvent internalFrameEvent) {
            Object[] listenerList = SwingEventMonitor.listenerList.getListenerList();
            for (int length = listenerList.length - 2; length >= 0; length -= 2) {
                if (listenerList[length] == InternalFrameListener.class) {
                    ((InternalFrameListener) listenerList[length + 1]).internalFrameIconified(internalFrameEvent);
                }
            }
        }

        @Override // javax.swing.event.InternalFrameListener
        public void internalFrameDeiconified(InternalFrameEvent internalFrameEvent) {
            Object[] listenerList = SwingEventMonitor.listenerList.getListenerList();
            for (int length = listenerList.length - 2; length >= 0; length -= 2) {
                if (listenerList[length] == InternalFrameListener.class) {
                    ((InternalFrameListener) listenerList[length + 1]).internalFrameDeiconified(internalFrameEvent);
                }
            }
        }

        @Override // javax.swing.event.InternalFrameListener
        public void internalFrameClosing(InternalFrameEvent internalFrameEvent) {
            Object[] listenerList = SwingEventMonitor.listenerList.getListenerList();
            for (int length = listenerList.length - 2; length >= 0; length -= 2) {
                if (listenerList[length] == InternalFrameListener.class) {
                    ((InternalFrameListener) listenerList[length + 1]).internalFrameClosing(internalFrameEvent);
                }
            }
        }

        @Override // javax.swing.event.InternalFrameListener
        public void internalFrameClosed(InternalFrameEvent internalFrameEvent) {
            Object[] listenerList = SwingEventMonitor.listenerList.getListenerList();
            for (int length = listenerList.length - 2; length >= 0; length -= 2) {
                if (listenerList[length] == InternalFrameListener.class) {
                    ((InternalFrameListener) listenerList[length + 1]).internalFrameClosed(internalFrameEvent);
                }
            }
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) throws IllegalArgumentException {
            Document document;
            Object[] listenerList = SwingEventMonitor.listenerList.getListenerList();
            for (int length = listenerList.length - 2; length >= 0; length -= 2) {
                if (listenerList[length] == PropertyChangeListener.class) {
                    ((PropertyChangeListener) listenerList[length + 1]).propertyChange(propertyChangeEvent);
                }
            }
            if (!(propertyChangeEvent.getSource() instanceof JTextComponent) || (document = ((JTextComponent) propertyChangeEvent.getSource()).getDocument()) == null) {
                return;
            }
            try {
                this.removeDocumentMethod = document.getClass().getMethod("removeDocumentListener", this.documentListeners);
                this.addDocumentMethod = document.getClass().getMethod("addDocumentListener", this.documentListeners);
                try {
                    this.removeDocumentMethod.invoke(document, this.documentArgs);
                    this.addDocumentMethod.invoke(document, this.documentArgs);
                } catch (IllegalAccessException e2) {
                    System.out.println("Exception: " + e2.toString());
                } catch (InvocationTargetException e3) {
                    System.out.println("Exception: " + e3.toString());
                }
            } catch (NoSuchMethodException e4) {
            } catch (SecurityException e5) {
                System.out.println("Exception: " + e5.toString());
            }
        }

        @Override // java.beans.VetoableChangeListener
        public void vetoableChange(PropertyChangeEvent propertyChangeEvent) throws PropertyVetoException {
            Object[] listenerList = SwingEventMonitor.listenerList.getListenerList();
            for (int length = listenerList.length - 2; length >= 0; length -= 2) {
                if (listenerList[length] == VetoableChangeListener.class) {
                    ((VetoableChangeListener) listenerList[length + 1]).vetoableChange(propertyChangeEvent);
                }
            }
        }
    }
}
