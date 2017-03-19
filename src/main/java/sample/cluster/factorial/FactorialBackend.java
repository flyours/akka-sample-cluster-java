package sample.cluster.factorial;

import akka.actor.UntypedActor;
import akka.dispatch.Mapper;
import scala.concurrent.Future;

import java.math.BigInteger;

import static akka.dispatch.Futures.future;
import static akka.pattern.Patterns.pipe;

//#backend
public class FactorialBackend extends UntypedActor {

  @Override
  public void onReceive(Object message) {
    if (message instanceof Integer) {
      final Integer n = (Integer) message;
      Future<BigInteger> f = future(() -> factorial(n), getContext().dispatcher());

      Future<FactorialResult> result = f.map(
          new Mapper<BigInteger, FactorialResult>() {
            public FactorialResult apply(BigInteger factorial) {
              return new FactorialResult(n, factorial);
            }
          }, getContext().dispatcher());

      pipe(result, getContext().dispatcher()).to(getSender());

    } else {
      unhandled(message);
    }
  }

  private BigInteger factorial(int n) {
    BigInteger acc = BigInteger.ONE;
    for (int i = 1; i <= n; ++i) {
      acc = acc.multiply(BigInteger.valueOf(i));
    }
    return acc;
  }
}
//#backend

