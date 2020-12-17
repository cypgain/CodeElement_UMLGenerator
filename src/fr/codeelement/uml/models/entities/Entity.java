package fr.codeelement.uml.models.entities;

import fr.codeelement.uml.models.Component;
import fr.codeelement.uml.models.entities.members.Member;
import fr.codeelement.uml.models.entities.members.MemberAttribute;
import fr.codeelement.uml.models.entities.members.MemberEnumConstant;
import fr.codeelement.uml.models.entities.members.MemberMethod;
import fr.codeelement.uml.utils.StringUtils;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

public class Entity extends Component
{

    private List<Member> members;
    private List<MemberEnumConstant> membersEnumConstants;
    private List<String> implement;

    private Class<?> entityClass;
    private String name;
    private EntityType type;
    private String superClass;

    public Entity(Class<?> entityClass)
    {
        this.members = new ArrayList<>();
        this.membersEnumConstants = new ArrayList<>();
        this.implement = new ArrayList<>();

        this.entityClass = entityClass;
        this.name = entityClass.getSimpleName();
        this.type = this.getEntityType(entityClass);
        this.superClass = (type == EntityType.CLASS) ? entityClass.getSuperclass().getSimpleName() : "";

        // Remplissage de l'entité avec les arguments et méthodes
        this.initArguments(entityClass);
        this.initMethods(entityClass);
        this.initEnumConstants(entityClass);
        this.initImplement(entityClass);
    }

    private void initArguments(Class<?> entityClass)
    {
        for (Field field : entityClass.getDeclaredFields())
        {
            if (entityClass.isEnum() && field.getType().getSimpleName().contains(entityClass.getSimpleName()))
                continue;

            this.members.add(new MemberAttribute(field.getName(), StringUtils.parseFieldName(field.getGenericType().getTypeName()), field.getModifiers()));
        }
    }

    private void initMethods(Class<?> entityClass)
    {
        for (Method method : entityClass.getDeclaredMethods())
        {
            MemberMethod memberMethod = new MemberMethod(method.getName(), StringUtils.parseFieldName(method.getReturnType().getTypeName()), method.getModifiers());
            this.members.add(memberMethod);

            for (Parameter param : method.getParameters())
                memberMethod.addArgument(param);
        }
    }

    private void initEnumConstants(Class<?> entityClass)
    {
        if (entityClass.isEnum())
        {
            for (Object obj : entityClass.getEnumConstants())
                this.membersEnumConstants.add(new MemberEnumConstant("" + obj));
        }
    }

    private void initImplement(Class<?> entityClass)
    {
        for(AnnotatedType inter : entityClass.getAnnotatedInterfaces())
        {
            this.implement.add(inter.getType().getTypeName());
        }
    }

    private EntityType getEntityType(Class<?> entityClass)
    {
        return entityClass.isInterface() ? EntityType.INTERFACE : Modifier.isAbstract(entityClass.getModifiers()) ? EntityType.ABSTRACT : entityClass.isEnum() ? EntityType.ENUM : EntityType.CLASS;
    }

    private int getMaxStringWitdh()
    {
        int res = 0;
        for (Member m : this.members)
        {
            if(m.getWitdh() > res)res = m.getWitdh();
        }

        if (res < this.name.length())
            res = this.name.length();

        if (res < this.type.getLabel().length())
            res = this.type.getLabel().length();

        return res;
    }

    private int getMaxMemberTypeStringWidth()
    {
        int res = 0;
        for (Member m : this.members)
        {
            if (!m.isShow())
                continue;

            if(m.getTypeWidth() > res) res = m.getTypeWidth();
        }
        return res;
    }

    public void hideAllAttributes()
    {
        List<MemberAttribute> attributes = this.getAttributes();
        for (MemberAttribute attr : attributes)
        {
            attr.setShow(false);
        }
    }

    public void hideAllMethods()
    {
        List<MemberMethod> methods = this.getMethods();
        for (MemberMethod method : methods)
        {
            method.setShow(false);
        }
    }

    public List<MemberAttribute> getAttributes()
    {
        List<MemberAttribute> attributes = new ArrayList<>();

        for (Member member : this.members)
        {
            if (member instanceof  MemberAttribute)
                attributes.add((MemberAttribute)member);
        }

        return attributes;
    }

    public List<MemberMethod> getMethods()
    {
        List<MemberMethod> methods = new ArrayList<>();

        for (Member member : this.members)
        {
            if (member instanceof  MemberMethod)
                methods.add((MemberMethod)member);
        }

        return methods;
    }

    public Class<?> getEntityClass()
    {
        return this.entityClass;
    }

    public MemberAttribute getAttribute(String name)
    {
        List<MemberAttribute> attributes = this.getAttributes();
        for (MemberAttribute attr : attributes)
        {
            if (attr.getName().equalsIgnoreCase(name))
                return attr;
        }
        return null;
    }

    public MemberMethod getMethod(String signature)
    {
        List<MemberMethod> methods = this.getMethods();
        for (MemberMethod method : methods)
        {
            if (method.getSignature().equalsIgnoreCase(signature))
                return method;
        }
        return null;
    }

    public String getName()
    {
        return this.name;
    }

    public String getSuperClass()
    {
        return this.superClass;
    }

    public List<String> getImplement()
    {
        return this.implement;
    }

    public EntityType getType()
    {
        return this.type;
    }

    public String toString()
    {
        int maxWitdh = this.getMaxStringWitdh() + 1;
        int maxTypeWidth = this.getMaxMemberTypeStringWidth() + 4;

        String str = "";
        String sepTop = "┌";
        String sepMiddle = "├";
        String sepBottom = "└";


        for(int witdh = 0; witdh < maxWitdh + maxTypeWidth + 2; witdh++)
        {
            sepTop += "─";
            sepMiddle += "─";
            sepBottom += "─";
        }

        sepTop += "┐\n";
        sepMiddle += "┤\n";
        sepBottom += "┘\n";
        str += sepTop;

        if (this.type != EntityType.CLASS)
        {

            str += "│";
            str += StringUtils.center(this.type.getLabel(), maxWitdh + maxTypeWidth + 2);
            str += "│\n";
        }

        str += "│";
        str += StringUtils.center(this.name, maxWitdh + maxTypeWidth + 2);
        str += "│\n";
        str += sepMiddle;

        for (Member m : this.members)
        {
            if (m instanceof MemberAttribute)
                if(!(m.toString().equals("")))
                    str += "│ " + m.toString(maxWitdh, maxTypeWidth - 3) + " │\n";
        }

        str += sepMiddle;

        for (MemberEnumConstant m : this.membersEnumConstants)
        {
            str += "│ " + m.toString(maxWitdh + maxTypeWidth) + " │\n";
        }

        for (Member m : this.members)
        {
            if (m instanceof MemberMethod)
                if(!(m.toString().isEmpty()))
                    str += "│ " + m.toString(maxWitdh, maxTypeWidth - 3) + " │\n";
        }

        str += sepBottom;
        return str;
    }

}