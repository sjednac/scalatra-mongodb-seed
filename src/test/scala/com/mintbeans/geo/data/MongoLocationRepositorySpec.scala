package com.mintbeans.geo.data

import com.github.simplyscala.{MongoEmbedDatabase, MongodProps}
import com.mongodb.DBObject
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.MongoClient
import de.flapdoodle.embed.mongo.distribution.Version
import org.scalatest.{FlatSpec, Matchers}

class MongoLocationRepositorySpec extends FlatSpec with Matchers with MongoEmbedDatabase {

  sealed trait Test {
    lazy val mongoPort = 12345
    lazy val mongoClient = MongoClient("localhost", mongoPort)
    lazy val db = mongoClient("test")
    lazy val collection = db("locations")
    lazy val repository = new MongoLocationRepository(collection)

    def withMongo(fixture: MongodProps => Any) = withEmbedMongoFixture(port = mongoPort, version = Version.V2_6_1)(fixture)
  }

  "MongoLocationRepository" should "return all locations" in new Test {
    withMongo { mongoProps =>
      collection.insert(MongoDBObject("name" -> "Sopot", "latitude" -> 54.45, "longitude" -> 18.56), WriteConcern.Safe)
      collection.insert(MongoDBObject("name" -> "Warsaw", "latitude" -> 52.23, "longitude" -> 21.01), WriteConcern.Safe)

      repository.all() should have length (2)
    }
  }

  it should "return locations containing a given name fragment" in new Test {
    withMongo { mongoProps =>
      collection.insert(MongoDBObject("name" -> "Malaga", "latitude" -> 54.45, "longitude" -> 18.56), WriteConcern.Safe)
      collection.insert(MongoDBObject("name" -> "Warsaw", "latitude" -> 52.23, "longitude" -> 21.01), WriteConcern.Safe)
      collection.insert(MongoDBObject("name" -> "Bremal", "latitude" -> 0.0, "longitude" -> 0.0), WriteConcern.Safe)
      collection.insert(MongoDBObject("name" -> "Palma Mallorca", "latitude" -> 0.0, "longitude" -> 0.0), WriteConcern.Safe)

      repository.byNameFragment("mal") should have length (3)
    }
  }

  it should "return locations matching given text phrase" in new Test {
    withMongo { mongoPops =>
      collection.insert(MongoDBObject("name" -> "Sopot", "latitude" -> 54.45, "longitude" -> 18.56), WriteConcern.Safe)
      collection.insert(MongoDBObject("name" -> "Warsaw", "latitude" -> 52.23, "longitude" -> 21.01), WriteConcern.Safe)
      collection.insert(MongoDBObject("name" -> "New York", "latitude" -> 0.0, "longitude" -> 0.0), WriteConcern.Safe)
      collection.insert(MongoDBObject("name" -> "New Delhi", "latitude" -> 0.0, "longitude" -> 0.0), WriteConcern.Safe)
      collection.insert(MongoDBObject("name" -> "New Orleans", "latitude" -> 0.0, "longitude" -> 0.0), WriteConcern.Safe)

      collection.createIndex(MongoDBObject("name" -> "text"))
      collection.indexInfo.exists( idx => idx.get("key").asInstanceOf[DBObject].toMap.values().contains("text")) should be (true)

      repository.byTextPhrase("new") should have length (3)
    }
  }

  it should "return location by id" in new Test {
    withMongo { mongoProps =>
      val id1 = "54ca8eaf5f70df15a926b528"
      collection.insert(MongoDBObject("_id" -> new ObjectId(id1), "name" -> "Sopot", "latitude" -> 54.45, "longitude" -> 18.56), WriteConcern.Safe)

      val id2 = "54ca8eb15f70df15a926b6ef"
      collection.insert(MongoDBObject("_id" -> new ObjectId(id2), "name" -> "Warsaw", "latitude" -> 52.23, "longitude" -> 21.01), WriteConcern.Safe)

      val location = repository.byId(id2)
      location.get.id should be(id2)
    }
  }

  it should "return None when id doesn't exist" in new Test {
    withMongo { mongoProps =>
      val id = "54ca8eaf5f70df15a926b528"

      val location = repository.byId(id)
      location should be(None)
    }
  }
}
