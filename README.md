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

# TODO

### As far as I'm aware, this is what is missing from R5RS at the moment:

#### Incomplete features:
* Suffixes s, f, d, l for numerical constants
* (expt n) must return exact numbers if possible
* Some builtins break if other builtins are re-defined

#### Missing builtins:
* apply
* load
* Complex numbers: angle, make-polar
* Continuations: call-with-current-continuation, values, call-with-values, dynamic-wind
* Macros: define-syntax, let-syntax, letrec-syntax, syntax-rules

#### Ideas for the future
* Line numbers in error messages (can of worms)
* Interface to Java types and code

