package com.mintbeans.geo.data

import com.mintbeans.geo.core.{ Location, LocationRepository }
import com.mongodb.casbah.MongoCollection
import com.mongodb.casbah.commons.Imports._
import com.mongodb.casbah.commons.MongoDBObject
import com.novus.salat._

class MongoLocationRepository(collection: MongoCollection) extends LocationRepository with SalatContext {

  def all(limit: Option[Int]): Seq[Location] = {
    collection.find
      .limit(limit.getOrElse(0))
      .toList.map(o => grater[Location].asObject(o))
  }

  def byId(id: org.bson.types.ObjectId): Option[Location] = {
    collection.findOneByID(id) match {
      case Some(document) => Some(grater[Location].asObject(document))
      case None           => None
    }
  }

  def byNameFragment(name: String, limit: Option[Int]): Seq[Location] = {
    collection.find(MongoDBObject("name" -> s"(?i).*\\Q${name}\\E.*".r))
      .limit(limit.getOrElse(0))
      .toList.map(o => grater[Location].asObject(o))
  }

  def byTextPhrase(phrase: String, limit: Option[Int]): Seq[Location] = {
    collection.find(MongoDBObject("$text" -> MongoDBObject("$search" -> phrase)))
      .limit(limit.getOrElse(0))
      .toList.map(o => grater[Location].asObject(o))
  }

}
