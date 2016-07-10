package model

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

/**
  * Created by cheb on 7/9/16.
  */
case class Caste(
                  id: Long,
                  name: String,
                  tagType: String,
                  description: Option[String],
                  image: Option[Picture]
                )

object Caste {

  import model.Picture._

  implicit val casteReads: Reads[Caste] = (
    (JsPath \ "id").read[Long] and
      (JsPath \ "name").read[String] and
      (JsPath \ "tagType").read[String] and
      (JsPath \ "description").readNullable[String] and
      (JsPath \ "image").readNullable[Picture]
    ) (Caste.apply _)
}
