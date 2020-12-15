package fr.codelement.uml.metiers;

public abstract class Member
{

    protected String name;
    protected String type;
    protected MemberVisibility visibility;
    protected boolean isStatic;
    protected boolean show;

    public Member(String name, String type, int visibility)
    {
        this.name = name;
        this.type = type;
        this.isStatic = this.isStatic(visibility);
        this.visibility = this.getMemberVisibility(visibility);
        this.show = true;
    }

    protected abstract boolean isStatic(int visibility);
    protected abstract MemberVisibility getMemberVisibility(int visibility);

    public String getType()
    {
        return this.type;
    }

    public void setShow(boolean b)
    {
        this.show = b;
    }

    public String toString()
    {
        if(!this.show)return "";
        String str = "";

        if (this.isStatic)
            str += "\033[0;4m";

        str += String.format("%1s %-15s : %s", this.visibility.getSymbol(), this.name, this.type);

        if (this.isStatic)
            str += "\033[0m";

        return str;
    }

}
