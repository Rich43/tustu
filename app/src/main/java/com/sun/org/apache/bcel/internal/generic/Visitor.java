package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/Visitor.class */
public interface Visitor {
    void visitStackInstruction(StackInstruction stackInstruction);

    void visitLocalVariableInstruction(LocalVariableInstruction localVariableInstruction);

    void visitBranchInstruction(BranchInstruction branchInstruction);

    void visitLoadClass(LoadClass loadClass);

    void visitFieldInstruction(FieldInstruction fieldInstruction);

    void visitIfInstruction(IfInstruction ifInstruction);

    void visitConversionInstruction(ConversionInstruction conversionInstruction);

    void visitPopInstruction(PopInstruction popInstruction);

    void visitStoreInstruction(StoreInstruction storeInstruction);

    void visitTypedInstruction(TypedInstruction typedInstruction);

    void visitSelect(Select select);

    void visitJsrInstruction(JsrInstruction jsrInstruction);

    void visitGotoInstruction(GotoInstruction gotoInstruction);

    void visitUnconditionalBranch(UnconditionalBranch unconditionalBranch);

    void visitPushInstruction(PushInstruction pushInstruction);

    void visitArithmeticInstruction(ArithmeticInstruction arithmeticInstruction);

    void visitCPInstruction(CPInstruction cPInstruction);

    void visitInvokeInstruction(InvokeInstruction invokeInstruction);

    void visitArrayInstruction(ArrayInstruction arrayInstruction);

    void visitAllocationInstruction(AllocationInstruction allocationInstruction);

    void visitReturnInstruction(ReturnInstruction returnInstruction);

    void visitFieldOrMethod(FieldOrMethod fieldOrMethod);

    void visitConstantPushInstruction(ConstantPushInstruction constantPushInstruction);

    void visitExceptionThrower(ExceptionThrower exceptionThrower);

    void visitLoadInstruction(LoadInstruction loadInstruction);

    void visitVariableLengthInstruction(VariableLengthInstruction variableLengthInstruction);

    void visitStackProducer(StackProducer stackProducer);

    void visitStackConsumer(StackConsumer stackConsumer);

    void visitACONST_NULL(ACONST_NULL aconst_null);

    void visitGETSTATIC(GETSTATIC getstatic);

    void visitIF_ICMPLT(IF_ICMPLT if_icmplt);

    void visitMONITOREXIT(MONITOREXIT monitorexit);

    void visitIFLT(IFLT iflt);

    void visitLSTORE(LSTORE lstore);

    void visitPOP2(POP2 pop2);

    void visitBASTORE(BASTORE bastore);

    void visitISTORE(ISTORE istore);

    void visitCHECKCAST(CHECKCAST checkcast);

    void visitFCMPG(FCMPG fcmpg);

    void visitI2F(I2F i2f);

    void visitATHROW(ATHROW athrow);

    void visitDCMPL(DCMPL dcmpl);

    void visitARRAYLENGTH(ARRAYLENGTH arraylength);

    void visitDUP(DUP dup);

    void visitINVOKESTATIC(INVOKESTATIC invokestatic);

    void visitLCONST(LCONST lconst);

    void visitDREM(DREM drem);

    void visitIFGE(IFGE ifge);

    void visitCALOAD(CALOAD caload);

    void visitLASTORE(LASTORE lastore);

    void visitI2D(I2D i2d);

    void visitDADD(DADD dadd);

    void visitINVOKESPECIAL(INVOKESPECIAL invokespecial);

    void visitIAND(IAND iand);

    void visitPUTFIELD(PUTFIELD putfield);

    void visitILOAD(ILOAD iload);

    void visitDLOAD(DLOAD dload);

    void visitDCONST(DCONST dconst);

    void visitNEW(NEW r1);

    void visitIFNULL(IFNULL ifnull);

    void visitLSUB(LSUB lsub);

    void visitL2I(L2I l2i);

    void visitISHR(ISHR ishr);

    void visitTABLESWITCH(TABLESWITCH tableswitch);

    void visitIINC(IINC iinc);

    void visitDRETURN(DRETURN dreturn);

    void visitFSTORE(FSTORE fstore);

    void visitDASTORE(DASTORE dastore);

    void visitIALOAD(IALOAD iaload);

    void visitDDIV(DDIV ddiv);

    void visitIF_ICMPGE(IF_ICMPGE if_icmpge);

    void visitLAND(LAND land);

    void visitIDIV(IDIV idiv);

    void visitLOR(LOR lor);

    void visitCASTORE(CASTORE castore);

    void visitFREM(FREM frem);

    void visitLDC(LDC ldc);

    void visitBIPUSH(BIPUSH bipush);

    void visitDSTORE(DSTORE dstore);

    void visitF2L(F2L f2l);

    void visitFMUL(FMUL fmul);

    void visitLLOAD(LLOAD lload);

    void visitJSR(JSR jsr);

    void visitFSUB(FSUB fsub);

    void visitSASTORE(SASTORE sastore);

