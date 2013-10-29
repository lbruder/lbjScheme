; vim: lisp:et:ai

; Just a small test suite that contains all (?) examples from R5RS
; to make sure lbjScheme behaves as stated in the standard

(define (perform-test name f)
  (if (f)
      name
      (error "Test failed: " name)))

(defmacro test (r5rs-section expression expected-value)
  `(perform-test ,r5rs-section
                 (lambda () (equal? ,expression ,expected-value))))

; ------------------------------------------------------------------------------
; Make sure all symbols as defined in the standard do exist
; List of symbols copied from the Scheme page on Wikipedia

(define (dynamic-wind . args) (error "TODO: dynamic-wind not implemented yet"))

append apply assoc assq assv boolean? call/cc
call-with-current-continuation call-with-input-file call-with-output-file
call-with-values car cdr char<=? char<? char=? char>=? char>? char?
char-alphabetic? char-ci<=? char-ci<? char-ci=? char-ci>=? char-ci>?
char-downcase char->integer char-lower-case? char-numeric? char-ready?
char-upcase char-upper-case? char-whitespace? close-input-port
close-output-port cons current-input-port current-output-port display
dynamic-wind eof-object? eq? equal? eqv? eval for-each input-port?
integer->char interaction-environment length list list? list-ref
list->string list-tail list->vector make-string make-vector map member
memq memv newline not null? null-environment number? number->string
open-input-file open-output-file output-port? pair? peek-char port?
procedure? read read-char reverse scheme-report-environment set-car!
set-cdr! string string<=? string<? string=? string>=? string>?
string? string-append string-ci<=? string-ci<? string-ci=?
string-ci>=? string-ci>? string-copy string-fill! string-length
string->list string->number string-ref string-set! string->symbol
substring symbol? symbol->string values vector vector? vector-fill!
vector-length vector->list vector-ref vector-set! with-input-from-file
with-output-to-file write write-char

+ - * / abs quotient remainder modulo gcd lcm expt sqrt numerator denominator
rational? rationalize floor ceiling truncate round inexact->exact exact->inexact
exact? inexact? < <= > >= = zero? negative? positive? odd? even? max min sin cos
tan asin acos atan exp log make-rectangular make-polar real-part imag-part
magnitude angle complex? number->string string->number integer? rational? real?
complex? number?

; ------------------------------------------------------------------------------
; 1.3.4

(test "1.3.4" (* 5 8) 40)

; ------------------------------------------------------------------------------
; 2.2

;;; The FACT procedure computes the factorial
;;; of a non-negative integer.
(define fact
  (lambda (n)
    (if (= n 0)
        1        ;Base case: return 1
        (* n (fact (- n 1))))))

; ------------------------------------------------------------------------------
; 3.2

(define (test-disjointness x)
  (test "3.2"
        (length (filter (lambda (pred) (pred x))
                        (list boolean?
                              symbol?
                              char?
                              vector?
                              procedure?
                              pair?
                              number?
                              string?
                              null? ; Not in R5RS, but makes sense
                              port?)))
        1))

(for-each test-disjointness
          (list 1
                1.0
                1234567890123456789012345678901234567890
                42+2.34i
                47/23
                -3-12/3i
                'foo
                "foo"
                #t
                #f
                '()
                '(1 2 3)
                '#(1 2 3)
                car
                (lambda (x) x)
                (cons 1 2)
                (delay (+ 1 2))))

; TODO: All values count as true in [...] a test except for #f.

; ------------------------------------------------------------------------------
; 3.3

(test "3.3" 28 28)
(test "3.3" 28 #x1c)
(test "3.3" '(8 13) '( 08 13 ))
(test "3.3" '(8 13) '(8 . (13 . ())))

; ------------------------------------------------------------------------------
; 4.1.1

(define x 28)
(test "4.1.1" x 28)

; ------------------------------------------------------------------------------
; 4.1.2

(test "4.1.2" (quote a) 'a)
(test "4.1.2" (quote #(a b c)) '#(a b c))
(test "4.1.2" (quote (+ 1 2)) '(+ 1 2))
(test "4.1.2" 'a 'a)
(test "4.1.2" '#(a b c) '#(a b c))
(test "4.1.2" '() '())
(test "4.1.2" '(+ 1 2) '(+ 1 2))
(test "4.1.2" '(quote a) '(quote a))
(test "4.1.2" ''a '(quote a))
(test "4.1.2" '"abc" "abc")
(test "4.1.2" "abc" "abc")
(test "4.1.2" '145932 145932)
(test "4.1.2" 145932 145932)
(test "4.1.2" '#t #t)
(test "4.1.2" #t #t)

; ------------------------------------------------------------------------------
; 4.1.3

(test "4.1.3" (+ 3 4) 7)
(test "4.1.3" ((if #f + *) 3 4) 12)

; ------------------------------------------------------------------------------
; 4.1.4

(test "4.1.4" (procedure? (lambda (x) (+ x x))) #t)
(test "4.1.4" ((lambda (x) (+ x x)) 4) 8)

(define reverse-subtract (lambda (x y) (- y x)))
(test "4.1.4" (reverse-subtract 7 10) 3)

(define add4 (let ((x 4)) (lambda (y) (+ x y))))
(test "4.1.4" (add4 6) 10)

(test "4.1.4" ((lambda x x) 3 4 5 6) '(3 4 5 6))
(test "4.1.4" ((lambda (x y . z) z) 3 4 5 6) '(5 6))

; ------------------------------------------------------------------------------
; 4.1.5

(test "4.1.5" (if (> 3 2) 'yes 'no) 'yes)
(test "4.1.5" (if (> 2 3) 'yes 'no) 'no)
(test "4.1.5" (if (> 3 2) (- 3 2) (+ 3 2)) 1)

; ------------------------------------------------------------------------------
; 4.1.6

(define x 2)
(test "4.1.6" (+ x 1) 3)
(set! x 4)
(test "4.1.6" (+ x 1) 5)

; ------------------------------------------------------------------------------
; 4.2.1

(test "4.2.1" (cond ((> 3 2) 'greater) ((< 3 2) 'less)) 'greater)
(test "4.2.1" (cond ((> 3 3) 'greater) ((< 3 3) 'less) (else 'equal)) 'equal)
; TODO: (test "4.2.1" (cond ((assv 'b '((a 1) (b 2))) => cadr) (else #f)) 2)

(test "4.2.1"
      (case (* 2 3) ((2 3 5 7) 'prime)
        ((1 4 6 8 9) 'composite))
      'composite)

(test "4.2.1"
      (case (car '(c d)) ((a e i o u) 'vowel)
        ((w y) 'semivowel)
        (else 'consonant))
      'consonant)

(test "4.2.1" (and (= 2 2) (> 2 1)) #t)
(test "4.2.1" (and (= 2 2) (< 2 1)) #f)
(test "4.2.1" (and 1 2 'c '(f g)) '(f g))
(test "4.2.1" (and) #t)

(test "4.2.1" (or (= 2 2) (> 2 1)) #t)
(test "4.2.1" (or (= 2 2) (< 2 1)) #t)
(test "4.2.1" (or #f #f #f) #f)
(test "4.2.1" (or (memq 'b '(a b c)) (/ 3 0)) '(b c))

; ------------------------------------------------------------------------------
; 4.2.2

(test "4.2.2" (let ((x 2) (y 3)) (* x y)) 6)
(test "4.2.2" (let ((x 2) (y 3)) (let ((x 7) (z (+ x y))) (* z x))) 35)
(test "4.2.2" (let ((x 2) (y 3)) (let* ((x 7) (z (+ x y))) (* z x))) 70)
(test "4.2.2"
      (letrec ((even?
                 (lambda (n)
                   (if (zero? n)
                       #t
                       (odd? (- n 1)))))
               (odd?
                 (lambda (n)
                   (if (zero? n)
                       #f
                       (even? (- n 1))))))
        (even? 88))
      #t)

; ------------------------------------------------------------------------------
; 4.2.3

(define x 0)
(test "4.2.3" (begin (set! x 5) (+ x 1)) 6)

; ------------------------------------------------------------------------------
; 4.2.4

(test "4.2.4"
      (do ((vec (make-vector 5) vec) ; TODO: Must work without the second "vec" too
           (i 0 (+ i 1)))
        ((= i 5) vec)
        (vector-set! vec i i))
      '#(0 1 2 3 4))

(test "4.2.4"
      (let ((x '(1 3 5 7 9)))
        (do ((x x (cdr x))
             (sum 0 (+ sum (car x))))
          ((null? x) sum)))
      25)

(test "4.2.4"
      (let loop ((numbers '(3 -2 1 6 -5))
                 (nonneg '())
                 (neg '()))
        (cond ((null? numbers) (list nonneg neg))
              ((>= (car numbers) 0)
               (loop (cdr numbers)
                     (cons (car numbers) nonneg)
                     neg))
              ((< (car numbers) 0)
               (loop (cdr numbers)
                     nonneg
                     (cons (car numbers) neg)))))
      '((6 1 3) (-5 -2)))

; ------------------------------------------------------------------------------
; 4.2.6

(test "4.2.6" `(list ,(+ 1 2) 4) '(list 3 4))
(test "4.2.6" (let ((name 'a)) `(list ,name ',name)) '(list a (quote a)))
(test "4.2.6" `(a ,(+ 1 2) ,@(map abs '(4 -5 6)) b) '(a 3 4 5 6 b))

; TODO: Test fails
; (test "4.2.6" `(( foo ,(- 10 3)) ,@(cdr '(c)) . ,(car '(cons))) '((foo 7) . cons))

; TODO: Test fails
; (test "4.2.6" `#(10 5 ,(sqrt 4) ,@(map sqrt '(16 9)) 8) '#(10 5 2 4 3 8))

; TODO: Test fails
; (test "4.2.6"
;       `(a `(b ,(+ 1 2) ,(foo ,(+ 1 3) d) e) f)
;       '(a `(b ,(+ 1 2) ,(foo 4 d) e) f))

; TODO: Test fails
; (test "4.2.6"
;       (let ((name1 'x)
;             (name2 'y))
;         `(a `(b ,,name1 ,',name2 d) e))
;       '(a `(b ,x ,'y d) e))

; ------------------------------------------------------------------------------
; 5.2.1

(define add3 (lambda (x) (+ x 3)))
(test "5.2.1" (add3 3) 6)
(define first car)
(test "5.2.1" (first '(1 2)) 1)

; ------------------------------------------------------------------------------
; 5.2.2

(test "5.2.2"
      (let ((x 5))
        (define foo (lambda (y) (bar x y)))
        (define bar (lambda (a b) (+ (* a b) a)))
        (foo (+ x 3)))
      45)

(test "5.2.2"
      (let ((x 5))
        (letrec ((foo (lambda (y) (bar x y)))
                 (bar (lambda (a b) (+ (* a b) a))))
          (foo (+ x 3))))
      45)

; ------------------------------------------------------------------------------
; 6.1

(test "6.1" (eqv? 'a 'a) #t)
(test "6.1" (eqv? 'a 'b) #f)
(test "6.1" (eqv? 2 2) #t)
(test "6.1" (eqv? '() '()) #t)
(test "6.1" (eqv? 100000000 100000000) #t)
(test "6.1" (eqv? (cons 1 2) (cons 1 2)) #f)
(test "6.1" (eqv? (lambda () 1) (lambda () 2)) #f)
(test "6.1" (eqv? #f 'nil) #f)
(test "6.1" (let ((p (lambda (x) x))) (eqv? p p)) #t)

(define gen-counter
  (lambda ()
    (let ((n 0))
      (lambda () (set! n (+ n 1)) n))))

(test "6.1" (let ((g (gen-counter))) (eqv? g g)) #t)
(test "6.1" (eqv? (gen-counter) (gen-counter)) #f)

(define gen-loser
  (lambda ()
    (let ((n 0))
      (lambda () (set! n (+ n 1)) 27))))

(test "6.1" (let ((g (gen-loser))) (eqv? g g)) #t)

(test "6.1"
      (letrec ((f (lambda () (if (eqv? f g) 'f 'both)))
               (g (lambda () (if (eqv? f g) 'g 'both))))
        (eqv? f g))
      #f)

(test "6.1" (let ((x '(a))) (eqv? x x)) #t)

(test "6.1" (eq? 'a 'a) #t)
(test "6.1" (eq? (list 'a) (list 'a)) #f)
(test "6.1" (eq? '() '()) #t)
(test "6.1" (eq? car car) #t)
(test "6.1" (let ((x '(a))) (eq? x x)) #t)
(test "6.1" (let ((x '#())) (eq? x x)) #t)
(test "6.1" (let ((p (lambda (x) x))) (eq? p p)) #t)

(test "6.1" (equal? 'a 'a) #t)
(test "6.1" (equal? '(a) '(a)) #t)
(test "6.1" (equal? '(a (b) c) '(a (b) c)) #t)
(test "6.1" (equal? "abc" "abc") #t)
(test "6.1" (equal? 2 2) #t)
(test "6.1" (equal? (make-vector 5 'a) (make-vector 5 'a)) #t)

; ------------------------------------------------------------------------------
; 6.2

; TODO: Page 19ff

