package org.lb.lbjscheme;

import java.io.*;
import java.util.List;

public abstract class EvaluatorBase implements Evaluator {
	private final Environment _global = new Environment();

	@Override
	public Environment getGlobalEnvironment() {
		return _global;
	}

	public EvaluatorBase() throws SchemeException {
		addBuiltinsToGlobalEnvironment();
		eval(_initScript);
	}

	private void addBuiltin(Builtin builtin) throws SchemeException {
		_global.define(Symbol.fromString(builtin.getName()), builtin);

	}

	private void addBuiltinsToGlobalEnvironment() throws SchemeException {
		// Oh lambda syntax, how I long for thee...

		addBuiltin(new Builtin("cons") {
			@Override
			public SchemeObject apply(List<SchemeObject> parameters)
					throws SchemeException {
				assertParameterCount(2, parameters);
				return Builtins.cons(parameters.get(0), parameters.get(1));
			}
		});

		addBuiltin(new Builtin("cons") {
			@Override
			public SchemeObject apply(List<SchemeObject> parameters)
					throws SchemeException {
				assertParameterCount(2, parameters);
				return Builtins.cons(parameters.get(0), parameters.get(1));
			}
		});

		addBuiltin(new Builtin("car") {
			@Override
			public SchemeObject apply(List<SchemeObject> parameters)
					throws SchemeException {
				assertParameterCount(1, parameters);
				return Builtins.car(parameters.get(0));
			}
		});

		addBuiltin(new Builtin("cdr") {
			@Override
			public SchemeObject apply(List<SchemeObject> parameters)
					throws SchemeException {
				assertParameterCount(1, parameters);
				return Builtins.cdr(parameters.get(0));
			}
		});

		addBuiltin(new Builtin("set-car!") {
			@Override
			public SchemeObject apply(List<SchemeObject> parameters)
					throws SchemeException {
				assertParameterCount(2, parameters);
				return Builtins.setCar(parameters.get(0), parameters.get(1));
			}
		});

		addBuiltin(new Builtin("set-cdr!") {
			@Override
			public SchemeObject apply(List<SchemeObject> parameters)
					throws SchemeException {
				assertParameterCount(2, parameters);
				return Builtins.setCdr(parameters.get(0), parameters.get(1));
			}
		});

		addBuiltin(new Builtin("set-car!") {
			@Override
			public SchemeObject apply(List<SchemeObject> parameters)
					throws SchemeException {
				assertParameterCount(2, parameters);
				return Builtins.setCar(parameters.get(0), parameters.get(1));
			}
		});

		addBuiltin(new Builtin("eq?") {
			@Override
			public SchemeObject apply(List<SchemeObject> parameters)
					throws SchemeException {
				assertParameterCount(2, parameters);
				return parameters.get(0) == parameters.get(1) ? True
						.getInstance() : False.getInstance();
			}
		});

		addBuiltin(new Builtin("null?") {
			@Override
			public SchemeObject apply(List<SchemeObject> parameters)
					throws SchemeException {
				assertParameterCount(1, parameters);
				return parameters.get(0) instanceof Nil ? True.getInstance()
						: False.getInstance();
			}
		});

		addBuiltin(new Builtin("pair?") {
			@Override
			public SchemeObject apply(List<SchemeObject> parameters)
					throws SchemeException {
				assertParameterCount(1, parameters);
				return parameters.get(0) instanceof Pair ? True.getInstance()
						: False.getInstance();
			}
		});

		addBuiltin(new Builtin("number?") {
			@Override
			public SchemeObject apply(List<SchemeObject> parameters)
					throws SchemeException {
				assertParameterCount(1, parameters);
				return parameters.get(0) instanceof SchemeNumber ? True
						.getInstance() : False.getInstance();
			}
		});

		addBuiltin(new Builtin("string?") {
			@Override
			public SchemeObject apply(List<SchemeObject> parameters)
					throws SchemeException {
				assertParameterCount(1, parameters);
				return parameters.get(0) instanceof SchemeString ? True
						.getInstance() : False.getInstance();
			}
		});

		addBuiltin(new Builtin("char?") {
			@Override
			public SchemeObject apply(List<SchemeObject> parameters)
					throws SchemeException {
				assertParameterCount(1, parameters);
				return parameters.get(0) instanceof SchemeCharacter ? True
						.getInstance() : False.getInstance();
			}
		});

		addBuiltin(new Builtin("boolean?") {
			@Override
			public SchemeObject apply(List<SchemeObject> parameters)
					throws SchemeException {
				assertParameterCount(1, parameters);
				return (parameters.get(0) instanceof True)
						|| (parameters.get(0) instanceof False) ? True
						.getInstance() : False.getInstance();
			}
		});

		addBuiltin(new Builtin("symbol?") {
			@Override
			public SchemeObject apply(List<SchemeObject> parameters)
					throws SchemeException {
				assertParameterCount(1, parameters);
				return parameters.get(0) instanceof Symbol ? True.getInstance()
						: False.getInstance();
			}
		});

		addBuiltin(new Builtin("procedure?") {
			@Override
			public SchemeObject apply(List<SchemeObject> parameters)
					throws SchemeException {
				assertParameterCount(1, parameters);
				return (parameters.get(0) instanceof Lambda)
						|| (parameters.get(0) instanceof Builtin) ? True
						.getInstance() : False.getInstance();
			}
		});

		addBuiltin(new Builtin("list?") {
			@Override
			public SchemeObject apply(List<SchemeObject> parameters)
					throws SchemeException {
				assertParameterCount(1, parameters);
				return (parameters.get(0) instanceof SchemeList)
						&& (!((SchemeList) parameters.get(0)).isDottedList()) ? True
						.getInstance() : False.getInstance();
			}
		});

		// AddFunction("integer?", (object a) => a is int);
		// AddFunction("real?", (object a) => a is double);
		// AddFunction("+", MakeNumericalFunction("+", (i1, i2) => checked(i1 +
		// i2), (d1, d2) => d1 + d2));
		// AddFunction("-", MakeNumericalFunction("-", (i1, i2) => i1 - i2, (d1,
		// d2) => d1 - d2));
		// AddFunction("*", MakeNumericalFunction("*", (i1, i2) => checked(i1 *
		// i2), (d1, d2) => d1 * d2));
		// AddFunction("/", MakeNumericalFunction("/", (i1, i2) => i1 / i2, (d1,
		// d2) => d1 / d2));
		// AddFunction("<", MakeNumericalFunction("<", (i1, i2) => i1 < i2, (d1,
		// d2) => d1 < d2));
		// AddFunction(">", MakeNumericalFunction(">", (i1, i2) => i1 > i2, (d1,
		// d2) => d1 > d2));
		// AddFunction("=", MakeNumericalFunction("=", (i1, i2) => i1 == i2,
		// (d1, d2) => d1 == d2));
		// AddFunction("sin", (double d) => Math.Sin(d));
		// AddFunction("cos", (double d) => Math.Cos(d));
		// AddFunction("tan", (double d) => Math.Tan(d));
		// AddFunction("sqrt", (object a) => Math.Sqrt(Convert.ToDouble(a)));
		// AddFunction("expt", (object b, object exp) =>
		// Math.Pow(Convert.ToDouble(b), Convert.ToDouble(exp)));
		// AddFunction("quotient", MakeNumericalFunction("quotient", (i1, i2) =>
		// i1 / i2, (d1, d2) => (int)(d1 / d2)));
		// AddFunction("remainder", MakeNumericalFunction("remainder", (i1, i2)
		// => i1 % i2, (d1, d2) => (int)d1 % (int)d2));
		// AddFunction("random", (int a) => random.Next(a));
		// AddFunction("display", (object a) => { Print(ObjectToString(a,
		// true)); return undefinedSymbol; });
		// AddFunction("char=?", (char a, char b) => a == b);
		// AddFunction("char>?", (char a, char b) => a > b);
		// AddFunction("char<?", (char a, char b) => a < b);
		// AddFunction("char-ci=?", (char a, char b) => char.ToLowerInvariant(a)
		// == char.ToLowerInvariant(b));
		// AddFunction("char-ci>?", (char a, char b) => char.ToLowerInvariant(a)
		// > char.ToLowerInvariant(b));
		// AddFunction("char-ci<?", (char a, char b) => char.ToLowerInvariant(a)
		// < char.ToLowerInvariant(b));
		// AddFunction("char-alphabetic?", (char a) => char.IsLetter(a)); //
		// HACK: Re-code in Scheme
		// AddFunction("char-numeric?", (char a) => char.IsDigit(a)); // HACK:
		// Re-code in Scheme
		// AddFunction("char-whitespace?", (char a) => char.IsWhiteSpace(a)); //
		// HACK: Re-code in Scheme
		// AddFunction("char-upper-case?", (char a) => char.IsUpper(a)); //
		// HACK: Re-code in Scheme
		// AddFunction("char-lower-case?", (char a) => char.IsLower(a)); //
		// HACK: Re-code in Scheme
		// AddFunction("char-upcase", (char a) => char.ToUpperInvariant(a)); //
		// HACK: Re-code in Scheme
		// AddFunction("char-downcase", (char a) => char.ToLowerInvariant(a));
		// // HACK: Re-code in Scheme
		// AddFunction("string=?", (string a, string b) => String.Compare(a, b,
		// false, CultureInfo.InvariantCulture) == 0);
		// AddFunction("string>?", (string a, string b) => String.Compare(a, b,
		// false, CultureInfo.InvariantCulture) > 0);
		// AddFunction("string<?", (string a, string b) => String.Compare(a, b,
		// false, CultureInfo.InvariantCulture) < 0);
		// AddFunction("string-ci=?", (string a, string b) => String.Compare(a,
		// b, true, CultureInfo.InvariantCulture) == 0);
		// AddFunction("string-ci>?", (string a, string b) => String.Compare(a,
		// b, true, CultureInfo.InvariantCulture) > 0);
		// AddFunction("string-ci<?", (string a, string b) => String.Compare(a,
		// b, true, CultureInfo.InvariantCulture) < 0);
		// AddFunction("string-length", (string a) => a.Length);
		// AddFunction("substring", (string a, int start, int end) =>
		// a.Substring(start, end - start)); // HACK: Re-code in Scheme
		// AddFunction("string-append", (string a, string b) => a + b); // HACK:
		// Re-code in Scheme
		// AddFunction("char->integer", (char c) => (int)c);
		// AddFunction("integer->char", (int i) => (char)i);
		// AddFunction("string-ref", (string s, int index) => s[index]);
		// AddFunction("string->symbol", (string s) => Symbol.FromString(s));
		// AddFunction("symbol->string", (Symbol s) => s.ToString());
		// AddFunction("string->list", (string s) =>
		// Pair.FromEnumerable(s.ToCharArray().Cast<object>())); // HACK:
		// Re-code in Scheme
		// AddFunction("list->string", (IEnumerable<object> list) => list ==
		// null ? "" : new string(list.Cast<char>().ToArray())); // HACK:
		// Re-code in Scheme
		// AddFunction("length", (IEnumerable<object> a) => a == null ? 0 :
		// a.Count());
		// AddFunction("reverse", (IEnumerable<object> list) => list == null ?
		// null : Pair.FromEnumerable(list.Reverse())); // HACK: Re-code in
		// Scheme
		// AddFunction("sys:strtonum", (string s, int b) => s.Contains('.') ?
		// Convert.ToDouble(s, CultureInfo.InvariantCulture) :
		// Convert.ToInt32(s, b)); // HACK: Re-code in Scheme
		// AddFunction("sys:numtostr", (object i, int b) => (i is int) ?
		// Convert.ToString((int)i, b) : Convert.ToString((double)i)); // HACK:
		// Re-code in Scheme
		// AddFunction("map", (object f, IEnumerable<object> list) => list ==
		// null ? null : Pair.FromEnumerable(list.Select(i => Apply(f, false,
		// i)))); // HACK: Re-code in Scheme
		// AddFunction("for-each", (object f, IEnumerable<object> list) => list
		// == null ? 0 : list.Select(i => Apply(f, false, i)).Count()); // HACK:
		// Re-code in Scheme
		// AddFunction("filter", (object f, IEnumerable<object> list) => list ==
		// null ? null : Pair.FromEnumerable(list.Where(i =>
		// EvaluatesToTrue(Apply(f, false, i))))); // HACK: Re-code in Scheme
		// AddFunction("every", (object f, IEnumerable<object> list) => list ==
		// null || list.All(i => EvaluatesToTrue(Apply(f, false, i)))); // HACK:
		// Re-code in Scheme
		// AddFunction("any", (object f, IEnumerable<object> list) => list !=
		// null && list.Any(i => EvaluatesToTrue(Apply(f, false, i)))); // HACK:
		// Re-code in Scheme
		// AddFunction("sort", (IEnumerable<object> list, object f) => list ==
		// null ? null : Pair.FromEnumerable(Sort(list.ToList(), f))); // HACK:
		// Re-code in Scheme
		// AddFunction("apply", (object f, IEnumerable<object> arguments) =>
		// arguments == null ? Apply(f, false) : Apply(f, false,
		// arguments.ToArray()));
		// AddFunction("sys:make-vector", (int size) => new object[size]);
		// AddFunction("vector-ref", (object[] vector, int index) =>
		// vector[index]);
		// AddFunction("vector-length", (object[] vector) => vector.Length);
		// AddFunction("vector-set!", (object[] vector, int index, object obj)
		// => { vector[index] = obj; return undefinedSymbol; });
		// AddFunction("vector?", (object o) => o is object[]);
		// AddFunction("wall-time", (object f) => { var sw = new
		// System.Diagnostics.Stopwatch(); sw.Start(); Apply(f, false);
		// sw.Stop(); return (int)sw.ElapsedMilliseconds; });
		// AddFunction<object, object>("eqv?", Eqv);
		// AddFunction<object, object>("equal?", Equal);
		// AddFunction<IEnumerable<object>>("sys:error", ErrorFunction);
		//
		// AddFunction("sys:read-file", (string fileName) =>
		// File.ReadAllText(fileName));
		// AddFunction("sys:write-file", (string fileName, string contents) => {
		// File.WriteAllText(fileName, contents); return true; });
		// AddFunction("sys:string->object", (string value) => new Reader(new
		// StringReader(value)).Read());
		// AddFunction("sys:object->string", (object o) => ObjectToString(o,
		// false));
		//
		// AddFunction("lb:sleep", (int ms) => { Thread.Sleep(ms); return
		// undefinedSymbol; });
		// // TODO: string-set, string-fill!, make-string, string-copy.
		// Impossible with .NET strings.
		// AddFunction("lb:clr-method", (object o, object name) => new
		// ClrClosure(o, name.ToString()));
		// AddFunction("lb:clr-property-names", (object o) =>
		// o.GetType().GetProperties().Select(i =>
		// Symbol.FromString(i.Name)).ToList());
		// AddFunction("lb:clr-method-names", (object o) =>
		// o.GetType().GetMethods().Select(i =>
		// Symbol.FromString(i.Name)).ToList());
		// AddFunction("lb:clr-get", (object o, Symbol name) =>
		// GetClrProperty(o, name).GetValue(o, new object[0]));
		// AddFunction("lb:clr-set", (object o, Symbol name, object value) =>
		// SetClrProperty(o, name, value));
		// AddFunction("lb:clr-new", (object className, IEnumerable<object>
		// parameters) =>
		// {
		// Type type = Type.GetType(className.ToString(), true);
		// return parameters == null ? Activator.CreateInstance(type) :
		// Activator.CreateInstance(type, parameters.ToArray());
		// });
	}

