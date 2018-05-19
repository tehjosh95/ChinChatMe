import akka.actor.{Actor, ActorRef, Address}

import scalafx.collections.{ObservableHashSet, ObservableSet}
import akka.pattern.ask
import akka.remote.DisassociatedEvent

import scala.concurrent.ExecutionContext.Implicits.global
import akka.util.Timeout

import scala.concurrent.Future
import scala.concurrent.duration._
class Server extends Actor{
  import Server._
  import Client._
  val clients: ObservableSet[User] = new  ObservableHashSet()
  var circularIterator: Option[Iterator[User]] = None

  var ballOwner: Option[Address] = None

  clients.onChange((_,_) => {
    circularIterator = Some(Iterator.continually(clients.toList).flatten)
    for(client <- clients) {
      client.ref ! UserList(clients.toList)
    }
  })

  override def preStart(): Unit = {
    context.system.eventStream.subscribe(self, classOf[akka.remote.DisassociatedEvent])
    //context.system.eventStream.subscribe(self, classOf[akka.remote.AssociatedEvent])
  }

  override def receive: Receive = {
    case DisassociatedEvent(localAddress, remoteAddress, _) =>
      clients.removeIf(x => x.ref.path.address == remoteAddress)

    case Join(x) =>
      clients += User(x,sender())
      sender() ! Joined
//    case Start =>
//      implicit val timeout: Timeout = Timeout(20 second)
//      context.become(started)
//      val futures = for (client <- clients) yield {
//        client.ref ? Begin
//      }
//      val totalf = Future {
//        var count = 0
//        for (f <- futures){
//          for (v <- f ){
//            count = count + 1
//          }
//        }
//        count
//      }
      //taking prcessing
//      totalf foreach { x =>
        //take a ball from the collection

        //take 1 client circular iterator
//        val clients1 = circularIterator.get.take(1).toList
//        for(client <- clients1){
//          client.ref ! Take
//          ballOwner = Some(client.ref.path.address)
//        }
//
//      }

    case _=> println("unknown message")
  }
//  def started: Receive = {
//    case DisassociatedEvent(localAddress, remoteAddress, _) =>
//      clients.removeIf(x => x.ref.path.address == remoteAddress)
//      implicit val timeout = Timeout(20 second)
//      if (remoteAddress == ballOwner.get) {
//        self ? Pass
//      }
//    case Pass =>
//      //take 1 client circular iterator
//      val clients1 = circularIterator.get.take(1).toList
//      for(client <- clients1){
//        client.ref ! Take
//        ballOwner = Some(client.ref.path.address)
//      }
//      sender() ! "OK"
//    case _=> println("unknown message")
//  }
}
object Server {
  case class Join(text: String)
//  case object Start
}