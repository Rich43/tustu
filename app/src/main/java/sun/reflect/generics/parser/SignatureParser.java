package sun.reflect.generics.parser;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import java.lang.reflect.GenericSignatureFormatError;
import java.util.ArrayList;
import java.util.List;
import org.icepdf.core.util.PdfOps;
import sun.reflect.generics.tree.ArrayTypeSignature;
import sun.reflect.generics.tree.BaseType;
import sun.reflect.generics.tree.BooleanSignature;
import sun.reflect.generics.tree.BottomSignature;
import sun.reflect.generics.tree.ByteSignature;
import sun.reflect.generics.tree.CharSignature;
import sun.reflect.generics.tree.ClassSignature;
import sun.reflect.generics.tree.ClassTypeSignature;
import sun.reflect.generics.tree.DoubleSignature;
import sun.reflect.generics.tree.FieldTypeSignature;
import sun.reflect.generics.tree.FloatSignature;
import sun.reflect.generics.tree.FormalTypeParameter;
import sun.reflect.generics.tree.IntSignature;
import sun.reflect.generics.tree.LongSignature;
import sun.reflect.generics.tree.MethodTypeSignature;
import sun.reflect.generics.tree.ReturnType;
import sun.reflect.generics.tree.ShortSignature;
import sun.reflect.generics.tree.SimpleClassTypeSignature;
import sun.reflect.generics.tree.TypeArgument;
import sun.reflect.generics.tree.TypeSignature;
import sun.reflect.generics.tree.TypeVariableSignature;
import sun.reflect.generics.tree.VoidDescriptor;
import sun.reflect.generics.tree.Wildcard;

