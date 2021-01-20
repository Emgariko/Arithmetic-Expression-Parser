; accepted hw10

;***************;
;     HW 10     ;
;***************;

(defn constant [value] (fn [& args] (+ value)))
(defn variable [var] (fn [vars] (get vars var)))
(defn expr-operation [func] (fn [& a]
                              (fn [arg] (apply func
                                               (mapv #(%1 arg) a)))))

(def div #(/ (double %1) (double %2)))
(def negate (expr-operation -))
(def add (expr-operation +))
(def subtract (expr-operation -))
(def divide (expr-operation div))
(def multiply (expr-operation *))
(def avg (expr-operation #(/ (apply + %&) (count %&))))
(def med (expr-operation #(nth (sort %&) (int (/ (count %&) 2)))))

(def tokenToOperation {'+ add '- subtract '* multiply '/ divide 'negate negate 'avg avg 'med med})
(defn parse [mappp var' const']
  (fn [expr] (letfn [(myParse [token] (cond (symbol? token) (var' (str token)) (number? token) (const' token)
                                            :else (apply (mappp (first token)) (mapv myParse (rest token)))
                                            )
                       )]
               (myParse (read-string expr))
               )
    ))

(def parseFunction (parse tokenToOperation variable constant))

;***************;
;     HW 11     ;
;***************;
; review hw 11


(definterface Expression
  (evaluate [vars])
  (toString [])
  (diff [varr]))

(declare ZERO)
(declare ONE)
(comment ":NOTE: merge or remove prototypes for Constant and Variable")
(comment "fixed")

(deftype Elementary-Expression
  [evaluate toString diff elementary]
  Expression
  (evaluate [this var] (evaluate elementary var))
  (toString [this] (toString elementary))
  (diff [this var] (diff elementary var))
  )

(defn build-elementary [ev tostr dff]
  (fn [elem] (Elementary-Expression. ev tostr dff elem)))

(declare ZERO)

(def Constant
  (build-elementary
    (fn [elema elemb] elema)
    (fn [x] (format "%.1f" (double x)))
    (fn [elema elemb] ZERO)))
(def ZERO (Constant 0.0))

(def Variable
  (build-elementary
    (fn [elema elemb] (elemb elema))
    (fn [elema] elema)
    (fn [elema elemb]
      (cond (= elema elemb)
            ONE
            :else ZERO))))
(comment ":NOTE: this is not good that constants defined in different places")
(def ONE (Constant 1.0))

(deftype Operation [operation symbol diffRule operands]
  Expression
  (evaluate [this vars] (apply operation (mapv #(.evaluate %1 vars) (vec operands))))
  (toString [this] (str "(" symbol " " (apply clojure.string/join " " (vector (mapv #(.toString %1) (vec operands)))) ")"))
  (diff [this diffVar] (letfn [(create-common-diff [func] (fn [varr & args] (func (mapv #(.diff % varr) args) args)))]
                         (apply (create-common-diff diffRule) diffVar (vec operands)))))

(comment ":NOTE: if you do nothing with arguments use anonymous functions (args is not necessarry here)")
(defn toString [a] (.toString a))
(defn diff [exp varr] (.diff exp varr))
(defn evaluate [exp varr] (.evaluate exp varr))

;(defn create-diff [rule] (fn [varr & args] (rule args (mapv #(diff % varr) args))))
(defn create-operation [op sym diff']  (fn [& operands] (Operation. op sym diff' operands)))
(comment ":NOTE: bad names of variables, it's not obvious that differentials at first")
(def Add (create-operation + "+" (fn [a b] (apply Add a))))
;(def Subtract (create-operation - "-" (fn [var & args] (apply Subtract (mapv #(.diff % var) args)))))
;(def Subtract (create-operation - "-" (create-common-diff #(apply Subtract %&))))
(def Subtract (create-operation - "-" (fn [a b] (apply Subtract a))))
(declare Multiply)
(def Multiply
  (create-operation * "*" (fn [a b]
                      (if (= 1 (count a)) (first a) (Add (Multiply (apply Multiply (rest a)) (first b))
                                     (apply Multiply (first a) (rest b)))))))
(declare Divide)
;(def Negate (create-operation - "negate" (fn [var & args] (Negate (diff (first args) var)))))
(declare Negate)
;(def Negate (create-operation - "negate" (fn [vars & args] (Negate (diff (first args) vars)))))
(def Negate (create-operation - "negate" (fn [a b] (Negate (first a)))))
(def Divide
  (create-operation (fn ([first] first) ([first & args] (/ (double first) (apply * args)))) "/" (fn [a b]
                    (if (= (count a) 1) (first a) (Divide (Subtract (Multiply (first a) (apply Multiply (rest b)))
                                (Multiply (first b) (apply Multiply (rest a))))
                        (Multiply (apply Multiply (rest b)) (apply Multiply (rest b)))))
                        )))
(def Sum (create-operation + "sum" (fn [a b] (apply Add a))))
(def Avg (create-operation #(/ (double (apply + %&)) (double (count %&))) "avg"
                           (fn [a b] (Divide (apply Add a) (Constant (count a))))))


(def tokenToObjOperation {'+ Add '- Subtract '* Multiply '/ Divide 'negate Negate 'sum Sum 'avg Avg})
(def parseObject (parse tokenToObjOperation Variable Constant))

;(print (toString (diff (Add (Constant 2) (Variable "y")) "y")))
;(print (toString (diff (Multiply (Constant 4.0) (Variable "z")) "x")))
