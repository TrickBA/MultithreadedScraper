import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by arein on 03/11/14.
 */
public class UrlRepository
{
    protected final LinkedList<String> unprocessedUrls;

    /**
     * The hashtable maps all URLs to all assets found on these urls
     */
    protected final Hashtable<String, List<String>> links;
    protected volatile boolean isFirstUrl;
    public UrlRepository()
    {
        this.links = new Hashtable<String, List<String>>();
        this.unprocessedUrls = new LinkedList<String>();
    }

    public void add(String url)
    {
        synchronized (this.unprocessedUrls)
        {
            if (!this.links.containsKey(url))
            {
                this.links.put(url, new LinkedList<String>());
                this.unprocessedUrls.add(url);
            }
        }
    }

    public void addAsset(String url, String asset)
    {
        synchronized (this.links)
        {
            if (this.links.containsKey(url))
            {
                this.links.get(url).add(asset);
            }
        }
    }

    public String getNextUrl() throws InterruptedException
    {
        synchronized (this.unprocessedUrls)
        {
            if (this.unprocessedUrls.size() == 0) return null;

            return unprocessedUrls.remove();
        }
    }

    public int getSize() throws InterruptedException
    {
        synchronized (this.unprocessedUrls)
        {
            return this.unprocessedUrls.size();
        }
    }

    public String toString()
    {
        synchronized (this.links)
        {
            String prev = "";
            StringBuilder sb = new StringBuilder();
            for (String key : this.links.keySet())
            {
                sb.append(prev + key + "\n");
                for (String asset: this.links.get(key))
                {
                    sb.append(" | " + asset + "\n");
                }
                prev = "\n\n";
            }

            return sb.toString();
        }
    }
}
