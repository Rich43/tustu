package org.icepdf.ri.util;

import java.awt.HeadlessException;
import java.util.ResourceBundle;
import javax.swing.UIManager;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/util/Parse.class */
final class Parse {
    private static final String[] booleanNames = {"yes", "no", "true", "false"};
    private static final boolean[] booleans = {true, false, true, false};

    Parse() {
    }

    public static Integer parseInteger(String s2, ResourceBundle messageBundle) throws HeadlessException {
        String s3 = s2.trim();
        try {
            return new Integer(s3);
        } catch (NumberFormatException e2) {
            if (messageBundle != null) {
                Resources.showMessageDialog(null, 1, messageBundle, "parse.title", "parse.integer", s3);
                return null;
            }
            return null;
        }
    }

    public static Long parseLong(String s2, ResourceBundle messageBundle) throws HeadlessException {
        String s3 = s2.trim();
        try {
            return new Long(s3);
        } catch (NumberFormatException e2) {
            if (messageBundle != null) {
                Resources.showMessageDialog(null, 1, messageBundle, "parse.title", "parse.float", s3);
                return null;
            }
            return null;
        }
    }

    public static Float parseFloat(String s2, ResourceBundle messageBundle) throws HeadlessException {
        String s3 = s2.trim();
        try {
            return new Float(s3);
        } catch (NumberFormatException e2) {
            if (messageBundle != null) {
                Resources.showMessageDialog(null, 1, messageBundle, "parse.title", "parse.float", s3);
                return null;
            }
            return null;
        }
    }

    public static Double parseDouble(String s2, ResourceBundle messageBundle) throws HeadlessException {
        String s3 = s2.trim();
        try {
            return new Double(s3);
        } catch (NumberFormatException e2) {
            if (messageBundle != null) {
                Resources.showMessageDialog(null, 1, messageBundle, "parse.title", "parse.double", s3);
                return null;
            }
            return null;
        }
    }

    public static Boolean parseBoolean(String s2, ResourceBundle messageBundle) throws HeadlessException {
        String s3 = s2.trim();
        for (int i2 = 0; i2 < booleanNames.length; i2++) {
            if (s3.equalsIgnoreCase(booleanNames[i2])) {
                return booleans[i2] ? Boolean.TRUE : Boolean.FALSE;
            }
        }
        if (messageBundle != null) {
            Resources.showMessageDialog(null, 1, messageBundle, "parse.title", "parse.choice", s3);
            return null;
        }
        return null;
    }

    public static String parseLookAndFeel(String s2, ResourceBundle messageBundle) throws HeadlessException {
        String s3 = s2.trim();
        UIManager.LookAndFeelInfo[] looks = UIManager.getInstalledLookAndFeels();
        for (UIManager.LookAndFeelInfo look : looks) {
            if (s3.equalsIgnoreCase(look.getName())) {
                return look.getClassName();
            }
        }
        if (messageBundle != null) {
            Resources.showMessageDialog(null, 1, messageBundle, "parse.title", "parse.laf", s3);
            return null;
        }
        return null;
    }
}
