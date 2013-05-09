package starj.options;

public class Author {
    private String name;
    private String email;
    private String institution;
    
    public Author(String name) {
        this(name, null, null);
    }

    public Author(String name, String email) {
        this(name, email, null);
    }

    public Author(String name, String email, String institution) {
        this.name = name;
        this.email = email;
        this.institution = institution;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getEmail() {
        return this.email;
    }
    
    public String getInstitution() {
        return this.institution;
    }

    public String toString() {
        String rv = null;

        if (this.name != null) {
            rv = this.name;
        }

        if (this.institution != null) {
            if (rv != null) {
                rv += " - ";
            } else {
                rv = "";
            }
            rv += this.institution;
        } 

        if (this.email != null) {
            if (rv != null) {
                rv += " (" + this.email + ")";
            } else {
                rv = this.email;
            }
        }
        return rv;
    }
}
