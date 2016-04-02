package proxy;

import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import us.monoid.web.Resty;

/**
 * @author Maxim Neverov
 */
@Controller
@RequestMapping("/proxy")
public class ProxyWithMutex {

    private InterProcessMutex lock;
    private static final String PATH = "/global/lock";
    private static int counter = 0;

    public ProxyWithMutex() {
        CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2182",
                new ExponentialBackoffRetry(1000, 3));
        client.start();
        lock = new InterProcessMutex(client, PATH);
    }

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody String getId() throws Exception {
        try {
            if (!lock.acquire(10, TimeUnit.SECONDS)) {
                throw new IllegalStateException("could not acquire the lock");
            }
//            return Integer.toString(getIdFromDB());
            Resty r = new Resty();
            return r.text("http://localhost:9999/id").toString();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            lock.release();
        }
        return null;
    }

    private int getIdFromDB() {
        return counter++;
    }
}
