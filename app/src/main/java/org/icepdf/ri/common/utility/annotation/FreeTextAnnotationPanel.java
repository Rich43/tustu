package org.icepdf.ri.common.utility.annotation;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.rtf.RTFGenerator;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.pobjects.annotations.FreeTextAnnotation;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.views.AnnotationComponent;
import org.icepdf.ri.common.views.annotations.FreeTextAnnotationComponent;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/utility/annotation/FreeTextAnnotationPanel.class */
public class FreeTextAnnotationPanel extends AnnotationPanelAdapter implements ItemListener, ActionListener {
    private static final int DEFAULT_FONT_SIZE = 5;
    private static final int DEFAULT_FONT_FAMILY = 0;
    public static final int DEFAULT_STROKE_THICKNESS_STYLE = 0;
    public static final int DEFAULT_STROKE_STYLE = 0;
    public static final int DEFAULT_FILL_STYLE = 0;
    private static ValueLabelItem[] FONT_NAMES_LIST;
    private static ValueLabelItem[] FONT_SIZES_LIST;
    private FreeTextAnnotation freeTextAnnotation;
    private JComboBox fontNameBox;
    private JComboBox fontSizeBox;
    private JButton fontColorButton;
    private JComboBox fillTypeBox;
    private JButton fillColorButton;
    private JComboBox strokeTypeBox;
    private JComboBox strokeThicknessBox;
    private JComboBox strokeStyleBox;
    private JButton strokeColorButton;
    private static final Color DEFAULT_FONT_COLOR = Color.DARK_GRAY;
    private static final Color DEFAULT_BORDER_COLOR = Color.LIGHT_GRAY;
    private static final Color DEFAULT_STROKE_COLOR = new Color(1, 1, 1);

    public FreeTextAnnotationPanel(SwingController controller) {
        super(controller);
        setLayout(new GridLayout(9, 2, 5, 2));
        setFocusable(true);
        createGUI();
        setEnabled(false);
        revalidate();
    }

    @Override // org.icepdf.ri.common.utility.annotation.AnnotationProperties
    public void setAnnotationComponent(AnnotationComponent newAnnotation) {
        if (newAnnotation == null || newAnnotation.getAnnotation() == null) {
            setEnabled(false);
            return;
        }
        this.currentAnnotationComponent = newAnnotation;
        FreeTextAnnotationComponent freeTextAnnotationComponent = (FreeTextAnnotationComponent) this.currentAnnotationComponent;
        this.freeTextAnnotation = (FreeTextAnnotation) freeTextAnnotationComponent.getAnnotation();
        applySelectedValue(this.fontNameBox, this.freeTextAnnotation.getFontName());
        applySelectedValue(this.fontSizeBox, Integer.valueOf(this.freeTextAnnotation.getFontSize()));
        this.fontColorButton.setBackground(this.freeTextAnnotation.getFontColor());
        applySelectedValue(this.strokeTypeBox, Boolean.valueOf(this.freeTextAnnotation.isStrokeType()));
        applySelectedValue(this.strokeStyleBox, this.freeTextAnnotation.getBorderStyle().getBorderStyle());
        applySelectedValue(this.strokeThicknessBox, Float.valueOf(this.freeTextAnnotation.getBorderStyle().getStrokeWidth()));
        this.strokeColorButton.setBackground(this.freeTextAnnotation.getColor());
        applySelectedValue(this.fillTypeBox, Boolean.valueOf(this.freeTextAnnotation.isFillType()));
        this.fillColorButton.setBackground(this.freeTextAnnotation.getFillColor());
        safeEnable(this.fontNameBox, true);
        safeEnable(this.fontSizeBox, true);
        safeEnable(this.fontColorButton, true);
        safeEnable(this.strokeTypeBox, true);
        safeEnable(this.strokeThicknessBox, true);
        safeEnable(this.strokeStyleBox, true);
        safeEnable(this.strokeColorButton, true);
        safeEnable(this.fillTypeBox, true);
        safeEnable(this.fillColorButton, true);
        disableInvisibleFields();
    }

