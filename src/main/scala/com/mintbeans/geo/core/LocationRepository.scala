package com.mintbeans.geo.core

trait LocationRepository {
  def all(): Seq[Location]
  def byId(id: org.bson.types.ObjectId): Option[Location]
  def byNameFragment(name: String): Seq[Location]
  def byTextPhrase(phrase: String): Seq[Location]
}
