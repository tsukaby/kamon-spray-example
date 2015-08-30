import java.util.UUID

import UUIDService.Protocol.Generate
import akka.actor.{Actor, ActorSystem, Props}
import akka.pattern._
import akka.util.Timeout
import kamon.Kamon
import kamon.spray.KamonTraceDirectives
import kamon.trace.Tracer
import spray.routing.SimpleRoutingApp

import scala.concurrent.duration._
import scala.language.postfixOps

object Main extends App with SimpleRoutingApp with KamonTraceDirectives {
  Kamon.start()

  implicit val system = ActorSystem("my-system")
  implicit val timeout = Timeout(5 seconds)

  import system.dispatcher

  val personService = system.actorOf(Props(classOf[UUIDService]), "uuid_service")

  startServer(interface = "localhost", port = 8080) {
    path("uuid") {
      traceName("get:uuid") {
        get {
          complete(
            personService.ask(UUIDService.Protocol.Generate()).mapTo[UUID].map(_.toString)
          )
        }
      }
    }
  }
}

class UUIDService extends Actor {
  val counter = Kamon.metrics.counter("uuid-count")
  val minMaxCounter = Kamon.metrics.minMaxCounter("uuid-min-max")
  val histogram = Kamon.metrics.histogram("uuid-histogram")
  val gauge = Kamon.metrics.gauge("uuid-gauge")(0L)

  override def receive: Receive = {
    case Generate() =>
      val currentMillis = System.currentTimeMillis()

      counter.increment()
      if(currentMillis % 2 == 0) minMaxCounter.increment() else minMaxCounter.decrement()
      histogram.record(currentMillis % 1000)
      gauge.record(currentMillis % 1000)

      val uuid = Tracer.withNewContext("uuid-generate", autoFinish = true) {
        UUID.randomUUID()
      }
      sender() ! uuid
  }
}

object UUIDService {

  object Protocol {

    sealed case class Generate()

  }

}
