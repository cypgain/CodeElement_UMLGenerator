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

    public List<MemberAttribute> getMemberAttributeList()
    {
        List<MemberAttribute> res = new ArrayList<>();
        for(Member m : this.members)
            if(m instanceof MemberAttribute)
                res.add((MemberAttribute)m);

        return res;
    }

    public Member getMember(String name)
    {
        for (Member member : this.members)
        {
            if (member.getName().equalsIgnoreCase(name))
                return member;
        }

        return null;
    }

    public void addMember(Member m)
    {
        this.members.add(m);
    }

    public void addImplementation(String i)
    {
        this.implementations.add(i);
    }

    public int getMaxWitdh()
    {
        int res = 0;
        for (Member m : this.members)
        {
            if(m.getWitdh() > res)res = m.getWitdh();
        }
        return res;
    }

    public String toString()
    {
        String str = "";
        int maxWitdh = this.getMaxWitdh()+1;
        String sep = "";
        for(int witdh = 0; witdh < maxWitdh+10; witdh++)
            sep += "-";

        sep += "\n";

        str += sep;
        str += StringUtils.center(this.name, maxWitdh+10);
        str += "\n";
        str += sep;
        for (Member m : this.members)
        {
            if (m instanceof MemberAttribute)
                if(!(m.toString().isEmpty()))
                    str += m.toString(maxWitdh) + "\n";
        }
        str += sep;
        for (Member m : this.members)
        {
            if (m instanceof MemberMethod)
                if(!(m.toString().isEmpty()))
                    str += m.toString(maxWitdh) + "\n";
        }
        str += sep;
        return str;
    }
}
