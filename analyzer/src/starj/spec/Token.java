package starj.spec;

abstract class Token {
    public static final int T_EOF      = -1;
    public static final int T_COLON    = 0;
    public static final int T_SEMICOL  = 1;
    public static final int T_LBRACKET = 2;
    public static final int T_RBRACKET = 3;
    public static final int T_EVENT    = 4;
    public static final int T_DEFAULT  = 5;
    public static final int T_PATTERN  = 6;
    public static final int T_BOOL     = 7;

    private int type;
    
    public Token(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }
}

class EOFToken extends Token {
    public EOFToken() {
        super(Token.T_EOF);
    }

    public String toString() {
        return "<<EOF>>";
    }
}

abstract class CharacterToken extends Token {
    private char character;
    
    public CharacterToken(int type, char c) {
        super(type);
        this.character = c;
    }

    public char getCharacter() {
        return this.character;
    }

    public String toString() {
        return "'" + this.character + "'";
    }
}

class ColonToken extends CharacterToken {
    public ColonToken() {
        super(Token.T_COLON, ':');
    }
}

class SemiColonToken extends CharacterToken {
    public SemiColonToken() {
        super(Token.T_SEMICOL, ';');
    }
}

class LBracketToken extends CharacterToken {
    public LBracketToken() {
        super(Token.T_LBRACKET, '{');
    }
}

class RBracketToken extends CharacterToken {
    public RBracketToken() {
        super(Token.T_RBRACKET, '}');
    }
}

abstract class KeywordToken extends Token {
    private String keyword;

    public KeywordToken(int type, String keyword) {
        super(type);
        this.keyword = keyword;
    }

    public String getKeyword() {
        return this.keyword;
    }

    public String toString() {
        return this.keyword.toUpperCase();
    }
}

class EventToken extends KeywordToken {
    public EventToken() {
        super(Token.T_EVENT, "event");
    }
}

class DefaultToken extends KeywordToken {
    public DefaultToken() {
        super(Token.T_DEFAULT, "default");
    }
}

class PatternToken extends Token {
    private String pattern;
    
    public PatternToken(String pattern) {
        super(Token.T_PATTERN);
        this.pattern = pattern;
    }

    public String getPattern() {
        return this.pattern;
    }

    public String toString() {
        return this.pattern;
    }
}

class BooleanToken extends Token {
    private boolean value;

    public BooleanToken(boolean value) {
        super(Token.T_BOOL);
        this.value = value;
    }

    public boolean getValue() {
        return this.value;
    }

    public String toString() {
        return "@" + this.value + "@";
    }
}