	private final String _initScript = "(define (complex? obj) #f)"
			+ "(define (rational? obj) #f)"
			// + "(define exact? integer?)"
			// + "(define inexact? real?)"
			+ "(define (caar x) (car (car x)))"
			+ "(define (cadr x) (car (cdr x)))"
			+ "(define (cdar x) (cdr (car x)))"
			+ "(define (cddr x) (cdr (cdr x)))"
			+ "(define (caaar x) (car (car (car x))))"
			+ "(define (caadr x) (car (car (cdr x))))"
			+ "(define (cadar x) (car (cdr (car x))))"
			+ "(define (caddr x) (car (cdr (cdr x))))"
			+ "(define (cdaar x) (cdr (car (car x))))"
			+ "(define (cdadr x) (cdr (car (cdr x))))"
			+ "(define (cddar x) (cdr (cdr (car x))))"
			+ "(define (cdddr x) (cdr (cdr (cdr x))))"
			+ "(define (caaaar x) (car (car (car (car x)))))"
			+ "(define (caaadr x) (car (car (car (cdr x)))))"
			+ "(define (caadar x) (car (car (cdr (car x)))))"
			+ "(define (caaddr x) (car (car (cdr (cdr x)))))"
			+ "(define (cadaar x) (car (cdr (car (car x)))))"
			+ "(define (cadadr x) (car (cdr (car (cdr x)))))"
			+ "(define (caddar x) (car (cdr (cdr (car x)))))"
			+ "(define (cadddr x) (car (cdr (cdr (cdr x)))))"
			+ "(define (cdaaar x) (cdr (car (car (car x)))))"
			+ "(define (cdaadr x) (cdr (car (car (cdr x)))))"
			+ "(define (cdadar x) (cdr (car (cdr (car x)))))"
			+ "(define (cdaddr x) (cdr (car (cdr (cdr x)))))"
			+ "(define (cddaar x) (cdr (cdr (car (car x)))))"
			+ "(define (cddadr x) (cdr (cdr (car (cdr x)))))"
			+ "(define (cdddar x) (cdr (cdr (cdr (car x)))))"
			+ "(define (cddddr x) (cdr (cdr (cdr (cdr x)))))"
			+ "(define (list . lst) lst)"
			+ "(define (flip f) (lambda (a b) (f b a)))"
			+ "(define (newline) (display \"\\n\") 'undefined)"
			+ "(define (zero? x) (= x 0))"
			+ "(define (positive? x) (> x 0))"
			+ "(define (negative? x) (< x 0))"
			+ "(define (<= a b) (if (> a b) #f #t))"
			+ "(define (>= a b) (if (< a b) #f #t))"
			+ "(define (char>=? a b) (if (char<? a b) #f #t))"
			+ "(define (char<=? a b) (if (char>? a b) #f #t))"
			+ "(define (char-ci>=? a b) (if (char-ci<? a b) #f #t))"
			+ "(define (char-ci<=? a b) (if (char-ci>? a b) #f #t))"
			+ "(define (string>=? a b) (if (string<? a b) #f #t))"
			+ "(define (string<=? a b) (if (string>? a b) #f #t))"
			+ "(define (string-ci>=? a b) (if (string-ci<? a b) #f #t))"
			+ "(define (string-ci<=? a b) (if (string-ci>? a b) #f #t))"
			+ "(define (error . params) (sys:error params))"
			+ "(define (abs x) (if (positive? x) x (- 0 x)))"
			+ "(define (sys:sign x) (if (>= x 0) 1 -1))"
			+ "(define (modulo a b) (if (= (sys:sign a) (sys:sign b)) (remainder a b) (+ b (remainder a b))))"
			+ "(define (even? x) (zero? (remainder x 2)))"
			+ "(define (odd? x) (if (even? x) #f #t))"
			+ "(define (not x) (if x #f #t))"
			+ "(define (string . values) (list->string values))"
			+ "(define (list-tail lst k) (if (zero? k) lst (list-tail (cdr lst) (- k 1))))"
			+ "(define (list-ref lst k) (car (list-tail lst k)))"
			+ "(define (string->number n . rest) (if (pair? rest) (sys:strtonum n (car rest)) (sys:strtonum n 10)))"
			+ "(define (number->string n . rest) (if (pair? rest) (sys:numtostr n (car rest)) (sys:numtostr n 10)))"
			+ "(define (sys:gcd-of-two a b) (if (= b 0) a (sys:gcd-of-two b (remainder a b))))"
			+ "(define (sys:lcm-of-two a b) (/ (* a b) (sys:gcd-of-two a b)))"
			+ "(define (fold f acc lst) (if (null? lst) acc (fold f (f (car lst) acc) (cdr lst))))"
			+ "(define (reduce f ridentity lst) (if (null? lst) ridentity (fold f (car lst) (cdr lst))))"
			+ "(define (gcd . args) (if (null? args) 0 (abs (fold sys:gcd-of-two (car args) (cdr args)))))"
			+ "(define (lcm . args) (if (null? args) 1 (abs (fold sys:lcm-of-two (car args) (cdr args)))))"
			+ "(define (append . lsts) (define (iter current acc) (if (pair? current) (iter (cdr current) (cons (car current) acc)) acc)) (reverse (fold iter '() lsts)))"
			+ "(define (memq obj lst) (if (pair? lst) (if (eq? obj (car lst)) lst (memq obj (cdr lst))) #f))"
			+ "(define (memv obj lst) (if (pair? lst) (if (eqv? obj (car lst)) lst (memv obj (cdr lst))) #f))"
			+ "(define (member obj lst) (if (pair? lst) (if (equal? obj (car lst)) lst (member obj (cdr lst))) #f))"
			+ "(define (assq obj lst) (if (pair? lst) (if (eq? obj (caar lst)) (car lst) (assq obj (cdr lst))) #f))"
			+ "(define (assv obj lst) (if (pair? lst) (if (eqv? obj (caar lst)) (car lst) (assv obj (cdr lst))) #f))"
			+ "(define (assoc obj lst) (if (pair? lst) (if (equal? obj (caar lst)) (car lst) (assoc obj (cdr lst))) #f))"
			// +
			// "(defmacro quasiquote (value) (define (qq i) (if (pair? i) (if (eq? 'unquote (car i)) (cadr i) (cons 'list (map qq i))) (list 'quote i))) (qq value))"
			// +
			// "(defmacro let (lst . forms) (cons (cons 'lambda (cons (map car lst) forms)) (map cadr lst)))"
			// +
			// "(defmacro let* (lst . forms) (if (null? lst) (cons 'begin forms) (list 'let (list (car lst)) (cons 'let* (cons (cdr lst) forms)))))"
			// +
			// "(defmacro cond list-of-forms (define (expand-cond lst) (if (null? lst) #f (if (eq? (caar lst) 'else) (cons 'begin (cdar lst)) (list 'if (caar lst) (cons 'begin (cdar lst)) (expand-cond (cdr lst)))))) (expand-cond list-of-forms))"
			// +
			// "(defmacro and list-of-forms (if (null? list-of-forms) #t (if (null? (cdr list-of-forms)) (car list-of-forms) (list 'if (car list-of-forms) (append '(and) (cdr list-of-forms)) #f))))"
			// +
			// "(defmacro delay (expression) (list 'let '((##forced_value (quote ##not_forced_yet))) (list 'lambda '() (list 'if '(eq? ##forced_value (quote ##not_forced_yet)) (list 'set! '##forced_value expression)) '##forced_value)))"
			// + "(define (force promise) (promise))"
			// +
			// "(define (min . args) (define (min-of-two a b) (if (< a b) a b)) (let ((l (length args))) (cond ((= 0 l) (error \"min called without parameters\")) ((= 1 l) (car args)) (else (fold min-of-two (car args) (cdr args))))))"
			// +
			// "(define (max . args) (define (max-of-two a b) (if (> a b) a b)) (let ((l (length args))) (cond ((= 0 l) (error \"max called without parameters\")) ((= 1 l) (car args)) (else (fold max-of-two (car args) (cdr args))))))"
			// +
			// "(define (find-tail f lst) (cond ((null? lst) #f) ((f (car lst)) lst) (else (find-tail f (cdr lst)))))"
			// +
			// "(define (find f lst) (cond ((null? lst) #f) ((f (car lst)) (car lst)) (else (find f (cdr lst)))))"
			// +
			// "(define (drop-while f lst) (cond ((null? lst) '()) ((f (car lst)) (drop-while f (cdr lst))) (else lst)))"
			// +
			// "(define (take-while f lst) (define (iter l acc) (cond ((null? l) acc) ((f (car l)) (iter (cdr l) (cons (car l) acc))) (else acc))) (reverse (iter lst '())))"
			// +
			// "(define (take lst i) (define (iter l totake acc) (cond ((null? l) acc) ((zero? totake) acc) (else (iter (cdr l) (- totake 1) (cons (car l) acc))))) (reverse (iter lst i '())))"
			// + "(define drop list-tail)"
			// +
			// "(define (last-pair lst) (if (null? (cdr lst)) lst (last-pair (cdr lst))))"
			// + "(define (last lst) (car (last-pair lst)))"
			// +
			// "(define (dotted-list? lst) (if (null? lst) #f (if (pair? lst) (dotted-list? (cdr lst)) #t)))"
			// +
			// "(define (make-proper-list lst) (define (iter i acc) (cond ((pair? i) (iter (cdr i) (cons (car i) acc))) ((null? i) acc) (else (cons i acc)))) (reverse (iter lst '())))"
			// +
			// "(defmacro when (expr . body) `(if ,expr ,(cons 'begin body) #f))"
			// +
			// "(defmacro unless (expr . body) `(if ,expr #f ,(cons 'begin body)))"
			// +
			// "(defmacro aif (expr then . rest) `(let ((it ,expr)) (if it ,then ,(if (null? rest) #f (car rest)))))"
			// +
			// "(defmacro awhen (expr . then) `(let ((it ,expr)) (if it ,(cons 'begin then) #f)))"
			// +
			// "(defmacro or args (if (null? (cdr args)) (car args) (list 'aif (car args) 'it (cons 'or (cdr args)))))"
			// +
			// "(define (sys:count upto f) (define (iter i) (if (= i upto) 'undefined (begin (f i) (iter (+ i 1))))) (iter 0))"
			// +
			// "(defmacro dotimes (lst . body) (list 'sys:count (cadr lst) (cons 'lambda (cons (list (car lst)) body))))"
			// +
			// "(defmacro dolist (lst . forms) (list 'for-each (cons 'lambda (cons (list (car lst)) forms)) (cadr lst)))"
			// +
			// "(define gensym (let ((sym 0)) (lambda () (set! sym (+ sym 1)) (string->symbol (string-append \"##gensym##\" (number->string sym))))))"
			// +
			// "(defmacro do (vars pred . body) (let ((symbol (gensym))) `(let ((,symbol '())) (set! ,symbol (lambda ,(map car vars) (if ,(car pred) ,(cadr pred) ,(cons 'begin (append body (list (cons symbol (map caddr vars)))))))) ,(cons symbol (map cadr vars))))) "
			// +
			// "(defmacro while (exp . body) (cons 'do (cons '() (cons `((not ,exp) 'undefined) body))))"
			// +
			// "(define (flatten lst) (define (iter i acc) (cond ((null? i) acc) ((pair? (car i)) (iter (cdr i) (iter (car i) acc))) (else (iter (cdr i) (cons (car i) acc))))) (reverse (iter lst '())))"
			// +
			// "(define (print . args) (for-each display (flatten args)) (newline))"
			// +
			// "(define (lb:partial-apply proc . cargs) (lambda args (apply proc (append cargs args))))"
			// +
			// "(define (lb:range from to) (define (iter i acc) (if (> from i) acc (iter (- i 1) (cons i acc)))) (iter to '()))"
			// +
			// "(define (lb:count from to f) (if (< to from) '() (begin (f from) (lb:count (+ 1 from) to f))))"
			// +
			// "(defmacro lb:with-range (var from to . body) (list 'lb:count from to (append (list 'lambda (list var)) body)))"
			// +
			// "(define (lb:split str sep) (define (iter acc cur s) (cond ((string=? s \"\") (reverse (cons cur acc))) ((char=? (string-ref s 0) sep) (iter (cons cur acc) \"\" (substring s 1 (string-length s)))) (else (iter acc (string-append cur (substring s 0 1)) (substring s 1 (string-length s)))))) (iter '() \"\" str))"
			// +
			// "(define (vector-fill! v obj) (lb:with-range i 0 (- (vector-length v) 1) (vector-set! v i obj)) 'unspecified)"
			// +
			// "(define (make-vector . args) (let ((v (sys:make-vector (car args)))) (if (null? (cdr args)) v (begin (vector-fill! v (cadr args)) v))))"
			// +
			// "(define (list->vector lst) (define (iter v i vals) (vector-set! v i (car vals)) (if (zero? i) v (iter v (- i 1) (cdr vals)))) (let ((v (sys:make-vector (length lst)))) (if (zero? (vector-length v)) v (iter v (- (vector-length v) 1) (reverse lst)))))"
			// +
			// "(define (vector->list v) (define (iter i acc) (if (< i 0) acc (iter (- i 1) (cons (vector-ref v i) acc)))) (iter (- (vector-length v) 1) '()))"
			// + "(define (vector . lst) (list->vector lst))"
			// +
			// "(define (lb:clr. object name . params) (let ((closure (lb:clr-method object name))) (if (null? closure) (error \"Method not found:\" name) (apply closure params))))"
			// +
			// "(define (lb:clr-properties obj) (map (lambda (name) (cons name (lb:clr-get obj name))) (lb:clr-property-names obj)))"
			// +
			// "(define (sys:test-assertion name value) (if value 'ok (error 'Assertion 'failed: name)))"
			// +
			// "(defmacro assert (form) `(sys:test-assertion (quote ,form) ,form))"
			//
			// "(let ((original display)) (set! display (lambda args (for-each original args))))"
			// +
			// "(let ((original +)) (set! + (lambda args (fold original 0 args))))"
			// +
			// "(let ((original *)) (set! * (lambda args (fold original 1 args))))"
			// +
			// "(let ((original -)) (set! - (lambda args (if (null? (cdr args)) (original 0 (car args)) (fold (flip original) (car args) (cdr args))))))"
			// +
			// "(let ((original /)) (set! / (lambda args (if (null? (cdr args)) (original 1 (car args)) (fold (flip original) (car args) (cdr args))))))"
			// +
			// "(let ((original string-append)) (set! string-append (lambda args (fold (flip original) \"\" args))))"
			// + "(define partial-apply lb:partial-apply)"
			// + "(define range lb:range)" + "(define count lb:count)"
			// + "(define sleep lb:sleep)" + "(define split lb:split)"
			// + "(define clr-property-names lb:clr-property-names)"
			// + "(define clr-method-names lb:clr-method-names)"
			// + "(define clr-properties lb:clr-properties)"
			// + "(define clr-get lb:clr-get)" + "(define clr-set lb:clr-set)"
			// + "(define clr. lb:clr.)"
			+ "(define (id x) x)";

	@Override
	public SchemeObject eval(String commands) throws SchemeException {
		Reader r = new Reader(new StringReader(commands));
		SchemeObject ret = Symbol.fromString("undefined");
		while (true) {
			try {
				ret = eval(r.read());
			} catch (EOFException ex) {
				return ret;
			} catch (IOException ex) {
				return ret;
			}
		}
	}

	@Override
	public SchemeObject eval(SchemeObject o) throws SchemeException {
		return eval(o, _global);
	}

	@Override
	public abstract SchemeObject eval(SchemeObject o, Environment env)
			throws SchemeException;
}
