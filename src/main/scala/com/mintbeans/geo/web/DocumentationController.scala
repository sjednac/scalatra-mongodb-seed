package com.mintbeans.geo.web

import org.scalatra.ScalatraServlet
import org.scalatra.swagger.{ JacksonSwaggerBase, Swagger }

class DocumentationController(implicit val swagger: Swagger) extends ScalatraServlet with JacksonSwaggerBase

