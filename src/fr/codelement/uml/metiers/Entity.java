package fr.codelement.uml.metiers;

import fr.codelement.uml.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Entity
{

    private String name;
    private EntityType type;
    private List<Member> members;
    private String superClass;
    private List<String> implementations;

    public Entity(String name, EntityType type, String superClass)
    {
        this.name = name;
        this.type = type;
        this.members = new ArrayList<>();
        this.superClass = superClass;
        this.implementations = new ArrayList<>();
    }

    public String getSuperClass()
    {
        return this.superClass;
    }

    public String getName()
    {
        return this.name;
    }

    public List<String> getImplementations()
    {
        return this.implementations;
    }

    public void addMember(Member m)
    {
        this.members.add(m);
    }

    public void addImplementation(String i)
    {
        this.implementations.add(i);
    }

    public String toString()
    {
        String str = "";
        String sep = "------------------------------------------------\n";

        str += sep;
        str += StringUtils.center(this.name, 48);
        str += "\n";
        str += sep;
        for (Member m : this.members)
        {
            if (m instanceof MemberAttribute)
                str += m + "\n";
        }
        str += sep;
        for (Member m : this.members)
        {
            if (m instanceof MemberMethod)
                str += m + "\n";
        }
        str += sep;
        return str;
    }

}
