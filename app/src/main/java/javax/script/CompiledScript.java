package javax.script;

/* loaded from: rt.jar:javax/script/CompiledScript.class */
public abstract class CompiledScript {
    public abstract Object eval(ScriptContext scriptContext) throws ScriptException;

    public abstract ScriptEngine getEngine();

    public Object eval(Bindings bindings) throws ScriptException {
        ScriptContext context = getEngine().getContext();
        if (bindings != null) {
            SimpleScriptContext simpleScriptContext = new SimpleScriptContext();
            simpleScriptContext.setBindings(bindings, 100);
            simpleScriptContext.setBindings(context.getBindings(200), 200);
            simpleScriptContext.setWriter(context.getWriter());
            simpleScriptContext.setReader(context.getReader());
            simpleScriptContext.setErrorWriter(context.getErrorWriter());
            context = simpleScriptContext;
        }
        return eval(context);
    }

    public Object eval() throws ScriptException {
        return eval(getEngine().getContext());
    }
}
