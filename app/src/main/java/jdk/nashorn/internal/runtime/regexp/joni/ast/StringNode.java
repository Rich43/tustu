package jdk.nashorn.internal.runtime.regexp.joni.ast;

import jdk.nashorn.internal.runtime.regexp.joni.EncodingHelper;
import jdk.nashorn.internal.runtime.regexp.joni.constants.StringType;
import org.icepdf.core.util.PdfOps;

/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/regexp/joni/ast/StringNode.class */
public final class StringNode extends Node implements StringType {
    private static final int NODE_STR_MARGIN = 16;
    private static final int NODE_STR_BUF_SIZE = 24;
    public char[] chars;

    /* renamed from: p, reason: collision with root package name */
    public int f12885p;
    public int end;
    public int flag;

    public StringNode() {
        this(24);
    }

    private StringNode(int size) {
        this.chars = new char[size];
        this.f12885p = 0;
        this.end = 0;
    }

    public StringNode(char[] chars, int p2, int end) {
        this.chars = chars;
        this.f12885p = p2;
        this.end = end;
        setShared();
    }

    public StringNode(char c2) {
        this();
        char[] cArr = this.chars;
        int i2 = this.end;
        this.end = i2 + 1;
        cArr[i2] = c2;
    }

    public static StringNode createEmpty() {
        return new StringNode(0);
    }

    public void ensure(int ahead) {
        int len = (this.end - this.f12885p) + ahead;
        if (len >= this.chars.length) {
            char[] tmp = new char[len + 16];
            System.arraycopy(this.chars, this.f12885p, tmp, 0, this.end - this.f12885p);
            this.chars = tmp;
        }
    }

    private void modifyEnsure(int ahead) {
        if (isShared()) {
            int len = (this.end - this.f12885p) + ahead;
            char[] tmp = new char[len + 16];
            System.arraycopy(this.chars, this.f12885p, tmp, 0, this.end - this.f12885p);
            this.chars = tmp;
            this.end -= this.f12885p;
            this.f12885p = 0;
            clearShared();
            return;
        }
        ensure(ahead);
    }

    @Override // jdk.nashorn.internal.runtime.regexp.joni.ast.Node
    public int getType() {
        return 0;
    }

    @Override // jdk.nashorn.internal.runtime.regexp.joni.ast.Node
    public String getName() {
        return "String";
    }

    @Override // jdk.nashorn.internal.runtime.regexp.joni.ast.Node
    public String toString(int level) {
        StringBuilder value = new StringBuilder();
        value.append("\n  bytes: '");
        for (int i2 = this.f12885p; i2 < this.end; i2++) {
            if (this.chars[i2] >= ' ' && this.chars[i2] < 127) {
                value.append(this.chars[i2]);
            } else {
                value.append(String.format("[0x%04x]", Integer.valueOf(this.chars[i2])));
            }
        }
        value.append(PdfOps.SINGLE_QUOTE_TOKEN);
        return value.toString();
    }

    public int length() {
        return this.end - this.f12885p;
    }

    public StringNode splitLastChar() {
        int prev;
        StringNode n2 = null;
        if (this.end > this.f12885p && (prev = EncodingHelper.prevCharHead(this.f12885p, this.end)) != -1 && prev > this.f12885p) {
            n2 = new StringNode(this.chars, prev, this.end);
            if (isRaw()) {
                n2.setRaw();
            }
            this.end = prev;
        }
        return n2;
    }

    public boolean canBeSplit() {
        return this.end > this.f12885p && 1 < this.end - this.f12885p;
    }

    public void set(char[] chars, int p2, int end) {
        this.chars = chars;
        this.f12885p = p2;
        this.end = end;
        setShared();
    }

    public void cat(char[] cat, int catP, int catEnd) {
        int len = catEnd - catP;
        modifyEnsure(len);
        System.arraycopy(cat, catP, this.chars, this.end, len);
        this.end += len;
    }

    public void cat(char c2) {
        modifyEnsure(1);
        char[] cArr = this.chars;
        int i2 = this.end;
        this.end = i2 + 1;
        cArr[i2] = c2;
    }

    public void catCode(int code) {
        cat((char) code);
    }

    public void clear() {
        if (this.chars.length > 24) {
            this.chars = new char[24];
        }
        this.flag = 0;
        this.end = 0;
        this.f12885p = 0;
    }

    public void setRaw() {
        this.flag |= 1;
    }

    public void clearRaw() {
        this.flag &= -2;
    }

    public boolean isRaw() {
        return (this.flag & 1) != 0;
    }

    public void setAmbig() {
        this.flag |= 2;
    }

    public void clearAmbig() {
        this.flag &= -3;
    }

    public boolean isAmbig() {
        return (this.flag & 2) != 0;
    }

    public void setDontGetOptInfo() {
        this.flag |= 4;
    }

    public void clearDontGetOptInfo() {
        this.flag &= -5;
    }

    public boolean isDontGetOptInfo() {
        return (this.flag & 4) != 0;
    }

    public void setShared() {
        this.flag |= 8;
    }

    public void clearShared() {
        this.flag &= -9;
    }

    public boolean isShared() {
        return (this.flag & 8) != 0;
    }
}
