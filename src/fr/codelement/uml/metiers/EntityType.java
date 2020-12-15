package fr.codelement.uml.metiers;

public enum EntityType
{
    CLASS(""),
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
