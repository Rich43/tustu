package javax.swing.plaf.synth;

import java.awt.Graphics;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import sun.swing.plaf.synth.DefaultSynthStyle;

/* loaded from: rt.jar:javax/swing/plaf/synth/ParsedSynthStyle.class */
class ParsedSynthStyle extends DefaultSynthStyle {
    private static SynthPainter DELEGATING_PAINTER_INSTANCE = new DelegatingPainter();
    private PainterInfo[] _painters;

    /* JADX INFO: Access modifiers changed from: private */
    public static PainterInfo[] mergePainterInfo(PainterInfo[] painterInfoArr, PainterInfo[] painterInfoArr2) {
        if (painterInfoArr == null) {
            return painterInfoArr2;
        }
        if (painterInfoArr2 == null) {
            return painterInfoArr;
        }
        int length = painterInfoArr.length;
        int length2 = painterInfoArr2.length;
        int i2 = 0;
        PainterInfo[] painterInfoArr3 = new PainterInfo[length + length2];
        System.arraycopy(painterInfoArr, 0, painterInfoArr3, 0, length);
        for (int i3 = 0; i3 < length2; i3++) {
            boolean z2 = false;
            int i4 = 0;
            while (true) {
                if (i4 >= length - i2) {
                    break;
                }
                if (!painterInfoArr2[i3].equalsPainter(painterInfoArr[i4])) {
                    i4++;
                } else {
                    painterInfoArr3[i4] = painterInfoArr2[i3];
                    i2++;
                    z2 = true;
                    break;
                }
            }
            if (!z2) {
                painterInfoArr3[(length + i3) - i2] = painterInfoArr2[i3];
            }
        }
        if (i2 > 0) {
            painterInfoArr3 = new PainterInfo[painterInfoArr3.length - i2];
            System.arraycopy(painterInfoArr3, 0, painterInfoArr3, 0, painterInfoArr3.length);
        }
        return painterInfoArr3;
    }

    public ParsedSynthStyle() {
    }

    public ParsedSynthStyle(DefaultSynthStyle defaultSynthStyle) {
        super(defaultSynthStyle);
        if (defaultSynthStyle instanceof ParsedSynthStyle) {
            ParsedSynthStyle parsedSynthStyle = (ParsedSynthStyle) defaultSynthStyle;
            if (parsedSynthStyle._painters != null) {
                this._painters = parsedSynthStyle._painters;
            }
        }
    }

    @Override // sun.swing.plaf.synth.DefaultSynthStyle, javax.swing.plaf.synth.SynthStyle
    public SynthPainter getPainter(SynthContext synthContext) {
        return DELEGATING_PAINTER_INSTANCE;
    }

    public void setPainters(PainterInfo[] painterInfoArr) {
        this._painters = painterInfoArr;
    }

    @Override // sun.swing.plaf.synth.DefaultSynthStyle
    public DefaultSynthStyle addTo(DefaultSynthStyle defaultSynthStyle) {
        if (!(defaultSynthStyle instanceof ParsedSynthStyle)) {
            defaultSynthStyle = new ParsedSynthStyle(defaultSynthStyle);
        }
        ParsedSynthStyle parsedSynthStyle = (ParsedSynthStyle) super.addTo(defaultSynthStyle);
        parsedSynthStyle._painters = mergePainterInfo(parsedSynthStyle._painters, this._painters);
        return parsedSynthStyle;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public SynthPainter getBestPainter(SynthContext synthContext, String str, int i2) {
        SynthPainter bestPainter;
        StateInfo stateInfo = (StateInfo) getStateInfo(synthContext.getComponentState());
        if (stateInfo != null && (bestPainter = getBestPainter(stateInfo.getPainters(), str, i2)) != null) {
            return bestPainter;
        }
        SynthPainter bestPainter2 = getBestPainter(this._painters, str, i2);
        if (bestPainter2 != null) {
            return bestPainter2;
        }
        return SynthPainter.NULL_PAINTER;
    }

    private SynthPainter getBestPainter(PainterInfo[] painterInfoArr, String str, int i2) {
        if (painterInfoArr != null) {
            SynthPainter painter = null;
            SynthPainter painter2 = null;
            for (int length = painterInfoArr.length - 1; length >= 0; length--) {
                PainterInfo painterInfo = painterInfoArr[length];
                if (painterInfo.getMethod() == str) {
                    if (painterInfo.getDirection() == i2) {
                        return painterInfo.getPainter();
                    }
                    if (painter2 == null && painterInfo.getDirection() == -1) {
                        painter2 = painterInfo.getPainter();
                    }
                } else if (painter == null && painterInfo.getMethod() == null) {
                    painter = painterInfo.getPainter();
                }
            }
            if (painter2 != null) {
                return painter2;
            }
            return painter;
        }
        return null;
    }

    @Override // sun.swing.plaf.synth.DefaultSynthStyle
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer(super.toString());
        if (this._painters != null) {
            stringBuffer.append(",painters=[");
            for (int i2 = 0; i2 < this._painters.length; i2++) {
                stringBuffer.append(this._painters[i2].toString());
            }
            stringBuffer.append("]");
        }
        return stringBuffer.toString();
    }

