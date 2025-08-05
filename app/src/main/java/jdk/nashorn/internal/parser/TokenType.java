package jdk.nashorn.internal.parser;

import com.sun.org.apache.xalan.internal.templates.Constants;
import com.sun.xml.internal.ws.model.RuntimeModeler;
import com.sun.xml.internal.ws.policy.PolicyConstants;
import java.util.Locale;
import javafx.fxml.FXMLLoader;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.apache.commons.math3.geometry.VectorFormat;
import org.slf4j.Marker;
import sun.security.util.SecurityConstants;
import sun.util.locale.LanguageTag;

/* loaded from: nashorn.jar:jdk/nashorn/internal/parser/TokenType.class */
public enum TokenType {
    ERROR(TokenKind.SPECIAL, null),
    EOF(TokenKind.SPECIAL, null),
    EOL(TokenKind.SPECIAL, null),
    COMMENT(TokenKind.SPECIAL, null),
    DIRECTIVE_COMMENT(TokenKind.SPECIAL, null),
    NOT(TokenKind.UNARY, "!", 14, false),
    NE(TokenKind.BINARY, "!=", 9, true),
    NE_STRICT(TokenKind.BINARY, "!==", 9, true),
    MOD(TokenKind.BINARY, FXMLLoader.RESOURCE_KEY_PREFIX, 13, true),
    ASSIGN_MOD(TokenKind.BINARY, "%=", 2, false),
    BIT_AND(TokenKind.BINARY, "&", 8, true),
    AND(TokenKind.BINARY, "&&", 5, true),
    ASSIGN_BIT_AND(TokenKind.BINARY, "&=", 2, false),
    LPAREN(TokenKind.BRACKET, "(", 16, true),
    RPAREN(TokenKind.BRACKET, ")", 0, true),
    MUL(TokenKind.BINARY, "*", 13, true),
    ASSIGN_MUL(TokenKind.BINARY, "*=", 2, false),
    ADD(TokenKind.BINARY, Marker.ANY_NON_NULL_MARKER, 12, true),
    INCPREFIX(TokenKind.UNARY, "++", 15, true),
    ASSIGN_ADD(TokenKind.BINARY, "+=", 2, false),
    COMMARIGHT(TokenKind.BINARY, ",", 1, true),
    SUB(TokenKind.BINARY, LanguageTag.SEP, 12, true),
    DECPREFIX(TokenKind.UNARY, "--", 15, true),
    ASSIGN_SUB(TokenKind.BINARY, "-=", 2, false),
    PERIOD(TokenKind.BRACKET, ".", 17, true),
    DIV(TokenKind.BINARY, "/", 13, true),
    ASSIGN_DIV(TokenKind.BINARY, "/=", 2, false),
    COLON(TokenKind.BINARY, CallSiteDescriptor.TOKEN_DELIMITER),
    SEMICOLON(TokenKind.BINARY, ";"),
    LT(TokenKind.BINARY, "<", 10, true),
    SHL(TokenKind.BINARY, "<<", 11, true),
    ASSIGN_SHL(TokenKind.BINARY, "<<=", 2, false),
    LE(TokenKind.BINARY, "<=", 10, true),
    ASSIGN(TokenKind.BINARY, "=", 2, false),
    EQ(TokenKind.BINARY, "==", 9, true),
    EQ_STRICT(TokenKind.BINARY, "===", 9, true),
    BIND(TokenKind.BINARY, "=>", 9, true),
    GT(TokenKind.BINARY, ">", 10, true),
    GE(TokenKind.BINARY, ">=", 10, true),
    SAR(TokenKind.BINARY, ">>", 11, true),
    ASSIGN_SAR(TokenKind.BINARY, ">>=", 2, false),
    SHR(TokenKind.BINARY, ">>>", 11, true),
    ASSIGN_SHR(TokenKind.BINARY, ">>>=", 2, false),
    TERNARY(TokenKind.BINARY, "?", 3, false),
    LBRACKET(TokenKind.BRACKET, "[", 17, true),
    RBRACKET(TokenKind.BRACKET, "]", 0, true),
    BIT_XOR(TokenKind.BINARY, "^", 7, true),
    ASSIGN_BIT_XOR(TokenKind.BINARY, "^=", 2, false),
    LBRACE(TokenKind.BRACKET, VectorFormat.DEFAULT_PREFIX),
    BIT_OR(TokenKind.BINARY, CallSiteDescriptor.OPERATOR_DELIMITER, 6, true),
    ASSIGN_BIT_OR(TokenKind.BINARY, "|=", 2, false),
    OR(TokenKind.BINARY, "||", 4, true),
    RBRACE(TokenKind.BRACKET, "}"),
    BIT_NOT(TokenKind.UNARY, "~", 14, false),
    BREAK(TokenKind.KEYWORD, "break"),
    CASE(TokenKind.KEYWORD, "case"),
    CATCH(TokenKind.KEYWORD, "catch"),
    CLASS(TokenKind.FUTURE, Constants.ATTRNAME_CLASS),
    CONST(TokenKind.KEYWORD, "const"),
    CONTINUE(TokenKind.KEYWORD, "continue"),
    DEBUGGER(TokenKind.KEYWORD, "debugger"),
    DEFAULT(TokenKind.KEYWORD, "default"),
    DELETE(TokenKind.UNARY, SecurityConstants.FILE_DELETE_ACTION, 14, false),
    DO(TokenKind.KEYWORD, "do"),
    ELSE(TokenKind.KEYWORD, "else"),
    ENUM(TokenKind.FUTURE, "enum"),
    EXPORT(TokenKind.FUTURE, "export"),
    EXTENDS(TokenKind.FUTURE, "extends"),
    FALSE(TokenKind.LITERAL, "false"),
    FINALLY(TokenKind.KEYWORD, "finally"),
    FOR(TokenKind.KEYWORD, "for"),
    FUNCTION(TokenKind.KEYWORD, Constants.EXSLT_ELEMNAME_FUNCTION_STRING),
    IF(TokenKind.KEYWORD, Constants.ELEMNAME_IF_STRING),
    IMPLEMENTS(TokenKind.FUTURESTRICT, "implements"),
    IMPORT(TokenKind.FUTURE, "import"),
    IN(TokenKind.BINARY, "in", 10, true),
    INSTANCEOF(TokenKind.BINARY, "instanceof", 10, true),
    INTERFACE(TokenKind.FUTURESTRICT, "interface"),
    LET(TokenKind.FUTURESTRICT, "let"),
    NEW(TokenKind.UNARY, "new", 17, false),
    NULL(TokenKind.LITERAL, FXMLLoader.NULL_KEYWORD),
    PACKAGE(TokenKind.FUTURESTRICT, "package"),
    PRIVATE(TokenKind.FUTURESTRICT, PolicyConstants.VISIBILITY_VALUE_PRIVATE),
    PROTECTED(TokenKind.FUTURESTRICT, "protected"),
    PUBLIC(TokenKind.FUTURESTRICT, "public"),
    RETURN(TokenKind.KEYWORD, RuntimeModeler.RETURN),
    STATIC(TokenKind.FUTURESTRICT, "static"),
    SUPER(TokenKind.FUTURE, "super"),
    SWITCH(TokenKind.KEYWORD, "switch"),
    THIS(TokenKind.KEYWORD, "this"),
    THROW(TokenKind.KEYWORD, "throw"),
    TRUE(TokenKind.LITERAL, "true"),
    TRY(TokenKind.KEYWORD, "try"),
    TYPEOF(TokenKind.UNARY, "typeof", 14, false),
    VAR(TokenKind.KEYWORD, "var"),
    VOID(TokenKind.UNARY, "void", 14, false),
    WHILE(TokenKind.KEYWORD, "while"),
    WITH(TokenKind.KEYWORD, "with"),
    YIELD(TokenKind.FUTURESTRICT, "yield"),
    DECIMAL(TokenKind.LITERAL, null),
    OCTAL(TokenKind.LITERAL, null),
    HEXADECIMAL(TokenKind.LITERAL, null),
    FLOATING(TokenKind.LITERAL, null),
    STRING(TokenKind.LITERAL, null),
    ESCSTRING(TokenKind.LITERAL, null),
    EXECSTRING(TokenKind.LITERAL, null),
    IDENT(TokenKind.LITERAL, null),
    REGEX(TokenKind.LITERAL, null),
    XML(TokenKind.LITERAL, null),
    OBJECT(TokenKind.LITERAL, null),
    ARRAY(TokenKind.LITERAL, null),
    COMMALEFT(TokenKind.IR, null),
    DECPOSTFIX(TokenKind.IR, null),
    INCPOSTFIX(TokenKind.IR, null);

