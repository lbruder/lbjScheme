// lbjScheme
// An experimental Scheme subset interpreter in Java, based on SchemeNet.cs
// Copyright (c) 2013, Leif Bruder <leifbruder@gmail.com>
//
// Permission to use, copy, modify, and/or distribute this software for any
// purpose with or without fee is hereby granted, provided that the above
// copyright notice and this permission notice appear in all copies.
//
// THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
// WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
// ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
// WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
// ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
// OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.

package org.lb.lbjscheme;

import java.io.*;

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
		for (Builtin.Type t : Builtin.Type.values())
			addBuiltin(new Builtin(t));

		// TODO: integer? real? sin cos tan sqrt expt display random

		// TODO String procedures:
		// AddFunction("string->symbol", (string s) => Symbol.FromString(s));
		// AddFunction("symbol->string", (Symbol s) => s.ToString());

		// TODO vector procedures:
		// AddFunction("sys:make-vector", (int size) => new object[size]);
		// AddFunction("vector-ref", (object[] vector, int index) =>
		// vector[index]);
		// AddFunction("vector-length", (object[] vector) => vector.Length);
		// AddFunction("vector-set!", (object[] vector, int index, object obj)
		// => { vector[index] = obj; return undefinedSymbol; });
		// AddFunction("vector?", (object o) => o is object[]);

		// TODO other procedures...
		// AddFunction("sys:strtonum", (string s, int b) => s.Contains('.') ?
		// Convert.ToDouble(s, CultureInfo.InvariantCulture) :
		// Convert.ToInt32(s, b));
		// AddFunction("sys:numtostr", (object i, int b) => (i is int) ?
		// Convert.ToString((int)i, b) : Convert.ToString((double)i));
		// AddFunction("apply", (object f, IEnumerable<object> arguments) =>
		// arguments == null ? Apply(f, false) : Apply(f, false,
		// arguments.ToArray()));

		// AddFunction<object, object>("eqv?", Eqv);
		// AddFunction<object, object>("equal?", Equal);
		// AddFunction<IEnumerable<object>>("sys:error", ErrorFunction);
	}

	// HACK: Written in Scheme for simplicity. Re-write as builtins for
	// performance and better error messages!
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
			+ "(define (zero? x) (= x 0))"
			+ "(define (positive? x) (> x 0))"
			+ "(define (negative? x) (< x 0))"
			+ "(define (abs x) (if (positive? x) x (- 0 x)))"
			+ "(define (modulo a b) (define (sgn x) (if (>= x 0) 1 -1)) (if (= (sgn a) (sgn b)) (remainder a b) (+ b (remainder a b))))"
			+ "(define (not x) (if x #f #t))"
			+ "(define (fold f acc lst) (if (null? lst) acc (fold f (f (car lst) acc) (cdr lst))))"
			+ "(define (reduce f ridentity lst) (if (null? lst) ridentity (fold f (car lst) (cdr lst))))"
			+ "(define (list-tail lst k) (if (zero? k) lst (list-tail (cdr lst) (- k 1))))"
			+ "(define (list-ref lst k) (car (list-tail lst k)))"
			+ "(define (last-pair lst) (if (null? (cdr lst)) lst (last-pair (cdr lst))))"
			+ "(define (last lst) (car (last-pair lst)))"
			+ "(define (dotted-list? lst) (if (null? lst) #f (if (pair? lst) (dotted-list? (cdr lst)) #t)))"
			+ "(define drop list-tail)"
			+ "(define (reverse lst) (fold cons '() lst))"
			+ "(define (map f lst) (reverse (fold (lambda (i acc) (cons (f i) acc)) '() lst)))"
			+ "(define (for-each f lst) (fold (lambda (i acc) (f i) 'undefined) 'undefined lst))"
			+ "(define (filter f lst) (define (iter l acc) (if (null? l) (reverse acc) (if (f (car l)) (iter (cdr l) (cons (car l) acc)) (iter (cdr l) acc)))) (iter lst '()))"
			+ "(define (even? x) (zero? (remainder x 2)))"
			+ "(define (odd? x) (if (even? x) #f #t))"
			+ "(define (sys:gcd-of-two a b) (if (zero? b) a (sys:gcd-of-two b (remainder a b))))"
			+ "(define (sys:lcm-of-two a b) (/ (* a b) (sys:gcd-of-two a b)))"
			+ "(define (gcd . args) (if (null? args) 0 (abs (fold sys:gcd-of-two (car args) (cdr args)))))"
			+ "(define (lcm . args) (if (null? args) 1 (abs (fold sys:lcm-of-two (car args) (cdr args)))))"
			+ "(define (range from to) (define (iter t acc) (if (> from t) acc (iter (- t 1) (cons t acc)))) (iter to '()))"
			+ "(define (every f lst) (if (null? lst) #t (if (f (car lst)) (every f (cdr lst)) #f)))"
			+ "(define (any f lst) (if (null? lst) #f (if (f (car lst)) #t (any f (cdr lst)))))"
			+ "(define (length lst) (define (iter l acc) (if (null? l) acc (iter (cdr l) (+ acc 1)))) (iter lst 0))"
			+ "(define (find-tail f lst) (if (null? lst) #f (if (f (car lst)) lst (find-tail f (cdr lst)))))"
			+ "(define (find f lst) (if (null? lst) #f (if (f (car lst)) (car lst) (find f (cdr lst)))))"
			+ "(define (drop-while f lst) (if (null? lst) '() (if (f (car lst)) (drop-while f (cdr lst)) lst)))"
			+ "(define (take-while f lst) (define (iter l acc) (if (null? l) acc (if (f (car l)) (iter (cdr l) (cons (car l) acc)) acc))) (reverse (iter lst '())))"
			+ "(define (take lst i) (define (iter l totake acc) (if (null? l) acc (if (zero? totake) acc (iter (cdr l) (- totake 1) (cons (car l) acc))))) (reverse (iter lst i '())))"
			+ "(define (char=? a b) (= (char->integer a) (char->integer b)))"
			+ "(define (char>? a b) (> (char->integer a) (char->integer b)))"
			+ "(define (char<? a b) (< (char->integer a) (char->integer b)))"
			+ "(define (char>=? a b) (>= (char->integer a) (char->integer b)))"
			+ "(define (char<=? a b) (<= (char->integer a) (char->integer b)))"
			+ "(define (char-upper-case? x) (if (< (char->integer x) 65) #f (< (char->integer x) 91)))"
			+ "(define (char-lower-case? x) (if (< (char->integer x) 97) #f (< (char->integer x) 123)))"
			+ "(define (char-upcase x) (if (char-lower-case? x) (integer->char (- (char->integer x) 32)) x))"
			+ "(define (char-downcase x) (if (char-upper-case? x) (integer->char (+ (char->integer x) 32)) x))"
			+ "(define (char-ci=? a b) (char=? (char-downcase a) (char-downcase b)))"
			+ "(define (char-ci<? a b) (char<? (char-downcase a) (char-downcase b)))"
			+ "(define (char-ci>? a b) (char>? (char-downcase a) (char-downcase b)))"
			+ "(define (char-ci<=? a b) (char<=? (char-downcase a) (char-downcase b)))"
			+ "(define (char-ci>=? a b) (char>=? (char-downcase a) (char-downcase b)))"
			+ "(define (char-alphabetic? x) (if (char-upper-case? x) #t (char-lower-case? x)))"
			+ "(define (char-numeric? x) (if (>= (char->integer x) 48) (<= (char->integer x) 57) #f))"
			+ "(define (char-whitespace? x) (if (char=? x #\\space) #t (if (char=? x #\\tab) #t (if (char=? x #\\newline) #t (char=? x #\\cr)))))"
			+ "(define (append . lsts) (define (iter current acc) (if (pair? current) (iter (cdr current) (cons (car current) acc)) acc)) (reverse (fold iter '() lsts)))"
			+ "(define (memq obj lst) (if (pair? lst) (if (eq? obj (car lst)) lst (memq obj (cdr lst))) #f))"
			// "(define (memv obj lst) (if (pair? lst) (if (eqv? obj (car lst)) lst (memv obj (cdr lst))) #f))"
			// "(define (member obj lst) (if (pair? lst) (if (equal? obj (car lst)) lst (member obj (cdr lst))) #f))"
			+ "(define (assq obj lst) (if (pair? lst) (if (eq? obj (caar lst)) (car lst) (assq obj (cdr lst))) #f))"
			// "(define (assv obj lst) (if (pair? lst) (if (eqv? obj (caar lst)) (car lst) (assv obj (cdr lst))) #f))"
			// "(define (assoc obj lst) (if (pair? lst) (if (equal? obj (caar lst)) (car lst) (assoc obj (cdr lst))) #f))"

			// TODO String procedures, write in Scheme for now
			+ "(define (string=? a b) (define (check i max) (if (>= i max) #t (if (char=? (string-ref a i) (string-ref b i)) (check (+ i 1) max) #f))) (if (= (string-length a) (string-length b)) (check 0 (string-length a)) #f))"
			+ "(define (string-ci=? a b) (define (check i max) (if (>= i max) #t (if (char-ci=? (string-ref a i) (string-ref b i)) (check (+ i 1) max) #f))) (if (= (string-length a) (string-length b)) (check 0 (string-length a)) #f))"
			// AddFunction("string>?", (string a, string b) => String.Compare(a,
			// b,
			// false, CultureInfo.InvariantCulture) > 0);
			// AddFunction("string<?", (string a, string b) => String.Compare(a,
			// b,
			// false, CultureInfo.InvariantCulture) < 0);

			// AddFunction("string-ci>?", (string a, string b) =>
			// String.Compare(a,
			// b, true, CultureInfo.InvariantCulture) > 0);
			// AddFunction("string-ci<?", (string a, string b) =>
			// String.Compare(a,
			// b, true, CultureInfo.InvariantCulture) < 0);
			// AddFunction("substring", (string a, int start, int end) =>
			// a.Substring(start, end - start));
			// AddFunction("string-append", (string a, string b) => a + b);
			// AddFunction("string->list", (string s) =>
			// Pair.FromEnumerable(s.ToCharArray().Cast<object>()));
			// AddFunction("list->string", (IEnumerable<object> list) => list ==
			// null ? "" : new string(list.Cast<char>().ToArray()));
			// + "(define (string>=? a b) (if (string<? a b) #f #t))"
			// + "(define (string<=? a b) (if (string>? a b) #f #t))"
			// + "(define (string-ci>=? a b) (if (string-ci<? a b) #f #t))"
			// + "(define (string-ci<=? a b) (if (string-ci>? a b) #f #t))"
			// TODO: string-fill!, string-copy.
			// + "(define (string . values) (list->string values))"

			// TODO
			// + "(define (newline) (display \"\\n\") 'undefined)"
			// + "(define (error . params) (sys:error params))"
			// +
			// "(define (string->number n . rest) (if (pair? rest) (sys:strtonum n (car rest)) (sys:strtonum n 10)))"
			// +
			// "(define (number->string n . rest) (if (pair? rest) (sys:numtostr n (car rest)) (sys:numtostr n 10)))"
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
			// "(define (vector-fill! v obj) (lb:with-range i 0 (- (vector-length v) 1) (vector-set! v i obj)) 'unspecified)"
			// +
			// "(define (list->vector lst) (define (iter v i vals) (vector-set! v i (car vals)) (if (zero? i) v (iter v (- i 1) (cdr vals)))) (let ((v (sys:make-vector (length lst)))) (if (zero? (vector-length v)) v (iter v (- (vector-length v) 1) (reverse lst)))))"
			// +
			// "(define (vector->list v) (define (iter i acc) (if (< i 0) acc (iter (- i 1) (cons (vector-ref v i) acc)))) (iter (- (vector-length v) 1) '()))"
			// + "(define (vector . lst) (list->vector lst))"
			// +
			// "(let ((original string-append)) (set! string-append (lambda args (fold (flip original) \"\" args))))"
			// + "(define partial-apply lb:partial-apply)"
			+ "(define (id x) x)";

	@Override
	public SchemeObject eval(String commands) throws SchemeException {
		final Reader r = new Reader(new StringReader(commands));
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
