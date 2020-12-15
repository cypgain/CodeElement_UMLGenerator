package fr.codelement.uml.metiers;

public abstract class Component implements Comparable<Component>
{

    protected int order;

    public Component()
    {
        this.order = 999;
    }

    public int getOrder()
    {
        return order;
    }

    public void setOrder(int order)
    {
        this.order = order;
    }

    @Override
    public int compareTo(Component component)
    {
        return this.order - component.order;
    }

}
