package fr.codeelement.uml.models.relations;

import fr.codeelement.uml.models.entities.Entity;

public class Association extends Relation
{

    protected char cardMin;
    protected char cardMax;
    private boolean compo;

    public Association(Entity entity1, Entity entity2, char cardMin, char cardMax)
    {
        super(RelationType.ASSOCIATION, entity1, entity2);
        this.cardMin = cardMin;
        this.cardMax = cardMax;
        this.compo = false;
    }

    public void setCompo(boolean comp)
    {
        if(!(this instanceof AssociationBi))
            this.compo = comp;
    }

    public char getCardMin()
    {
        return this.cardMin;
    }

    public char getCardMax()
    {
        return this.cardMax;
    }

    public String toString()
    {
        if(!this.show) return "";

        String constraint = "";
        if (!this.constraint.isEmpty())
        {
            for(Constraint c : this.constraint)
                constraint += "(" + c.getId() + ") {" + c.getName() + "}\n";
        }

        if(this.compo)
            return constraint + "Association " + this.id + " : composition\n\t" + this.entity2.getName() + " " + this.cardMin + ".." + this.cardMax + " -------<//> 1..1 " + this.entity1.getName() + "\n";

        return constraint + "Association " + this.id + " : unidirectionnelle\n\t" + this.entity1.getName() + " 0..* -------> " + this.cardMin + ".." + this.cardMax + " " + this.entity2.getName() + "\n";
    }

}
