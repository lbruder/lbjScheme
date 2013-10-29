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
continuations. Let's see how it works out. In the future there might
even be a compiler that generates Java or .NET bytecode, or C sources
or whatever. Focus is on keeping this thing embeddable though, as there
are lots of great Scheme-to-something compilers out there already.

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

## FAQ

Q: Which of the "advanced features" of Scheme are supported?
A: The full numeric tower, tail calls and lexical scoping work. Exactness of
   numbers is preserved as long as feasible. First-class continuations and the
   ability to freely redefine builtins without others breaking will be present
   in the 1.0 release. Macros will be CL style in the 1.0 release,
   i.e. (defmacro name (params) ...) instead of Scheme (hygienic) macros. Maybe
   in a later version I'll support Scheme macros too.
   There are a few builtins that are not part of the standard, and a few parts
   of SRFI-1, but none of the SRFIs are officially part of lbjScheme at the
   moment.

Q: How fast is this thing?
A: Not my primary concern. At the moment, this is a very simple interpreter with
   no performance optimizations so I can keep the code clean and simple. The
   next evaluator will be a compiler though, and maybe I'll add a JVM bytecode
   backend, too. Let's see. First thing is to get lbjScheme R5RS compliant.
   
Q: Is there a console REPL?
A: The .jar file, when started via "java -jar lbjScheme.jar", will open a GUI
   window or start a console REPL depending on from where it was started.
   There are a few command line options for the console REPL that I'll refactor
   a bit in the near future so that the GUI will support them too, and add a
   force-console mode too. Nothing fancy yet.
   
Q: Why did you write this?
A: Why not?

Q: Seriously, why?
A: This is my Scheme interpreter. There are many like it, but this one is mine.
   I'm doing this because I want to learn more about the Scheme language,
   compiler and interpreter implementation techniques, and so on. Reading
   [SICP](http://mitpress.mit.edu/sicp/) challenged my mind in so many ways
   that I wanted to build my own interpreter to toy around with. This is the
   culmination of lots of little side projects evolving in parallel, and now
   I'm closer than ever to completing my very own Scheme implementation, one
   that I fully understand, without any magic going on inside some source code
   taken from other Open Source projects. Maybe I'll even add a Scheme-to-C
   compiler that uses its own garbage collector. Lots of cool stuff to
   experiment with.
   Apart from that, if you truly want to learn a programming language, write a
   compiler for it. And Scheme is one of the few languages out there that manage
   to be both powerful and simple, so the concepts you learn in the process of
   implementing Scheme change how you think about programming in general.

## TODO

As far as I'm aware (!), this is what's left to do to reach full R5RS
compliance:

* Make (expt n) return exact numbers if possible
* Some builtins break if other builtins are re-defined
* Add (load) builtin (optional in R5RS)
* Add compiling evaluator, continuations, (dynamic-wind)
* Add Scheme macros: define-syntax, let-syntax, letrec-syntax, syntax-rules

