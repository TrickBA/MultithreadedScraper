import Threading.NotifyingThread;
import Util.UrlHelper;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.URL;

/**
 * Created by arein on 03/11/14.
 */
public class ScrapeTask extends NotifyingThread
{
    private UrlRepository repo;
    private String host;
    private String url;

    public ScrapeTask(UrlRepository repo, String host, String url)
    {
        this.repo = repo;
        this.host = host;
        this.url = url;
    }

    @Override
    public void doRun()
    {
        try
        {
            Document doc = Jsoup.connect(this.url).get();
            this.getLinks(doc); //
            this.getAssets(doc);
        }
        catch (UnknownHostException ex)
        {
            // Swallow
        }
        catch (UnsupportedMimeTypeException ex)
        {
            // Swallow
        }
        catch (HttpStatusException ex)
        {
            // Swallow the exception
        }
        catch (IOException ex)
        {
            Logger.getLogger(Scraper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getLinks(Document doc)
    {
        // Get Links
        Elements anchors = doc.select("a");
        for (Element e : anchors)
        {
            e.attr("href", e.absUrl("href"));
            try
            {
                URL u = new URL(e.attr("href"));
                if (UrlHelper.getHost(u).equals(this.host)) repo.add(e.attr("href"));
            } catch (MalformedURLException ex2) {
            } catch (ArrayIndexOutOfBoundsException ex3) {
            }
        }
    }

    public void getAssets(Document doc)
    {
        this.getScripts(doc);
        this.getStylesheets(doc);
        this.getImages(doc);
    }

    public void getScripts(Document doc)
    {
        Elements scripts = doc.select("script");
        for (Element e : scripts)
        {
            e.attr("src", e.absUrl("src"));
            if (!e.attr("src").equals(""))
            {
                this.repo.addAsset(this.url, e.attr("src"));
            }
        }
    }

    public void getStylesheets(Document doc)
    {
        Elements links = doc.select("link");
        for (Element e : links)
        {
            e.attr("href", e.absUrl("href"));
            if (!e.attr("href").equals(""))
            {
                this.repo.addAsset(this.url, e.attr("href"));
            }
        }
    }

    public void getImages(Document doc)
    {
        Elements images = doc.select("img");
        for (Element e : images)
        {
            e.attr("src", e.absUrl("src"));
            if (!e.attr("src").equals(""))
            {
                this.repo.addAsset(this.url, e.attr("src"));
            }
        }
    }
}
