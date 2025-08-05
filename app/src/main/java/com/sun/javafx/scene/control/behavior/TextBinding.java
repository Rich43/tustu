package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.PlatformUtil;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import org.icepdf.core.util.PdfOps;
import org.slf4j.Marker;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/behavior/TextBinding.class */
public class TextBinding {
    private String MNEMONIC_SYMBOL = "_";
    private String text = null;
    private String mnemonic = null;
    private KeyCombination mnemonicKeyCombination = null;
    private int mnemonicIndex = -1;
    private String extendedMnemonicText = null;

    public String getText() {
        return this.text;
    }

    public String getMnemonic() {
        return this.mnemonic;
    }

    public KeyCombination getMnemonicKeyCombination() {
        if (this.mnemonic != null && this.mnemonicKeyCombination == null) {
            this.mnemonicKeyCombination = new MnemonicKeyCombination(this.mnemonic);
        }
        return this.mnemonicKeyCombination;
    }

    public int getMnemonicIndex() {
        return this.mnemonicIndex;
    }

    public String getExtendedMnemonicText() {
        return this.extendedMnemonicText;
    }

    public TextBinding(String s2) {
        parseAndSplit(s2);
    }

    /* JADX WARN: Code restructure failed: missing block: B:20:0x0067, code lost:
    
        r6.mnemonic = r0.substring(r9 + 1, r9 + 2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0079, code lost:
    
        if (r6.mnemonic == null) goto L23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x007c, code lost:
    
        r6.mnemonicIndex = r9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0081, code lost:
    
        r0.delete(r9, r9 + 1);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void parseAndSplit(java.lang.String r7) {
        /*
            Method dump skipped, instructions count: 269
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.javafx.scene.control.behavior.TextBinding.parseAndSplit(java.lang.String):void");
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/behavior/TextBinding$MnemonicKeyCombination.class */
    public static class MnemonicKeyCombination extends KeyCombination {
        private String character;

        /* JADX WARN: Illegal instructions before constructor call */
        public MnemonicKeyCombination(String character) {
            KeyCombination.Modifier[] modifierArr = new KeyCombination.Modifier[1];
            modifierArr[0] = PlatformUtil.isMac() ? KeyCombination.META_DOWN : KeyCombination.ALT_DOWN;
            super(modifierArr);
            this.character = "";
            this.character = character;
        }

        public final String getCharacter() {
            return this.character;
        }

        @Override // javafx.scene.input.KeyCombination
        public boolean match(KeyEvent event) {
            String text = event.getText();
            return text != null && !text.isEmpty() && text.equalsIgnoreCase(getCharacter()) && super.match(event);
        }

        @Override // javafx.scene.input.KeyCombination
        public String getName() {
            StringBuilder sb = new StringBuilder();
            sb.append(super.getName());
            if (sb.length() > 0) {
                sb.append(Marker.ANY_NON_NULL_MARKER);
            }
            return sb.append('\'').append(this.character.replace(PdfOps.SINGLE_QUOTE_TOKEN, "\\'")).append('\'').toString();
        }

        @Override // javafx.scene.input.KeyCombination
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return (obj instanceof MnemonicKeyCombination) && this.character.equals(((MnemonicKeyCombination) obj).getCharacter()) && super.equals(obj);
        }

        @Override // javafx.scene.input.KeyCombination
        public int hashCode() {
            return (23 * super.hashCode()) + this.character.hashCode();
        }
    }
}
