import akka.actor.{Actor, ActorRef}

import scalafx.application.Platform

class Display extends Actor {
  import Display._
  import Client._
  override def receive: Receive = {
    case DisplayStat(text) =>
      Platform.runLater {
        MyApp.ctl.setDisplay(text)
      }
    case Begin =>
//      Platform.runLater {
//        MyApp.ctl.hideBall
//      }
//    case ShowBall =>
//      Platform.runLater {
//        MyApp.ctl.showBall
//      }
//    case HideBall =>
//      Platform.runLater {
//        MyApp.ctl.hideBall
//      }
    case DisplayPeer(x) =>
      Platform.runLater {
        MyApp.ctl.displayPeer(x)
      }
    case DisplayIncomingMessage(x)=>
      Platform.runLater {
        MyApp.ctl.displayIncomingMessage(x)
      }
    case SetMessageStatus(x) =>
      Platform.runLater {
        MyApp.ctl.displayMessageStatus(x)
      }
    case ClearMessageStatus =>
      Platform.runLater {
        MyApp.ctl.displayMessageStatus("")
      }
    case _=> println("unknown message")
  }
}

object Display {
  case object ClearMessageStatus
  case class SetMessageStatus(text: String)
  case class DisplayIncomingMessage(text: String)
  case class DisplayStat(text: String)
  case class DisplayPeer(list: List[User])
}