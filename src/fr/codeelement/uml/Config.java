package fr.codeelement.uml;

import fr.codeelement.uml.models.entities.Entity;
import fr.codeelement.uml.models.entities.members.Member;
import fr.codeelement.uml.models.entities.members.MemberAttribute;
import fr.codeelement.uml.models.relations.Relation;
import fr.codeelement.uml.models.relations.RelationType;
import fr.codeelement.uml.models.relations.Association;
import fr.codeelement.uml.models.relations.AssociationBi;
import fr.codeelement.uml.models.relations.Constraint;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * Classe qui permet de lire des modifications sur le diagramme UML, dans un fichier .config
 */
public class Config
{

    private File file;
    private UMLGenerator umlGenerator;

    public Config(String configFileName, UMLGenerator umlGenerator)
    {
        this.file = new File(configFileName);
        this.umlGenerator = umlGenerator;
    }

    /**
     * Traite la ligne actuelle dans le fichier
     * @param line
     */
    private void processConfigLine(String line)
    {
        if (line.length() <= 0)
            return;

        String[] cmd = line.split(" +");
        String err = "Erreur dans le fichier de configuration pour la ligne '" + line + "'";

        switch (cmd[0].toUpperCase())
        {
            case "CACHER":
                this.hide(cmd, err);
                break;

            case "MULTIPLICITE":
                this.changeMult(cmd, err);
                break;

            case "PROPRIETE":
                this.changerProp(cmd, err);
                break;

            case "COMPOSITION":
                this.addCompo(cmd, err);
                break;

            case "CONTRAINTE":
                this.addContr(cmd, err);
                break;

            case "ORDRE":
                this.changeOrder(cmd, err);
                break;

            default:
                System.out.println(err);
                break;
        }
    }

    /**
     * Méthode qui permet de cacher un attribut, une méthode ou une relation
     * @param cmd
     * @param err
     */
    private void hide(String[] cmd, String err)
    {
        if (cmd.length < 3)
        {
            System.out.println(err);
            return;
        }

        Member member;
        Entity entity;
        switch (cmd[1].toUpperCase())
        {
            case "ATTRIBUT":
                if (cmd.length < 4)
                {
                    System.out.println(err);
                    return;
                }

                entity = this.umlGenerator.getEntity(cmd[2]);

                if (entity == null)
                {
                    System.out.println(err + " : L'entité spécifiée n'existe pas");
                    return;
                }

                if (cmd[3].equalsIgnoreCase("*"))
                {
                    entity.hideAllAttributes();
                }
                else
                {
                    member = entity.getAttribute(cmd[3]);
                    if (member != null) { member.setShow(false); } else { System.out.println(err + " : L'attribut n'existe pas"); }
                }
                break;

            case "METHODE":
                if (cmd.length < 4)
                {
                    System.out.println(err);
                    return;
                }

                entity = this.umlGenerator.getEntity(cmd[2]);

                if (entity == null)
                {
                    System.out.println(err + " : L'entité spécifiée n'existe pas");
                    return;
                }

                if (cmd[3].equalsIgnoreCase("*"))
                {
                    entity.hideAllMethods();
                }
                else
                {
                    member = entity.getMethod(cmd[3].trim().replaceAll(" ", ""));
                    if (member != null) { member.setShow(false); } else { System.out.println(err + " : La méthode n'existe pas"); }
                }
                break;

            case "RELATION":
                Relation relation = this.umlGenerator.getRelation(Integer.parseInt(cmd[2]));

                if (relation == null)
                {
                    System.out.println(err + " : La relation n'existe pas");
                    return;
                }

                relation.setShow(false);
                break;

            default:
                System.out.println(err);
                break;
        }
    }

    /**
     * Méthode qui permet le changement de multiplicité
     * @param cmd
     * @param err
     */
    private void changeMult(String[] cmd, String err)
    {
        if (cmd.length < 4)
        {
            System.out.println(err);
            return;
        }

        Entity entity = this.umlGenerator.getEntity(cmd[1]);
        if (entity == null)
        {
            System.out.println(err + " : L'entité spécifiée n'existe pas");
            return;
        }

        MemberAttribute member = entity.getAttribute(cmd[2]);
        if (member != null) { member.setMult(" [" + cmd[3] + "]"); } else { System.out.println(err + " : L'attribut n'existe pas"); }
    }

