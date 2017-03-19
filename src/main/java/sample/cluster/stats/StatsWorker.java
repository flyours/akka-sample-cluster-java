package sample.cluster.stats;

import akka.actor.UntypedActor;

import java.util.HashMap;
import java.util.Map;

//#worker
public class StatsWorker extends UntypedActor {

  private Map<String, Integer> cache = new HashMap<>();

  @Override
  public void onReceive(Object message) {
    if (message instanceof String) {
      String word = (String) message;
      Integer length = cache.computeIfAbsent(word, k -> word.length());
      getSender().tell(length, getSelf());

    } else {
      unhandled(message);
    }
  }

}
//#worker