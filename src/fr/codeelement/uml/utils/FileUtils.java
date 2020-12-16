package fr.codeelement.uml.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileUtils
{

    public static File getFile(String source)
    {
        File file = new File(source);
        return file.exists() ? file : null;
    }

    public static void compile(File source, boolean sourceIsFolder)
    {
        String cmd;
        if (sourceIsFolder)
        {
            System.out.println("Compilation du contenu du dossier " + source.getName());
            cmd = String.format("javac -parameters %s/*.java", source.getName());
        }
        else if (FileUtils.isJavaFile(source))
        {
            if (source.getName().contains(".class"))
                return;

            System.out.println("Compilation du fichier " + source.getPath());
            cmd = String.format("javac -parameters %s", source.getPath());
        }
        else
        {
            System.out.println("Le fichier que vous avez spécifié n'est pas un .class ou un .java");
            return;
        }

        try
        {
            Process p = Runtime.getRuntime().exec(cmd);
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            br.lines().forEach(System.out::println);
            p.waitFor();
        } catch (IOException | InterruptedException e) { e.printStackTrace(); }
    }

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

    public static String parseFileName(String fileName)
    {
        return fileName.replaceAll(".java", "").replaceAll(".class", "");
    }

    public static boolean isJavaFile(File source)
    {
        return source.getName().contains(".java") || source.getName().contains(".class");
    }

}
