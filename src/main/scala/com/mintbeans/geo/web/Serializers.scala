package com.mintbeans.geo.web

import org.json4s.JsonAST.JString
import org.json4s._

object Serializers {

  lazy val objectId = new CustomSerializer[org.bson.types.ObjectId](format => (
    {
      case JString(s) => new org.bson.types.ObjectId(s)
      case JNull      => null
    },
    {
      case id: org.bson.types.ObjectId => JString(id.toString)
    }
  )
  )

}
