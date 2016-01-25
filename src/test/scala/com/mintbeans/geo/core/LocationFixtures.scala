package com.mintbeans.geo.core

import org.bson.types.ObjectId

trait LocationFixtures {

  def locations(names: String*) = names.zipWithIndex.map({
    case (name, i) => Location(ObjectId.createFromLegacyFormat(0, 0, i), name, Point(0.0, 0.0))
  })

  def locationWithId(id: org.bson.types.ObjectId) = Location(id, "Test", Point(0.0, 0.0))
}
