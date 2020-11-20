"use strict";

function Expr(evaluate, diff, toString, prefix, postfix) {
    this.prototype.evaluate = evaluate;
    this.prototype.diff = diff;
    this.prototype.toString = toString;
    this.prototype.prefix = prefix;
    this.prototype.postfix = postfix;
}
function setMethods(object, evaluate, diff, toString, prefix = toString, postfix = toString) {
    object.prototype = Object.create(Expr.prototype);
    Expr.call(object, evaluate, diff, toString, prefix, postfix);
}

function CreateConstOrVariable(constructor, get, diff, toString, prefix = toString, postfix = toString) {
    let result = constructor;
    setMethods(result, get, diff, toString);
    return result;
}
const Const = CreateConstOrVariable(
    function(x) { this.x = x; },
    function() { return this.x },
    function() { return Const.ZERO },
    function() { return this.x.toString() }
)
Const.ZERO = new Const(0);
Const.ONE = new Const(1);
const Variable = CreateConstOrVariable(
    function(name) { this.name = name; this.argIndex = vars[name];},
    function(...args) { return args[this.argIndex] },
    function(variable) { return this.name === variable ? Const.ONE : Const.ZERO},
    function() { return this.name.toString() }
)
const vars = {"x" : 0, "y" : 1, "z" : 2};
let tokenToOperation = {};

function toSpecialFormat(left, format, right, operands) {
    return left + operands.map(format).join(' ') + right;
}
function CreateOperation(makeOperation, makeDiff, operationSymbol) {
    let operation = function (...args) {
        this.operands = args;
    };
    setMethods(operation,
        function (...args) {
            return this.makeOperation(...this.operands.map((element) => element.evaluate(...args)))
        },
        function (variable) {
            return this.makeDiff(variable, ...this.operands)
        },
        function () { return toSpecialFormat('', (x) => (x.toString()), this.operationSymbol, this.operands) },
        function () { return toSpecialFormat('(' + this.operationSymbol + ' ', (x) => (x.prefix()), ')', this.operands) },
        function () { return toSpecialFormat('(', (x) => (x.postfix()), ' ' + this.operationSymbol + ')', this.operands) }
    );
    operation.prototype.makeOperation = makeOperation;
    operation.prototype.makeDiff = makeDiff;
    operation.prototype.operationSymbol = operationSymbol;
    tokenToOperation[operationSymbol] = operation;
    return operation;
}

const Add = CreateOperation(
    (l, r) => (l + r),
    (variable, l, r) => new Add(l.diff(variable), r.diff(variable)),
    "+"
)

const Subtract = CreateOperation(
    (l, r) => (l - r),
    (variable, l, r) => new Subtract(l.diff(variable), r.diff(variable)),
    "-"
)

const Divide = CreateOperation(
    (l, r) => (l / r),
    (variable, l, r) => new Divide(
        new Subtract(
            new Multiply(l.diff(variable), r), new Multiply(l, r.diff(variable))),
        new Multiply(r, r)),
    "/"
)

const Multiply = CreateOperation(
    (l, r) => (l * r) ,
    (variable, l, r) => (new Add(
        new Multiply(l.diff(variable), r), new Multiply(l, r.diff(variable)))),
    "*"
)

const Negate = CreateOperation(
    (l) => (-l),
    (variable, l) => (new Negate(l.diff(variable))),
    "negate"
)

const Power = CreateOperation(
    (l, r) => (Math.pow(l, r)),
    (variable, l, r) => (new Multiply(
        new Power(l, new Subtract(r, new Const(+'1'))),
        new Add(
            new Multiply(r, l.diff(variable)), new Multiply(l, new Multiply(new Log(E, l), r.diff(variable)))))),
    "pow"
)

const E = new Const(Math.E);
const Log = CreateOperation(
    (l, r) => (Math.log(Math.abs(r)) / Math.log(Math.abs(l))),
    (variable, l, r) => (new Divide(
        new Subtract(
            new Divide(new Multiply(new Log(E, l), r.diff(variable)), r),
            new Divide(new Multiply(l.diff(variable), new Log(E, r)), l)
        ), new Multiply(new Log(E, l), new Log(E, l))
    )) ,
    "log"
)