    private void disableInvisibleFields() {
        boolean fillType = this.freeTextAnnotation.isFillType();
        boolean strokeType = this.freeTextAnnotation.isStrokeType();
        safeEnable(this.fillColorButton, fillType);
        safeEnable(this.strokeThicknessBox, strokeType);
        safeEnable(this.strokeStyleBox, strokeType);
        safeEnable(this.strokeColorButton, strokeType);
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent e2) {
        ValueLabelItem item = (ValueLabelItem) e2.getItem();
        if (e2.getStateChange() == 1) {
            if (e2.getSource() == this.fontNameBox) {
                this.freeTextAnnotation.setFontName((String) item.getValue());
            } else if (e2.getSource() == this.fontSizeBox) {
                this.freeTextAnnotation.setFontSize(((Integer) item.getValue()).intValue());
            } else if (e2.getSource() == this.strokeTypeBox) {
                Boolean visible = (Boolean) item.getValue();
                this.freeTextAnnotation.setStrokeType(visible.booleanValue());
                if (visible.booleanValue()) {
                    this.freeTextAnnotation.getBorderStyle().setStrokeWidth(((Float) ((ValueLabelItem) this.strokeThicknessBox.getSelectedItem()).getValue()).floatValue());
                    this.freeTextAnnotation.getBorderStyle().setBorderStyle((Name) ((ValueLabelItem) this.strokeStyleBox.getSelectedItem()).getValue());
                } else {
                    this.freeTextAnnotation.getBorderStyle().setStrokeWidth(0.0f);
                }
                disableInvisibleFields();
            } else if (e2.getSource() == this.strokeStyleBox) {
                this.freeTextAnnotation.getBorderStyle().setBorderStyle((Name) item.getValue());
            } else if (e2.getSource() == this.strokeThicknessBox) {
                this.freeTextAnnotation.getBorderStyle().setStrokeWidth(((Float) item.getValue()).floatValue());
            } else if (e2.getSource() == this.fillTypeBox) {
                this.freeTextAnnotation.setFillType(((Boolean) item.getValue()).booleanValue());
                if (this.freeTextAnnotation.isFillType()) {
                    this.freeTextAnnotation.setFillColor(this.fillColorButton.getBackground());
                }
                disableInvisibleFields();
            }
            updateCurrentAnnotation();
            this.currentAnnotationComponent.resetAppearanceShapes();
            this.currentAnnotationComponent.repaint();
        }
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent e2) throws HeadlessException {
        Color chosenColor;
        if (e2.getSource() == this.strokeColorButton) {
            Color chosenColor2 = JColorChooser.showDialog(this.strokeColorButton, this.messageBundle.getString("viewer.utilityPane.annotation.freeText.border.color.ChooserTitle"), this.strokeColorButton.getBackground());
            if (chosenColor2 != null) {
                this.strokeColorButton.setBackground(chosenColor2);
                this.freeTextAnnotation.setColor(chosenColor2);
            }
        } else if (e2.getSource() == this.fillColorButton) {
            Color chosenColor3 = JColorChooser.showDialog(this.fillColorButton, this.messageBundle.getString("viewer.utilityPane.annotation.freeText.fill.color.ChooserTitle"), this.fillColorButton.getBackground());
            if (chosenColor3 != null) {
                this.fillColorButton.setBackground(chosenColor3);
                this.freeTextAnnotation.setFillColor(chosenColor3);
            }
        } else if (e2.getSource() == this.fontColorButton && (chosenColor = JColorChooser.showDialog(this.fillColorButton, this.messageBundle.getString("viewer.utilityPane.annotation.freeText.font.color.ChooserTitle"), this.fontColorButton.getBackground())) != null) {
            this.fontColorButton.setBackground(chosenColor);
            this.freeTextAnnotation.setFontColor(chosenColor);
        }
        updateCurrentAnnotation();
        this.currentAnnotationComponent.resetAppearanceShapes();
        this.currentAnnotationComponent.repaint();
    }

