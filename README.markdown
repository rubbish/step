About
=====

Step is a tiny Scala web framework inspired by [Sinatra](http://www.sinatrarb.com/) and originally based on some code I found on an [awesome blog post](http://www.riffraff.info/2009/4/11/step-a-scala-web-picoframework).

Comments, issues, and pull requests are welcome.  Please also see the new [step-user](http://groups.google.com/group/step-user) mailing list, or drop in on IRC at #stepframework on irc.freenode.org

Example
=======

    package com.thinkminimo.step

    class StepExample extends Step {

      // send a text/html content type back each time
      before {
        contentType = "text/html"
      }

      // parse matching requests, saving things prefixed with ':' as params
      get("/date/:year/:month/:day") {
        <ul>
          <li>Year: {params(":year")}</li>
          <li>Month: {params(":month")}</li>
          <li>Day: {params(":day")}</li>
        </ul>
      }

      // produce a simple HTML form
      get("/form") {
        <form action='/post' method='POST'>
          Post something: <input name='submission' type='text'/>
          <input type='submit'/>
        </form>
      }

      // handle POSTs from the form generated above
      post("/post") {
        <h1>You posted: {params("submission")}</h1>
      }

      // respond to '/' with a greeting
      get("/") {
        <h1>Hello world!</h1>
      }

      // send redirect headers
      get("/see_ya") {
        redirect("http://google.com")
      }

      // set a session var
      get("/set/:session_val") {
        session("val") = params("session_val")
        <h1>Session var set</h1>
      }

      // see session var
      get("/see") {
        session("val") match {
          case Some(v:String) => v
          case _ => "No session var set"
        }
      }

      // Actions that return byte arrays render a binary response
      get("/report.pdf") {
        contentType = "application/pdf"
        val pdf = generatePdf()
        pdf.toBytes
      }

      notFound {
        response.setStatus(404)
        "Not found"
      }
    }


Quick Start
===========
1.   __Install Java__

    You'll need Java installed; I have it running with 1.5

2.   __Install simple-build-tool__

    Step uses sbt (0.7 or above), a fantastic tool for building Scala programs.  For instructions, see [the sbt site](http://code.google.com/p/simple-build-tool/wiki/Setup)

3.   __Run sbt__

    In the directory you downloaded step to, run `sbt`.
    sbt will download core dependencies, and Scala itself if it needs to.

4.   __Download dependencies__

    At the sbt prompt, type `update`.  This will download required dependencies.

5.   __Try it out__

    At the sbt prompt, type `jetty-run`.  This will run step with the example servlet on port 8080.

6.   __Navigate to http://localhost:8080__

    You should see "Hello world."  You can poke around the example code in src/main/scala/StepExample.scala
    to see what's going on.


Supported Methods
=================

*   __before__

    Run some block before a request is returned.

*   __get(`path`)__

    Respond to a GET request.

    Specify the route to match, with parameters to store prefixed with : like Sinatra.
    "/match/this/path/and/save/:this" would match that GET request, and provide you with a
    params(":this") in your block.

*   __post(`path`)__

    Respond to a POST request.

    Posted variables are available in the `params` hash.

*   __delete(`path`)__

    Respond to a DELETE request.

*   __put(`path`)__

    Respond to a PUT request.

*   __error__

    Run some block when an error is caught.  The error is available in the variable `caughtThrowable`.

*   __after__

    Run some block after the matching get/post/delete/put block is run.

Sessions
========
Session support has recently been added.  To see how to use sessions in your Step apps, check out the test servlet, at src/test/scala/StepTest.scala

Testing Your Step Applications
==============================
Step includes StepTests - a framework for writing the unit tests for your Step application.  It's a trait with some utility functions to send requests to your app and examine the response.  It can be mixed into the test framework of your choosing.  StepTests supports HTTP GET/POST tests with or without request parameters and sessions.  For more examples, please refer to src/test/scala.

ScalaTest example
-----------------

    class TheStepAppTests extends FunSuite with ShouldMatchers with StepTests {
      // `TheStepApp` is your app which extends Step
      route(classOf[TheStepApp], "/*")

      test("simple get") {
        get("/path/to/something") {
          status should equal (200)
          body should include ("hi!")
        }
      }
    }

Specs example
-------------

    object TheStepAppTests extends Specification with StepTests {
      route(classOf[TheStepApp], "/*")
      
      "TheStepApp when using GET" should {
        "/path/to/something should return 'hi!'" in {
          get("/") {
            status mustEqual(200)
            body mustEqual("hi!")
          }
        }
      }
    }

Testing Step
============
A test suite can be found in `src/test/scala`.  Inside StepTest.scala is a small testing servlet along with some assertions.  If you've made changes to Step itself and you'd like to make sure that this testing servlet still works, you can type `test` at the sbt prompt.

Miscellaneous
=============
While Step can be run standalone for testing and meddling, you can also package it up in a .jar for use in other projects.  At the sbt prompt, type `package`.  Step's only dependency is a recent version of the servlet API. For more information, see the [sbt site](http://code.google.com/p/simple-build-tool/)

Props
=====
I'd like to thank [Gabriele Renzi](http://www.riffraff.info/) for the inspirational blog post and continual help, and Mark Harrah for help on the sbt mailing list and for creating sbt. Ant+Ivy by itself was a total bitch.

I'd also like to thank [Yusuke Kuoka](http://github.com/mumoshu) for adding sessions and header support.

Todo
====
* more tests
* 'splat' support ala Sinatra?