    /* loaded from: rt.jar:javax/swing/plaf/synth/ParsedSynthStyle$StateInfo.class */
    static class StateInfo extends DefaultSynthStyle.StateInfo {
        private PainterInfo[] _painterInfo;

        public StateInfo() {
        }

        public StateInfo(DefaultSynthStyle.StateInfo stateInfo) {
            super(stateInfo);
            if (stateInfo instanceof StateInfo) {
                this._painterInfo = ((StateInfo) stateInfo)._painterInfo;
            }
        }

        public void setPainters(PainterInfo[] painterInfoArr) {
            this._painterInfo = painterInfoArr;
        }

        public PainterInfo[] getPainters() {
            return this._painterInfo;
        }

        @Override // sun.swing.plaf.synth.DefaultSynthStyle.StateInfo
        public Object clone() {
            return new StateInfo(this);
        }

        @Override // sun.swing.plaf.synth.DefaultSynthStyle.StateInfo
        public DefaultSynthStyle.StateInfo addTo(DefaultSynthStyle.StateInfo stateInfo) {
            DefaultSynthStyle.StateInfo stateInfoAddTo;
            if (!(stateInfo instanceof StateInfo)) {
                stateInfoAddTo = new StateInfo(stateInfo);
            } else {
                stateInfoAddTo = super.addTo(stateInfo);
                StateInfo stateInfo2 = (StateInfo) stateInfoAddTo;
                stateInfo2._painterInfo = ParsedSynthStyle.mergePainterInfo(stateInfo2._painterInfo, this._painterInfo);
            }
            return stateInfoAddTo;
        }

