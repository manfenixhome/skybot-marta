package model

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
/**
  * Created by cheb on 7/9/16.
  */
case class Language(
                id: Long,
                name: String,
                tagType: String,
                description: Option[String],
                image: Option[Image]
              )
object Language{

  import model.Image._

  implicit val languageReads: Reads[Language] = (
    (JsPath \ "id").read[Long] and
      (JsPath \ "name").read[String] and
      (JsPath \ "tagType").read[String] and
      (JsPath \ "description").readNullable[String] and
      (JsPath \ "image").readNullable[Image]
    ) (Language.apply _)
}
