package com.sun.glass.ui.win;

import com.sun.glass.ui.Accessible;
import com.sun.glass.ui.View;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;

/* loaded from: jfxrt.jar:com/sun/glass/ui/win/WinAccessible.class */
final class WinAccessible extends Accessible {
    private static int idCount;
    private static final int UIA_BoundingRectanglePropertyId = 30001;
    private static final int UIA_ProcessIdPropertyId = 30002;
    private static final int UIA_ControlTypePropertyId = 30003;
    private static final int UIA_LocalizedControlTypePropertyId = 30004;
    private static final int UIA_NamePropertyId = 30005;
    private static final int UIA_AcceleratorKeyPropertyId = 30006;
    private static final int UIA_AccessKeyPropertyId = 30007;
    private static final int UIA_HasKeyboardFocusPropertyId = 30008;
    private static final int UIA_IsKeyboardFocusablePropertyId = 30009;
    private static final int UIA_IsEnabledPropertyId = 30010;
    private static final int UIA_AutomationIdPropertyId = 30011;
    private static final int UIA_ClassNamePropertyId = 30012;
    private static final int UIA_HelpTextPropertyId = 30013;
    private static final int UIA_ClickablePointPropertyId = 30014;
    private static final int UIA_CulturePropertyId = 30015;
    private static final int UIA_IsControlElementPropertyId = 30016;
    private static final int UIA_IsContentElementPropertyId = 30017;
    private static final int UIA_LabeledByPropertyId = 30018;
    private static final int UIA_IsPasswordPropertyId = 30019;
    private static final int UIA_NativeWindowHandlePropertyId = 30020;
    private static final int UIA_ItemTypePropertyId = 30021;
    private static final int UIA_IsOffscreenPropertyId = 30022;
    private static final int UIA_OrientationPropertyId = 30023;
    private static final int UIA_FrameworkIdPropertyId = 30024;
    private static final int UIA_ValueValuePropertyId = 30045;
    private static final int UIA_RangeValueValuePropertyId = 30047;
    private static final int UIA_ExpandCollapseExpandCollapseStatePropertyId = 30070;
    private static final int UIA_ToggleToggleStatePropertyId = 30086;
    private static final int UIA_AriaRolePropertyId = 30101;
    private static final int UIA_ProviderDescriptionPropertyId = 30107;
    private static final int UIA_PositionInSetPropertyId = 30152;
    private static final int UIA_SizeOfSetPropertyId = 30153;
    private static final int UIA_IsDialogPropertyId = 30174;
    private static final int UIA_InvokePatternId = 10000;
    private static final int UIA_SelectionPatternId = 10001;
    private static final int UIA_ValuePatternId = 10002;
    private static final int UIA_RangeValuePatternId = 10003;
    private static final int UIA_ScrollPatternId = 10004;
    private static final int UIA_ExpandCollapsePatternId = 10005;
    private static final int UIA_GridPatternId = 10006;
    private static final int UIA_GridItemPatternId = 10007;
    private static final int UIA_SelectionItemPatternId = 10010;
    private static final int UIA_TablePatternId = 10012;
    private static final int UIA_TableItemPatternId = 10013;
    private static final int UIA_TextPatternId = 10014;
    private static final int UIA_TogglePatternId = 10015;
    private static final int UIA_TransformPatternId = 10016;
    private static final int UIA_ScrollItemPatternId = 10017;
    private static final int UIA_ItemContainerPatternId = 10019;
    private static final int UIA_ButtonControlTypeId = 50000;
    private static final int UIA_CheckBoxControlTypeId = 50002;
    private static final int UIA_ComboBoxControlTypeId = 50003;
    private static final int UIA_EditControlTypeId = 50004;
    private static final int UIA_HyperlinkControlTypeId = 50005;
    private static final int UIA_ImageControlTypeId = 50006;
    private static final int UIA_ListItemControlTypeId = 50007;
    private static final int UIA_ListControlTypeId = 50008;
    private static final int UIA_MenuControlTypeId = 50009;
    private static final int UIA_MenuBarControlTypeId = 50010;
    private static final int UIA_MenuItemControlTypeId = 50011;
    private static final int UIA_ProgressBarControlTypeId = 50012;
    private static final int UIA_RadioButtonControlTypeId = 50013;
    private static final int UIA_ScrollBarControlTypeId = 50014;
    private static final int UIA_SliderControlTypeId = 50015;
    private static final int UIA_SpinnerControlTypeId = 50016;
    private static final int UIA_TabControlTypeId = 50018;
    private static final int UIA_TabItemControlTypeId = 50019;
    private static final int UIA_TextControlTypeId = 50020;
    private static final int UIA_ToolBarControlTypeId = 50021;
    private static final int UIA_TreeControlTypeId = 50023;
    private static final int UIA_TreeItemControlTypeId = 50024;
    private static final int UIA_GroupControlTypeId = 50026;
    private static final int UIA_ThumbControlTypeId = 50027;
    private static final int UIA_DataGridControlTypeId = 50028;
    private static final int UIA_DataItemControlTypeId = 50029;
    private static final int UIA_SplitButtonControlTypeId = 50031;
    private static final int UIA_WindowControlTypeId = 50032;
    private static final int UIA_PaneControlTypeId = 50033;
    private static final int UIA_TableControlTypeId = 50036;
    private static final int NavigateDirection_Parent = 0;
    private static final int NavigateDirection_NextSibling = 1;
    private static final int NavigateDirection_PreviousSibling = 2;
    private static final int NavigateDirection_FirstChild = 3;
    private static final int NavigateDirection_LastChild = 4;
    private static final int RowOrColumnMajor_RowMajor = 0;
    private static final int RowOrColumnMajor_ColumnMajor = 1;
    private static final int RowOrColumnMajor_Indeterminate = 2;
    private static final int UIA_MenuOpenedEventId = 20003;
    private static final int UIA_AutomationPropertyChangedEventId = 20004;
    private static final int UIA_AutomationFocusChangedEventId = 20005;
    private static final int UIA_MenuClosedEventId = 20007;
    private static final int UIA_SelectionItem_ElementRemovedFromSelectionEventId = 20011;
    private static final int UIA_SelectionItem_ElementSelectedEventId = 20012;
    private static final int UIA_Text_TextSelectionChangedEventId = 20014;
    private static final int UIA_Text_TextChangedEventId = 20015;
    private static final int UIA_MenuModeStartEventId = 20018;
    private static final int UIA_MenuModeEndEventId = 20019;
    private static final int SupportedTextSelection_None = 0;
    private static final int SupportedTextSelection_Single = 1;
    private static final int SupportedTextSelection_Multiple = 2;
    private static final int ExpandCollapseState_Collapsed = 0;
    private static final int ExpandCollapseState_Expanded = 1;
    private static final int ExpandCollapseState_PartiallyExpanded = 2;
    private static final int ExpandCollapseState_LeafNode = 3;
    private static final int ScrollAmount_LargeDecrement = 0;
    private static final int ScrollAmount_SmallDecrement = 1;
    private static final int ScrollAmount_NoAmount = 2;
    private static final int ScrollAmount_LargeIncrement = 3;
    private static final int ScrollAmount_SmallIncrement = 4;
    private static final int UIA_ScrollPatternNoScroll = -1;
    private static final int ToggleState_Off = 0;
    private static final int ToggleState_On = 1;
    private static final int ToggleState_Indeterminate = 2;
    private static final int UiaAppendRuntimeId = 3;
    private int id;
    private WinTextRangeProvider documentRange;
    private WinTextRangeProvider selectionRange;
    private int lastIndex = 0;
    private long peer = _createGlassAccessible();

    private static native void _initIDs();

    private native long _createGlassAccessible();

    private native void _destroyGlassAccessible(long j2);

    private static native long UiaRaiseAutomationEvent(long j2, int i2);

    private static native long UiaRaiseAutomationPropertyChangedEvent(long j2, int i2, WinVariant winVariant, WinVariant winVariant2);

    private static native boolean UiaClientsAreListening();

    static {
        _initIDs();
        idCount = 1;
    }

    WinAccessible() {
        if (this.peer == 0) {
            throw new RuntimeException("could not create platform accessible");
        }
        int i2 = idCount;
        idCount = i2 + 1;
        this.id = i2;
    }

    @Override // com.sun.glass.ui.Accessible
    public void dispose() {
        super.dispose();
        if (this.selectionRange != null) {
            this.selectionRange.dispose();
            this.selectionRange = null;
        }
        if (this.documentRange != null) {
            this.documentRange.dispose();
            this.documentRange = null;
        }
        if (this.peer != 0) {
            _destroyGlassAccessible(this.peer);
            this.peer = 0L;
        }
    }

