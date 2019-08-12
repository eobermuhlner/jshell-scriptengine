package ch.obermuhlner.scriptengine.example;

import javax.script.*;

public class ScriptEngineExample {
    public static void main(String[] args) {
        runNashornExamples();
        runJShellExamples();
    }

    private static void runNashornExamples() {
        runExample("nashorn", "2+3");
    }

    private static void runJShellExamples() {
        runExample("jshell", "2+3");

        runJShellBindingExample();
        runJShellVisibleClassesExample();
        runJShellErrorExample();
        runJShellCompileExample();
    }

    private static void runExample(String engineName, String script) {
        try {
            System.out.println("Engine: " + engineName);
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName(engineName);
            Object result = engine.eval(script);
            System.out.println("Result: " + result);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }

    private static void runJShellBindingExample() {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("jshell");

            String script = "" +
                    "System.out.println(\"Input A: \" + inputA);" +
                    "System.out.println(\"Input B: \" + inputB);" +
                    "var output = inputA + inputB;" +
                    "1000 + output;";

            engine.put("inputA", 2);
            engine.put("inputB", 3);

            Object result = engine.eval(script);
            System.out.println("Result: " + result);

            Object output = engine.get("output");
            System.out.println("Output Variable: " + output);

        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }

    private static void runJShellVisibleClassesExample() {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("jshell");

            String script = "" +
                    "import ch.obermuhlner.scriptengine.example.Person;" +
                    "var person = new Person();" +
                    "person.name = \"Eric\";" +
                    "person.birthYear = 1967;";

            Object result = engine.eval(script);
            System.out.println("Result: " + result);

            Object person = engine.get("person");
            System.out.println("Person Variable: " + person);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }

    private static void runJShellErrorExample() {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("jshell");

            String script = "" +
                    "System.out.println(unknown);" +
                    "var message = \"Should never reach this point\"";

            Object result = engine.eval(script);
            System.out.println("Result: " + result);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }

    private static void runJShellCompileExample() {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("jshell");
            Compilable compiler = (Compilable) engine;

            CompiledScript compiledScript = compiler.compile("var output = alpha + beta");

            {
                Bindings bindings = engine.createBindings();

                bindings.put("alpha", 2);
                bindings.put("beta", 3);
                Object result = compiledScript.eval(bindings);
                Integer output = (Integer) bindings.get("output");
                System.out.println("Result (Integer): " + result);
                System.out.println("Output (Integer): " + output);
            }

            {
                Bindings bindings = engine.createBindings();

                bindings.put("alpha", "aaa");
                bindings.put("beta", "bbb");
                Object result = compiledScript.eval(bindings);
                String output = (String) bindings.get("output");
                System.out.println("Result (String): " + result);
                System.out.println("Output (String): " + output);
            }
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }
}
