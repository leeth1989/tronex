package tronex;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import tronex.tasks.SyncHistoryBlockTask;

@Component
public class AppStartup implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private SyncHistoryBlockTask syncHistoryBlockTask;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        new Thread(syncHistoryBlockTask).start();
    }
}
