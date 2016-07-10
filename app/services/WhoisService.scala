package services

import play.api.libs.ws.WSClient

import scala.concurrent.{Await, ExecutionContext}
import javax.inject.{Inject, Singleton}

import play.api.libs.json._
import com.codahale.jerkson.{ParsingException, Json => json}
import model.{AuthToken, User}
import play.api.cache.CacheApi

import scala.collection.mutable
import scala.concurrent.duration._

/**
  * Created by cheb on 7/10/16.
  */
class WhoisService @Inject()(implicit exec: ExecutionContext, ws: WSClient, cache: CacheApi) {
  val keywords = Seq("whois", "who", "wi", "кто", "хто")
  val keywordsHelp = Seq("-hlp", "-help", "-хелп", "-помоги", "-помощь", "-допомога", "-допоможи")

  def hasKeywords(message: String): Boolean = {
    val tags = message.toLowerCase.split(" ")
    keywords.exists(key => tags.contains(key))
  }

  def trySearch(message: String, userID: String, sendService: SendMessageService): Unit = {
    println("Query: " + message);
    val tags = message.toLowerCase.split(" ")
    if (tags.length < 2) {
      sendService.sendMessage(userID, "Please add a skype ID parameter or type 'whois -help'")
    } else {
      if (keywordsHelp.exists(key => tags.contains(key))) {
        doShowHelp(userID, sendService)
      } else {
        if (keywords.contains(tags(0))) {
          doSearch(tags(1), userID, sendService);
        } else {
          doSearch(tags(0), userID, sendService);
        }
      }
    }
    //sendService.sendMessage(userID, "searching...")
  }

  def doShowHelp(userID: String, sendService: SendMessageService): Unit = {
    sendService.sendMessage(userID,
      "whois [skypeID] - search user by skype ID"
    )
  }

  def doSearch(q: String, userID: String, sendService: SendMessageService): Unit = {
    println("Search for: " + q);

    val users: Seq[User] = cache.getOrElse[Seq[User]]("users.filter", 12.hours) {
      val request = ws.url("http://10.0.1.249/api/workers").get.map {
        response =>
          println(response.status)
          var users = Seq[User]()
          if (response.status == 200) {
            users = json.parse[Seq[User]](Json.parse(response.body).\("workers").get.toString())
            findUsers(q, users, userID, sendService)
          } else {
            sendService.sendMessage(userID, "Failed get data from Viktor server")
          }
          users
      }
      Await.result(request, 30 seconds)
    }
    findUsers(q, users, userID, sendService)
  }

  def findUsers(q: String, users: Seq[User], userID: String, sendService: SendMessageService): Unit = {
    println("Trying to find: " + q);
    val matchedUsers = users.filter(u => u.skype != null && u.skype.indexOf(q) > -1)
    if (matchedUsers.isEmpty) {
      sendService.sendMessage(userID, "No results, sorry")
    } else {
      printUsers(matchedUsers, userID, sendService)
    }
  }

  def printUsers(users: Seq[User], userID: String, sendService: SendMessageService): Unit = {
    println(users)
    //val sb = mutable.Buffer[String]()
    val sb = new java.lang.StringBuilder
    users.foreach { item =>
      sb.append("name: " + item.name)
      sb.append(" skype: " + item.skype)
      if (item.images.isDefined && item.images.nonEmpty) {
        val image =  item.images.get.head
        sb.append(" image: " + image.src)
      }
      sb.append("\n")
    }
    sendService.sendMessage(userID, "List: \n" + sb.toString())
  }
}
