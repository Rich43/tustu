package de.muntjak.tinylookandfeel;

import com.sun.imageio.plugins.jpeg.JPEG;
import de.muntjak.tinylookandfeel.util.BooleanReference;
import de.muntjak.tinylookandfeel.util.ColorRoutines;
import de.muntjak.tinylookandfeel.util.ColoredFont;
import de.muntjak.tinylookandfeel.util.HSBReference;
import de.muntjak.tinylookandfeel.util.IntReference;
import de.muntjak.tinylookandfeel.util.SBReference;
import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;
import javax.swing.plaf.InsetsUIResource;
import org.apache.commons.net.telnet.TelnetCommand;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/Theme.class */
public class Theme {
    private static final boolean DEBUG = false;
    public static final int ERROR_NONE = 1;
    public static final int ERROR_NULL_ARGUMENT = 2;
    public static final int ERROR_FILE_NOT_FOUND = 3;
    public static final int ERROR_IO_EXCEPTION = 4;
    public static final int ERROR_NO_TINYLAF_THEME = 5;
    public static final int ERROR_WIN99_STYLE = 6;
    public static final int ERROR_INVALID_THEME_DESCRIPTION = 7;
    private static final int DEFAULT_SCROLL_SIZE = 17;
    protected static final int YQ_STYLE = 2;
    public static final String DEFAULT_THEME = "Default.theme";
    public static final String FILE_EXTENSION = ".theme";
    protected static final int FILE_ID_1 = 4660;
    protected static final int FILE_ID_2 = 8756;
    public static final int FILE_ID_3A = 12852;
    protected static final int FILE_ID_3B = 12853;
    protected static final int FILE_ID_3C = 12854;
    protected static final int FILE_ID_3D = 12855;
    protected static final int FILE_ID_3E = 12856;
    protected static final int FILE_ID_3F = 12857;
    protected static final int FILE_ID_4 = 16384;
    protected static final int FILE_ID_4B = 16385;
    protected static final int FILE_ID_4C = 16386;
    protected static final int FILE_ID_4D = 16387;
    protected static final int FILE_ID_4E = 16388;
    public static int fileID;
    public static SBReference mainColor;
    public static SBReference disColor;
    public static SBReference backColor;
    public static SBReference frameColor;
    public static SBReference sub1Color;
    public static SBReference sub2Color;
    public static SBReference sub3Color;
    public static SBReference sub4Color;
    public static SBReference sub5Color;
    public static SBReference sub6Color;
    public static SBReference sub7Color;
    public static SBReference sub8Color;
    public static ColoredFont plainFont;
    public static ColoredFont boldFont;
    public static ColoredFont buttonFont;
    public static SBReference buttonFontColor;
    public static ColoredFont labelFont;
    public static SBReference labelFontColor;
    public static ColoredFont comboFont;
    public static ColoredFont listFont;
    public static ColoredFont menuFont;
    public static SBReference menuFontColor;
    public static ColoredFont menuItemFont;
    public static SBReference menuItemFontColor;
    public static ColoredFont passwordFont;
    public static ColoredFont radioFont;
    public static SBReference radioFontColor;
    public static ColoredFont checkFont;
    public static SBReference checkFontColor;
    public static ColoredFont tableFont;
    public static SBReference tableFontColor;
    public static ColoredFont tableHeaderFont;
    public static SBReference tableHeaderFontColor;
    public static ColoredFont textAreaFont;
    public static ColoredFont textFieldFont;
    public static ColoredFont textPaneFont;
    public static ColoredFont titledBorderFont;
    public static SBReference titledBorderFontColor;
    public static ColoredFont toolTipFont;
    public static ColoredFont treeFont;
    public static ColoredFont tabFont;
    public static SBReference tabFontColor;
    public static ColoredFont editorFont;
    public static ColoredFont frameTitleFont;
    public static ColoredFont internalFrameTitleFont;
    public static ColoredFont internalPaletteTitleFont;
    public static ColoredFont progressBarFont;
    public static SBReference progressColor;
    public static SBReference progressTrackColor;
    public static SBReference progressBorderColor;
    public static SBReference progressDarkColor;
    public static SBReference progressLightColor;
    public static SBReference progressSelectForeColor;
    public static SBReference progressSelectBackColor;
    public static SBReference textBgColor;
    public static SBReference textSelectedBgColor;
    public static SBReference textDisabledBgColor;
    public static SBReference textNonEditableBgColor;
    public static SBReference textTextColor;
    public static SBReference textSelectedTextColor;
    public static SBReference textBorderColor;
    public static SBReference textBorderDisabledColor;
    public static SBReference textCaretColor;
    public static SBReference textPaneBgColor;
    public static SBReference editorPaneBgColor;
    public static SBReference desktopPaneBgColor;
    public static InsetsUIResource textInsets;
    public static SBReference comboBorderColor;
    public static SBReference comboBorderDisabledColor;
    public static SBReference comboSelectedBgColor;
    public static SBReference comboSelectedTextColor;
    public static SBReference comboFocusBgColor;
    public static SBReference comboArrowColor;
    public static SBReference comboArrowDisabledColor;
    public static SBReference comboButtColor;
    public static SBReference comboButtRolloverColor;
    public static SBReference comboButtPressedColor;
    public static SBReference comboButtDisabledColor;
    public static SBReference comboButtBorderColor;
    public static SBReference comboButtBorderDisabledColor;
    public static SBReference comboBgColor;
    public static SBReference comboTextColor;
    public static IntReference comboSpreadLight;
    public static IntReference comboSpreadLightDisabled;
    public static IntReference comboSpreadDark;
    public static IntReference comboSpreadDarkDisabled;
    public static InsetsUIResource comboInsets;
    public static BooleanReference comboRollover;
    public static BooleanReference comboFocus;
    public static SBReference listBgColor;
    public static SBReference listTextColor;
    public static SBReference listSelectedBgColor;
    public static SBReference listSelectedTextColor;
    public static SBReference listFocusBorderColor;
    public static SBReference menuBarColor;
    public static SBReference menuRolloverBgColor;
    public static SBReference menuRolloverFgColor;
    public static SBReference menuDisabledFgColor;
    public static SBReference menuItemDisabledFgColor;
    public static SBReference menuItemRolloverColor;
    public static SBReference menuItemSelectedTextColor;
    public static SBReference menuBorderColor;
    public static SBReference menuPopupColor;
    public static SBReference menuInnerHilightColor;
    public static SBReference menuInnerShadowColor;
    public static SBReference menuOuterHilightColor;
    public static SBReference menuOuterShadowColor;
    public static SBReference menuIconColor;
    public static SBReference menuIconRolloverColor;
    public static SBReference menuIconDisabledColor;
    public static SBReference menuSeparatorColor;
    public static BooleanReference menuRollover;
    public static BooleanReference menuPopupShadow;
    public static BooleanReference menuAllowTwoIcons;
    public static SBReference toolBarColor;
    public static SBReference toolBarDarkColor;
    public static SBReference toolBarLightColor;
    public static SBReference toolButtColor;
    public static SBReference toolButtSelectedColor;
    public static SBReference toolButtRolloverColor;
    public static SBReference toolButtPressedColor;
    public static SBReference toolBorderColor;
    public static SBReference toolBorderSelectedColor;
    public static SBReference toolBorderRolloverColor;
    public static SBReference toolBorderPressedColor;
    public static SBReference toolGripDarkColor;
    public static SBReference toolGripLightColor;
    public static SBReference toolSeparatorColor;
    public static InsetsUIResource toolMargin;
    public static BooleanReference toolFocus;
    public static BooleanReference toolRollover;
    public static SBReference buttonNormalColor;
    public static SBReference buttonRolloverBgColor;
    public static SBReference buttonPressedColor;
    public static SBReference buttonDisabledColor;
    public static SBReference buttonRolloverColor;
    public static SBReference buttonDefaultColor;
    public static SBReference buttonCheckColor;
    public static SBReference buttonCheckDisabledColor;
    public static SBReference buttonBorderColor;
    public static SBReference buttonBorderDisabledColor;
    public static SBReference buttonDisabledFgColor;
    public static SBReference checkDisabledFgColor;
    public static SBReference radioDisabledFgColor;
    public static SBReference toggleSelectedBg;
    public static BooleanReference buttonRolloverBorder;
    public static BooleanReference buttonFocus;
    public static BooleanReference buttonFocusBorder;
    public static BooleanReference buttonEnter;
    public static BooleanReference shiftButtonText;
    public static InsetsUIResource buttonMargin;
    public static IntReference buttonSpreadLight;
    public static IntReference buttonSpreadLightDisabled;
    public static IntReference buttonSpreadDark;
    public static IntReference buttonSpreadDarkDisabled;
    public static InsetsUIResource checkMargin;
    public static SBReference tabPaneBorderColor;
    public static SBReference tabNormalColor;
    public static SBReference tabSelectedColor;
    public static SBReference tabDisabledColor;
    public static SBReference tabDisabledSelectedColor;
    public static SBReference tabDisabledTextColor;
    public static SBReference tabBorderColor;
    public static SBReference tabRolloverColor;
    public static SBReference tabPaneDisabledBorderColor;
    public static SBReference tabDisabledBorderColor;
    public static BooleanReference tabRollover;
    public static BooleanReference tabFocus;
    public static BooleanReference ignoreSelectedBg;
    public static BooleanReference fixedTabs;
    public static InsetsUIResource tabInsets;
    public static InsetsUIResource tabAreaInsets;
    public static BooleanReference sliderRolloverEnabled;
    public static BooleanReference sliderFocusEnabled;
    public static SBReference sliderThumbColor;
    public static SBReference sliderThumbRolloverColor;
    public static SBReference sliderThumbPressedColor;
    public static SBReference sliderThumbDisabledColor;
    public static SBReference sliderBorderColor;
    public static SBReference sliderDarkColor;
    public static SBReference sliderLightColor;
    public static SBReference sliderBorderDisabledColor;
    public static SBReference sliderTrackColor;
    public static SBReference sliderTrackBorderColor;
    public static SBReference sliderTrackDarkColor;
    public static SBReference sliderTrackLightColor;
    public static SBReference sliderTickColor;
    public static SBReference sliderTickDisabledColor;
    public static SBReference sliderFocusColor;
    public static BooleanReference spinnerRollover;
    public static SBReference spinnerButtColor;
    public static SBReference spinnerButtRolloverColor;
    public static SBReference spinnerButtPressedColor;
    public static SBReference spinnerButtDisabledColor;
    public static SBReference spinnerBorderColor;
    public static SBReference spinnerBorderDisabledColor;
    public static SBReference spinnerArrowColor;
    public static SBReference spinnerArrowDisabledColor;
    public static IntReference spinnerSpreadLight;
    public static IntReference spinnerSpreadLightDisabled;
    public static IntReference spinnerSpreadDark;
    public static IntReference spinnerSpreadDarkDisabled;
    public static SBReference scrollTrackColor;
    public static SBReference scrollTrackDisabledColor;
    public static SBReference scrollTrackBorderColor;
    public static SBReference scrollTrackBorderDisabledColor;
    public static SBReference scrollThumbColor;
    public static SBReference scrollThumbRolloverColor;
    public static SBReference scrollThumbPressedColor;
    public static SBReference scrollThumbDisabledColor;
    public static SBReference scrollButtColor;
    public static SBReference scrollButtRolloverColor;
    public static SBReference scrollButtPressedColor;
    public static SBReference scrollButtDisabledColor;
    public static SBReference scrollArrowColor;
    public static SBReference scrollArrowDisabledColor;
    public static SBReference scrollGripLightColor;
    public static SBReference scrollGripDarkColor;
    public static SBReference scrollBorderColor;
    public static SBReference scrollBorderLightColor;
    public static SBReference scrollBorderDisabledColor;
    public static SBReference scrollLightDisabledColor;
    public static SBReference scrollPaneBorderColor;
    public static IntReference scrollSpreadLight;
    public static IntReference scrollSpreadLightDisabled;
    public static IntReference scrollSpreadDark;
    public static IntReference scrollSpreadDarkDisabled;
    public static BooleanReference scrollRollover;
    public static IntReference scrollSize;
    public static SBReference treeBgColor;
    public static SBReference treeTextColor;
    public static SBReference treeTextBgColor;
    public static SBReference treeSelectedTextColor;
    public static SBReference treeSelectedBgColor;
    public static SBReference treeLineColor;
    public static SBReference frameCaptionColor;
    public static SBReference frameCaptionDisabledColor;
    public static SBReference frameBorderColor;
    public static SBReference frameLightColor;
    public static SBReference frameBorderDisabledColor;
    public static SBReference frameLightDisabledColor;
    public static SBReference frameTitleColor;
    public static SBReference frameTitleShadowColor;
    public static SBReference frameTitleDisabledColor;
    public static SBReference frameButtColor;
    public static SBReference frameButtRolloverColor;
    public static SBReference frameButtPressedColor;
    public static SBReference frameButtDisabledColor;
    public static SBReference frameButtCloseColor;
    public static SBReference frameButtCloseRolloverColor;
    public static SBReference frameButtClosePressedColor;
    public static SBReference frameButtCloseDisabledColor;
    public static SBReference frameButtBorderColor;
    public static SBReference frameButtBorderDisabledColor;
    public static IntReference frameButtSpreadLight;
    public static IntReference frameButtSpreadDark;
    public static IntReference frameButtSpreadLightDisabled;
    public static IntReference frameButtSpreadDarkDisabled;
    public static SBReference frameButtCloseBorderColor;
    public static SBReference frameButtCloseDarkColor;
    public static SBReference frameButtCloseLightColor;
    public static SBReference frameButtCloseBorderDisabledColor;
    public static IntReference frameButtCloseSpreadLight;
    public static IntReference frameButtCloseSpreadLightDisabled;
    public static IntReference frameButtCloseSpreadDark;
    public static IntReference frameButtCloseSpreadDarkDisabled;
    public static SBReference frameSymbolColor;
    public static SBReference frameSymbolPressedColor;
    public static SBReference frameSymbolDisabledColor;
    public static SBReference frameSymbolDarkColor;
    public static SBReference frameSymbolLightColor;
    public static SBReference frameSymbolDarkDisabledColor;
    public static SBReference frameSymbolLightDisabledColor;
    public static SBReference frameSymbolCloseColor;
    public static SBReference frameSymbolClosePressedColor;
    public static SBReference frameSymbolCloseDisabledColor;
    public static SBReference frameSymbolCloseDarkColor;
    public static SBReference frameSymbolCloseDarkDisabledColor;
    public static IntReference frameSpreadDark;
    public static IntReference frameSpreadLight;
    public static IntReference frameSpreadDarkDisabled;
    public static IntReference frameSpreadLightDisabled;
    public static SBReference tableBackColor;
    public static SBReference tableHeaderBackColor;
    public static SBReference tableHeaderRolloverBackColor;
    public static SBReference tableHeaderRolloverColor;
    public static SBReference tableHeaderArrowColor;
    public static SBReference tableGridColor;
    public static SBReference tableSelectedBackColor;
    public static SBReference tableSelectedForeColor;
    public static SBReference tableBorderDarkColor;
    public static SBReference tableBorderLightColor;
    public static SBReference tableHeaderDarkColor;
    public static SBReference tableHeaderLightColor;
    public static SBReference tableFocusBorderColor;
    public static SBReference tableAlternateRowColor;
    private static final int hue = 51;
    public static SBReference separatorColor;
    public static SBReference tipBorderColor;
    public static SBReference tipBorderDis;
    public static SBReference tipBgColor;
    public static SBReference tipBgDis;
    public static SBReference tipTextColor;
    public static SBReference tipTextDis;
    public static SBReference titledBorderColor;
    public static SBReference splitPaneButtonColor;
    static Class class$de$muntjak$tinylookandfeel$TinyLookAndFeel;
    static Class class$de$muntjak$tinylookandfeel$Theme;
    public static final FilenameFilter THEMES_FILTER = new FilenameFilter() { // from class: de.muntjak.tinylookandfeel.Theme.1
        @Override // java.io.FilenameFilter
        public boolean accept(File file, String str) {
            return str.toLowerCase().endsWith(Theme.FILE_EXTENSION);
        }
    };
    public static int errorCode = 1;
    protected static final Properties MAC_FONT_MAPPINGS = new Properties();
    protected static final Properties LINUX_FONT_MAPPINGS = new Properties();
    static final URL YQ_URL = getYQ_URL();
    static final URI YQ_URI = getYQ_URI();
    public static HSBReference[] colorizer = new HSBReference[20];
    public static BooleanReference[] colorize = new BooleanReference[20];

