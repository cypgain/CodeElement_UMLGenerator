package fr.codelement.uml;

import fr.codelement.uml.metiers.*;
import fr.codelement.uml.utils.StringUtils;

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
    private Config config;
    private List<RelationAssociationBi> relationsAssocBi;

    public UMLGenerator(String source, String configFileName)
    {
        this.sourceFile = new File(source);
        this.entities = new ArrayList<>();
        this.relations = new ArrayList<>();
        this.relationsAssocBi = new ArrayList<>();

        if (!configFileName.equalsIgnoreCase(""))
            this.config = new Config(configFileName, this);

        if (!this.sourceFile.exists())
        {
            System.out.println("Le fichier/repertoire spécifié est invalide");
            this.generationType = GenerationType.INVALID;
            return;
        }

        this.generationType = (this.sourceFile.isDirectory()) ? GenerationType.FOLDER : GenerationType.FILE;
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
                    if (cls.isEnum() && field.getType().getSimpleName().contains(cls.getSimpleName()))
                        continue;
                    entity.addMember(new MemberAttribute(field.getName(), StringUtils.parseFieldName(field.getGenericType().getTypeName()), field.getModifiers()));
                }

                // Ajout des constantes d'énumeration
                if (cls.isEnum())
                {
                    for (Object obj : cls.getEnumConstants())
                        entity.addEnumConstant(new EnumConstantMember("" + obj));
                }

                // Ajout des méthodes
                for (Method method : cls.getDeclaredMethods())
                {
                    MemberMethod memberMethod = new MemberMethod(method.getName(), StringUtils.parseFieldName(method.getReturnType().getTypeName()), method.getModifiers());
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

        if (this.config != null)
            this.config.loadConfig();
    }

    public Entity getEntity(String name)
    {
        for(Entity entity : this.entities)
            if(entity.getName().equalsIgnoreCase(name))
                return entity;

        return null;
    }

    public Relation getRelation(Entity entity1, Entity entity2)
    {
        for (Relation relation : this.relations)
        {
            if (relation instanceof RelationAssociation && relation.isShow() && ((relation.getEntity1() == entity1 && relation.getEntity2() == entity2) || (relation.getEntity1() == entity2 && relation.getEntity2() == entity1)))
                return relation;
        }

        return null;
    }

    public RelationAssociationBi getAssociationBi(Entity e1, Entity e2)
    {
        for (RelationAssociationBi bi : this.relationsAssocBi)
        {
            if (bi.isShow() && ((bi.getR1().getEntity1() == e1 && bi.getR1().getEntity2() == e2) || (bi.getR1().getEntity1() == e2 && bi.getR1().getEntity2() == e1)))
                return bi;
        }

        return null;
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
            if(e.getType().equals(EntityType.CLASS))
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
                            m.setShow(false);
                            this.relations.add(new RelationAssociation(e, e2, '0', c));
                        }
                    }
                }
            }
        }
        this.assignAssociationBi();
    }

    public void assignAssociationBi()
    {
        ArrayList<Relation> deleteList = new ArrayList<>();
        for(Relation r : this.relations)
        {
            if(!deleteList.contains(r))
            {
                for(Relation r2 : this.relations)
                {
                    if(r instanceof RelationAssociation && r2 instanceof RelationAssociation)
                    {
                        if(r.getEntity1().equals(r2.getEntity2()) && r.getEntity2().equals(r2.getEntity1()))
                        {
                            this.relationsAssocBi.add(new RelationAssociationBi((RelationAssociation)r, (RelationAssociation)r2));
                            deleteList.add(r);
                            deleteList.add(r2);
                        }
                    }
                }
            }
        }
        for(Relation del : deleteList)
        {
            this.relations.remove(del);
        }
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
            if(r.isShow()) System.out.println(r + "\n");
        
        for(RelationAssociationBi r2 : this.relationsAssocBi)
            if(r2.isShow()) System.out.println(r2 + "\n");
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
            if(!new File(configFileName).exists())
            {
                System.out.println("Le fichier de configuration spécifié n'existe pas !");
                configFileName = "";
            }

        UMLGenerator generator = new UMLGenerator(args[args.length - 1], configFileName);
        generator.generate();
        generator.printEntities();
        generator.printRelations();
    }

}
