package fr.codeelement.uml.models.relations;

import fr.codeelement.uml.models.entities.Entity;

public class AssociationBi extends Association
{

    private char cardMin2;
    private char cardMax2;

    public AssociationBi(Entity entity1, Entity entity2, char cardMin, char cardMax, char cardMin2, char cardMax2)
    {
        super(entity1, entity2, cardMin, cardMax);
        this.cardMin2 = cardMin2;
        this.cardMax2 = cardMax2;
    }

    /**
     * Recupere la cardinalité min 2
     * @return
     */
    public char getCardMin2()
    {
        return this.cardMin2;
    }

    /**
     * Recupere la cardinalité max 2
     * @return
     */
    public char getCardMax2()
    {
        return this.cardMax2;
    }

    /**
     * Retourne une chaine de caractere contenant l'association bidirectionnelle
     * @return
     */
    public String toString()
    {
        if (!this.show) return "";
        String type = "bidirectionnelle";

        String constraint = "";
        if (!this.constraint.isEmpty())
        {
            for(Constraint c : this.constraint)
                constraint += "(" + c.getId() + ") {" + c.getName() + "}\n";
        }

        if(entity1.getName().equalsIgnoreCase(entity2.getName()))
            type = "réflexive";

        return constraint + "Association " + this.id + " : " + type + "\n\t" + this.entity1.getName() + " " + this.cardMin2 + ".." + this.cardMax2 + " <-------> " + this.cardMin + ".." + this.cardMax + " " + this.entity2.getName() + "\n";
    }

}