    private Theme() {
    }

    private static URI getYQ_URI() {
        try {
            return new URI("file:/YQ%20Theme");
        } catch (URISyntaxException e2) {
            System.err.println(new StringBuffer().append("Exception creating YQ URI:\n").append((Object) e2).toString());
            return null;
        }
    }

    private static URL getYQ_URL() {
        try {
            return new URL("file:/YQ%20Theme");
        } catch (MalformedURLException e2) {
            System.err.println(new StringBuffer().append("Exception creating YQ URL:\n").append((Object) e2).toString());
            return null;
        }
    }

    public static ThemeDescription[] getAvailableThemes() throws Throwable {
        File[] fileArrListFiles;
        File[] fileArrListFiles2;
        Vector vector = new Vector();
        if (YQ_URI != null) {
            vector.add(new ThemeDescription(YQ_URI));
        }
        try {
            String systemProperty = TinyUtils.getSystemProperty("user.home");
            if (systemProperty != null && (fileArrListFiles2 = new File(systemProperty).listFiles(THEMES_FILTER)) != null && fileArrListFiles2.length > 0) {
                for (File file : fileArrListFiles2) {
                    ThemeDescription themeDescription = new ThemeDescription(file.toURI());
                    if (themeDescription.isValid()) {
                        vector.add(themeDescription);
                    }
                }
            }
        } catch (SecurityException e2) {
        }
        try {
            String systemProperty2 = TinyUtils.getSystemProperty("user.dir");
            if (systemProperty2 != null && (fileArrListFiles = new File(systemProperty2).listFiles(THEMES_FILTER)) != null && fileArrListFiles.length > 0) {
                for (File file2 : fileArrListFiles) {
                    ThemeDescription themeDescription2 = new ThemeDescription(file2.toURI());
                    if (themeDescription2.isValid()) {
                        vector.add(themeDescription2);
                    }
                }
            }
        } catch (SecurityException e3) {
        }
        addResourceTheme("/themes/Forest.theme", vector);
        addResourceTheme("/themes/Golden.theme", vector);
        addResourceTheme("/themes/Nightly.theme", vector);
        addResourceTheme("/themes/Plastic.theme", vector);
        addResourceTheme("/themes/Silver.theme", vector);
        addResourceTheme("/themes/Unicode.theme", vector);
        return vector.isEmpty() ? new ThemeDescription[0] : (ThemeDescription[]) vector.toArray(new ThemeDescription[vector.size()]);
    }

    private static void addResourceTheme(String str, Vector vector) throws Throwable {
        Class clsClass$;
        if (class$de$muntjak$tinylookandfeel$TinyLookAndFeel == null) {
            clsClass$ = class$("de.muntjak.tinylookandfeel.TinyLookAndFeel");
            class$de$muntjak$tinylookandfeel$TinyLookAndFeel = clsClass$;
        } else {
            clsClass$ = class$de$muntjak$tinylookandfeel$TinyLookAndFeel;
        }
        URL resource = clsClass$.getResource(str);
        if (resource != null) {
            ThemeDescription themeDescription = new ThemeDescription(resource);
            if (!themeDescription.isValid() || vector.contains(themeDescription)) {
                return;
            }
            vector.add(themeDescription);
        }
    }

    private static void printThemes(Vector vector) {
        System.out.println(new StringBuffer().append(vector.size()).append(" themes:").toString());
        Iterator it = vector.iterator();
        while (it.hasNext()) {
            ThemeDescription themeDescription = (ThemeDescription) it.next();
            System.out.println(new StringBuffer().append(Constants.INDENT).append(themeDescription.getURL().toExternalForm()).toString());
            System.out.println(new StringBuffer().append("  '").append(themeDescription.getName()).append("' valid: ").append(themeDescription.isValid()).toString());
        }
    }

    public static String getPlatformFont(String str) {
        String property;
        if (TinyUtils.isOSMac()) {
            String property2 = MAC_FONT_MAPPINGS.getProperty(str);
            return property2 != null ? property2 : str;
        }
        if (TinyUtils.isOSLinux() && (property = LINUX_FONT_MAPPINGS.getProperty(str)) != null) {
            return property;
        }
        return str;
    }

    private static void loadFontMappings() throws Throwable {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        Class clsClass$4;
        Class clsClass$5;
        Class clsClass$6;
        if (class$de$muntjak$tinylookandfeel$Theme == null) {
            clsClass$ = class$("de.muntjak.tinylookandfeel.Theme");
            class$de$muntjak$tinylookandfeel$Theme = clsClass$;
        } else {
            clsClass$ = class$de$muntjak$tinylookandfeel$Theme;
        }
        URL resource = clsClass$.getResource("/de/muntjak/tinylookandfeel/MacFontMappings.properties");
        if (resource != null) {
            try {
                MAC_FONT_MAPPINGS.load(resource.openStream());
            } catch (IOException e2) {
                PrintStream printStream = System.err;
                StringBuffer stringBuffer = new StringBuffer();
                if (class$de$muntjak$tinylookandfeel$Theme == null) {
                    clsClass$2 = class$("de.muntjak.tinylookandfeel.Theme");
                    class$de$muntjak$tinylookandfeel$Theme = clsClass$2;
                } else {
                    clsClass$2 = class$de$muntjak$tinylookandfeel$Theme;
                }
                printStream.println(stringBuffer.append(clsClass$2.getName()).append(": Unable to locate MacFontMappings.properties.").toString());
            }
        } else {
            PrintStream printStream2 = System.err;
            StringBuffer stringBuffer2 = new StringBuffer();
            if (class$de$muntjak$tinylookandfeel$Theme == null) {
                clsClass$6 = class$("de.muntjak.tinylookandfeel.Theme");
                class$de$muntjak$tinylookandfeel$Theme = clsClass$6;
            } else {
                clsClass$6 = class$de$muntjak$tinylookandfeel$Theme;
            }
            printStream2.println(stringBuffer2.append(clsClass$6.getName()).append(": Unable to locate MacFontMappings.properties.").toString());
        }
        if (class$de$muntjak$tinylookandfeel$Theme == null) {
            clsClass$3 = class$("de.muntjak.tinylookandfeel.Theme");
            class$de$muntjak$tinylookandfeel$Theme = clsClass$3;
        } else {
            clsClass$3 = class$de$muntjak$tinylookandfeel$Theme;
        }
        URL resource2 = clsClass$3.getResource("/de/muntjak/tinylookandfeel/LinuxFontMappings.properties");
        if (resource2 == null) {
            PrintStream printStream3 = System.err;
            StringBuffer stringBuffer3 = new StringBuffer();
            if (class$de$muntjak$tinylookandfeel$Theme == null) {
                clsClass$5 = class$("de.muntjak.tinylookandfeel.Theme");
                class$de$muntjak$tinylookandfeel$Theme = clsClass$5;
            } else {
                clsClass$5 = class$de$muntjak$tinylookandfeel$Theme;
            }
            printStream3.println(stringBuffer3.append(clsClass$5.getName()).append(": Unable to locate LinuxFontMappings.properties.").toString());
            return;
        }
        try {
            LINUX_FONT_MAPPINGS.load(resource2.openStream());
        } catch (IOException e3) {
            PrintStream printStream4 = System.err;
            StringBuffer stringBuffer4 = new StringBuffer();
            if (class$de$muntjak$tinylookandfeel$Theme == null) {
                clsClass$4 = class$("de.muntjak.tinylookandfeel.Theme");
                class$de$muntjak$tinylookandfeel$Theme = clsClass$4;
            } else {
                clsClass$4 = class$de$muntjak$tinylookandfeel$Theme;
            }
            printStream4.println(stringBuffer4.append(clsClass$4.getName()).append(": Unable to locate LinuxFontMappings.properties.").toString());
        }
    }

