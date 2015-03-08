package com.mintbeans.geo.core

trait LocationRepository {
  def all(limit: Option[Int] = None): Seq[Location]
  def byId(id: org.bson.types.ObjectId): Option[Location]
  def byNameFragment(name: String, limit: Option[Int] = None): Seq[Location]
  def byTextPhrase(phrase: String, limit: Option[Int] = None): Seq[Location]
}
