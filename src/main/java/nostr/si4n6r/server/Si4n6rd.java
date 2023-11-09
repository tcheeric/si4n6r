package nostr.si4n6r.server;

import lombok.extern.java.Log;
import nostr.api.NIP01;
import nostr.api.Nostr;
import nostr.event.list.KindList;
import nostr.si4n6r.signer.SignerService;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

@Log
public class Si4n6rd extends Thread {
    private final SignerService service;

    private Si4n6rd() {
        this.service = SignerService.getInstance();
        this.setDaemon(true);
    }

    @Override
    public void run() {
        filter();
    }

    public static void main(String[] args) {
        Thread daemon = new Si4n6rd();
        daemon.setDaemon(true);
        daemon.setName("si4n6rd-" + System.currentTimeMillis());

        log.log(Level.INFO, "Starting the si4n6rd service...");
        daemon.start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.log(Level.INFO, "Shutting down the si4n6rd service...");
        }));
    }

    private void filter() {
        try (var executor = Executors.newSingleThreadExecutor()) {
            executor.submit(() -> {
                var kinds = new KindList();
                kinds.add(24133); // TODO - use a global constant

                log.log(Level.INFO, "Creating subscription for kind-24133 events...");
                var filters = NIP01.createFilters(null, null, kinds, null, null, null, null, null, null);
                Nostr.send(filters, "si4n6r-" + Thread.currentThread().getName());
            });

            try {
                executor.shutdown();
                executor.awaitTermination(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                executor.shutdownNow();
            }
        }
    }
}