    private static void initData() throws Throwable {
        loadFontMappings();
        mainColor = new SBReference(new Color(0, 106, 255), 0, 0, 1, true);
        disColor = new SBReference(new Color(143, 142, 139), 0, 0, 1, true);
        backColor = new SBReference(new Color(236, 233, 216), 0, 0, 1, true);
        frameColor = new SBReference(new Color(0, 85, 255), 0, 0, 1, true);
        sub1Color = new SBReference(new Color(197, 213, 252), 0, 0, 1);
        sub2Color = new SBReference(new Color(34, 161, 34), 0, 0, 1);
        sub3Color = new SBReference(new Color(231, JPEG.APP8, 245), 0, 0, 1);
        sub4Color = new SBReference(new Color(227, 92, 60), 0, 0, 1);
        sub5Color = new SBReference(new Color(120, 123, 189), 0, 0, 1);
        sub6Color = new SBReference(new Color(248, 179, 48), 0, 0, 1);
        sub7Color = new SBReference(new Color(175, 105, 125), 0, 0, 1);
        sub8Color = new SBReference(new Color(255, 255, 255), 0, 0, 1);
        buttonFontColor = new SBReference();
        labelFontColor = new SBReference();
        menuFontColor = new SBReference();
        menuItemFontColor = new SBReference();
        radioFontColor = new SBReference();
        checkFontColor = new SBReference();
        tableFontColor = new SBReference();
        tableHeaderFontColor = new SBReference();
        titledBorderFontColor = new SBReference();
        tabFontColor = new SBReference();
        plainFont = new ColoredFont("Tahoma", 0, 11);
        boldFont = new ColoredFont("Tahoma", 1, 11);
        buttonFont = new ColoredFont(buttonFontColor);
        labelFont = new ColoredFont(labelFontColor);
        passwordFont = new ColoredFont();
        comboFont = new ColoredFont();
        listFont = new ColoredFont();
        menuFont = new ColoredFont(menuFontColor);
        menuItemFont = new ColoredFont(menuItemFontColor);
        radioFont = new ColoredFont(radioFontColor);
        checkFont = new ColoredFont(checkFontColor);
        tableFont = new ColoredFont(tableFontColor);
        tableHeaderFont = new ColoredFont(tableHeaderFontColor);
        textAreaFont = new ColoredFont();
        textFieldFont = new ColoredFont();
        textPaneFont = new ColoredFont();
        titledBorderFont = new ColoredFont(titledBorderFontColor);
        toolTipFont = new ColoredFont();
        treeFont = new ColoredFont();
        tabFontColor = new SBReference();
        tabFont = new ColoredFont(tabFontColor);
        tabFont.setBoldFont(false);
        editorFont = new ColoredFont();
        frameTitleFont = new ColoredFont("Trebuchet MS", 1, 13);
        internalFrameTitleFont = new ColoredFont("Trebuchet MS", 1, 13);
        internalPaletteTitleFont = new ColoredFont("Trebuchet MS", 1, 12);
        progressBarFont = new ColoredFont();
        progressColor = new SBReference(new Color(44, 212, 43), 43, 19, 7);
        progressTrackColor = new SBReference(new Color(255, 255, 255), 0, 100, 3);
        progressBorderColor = new SBReference(new Color(104, 104, 104), -100, -54, 3);
        progressDarkColor = new SBReference(new Color(190, 190, 190), -100, -16, 3);
        progressLightColor = new SBReference(new Color(238, 238, 238), -100, 40, 3);
        progressSelectForeColor = new SBReference(new Color(0, 0, 0), 0, -100, 3);
        progressSelectBackColor = new SBReference(new Color(0, 0, 0), 0, -100, 3);
        textBgColor = new SBReference(new Color(255, 255, 255), 0, 100, 3);
        textPaneBgColor = new SBReference(new Color(255, 255, 255), 0, 100, 3);
        editorPaneBgColor = new SBReference(new Color(255, 255, 255), 0, 100, 3);
        desktopPaneBgColor = new SBReference(new Color(212, 210, 194), 0, -10, 3);
        textTextColor = new SBReference(new Color(0, 0, 0), 0, -100, 3);
        textCaretColor = new SBReference(new Color(0, 0, 0), 0, -100, 3);
        textSelectedBgColor = new SBReference(new Color(43, 107, 197), -36, -6, 2);
        textSelectedTextColor = new SBReference(new Color(255, 255, 255), 0, 100, 3);
        textDisabledBgColor = new SBReference(new Color(240, 237, 224), 0, 20, 3);
        textNonEditableBgColor = new SBReference(new Color(240, 237, 224), 0, 20, 3);
        textBorderColor = new SBReference(new Color(128, 152, 186), -70, 23, 2);
        textBorderDisabledColor = new SBReference(new Color(201, 198, 184), 0, -15, 3);
        textInsets = new InsetsUIResource(2, 3, 2, 3);
        buttonRolloverBorder = new BooleanReference(true);
        buttonFocus = new BooleanReference(false);
        buttonFocusBorder = new BooleanReference(true);
        buttonEnter = new BooleanReference(true);
        shiftButtonText = new BooleanReference(true);
        buttonNormalColor = new SBReference(new Color(231, JPEG.APP8, 245), 0, 0, 8);
        buttonRolloverBgColor = new SBReference(new Color(239, 240, 248), 0, 33, 8);
        buttonPressedColor = new SBReference(new Color(217, 218, 230), 0, -6, 8);
        buttonDisabledColor = new SBReference(new Color(245, 244, 235), 0, 48, 3);
        buttonBorderColor = new SBReference(new Color(21, 61, 117), -30, -46, 2);
        buttonBorderDisabledColor = new SBReference(new Color(201, 198, 184), 0, -15, 3);
        buttonDisabledFgColor = new SBReference(new Color(143, 142, 139), 0, 0, 4);
        checkDisabledFgColor = new SBReference(new Color(143, 142, 139), 0, 0, 4);
        radioDisabledFgColor = new SBReference(new Color(143, 142, 139), 0, 0, 4);
        toggleSelectedBg = new SBReference(new Color(160, 182, 235), 38, -12, 6);
        buttonMargin = new InsetsUIResource(2, 12, 2, 12);
        buttonRolloverColor = new SBReference(new Color(248, 179, 48), 0, 0, 11);
        buttonDefaultColor = new SBReference(new Color(160, 182, 235), 38, -12, 6);
        buttonCheckColor = new SBReference(new Color(34, 161, 34), 0, 0, 7);
        buttonCheckDisabledColor = new SBReference(new Color(208, 205, 190), 0, -12, 3);
        checkMargin = new InsetsUIResource(2, 2, 2, 2);
        buttonSpreadLight = new IntReference(20);
        buttonSpreadDark = new IntReference(3);
        buttonSpreadLightDisabled = new IntReference(20);
        buttonSpreadDarkDisabled = new IntReference(1);
        scrollRollover = new BooleanReference(true);
        scrollSize = new IntReference(17);
        scrollTrackColor = new SBReference(new Color(TelnetCommand.GA, TelnetCommand.GA, 247), -50, 76, 3);
        scrollTrackDisabledColor = new SBReference(new Color(TelnetCommand.GA, TelnetCommand.GA, 247), -50, 76, 3);
        scrollTrackBorderColor = new SBReference(new Color(234, 231, 218), -23, 0, 3);
        scrollTrackBorderDisabledColor = new SBReference(new Color(234, 231, 218), -23, 0, 3);
        scrollThumbColor = new SBReference(new Color(197, 213, 252), 0, 0, 6);
        scrollThumbRolloverColor = new SBReference(new Color(226, 234, 254), 0, 50, 6);
        scrollThumbPressedColor = new SBReference(new Color(187, 202, 239), 0, -5, 6);
        scrollThumbDisabledColor = new SBReference(new Color(238, 238, 231), 0, -3, 6);
        scrollGripLightColor = new SBReference(new Color(238, 243, 254), 0, 71, 6);
        scrollGripDarkColor = new SBReference(new Color(171, 185, 219), 0, -13, 6);
        scrollButtColor = new SBReference(new Color(197, 213, 252), 0, 0, 6);
        scrollButtRolloverColor = new SBReference(new Color(226, 234, 254), 0, 50, 6);
        scrollButtPressedColor = new SBReference(new Color(187, 202, 239), 0, -5, 6);
        scrollButtDisabledColor = new SBReference(new Color(238, 237, 231), -48, 29, 3);
        scrollSpreadLight = new IntReference(20);
        scrollSpreadDark = new IntReference(2);
        scrollSpreadLightDisabled = new IntReference(20);
        scrollSpreadDarkDisabled = new IntReference(1);
        scrollArrowColor = new SBReference(new Color(77, 100, 132), -74, -18, 2);
        scrollArrowDisabledColor = new SBReference(new Color(193, 193, 193), -100, -15, 3);
        scrollBorderColor = new SBReference(new Color(212, 210, 194), 0, -10, 6);
        scrollBorderLightColor = new SBReference(new Color(255, 255, 255), 0, 100, 6);
        scrollBorderDisabledColor = new SBReference(new Color(JPEG.APP8, 230, 220), -41, 0, 3);
        scrollLightDisabledColor = new SBReference(new Color(JPEG.APP8, 230, 220), -41, 0, 3);
        scrollPaneBorderColor = new SBReference(new Color(201, 198, 184), 0, -15, 3);
        tabPaneBorderColor = new SBReference(new Color(143, 160, 183), -78, 28, 2);
        tabNormalColor = new SBReference(new Color(242, 240, 238), 0, 69, 3);
        tabSelectedColor = new SBReference(new Color(251, 251, 250), 0, 91, 3);
        tabDisabledColor = new SBReference(new Color(244, 242, JPEG.APP8), 0, 40, 3);
        tabDisabledSelectedColor = new SBReference(new Color(251, 251, 247), 0, 80, 3);
        tabDisabledTextColor = new SBReference(new Color(188, 187, 185), 0, 40, 4);
        tabBorderColor = new SBReference(new Color(143, 160, 183), -78, 28, 2);
        tabRolloverColor = new SBReference(new Color(255, 199, 59), 0, 0, 11);
        tabPaneDisabledBorderColor = new SBReference(new Color(208, 205, 190), 0, -12, 3);
        tabDisabledBorderColor = new SBReference(new Color(208, 205, 190), 0, -12, 3);
        tabRollover = new BooleanReference(true);
        tabFocus = new BooleanReference(true);
        ignoreSelectedBg = new BooleanReference(false);
        fixedTabs = new BooleanReference(true);
        tabInsets = new InsetsUIResource(1, 6, 4, 6);
        tabAreaInsets = new InsetsUIResource(4, 2, 0, 0);
        sliderRolloverEnabled = new BooleanReference(true);
        sliderFocusEnabled = new BooleanReference(true);
        sliderThumbColor = new SBReference(new Color(245, 244, 235), 0, 49, 3);
        sliderThumbRolloverColor = new SBReference(new Color(233, 166, 0), 100, -26, 11);
        sliderThumbPressedColor = new SBReference(new Color(244, 243, 239), -50, 50, 3);
        sliderThumbDisabledColor = new SBReference(new Color(245, 243, 234), 0, 45, 3);
        sliderBorderColor = new SBReference(new Color(176, 189, 207), -76, 50, 2);
        sliderDarkColor = new SBReference(new Color(119, 130, 146), -89, 4, 2);
        sliderLightColor = new SBReference(new Color(27, 155, 27), 16, -7, 7);
        sliderBorderDisabledColor = new SBReference(new Color(214, 212, 198), -6, -9, 3);
        sliderTrackColor = new SBReference(new Color(240, 237, 224), 0, 20, 3);
        sliderTrackBorderColor = new SBReference(new Color(157, 156, 150), -53, -32, 3);
        sliderTrackDarkColor = new SBReference(new Color(242, 241, JPEG.APP8), -22, 39, 3);
        sliderTrackLightColor = new SBReference(new Color(255, 255, 255), 0, 100, 3);
        sliderTickColor = new SBReference(new Color(118, 117, 108), 0, -50, 3);
        sliderTickDisabledColor = new SBReference(new Color(174, 174, 171), 0, 28, 4);
        sliderFocusColor = new SBReference(new Color(113, 112, 104), 0, -52, 3);
        spinnerRollover = new BooleanReference(false);
        spinnerButtColor = new SBReference(new Color(198, 213, 250), 0, 0, 6);
        spinnerButtRolloverColor = new SBReference(new Color(JPEG.APP8, 238, 254), 0, 60, 6);
        spinnerButtPressedColor = new SBReference(new Color(175, 190, 224), 0, -11, 6);
        spinnerButtDisabledColor = new SBReference(new Color(242, 240, 228), 0, 30, 3);
        spinnerSpreadLight = new IntReference(20);
        spinnerSpreadDark = new IntReference(3);
        spinnerSpreadLightDisabled = new IntReference(20);
        spinnerSpreadDarkDisabled = new IntReference(1);
        spinnerBorderColor = new SBReference(new Color(128, 152, 186), -70, 23, 2);
        spinnerBorderDisabledColor = new SBReference(new Color(215, 212, 197), 0, -9, 3);
        spinnerArrowColor = new SBReference(new Color(77, 100, 132), -74, -18, 2);
        spinnerArrowDisabledColor = new SBReference(new Color(212, 210, 194), 0, -10, 3);
        comboBorderColor = new SBReference(new Color(128, 152, 186), -70, 23, 2);
        comboBorderDisabledColor = new SBReference(new Color(201, 198, 184), 0, -15, 3);
        comboSelectedBgColor = new SBReference(new Color(43, 107, 197), -36, -6, 2);
        comboSelectedTextColor = new SBReference(new Color(255, 255, 255), 0, 100, 3);
        comboFocusBgColor = new SBReference(new Color(43, 107, 197), 0, 0, 1);
        comboBgColor = new SBReference(new Color(255, 255, 255), 0, 100, 3);
        comboTextColor = new SBReference(new Color(0, 0, 0), 0, -100, 3);
        comboButtColor = new SBReference(new Color(197, 213, 252), 0, 0, 6);
        comboButtRolloverColor = new SBReference(new Color(226, 234, 254), 0, 50, 6);
        comboButtPressedColor = new SBReference(new Color(175, 190, 224), 0, -11, 6);
        comboButtDisabledColor = new SBReference(new Color(238, 237, 231), -48, 29, 3);
        comboSpreadLight = new IntReference(20);
        comboSpreadDark = new IntReference(3);
        comboSpreadLightDisabled = new IntReference(20);
        comboSpreadDarkDisabled = new IntReference(1);
        comboButtBorderColor = new SBReference(new Color(212, 210, 194), 0, -10, 6);
        comboButtBorderDisabledColor = new SBReference(new Color(JPEG.APP8, 230, 220), -41, 0, 3);
        comboArrowColor = new SBReference(new Color(77, 100, 132), -74, -18, 2);
        comboArrowDisabledColor = new SBReference(new Color(203, 200, 186), 0, -14, 3);
        comboInsets = new InsetsUIResource(2, 2, 2, 2);
        comboRollover = new BooleanReference(false);
        comboFocus = new BooleanReference(false);
        menuBarColor = new SBReference(new Color(238, 237, 230), -43, 28, 3);
        menuItemSelectedTextColor = new SBReference(new Color(0, 0, 0), 0, -100, 3);
        menuPopupColor = new SBReference(new Color(255, 255, 255), 0, 100, 3);
        menuRolloverBgColor = new SBReference(new Color(189, 208, 234), -50, 66, 2);
        menuRolloverFgColor = new SBReference(new Color(0, 0, 0), 0, -100, 3);
        menuDisabledFgColor = new SBReference(new Color(143, 142, 139), 0, 0, 4);
        menuItemDisabledFgColor = new SBReference(new Color(143, 142, 139), 0, 0, 4);
        menuItemRolloverColor = new SBReference(new Color(189, 208, 234), -50, 66, 2);
        menuBorderColor = new SBReference(new Color(173, 170, 153), 4, -28, 3);
        menuInnerHilightColor = new SBReference(new Color(255, 255, 255), 0, 100, 3);
        menuInnerShadowColor = new SBReference(new Color(213, 212, 207), -70, -7, 3);
        menuOuterHilightColor = new SBReference(new Color(173, 170, 153), 4, -28, 3);
        menuOuterShadowColor = new SBReference(new Color(173, 170, 153), 4, -28, 3);
        menuIconColor = new SBReference(new Color(0, 0, 0), 0, -100, 3);
        menuIconRolloverColor = new SBReference(new Color(0, 0, 0), 0, -100, 3);
        menuIconDisabledColor = new SBReference(new Color(165, 163, 151), 0, -30, 3);
        menuSeparatorColor = new SBReference(new Color(173, 170, 153), 4, -28, 3);
        menuRollover = new BooleanReference(true);
        menuPopupShadow = new BooleanReference(false);
        menuAllowTwoIcons = new BooleanReference(false);
        toolBarColor = new SBReference(new Color(239, 237, 229), -35, 28, 3);
        toolBarLightColor = new SBReference(new Color(255, 255, 255), 0, 100, 3);
        toolBarDarkColor = new SBReference(new Color(214, 210, 187), 10, -11, 3);
        toolButtColor = new SBReference(new Color(239, 237, 229), -35, 28, 3);
        toolButtSelectedColor = new SBReference(new Color(243, 242, 239), -51, 52, 3);
        toolButtRolloverColor = new SBReference(new Color(251, 251, 248), -30, 81, 3);
        toolButtPressedColor = new SBReference(new Color(225, 224, 218), -58, -2, 3);
        toolGripDarkColor = new SBReference(new Color(167, 167, 163), -70, -27, 3);
        toolGripLightColor = new SBReference(new Color(255, 255, 255), 0, 100, 3);
        toolSeparatorColor = new SBReference(new Color(167, 167, 163), -70, -27, 3);
        toolBorderColor = new SBReference(new Color(239, 237, 229), -35, 28, 3);
        toolBorderPressedColor = new SBReference(new Color(122, 144, 174), -76, 16, 2);
        toolBorderRolloverColor = new SBReference(new Color(122, 144, 174), -76, 16, 2);
        toolBorderSelectedColor = new SBReference(new Color(122, 144, 174), -76, 16, 2);
        toolMargin = new InsetsUIResource(5, 5, 5, 5);
        toolFocus = new BooleanReference(false);
        toolRollover = new BooleanReference(true);
        listBgColor = new SBReference(new Color(255, 255, 255), 0, 100, 3);
        listTextColor = new SBReference(new Color(0, 0, 0), 0, -100, 3);
        listSelectedBgColor = new SBReference(new Color(43, 107, 197), -36, -6, 2);
        listSelectedTextColor = new SBReference(new Color(255, 255, 255), 0, 100, 3);
        listFocusBorderColor = new SBReference(new Color(179, 211, 255), 100, 70, 2);
        treeBgColor = new SBReference(new Color(255, 255, 255), 0, 100, 3);
        treeTextColor = new SBReference(new Color(0, 0, 0), 0, -100, 3);
        treeTextBgColor = new SBReference(new Color(255, 255, 255), 0, 100, 3);
        treeSelectedTextColor = new SBReference(new Color(255, 255, 255), 0, 100, 3);
        treeSelectedBgColor = new SBReference(new Color(43, 107, 197), -36, -6, 2);
        treeLineColor = new SBReference(new Color(208, 205, 190), 0, -12, 3);
        frameCaptionColor = new SBReference(new Color(13, 94, 255), 0, 5, 5);
        frameCaptionDisabledColor = new SBReference(new Color(122, 159, 223), -25, 41, 5);
        frameBorderColor = new SBReference(new Color(0, 60, 161), 0, -30, 5);
        frameLightColor = new SBReference(new Color(0, 68, 184), 0, -20, 5);
        frameBorderDisabledColor = new SBReference(new Color(74, 125, 212), -25, 20, 5);
        frameLightDisabledColor = new SBReference(new Color(99, 144, 233), -25, 30, 5);
        frameTitleColor = new SBReference(new Color(255, 255, 255), 0, 100, 5);
        frameTitleShadowColor = new SBReference(new Color(0, 43, 128), 0, -50, 5);
        frameTitleDisabledColor = new SBReference(new Color(216, 226, 248), -29, 82, 5);
        frameButtColor = new SBReference(new Color(13, 94, 255), 0, 5, 5);
        frameButtRolloverColor = new SBReference(new Color(51, 119, 255), 0, 20, 5);
        frameButtPressedColor = new SBReference(new Color(0, 68, 204), 0, -20, 5);
        frameButtDisabledColor = new SBReference(new Color(63, 120, 233), -21, 16, 5);
        frameButtSpreadLight = new IntReference(8);
        frameButtSpreadDark = new IntReference(2);
        frameButtSpreadLightDisabled = new IntReference(5);
        frameButtSpreadDarkDisabled = new IntReference(2);
        frameButtCloseColor = new SBReference(new Color(227, 92, 60), 0, 0, 9);
        frameButtCloseRolloverColor = new SBReference(new Color(233, 125, 99), 0, 20, 9);
        frameButtClosePressedColor = new SBReference(new Color(193, 78, 51), 0, -15, 9);
        frameButtCloseDisabledColor = new SBReference(new Color(175, 105, 125), 0, 0, 12);
        frameButtCloseSpreadLight = new IntReference(8);
        frameButtCloseSpreadDark = new IntReference(2);
        frameButtCloseSpreadLightDisabled = new IntReference(5);
        frameButtCloseSpreadDarkDisabled = new IntReference(2);
        frameButtBorderColor = new SBReference(new Color(255, 255, 255), 0, 100, 5);
        frameButtBorderDisabledColor = new SBReference(new Color(190, 206, 238), -42, 68, 5);
        frameSymbolColor = new SBReference(new Color(255, 255, 255), 0, 100, 5);
        frameSymbolPressedColor = new SBReference(new Color(255, 255, 255), 0, 100, 5);
        frameSymbolDisabledColor = new SBReference(new Color(255, 255, 255), 0, 100, 5);
        frameSymbolDarkColor = new SBReference(new Color(255, 255, 255), 0, 100, 5);
        frameSymbolLightColor = new SBReference(new Color(13, 94, 255), 0, 5, 5);
        frameSymbolDarkDisabledColor = new SBReference(new Color(255, 255, 255), 0, 100, 5);
        frameSymbolLightDisabledColor = new SBReference(new Color(63, 120, 233), -21, 16, 5);
        frameButtCloseBorderColor = new SBReference(new Color(255, 255, 255), 0, 100, 5);
        frameButtCloseDarkColor = new SBReference(new Color(174, 51, 20), 50, -32, 9);
        frameButtCloseLightColor = new SBReference(new Color(226, 88, 55), 11, -2, 9);
        frameButtCloseBorderDisabledColor = new SBReference(new Color(190, 206, 238), -42, 68, 5);
        frameSymbolCloseColor = new SBReference(new Color(255, 255, 255), 0, 100, 5);
        frameSymbolClosePressedColor = new SBReference(new Color(231, 180, 168), -24, 50, 9);
        frameSymbolCloseDisabledColor = new SBReference(new Color(255, 255, 255), 0, 100, 5);
        frameSymbolCloseDarkColor = new SBReference(new Color(227, 92, 60), 0, 0, 9);
        frameSymbolCloseDarkDisabledColor = new SBReference(new Color(175, 105, 125), 0, 0, 12);
        frameSpreadDark = new IntReference(3);
        frameSpreadLight = new IntReference(2);
        frameSpreadDarkDisabled = new IntReference(2);
        frameSpreadLightDisabled = new IntReference(2);
        tableBackColor = new SBReference(new Color(255, 255, 255), 0, 100, 3);
        tableHeaderBackColor = new SBReference(new Color(236, 233, 216), 0, 0, 3);
        tableHeaderRolloverBackColor = new SBReference(new Color(TelnetCommand.GA, 248, 243), 0, 70, 3);
        tableHeaderRolloverColor = new SBReference(new Color(248, 179, 48), 0, 0, 11);
        tableGridColor = new SBReference(new Color(167, 166, 160), -50, -28, 3);
        tableHeaderArrowColor = new SBReference(new Color(167, 166, 160), -50, -28, 3);
        tableSelectedBackColor = new SBReference(new Color(213, 211, 204), -50, -8, 3);
        tableSelectedForeColor = new SBReference(new Color(0, 0, 0), 0, -100, 3);
        tableBorderDarkColor = new SBReference(new Color(167, 166, 160), -50, -28, 3);
        tableBorderLightColor = new SBReference(new Color(255, 255, 255), 0, 100, 3);
        tableHeaderDarkColor = new SBReference(new Color(189, 186, 173), 0, -20, 3);
        tableHeaderLightColor = new SBReference(new Color(255, 255, 255), 0, 100, 3);
        tableFocusBorderColor = new SBReference(new Color(185, 184, 177), -50, -20, 3);
        tableAlternateRowColor = new SBReference(new Color(255, 255, 255), 0, 100, 3);
        for (int i2 = 0; i2 < 20; i2++) {
            colorizer[i2] = new HSBReference(51, 25, 0, 3);
            colorize[i2] = new BooleanReference(false);
        }
        separatorColor = new SBReference(new Color(167, 167, 163), -70, -27, 3);
        tipBorderColor = new SBReference(new Color(0, 0, 0), 0, -100, 3);
        tipBorderDis = new SBReference(new Color(143, 141, 139), 0, 0, 4);
        tipBgColor = new SBReference(new Color(255, 255, 225), 0, 0, 1);
        tipBgDis = new SBReference(new Color(236, 233, 216), 0, 0, 3);
        tipTextColor = new SBReference(new Color(0, 0, 0), 0, -100, 3);
        tipTextDis = new SBReference(new Color(143, 141, 139), 0, 0, 4);
        titledBorderColor = new SBReference(new Color(165, 163, 151), 0, -30, 3);
        splitPaneButtonColor = new SBReference(new Color(170, 168, 156), 0, -28, 3);
    }

