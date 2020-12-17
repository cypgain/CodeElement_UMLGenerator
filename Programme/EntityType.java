public enum EntityType
{
    CLASS(""),
    ABSTRACT("{abstract}"),
    INTERFACE("<<interface>>"),
    ENUM("<<enumeration>>");

    private String label;

    EntityType(String label)
    {
        this.label = label;
    }

    public String getLabel()
    {
        return this.label;
    }

}
