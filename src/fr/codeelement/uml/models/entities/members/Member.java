package fr.codeelement.uml.models.entities.members;

import java.lang.reflect.Modifier;

public abstract class Member
{

    protected String name;
    protected String type;
    protected MemberVisibility visibility;
    protected boolean isStatic;
    protected boolean show;
    protected String property;

    public Member(String name, String type, int visibility)
    {
        this.name = name;
        this.type = type;
        this.isStatic = this.isStatic(visibility);
        this.visibility = this.getMemberVisibility(visibility);
        this.show = true;
        this.property = "";
    }

    protected boolean isStatic(int visibility)
    {
        return Modifier.isStatic(visibility);
    }

    protected abstract MemberVisibility getMemberVisibility(int visibility);

    public String getType()
    {
        return this.type;
    }

    public void addProperty(String name)
    {
        this.property = name;
    }

    public void removeProperty()
    {
        this.property = "";
    }

    public String getProperty()
    {
        return this.property;
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

    public abstract int getTypeWidth();

    public abstract String toString(int maxWitdh, int maxTypeWidth);

}
