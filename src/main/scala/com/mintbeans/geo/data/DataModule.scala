package com.mintbeans.geo.data

import com.mintbeans.geo.core.LocationRepository
import com.mongodb.casbah.MongoClient
import com.softwaremill.macwire.MacwireMacros._
import com.typesafe.config.Config

trait DataModule {

  val config: Config

  lazy val mongo = MongoClient(config.getString("mongo.host"), config.getInt("mongo.port"))
  lazy val db = mongo(config.getString("mongo.db"))

  lazy val locationCollection = db(config.getString("mongo.collections.locations"))
  lazy val locationRepository: LocationRepository = wire[MongoLocationRepository]


}