    @Override // com.sun.glass.ui.Accessible
    public void sendNotification(AccessibleAttribute notification) {
        Node node;
        if (isDisposed()) {
        }
        switch (notification) {
            case FOCUS_NODE:
                if (getView() != null) {
                    long focus = GetFocus();
                    if (focus != 0) {
                        UiaRaiseAutomationEvent(focus, UIA_AutomationFocusChangedEventId);
                        break;
                    }
                } else {
                    Node node2 = (Node) getAttribute(AccessibleAttribute.FOCUS_NODE, new Object[0]);
                    if (node2 != null) {
                        UiaRaiseAutomationEvent(getNativeAccessible(node2), UIA_AutomationFocusChangedEventId);
                        break;
                    } else {
                        Scene scene = (Scene) getAttribute(AccessibleAttribute.SCENE, new Object[0]);
                        Accessible acc = getAccessible(scene);
                        if (acc != null) {
                            acc.sendNotification(AccessibleAttribute.FOCUS_NODE);
                            break;
                        }
                    }
                }
                break;
            case FOCUS_ITEM:
                long id = getNativeAccessible((Node) getAttribute(AccessibleAttribute.FOCUS_ITEM, new Object[0]));
                if (id != 0) {
                    UiaRaiseAutomationEvent(id, UIA_AutomationFocusChangedEventId);
                    break;
                }
                break;
            case INDETERMINATE:
                Object role = getAttribute(AccessibleAttribute.ROLE, new Object[0]);
                if (role == AccessibleRole.CHECK_BOX || role == AccessibleRole.CHECK_BOX_TREE_ITEM) {
                    notifyToggleState();
                    break;
                }
                break;
            case SELECTED:
                Object role2 = getAttribute(AccessibleAttribute.ROLE, new Object[0]);
                if (role2 == AccessibleRole.CHECK_BOX || role2 == AccessibleRole.TOGGLE_BUTTON || role2 == AccessibleRole.CHECK_BOX_TREE_ITEM) {
                    notifyToggleState();
                    break;
                } else {
                    Boolean selected = (Boolean) getAttribute(AccessibleAttribute.SELECTED, new Object[0]);
                    if (selected != null) {
                        if (selected.booleanValue()) {
                            UiaRaiseAutomationEvent(this.peer, UIA_SelectionItem_ElementSelectedEventId);
                            break;
                        } else {
                            UiaRaiseAutomationEvent(this.peer, UIA_SelectionItem_ElementRemovedFromSelectionEventId);
                            break;
                        }
                    }
                }
                break;
            case FOCUSED:
            case PARENT:
                break;
            case VALUE:
                Double value = (Double) getAttribute(AccessibleAttribute.VALUE, new Object[0]);
                if (value != null) {
                    WinVariant vo = new WinVariant();
                    vo.vt = (short) 5;
                    vo.dblVal = 0.0d;
                    WinVariant vn = new WinVariant();
                    vn.vt = (short) 5;
                    vn.dblVal = value.doubleValue();
                    UiaRaiseAutomationPropertyChangedEvent(this.peer, UIA_RangeValueValuePropertyId, vo, vn);
                    break;
                }
                break;
            case SELECTION_START:
            case SELECTION_END:
                if (this.selectionRange != null) {
                    Integer start = (Integer) getAttribute(AccessibleAttribute.SELECTION_START, new Object[0]);
                    boolean newStart = (start == null || start.intValue() == this.selectionRange.getStart()) ? false : true;
                    Integer end = (Integer) getAttribute(AccessibleAttribute.SELECTION_END, new Object[0]);
                    boolean newEnd = (end == null || end.intValue() == this.selectionRange.getEnd()) ? false : true;
                    if (newStart || newEnd) {
                        UiaRaiseAutomationEvent(this.peer, UIA_Text_TextSelectionChangedEventId);
                        break;
                    }
                }
                break;
            case TEXT:
                String value2 = (String) getAttribute(AccessibleAttribute.TEXT, new Object[0]);
                if (value2 != null) {
                    WinVariant vo2 = new WinVariant();
                    vo2.vt = (short) 8;
                    vo2.bstrVal = "";
                    WinVariant vn2 = new WinVariant();
                    vn2.vt = (short) 8;
                    vn2.bstrVal = value2;
                    if (getAttribute(AccessibleAttribute.ROLE, new Object[0]) == AccessibleRole.SPINNER) {
                        UiaRaiseAutomationPropertyChangedEvent(this.peer, UIA_NamePropertyId, vo2, vn2);
                    } else {
                        UiaRaiseAutomationPropertyChangedEvent(this.peer, UIA_ValueValuePropertyId, vo2, vn2);
                    }
                }
                if (this.selectionRange != null || this.documentRange != null) {
                    UiaRaiseAutomationEvent(this.peer, UIA_Text_TextChangedEventId);
                    break;
                }
                break;
            case EXPANDED:
                Boolean expanded = (Boolean) getAttribute(AccessibleAttribute.EXPANDED, new Object[0]);
                if (expanded != null) {
                    WinVariant vo3 = new WinVariant();
                    vo3.vt = (short) 3;
                    vo3.lVal = expanded.booleanValue() ? 0 : 1;
                    WinVariant vn3 = new WinVariant();
                    vn3.vt = (short) 3;
                    vn3.lVal = expanded.booleanValue() ? 1 : 0;
                    if (getAttribute(AccessibleAttribute.ROLE, new Object[0]) == AccessibleRole.TREE_TABLE_ROW) {
                        Accessible treeTableView = getContainer();
                        Integer rowIndex = (Integer) getAttribute(AccessibleAttribute.INDEX, new Object[0]);
                        if (treeTableView != null && rowIndex != null && (node = (Node) treeTableView.getAttribute(AccessibleAttribute.CELL_AT_ROW_COLUMN, rowIndex, 0)) != null) {
                            long ptr = ((WinAccessible) getAccessible(node)).getNativeAccessible();
                            UiaRaiseAutomationPropertyChangedEvent(ptr, UIA_ExpandCollapseExpandCollapseStatePropertyId, vo3, vn3);
                            break;
                        }
                    } else {
                        UiaRaiseAutomationPropertyChangedEvent(this.peer, UIA_ExpandCollapseExpandCollapseStatePropertyId, vo3, vn3);
                        break;
                    }
                }
                break;
            default:
                UiaRaiseAutomationEvent(this.peer, UIA_AutomationPropertyChangedEventId);
                break;
        }
    }

    private void notifyToggleState() {
        int state = get_ToggleState();
        WinVariant vo = new WinVariant();
        vo.vt = (short) 3;
        vo.lVal = state;
        WinVariant vn = new WinVariant();
        vn.vt = (short) 3;
        vn.lVal = state;
        UiaRaiseAutomationPropertyChangedEvent(this.peer, UIA_ToggleToggleStatePropertyId, vo, vn);
    }

    @Override // com.sun.glass.ui.Accessible
    protected long getNativeAccessible() {
        return this.peer;
    }

    private Accessible getContainer() {
        AccessibleRole role;
        if (!isDisposed() && (role = (AccessibleRole) getAttribute(AccessibleAttribute.ROLE, new Object[0])) != null) {
            switch (role) {
                case TABLE_ROW:
                case TABLE_CELL:
                    return getContainerAccessible(AccessibleRole.TABLE_VIEW);
                case LIST_ITEM:
                    return getContainerAccessible(AccessibleRole.LIST_VIEW);
                case TAB_ITEM:
                    return getContainerAccessible(AccessibleRole.TAB_PANE);
                case PAGE_ITEM:
                    return getContainerAccessible(AccessibleRole.PAGINATION);
                case CHECK_BOX_TREE_ITEM:
                case TREE_ITEM:
                    return getContainerAccessible(AccessibleRole.TREE_VIEW);
                case TREE_TABLE_ROW:
                case TREE_TABLE_CELL:
                    return getContainerAccessible(AccessibleRole.TREE_TABLE_VIEW);
                default:
                    return null;
            }
        }
        return null;
    }

