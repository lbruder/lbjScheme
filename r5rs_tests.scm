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

(test "4.2.6" `(( foo ,(- 10 3)) ,@(cdr '(c)) . ,(car '(cons))) '((foo 7) . cons))

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
; 6.2.1

(test "6.2.1" (integer? 3) #t)
(test "6.2.1" (rational? 3) #t)
(test "6.2.1" (real? 3) #t)
(test "6.2.1" (complex? 3) #t)
(test "6.2.1" (number? 3) #t)

; ------------------------------------------------------------------------------
; 6.2.3

; TODO Page 20f

; ------------------------------------------------------------------------------
; 6.2.4

; TODO Page 21

; ------------------------------------------------------------------------------
; 6.2.5

(test "6.2.5" (complex? 3+4i) #t)
(test "6.2.5" (complex? 3) #t)
(test "6.2.5" (real? 3) #t)
(test "6.2.5" (real? -2.5+0.0i) #t)
(test "6.2.5" (real? #e1e10) #t)
(test "6.2.5" (rational? 6/10) #t)
(test "6.2.5" (rational? 6/3) #t)
(test "6.2.5" (integer? 3+0i) #t)
(test "6.2.5" (integer? 3.0) #t)
(test "6.2.5" (integer? 8/4) #t)

(test "6.2.5" (max 3 4) 4)
(test "6.2.5" (max 3.9 4) 4.0)
(test "6.2.5" (exact? (max 3 4)) #t)
(test "6.2.5" (inexact? (max 3.9 4)) #t)
(test "6.2.5" (+ 3 4) 7)
(test "6.2.5" (+ 3) 3)
(test "6.2.5" (+) 0)
(test "6.2.5" (* 4) 4)
(test "6.2.5" (*) 1)
(test "6.2.5" (- 3 4) -1)
(test "6.2.5" (- 3 4 5) -6)
(test "6.2.5" (- 3) -3)
(test "6.2.5" (/ 3 4 5) 3/20)
(test "6.2.5" (/ 3) 1/3)
(test "6.2.5" (abs -7) 7)
(test "6.2.5" (modulo 13 4) 1)
(test "6.2.5" (remainder 13 4) 1)
(test "6.2.5" (modulo -13 4) 3)
(test "6.2.5" (remainder -13 4) -1)
(test "6.2.5" (modulo 13 -4) -3)
(test "6.2.5" (remainder 13 -4) 1)
(test "6.2.5" (modulo -13 -4) -1)
(test "6.2.5" (remainder -13 -4) -1)
(test "6.2.5" (remainder -13 -4.0) -1.0)
(test "6.2.5" (inexact? (remainder -13 -4.0)) #t)

(test "6.2.5" (gcd 32 -36) 4)
(test "6.2.5" (gcd) 0)
(test "6.2.5" (lcm 32 -36) 288)
(test "6.2.5" (lcm 32.0 -36) 288.0)
(test "6.2.5" (inexact? (lcm 32.0 -36)) #t)
(test "6.2.5" (lcm) 1)
(test "6.2.5" (denominator 0) 1)
(test "6.2.5" (numerator (/ 6 4)) 3)
(test "6.2.5" (denominator (/ 6 4)) 2)
(test "6.2.5" (denominator (exact->inexact (/ 6 4))) 2.0)
(test "6.2.5" (floor -4.3) -5.0)
(test "6.2.5" (ceiling -4.3) -4.0)
(test "6.2.5" (truncate -4.3) -4.0)
(test "6.2.5" (round -4.3) -4.0)
(test "6.2.5" (floor 3.5) 3.0)
(test "6.2.5" (ceiling 3.5) 4.0)
(test "6.2.5" (truncate 3.5) 3.0)
(test "6.2.5" (round 3.5) 4.0)
(test "6.2.5" (round 7/2) 4)
(test "6.2.5" (round 7) 7)

(test "6.2.5" (rationalize (inexact->exact .3) 1/10) 1/3)
(test "6.2.5" (rationalize .3 1/10) #i1/3)
(test "6.2.5" (inexact? (rationalize .3 1/10)) #t)

; ------------------------------------------------------------------------------
; 6.2.5

(test "6.2.6" (string->number "100") 100)
(test "6.2.6" (string->number "100" 16) 256)
(test "6.2.6" (string->number "1e2") 100.0)
; TODO: Test fails (test "6.2.6" (string->number "15##") 1500.0)

; ------------------------------------------------------------------------------
; 6.3.1

(test "6.3.1" (not #t) #f)
(test "6.3.1" (not 3) #f)
(test "6.3.1" (not (list 3)) #f)
(test "6.3.1" (not #f) #t)
(test "6.3.1" (not '()) #f)
(test "6.3.1" (not (list)) #f)
(test "6.3.1" (not 'nil) #f)
(test "6.3.1" (boolean? #f) #t)
(test "6.3.1" (boolean? 0) #f)
(test "6.3.1" (boolean? '()) #f)

; ------------------------------------------------------------------------------
; 6.3.2

(define x (list 'a 'b 'c))
(define y x)
(test "6.3.2" y '(a b c))
(test "6.3.2" (list? y) #t)
(set-cdr! x 4)
(test "6.3.2" x '(a . 4))
(test "6.3.2" (eqv? x y) #t)
(test "6.3.2" y '(a . 4))
(test "6.3.2" (list? y) #f)
(set-cdr! x x)
(test "6.3.2" (list? x) #f)

(test "6.3.2" (pair? '(a . b)) #t)
(test "6.3.2" (pair? '(a b c)) #t)
(test "6.3.2" (pair? '()) #f)
(test "6.3.2" (pair? '#(a b)) #f)

(test "6.3.2" (cons 'a '()) '(a))
(test "6.3.2" (cons '(a) '(b c d)) '((a) b c d))
(test "6.3.2" (cons "a" '(b c)) '("a" b c))
(test "6.3.2" (cons 'a 3) '(a . 3))
(test "6.3.2" (cons '(a b) 'c) '((a b) . c))

(test "6.3.2" (car '(a b c)) 'a)
(test "6.3.2" (car '((a) b c d)) '(a))
(test "6.3.2" (car '(1 . 2)) 1)

(test "6.3.2" (cdr '((a) b c d)) '(b c d))
(test "6.3.2" (cdr '(1 . 2)) 2)

(test "6.3.2" (list? '(a b c)) #t)
(test "6.3.2" (list? '()) #t)
(test "6.3.2" (list? '(a . b)) #f)
(test "6.3.2" (let ((x (list 'a))) (set-cdr! x x) (list? x)) #f)

(test "6.3.2" (list 'a (+ 3 4) 'c) '(a 7 c))
(test "6.3.2" (list) '())

(test "6.3.2" (length '(a b c)) 3)
(test "6.3.2" (length '(a (b) (c d e))) 3)
(test "6.3.2" (length '()) 0)

(test "6.3.2" (append '(x) '(y)) '(x y))
(test "6.3.2" (append '(a) '(b c d)) '(a b c d))
(test "6.3.2" (append '(a (b)) '((c))) '(a (b) (c)))
(test "6.3.2" (append '(a b) '(c . d)) '(a b c . d))
(test "6.3.2" (append '() 'a) 'a)

(test "6.3.2" (reverse '(a b c)) '(c b a))
(test "6.3.2" (reverse '(a (b c) d (e (f)))) '((e (f)) d (b c) a))

(test "6.3.2" (list-ref '(a b c d) 2) 'c)
(test "6.3.2" (list-ref '(a b c d) (inexact->exact (round 1.8))) 'c)

(test "6.3.2" (memq 'a '(a b c)) '(a b c))
(test "6.3.2" (memq 'b '(a b c)) '(b c))
(test "6.3.2" (memq 'a '(b c d)) #f)
(test "6.3.2" (memq (list 'a) '(b (a) c)) #f)
(test "6.3.2" (member (list 'a) '(b (a) c)) '((a) c))
(test "6.3.2" (memv 101 '(100 101 102)) '(101 102))

(define e '((a 1) (b 2) (c 3)))
(test "6.3.2" (assq 'a e) '(a 1))
(test "6.3.2" (assq 'b e) '(b 2))
(test "6.3.2" (assq 'd e) #f)
(test "6.3.2" (assq (list 'a) '(((a)) ((b)) ((c)))) #f)
(test "6.3.2" (assoc (list 'a) '(((a)) ((b)) ((c)))) '((a)))
(test "6.3.2" (assv 5 '((2 3) (5 7) (11 13))) '(5 7))

; ------------------------------------------------------------------------------
; 6.3.3

(test "6.3.3" (symbol? 'foo) #t)
(test "6.3.3" (symbol? (car '(a b))) #t)
(test "6.3.3" (symbol? "bar") #f)
(test "6.3.3" (symbol? 'nil) #t)
(test "6.3.3" (symbol? '()) #f)
(test "6.3.3" (symbol? #f) #f)

(test "6.3.3" (symbol->string 'flying-fish) "flying-fish")
; TODO: No case conversion (test "6.3.3" (symbol->string 'Martin) "Martin")
(test "6.3.3" (symbol->string (string->symbol "Malvina")) "Malvina")
; TODO: No case conversion (test "6.3.3" (eq? 'mISSISSIppi 'mississippi) #t)
; TODO: Test fails (test "6.3.3" (eq? 'bitBlt (string->symbol "bitBlt")) #f)
(test "6.3.3" (eq? 'JollyWog (string->symbol (symbol->string 'JollyWog))) #t)
(test "6.3.3" (string=? "K. Harper, M.D." (symbol->string (string->symbol "K. Harper, M.D."))) #t)

; ------------------------------------------------------------------------------
; 6.3.4

(test "6.3.4" (char<? #\A #\B) #t)
(test "6.3.4" (char<? #\a #\b) #t)
(test "6.3.4" (char<? #\0 #\9) #t)
(test "6.3.4" (char-ci=? #\A #\a) #t)

; ------------------------------------------------------------------------------
; 6.3.5

; No direct examples here, providing a few of my own

(define a "test string")
(test "6.3.5" (string? a) #t)
(test "6.3.5" (string-length a) 11)
(test "6.3.5" (string=? a (string #\t #\e #\s #\t #\space #\s #\t #\r #\i #\n #\g)) #t)
(test "6.3.5" (string-ref a 3) #\t)
(string-set! a 3 #\x)
(test "6.3.5" (string-ref a 3) #\x)
(test "6.3.5" (string=? (substring a 6 9) "tri") #t)

(define b (make-string 3 #\*))
(test "6.3.5" (string=? b "***") #t)
(test "6.3.5" (string-append a b) "tesx string***")

(define c "banana")
(define d (string-copy c))
(test "6.3.5" (string=? c d) #t)
(test "6.3.5" (eq? c d) #f)
(string-set! c 3 #\space)
(test "6.3.5" (string=? c d) #f)

; ------------------------------------------------------------------------------
; 6.3.6

(define v (vector 'a 'b 'c))
(test "6.3.6" v '#(a b c))
(test "6.3.6" (vector-length v) 3)
(test "6.3.6" (vector-ref v 2) 'c)
(test "6.3.6" (vector-ref '#(1 1 2 3 5 8 13 21) 5) 8)
(test "6.3.6" (vector-ref '#(1 1 2 3 5 8 13 21) (let ((i (round (* 2 (acos -1))))) (if (inexact? i) (inexact->exact i) i))) 13)

(test "6.3.6" (let ((vec (vector 0 '(2 2 2 2) "Anna")))
                (vector-set! vec 1 '("Sue" "Sue"))
                vec)
              '#(0 ("Sue" "Sue") "Anna"))

(test "6.3.6" (vector->list '#(dah dah didah)) '(dah dah didah))
(test "6.3.6" (list->vector '(dah dah didah)) '#(dah dah didah))

; ------------------------------------------------------------------------------
; 6.4

(test "6.4" (procedure? car) #t)
(test "6.4" (procedure? 'car) #f)
(test "6.4" (procedure? (lambda (x) (* x x))) #t)
(test "6.4" (procedure? '(lambda (x) (* x x))) #f)
; TODO: Compiling evaluator only (test "6.4" (call-with-current-continuation procedure?) #t)

(test "6.4" (apply + (list 3 4)) 7)
(define compose (lambda (f g) (lambda args (f (apply g args)))))
(test "6.4" ((compose sqrt *) 12 75) 30)

(test "6.4" (map cadr '((a b) (d e) (g h))) '(b e h))
(test "6.4" (map (lambda (n) (expt n n)) '(1 2 3 4 5)) '(1 4 27 256 3125))
; TODO: Fix map (test "6.4" (map + '(1 2 3) '(4 5 6)) '(5 7 9))

(define foo (let ((count 0)) (map (lambda (ignored) (set! count (+ count 1)) count) '(a b))))
(test "6.4" (or (equal? foo '(1 2)) (equal? foo '(2 1))) #t)

(test "6.4" (let ((v (make-vector 5)))
              (for-each (lambda (i)
                          (vector-set! v i (* i i)))
                        '(0 1 2 3 4))
              v)
            '#(0 1 4 9 16))

(test "6.4" (force (delay (+ 1 2))) 3)
(test "6.4" (let ((p (delay (+ 1 2)))) (list (force p) (force p))) '(3 3))

(define a-stream
  (letrec ((next
             (lambda (n)
               (cons n (delay (next (+ n 1)))))))
    (next 0)))               
(define head car)
(define tail
  (lambda (stream) (force (cdr stream))))

(test "6.4" (head (tail (tail a-stream))) 2)

(define count 0)
(define p
  (delay (begin (set! count (+ count 1))
                (if (> count x)
                    count
                    (force p)))))
(define x 5)
(test "6.4" (force p) 6)
(test "6.4" (begin (set! x 10) (force p)) 6)

; TODO: No continuations yet
; (test "6.4" (call-with-current-continuation
;               (lambda (exit)
;                 (for-each (lambda (x)
;                             (if (negative? x)
;                                 (exit x)))
;                           '(54 0 37 -3 245 19))
;                 #t))
;             -3)

(define list-length
  (lambda (obj)
    (call-with-current-continuation
      (lambda (return)
        (letrec ((r
                  (lambda (obj)
                    (cond ((null? obj) 0)
                          ((pair? obj)
                           (+ (r (cdr obj)) 1))
                          (else (return #f))))))
          (r obj))))))

; TODO: No continuations yet (test "6.4" (list-length '(1 2 3 4)) 4)
; TODO: No continuations yet (test "6.4" (list-length '(a b . c)) #f)

(test "6.4" (call-with-values (lambda () (values 4 5)) (lambda (a b) b)) 5)
(test "6.4" (call-with-values * -) -1)

; TODO: dynamic-wind not implemented yet
; (test "6.4" (let ((path '())
;                   (c #f))
;               (let ((add (lambda (s)
;                            (set! path (cons s path)))))
;                 (dynamic-wind
;                   (lambda () (add 'connect))
;                   (lambda ()
;                     (add (call-with-current-continuation
;                            (lambda (c0)
;                              (set! c c0)
;                              'talk1))))
;                   (lambda () (add 'disconnect)))
;                     (if (< (length path) 4)
;                         (c 'talk2)
;                         (reverse path))))
;             '(connect talk1 disconnect connect talk2 disconnect))

; ------------------------------------------------------------------------------
; 6.5

(test "6.5" (eval '(* 7 3) (scheme-report-environment 5)) 21)
(test "6.5" (let ((f (eval '(lambda (f x) (f x x)) (null-environment 5)))) (f + 10)) 20)

; TODO: When executing a file or REPL script, make sure all parens are matched. Stop on first error!

