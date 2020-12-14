package fr.codelement.uml;

import fr.codelement.uml.metiers.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class UMLGenerator
{

    private GenerationType generationType;
    private File sourceFile;
    private List<Entity> entities;

    public UMLGenerator(String source)
    {
        this.sourceFile = new File(source);
        this.entities = new ArrayList<>();

        if (!this.sourceFile.exists())
        {
            System.out.println("Le fichier/repertoire spécifié est invalide");
            this.generationType = GenerationType.INVALID;
            return;
        }

        this.generationType = (this.sourceFile.isDirectory()) ? GenerationType.FOLDER : GenerationType.FILE;
    }

    private boolean isJavaFile()
    {
        return this.sourceFile.getName().contains(".java") || this.sourceFile.getName().contains(".class");
    }

    private void compile()
    {
        String cmd;
        if (this.generationType == GenerationType.FOLDER)
        {
            System.out.println("Compilation du contenu du dossier " + this.sourceFile.getName());
            cmd = String.format("javac -parameters %s/*.java", this.sourceFile.getName());
        }
        else if (this.isJavaFile())
        {
            if (this.sourceFile.getName().contains(".class"))
                return;

            System.out.println("Compilation du fichier " + this.sourceFile.getPath());
            cmd = String.format("javac -parameters %s", this.sourceFile.getPath());
        }
        else
        {
            System.out.println("Le fichier que vous avez spécifié n'est pas un .class ou un .java");
            return;
        }

        try
        {
            Process p = Runtime.getRuntime().exec(cmd);
            p.waitFor();
        } catch (IOException | InterruptedException e) { e.printStackTrace(); }
    }

    public void generate()
    {
        if (this.generationType == GenerationType.INVALID)
            return;

        this.compile();
        File parent = new File(this.generationType == GenerationType.FOLDER ? this.sourceFile.getName() : this.sourceFile.getParent());

        try
        {
            URL url = parent.toURI().toURL();
            URL[] urls = new URL[] { url };
            ClassLoader cl = new URLClassLoader(urls);

            List<String> filesGenerate = this.getFilesGenerate();

            for (String p : filesGenerate)
            {
                Class cls = cl.loadClass(p);

                // Creation de l'entité
                Entity entity = new Entity(cls.getSimpleName(), this.getEntityType(cls));

                // Ajout des attributs
                for (Field field : cls.getDeclaredFields())
                {
                    entity.addMember(new MemberAttribute(field.getName(), field.getType().getSimpleName(), field.getModifiers()));
                }

                // Ajout des méthodes
                for (Method method : cls.getDeclaredMethods())
                {
                    MemberMethod memberMethod = new MemberMethod(method.getName(), method.getReturnType().getSimpleName(), method.getModifiers());
                    entity.addMember(memberMethod);

                    for (Parameter param : method.getParameters())
                        memberMethod.addArgument(param);
                }

                this.entities.add(entity);
            }


        }
        catch (MalformedURLException | ClassNotFoundException e) { e.printStackTrace(); }
    }

    private List<String> getFilesGenerate()
    {
        List<String> filesGenerate = new ArrayList<>();

        if (this.generationType == GenerationType.FILE)
        {
            filesGenerate.add(this.parseFileName(this.sourceFile.getName()));
        }
        else
        {
            String[] files = this.sourceFile.list();

            if (files == null)
            {
                System.out.println("Il n'y a aucun fichiers dans le dossier selectionné");
                return filesGenerate;
            }

            for (int i = 0; i < files.length; i++)
            {
                if (files[i].contains(".class"))
                    filesGenerate.add(this.parseFileName(files[i]));
            }
        }

        return filesGenerate;
    }

    public void printEntities()
    {
        for (Entity e : this.entities)
            System.out.println(e);
    }

    private MemberVisibility getMemberVisibility(int modifier)
    {
        return modifier == 1 ? MemberVisibility.PUBLIC : modifier == 2 ? MemberVisibility.PRIVATE : MemberVisibility.PROTECTED;
    }

    private EntityType getEntityType(Class entity)
    {
        return entity.isInterface() ? EntityType.INTERFACE : entity.isEnum() ? EntityType.ENUM : EntityType.CLASS;
    }

    private String parseFileName(String fileName)
    {
        return fileName.replaceAll(".java", "").replaceAll(".class", "");
    }

    public static void main(String[] args)
    {
        if (args.length < 1)
        {
            System.out.println("Utilisation : java UMLGenerator <repertoire/fichier>");
            return;
        }

        UMLGenerator generator = new UMLGenerator(args[0]);
        generator.generate();
        generator.printEntities();
    }

}
