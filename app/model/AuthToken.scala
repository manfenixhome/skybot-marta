package model
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Reads}

/**
  * Created by ekreative on 7/9/2016.
  */
case class AuthToken (token_type: String, access_token: String)
//object AuthToken {
//  implicit val AuthTokenReads: Reads[AuthToken] = (
//    (JsPath \ "token_type").read[String] and
//    (JsPath \ "access_token").read[String]
//    )(AuthToken.apply _ )
//
//}
