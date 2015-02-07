package com.mintbeans.geo.data

import com.mintbeans.geo.core.Point
import com.mongodb.DBObject
import com.mongodb.casbah.commons.Imports._
import com.novus.salat._
import com.novus.salat.json._
import com.novus.salat.transformers._

trait SalatContext {

  /**
   * Transforms a Point to GeoJSON format and the other way around.
   */
  object PointTransformer extends CustomTransformer[Point, DBObject] {

    def serialize(p: Point): DBObject =
      MongoDBObject("type" -> "Point", "coordinates" -> List(p.longitude, p.latitude))

    def deserialize(o: DBObject): Point = {
      val coordinates = o.get("coordinates").asInstanceOf[java.util.List[Double]]
      Point(coordinates.get(0), coordinates.get(1))
    }

  }

  implicit val salatContext = new Context() {
    override val name = "custom_salat_context"
    override val typeHintStrategy = StringTypeHintStrategy(TypeHintFrequency.WhenNecessary, "_t")
    override val jsonConfig = JSONConfig(objectIdStrategy = StringObjectIdStrategy)

    registerCustomTransformer(PointTransformer)

    registerGlobalKeyOverride(remapThis = "id", toThisInstead = "_id")
  }

}