    private void createGUI() {
        if (FONT_NAMES_LIST == null) {
            FONT_NAMES_LIST = new ValueLabelItem[]{new ValueLabelItem(RTFGenerator.defaultFontFamily, RTFGenerator.defaultFontFamily), new ValueLabelItem("Helvetica-Oblique", "Helvetica-Oblique"), new ValueLabelItem("Helvetica-Bold", "Helvetica-Bold"), new ValueLabelItem("Helvetica-BoldOblique", "Helvetica-BoldOblique"), new ValueLabelItem("Times-Italic", "Times-Italic"), new ValueLabelItem("Times-Bold", "Times-Bold"), new ValueLabelItem("Times-BoldItalic", "Times-BoldItalic"), new ValueLabelItem("Times-Roman", "Times-Roman"), new ValueLabelItem("Courier", "Courier"), new ValueLabelItem("Courier-Oblique", "Courier-Oblique"), new ValueLabelItem("Courier-BoldOblique", "Courier-BoldOblique"), new ValueLabelItem("Courier-Bold", "Courier-Bold"), new ValueLabelItem("Courier-Bold", "Courier-Bold")};
        }
        if (FONT_SIZES_LIST == null) {
            FONT_SIZES_LIST = new ValueLabelItem[]{new ValueLabelItem(6, this.messageBundle.getString("viewer.common.number.six")), new ValueLabelItem(8, this.messageBundle.getString("viewer.common.number.eight")), new ValueLabelItem(9, this.messageBundle.getString("viewer.common.number.nine")), new ValueLabelItem(10, this.messageBundle.getString("viewer.common.number.ten")), new ValueLabelItem(12, this.messageBundle.getString("viewer.common.number.twelve")), new ValueLabelItem(14, this.messageBundle.getString("viewer.common.number.fourteen")), new ValueLabelItem(16, this.messageBundle.getString("viewer.common.number.sixteen")), new ValueLabelItem(18, this.messageBundle.getString("viewer.common.number.eighteen")), new ValueLabelItem(20, this.messageBundle.getString("viewer.common.number.twenty")), new ValueLabelItem(24, this.messageBundle.getString("viewer.common.number.twentyFour")), new ValueLabelItem(36, this.messageBundle.getString("viewer.common.number.thirtySix")), new ValueLabelItem(48, this.messageBundle.getString("viewer.common.number.fortyEight"))};
        }
        setBorder(new TitledBorder(new EtchedBorder(1), this.messageBundle.getString("viewer.utilityPane.annotation.freeText.appearance.title"), 1, 0));
        this.fontNameBox = new JComboBox(FONT_NAMES_LIST);
        this.fontNameBox.setSelectedIndex(0);
        this.fontNameBox.addItemListener(this);
        add(new JLabel(this.messageBundle.getString("viewer.utilityPane.annotation.freeText.font.name")));
        add(this.fontNameBox);
        this.fontSizeBox = new JComboBox(FONT_SIZES_LIST);
        this.fontSizeBox.setSelectedIndex(5);
        this.fontSizeBox.addItemListener(this);
        add(new JLabel(this.messageBundle.getString("viewer.utilityPane.annotation.freeText.font.size")));
        add(this.fontSizeBox);
        this.fontColorButton = new JButton();
        this.fontColorButton.addActionListener(this);
        this.fontColorButton.setOpaque(true);
        this.fontColorButton.setBackground(DEFAULT_FONT_COLOR);
        add(new JLabel(this.messageBundle.getString("viewer.utilityPane.annotation.freeText.font.color")));
        add(this.fontColorButton);
        this.strokeTypeBox = new JComboBox(VISIBLE_TYPE_LIST);
        this.strokeTypeBox.setSelectedIndex(0);
        this.strokeTypeBox.addItemListener(this);
        add(new JLabel(this.messageBundle.getString("viewer.utilityPane.annotation.freeText.border.type")));
        add(this.strokeTypeBox);
        this.strokeThicknessBox = new JComboBox(LINE_THICKNESS_LIST);
        this.strokeThicknessBox.setSelectedIndex(0);
        this.strokeThicknessBox.addItemListener(this);
        add(new JLabel(this.messageBundle.getString("viewer.utilityPane.annotation.freeText.border.thickness")));
        add(this.strokeThicknessBox);
        this.strokeStyleBox = new JComboBox(LINE_STYLE_LIST);
        this.strokeStyleBox.setSelectedIndex(0);
        this.strokeStyleBox.addItemListener(this);
        add(new JLabel(this.messageBundle.getString("viewer.utilityPane.annotation.freeText.border.style")));
        add(this.strokeStyleBox);
        this.strokeColorButton = new JButton();
        this.strokeColorButton.addActionListener(this);
        this.strokeColorButton.setOpaque(true);
        this.strokeColorButton.setBackground(DEFAULT_BORDER_COLOR);
        add(new JLabel(this.messageBundle.getString("viewer.utilityPane.annotation.freeText.border.color")));
        add(this.strokeColorButton);
        this.fillTypeBox = new JComboBox(VISIBLE_TYPE_LIST);
        this.fillTypeBox.setSelectedIndex(0);
        this.fillTypeBox.addItemListener(this);
        add(new JLabel(this.messageBundle.getString("viewer.utilityPane.annotation.freeText.fill.type")));
        add(this.fillTypeBox);
        this.fillColorButton = new JButton();
        this.fillColorButton.addActionListener(this);
        this.fillColorButton.setOpaque(true);
        this.fillColorButton.setBackground(DEFAULT_STROKE_COLOR);
        add(new JLabel(this.messageBundle.getString("viewer.utilityPane.annotation.freeText.fill.color")));
        add(this.fillColorButton);
    }

    @Override // javax.swing.JComponent, java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        safeEnable(this.fontNameBox, enabled);
        safeEnable(this.fontSizeBox, enabled);
        safeEnable(this.fontColorButton, enabled);
        safeEnable(this.strokeTypeBox, enabled);
        safeEnable(this.strokeThicknessBox, enabled);
        safeEnable(this.strokeStyleBox, enabled);
        safeEnable(this.strokeColorButton, enabled);
        safeEnable(this.fillTypeBox, enabled);
        safeEnable(this.fillColorButton, enabled);
    }

    protected boolean safeEnable(JComponent comp, boolean enabled) {
        if (comp != null) {
            comp.setEnabled(enabled);
            return true;
        }
        return false;
    }

    private void applySelectedValue(JComboBox comboBox, Object value) {
        comboBox.removeItemListener(this);
        int i2 = 0;
        while (true) {
            if (i2 >= comboBox.getItemCount()) {
                break;
            }
            ValueLabelItem currentItem = (ValueLabelItem) comboBox.getItemAt(i2);
            if (!currentItem.getValue().equals(value)) {
                i2++;
            } else {
                comboBox.setSelectedIndex(i2);
                break;
            }
        }
        comboBox.addItemListener(this);
    }
}
