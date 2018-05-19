
import akka.actor.{Actor, ActorRef, ActorSelection}
import akka.pattern.ask

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import akka.util.Timeout
import com.github.nscala_time.time.RichInt
import org.joda.time.DateTime
class Client extends Actor {
  import Client._
  import Display._
  var serverActor: Option[ActorSelection] = None
  var peerlist: Option[List[User]] = None
  var lastReceiveTyping: Option[DateTime] = None
  override def preStart(): Unit = {
    context.system.eventStream.subscribe(self, classOf[akka.remote.DisassociatedEvent])
    //context.system.eventStream.subscribe(self, classOf[akka.remote.AssociatedEvent])
//    context.system.scheduler.schedule(0 second, 1 second, self, CheckMessageExpired)
  }

  override def receive: Receive = {
    case JoinRequest(s, p, n) =>
      import Server._
      serverActor = Some(MyApp.system.actorSelection(s"akka.tcp://ball@$s:$p/user/server"))
      serverActor.get ! Server.Join(n)
    case Joined =>
      MyApp.displayActor ! DisplayStat("Joined")
      context.become(joined)
    case UserList(x) =>
      peerlist = Some(x)
      MyApp.displayActor ! DisplayPeer(x)
    case _=> println("unknown message")
  }
  def joined: Receive = {
    case Begin =>
      MyApp.displayActor ! Begin
      sender() ! "OK"
//    case Take =>
//      MyApp.displayActor ! ShowBall
//    case Pass =>
//      implicit val timeout = Timeout(20 second)
//      for (server <- serverActor){
//        val future = server ? Pass
//        future foreach {x =>
//          MyApp.displayActor ! HideBall
//        }
//      }
    case Message(x) =>
      MyApp.displayActor ! DisplayIncomingMessage(x)
      //MyApp.ctl.displayIncomingMessage(x)
    case SendMessage(x) =>
      for(list <- peerlist){
        for (peer <- list)
          peer.ref ! Message(x)
      }
    case UserList(x) =>
      peerlist = Some(x)
      MyApp.displayActor ! DisplayPeer(x)
    case KeyPressed(x) =>
      for (list <- peerlist;
           client <- list){
        client.ref ! Typing(x)
      }
    case Typing(x) =>
      lastReceiveTyping = Some(DateTime.now)
      MyApp.displayActor ! SetMessageStatus(s"$x is typing")
//    case CheckMessageExpired =>
//      import com.github.nscala_time.time.Imports._
//      import org.joda.time.DateTime
//      for(lastTime <- lastReceiveTyping if(lastTime + new RichInt(5).seconds < DateTime.now())){
//
//          MyApp.displayActor ! ClearMessageStatus
//
//      }
    case _=> println("unknown message")
  }

}
object Client {
//  case object CheckMessageExpired
  case class Typing(text: String)
  case class KeyPressed(text: String)
  case class JoinRequest(server: String, port: String, name: String)
  case class UserList(list: List[User])
  case class Message(text: String)
  case object Joined
  case object Begin
//  case object Take
//  case object Pass
  case class SendMessage(text: String)
}