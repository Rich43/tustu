package javax.swing.plaf.basic;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import java.awt.Color;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.plaf.ActionMapUIResource;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicTextUI;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.JTextComponent;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.StyleSheet;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:javax/swing/plaf/basic/BasicEditorPaneUI.class */
public class BasicEditorPaneUI extends BasicTextUI {
    private static final String FONT_ATTRIBUTE_KEY = "FONT_ATTRIBUTE_KEY";

    public static ComponentUI createUI(JComponent jComponent) {
        return new BasicEditorPaneUI();
    }

    @Override // javax.swing.plaf.basic.BasicTextUI
    protected String getPropertyPrefix() {
        return "EditorPane";
    }

    @Override // javax.swing.plaf.basic.BasicTextUI, javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        super.installUI(jComponent);
        updateDisplayProperties(jComponent.getFont(), jComponent.getForeground());
    }

    @Override // javax.swing.plaf.basic.BasicTextUI, javax.swing.plaf.ComponentUI
    public void uninstallUI(JComponent jComponent) {
        cleanDisplayProperties();
        super.uninstallUI(jComponent);
    }

    @Override // javax.swing.plaf.basic.BasicTextUI, javax.swing.plaf.TextUI
    public EditorKit getEditorKit(JTextComponent jTextComponent) {
        return ((JEditorPane) getComponent()).getEditorKit();
    }

    @Override // javax.swing.plaf.basic.BasicTextUI
    ActionMap getActionMap() {
        Action[] actions;
        ActionMapUIResource actionMapUIResource = new ActionMapUIResource();
        actionMapUIResource.put("requestFocus", new BasicTextUI.FocusAction());
        EditorKit editorKit = getEditorKit(getComponent());
        if (editorKit != null && (actions = editorKit.getActions()) != null) {
            addActions(actionMapUIResource, actions);
        }
        actionMapUIResource.put(TransferHandler.getCutAction().getValue("Name"), TransferHandler.getCutAction());
        actionMapUIResource.put(TransferHandler.getCopyAction().getValue("Name"), TransferHandler.getCopyAction());
        actionMapUIResource.put(TransferHandler.getPasteAction().getValue("Name"), TransferHandler.getPasteAction());
        return actionMapUIResource;
    }

    @Override // javax.swing.plaf.basic.BasicTextUI
    protected void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        Action[] actions;
        Action[] actions2;
        super.propertyChange(propertyChangeEvent);
        String propertyName = propertyChangeEvent.getPropertyName();
        if ("editorKit".equals(propertyName)) {
            ActionMap uIActionMap = SwingUtilities.getUIActionMap(getComponent());
            if (uIActionMap != null) {
                Object oldValue = propertyChangeEvent.getOldValue();
                if ((oldValue instanceof EditorKit) && (actions2 = ((EditorKit) oldValue).getActions()) != null) {
                    removeActions(uIActionMap, actions2);
                }
                Object newValue = propertyChangeEvent.getNewValue();
                if ((newValue instanceof EditorKit) && (actions = ((EditorKit) newValue).getActions()) != null) {
                    addActions(uIActionMap, actions);
                }
            }
            updateFocusTraversalKeys();
            return;
        }
        if (JTree.EDITABLE_PROPERTY.equals(propertyName)) {
            updateFocusTraversalKeys();
            return;
        }
        if ("foreground".equals(propertyName) || "font".equals(propertyName) || Constants.DOCUMENT_PNAME.equals(propertyName) || JEditorPane.W3C_LENGTH_UNITS.equals(propertyName) || JEditorPane.HONOR_DISPLAY_PROPERTIES.equals(propertyName)) {
            JTextComponent component = getComponent();
            updateDisplayProperties(component.getFont(), component.getForeground());
            if (JEditorPane.W3C_LENGTH_UNITS.equals(propertyName) || JEditorPane.HONOR_DISPLAY_PROPERTIES.equals(propertyName)) {
                modelChanged();
            }
            if ("foreground".equals(propertyName)) {
                Object clientProperty = component.getClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES);
                boolean zBooleanValue = false;
                if (clientProperty instanceof Boolean) {
                    zBooleanValue = ((Boolean) clientProperty).booleanValue();
                }
                if (zBooleanValue) {
                    modelChanged();
                }
            }
        }
    }

    void removeActions(ActionMap actionMap, Action[] actionArr) {
        for (Action action : actionArr) {
            actionMap.remove(action.getValue("Name"));
        }
    }

    void addActions(ActionMap actionMap, Action[] actionArr) {
        for (Action action : actionArr) {
            actionMap.put(action.getValue("Name"), action);
        }
    }

    void updateDisplayProperties(Font font, Color color) {
        JTextComponent component = getComponent();
        Object clientProperty = component.getClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES);
        boolean zBooleanValue = false;
        Object clientProperty2 = component.getClientProperty(JEditorPane.W3C_LENGTH_UNITS);
        boolean zBooleanValue2 = false;
        if (clientProperty instanceof Boolean) {
            zBooleanValue = ((Boolean) clientProperty).booleanValue();
        }
        if (clientProperty2 instanceof Boolean) {
            zBooleanValue2 = ((Boolean) clientProperty2).booleanValue();
        }
        if ((this instanceof BasicTextPaneUI) || zBooleanValue) {
            Document document = getComponent().getDocument();
            if (document instanceof StyledDocument) {
                if ((document instanceof HTMLDocument) && zBooleanValue) {
                    updateCSS(font, color);
                } else {
                    updateStyle(font, color);
                }
            }
        } else {
            cleanDisplayProperties();
        }
        if (zBooleanValue2) {
            Document document2 = getComponent().getDocument();
            if (document2 instanceof HTMLDocument) {
                ((HTMLDocument) document2).getStyleSheet().addRule("W3C_LENGTH_UNITS_ENABLE");
                return;
            }
            return;
        }
        Document document3 = getComponent().getDocument();
        if (document3 instanceof HTMLDocument) {
            ((HTMLDocument) document3).getStyleSheet().addRule("W3C_LENGTH_UNITS_DISABLE");
        }
    }

    void cleanDisplayProperties() {
        Document document = getComponent().getDocument();
        if (document instanceof HTMLDocument) {
            StyleSheet styleSheet = ((HTMLDocument) document).getStyleSheet();
            StyleSheet[] styleSheets = styleSheet.getStyleSheets();
            if (styleSheets != null) {
                int length = styleSheets.length;
                int i2 = 0;
                while (true) {
                    if (i2 >= length) {
                        break;
                    }
                    StyleSheet styleSheet2 = styleSheets[i2];
                    if (!(styleSheet2 instanceof StyleSheetUIResource)) {
                        i2++;
                    } else {
                        styleSheet.removeStyleSheet(styleSheet2);
                        styleSheet.addRule("BASE_SIZE_DISABLE");
                        break;
                    }
                }
            }
            Style style = ((StyledDocument) document).getStyle("default");
            if (style.getAttribute(FONT_ATTRIBUTE_KEY) != null) {
                style.removeAttribute(FONT_ATTRIBUTE_KEY);
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicEditorPaneUI$StyleSheetUIResource.class */
    static class StyleSheetUIResource extends StyleSheet implements UIResource {
        StyleSheetUIResource() {
        }
    }

    private void updateCSS(Font font, Color color) {
        JTextComponent component = getComponent();
        Document document = component.getDocument();
        if (document instanceof HTMLDocument) {
            StyleSheetUIResource styleSheetUIResource = new StyleSheetUIResource();
            StyleSheet styleSheet = ((HTMLDocument) document).getStyleSheet();
            StyleSheet[] styleSheets = styleSheet.getStyleSheets();
            if (styleSheets != null) {
                for (StyleSheet styleSheet2 : styleSheets) {
                    if (styleSheet2 instanceof StyleSheetUIResource) {
                        styleSheet.removeStyleSheet(styleSheet2);
                    }
                }
            }
            styleSheetUIResource.addRule(SwingUtilities2.displayPropertiesToCSS(font, color));
            styleSheet.addStyleSheet(styleSheetUIResource);
            styleSheet.addRule("BASE_SIZE " + component.getFont().getSize());
            Style style = ((StyledDocument) document).getStyle("default");
            if (!font.equals(style.getAttribute(FONT_ATTRIBUTE_KEY))) {
                style.addAttribute(FONT_ATTRIBUTE_KEY, font);
            }
        }
    }

    private void updateStyle(Font font, Color color) {
        updateFont(font);
        updateForeground(color);
    }

    private void updateForeground(Color color) {
        Style style = ((StyledDocument) getComponent().getDocument()).getStyle("default");
        if (style == null) {
            return;
        }
        if (color == null) {
            if (style.getAttribute(StyleConstants.Foreground) != null) {
                style.removeAttribute(StyleConstants.Foreground);
            }
        } else if (!color.equals(StyleConstants.getForeground(style))) {
            StyleConstants.setForeground(style, color);
        }
    }

    private void updateFont(Font font) {
        Style style = ((StyledDocument) getComponent().getDocument()).getStyle("default");
        if (style == null) {
            return;
        }
        String str = (String) style.getAttribute(StyleConstants.FontFamily);
        Integer num = (Integer) style.getAttribute(StyleConstants.FontSize);
        Boolean bool = (Boolean) style.getAttribute(StyleConstants.Bold);
        Boolean bool2 = (Boolean) style.getAttribute(StyleConstants.Italic);
        Font font2 = (Font) style.getAttribute(FONT_ATTRIBUTE_KEY);
        if (font == null) {
            if (str != null) {
                style.removeAttribute(StyleConstants.FontFamily);
            }
            if (num != null) {
                style.removeAttribute(StyleConstants.FontSize);
            }
            if (bool != null) {
                style.removeAttribute(StyleConstants.Bold);
            }
            if (bool2 != null) {
                style.removeAttribute(StyleConstants.Italic);
            }
            if (font2 != null) {
                style.removeAttribute(FONT_ATTRIBUTE_KEY);
                return;
            }
            return;
        }
        if (!font.getName().equals(str)) {
            StyleConstants.setFontFamily(style, font.getName());
        }
        if (num == null || num.intValue() != font.getSize()) {
            StyleConstants.setFontSize(style, font.getSize());
        }
        if (bool == null || bool.booleanValue() != font.isBold()) {
            StyleConstants.setBold(style, font.isBold());
        }
        if (bool2 == null || bool2.booleanValue() != font.isItalic()) {
            StyleConstants.setItalic(style, font.isItalic());
        }
        if (!font.equals(font2)) {
            style.addAttribute(FONT_ATTRIBUTE_KEY, font);
        }
    }
}
