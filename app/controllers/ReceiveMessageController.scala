package controllers

import javax.inject.{Inject, Singleton}

import model.UserMessage
import play.api.mvc.{Action, Controller}
import com.codahale.jerkson.{ParsingException, Json => json}
import play.api.libs.json.Json
import play.api.libs.ws.WSClient
import services.{DoorOpenerService, HelloService, ListService, SendMessageService}

import scala.concurrent.ExecutionContext

/**
  * Created by ekreative on 7/9/2016.
  */
@Singleton
class ReceiveMessageController @Inject()(implicit exec: ExecutionContext, ws: WSClient) extends Controller {

  val sendService = new SendMessageService()

    def receive = Action(parse.json) {
      request =>
        val messages = request.body.as[Seq[UserMessage]]
        println(messages)
        for (msg <- messages) {
          msg.content match {
            case "ping" => sendService.sendMessage(msg.from, "pong")
            case x if HelloService.hasKeywords(x) => HelloService.doAction(msg, sendService)
            case x if new DoorOpenerService().hasKeywords(x) =>
                      new DoorOpenerService().openDoor(msg.from, sendService)
            case x if new ListService().hasKeywords(x) =>
                      new ListService().showList(msg.from, sendService)
            case "opendoor" => new DoorOpenerService().openDoor(msg.from, sendService)
            case _ => sendService.sendMessage(msg.from, "Sorry %s, but I don't understand what you want".format(msg.realName))
          }
        }
        Ok("OK")
    }
}
