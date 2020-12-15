package fr.codelement.uml;

import fr.codelement.uml.metiers.Entity;
import fr.codelement.uml.metiers.Member;

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

        System.out.println(Arrays.toString(cmd));

        String err = "Erreur dans le fichier de configuration pour la ligne '" + line + "'";

        switch (cmd[0])
        {
            // Cacher quelque chose
            case "C":
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

                switch (cmd[1])
                {
                    case "A":
                        Member member = entity.getMember(cmd[3]);
                        if (member != null)
                        {
                            member.setShow(false);
                        }
                        else
                        {
                            System.out.println(err + " : Le membre n'existe pas");
                        }
                        break;

                    case "M":
                        
                        break;

                    case "R":

                        break;

                    default:
                        System.out.println(err);
                        break;
                }
                break;

            default:
                System.out.println(err);
                break;
        }
    }

}
