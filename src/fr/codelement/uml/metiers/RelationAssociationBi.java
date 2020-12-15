package fr.codelement.uml.metiers;

public class RelationAssociationBi
{
    private RelationAssociation r1;
    private RelationAssociation r2;
    private boolean show;

    public RelationAssociationBi(RelationAssociation r1, RelationAssociation r2)
    {
        this.r1 = r1;
        this.r2 = r2;
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

    public RelationAssociation getR1()
    {
        return this.r1;
    }

    public RelationAssociation getR2()
    {
        return this.r2;
    }

    public String toString()
    {
        if (!this.show) return "";
        String type = "bidirectionnelle";

        if(r1.getEntity1().getName().equals(r2.getEntity1().getName()))
            type = "reflexive";

        return "Association : " + type + "\n\t" + r1.getEntity1().getName() + " " + r2.getCardMin() + ".." + r2.getCardMax() + " <-------> " + r1.getCardMin() + ".." + r1.getCardMax() + " " + r2.getEntity1().getName();
    }
}