    private int getControlType() {
        AccessibleRole role = (AccessibleRole) getAttribute(AccessibleAttribute.ROLE, new Object[0]);
        if (role == null) {
            return UIA_GroupControlTypeId;
        }
        switch (role) {
            case TABLE_CELL:
            case TREE_TABLE_CELL:
                return UIA_DataItemControlTypeId;
            case LIST_ITEM:
                return UIA_ListItemControlTypeId;
            case TAB_ITEM:
            case PAGE_ITEM:
                return UIA_TabItemControlTypeId;
            case CHECK_BOX_TREE_ITEM:
            case TREE_ITEM:
                return UIA_TreeItemControlTypeId;
            case TREE_TABLE_ROW:
            default:
                return 0;
            case CONTEXT_MENU:
                return UIA_MenuControlTypeId;
            case RADIO_MENU_ITEM:
            case CHECK_MENU_ITEM:
            case MENU:
            case MENU_ITEM:
                return UIA_MenuItemControlTypeId;
            case BUTTON:
            case MENU_BUTTON:
            case TOGGLE_BUTTON:
            case INCREMENT_BUTTON:
            case DECREMENT_BUTTON:
                return 50000;
            case SPLIT_MENU_BUTTON:
                return UIA_SplitButtonControlTypeId;
            case PAGINATION:
            case TAB_PANE:
                return UIA_TabControlTypeId;
            case SLIDER:
                return UIA_SliderControlTypeId;
            case PARENT:
                return getView() != null ? UIA_WindowControlTypeId : UIA_PaneControlTypeId;
            case TEXT:
                return UIA_TextControlTypeId;
            case TEXT_FIELD:
            case PASSWORD_FIELD:
            case TEXT_AREA:
                return UIA_EditControlTypeId;
            case TREE_TABLE_VIEW:
            case TABLE_VIEW:
                return UIA_TableControlTypeId;
            case LIST_VIEW:
                return UIA_ListControlTypeId;
            case IMAGE_VIEW:
                return UIA_ImageControlTypeId;
            case RADIO_BUTTON:
                return UIA_RadioButtonControlTypeId;
            case CHECK_BOX:
                return UIA_CheckBoxControlTypeId;
            case COMBO_BOX:
                return UIA_ComboBoxControlTypeId;
            case HYPERLINK:
                return UIA_HyperlinkControlTypeId;
            case TREE_VIEW:
                return UIA_TreeControlTypeId;
            case PROGRESS_INDICATOR:
                return UIA_ProgressBarControlTypeId;
            case TOOL_BAR:
                return UIA_ToolBarControlTypeId;
            case TITLED_PANE:
                return UIA_GroupControlTypeId;
            case SCROLL_PANE:
                return UIA_PaneControlTypeId;
            case SCROLL_BAR:
                return UIA_ScrollBarControlTypeId;
            case THUMB:
                return UIA_ThumbControlTypeId;
            case MENU_BAR:
                return UIA_MenuBarControlTypeId;
            case DATE_PICKER:
                return UIA_PaneControlTypeId;
            case SPINNER:
                return UIA_SpinnerControlTypeId;
        }
    }

    private List<Node> getUnignoredChildren(WinAccessible acc) {
        ObservableList<Node> children;
        if (acc != null && (children = (ObservableList) acc.getAttribute(AccessibleAttribute.CHILDREN, new Object[0])) != null) {
            return (List) children.stream().filter((v0) -> {
                return v0.isVisible();
            }).collect(Collectors.toList());
        }
        return FXCollections.emptyObservableList();
    }

    private Accessible getRow() {
        Integer rowIndex;
        Accessible treeTableView;
        Integer columnIndex = (Integer) getAttribute(AccessibleAttribute.COLUMN_INDEX, new Object[0]);
        if (columnIndex == null || columnIndex.intValue() != 0 || (rowIndex = (Integer) getAttribute(AccessibleAttribute.ROW_INDEX, new Object[0])) == null || (treeTableView = getContainer()) == null) {
            return null;
        }
        Node treeTableRow = (Node) treeTableView.getAttribute(AccessibleAttribute.ROW_AT_INDEX, rowIndex);
        return getAccessible(treeTableRow);
    }

    private void changeSelection(boolean add, boolean clear) {
        Accessible container;
        ObservableList<Node> items;
        AccessibleRole role = (AccessibleRole) getAttribute(AccessibleAttribute.ROLE, new Object[0]);
        if (role == null || (container = getContainer()) == null) {
            return;
        }
        Node item = null;
        switch (role) {
            case TABLE_CELL:
            case TREE_TABLE_CELL:
                Integer rowIndex = (Integer) getAttribute(AccessibleAttribute.ROW_INDEX, new Object[0]);
                Integer columnIndex = (Integer) getAttribute(AccessibleAttribute.COLUMN_INDEX, new Object[0]);
                if (rowIndex != null && columnIndex != null) {
                    item = (Node) container.getAttribute(AccessibleAttribute.CELL_AT_ROW_COLUMN, rowIndex, columnIndex);
                    break;
                }
                break;
            case LIST_ITEM:
                Integer index = (Integer) getAttribute(AccessibleAttribute.INDEX, new Object[0]);
                if (index != null) {
                    item = (Node) container.getAttribute(AccessibleAttribute.ITEM_AT_INDEX, index);
                    break;
                }
                break;
            case CHECK_BOX_TREE_ITEM:
            case TREE_ITEM:
                Integer index2 = (Integer) getAttribute(AccessibleAttribute.INDEX, new Object[0]);
                if (index2 != null) {
                    item = (Node) container.getAttribute(AccessibleAttribute.ROW_AT_INDEX, index2);
                    break;
                }
                break;
        }
        if (item != null) {
            ObservableList<Node> newItems = FXCollections.observableArrayList();
            if (!clear && (items = (ObservableList) container.getAttribute(AccessibleAttribute.SELECTED_ITEMS, new Object[0])) != null) {
                newItems.addAll(items);
            }
            if (add) {
                newItems.add(item);
            } else {
                newItems.remove(item);
            }
            container.executeAction(AccessibleAction.SET_SELECTED_ITEMS, newItems);
        }
    }

    private long GetPatternProvider(int patternId) {
        AccessibleRole role;
        if (isDisposed() || (role = (AccessibleRole) getAttribute(AccessibleAttribute.ROLE, new Object[0])) == null) {
            return 0L;
        }
        boolean impl = false;
        switch (role) {
            case TABLE_CELL:
                impl = patternId == UIA_SelectionItemPatternId || patternId == UIA_GridItemPatternId || patternId == UIA_TableItemPatternId || patternId == UIA_ScrollItemPatternId;
                break;
            case LIST_ITEM:
                impl = patternId == UIA_SelectionItemPatternId || patternId == UIA_ScrollItemPatternId;
                break;
            case TAB_ITEM:
            case PAGE_ITEM:
                impl = patternId == UIA_SelectionItemPatternId;
                break;
            case CHECK_BOX_TREE_ITEM:
                impl = patternId == UIA_SelectionItemPatternId || patternId == UIA_ExpandCollapsePatternId || patternId == UIA_ScrollItemPatternId || patternId == UIA_TogglePatternId;
                break;
            case TREE_ITEM:
                impl = patternId == UIA_SelectionItemPatternId || patternId == UIA_ExpandCollapsePatternId || patternId == UIA_ScrollItemPatternId;
                break;
            case TREE_TABLE_CELL:
                impl = patternId == UIA_SelectionItemPatternId || patternId == UIA_GridItemPatternId || patternId == UIA_TableItemPatternId || patternId == UIA_ExpandCollapsePatternId || patternId == UIA_ScrollItemPatternId;
                break;
            case RADIO_MENU_ITEM:
            case CHECK_MENU_ITEM:
                impl = patternId == 10000 || patternId == UIA_TogglePatternId;
                break;
            case MENU:
            case SPLIT_MENU_BUTTON:
                impl = patternId == 10000 || patternId == UIA_ExpandCollapsePatternId;
                break;
            case MENU_ITEM:
            case BUTTON:
            case MENU_BUTTON:
            case INCREMENT_BUTTON:
            case DECREMENT_BUTTON:
            case HYPERLINK:
                impl = patternId == 10000;
                break;
            case TOGGLE_BUTTON:
            case CHECK_BOX:
                impl = patternId == UIA_TogglePatternId;
                break;
            case PAGINATION:
            case TAB_PANE:
                impl = patternId == UIA_SelectionPatternId;
                break;
            case SLIDER:
            case PROGRESS_INDICATOR:
            case SCROLL_BAR:
                impl = patternId == UIA_RangeValuePatternId;
                break;
            case TEXT_FIELD:
            case TEXT_AREA:
                impl = patternId == UIA_TextPatternId || patternId == UIA_ValuePatternId;
                break;
            case TREE_TABLE_VIEW:
            case TABLE_VIEW:
                impl = patternId == UIA_SelectionPatternId || patternId == UIA_GridPatternId || patternId == UIA_TablePatternId || patternId == UIA_ScrollPatternId;
                break;
            case LIST_VIEW:
                impl = patternId == UIA_SelectionPatternId || patternId == UIA_ScrollPatternId;
                break;
            case RADIO_BUTTON:
                impl = patternId == UIA_SelectionItemPatternId;
                break;
            case COMBO_BOX:
                impl = patternId == UIA_ExpandCollapsePatternId || patternId == UIA_ValuePatternId;
                break;
            case TREE_VIEW:
                impl = patternId == UIA_SelectionPatternId || patternId == UIA_ScrollPatternId;
                break;
            case TOOL_BAR:
            case TITLED_PANE:
                impl = patternId == UIA_ExpandCollapsePatternId;
                break;
            case SCROLL_PANE:
                impl = patternId == UIA_ScrollPatternId;
                break;
        }
        if (impl) {
            return getNativeAccessible();
        }
        return 0L;
    }

