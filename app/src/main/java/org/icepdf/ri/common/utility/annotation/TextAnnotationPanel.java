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
import org.icepdf.core.pobjects.annotations.TextAnnotation;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.views.AnnotationComponent;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/utility/annotation/TextAnnotationPanel.class */
public class TextAnnotationPanel extends AnnotationPanelAdapter implements ItemListener, ActionListener {
    private static final int DEFAULT_ICON_NAME = 0;
    private static final Color DEFAULT_COLOR = new Color(1.0f, 1.0f, 0.0f);
    private static ValueLabelItem[] TEXT_ICON_LIST;
    private JComboBox iconNameBox;
    private JButton colorButton;
    private TextAnnotation annotation;

    public TextAnnotationPanel(SwingController controller) {
        super(controller);
        setLayout(new GridLayout(2, 2, 5, 2));
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
        this.annotation = (TextAnnotation) this.currentAnnotationComponent.getAnnotation();
        applySelectedValue(this.iconNameBox, this.annotation.getIconName());
        this.colorButton.setBackground(this.annotation.getColor());
        safeEnable(this.iconNameBox, true);
        safeEnable(this.colorButton, true);
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent e2) {
        ValueLabelItem item = (ValueLabelItem) e2.getItem();
        if (e2.getStateChange() == 1) {
            if (e2.getSource() == this.iconNameBox) {
                this.annotation.setIconName((Name) item.getValue());
            }
            updateCurrentAnnotation();
            this.currentAnnotationComponent.resetAppearanceShapes();
            this.currentAnnotationComponent.repaint();
        }
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent e2) {
        Color chosenColor;
        if (e2.getSource() == this.colorButton && (chosenColor = JColorChooser.showDialog(this.colorButton, this.messageBundle.getString("viewer.utilityPane.annotation.textMarkup.colorChooserTitle"), this.colorButton.getBackground())) != null) {
            this.colorButton.setBackground(chosenColor);
            this.annotation.setColor(chosenColor);
            updateCurrentAnnotation();
            this.currentAnnotationComponent.resetAppearanceShapes();
            this.currentAnnotationComponent.repaint();
        }
    }

    private void createGUI() {
        if (TEXT_ICON_LIST == null) {
            TEXT_ICON_LIST = new ValueLabelItem[]{new ValueLabelItem(TextAnnotation.COMMENT_ICON, this.messageBundle.getString("viewer.utilityPane.annotation.text.iconName.comment")), new ValueLabelItem(TextAnnotation.CHECK_ICON, this.messageBundle.getString("viewer.utilityPane.annotation.text.iconName.check")), new ValueLabelItem(TextAnnotation.CHECK_MARK_ICON, this.messageBundle.getString("viewer.utilityPane.annotation.text.iconName.checkMark")), new ValueLabelItem(TextAnnotation.CIRCLE_ICON, this.messageBundle.getString("viewer.utilityPane.annotation.text.iconName.circle")), new ValueLabelItem(TextAnnotation.CROSS_ICON, this.messageBundle.getString("viewer.utilityPane.annotation.text.iconName.cross")), new ValueLabelItem(TextAnnotation.CROSS_HAIRS_ICON, this.messageBundle.getString("viewer.utilityPane.annotation.text.iconName.crossHairs")), new ValueLabelItem(TextAnnotation.HELP_ICON, this.messageBundle.getString("viewer.utilityPane.annotation.text.iconName.help")), new ValueLabelItem(TextAnnotation.INSERT_ICON, this.messageBundle.getString("viewer.utilityPane.annotation.text.iconName.insert")), new ValueLabelItem(TextAnnotation.KEY_ICON, this.messageBundle.getString("viewer.utilityPane.annotation.text.iconName.key")), new ValueLabelItem(TextAnnotation.NEW_PARAGRAPH_ICON, this.messageBundle.getString("viewer.utilityPane.annotation.text.iconName.newParagraph")), new ValueLabelItem(TextAnnotation.PARAGRAPH_ICON, this.messageBundle.getString("viewer.utilityPane.annotation.text.iconName.paragraph")), new ValueLabelItem(TextAnnotation.RIGHT_ARROW_ICON, this.messageBundle.getString("viewer.utilityPane.annotation.text.iconName.rightArrow")), new ValueLabelItem(TextAnnotation.RIGHT_POINTER_ICON, this.messageBundle.getString("viewer.utilityPane.annotation.text.iconName.rightPointer")), new ValueLabelItem(TextAnnotation.STAR_ICON, this.messageBundle.getString("viewer.utilityPane.annotation.text.iconName.star")), new ValueLabelItem(TextAnnotation.UP_LEFT_ARROW_ICON, this.messageBundle.getString("viewer.utilityPane.annotation.text.iconName.upLeftArrow")), new ValueLabelItem(TextAnnotation.UP_ARROW_ICON, this.messageBundle.getString("viewer.utilityPane.annotation.text.iconName.upArrow"))};
        }
        setBorder(new TitledBorder(new EtchedBorder(1), this.messageBundle.getString("viewer.utilityPane.annotation.text.appearance.title"), 1, 0));
        this.iconNameBox = new JComboBox(TEXT_ICON_LIST);
        this.iconNameBox.setSelectedIndex(0);
        this.iconNameBox.addItemListener(this);
        add(new JLabel(this.messageBundle.getString("viewer.utilityPane.annotation.text.iconName")));
        add(this.iconNameBox);
        this.colorButton = new JButton();
        this.colorButton.addActionListener(this);
        this.colorButton.setOpaque(true);
        this.colorButton.setBackground(DEFAULT_COLOR);
        add(new JLabel(this.messageBundle.getString("viewer.utilityPane.annotation.textMarkup.colorLabel")));
        add(this.colorButton);
    }

    @Override // javax.swing.JComponent, java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        safeEnable(this.iconNameBox, enabled);
        safeEnable(this.colorButton, enabled);
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
