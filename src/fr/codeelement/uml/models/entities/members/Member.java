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

    /**
     * Retourne si le membre est static
     * @param visibility
     * @return
     */
    protected boolean isStatic(int visibility)
    {
        return Modifier.isStatic(visibility);
    }

    /**
     * Retourne la visibilité du membre
     * @param visibility
     * @return
     */
    protected abstract MemberVisibility getMemberVisibility(int visibility);

    /**
     * Retourne le type du membre
     * @return
     */
    public String getType()
    {
        return this.type;
    }

    /**
     * Ajoute une propriété
     * @param name
     */
    public void addProperty(String name)
    {
        this.property = name;
    }

    /**
     * Supprimé une propriété
     */
    public void removeProperty()
    {
        this.property = "";
    }

    /**
     * Recupere la propriété
     * @return
     */
    public String getProperty()
    {
        return this.property;
    }

    /**
     * Definit le statut
     * @param b
     */
    public void setShow(boolean b)
    {
        this.show = b;
    }

    /**
     * Recupere le nom
     * @return
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Recupere le statut
     * @return
     */
    public boolean isShow()
    {
        return this.show;
    }

    /**
     * Recupere la longueur de la chaine de caractere
     * @return
     */
    public int getWitdh()
    {
        if(!this.show) return 0;
        String str = this.visibility.getSymbol() + " " + this.name;
        return str.length();
    }

    /**
     * Recupere la longueur du type
     * @return
     */
    public abstract int getTypeWidth();

    /**
     * Retourne une chaine de caractere contenant le membre
     * @param maxWitdh
     * @param maxTypeWidth
     * @return
     */
    public abstract String toString(int maxWitdh, int maxTypeWidth);

}
