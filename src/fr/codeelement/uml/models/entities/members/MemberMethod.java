package fr.codeelement.uml.models.entities.members;

import fr.codeelement.uml.utils.StringUtils;

import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class MemberMethod extends Member
{

    private List<Parameter> arguments;

    public MemberMethod(String name, String type, int visibility)
    {
        super(name, type, visibility);
        this.arguments = new ArrayList<>();

        if (Modifier.isAbstract(visibility))
        {
            this.addProperty(" {abstrait}");
        }
    }

    /**
     * Recupere la visibilité du membre
     * @param visibility
     * @return
     */
    @Override
    protected MemberVisibility getMemberVisibility(int visibility)
    {
        visibility %= 8;
        return visibility == 1 ? MemberVisibility.PUBLIC : visibility == 2 ? MemberVisibility.PRIVATE : MemberVisibility.PROTECTED;
    }

    /**
     * Ajoute un argument
     * @param param
     */
    public void addArgument(Parameter param)
    {
        this.arguments.add(param);
    }

    /**
     * Recupere la longueur du membre
     * @return
     */
    @Override
    public int getWitdh()
    {
        if(!this.show)return 0;
        String str = "";

        str += this.visibility.getSymbol() + " ";
        str += this.name + " (";

        int i = 0;
        for (Parameter param : this.arguments)
        {
            str += param.getName() + " : " + StringUtils.parseFieldName(param.getParameterizedType().getTypeName());
            if (i + 1 != this.arguments.size())
                str += ",";
            i++;
        }

        str += ")";
        return str.length();
    }

    /**
     * Recupere la signature de la méthode
     * @return
     */
    public String getSignature()
    {
        String str = "";

        int i = 0;

        str += this.name;
        str += "(";
        for (Parameter arg : this.arguments)
        {
            str += StringUtils.parseFieldName(arg.getParameterizedType().getTypeName());
            if (i + 1 != this.arguments.size())
                str += ",";
            i++;
        }
        str += ")";

        return str;
    }

    /**
     * Recupere la longueur du type
     * @return
     */
    @Override
    public int getTypeWidth() 
    {
        return this.type.length() + this.property.length();
    }

    /**
     * Recupere la chaine de caractere de la méthode
     * @param maxW
     * @param maxTypeWidth
     * @return
     */
    @Override
    public String toString(int maxW, int maxTypeWidth)
    {
        if(!this.show)return "";
        String str = "";

        str += this.visibility.getSymbol() + " ";

        str += this.name + " (";

        int i = 0;
        for (Parameter param : this.arguments)
        {
            str += param.getName() + " : " + StringUtils.parseFieldName(param.getParameterizedType().getTypeName());
            if (i + 1 != this.arguments.size())
                str += ", ";
            i++;
        }

        str += ")";
        str = String.format("%-" + maxW + "s", str);
        str += " : " + String.format("%" + maxTypeWidth + "s", this.type + this.property);

        if (this.isStatic)
            str = "\033[0;4m" + str +"\033[0m";

        return str;
    }

    /**
     * Recupere la chaine de caractere de la méthode
     * @return
     */
    public String toString()
    {
        return this.toString(25, 25);
    }

}