    private long get_HostRawElementProvider() {
        View view;
        if (isDisposed() || (view = getView()) == null) {
            return 0L;
        }
        return view.getNativeView();
    }

    private WinVariant GetPropertyValue(int propertyId) {
        Accessible listAccessible;
        Scene scene;
        Accessible acc;
        Node node;
        String name;
        Node label;
        if (isDisposed()) {
            return null;
        }
        WinVariant variant = null;
        switch (propertyId) {
            case UIA_ControlTypePropertyId /* 30003 */:
                int controlType = getControlType();
                if (controlType != 0) {
                    variant = new WinVariant();
                    variant.vt = (short) 3;
                    variant.lVal = controlType;
                    break;
                }
                break;
            case UIA_LocalizedControlTypePropertyId /* 30004 */:
                String description = (String) getAttribute(AccessibleAttribute.ROLE_DESCRIPTION, new Object[0]);
                if (description == null) {
                    AccessibleRole role = (AccessibleRole) getAttribute(AccessibleAttribute.ROLE, new Object[0]);
                    if (role == null) {
                        role = AccessibleRole.NODE;
                    }
                    switch (role) {
                        case PAGE_ITEM:
                            description = "page";
                            break;
                        case TITLED_PANE:
                            description = "title pane";
                            break;
                        case DIALOG:
                            description = "dialog";
                            break;
                    }
                }
                if (description != null) {
                    variant = new WinVariant();
                    variant.vt = (short) 8;
                    variant.bstrVal = description;
                    break;
                }
                break;
            case UIA_NamePropertyId /* 30005 */:
                AccessibleRole role2 = (AccessibleRole) getAttribute(AccessibleAttribute.ROLE, new Object[0]);
                if (role2 == null) {
                    role2 = AccessibleRole.NODE;
                }
                switch (role2) {
                    case INCREMENT_BUTTON:
                    case DECREMENT_BUTTON:
                        name = (String) getAttribute(AccessibleAttribute.TEXT, new Object[0]);
                        if (name == null || name.length() == 0) {
                            if (role2 == AccessibleRole.INCREMENT_BUTTON) {
                                name = "increment";
                                break;
                            } else {
                                name = "decrement";
                                break;
                            }
                        }
                        break;
                    case TEXT_FIELD:
                    case TEXT_AREA:
                    case COMBO_BOX:
                        name = null;
                        break;
                    default:
                        name = (String) getAttribute(AccessibleAttribute.TEXT, new Object[0]);
                        break;
                }
                if ((name == null || name.length() == 0) && (label = (Node) getAttribute(AccessibleAttribute.LABELED_BY, new Object[0])) != null) {
                    name = (String) getAccessible(label).getAttribute(AccessibleAttribute.TEXT, new Object[0]);
                }
                if (name != null && name.length() != 0) {
                    variant = new WinVariant();
                    variant.vt = (short) 8;
                    variant.bstrVal = name;
                    break;
                }
                break;
            case UIA_AcceleratorKeyPropertyId /* 30006 */:
                KeyCombination kc = (KeyCombination) getAttribute(AccessibleAttribute.ACCELERATOR, new Object[0]);
                if (kc != null) {
                    variant = new WinVariant();
                    variant.vt = (short) 8;
                    variant.bstrVal = kc.toString().replaceAll("Shortcut", "Ctrl");
                    break;
                }
                break;
            case UIA_AccessKeyPropertyId /* 30007 */:
                String mnemonic = (String) getAttribute(AccessibleAttribute.MNEMONIC, new Object[0]);
                if (mnemonic != null) {
                    variant = new WinVariant();
                    variant.vt = (short) 8;
                    variant.bstrVal = "Alt " + mnemonic.toLowerCase();
                    break;
                }
                break;
            case UIA_HasKeyboardFocusPropertyId /* 30008 */:
                Boolean focus = (Boolean) getAttribute(AccessibleAttribute.FOCUSED, new Object[0]);
                if (Boolean.FALSE.equals(focus) && (scene = (Scene) getAttribute(AccessibleAttribute.SCENE, new Object[0])) != null && (acc = getAccessible(scene)) != null && (node = (Node) acc.getAttribute(AccessibleAttribute.FOCUS_NODE, new Object[0])) != null) {
                    Node item = (Node) getAccessible(node).getAttribute(AccessibleAttribute.FOCUS_ITEM, new Object[0]);
                    if (getNativeAccessible(item) == this.peer) {
                        focus = true;
                    }
                }
                variant = new WinVariant();
                variant.vt = (short) 11;
                variant.boolVal = focus != null ? focus.booleanValue() : false;
                break;
            case UIA_IsKeyboardFocusablePropertyId /* 30009 */:
                variant = new WinVariant();
                variant.vt = (short) 11;
                variant.boolVal = true;
                break;
            case UIA_IsEnabledPropertyId /* 30010 */:
                Boolean disabled = (Boolean) getAttribute(AccessibleAttribute.DISABLED, new Object[0]);
                variant = new WinVariant();
                variant.vt = (short) 11;
                boolean z2 = disabled == null || !disabled.booleanValue();
                variant.boolVal = z2;
                break;
            case UIA_AutomationIdPropertyId /* 30011 */:
                variant = new WinVariant();
                variant.vt = (short) 8;
                variant.bstrVal = "JavaFX" + this.id;
                break;
            case UIA_HelpTextPropertyId /* 30013 */:
                String help = (String) getAttribute(AccessibleAttribute.HELP, new Object[0]);
                if (help != null) {
                    variant = new WinVariant();
                    variant.vt = (short) 8;
                    variant.bstrVal = help;
                    break;
                }
                break;
            case UIA_IsControlElementPropertyId /* 30016 */:
            case UIA_IsContentElementPropertyId /* 30017 */:
                variant = new WinVariant();
                variant.vt = (short) 11;
                variant.boolVal = (getView() == null && isIgnored()) ? false : true;
                break;
            case UIA_IsPasswordPropertyId /* 30019 */:
                AccessibleRole role3 = (AccessibleRole) getAttribute(AccessibleAttribute.ROLE, new Object[0]);
                variant = new WinVariant();
                variant.vt = (short) 11;
                variant.boolVal = role3 == AccessibleRole.PASSWORD_FIELD;
                break;
            case UIA_ToggleToggleStatePropertyId /* 30086 */:
                AccessibleRole role4 = (AccessibleRole) getAttribute(AccessibleAttribute.ROLE, new Object[0]);
                if (role4 == AccessibleRole.CHECK_BOX_TREE_ITEM) {
                    variant = new WinVariant();
                    variant.vt = (short) 3;
                    variant.lVal = get_ToggleState();
                    break;
                }
                break;
            case UIA_ProviderDescriptionPropertyId /* 30107 */:
                variant = new WinVariant();
                variant.vt = (short) 8;
                variant.bstrVal = "JavaFXProvider";
                break;
            case UIA_PositionInSetPropertyId /* 30152 */:
                AccessibleRole role5 = (AccessibleRole) getAttribute(AccessibleAttribute.ROLE, new Object[0]);
                if (role5 == AccessibleRole.LIST_ITEM) {
                    variant = new WinVariant();
                    variant.vt = (short) 3;
                    variant.lVal = ((Integer) getAttribute(AccessibleAttribute.INDEX, new Object[0])).intValue() + 1;
                    break;
                }
                break;
            case UIA_SizeOfSetPropertyId /* 30153 */:
                AccessibleRole role6 = (AccessibleRole) getAttribute(AccessibleAttribute.ROLE, new Object[0]);
                if (role6 == AccessibleRole.LIST_ITEM && (listAccessible = getContainer()) != null) {
                    Integer count = (Integer) listAccessible.getAttribute(AccessibleAttribute.ITEM_COUNT, new Object[0]);
                    if (count.intValue() != 0) {
                        variant = new WinVariant();
                        variant.vt = (short) 3;
                        variant.lVal = count.intValue();
                        break;
                    }
                }
                break;
            case UIA_IsDialogPropertyId /* 30174 */:
                AccessibleRole role7 = (AccessibleRole) getAttribute(AccessibleAttribute.ROLE, new Object[0]);
                variant = new WinVariant();
                variant.vt = (short) 11;
                variant.boolVal = role7 == AccessibleRole.DIALOG;
                break;
        }
        return variant;
    }

