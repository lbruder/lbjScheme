lbjScheme
=========

Java version of my toy Scheme interpreter

This will become my most complete Scheme implementation so far, striving for 100% R5RS compliance when done.

There will be (at least) three evaluators:

* A very simple interpreter that even expands macros on the fly as they are encountered
* An analyzing interpreter that performs an analyzer step before interpreting; macros are expanded during analysis
* A compiler that uses the same analyzer, but instead of interpreting the AST will emit bytecode for a VM that is then used to run the program.

Currently I plan for the first two to remain quite simple and incomplete, more as a test harness for the builtins, while the compiler
will support first class continuations, (values) and so on. Let's see how it works out.

Everything in this repo is published under the BSD license unless stated otherwise in the respective file.

## TODO

As far as I'm aware (!), this is what's left to do to reach full R5RS compliance:

* Make (expt n) return exact numbers if possible
* Speed up (sqrt n)
* Some builtins break if other builtins are re-defined
* Add (apply)
* Add (load)
* Add call-with-current-continuation, values, call-with-values, dynamic-wind
* Add define-syntax, let-syntax, letrec-syntax, syntax-rules

