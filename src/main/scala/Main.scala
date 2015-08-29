import java.util.UUID

import UUIDService.Protocol.Generate
import akka.actor.{Actor, ActorSystem, Props}
import akka.pattern._
import akka.util.Timeout
import spray.routing.SimpleRoutingApp

import scala.concurrent.duration._
import scala.language.postfixOps

object Main extends App with SimpleRoutingApp {
  implicit val system = ActorSystem("my-system")
  implicit val timeout = Timeout(5 seconds)

  import system.dispatcher

  val personService = system.actorOf(Props(classOf[UUIDService]), "uuid_service")

  startServer(interface = "localhost", port = 8080) {
    path("uuid") {
      get {
        complete(
          personService.ask(UUIDService.Protocol.Generate()).mapTo[UUID].map(_.toString)
        )
      }
    }
  }
}

class UUIDService extends Actor {
  override def receive: Receive = {
    case Generate() => sender() ! UUID.randomUUID()
  }
}

object UUIDService {

  object Protocol {

    sealed case class Generate()

  }

}