    public static void loadYQTheme() {
        mainColor.update(new Color(0, 106, 255), 0, 0, 1);
        disColor.update(new Color(143, 142, 139), 0, 0, 1);
        backColor.update(new Color(236, 233, 216), 0, 0, 1);
        frameColor.update(new Color(0, 85, 255), 0, 0, 1);
        sub1Color.update(new Color(197, 213, 252), 0, 0, 1);
        sub2Color.update(new Color(34, 161, 34), 0, 0, 1);
        sub3Color.update(new Color(231, JPEG.APP8, 245), 0, 0, 1);
        sub4Color.update(new Color(227, 92, 60), 0, 0, 1);
        sub5Color.update(new Color(120, 123, 189), 0, 0, 1);
        sub6Color.update(new Color(248, 179, 48), 0, 0, 1);
        sub7Color.update(new Color(175, 105, 125), 0, 0, 1);
        sub8Color.update(new Color(255, 255, 255), 0, 0, 1);
        buttonFontColor.update(Color.BLACK);
        labelFontColor.update(Color.BLACK);
        menuFontColor.update(Color.BLACK);
        menuItemFontColor.update(Color.BLACK);
        radioFontColor.update(Color.BLACK);
        checkFontColor.update(Color.BLACK);
        tableFontColor.update(Color.BLACK);
        tableHeaderFontColor.update(Color.BLACK);
        titledBorderFontColor.update(Color.BLACK);
        tabFontColor.update(Color.BLACK);
        plainFont.update("Tahoma", 0, 11);
        boldFont.update("Tahoma", 1, 11);
        buttonFont.update(buttonFontColor);
        labelFont.update(labelFontColor);
        passwordFont.update((SBReference) null);
        comboFont.update((SBReference) null);
        listFont.update((SBReference) null);
        menuFont.update(menuFontColor);
        menuItemFont.update(menuItemFontColor);
        radioFont.update(radioFontColor);
        checkFont.update(checkFontColor);
        tableFont.update(tableFontColor);
        tableHeaderFont.update(tableHeaderFontColor);
        textAreaFont.update((SBReference) null);
        textFieldFont.update((SBReference) null);
        textPaneFont.update((SBReference) null);
        titledBorderFont.update(titledBorderFontColor);
        toolTipFont.update((SBReference) null);
        treeFont.update((SBReference) null);
        tabFontColor.update(Color.BLACK);
        tabFont.update(tabFontColor);
        tabFont.setBoldFont(false);
        editorFont.update((SBReference) null);
        frameTitleFont.update("Trebuchet MS", 1, 13);
        internalFrameTitleFont.update("Trebuchet MS", 1, 13);
        internalPaletteTitleFont.update("Trebuchet MS", 1, 12);
        progressBarFont.update((SBReference) null);
        progressColor.update(new Color(44, 212, 43), 43, 19, 7);
        progressTrackColor.update(new Color(255, 255, 255), 0, 100, 3);
        progressBorderColor.update(new Color(104, 104, 104), -100, -54, 3);
        progressDarkColor.update(new Color(190, 190, 190), -100, -16, 3);
        progressLightColor.update(new Color(238, 238, 238), -100, 40, 3);
        progressSelectForeColor.update(new Color(0, 0, 0), 0, -100, 3);
        progressSelectBackColor.update(new Color(0, 0, 0), 0, -100, 3);
        textBgColor.update(new Color(255, 255, 255), 0, 100, 3);
        textPaneBgColor.update(new Color(255, 255, 255), 0, 100, 3);
        editorPaneBgColor.update(new Color(255, 255, 255), 0, 100, 3);
        desktopPaneBgColor.update(new Color(212, 210, 194), 0, -10, 3);
        textTextColor.update(new Color(0, 0, 0), 0, -100, 3);
        textCaretColor.update(new Color(0, 0, 0), 0, -100, 3);
        textSelectedBgColor.update(new Color(43, 107, 197), -36, -6, 2);
        textSelectedTextColor.update(new Color(255, 255, 255), 0, 100, 3);
        textDisabledBgColor.update(new Color(240, 237, 224), 0, 20, 3);
        textNonEditableBgColor.update(new Color(240, 237, 224), 0, 20, 3);
        textBorderColor.update(new Color(128, 152, 186), -70, 23, 2);
        textBorderDisabledColor.update(new Color(201, 198, 184), 0, -15, 3);
        textInsets.top = 2;
        textInsets.left = 3;
        textInsets.bottom = 2;
        textInsets.right = 3;
        buttonRolloverBorder.setValue(true);
        buttonFocus.setValue(false);
        buttonFocusBorder.setValue(true);
        buttonEnter.setValue(true);
        shiftButtonText.setValue(true);
        buttonNormalColor.update(new Color(231, JPEG.APP8, 245), 0, 0, 8);
        buttonRolloverBgColor.update(new Color(239, 240, 248), 0, 33, 8);
        buttonPressedColor.update(new Color(217, 218, 230), 0, -6, 8);
        buttonDisabledColor.update(new Color(245, 244, 235), 0, 48, 3);
        buttonBorderColor.update(new Color(21, 61, 117), -30, -46, 2);
        buttonBorderDisabledColor.update(new Color(201, 198, 184), 0, -15, 3);
        buttonDisabledFgColor.update(new Color(143, 142, 139), 0, 0, 4);
        checkDisabledFgColor.update(new Color(143, 142, 139), 0, 0, 4);
        radioDisabledFgColor.update(new Color(143, 142, 139), 0, 0, 4);
        toggleSelectedBg.update(new Color(160, 182, 235), 38, -12, 6);
        buttonMargin.top = 2;
        buttonMargin.left = 12;
        buttonMargin.bottom = 2;
        buttonMargin.right = 12;
        buttonRolloverColor.update(new Color(248, 179, 48), 0, 0, 11);
        buttonDefaultColor.update(new Color(160, 182, 235), 38, -12, 6);
        buttonCheckColor.update(new Color(34, 161, 34), 0, 0, 7);
        buttonCheckDisabledColor.update(new Color(208, 205, 190), 0, -12, 3);
        checkMargin.top = 2;
        checkMargin.left = 2;
        checkMargin.bottom = 2;
        checkMargin.right = 2;
        buttonSpreadLight.setValue(20);
        buttonSpreadDark.setValue(3);
        buttonSpreadLightDisabled.setValue(20);
        buttonSpreadDarkDisabled.setValue(1);
        scrollRollover.setValue(true);
        scrollSize.setValue(17);
        scrollTrackColor.update(new Color(TelnetCommand.GA, TelnetCommand.GA, 247), -50, 76, 3);
        scrollTrackDisabledColor.update(new Color(TelnetCommand.GA, TelnetCommand.GA, 247), -50, 76, 3);
        scrollTrackBorderColor.update(new Color(234, 231, 218), -23, 0, 3);
        scrollTrackBorderDisabledColor.update(new Color(234, 231, 218), -23, 0, 3);
        scrollThumbColor.update(new Color(197, 213, 252), 0, 0, 6);
        scrollThumbRolloverColor.update(new Color(226, 234, 254), 0, 50, 6);
        scrollThumbPressedColor.update(new Color(187, 202, 239), 0, -5, 6);
        scrollThumbDisabledColor.update(new Color(238, 238, 231), 0, -3, 6);
        scrollGripLightColor.update(new Color(238, 243, 254), 0, 71, 6);
        scrollGripDarkColor.update(new Color(171, 185, 219), 0, -13, 6);
        scrollButtColor.update(new Color(197, 213, 252), 0, 0, 6);
        scrollButtRolloverColor.update(new Color(226, 234, 254), 0, 50, 6);
        scrollButtPressedColor.update(new Color(187, 202, 239), 0, -5, 6);
        scrollButtDisabledColor.update(new Color(238, 237, 231), -48, 29, 3);
        scrollSpreadLight.setValue(20);
        scrollSpreadDark.setValue(2);
        scrollSpreadLightDisabled.setValue(20);
        scrollSpreadDarkDisabled.setValue(1);
        scrollArrowColor.update(new Color(77, 100, 132), -74, -18, 2);
        scrollArrowDisabledColor.update(new Color(193, 193, 193), -100, -15, 3);
        scrollBorderColor.update(new Color(212, 210, 194), 0, -10, 6);
        scrollBorderLightColor.update(new Color(255, 255, 255), 0, 100, 6);
        scrollBorderDisabledColor.update(new Color(JPEG.APP8, 230, 220), -41, 0, 3);
        scrollLightDisabledColor.update(new Color(JPEG.APP8, 230, 220), -41, 0, 3);
        scrollPaneBorderColor.update(new Color(201, 198, 184), 0, -15, 3);
        tabPaneBorderColor.update(new Color(143, 160, 183), -78, 28, 2);
        tabNormalColor.update(new Color(242, 240, 238), 0, 69, 3);
        tabSelectedColor.update(new Color(251, 251, 250), 0, 91, 3);
        tabDisabledColor.update(new Color(244, 242, JPEG.APP8), 0, 40, 3);
        tabDisabledSelectedColor.update(new Color(251, 251, 247), 0, 80, 3);
        tabDisabledTextColor.update(new Color(188, 187, 185), 0, 40, 4);
        tabBorderColor.update(new Color(143, 160, 183), -78, 28, 2);
        tabRolloverColor.update(new Color(255, 199, 59), 0, 0, 11);
        tabRollover.setValue(true);
        tabFocus.setValue(true);
        ignoreSelectedBg.setValue(false);
        fixedTabs.setValue(true);
        tabInsets.top = 1;
        tabInsets.left = 6;
        tabInsets.bottom = 4;
        tabInsets.right = 6;
        tabAreaInsets.top = 4;
        tabAreaInsets.left = 2;
        tabAreaInsets.bottom = 0;
        tabAreaInsets.right = 0;
        sliderRolloverEnabled.setValue(true);
        sliderFocusEnabled.setValue(true);
        sliderThumbColor.update(new Color(245, 244, 235), 0, 49, 3);
        sliderThumbRolloverColor.update(new Color(233, 166, 0), 100, -26, 11);
        sliderThumbPressedColor.update(new Color(244, 243, 239), -50, 50, 3);
        sliderThumbDisabledColor.update(new Color(245, 243, 234), 0, 45, 3);
        sliderBorderColor.update(new Color(176, 189, 207), -76, 50, 2);
        sliderDarkColor.update(new Color(119, 130, 146), -89, 4, 2);
        sliderLightColor.update(new Color(27, 155, 27), 16, -7, 7);
        sliderBorderDisabledColor.update(new Color(214, 212, 198), -6, -9, 3);
        sliderTrackColor.update(new Color(240, 237, 224), 0, 20, 3);
        sliderTrackBorderColor.update(new Color(157, 156, 150), -53, -32, 3);
        sliderTrackDarkColor.update(new Color(242, 241, JPEG.APP8), -22, 39, 3);
        sliderTrackLightColor.update(new Color(255, 255, 255), 0, 100, 3);
        sliderTickColor.update(new Color(118, 117, 108), 0, -50, 3);
        sliderTickDisabledColor.update(new Color(174, 174, 171), 0, 28, 4);
        sliderFocusColor.update(new Color(113, 112, 104), 0, -52, 3);
        spinnerRollover.setValue(false);
        spinnerButtColor.update(new Color(198, 213, 250), 0, 0, 6);
        spinnerButtRolloverColor.update(new Color(JPEG.APP8, 238, 254), 0, 60, 6);
        spinnerButtPressedColor.update(new Color(175, 190, 224), 0, -11, 6);
        spinnerButtDisabledColor.update(new Color(242, 240, 228), 0, 30, 3);
        spinnerSpreadLight.setValue(20);
        spinnerSpreadDark.setValue(3);
        spinnerSpreadLightDisabled.setValue(20);
        spinnerSpreadDarkDisabled.setValue(1);
        spinnerBorderColor.update(new Color(128, 152, 186), -70, 23, 2);
        spinnerBorderDisabledColor.update(new Color(215, 212, 197), 0, -9, 3);
        spinnerArrowColor.update(new Color(77, 100, 132), -74, -18, 2);
        spinnerArrowDisabledColor.update(new Color(212, 210, 194), 0, -10, 3);
        comboBorderColor.update(new Color(128, 152, 186), -70, 23, 2);
        comboBorderDisabledColor.update(new Color(201, 198, 184), 0, -15, 3);
        comboSelectedBgColor.update(new Color(43, 107, 197), -36, -6, 2);
        comboSelectedTextColor.update(new Color(255, 255, 255), 0, 100, 3);
        comboFocusBgColor.update(new Color(43, 107, 197), 0, 0, 1);
        comboBgColor.update(new Color(255, 255, 255), 0, 100, 3);
        comboTextColor.update(new Color(0, 0, 0), 0, -100, 3);
        comboButtColor.update(new Color(197, 213, 252), 0, 0, 6);
        comboButtRolloverColor.update(new Color(226, 234, 254), 0, 50, 6);
        comboButtPressedColor.update(new Color(175, 190, 224), 0, -11, 6);
        comboButtDisabledColor.update(new Color(238, 237, 231), -48, 29, 3);
        comboSpreadLight.setValue(20);
        comboSpreadDark.setValue(3);
        comboSpreadLightDisabled.setValue(20);
        comboSpreadDarkDisabled.setValue(1);
        comboButtBorderColor.update(new Color(212, 210, 194), 0, -10, 6);
        comboButtBorderDisabledColor.update(new Color(JPEG.APP8, 230, 220), -41, 0, 3);
        comboArrowColor.update(new Color(77, 100, 132), -74, -18, 2);
        comboArrowDisabledColor.update(new Color(203, 200, 186), 0, -14, 3);
        comboInsets.top = 2;
        comboInsets.left = 2;
        comboInsets.bottom = 2;
        comboInsets.right = 2;
        comboRollover.setValue(false);
        comboFocus.setValue(false);
        menuBarColor.update(new Color(238, 237, 230), -43, 28, 3);
        menuItemSelectedTextColor.update(new Color(0, 0, 0), 0, -100, 3);
        menuPopupColor.update(new Color(255, 255, 255), 0, 100, 3);
        menuRolloverBgColor.update(new Color(189, 208, 234), -50, 66, 2);
        menuRolloverFgColor.update(new Color(0, 0, 0), 0, -100, 3);
        menuDisabledFgColor.update(new Color(143, 142, 139), 0, 0, 4);
        menuItemDisabledFgColor.update(new Color(143, 142, 139), 0, 0, 4);
        menuItemRolloverColor.update(new Color(189, 208, 234), -50, 66, 2);
        menuBorderColor.update(new Color(173, 170, 153), 4, -28, 3);
        menuInnerHilightColor.update(new Color(255, 255, 255), 0, 100, 3);
        menuInnerShadowColor.update(new Color(213, 212, 207), -70, -7, 3);
        menuOuterHilightColor.update(new Color(173, 170, 153), 4, -28, 3);
        menuOuterShadowColor.update(new Color(173, 170, 153), 4, -28, 3);
        menuIconColor.update(new Color(0, 0, 0), 0, -100, 3);
        menuIconRolloverColor.update(new Color(0, 0, 0), 0, -100, 3);
        menuIconDisabledColor.update(new Color(165, 163, 151), 0, -30, 3);
        menuSeparatorColor.update(new Color(173, 170, 153), 4, -28, 3);
        menuRollover.setValue(true);
        menuPopupShadow.setValue(false);
        menuAllowTwoIcons.setValue(false);
        toolBarColor.update(new Color(239, 237, 229), -35, 28, 3);
        toolBarLightColor.update(new Color(255, 255, 255), 0, 100, 3);
        toolBarDarkColor.update(new Color(214, 210, 187), 10, -11, 3);
        toolButtColor.update(new Color(239, 237, 229), -35, 28, 3);
        toolButtSelectedColor.update(new Color(243, 242, 239), -51, 52, 3);
        toolButtRolloverColor.update(new Color(251, 251, 248), -30, 81, 3);
        toolButtPressedColor.update(new Color(225, 224, 218), -58, -2, 3);
        toolGripDarkColor.update(new Color(167, 167, 163), -70, -27, 3);
        toolGripLightColor.update(new Color(255, 255, 255), 0, 100, 3);
        toolSeparatorColor.update(new Color(167, 167, 163), -70, -27, 3);
        toolBorderColor.update(new Color(239, 237, 229), -35, 28, 3);
        toolBorderPressedColor.update(new Color(122, 144, 174), -76, 16, 2);
        toolBorderRolloverColor.update(new Color(122, 144, 174), -76, 16, 2);
        toolBorderSelectedColor.update(new Color(122, 144, 174), -76, 16, 2);
        toolMargin.top = 5;
        toolMargin.left = 5;
        toolMargin.bottom = 5;
        toolMargin.right = 5;
        toolFocus.setValue(false);
        toolRollover.setValue(true);
        listBgColor.update(new Color(255, 255, 255), 0, 100, 3);
        listTextColor.update(new Color(0, 0, 0), 0, -100, 3);
        listSelectedBgColor.update(new Color(43, 107, 197), -36, -6, 2);
        listSelectedTextColor.update(new Color(255, 255, 255), 0, 100, 3);
        listFocusBorderColor.update(new Color(179, 211, 255), 100, 70, 2);
        treeBgColor.update(new Color(255, 255, 255), 0, 100, 3);
        treeTextColor.update(new Color(0, 0, 0), 0, -100, 3);
        treeTextBgColor.update(new Color(255, 255, 255), 0, 100, 3);
        treeSelectedTextColor.update(new Color(255, 255, 255), 0, 100, 3);
        treeSelectedBgColor.update(new Color(43, 107, 197), -36, -6, 2);
        treeLineColor.update(new Color(208, 205, 190), 0, -12, 3);
        frameCaptionColor.update(new Color(13, 94, 255), 0, 5, 5);
        frameCaptionDisabledColor.update(new Color(122, 159, 223), -25, 41, 5);
        frameBorderColor.update(new Color(0, 60, 161), 0, -30, 5);
        frameLightColor.update(new Color(0, 68, 184), 0, -20, 5);
        frameBorderDisabledColor.update(new Color(74, 125, 212), -25, 20, 5);
        frameLightDisabledColor.update(new Color(99, 144, 233), -25, 30, 5);
        frameTitleColor.update(new Color(255, 255, 255), 0, 100, 5);
        frameTitleShadowColor.update(new Color(0, 43, 128), 0, -50, 5);
        frameTitleDisabledColor.update(new Color(216, 226, 248), -29, 82, 5);
        frameButtColor.update(new Color(13, 94, 255), 0, 5, 5);
        frameButtRolloverColor.update(new Color(51, 119, 255), 0, 20, 5);
        frameButtPressedColor.update(new Color(0, 68, 204), 0, -20, 5);
        frameButtDisabledColor.update(new Color(63, 120, 233), -21, 16, 5);
        frameButtSpreadLight.setValue(8);
        frameButtSpreadDark.setValue(2);
        frameButtSpreadLightDisabled.setValue(5);
        frameButtSpreadDarkDisabled.setValue(2);
        frameButtCloseColor.update(new Color(227, 92, 60), 0, 0, 9);
        frameButtCloseRolloverColor.update(new Color(233, 125, 99), 0, 20, 9);
        frameButtClosePressedColor.update(new Color(193, 78, 51), 0, -15, 9);
        frameButtCloseDisabledColor.update(new Color(175, 105, 125), 0, 0, 12);
        frameButtCloseSpreadLight.setValue(8);
        frameButtCloseSpreadDark.setValue(2);
        frameButtCloseSpreadLightDisabled.setValue(5);
        frameButtCloseSpreadDarkDisabled.setValue(2);
        frameButtBorderColor.update(new Color(255, 255, 255), 0, 100, 5);
        frameButtBorderDisabledColor.update(new Color(190, 206, 238), -42, 68, 5);
        frameSymbolColor.update(new Color(255, 255, 255), 0, 100, 5);
        frameSymbolPressedColor.update(new Color(255, 255, 255), 0, 100, 5);
        frameSymbolDisabledColor.update(new Color(255, 255, 255), 0, 100, 5);
        frameSymbolDarkColor.update(new Color(255, 255, 255), 0, 100, 5);
        frameSymbolLightColor.update(new Color(13, 94, 255), 0, 5, 5);
        frameSymbolDarkDisabledColor.update(new Color(255, 255, 255), 0, 100, 5);
        frameSymbolLightDisabledColor.update(new Color(63, 120, 233), -21, 16, 5);
        frameButtCloseBorderColor.update(new Color(255, 255, 255), 0, 100, 5);
        frameButtCloseDarkColor.update(new Color(174, 51, 20), 50, -32, 9);
        frameButtCloseLightColor.update(new Color(226, 88, 55), 11, -2, 9);
        frameButtCloseBorderDisabledColor.update(new Color(190, 206, 238), -42, 68, 5);
        frameSymbolCloseColor.update(new Color(255, 255, 255), 0, 100, 5);
        frameSymbolClosePressedColor.update(new Color(231, 180, 168), -24, 50, 9);
        frameSymbolCloseDisabledColor.update(new Color(255, 255, 255), 0, 100, 5);
        frameSymbolCloseDarkColor.update(new Color(227, 92, 60), 0, 0, 9);
        frameSymbolCloseDarkDisabledColor.update(new Color(175, 105, 125), 0, 0, 12);
        frameSpreadDark.setValue(3);
        frameSpreadLight.setValue(2);
        frameSpreadDarkDisabled.setValue(2);
        frameSpreadLightDisabled.setValue(2);
        tableBackColor.update(new Color(255, 255, 255), 0, 100, 3);
        tableHeaderBackColor.update(new Color(236, 233, 216), 0, 0, 3);
        tableHeaderRolloverBackColor.update(new Color(TelnetCommand.GA, 248, 243), 0, 70, 3);
        tableHeaderRolloverColor.update(new Color(248, 179, 48), 0, 0, 11);
        tableGridColor.update(new Color(167, 166, 160), -50, -28, 3);
        tableHeaderArrowColor.update(new Color(167, 166, 160), -50, -28, 3);
        tableSelectedBackColor.update(new Color(213, 211, 204), -50, -8, 3);
        tableSelectedForeColor.update(new Color(0, 0, 0), 0, -100, 3);
        tableBorderDarkColor.update(new Color(167, 166, 160), -50, -28, 3);
        tableBorderLightColor.update(new Color(255, 255, 255), 0, 100, 3);
        tableHeaderDarkColor.update(new Color(189, 186, 173), 0, -20, 3);
        tableHeaderLightColor.update(new Color(255, 255, 255), 0, 100, 3);
        tableFocusBorderColor.update(new Color(185, 184, 177), -50, -20, 3);
        tableAlternateRowColor.update(new Color(255, 255, 255), 0, 100, 3);
        for (int i2 = 0; i2 < 20; i2++) {
            colorizer[i2].setHue(51);
            colorizer[i2].setSaturation(25);
            colorizer[i2].setBrightness(0);
            colorizer[i2].setReference(3);
            colorize[i2].setValue(false);
        }
        separatorColor.update(new Color(167, 167, 163), -70, -27, 3);
        tipBorderColor.update(new Color(0, 0, 0), 0, -100, 3);
        tipBorderDis.update(new Color(143, 141, 139), 0, 0, 4);
        tipBgColor.update(new Color(255, 255, 225), 0, 0, 1);
        tipBgDis.update(new Color(236, 233, 216), 0, 0, 3);
        tipTextColor.update(new Color(0, 0, 0), 0, -100, 3);
        tipTextDis.update(new Color(143, 141, 139), 0, 0, 4);
        titledBorderColor.update(new Color(165, 163, 151), 0, -30, 3);
        splitPaneButtonColor.update(new Color(170, 168, 156), 0, -28, 3);
    }

