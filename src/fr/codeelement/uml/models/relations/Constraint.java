package fr.codeelement.uml.models.relations;

import java.util.ArrayList;

public class Constraint 
{
    private String name;
    private int id;
    private ArrayList<Relation> relationList;
    private static int idConstraint = 1;

    public Constraint(String name, ArrayList<Relation> relationList)
    {
        this.name = name;
        this.id = Constraint.idConstraint++;
        this.relationList = relationList;

        for(Relation r : relationList)
        {
            r.setConstraint(this);
        }
    }

    public String getName()
    {
        return this.name;
    }

    public int getId()
    {
        return this.id;
    }

}
