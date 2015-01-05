import Threading.NotifyingThread;
import Threading.ThreadCompleteListener;
import Util.UrlHelper;
import com.sun.corba.se.spi.orbutil.threadpool.ThreadPool;

import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by arein on 05/11/14.
 */
public class ScrapeClient implements ThreadCompleteListener
{
    private ExecutorService executor;
    private URL url;
    private UrlRepository repository;
    private int maxNumberOfThreads;
    private volatile Integer threadCount;

    public ScrapeClient(URL url, int maxNumberOfThreads)
    {
        this.url = url;
        this.repository = new UrlRepository();
        this.maxNumberOfThreads = maxNumberOfThreads;
        this.executor = Executors.newFixedThreadPool(this.maxNumberOfThreads);
        this.threadCount = 0;
        System.out.print("Starting to scrape");
    }

    /**
     * Starts the Scraping
     */
    public void scrape()
    {
        ScrapeTask worker = new ScrapeTask(this.repository, UrlHelper.getHost(this.url), this.url.toString());
        worker.addListener(this);
        this.threadCount++;
        this.executor.execute(worker);
    }

    /**
     * Invoked as the scraping is done
     */
    protected void scrapingCompleted()
    {
        this.executor.shutdownNow();
        System.out.println();
        System.out.println(this.repository.toString());
    }

    /**
     * This method is invoced by any thread that is about to die.
     * @param thread
     */
    @Override
    public void notifyOfThreadComplete(Thread thread)
    {
        System.out.print(".");

        synchronized (threadCount)
        {
            threadCount--;
            ScrapeTask t = (ScrapeTask) thread;
            t.removeListener(this);

            try
            {
                String u;
                while ((u = this.repository.getNextUrl()) != null)
                {
                    ScrapeTask worker = new ScrapeTask(this.repository, UrlHelper.getHost(this.url), u);
                    worker.addListener(this);
                    this.executor.execute(worker);
                    this.threadCount++;
                }
            }
            catch (InterruptedException ex)
            {
            }

            if (this.threadCount == 0)
            {
                this.scrapingCompleted();
            }
        }
    }
}
