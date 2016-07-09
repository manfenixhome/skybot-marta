package services
import javax.inject.{Inject, Singleton}
import play.api.libs.ws.WSClient
/**
  * Created by cheb on 7/9/16.
  */
@Singleton
class DoorOpenerService @Inject()(implicit  ws: WSClient) {

  def openDoor: Unit ={
    println("Test")

  }
}
