package fr.codeelement.uml.models.relations;

import fr.codeelement.uml.models.entities.Entity;

public class Association extends Relation
{

    protected char cardMin;
    protected char cardMax;

    public Association(Entity entity1, Entity entity2, char cardMin, char cardMax)
    {
        super(RelationType.ASSOCIATION, entity1, entity2);
        this.cardMin = cardMin;
        this.cardMax = cardMax;
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
        return "Association " + this.id + " : unidirectionnelle\n\t" + this.entity1.getName() + " 0..* -------> " + this.cardMin + ".." + this.cardMax + " " + this.entity2.getName() + "\n";
    }

}
