package fr.codelement.uml.metiers;

public class EnumConstantMember
{

    public String name;

    public EnumConstantMember(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    @Override
    public String toString()
    {
        return this.name;
    }
}
