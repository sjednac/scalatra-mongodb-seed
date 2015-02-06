package com.mintbeans.geo.web

import com.mintbeans.geo.core.{Location, LocationFixtures, LocationRepository}
import org.json4s.jackson.JsonMethods._
import org.json4s.{DefaultFormats, Formats, _}
import org.scalamock.scalatest.MockFactory
import org.scalatest.FlatSpecLike
import org.scalatra.test.scalatest.ScalatraSuite

class LocationControllerSpec extends ScalatraSuite with FlatSpecLike with MockFactory with LocationFixtures {

  implicit val jsonFormats: Formats = DefaultFormats

  val repository = mock[LocationRepository]
  val controller = new LocationController(repository)

  addServlet(controller, "/locations/*")

  "LocationController" should "return a sequence of locations" in {
    val fixture = locations("Sopot", "Warsaw")
    (repository.all _).expects().returning(fixture)

    get("/locations") {
      status should equal(200)

      val response = parse(body)
      val locations = response.extract[List[Location]]

      locations should have length (fixture.size)
    }
  }

  it should "return a sequence of locations filtered by a text phrase" in {
    val fixture = locations("New York", "New Orleans", "New Delhi")
    val phrase = "new"
    (repository.byTextPhrase _).expects(phrase).returning(fixture)

    get("/locations", ("phrase", phrase)) {
      status should equal(200)

      val response = parse(body)
      val locations = response.extract[List[Location]]

      locations should have length (fixture.size)
    }
  }

  it should "return a sequence of locations filtered by a name fragment" in {
    val fixture = locations("Berlin", "Bern", "Bergen", "Bermuda")
    val name = "ber"
    (repository.byNameFragment _).expects(name).returning(fixture)

    get("/locations", ("name", name)) {
      status should equal(200)

      val response = parse(body)
      val locations = response.extract[List[Location]]

      locations should have length (fixture.size)
    }
  }

  it should "return a location by id" in {
    val id = "12345"
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
    val id = "12345"
    (repository.byId _).expects(id).returning(None)

    get(s"/locations/${id}") {
      status should equal(404)
    }
  }
}