    private float[] get_BoundingRectangle() {
        Bounds bounds;
        if (!isDisposed() && getView() == null && (bounds = (Bounds) getAttribute(AccessibleAttribute.BOUNDS, new Object[0])) != null) {
            return new float[]{(float) bounds.getMinX(), (float) bounds.getMinY(), (float) bounds.getWidth(), (float) bounds.getHeight()};
        }
        return null;
    }

    private long get_FragmentRoot() {
        Scene scene;
        WinAccessible acc;
        if (isDisposed() || (scene = (Scene) getAttribute(AccessibleAttribute.SCENE, new Object[0])) == null || (acc = (WinAccessible) getAccessible(scene)) == null || acc.isDisposed()) {
            return 0L;
        }
        return acc.getNativeAccessible();
    }

    private long[] GetEmbeddedFragmentRoots() {
        return isDisposed() ? null : null;
    }

    private int[] GetRuntimeId() {
        if (!isDisposed() && getView() == null) {
            return new int[]{3, this.id};
        }
        return null;
    }

    private long NavigateListView(WinAccessible listItemAccessible, int direction) {
        Integer count;
        Accessible listAccessible = listItemAccessible.getContainer();
        if (listAccessible == null || (count = (Integer) listAccessible.getAttribute(AccessibleAttribute.ITEM_COUNT, new Object[0])) == null || count.intValue() == 0) {
            return 0L;
        }
        Integer index = (Integer) listItemAccessible.getAttribute(AccessibleAttribute.INDEX, new Object[0]);
        if (index == null || 0 > index.intValue() || index.intValue() >= count.intValue()) {
            return 0L;
        }
        switch (direction) {
            case 1:
                index = Integer.valueOf(index.intValue() + 1);
                break;
            case 2:
                index = Integer.valueOf(index.intValue() - 1);
                break;
            case 3:
                index = 0;
                break;
            case 4:
                index = Integer.valueOf(count.intValue() - 1);
                break;
        }
        if (0 > index.intValue() || index.intValue() >= count.intValue()) {
            return 0L;
        }
        Node node = (Node) listAccessible.getAttribute(AccessibleAttribute.ITEM_AT_INDEX, index);
        return getNativeAccessible(node);
    }

    private long Navigate(int direction) {
        int count;
        Function<Integer, Node> getChild;
        int currentIndex;
        if (isDisposed()) {
            return 0L;
        }
        AccessibleRole role = (AccessibleRole) getAttribute(AccessibleAttribute.ROLE, new Object[0]);
        boolean treeCell = role == AccessibleRole.TREE_ITEM || role == AccessibleRole.CHECK_BOX_TREE_ITEM;
        Node node = null;
        switch (direction) {
            case 0:
                if (getView() != null) {
                    return 0L;
                }
                if (treeCell) {
                    node = (Node) getAttribute(AccessibleAttribute.TREE_ITEM_PARENT, new Object[0]);
                    if (node == null) {
                        WinAccessible acc = (WinAccessible) getContainer();
                        if (acc != null) {
                            return acc.getNativeAccessible();
                        }
                        return 0L;
                    }
                } else {
                    node = (Node) getAttribute(AccessibleAttribute.PARENT, new Object[0]);
                    if (node == null) {
                        Scene scene = (Scene) getAttribute(AccessibleAttribute.SCENE, new Object[0]);
                        WinAccessible acc2 = (WinAccessible) getAccessible(scene);
                        if (acc2 == null || acc2 == this || acc2.isDisposed()) {
                            return 0L;
                        }
                        return acc2.getNativeAccessible();
                    }
                }
                break;
            case 1:
            case 2:
                if (role == AccessibleRole.LIST_ITEM) {
                    return NavigateListView(this, direction);
                }
                Node parent = (Node) getAttribute(treeCell ? AccessibleAttribute.TREE_ITEM_PARENT : AccessibleAttribute.PARENT, new Object[0]);
                if (parent != null) {
                    WinAccessible parentAccessible = (WinAccessible) getAccessible(parent);
                    if (treeCell) {
                        Integer result = (Integer) parentAccessible.getAttribute(AccessibleAttribute.TREE_ITEM_COUNT, new Object[0]);
                        if (result == null) {
                            return 0L;
                        }
                        count = result.intValue();
                        getChild = index -> {
                            return (Node) parentAccessible.getAttribute(AccessibleAttribute.TREE_ITEM_AT_INDEX, index);
                        };
                    } else {
                        List<Node> children = getUnignoredChildren(parentAccessible);
                        if (children == null) {
                            return 0L;
                        }
                        count = children.size();
                        getChild = index2 -> {
                            return (Node) children.get(index2.intValue());
                        };
                    }
                    int lastIndex = parentAccessible.lastIndex;
                    int currentIndex2 = -1;
                    if (0 <= lastIndex && lastIndex < count && getNativeAccessible(getChild.apply(Integer.valueOf(lastIndex))) == this.peer) {
                        currentIndex2 = lastIndex;
                    } else {
                        int i2 = 0;
                        while (true) {
                            if (i2 < count) {
                                if (getNativeAccessible(getChild.apply(Integer.valueOf(i2))) != this.peer) {
                                    i2++;
                                } else {
                                    currentIndex2 = i2;
                                }
                            }
                        }
                    }
                    if (currentIndex2 != -1) {
                        if (direction == 1) {
                            currentIndex = currentIndex2 + 1;
                        } else {
                            currentIndex = currentIndex2 - 1;
                        }
                        if (0 <= currentIndex && currentIndex < count) {
                            node = getChild.apply(Integer.valueOf(currentIndex));
                            parentAccessible.lastIndex = currentIndex;
                            break;
                        }
                    }
                }
                break;
            case 3:
            case 4:
                this.lastIndex = -1;
                if (role == AccessibleRole.LIST_VIEW) {
                    getAttribute(AccessibleAttribute.ITEM_AT_INDEX, 0);
                }
                if (role == AccessibleRole.TREE_VIEW) {
                    this.lastIndex = 0;
                    node = (Node) getAttribute(AccessibleAttribute.ROW_AT_INDEX, 0);
                    break;
                } else if (treeCell) {
                    Integer count2 = (Integer) getAttribute(AccessibleAttribute.TREE_ITEM_COUNT, new Object[0]);
                    if (count2 != null && count2.intValue() > 0) {
                        this.lastIndex = direction == 3 ? 0 : count2.intValue() - 1;
                        node = (Node) getAttribute(AccessibleAttribute.TREE_ITEM_AT_INDEX, Integer.valueOf(this.lastIndex));
                        break;
                    }
                } else {
                    List<Node> children2 = getUnignoredChildren(this);
                    if (children2 != null && children2.size() > 0) {
                        this.lastIndex = direction == 3 ? 0 : children2.size() - 1;
                        node = children2.get(this.lastIndex);
                    }
                    if (node != null && ((AccessibleRole) getAccessible(node).getAttribute(AccessibleAttribute.ROLE, new Object[0])) == AccessibleRole.LIST_ITEM) {
                        WinAccessible itemAcc = (WinAccessible) getAccessible(node);
                        return NavigateListView(itemAcc, direction);
                    }
                }
                break;
        }
        return getNativeAccessible(node);
    }

    private void SetFocus() {
        if (isDisposed()) {
            return;
        }
        executeAction(AccessibleAction.REQUEST_FOCUS, new Object[0]);
    }

    private long ElementProviderFromPoint(double x2, double y2) {
        if (isDisposed()) {
            return 0L;
        }
        Node node = (Node) getAttribute(AccessibleAttribute.NODE_AT_POINT, new Point2D(x2, y2));
        return getNativeAccessible(node);
    }

    private long GetFocus() {
        Node node;
        if (isDisposed() || (node = (Node) getAttribute(AccessibleAttribute.FOCUS_NODE, new Object[0])) == null) {
            return 0L;
        }
        Node item = (Node) getAccessible(node).getAttribute(AccessibleAttribute.FOCUS_ITEM, new Object[0]);
        return item != null ? getNativeAccessible(item) : getNativeAccessible(node);
    }

    private void AdviseEventAdded(int eventId, long propertyIDs) {
    }

    private void AdviseEventRemoved(int eventId, long propertyIDs) {
    }

