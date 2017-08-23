define('ace/mode/cypher', [],function(require, exports, module) {
    "use strict";

    var oop = require("ace/lib/oop");
    var TextMode = require("ace/mode/text").Mode;
    var CypherHighlightRules = require("ace/mode/cypher_highlight_rules").CypherHighlightRules;

    var Mode = function() {
        this.HighlightRules = CypherHighlightRules;
    };
    oop.inherits(Mode, TextMode);

    (function() {
        this.$id = "ace/mode/cypher"
    }).call(Mode.prototype);

    exports.Mode = Mode;
});

define('ace/mode/cypher_highlight_rules', [],function(require, exports, module) {
    "use strict";

    var oop = require("ace/lib/oop");
    var TextHighlightRules = require("ace/mode/text_highlight_rules").TextHighlightRules;

    var CypherHighlightRules = function() {

        var keywords = (
            "match|where|create|merge|return|with|skip|limit|order|by"
        );

        var builtinConstants = (
            "true|false|null"
        );

        var builtinFunctions = (
            "avg|count|first|last|max|min|sum|ucase|lcase|mid|len|round|rank|now|format|" +
            "coalesce|ifnull|isnull|nvl"
        );

        var dataTypes = (
            "int|numeric|decimal|date|varchar|char|bigint|float|double|bit|binary|text|set|timestamp|" +
            "money|real|number|integer"
        );

        var keywordMapper = this.createKeywordMapper({
            "support.function": builtinFunctions,
            "keyword": keywords,
            "constant.language": builtinConstants,
            "storage.type": dataTypes
        }, "identifier", true);

        this.$rules = {
            "start" : [ {
                token : "comment",
                regex : "--.*$"
            },  {
                token : "comment",
                start : "/\\*",
                end : "\\*/"
            }, {
                token : "string",           // " string
                regex : '".*?"'
            }, {
                token : "string",           // ' string
                regex : "'.*?'"
            }, {
                token : "string",           // ` string (apache drill)
                regex : "`.*?`"
            }, {
                token : "constant.numeric", // float
                regex : "[+-]?\\d+(?:(?:\\.\\d*)?(?:[eE][+-]?\\d+)?)?\\b"
            }, {
                token : keywordMapper,
                regex : "[a-zA-Z_$][a-zA-Z0-9_$]*\\b"
            }, {
                token : "keyword.operator",
                regex : "\\+|\\-|\\/|\\/\\/|%|<@>|@>|<@|&|\\^|~|<|>|<=|=>|==|!=|<>|="
            }, {
                token : "paren.lparen",
                regex : "[\\(]"
            }, {
                token : "paren.rparen",
                regex : "[\\)]"
            }, {
                token : "text",
                regex : "\\s+"
            } ]
        };
        this.normalizeRules();
    };

    oop.inherits(CypherHighlightRules, TextHighlightRules);

    exports.CypherHighlightRules = CypherHighlightRules;
});
