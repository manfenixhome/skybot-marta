package model
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Reads}

/**
  * Created by ekreative on 7/9/2016.
  */
case class UserMessage(content: String, from: String, realName: String, time: String)
object UserMessage {
  implicit val UserMessageReads: Reads[UserMessage] = (
    (JsPath \ "content").read[String] and
      (JsPath \ "from").read[String] and
      (JsPath \ "fromDisplayName").read[String] and
      (JsPath \ "time").read[String]
    )(UserMessage.apply _ )

}