    private void Invoke() {
        if (isDisposed()) {
            return;
        }
        executeAction(AccessibleAction.FIRE, new Object[0]);
    }

    private long[] GetSelection() {
        AccessibleRole role;
        if (isDisposed() || (role = (AccessibleRole) getAttribute(AccessibleAttribute.ROLE, new Object[0])) == null) {
            return null;
        }
        switch (role) {
            case PAGINATION:
            case TAB_PANE:
                Node node = (Node) getAttribute(AccessibleAttribute.FOCUS_ITEM, new Object[0]);
                if (node != null) {
                    break;
                }
                break;
            case TEXT_FIELD:
            case TEXT_AREA:
                if (this.selectionRange == null) {
                    this.selectionRange = new WinTextRangeProvider(this);
                }
                Integer result = (Integer) getAttribute(AccessibleAttribute.SELECTION_START, new Object[0]);
                int start = result != null ? result.intValue() : 0;
                int end = -1;
                int length = -1;
                if (start >= 0) {
                    Integer result2 = (Integer) getAttribute(AccessibleAttribute.SELECTION_END, new Object[0]);
                    end = result2 != null ? result2.intValue() : 0;
                    if (end >= start) {
                        String string = (String) getAttribute(AccessibleAttribute.TEXT, new Object[0]);
                        length = string != null ? string.length() : 0;
                    }
                }
                if (length != -1 && end <= length) {
                    this.selectionRange.setRange(start, end);
                } else {
                    this.selectionRange.setRange(0, 0);
                }
                break;
            case TREE_TABLE_VIEW:
            case TABLE_VIEW:
            case LIST_VIEW:
            case TREE_VIEW:
                ObservableList<Node> selection = (ObservableList) getAttribute(AccessibleAttribute.SELECTED_ITEMS, new Object[0]);
                if (selection != null) {
                    break;
                }
                break;
        }
        return null;
    }

    private boolean get_CanSelectMultiple() {
        AccessibleRole role;
        if (!isDisposed() && (role = (AccessibleRole) getAttribute(AccessibleAttribute.ROLE, new Object[0])) != null) {
            switch (role) {
                case TREE_TABLE_VIEW:
                case TABLE_VIEW:
                case LIST_VIEW:
                case TREE_VIEW:
                    return Boolean.TRUE.equals(getAttribute(AccessibleAttribute.MULTIPLE_SELECTION, new Object[0]));
                case IMAGE_VIEW:
                case RADIO_BUTTON:
                case CHECK_BOX:
                case COMBO_BOX:
                case HYPERLINK:
                default:
                    return false;
            }
        }
        return false;
    }

    private boolean get_IsSelectionRequired() {
        return !isDisposed();
    }

    private void SetValue(double val) {
        AccessibleRole role;
        if (!isDisposed() && (role = (AccessibleRole) getAttribute(AccessibleAttribute.ROLE, new Object[0])) != null) {
            switch (role) {
                case SLIDER:
                case SCROLL_BAR:
                    executeAction(AccessibleAction.SET_VALUE, Double.valueOf(val));
                    break;
            }
        }
    }

    private double get_Value() {
        Double value;
        if (isDisposed() || Boolean.TRUE.equals(getAttribute(AccessibleAttribute.INDETERMINATE, new Object[0])) || (value = (Double) getAttribute(AccessibleAttribute.VALUE, new Object[0])) == null) {
            return 0.0d;
        }
        return value.doubleValue();
    }

    private boolean get_IsReadOnly() {
        if (isDisposed()) {
            return false;
        }
        AccessibleRole role = (AccessibleRole) getAttribute(AccessibleAttribute.ROLE, new Object[0]);
        if (role == null) {
            return true;
        }
        switch (role) {
            case SLIDER:
                break;
            case TEXT_FIELD:
            case TEXT_AREA:
            case COMBO_BOX:
                break;
            case SCROLL_BAR:
                break;
        }
        return false;
    }

    private double get_Maximum() {
        Double value;
        if (isDisposed() || (value = (Double) getAttribute(AccessibleAttribute.MAX_VALUE, new Object[0])) == null) {
            return 0.0d;
        }
        return value.doubleValue();
    }

    private double get_Minimum() {
        Double value;
        if (isDisposed() || (value = (Double) getAttribute(AccessibleAttribute.MIN_VALUE, new Object[0])) == null) {
            return 0.0d;
        }
        return value.doubleValue();
    }

    private double get_LargeChange() {
        return isDisposed() ? 0.0d : 10.0d;
    }

    private double get_SmallChange() {
        return isDisposed() ? 0.0d : 3.0d;
    }

    private void SetValueString(String val) {
        AccessibleRole role;
        if (!isDisposed() && (role = (AccessibleRole) getAttribute(AccessibleAttribute.ROLE, new Object[0])) != null) {
            switch (role) {
                case TEXT_FIELD:
                case TEXT_AREA:
                    executeAction(AccessibleAction.SET_TEXT, val);
                    break;
            }
        }
    }

    private String get_ValueString() {
        if (isDisposed()) {
            return null;
        }
        return (String) getAttribute(AccessibleAttribute.TEXT, new Object[0]);
    }

    private void Select() {
        AccessibleRole role;
        if (!isDisposed() && (role = (AccessibleRole) getAttribute(AccessibleAttribute.ROLE, new Object[0])) != null) {
            switch (role) {
                case TABLE_CELL:
                case LIST_ITEM:
                case CHECK_BOX_TREE_ITEM:
                case TREE_ITEM:
                case TREE_TABLE_CELL:
                    changeSelection(true, true);
                    break;
                case TAB_ITEM:
                case PAGE_ITEM:
                    executeAction(AccessibleAction.REQUEST_FOCUS, new Object[0]);
                    break;
                case BUTTON:
                case TOGGLE_BUTTON:
                case INCREMENT_BUTTON:
                case DECREMENT_BUTTON:
                case RADIO_BUTTON:
                    executeAction(AccessibleAction.FIRE, new Object[0]);
                    break;
            }
        }
    }

    private void AddToSelection() {
        if (isDisposed()) {
            return;
        }
        changeSelection(true, false);
    }

    private void RemoveFromSelection() {
        if (isDisposed()) {
            return;
        }
        changeSelection(false, false);
    }

    private boolean get_IsSelected() {
        if (isDisposed()) {
            return false;
        }
        return Boolean.TRUE.equals(getAttribute(AccessibleAttribute.SELECTED, new Object[0]));
    }

    private long get_SelectionContainer() {
        WinAccessible acc;
        if (isDisposed() || (acc = (WinAccessible) getContainer()) == null) {
            return 0L;
        }
        return acc.getNativeAccessible();
    }

    private long[] GetVisibleRanges() {
        if (isDisposed()) {
            return null;
        }
        return new long[]{get_DocumentRange()};
    }

    private long RangeFromChild(long childElement) {
        return isDisposed() ? 0L : 0L;
    }

    private long RangeFromPoint(double x2, double y2) {
        Integer offset;
        if (!isDisposed() && (offset = (Integer) getAttribute(AccessibleAttribute.OFFSET_AT_POINT, new Point2D(x2, y2))) != null) {
            WinTextRangeProvider range = new WinTextRangeProvider(this);
            range.setRange(offset.intValue(), offset.intValue());
            return range.getNativeProvider();
        }
        return 0L;
    }

    private long get_DocumentRange() {
        if (isDisposed()) {
            return 0L;
        }
        if (this.documentRange == null) {
            this.documentRange = new WinTextRangeProvider(this);
        }
        String text = (String) getAttribute(AccessibleAttribute.TEXT, new Object[0]);
        if (text == null) {
            return 0L;
        }
        this.documentRange.setRange(0, text.length());
        return this.documentRange.getNativeProvider();
    }

    private int get_SupportedTextSelection() {
        return isDisposed() ? 0 : 1;
    }

    private int get_ColumnCount() {
        if (isDisposed()) {
            return 0;
        }
        Integer count = (Integer) getAttribute(AccessibleAttribute.COLUMN_COUNT, new Object[0]);
        if (count != null) {
            return count.intValue();
        }
        return 1;
    }

    private int get_RowCount() {
        Integer count;
        if (isDisposed() || (count = (Integer) getAttribute(AccessibleAttribute.ROW_COUNT, new Object[0])) == null) {
            return 0;
        }
        return count.intValue();
    }

    private long GetItem(int row, int column) {
        if (isDisposed()) {
            return 0L;
        }
        Node node = (Node) getAttribute(AccessibleAttribute.CELL_AT_ROW_COLUMN, Integer.valueOf(row), Integer.valueOf(column));
        return getNativeAccessible(node);
    }

