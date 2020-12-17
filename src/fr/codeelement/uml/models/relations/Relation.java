package fr.codeelement.uml.models.relations;

import fr.codeelement.uml.models.Component;
import fr.codeelement.uml.models.entities.Entity;

public class Relation extends Component
{

    protected RelationType type;
    protected Entity entity1;
    protected Entity entity2;
    protected String contr;

    public Relation(RelationType type, Entity entity1, Entity entity2)
    {
        this.type = type;
        this.entity1 = entity1;
        this.entity2 = entity2;
        this.contr = "";
    }

    public void setContr(String c)
    {
        this.contr = c;
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

        String contr = "";
        if(!this.contr.equals(""))
            contr = "{" + this.contr + "} ";
        
        return contr + this.entity1.getName() + " " + (this.type == RelationType.EXTENDS ? "herite de" : "implemente") + " " + this.entity2.getName() +  " (" + this.id + ")\n";
    }

}
