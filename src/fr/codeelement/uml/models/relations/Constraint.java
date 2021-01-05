package fr.codeelement.uml.models.relations;

import java.util.ArrayList;

public class Constraint 
{
    private static int idConstraint = 1;

    private String name;
    private int id;
    private ArrayList<Relation> relationList;

    public Constraint(String name, ArrayList<Relation> relationList)
    {
        this.name = name;
        this.id = Constraint.idConstraint++;
        this.relationList = relationList;

        for(Relation r : relationList)
        {
            r.addConstraint(this);
        }
    }

    /**
     * Recupere le nom de la contrainte
     * @return
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Recupere l'id de la contrainte
     * @return
     */
    public int getId()
    {
        return this.id;
    }

}
