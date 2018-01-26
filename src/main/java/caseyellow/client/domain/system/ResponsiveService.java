package caseyellow.client.domain.system;

import java.io.Closeable;

public interface ResponsiveService extends Closeable{
    void keepAlive();
}
