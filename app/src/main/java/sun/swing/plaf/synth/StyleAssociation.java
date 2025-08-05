package sun.swing.plaf.synth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.swing.plaf.synth.SynthStyle;

/* loaded from: rt.jar:sun/swing/plaf/synth/StyleAssociation.class */
public class StyleAssociation {
    private SynthStyle _style;
    private Pattern _pattern;
    private Matcher _matcher;
    private int _id;

    public static StyleAssociation createStyleAssociation(String str, SynthStyle synthStyle) throws PatternSyntaxException {
        return createStyleAssociation(str, synthStyle, 0);
    }

    public static StyleAssociation createStyleAssociation(String str, SynthStyle synthStyle, int i2) throws PatternSyntaxException {
        return new StyleAssociation(str, synthStyle, i2);
    }

    private StyleAssociation(String str, SynthStyle synthStyle, int i2) throws PatternSyntaxException {
        this._style = synthStyle;
        this._pattern = Pattern.compile(str);
        this._id = i2;
    }

    public int getID() {
        return this._id;
    }

    public synchronized boolean matches(CharSequence charSequence) {
        if (this._matcher == null) {
            this._matcher = this._pattern.matcher(charSequence);
        } else {
            this._matcher.reset(charSequence);
        }
        return this._matcher.matches();
    }

    public String getText() {
        return this._pattern.pattern();
    }

    public SynthStyle getStyle() {
        return this._style;
    }
}
