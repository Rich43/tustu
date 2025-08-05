package de.muntjak.tinylookandfeel;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.AttributedCharacterIterator;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSpinnerUI;
import javax.swing.text.InternationalFormatter;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinySpinnerUI.class */
public class TinySpinnerUI extends BasicSpinnerUI {
    private static final ArrowButtonHandler nextButtonHandler = new ArrowButtonHandler("increment", true);
    private static final ArrowButtonHandler previousButtonHandler = new ArrowButtonHandler("decrement", false);

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinySpinnerUI$ArrowButtonHandler.class */
    private static class ArrowButtonHandler extends AbstractAction implements MouseListener {
        final Timer autoRepeatTimer;
        final boolean isNext;
        private transient boolean mouseInsideButton;
        private transient int actionCount;
        private transient int modCount;
        JSpinner spinner;

        ArrowButtonHandler(String str, boolean z2) {
            super(str);
            this.mouseInsideButton = false;
            this.spinner = null;
            this.isNext = z2;
            this.autoRepeatTimer = new Timer(10, this);
            this.autoRepeatTimer.setInitialDelay(300);
        }

        private JSpinner eventToSpinner(AWTEvent aWTEvent) {
            Object obj;
            Object source = aWTEvent.getSource();
            while (true) {
                obj = source;
                if (!(obj instanceof Component) || (obj instanceof JSpinner)) {
                    break;
                }
                source = ((Component) obj).getParent();
            }
            if (obj instanceof JSpinner) {
                return (JSpinner) obj;
            }
            return null;
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) throws SecurityException {
            if (this.modCount == 0 || this.actionCount % this.modCount == 0) {
                JSpinner jSpinnerEventToSpinner = this.spinner;
                if (!(actionEvent.getSource() instanceof Timer)) {
                    jSpinnerEventToSpinner = eventToSpinner(actionEvent);
                } else if (!this.mouseInsideButton) {
                    return;
                }
                if (jSpinnerEventToSpinner != null) {
                    try {
                        int calendarField = getCalendarField(jSpinnerEventToSpinner);
                        jSpinnerEventToSpinner.commitEdit();
                        if (calendarField != -1) {
                            ((SpinnerDateModel) jSpinnerEventToSpinner.getModel()).setCalendarField(calendarField);
                        }
                        Object nextValue = this.isNext ? jSpinnerEventToSpinner.getNextValue() : jSpinnerEventToSpinner.getPreviousValue();
                        if (nextValue != null) {
                            jSpinnerEventToSpinner.setValue(nextValue);
                            select(jSpinnerEventToSpinner);
                        }
                    } catch (IllegalArgumentException e2) {
                        UIManager.getLookAndFeel().provideErrorFeedback(jSpinnerEventToSpinner);
                    } catch (ParseException e3) {
                        UIManager.getLookAndFeel().provideErrorFeedback(jSpinnerEventToSpinner);
                    }
                }
                if (this.modCount > 0) {
                    this.modCount--;
                    this.actionCount = 0;
                }
            }
            this.actionCount++;
        }

        private void select(JSpinner jSpinner) {
            Object value;
            DateFormat.Field fieldOfCalendarField;
            JComponent editor = jSpinner.getEditor();
            if (editor instanceof JSpinner.DateEditor) {
                JSpinner.DateEditor dateEditor = (JSpinner.DateEditor) editor;
                JFormattedTextField textField = dateEditor.getTextField();
                SimpleDateFormat format = dateEditor.getFormat();
                if (format == null || (value = jSpinner.getValue()) == null || (fieldOfCalendarField = DateFormat.Field.ofCalendarField(dateEditor.getModel().getCalendarField())) == null) {
                    return;
                }
                try {
                    AttributedCharacterIterator toCharacterIterator = format.formatToCharacterIterator(value);
                    if (!select(textField, toCharacterIterator, fieldOfCalendarField) && fieldOfCalendarField == DateFormat.Field.HOUR0) {
                        select(textField, toCharacterIterator, DateFormat.Field.HOUR1);
                    }
                } catch (IllegalArgumentException e2) {
                }
            }
        }

        private boolean select(JFormattedTextField jFormattedTextField, AttributedCharacterIterator attributedCharacterIterator, DateFormat.Field field) {
            int length = jFormattedTextField.getDocument().getLength();
            attributedCharacterIterator.first();
            do {
                Map<AttributedCharacterIterator.Attribute, Object> attributes = attributedCharacterIterator.getAttributes();
                if (attributes != null && attributes.containsKey(field)) {
                    int runStart = attributedCharacterIterator.getRunStart(field);
                    int runLimit = attributedCharacterIterator.getRunLimit(field);
                    if (runStart == -1 || runLimit == -1 || runStart > length || runLimit > length) {
                        return true;
                    }
                    jFormattedTextField.select(runStart, runLimit);
                    return true;
                }
            } while (attributedCharacterIterator.next() != 65535);
            return false;
        }