    private TokenType next;
    private final TokenKind kind;
    private final String name;
    private final int precedence;
    private final boolean isLeftAssociative;
    private static final TokenType[] values;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !TokenType.class.desiredAssertionStatus();
        values = values();
    }

    TokenType(TokenKind kind, String name) {
        this.next = null;
        this.kind = kind;
        this.name = name;
        this.precedence = 0;
        this.isLeftAssociative = false;
    }

    TokenType(TokenKind kind, String name, int precedence, boolean isLeftAssociative) {
        this.next = null;
        this.kind = kind;
        this.name = name;
        this.precedence = precedence;
        this.isLeftAssociative = isLeftAssociative;
    }

    public boolean needsParens(TokenType other, boolean isLeft) {
        return other.precedence != 0 && (this.precedence > other.precedence || (this.precedence == other.precedence && this.isLeftAssociative && !isLeft));
    }

    public boolean isOperator(boolean noIn) {
        return this.kind == TokenKind.BINARY && !((noIn && this == IN) || this.precedence == 0);
    }

    public int getLength() {
        if ($assertionsDisabled || this.name != null) {
            return this.name.length();
        }
        throw new AssertionError((Object) "Token name not set");
    }

    public String getName() {
        return this.name;
    }

    public String getNameOrType() {
        return this.name == null ? super.name().toLowerCase(Locale.ENGLISH) : this.name;
    }

    public TokenType getNext() {
        return this.next;
    }

    public void setNext(TokenType next) {
        this.next = next;
    }

    public TokenKind getKind() {
        return this.kind;
    }

    public int getPrecedence() {
        return this.precedence;
    }

    public boolean isLeftAssociative() {
        return this.isLeftAssociative;
    }

    boolean startsWith(char c2) {
        return this.name != null && this.name.length() > 0 && this.name.charAt(0) == c2;
    }

    static TokenType[] getValues() {
        return values;
    }

    @Override // java.lang.Enum
    public String toString() {
        return getNameOrType();
    }
}
