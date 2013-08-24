(define (factors n)
  (filter (lambda (i) (zero? (remainder n i)))
          (range 1 n)))

(define (prime? n)
  (= 2 (length (factors n))))

(display (filter prime? (range 1 10000)))

