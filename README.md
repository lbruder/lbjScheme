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

### Missing stuff in the most probable order of my implementing it:

#### High prio:
* Preserve exactness on Complex operations
* angle, magnitude, make-polar, make-rectangular
* case
* (sqrt n) must return complex for negative n, and work on complex numbers too
* (expt n) must return exact numbers if possible
* Tests, tests, tests!
* apply
* load
* unquote-splicing
* Line numbers in error messages (can of worms)
* Some kind of interface to native Java data types, javax.script

#### Reading numerical constants:
* Prefixes #e, #i
* Suffixes s, f, d, l

#### Compiling evaluator:
* class CompilingEvaluator
* virtual machine to run the compiled code
* call-with-current-continuation
* values, call-with-values
* dynamic-wind

#### Hygienic macros:
* define-syntax
* let-syntax
* letrec-syntax
* syntax-rules

