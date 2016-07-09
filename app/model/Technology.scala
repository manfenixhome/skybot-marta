package model

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

/**
  * Created by cheb on 7/9/16.
  */
case class Technology(
                       id: Long,
                       name: String,
                       tagType: String,
                       description: Option[String],
                       image: Option[Image]
                     )

object Technology {
  import model.Image._

  implicit val technologyReads: Reads[Technology] = (
      (JsPath \ "id").read[Long] and
      (JsPath \ "name").read[String] and
      (JsPath \ "tagType").read[String] and
      (JsPath \ "description").readNullable[String] and
      (JsPath \ "image").readNullable[Image]
    ) (Technology.apply _)
}
