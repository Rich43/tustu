package org.icepdf.ri.common.utility.annotation;

import java.awt.Color;
import java.awt.GridLayout;
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
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.pobjects.annotations.Annotation;
import org.icepdf.core.pobjects.annotations.BorderStyle;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.views.AnnotationComponent;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/utility/annotation/BorderPanel.class */
public class BorderPanel extends AnnotationPanelAdapter implements ItemListener, ActionListener {
    private static final int DEFAULT_LINK_TYPE = 1;
    private static final int DEFAULT_LINE_THICKNESS = 0;
    private static final int DEFAULT_LINE_STYLE = 0;
    private static final Color DEFAULT_BORDER_COLOR = Color.BLACK;
    private static ValueLabelItem[] LINE_STYLE_LIST;
    private JComboBox linkTypeBox;
    private JComboBox lineThicknessBox;
    private JComboBox lineStyleBox;
    private JButton colorButton;

    public BorderPanel(SwingController controller) {
        super(controller);
        setLayout(new GridLayout(4, 2, 5, 2));
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
        Annotation annotation = this.currentAnnotationComponent.getAnnotation();
        if (annotation.getLineThickness() == 0.0f) {
            applySelectedValue(this.linkTypeBox, 0);
        } else {
            applySelectedValue(this.linkTypeBox, 1);
        }
        applySelectedValue(this.lineThicknessBox, Float.valueOf(annotation.getLineThickness()));
        applySelectedValue(this.lineStyleBox, annotation.getLineStyle());
        this.colorButton.setBackground(annotation.getColor());
        enableAppearanceInputComponents(annotation.getBorderType() == 1);
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent e2) {
        Annotation annotation = this.currentAnnotationComponent.getAnnotation();
        ValueLabelItem item = (ValueLabelItem) e2.getItem();
        if (e2.getStateChange() == 1) {
            if (e2.getSource() == this.linkTypeBox) {
                boolean linkVisible = ((Boolean) item.getValue()).booleanValue();
                if (linkVisible) {
                    annotation.getBorderStyle().setStrokeWidth(1.0f);
                    if (annotation.getColor() == null) {
                        annotation.setColor(Color.BLACK);
                    }
                } else {
                    annotation.getBorderStyle().setStrokeWidth(0.0f);
                }
                applySelectedValue(this.lineThicknessBox, Float.valueOf(annotation.getLineThickness()));
                enableAppearanceInputComponents(linkVisible);
            } else if (e2.getSource() == this.lineThicknessBox) {
                float lineThickness = ((Float) item.getValue()).floatValue();
                annotation.getBorderStyle().setStrokeWidth(lineThickness);
            } else if (e2.getSource() == this.lineStyleBox) {
                Name lineStyle = (Name) item.getValue();
                annotation.getBorderStyle().setBorderStyle(lineStyle);
            }
            updateCurrentAnnotation();
            this.currentAnnotationComponent.repaint();
        }
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent e2) {
        Color chosenColor;
        Annotation annotation = this.currentAnnotationComponent.getAnnotation();
        if (e2.getSource() == this.colorButton && (chosenColor = JColorChooser.showDialog(this.colorButton, this.messageBundle.getString("viewer.utilityPane.annotation.border.colorChooserTitle"), this.colorButton.getBackground())) != null) {
            this.colorButton.setBackground(chosenColor);
            annotation.setColor(chosenColor);
            updateCurrentAnnotation();
            this.currentAnnotationComponent.repaint();
        }
    }

    private void createGUI() {
        if (LINE_STYLE_LIST == null) {
            LINE_STYLE_LIST = new ValueLabelItem[]{new ValueLabelItem(BorderStyle.BORDER_STYLE_SOLID, this.messageBundle.getString("viewer.utilityPane.annotation.border.solid")), new ValueLabelItem(BorderStyle.BORDER_STYLE_DASHED, this.messageBundle.getString("viewer.utilityPane.annotation.border.dashed")), new ValueLabelItem(BorderStyle.BORDER_STYLE_BEVELED, this.messageBundle.getString("viewer.utilityPane.annotation.border.beveled")), new ValueLabelItem(BorderStyle.BORDER_STYLE_INSET, this.messageBundle.getString("viewer.utilityPane.annotation.border.inset")), new ValueLabelItem(BorderStyle.BORDER_STYLE_UNDERLINE, this.messageBundle.getString("viewer.utilityPane.annotation.border.underline"))};
        }
        setBorder(new TitledBorder(new EtchedBorder(1), this.messageBundle.getString("viewer.utilityPane.annotation.border.title"), 1, 0));
        this.linkTypeBox = new JComboBox(VISIBLE_TYPE_LIST);
        this.linkTypeBox.setSelectedIndex(1);
        this.linkTypeBox.addItemListener(this);
        add(new JLabel(this.messageBundle.getString("viewer.utilityPane.annotation.border.linkType")));
        add(this.linkTypeBox);
        this.lineThicknessBox = new JComboBox(LINE_THICKNESS_LIST);
        this.lineThicknessBox.setSelectedIndex(0);
        this.lineThicknessBox.addItemListener(this);
        add(new JLabel(this.messageBundle.getString("viewer.utilityPane.annotation.border.lineThickness")));
        add(this.lineThicknessBox);
        this.lineStyleBox = new JComboBox(LINE_STYLE_LIST);
        this.lineStyleBox.setSelectedIndex(0);
        this.lineStyleBox.addItemListener(this);
        add(new JLabel(this.messageBundle.getString("viewer.utilityPane.annotation.border.lineStyle")));
        add(this.lineStyleBox);
        this.colorButton = new JButton();
        this.colorButton.addActionListener(this);
        this.colorButton.setOpaque(true);
        this.colorButton.setBackground(DEFAULT_BORDER_COLOR);
        add(new JLabel(this.messageBundle.getString("viewer.utilityPane.annotation.border.colorLabel")));
        add(this.colorButton);
    }

    @Override // javax.swing.JComponent, java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        safeEnable(this.linkTypeBox, enabled);
        safeEnable(this.lineThicknessBox, enabled);
        safeEnable(this.lineStyleBox, enabled);
        safeEnable(this.colorButton, enabled);
    }

    private void enableAppearanceInputComponents(boolean visible) {
        if (!visible) {
            safeEnable(this.linkTypeBox, true);
            safeEnable(this.lineThicknessBox, false);
            safeEnable(this.lineStyleBox, false);
            safeEnable(this.colorButton, false);
            return;
        }
        safeEnable(this.linkTypeBox, true);
        safeEnable(this.lineThicknessBox, true);
        safeEnable(this.lineStyleBox, true);
        safeEnable(this.colorButton, true);
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
