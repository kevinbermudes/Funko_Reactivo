package org.example.service.Funko;

import org.example.models.Funko;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FunkoCachempl  implements FunkoCache {
    private final Logger logger = LoggerFactory.getLogger(FunkoCachempl.class);
    private int maxSize = 10;
    private Map<Long, Funko> cache;
    private final ScheduledExecutorService cleaner;

    public FunkoCachempl(int maxSize) {
        this.maxSize = maxSize;
        this.cache = new LinkedHashMap<Long, Funko>(maxSize, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Long, Funko> eldest) {
                return size() > maxSize;
            }
        };
        this.cleaner = Executors.newSingleThreadScheduledExecutor();
        this.cleaner.scheduleAtFixedRate(this::ExpiraEntrada, 2, 2, TimeUnit.MINUTES);


    }

    private void ExpiraEntrada() {
        LocalDateTime haceDosMinutos = LocalDateTime.now().minusMinutes(2);
        cache.entrySet().removeIf(entry -> entry.getValue().getUltimoAcceso().isBefore(haceDosMinutos));
    }



    @Override
    public CompletableFuture<Void> put(Long key, Funko value) {
      /*  logger.debug("Añadiendo Funko a cache con id: " + key + " y valor: " + value);
        value.setUltimoAcceso(System.currentTimeMillis());
        cache.put(key, value);
        return CompletableFuture.completedFuture(null);
*/
        return null;
    }


    @Override
    public CompletableFuture<Funko> get(Long key) {
   /*     logger.debug("Obteniendo Funko de cache con id: " + key);
        Funko funko = cache.get(key);
        if (funko != null) {
            funko.setUltimoAcceso(convertMillisToLocalDateTime(System.currentTimeMillis()));
        }
        return CompletableFuture.completedFuture(funko);
   */ return null;
    }




    @Override
    public CompletableFuture<Void> remove(Long key) {
        logger.debug("Eliminando Funko de cache con id: " + key);
        cache.remove(key);
        return null;
    }

    @Override
    public CompletableFuture<Void> clear() {
        cache.entrySet().removeIf(entry -> {
            boolean shouldRemove = entry.getValue().getUltimoAcceso().plusMinutes(2).isBefore(LocalDateTime.now());
            if (shouldRemove) {
                logger.debug("Autoeliminando por caducidad Funko de cache con id: " + entry.getKey());
            }
            return shouldRemove;
        });
        return null;
    }

    @Override
    public void shutdown() {
        cleaner.shutdown();
    }
}
