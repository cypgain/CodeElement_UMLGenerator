package fr.codeelement.uml.models.entities.members;

import java.lang.reflect.Modifier;

public class MemberAttribute extends Member
{

    private String mult;

    public MemberAttribute(String name, String type, int visibility)
    {
        super(name, type, visibility);
        this.mult = "";

        if (Modifier.isFinal(visibility))
        {
            this.addProperty(" {gele}");
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
        visibility %= 24;
        return visibility == 1 ? MemberVisibility.PUBLIC : visibility == 2 ? MemberVisibility.PRIVATE : MemberVisibility.PROTECTED;
    }

    /**
     * Definit la multiplicité
     * @param mult
     */
    public void setMult(String mult)
    {
        this.mult = mult;
    }

    /**
     * Recupere la longueur du type
     * @return
     */
    @Override
    public int getTypeWidth()
    {
        return this.mult.length() + this.type.length() + this.property.length();
    }

    /**
     * Recupere la chaine de caractere de l'attribut
     * @param maxW
     * @param maxTypeWidth
     * @return
     */
    @Override
    public String toString(int maxW, int maxTypeWidth)
    {
        if(!this.show) return "";
        String str = "";

        str += this.visibility.getSymbol() + " " + this.name;

        str = String.format("%-" + maxW + "s", str);
        str += " : " + String.format("%" + maxTypeWidth + "s", this.type + this.mult + this.property);

        if (this.isStatic)
            str = "\033[0;4m" + str +"\033[0m";

        return str;
    }

    /**
     * Recupere la chaine de caractere de l'attribut
     * @return
     */
    public String toString()
    {
        return this.toString(25, 25);
    }

}
