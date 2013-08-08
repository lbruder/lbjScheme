lbjScheme
=========

Java version of my toy Scheme interpreter

This will become my most complete Scheme implementation so far, striving for 100% R5RS compatibility when done.
I'll implement the type system and builtins first, with a very basic evaluator (interpreter) to test it, then add an
analyzing evaluator like the one described in SICP for better performance, then I'll upgrade to a compiler with a
VM backend that supports continuations. Let's see how it works out.

Everything in this repo is published under the BSD license unless stated otherwise in the respective file.

# TODO

### Missing stuff in the most probable order of my implementing it:

#### Input Ports:
* "Input Port" data type
* call-with-input-file
* char-ready?
* close-input-port
* current-input-port
* eof-object?
* input-port?
* open-input-file
* peek-char
* read
* read-char
* with-input-from-file

#### System stuff and missing special forms:
* apply
* case
* letrec
* load
* named let
* transcript-on
* transcript-off
* unquote-splicing

#### Real numbers:
* "Real" data type
* exact->inexact
* exact?
* ceiling
* floor
* inexact->exact
* inexact?
* rationalize
* real?
* round
* truncate

#### Math:
* acos
* asin
* atan
* cos
* exp
* expt
* log
* sin
* sqrt
* tan

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
* free-identifier=?
* generate-identifier
* identifier->symbol
* identifier?
* let-syntax
* letrec-syntax
* syntax
* syntax-rules
* unwrap-syntax
