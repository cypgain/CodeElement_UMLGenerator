package fr.codelement.uml.metiers;

public class MemberAttribute extends Member
{
    private String mult;

    public MemberAttribute(String name, String type, int visibility)
    {
        super(name, type, visibility);
        this.mult = "";
    }

    @Override
    protected boolean isStatic(int visibility)
    {
        return (visibility / 24) >= 1;
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

    public String toString()
    {
        return this.toString(25);
    }

    @Override
    public String toString(int maxW)
    {
        if(!this.show) return "";
        String str = "";

        str += this.visibility.getSymbol() + " " + this.name;

        str = String.format("%-" + maxW + "s", str);
        str += " : " + this.type;
        if(!mult.equals(""))
        {
            str += " [" + this.mult + "]";
        }

        if (this.isStatic)
            str = "\033[0;4m" + str +"\033[0m";

        return str;
    }

}
