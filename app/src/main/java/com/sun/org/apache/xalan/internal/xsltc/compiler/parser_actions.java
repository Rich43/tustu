package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.java_cup.internal.runtime.Symbol;
import com.sun.java_cup.internal.runtime.lr_parser;
import com.sun.org.apache.xpath.internal.compiler.Keywords;
import java.util.Stack;
import java.util.Vector;

/* compiled from: XPathParser.java */
/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/parser_actions.class */
class parser_actions {
    private final XPathParser parser;

    parser_actions(XPathParser parser) {
        this.parser = parser;
    }

    public final Symbol parser_do_action(int parser_act_num, lr_parser parser_parser, Stack<Symbol> parser_stack, int parser_top) throws Exception {
        Expression result;
        Expression result2;
        Expression result3;
        Expression result4;
        Expression result5;
        Expression result6;
        Expression result7;
        switch (parser_act_num) {
            case 0:
                SyntaxTreeNode start_val = (SyntaxTreeNode) parser_stack.get(parser_top - 1).value;
                Symbol parser_result = new Symbol(0, parser_stack.get(parser_top - 1).left, parser_stack.get(parser_top - 0).right, start_val);
                parser_parser.done_parsing();
                return parser_result;
            case 1:
                Pattern pattern = (Pattern) parser_stack.get(parser_top - 0).value;
                Symbol parser_result2 = new Symbol(1, parser_stack.get(parser_top - 1).left, parser_stack.get(parser_top - 0).right, pattern);
                return parser_result2;
            case 2:
                Expression expr = (Expression) parser_stack.get(parser_top - 0).value;
                Symbol parser_result3 = new Symbol(1, parser_stack.get(parser_top - 1).left, parser_stack.get(parser_top - 0).right, expr);
                return parser_result3;
            case 3:
                Pattern lpp = (Pattern) parser_stack.get(parser_top - 0).value;
                Symbol parser_result4 = new Symbol(28, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, lpp);
                return parser_result4;
            case 4:
                Pattern lpp2 = (Pattern) parser_stack.get(parser_top - 2).value;
                Pattern p2 = (Pattern) parser_stack.get(parser_top - 0).value;
                Pattern result8 = new AlternativePattern(lpp2, p2);
                Symbol parser_result5 = new Symbol(28, parser_stack.get(parser_top - 2).left, parser_stack.get(parser_top - 0).right, result8);
                return parser_result5;
            case 5:
                Pattern result9 = new AbsolutePathPattern(null);
                Symbol parser_result6 = new Symbol(29, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, result9);
                return parser_result6;
            case 6:
                RelativePathPattern rpp = (RelativePathPattern) parser_stack.get(parser_top - 0).value;
                Pattern result10 = new AbsolutePathPattern(rpp);
                Symbol parser_result7 = new Symbol(29, parser_stack.get(parser_top - 1).left, parser_stack.get(parser_top - 0).right, result10);
                return parser_result7;
            case 7:
                IdKeyPattern ikp = (IdKeyPattern) parser_stack.get(parser_top - 0).value;
                Symbol parser_result8 = new Symbol(29, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, ikp);
                return parser_result8;
            case 8:
                IdKeyPattern ikp2 = (IdKeyPattern) parser_stack.get(parser_top - 2).value;
                RelativePathPattern rpp2 = (RelativePathPattern) parser_stack.get(parser_top - 0).value;
                Pattern result11 = new ParentPattern(ikp2, rpp2);
                Symbol parser_result9 = new Symbol(29, parser_stack.get(parser_top - 2).left, parser_stack.get(parser_top - 0).right, result11);
                return parser_result9;
            case 9:
                IdKeyPattern ikp3 = (IdKeyPattern) parser_stack.get(parser_top - 2).value;
                RelativePathPattern rpp3 = (RelativePathPattern) parser_stack.get(parser_top - 0).value;
                Pattern result12 = new AncestorPattern(ikp3, rpp3);
                Symbol parser_result10 = new Symbol(29, parser_stack.get(parser_top - 2).left, parser_stack.get(parser_top - 0).right, result12);
                return parser_result10;
            case 10:
                RelativePathPattern rpp4 = (RelativePathPattern) parser_stack.get(parser_top - 0).value;
                Pattern result13 = new AncestorPattern(rpp4);
                Symbol parser_result11 = new Symbol(29, parser_stack.get(parser_top - 1).left, parser_stack.get(parser_top - 0).right, result13);
                return parser_result11;
            case 11:
                RelativePathPattern rpp5 = (RelativePathPattern) parser_stack.get(parser_top - 0).value;
                Symbol parser_result12 = new Symbol(29, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, rpp5);
                return parser_result12;
            case 12:
                String l2 = (String) parser_stack.get(parser_top - 1).value;
                IdKeyPattern result14 = new IdPattern(l2);
                this.parser.setHasIdCall(true);
                Symbol parser_result13 = new Symbol(27, parser_stack.get(parser_top - 3).left, parser_stack.get(parser_top - 0).right, result14);
                return parser_result13;
            case 13:
                String l1 = (String) parser_stack.get(parser_top - 3).value;
                String l22 = (String) parser_stack.get(parser_top - 1).value;
                IdKeyPattern result15 = new KeyPattern(l1, l22);
                Symbol parser_result14 = new Symbol(27, parser_stack.get(parser_top - 5).left, parser_stack.get(parser_top - 0).right, result15);
                return parser_result14;
            case 14:
                String l3 = (String) parser_stack.get(parser_top - 1).value;
                StepPattern result16 = new ProcessingInstructionPattern(l3);
                Symbol parser_result15 = new Symbol(30, parser_stack.get(parser_top - 3).left, parser_stack.get(parser_top - 0).right, result16);
                return parser_result15;
            case 15:
                StepPattern sp = (StepPattern) parser_stack.get(parser_top - 0).value;
                Symbol parser_result16 = new Symbol(31, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, sp);
                return parser_result16;
            case 16:
                StepPattern sp2 = (StepPattern) parser_stack.get(parser_top - 2).value;
                RelativePathPattern rpp6 = (RelativePathPattern) parser_stack.get(parser_top - 0).value;
                RelativePathPattern result17 = new ParentPattern(sp2, rpp6);
                Symbol parser_result17 = new Symbol(31, parser_stack.get(parser_top - 2).left, parser_stack.get(parser_top - 0).right, result17);
                return parser_result17;
            case 17:
                StepPattern sp3 = (StepPattern) parser_stack.get(parser_top - 2).value;
                RelativePathPattern rpp7 = (RelativePathPattern) parser_stack.get(parser_top - 0).value;
                RelativePathPattern result18 = new AncestorPattern(sp3, rpp7);
                Symbol parser_result18 = new Symbol(31, parser_stack.get(parser_top - 2).left, parser_stack.get(parser_top - 0).right, result18);
                return parser_result18;
            case 18:
                Object nt = parser_stack.get(parser_top - 0).value;
                StepPattern result19 = this.parser.createStepPattern(3, nt, null);
                Symbol parser_result19 = new Symbol(32, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, result19);
                return parser_result19;
            case 19:
                Object nt2 = parser_stack.get(parser_top - 1).value;
                Vector pp = (Vector) parser_stack.get(parser_top - 0).value;
                StepPattern result20 = this.parser.createStepPattern(3, nt2, pp);
                Symbol parser_result20 = new Symbol(32, parser_stack.get(parser_top - 1).left, parser_stack.get(parser_top - 0).right, result20);
                return parser_result20;
            case 20:
                StepPattern pip = (StepPattern) parser_stack.get(parser_top - 0).value;
                Symbol parser_result21 = new Symbol(32, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, pip);
                return parser_result21;
            case 21:
                StepPattern pip2 = (StepPattern) parser_stack.get(parser_top - 1).value;
                Vector pp2 = (Vector) parser_stack.get(parser_top - 0).value;
                StepPattern result21 = (ProcessingInstructionPattern) pip2.setPredicates(pp2);
                Symbol parser_result22 = new Symbol(32, parser_stack.get(parser_top - 1).left, parser_stack.get(parser_top - 0).right, result21);
                return parser_result22;
            case 22:
                Integer axis = (Integer) parser_stack.get(parser_top - 1).value;
                Object nt3 = parser_stack.get(parser_top - 0).value;
                StepPattern result22 = this.parser.createStepPattern(axis.intValue(), nt3, null);
                Symbol parser_result23 = new Symbol(32, parser_stack.get(parser_top - 1).left, parser_stack.get(parser_top - 0).right, result22);
                return parser_result23;
            case 23:
                Integer axis2 = (Integer) parser_stack.get(parser_top - 2).value;
                Object nt4 = parser_stack.get(parser_top - 1).value;
                Vector pp3 = (Vector) parser_stack.get(parser_top - 0).value;
                StepPattern result23 = this.parser.createStepPattern(axis2.intValue(), nt4, pp3);
                Symbol parser_result24 = new Symbol(32, parser_stack.get(parser_top - 2).left, parser_stack.get(parser_top - 0).right, result23);
                return parser_result24;
            case 24:
                StepPattern pip3 = (StepPattern) parser_stack.get(parser_top - 0).value;
                Symbol parser_result25 = new Symbol(32, parser_stack.get(parser_top - 1).left, parser_stack.get(parser_top - 0).right, pip3);
                return parser_result25;
            case 25:
                StepPattern pip4 = (StepPattern) parser_stack.get(parser_top - 1).value;
                Vector pp4 = (Vector) parser_stack.get(parser_top - 0).value;
                StepPattern result24 = (ProcessingInstructionPattern) pip4.setPredicates(pp4);
                Symbol parser_result26 = new Symbol(32, parser_stack.get(parser_top - 2).left, parser_stack.get(parser_top - 0).right, result24);
                return parser_result26;
            case 26:
                Object nt5 = parser_stack.get(parser_top - 0).value;
                Symbol parser_result27 = new Symbol(33, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, nt5);
                return parser_result27;
            case 27:
                Symbol parser_result28 = new Symbol(33, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, -1);
                return parser_result28;
            case 28:
                Symbol parser_result29 = new Symbol(33, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, 3);
                return parser_result29;
            case 29:
                Symbol parser_result30 = new Symbol(33, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, 8);
                return parser_result30;
            case 30:
                Symbol parser_result31 = new Symbol(33, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, 7);
                return parser_result31;
            case 31:
                Symbol parser_result32 = new Symbol(34, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, null);
                return parser_result32;
            case 32:
                QName qn = (QName) parser_stack.get(parser_top - 0).value;
                Symbol parser_result33 = new Symbol(34, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, qn);
                return parser_result33;
            case 33:
                Symbol parser_result34 = new Symbol(42, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, 2);
                return parser_result34;
            case 34:
                Symbol parser_result35 = new Symbol(42, parser_stack.get(parser_top - 1).left, parser_stack.get(parser_top - 0).right, 3);
                return parser_result35;
            case 35:
                Symbol parser_result36 = new Symbol(42, parser_stack.get(parser_top - 1).left, parser_stack.get(parser_top - 0).right, 2);
                return parser_result36;
            case 36:
                Expression p3 = (Expression) parser_stack.get(parser_top - 0).value;
                Vector temp = new Vector();
                temp.add(p3);
                Symbol parser_result37 = new Symbol(35, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, temp);
                return parser_result37;
            case 37:
                Expression p4 = (Expression) parser_stack.get(parser_top - 1).value;
                Vector pp5 = (Vector) parser_stack.get(parser_top - 0).value;
                pp5.add(0, p4);
                Symbol parser_result38 = new Symbol(35, parser_stack.get(parser_top - 1).left, parser_stack.get(parser_top - 0).right, pp5);
                return parser_result38;
            case 38:
                Expression e2 = (Expression) parser_stack.get(parser_top - 1).value;
                Expression result25 = new Predicate(e2);
                Symbol parser_result39 = new Symbol(5, parser_stack.get(parser_top - 2).left, parser_stack.get(parser_top - 0).right, result25);
                return parser_result39;
            case 39:
                Expression ex = (Expression) parser_stack.get(parser_top - 0).value;
                Symbol parser_result40 = new Symbol(2, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, ex);
                return parser_result40;
            case 40:
                Expression ae2 = (Expression) parser_stack.get(parser_top - 0).value;
                Symbol parser_result41 = new Symbol(8, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, ae2);
                return parser_result41;
            case 41:
                Expression oe = (Expression) parser_stack.get(parser_top - 2).value;
                Expression ae3 = (Expression) parser_stack.get(parser_top - 0).value;
                Expression result26 = new LogicalExpr(0, oe, ae3);
                Symbol parser_result42 = new Symbol(8, parser_stack.get(parser_top - 2).left, parser_stack.get(parser_top - 0).right, result26);
                return parser_result42;
            case 42:
                Expression e3 = (Expression) parser_stack.get(parser_top - 0).value;
                Symbol parser_result43 = new Symbol(9, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, e3);
                return parser_result43;
            case 43:
                Expression ae4 = (Expression) parser_stack.get(parser_top - 2).value;
                Expression ee = (Expression) parser_stack.get(parser_top - 0).value;
                Expression result27 = new LogicalExpr(1, ae4, ee);
                Symbol parser_result44 = new Symbol(9, parser_stack.get(parser_top - 2).left, parser_stack.get(parser_top - 0).right, result27);
                return parser_result44;
            case 44:
                Expression re = (Expression) parser_stack.get(parser_top - 0).value;
                Symbol parser_result45 = new Symbol(10, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, re);
                return parser_result45;
            case 45:
                Expression ee2 = (Expression) parser_stack.get(parser_top - 2).value;
                Expression re2 = (Expression) parser_stack.get(parser_top - 0).value;
                Expression result28 = new EqualityExpr(0, ee2, re2);
                Symbol parser_result46 = new Symbol(10, parser_stack.get(parser_top - 2).left, parser_stack.get(parser_top - 0).right, result28);
                return parser_result46;
            case 46:
                Expression ee3 = (Expression) parser_stack.get(parser_top - 2).value;
                Expression re3 = (Expression) parser_stack.get(parser_top - 0).value;
                Expression result29 = new EqualityExpr(1, ee3, re3);
                Symbol parser_result47 = new Symbol(10, parser_stack.get(parser_top - 2).left, parser_stack.get(parser_top - 0).right, result29);
                return parser_result47;
            case 47:
                Expression ae5 = (Expression) parser_stack.get(parser_top - 0).value;
                Symbol parser_result48 = new Symbol(11, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, ae5);
                return parser_result48;
            case 48:
                Expression re4 = (Expression) parser_stack.get(parser_top - 2).value;
                Expression ae6 = (Expression) parser_stack.get(parser_top - 0).value;
                Expression result30 = new RelationalExpr(3, re4, ae6);
                Symbol parser_result49 = new Symbol(11, parser_stack.get(parser_top - 2).left, parser_stack.get(parser_top - 0).right, result30);
                return parser_result49;
            case 49:
                Expression re5 = (Expression) parser_stack.get(parser_top - 2).value;
                Expression ae7 = (Expression) parser_stack.get(parser_top - 0).value;
                Expression result31 = new RelationalExpr(2, re5, ae7);
                Symbol parser_result50 = new Symbol(11, parser_stack.get(parser_top - 2).left, parser_stack.get(parser_top - 0).right, result31);
                return parser_result50;
            case 50:
                Expression re6 = (Expression) parser_stack.get(parser_top - 2).value;
                Expression ae8 = (Expression) parser_stack.get(parser_top - 0).value;
                Expression result32 = new RelationalExpr(5, re6, ae8);
                Symbol parser_result51 = new Symbol(11, parser_stack.get(parser_top - 2).left, parser_stack.get(parser_top - 0).right, result32);
                return parser_result51;
            case 51:
                Expression re7 = (Expression) parser_stack.get(parser_top - 2).value;
                Expression ae9 = (Expression) parser_stack.get(parser_top - 0).value;
                Expression result33 = new RelationalExpr(4, re7, ae9);
                Symbol parser_result52 = new Symbol(11, parser_stack.get(parser_top - 2).left, parser_stack.get(parser_top - 0).right, result33);
                return parser_result52;
            case 52:
                Expression me = (Expression) parser_stack.get(parser_top - 0).value;
                Symbol parser_result53 = new Symbol(12, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, me);
                return parser_result53;
            case 53:
                Expression ae10 = (Expression) parser_stack.get(parser_top - 2).value;
                Expression me2 = (Expression) parser_stack.get(parser_top - 0).value;
                Expression result34 = new BinOpExpr(0, ae10, me2);
                Symbol parser_result54 = new Symbol(12, parser_stack.get(parser_top - 2).left, parser_stack.get(parser_top - 0).right, result34);
                return parser_result54;
            case 54:
                Expression ae11 = (Expression) parser_stack.get(parser_top - 2).value;
                Expression me3 = (Expression) parser_stack.get(parser_top - 0).value;
                Expression result35 = new BinOpExpr(1, ae11, me3);
                Symbol parser_result55 = new Symbol(12, parser_stack.get(parser_top - 2).left, parser_stack.get(parser_top - 0).right, result35);
                return parser_result55;
            case 55:
                Expression ue = (Expression) parser_stack.get(parser_top - 0).value;
                Symbol parser_result56 = new Symbol(13, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, ue);
                return parser_result56;
            case 56:
                Expression me4 = (Expression) parser_stack.get(parser_top - 2).value;
                Expression ue2 = (Expression) parser_stack.get(parser_top - 0).value;
                Expression result36 = new BinOpExpr(2, me4, ue2);
                Symbol parser_result57 = new Symbol(13, parser_stack.get(parser_top - 2).left, parser_stack.get(parser_top - 0).right, result36);
                return parser_result57;
            case 57:
                Expression me5 = (Expression) parser_stack.get(parser_top - 2).value;
                Expression ue3 = (Expression) parser_stack.get(parser_top - 0).value;
                Expression result37 = new BinOpExpr(3, me5, ue3);
                Symbol parser_result58 = new Symbol(13, parser_stack.get(parser_top - 2).left, parser_stack.get(parser_top - 0).right, result37);
                return parser_result58;
            case 58:
                Expression me6 = (Expression) parser_stack.get(parser_top - 2).value;
                Expression ue4 = (Expression) parser_stack.get(parser_top - 0).value;
                Expression result38 = new BinOpExpr(4, me6, ue4);
                Symbol parser_result59 = new Symbol(13, parser_stack.get(parser_top - 2).left, parser_stack.get(parser_top - 0).right, result38);
                return parser_result59;
            case 59:
                Expression ue5 = (Expression) parser_stack.get(parser_top - 0).value;
                Symbol parser_result60 = new Symbol(14, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, ue5);
                return parser_result60;
            case 60:
                Expression ue6 = (Expression) parser_stack.get(parser_top - 0).value;
                Expression result39 = new UnaryOpExpr(ue6);
                Symbol parser_result61 = new Symbol(14, parser_stack.get(parser_top - 1).left, parser_stack.get(parser_top - 0).right, result39);
                return parser_result61;
            case 61:
                Expression pe = (Expression) parser_stack.get(parser_top - 0).value;
                Symbol parser_result62 = new Symbol(18, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, pe);
                return parser_result62;
            case 62:
                Expression pe2 = (Expression) parser_stack.get(parser_top - 2).value;
                Expression rest = (Expression) parser_stack.get(parser_top - 0).value;
                Expression result40 = new UnionPathExpr(pe2, rest);
                Symbol parser_result63 = new Symbol(18, parser_stack.get(parser_top - 2).left, parser_stack.get(parser_top - 0).right, result40);
                return parser_result63;
            case 63:
                Expression lp = (Expression) parser_stack.get(parser_top - 0).value;
                Symbol parser_result64 = new Symbol(19, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, lp);
                return parser_result64;
            case 64:
                Symbol parser_result65 = new Symbol(19, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, (Expression) parser_stack.get(parser_top - 0).value);
                return parser_result65;
            case 65:
                Expression result41 = new FilterParentPath((Expression) parser_stack.get(parser_top - 2).value, (Expression) parser_stack.get(parser_top - 0).value);
                Symbol parser_result66 = new Symbol(19, parser_stack.get(parser_top - 2).left, parser_stack.get(parser_top - 0).right, result41);
                return parser_result66;
            case 66:
                Expression fexp = (Expression) parser_stack.get(parser_top - 2).value;
                Expression rlp = (Expression) parser_stack.get(parser_top - 0).value;
                int nodeType = -1;
                if ((rlp instanceof Step) && this.parser.isElementAxis(((Step) rlp).getAxis())) {
                    nodeType = 1;
                }
                Step step = new Step(5, nodeType, null);
                FilterParentPath fpp = new FilterParentPath(new FilterParentPath(fexp, step), rlp);
                if (!(fexp instanceof KeyCall)) {
                    fpp.setDescendantAxis();
                }
                Symbol parser_result67 = new Symbol(19, parser_stack.get(parser_top - 2).left, parser_stack.get(parser_top - 0).right, fpp);
                return parser_result67;
            case 67:
                Symbol parser_result68 = new Symbol(4, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, (Expression) parser_stack.get(parser_top - 0).value);
                return parser_result68;
            case 68:
                Expression alp = (Expression) parser_stack.get(parser_top - 0).value;
                Symbol parser_result69 = new Symbol(4, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, alp);
                return parser_result69;
            case 69:
                Expression step2 = (Expression) parser_stack.get(parser_top - 0).value;
                Symbol parser_result70 = new Symbol(21, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, step2);
                return parser_result70;
            case 70:
                Expression rlp2 = (Expression) parser_stack.get(parser_top - 2).value;
                Expression step3 = (Expression) parser_stack.get(parser_top - 0).value;
                if ((rlp2 instanceof Step) && ((Step) rlp2).isAbbreviatedDot()) {
                    result7 = step3;
                } else if (((Step) step3).isAbbreviatedDot()) {
                    result7 = rlp2;
                } else {
                    result7 = new ParentLocationPath((RelativeLocationPath) rlp2, step3);
                }
                Symbol parser_result71 = new Symbol(21, parser_stack.get(parser_top - 2).left, parser_stack.get(parser_top - 0).right, result7);
                return parser_result71;
            case 71:
                Expression arlp = (Expression) parser_stack.get(parser_top - 0).value;
                Symbol parser_result72 = new Symbol(21, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, arlp);
                return parser_result72;
            case 72:
                Expression result42 = new AbsoluteLocationPath();
                Symbol parser_result73 = new Symbol(23, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, result42);
                return parser_result73;
            case 73:
                Expression result43 = new AbsoluteLocationPath((Expression) parser_stack.get(parser_top - 0).value);
                Symbol parser_result74 = new Symbol(23, parser_stack.get(parser_top - 1).left, parser_stack.get(parser_top - 0).right, result43);
                return parser_result74;
            case 74:
                Expression aalp = (Expression) parser_stack.get(parser_top - 0).value;
                Symbol parser_result75 = new Symbol(23, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, aalp);
                return parser_result75;
            case 75:
                Expression rlp3 = (Expression) parser_stack.get(parser_top - 2).value;
                Expression step4 = (Expression) parser_stack.get(parser_top - 0).value;
                Step right = (Step) step4;
                int axis3 = right.getAxis();
                int type = right.getNodeType();
                Vector predicates = right.getPredicates();
                if (axis3 == 3 && type != 2) {
                    if (predicates == null) {
                        right.setAxis(4);
                        if ((rlp3 instanceof Step) && ((Step) rlp3).isAbbreviatedDot()) {
                            result6 = right;
                        } else {
                            RelativeLocationPath left = (RelativeLocationPath) rlp3;
                            result6 = new ParentLocationPath(left, right);
                        }
                    } else if ((rlp3 instanceof Step) && ((Step) rlp3).isAbbreviatedDot()) {
                        Step left2 = new Step(5, 1, null);
                        result6 = new ParentLocationPath(left2, right);
                    } else {
                        RelativeLocationPath left3 = (RelativeLocationPath) rlp3;
                        Step mid = new Step(5, 1, null);
                        ParentLocationPath ppl = new ParentLocationPath(mid, right);
                        result6 = new ParentLocationPath(left3, ppl);
                    }
                } else if (axis3 == 2 || type == 2) {
                    RelativeLocationPath left4 = (RelativeLocationPath) rlp3;
                    Step middle = new Step(5, 1, null);
                    ParentLocationPath ppl2 = new ParentLocationPath(middle, right);
                    result6 = new ParentLocationPath(left4, ppl2);
                } else {
                    RelativeLocationPath left5 = (RelativeLocationPath) rlp3;
                    Step middle2 = new Step(5, -1, null);
                    ParentLocationPath ppl3 = new ParentLocationPath(middle2, right);
                    result6 = new ParentLocationPath(left5, ppl3);
                }
                Symbol parser_result76 = new Symbol(22, parser_stack.get(parser_top - 2).left, parser_stack.get(parser_top - 0).right, result6);
                return parser_result76;
            case 76:
                Expression rlp4 = (Expression) parser_stack.get(parser_top - 0).value;
                int nodeType2 = -1;
                if ((rlp4 instanceof Step) && this.parser.isElementAxis(((Step) rlp4).getAxis())) {
                    nodeType2 = 1;
                }
                Step step5 = new Step(5, nodeType2, null);
                Expression result44 = new AbsoluteLocationPath(this.parser.insertStep(step5, (RelativeLocationPath) rlp4));
                Symbol parser_result77 = new Symbol(24, parser_stack.get(parser_top - 1).left, parser_stack.get(parser_top - 0).right, result44);
                return parser_result77;
            case 77:
                Object ntest = parser_stack.get(parser_top - 0).value;
                if (ntest instanceof Step) {
                    result5 = (Step) ntest;
                } else {
                    result5 = new Step(3, this.parser.findNodeType(3, ntest), null);
                }
                Symbol parser_result78 = new Symbol(7, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, result5);
                return parser_result78;
            case 78:
                Object ntest2 = parser_stack.get(parser_top - 1).value;
                Vector pp6 = (Vector) parser_stack.get(parser_top - 0).value;
                if (ntest2 instanceof Step) {
                    Step step6 = (Step) ntest2;
                    step6.addPredicates(pp6);
                    result4 = (Step) ntest2;
                } else {
                    result4 = new Step(3, this.parser.findNodeType(3, ntest2), pp6);
                }
                Symbol parser_result79 = new Symbol(7, parser_stack.get(parser_top - 1).left, parser_stack.get(parser_top - 0).right, result4);
                return parser_result79;
            case 79:
                Integer axis4 = (Integer) parser_stack.get(parser_top - 2).value;
                Object ntest3 = parser_stack.get(parser_top - 1).value;
                Vector pp7 = (Vector) parser_stack.get(parser_top - 0).value;
                Expression result45 = new Step(axis4.intValue(), this.parser.findNodeType(axis4.intValue(), ntest3), pp7);
                Symbol parser_result80 = new Symbol(7, parser_stack.get(parser_top - 2).left, parser_stack.get(parser_top - 0).right, result45);
                return parser_result80;
            case 80:
                Integer axis5 = (Integer) parser_stack.get(parser_top - 1).value;
                Object ntest4 = parser_stack.get(parser_top - 0).value;
                Expression result46 = new Step(axis5.intValue(), this.parser.findNodeType(axis5.intValue(), ntest4), null);
                Symbol parser_result81 = new Symbol(7, parser_stack.get(parser_top - 1).left, parser_stack.get(parser_top - 0).right, result46);
                return parser_result81;
            case 81:
                Expression abbrev = (Expression) parser_stack.get(parser_top - 0).value;
                Symbol parser_result82 = new Symbol(7, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, abbrev);
                return parser_result82;
            case 82:
                Integer an2 = (Integer) parser_stack.get(parser_top - 1).value;
                Symbol parser_result83 = new Symbol(41, parser_stack.get(parser_top - 1).left, parser_stack.get(parser_top - 0).right, an2);
                return parser_result83;
            case 83:
                Symbol parser_result84 = new Symbol(41, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, 2);
                return parser_result84;
            case 84:
                Symbol parser_result85 = new Symbol(40, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, 0);
                return parser_result85;
            case 85:
                Symbol parser_result86 = new Symbol(40, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, 1);
                return parser_result86;
            case 86:
                Symbol parser_result87 = new Symbol(40, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, 2);
                return parser_result87;
            case 87:
                Symbol parser_result88 = new Symbol(40, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, 3);
                return parser_result88;
            case 88:
                Symbol parser_result89 = new Symbol(40, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, 4);
                return parser_result89;
            case 89:
                Symbol parser_result90 = new Symbol(40, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, 5);
                return parser_result90;
            case 90:
                Symbol parser_result91 = new Symbol(40, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, 6);
                return parser_result91;
            case 91:
                Symbol parser_result92 = new Symbol(40, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, 7);
                return parser_result92;
            case 92:
                Symbol parser_result93 = new Symbol(40, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, 9);
                return parser_result93;
            case 93:
                Symbol parser_result94 = new Symbol(40, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, 10);
                return parser_result94;
            case 94:
                Symbol parser_result95 = new Symbol(40, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, 11);
                return parser_result95;
            case 95:
                Symbol parser_result96 = new Symbol(40, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, 12);
                return parser_result96;
            case 96:
                Symbol parser_result97 = new Symbol(40, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, 13);
                return parser_result97;
            case 97:
                Expression result47 = new Step(13, -1, null);
                Symbol parser_result98 = new Symbol(20, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, result47);
                return parser_result98;
            case 98:
                Expression result48 = new Step(10, -1, null);
                Symbol parser_result99 = new Symbol(20, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, result48);
                return parser_result99;
            case 99:
                Expression primary = (Expression) parser_stack.get(parser_top - 0).value;
                Symbol parser_result100 = new Symbol(6, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, primary);
                return parser_result100;
            case 100:
                Expression primary2 = (Expression) parser_stack.get(parser_top - 1).value;
                Vector pp8 = (Vector) parser_stack.get(parser_top - 0).value;
                Expression result49 = new FilterExpr(primary2, pp8);
                Symbol parser_result101 = new Symbol(6, parser_stack.get(parser_top - 1).left, parser_stack.get(parser_top - 0).right, result49);
                return parser_result101;
            case 101:
                Expression vr = (Expression) parser_stack.get(parser_top - 0).value;
                Symbol parser_result102 = new Symbol(17, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, vr);
                return parser_result102;
            case 102:
                Expression ex2 = (Expression) parser_stack.get(parser_top - 1).value;
                Symbol parser_result103 = new Symbol(17, parser_stack.get(parser_top - 2).left, parser_stack.get(parser_top - 0).right, ex2);
                return parser_result103;
            case 103:
                String string = (String) parser_stack.get(parser_top - 0).value;
                String namespace = null;
                int index = string.lastIndexOf(58);
                if (index > 0) {
                    String prefix = string.substring(0, index);
                    namespace = this.parser._symbolTable.lookupNamespace(prefix);
                }
                Expression result50 = namespace == null ? new LiteralExpr(string) : new LiteralExpr(string, namespace);
                Symbol parser_result104 = new Symbol(17, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, result50);
                return parser_result104;
            case 104:
                Long num = (Long) parser_stack.get(parser_top - 0).value;
                if (num.longValue() < -2147483648L || num.longValue() > 2147483647L) {
                    result3 = new RealExpr(num.longValue());
                } else if (num.doubleValue() == 0.0d) {
                    result3 = new RealExpr(num.doubleValue());
                } else if (num.intValue() != 0 && num.doubleValue() == 0.0d) {
                    result3 = new RealExpr(num.doubleValue());
                } else {
                    result3 = new IntExpr(num.intValue());
                }
                Symbol parser_result105 = new Symbol(17, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, result3);
                return parser_result105;
            case 105:
                Expression result51 = new RealExpr(((Double) parser_stack.get(parser_top - 0).value).doubleValue());
                Symbol parser_result106 = new Symbol(17, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, result51);
                return parser_result106;
            case 106:
                Expression fc = (Expression) parser_stack.get(parser_top - 0).value;
                Symbol parser_result107 = new Symbol(17, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, fc);
                return parser_result107;
            case 107:
                Expression result52 = null;
                QName varName = (QName) parser_stack.get(parser_top - 0).value;
                SyntaxTreeNode node = this.parser.lookupName(varName);
                if (node != null) {
                    if (node instanceof Variable) {
                        result52 = new VariableRef((Variable) node);
                    } else if (node instanceof Param) {
                        result52 = new ParameterRef((Param) node);
                    } else {
                        result52 = new UnresolvedRef(varName);
                    }
                }
                if (node == null) {
                    result52 = new UnresolvedRef(varName);
                }
                Symbol parser_result108 = new Symbol(15, parser_stack.get(parser_top - 1).left, parser_stack.get(parser_top - 0).right, result52);
                return parser_result108;
            case 108:
                QName fname = (QName) parser_stack.get(parser_top - 2).value;
                if (fname == this.parser.getQNameIgnoreDefaultNs(Keywords.FUNC_CURRENT_STRING)) {
                    result2 = new CurrentCall(fname);
                } else if (fname == this.parser.getQNameIgnoreDefaultNs("number")) {
                    result2 = new NumberCall(fname, XPathParser.EmptyArgs);
                } else if (fname == this.parser.getQNameIgnoreDefaultNs("string")) {
                    result2 = new StringCall(fname, XPathParser.EmptyArgs);
                } else if (fname == this.parser.getQNameIgnoreDefaultNs(Keywords.FUNC_CONCAT_STRING)) {
                    result2 = new ConcatCall(fname, XPathParser.EmptyArgs);
                } else if (fname == this.parser.getQNameIgnoreDefaultNs("true")) {
                    result2 = new BooleanExpr(true);
                } else if (fname == this.parser.getQNameIgnoreDefaultNs("false")) {
                    result2 = new BooleanExpr(false);
                } else if (fname == this.parser.getQNameIgnoreDefaultNs("name")) {
                    result2 = new NameCall(fname);
                } else if (fname == this.parser.getQNameIgnoreDefaultNs(Keywords.FUNC_GENERATE_ID_STRING)) {
                    result2 = new GenerateIdCall(fname, XPathParser.EmptyArgs);
                } else if (fname == this.parser.getQNameIgnoreDefaultNs(Keywords.FUNC_STRING_LENGTH_STRING)) {
                    result2 = new StringLengthCall(fname, XPathParser.EmptyArgs);
                } else if (fname == this.parser.getQNameIgnoreDefaultNs(Keywords.FUNC_POSITION_STRING)) {
                    result2 = new PositionCall(fname);
                } else if (fname == this.parser.getQNameIgnoreDefaultNs(Keywords.FUNC_LAST_STRING)) {
                    result2 = new LastCall(fname);
                } else if (fname == this.parser.getQNameIgnoreDefaultNs(Keywords.FUNC_LOCAL_PART_STRING)) {
                    result2 = new LocalNameCall(fname);
                } else if (fname == this.parser.getQNameIgnoreDefaultNs(Keywords.FUNC_NAMESPACE_STRING)) {
                    result2 = new NamespaceUriCall(fname);
                } else {
                    result2 = new FunctionCall(fname, XPathParser.EmptyArgs);
                }
                Symbol parser_result109 = new Symbol(16, parser_stack.get(parser_top - 2).left, parser_stack.get(parser_top - 0).right, result2);
                return parser_result109;
            case 109:
                QName fname2 = (QName) parser_stack.get(parser_top - 3).value;
                Vector argl = (Vector) parser_stack.get(parser_top - 1).value;
                if (fname2 == this.parser.getQNameIgnoreDefaultNs(Keywords.FUNC_CONCAT_STRING)) {
                    result = new ConcatCall(fname2, argl);
                } else if (fname2 == this.parser.getQNameIgnoreDefaultNs("number")) {
                    result = new NumberCall(fname2, argl);
                } else if (fname2 == this.parser.getQNameIgnoreDefaultNs(Constants.DOCUMENT_PNAME)) {
                    this.parser.setMultiDocument(true);
                    result = new DocumentCall(fname2, argl);
                } else if (fname2 == this.parser.getQNameIgnoreDefaultNs("string")) {
                    result = new StringCall(fname2, argl);
                } else if (fname2 == this.parser.getQNameIgnoreDefaultNs("boolean")) {
                    result = new BooleanCall(fname2, argl);
                } else if (fname2 == this.parser.getQNameIgnoreDefaultNs("name")) {
                    result = new NameCall(fname2, argl);
                } else if (fname2 == this.parser.getQNameIgnoreDefaultNs(Keywords.FUNC_GENERATE_ID_STRING)) {
                    result = new GenerateIdCall(fname2, argl);
                } else if (fname2 == this.parser.getQNameIgnoreDefaultNs(Keywords.FUNC_NOT_STRING)) {
                    result = new NotCall(fname2, argl);
                } else if (fname2 == this.parser.getQNameIgnoreDefaultNs("format-number")) {
                    result = new FormatNumberCall(fname2, argl);
                } else if (fname2 == this.parser.getQNameIgnoreDefaultNs(Keywords.FUNC_UNPARSED_ENTITY_URI_STRING)) {
                    result = new UnparsedEntityUriCall(fname2, argl);
                } else if (fname2 == this.parser.getQNameIgnoreDefaultNs("key")) {
                    result = new KeyCall(fname2, argl);
                } else if (fname2 == this.parser.getQNameIgnoreDefaultNs("id")) {
                    result = new KeyCall(fname2, argl);
                    this.parser.setHasIdCall(true);
                } else if (fname2 == this.parser.getQNameIgnoreDefaultNs(Keywords.FUNC_CEILING_STRING)) {
                    result = new CeilingCall(fname2, argl);
                } else if (fname2 == this.parser.getQNameIgnoreDefaultNs(Keywords.FUNC_ROUND_STRING)) {
                    result = new RoundCall(fname2, argl);
                } else if (fname2 == this.parser.getQNameIgnoreDefaultNs(Keywords.FUNC_FLOOR_STRING)) {
                    result = new FloorCall(fname2, argl);
                } else if (fname2 == this.parser.getQNameIgnoreDefaultNs(Keywords.FUNC_CONTAINS_STRING)) {
                    result = new ContainsCall(fname2, argl);
                } else if (fname2 == this.parser.getQNameIgnoreDefaultNs(Keywords.FUNC_STRING_LENGTH_STRING)) {
                    result = new StringLengthCall(fname2, argl);
                } else if (fname2 == this.parser.getQNameIgnoreDefaultNs(Keywords.FUNC_STARTS_WITH_STRING)) {
                    result = new StartsWithCall(fname2, argl);
                } else if (fname2 == this.parser.getQNameIgnoreDefaultNs(Keywords.FUNC_EXT_FUNCTION_AVAILABLE_STRING)) {
                    result = new FunctionAvailableCall(fname2, argl);
                } else if (fname2 == this.parser.getQNameIgnoreDefaultNs(Keywords.FUNC_EXT_ELEM_AVAILABLE_STRING)) {
                    result = new ElementAvailableCall(fname2, argl);
                } else if (fname2 == this.parser.getQNameIgnoreDefaultNs(Keywords.FUNC_LOCAL_PART_STRING)) {
                    result = new LocalNameCall(fname2, argl);
                } else if (fname2 == this.parser.getQNameIgnoreDefaultNs("lang")) {
                    result = new LangCall(fname2, argl);
                } else if (fname2 == this.parser.getQNameIgnoreDefaultNs(Keywords.FUNC_NAMESPACE_STRING)) {
                    result = new NamespaceUriCall(fname2, argl);
                } else if (fname2 == this.parser.getQName(Constants.TRANSLET_URI, "xsltc", "cast")) {
                    result = new CastCall(fname2, argl);
                } else if (fname2.getLocalPart().equals("nodeset") || fname2.getLocalPart().equals("node-set")) {
                    this.parser.setCallsNodeset(true);
                    result = new FunctionCall(fname2, argl);
                } else {
                    result = new FunctionCall(fname2, argl);
                }
                Symbol parser_result110 = new Symbol(16, parser_stack.get(parser_top - 3).left, parser_stack.get(parser_top - 0).right, result);
                return parser_result110;
            case 110:
                Expression arg = (Expression) parser_stack.get(parser_top - 0).value;
                Vector temp2 = new Vector();
                temp2.add(arg);
                Symbol parser_result111 = new Symbol(36, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, temp2);
                return parser_result111;
            case 111:
                Expression arg2 = (Expression) parser_stack.get(parser_top - 2).value;
                Vector argl2 = (Vector) parser_stack.get(parser_top - 0).value;
                argl2.add(0, arg2);
                Symbol parser_result112 = new Symbol(36, parser_stack.get(parser_top - 2).left, parser_stack.get(parser_top - 0).right, argl2);
                return parser_result112;
            case 112:
                Symbol parser_result113 = new Symbol(38, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, (QName) parser_stack.get(parser_top - 0).value);
                return parser_result113;
            case 113:
                QName vname = (QName) parser_stack.get(parser_top - 0).value;
                Symbol parser_result114 = new Symbol(39, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, vname);
                return parser_result114;
            case 114:
                Expression ex3 = (Expression) parser_stack.get(parser_top - 0).value;
                Symbol parser_result115 = new Symbol(3, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, ex3);
                return parser_result115;
            case 115:
                Object nt6 = parser_stack.get(parser_top - 0).value;
                Symbol parser_result116 = new Symbol(25, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, nt6);
                return parser_result116;
            case 116:
                Symbol parser_result117 = new Symbol(25, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, -1);
                return parser_result117;
            case 117:
                Symbol parser_result118 = new Symbol(25, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, 3);
                return parser_result118;
            case 118:
                Symbol parser_result119 = new Symbol(25, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, 8);
                return parser_result119;
            case 119:
                String l4 = (String) parser_stack.get(parser_top - 1).value;
                QName name = this.parser.getQNameIgnoreDefaultNs("name");
                Expression exp = new EqualityExpr(0, new NameCall(name), new LiteralExpr(l4));
                Vector predicates2 = new Vector();
                predicates2.add(new Predicate(exp));
                Object result53 = new Step(3, 7, predicates2);
                Symbol parser_result120 = new Symbol(25, parser_stack.get(parser_top - 3).left, parser_stack.get(parser_top - 0).right, result53);
                return parser_result120;
            case 120:
                Symbol parser_result121 = new Symbol(25, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, 7);
                return parser_result121;
            case 121:
                Symbol parser_result122 = new Symbol(26, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, null);
                return parser_result122;
            case 122:
                QName qn2 = (QName) parser_stack.get(parser_top - 0).value;
                Symbol parser_result123 = new Symbol(26, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, qn2);
                return parser_result123;
            case 123:
                String qname = (String) parser_stack.get(parser_top - 0).value;
                QName result54 = this.parser.getQNameIgnoreDefaultNs(qname);
                Symbol parser_result124 = new Symbol(37, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, result54);
                return parser_result124;
            case 124:
                QName result55 = this.parser.getQNameIgnoreDefaultNs("div");
                Symbol parser_result125 = new Symbol(37, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, result55);
                return parser_result125;
            case 125:
                QName result56 = this.parser.getQNameIgnoreDefaultNs("mod");
                Symbol parser_result126 = new Symbol(37, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, result56);
                return parser_result126;
            case 126:
                QName result57 = this.parser.getQNameIgnoreDefaultNs("key");
                Symbol parser_result127 = new Symbol(37, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, result57);
                return parser_result127;
            case 127:
                QName result58 = this.parser.getQNameIgnoreDefaultNs("child");
                Symbol parser_result128 = new Symbol(37, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, result58);
                return parser_result128;
            case 128:
                QName result59 = this.parser.getQNameIgnoreDefaultNs("ancestor-or-self");
                Symbol parser_result129 = new Symbol(37, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, result59);
                return parser_result129;
            case 129:
                QName result60 = this.parser.getQNameIgnoreDefaultNs("attribute");
                Symbol parser_result130 = new Symbol(37, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, result60);
                return parser_result130;
            case 130:
                QName result61 = this.parser.getQNameIgnoreDefaultNs("child");
                Symbol parser_result131 = new Symbol(37, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, result61);
                return parser_result131;
            case 131:
                QName result62 = this.parser.getQNameIgnoreDefaultNs("decendant");
                Symbol parser_result132 = new Symbol(37, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, result62);
                return parser_result132;
            case 132:
                QName result63 = this.parser.getQNameIgnoreDefaultNs("decendant-or-self");
                Symbol parser_result133 = new Symbol(37, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, result63);
                return parser_result133;
            case 133:
                QName result64 = this.parser.getQNameIgnoreDefaultNs("following");
                Symbol parser_result134 = new Symbol(37, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, result64);
                return parser_result134;
            case 134:
                QName result65 = this.parser.getQNameIgnoreDefaultNs("following-sibling");
                Symbol parser_result135 = new Symbol(37, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, result65);
                return parser_result135;
            case 135:
                QName result66 = this.parser.getQNameIgnoreDefaultNs(com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_NAMESPACE);
                Symbol parser_result136 = new Symbol(37, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, result66);
                return parser_result136;
            case 136:
                QName result67 = this.parser.getQNameIgnoreDefaultNs("parent");
                Symbol parser_result137 = new Symbol(37, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, result67);
                return parser_result137;
            case 137:
                QName result68 = this.parser.getQNameIgnoreDefaultNs("preceding");
                Symbol parser_result138 = new Symbol(37, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, result68);
                return parser_result138;
            case 138:
                QName result69 = this.parser.getQNameIgnoreDefaultNs("preceding-sibling");
                Symbol parser_result139 = new Symbol(37, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, result69);
                return parser_result139;
            case 139:
                QName result70 = this.parser.getQNameIgnoreDefaultNs("self");
                Symbol parser_result140 = new Symbol(37, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, result70);
                return parser_result140;
            case 140:
                QName result71 = this.parser.getQNameIgnoreDefaultNs("id");
                Symbol parser_result141 = new Symbol(37, parser_stack.get(parser_top - 0).left, parser_stack.get(parser_top - 0).right, result71);
                return parser_result141;
            default:
                throw new Exception("Invalid action number found in internal parse table");
        }
    }
}