    private int get_Column() {
        Integer result;
        if (isDisposed() || (result = (Integer) getAttribute(AccessibleAttribute.COLUMN_INDEX, new Object[0])) == null) {
            return 0;
        }
        return result.intValue();
    }

    private int get_ColumnSpan() {
        return isDisposed() ? 0 : 1;
    }

    private long get_ContainingGrid() {
        WinAccessible acc;
        if (isDisposed() || (acc = (WinAccessible) getContainer()) == null) {
            return 0L;
        }
        return acc.getNativeAccessible();
    }

    private int get_Row() {
        if (isDisposed()) {
            return 0;
        }
        Integer result = null;
        AccessibleRole role = (AccessibleRole) getAttribute(AccessibleAttribute.ROLE, new Object[0]);
        if (role != null) {
            switch (role) {
                case TABLE_ROW:
                case LIST_ITEM:
                case TREE_TABLE_ROW:
                    result = (Integer) getAttribute(AccessibleAttribute.INDEX, new Object[0]);
                    break;
                case TABLE_CELL:
                case TREE_TABLE_CELL:
                    result = (Integer) getAttribute(AccessibleAttribute.ROW_INDEX, new Object[0]);
                    break;
            }
        }
        if (result != null) {
            return result.intValue();
        }
        return 0;
    }

    private int get_RowSpan() {
        return isDisposed() ? 0 : 1;
    }

    private long[] GetColumnHeaders() {
        return isDisposed() ? null : null;
    }

    private long[] GetRowHeaders() {
        return isDisposed() ? null : null;
    }

    private int get_RowOrColumnMajor() {
        return isDisposed() ? 0 : 0;
    }

    private long[] GetColumnHeaderItems() {
        Integer columnIndex;
        Accessible acc;
        Node column;
        if (isDisposed() || (columnIndex = (Integer) getAttribute(AccessibleAttribute.COLUMN_INDEX, new Object[0])) == null || (acc = getContainer()) == null || (column = (Node) acc.getAttribute(AccessibleAttribute.COLUMN_AT_INDEX, columnIndex)) == null) {
            return null;
        }
        return new long[]{getNativeAccessible(column)};
    }

    private long[] GetRowHeaderItems() {
        return isDisposed() ? null : null;
    }

    private void Toggle() {
        if (isDisposed()) {
            return;
        }
        executeAction(AccessibleAction.FIRE, new Object[0]);
    }

    private int get_ToggleState() {
        if (isDisposed()) {
            return 0;
        }
        if (getAttribute(AccessibleAttribute.ROLE, new Object[0]) == AccessibleRole.CHECK_BOX_TREE_ITEM) {
            AccessibleAttribute.ToggleState toggleState = (AccessibleAttribute.ToggleState) getAttribute(AccessibleAttribute.TOGGLE_STATE, new Object[0]);
            if (toggleState == AccessibleAttribute.ToggleState.INDETERMINATE) {
                return 2;
            }
            if (toggleState == AccessibleAttribute.ToggleState.CHECKED) {
                return 1;
            }
            return 0;
        }
        if (Boolean.TRUE.equals(getAttribute(AccessibleAttribute.INDETERMINATE, new Object[0]))) {
            return 2;
        }
        boolean selected = Boolean.TRUE.equals(getAttribute(AccessibleAttribute.SELECTED, new Object[0]));
        return selected ? 1 : 0;
    }

    private void Collapse() {
        if (isDisposed()) {
            return;
        }
        AccessibleRole role = (AccessibleRole) getAttribute(AccessibleAttribute.ROLE, new Object[0]);
        if (role == AccessibleRole.TOOL_BAR) {
            Node button = (Node) getAttribute(AccessibleAttribute.OVERFLOW_BUTTON, new Object[0]);
            if (button != null) {
                getAccessible(button).executeAction(AccessibleAction.FIRE, new Object[0]);
                return;
            }
            return;
        }
        if (role == AccessibleRole.TREE_TABLE_CELL) {
            Accessible row = getRow();
            if (row != null) {
                row.executeAction(AccessibleAction.COLLAPSE, new Object[0]);
                return;
            }
            return;
        }
        executeAction(AccessibleAction.COLLAPSE, new Object[0]);
    }

    private void Expand() {
        if (isDisposed()) {
            return;
        }
        AccessibleRole role = (AccessibleRole) getAttribute(AccessibleAttribute.ROLE, new Object[0]);
        if (role == AccessibleRole.TOOL_BAR) {
            Node button = (Node) getAttribute(AccessibleAttribute.OVERFLOW_BUTTON, new Object[0]);
            if (button != null) {
                getAccessible(button).executeAction(AccessibleAction.FIRE, new Object[0]);
                return;
            }
            return;
        }
        if (role == AccessibleRole.TREE_TABLE_CELL) {
            Accessible row = getRow();
            if (row != null) {
                row.executeAction(AccessibleAction.EXPAND, new Object[0]);
                return;
            }
            return;
        }
        executeAction(AccessibleAction.EXPAND, new Object[0]);
    }

    private int get_ExpandCollapseState() {
        Node button;
        if (isDisposed()) {
            return 0;
        }
        AccessibleRole role = (AccessibleRole) getAttribute(AccessibleAttribute.ROLE, new Object[0]);
        if (role == AccessibleRole.TOOL_BAR && (button = (Node) getAttribute(AccessibleAttribute.OVERFLOW_BUTTON, new Object[0])) != null) {
            boolean visible = Boolean.TRUE.equals(getAccessible(button).getAttribute(AccessibleAttribute.VISIBLE, new Object[0]));
            return visible ? 0 : 1;
        }
        if (role == AccessibleRole.TREE_TABLE_CELL) {
            Accessible row = getRow();
            if (row == null) {
                return 3;
            }
            Object o2 = row.getAttribute(AccessibleAttribute.LEAF, new Object[0]);
            if (Boolean.TRUE.equals(o2)) {
                return 3;
            }
            Object o3 = row.getAttribute(AccessibleAttribute.EXPANDED, new Object[0]);
            boolean isExpanded = Boolean.TRUE.equals(o3);
            return isExpanded ? 1 : 0;
        }
        Object o4 = getAttribute(AccessibleAttribute.LEAF, new Object[0]);
        if (Boolean.TRUE.equals(o4)) {
            return 3;
        }
        Object o5 = getAttribute(AccessibleAttribute.EXPANDED, new Object[0]);
        boolean isExpanded2 = Boolean.TRUE.equals(o5);
        return isExpanded2 ? 1 : 0;
    }

    private boolean get_CanMove() {
        return false;
    }

    private boolean get_CanResize() {
        return false;
    }

    private boolean get_CanRotate() {
        return false;
    }

    private void Move(double x2, double y2) {
    }

    private void Resize(double width, double height) {
    }

    private void Rotate(double degrees) {
    }

    private void Scroll(int horizontalAmount, int verticalAmount) {
        if (isDisposed()) {
        }
        if (get_VerticallyScrollable()) {
            Node vsb = (Node) getAttribute(AccessibleAttribute.VERTICAL_SCROLLBAR, new Object[0]);
            Accessible vsba = getAccessible(vsb);
            if (vsba != null) {
                switch (verticalAmount) {
                    case 0:
                        vsba.executeAction(AccessibleAction.BLOCK_DECREMENT, new Object[0]);
                        break;
                    case 1:
                        vsba.executeAction(AccessibleAction.DECREMENT, new Object[0]);
                        break;
                    case 3:
                        vsba.executeAction(AccessibleAction.BLOCK_INCREMENT, new Object[0]);
                        break;
                    case 4:
                        vsba.executeAction(AccessibleAction.INCREMENT, new Object[0]);
                        break;
                }
            } else {
                return;
            }
        }
        if (get_HorizontallyScrollable()) {
            Node hsb = (Node) getAttribute(AccessibleAttribute.HORIZONTAL_SCROLLBAR, new Object[0]);
            Accessible hsba = getAccessible(hsb);
            if (hsba == null) {
                return;
            }
            switch (horizontalAmount) {
                case 0:
                    hsba.executeAction(AccessibleAction.BLOCK_DECREMENT, new Object[0]);
                    break;
                case 1:
                    hsba.executeAction(AccessibleAction.DECREMENT, new Object[0]);
                    break;
                case 3:
                    hsba.executeAction(AccessibleAction.BLOCK_INCREMENT, new Object[0]);
                    break;
                case 4:
                    hsba.executeAction(AccessibleAction.INCREMENT, new Object[0]);
                    break;
            }
        }
    }

