lbjScheme
=========

Java version of my toy Scheme interpreter

This will become my most complete Scheme implementation so far, striving for 100% R5RS compatibility when done.

There will be (at least) three evaluators:

* A very simple interpreter that even expands macros on the fly as they are encountered
* An analyzing interpreter that performs an analyzer step before interpreting; macros are expanded during analysis
* A compiler that uses the same analyzer, but instead of interpreting the AST will emit bytecode for a VM that is then used to run the program.

Currently I plan for the first two to remain quite simple and incomplete, more as a test harness for the builtins, while the compiler
will support first class continuations, (values) and so on. Let's see how it works out.

Everything in this repo is published under the BSD license unless stated otherwise in the respective file.

# TODO

### Missing stuff in the most probable order of my implementing it:

#### Unit tests

* 'nuff said.

#### Real numbers:
* (expt n) must return exact numbers if possible
* ceiling
* floor
* round
* truncate

#### System stuff:
* apply
* case
* letrec
* load
* named let
* transcript-on
* transcript-off
* unquote-splicing
* Some kind of interface to native Java data types
* Interface to javax.script
* Line numbers in error messages (can of worms)
* CompilingEvaluator that supports continuations and multiple value returns

#### Complex numbers:
* "Complex" data type
* angle
* complex?
* imag-part
* magnitude
* make-polar
* make-rectangular
* real-part

#### Reading numerical constants:
* Prefixes #b #o, #d, #e, #i
* Suffixes s, f, d, l

#### Other stuff:
* call-with-values
* dynamic-wind
* values
* call-with-current-continuation
* call/cc

#### Hygienic macros:
* define-syntax
* let-syntax
* letrec-syntax
* syntax-rules

