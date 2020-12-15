package fr.codelement.uml.metiers;

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
    }

    public void addArgument(Parameter param)
    {
        this.arguments.add(param);
    }

    @Override
    protected boolean isStatic(int visibility)
    {
        return (visibility / 8) >= 1;
    }

    @Override
    protected MemberVisibility getMemberVisibility(int visibility)
    {
        visibility %= 8;
        return visibility == 1 ? MemberVisibility.PUBLIC : visibility == 2 ? MemberVisibility.PRIVATE : MemberVisibility.PROTECTED;
    }

    @Override
    public int getWitdh()
    {
        if(!this.show)return 0;
        String str = "";

        str += this.visibility.getSymbol() + " ";
        str += this.name + " (";

        for (Parameter param : this.arguments)
            str += param.getName() + " : " + param.getType().getSimpleName();

        str += ")";
        return str.length();
    }

    public String toString()
    {
        return this.toString(25);
    }

    public String toString(int maxW)
    {
        if(!this.show)return "";
        String str = "";

        str += this.visibility.getSymbol() + " ";

        str += this.name + " (";

        for (Parameter param : this.arguments)
            str += param.getName() + " : " + param.getType().getSimpleName();

        str += ")";
        str = String.format("%-" + maxW + "s", str);
        str += " : " + this.type;


        //----------------------
        if (this.isStatic)
            str = "\033[0;4m" + str +"\033[0m";

        return str;
    }

}
