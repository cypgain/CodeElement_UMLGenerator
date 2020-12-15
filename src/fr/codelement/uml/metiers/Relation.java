package fr.codelement.uml.metiers;

public class Relation 
{
    protected Entity entity1;
    protected Entity entity2;
    private RelationType type;
    
    public Relation(Entity entity1, Entity entity2, RelationType type)
    {
        this.entity1 = entity1;
        this.entity2 = entity2;
        this.type = type;
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
        return entity1.getName() + " " + (type == RelationType.EXTEND ? "herite de" : "implemente") + " " + entity2.getName();
    }


}
