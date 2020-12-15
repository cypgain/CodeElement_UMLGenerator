package fr.codelement.uml.metiers;

public class Relation 
{
    protected Entity entity1;
    protected Entity entity2;
    protected RelationType type;
    protected boolean show;
    
    public Relation(Entity entity1, Entity entity2, RelationType type)
    {
        this.entity1 = entity1;
        this.entity2 = entity2;
        this.type = type;
        this.show = true;
    }

    public void setShow(boolean show)
    {
        this.show = show;
    }

    public boolean isShow()
    {
        return this.show;
    }

    public Entity getEntity1()
    {
        return this.entity1;
    }

    public Entity getEntity2()
    {
        return this.entity2;
    }

    public String toString()
    {
        if (!this.show) return "";
        return this.entity1.getName() + " " + (this.type == RelationType.EXTEND ? "herite de" : "implemente") + " " + this.entity2.getName();
    }


}
