package javax.swing.plaf.metal;

import javax.swing.UIDefaults;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.basic.BasicBorders;

/* loaded from: rt.jar:javax/swing/plaf/metal/MetalHighContrastTheme.class */
class MetalHighContrastTheme extends DefaultMetalTheme {
    private static final ColorUIResource primary1 = new ColorUIResource(0, 0, 0);
    private static final ColorUIResource primary2 = new ColorUIResource(204, 204, 204);
    private static final ColorUIResource primary3 = new ColorUIResource(255, 255, 255);
    private static final ColorUIResource primaryHighlight = new ColorUIResource(102, 102, 102);
    private static final ColorUIResource secondary2 = new ColorUIResource(204, 204, 204);
    private static final ColorUIResource secondary3 = new ColorUIResource(255, 255, 255);
    private static final ColorUIResource controlHighlight = new ColorUIResource(102, 102, 102);

    MetalHighContrastTheme() {
    }

    @Override // javax.swing.plaf.metal.DefaultMetalTheme, javax.swing.plaf.metal.MetalTheme
    public String getName() {
        return "Contrast";
    }

    @Override // javax.swing.plaf.metal.DefaultMetalTheme, javax.swing.plaf.metal.MetalTheme
    protected ColorUIResource getPrimary1() {
        return primary1;
    }

    @Override // javax.swing.plaf.metal.DefaultMetalTheme, javax.swing.plaf.metal.MetalTheme
    protected ColorUIResource getPrimary2() {
        return primary2;
    }

    @Override // javax.swing.plaf.metal.DefaultMetalTheme, javax.swing.plaf.metal.MetalTheme
    protected ColorUIResource getPrimary3() {
        return primary3;
    }

    @Override // javax.swing.plaf.metal.MetalTheme
    public ColorUIResource getPrimaryControlHighlight() {
        return primaryHighlight;
    }

    @Override // javax.swing.plaf.metal.DefaultMetalTheme, javax.swing.plaf.metal.MetalTheme
    protected ColorUIResource getSecondary2() {
        return secondary2;
    }

    @Override // javax.swing.plaf.metal.DefaultMetalTheme, javax.swing.plaf.metal.MetalTheme
    protected ColorUIResource getSecondary3() {
        return secondary3;
    }

    @Override // javax.swing.plaf.metal.MetalTheme
    public ColorUIResource getControlHighlight() {
        return secondary2;
    }

    @Override // javax.swing.plaf.metal.MetalTheme
    public ColorUIResource getFocusColor() {
        return getBlack();
    }

    @Override // javax.swing.plaf.metal.MetalTheme
    public ColorUIResource getTextHighlightColor() {
        return getBlack();
    }

    @Override // javax.swing.plaf.metal.MetalTheme
    public ColorUIResource getHighlightedTextColor() {
        return getWhite();
    }

    @Override // javax.swing.plaf.metal.MetalTheme
    public ColorUIResource getMenuSelectedBackground() {
        return getBlack();
    }

    @Override // javax.swing.plaf.metal.MetalTheme
    public ColorUIResource getMenuSelectedForeground() {
        return getWhite();
    }

    @Override // javax.swing.plaf.metal.MetalTheme
    public ColorUIResource getAcceleratorForeground() {
        return getBlack();
    }

    @Override // javax.swing.plaf.metal.MetalTheme
    public ColorUIResource getAcceleratorSelectedForeground() {
        return getWhite();
    }

    @Override // javax.swing.plaf.metal.MetalTheme
    public void addCustomEntriesToTable(UIDefaults uIDefaults) {
        BorderUIResource borderUIResource = new BorderUIResource(new LineBorder(getBlack()));
        new BorderUIResource(new LineBorder(getWhite()));
        Object borderUIResource2 = new BorderUIResource(new CompoundBorder(borderUIResource, new BasicBorders.MarginBorder()));
        uIDefaults.putDefaults(new Object[]{"ToolTip.border", borderUIResource, "TitledBorder.border", borderUIResource, "TextField.border", borderUIResource2, "PasswordField.border", borderUIResource2, "TextArea.border", borderUIResource2, "TextPane.border", borderUIResource2, "EditorPane.border", borderUIResource2, "ComboBox.background", getWindowBackground(), "ComboBox.foreground", getUserTextColor(), "ComboBox.selectionBackground", getTextHighlightColor(), "ComboBox.selectionForeground", getHighlightedTextColor(), "ProgressBar.foreground", getUserTextColor(), "ProgressBar.background", getWindowBackground(), "ProgressBar.selectionForeground", getWindowBackground(), "ProgressBar.selectionBackground", getUserTextColor(), "OptionPane.errorDialog.border.background", getPrimary1(), "OptionPane.errorDialog.titlePane.foreground", getPrimary3(), "OptionPane.errorDialog.titlePane.background", getPrimary1(), "OptionPane.errorDialog.titlePane.shadow", getPrimary2(), "OptionPane.questionDialog.border.background", getPrimary1(), "OptionPane.questionDialog.titlePane.foreground", getPrimary3(), "OptionPane.questionDialog.titlePane.background", getPrimary1(), "OptionPane.questionDialog.titlePane.shadow", getPrimary2(), "OptionPane.warningDialog.border.background", getPrimary1(), "OptionPane.warningDialog.titlePane.foreground", getPrimary3(), "OptionPane.warningDialog.titlePane.background", getPrimary1(), "OptionPane.warningDialog.titlePane.shadow", getPrimary2()});
    }

    @Override // javax.swing.plaf.metal.DefaultMetalTheme, javax.swing.plaf.metal.MetalTheme
    boolean isSystemTheme() {
        return getClass() == MetalHighContrastTheme.class;
    }
}
