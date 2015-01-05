package Util;

import java.net.URL;
import java.util.regex.Pattern;

/**
 * Created by arein on 04/11/14.
 */
public class UrlHelper
{
    public static String getHost(URL u)
    {
        String host = u.getHost();
        String[] parts = host.split(Pattern.quote("."));
        return parts[parts.length - 2] + "." + parts[parts.length - 1];
    }

    public static boolean isEmail(URL u)
    {
        return u.toString().contains("mailto:");
    }
}
