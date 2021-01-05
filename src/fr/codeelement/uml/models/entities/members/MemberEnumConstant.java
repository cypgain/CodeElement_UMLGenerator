package fr.codeelement.uml.models.entities.members;

public class MemberEnumConstant
{

    public String name;

    public MemberEnumConstant(String name)
    {
        this.name = name;
    }

    /**
     * Recupere de nom de la constante d'enumeration
     * @return
     */
    public String getName()
    {
        return this.name;
    }

    public String toString(int width)
    {
        return String.format("%-" + width + "s", this.name);
    }

}