    private void SetScrollPercent(double horizontalPercent, double verticalPercent) {
        if (isDisposed()) {
            return;
        }
        if (verticalPercent != -1.0d && get_VerticallyScrollable()) {
            Node vsb = (Node) getAttribute(AccessibleAttribute.VERTICAL_SCROLLBAR, new Object[0]);
            Accessible acc = getAccessible(vsb);
            if (acc == null) {
                return;
            }
            Double min = (Double) acc.getAttribute(AccessibleAttribute.MIN_VALUE, new Object[0]);
            Double max = (Double) acc.getAttribute(AccessibleAttribute.MAX_VALUE, new Object[0]);
            if (min != null && max != null) {
                acc.executeAction(AccessibleAction.SET_VALUE, Double.valueOf(((max.doubleValue() - min.doubleValue()) * (verticalPercent / 100.0d)) + min.doubleValue()));
            }
        }
        if (horizontalPercent != -1.0d && get_HorizontallyScrollable()) {
            Node hsb = (Node) getAttribute(AccessibleAttribute.HORIZONTAL_SCROLLBAR, new Object[0]);
            Accessible acc2 = getAccessible(hsb);
            if (acc2 == null) {
                return;
            }
            Double min2 = (Double) acc2.getAttribute(AccessibleAttribute.MIN_VALUE, new Object[0]);
            Double max2 = (Double) acc2.getAttribute(AccessibleAttribute.MAX_VALUE, new Object[0]);
            if (min2 != null && max2 != null) {
                acc2.executeAction(AccessibleAction.SET_VALUE, Double.valueOf(((max2.doubleValue() - min2.doubleValue()) * (horizontalPercent / 100.0d)) + min2.doubleValue()));
            }
        }
    }

    private boolean get_HorizontallyScrollable() {
        Node hsb;
        if (isDisposed() || (hsb = (Node) getAttribute(AccessibleAttribute.HORIZONTAL_SCROLLBAR, new Object[0])) == null) {
            return false;
        }
        Boolean visible = (Boolean) getAccessible(hsb).getAttribute(AccessibleAttribute.VISIBLE, new Object[0]);
        return Boolean.TRUE.equals(visible);
    }

    private double get_HorizontalScrollPercent() {
        Double max;
        Double min;
        if (isDisposed()) {
            return 0.0d;
        }
        if (!get_HorizontallyScrollable()) {
            return -1.0d;
        }
        Node hsb = (Node) getAttribute(AccessibleAttribute.HORIZONTAL_SCROLLBAR, new Object[0]);
        if (hsb != null) {
            Accessible hsba = getAccessible(hsb);
            Double value = (Double) hsba.getAttribute(AccessibleAttribute.VALUE, new Object[0]);
            if (value == null || (max = (Double) hsba.getAttribute(AccessibleAttribute.MAX_VALUE, new Object[0])) == null || (min = (Double) hsba.getAttribute(AccessibleAttribute.MIN_VALUE, new Object[0])) == null) {
                return 0.0d;
            }
            return (100.0d * (value.doubleValue() - min.doubleValue())) / (max.doubleValue() - min.doubleValue());
        }
        return 0.0d;
    }

    private double get_HorizontalViewSize() {
        Node content;
        Bounds scrollPaneBounds;
        if (isDisposed()) {
            return 0.0d;
        }
        if (!get_HorizontallyScrollable() || (content = (Node) getAttribute(AccessibleAttribute.CONTENTS, new Object[0])) == null) {
            return 100.0d;
        }
        Bounds contentBounds = (Bounds) getAccessible(content).getAttribute(AccessibleAttribute.BOUNDS, new Object[0]);
        if (contentBounds == null || (scrollPaneBounds = (Bounds) getAttribute(AccessibleAttribute.BOUNDS, new Object[0])) == null) {
            return 0.0d;
        }
        return (scrollPaneBounds.getWidth() / contentBounds.getWidth()) * 100.0d;
    }

    private boolean get_VerticallyScrollable() {
        Node vsb;
        if (isDisposed() || (vsb = (Node) getAttribute(AccessibleAttribute.VERTICAL_SCROLLBAR, new Object[0])) == null) {
            return false;
        }
        Boolean visible = (Boolean) getAccessible(vsb).getAttribute(AccessibleAttribute.VISIBLE, new Object[0]);
        return Boolean.TRUE.equals(visible);
    }

    private double get_VerticalScrollPercent() {
        Double max;
        Double min;
        if (isDisposed()) {
            return 0.0d;
        }
        if (!get_VerticallyScrollable()) {
            return -1.0d;
        }
        Node vsb = (Node) getAttribute(AccessibleAttribute.VERTICAL_SCROLLBAR, new Object[0]);
        if (vsb != null) {
            Accessible vsba = getAccessible(vsb);
            Double value = (Double) vsba.getAttribute(AccessibleAttribute.VALUE, new Object[0]);
            if (value == null || (max = (Double) vsba.getAttribute(AccessibleAttribute.MAX_VALUE, new Object[0])) == null || (min = (Double) vsba.getAttribute(AccessibleAttribute.MIN_VALUE, new Object[0])) == null) {
                return 0.0d;
            }
            return (100.0d * (value.doubleValue() - min.doubleValue())) / (max.doubleValue() - min.doubleValue());
        }
        return 0.0d;
    }

    private double get_VerticalViewSize() {
        if (isDisposed()) {
            return 0.0d;
        }
        if (!get_VerticallyScrollable()) {
            return 100.0d;
        }
        double contentHeight = 0.0d;
        Bounds scrollPaneBounds = (Bounds) getAttribute(AccessibleAttribute.BOUNDS, new Object[0]);
        if (scrollPaneBounds == null) {
            return 0.0d;
        }
        double scrollPaneHeight = scrollPaneBounds.getHeight();
        AccessibleRole role = (AccessibleRole) getAttribute(AccessibleAttribute.ROLE, new Object[0]);
        if (role == null) {
            return 0.0d;
        }
        if (role == AccessibleRole.SCROLL_PANE) {
            Node content = (Node) getAttribute(AccessibleAttribute.CONTENTS, new Object[0]);
            if (content != null) {
                Bounds contentBounds = (Bounds) getAccessible(content).getAttribute(AccessibleAttribute.BOUNDS, new Object[0]);
                contentHeight = contentBounds == null ? 0.0d : contentBounds.getHeight();
            }
        } else {
            Integer itemCount = 0;
            switch (role) {
                case TREE_TABLE_VIEW:
                case TABLE_VIEW:
                case TREE_VIEW:
                    itemCount = (Integer) getAttribute(AccessibleAttribute.ROW_COUNT, new Object[0]);
                    break;
                case LIST_VIEW:
                    itemCount = (Integer) getAttribute(AccessibleAttribute.ITEM_COUNT, new Object[0]);
                    break;
            }
            contentHeight = itemCount == null ? 0.0d : itemCount.intValue() * 24;
        }
        if (contentHeight == 0.0d) {
            return 0.0d;
        }
        return (scrollPaneHeight / contentHeight) * 100.0d;
    }

    private void ScrollIntoView() {
        AccessibleRole role;
        Accessible container;
        if (isDisposed() || (role = (AccessibleRole) getAttribute(AccessibleAttribute.ROLE, new Object[0])) == null || (container = getContainer()) == null) {
            return;
        }
        Node item = null;
        switch (role) {
            case TABLE_CELL:
            case TREE_TABLE_CELL:
                Integer rowIndex = (Integer) getAttribute(AccessibleAttribute.ROW_INDEX, new Object[0]);
                Integer columnIndex = (Integer) getAttribute(AccessibleAttribute.COLUMN_INDEX, new Object[0]);
                if (rowIndex != null && columnIndex != null) {
                    item = (Node) container.getAttribute(AccessibleAttribute.CELL_AT_ROW_COLUMN, rowIndex, columnIndex);
                    break;
                }
                break;
            case LIST_ITEM:
                Integer index = (Integer) getAttribute(AccessibleAttribute.INDEX, new Object[0]);
                if (index != null) {
                    item = (Node) container.getAttribute(AccessibleAttribute.ITEM_AT_INDEX, index);
                    break;
                }
                break;
            case CHECK_BOX_TREE_ITEM:
            case TREE_ITEM:
                Integer index2 = (Integer) getAttribute(AccessibleAttribute.INDEX, new Object[0]);
                if (index2 != null) {
                    item = (Node) container.getAttribute(AccessibleAttribute.ROW_AT_INDEX, index2);
                    break;
                }
                break;
        }
        if (item != null) {
            container.executeAction(AccessibleAction.SHOW_ITEM, item);
        }
    }
}
