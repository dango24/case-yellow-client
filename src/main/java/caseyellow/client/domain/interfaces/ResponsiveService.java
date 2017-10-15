package caseyellow.client.domain.interfaces;

import java.io.Closeable;

public interface ResponsiveService extends Closeable{
    void keepAlive();
}
