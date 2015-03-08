package com.mintbeans.geo.web

import com.mintbeans.geo.core.{Location, LocationFixtures, LocationRepository}
import org.bson.types.ObjectId
import org.json4s.jackson.JsonMethods._
import org.json4s.{DefaultFormats, Formats, _}
import org.scalamock.scalatest.MockFactory
import org.scalatest.FlatSpecLike
import org.scalatra.test.scalatest.ScalatraSuite

class LocationControllerSpec extends ScalatraSuite with FlatSpecLike with MockFactory with LocationFixtures {

  implicit val jsonFormats: Formats = DefaultFormats + Serializers.objectId

  val repository = mock[LocationRepository]
  val controller = new LocationController(repository)

  addServlet(controller, "/locations/*")

  "LocationController" should "return a sequence of locations" in {
    val fixture = locations("Sopot", "Warsaw")
    (repository.all _).expects(Some(controller.maxAllowedLimit)).returning(fixture)

    get("/locations") {
      status should equal(200)

      val response = parse(body)
      val locations = response.extract[List[Location]]

      locations should have length (fixture.size)
    }
  }

  it should "return a sequence of locations with given length limit" in {
    val fixture = locations("Sopot", "Warsaw")
    val limit = fixture.size
    (repository.all _).expects(Some(limit)).returning(fixture)

    get("/locations", ("limit", limit.toString) ) {
      status should equal(200)

      val response = parse(body)
      val locations = response.extract[List[Location]]

      locations should have length (fixture.size)
    }
  }

  it should "return a sequence of locations filtered by a text phrase" in {
    val fixture = locations("New York", "New Orleans", "New Delhi")
    val phrase = "new"
    (repository.byTextPhrase _).expects(phrase, Some(controller.maxAllowedLimit)).returning(fixture)

    get("/locations", ("phrase", phrase)) {
      status should equal(200)

      val response = parse(body)
      val locations = response.extract[List[Location]]

      locations should have length (fixture.size)
    }
  }

  it should "return a sequence of locations filtered by a text phrase (with limit)" in {
    val fixture = locations("New York", "New Orleans", "New Delhi")
    val phrase = "new"
    val limit = fixture.size
    (repository.byTextPhrase _).expects(phrase, Some(limit)).returning(fixture)

    get("/locations", ("phrase", phrase), ("limit", limit.toString)) {
      status should equal(200)

      val response = parse(body)
      val locations = response.extract[List[Location]]

      locations should have length (fixture.size)
    }
  }

  it should "return a sequence of locations filtered by a name fragment" in {
    val fixture = locations("Berlin", "Bern", "Bergen", "Bermuda")
    val name = "ber"
    (repository.byNameFragment _).expects(name, Some(controller.maxAllowedLimit)).returning(fixture)

    get("/locations", ("name", name)) {
      status should equal(200)

      val response = parse(body)
      val locations = response.extract[List[Location]]

      locations should have length (fixture.size)
    }
  }

  it should "return a sequence of locations filtered by a name fragment (with limit)" in {
    val fixture = locations("Berlin", "Bern", "Bergen", "Bermuda")
    val name = "ber"
    val limit = fixture.size
    (repository.byNameFragment _).expects(name, Some(limit)).returning(fixture)

    get("/locations", ("name", name), ("limit", limit.toString)) {
      status should equal(200)

      val response = parse(body)
      val locations = response.extract[List[Location]]

      locations should have length (fixture.size)
    }
  }

  it should "fail when list size limit exceeds the maximum allowed value" in {
    val overflowedLimit = controller.maxAllowedLimit + 1
    get("/locations", ("limit", overflowedLimit.toString)) {
      status should equal(400)
    }
  }

  it should "fail when list size limit is zero" in {
    get("/locations", ("limit", "0")) {
      status should equal(400)
    }
  }

  it should "fail when list size limit is less than zero" in {
    get("/locations", ("limit", "-1")) {
      status should equal(400)
    }
  }

  it should "return a location by id" in {
    val id = new ObjectId("54ca8eaf5f70df15a926b528")
    val fixture = Some(locationWithId(id))
    (repository.byId _).expects(id).returning(fixture)

    get(s"/locations/${id}") {
      status should equal(200)

      val response = parse(body)
      val location = response.extract[Location]
      location.id should be (id)
    }
  }

  it should "set status 404 when given location id does not exist" in {
    val id = new ObjectId("54ca8eaf5f70df15a926b528")
    (repository.byId _).expects(id).returning(None)

    get(s"/locations/${id}") {
      status should equal(404)
    }
  }
}
