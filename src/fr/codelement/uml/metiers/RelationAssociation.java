package fr.codelement.uml.metiers;

public class RelationAssociation extends Relation
{
    private char cardMin;
    private char cardMax;

    public RelationAssociation(Entity entity1, Entity entity2, char cardMin, char cardMax)
    {
        super(entity1, entity2, RelationType.ASSOCIATION);
        this.cardMin = cardMin;
        this.cardMax = cardMax;
    }

    @Override
    public String toString()
    {
        return "Association : unidirectionnelle\n\t" + this.entity1.getName() + " 1..1 -------> " + this.cardMin + ".." + this.cardMax + " " + this.entity2.getName();
    }
}