        private int getCalendarField(JSpinner jSpinner) throws SecurityException {
            JComponent editor = jSpinner.getEditor();
            if (!(editor instanceof JSpinner.DateEditor)) {
                return -1;
            }
            JFormattedTextField textField = ((JSpinner.DateEditor) editor).getTextField();
            int selectionStart = textField.getSelectionStart();
            JFormattedTextField.AbstractFormatter formatter = textField.getFormatter();
            if (!(formatter instanceof InternationalFormatter)) {
                return -1;
            }
            Format.Field[] fields = ((InternationalFormatter) formatter).getFields(selectionStart);
            for (int i2 = 0; i2 < fields.length; i2++) {
                if (fields[i2] instanceof DateFormat.Field) {
                    int calendarField = fields[i2] == DateFormat.Field.HOUR1 ? 10 : ((DateFormat.Field) fields[i2]).getCalendarField();
                    if (calendarField != -1) {
                        return calendarField;
                    }
                }
            }
            return -1;
        }

        @Override // java.awt.event.MouseListener
        public void mousePressed(MouseEvent mouseEvent) {
            if (SwingUtilities.isLeftMouseButton(mouseEvent) && mouseEvent.getComponent().isEnabled()) {
                this.spinner = eventToSpinner(mouseEvent);
                this.actionCount = 0;
                this.modCount = 20;
                this.autoRepeatTimer.start();
                focusSpinnerIfNecessary();
            }
        }

        @Override // java.awt.event.MouseListener
        public void mouseReleased(MouseEvent mouseEvent) {
            this.autoRepeatTimer.stop();
            this.spinner = null;
        }

        @Override // java.awt.event.MouseListener
        public void mouseClicked(MouseEvent mouseEvent) {
        }

        @Override // java.awt.event.MouseListener
        public void mouseEntered(MouseEvent mouseEvent) {
            this.mouseInsideButton = true;
        }

        @Override // java.awt.event.MouseListener
        public void mouseExited(MouseEvent mouseEvent) {
            this.mouseInsideButton = false;
        }

        private void focusSpinnerIfNecessary() {
            Component componentAfter;
            Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
            if (this.spinner.isRequestFocusEnabled()) {
                if (focusOwner == null || !SwingUtilities.isDescendingFrom(focusOwner, this.spinner)) {
                    JSpinner focusCycleRootAncestor = this.spinner;
                    if (!focusCycleRootAncestor.isFocusCycleRoot()) {
                        focusCycleRootAncestor = focusCycleRootAncestor.getFocusCycleRootAncestor();
                    }
                    if (focusCycleRootAncestor == null || (componentAfter = focusCycleRootAncestor.getFocusTraversalPolicy().getComponentAfter(focusCycleRootAncestor, this.spinner)) == null || !SwingUtilities.isDescendingFrom(componentAfter, this.spinner)) {
                        return;
                    }
                    componentAfter.requestFocus();
                }
            }
        }
    }

    public static ComponentUI createUI(JComponent jComponent) {
        return new TinySpinnerUI();
    }

    @Override // javax.swing.plaf.basic.BasicSpinnerUI
    protected Component createPreviousButton() {
        SpecialUIButton specialUIButton = new SpecialUIButton(new TinySpinnerButtonUI(5));
        specialUIButton.putClientProperty("isSpinnerButton", Boolean.TRUE);
        specialUIButton.putClientProperty("isNextButton", Boolean.FALSE);
        specialUIButton.setFocusable(false);
        specialUIButton.addActionListener(previousButtonHandler);
        specialUIButton.addMouseListener(previousButtonHandler);
        return specialUIButton;
    }

    @Override // javax.swing.plaf.basic.BasicSpinnerUI
    protected Component createNextButton() {
        SpecialUIButton specialUIButton = new SpecialUIButton(new TinySpinnerButtonUI(1));
        specialUIButton.putClientProperty("isSpinnerButton", Boolean.TRUE);
        specialUIButton.putClientProperty("isNextButton", Boolean.TRUE);
        specialUIButton.setFocusable(false);
        specialUIButton.addActionListener(nextButtonHandler);
        specialUIButton.addMouseListener(nextButtonHandler);
        return specialUIButton;
    }
}
