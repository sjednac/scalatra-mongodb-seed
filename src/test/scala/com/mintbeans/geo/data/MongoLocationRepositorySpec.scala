package com.mintbeans.geo.data

import com.github.simplyscala.{MongoEmbedDatabase, MongodProps}
import com.mintbeans.geo.core.{Location, LocationFixtures}
import com.mongodb.DBObject
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.MongoClient
import com.novus.salat._
import de.flapdoodle.embed.mongo.distribution.Version
import org.bson.types.ObjectId
import org.scalatest.{FlatSpec, Matchers}

class MongoLocationRepositorySpec extends FlatSpec with Matchers with MongoEmbedDatabase with SalatContext {

  sealed trait Test {
    lazy val mongoPort = 12345
    lazy val mongoClient = MongoClient("localhost", mongoPort)
    lazy val db = mongoClient("test")
    lazy val collection = db("locations")
    lazy val repository = new MongoLocationRepository(collection)

    def withMongo(fixture: MongodProps => Any) = withEmbedMongoFixture(port = mongoPort, version = Version.V2_6_1)(fixture)

    def withLocations(names: String*)(testFun: => Unit) = new LocationFixtures {
      locations(names:_*).foreach({ l =>
        collection.insert(grater[Location].asDBObject(l), WriteConcern.Safe)
      })

      testFun
    }

    def withLocationIDs(ids: ObjectId*)(testFun: => Unit) = new LocationFixtures {
      ids.foreach({ id =>
        collection.insert(grater[Location].asDBObject(locationWithId(id)), WriteConcern.Safe)
      })

      testFun
    }

    def withTextIndex(field: String)(testFun: => Unit) = {
      collection.createIndex(MongoDBObject(field -> "text"))
      collection.indexInfo.exists( idx => idx.get("key").asInstanceOf[DBObject].toMap.values().contains("text")) should be (true)

      testFun
    }

  }

  "MongoLocationRepository" should "return all locations" in new Test {
    withMongo { mongoProps =>
      withLocations("Sopot", "Warsaw", "Paris") {
        repository.all() should have length (3)
        repository.all(limit = Some(2)) should have length (2)
      }
    }
  }

  it should "return locations containing a given name fragment" in new Test {
    withMongo { mongoProps =>
      withLocations("Malaga", "Warsaw", "Bremal", "Palma Mallorca") {
        repository.byNameFragment("mal") should have length (3)
        repository.byNameFragment("mal", limit = Some(2)) should have length (2)
      }
    }
  }

  it should "return locations matching given text phrase" in new Test {
    withMongo { mongoPops =>
      withLocations("Sopot", "Warsaw", "New York", "New Delhi", "New Orleans") {
        withTextIndex("name") {
          repository.byTextPhrase("new") should have length (3)
          repository.byTextPhrase("new", limit = Some(2)) should have length (2)
        }
      }
    }
  }

  it should "return location by id" in new Test {
    withMongo { mongoProps =>
      val id1 = new ObjectId("54ca8eaf5f70df15a926b528")
      val id2 = new ObjectId("54ca8eb15f70df15a926b6ef")
      withLocationIDs(id1, id2) {
        val location = repository.byId(id1)
        location.get.id should be(id1)
      }
    }
  }

  it should "return None when id doesn't exist" in new Test {
    withMongo { mongoProps =>
      val id = "54ca8eaf5f70df15a926b528"
      val location = repository.byId(new ObjectId(id))
      location should be(None)
    }
  }
}
