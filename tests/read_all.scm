(define (read-all)
  (let ((value (read)))
    (if (eof-object? value)
        '()
        (cons value (read-all)))))

(define env
  (interaction-environment))

(define (exec expr)
  (eval expr env))

(define (writeln x)
  (write x)
  (newline))

(for-each
  writeln
;  exec
  (with-input-from-file
    "primes.scm"
    read-all))

