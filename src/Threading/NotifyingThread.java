package Threading;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.RejectedExecutionException;

/**
 * Created by arein on 05/11/14.
 */
public abstract class NotifyingThread extends Thread
{
    private final Set<ThreadCompleteListener> listeners = new CopyOnWriteArraySet<ThreadCompleteListener>();
    public final void addListener(final ThreadCompleteListener listener)
    {
        listeners.add(listener);
    }

    public final void removeListener(final ThreadCompleteListener listener)
    {
        listeners.remove(listener);
    }

    private final void notifyListeners()
    {
        for (ThreadCompleteListener listener : listeners)
        {
            listener.notifyOfThreadComplete(this);
        }
    }

    @Override
    public final void run()
    {
        try
        {
            doRun();
        }
        finally
        {
            try
            {
                notifyListeners();
            }
            catch (RejectedExecutionException ex)
            {
                // Swallow
            }
        }
    }

    public abstract void doRun();
}