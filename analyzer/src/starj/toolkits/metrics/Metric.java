package starj.toolkits.metrics;

public abstract class Metric {
    private String category;
    private String name;

    public Metric(String category, String name) {
        if (category == null || name == null) {
            // Can't allow null category/name for obvious reasons
            throw new NullPointerException("Invalid metric name component");
        }
        this.category = category;
        this.name = name;
    }
    
    public String getCategory() {
        return this.category;
    }
    
    public String getName() {
        return this.name;
    }

    public abstract String getKind();

    public String getFullName() {
        return this.category + "." + this.name + "." + this.getKind();
    }
}