const buildSum = function(...args) {
    if (args.length === 1) {
        return args[0];
    }
    let res = new Add(args[0], args[1]);
    for (let i = 2; i < args.length; i++) {
        res = new Add(res, args[i]);
    }
    return res;
}

const Sumexp = CreateOperation(
    (...args) => (args.reduce((acc, cur) => acc + Math.pow(Math.E, cur), 0)),
    (variable, ...args) => (
        (args.length === 0) ? new Const(+'0') : buildSum(...args.map((element) =>
            (new Multiply(new Power(E, element), element.diff(variable)))))
    ),
    "sumexp"
)

const Softmax = CreateOperation(
    (...args) => (Math.pow(Math.E, args[0]) / Sumexp.prototype.makeOperation(...args)),
    (variable, ...args) => (
        args.length === 0 ? new Const(+'0') : (new Divide(new Power(E, args[0]),
                buildSum(...args.map((element) => new Power(E, element))))
        ).diff(variable)
    ),
    "softmax"
)

function parse(s) {
    let exprs = []
    const parse_token = function(token) {
        if (token in tokenToOperation) {
            token = tokenToOperation[token];
            let l = token.prototype.makeOperation.length;
            exprs.splice(-l, l, new token(...exprs.slice(-l)));
        } else if (token in vars) {
            exprs.push(new Variable(token));
        } else if (token.split("").map((x) => ('0' <= x && x <= '9')).reduce((x, y) => (x & y)), true) { // a simple const
            exprs.push(new Const(1 * token));
        }
    }
    s.trim().split(/\s+/).forEach(parse_token);
    return exprs[0];
}

function CreateStringSource() {
    let source = function(data) {
        this._data = data;
        this._pos = 0;
        this.curToken = "";
    }
    source.prototype.hasNext = function() { return this._pos < this._data.length; }
    source.prototype.next = function() { return this.hasNext() ? this._data[this._pos++] : '\0'; }
    source.prototype.cur = function() { return this.hasNext() ? this._data[this._pos] : '\0'; }
    source.prototype.inc = function() { this._pos++; }
    source.prototype.getPos = function() { return this._pos; }
    source.prototype.check = function(ch) { return this.cur() === ch; }
    source.prototype.getSubstr = function() { return this._data.substring(Math.max(this._pos - 15, 0),
        Math.min(this._pos + 10, this._data.length)) }
    source.prototype.isWhiteSpace = function(value) {
        return /\s/.test(value);
    }
    source.prototype.skipWhiteSpaces = function() {
        while (this.isWhiteSpace(this.cur())) {
            this.next();
        }
    }
    source.prototype.nextToken = function(convert) {
        this.skipWhiteSpaces();
        if (this.check('(') || this.check(')')) {
            this.curToken = this.next();
            return convert ? this.handle(this.curToken) : this.curToken;
        }
        let token = '';
        while (this.hasNext() && !this.isWhiteSpace(this.cur()) && !this.check('(') && !this.check(')')) {
            token = token + this.cur();
            this.next();
        }
        token = (token === '') ? this.next() : token;
        this.curToken = token;
        return convert ? this.handle(token) : token;
    }
    source.prototype.getCurToken = function(convert) {
        return convert ? this.handle(this.curToken) : this.curToken;
    }
    source.prototype.handle = function(token) {
        if (token === "(") {
            return "Lb";
        } else if (token === ")") {
            return "Rb";
        } else if (/^-{0,1}\d+$/.test(token)) {
            return "Const";
        } else if (token in tokenToOperation) {
            return "Operation";
        } else if (token in vars) {
            return "Variable";
        } else if (token === "\0") {
            return "End";
        } else if (token === "") {
            return "Empty";
        }
    }
    return source;
}

const StringSource = CreateStringSource();

