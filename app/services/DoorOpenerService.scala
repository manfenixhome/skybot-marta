package services

import javax.inject.{Inject, Singleton}

import play.api.libs.ws.WSClient

import scala.concurrent.ExecutionContext

/**
  * Created by cheb on 7/9/16.
  */
@Singleton
class DoorOpenerService @Inject()(implicit exec: ExecutionContext, ws: WSClient) {

  val keywords = Seq("opendoor", "opendor", "opengates")

  def hasKeywords(message: String): Boolean = {
    val tags = message.toLowerCase.split(" ")
    keywords.exists(key => tags.contains(key))
  }

  def openDoor(userID: String, sendService: SendMessageService): Unit = {
    //println("Test")
    ws.url("http://doorbell.ekreative.com/app").get.map {
      response =>
        if (response.status == 200) {
          sendService.sendMessage(userID, "Door opened")
          //println("Door opened")
        } else {
          sendService.sendMessage(userID, "Failed")
          //println("Failed")
        }
    }
  }
}
