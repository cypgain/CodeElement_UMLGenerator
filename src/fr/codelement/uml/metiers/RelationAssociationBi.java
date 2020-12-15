package fr.codelement.uml.metiers;

public class RelationAssociationBi
{
    private RelationAssociation r1;
    private RelationAssociation r2;

    public RelationAssociationBi(RelationAssociation r1, RelationAssociation r2)
    {
        this.r1 = r1;
        this.r2 = r2;
    }

    public String toString()
    {
        return "Association : bidirectionnelle\n\t" + r1.getEntity1().getName() + " " + r2.getCardMin() + ".." + r2.getCardMax() + " <-------> " + r1.getCardMin() + ".." + r1.getCardMax() + " " + r2.getEntity1().getName();
    }
}
