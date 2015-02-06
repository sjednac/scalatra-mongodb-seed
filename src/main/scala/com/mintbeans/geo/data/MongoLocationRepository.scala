package com.mintbeans.geo.data

import com.mintbeans.geo.core.{Location, LocationRepository, Point}
import com.mongodb.DBObject
import com.mongodb.casbah.MongoCollection
import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.casbah.commons.Imports._

class MongoLocationRepository(collection: MongoCollection) extends LocationRepository {

  private def convert(obj: DBObject): Location = Location(obj.get("_id").toString,
                                                          obj.get("name").toString,
                                                          Point(java.lang.Double.parseDouble(obj.get("latitude").toString),
                                                                java.lang.Double.parseDouble(obj.get("longitude").toString)))

  def all(): Seq[Location] = {
    for (o <- collection) yield convert(o)
  }.toList

  def byId(id: String): Location = {
    convert(collection.findOneByID(new ObjectId(id)).get)
  }

  def byTextPhrase(phrase: String): Seq[Location] = {
    collection.find(MongoDBObject("$text" -> MongoDBObject("$search" -> phrase))).toList.map(o => convert(o))
  }

}
