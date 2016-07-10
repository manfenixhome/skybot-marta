package model


import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
/**
  * Created by cheb on 7/9/16.
  */
case class Picture(
             id: Long,
             name: String,
             src: String
           )
object Picture{
  implicit val picturesReads: Reads[Picture] = (
      (JsPath \ "id").read[Long] and
      (JsPath \ "name").read[String] and
      (JsPath \ "src").read[String]
    ) (Picture.apply _)
}
