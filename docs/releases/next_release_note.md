# API changes

## Support for `Compilable`

The `jshell-scriptengine` supports now the `Compilable` interface.

If the same script needs to be evaluated multiple times with different
variable bindings then it is much faster to compile the script only once
and evaluate the compiled script multiple times.

# Bugfixes

## Close `JShell` instance after evaluation

In order to free resources the JShell instance is now closed after
evaluation.

# Examples

Note: The example code is available on github, but not part of the
`jshell-scriptengine` library.

No changes in the examples.
