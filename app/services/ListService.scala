package services

import play.api.libs.ws.WSClient
import scala.concurrent.ExecutionContext
import javax.inject.{Inject, Singleton}

/**
  * Created by cheb on 7/9/16.
  */
class ListService @Inject()(implicit exec: ExecutionContext, ws: WSClient) {

  val keywords = Seq("list", "ls", "lst")

  def hasKeywords(message: String): Boolean = {
    val tags = message.toLowerCase.split(" ")
    keywords.exists(key => tags.contains(key))
  }

  def showList(userID: String, sendService: SendMessageService): Unit = {
    println("Show list")
    ws.url("http://10.0.1.249/api/workers").get.map {
      response =>
        if (response.status == 200) {

          sendService.sendMessage(userID, "List:")
          //println("Door opened")
        } else {
          sendService.sendMessage(userID, "Failed get data from Viktor server")
          //println("Failed")
        }
    }
  }
}
