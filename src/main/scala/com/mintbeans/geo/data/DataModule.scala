package com.mintbeans.geo.data

import com.mintbeans.geo.core.LocationRepository
import com.mongodb.casbah.MongoClient
import com.softwaremill.macwire._
import com.typesafe.config.Config
import org.slf4j.LoggerFactory

trait DataModule {

  val config: Config

  lazy val mongo = {
    val host = config.getString("mongo.host")
    val port = config.getInt("mongo.port")

    val logger = LoggerFactory.getLogger(classOf[DataModule])
    logger.info("Mongo client: host={} port={}", host, port)

    MongoClient(host, port)
  }

  lazy val db = mongo(config.getString("mongo.db"))

  lazy val locationCollection = db(config.getString("mongo.collections.locations"))
  lazy val locationRepository: LocationRepository = wire[MongoLocationRepository]

}
