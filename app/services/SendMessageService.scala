package services

import javax.inject.{Inject, Singleton}

import model.{AuthToken, UserMessage}
import play.api.libs.json.Json
import play.api.libs.ws.WSClient

import scala.concurrent.{Await, ExecutionContext, Future}
import com.codahale.jerkson.{ParsingException, Json => json}
import scala.concurrent.duration._

/**
  * Created by ekreative on 7/9/2016.
  */

trait SendMessage {
  def sendMessage(userId: String, message: String, step: Int = 0): Unit
}
@Singleton
class SendMessageService @Inject()(implicit exec: ExecutionContext, ws: WSClient)  extends SendMessage {
  private val appId = "569ef52b-63f6-43a7-bda3-f1a6b5b29f80"
  private val secretKey = "Z3fUGDu9iF9xFF9HdDvtJtL"

  private var token: String = ""

  override def sendMessage(userId: String, message: String, step: Int = 0): Unit = {
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
          println(response.status)
          response.status match {
            case 201 =>
            case _ => updateToken match {
                //try repeat not more that 3 times
              case true => if (step < 3) {sendMessage(userId, message, step + 1)}
              case false =>
            }
          }
      }
  }

  def updateToken = {
    var params = ""
    params += "client_id="+appId
    params += "&client_secret=" +secretKey
    params += "&grant_type=client_credentials"
    params += "&scope=https%3A%2F%2Fgraph.microsoft.com%2F.default"
    println(params)
    val request = ws.url("https://login.microsoftonline.com/common/oauth2/v2.0/token")
      .withHeaders("Content-Type" -> "application/x-www-form-urlencoded")
      .post(params)
      .map{
        response =>
          println("updateToken status code="+response.status)
          if (response.status == 200) {
            val tokenObj = json.parse[AuthToken](response.body)
            token = tokenObj.access_token
            true
          } else {
            false
          }
      }
    Await.result(request, 30 seconds)
  }

}
