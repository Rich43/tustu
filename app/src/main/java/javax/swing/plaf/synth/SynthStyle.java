package javax.swing.plaf.synth;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.LookAndFeel;
import javax.swing.UIDefaults;
import javax.swing.border.Border;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicLookAndFeel;
import javax.swing.plaf.basic.BasicRootPaneUI;
import javax.swing.plaf.basic.BasicSliderUI;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.JTextComponent;
import sun.swing.FilePane;

/* loaded from: rt.jar:javax/swing/plaf/synth/SynthStyle.class */
public abstract class SynthStyle {
    private static Map<Object, Object> DEFAULT_VALUES;
    private static final SynthGraphicsUtils SYNTH_GRAPHICS = new SynthGraphicsUtils();

    protected abstract Color getColorForState(SynthContext synthContext, ColorType colorType);

    protected abstract Font getFontForState(SynthContext synthContext);

    private static void populateDefaultValues() {
        UIDefaults.LazyInputMap lazyInputMap = new UIDefaults.LazyInputMap(new Object[]{"SPACE", "pressed", "released SPACE", "released"});
        DEFAULT_VALUES.put("Button.focusInputMap", lazyInputMap);
        DEFAULT_VALUES.put("CheckBox.focusInputMap", lazyInputMap);
        DEFAULT_VALUES.put("RadioButton.focusInputMap", lazyInputMap);
        DEFAULT_VALUES.put("ToggleButton.focusInputMap", lazyInputMap);
        DEFAULT_VALUES.put("SynthArrowButton.focusInputMap", lazyInputMap);
        DEFAULT_VALUES.put("List.dropLineColor", Color.BLACK);
        DEFAULT_VALUES.put("Tree.dropLineColor", Color.BLACK);
        DEFAULT_VALUES.put("Table.dropLineColor", Color.BLACK);
        DEFAULT_VALUES.put("Table.dropLineShortColor", Color.RED);
        UIDefaults.LazyInputMap lazyInputMap2 = new UIDefaults.LazyInputMap(new Object[]{"ctrl C", DefaultEditorKit.copyAction, "ctrl V", DefaultEditorKit.pasteAction, "ctrl X", DefaultEditorKit.cutAction, "COPY", DefaultEditorKit.copyAction, "PASTE", DefaultEditorKit.pasteAction, "CUT", DefaultEditorKit.cutAction, "control INSERT", DefaultEditorKit.copyAction, "shift INSERT", DefaultEditorKit.pasteAction, "shift DELETE", DefaultEditorKit.cutAction, "shift LEFT", DefaultEditorKit.selectionBackwardAction, "shift KP_LEFT", DefaultEditorKit.selectionBackwardAction, "shift RIGHT", DefaultEditorKit.selectionForwardAction, "shift KP_RIGHT", DefaultEditorKit.selectionForwardAction, "ctrl LEFT", DefaultEditorKit.previousWordAction, "ctrl KP_LEFT", DefaultEditorKit.previousWordAction, "ctrl RIGHT", DefaultEditorKit.nextWordAction, "ctrl KP_RIGHT", DefaultEditorKit.nextWordAction, "ctrl shift LEFT", DefaultEditorKit.selectionPreviousWordAction, "ctrl shift KP_LEFT", DefaultEditorKit.selectionPreviousWordAction, "ctrl shift RIGHT", DefaultEditorKit.selectionNextWordAction, "ctrl shift KP_RIGHT", DefaultEditorKit.selectionNextWordAction, "ctrl A", DefaultEditorKit.selectAllAction, "HOME", DefaultEditorKit.beginLineAction, "END", DefaultEditorKit.endLineAction, "shift HOME", DefaultEditorKit.selectionBeginLineAction, "shift END", DefaultEditorKit.selectionEndLineAction, "UP", DefaultEditorKit.upAction, "KP_UP", DefaultEditorKit.upAction, "DOWN", DefaultEditorKit.downAction, "KP_DOWN", DefaultEditorKit.downAction, "PAGE_UP", DefaultEditorKit.pageUpAction, "PAGE_DOWN", DefaultEditorKit.pageDownAction, "shift PAGE_UP", "selection-page-up", "shift PAGE_DOWN", "selection-page-down", "ctrl shift PAGE_UP", "selection-page-left", "ctrl shift PAGE_DOWN", "selection-page-right", "shift UP", DefaultEditorKit.selectionUpAction, "shift KP_UP", DefaultEditorKit.selectionUpAction, "shift DOWN", DefaultEditorKit.selectionDownAction, "shift KP_DOWN", DefaultEditorKit.selectionDownAction, "ENTER", DefaultEditorKit.insertBreakAction, "BACK_SPACE", DefaultEditorKit.deletePrevCharAction, "shift BACK_SPACE", DefaultEditorKit.deletePrevCharAction, "ctrl H", DefaultEditorKit.deletePrevCharAction, "DELETE", DefaultEditorKit.deleteNextCharAction, "ctrl DELETE", DefaultEditorKit.deleteNextWordAction, "ctrl BACK_SPACE", DefaultEditorKit.deletePrevWordAction, "RIGHT", DefaultEditorKit.forwardAction, "LEFT", DefaultEditorKit.backwardAction, "KP_RIGHT", DefaultEditorKit.forwardAction, "KP_LEFT", DefaultEditorKit.backwardAction, "TAB", DefaultEditorKit.insertTabAction, "ctrl BACK_SLASH", "unselect", "ctrl HOME", DefaultEditorKit.beginAction, "ctrl END", DefaultEditorKit.endAction, "ctrl shift HOME", DefaultEditorKit.selectionBeginAction, "ctrl shift END", DefaultEditorKit.selectionEndAction, "ctrl T", "next-link-action", "ctrl shift T", "previous-link-action", "ctrl SPACE", "activate-link-action", "control shift O", "toggle-componentOrientation"});
        DEFAULT_VALUES.put("EditorPane.focusInputMap", lazyInputMap2);
        DEFAULT_VALUES.put("TextArea.focusInputMap", lazyInputMap2);
        DEFAULT_VALUES.put("TextPane.focusInputMap", lazyInputMap2);
        UIDefaults.LazyInputMap lazyInputMap3 = new UIDefaults.LazyInputMap(new Object[]{"ctrl C", DefaultEditorKit.copyAction, "ctrl V", DefaultEditorKit.pasteAction, "ctrl X", DefaultEditorKit.cutAction, "COPY", DefaultEditorKit.copyAction, "PASTE", DefaultEditorKit.pasteAction, "CUT", DefaultEditorKit.cutAction, "control INSERT", DefaultEditorKit.copyAction, "shift INSERT", DefaultEditorKit.pasteAction, "shift DELETE", DefaultEditorKit.cutAction, "shift LEFT", DefaultEditorKit.selectionBackwardAction, "shift KP_LEFT", DefaultEditorKit.selectionBackwardAction, "shift RIGHT", DefaultEditorKit.selectionForwardAction, "shift KP_RIGHT", DefaultEditorKit.selectionForwardAction, "ctrl LEFT", DefaultEditorKit.previousWordAction, "ctrl KP_LEFT", DefaultEditorKit.previousWordAction, "ctrl RIGHT", DefaultEditorKit.nextWordAction, "ctrl KP_RIGHT", DefaultEditorKit.nextWordAction, "ctrl shift LEFT", DefaultEditorKit.selectionPreviousWordAction, "ctrl shift KP_LEFT", DefaultEditorKit.selectionPreviousWordAction, "ctrl shift RIGHT", DefaultEditorKit.selectionNextWordAction, "ctrl shift KP_RIGHT", DefaultEditorKit.selectionNextWordAction, "ctrl A", DefaultEditorKit.selectAllAction, "HOME", DefaultEditorKit.beginLineAction, "END", DefaultEditorKit.endLineAction, "shift HOME", DefaultEditorKit.selectionBeginLineAction, "shift END", DefaultEditorKit.selectionEndLineAction, "BACK_SPACE", DefaultEditorKit.deletePrevCharAction, "shift BACK_SPACE", DefaultEditorKit.deletePrevCharAction, "ctrl H", DefaultEditorKit.deletePrevCharAction, "DELETE", DefaultEditorKit.deleteNextCharAction, "ctrl DELETE", DefaultEditorKit.deleteNextWordAction, "ctrl BACK_SPACE", DefaultEditorKit.deletePrevWordAction, "RIGHT", DefaultEditorKit.forwardAction, "LEFT", DefaultEditorKit.backwardAction, "KP_RIGHT", DefaultEditorKit.forwardAction, "KP_LEFT", DefaultEditorKit.backwardAction, "ENTER", JTextField.notifyAction, "ctrl BACK_SLASH", "unselect", "control shift O", "toggle-componentOrientation"});
        DEFAULT_VALUES.put("TextField.focusInputMap", lazyInputMap3);
        DEFAULT_VALUES.put("PasswordField.focusInputMap", lazyInputMap3);
        DEFAULT_VALUES.put("ComboBox.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[]{"ESCAPE", "hidePopup", "PAGE_UP", "pageUpPassThrough", "PAGE_DOWN", "pageDownPassThrough", "HOME", "homePassThrough", "END", "endPassThrough", "DOWN", "selectNext", "KP_DOWN", "selectNext", "alt DOWN", "togglePopup", "alt KP_DOWN", "togglePopup", "alt UP", "togglePopup", "alt KP_UP", "togglePopup", "SPACE", "spacePopup", "ENTER", "enterPressed", "UP", "selectPrevious", "KP_UP", "selectPrevious"}));
        DEFAULT_VALUES.put("Desktop.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[]{"ctrl F5", "restore", "ctrl F4", "close", "ctrl F7", "move", "ctrl F8", "resize", "RIGHT", JSplitPane.RIGHT, "KP_RIGHT", JSplitPane.RIGHT, "shift RIGHT", "shrinkRight", "shift KP_RIGHT", "shrinkRight", "LEFT", JSplitPane.LEFT, "KP_LEFT", JSplitPane.LEFT, "shift LEFT", "shrinkLeft", "shift KP_LEFT", "shrinkLeft", "UP", "up", "KP_UP", "up", "shift UP", "shrinkUp", "shift KP_UP", "shrinkUp", "DOWN", "down", "KP_DOWN", "down", "shift DOWN", "shrinkDown", "shift KP_DOWN", "shrinkDown", "ESCAPE", "escape", "ctrl F9", "minimize", "ctrl F10", "maximize", "ctrl F6", "selectNextFrame", "ctrl TAB", "selectNextFrame", "ctrl alt F6", "selectNextFrame", "shift ctrl alt F6", "selectPreviousFrame", "ctrl F12", "navigateNext", "shift ctrl F12", "navigatePrevious"}));
        DEFAULT_VALUES.put("FileChooser.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[]{"ESCAPE", FilePane.ACTION_CANCEL, "F2", FilePane.ACTION_EDIT_FILE_NAME, "F5", FilePane.ACTION_REFRESH, "BACK_SPACE", FilePane.ACTION_CHANGE_TO_PARENT_DIRECTORY, "ENTER", FilePane.ACTION_APPROVE_SELECTION, "ctrl ENTER", FilePane.ACTION_APPROVE_SELECTION}));
        DEFAULT_VALUES.put("FormattedTextField.focusInputMap", new UIDefaults.LazyInputMap(new Object[]{"ctrl C", DefaultEditorKit.copyAction, "ctrl V", DefaultEditorKit.pasteAction, "ctrl X", DefaultEditorKit.cutAction, "COPY", DefaultEditorKit.copyAction, "PASTE", DefaultEditorKit.pasteAction, "CUT", DefaultEditorKit.cutAction, "control INSERT", DefaultEditorKit.copyAction, "shift INSERT", DefaultEditorKit.pasteAction, "shift DELETE", DefaultEditorKit.cutAction, "shift LEFT", DefaultEditorKit.selectionBackwardAction, "shift KP_LEFT", DefaultEditorKit.selectionBackwardAction, "shift RIGHT", DefaultEditorKit.selectionForwardAction, "shift KP_RIGHT", DefaultEditorKit.selectionForwardAction, "ctrl LEFT", DefaultEditorKit.previousWordAction, "ctrl KP_LEFT", DefaultEditorKit.previousWordAction, "ctrl RIGHT", DefaultEditorKit.nextWordAction, "ctrl KP_RIGHT", DefaultEditorKit.nextWordAction, "ctrl shift LEFT", DefaultEditorKit.selectionPreviousWordAction, "ctrl shift KP_LEFT", DefaultEditorKit.selectionPreviousWordAction, "ctrl shift RIGHT", DefaultEditorKit.selectionNextWordAction, "ctrl shift KP_RIGHT", DefaultEditorKit.selectionNextWordAction, "ctrl A", DefaultEditorKit.selectAllAction, "HOME", DefaultEditorKit.beginLineAction, "END", DefaultEditorKit.endLineAction, "shift HOME", DefaultEditorKit.selectionBeginLineAction, "shift END", DefaultEditorKit.selectionEndLineAction, "BACK_SPACE", DefaultEditorKit.deletePrevCharAction, "shift BACK_SPACE", DefaultEditorKit.deletePrevCharAction, "ctrl H", DefaultEditorKit.deletePrevCharAction, "DELETE", DefaultEditorKit.deleteNextCharAction, "ctrl DELETE", DefaultEditorKit.deleteNextWordAction, "ctrl BACK_SPACE", DefaultEditorKit.deletePrevWordAction, "RIGHT", DefaultEditorKit.forwardAction, "LEFT", DefaultEditorKit.backwardAction, "KP_RIGHT", DefaultEditorKit.forwardAction, "KP_LEFT", DefaultEditorKit.backwardAction, "ENTER", JTextField.notifyAction, "ctrl BACK_SLASH", "unselect", "control shift O", "toggle-componentOrientation", "ESCAPE", "reset-field-edit", "UP", "increment", "KP_UP", "increment", "DOWN", "decrement", "KP_DOWN", "decrement"}));
        DEFAULT_VALUES.put("InternalFrame.icon", LookAndFeel.makeIcon(BasicLookAndFeel.class, "icons/JavaCup16.png"));
        DEFAULT_VALUES.put("InternalFrame.windowBindings", new Object[]{"shift ESCAPE", "showSystemMenu", "ctrl SPACE", "showSystemMenu", "ESCAPE", "hideSystemMenu"});
        DEFAULT_VALUES.put("List.focusInputMap", new UIDefaults.LazyInputMap(new Object[]{"ctrl C", "copy", "ctrl V", "paste", "ctrl X", "cut", "COPY", "copy", "PASTE", "paste", "CUT", "cut", "control INSERT", "copy", "shift INSERT", "paste", "shift DELETE", "cut", "UP", "selectPreviousRow", "KP_UP", "selectPreviousRow", "shift UP", "selectPreviousRowExtendSelection", "shift KP_UP", "selectPreviousRowExtendSelection", "ctrl shift UP", "selectPreviousRowExtendSelection", "ctrl shift KP_UP", "selectPreviousRowExtendSelection", "ctrl UP", "selectPreviousRowChangeLead", "ctrl KP_UP", "selectPreviousRowChangeLead", "DOWN", "selectNextRow", "KP_DOWN", "selectNextRow", "shift DOWN", "selectNextRowExtendSelection", "shift KP_DOWN", "selectNextRowExtendSelection", "ctrl shift DOWN", "selectNextRowExtendSelection", "ctrl shift KP_DOWN", "selectNextRowExtendSelection", "ctrl DOWN", "selectNextRowChangeLead", "ctrl KP_DOWN", "selectNextRowChangeLead", "LEFT", "selectPreviousColumn", "KP_LEFT", "selectPreviousColumn", "shift LEFT", "selectPreviousColumnExtendSelection", "shift KP_LEFT", "selectPreviousColumnExtendSelection", "ctrl shift LEFT", "selectPreviousColumnExtendSelection", "ctrl shift KP_LEFT", "selectPreviousColumnExtendSelection", "ctrl LEFT", "selectPreviousColumnChangeLead", "ctrl KP_LEFT", "selectPreviousColumnChangeLead", "RIGHT", "selectNextColumn", "KP_RIGHT", "selectNextColumn", "shift RIGHT", "selectNextColumnExtendSelection", "shift KP_RIGHT", "selectNextColumnExtendSelection", "ctrl shift RIGHT", "selectNextColumnExtendSelection", "ctrl shift KP_RIGHT", "selectNextColumnExtendSelection", "ctrl RIGHT", "selectNextColumnChangeLead", "ctrl KP_RIGHT", "selectNextColumnChangeLead", "HOME", "selectFirstRow", "shift HOME", "selectFirstRowExtendSelection", "ctrl shift HOME", "selectFirstRowExtendSelection", "ctrl HOME", "selectFirstRowChangeLead", "END", "selectLastRow", "shift END", "selectLastRowExtendSelection", "ctrl shift END", "selectLastRowExtendSelection", "ctrl END", "selectLastRowChangeLead", "PAGE_UP", "scrollUp", "shift PAGE_UP", "scrollUpExtendSelection", "ctrl shift PAGE_UP", "scrollUpExtendSelection", "ctrl PAGE_UP", "scrollUpChangeLead", "PAGE_DOWN", "scrollDown", "shift PAGE_DOWN", "scrollDownExtendSelection", "ctrl shift PAGE_DOWN", "scrollDownExtendSelection", "ctrl PAGE_DOWN", "scrollDownChangeLead", "ctrl A", "selectAll", "ctrl SLASH", "selectAll", "ctrl BACK_SLASH", "clearSelection", "SPACE", "addToSelection", "ctrl SPACE", "toggleAndAnchor", "shift SPACE", "extendTo", "ctrl shift SPACE", "moveSelectionTo"}));
        DEFAULT_VALUES.put("List.focusInputMap.RightToLeft", new UIDefaults.LazyInputMap(new Object[]{"LEFT", "selectNextColumn", "KP_LEFT", "selectNextColumn", "shift LEFT", "selectNextColumnExtendSelection", "shift KP_LEFT", "selectNextColumnExtendSelection", "ctrl shift LEFT", "selectNextColumnExtendSelection", "ctrl shift KP_LEFT", "selectNextColumnExtendSelection", "ctrl LEFT", "selectNextColumnChangeLead", "ctrl KP_LEFT", "selectNextColumnChangeLead", "RIGHT", "selectPreviousColumn", "KP_RIGHT", "selectPreviousColumn", "shift RIGHT", "selectPreviousColumnExtendSelection", "shift KP_RIGHT", "selectPreviousColumnExtendSelection", "ctrl shift RIGHT", "selectPreviousColumnExtendSelection", "ctrl shift KP_RIGHT", "selectPreviousColumnExtendSelection", "ctrl RIGHT", "selectPreviousColumnChangeLead", "ctrl KP_RIGHT", "selectPreviousColumnChangeLead"}));
        DEFAULT_VALUES.put("MenuBar.windowBindings", new Object[]{"F10", "takeFocus"});
        DEFAULT_VALUES.put("OptionPane.windowBindings", new Object[]{"ESCAPE", "close"});
        DEFAULT_VALUES.put("RootPane.defaultButtonWindowKeyBindings", new Object[]{"ENTER", BasicRootPaneUI.Actions.PRESS, "released ENTER", BasicRootPaneUI.Actions.RELEASE, "ctrl ENTER", BasicRootPaneUI.Actions.PRESS, "ctrl released ENTER", BasicRootPaneUI.Actions.RELEASE});
        DEFAULT_VALUES.put("RootPane.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[]{"shift F10", BasicRootPaneUI.Actions.POST_POPUP}));
        DEFAULT_VALUES.put("ScrollBar.anecstorInputMap", new UIDefaults.LazyInputMap(new Object[]{"RIGHT", BasicSliderUI.Actions.POSITIVE_UNIT_INCREMENT, "KP_RIGHT", BasicSliderUI.Actions.POSITIVE_UNIT_INCREMENT, "DOWN", BasicSliderUI.Actions.POSITIVE_UNIT_INCREMENT, "KP_DOWN", BasicSliderUI.Actions.POSITIVE_UNIT_INCREMENT, "PAGE_DOWN", BasicSliderUI.Actions.POSITIVE_BLOCK_INCREMENT, "LEFT", BasicSliderUI.Actions.NEGATIVE_UNIT_INCREMENT, "KP_LEFT", BasicSliderUI.Actions.NEGATIVE_UNIT_INCREMENT, "UP", BasicSliderUI.Actions.NEGATIVE_UNIT_INCREMENT, "KP_UP", BasicSliderUI.Actions.NEGATIVE_UNIT_INCREMENT, "PAGE_UP", BasicSliderUI.Actions.NEGATIVE_BLOCK_INCREMENT, "HOME", BasicSliderUI.Actions.MIN_SCROLL_INCREMENT, "END", BasicSliderUI.Actions.MAX_SCROLL_INCREMENT}));
        DEFAULT_VALUES.put("ScrollBar.ancestorInputMap.RightToLeft", new UIDefaults.LazyInputMap(new Object[]{"RIGHT", BasicSliderUI.Actions.NEGATIVE_UNIT_INCREMENT, "KP_RIGHT", BasicSliderUI.Actions.NEGATIVE_UNIT_INCREMENT, "LEFT", BasicSliderUI.Actions.POSITIVE_UNIT_INCREMENT, "KP_LEFT", BasicSliderUI.Actions.POSITIVE_UNIT_INCREMENT}));
        DEFAULT_VALUES.put("ScrollPane.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[]{"RIGHT", "unitScrollRight", "KP_RIGHT", "unitScrollRight", "DOWN", "unitScrollDown", "KP_DOWN", "unitScrollDown", "LEFT", "unitScrollLeft", "KP_LEFT", "unitScrollLeft", "UP", "unitScrollUp", "KP_UP", "unitScrollUp", "PAGE_UP", "scrollUp", "PAGE_DOWN", "scrollDown", "ctrl PAGE_UP", "scrollLeft", "ctrl PAGE_DOWN", "scrollRight", "ctrl HOME", "scrollHome", "ctrl END", "scrollEnd"}));
        DEFAULT_VALUES.put("ScrollPane.ancestorInputMap.RightToLeft", new UIDefaults.LazyInputMap(new Object[]{"ctrl PAGE_UP", "scrollRight", "ctrl PAGE_DOWN", "scrollLeft"}));
        DEFAULT_VALUES.put("SplitPane.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[]{"UP", "negativeIncrement", "DOWN", "positiveIncrement", "LEFT", "negativeIncrement", "RIGHT", "positiveIncrement", "KP_UP", "negativeIncrement", "KP_DOWN", "positiveIncrement", "KP_LEFT", "negativeIncrement", "KP_RIGHT", "positiveIncrement", "HOME", "selectMin", "END", "selectMax", "F8", "startResize", "F6", "toggleFocus", "ctrl TAB", "focusOutForward", "ctrl shift TAB", "focusOutBackward"}));
        DEFAULT_VALUES.put("Spinner.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[]{"UP", "increment", "KP_UP", "increment", "DOWN", "decrement", "KP_DOWN", "decrement"}));
        DEFAULT_VALUES.put("Slider.focusInputMap", new UIDefaults.LazyInputMap(new Object[]{"RIGHT", BasicSliderUI.Actions.POSITIVE_UNIT_INCREMENT, "KP_RIGHT", BasicSliderUI.Actions.POSITIVE_UNIT_INCREMENT, "DOWN", BasicSliderUI.Actions.NEGATIVE_UNIT_INCREMENT, "KP_DOWN", BasicSliderUI.Actions.NEGATIVE_UNIT_INCREMENT, "PAGE_DOWN", BasicSliderUI.Actions.NEGATIVE_BLOCK_INCREMENT, "ctrl PAGE_DOWN", BasicSliderUI.Actions.NEGATIVE_BLOCK_INCREMENT, "LEFT", BasicSliderUI.Actions.NEGATIVE_UNIT_INCREMENT, "KP_LEFT", BasicSliderUI.Actions.NEGATIVE_UNIT_INCREMENT, "UP", BasicSliderUI.Actions.POSITIVE_UNIT_INCREMENT, "KP_UP", BasicSliderUI.Actions.POSITIVE_UNIT_INCREMENT, "PAGE_UP", BasicSliderUI.Actions.POSITIVE_BLOCK_INCREMENT, "ctrl PAGE_UP", BasicSliderUI.Actions.POSITIVE_BLOCK_INCREMENT, "HOME", BasicSliderUI.Actions.MIN_SCROLL_INCREMENT, "END", BasicSliderUI.Actions.MAX_SCROLL_INCREMENT}));
        DEFAULT_VALUES.put("Slider.focusInputMap.RightToLeft", new UIDefaults.LazyInputMap(new Object[]{"RIGHT", BasicSliderUI.Actions.NEGATIVE_UNIT_INCREMENT, "KP_RIGHT", BasicSliderUI.Actions.NEGATIVE_UNIT_INCREMENT, "LEFT", BasicSliderUI.Actions.POSITIVE_UNIT_INCREMENT, "KP_LEFT", BasicSliderUI.Actions.POSITIVE_UNIT_INCREMENT}));
        DEFAULT_VALUES.put("TabbedPane.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[]{"ctrl PAGE_DOWN", "navigatePageDown", "ctrl PAGE_UP", "navigatePageUp", "ctrl UP", "requestFocus", "ctrl KP_UP", "requestFocus"}));
        DEFAULT_VALUES.put("TabbedPane.focusInputMap", new UIDefaults.LazyInputMap(new Object[]{"RIGHT", "navigateRight", "KP_RIGHT", "navigateRight", "LEFT", "navigateLeft", "KP_LEFT", "navigateLeft", "UP", "navigateUp", "KP_UP", "navigateUp", "DOWN", "navigateDown", "KP_DOWN", "navigateDown", "ctrl DOWN", "requestFocusForVisibleComponent", "ctrl KP_DOWN", "requestFocusForVisibleComponent"}));
        DEFAULT_VALUES.put("Table.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[]{"ctrl C", "copy", "ctrl V", "paste", "ctrl X", "cut", "COPY", "copy", "PASTE", "paste", "CUT", "cut", "control INSERT", "copy", "shift INSERT", "paste", "shift DELETE", "cut", "RIGHT", "selectNextColumn", "KP_RIGHT", "selectNextColumn", "shift RIGHT", "selectNextColumnExtendSelection", "shift KP_RIGHT", "selectNextColumnExtendSelection", "ctrl shift RIGHT", "selectNextColumnExtendSelection", "ctrl shift KP_RIGHT", "selectNextColumnExtendSelection", "ctrl RIGHT", "selectNextColumnChangeLead", "ctrl KP_RIGHT", "selectNextColumnChangeLead", "LEFT", "selectPreviousColumn", "KP_LEFT", "selectPreviousColumn", "shift LEFT", "selectPreviousColumnExtendSelection", "shift KP_LEFT", "selectPreviousColumnExtendSelection", "ctrl shift LEFT", "selectPreviousColumnExtendSelection", "ctrl shift KP_LEFT", "selectPreviousColumnExtendSelection", "ctrl LEFT", "selectPreviousColumnChangeLead", "ctrl KP_LEFT", "selectPreviousColumnChangeLead", "DOWN", "selectNextRow", "KP_DOWN", "selectNextRow", "shift DOWN", "selectNextRowExtendSelection", "shift KP_DOWN", "selectNextRowExtendSelection", "ctrl shift DOWN", "selectNextRowExtendSelection", "ctrl shift KP_DOWN", "selectNextRowExtendSelection", "ctrl DOWN", "selectNextRowChangeLead", "ctrl KP_DOWN", "selectNextRowChangeLead", "UP", "selectPreviousRow", "KP_UP", "selectPreviousRow", "shift UP", "selectPreviousRowExtendSelection", "shift KP_UP", "selectPreviousRowExtendSelection", "ctrl shift UP", "selectPreviousRowExtendSelection", "ctrl shift KP_UP", "selectPreviousRowExtendSelection", "ctrl UP", "selectPreviousRowChangeLead", "ctrl KP_UP", "selectPreviousRowChangeLead", "HOME", "selectFirstColumn", "shift HOME", "selectFirstColumnExtendSelection", "ctrl shift HOME", "selectFirstRowExtendSelection", "ctrl HOME", "selectFirstRow", "END", "selectLastColumn", "shift END", "selectLastColumnExtendSelection", "ctrl shift END", "selectLastRowExtendSelection", "ctrl END", "selectLastRow", "PAGE_UP", "scrollUpChangeSelection", "shift PAGE_UP", "scrollUpExtendSelection", "ctrl shift PAGE_UP", "scrollLeftExtendSelection", "ctrl PAGE_UP", "scrollLeftChangeSelection", "PAGE_DOWN", "scrollDownChangeSelection", "shift PAGE_DOWN", "scrollDownExtendSelection", "ctrl shift PAGE_DOWN", "scrollRightExtendSelection", "ctrl PAGE_DOWN", "scrollRightChangeSelection", "TAB", "selectNextColumnCell", "shift TAB", "selectPreviousColumnCell", "ENTER", "selectNextRowCell", "shift ENTER", "selectPreviousRowCell", "ctrl A", "selectAll", "ctrl SLASH", "selectAll", "ctrl BACK_SLASH", "clearSelection", "ESCAPE", "cancel", "F2", "startEditing", "SPACE", "addToSelection", "ctrl SPACE", "toggleAndAnchor", "shift SPACE", "extendTo", "ctrl shift SPACE", "moveSelectionTo", "F8", "focusHeader"}));
        DEFAULT_VALUES.put("TableHeader.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[]{"SPACE", BasicTableHeaderUI.Actions.TOGGLE_SORT_ORDER, "LEFT", BasicTableHeaderUI.Actions.SELECT_COLUMN_TO_LEFT, "KP_LEFT", BasicTableHeaderUI.Actions.SELECT_COLUMN_TO_LEFT, "RIGHT", BasicTableHeaderUI.Actions.SELECT_COLUMN_TO_RIGHT, "KP_RIGHT", BasicTableHeaderUI.Actions.SELECT_COLUMN_TO_RIGHT, "alt LEFT", BasicTableHeaderUI.Actions.MOVE_COLUMN_LEFT, "alt KP_LEFT", BasicTableHeaderUI.Actions.MOVE_COLUMN_LEFT, "alt RIGHT", BasicTableHeaderUI.Actions.MOVE_COLUMN_RIGHT, "alt KP_RIGHT", BasicTableHeaderUI.Actions.MOVE_COLUMN_RIGHT, "alt shift LEFT", BasicTableHeaderUI.Actions.RESIZE_LEFT, "alt shift KP_LEFT", BasicTableHeaderUI.Actions.RESIZE_LEFT, "alt shift RIGHT", BasicTableHeaderUI.Actions.RESIZE_RIGHT, "alt shift KP_RIGHT", BasicTableHeaderUI.Actions.RESIZE_RIGHT, "ESCAPE", BasicTableHeaderUI.Actions.FOCUS_TABLE}));
        DEFAULT_VALUES.put("Tree.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[]{"ESCAPE", "cancel"}));
        DEFAULT_VALUES.put("Tree.focusInputMap", new UIDefaults.LazyInputMap(new Object[]{"ADD", "expand", "SUBTRACT", SchemaSymbols.ATTVAL_COLLAPSE, "ctrl C", "copy", "ctrl V", "paste", "ctrl X", "cut", "COPY", "copy", "PASTE", "paste", "CUT", "cut", "control INSERT", "copy", "shift INSERT", "paste", "shift DELETE", "cut", "UP", "selectPrevious", "KP_UP", "selectPrevious", "shift UP", "selectPreviousExtendSelection", "shift KP_UP", "selectPreviousExtendSelection", "ctrl shift UP", "selectPreviousExtendSelection", "ctrl shift KP_UP", "selectPreviousExtendSelection", "ctrl UP", "selectPreviousChangeLead", "ctrl KP_UP", "selectPreviousChangeLead", "DOWN", "selectNext", "KP_DOWN", "selectNext", "shift DOWN", "selectNextExtendSelection", "shift KP_DOWN", "selectNextExtendSelection", "ctrl shift DOWN", "selectNextExtendSelection", "ctrl shift KP_DOWN", "selectNextExtendSelection", "ctrl DOWN", "selectNextChangeLead", "ctrl KP_DOWN", "selectNextChangeLead", "RIGHT", "selectChild", "KP_RIGHT", "selectChild", "LEFT", "selectParent", "KP_LEFT", "selectParent", "PAGE_UP", "scrollUpChangeSelection", "shift PAGE_UP", "scrollUpExtendSelection", "ctrl shift PAGE_UP", "scrollUpExtendSelection", "ctrl PAGE_UP", "scrollUpChangeLead", "PAGE_DOWN", "scrollDownChangeSelection", "shift PAGE_DOWN", "scrollDownExtendSelection", "ctrl shift PAGE_DOWN", "scrollDownExtendSelection", "ctrl PAGE_DOWN", "scrollDownChangeLead", "HOME", "selectFirst", "shift HOME", "selectFirstExtendSelection", "ctrl shift HOME", "selectFirstExtendSelection", "ctrl HOME", "selectFirstChangeLead", "END", "selectLast", "shift END", "selectLastExtendSelection", "ctrl shift END", "selectLastExtendSelection", "ctrl END", "selectLastChangeLead", "F2", "startEditing", "ctrl A", "selectAll", "ctrl SLASH", "selectAll", "ctrl BACK_SLASH", "clearSelection", "ctrl LEFT", "scrollLeft", "ctrl KP_LEFT", "scrollLeft", "ctrl RIGHT", "scrollRight", "ctrl KP_RIGHT", "scrollRight", "SPACE", "addToSelection", "ctrl SPACE", "toggleAndAnchor", "shift SPACE", "extendTo", "ctrl shift SPACE", "moveSelectionTo"}));
        DEFAULT_VALUES.put("Tree.focusInputMap.RightToLeft", new UIDefaults.LazyInputMap(new Object[]{"RIGHT", "selectParent", "KP_RIGHT", "selectParent", "LEFT", "selectChild", "KP_LEFT", "selectChild"}));
    }

    private static Object getDefaultValue(Object obj) {
        Object obj2;
        synchronized (SynthStyle.class) {
            if (DEFAULT_VALUES == null) {
                DEFAULT_VALUES = new HashMap();
                populateDefaultValues();
            }
            Object objCreateValue = DEFAULT_VALUES.get(obj);
            if (objCreateValue instanceof UIDefaults.LazyValue) {
                objCreateValue = ((UIDefaults.LazyValue) objCreateValue).createValue(null);
                DEFAULT_VALUES.put(obj, objCreateValue);
            }
            obj2 = objCreateValue;
        }
        return obj2;
    }

    public SynthGraphicsUtils getGraphicsUtils(SynthContext synthContext) {
        return SYNTH_GRAPHICS;
    }

    public Color getColor(SynthContext synthContext, ColorType colorType) {
        JComponent component = synthContext.getComponent();
        Region region = synthContext.getRegion();
        if ((synthContext.getComponentState() & 8) != 0) {
            if (component instanceof JTextComponent) {
                Color disabledTextColor = ((JTextComponent) component).getDisabledTextColor();
                if (disabledTextColor == null || (disabledTextColor instanceof UIResource)) {
                    return getColorForState(synthContext, colorType);
                }
            } else if ((component instanceof JLabel) && (colorType == ColorType.FOREGROUND || colorType == ColorType.TEXT_FOREGROUND)) {
                return getColorForState(synthContext, colorType);
            }
        }
        Color colorForState = null;
        if (!region.isSubregion()) {
            if (colorType == ColorType.BACKGROUND) {
                colorForState = component.getBackground();
            } else if (colorType == ColorType.FOREGROUND || colorType == ColorType.TEXT_FOREGROUND) {
                colorForState = component.getForeground();
            }
        }
        if (colorForState == null || (colorForState instanceof UIResource)) {
            colorForState = getColorForState(synthContext, colorType);
        }
        if (colorForState == null) {
            if (colorType == ColorType.BACKGROUND || colorType == ColorType.TEXT_BACKGROUND) {
                return component.getBackground();
            }
            if (colorType == ColorType.FOREGROUND || colorType == ColorType.TEXT_FOREGROUND) {
                return component.getForeground();
            }
        }
        return colorForState;
    }

    public Font getFont(SynthContext synthContext) {
        JComponent component = synthContext.getComponent();
        if (synthContext.getComponentState() == 1) {
            return component.getFont();
        }
        Font font = component.getFont();
        if (font != null && !(font instanceof UIResource)) {
            return font;
        }
        return getFontForState(synthContext);
    }

    public Insets getInsets(SynthContext synthContext, Insets insets) {
        if (insets == null) {
            insets = new Insets(0, 0, 0, 0);
        }
        insets.right = 0;
        insets.left = 0;
        insets.bottom = 0;
        insets.top = 0;
        return insets;
    }

    public SynthPainter getPainter(SynthContext synthContext) {
        return null;
    }

    public boolean isOpaque(SynthContext synthContext) {
        return true;
    }

    public Object get(SynthContext synthContext, Object obj) {
        return getDefaultValue(obj);
    }

    void installDefaults(SynthContext synthContext, SynthUI synthUI) {
        JComponent component;
        Border border;
        if (!synthContext.isSubregion() && ((border = (component = synthContext.getComponent()).getBorder()) == null || (border instanceof UIResource))) {
            component.setBorder(new SynthBorder(synthUI, getInsets(synthContext, null)));
        }
        installDefaults(synthContext);
    }

    public void installDefaults(SynthContext synthContext) {
        if (!synthContext.isSubregion()) {
            JComponent component = synthContext.getComponent();
            synthContext.getRegion();
            Font font = component.getFont();
            if (font == null || (font instanceof UIResource)) {
                component.setFont(getFontForState(synthContext));
            }
            Color background = component.getBackground();
            if (background == null || (background instanceof UIResource)) {
                component.setBackground(getColorForState(synthContext, ColorType.BACKGROUND));
            }
            Color foreground = component.getForeground();
            if (foreground == null || (foreground instanceof UIResource)) {
                component.setForeground(getColorForState(synthContext, ColorType.FOREGROUND));
            }
            LookAndFeel.installProperty(component, "opaque", Boolean.valueOf(isOpaque(synthContext)));
        }
    }

    public void uninstallDefaults(SynthContext synthContext) {
        if (!synthContext.isSubregion()) {
            JComponent component = synthContext.getComponent();
            if (component.getBorder() instanceof UIResource) {
                component.setBorder(null);
            }
        }
    }

    public int getInt(SynthContext synthContext, Object obj, int i2) {
        Object obj2 = get(synthContext, obj);
        if (obj2 instanceof Number) {
            return ((Number) obj2).intValue();
        }
        return i2;
    }

    public boolean getBoolean(SynthContext synthContext, Object obj, boolean z2) {
        Object obj2 = get(synthContext, obj);
        if (obj2 instanceof Boolean) {
            return ((Boolean) obj2).booleanValue();
        }
        return z2;
    }

    public Icon getIcon(SynthContext synthContext, Object obj) {
        Object obj2 = get(synthContext, obj);
        if (obj2 instanceof Icon) {
            return (Icon) obj2;
        }
        return null;
    }

    public String getString(SynthContext synthContext, Object obj, String str) {
        Object obj2 = get(synthContext, obj);
        if (obj2 instanceof String) {
            return (String) obj2;
        }
        return str;
    }
}
