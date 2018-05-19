
import MyApp.{getClass, main, stage, system}
import akka.actor.Props

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.collections.ObservableBuffer
import scalafx.event.ActionEvent
import scalafx.application.Platform
import scalafx.scene.control._
import scalafx.scene.Scene
import scalafx.application.{JFXApp, Platform}
import scalafx.scene.image.Image
import scalafxml.core.{FXMLLoader, NoDependencyResolver}
//import scalafx.scene.shape.Circle
import scalafxml.core.macros.sfxml
import scalafx.Includes._
@sfxml
class MainController(
//                    private val ball: Circle,
                    private val server: TextField,
                    private val port: TextField,
                    private val status: Label,
//                    private val btnPass: Button,
                    private val peerList: ListView[User],
                    private val txtMessage: TextField,
                    private val txaMessage: TextArea,
                    private val lblMessageStatus: Label,
                    private val txtName: TextField
                    ) {
  txtMessage.onKeyPressed = handle {
    import Client._
    MyApp.clientActor ! KeyPressed(txtName.text.value)
  }
  def handleJoin(action: ActionEvent) ={
    import Client._
    server.text.value = "127.0.0.1"
    MyApp.clientActor ! JoinRequest(server.text.value, port.text.value,
      txtName.text.value)
  }
//  def handleStart(actionEvent: ActionEvent) = {
//    import Server._
//    MyApp.serverActor ! Start
//  }
//  def handlePass(actionEvent: ActionEvent) = {
//    import Client._
//    MyApp.clientActor ! Pass
//  }
//  def handleSend(actionEvent: ActionEvent) = {
//    import scalafx.Includes._
//    import Client._
//    val selectedPeer = peerList.selectionModel().selectedItem.value
//    selectedPeer.ref ! Message(txtMessage.text.value)
//  }

  def handleSend(actionEvent: ActionEvent) = {
    import scalafx.Includes._
    import Client._
    MyApp.clientActor ! SendMessage("(" + txtName.text.value + ")   " +  txtMessage.text.value)
    txtMessage.text.value = ""
  }

  def handleClear(actionEvent: ActionEvent) = {
    import scalafx.Includes._
    import Client._
    txaMessage.text.value = ""
  }

  def handleClose(event: ActionEvent) {
    Platform.exit()
  }

//
//  def handleGo(actionEvent: ActionEvent) = {
//    import scalafx.Includes._
//    import Client._
//    Stage stage = new Stage();
//  }


  def displayIncomingMessage(text: String): Unit ={

    txaMessage.text =  txaMessage.text.value + text + "\n"
  }

  def setDisplay(text: String): Unit ={
    status.text = text
  }
//  def hideBall: Unit = {
//    ball.visible = false
//    btnPass.disable = true
//  }
//  def showBall: Unit = {
//    ball.visible = true
//    btnPass.disable = false
//  }
  def displayPeer(list: List[User]): Unit ={
    peerList.items = new ObservableBuffer() ++ list
  }
  def displayMessageStatus(text: String): Unit ={
    lblMessageStatus.text = text
  }
}
