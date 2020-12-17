package fr.codeelement.uml.models.relations;

import fr.codeelement.uml.models.Component;
import fr.codeelement.uml.models.entities.Entity;

public class Relation extends Component
{

    protected RelationType type;
    protected Entity entity1;
    protected Entity entity2;

    public Relation(RelationType type, Entity entity1, Entity entity2)
    {
        this.type = type;
        this.entity1 = entity1;
        this.entity2 = entity2;
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
        if (!this.show || this.type == RelationType.ASSOCIATION) return "";
        if (this.type == RelationType.INTERNAL_CLASS) return this.entity1.getName() + " <+>------------ " + this.entity2.getName() +  " (" + this.id + ")\n";
        return this.entity1.getName() + " " + (this.type == RelationType.EXTENDS ? "herite de" : "implemente") + " " + this.entity2.getName() +  " (" + this.id + ")\n";
    }

}
