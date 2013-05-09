package starj.spec;

import java.io.*;

public class Parser {
    private Lexer lexer;
    private int level = 0;
    
    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public AST parse() throws InvalidSyntaxException, IOException {
        return new AST(this.start());
    }

    // start ::= eventDeclarationList
    public ASTEventDeclarationSequence start()
            throws InvalidSyntaxException, IOException {
        ASTEventDeclarationSequence seq = this.eventDeclarationList();
        this.consume(Token.T_EOF);
        return seq;
    }

    // eventDeclarationList ::= <empty> | NEEventDeclarationList
    public ASTEventDeclarationSequence eventDeclarationList()
            throws InvalidSyntaxException, IOException {
        Token token = this.lexer.peek();
        switch (token.getType()) {
            case Token.T_EOF:
                return new ASTEventDeclarationSequence();
            default:
                return this.NEEventDeclarationList();
        }
    }

    // NEEventDeclarationList ::= eventDeclaration
    //                         | eventDeclaration NEEventDeclarationList
    public ASTEventDeclarationSequence NEEventDeclarationList()
            throws InvalidSyntaxException, IOException {
        ASTEventDeclaration decl = this.eventDeclaration();
        Token token = this.lexer.peek();
        ASTEventDeclarationSequence seq;
        int token_type = token.getType();
        if (token_type == Token.T_EVENT || token_type == Token.T_DEFAULT) {
             seq = this.NEEventDeclarationList();
             seq.add(decl);
        } else {
            seq = new ASTEventDeclarationSequence(decl);
        }
        return seq;
    }

    public ASTEventDeclaration eventDeclaration()
            throws InvalidSyntaxException, IOException {
        Token token = this.lexer.peek();
        switch (token.getType()) {
            case Token.T_EVENT: {
                    this.consume(Token.T_EVENT);
                    return new ASTPatternEventDeclaration(this.pattern(),
                            this.fieldDeclarationBlock());
                }
            case Token.T_DEFAULT:
                this.consume(Token.T_DEFAULT);
                return new ASTDefaultEventDeclaration(
                        this.fieldDeclarationBlock());
            default:
                this.error("Expected 'event' or 'default'");
        }
        
        // Should not happen as long as this.error() call under default
        // label throws an exception
        throw new RuntimeException("Should not happen");
    }

    public ASTFieldDeclarationSequence fieldDeclarationBlock()
            throws InvalidSyntaxException, IOException {
        this.consume(Token.T_LBRACKET);
        ASTFieldDeclarationSequence seq = this.fieldDeclarationList();
        this.consume(Token.T_RBRACKET);
        return seq;
    }

    // fieldDeclarationList ::= <empty> | NEFieldDeclarationList
    public ASTFieldDeclarationSequence fieldDeclarationList()
            throws InvalidSyntaxException, IOException {
        Token token = this.lexer.peek();
        switch (token.getType()) {
            case Token.T_PATTERN:
                return this.NEFieldDeclarationList();
            default:
                return new ASTFieldDeclarationSequence();
        }
    }

    // NEFieldDeclarationList ::= fieldDeclaration
    //                          | fieldDeclaration NEFieldDeclarationList
    public ASTFieldDeclarationSequence NEFieldDeclarationList()
            throws InvalidSyntaxException, IOException {
        ASTFieldDeclaration decl = this.fieldDeclaration();
        Token token = this.lexer.peek();
        ASTFieldDeclarationSequence seq;
        if (token.getType() != Token.T_RBRACKET) {
            seq = this.NEFieldDeclarationList();
            seq.add(decl);
        } else {
            seq = new ASTFieldDeclarationSequence(decl);
        }
        return seq;
    }

    // fieldDeclaration ::= pattern ':' fieldValue ';'
    public ASTFieldDeclaration fieldDeclaration()
            throws InvalidSyntaxException, IOException {
        ASTPattern pattern = this.pattern();
        this.consume(Token.T_COLON);
        ASTBoolean value = this.fieldValue();
        this.consume(Token.T_SEMICOL);
        return new ASTFieldDeclaration(pattern, value);
    }

    // pattern ::= T_PATTERN
    public ASTPattern pattern() throws InvalidSyntaxException, IOException {
        PatternToken t = (PatternToken) this.consume(Token.T_PATTERN);
        return new ASTPattern(t.getPattern());
    }

    // fieldValue ::= boolean
    public ASTBoolean fieldValue() throws InvalidSyntaxException, IOException {
        return this.parseBoolean();
    }

    // boolean ::= T_BOOLEAN
    public ASTBoolean parseBoolean()
            throws InvalidSyntaxException, IOException {
        BooleanToken t = (BooleanToken) this.consume(Token.T_BOOL);
        return new ASTBoolean(t.getValue());
    }

    public Token consume(int type) throws InvalidSyntaxException, IOException {
        Token token = this.lexer.nextToken();
        if (token.getType() != type) {
            String token_type = null;
            switch (type) {
                case Token.T_EOF:
                    token_type = "EOF";
                    break;
                case Token.T_COLON:
                    token_type = "':'";
                    break;
                case Token.T_SEMICOL:
                    token_type = "';'";
                    break;
                case Token.T_LBRACKET:
                    token_type = "'{'";
                    break;
                case Token.T_RBRACKET:
                    token_type = "'}'";
                    break;
                case Token.T_EVENT:
                    token_type = "'event'";
                    break;
                case Token.T_DEFAULT:
                    token_type = "'default'";
                    break;
                case Token.T_PATTERN:
                    token_type = "pattern";
                    break;
                case Token.T_BOOL:
                    token_type = "boolean value";
                    break;
                default:
                    token_type = " different token"; // FIXME: not very good ...
                    break;
            }
            this.error("Expected " + token_type);
        }

        return token;
    }

    public void error(String message) throws InvalidSyntaxException {
        throw new InvalidSyntaxException(this.lexer.lineno(), message);
    }

    public static void main(String[] args) throws Exception {
        InputStreamReader r = new InputStreamReader(System.in);
        Lexer lexer = new Lexer(r);
        Parser parser = new Parser(lexer);
        AST ast = parser.parse();
        //System.out.println(ast);
        SpecFormat jvmpi_format = SpecFormatManager.v().getCurrentFormat();
        ASTExpander expander = new ASTExpander(jvmpi_format);
        TraceSpecification spec = expander.expand(ast);
        (new PrettyPrinter(System.out)).visit(spec);
    }
}
