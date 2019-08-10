[![Build Status](https://api.travis-ci.org/eobermuhlner/jshell-scriptengine.svg?branch=master)](https://travis-ci.org/eobermuhlner/jshell-scriptengine)
[![Code Coverage](https://badgen.net/codecov/c/github/eobermuhlner/jshell-scriptengine)](https://codecov.io/gh/eobermuhlner/jshell-scriptengine)
[![Open Issues](https://badgen.net/github/open-issues/eobermuhlner/jshell-scriptengine)](https://github.com/eobermuhlner/jshell-scriptengine/issues)
[![Commits](https://badgen.net/github/commits/eobermuhlner/jshell-scriptengine)](https://github.com/eobermuhlner/jshell-scriptengine/graphs/commit-activity)
[![Last Commits](https://badgen.net/github/last-commit/eobermuhlner/jshell-scriptengine)](https://github.com/eobermuhlner/jshell-scriptengine/graphs/commit-activity)
[![Maven Central - jshell-scriptengine](https://img.shields.io/maven-central/v/ch.obermuhlner/jshell-scriptengine.svg)](https://search.maven.org/artifact/ch.obermuhlner/jshell-scriptengine)

# JShell scripting engine

The JShell was introduced with Java 9 and was designed to be used
for interactive execution of code snippets in Java.

The `jshell-scriptengine` library is a Java 11 wrapper around the JShell
API that executes an entire script and handles the binding of variables.

`jshell-scriptengine` is a good alternative to the usual `javascript`
commonly used, especially if the users that will end up writing the
scripts are already experienced Java programmers or can use the leverage
that a `Java` framework can provide.

Using a scripting engine is a powerful choice if your project needs to
execute code that can be easily changed outside of the development cycle
and when already deployed.

Typical use cases are:
- directory in your deployed application containing scripts for customizable business logic
- editor in your application to edit (and persist) scripts

If you believe that `Java` as a scripting language does not exactly fit
your needs, consider the sibling project
[`spel-scriptengine`](https://github.com/eobermuhlner/spel-scriptengine)
(Spring Expression Language Scripting Engine).

## Using JShell scripting engine in your projects 

To use the JShell scripting you can either download the newest version of the .jar file from the
[published releases on Github](https://github.com/eobermuhlner/jshell-scriptengine/releases/)
or use the following dependency to
[Maven Central](https://search.maven.org/#search%7Cga%7C1%7Cjshell-scriptengine)
in your build script (please verify the version number to be the newest release):

### Use JShell scripting engine in Maven build

```xml
<dependency>
  <groupId>ch.obermuhlner</groupId>
  <artifactId>jshell-scriptengine</artifactId>
  <version>1.0.0</version>
</dependency>
```

### Use JShell scripting engine in Gradle build

```gradle
repositories {
  mavenCentral()
}

dependencies {
  compile 'ch.obermuhlner:jshell-scriptengine:1.0.0'
}
```

## Simple usage

The following code snippet shows a simple usage of the JShell script engine:
```java
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
```

The console output of this snippet shows that the bindings for input and output variables are working correctly.
The return value of the JShell script is the value of the last statement `1000 + output`.
```console
Input A: 2
Input B: 3
Result: 1005
Output Variable: 5
```

## Access to classes

The JShell script is executed in the same process 
and has therefore access to the same classes.

Assume your project has the following class:
```java
package ch.obermuhlner.scriptengine.example;

public class Person {
    public String name;
    public int birthYear;

    @Override
    public String toString() {
        return "Person{name=" + name + ", birthYear=" + birthYear + "}";
    }
}
```

In this case you can run a JShell script that uses this class `Person`:
```java
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
```

The console output of this snippet shows that the variable `person` created inside the JShell script is now available in the calling Java:
```console
Result: 1967
Person Variable: Person{name=Eric, birthYear=1967}
```

## Error handling

```java
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
```

The console output of this snippet shows that the variable `unknown` cannot be found:
```console
javax.script.ScriptException: cannot find symbol
  symbol:   variable unknown
  location: class 
System.out.println(unknown);
	at ch.obermuhlner.scriptengine.jshell.JShellScriptEngine.evaluateScript(JShellScriptEngine.java:216)
	at ch.obermuhlner.scriptengine.jshell.JShellScriptEngine.eval(JShellScriptEngine.java:98)
	at ch.obermuhlner.scriptengine.jshell.JShellScriptEngine.eval(JShellScriptEngine.java:84)
	at ch.obermuhlner.scriptengine.jshell.JShellScriptEngine.eval(JShellScriptEngine.java:74)
	at ch.obermuhlner.scriptengine.example.ScriptEngineExample.runErrorExample(ScriptEngineExample.java:84)
	at ch.obermuhlner.scriptengine.example.ScriptEngineExample.main(ScriptEngineExample.java:14)
```
