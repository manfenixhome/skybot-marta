package services

import play.api.libs.ws.WSClient

import scala.concurrent.ExecutionContext
import javax.inject.{Inject, Singleton}

import play.api.libs.json._
import com.codahale.jerkson.{ParsingException, Json => json}
import model.{AuthToken, User}

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
        println(response.status)
        if (response.status == 200) {

          val users = json.parse[Seq[User]](Json.parse(response.body).\("workers").get.toString())
          println(users)
          println(users.head.name)
          println(users.head.images.get)
          println(users.head.technology.get)
          println(users.head.caste.get)
          println(users.head.language.get)
          val sb = new java.lang.StringBuilder
          users.foreach{ item =>
            sb.append("name: " + item.name)
            sb.append(" skype: " + item.skype)
            sb.append("\n")
          }
          sendService.sendMessage(userID, "List: \n" + sb)

          //println(users.head.technology.head.)
/*          val users = json.parse[Seq[User]](Json.parse(response.body).\("workers").get.toString())
          println(users)
          println(users.head.name)*/

          /*val users = Json.parse(
            response.body).\("workers")
          val user = users.head.get
          println(user)
          val u = json.parse[User](user.toString())
          println(u.name)*/


          //println("OK")
        } else {
          sendService.sendMessage(userID, "Failed get data from Viktor server")
          //println("Failed")
        }
    }
  }
}
//Json.parse(
//response.body
//).validate[Seq[Press]] match {
//  case JsSuccess(x,p) => {
//  x
//}
//  case JsError(e) => {
//  // TODO throw error?
//  Logger.error("JsError: " + e)
//  Seq()
//}
//}