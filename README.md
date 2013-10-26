lbjScheme
=========

## An embeddable Scheme interpreter in pure Java

This will become my most complete Scheme implementation so far, striving
for 100% R5RS compliance when done.

There will be (at least) three evaluators:

* A very simple interpreter that even expands macros on the fly as they
  are encountered (done)

* An analyzing interpreter that performs an analyzer step before
  interpreting; macros are expanded during analysis (done)

* A compiler that uses the same analyzer, but instead of interpreting
  the AST will emit bytecode for a VM that is then used to run the
  program (todo)

The first two will remain quite simple and incomplete, more as a test
harness for the builtins, while the compiler will support first class
continuations, (values) and so on. Let's see how it works out. In the
future there might even be a compiler that generates Java or .NET
bytecode, or C sources or whatever. Focus is on keeping this thing
embeddable though, as there are lots of great Scheme-to-something
compilers out there already.

Everything in this repo is published under the BSD license unless stated
otherwise in the respective file.

## How to build

Make sure you have the following installed on your system:

* A working [JDK 1.7 or higher](http://openjdk.java.net/)
* [Apache Ant](http://ant.apache.org)

Download the .zip file, extract and run Ant in the extracted folder. Once
Ant has finished building and testing lbjScheme, you will find the
generated .jar file in the build/jar folder.

Alternatively, you can import the project into
[Eclipse](http://eclipse.org/) and build/test from there.

## JUnit

The git repo contains the JUnit 4 .jar file. Building lbjScheme with
Ant will use it to run all unit and integration tests during the build
process.  Legalese: JUnit is included ONLY to make testing as easy as
possible. I am not connected to the JUnit project in any other way. You
can download the sources and possibly newer releases from
[the JUnit repo on GitHub](https://github.com/junit-team/junit).

## TODO

As far as I'm aware (!), this is what's left to do to reach full R5RS
compliance:

* Make (expt n) return exact numbers if possible
* Some builtins break if other builtins are re-defined
* Add (load) builtin (optional)
* Add (values) and (call-with-values)
* Add call-with-current-continuation, dynamic-wind
* Add Scheme macros: define-syntax, let-syntax, letrec-syntax, syntax-rules

