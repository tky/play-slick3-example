package controllers


import org.specs2.specification.AfterEach
import org.jsoup.Jsoup
import play.api.db.DBApi
import play.api.db.evolutions.Evolutions
import play.api.test._
import play.api.mvc._
import play.api.test.Helpers._

import testhelpers.{EvolutionHelper, Injector}
import models.ProjectRepo

import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

class ApplicationSpec extends PlaySpecification with Results with AfterEach {

  import collection.JavaConversions._

  override def after = EvolutionHelper.clean()
  val projectRepo = Injector.inject[ProjectRepo]
  val controller = Injector.inject[Application]

  "Application#index" should {
    "should be valid" in {
      Await.result(projectRepo.create("Test"), Duration.Inf)
      val result: Future[Result] = controller.listProjects().apply(FakeRequest())
      val bodyText:String = contentAsString(result)
      val doc = Jsoup.parse(bodyText)
      doc.select(".project").map( _ attr("href")) must be_==(List("/projects/1"))
    }
  }
}
