package fr.codeelement.uml.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileUtils
{

    /**
     * Permet de recuperer un fichier en fonction de son nom
     * @param source
     * @return
     */
    public static File getFile(String source)
    {
        File file = new File(source);
        return file.exists() ? file : null;
    }

    /**
     * Permet de compiler un fichier ou contenu d'un dossier
     * @param source
     * @param sourceIsFolder
     */
    public static void compile(File source, boolean sourceIsFolder)
    {
        String cmd;
        if (sourceIsFolder)
        {
            System.out.println("Compilation du contenu du dossier " + source.getName());
            cmd = String.format("javac -encoding utf8 -parameters %s/*.java", source.getName());
        }
        else if (FileUtils.isJavaFile(source))
        {
            if (source.getName().contains(".class"))
                return;

            System.out.println("Compilation du fichier " + source.getPath());
            cmd = String.format("javac -encoding utf8 -parameters %s", source.getPath());
        }
        else
        {
            System.out.println("Le fichier que vous avez spécifié n'est pas un .class ou un .java");
            return;
        }

        try
        {
            System.out.println("Cmd: " + cmd);
            if (System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd", "/c", cmd).start().waitFor();
            else
                new ProcessBuilder("/bin/bash", "-c", cmd).start().waitFor();
        } catch (IOException | InterruptedException e) { e.printStackTrace(); }
    }

    /**
     * Retourne la liste des fichiers .class
     * @param source
     * @param sourceIsFolder
     * @return
     */
    public static List<String> getFilesToGenerate(File source, boolean sourceIsFolder)
    {
        List<String> filesGenerate = new ArrayList<>();

        if (!sourceIsFolder)
        {
            filesGenerate.add(FileUtils.parseFileName(source.getName()));
        }
        else
        {
            String[] files = source.list();

            if (files == null)
            {
                System.out.println("Il n'y a aucun fichiers dans le dossier selectionné");
                return filesGenerate;
            }

            for (String file : files)
            {
                if (file.contains(".class"))
                    filesGenerate.add(FileUtils.parseFileName(file));
            }
        }

        return filesGenerate;
    }

    /**
     * Permet de formater le nom du fichier
     * @param fileName
     * @return
     */
    public static String parseFileName(String fileName)
    {
        return fileName.replaceAll(".java", "").replaceAll(".class", "");
    }

    /**
     * Permet de savoir si le fichier est un fichier java
     * @param source
     * @return
     */
    public static boolean isJavaFile(File source)
    {
        return source.getName().contains(".java") || source.getName().contains(".class");
    }

}
