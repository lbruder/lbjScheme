;;;; -------------------------------------------------------------------
;;;; Helper procedures (the "Model")

(define (string-reverse str)
  (list->string
    (reverse
      (string->list str))))

;;;; -------------------------------------------------------------------
;;;; Event handlers (the "Controller")

(define (handle-text-change)
  (gui::set-text
    output
    (string-reverse
      (gui::get-text input))))

(define (handle-close-button)
  (gui::close form 'ok))

;;;; -------------------------------------------------------------------
;;;; The actual GUI definition (the "View")

(define output
  (gui::label))

(define input
  (gui::entry
    :on-change handle-text-change))

(define close-button
  (gui::button
    :text "Close"
    :on-click handle-close-button))

(define form
  (gui::window
    "I'm a string reverser!"
    (gui::hbox
      (gui::vbox input output)
      close-button)))

(if (eq? 'ok (gui::show-modal form))
    (display
      (string-append "Your last input was: "
      (gui::get-text input))))

