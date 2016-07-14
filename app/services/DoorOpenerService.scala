package services

import javax.inject.{Inject, Singleton}

import play.api.libs.ws.WSClient

import scala.concurrent.ExecutionContext

/**
  * Created by cheb on 7/9/16.
  */
@Singleton
class DoorOpenerService @Inject()(implicit exec: ExecutionContext, ws: WSClient) {

  val keywords = Seq("^\\s*open\\s*d(o+)r\\s*$", "^\\s*open\\s*gate(s)?\\s*$")

  def hasKeywords(message: String): Boolean = {
    keywords.exists(r => message.toLowerCase.matches(r))
  }

  def openDoor(userID: String, sendService: SendMessageService): Unit = {
    ws.url("http://doorbell.ekreative.com/app").get.map {
      response =>
        if (response.status == 200) {
          sendService.sendMessage(userID, "Door opened, please come (wfh)")
        } else {
          sendService.sendMessage(userID, "(swear) damn, can't connect to doorbell API. Could you ring the doorbell please?")
        }
    }
  }
}
