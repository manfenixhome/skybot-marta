package controllers

import javax.inject.{Inject, Singleton}

import akka.actor.ActorSystem
import model.UserMessage
import play.api.mvc.{Action, Controller}
import com.codahale.jerkson.{ParsingException, Json => json}
import play.api.cache.CacheApi
import play.api.libs.json.Json
import play.api.libs.ws.WSClient
import services._
import model.Task
import utils.DB

import scala.concurrent.ExecutionContext

/**
  * Created by ekreative on 7/9/2016.
  */
@Singleton
class ReceiveMessageController @Inject()(actorSystem: ActorSystem, sendService: SendMessageService)(implicit exec: ExecutionContext, ws: WSClient, cache: CacheApi) extends Controller {

  def receive = Action(parse.json) {
    request =>
      println("request body ="+request.body)
      val messages = request.body.as[Seq[UserMessage]]
      println(messages)
      for (msg <- messages) {
        msg.content.toLowerCase match {
          case x if x.matches("^ping\\s*") => sendService.sendMessage(msg.from, "pong")
          case x if "admin-start-tasks".equals(x) && msg.from.equals("8:antonekreative") => new TaskScheduleService(actorSystem, sendService).startPlanning
          case x if "start task 2".equals(x) && msg.from.equals("8:antonekreative") => new TaskScheduleService(actorSystem, sendService).launchTask(Task.tasks(1))
          case x if x.toLowerCase.matches("^tasks.*") => sendService.sendMessage(msg.from, "Here is s list of all tasks:\n" + Task.tasks.map(t => "%d) %s".format(t.id, t.title)).mkString("\n"))
          case x if x.toLowerCase.matches("^my\\s+tasks\\s*") => {
            val myTasksIds = DB.getTasksByUser(msg.from)
            val myTasks = Task.tasks.filter(t => myTasksIds.contains(t.id))
            myTasks.nonEmpty match {
              case true => sendService.sendMessage(msg.from, "Here is s list of all your tasks:\n" + myTasks.map(t => "%d) %s".format(t.id, t.title)).mkString("\n"))
              case false => sendService.sendMessage(msg.from, "I'm sorry but seems you don't have any tasks (shake)")
            }
          }
          case x if HelpService.hasKeywords(x) => HelpService.showHelp(msg, sendService)
          case x if SubscribeService.hasKeywords(x) => SubscribeService.doAction(msg, sendService)
          case x if HelloService.hasKeywords(x) => HelloService.doAction(msg, sendService)
          case x if new DoorOpenerService().hasKeywords(x) =>
            new DoorOpenerService().openDoor(msg.from, sendService)
          case x if new ListService().hasKeywords(x) =>
            new ListService().showList(x, msg.from, sendService)
          case x if new WhoisService().hasKeywords(x) =>
            new WhoisService().trySearch(x, msg.from, sendService)
          //case "redmine" => new RedmineService().doCheck(msg.from, sendService)
          case _ => sendService.sendMessage(msg.from, "Sorry %s, but I don't understand what you want. I'm not smart enough".format(msg.realName))
                    sendService.sendMessage(msg.from, "(sadness)")
        }
      }
      Ok("OK")
  }
}
