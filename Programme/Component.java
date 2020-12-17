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

    public int getId()
    {
        return this.id;
    }

    public boolean isShow()
    {
        return this.show;
    }

    public int getOrder()
    {
        return this.order;
    }

    public void setOrder(int order)
    {
        this.order = order;
    }

    public void setShow(boolean show)
    {
        this.show = show;
    }

    @Override
    public int compareTo(Component component)
    {
        return this.order - component.order;
    }

}
