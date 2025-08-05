package org.icepdf.ri.util;

import java.awt.Component;
import java.awt.HeadlessException;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/util/Resources.class */
public final class Resources extends StringResource {
    public static void showMessageDialog(Component parent, int dialogType, ResourceBundle messageBundle, String titleKey, String messageKey) throws HeadlessException {
        showMessageDialog(parent, dialogType, messageBundle, titleKey, messageKey, null, null, null, null);
    }

    public static void showMessageDialog(Component parent, int dialogType, ResourceBundle messageBundle, String titleKey, String messageKey, Object messageArg1) throws HeadlessException {
        showMessageDialog(parent, dialogType, messageBundle, titleKey, messageKey, messageArg1, null, null, null);
    }

    public static void showMessageDialog(Component parent, int dialogType, ResourceBundle messageBundle, String titleKey, String messageKey, Object messageArg1, Object messageArg2) throws HeadlessException {
        showMessageDialog(parent, dialogType, messageBundle, titleKey, messageKey, messageArg1, messageArg2, null, null);
    }

    public static void showMessageDialog(Component parent, int dialogType, ResourceBundle messageBundle, String titleKey, String messageKey, Object messageArg1, Object messageArg2, Object messageArg3) throws HeadlessException {
        showMessageDialog(parent, dialogType, messageBundle, titleKey, messageKey, messageArg1, messageArg2, messageArg3, null);
    }

    public static void showMessageDialog(Component parent, int dialogType, ResourceBundle messageBundle, String titleKey, String messageKey, Object messageArg1, Object messageArg2, Object messageArg3, Object messageArg4) throws HeadlessException {
        Object[] messageArguments = {messageArg1, messageArg2, messageArg3, messageArg4};
        MessageFormat formatter = new MessageFormat(messageBundle.getString(messageKey));
        JOptionPane.showMessageDialog(parent, formatter.format(messageArguments), messageBundle.getString(titleKey), dialogType);
    }

    public static boolean showConfirmDialog(Component parent, ResourceBundle messageBundle, String titleKey, String messageKey) {
        return showConfirmDialog(parent, messageBundle, titleKey, messageKey, null, null, null, null);
    }

    public static boolean showConfirmDialog(Component parent, ResourceBundle messageBundle, String titleKey, String messageKey, Object messageArg1) {
        return showConfirmDialog(parent, messageBundle, titleKey, messageKey, messageArg1, null, null, null);
    }

    public static boolean showConfirmDialog(Component parent, ResourceBundle messageBundle, String titleKey, String messageKey, Object messageArg1, Object messageArg2) {
        return showConfirmDialog(parent, messageBundle, titleKey, messageKey, messageArg1, messageArg2, null, null);
    }

    public static boolean showConfirmDialog(Component parent, ResourceBundle messageBundle, String titleKey, String messageKey, Object messageArg1, Object messageArg2, Object messageArg3) {
        return showConfirmDialog(parent, messageBundle, titleKey, messageKey, messageArg1, messageArg2, messageArg3, null);
    }

    public static boolean showConfirmDialog(Component parent, ResourceBundle messageBundle, String titleKey, String messageKey, Object messageArg1, Object messageArg2, Object messageArg3, Object messageArg4) {
        Object[] messageArguments = {messageArg1, messageArg2, messageArg3, messageArg4};
        MessageFormat formatter = new MessageFormat(messageBundle.getString(messageKey));
        return JOptionPane.showConfirmDialog(parent, formatter.format(messageArguments), messageBundle.getString(titleKey), 0) == 0;
    }
}
