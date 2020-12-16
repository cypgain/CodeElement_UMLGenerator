package fr.codeelement.uml.models.entities.members;

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

    public String getName()
    {
        return this.name;
    }

    public boolean isShow()
    {
        return this.show;
    }

    public int getWitdh()
    {
        if(!this.show) return 0;
        String str = this.visibility.getSymbol() + " " + this.name;
        return str.length();
    }

    public abstract String toString(int maxWitdh, int maxTypeWidth);

}
