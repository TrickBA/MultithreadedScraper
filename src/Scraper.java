import Util.UrlHelper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by arein on 03/11/14.
 */
public class Scraper
{
    public static void main(String[] args)
    {
        if (args.length == 2)
        {
            try
            {
                try
                {
                    int maxNumberOfThreads = Integer.parseInt(args[1]);
                    if (maxNumberOfThreads < 1 || maxNumberOfThreads > 50)
                    {
                        System.out.println("The number of Threads should be between 1 and 50");
                        return;
                    }

                    URL url = new URL(args[0]);
                    ScrapeClient client = new ScrapeClient(url, maxNumberOfThreads);
                    client.scrape();
                }
                catch (NumberFormatException ex2)
                {
                    System.out.println("The number of threads you provided is not valid");
                }
            }
            catch (MalformedURLException ex)
            {
                System.out.println("The URL you provided is not valid");
            }
        }
        else
        {
            System.out.println("Please provide an URL as the first and the max number of threads as the second argument");
        }
    }
}