package services

import play.api.libs.ws.WSClient

import scala.concurrent.{Await, ExecutionContext}
import javax.inject.{Inject, Singleton}

import play.api.libs.json._
import com.codahale.jerkson.{ParsingException, Json => json}
import model.{AuthToken, Picture, User}
import play.api.cache.CacheApi

import scala.concurrent.duration._

/**
  * Created by cheb on 7/9/16.
  */
class ListService @Inject()(implicit exec: ExecutionContext, ws: WSClient, cache: CacheApi) {

  val keys = Seq("-a", "-dgb")
  val keywords = Seq("list", "ls", "lst")

  def hasKeywords(message: String): Boolean = {
    val tags = message.toLowerCase.split(" ")
    keywords.exists(key => tags.contains(key))
  }

  def showList(msg: String, userID: String, sendService: SendMessageService): Unit = {
    println("Show list")
    val users: Seq[User] = cache.getOrElse[Seq[User]]("users.filter", 12.hours) {
      val request = ws.url("http://eworkers.paul.ekreative.com/api/workers").get.map {
        response =>
          println(response.status)
          var users = Seq[User]()
          if (response.status == 200) {
            users = json.parse[Seq[User]](Json.parse(response.body).\("workers").get.toString())
            //printUsers(msg, users, userID, sendService)
          } else {
            sendService.sendMessage(userID, "Failed get data from eWorkers API")
          }
          users
      }
      Await.result(request, 30 seconds)
    }
    printUsers(msg, users, userID, sendService)
  }

  def printUsers(msg: String, users: Seq[User], userID: String, sendService: SendMessageService): Unit = {
//    println(users)
    val tags = msg.toLowerCase.split(" ")
    if (tags.length > 1){
      if (tags.contains("-dbg")){
        val sb = new java.lang.StringBuilder
        users.foreach { item =>
          sb.append(item.toString + "\n\n")
        }
        sendService.sendMessage(userID, "List: \n" + sb)
      }else if (tags.contains("-a")){
        val sb = new java.lang.StringBuilder
        users.foreach { item =>
          sb.append(" name: " + item.name)
          sb.append(" skype: " + item.skype)
          sb.append(" workingEmail: " + item.workingEmail)
          sb.append(" startWorking: " + item.startWorking)
          sb.append(" birthday: " + item.birthday)
          if (item.technology.isDefined && item.technology.get.nonEmpty){
            sb.append("technologies: " + item.technology.get.toString())
          }
          sb.append("\n\n")
        }
        sendService.sendMessage(userID, "List: \n" + sb)
      }else{
        val sb = new java.lang.StringBuilder
        users.foreach { item =>
          sb.append("name: " + item.name)
          sb.append(" skype: " + item.skype)
//          println("images.size=" + item.images.size)
//          item.images.headOption match {
//            case Some(picture) => sb.append("\n image: " + picture )
//            case None =>
//          }

          sb.append("\n")
        }
        sendService.sendMessage(userID, "List: \n" + sb)
      }
    }else{
      val sb = new java.lang.StringBuilder
      users.foreach { item =>
        sb.append("name: " + item.name)
        sb.append(" skype: " + item.skype)
        sb.append("\n")
      }
      sendService.sendMessage(userID, "List: \n" + sb)
    }
  }
}