    private void changerProp(String[] cmd, String err)
    {
        if (cmd.length < 4)
        {
            System.out.println(err);
            return;
        }

        Entity entity = this.umlGenerator.getEntity(cmd[2]);

        if (entity == null)
        {
            System.out.println(err + " : la entité n'existe pas");
            return;
        }

        Member member = entity.getAttribute(cmd[3]);
        if(member == null)
            member = entity.getMethod(cmd[3]);

        if (member == null)
        {
            System.out.println(err + " : le membre n'existe pas");
            return;
        }

        switch (cmd[1].toUpperCase())
        {
            case "AJOUTER":
                if (cmd.length < 5)
                {
                    System.out.println(err);
                    return;
                }

                member.addProperty(" {" + cmd[4] + "}");

                break;

            case "SUPPRIMER":
                member.removeProperty();
                break;

            default:
                System.out.println(err);
                break;
        }
    }

    /**
     * Méthode permettant l'ajout d'une composition entre 2 entités
     * @param cmd
     * @param err
     */
    private void addCompo(String[] cmd, String err)
    {
        if (cmd.length < 2)
        {
            System.out.println(err);
            return;
        }

        Relation relation = this.umlGenerator.getRelation(Integer.parseInt(cmd[1]));

        if (relation == null)
        {
            System.out.println(err + " : la relation n'existe pas");
            return;
        }

        if (!(relation instanceof Association) || relation instanceof AssociationBi)
        {
            System.out.println(err + " : la relation n'est pas unidirectionnelle");
            return;
        }

        ((Association)relation).setCompo(true);
    }

    /**
     * Méthode permettant l'ajout de contraintes
     * @param cmd
     * @param err
     */
    private void addContr(String[] cmd, String err)
    {
        if (cmd.length < 3)
        {
            System.out.println(err);
            return;
        }

        ArrayList<Relation> relationList = new ArrayList<>();
        for(int i = 2; i < cmd.length; i++)
        {
            Relation relation = this.umlGenerator.getRelation(Integer.parseInt(cmd[i]));
            
            if (relation == null)
            {
                System.out.println(err + " : la relation n'existe pas");
            }
            else
            {
                relationList.add(relation);
            }
        }

        RelationType type = relationList.get(0).getType();
        for(Relation r : relationList)
        {
            if(!r.getType().equals(type))
            {
                System.out.println(err + " : les relations n'ont pas le même type");
                return;
            }
        }

        new Constraint(cmd[1], relationList);
    }

    /**
     * Méthode permettant de changer l'ordre d'un composant
     * @param cmd
     * @param err
     */
    private void changeOrder(String[] cmd, String err)
    {
        if (cmd.length < 4)
        {
            System.out.println(err);
            return;
        }

        switch (cmd[1].toUpperCase())
        {
            case "ENTITE":
                Entity entity = this.umlGenerator.getEntity(cmd[2]);

                if (entity == null)
                {
                    System.out.println(err + " : la entité n'existe pas");
                    return;
                }

                entity.setOrder(Integer.parseInt(cmd[3]));
                break;

            case "RELATION":
                Relation relation = this.umlGenerator.getRelation(Integer.parseInt(cmd[2]));

                if (relation == null)
                {
                    System.out.println(err + " : la relation n'existe pas");
                    return;
                }

                relation.setOrder(Integer.parseInt(cmd[3]));
                break;

            default:
                System.out.println(err);
                break;
        }
    }

    /**
     * Méthode permettant la lecture d'un fichier de configuration
     */
    public void loadConfig()
    {
        System.out.println("Chargement de la configuration");

        try
        {
            Scanner sc = new Scanner(new FileInputStream(this.file));

            String line;
            while (sc.hasNext())
            {
                line = sc.nextLine();

                if (line.length() >= 2 && line.trim().startsWith("//"))
                    continue;

                this.processConfigLine(line);
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

}