    void visitALOAD(ALOAD aload);

    void visitDUP2_X2(DUP2_X2 dup2_x2);

    void visitRETURN(RETURN r1);

    void visitDALOAD(DALOAD daload);

    void visitSIPUSH(SIPUSH sipush);

    void visitDSUB(DSUB dsub);

    void visitL2F(L2F l2f);

    void visitIF_ICMPGT(IF_ICMPGT if_icmpgt);

    void visitF2D(F2D f2d);

    void visitI2L(I2L i2l);

    void visitIF_ACMPNE(IF_ACMPNE if_acmpne);

    void visitPOP(POP pop);

    void visitI2S(I2S i2s);

    void visitIFEQ(IFEQ ifeq);

    void visitSWAP(SWAP swap);

    void visitIOR(IOR ior);

    void visitIREM(IREM irem);

    void visitIASTORE(IASTORE iastore);

    void visitNEWARRAY(NEWARRAY newarray);

    void visitINVOKEINTERFACE(INVOKEINTERFACE invokeinterface);

    void visitINEG(INEG ineg);

    void visitLCMP(LCMP lcmp);

    void visitJSR_W(JSR_W jsr_w);

    void visitMULTIANEWARRAY(MULTIANEWARRAY multianewarray);

    void visitDUP_X2(DUP_X2 dup_x2);

    void visitSALOAD(SALOAD saload);

    void visitIFNONNULL(IFNONNULL ifnonnull);

    void visitDMUL(DMUL dmul);

    void visitIFNE(IFNE ifne);

    void visitIF_ICMPLE(IF_ICMPLE if_icmple);

    void visitLDC2_W(LDC2_W ldc2_w);

    void visitGETFIELD(GETFIELD getfield);

    void visitLADD(LADD ladd);

    void visitNOP(NOP nop);

    void visitFALOAD(FALOAD faload);

    void visitINSTANCEOF(INSTANCEOF r1);

    void visitIFLE(IFLE ifle);

    void visitLXOR(LXOR lxor);

    void visitLRETURN(LRETURN lreturn);

    void visitFCONST(FCONST fconst);

    void visitIUSHR(IUSHR iushr);

    void visitBALOAD(BALOAD baload);

    void visitDUP2(DUP2 dup2);

    void visitIF_ACMPEQ(IF_ACMPEQ if_acmpeq);

    void visitIMPDEP1(IMPDEP1 impdep1);

    void visitMONITORENTER(MONITORENTER monitorenter);

    void visitLSHL(LSHL lshl);

    void visitDCMPG(DCMPG dcmpg);

    void visitD2L(D2L d2l);

    void visitIMPDEP2(IMPDEP2 impdep2);

    void visitL2D(L2D l2d);

    void visitRET(RET ret);

    void visitIFGT(IFGT ifgt);

    void visitIXOR(IXOR ixor);

    void visitINVOKEVIRTUAL(INVOKEVIRTUAL invokevirtual);

    void visitFASTORE(FASTORE fastore);

    void visitIRETURN(IRETURN ireturn);

    void visitIF_ICMPNE(IF_ICMPNE if_icmpne);

    void visitFLOAD(FLOAD fload);

    void visitLDIV(LDIV ldiv);

    void visitPUTSTATIC(PUTSTATIC putstatic);

    void visitAALOAD(AALOAD aaload);

    void visitD2I(D2I d2i);

    void visitIF_ICMPEQ(IF_ICMPEQ if_icmpeq);

    void visitAASTORE(AASTORE aastore);

    void visitARETURN(ARETURN areturn);

    void visitDUP2_X1(DUP2_X1 dup2_x1);

    void visitFNEG(FNEG fneg);

    void visitGOTO_W(GOTO_W goto_w);

    void visitD2F(D2F d2f);

    void visitGOTO(GOTO r1);

    void visitISUB(ISUB isub);

    void visitF2I(F2I f2i);

    void visitDNEG(DNEG dneg);

    void visitICONST(ICONST iconst);

    void visitFDIV(FDIV fdiv);

    void visitI2B(I2B i2b);

    void visitLNEG(LNEG lneg);

    void visitLREM(LREM lrem);

    void visitIMUL(IMUL imul);

    void visitIADD(IADD iadd);

    void visitLSHR(LSHR lshr);

    void visitLOOKUPSWITCH(LOOKUPSWITCH lookupswitch);

    void visitDUP_X1(DUP_X1 dup_x1);

    void visitFCMPL(FCMPL fcmpl);

    void visitI2C(I2C i2c);

    void visitLMUL(LMUL lmul);

    void visitLUSHR(LUSHR lushr);

    void visitISHL(ISHL ishl);

    void visitLALOAD(LALOAD laload);

    void visitASTORE(ASTORE astore);

    void visitANEWARRAY(ANEWARRAY anewarray);

    void visitFRETURN(FRETURN freturn);

    void visitFADD(FADD fadd);

    void visitBREAKPOINT(BREAKPOINT breakpoint);
}