/* loaded from: rt.jar:sun/reflect/generics/parser/SignatureParser.class */
public class SignatureParser {
    private String input;
    private int index;
    private int mark;
    private static final char EOI = ':';
    private static final boolean DEBUG = false;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !SignatureParser.class.desiredAssertionStatus();
    }

    private SignatureParser() {
    }

    private void init(String str) {
        this.input = str;
        this.index = 0;
        this.mark = 0;
    }

    private char current() {
        if (!$assertionsDisabled && this.index > this.input.length()) {
            throw new AssertionError();
        }
        if (this.index < this.input.length()) {
            return this.input.charAt(this.index);
        }
        return ':';
    }

    private void advance() {
        if (!$assertionsDisabled && this.index > this.input.length()) {
            throw new AssertionError();
        }
        if (this.index < this.input.length()) {
            this.index++;
        }
    }

    private void mark() {
        this.mark = this.index;
    }

    private String remainder() {
        return this.input.substring(this.index);
    }

    private String markToCurrent() {
        return this.input.substring(this.mark, this.index);
    }

    private Error error(String str) {
        return new GenericSignatureFormatError("Signature Parse error: " + str + "\n\tRemaining input: " + remainder());
    }

    private void progress(int i2) {
        if (this.index <= i2) {
            throw error("Failure to make progress!");
        }
    }

    public static SignatureParser make() {
        return new SignatureParser();
    }

    public ClassSignature parseClassSig(String str) {
        init(str);
        return parseClassSignature();
    }

    public MethodTypeSignature parseMethodSig(String str) {
        init(str);
        return parseMethodTypeSignature();
    }

    public TypeSignature parseTypeSig(String str) {
        init(str);
        return parseTypeSignature();
    }

    private ClassSignature parseClassSignature() {
        if ($assertionsDisabled || this.index == 0) {
            return ClassSignature.make(parseZeroOrMoreFormalTypeParameters(), parseClassTypeSignature(), parseSuperInterfaces());
        }
        throw new AssertionError();
    }

    private FormalTypeParameter[] parseZeroOrMoreFormalTypeParameters() {
        if (current() == '<') {
            return parseFormalTypeParameters();
        }
        return new FormalTypeParameter[0];
    }

    private FormalTypeParameter[] parseFormalTypeParameters() {
        ArrayList arrayList = new ArrayList(3);
        if (!$assertionsDisabled && current() != '<') {
            throw new AssertionError();
        }
        if (current() != '<') {
            throw error("expected '<'");
        }
        advance();
        arrayList.add(parseFormalTypeParameter());
        while (current() != '>') {
            int i2 = this.index;
            arrayList.add(parseFormalTypeParameter());
            progress(i2);
        }
        advance();
        return (FormalTypeParameter[]) arrayList.toArray(new FormalTypeParameter[arrayList.size()]);
    }

    private FormalTypeParameter parseFormalTypeParameter() {
        return FormalTypeParameter.make(parseIdentifier(), parseBounds());
    }

    private String parseIdentifier() {
        mark();
        skipIdentifier();
        return markToCurrent();
    }

    private void skipIdentifier() {
        char cCurrent = current();
        while (true) {
            char c2 = cCurrent;
            if (c2 != ';' && c2 != '.' && c2 != '/' && c2 != '[' && c2 != ':' && c2 != '>' && c2 != '<' && !Character.isWhitespace(c2)) {
                advance();
                cCurrent = current();
            } else {
                return;
            }
        }
    }

    private FieldTypeSignature parseFieldTypeSignature() {
        return parseFieldTypeSignature(true);
    }

    private FieldTypeSignature parseFieldTypeSignature(boolean z2) {
        switch (current()) {
            case 'L':
                return parseClassTypeSignature();
            case 'T':
                return parseTypeVariableSignature();
            case '[':
                if (z2) {
                    return parseArrayTypeSignature();
                }
                throw error("Array signature not allowed here.");
            default:
                throw error("Expected Field Type Signature");
        }
    }

    private ClassTypeSignature parseClassTypeSignature() {
        if (!$assertionsDisabled && current() != 'L') {
            throw new AssertionError();
        }
        if (current() != 'L') {
            throw error("expected a class type");
        }
        advance();
        ArrayList arrayList = new ArrayList(5);
        arrayList.add(parsePackageNameAndSimpleClassTypeSignature());
        parseClassTypeSignatureSuffix(arrayList);
        if (current() != ';') {
            throw error("expected ';' got '" + current() + PdfOps.SINGLE_QUOTE_TOKEN);
        }
        advance();
        return ClassTypeSignature.make(arrayList);
    }

    private SimpleClassTypeSignature parsePackageNameAndSimpleClassTypeSignature() {
        mark();
        skipIdentifier();
        while (current() == '/') {
            advance();
            skipIdentifier();
        }
        String strReplace = markToCurrent().replace('/', '.');
        switch (current()) {
            case ';':
                return SimpleClassTypeSignature.make(strReplace, false, new TypeArgument[0]);
            case '<':
                return SimpleClassTypeSignature.make(strReplace, false, parseTypeArguments());
            default:
                throw error("expected '<' or ';' but got " + current());
        }
    }

    private SimpleClassTypeSignature parseSimpleClassTypeSignature(boolean z2) {
        String identifier = parseIdentifier();
        char cCurrent = current();
        switch (cCurrent) {
            case '.':
            case ';':
                return SimpleClassTypeSignature.make(identifier, z2, new TypeArgument[0]);
            case '<':
                return SimpleClassTypeSignature.make(identifier, z2, parseTypeArguments());
            default:
                throw error("expected '<' or ';' or '.', got '" + cCurrent + "'.");
        }
    }

    private void parseClassTypeSignatureSuffix(List<SimpleClassTypeSignature> list) {
        while (current() == '.') {
            advance();
            list.add(parseSimpleClassTypeSignature(true));
        }
    }

    private TypeArgument[] parseTypeArguments() {
        ArrayList arrayList = new ArrayList(3);
        if (!$assertionsDisabled && current() != '<') {
            throw new AssertionError();
        }
        if (current() != '<') {
            throw error("expected '<'");
        }
        advance();
        arrayList.add(parseTypeArgument());
        while (current() != '>') {
            arrayList.add(parseTypeArgument());
        }
        advance();
        return (TypeArgument[]) arrayList.toArray(new TypeArgument[arrayList.size()]);
    }

    private TypeArgument parseTypeArgument() {
        FieldTypeSignature[] fieldTypeSignatureArr = new FieldTypeSignature[1];
        FieldTypeSignature[] fieldTypeSignatureArr2 = new FieldTypeSignature[1];
        TypeArgument[] typeArgumentArr = new TypeArgument[0];
        switch (current()) {
            case '*':
                advance();
                fieldTypeSignatureArr[0] = SimpleClassTypeSignature.make(Constants.OBJECT_CLASS, false, typeArgumentArr);
                fieldTypeSignatureArr2[0] = BottomSignature.make();
                break;
            case '+':
                advance();
                fieldTypeSignatureArr[0] = parseFieldTypeSignature();
                fieldTypeSignatureArr2[0] = BottomSignature.make();
                break;
            case '-':
                advance();
                fieldTypeSignatureArr2[0] = parseFieldTypeSignature();
                fieldTypeSignatureArr[0] = SimpleClassTypeSignature.make(Constants.OBJECT_CLASS, false, typeArgumentArr);
                break;
        }
        return Wildcard.make(fieldTypeSignatureArr, fieldTypeSignatureArr2);
    }

    private TypeVariableSignature parseTypeVariableSignature() {
        if (!$assertionsDisabled && current() != 'T') {
            throw new AssertionError();
        }
        if (current() != 'T') {
            throw error("expected a type variable usage");
        }
        advance();
        TypeVariableSignature typeVariableSignatureMake = TypeVariableSignature.make(parseIdentifier());
        if (current() != ';') {
            throw error("; expected in signature of type variable named" + typeVariableSignatureMake.getIdentifier());
        }
        advance();
        return typeVariableSignatureMake;
    }

    private ArrayTypeSignature parseArrayTypeSignature() {
        if (current() != '[') {
            throw error("expected array type signature");
        }
        advance();
        return ArrayTypeSignature.make(parseTypeSignature());
    }

    private TypeSignature parseTypeSignature() {
        switch (current()) {
            case 'B':
            case 'C':
            case 'D':
            case 'F':
            case 'I':
            case 'J':
            case 'S':
            case 'Z':
                return parseBaseType();
            case 'E':
            case 'G':
            case 'H':
            case 'K':
            case 'L':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'T':
            case 'U':
            case 'V':
            case 'W':
            case 'X':
            case 'Y':
            default:
                return parseFieldTypeSignature();
        }
    }

    private BaseType parseBaseType() {
        switch (current()) {
            case 'B':
                advance();
                return ByteSignature.make();
            case 'C':
                advance();
                return CharSignature.make();
            case 'D':
                advance();
                return DoubleSignature.make();
            case 'E':
            case 'G':
            case 'H':
            case 'K':
            case 'L':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'T':
            case 'U':
            case 'V':
            case 'W':
            case 'X':
            case 'Y':
            default:
                if ($assertionsDisabled) {
                    throw error("expected primitive type");
                }
                throw new AssertionError();
            case 'F':
                advance();
                return FloatSignature.make();
            case 'I':
                advance();
                return IntSignature.make();
            case 'J':
                advance();
                return LongSignature.make();
            case 'S':
                advance();
                return ShortSignature.make();
            case 'Z':
                advance();
                return BooleanSignature.make();
        }
    }

    private FieldTypeSignature[] parseBounds() {
        ArrayList arrayList = new ArrayList(3);
        if (current() == ':') {
            advance();
            switch (current()) {
                case ':':
                    break;
                default:
                    arrayList.add(parseFieldTypeSignature());
                    break;
            }
            while (current() == ':') {
                advance();
                arrayList.add(parseFieldTypeSignature());
            }
        } else {
            error("Bound expected");
        }
        return (FieldTypeSignature[]) arrayList.toArray(new FieldTypeSignature[arrayList.size()]);
    }

    private ClassTypeSignature[] parseSuperInterfaces() {
        ArrayList arrayList = new ArrayList(5);
        while (current() == 'L') {
            arrayList.add(parseClassTypeSignature());
        }
        return (ClassTypeSignature[]) arrayList.toArray(new ClassTypeSignature[arrayList.size()]);
    }

    private MethodTypeSignature parseMethodTypeSignature() {
        if ($assertionsDisabled || this.index == 0) {
            return MethodTypeSignature.make(parseZeroOrMoreFormalTypeParameters(), parseFormalParameters(), parseReturnType(), parseZeroOrMoreThrowsSignatures());
        }
        throw new AssertionError();
    }

    private TypeSignature[] parseFormalParameters() {
        if (current() != '(') {
            throw error("expected '('");
        }
        advance();
        TypeSignature[] zeroOrMoreTypeSignatures = parseZeroOrMoreTypeSignatures();
        if (current() != ')') {
            throw error("expected ')'");
        }
        advance();
        return zeroOrMoreTypeSignatures;
    }

    private TypeSignature[] parseZeroOrMoreTypeSignatures() {
        ArrayList arrayList = new ArrayList();
        boolean z2 = false;
        while (!z2) {
            switch (current()) {
                case 'B':
                case 'C':
                case 'D':
                case 'F':
                case 'I':
                case 'J':
                case 'L':
                case 'S':
                case 'T':
                case 'Z':
                case '[':
                    arrayList.add(parseTypeSignature());
                    break;
                case 'E':
                case 'G':
                case 'H':
                case 'K':
                case 'M':
                case 'N':
                case 'O':
                case 'P':
                case 'Q':
                case 'R':
                case 'U':
                case 'V':
                case 'W':
                case 'X':
                case 'Y':
                default:
                    z2 = true;
                    break;
            }
        }
        return (TypeSignature[]) arrayList.toArray(new TypeSignature[arrayList.size()]);
    }

    private ReturnType parseReturnType() {
        if (current() == 'V') {
            advance();
            return VoidDescriptor.make();
        }
        return parseTypeSignature();
    }

    private FieldTypeSignature[] parseZeroOrMoreThrowsSignatures() {
        ArrayList arrayList = new ArrayList(3);
        while (current() == '^') {
            arrayList.add(parseThrowsSignature());
        }
        return (FieldTypeSignature[]) arrayList.toArray(new FieldTypeSignature[arrayList.size()]);
    }

    private FieldTypeSignature parseThrowsSignature() {
        if (!$assertionsDisabled && current() != '^') {
            throw new AssertionError();
        }
        if (current() != '^') {
            throw error("expected throws signature");
        }
        advance();
        return parseFieldTypeSignature(false);
    }
}
