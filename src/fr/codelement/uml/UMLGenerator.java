package fr.codelement.uml;

import fr.codelement.uml.metiers.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.AnnotatedType;
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
    private List<Relation> relations;

    public UMLGenerator(String source)
    {
        this.sourceFile = new File(source);
        this.entities = new ArrayList<>();
        this.relations = new ArrayList<>();

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
                EntityType entityType = this.getEntityType(cls);
                Entity entity = new Entity(cls.getSimpleName(), entityType, entityType == EntityType.CLASS ? cls.getSuperclass().getSimpleName() : "");


                for(AnnotatedType inter : cls.getAnnotatedInterfaces())
                {
                    entity.addImplementation(inter.getType().getTypeName());
                }

                // Ajout des attributs
                for (Field field : cls.getDeclaredFields())
                {
                    entity.addMember(new MemberAttribute(field.getName(), this.parseFieldName(field.getGenericType().getTypeName()), field.getModifiers()));
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

        this.generateExtends();
        this.generateImplementations();
        this.assignAssociations();
    }

    private String parseFieldName(String fieldName)
    {
        if (fieldName.contains("<"))
        {
            String[] splitted = fieldName.split("<");
            if (splitted.length < 2)
                return fieldName;

            splitted[0] = splitted[0].replaceAll("^.+[.]", "");
            splitted[1] = splitted[1].replaceAll("^.+[.]", "");
            fieldName = splitted[0] + "<" + splitted[1];
        }
        else
        {
            fieldName = fieldName.replaceAll("^.+[.]", "");
        }

        return fieldName;
    }

    private void generateExtends()
    {
        for(Entity entity : this.entities)
        {
            if(entity.getSuperClass().equalsIgnoreCase(""))
                continue;

            Entity e = this.getEntity(entity.getSuperClass());
            if(e != null)
            {
                this.relations.add(new Relation(entity, e, RelationType.EXTEND));
                break;
            }
        }
    }

    private void generateImplementations()
    {
        for(Entity entity : this.entities)
        {
            if(entity.getSuperClass().equalsIgnoreCase(""))
                continue;

            for(String implementation : entity.getImplementations())
            {
                Entity e = this.getEntity(implementation);

                if(e != null)
                {
                    this.relations.add(new Relation(entity, e, RelationType.IMPLEMENT));
                }
            }
        }
    }

    private void assignAssociations()
    {
        for(Entity e : this.entities)
        {
            for(MemberAttribute m : e.getMemberAttributeList())
            {
                char c = '1';
                String type;
                if(m.getType().contains("[]"))
                {
                    c = '*';
                    type = m.getType().substring(0, m.getType().indexOf("["));
                }
                else if(m.getType().contains("<"))
                {
                    c = '*';
                    type = m.getType().substring(m.getType().indexOf("<")+1, m.getType().length()-1);
                }
                else
                {
                    type = m.getType();
                }
                for(Entity e2 : this.entities)
                {
                    if(e2.getName().equals(type))
                    {
                        this.relations.add(new RelationAssociation(e, e2, '0', c));
                    }
                }
            }
        }
    }

    private Entity getEntity(String name)
    {
        for(Entity entity : this.entities)
            if(entity.getName().equalsIgnoreCase(name))
                return entity;

        return null;
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

    public void printRelations()
    {
        for(Relation r : this.relations)
            System.out.println(r);
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
        generator.printRelations();
    }

}
