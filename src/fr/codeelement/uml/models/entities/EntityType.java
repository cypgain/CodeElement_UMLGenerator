package fr.codeelement.uml.models.entities;

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

    /**
     * Recupere le label du type de l'entité
     * @return
     */
    public String getLabel()
    {
        return this.label;
    }

}
