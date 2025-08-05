package com.sun.java.swing.plaf.windows;

import com.sun.javafx.animation.TickCalculation;
import com.sun.org.apache.xalan.internal.templates.Constants;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.util.EnumMap;
import javax.swing.JComponent;
import org.icepdf.core.pobjects.graphics.Separation;
import sun.awt.windows.ThemeReader;

/* loaded from: rt.jar:com/sun/java/swing/plaf/windows/TMSchema.class */
class TMSchema {

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/TMSchema$Control.class */
    public enum Control {
        BUTTON,
        COMBOBOX,
        EDIT,
        HEADER,
        LISTBOX,
        LISTVIEW,
        MENU,
        PROGRESS,
        REBAR,
        SCROLLBAR,
        SPIN,
        TAB,
        TOOLBAR,
        TRACKBAR,
        TREEVIEW,
        WINDOW
    }

    TMSchema() {
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/TMSchema$Part.class */
    public enum Part {
        MENU(Control.MENU, 0),
        MP_BARBACKGROUND(Control.MENU, 7),
        MP_BARITEM(Control.MENU, 8),
        MP_POPUPBACKGROUND(Control.MENU, 9),
        MP_POPUPBORDERS(Control.MENU, 10),
        MP_POPUPCHECK(Control.MENU, 11),
        MP_POPUPCHECKBACKGROUND(Control.MENU, 12),
        MP_POPUPGUTTER(Control.MENU, 13),
        MP_POPUPITEM(Control.MENU, 14),
        MP_POPUPSEPARATOR(Control.MENU, 15),
        MP_POPUPSUBMENU(Control.MENU, 16),
        BP_PUSHBUTTON(Control.BUTTON, 1),
        BP_RADIOBUTTON(Control.BUTTON, 2),
        BP_CHECKBOX(Control.BUTTON, 3),
        BP_GROUPBOX(Control.BUTTON, 4),
        CP_COMBOBOX(Control.COMBOBOX, 0),
        CP_DROPDOWNBUTTON(Control.COMBOBOX, 1),
        CP_BACKGROUND(Control.COMBOBOX, 2),
        CP_TRANSPARENTBACKGROUND(Control.COMBOBOX, 3),
        CP_BORDER(Control.COMBOBOX, 4),
        CP_READONLY(Control.COMBOBOX, 5),
        CP_DROPDOWNBUTTONRIGHT(Control.COMBOBOX, 6),
        CP_DROPDOWNBUTTONLEFT(Control.COMBOBOX, 7),
        CP_CUEBANNER(Control.COMBOBOX, 8),
        EP_EDIT(Control.EDIT, 0),
        EP_EDITTEXT(Control.EDIT, 1),
        HP_HEADERITEM(Control.HEADER, 1),
        HP_HEADERSORTARROW(Control.HEADER, 4),
        LBP_LISTBOX(Control.LISTBOX, 0),
        LVP_LISTVIEW(Control.LISTVIEW, 0),
        PP_PROGRESS(Control.PROGRESS, 0),
        PP_BAR(Control.PROGRESS, 1),
        PP_BARVERT(Control.PROGRESS, 2),
        PP_CHUNK(Control.PROGRESS, 3),
        PP_CHUNKVERT(Control.PROGRESS, 4),
        RP_GRIPPER(Control.REBAR, 1),
        RP_GRIPPERVERT(Control.REBAR, 2),
        SBP_SCROLLBAR(Control.SCROLLBAR, 0),
        SBP_ARROWBTN(Control.SCROLLBAR, 1),
        SBP_THUMBBTNHORZ(Control.SCROLLBAR, 2),
        SBP_THUMBBTNVERT(Control.SCROLLBAR, 3),
        SBP_LOWERTRACKHORZ(Control.SCROLLBAR, 4),
        SBP_UPPERTRACKHORZ(Control.SCROLLBAR, 5),
        SBP_LOWERTRACKVERT(Control.SCROLLBAR, 6),
        SBP_UPPERTRACKVERT(Control.SCROLLBAR, 7),
        SBP_GRIPPERHORZ(Control.SCROLLBAR, 8),
        SBP_GRIPPERVERT(Control.SCROLLBAR, 9),
        SBP_SIZEBOX(Control.SCROLLBAR, 10),
        SPNP_UP(Control.SPIN, 1),
        SPNP_DOWN(Control.SPIN, 2),
        TABP_TABITEM(Control.TAB, 1),
        TABP_TABITEMLEFTEDGE(Control.TAB, 2),
        TABP_TABITEMRIGHTEDGE(Control.TAB, 3),
        TABP_PANE(Control.TAB, 9),
        TP_TOOLBAR(Control.TOOLBAR, 0),
        TP_BUTTON(Control.TOOLBAR, 1),
        TP_SEPARATOR(Control.TOOLBAR, 5),
        TP_SEPARATORVERT(Control.TOOLBAR, 6),
        TKP_TRACK(Control.TRACKBAR, 1),
        TKP_TRACKVERT(Control.TRACKBAR, 2),
        TKP_THUMB(Control.TRACKBAR, 3),
        TKP_THUMBBOTTOM(Control.TRACKBAR, 4),
        TKP_THUMBTOP(Control.TRACKBAR, 5),
        TKP_THUMBVERT(Control.TRACKBAR, 6),
        TKP_THUMBLEFT(Control.TRACKBAR, 7),
        TKP_THUMBRIGHT(Control.TRACKBAR, 8),
        TKP_TICS(Control.TRACKBAR, 9),
        TKP_TICSVERT(Control.TRACKBAR, 10),
        TVP_TREEVIEW(Control.TREEVIEW, 0),
        TVP_GLYPH(Control.TREEVIEW, 2),
        WP_WINDOW(Control.WINDOW, 0),
        WP_CAPTION(Control.WINDOW, 1),
        WP_MINCAPTION(Control.WINDOW, 3),
        WP_MAXCAPTION(Control.WINDOW, 5),
        WP_FRAMELEFT(Control.WINDOW, 7),
        WP_FRAMERIGHT(Control.WINDOW, 8),
        WP_FRAMEBOTTOM(Control.WINDOW, 9),
        WP_SYSBUTTON(Control.WINDOW, 13),
        WP_MDISYSBUTTON(Control.WINDOW, 14),
        WP_MINBUTTON(Control.WINDOW, 15),
        WP_MDIMINBUTTON(Control.WINDOW, 16),
        WP_MAXBUTTON(Control.WINDOW, 17),
        WP_CLOSEBUTTON(Control.WINDOW, 18),
        WP_MDICLOSEBUTTON(Control.WINDOW, 20),
        WP_RESTOREBUTTON(Control.WINDOW, 21),
        WP_MDIRESTOREBUTTON(Control.WINDOW, 22);

        private final Control control;
        private final int value;

        Part(Control control, int i2) {
            this.control = control;
            this.value = i2;
        }

        public int getValue() {
            return this.value;
        }

        public String getControlName(Component component) {
            String str;
            String str2 = "";
            if ((component instanceof JComponent) && (str = (String) ((JComponent) component).getClientProperty("XPStyle.subAppName")) != null) {
                str2 = str + "::";
            }
            return str2 + this.control.toString();
        }

        @Override // java.lang.Enum
        public String toString() {
            return this.control.toString() + "." + name();
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/TMSchema$State.class */
    public enum State {
        ACTIVE,
        ASSIST,
        BITMAP,
        CHECKED,
        CHECKEDDISABLED,
        CHECKEDHOT,
        CHECKEDNORMAL,
        CHECKEDPRESSED,
        CHECKMARKNORMAL,
        CHECKMARKDISABLED,
        BULLETNORMAL,
        BULLETDISABLED,
        CLOSED,
        DEFAULTED,
        DISABLED,
        DISABLEDHOT,
        DISABLEDPUSHED,
        DOWNDISABLED,
        DOWNHOT,
        DOWNNORMAL,
        DOWNPRESSED,
        FOCUSED,
        HOT,
        HOTCHECKED,
        ICONHOT,
        ICONNORMAL,
        ICONPRESSED,
        ICONSORTEDHOT,
        ICONSORTEDNORMAL,
        ICONSORTEDPRESSED,
        INACTIVE,
        INACTIVENORMAL,
        INACTIVEHOT,
        INACTIVEPUSHED,
        INACTIVEDISABLED,
        LEFTDISABLED,
        LEFTHOT,
        LEFTNORMAL,
        LEFTPRESSED,
        MIXEDDISABLED,
        MIXEDHOT,
        MIXEDNORMAL,
        MIXEDPRESSED,
        NORMAL,
        PRESSED,
        OPENED,
        PUSHED,
        READONLY,
        RIGHTDISABLED,
        RIGHTHOT,
        RIGHTNORMAL,
        RIGHTPRESSED,
        SELECTED,
        UNCHECKEDDISABLED,
        UNCHECKEDHOT,
        UNCHECKEDNORMAL,
        UNCHECKEDPRESSED,
        UPDISABLED,
        UPHOT,
        UPNORMAL,
        UPPRESSED,
        HOVER,
        UPHOVER,
        DOWNHOVER,
        LEFTHOVER,
        RIGHTHOVER,
        SORTEDDOWN,
        SORTEDHOT,
        SORTEDNORMAL,
        SORTEDPRESSED,
        SORTEDUP;

        private static EnumMap<Part, State[]> stateMap;

        private static synchronized void initStates() {
            stateMap = new EnumMap<>(Part.class);
            stateMap.put((EnumMap<Part, State[]>) Part.EP_EDITTEXT, (Part) new State[]{NORMAL, HOT, SELECTED, DISABLED, FOCUSED, READONLY, ASSIST});
            stateMap.put((EnumMap<Part, State[]>) Part.BP_PUSHBUTTON, (Part) new State[]{NORMAL, HOT, PRESSED, DISABLED, DEFAULTED});
            stateMap.put((EnumMap<Part, State[]>) Part.BP_RADIOBUTTON, (Part) new State[]{UNCHECKEDNORMAL, UNCHECKEDHOT, UNCHECKEDPRESSED, UNCHECKEDDISABLED, CHECKEDNORMAL, CHECKEDHOT, CHECKEDPRESSED, CHECKEDDISABLED});
            stateMap.put((EnumMap<Part, State[]>) Part.BP_CHECKBOX, (Part) new State[]{UNCHECKEDNORMAL, UNCHECKEDHOT, UNCHECKEDPRESSED, UNCHECKEDDISABLED, CHECKEDNORMAL, CHECKEDHOT, CHECKEDPRESSED, CHECKEDDISABLED, MIXEDNORMAL, MIXEDHOT, MIXEDPRESSED, MIXEDDISABLED});
            State[] stateArr = {NORMAL, HOT, PRESSED, DISABLED};
            stateMap.put((EnumMap<Part, State[]>) Part.CP_COMBOBOX, (Part) stateArr);
            stateMap.put((EnumMap<Part, State[]>) Part.CP_DROPDOWNBUTTON, (Part) stateArr);
            stateMap.put((EnumMap<Part, State[]>) Part.CP_BACKGROUND, (Part) stateArr);
            stateMap.put((EnumMap<Part, State[]>) Part.CP_TRANSPARENTBACKGROUND, (Part) stateArr);
            stateMap.put((EnumMap<Part, State[]>) Part.CP_BORDER, (Part) stateArr);
            stateMap.put((EnumMap<Part, State[]>) Part.CP_READONLY, (Part) stateArr);
            stateMap.put((EnumMap<Part, State[]>) Part.CP_DROPDOWNBUTTONRIGHT, (Part) stateArr);
            stateMap.put((EnumMap<Part, State[]>) Part.CP_DROPDOWNBUTTONLEFT, (Part) stateArr);
            stateMap.put((EnumMap<Part, State[]>) Part.CP_CUEBANNER, (Part) stateArr);
            stateMap.put((EnumMap<Part, State[]>) Part.HP_HEADERITEM, (Part) new State[]{NORMAL, HOT, PRESSED, SORTEDNORMAL, SORTEDHOT, SORTEDPRESSED, ICONNORMAL, ICONHOT, ICONPRESSED, ICONSORTEDNORMAL, ICONSORTEDHOT, ICONSORTEDPRESSED});
            stateMap.put((EnumMap<Part, State[]>) Part.HP_HEADERSORTARROW, (Part) new State[]{SORTEDDOWN, SORTEDUP});
            State[] stateArr2 = {NORMAL, HOT, PRESSED, DISABLED, HOVER};
            stateMap.put((EnumMap<Part, State[]>) Part.SBP_SCROLLBAR, (Part) stateArr2);
            stateMap.put((EnumMap<Part, State[]>) Part.SBP_THUMBBTNVERT, (Part) stateArr2);
            stateMap.put((EnumMap<Part, State[]>) Part.SBP_THUMBBTNHORZ, (Part) stateArr2);
            stateMap.put((EnumMap<Part, State[]>) Part.SBP_GRIPPERVERT, (Part) stateArr2);
            stateMap.put((EnumMap<Part, State[]>) Part.SBP_GRIPPERHORZ, (Part) stateArr2);
            stateMap.put((EnumMap<Part, State[]>) Part.SBP_ARROWBTN, (Part) new State[]{UPNORMAL, UPHOT, UPPRESSED, UPDISABLED, DOWNNORMAL, DOWNHOT, DOWNPRESSED, DOWNDISABLED, LEFTNORMAL, LEFTHOT, LEFTPRESSED, LEFTDISABLED, RIGHTNORMAL, RIGHTHOT, RIGHTPRESSED, RIGHTDISABLED, UPHOVER, DOWNHOVER, LEFTHOVER, RIGHTHOVER});
            State[] stateArr3 = {NORMAL, HOT, PRESSED, DISABLED};
            stateMap.put((EnumMap<Part, State[]>) Part.SPNP_UP, (Part) stateArr3);
            stateMap.put((EnumMap<Part, State[]>) Part.SPNP_DOWN, (Part) stateArr3);
            stateMap.put((EnumMap<Part, State[]>) Part.TVP_GLYPH, (Part) new State[]{CLOSED, OPENED});
            State[] stateArr4 = {NORMAL, HOT, PUSHED, DISABLED, INACTIVENORMAL, INACTIVEHOT, INACTIVEPUSHED, INACTIVEDISABLED};
            if (ThemeReader.getInt(Control.WINDOW.toString(), Part.WP_CLOSEBUTTON.getValue(), 1, Prop.IMAGECOUNT.getValue()) == 10) {
                stateArr4 = new State[]{NORMAL, HOT, PUSHED, DISABLED, null, INACTIVENORMAL, INACTIVEHOT, INACTIVEPUSHED, INACTIVEDISABLED, null};
            }
            stateMap.put((EnumMap<Part, State[]>) Part.WP_MINBUTTON, (Part) stateArr4);
            stateMap.put((EnumMap<Part, State[]>) Part.WP_MAXBUTTON, (Part) stateArr4);
            stateMap.put((EnumMap<Part, State[]>) Part.WP_RESTOREBUTTON, (Part) stateArr4);
            stateMap.put((EnumMap<Part, State[]>) Part.WP_CLOSEBUTTON, (Part) stateArr4);
            stateMap.put((EnumMap<Part, State[]>) Part.TKP_TRACK, (Part) new State[]{NORMAL});
            stateMap.put((EnumMap<Part, State[]>) Part.TKP_TRACKVERT, (Part) new State[]{NORMAL});
            State[] stateArr5 = {NORMAL, HOT, PRESSED, FOCUSED, DISABLED};
            stateMap.put((EnumMap<Part, State[]>) Part.TKP_THUMB, (Part) stateArr5);
            stateMap.put((EnumMap<Part, State[]>) Part.TKP_THUMBBOTTOM, (Part) stateArr5);
            stateMap.put((EnumMap<Part, State[]>) Part.TKP_THUMBTOP, (Part) stateArr5);
            stateMap.put((EnumMap<Part, State[]>) Part.TKP_THUMBVERT, (Part) stateArr5);
            stateMap.put((EnumMap<Part, State[]>) Part.TKP_THUMBRIGHT, (Part) stateArr5);
            State[] stateArr6 = {NORMAL, HOT, SELECTED, DISABLED, FOCUSED};
            stateMap.put((EnumMap<Part, State[]>) Part.TABP_TABITEM, (Part) stateArr6);
            stateMap.put((EnumMap<Part, State[]>) Part.TABP_TABITEMLEFTEDGE, (Part) stateArr6);
            stateMap.put((EnumMap<Part, State[]>) Part.TABP_TABITEMRIGHTEDGE, (Part) stateArr6);
            stateMap.put((EnumMap<Part, State[]>) Part.TP_BUTTON, (Part) new State[]{NORMAL, HOT, PRESSED, DISABLED, CHECKED, HOTCHECKED});
            State[] stateArr7 = {ACTIVE, INACTIVE};
            stateMap.put((EnumMap<Part, State[]>) Part.WP_WINDOW, (Part) stateArr7);
            stateMap.put((EnumMap<Part, State[]>) Part.WP_FRAMELEFT, (Part) stateArr7);
            stateMap.put((EnumMap<Part, State[]>) Part.WP_FRAMERIGHT, (Part) stateArr7);
            stateMap.put((EnumMap<Part, State[]>) Part.WP_FRAMEBOTTOM, (Part) stateArr7);
            State[] stateArr8 = {ACTIVE, INACTIVE, DISABLED};
            stateMap.put((EnumMap<Part, State[]>) Part.WP_CAPTION, (Part) stateArr8);
            stateMap.put((EnumMap<Part, State[]>) Part.WP_MINCAPTION, (Part) stateArr8);
            stateMap.put((EnumMap<Part, State[]>) Part.WP_MAXCAPTION, (Part) stateArr8);
            stateMap.put((EnumMap<Part, State[]>) Part.MP_BARBACKGROUND, (Part) new State[]{ACTIVE, INACTIVE});
            stateMap.put((EnumMap<Part, State[]>) Part.MP_BARITEM, (Part) new State[]{NORMAL, HOT, PUSHED, DISABLED, DISABLEDHOT, DISABLEDPUSHED});
            stateMap.put((EnumMap<Part, State[]>) Part.MP_POPUPCHECK, (Part) new State[]{CHECKMARKNORMAL, CHECKMARKDISABLED, BULLETNORMAL, BULLETDISABLED});
            stateMap.put((EnumMap<Part, State[]>) Part.MP_POPUPCHECKBACKGROUND, (Part) new State[]{DISABLEDPUSHED, NORMAL, BITMAP});
            stateMap.put((EnumMap<Part, State[]>) Part.MP_POPUPITEM, (Part) new State[]{NORMAL, HOT, DISABLED, DISABLEDHOT});
            stateMap.put((EnumMap<Part, State[]>) Part.MP_POPUPSUBMENU, (Part) new State[]{NORMAL, DISABLED});
        }

        public static synchronized int getValue(Part part, State state) {
            if (stateMap == null) {
                initStates();
            }
            State[] stateArr = stateMap.get(part);
            if (stateArr != null) {
                for (int i2 = 0; i2 < stateArr.length; i2++) {
                    if (state == stateArr[i2]) {
                        return i2 + 1;
                    }
                }
            }
            if (state == null || state == NORMAL) {
                return 1;
            }
            return 0;
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/TMSchema$Prop.class */
    public enum Prop {
        COLOR(Color.class, 204),
        SIZE(Dimension.class, 207),
        FLATMENUS(Boolean.class, 1001),
        BORDERONLY(Boolean.class, 2203),
        IMAGECOUNT(Integer.class, 2401),
        BORDERSIZE(Integer.class, 2403),
        PROGRESSCHUNKSIZE(Integer.class, 2411),
        PROGRESSSPACESIZE(Integer.class, 2412),
        TEXTSHADOWOFFSET(Point.class, 3402),
        NORMALSIZE(Dimension.class, 3409),
        SIZINGMARGINS(Insets.class, 3601),
        CONTENTMARGINS(Insets.class, 3602),
        CAPTIONMARGINS(Insets.class, 3603),
        BORDERCOLOR(Color.class, 3801),
        FILLCOLOR(Color.class, 3802),
        TEXTCOLOR(Color.class, 3803),
        TEXTSHADOWCOLOR(Color.class, 3818),
        BGTYPE(Integer.class, 4001),
        TEXTSHADOWTYPE(Integer.class, 4010),
        TRANSITIONDURATIONS(Integer.class, TickCalculation.TICKS_PER_SECOND);

        private final Class type;
        private final int value;

        Prop(Class cls, int i2) {
            this.type = cls;
            this.value = i2;
        }

        public int getValue() {
            return this.value;
        }

        @Override // java.lang.Enum
        public String toString() {
            return name() + "[" + this.type.getName() + "] = " + this.value;
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/TMSchema$TypeEnum.class */
    public enum TypeEnum {
        BT_IMAGEFILE(Prop.BGTYPE, "imagefile", 0),
        BT_BORDERFILL(Prop.BGTYPE, "borderfill", 1),
        TST_NONE(Prop.TEXTSHADOWTYPE, Separation.COLORANT_NONE, 0),
        TST_SINGLE(Prop.TEXTSHADOWTYPE, Constants.ATTRVAL_SINGLE, 1),
        TST_CONTINUOUS(Prop.TEXTSHADOWTYPE, "continuous", 2);

        private final Prop prop;
        private final String enumName;
        private final int value;

        TypeEnum(Prop prop, String str, int i2) {
            this.prop = prop;
            this.enumName = str;
            this.value = i2;
        }

        @Override // java.lang.Enum
        public String toString() {
            return ((Object) this.prop) + "=" + this.enumName + "=" + this.value;
        }

        String getName() {
            return this.enumName;
        }

        static TypeEnum getTypeEnum(Prop prop, int i2) {
            for (TypeEnum typeEnum : values()) {
                if (typeEnum.prop == prop && typeEnum.value == i2) {
                    return typeEnum;
                }
            }
            return null;
        }
    }
}
