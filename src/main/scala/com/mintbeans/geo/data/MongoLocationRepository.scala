package com.mintbeans.geo.data

import com.mintbeans.geo.core.{Location, LocationRepository, Point}
import com.mongodb.casbah.MongoCollection

class MongoLocationRepository(collection: MongoCollection) extends LocationRepository {

  def all(): Seq[Location] = {
    for (l <- collection) yield Location(l.get("name").toString,
                                         Point(l.get("latitude").asInstanceOf[Double],
                                               l.get("longitude").asInstanceOf[Double]))

  }.toList

}