function CreateAbstractError() {
    let AbstractError = function(message) {
        this.message = message;
    }
    AbstractError.prototype = Object.create(Error.prototype);
    AbstractError.prototype.constructor = AbstractError;
    AbstractError.prototype.name = "AbstractError";
    return AbstractError;
}

const ExpressionError = CreateAbstractError();
function CreateExpressionError(name, head) {
    let error = function(source, message = '') {
        ExpressionError.call(this, head + ' ' + (message === '' ? "" : "")  + message + ', at pos ' + source.getPos() + ": " + source.getSubstr());
    }
    error.prototype = Object.create(ExpressionError.prototype);
    error.prototype.constructor = ExpressionError;
    error.prototype.name = name;
    return error;
}

const UnknownSymbolError = CreateExpressionError("UnknownSymbolError", "Unknown symbol");
const UnexpectedTokenError = CreateExpressionError("UnexpectedTokenError", "Unexpected token");
const MissingBracketError = CreateExpressionError("MissingBracketError", "Expected bracket");
const UnexpectedEndError =  CreateExpressionError("UnknownEndError", "Unexpected end of source");
const InvalidOperationFormatError = CreateExpressionError("InvalidOperationFormatError", "Invalid operation format");


function CreateParser() {
    let Parser = function(source) {
        this.source = source;
    }
    Parser.prototype.test = function(condition, error, message = '') {
        if (condition) { throw (message === '' ? new error(this.source) : new error(this.source, message)) }
    }
    Parser.prototype.throwError = function(error, message) {
        throw (message === '' ? new error(this.source) : new error(this.source, message))
    }
    let _mode = undefined;
    Parser.prototype.parse = function(mode) {
        this.source.nextToken();
        let result = this.parseExpr();
        _mode = mode;
        this.test(this.source.hasNext(), UnexpectedTokenError, "'" + this.source.getCurToken(false) + "'");
        return result;
    }
    Parser.prototype.parseExpr = function() {
        let token = this.source.getCurToken(true);
        if (token === "Lb") {
            let cur = '', content = [], operationCounter = 0, pos = -1;
            while (this.source.nextToken(true) !== "Rb" && this.source.hasNext()) {
                cur = this.source.getCurToken(true);
                if (cur === "Lb") {
                    content.push(this.parseExpr());
                } else if (cur === "Const") {
                    content.push(new Const(+this.source.getCurToken(false)));
                } else if (cur === "Variable") {
                    content.push(new Variable(this.source.getCurToken(false)));
                } else if (cur === "Operation"){
                    content.push(this.source.getCurToken(false));
                    operationCounter++;
                    pos = content.length - 1;
                } else {
                    this.throwError(UnexpectedTokenError, "'" + this.source.getCurToken(false) + "'");
                }
            }
            this.test(this.source.getCurToken(true) !== "Rb", MissingBracketError, "')'");
            this.test(operationCounter !== 1, InvalidOperationFormatError, "(operation expected)");
            this.test(_mode === "prefix" && pos !== 0, InvalidOperationFormatError, "for prefix mode");
            this.test(_mode === "postfix" && pos !== content.length - 1, InvalidOperationFormatError, "for postfix mode");
            let x = tokenToOperation[content[pos]].prototype.makeOperation.length;
            this.test(x !== 0 && content.length - 1 !== x, InvalidOperationFormatError, "(incorrect count of args)");
            return new tokenToOperation[content[pos]](...content.slice((_mode === "prefix" ? 1 : 0), content.length - 1 - (_mode === "postfix" ? 1 : 0) + 1));
        } else if (token === "Const") {
            return new Const(+this.source.getCurToken(false));
        } else if (token === "Variable") {
            return new Variable(this.source.getCurToken(false));
        } else {
            this.throwError(UnexpectedTokenError, "'" + this.source.getCurToken(false) + "'");
        }
    }
    return Parser;
}

const ExpressionParser = CreateParser();
function parsePrefix(s) {
    let parser = new ExpressionParser(new StringSource(s.trim()));
    return parser.parse("prefix");
}
function parsePostfix(s) {
    let parser = new ExpressionParser(new StringSource(s.trim()));
    return parser.parse("postfix");
}
