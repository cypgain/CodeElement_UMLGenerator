package fr.codeelement.uml.models;

import fr.codeelement.uml.models.relations.Relation;

public abstract class Component implements Comparable<Component>
{

    private static int componentsAmount = 1;

    protected int id;
    protected boolean show;
    protected int order;

    public Component()
    {
        if (this instanceof Relation)
            this.id = Component.componentsAmount++;
        else
            this.id = -1;

        this.order = 999;
        this.show = true;
    }

    /**
     * Recupere l'id du composant
     * @return
     */
    public int getId()
    {
        return this.id;
    }

    /**
     * Recupere le statut du composant
     * @return
     */
    public boolean isShow()
    {
        return this.show;
    }

    /**
     * Recupere l'ordre du composant
     * @return
     */
    public int getOrder()
    {
        return this.order;
    }

    /**
     * Deffinit l'ordre du composant
     * @param order
     */
    public void setOrder(int order)
    {
        this.order = order;
    }

    /**
     * Definit le statut du composant
     * @param show
     */
    public void setShow(boolean show)
    {
        this.show = show;
    }

    /**
     * Méthode de comparaison avec un autre composant
     * @param component
     * @return
     */
    @Override
    public int compareTo(Component component)
    {
        return this.order - component.order;
    }

}
