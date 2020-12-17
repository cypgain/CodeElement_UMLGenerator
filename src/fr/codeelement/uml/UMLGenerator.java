package fr.codeelement.uml;

import fr.codeelement.uml.models.Component;
import fr.codeelement.uml.models.entities.Entity;
import fr.codeelement.uml.models.entities.EntityType;
import fr.codeelement.uml.models.entities.members.MemberAttribute;
import fr.codeelement.uml.models.relations.Association;
import fr.codeelement.uml.models.relations.AssociationBi;
import fr.codeelement.uml.models.relations.Relation;
import fr.codeelement.uml.models.relations.RelationType;
import fr.codeelement.uml.utils.FileUtils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UMLGenerator
{

    private File source;
    private boolean sourceIsFolder;
    private List<Component> components;
    private Config config;

    public UMLGenerator(File source, String configFileName)
    {
        this.source = source;
        this.sourceIsFolder = this.source.isDirectory();
        this.components = new ArrayList<>();

        if (!configFileName.equalsIgnoreCase(""))
            this.config = new Config(configFileName, this);
    }

    public void generate()
    {
        // Compilation du fichier ou du contenu du repertoire source
        FileUtils.compile(this.source, this.sourceIsFolder);

        // Recuperation du dossier parent si la source est un fichier, sinon c'est le dossier
        File sourceParent = new File(this.sourceIsFolder ? this.source.getName() : this.source.getParent());

        try
        {
            URL url = sourceParent.toURI().toURL();
            URL[] urls = new URL[] { url };
            ClassLoader cl = new URLClassLoader(urls);

            List<String> filesToGenerate = FileUtils.getFilesToGenerate(this.source, this.sourceIsFolder);

            for (String fileName : filesToGenerate)
            {
                // Création de l'entité à partir d'un fichier
                Class<?> entityClass = cl.loadClass(fileName);
                Entity entity = new Entity(entityClass);
                this.components.add(entity);
            }
        }
        catch (MalformedURLException | ClassNotFoundException e) { e.printStackTrace(); }


        this.generateExtends();
        this.generateImplements();
        this.generateAssocations();
        this.generateInternalClassesRelations();

        if(this.config != null)
            this.config.loadConfig();
    }

    private void generateExtends()
    {
        for(Entity entity : this.getEntities())
        {
            if(entity.getSuperClass().isEmpty())
                continue;

            Entity e = this.getEntity(entity.getSuperClass());
            if(e != null)
            {
                Relation relation = new Relation(RelationType.EXTENDS, entity, e);
                this.components.add(relation);
            }
        }
    }

    private void generateImplements()
    {
        List<Entity> entities = this.getEntities();
        for(Entity entity : entities)
        {
            for(String implementation : entity.getImplement())
            {
                Entity e = this.getEntity(implementation);

                if(e != null)
                {
                    Relation relation = new Relation(RelationType.IMPLEMENTS, entity, e);
                    this.components.add(relation);
                }
            }
        }
    }

    private void generateAssocations()
    {
        List<Entity> entities = this.getEntities();
        for(Entity e : entities)
        {
            if(e.getType() == EntityType.CLASS)
            {
                for(MemberAttribute attr : e.getAttributes())
                {
                    char card = '1';
                    String type;
                    // L'attribut est un tableau
                    if(attr.getType().contains("[]"))
                    {
                        card = '*';
                        type = attr.getType().substring(0, attr.getType().indexOf("["));
                    }
                    // L'attribut est une collection
                    else if(attr.getType().contains("<"))
                    {
                        card = '*';
                        type = attr.getType().substring(attr.getType().indexOf("<") + 1, attr.getType().length() - 1);
                    }
                    else
                    {
                        type = attr.getType();
                    }

                    for(Entity e2 : entities)
                    {
                        if(e2.getName().equals(type))
                        {
                            attr.setShow(false);
                            Association association = new Association(e, e2, '0', card);
                            this.components.add(association);
                        }
                    }
                }
            }
        }
        this.groupAssociations();
    }

    private void groupAssociations()
    {
        List<Association> associations = this.getAssociations();

        for (Association a : new ArrayList<>(associations))
        {
            if (!associations.contains(a))
                continue;

            for (Association b : new ArrayList<>(associations))
            {
                if ((!associations.contains(b)) || a == b)
                    continue;

                if (a.getEntity1() == b.getEntity2() && a.getEntity2() == b.getEntity1() && a.getEntity1() != a.getEntity2())
                {
                    this.components.add(new AssociationBi(a.getEntity1(), a.getEntity2(), a.getCardMin(), a.getCardMax(), b.getCardMin(), b.getCardMax()));

                    associations.remove(a);
                    associations.remove(b);
                    this.components.remove(a);
                    this.components.remove(b);
                }
            }
        }

        for (Association a : new ArrayList<>(associations))
        {
            if (!associations.contains(a))
                continue;

            if (a.getEntity1() == a.getEntity2())
            {
                this.components.add(new AssociationBi(a.getEntity1(), a.getEntity2(), '0', '1', '0', '*'));
                this.components.remove(a);
                associations.remove(a);
            }
        }
    }

    public void generateInternalClassesRelations()
    {
        for (Entity entity : this.getEntities())
        {
            Class<?>[] classes = entity.getEntityClass().getDeclaredClasses();

            for (Class<?> cls : classes)
            {
                Entity e = this.getEntity(cls.getSimpleName());
                if (e != null)
                {
                    this.components.add(new Relation(RelationType.INTERNAL_CLASS, entity, e));
                }
            }
        }
    }

    public List<Entity> getEntities()
    {
        List<Entity> entities = new ArrayList<>();

        for (Component component : this.components)
        {
            if (component instanceof Entity)
                entities.add((Entity)component);
        }

        return entities;
    }

    public Entity getEntity(String name)
    {
        for (Component component : this.components)
        {
            if (component instanceof Entity && ((Entity) component).getName().equalsIgnoreCase(name))
                return (Entity)component;
        }
        return null;
    }

    public List<Association> getAssociations()
    {
        List<Association> associations = new ArrayList<>();

        for (Component component : this.components)
        {
            if (component instanceof Association && !(component instanceof AssociationBi))
                associations.add((Association) component);
        }

        return associations;
    }

    public Relation getRelation(int id)
    {
        for (Component component : this.components)
        {
            if (component instanceof Relation && component.getId() == id)
                return (Relation)component;
        }
        return null;
    }

    public String toString()
    {
        String str = "";

        Collections.sort(this.components);

        for (Component component : this.components)
        {
            if (!(component.toString().isEmpty() && component.toString().isBlank()))
                str += component + "\n";
        }

        return str;
    }

    public static void main(String[] args)
    {
        if (args.length < 1)
        {
            System.out.println("Utilisation : java UMLGenerator <repertoire/fichier>");
            return;
        }

        File source = FileUtils.getFile(args[args.length - 1]);

        if (source == null)
        {
            System.out.println("Impossible de trouver le répertoire ou le fichier spécifié");
            return;
        }

        String parameters = "";

        if (args.length >= 2)
            for (int i = 0; i < args.length - 1; i++)
                parameters += args[i];

        String configFileName = "";
        String[] paramsArr = parameters.replaceAll(" ", "").split("-");

        if (paramsArr.length > 0)
        {
            for(int i = 0; i < paramsArr.length; i++)
            {
                if (paramsArr[i].equalsIgnoreCase(""))
                    continue;

                if (paramsArr[i].contains("config:"))
                {
                    String[] config = paramsArr[i].split(":");
                    configFileName = config[1];
                }
                else
                {
                    System.out.println("Mauvais usage du parametre " + paramsArr[i]);
                }
            }
        }

        System.out.println("Fichier de configuration : " + (configFileName.equalsIgnoreCase("") ? "Aucun" : configFileName));

        if (!configFileName.equalsIgnoreCase(""))
        {
            if(!new File(configFileName).exists())
            {
                System.out.println("Le fichier de configuration spécifié n'existe pas !");
            }
        }

        UMLGenerator generator = new UMLGenerator(source, configFileName);
        generator.generate();
        System.out.println("\n" + generator);
    }

}