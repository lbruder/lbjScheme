; vim: lisp:et:ai

; Additional tests for lbjScheme's extensions

(define (perform-test name f)
  (if (f)
      name
      (error "Test failed: " name)))

(defmacro test (name assertion)
  `(perform-test ,name (lambda () ,assertion)))

; ------------------------------------------------------------------------------
; sys:get-method-names

(test "sys:get-method-names"
      (member "reverseString" (sys:get-method-names test-object)))

; ------------------------------------------------------------------------------
; sys:call

(test "sys:call, 1 integer parameter, void return"
      (sys:call test-object "setValue" 42))

(test "sys:call, no parameters"
      (= 42 (sys:call test-object "getValue")))

(test "sys:call, 1 string parameter"
      (equal? "dsa" (sys:call test-object "reverseString" "asd")))

(test "sys:call, 1 integer parameter, returns list of ints"
      (equal? '(2 3 5 7 11 13 17 19) (sys:call test-object "primesUpTo" 20)))

