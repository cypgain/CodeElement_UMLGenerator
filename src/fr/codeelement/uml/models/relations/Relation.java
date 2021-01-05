package fr.codeelement.uml.models.relations;

import fr.codeelement.uml.models.Component;
import fr.codeelement.uml.models.entities.Entity;

import java.util.ArrayList;

public class Relation extends Component
{

    protected RelationType type;
    protected Entity entity1;
    protected Entity entity2;
    protected ArrayList<Constraint> constraint;

    public Relation(RelationType type, Entity entity1, Entity entity2)
    {
        this.type = type;
        this.entity1 = entity1;
        this.entity2 = entity2;
        this.constraint = new ArrayList<>();
    }

    /**
     * Ajouter une contrainte à une relation
     * @param c
     */
    public void addConstraint(Constraint c)
    {
        this.constraint.add(c);
    }

    /**
     * Recupere le type de la relation
     * @return
     */
    public RelationType getType()
    {
        return this.type;
    }

    /**
     * Recupere l'entité 1 de la relation
     * @return
     */
    public Entity getEntity1()
    {
        return this.entity1;
    }

    /**
     * Recupere l'entité 2 de la relation
     * @return
     */
    public Entity getEntity2()
    {
        return this.entity2;
    }

    /**
     * Retourne une chaine de caractere de la relation
     * @return
     */
    public String toString()
    {
        if (!this.show || this.type == RelationType.ASSOCIATION) return "";
        
        String constraint = "";
        if (!this.constraint.isEmpty())
        {
            for(Constraint c : this.constraint)
                constraint += "(" + c.getId() + ") {" + c.getName() + "}\n";
        }
        
        if (this.type == RelationType.INTERNAL_CLASS) return constraint + this.entity1.getName() + " <+>------------ " + this.entity2.getName() +  " (" + this.id + ")\n";
        return constraint + this.entity1.getName() + " " + (this.type == RelationType.EXTENDS ? "herite de" : "implemente") + " " + this.entity2.getName() +  " (" + this.id + ")\n";
    }

}
