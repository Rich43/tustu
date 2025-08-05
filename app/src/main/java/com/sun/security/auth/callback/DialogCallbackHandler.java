package com.sun.security.auth.callback;

import java.awt.Component;
import java.awt.HeadlessException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.ConfirmationCallback;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.TextOutputCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import jdk.Exported;

@Exported(false)
@Deprecated
/* loaded from: rt.jar:com/sun/security/auth/callback/DialogCallbackHandler.class */
public class DialogCallbackHandler implements CallbackHandler {
    private Component parentComponent;
    private static final int JPasswordFieldLen = 8;
    private static final int JTextFieldLen = 8;

    /* loaded from: rt.jar:com/sun/security/auth/callback/DialogCallbackHandler$Action.class */
    private interface Action {
        void perform();
    }

    public DialogCallbackHandler() {
    }

    public DialogCallbackHandler(Component component) {
        this.parentComponent = component;
    }

    @Override // javax.security.auth.callback.CallbackHandler
    public void handle(Callback[] callbackArr) throws UnsupportedCallbackException, HeadlessException {
        ArrayList arrayList = new ArrayList(3);
        ArrayList arrayList2 = new ArrayList(2);
        ConfirmationInfo confirmationInfo = new ConfirmationInfo();
        for (int i2 = 0; i2 < callbackArr.length; i2++) {
            if (callbackArr[i2] instanceof TextOutputCallback) {
                TextOutputCallback textOutputCallback = (TextOutputCallback) callbackArr[i2];
                switch (textOutputCallback.getMessageType()) {
                    case 0:
                        confirmationInfo.messageType = 1;
                        break;
                    case 1:
                        confirmationInfo.messageType = 2;
                        break;
                    case 2:
                        confirmationInfo.messageType = 0;
                        break;
                    default:
                        throw new UnsupportedCallbackException(callbackArr[i2], "Unrecognized message type");
                }
                arrayList.add(textOutputCallback.getMessage());
            } else if (callbackArr[i2] instanceof NameCallback) {
                final NameCallback nameCallback = (NameCallback) callbackArr[i2];
                JLabel jLabel = new JLabel(nameCallback.getPrompt());
                final JTextField jTextField = new JTextField(8);
                String defaultName = nameCallback.getDefaultName();
                if (defaultName != null) {
                    jTextField.setText(defaultName);
                }
                Box boxCreateHorizontalBox = Box.createHorizontalBox();
                boxCreateHorizontalBox.add(jLabel);
                boxCreateHorizontalBox.add(jTextField);
                arrayList.add(boxCreateHorizontalBox);
                arrayList2.add(new Action() { // from class: com.sun.security.auth.callback.DialogCallbackHandler.1
                    @Override // com.sun.security.auth.callback.DialogCallbackHandler.Action
                    public void perform() {
                        nameCallback.setName(jTextField.getText());
                    }
                });
            } else if (callbackArr[i2] instanceof PasswordCallback) {
                final PasswordCallback passwordCallback = (PasswordCallback) callbackArr[i2];
                JLabel jLabel2 = new JLabel(passwordCallback.getPrompt());
                final JPasswordField jPasswordField = new JPasswordField(8);
                if (!passwordCallback.isEchoOn()) {
                    jPasswordField.setEchoChar('*');
                }
                Box boxCreateHorizontalBox2 = Box.createHorizontalBox();
                boxCreateHorizontalBox2.add(jLabel2);
                boxCreateHorizontalBox2.add(jPasswordField);
                arrayList.add(boxCreateHorizontalBox2);
                arrayList2.add(new Action() { // from class: com.sun.security.auth.callback.DialogCallbackHandler.2
                    @Override // com.sun.security.auth.callback.DialogCallbackHandler.Action
                    public void perform() {
                        passwordCallback.setPassword(jPasswordField.getPassword());
                    }
                });
            } else if (callbackArr[i2] instanceof ConfirmationCallback) {
                ConfirmationCallback confirmationCallback = (ConfirmationCallback) callbackArr[i2];
                confirmationInfo.setCallback(confirmationCallback);
                if (confirmationCallback.getPrompt() != null) {
                    arrayList.add(confirmationCallback.getPrompt());
                }
            } else {
                throw new UnsupportedCallbackException(callbackArr[i2], "Unrecognized Callback");
            }
        }
        int iShowOptionDialog = JOptionPane.showOptionDialog(this.parentComponent, arrayList.toArray(), "Confirmation", confirmationInfo.optionType, confirmationInfo.messageType, null, confirmationInfo.options, confirmationInfo.initialValue);
        if (iShowOptionDialog == 0 || iShowOptionDialog == 0) {
            Iterator<E> it = arrayList2.iterator();
            while (it.hasNext()) {
                ((Action) it.next()).perform();
            }
        }
        confirmationInfo.handleResult(iShowOptionDialog);
    }

    /* loaded from: rt.jar:com/sun/security/auth/callback/DialogCallbackHandler$ConfirmationInfo.class */
    private static class ConfirmationInfo {
        private int[] translations;
        int optionType;
        Object[] options;
        Object initialValue;
        int messageType;
        private ConfirmationCallback callback;

        private ConfirmationInfo() {
            this.optionType = 2;
            this.options = null;
            this.initialValue = null;
            this.messageType = 3;
        }

        void setCallback(ConfirmationCallback confirmationCallback) throws UnsupportedCallbackException {
            this.callback = confirmationCallback;
            int optionType = confirmationCallback.getOptionType();
            switch (optionType) {
                case -1:
                    this.options = confirmationCallback.getOptions();
                    this.translations = new int[]{-1, confirmationCallback.getDefaultOption()};
                    break;
                case 0:
                    this.optionType = 0;
                    this.translations = new int[]{0, 0, 1, 1, -1, 1};
                    break;
                case 1:
                    this.optionType = 1;
                    this.translations = new int[]{0, 0, 1, 1, 2, 2, -1, 2};
                    break;
                case 2:
                    this.optionType = 2;
                    this.translations = new int[]{0, 3, 2, 2, -1, 2};
                    break;
                default:
                    throw new UnsupportedCallbackException(confirmationCallback, "Unrecognized option type: " + optionType);
            }
            int messageType = confirmationCallback.getMessageType();
            switch (messageType) {
                case 0:
                    this.messageType = 1;
                    return;
                case 1:
                    this.messageType = 2;
                    return;
                case 2:
                    this.messageType = 0;
                    return;
                default:
                    throw new UnsupportedCallbackException(confirmationCallback, "Unrecognized message type: " + messageType);
            }
        }

        void handleResult(int i2) {
            if (this.callback == null) {
                return;
            }
            int i3 = 0;
            while (true) {
                if (i3 >= this.translations.length) {
                    break;
                }
                if (this.translations[i3] != i2) {
                    i3 += 2;
                } else {
                    i2 = this.translations[i3 + 1];
                    break;
                }
            }
            this.callback.setSelectedIndex(i2);
        }
    }
}
