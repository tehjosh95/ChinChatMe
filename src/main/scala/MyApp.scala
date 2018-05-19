import java.net.NetworkInterface
import scalafx.application.Platform
import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import scalafx.scene.image.Image
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.image.{Image, ImageView}
import scalafxml.core.{FXMLLoader, NoDependencyResolver}
import scalafx.Includes._
import scala.collection.JavaConverters._
import scalafx.scene.control.TextField
import scalafx.stage.{Screen, StageStyle}


object MyApp extends JFXApp {
//  var count = -1
//  val addresses = (for (inf <- NetworkInterface.getNetworkInterfaces.asScala;
//                        add <- inf.getInetAddresses.asScala) yield {
//    count = count + 1
//    (count -> add)
//  }).toMap
//  for((i, add) <- addresses){
//    println(s"$i = $add")
//  }
//  println("please select which interface to bind")
//  var selection: Int = 0
//  do {
//    selection = readInt()
//  } while(!(selection >= 0 && selection < addresses.size))
//
//  val ipaddress = addresses(selection)
//  println("please select your user name")


  val overrideConf = ConfigFactory.parseString(
    s"""
       |akka {
       |  loglevel = "INFO"
       |
 |  actor {
       |    provider = "akka.remote.RemoteActorRefProvider"
       |  }
       |
 |  remote {
       |    enabled-transports = ["akka.remote.netty.tcp"]
       |    netty.tcp {
       |      hostname = "127.0.0.1"
       |      port = 0
       |    }
       |
 |    log-sent-messages = on
       |    log-received-messages = on
       |  }
       |
 |}
       |"""
     .stripMargin)





  val myConf = overrideConf.withFallback(ConfigFactory.load())
  val system = ActorSystem("ball", myConf)


  val serverActor = system.actorOf(Props[Server], "server")
  val clientActor = system.actorOf(Props[Client], "client")
  val displayActor = system.actorOf(Props[Display], "display")
  //loading UI

  var ui = getClass.getResourceAsStream("MainUI.fxml")
  var loader: FXMLLoader = new FXMLLoader(null, NoDependencyResolver)
  loader.load(ui)

  var main = loader.getRoot[javafx.scene.layout.BorderPane]
  var ctl = loader.getController[MainController#Controller]()

  stage = new PrimaryStage(){
    title = "ChinChatMe"

    scene = new Scene(){
      root = main
    }

    icons += new Image("/ctm.jpg")

  }
  stage.onCloseRequest = handle {
    system.terminate
  }

}
