package fr.codelement.uml.utils;

public class StringUtils
{

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

        return fieldName;
    }

    public static String center(String s, int size)
    {
        return StringUtils.center(s, size, ' ');
    }

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