    public static boolean loadTheme(File file) {
        errorCode = 1;
        if (file == null) {
            errorCode = 2;
            return false;
        }
        try {
            return loadTheme(new FileInputStream(file));
        } catch (FileNotFoundException e2) {
            System.out.println(new StringBuffer().append("Theme.loadTheme(File) : ").append((Object) e2).toString());
            errorCode = 3;
            return false;
        } catch (IOException e3) {
            System.out.println(new StringBuffer().append("Theme.loadTheme(File) : ").append((Object) e3).toString());
            errorCode = 4;
            return false;
        }
    }

    public static boolean loadTheme(ThemeDescription themeDescription) {
        if (themeDescription == null) {
            errorCode = 2;
            return false;
        }
        if (themeDescription.isValid()) {
            return loadTheme(themeDescription.getURL());
        }
        errorCode = 7;
        return false;
    }

    public static boolean loadTheme(URL url) {
        errorCode = 1;
        if (url == null) {
            errorCode = 2;
            return false;
        }
        if (YQ_URL != null && url.equals(YQ_URL)) {
            loadYQTheme();
            return true;
        }
        try {
            return loadTheme(url.openStream());
        } catch (FileNotFoundException e2) {
            errorCode = 3;
            return false;
        } catch (IOException e3) {
            System.out.println(new StringBuffer().append("Theme.loadTheme(URL) : ").append((Object) e3).toString());
            errorCode = 4;
            return false;
        }
    }

