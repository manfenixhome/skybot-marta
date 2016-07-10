package services

import play.api.libs.ws.WSClient

import scala.concurrent.{Await, ExecutionContext}
import javax.inject.{Inject, Singleton}

import play.api.libs.json._
import com.codahale.jerkson.{ParsingException, Json => json}
import model.{AuthToken, User}
import play.api.cache.CacheApi

import scala.concurrent.duration._

/**
  * Created by cheb on 7/9/16.
  */
class ListService @Inject()(implicit exec: ExecutionContext, ws: WSClient, cache: CacheApi) {

  val keywords = Seq("list", "ls", "lst")

  def hasKeywords(message: String): Boolean = {
    val tags = message.toLowerCase.split(" ")
    keywords.exists(key => tags.contains(key))
  }

  def showList(userID: String, sendService: SendMessageService): Unit = {
    println("Show list")
    val users:  Seq[User] = cache.getOrElse[Seq[User]]("users.filter", 12.hours){
      val request = ws.url("http://10.0.1.249/api/workers").get.map {
        response =>
          println(response.status)
          var users = Seq[User]()
          if (response.status == 200) {
            users = json.parse[Seq[User]](Json.parse(response.body).\("workers").get.toString())
            printUsers(users,userID, sendService)
          } else {
            sendService.sendMessage(userID, "Failed get data from Viktor server")
          }
          users
      }
      Await.result(request, 30 seconds)
    }
    printUsers(users,userID, sendService)
  }

  def printUsers(users: Seq[User],userID: String, sendService: SendMessageService): Unit ={
    println(users)
    val sb = new java.lang.StringBuilder
    users.foreach{ item =>
      sb.append("name: " + item.name)
      sb.append(" skype: " + item.skype)
      sb.append("\n")
    }
    sendService.sendMessage(userID, "List: \n" + sb)
  }
}