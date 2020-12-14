package fr.codelement.uml.metiers;

public abstract class Member
{

    protected String name;
    protected String type;
    protected MemberVisibility visibility;
    protected boolean isStatic;

    public Member(String name, String type, int visibility)
    {
        this.name = name;
        this.type = type;
        this.isStatic = this.isStatic(visibility);
        this.visibility = this.getMemberVisibility(visibility);
    }

    protected abstract boolean isStatic(int visibility);
    protected abstract MemberVisibility getMemberVisibility(int visibility);

    public String toString()
    {
        String str = "";

        if (this.isStatic)
            str += "\033[0;4m";

        str += String.format("%1s %-15s : %s", this.visibility.getSymbol(), this.name, this.type);

        if (this.isStatic)
            str += "\033[0m";

        return str;
    }

}