    private static boolean loadTheme(InputStream inputStream) throws IOException {
        AutoCloseable autoCloseable = null;
        try {
            try {
                DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(inputStream));
                fileID = dataInputStream.readInt();
                if (fileID != 12852 && fileID != FILE_ID_3B && fileID != FILE_ID_3C && fileID != FILE_ID_3D && fileID != FILE_ID_3E && fileID != FILE_ID_3F && fileID != 16384 && fileID != FILE_ID_4B && fileID != FILE_ID_4C && fileID != FILE_ID_4D && fileID != FILE_ID_4E && fileID != FILE_ID_2 && fileID != FILE_ID_1) {
                    errorCode = 5;
                    if (dataInputStream != null) {
                        try {
                            dataInputStream.close();
                        } catch (IOException e2) {
                        }
                    }
                    return false;
                }
                if (dataInputStream.readInt() != 2) {
                    errorCode = 6;
                    if (dataInputStream != null) {
                        try {
                            dataInputStream.close();
                        } catch (IOException e3) {
                        }
                    }
                    return false;
                }
                mainColor.load(dataInputStream);
                disColor.load(dataInputStream);
                backColor.load(dataInputStream);
                frameColor.load(dataInputStream);
                sub1Color.load(dataInputStream);
                sub2Color.load(dataInputStream);
                sub3Color.load(dataInputStream);
                sub4Color.load(dataInputStream);
                sub5Color.load(dataInputStream);
                sub6Color.load(dataInputStream);
                sub7Color.load(dataInputStream);
                sub8Color.load(dataInputStream);
                plainFont.load(dataInputStream);
                boldFont.load(dataInputStream);
                buttonFont.load(dataInputStream);
                passwordFont.load(dataInputStream);
                labelFont.load(dataInputStream);
                comboFont.load(dataInputStream);
                if (fileID == FILE_ID_1) {
                    ColoredFont.loadDummyData(dataInputStream);
                }
                listFont.load(dataInputStream);
                menuFont.load(dataInputStream);
                menuItemFont.load(dataInputStream);
                radioFont.load(dataInputStream);
                checkFont.load(dataInputStream);
                tableFont.load(dataInputStream);
                tableHeaderFont.load(dataInputStream);
                textAreaFont.load(dataInputStream);
                textFieldFont.load(dataInputStream);
                textPaneFont.load(dataInputStream);
                titledBorderFont.load(dataInputStream);
                toolTipFont.load(dataInputStream);
                treeFont.load(dataInputStream);
                tabFont.load(dataInputStream);
                editorFont.load(dataInputStream);
                frameTitleFont.load(dataInputStream);
                if (fileID >= 12852) {
                    internalFrameTitleFont.load(dataInputStream);
                    internalPaletteTitleFont.load(dataInputStream);
                }
                if (fileID != FILE_ID_1) {
                    progressBarFont.load(dataInputStream);
                }
                buttonFontColor.load(dataInputStream);
                buttonFont.setSBReference(buttonFontColor);
                labelFontColor.load(dataInputStream);
                labelFont.setSBReference(labelFontColor);
                menuFontColor.load(dataInputStream);
                menuFont.setSBReference(menuFontColor);
                menuItemFontColor.load(dataInputStream);
                menuItemFont.setSBReference(menuItemFontColor);
                radioFontColor.load(dataInputStream);
                radioFont.setSBReference(radioFontColor);
                checkFontColor.load(dataInputStream);
                checkFont.setSBReference(checkFontColor);
                tableFontColor.load(dataInputStream);
                tableFont.setSBReference(tableFontColor);
                tableHeaderFontColor.load(dataInputStream);
                tableHeaderFont.setSBReference(tableHeaderFontColor);
                tabFontColor.load(dataInputStream);
                tabFont.setSBReference(tabFontColor);
                titledBorderFontColor.load(dataInputStream);
                titledBorderFont.setSBReference(titledBorderFontColor);
                if (fileID < FILE_ID_3C) {
                    SBReference.loadDummyData(dataInputStream);
                }
                progressColor.load(dataInputStream);
                progressTrackColor.load(dataInputStream);
                progressBorderColor.load(dataInputStream);
                progressDarkColor.load(dataInputStream);
                progressLightColor.load(dataInputStream);
                if (fileID != FILE_ID_1) {
                    progressSelectForeColor.load(dataInputStream);
                    progressSelectBackColor.load(dataInputStream);
                }
                textBgColor.load(dataInputStream);
                textTextColor.load(dataInputStream);
                if (fileID >= 12852) {
                    textCaretColor.load(dataInputStream);
                    editorPaneBgColor.load(dataInputStream);
                    textPaneBgColor.load(dataInputStream);
                    desktopPaneBgColor.load(dataInputStream);
                }
                textSelectedBgColor.load(dataInputStream);
                textSelectedTextColor.load(dataInputStream);
                textDisabledBgColor.load(dataInputStream);
                if (fileID < 16384) {
                    textNonEditableBgColor.update(textDisabledBgColor);
                } else {
                    textNonEditableBgColor.load(dataInputStream);
                }
                textBorderColor.load(dataInputStream);
                if (fileID < 16384) {
                    SBReference.loadDummyData(dataInputStream);
                    SBReference.loadDummyData(dataInputStream);
                }
                textBorderDisabledColor.load(dataInputStream);
                if (fileID < 16384) {
                    SBReference.loadDummyData(dataInputStream);
                    SBReference.loadDummyData(dataInputStream);
                }
                textInsets.top = dataInputStream.readInt();
                textInsets.left = dataInputStream.readInt();
                textInsets.bottom = dataInputStream.readInt();
                textInsets.right = dataInputStream.readInt();
                buttonRolloverBorder.setValue(dataInputStream.readBoolean());
                buttonFocus.setValue(dataInputStream.readBoolean());
                if (fileID >= 12852) {
                    buttonFocusBorder.setValue(dataInputStream.readBoolean());
                    buttonEnter.setValue(dataInputStream.readBoolean());
                }
                if (fileID >= FILE_ID_3D) {
                    shiftButtonText.setValue(dataInputStream.readBoolean());
                }
                buttonNormalColor.load(dataInputStream);
                buttonRolloverBgColor.load(dataInputStream);
                buttonPressedColor.load(dataInputStream);
                buttonDisabledColor.load(dataInputStream);
                buttonBorderColor.load(dataInputStream);
                if (fileID < 16384) {
                    SBReference.loadDummyData(dataInputStream);
                    SBReference.loadDummyData(dataInputStream);
                }
                buttonBorderDisabledColor.load(dataInputStream);
                if (fileID < 16384) {
                    SBReference.loadDummyData(dataInputStream);
                    SBReference.loadDummyData(dataInputStream);
                }
                buttonMargin.top = dataInputStream.readInt();
                buttonMargin.left = dataInputStream.readInt();
                buttonMargin.bottom = dataInputStream.readInt();
                buttonMargin.right = dataInputStream.readInt();
                if (fileID >= FILE_ID_3B) {
                    checkMargin.top = dataInputStream.readInt();
                    checkMargin.left = dataInputStream.readInt();
                    checkMargin.bottom = dataInputStream.readInt();
                    checkMargin.right = dataInputStream.readInt();
                } else {
                    checkMargin.top = 2;
                    checkMargin.left = 2;
                    checkMargin.bottom = 2;
                    checkMargin.right = 2;
                }
                buttonRolloverColor.load(dataInputStream);
                buttonDefaultColor.load(dataInputStream);
                buttonCheckColor.load(dataInputStream);
                buttonCheckDisabledColor.load(dataInputStream);
                buttonDisabledFgColor.load(dataInputStream);
                checkDisabledFgColor.load(dataInputStream);
                radioDisabledFgColor.load(dataInputStream);
                if (fileID >= FILE_ID_4B) {
                    toggleSelectedBg.load(dataInputStream);
                } else {
                    toggleSelectedBg.update(buttonPressedColor);
                }
                buttonSpreadLight.setValue(dataInputStream.readInt());
                buttonSpreadDark.setValue(dataInputStream.readInt());
                buttonSpreadLightDisabled.setValue(dataInputStream.readInt());
                buttonSpreadDarkDisabled.setValue(dataInputStream.readInt());
                if (fileID < 12852) {
                    buttonMargin.top = Math.max(0, buttonMargin.top - 2);
                    buttonMargin.left = Math.max(0, buttonMargin.left - 2);
                    buttonMargin.bottom = Math.max(0, buttonMargin.bottom - 2);
                    buttonMargin.right = Math.max(0, buttonMargin.right - 2);
                }
                scrollRollover.setValue(dataInputStream.readBoolean());
                if (fileID >= FILE_ID_4D) {
                    scrollSize.setValue(dataInputStream.readInt());
                } else {
                    scrollSize.setValue(17);
                }
                scrollTrackColor.load(dataInputStream);
                scrollTrackDisabledColor.load(dataInputStream);
                scrollTrackBorderColor.load(dataInputStream);
                scrollTrackBorderDisabledColor.load(dataInputStream);
                scrollThumbColor.load(dataInputStream);
                scrollThumbRolloverColor.load(dataInputStream);
                scrollThumbPressedColor.load(dataInputStream);
                scrollThumbDisabledColor.load(dataInputStream);
                scrollGripLightColor.load(dataInputStream);
                scrollGripDarkColor.load(dataInputStream);
                scrollButtColor.load(dataInputStream);
                scrollButtRolloverColor.load(dataInputStream);
                scrollButtPressedColor.load(dataInputStream);
                scrollButtDisabledColor.load(dataInputStream);
                scrollSpreadLight.setValue(dataInputStream.readInt());
                scrollSpreadDark.setValue(dataInputStream.readInt());
                scrollSpreadLightDisabled.setValue(dataInputStream.readInt());
                scrollSpreadDarkDisabled.setValue(dataInputStream.readInt());
                scrollArrowColor.load(dataInputStream);
                scrollArrowDisabledColor.load(dataInputStream);
                scrollBorderColor.load(dataInputStream);
                if (fileID < 16384) {
                    SBReference.loadDummyData(dataInputStream);
                }
                scrollBorderLightColor.load(dataInputStream);
                scrollBorderDisabledColor.load(dataInputStream);
                if (fileID < 16384) {
                    SBReference.loadDummyData(dataInputStream);
                }
                scrollLightDisabledColor.load(dataInputStream);
                scrollPaneBorderColor.load(dataInputStream);
                tabPaneBorderColor.load(dataInputStream);
                if (fileID < 16384) {
                    SBReference.loadDummyData(dataInputStream);
                    SBReference.loadDummyData(dataInputStream);
                }
                tabNormalColor.load(dataInputStream);
                tabSelectedColor.load(dataInputStream);
                if (fileID >= 12852) {
                    tabDisabledColor.load(dataInputStream);
                    tabDisabledSelectedColor.load(dataInputStream);
                    tabDisabledTextColor.load(dataInputStream);
                }
                tabBorderColor.load(dataInputStream);
                if (fileID < 16384) {
                    SBReference.loadDummyData(dataInputStream);
                    SBReference.loadDummyData(dataInputStream);
                }
                tabRolloverColor.load(dataInputStream);
                if (fileID >= 16384) {
                    tabDisabledBorderColor.load(dataInputStream);
                    tabPaneDisabledBorderColor.load(dataInputStream);
                } else {
                    tabDisabledBorderColor.update(tabBorderColor);
                    tabPaneDisabledBorderColor.update(tabPaneBorderColor);
                }
                int i2 = fileID < 12852 ? dataInputStream.readInt() : -1;
                tabRollover.setValue(dataInputStream.readBoolean());
                if (fileID >= FILE_ID_3E) {
                    tabFocus.setValue(dataInputStream.readBoolean());
                } else {
                    tabFocus.setValue(true);
                }
                ignoreSelectedBg.setValue(dataInputStream.readBoolean());
                if (fileID >= FILE_ID_3C) {
                    fixedTabs.setValue(dataInputStream.readBoolean());
                }
                if (fileID < 12852) {
                    dataInputStream.readInt();
                    dataInputStream.readInt();
                    dataInputStream.readInt();
                    dataInputStream.readInt();
                }
                if (fileID >= 12852) {
                    tabInsets.top = dataInputStream.readInt();
                    tabInsets.left = dataInputStream.readInt();
                    tabInsets.bottom = dataInputStream.readInt();
                    tabInsets.right = dataInputStream.readInt();
                    tabAreaInsets.top = dataInputStream.readInt();
                    tabAreaInsets.left = dataInputStream.readInt();
                    tabAreaInsets.bottom = dataInputStream.readInt();
                    tabAreaInsets.right = dataInputStream.readInt();
                    if (i2 > -1) {
                        tabAreaInsets.left = i2;
                    }
                }
                sliderRolloverEnabled.setValue(dataInputStream.readBoolean());
                if (fileID >= FILE_ID_3E) {
                    sliderFocusEnabled.setValue(dataInputStream.readBoolean());
                } else {
                    sliderFocusEnabled.setValue(true);
                }
                sliderThumbColor.load(dataInputStream);
                sliderThumbRolloverColor.load(dataInputStream);
                sliderThumbPressedColor.load(dataInputStream);
                sliderThumbDisabledColor.load(dataInputStream);
                sliderBorderColor.load(dataInputStream);
                sliderDarkColor.load(dataInputStream);
                sliderLightColor.load(dataInputStream);
                if (fileID < 12852) {
                    sliderLightColor.update(buttonCheckColor);
                }
                sliderBorderDisabledColor.load(dataInputStream);
                if (fileID < 16384) {
                    SBReference.loadDummyData(dataInputStream);
                    SBReference.loadDummyData(dataInputStream);
                }
                sliderTrackColor.load(dataInputStream);
                sliderTrackBorderColor.load(dataInputStream);
                sliderTrackDarkColor.load(dataInputStream);
                sliderTrackLightColor.load(dataInputStream);
                sliderTickColor.load(dataInputStream);
                sliderTickDisabledColor.load(dataInputStream);
                if (fileID >= FILE_ID_3E) {
                    sliderFocusColor.load(dataInputStream);
                }
                spinnerRollover.setValue(dataInputStream.readBoolean());
                spinnerButtColor.load(dataInputStream);
                spinnerButtRolloverColor.load(dataInputStream);
                spinnerButtPressedColor.load(dataInputStream);
                spinnerButtDisabledColor.load(dataInputStream);
                spinnerSpreadLight.setValue(dataInputStream.readInt());
                spinnerSpreadDark.setValue(dataInputStream.readInt());
                spinnerSpreadLightDisabled.setValue(dataInputStream.readInt());
                spinnerSpreadDarkDisabled.setValue(dataInputStream.readInt());
                spinnerBorderColor.load(dataInputStream);
                if (fileID < 16384) {
                    SBReference.loadDummyData(dataInputStream);
                    SBReference.loadDummyData(dataInputStream);
                }
                spinnerBorderDisabledColor.load(dataInputStream);
                if (fileID < 16384) {
                    SBReference.loadDummyData(dataInputStream);
                    SBReference.loadDummyData(dataInputStream);
                }
                spinnerArrowColor.load(dataInputStream);
                spinnerArrowDisabledColor.load(dataInputStream);
                comboBorderColor.load(dataInputStream);
                if (fileID < 16384) {
                    SBReference.loadDummyData(dataInputStream);
                    SBReference.loadDummyData(dataInputStream);
                }
                comboBorderDisabledColor.load(dataInputStream);
                if (fileID < 16384) {
                    SBReference.loadDummyData(dataInputStream);
                    SBReference.loadDummyData(dataInputStream);
                }
                comboSelectedBgColor.load(dataInputStream);
                comboSelectedTextColor.load(dataInputStream);
                comboFocusBgColor.load(dataInputStream);
                if (fileID >= 12852) {
                    comboBgColor.load(dataInputStream);
                    comboTextColor.load(dataInputStream);
                } else {
                    comboBgColor.update(textBgColor);
                    comboTextColor.update(textTextColor);
                }
                comboButtColor.load(dataInputStream);
                comboButtRolloverColor.load(dataInputStream);
                comboButtPressedColor.load(dataInputStream);
                comboButtDisabledColor.load(dataInputStream);
                comboSpreadLight.setValue(dataInputStream.readInt());
                comboSpreadDark.setValue(dataInputStream.readInt());
                comboSpreadLightDisabled.setValue(dataInputStream.readInt());
                comboSpreadDarkDisabled.setValue(dataInputStream.readInt());
                comboButtBorderColor.load(dataInputStream);
                if (fileID < 16384) {
                    SBReference.loadDummyData(dataInputStream);
                    SBReference.loadDummyData(dataInputStream);
                }
                comboButtBorderDisabledColor.load(dataInputStream);
                if (fileID < 16384) {
                    SBReference.loadDummyData(dataInputStream);
                    SBReference.loadDummyData(dataInputStream);
                }
                comboArrowColor.load(dataInputStream);
                comboArrowDisabledColor.load(dataInputStream);
                comboInsets.top = dataInputStream.readInt();
                comboInsets.left = dataInputStream.readInt();
                comboInsets.bottom = dataInputStream.readInt();
                comboInsets.right = dataInputStream.readInt();
                comboRollover.setValue(dataInputStream.readBoolean());
                comboFocus.setValue(dataInputStream.readBoolean());
                menuBarColor.load(dataInputStream);
                menuItemSelectedTextColor.load(dataInputStream);
                menuPopupColor.load(dataInputStream);
                menuRolloverBgColor.load(dataInputStream);
                menuItemRolloverColor.load(dataInputStream);
                menuBorderColor.load(dataInputStream);
                if (fileID < 16384) {
                    SBReference.loadDummyData(dataInputStream);
                    SBReference.loadDummyData(dataInputStream);
                }
                menuIconColor.load(dataInputStream);
                menuIconRolloverColor.load(dataInputStream);
                menuIconDisabledColor.load(dataInputStream);
                if (fileID < 16384) {
                    SBReference.loadDummyData(dataInputStream);
                }
                menuSeparatorColor.load(dataInputStream);
                if (fileID < 16384) {
                    SBReference.loadDummyData(dataInputStream);
                    dataInputStream.readInt();
                    dataInputStream.readInt();
                    dataInputStream.readInt();
                    dataInputStream.readInt();
                }
                menuRollover.setValue(dataInputStream.readBoolean());
                if (fileID >= 12852) {
                    menuInnerHilightColor.load(dataInputStream);
                    menuInnerShadowColor.load(dataInputStream);
                    menuOuterHilightColor.load(dataInputStream);
                    menuOuterShadowColor.load(dataInputStream);
                    menuRolloverFgColor.load(dataInputStream);
                    menuDisabledFgColor.load(dataInputStream);
                } else {
                    menuRolloverFgColor.update(menuFont.getSBReference());
                    menuDisabledFgColor.update(buttonDisabledFgColor);
                }
                if (fileID >= 16384) {
                    menuItemDisabledFgColor.load(dataInputStream);
                } else {
                    menuItemDisabledFgColor.update(menuDisabledFgColor);
                }
                if (fileID > FILE_ID_4B) {
                    menuPopupShadow.setValue(dataInputStream.readBoolean());
                    menuAllowTwoIcons.setValue(dataInputStream.readBoolean());
                } else {
                    menuPopupShadow.setValue(false);
                    menuAllowTwoIcons.setValue(false);
                }
                toolBarColor.load(dataInputStream);
                toolBarLightColor.load(dataInputStream);
                toolBarDarkColor.load(dataInputStream);
                if (fileID >= 12852) {
                    toolButtColor.load(dataInputStream);
                    toolButtRolloverColor.load(dataInputStream);
                    toolButtPressedColor.load(dataInputStream);
                    toolButtSelectedColor.load(dataInputStream);
                } else {
                    toolButtSelectedColor.load(dataInputStream);
                    toolButtRolloverColor.load(dataInputStream);
                    toolButtPressedColor.load(dataInputStream);
                    toolButtColor.update(toolButtSelectedColor);
                }
                if (fileID < 16384) {
                    SBReference.loadDummyData(dataInputStream);
                    SBReference.loadDummyData(dataInputStream);
                }
                toolBorderColor.load(dataInputStream);
                if (fileID >= 12852) {
                    toolBorderRolloverColor.load(dataInputStream);
                } else {
                    toolBorderRolloverColor.update(toolBorderColor);
                }
                toolBorderPressedColor.load(dataInputStream);
                toolBorderSelectedColor.load(dataInputStream);
                toolRollover.setValue(dataInputStream.readBoolean());
                toolFocus.setValue(dataInputStream.readBoolean());
                if (fileID >= 12852) {
                    toolGripDarkColor.load(dataInputStream);
                    toolGripLightColor.load(dataInputStream);
                    toolSeparatorColor.load(dataInputStream);
                    if (fileID < 16384) {
                        SBReference.loadDummyData(dataInputStream);
                    }
                    toolMargin.top = dataInputStream.readInt();
                    toolMargin.left = dataInputStream.readInt();
                    toolMargin.bottom = dataInputStream.readInt();
                    toolMargin.right = dataInputStream.readInt();
                } else {
                    toolMargin.top = 5;
                    toolMargin.left = 5;
                    toolMargin.bottom = 5;
                    toolMargin.right = 5;
                }
                listSelectedBgColor.load(dataInputStream);
                listSelectedTextColor.load(dataInputStream);
                if (fileID >= 12852) {
                    listBgColor.load(dataInputStream);
                    listTextColor.load(dataInputStream);
                }
                if (fileID >= 16384) {
                    listFocusBorderColor.load(dataInputStream);
                } else {
                    listFocusBorderColor.update(new Color(213, 211, 209), 0, 0, 1);
                }
                treeBgColor.load(dataInputStream);
                treeTextColor.load(dataInputStream);
                treeTextBgColor.load(dataInputStream);
                treeSelectedTextColor.load(dataInputStream);
                treeSelectedBgColor.load(dataInputStream);
                if (fileID >= 12852) {
                    treeLineColor.load(dataInputStream);
                }
                frameCaptionColor.load(dataInputStream);
                frameCaptionDisabledColor.load(dataInputStream);
                frameBorderColor.load(dataInputStream);
                if (fileID < 16384) {
                    SBReference.loadDummyData(dataInputStream);
                }
                frameLightColor.load(dataInputStream);
                frameBorderDisabledColor.load(dataInputStream);
                if (fileID < 16384) {
                    SBReference.loadDummyData(dataInputStream);
                }
                frameLightDisabledColor.load(dataInputStream);
                frameTitleColor.load(dataInputStream);
                if (fileID > FILE_ID_4B) {
                    frameTitleShadowColor.load(dataInputStream);
                } else {
                    frameTitleShadowColor.update(frameCaptionColor);
                    if (ColorRoutines.isColorDarker(frameTitleColor.getColor(), frameCaptionColor.getColor())) {
                        frameTitleShadowColor.setBrightness(-8);
                    } else {
                        frameTitleShadowColor.setBrightness(-50);
                    }
                }
                frameTitleDisabledColor.load(dataInputStream);
                frameButtColor.load(dataInputStream);
                frameButtRolloverColor.load(dataInputStream);
                frameButtPressedColor.load(dataInputStream);
                frameButtDisabledColor.load(dataInputStream);
                frameButtSpreadDark.setValue(dataInputStream.readInt());
                frameButtSpreadLight.setValue(dataInputStream.readInt());
                frameButtSpreadDarkDisabled.setValue(dataInputStream.readInt());
                frameButtSpreadLightDisabled.setValue(dataInputStream.readInt());
                frameButtCloseColor.load(dataInputStream);
                frameButtCloseRolloverColor.load(dataInputStream);
                frameButtClosePressedColor.load(dataInputStream);
                frameButtCloseDisabledColor.load(dataInputStream);
                frameButtCloseSpreadDark.setValue(dataInputStream.readInt());
                frameButtCloseSpreadLight.setValue(dataInputStream.readInt());
                frameButtCloseSpreadDarkDisabled.setValue(dataInputStream.readInt());
                frameButtCloseSpreadLightDisabled.setValue(dataInputStream.readInt());
                frameButtBorderColor.load(dataInputStream);
                if (fileID < 16384) {
                    SBReference.loadDummyData(dataInputStream);
                    SBReference.loadDummyData(dataInputStream);
                }
                frameButtBorderDisabledColor.load(dataInputStream);
                if (fileID < 16384) {
                    SBReference.loadDummyData(dataInputStream);
                    SBReference.loadDummyData(dataInputStream);
                }
                frameSymbolColor.load(dataInputStream);
                frameSymbolPressedColor.load(dataInputStream);
                frameSymbolDisabledColor.load(dataInputStream);
                frameSymbolDarkColor.load(dataInputStream);
                frameSymbolLightColor.load(dataInputStream);
                if (fileID >= 16384) {
                    frameSymbolDarkDisabledColor.load(dataInputStream);
                    frameSymbolLightDisabledColor.load(dataInputStream);
                } else {
                    frameSymbolDarkDisabledColor.update(ColorRoutines.getAverage(frameSymbolDarkColor.getColor(), frameSymbolColor.getColor()), 0, 0, 1);
                    frameSymbolLightDisabledColor.update(ColorRoutines.getAverage(frameSymbolLightColor.getColor(), frameSymbolColor.getColor()), 0, 0, 1);
                }
                frameButtCloseBorderColor.load(dataInputStream);
                frameButtCloseDarkColor.load(dataInputStream);
                frameButtCloseLightColor.load(dataInputStream);
                frameButtCloseBorderDisabledColor.load(dataInputStream);
                if (fileID < 16384) {
                    SBReference.loadDummyData(dataInputStream);
                    SBReference.loadDummyData(dataInputStream);
                }
                frameSymbolCloseColor.load(dataInputStream);
                frameSymbolClosePressedColor.load(dataInputStream);
                frameSymbolCloseDisabledColor.load(dataInputStream);
                frameSymbolCloseDarkColor.load(dataInputStream);
                if (fileID >= 16384) {
                    frameSymbolCloseDarkDisabledColor.load(dataInputStream);
                } else {
                    frameSymbolCloseDarkDisabledColor.update(ColorRoutines.getAverage(frameSymbolCloseDarkColor.getColor(), frameSymbolCloseColor.getColor()), 0, 0, 1);
                }
                if (fileID < 16384) {
                    SBReference.loadDummyData(dataInputStream);
                }
                frameSpreadDark.setValue(dataInputStream.readInt());
                frameSpreadLight.setValue(dataInputStream.readInt());
                frameSpreadDarkDisabled.setValue(dataInputStream.readInt());
                frameSpreadLightDisabled.setValue(dataInputStream.readInt());
                tableBackColor.load(dataInputStream);
                tableHeaderBackColor.load(dataInputStream);
                if (fileID >= FILE_ID_3F) {
                    tableHeaderArrowColor.load(dataInputStream);
                    tableHeaderRolloverBackColor.load(dataInputStream);
                    tableHeaderRolloverColor.load(dataInputStream);
                }
                tableGridColor.load(dataInputStream);
                tableSelectedBackColor.load(dataInputStream);
                tableSelectedForeColor.load(dataInputStream);
                if (fileID >= 12852) {
                    tableBorderDarkColor.load(dataInputStream);
                    tableBorderLightColor.load(dataInputStream);
                    tableHeaderDarkColor.load(dataInputStream);
                    tableHeaderLightColor.load(dataInputStream);
                }
                if (fileID >= 16384) {
                    tableFocusBorderColor.load(dataInputStream);
                } else {
                    tableFocusBorderColor.update(tableSelectedBackColor);
                }
                if (fileID >= FILE_ID_4E) {
                    tableAlternateRowColor.load(dataInputStream);
                } else {
                    tableAlternateRowColor.update(tableBackColor);
                }
                if (fileID >= 12852) {
                    for (int i3 = 0; i3 < colorizer.length; i3++) {
                        colorizer[i3].load(dataInputStream);
                        colorize[i3].setValue(dataInputStream.readBoolean());
                    }
                } else {
                    SBReference.loadDummyData(dataInputStream);
                    SBReference.loadDummyData(dataInputStream);
                    SBReference.loadDummyData(dataInputStream);
                    SBReference.loadDummyData(dataInputStream);
                    SBReference.loadDummyData(dataInputStream);
                    dataInputStream.readBoolean();
                    dataInputStream.readBoolean();
                    dataInputStream.readBoolean();
                    dataInputStream.readBoolean();
                    dataInputStream.readBoolean();
                    for (int i4 = 0; i4 < 15; i4++) {
                        dataInputStream.readBoolean();
                    }
                    for (int i5 = 0; i5 < colorizer.length; i5++) {
                        colorize[i5].setValue(false);
                    }
                }
                if (fileID >= 12852) {
                    separatorColor.load(dataInputStream);
                    if (fileID < 16384) {
                        SBReference.loadDummyData(dataInputStream);
                    }
                }
                tipBorderColor.load(dataInputStream);
                tipBgColor.load(dataInputStream);
                if (fileID >= FILE_ID_3C) {
                    tipBorderDis.load(dataInputStream);
                    tipBgDis.load(dataInputStream);
                    tipTextColor.load(dataInputStream);
                    tipTextDis.load(dataInputStream);
                }
                titledBorderColor.load(dataInputStream);
                if (fileID >= 16384) {
                    splitPaneButtonColor.load(dataInputStream);
                } else {
                    splitPaneButtonColor.update(scrollArrowColor);
                }
                dataInputStream.close();
                if (dataInputStream == null) {
                    return true;
                }
                try {
                    dataInputStream.close();
                    return true;
                } catch (IOException e4) {
                    return true;
                }
            } catch (IOException e5) {
                e5.printStackTrace();
                throw e5;
            }
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    autoCloseable.close();
                } catch (IOException e6) {
                }
            }
            throw th;
        }
    }

    public static boolean saveTheme(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Argument to Theme.saveTheme(String) is null");
        }
        return saveTheme(new File(str));
    }

    private static boolean saveTheme(File file) {
        DataOutputStream dataOutputStream = null;
        try {
            try {
                dataOutputStream = new DataOutputStream(new FileOutputStream(file));
                dataOutputStream.writeInt(FILE_ID_4E);
                dataOutputStream.writeInt(2);
                mainColor.save(dataOutputStream);
                disColor.save(dataOutputStream);
                backColor.save(dataOutputStream);
                frameColor.save(dataOutputStream);
                sub1Color.save(dataOutputStream);
                sub2Color.save(dataOutputStream);
                sub3Color.save(dataOutputStream);
                sub4Color.save(dataOutputStream);
                sub5Color.save(dataOutputStream);
                sub6Color.save(dataOutputStream);
                sub7Color.save(dataOutputStream);
                sub8Color.save(dataOutputStream);
                plainFont.save(dataOutputStream);
                boldFont.save(dataOutputStream);
                buttonFont.save(dataOutputStream);
                passwordFont.save(dataOutputStream);
                labelFont.save(dataOutputStream);
                comboFont.save(dataOutputStream);
                listFont.save(dataOutputStream);
                menuFont.save(dataOutputStream);
                menuItemFont.save(dataOutputStream);
                radioFont.save(dataOutputStream);
                checkFont.save(dataOutputStream);
                tableFont.save(dataOutputStream);
                tableHeaderFont.save(dataOutputStream);
                textAreaFont.save(dataOutputStream);
                textFieldFont.save(dataOutputStream);
                textPaneFont.save(dataOutputStream);
                titledBorderFont.save(dataOutputStream);
                toolTipFont.save(dataOutputStream);
                treeFont.save(dataOutputStream);
                tabFont.save(dataOutputStream);
                editorFont.save(dataOutputStream);
                frameTitleFont.save(dataOutputStream);
                internalFrameTitleFont.save(dataOutputStream);
                internalPaletteTitleFont.save(dataOutputStream);
                progressBarFont.save(dataOutputStream);
                buttonFontColor.save(dataOutputStream);
                labelFontColor.save(dataOutputStream);
                menuFontColor.save(dataOutputStream);
                menuItemFontColor.save(dataOutputStream);
                radioFontColor.save(dataOutputStream);
                checkFontColor.save(dataOutputStream);
                tableFontColor.save(dataOutputStream);
                tableHeaderFontColor.save(dataOutputStream);
                tabFontColor.save(dataOutputStream);
                titledBorderFontColor.save(dataOutputStream);
                progressColor.save(dataOutputStream);
                progressTrackColor.save(dataOutputStream);
                progressBorderColor.save(dataOutputStream);
                progressDarkColor.save(dataOutputStream);
                progressLightColor.save(dataOutputStream);
                progressSelectForeColor.save(dataOutputStream);
                progressSelectBackColor.save(dataOutputStream);
                textBgColor.save(dataOutputStream);
                textTextColor.save(dataOutputStream);
                textCaretColor.save(dataOutputStream);
                editorPaneBgColor.save(dataOutputStream);
                textPaneBgColor.save(dataOutputStream);
                desktopPaneBgColor.save(dataOutputStream);
                textSelectedBgColor.save(dataOutputStream);
                textSelectedTextColor.save(dataOutputStream);
                textDisabledBgColor.save(dataOutputStream);
                textNonEditableBgColor.save(dataOutputStream);
                textBorderColor.save(dataOutputStream);
                textBorderDisabledColor.save(dataOutputStream);
                dataOutputStream.writeInt(textInsets.top);
                dataOutputStream.writeInt(textInsets.left);
                dataOutputStream.writeInt(textInsets.bottom);
                dataOutputStream.writeInt(textInsets.right);
                dataOutputStream.writeBoolean(buttonRolloverBorder.getValue());
                dataOutputStream.writeBoolean(buttonFocus.getValue());
                dataOutputStream.writeBoolean(buttonFocusBorder.getValue());
                dataOutputStream.writeBoolean(buttonEnter.getValue());
                dataOutputStream.writeBoolean(shiftButtonText.getValue());
                buttonNormalColor.save(dataOutputStream);
                buttonRolloverBgColor.save(dataOutputStream);
                buttonPressedColor.save(dataOutputStream);
                buttonDisabledColor.save(dataOutputStream);
                buttonBorderColor.save(dataOutputStream);
                buttonBorderDisabledColor.save(dataOutputStream);
                dataOutputStream.writeInt(buttonMargin.top);
                dataOutputStream.writeInt(buttonMargin.left);
                dataOutputStream.writeInt(buttonMargin.bottom);
                dataOutputStream.writeInt(buttonMargin.right);
                dataOutputStream.writeInt(checkMargin.top);
                dataOutputStream.writeInt(checkMargin.left);
                dataOutputStream.writeInt(checkMargin.bottom);
                dataOutputStream.writeInt(checkMargin.right);
                buttonRolloverColor.save(dataOutputStream);
                buttonDefaultColor.save(dataOutputStream);
                buttonCheckColor.save(dataOutputStream);
                buttonCheckDisabledColor.save(dataOutputStream);
                buttonDisabledFgColor.save(dataOutputStream);
                checkDisabledFgColor.save(dataOutputStream);
                radioDisabledFgColor.save(dataOutputStream);
                toggleSelectedBg.save(dataOutputStream);
                dataOutputStream.writeInt(buttonSpreadLight.getValue());
                dataOutputStream.writeInt(buttonSpreadDark.getValue());
                dataOutputStream.writeInt(buttonSpreadLightDisabled.getValue());
                dataOutputStream.writeInt(buttonSpreadDarkDisabled.getValue());
                dataOutputStream.writeBoolean(scrollRollover.getValue());
                dataOutputStream.writeInt(scrollSize.getValue());
                scrollTrackColor.save(dataOutputStream);
                scrollTrackDisabledColor.save(dataOutputStream);
                scrollTrackBorderColor.save(dataOutputStream);
                scrollTrackBorderDisabledColor.save(dataOutputStream);
                scrollThumbColor.save(dataOutputStream);
                scrollThumbRolloverColor.save(dataOutputStream);
                scrollThumbPressedColor.save(dataOutputStream);
                scrollThumbDisabledColor.save(dataOutputStream);
                scrollGripLightColor.save(dataOutputStream);
                scrollGripDarkColor.save(dataOutputStream);
                scrollButtColor.save(dataOutputStream);
                scrollButtRolloverColor.save(dataOutputStream);
                scrollButtPressedColor.save(dataOutputStream);
                scrollButtDisabledColor.save(dataOutputStream);
                dataOutputStream.writeInt(scrollSpreadLight.getValue());
                dataOutputStream.writeInt(scrollSpreadDark.getValue());
                dataOutputStream.writeInt(scrollSpreadLightDisabled.getValue());
                dataOutputStream.writeInt(scrollSpreadDarkDisabled.getValue());
                scrollArrowColor.save(dataOutputStream);
                scrollArrowDisabledColor.save(dataOutputStream);
                scrollBorderColor.save(dataOutputStream);
                scrollBorderLightColor.save(dataOutputStream);
                scrollBorderDisabledColor.save(dataOutputStream);
                scrollLightDisabledColor.save(dataOutputStream);
                scrollPaneBorderColor.save(dataOutputStream);
                tabPaneBorderColor.save(dataOutputStream);
                tabNormalColor.save(dataOutputStream);
                tabSelectedColor.save(dataOutputStream);
                tabDisabledColor.save(dataOutputStream);
                tabDisabledSelectedColor.save(dataOutputStream);
                tabDisabledTextColor.save(dataOutputStream);
                tabBorderColor.save(dataOutputStream);
                tabRolloverColor.save(dataOutputStream);
                tabDisabledBorderColor.save(dataOutputStream);
                tabPaneDisabledBorderColor.save(dataOutputStream);
                dataOutputStream.writeBoolean(tabRollover.getValue());
                dataOutputStream.writeBoolean(tabFocus.getValue());
                dataOutputStream.writeBoolean(ignoreSelectedBg.getValue());
                dataOutputStream.writeBoolean(fixedTabs.getValue());
                dataOutputStream.writeInt(tabInsets.top);
                dataOutputStream.writeInt(tabInsets.left);
                dataOutputStream.writeInt(tabInsets.bottom);
                dataOutputStream.writeInt(tabInsets.right);
                dataOutputStream.writeInt(tabAreaInsets.top);
                dataOutputStream.writeInt(tabAreaInsets.left);
                dataOutputStream.writeInt(tabAreaInsets.bottom);
                dataOutputStream.writeInt(tabAreaInsets.right);
                dataOutputStream.writeBoolean(sliderRolloverEnabled.getValue());
                dataOutputStream.writeBoolean(sliderFocusEnabled.getValue());
                sliderThumbColor.save(dataOutputStream);
                sliderThumbRolloverColor.save(dataOutputStream);
                sliderThumbPressedColor.save(dataOutputStream);
                sliderThumbDisabledColor.save(dataOutputStream);
                sliderBorderColor.save(dataOutputStream);
                sliderDarkColor.save(dataOutputStream);
                sliderLightColor.save(dataOutputStream);
                sliderBorderDisabledColor.save(dataOutputStream);
                sliderTrackColor.save(dataOutputStream);
                sliderTrackBorderColor.save(dataOutputStream);
                sliderTrackDarkColor.save(dataOutputStream);
                sliderTrackLightColor.save(dataOutputStream);
                sliderTickColor.save(dataOutputStream);
                sliderTickDisabledColor.save(dataOutputStream);
                sliderFocusColor.save(dataOutputStream);
                dataOutputStream.writeBoolean(spinnerRollover.getValue());
                spinnerButtColor.save(dataOutputStream);
                spinnerButtRolloverColor.save(dataOutputStream);
                spinnerButtPressedColor.save(dataOutputStream);
                spinnerButtDisabledColor.save(dataOutputStream);
                dataOutputStream.writeInt(spinnerSpreadLight.getValue());
                dataOutputStream.writeInt(spinnerSpreadDark.getValue());
                dataOutputStream.writeInt(spinnerSpreadLightDisabled.getValue());
                dataOutputStream.writeInt(spinnerSpreadDarkDisabled.getValue());
                spinnerBorderColor.save(dataOutputStream);
                spinnerBorderDisabledColor.save(dataOutputStream);
                spinnerArrowColor.save(dataOutputStream);
                spinnerArrowDisabledColor.save(dataOutputStream);
                comboBorderColor.save(dataOutputStream);
                comboBorderDisabledColor.save(dataOutputStream);
                comboSelectedBgColor.save(dataOutputStream);
                comboSelectedTextColor.save(dataOutputStream);
                comboFocusBgColor.save(dataOutputStream);
                comboBgColor.save(dataOutputStream);
                comboTextColor.save(dataOutputStream);
                comboButtColor.save(dataOutputStream);
                comboButtRolloverColor.save(dataOutputStream);
                comboButtPressedColor.save(dataOutputStream);
                comboButtDisabledColor.save(dataOutputStream);
                dataOutputStream.writeInt(comboSpreadLight.getValue());
                dataOutputStream.writeInt(comboSpreadDark.getValue());
                dataOutputStream.writeInt(comboSpreadLightDisabled.getValue());
                dataOutputStream.writeInt(comboSpreadDarkDisabled.getValue());
                comboButtBorderColor.save(dataOutputStream);
                comboButtBorderDisabledColor.save(dataOutputStream);
                comboArrowColor.save(dataOutputStream);
                comboArrowDisabledColor.save(dataOutputStream);
                dataOutputStream.writeInt(comboInsets.top);
                dataOutputStream.writeInt(comboInsets.left);
                dataOutputStream.writeInt(comboInsets.bottom);
                dataOutputStream.writeInt(comboInsets.right);
                dataOutputStream.writeBoolean(comboRollover.getValue());
                dataOutputStream.writeBoolean(comboFocus.getValue());
                menuBarColor.save(dataOutputStream);
                menuItemSelectedTextColor.save(dataOutputStream);
                menuPopupColor.save(dataOutputStream);
                menuRolloverBgColor.save(dataOutputStream);
                menuItemRolloverColor.save(dataOutputStream);
                menuBorderColor.save(dataOutputStream);
                menuIconColor.save(dataOutputStream);
                menuIconRolloverColor.save(dataOutputStream);
                menuIconDisabledColor.save(dataOutputStream);
                menuSeparatorColor.save(dataOutputStream);
                dataOutputStream.writeBoolean(menuRollover.getValue());
                menuInnerHilightColor.save(dataOutputStream);
                menuInnerShadowColor.save(dataOutputStream);
                menuOuterHilightColor.save(dataOutputStream);
                menuOuterShadowColor.save(dataOutputStream);
                menuRolloverFgColor.save(dataOutputStream);
                menuDisabledFgColor.save(dataOutputStream);
                menuItemDisabledFgColor.save(dataOutputStream);
                dataOutputStream.writeBoolean(menuPopupShadow.getValue());
                dataOutputStream.writeBoolean(menuAllowTwoIcons.getValue());
                toolBarColor.save(dataOutputStream);
                toolBarLightColor.save(dataOutputStream);
                toolBarDarkColor.save(dataOutputStream);
                toolButtColor.save(dataOutputStream);
                toolButtRolloverColor.save(dataOutputStream);
                toolButtPressedColor.save(dataOutputStream);
                toolButtSelectedColor.save(dataOutputStream);
                toolBorderColor.save(dataOutputStream);
                toolBorderRolloverColor.save(dataOutputStream);
                toolBorderPressedColor.save(dataOutputStream);
                toolBorderSelectedColor.save(dataOutputStream);
                dataOutputStream.writeBoolean(toolRollover.getValue());
                dataOutputStream.writeBoolean(toolFocus.getValue());
                toolGripDarkColor.save(dataOutputStream);
                toolGripLightColor.save(dataOutputStream);
                toolSeparatorColor.save(dataOutputStream);
                dataOutputStream.writeInt(toolMargin.top);
                dataOutputStream.writeInt(toolMargin.left);
                dataOutputStream.writeInt(toolMargin.bottom);
                dataOutputStream.writeInt(toolMargin.right);
                listSelectedBgColor.save(dataOutputStream);
                listSelectedTextColor.save(dataOutputStream);
                listBgColor.save(dataOutputStream);
                listTextColor.save(dataOutputStream);
                listFocusBorderColor.save(dataOutputStream);
                treeBgColor.save(dataOutputStream);
                treeTextColor.save(dataOutputStream);
                treeTextBgColor.save(dataOutputStream);
                treeSelectedTextColor.save(dataOutputStream);
                treeSelectedBgColor.save(dataOutputStream);
                treeLineColor.save(dataOutputStream);
                frameCaptionColor.save(dataOutputStream);
                frameCaptionDisabledColor.save(dataOutputStream);
                frameBorderColor.save(dataOutputStream);
                frameLightColor.save(dataOutputStream);
                frameBorderDisabledColor.save(dataOutputStream);
                frameLightDisabledColor.save(dataOutputStream);
                frameTitleColor.save(dataOutputStream);
                frameTitleShadowColor.save(dataOutputStream);
                frameTitleDisabledColor.save(dataOutputStream);
                frameButtColor.save(dataOutputStream);
                frameButtRolloverColor.save(dataOutputStream);
                frameButtPressedColor.save(dataOutputStream);
                frameButtDisabledColor.save(dataOutputStream);
                dataOutputStream.writeInt(frameButtSpreadDark.getValue());
                dataOutputStream.writeInt(frameButtSpreadLight.getValue());
                dataOutputStream.writeInt(frameButtSpreadDarkDisabled.getValue());
                dataOutputStream.writeInt(frameButtSpreadLightDisabled.getValue());
                frameButtCloseColor.save(dataOutputStream);
                frameButtCloseRolloverColor.save(dataOutputStream);
                frameButtClosePressedColor.save(dataOutputStream);
                frameButtCloseDisabledColor.save(dataOutputStream);
                dataOutputStream.writeInt(frameButtCloseSpreadDark.getValue());
                dataOutputStream.writeInt(frameButtCloseSpreadLight.getValue());
                dataOutputStream.writeInt(frameButtCloseSpreadDarkDisabled.getValue());
                dataOutputStream.writeInt(frameButtCloseSpreadLightDisabled.getValue());
                frameButtBorderColor.save(dataOutputStream);
                frameButtBorderDisabledColor.save(dataOutputStream);
                frameSymbolColor.save(dataOutputStream);
                frameSymbolPressedColor.save(dataOutputStream);
                frameSymbolDisabledColor.save(dataOutputStream);
                frameSymbolDarkColor.save(dataOutputStream);
                frameSymbolLightColor.save(dataOutputStream);
                frameSymbolDarkDisabledColor.save(dataOutputStream);
                frameSymbolLightDisabledColor.save(dataOutputStream);
                frameButtCloseBorderColor.save(dataOutputStream);
                frameButtCloseDarkColor.save(dataOutputStream);
                frameButtCloseLightColor.save(dataOutputStream);
                frameButtCloseBorderDisabledColor.save(dataOutputStream);
                frameSymbolCloseColor.save(dataOutputStream);
                frameSymbolClosePressedColor.save(dataOutputStream);
                frameSymbolCloseDisabledColor.save(dataOutputStream);
                frameSymbolCloseDarkColor.save(dataOutputStream);
                frameSymbolCloseDarkDisabledColor.save(dataOutputStream);
                dataOutputStream.writeInt(frameSpreadDark.getValue());
                dataOutputStream.writeInt(frameSpreadLight.getValue());
                dataOutputStream.writeInt(frameSpreadDarkDisabled.getValue());
                dataOutputStream.writeInt(frameSpreadLightDisabled.getValue());
                tableBackColor.save(dataOutputStream);
                tableHeaderBackColor.save(dataOutputStream);
                tableHeaderArrowColor.save(dataOutputStream);
                tableHeaderRolloverBackColor.save(dataOutputStream);
                tableHeaderRolloverColor.save(dataOutputStream);
                tableGridColor.save(dataOutputStream);
                tableSelectedBackColor.save(dataOutputStream);
                tableSelectedForeColor.save(dataOutputStream);
                tableBorderDarkColor.save(dataOutputStream);
                tableBorderLightColor.save(dataOutputStream);
                tableHeaderDarkColor.save(dataOutputStream);
                tableHeaderLightColor.save(dataOutputStream);
                tableFocusBorderColor.save(dataOutputStream);
                tableAlternateRowColor.save(dataOutputStream);
                for (int i2 = 0; i2 < colorizer.length; i2++) {
                    colorizer[i2].save(dataOutputStream);
                    dataOutputStream.writeBoolean(colorize[i2].getValue());
                }
                separatorColor.save(dataOutputStream);
                tipBorderColor.save(dataOutputStream);
                tipBgColor.save(dataOutputStream);
                tipBorderDis.save(dataOutputStream);
                tipBgDis.save(dataOutputStream);
                tipTextColor.save(dataOutputStream);
                tipTextDis.save(dataOutputStream);
                titledBorderColor.save(dataOutputStream);
                splitPaneButtonColor.save(dataOutputStream);
                if (dataOutputStream != null) {
                    try {
                        dataOutputStream.close();
                    } catch (IOException e2) {
                    }
                }
                return true;
            } catch (Throwable th) {
                if (dataOutputStream != null) {
                    try {
                        dataOutputStream.close();
                    } catch (IOException e3) {
                    }
                }
                throw th;
            }
        } catch (IOException e4) {
            e4.printStackTrace();
            if (dataOutputStream == null) {
                return false;
            }
            try {
                dataOutputStream.close();
                return false;
            } catch (IOException e5) {
                return false;
            }
        }
    }

    static Class class$(String str) throws Throwable {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e2) {
            throw new NoClassDefFoundError().initCause(e2);
        }
    }

    static {
        initData();
    }
}
