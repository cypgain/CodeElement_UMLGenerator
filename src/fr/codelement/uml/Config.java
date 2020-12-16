package fr.codelement.uml;

import fr.codelement.uml.metiers.Entity;
import fr.codelement.uml.metiers.Member;
import fr.codelement.uml.metiers.MemberAttribute;
import fr.codelement.uml.metiers.Relation;
import fr.codelement.uml.metiers.RelationAssociationBi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

public class Config
{

    private File file;
    private UMLGenerator umlGenerator;

    public Config(String configFileName, UMLGenerator umlGenerator)
    {
        this.file = new File(configFileName);
        this.umlGenerator = umlGenerator;
    }

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

    private void processConfigLine(String line)
    {
        if (line.length() <= 0)
            return;

        String[] cmd = line.split(" +");

        String err = "Erreur dans le fichier de configuration pour la ligne '" + line + "'";

        // UpperCase pour traiter les eventualités d'erreurs de String, il existe aussi LowerCase ou equalsIgnoreCase (mais faut faire des if)
        cmd[0] = cmd[0].toUpperCase();

        switch (cmd[0])
        {
            case "CACHER":
                this.hide(cmd, err);
                break;

            case "MULTIPLICITE":
                this.changeMult(cmd, err);
                break;

            case "ORDRE":
                this.changeOrder(cmd, err);
                break;

            default:
                System.out.println(err);
                break;
        }
    }

    private void changeOrder(String[] cmd, String err)
    {
        if (cmd.length < 4)
        {
            System.out.println(err);
            return;
        }

        switch (cmd[1])
        {
            case "E":
                Entity entity = this.umlGenerator.getEntity(cmd[2]);

                if (entity == null)
                {
                    System.out.println(err + " : L'entité spécifiée n'existe pas");
                    return;
                }

                entity.setOrder(Integer.parseInt(cmd[3]));
                break;

            case "R":
                if (cmd.length < 5)
                {
                    System.out.println(err);
                    return;
                }

                Entity entity1 = this.umlGenerator.getEntity(cmd[2]);
                Entity entity2 = this.umlGenerator.getEntity(cmd[3]);
                int order = Integer.parseInt(cmd[4]);

                Relation relation = this.umlGenerator.getRelation(entity1, entity2);

                if (relation == null)
                {
                    RelationAssociationBi relationAssociationBi = this.umlGenerator.getAssociationBi(entity1, entity2);

                    if (relationAssociationBi == null)
                    {
                        System.out.println(err + " : l'association n'existe pas");
                        return;
                    }
                    
                    relationAssociationBi.setOrder(order);
                    return;
                }

                relation.setOrder(order);
                break;

            default:
                System.out.println(err);
                break;
        }
    }

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

        Member member = entity.getMember(cmd[2]);
        if (member instanceof MemberAttribute) { ((MemberAttribute)member).setMult(cmd[3]); } else { System.out.println(err + " : L'attribut n'existe pas"); }
    }

    private void hide(String[] cmd, String err)
    {
        if (cmd.length < 4)
        {
            System.out.println(err);
            return;
        }

        Entity entity = this.umlGenerator.getEntity(cmd[2]);
        if (entity == null)
        {
            System.out.println(err + " : L'entité spécifiée n'existe pas");
            return;
        }

        Member member;
        switch (cmd[1])
        {
            case "A":
                if (cmd[3].equalsIgnoreCase("*"))
                {
                    entity.hideAllAttributes();
                }
                else
                {
                    member = entity.getMember(cmd[3]);
                    if (member != null) { member.setShow(false); } else { System.out.println(err + " : Le membre n'existe pas"); }
                }
                break;

            case "M":
                if (cmd[3].equalsIgnoreCase("*"))
                {
                    entity.hideAllMethods();
                }
                else
                {
                    member = entity.getMemberBySignature(cmd[3].trim().replaceAll(" ", ""));
                    if (member != null) { member.setShow(false); } else { System.out.println(err + " : Le membre n'existe pas"); }
                }
                break;

            case "R":
                Entity entity2 = this.umlGenerator.getEntity(cmd[3]);
                if (entity2 == null)
                {
                    System.out.println(err + " : l'entité n'existe pas");
                    return;
                }
                // Suppression de toutes les associations entre les deux entités
                if (cmd.length >= 5 && cmd[4].equalsIgnoreCase("*"))
                {
                    Relation relation = this.umlGenerator.getRelation(entity, entity2);
                    while (relation != null)
                    {
                        relation.setShow(false);
                        relation = this.umlGenerator.getRelation(entity, entity2);
                    }
                }
                else
                {
                    Relation relation = this.umlGenerator.getRelation(entity, entity2);
                    if (relation == null)
                    {
                        RelationAssociationBi assoBi = this.umlGenerator.getAssociationBi(entity, entity2);
                        if (assoBi == null)
                        {
                            System.out.println(err + " : impossible de trouver la relation");
                            return;
                        }
                        assoBi.setShow(false);
                        return;
                    }
                    relation.setShow(false);
                }
                break;

            default:
                System.out.println(err);
                break;
        }
                
    }

}
