package controllers

import javax.inject.{Inject, Singleton}

import model.UserMessage
import play.api.mvc.{Action, Controller}
import com.codahale.jerkson.{ParsingException, Json => json}
import play.api.libs.json.Json
import play.api.libs.ws.WSClient
import services.DoorOpenerService
import scala.concurrent.ExecutionContext

/**
  * Created by ekreative on 7/9/2016.
  */
@Singleton
class ReceiveMessageController @Inject()(implicit exec: ExecutionContext, ws: WSClient) extends Controller {

  var token: String = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6Ik1uQ19WWmNBVGZNNXBPWWlKSE1iYTlnb0VLWSIsImtpZCI6Ik1uQ19WWmNBVGZNNXBPWWlKSE1iYTlnb0VLWSJ9.eyJhdWQiOiJodHRwczovL2dyYXBoLm1pY3Jvc29mdC5jb20iLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC83MmY5ODhiZi04NmYxLTQxYWYtOTFhYi0yZDdjZDAxMWRiNDcvIiwiaWF0IjoxNDY4MDg1MDQwLCJuYmYiOjE0NjgwODUwNDAsImV4cCI6MTQ2ODA4ODk0MCwiYXBwaWQiOiI1NjllZjUyYi02M2Y2LTQzYTctYmRhMy1mMWE2YjViMjlmODAiLCJhcHBpZGFjciI6IjEiLCJpZHAiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC83MmY5ODhiZi04NmYxLTQxYWYtOTFhYi0yZDdjZDAxMWRiNDcvIiwidGlkIjoiNzJmOTg4YmYtODZmMS00MWFmLTkxYWItMmQ3Y2QwMTFkYjQ3IiwidmVyIjoiMS4wIn0.k9gUdvAqrr92uV2l9JpBsttyfyqLu6xOLn5p7OZK_QUFrS1QVowuoN_TDyxLJDw_UMmxzhLlQyqAbTW0LxCXchJ0skqd8ERGLx9odm8NwtgPEfWCfEmzm705cJPT0JFvAxVQ0qOPkOmCISGh1KnYK5jVCfHiXx7fL4Rw_NKWtqAItGd4U5oUoWYWkA_dJ03crQtWGOLuHJNyhyKlyJa4V1K237ERA3rxZDraO9RqszxSASrqb7ly0wv4u1fxMvn_UY6ROd6dO-hfmg0xeC3JGw98LCz8AtU0i2LiAyjPyJDWRjvUaVt_Kfs8dTlWwBGhzbaHC3k9098wZLhXfihO3Q"

    def receive = Action(parse.json) {
      request =>
        val messages = request.body.as[Seq[UserMessage]]
        for (msg <- messages) {
          msg.content match {
            case "ping" => sendMessage(msg.from, "pong")
            case x if x.toUpperCase.contains("HI") => sendMessage(msg.from, "Hi "+ msg.from)
            case "opendoor" => new DoorOpenerService().openDoor
            case _ =>
          }
        }
        println(messages)
        Ok("OK")
    }



  private def sendMessage(userId: String, message: String): Unit = {
    val param = Json.obj(
      "message" -> Json.obj (
        "content" -> message
      )
    )
    ws.url("https://apis.skype.com/v2/conversations/%s/activities".format(userId))
        .withHeaders("Authorization" -> ("Bearer " + token))
      .post(param)
      .map {
        response =>

          println(response)
      }
  }

//  private def updateAuthToken: String = {
//    ws.url().withHeaders()
//  }
}
