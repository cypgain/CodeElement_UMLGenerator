package fr.codeelement.uml.utils;

import java.util.HashMap;
import java.util.Map;

public class StringUtils
{

    private static final HashMap<String, String> JAVA_TYPES = new HashMap<>()
    {{
        put("byte", "entier");
        put("short", "entier");
        put("int", "entier");
        put("long", "entier");
        put("char", "caractère");
        put("String", "Chaine");
        put("float", "réel");
        put("double", "réel");
        put("boolean", "booléen");
    }};

    /**
     * Permet de transformer le nom d'un type java en francais
     * @param type
     * @return String
     */
    public static String convertJavaTypeToFrench(String type)
    {
        for(Map.Entry<String, String> entry : StringUtils.JAVA_TYPES.entrySet())
        {
            if (entry.getKey().equals(type))
            {
                return entry.getValue();
            }
        }
        return type;
    }

    /**
     * Permet de formatter le nom passer en parametre en retirant le nom des packages
     * @param fieldName
     * @return
     */
    public static String parseFieldName(String fieldName)
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

        return StringUtils.convertJavaTypeToFrench(fieldName);
    }

    /**
     * Permet de centrer du texte
     * @param s
     * @param size
     * @return
     */
    public static String center(String s, int size)
    {
        return StringUtils.center(s, size, ' ');
    }

    /**
     * Permet de centrer du texte
     * @param s
     * @param size
     * @param pad
     * @return
     */
    public static String center(String s, int size, char pad)
    {
        if (s == null || size <= s.length())
            return s;

        StringBuilder sb = new StringBuilder(size);
        for (int i = 0; i < (size - s.length()) / 2; i++)
        {
            sb.append(pad);
        }
        sb.append(s);
        while (sb.length() < size)
        {
            sb.append(pad);
        }
        return sb.toString();
    }

}
