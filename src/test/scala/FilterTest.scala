package com.thinkminimo.step

import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.ShouldMatchers

class FilterTestServlet extends Step {
  var beforeCount = 0
  before { beforeCount += 1 }
  get("/") { beforeCount }
  post("/reset-before-counter") { beforeCount = 0 }
}

class FilterTest extends StepSuite with BeforeAndAfter with ShouldMatchers {
  route(classOf[FilterTestServlet], "/*")
  
  override def beforeEach() {
	post("/reset-before-counter") {}
  }
  
  test("before is called exactly once per request") {
    get("/") { body should equal("1") }
    get("/") { body should equal("2") }
  }
  
  test("before is called when route is not found") {
    get("/this-route-does-not-exist") {
      // Should be 1, but we can't see it yet
    }
    get("/") { 
     // Should now be 2.  1 for the last request, and one for this
     body should equal ("2")
    }
  }
}
