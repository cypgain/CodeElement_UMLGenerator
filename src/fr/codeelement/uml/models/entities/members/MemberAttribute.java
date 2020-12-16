package fr.codeelement.uml.models.entities.members;

import java.lang.reflect.Modifier;

public class MemberAttribute extends Member
{

    private String mult;

    public MemberAttribute(String name, String type, int visibility)
    {
        super(name, type, visibility);
        this.mult = "";

        if (Modifier.isFinal(visibility))
        {
            this.addProperty(" {gele}");
        }
    }

    @Override
    protected MemberVisibility getMemberVisibility(int visibility)
    {
        visibility %= 24;
        return visibility == 1 ? MemberVisibility.PUBLIC : visibility == 2 ? MemberVisibility.PRIVATE : MemberVisibility.PROTECTED;
    }

    public void setMult(String mult)
    {
        this.mult = mult;
    }

    @Override
    public int getTypeWidth()
    {
        return this.mult.length() + this.type.length() + this.property.length();
    }

    @Override
    public String toString(int maxW, int maxTypeWidth)
    {
        if(!this.show) return "";
        String str = "";

        str += this.visibility.getSymbol() + " " + this.name;

        str = String.format("%-" + maxW + "s", str);
        str += " : " + String.format("%" + maxTypeWidth + "s", this.type + this.mult + this.property);

        if (this.isStatic)
            str = "\033[0;4m" + str +"\033[0m";

        return str;
    }

    public String toString()
    {
        return this.toString(25, 25);
    }

}