        @Override // sun.swing.plaf.synth.DefaultSynthStyle.StateInfo
        public String toString() {
            StringBuffer stringBuffer = new StringBuffer(super.toString());
            stringBuffer.append(",painters=[");
            if (this._painterInfo != null) {
                for (int i2 = 0; i2 < this._painterInfo.length; i2++) {
                    stringBuffer.append("    ").append(this._painterInfo[i2].toString());
                }
            }
            stringBuffer.append("]");
            return stringBuffer.toString();
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/synth/ParsedSynthStyle$PainterInfo.class */
    static class PainterInfo {
        private String _method;
        private SynthPainter _painter;
        private int _direction;

        PainterInfo(String str, SynthPainter synthPainter, int i2) {
            if (str != null) {
                this._method = str.intern();
            }
            this._painter = synthPainter;
            this._direction = i2;
        }

        void addPainter(SynthPainter synthPainter) {
            if (!(this._painter instanceof AggregatePainter)) {
                this._painter = new AggregatePainter(this._painter);
            }
            ((AggregatePainter) this._painter).addPainter(synthPainter);
        }

        String getMethod() {
            return this._method;
        }

        SynthPainter getPainter() {
            return this._painter;
        }

        int getDirection() {
            return this._direction;
        }

        boolean equalsPainter(PainterInfo painterInfo) {
            return this._method == painterInfo._method && this._direction == painterInfo._direction;
        }

        public String toString() {
            return "PainterInfo {method=" + this._method + ",direction=" + this._direction + ",painter=" + ((Object) this._painter) + "}";
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/synth/ParsedSynthStyle$AggregatePainter.class */
    private static class AggregatePainter extends SynthPainter {
        private List<SynthPainter> painters = new LinkedList();

        AggregatePainter(SynthPainter synthPainter) {
            this.painters.add(synthPainter);
        }

        void addPainter(SynthPainter synthPainter) {
            if (synthPainter != null) {
                this.painters.add(synthPainter);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintArrowButtonBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintArrowButtonBackground(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintArrowButtonBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintArrowButtonBorder(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintArrowButtonForeground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintArrowButtonForeground(synthContext, graphics, i2, i3, i4, i5, i6);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintButtonBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintButtonBackground(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintButtonBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintButtonBorder(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintCheckBoxMenuItemBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintCheckBoxMenuItemBackground(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintCheckBoxMenuItemBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintCheckBoxMenuItemBorder(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintCheckBoxBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintCheckBoxBackground(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintCheckBoxBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintCheckBoxBorder(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintColorChooserBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintColorChooserBackground(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintColorChooserBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintColorChooserBorder(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintComboBoxBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintComboBoxBackground(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintComboBoxBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintComboBoxBorder(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintDesktopIconBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintDesktopIconBackground(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintDesktopIconBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintDesktopIconBorder(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintDesktopPaneBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintDesktopPaneBackground(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintDesktopPaneBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintDesktopPaneBorder(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintEditorPaneBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintEditorPaneBackground(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintEditorPaneBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintEditorPaneBorder(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintFileChooserBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintFileChooserBackground(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintFileChooserBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintFileChooserBorder(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintFormattedTextFieldBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintFormattedTextFieldBackground(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintFormattedTextFieldBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintFormattedTextFieldBorder(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintInternalFrameTitlePaneBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintInternalFrameTitlePaneBackground(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintInternalFrameTitlePaneBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintInternalFrameTitlePaneBorder(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintInternalFrameBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintInternalFrameBackground(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintInternalFrameBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintInternalFrameBorder(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintLabelBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintLabelBackground(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintLabelBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintLabelBorder(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintListBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintListBackground(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintListBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintListBorder(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintMenuBarBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintMenuBarBackground(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintMenuBarBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintMenuBarBorder(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintMenuItemBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintMenuItemBackground(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintMenuItemBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintMenuItemBorder(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintMenuBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintMenuBackground(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintMenuBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintMenuBorder(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintOptionPaneBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintOptionPaneBackground(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintOptionPaneBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintOptionPaneBorder(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintPanelBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintPanelBackground(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintPanelBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintPanelBorder(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintPasswordFieldBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintPasswordFieldBackground(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintPasswordFieldBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintPasswordFieldBorder(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintPopupMenuBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintPopupMenuBackground(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintPopupMenuBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintPopupMenuBorder(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintProgressBarBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintProgressBarBackground(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintProgressBarBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintProgressBarBackground(synthContext, graphics, i2, i3, i4, i5, i6);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintProgressBarBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintProgressBarBorder(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintProgressBarBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintProgressBarBorder(synthContext, graphics, i2, i3, i4, i5, i6);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintProgressBarForeground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintProgressBarForeground(synthContext, graphics, i2, i3, i4, i5, i6);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintRadioButtonMenuItemBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintRadioButtonMenuItemBackground(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintRadioButtonMenuItemBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintRadioButtonMenuItemBorder(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintRadioButtonBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintRadioButtonBackground(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintRadioButtonBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintRadioButtonBorder(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintRootPaneBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintRootPaneBackground(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintRootPaneBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintRootPaneBorder(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintScrollBarBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintScrollBarBackground(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintScrollBarBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintScrollBarBackground(synthContext, graphics, i2, i3, i4, i5, i6);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintScrollBarBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintScrollBarBorder(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintScrollBarBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintScrollBarBorder(synthContext, graphics, i2, i3, i4, i5, i6);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintScrollBarThumbBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintScrollBarThumbBackground(synthContext, graphics, i2, i3, i4, i5, i6);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintScrollBarThumbBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintScrollBarThumbBorder(synthContext, graphics, i2, i3, i4, i5, i6);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintScrollBarTrackBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintScrollBarTrackBackground(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintScrollBarTrackBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintScrollBarTrackBackground(synthContext, graphics, i2, i3, i4, i5, i6);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintScrollBarTrackBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintScrollBarTrackBorder(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintScrollBarTrackBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintScrollBarTrackBorder(synthContext, graphics, i2, i3, i4, i5, i6);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintScrollPaneBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintScrollPaneBackground(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintScrollPaneBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintScrollPaneBorder(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintSeparatorBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintSeparatorBackground(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintSeparatorBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintSeparatorBackground(synthContext, graphics, i2, i3, i4, i5, i6);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintSeparatorBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintSeparatorBorder(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintSeparatorBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintSeparatorBorder(synthContext, graphics, i2, i3, i4, i5, i6);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintSeparatorForeground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintSeparatorForeground(synthContext, graphics, i2, i3, i4, i5, i6);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintSliderBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintSliderBackground(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintSliderBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintSliderBackground(synthContext, graphics, i2, i3, i4, i5, i6);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintSliderBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintSliderBorder(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintSliderBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintSliderBorder(synthContext, graphics, i2, i3, i4, i5, i6);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintSliderThumbBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintSliderThumbBackground(synthContext, graphics, i2, i3, i4, i5, i6);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintSliderThumbBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintSliderThumbBorder(synthContext, graphics, i2, i3, i4, i5, i6);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintSliderTrackBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintSliderTrackBackground(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintSliderTrackBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintSliderTrackBackground(synthContext, graphics, i2, i3, i4, i5, i6);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintSliderTrackBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintSliderTrackBorder(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintSliderTrackBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintSliderTrackBorder(synthContext, graphics, i2, i3, i4, i5, i6);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintSpinnerBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintSpinnerBackground(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintSpinnerBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintSpinnerBorder(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintSplitPaneDividerBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintSplitPaneDividerBackground(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintSplitPaneDividerBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintSplitPaneDividerBackground(synthContext, graphics, i2, i3, i4, i5, i6);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintSplitPaneDividerForeground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintSplitPaneDividerForeground(synthContext, graphics, i2, i3, i4, i5, i6);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintSplitPaneDragDivider(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintSplitPaneDragDivider(synthContext, graphics, i2, i3, i4, i5, i6);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintSplitPaneBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintSplitPaneBackground(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintSplitPaneBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintSplitPaneBorder(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintTabbedPaneBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintTabbedPaneBackground(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintTabbedPaneBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintTabbedPaneBorder(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintTabbedPaneTabAreaBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintTabbedPaneTabAreaBackground(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintTabbedPaneTabAreaBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintTabbedPaneTabAreaBackground(synthContext, graphics, i2, i3, i4, i5, i6);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintTabbedPaneTabAreaBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintTabbedPaneTabAreaBorder(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintTabbedPaneTabAreaBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintTabbedPaneTabAreaBorder(synthContext, graphics, i2, i3, i4, i5, i6);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintTabbedPaneTabBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintTabbedPaneTabBackground(synthContext, graphics, i2, i3, i4, i5, i6);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintTabbedPaneTabBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6, int i7) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintTabbedPaneTabBackground(synthContext, graphics, i2, i3, i4, i5, i6, i7);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintTabbedPaneTabBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintTabbedPaneTabBorder(synthContext, graphics, i2, i3, i4, i5, i6);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintTabbedPaneTabBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6, int i7) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintTabbedPaneTabBorder(synthContext, graphics, i2, i3, i4, i5, i6, i7);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintTabbedPaneContentBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintTabbedPaneContentBackground(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintTabbedPaneContentBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintTabbedPaneContentBorder(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintTableHeaderBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintTableHeaderBackground(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintTableHeaderBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintTableHeaderBorder(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintTableBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintTableBackground(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintTableBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintTableBorder(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintTextAreaBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintTextAreaBackground(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintTextAreaBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintTextAreaBorder(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintTextPaneBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintTextPaneBackground(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintTextPaneBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintTextPaneBorder(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintTextFieldBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintTextFieldBackground(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintTextFieldBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintTextFieldBorder(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintToggleButtonBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintToggleButtonBackground(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintToggleButtonBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintToggleButtonBorder(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintToolBarBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintToolBarBackground(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintToolBarBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintToolBarBackground(synthContext, graphics, i2, i3, i4, i5, i6);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintToolBarBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintToolBarBorder(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintToolBarBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintToolBarBorder(synthContext, graphics, i2, i3, i4, i5, i6);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintToolBarContentBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintToolBarContentBackground(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintToolBarContentBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintToolBarContentBackground(synthContext, graphics, i2, i3, i4, i5, i6);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintToolBarContentBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintToolBarContentBorder(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintToolBarContentBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintToolBarContentBorder(synthContext, graphics, i2, i3, i4, i5, i6);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintToolBarDragWindowBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintToolBarDragWindowBackground(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintToolBarDragWindowBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintToolBarDragWindowBackground(synthContext, graphics, i2, i3, i4, i5, i6);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintToolBarDragWindowBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintToolBarDragWindowBorder(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintToolBarDragWindowBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintToolBarDragWindowBorder(synthContext, graphics, i2, i3, i4, i5, i6);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintToolTipBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintToolTipBackground(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintToolTipBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintToolTipBorder(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintTreeBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintTreeBackground(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintTreeBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintTreeBorder(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintTreeCellBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintTreeCellBackground(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintTreeCellBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintTreeCellBorder(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintTreeCellFocus(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintTreeCellFocus(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintViewportBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintViewportBackground(synthContext, graphics, i2, i3, i4, i5);
            }
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintViewportBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            Iterator<SynthPainter> it = this.painters.iterator();
            while (it.hasNext()) {
                it.next().paintViewportBorder(synthContext, graphics, i2, i3, i4, i5);
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/synth/ParsedSynthStyle$DelegatingPainter.class */
    private static class DelegatingPainter extends SynthPainter {
        private DelegatingPainter() {
        }

        private static SynthPainter getPainter(SynthContext synthContext, String str, int i2) {
            return ((ParsedSynthStyle) synthContext.getStyle()).getBestPainter(synthContext, str, i2);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintArrowButtonBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "arrowbuttonbackground", -1).paintArrowButtonBackground(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintArrowButtonBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "arrowbuttonborder", -1).paintArrowButtonBorder(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintArrowButtonForeground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            getPainter(synthContext, "arrowbuttonforeground", i6).paintArrowButtonForeground(synthContext, graphics, i2, i3, i4, i5, i6);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintButtonBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "buttonbackground", -1).paintButtonBackground(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintButtonBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "buttonborder", -1).paintButtonBorder(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintCheckBoxMenuItemBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "checkboxmenuitembackground", -1).paintCheckBoxMenuItemBackground(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintCheckBoxMenuItemBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "checkboxmenuitemborder", -1).paintCheckBoxMenuItemBorder(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintCheckBoxBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "checkboxbackground", -1).paintCheckBoxBackground(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintCheckBoxBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "checkboxborder", -1).paintCheckBoxBorder(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintColorChooserBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "colorchooserbackground", -1).paintColorChooserBackground(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintColorChooserBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "colorchooserborder", -1).paintColorChooserBorder(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintComboBoxBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "comboboxbackground", -1).paintComboBoxBackground(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintComboBoxBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "comboboxborder", -1).paintComboBoxBorder(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintDesktopIconBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "desktopiconbackground", -1).paintDesktopIconBackground(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintDesktopIconBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "desktopiconborder", -1).paintDesktopIconBorder(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintDesktopPaneBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "desktoppanebackground", -1).paintDesktopPaneBackground(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintDesktopPaneBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "desktoppaneborder", -1).paintDesktopPaneBorder(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintEditorPaneBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "editorpanebackground", -1).paintEditorPaneBackground(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintEditorPaneBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "editorpaneborder", -1).paintEditorPaneBorder(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintFileChooserBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "filechooserbackground", -1).paintFileChooserBackground(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintFileChooserBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "filechooserborder", -1).paintFileChooserBorder(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintFormattedTextFieldBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "formattedtextfieldbackground", -1).paintFormattedTextFieldBackground(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintFormattedTextFieldBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "formattedtextfieldborder", -1).paintFormattedTextFieldBorder(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintInternalFrameTitlePaneBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "internalframetitlepanebackground", -1).paintInternalFrameTitlePaneBackground(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintInternalFrameTitlePaneBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "internalframetitlepaneborder", -1).paintInternalFrameTitlePaneBorder(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintInternalFrameBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "internalframebackground", -1).paintInternalFrameBackground(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintInternalFrameBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "internalframeborder", -1).paintInternalFrameBorder(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintLabelBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "labelbackground", -1).paintLabelBackground(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintLabelBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "labelborder", -1).paintLabelBorder(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintListBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "listbackground", -1).paintListBackground(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintListBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "listborder", -1).paintListBorder(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintMenuBarBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "menubarbackground", -1).paintMenuBarBackground(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintMenuBarBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "menubarborder", -1).paintMenuBarBorder(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintMenuItemBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "menuitembackground", -1).paintMenuItemBackground(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintMenuItemBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "menuitemborder", -1).paintMenuItemBorder(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintMenuBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "menubackground", -1).paintMenuBackground(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintMenuBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "menuborder", -1).paintMenuBorder(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintOptionPaneBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "optionpanebackground", -1).paintOptionPaneBackground(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintOptionPaneBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "optionpaneborder", -1).paintOptionPaneBorder(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintPanelBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "panelbackground", -1).paintPanelBackground(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintPanelBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "panelborder", -1).paintPanelBorder(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintPasswordFieldBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "passwordfieldbackground", -1).paintPasswordFieldBackground(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintPasswordFieldBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "passwordfieldborder", -1).paintPasswordFieldBorder(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintPopupMenuBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "popupmenubackground", -1).paintPopupMenuBackground(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintPopupMenuBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "popupmenuborder", -1).paintPopupMenuBorder(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintProgressBarBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "progressbarbackground", -1).paintProgressBarBackground(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintProgressBarBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            getPainter(synthContext, "progressbarbackground", i6).paintProgressBarBackground(synthContext, graphics, i2, i3, i4, i5, i6);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintProgressBarBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "progressbarborder", -1).paintProgressBarBorder(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintProgressBarBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            getPainter(synthContext, "progressbarborder", i6).paintProgressBarBorder(synthContext, graphics, i2, i3, i4, i5, i6);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintProgressBarForeground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            getPainter(synthContext, "progressbarforeground", i6).paintProgressBarForeground(synthContext, graphics, i2, i3, i4, i5, i6);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintRadioButtonMenuItemBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "radiobuttonmenuitembackground", -1).paintRadioButtonMenuItemBackground(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintRadioButtonMenuItemBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "radiobuttonmenuitemborder", -1).paintRadioButtonMenuItemBorder(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintRadioButtonBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "radiobuttonbackground", -1).paintRadioButtonBackground(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintRadioButtonBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "radiobuttonborder", -1).paintRadioButtonBorder(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintRootPaneBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "rootpanebackground", -1).paintRootPaneBackground(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintRootPaneBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "rootpaneborder", -1).paintRootPaneBorder(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintScrollBarBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "scrollbarbackground", -1).paintScrollBarBackground(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintScrollBarBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            getPainter(synthContext, "scrollbarbackground", i6).paintScrollBarBackground(synthContext, graphics, i2, i3, i4, i5, i6);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintScrollBarBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "scrollbarborder", -1).paintScrollBarBorder(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintScrollBarBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            getPainter(synthContext, "scrollbarborder", i6).paintScrollBarBorder(synthContext, graphics, i2, i3, i4, i5, i6);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintScrollBarThumbBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            getPainter(synthContext, "scrollbarthumbbackground", i6).paintScrollBarThumbBackground(synthContext, graphics, i2, i3, i4, i5, i6);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintScrollBarThumbBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            getPainter(synthContext, "scrollbarthumbborder", i6).paintScrollBarThumbBorder(synthContext, graphics, i2, i3, i4, i5, i6);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintScrollBarTrackBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "scrollbartrackbackground", -1).paintScrollBarTrackBackground(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintScrollBarTrackBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            getPainter(synthContext, "scrollbartrackbackground", i6).paintScrollBarTrackBackground(synthContext, graphics, i2, i3, i4, i5, i6);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintScrollBarTrackBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "scrollbartrackborder", -1).paintScrollBarTrackBorder(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintScrollBarTrackBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            getPainter(synthContext, "scrollbartrackborder", i6).paintScrollBarTrackBorder(synthContext, graphics, i2, i3, i4, i5, i6);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintScrollPaneBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "scrollpanebackground", -1).paintScrollPaneBackground(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintScrollPaneBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "scrollpaneborder", -1).paintScrollPaneBorder(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintSeparatorBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "separatorbackground", -1).paintSeparatorBackground(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintSeparatorBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            getPainter(synthContext, "separatorbackground", i6).paintSeparatorBackground(synthContext, graphics, i2, i3, i4, i5, i6);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintSeparatorBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "separatorborder", -1).paintSeparatorBorder(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintSeparatorBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            getPainter(synthContext, "separatorborder", i6).paintSeparatorBorder(synthContext, graphics, i2, i3, i4, i5, i6);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintSeparatorForeground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            getPainter(synthContext, "separatorforeground", i6).paintSeparatorForeground(synthContext, graphics, i2, i3, i4, i5, i6);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintSliderBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "sliderbackground", -1).paintSliderBackground(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintSliderBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            getPainter(synthContext, "sliderbackground", i6).paintSliderBackground(synthContext, graphics, i2, i3, i4, i5, i6);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintSliderBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "sliderborder", -1).paintSliderBorder(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintSliderBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            getPainter(synthContext, "sliderborder", i6).paintSliderBorder(synthContext, graphics, i2, i3, i4, i5, i6);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintSliderThumbBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            getPainter(synthContext, "sliderthumbbackground", i6).paintSliderThumbBackground(synthContext, graphics, i2, i3, i4, i5, i6);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintSliderThumbBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            getPainter(synthContext, "sliderthumbborder", i6).paintSliderThumbBorder(synthContext, graphics, i2, i3, i4, i5, i6);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintSliderTrackBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "slidertrackbackground", -1).paintSliderTrackBackground(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintSliderTrackBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            getPainter(synthContext, "slidertrackbackground", i6).paintSliderTrackBackground(synthContext, graphics, i2, i3, i4, i5, i6);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintSliderTrackBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "slidertrackborder", -1).paintSliderTrackBorder(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintSliderTrackBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            getPainter(synthContext, "slidertrackborder", i6).paintSliderTrackBorder(synthContext, graphics, i2, i3, i4, i5, i6);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintSpinnerBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "spinnerbackground", -1).paintSpinnerBackground(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintSpinnerBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "spinnerborder", -1).paintSpinnerBorder(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintSplitPaneDividerBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "splitpanedividerbackground", -1).paintSplitPaneDividerBackground(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintSplitPaneDividerBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            getPainter(synthContext, "splitpanedividerbackground", i6).paintSplitPaneDividerBackground(synthContext, graphics, i2, i3, i4, i5, i6);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintSplitPaneDividerForeground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            getPainter(synthContext, "splitpanedividerforeground", i6).paintSplitPaneDividerForeground(synthContext, graphics, i2, i3, i4, i5, i6);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintSplitPaneDragDivider(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            getPainter(synthContext, "splitpanedragdivider", i6).paintSplitPaneDragDivider(synthContext, graphics, i2, i3, i4, i5, i6);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintSplitPaneBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "splitpanebackground", -1).paintSplitPaneBackground(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintSplitPaneBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "splitpaneborder", -1).paintSplitPaneBorder(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintTabbedPaneBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "tabbedpanebackground", -1).paintTabbedPaneBackground(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintTabbedPaneBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "tabbedpaneborder", -1).paintTabbedPaneBorder(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintTabbedPaneTabAreaBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "tabbedpanetabareabackground", -1).paintTabbedPaneTabAreaBackground(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintTabbedPaneTabAreaBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            getPainter(synthContext, "tabbedpanetabareabackground", i6).paintTabbedPaneTabAreaBackground(synthContext, graphics, i2, i3, i4, i5, i6);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintTabbedPaneTabAreaBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "tabbedpanetabareaborder", -1).paintTabbedPaneTabAreaBorder(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintTabbedPaneTabAreaBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            getPainter(synthContext, "tabbedpanetabareaborder", i6).paintTabbedPaneTabAreaBorder(synthContext, graphics, i2, i3, i4, i5, i6);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintTabbedPaneTabBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            getPainter(synthContext, "tabbedpanetabbackground", -1).paintTabbedPaneTabBackground(synthContext, graphics, i2, i3, i4, i5, i6);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintTabbedPaneTabBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6, int i7) {
            getPainter(synthContext, "tabbedpanetabbackground", i7).paintTabbedPaneTabBackground(synthContext, graphics, i2, i3, i4, i5, i6, i7);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintTabbedPaneTabBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            getPainter(synthContext, "tabbedpanetabborder", -1).paintTabbedPaneTabBorder(synthContext, graphics, i2, i3, i4, i5, i6);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintTabbedPaneTabBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6, int i7) {
            getPainter(synthContext, "tabbedpanetabborder", i7).paintTabbedPaneTabBorder(synthContext, graphics, i2, i3, i4, i5, i6, i7);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintTabbedPaneContentBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "tabbedpanecontentbackground", -1).paintTabbedPaneContentBackground(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintTabbedPaneContentBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "tabbedpanecontentborder", -1).paintTabbedPaneContentBorder(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintTableHeaderBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "tableheaderbackground", -1).paintTableHeaderBackground(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintTableHeaderBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "tableheaderborder", -1).paintTableHeaderBorder(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintTableBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "tablebackground", -1).paintTableBackground(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintTableBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "tableborder", -1).paintTableBorder(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintTextAreaBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "textareabackground", -1).paintTextAreaBackground(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintTextAreaBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "textareaborder", -1).paintTextAreaBorder(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintTextPaneBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "textpanebackground", -1).paintTextPaneBackground(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintTextPaneBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "textpaneborder", -1).paintTextPaneBorder(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintTextFieldBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "textfieldbackground", -1).paintTextFieldBackground(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintTextFieldBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "textfieldborder", -1).paintTextFieldBorder(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintToggleButtonBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "togglebuttonbackground", -1).paintToggleButtonBackground(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintToggleButtonBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "togglebuttonborder", -1).paintToggleButtonBorder(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintToolBarBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "toolbarbackground", -1).paintToolBarBackground(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintToolBarBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            getPainter(synthContext, "toolbarbackground", i6).paintToolBarBackground(synthContext, graphics, i2, i3, i4, i5, i6);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintToolBarBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "toolbarborder", -1).paintToolBarBorder(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintToolBarBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            getPainter(synthContext, "toolbarborder", i6).paintToolBarBorder(synthContext, graphics, i2, i3, i4, i5, i6);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintToolBarContentBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "toolbarcontentbackground", -1).paintToolBarContentBackground(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintToolBarContentBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            getPainter(synthContext, "toolbarcontentbackground", i6).paintToolBarContentBackground(synthContext, graphics, i2, i3, i4, i5, i6);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintToolBarContentBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "toolbarcontentborder", -1).paintToolBarContentBorder(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintToolBarContentBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            getPainter(synthContext, "toolbarcontentborder", i6).paintToolBarContentBorder(synthContext, graphics, i2, i3, i4, i5, i6);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintToolBarDragWindowBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "toolbardragwindowbackground", -1).paintToolBarDragWindowBackground(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintToolBarDragWindowBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            getPainter(synthContext, "toolbardragwindowbackground", i6).paintToolBarDragWindowBackground(synthContext, graphics, i2, i3, i4, i5, i6);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintToolBarDragWindowBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "toolbardragwindowborder", -1).paintToolBarDragWindowBorder(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintToolBarDragWindowBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
            getPainter(synthContext, "toolbardragwindowborder", i6).paintToolBarDragWindowBorder(synthContext, graphics, i2, i3, i4, i5, i6);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintToolTipBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "tooltipbackground", -1).paintToolTipBackground(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintToolTipBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "tooltipborder", -1).paintToolTipBorder(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintTreeBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "treebackground", -1).paintTreeBackground(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintTreeBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "treeborder", -1).paintTreeBorder(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintTreeCellBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "treecellbackground", -1).paintTreeCellBackground(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintTreeCellBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "treecellborder", -1).paintTreeCellBorder(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintTreeCellFocus(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "treecellfocus", -1).paintTreeCellFocus(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintViewportBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "viewportbackground", -1).paintViewportBackground(synthContext, graphics, i2, i3, i4, i5);
        }

        @Override // javax.swing.plaf.synth.SynthPainter
        public void paintViewportBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
            getPainter(synthContext, "viewportborder", -1).paintViewportBorder(synthContext, graphics, i2, i3, i4, i5);
        }
    }
}
