package starj.spec;

import java.io.*;

class Lexer {
    private boolean reached_eof;
    private StreamTokenizer tokenizer;
    private Token current_token;
    private Token next_token;

    public Lexer(Reader reader) {
        this.reached_eof = false;

        StreamTokenizer tokenizer = new StreamTokenizer(reader);

        tokenizer.resetSyntax();
        tokenizer.wordChars(33, Character.MAX_VALUE);
        tokenizer.whitespaceChars(0, 32);
        tokenizer.commentChar('#');
        tokenizer.slashSlashComments(true);
        tokenizer.slashStarComments(true);
        tokenizer.ordinaryChar('{');
        tokenizer.ordinaryChar('}');
        tokenizer.ordinaryChar(':');
        tokenizer.ordinaryChar(';');
        this.tokenizer = tokenizer;
    }

    public Token peek() throws EOFException, IOException {
        if (this.next_token != null) {
            return this.next_token;
        } else if (this.reached_eof) {
            throw new EOFException();
        } else {
            this.next_token = this.nextToken();
            return this.next_token;
        }
    }

    public boolean hasToken() throws IOException {
        if (this.next_token != null) {
            return true;
        } else if (this.reached_eof) {
            return false;
        } else {
            this.peek();
            return !this.reached_eof;
        }
    }

    public Token nextToken() throws IOException {
        if (this.next_token != null) {
            this.current_token = this.next_token;
            this.next_token = null;
            return this.current_token;
        } else if (this.reached_eof) {
            throw new EOFException();
        } else {
            int token_type = this.tokenizer.nextToken();
            switch (token_type) {
                case '{':
                    this.current_token = new LBracketToken();
                    break;
                case '}':
                    this.current_token = new RBracketToken();
                    break;
                case ':':
                    this.current_token = new ColonToken();
                    break;
                case ';':
                    this.current_token = new SemiColonToken();
                    break;
                case StreamTokenizer.TT_WORD:
                    String s = this.tokenizer.sval;
                    if (s.equals("default")) {
                        this.current_token = new DefaultToken();
                    } else if (s.equals("event")) {
                        this.current_token = new EventToken();
                    } else if (s.equals("true") || s.equals("on")
                            || s.equals("yes")) {
                        this.current_token = new BooleanToken(true);
                    } else if (s.equals("false") || s.equals("off")
                            || s.equals("no")) {
                        this.current_token = new BooleanToken(false);
                    } else {
                        this.current_token = new PatternToken(s);
                    }
                    break;
                case StreamTokenizer.TT_EOF:
                    this.current_token = new EOFToken();
                    this.reached_eof = true;
                    break;

            }

            return this.current_token;
        }
    }

    public int lineno() {
        return this.tokenizer.lineno();
    }
}
