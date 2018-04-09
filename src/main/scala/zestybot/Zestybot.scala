package zestybot

import com.softwaremill.sttp._
import com.softwaremill.sttp.akkahttp.AkkaHttpBackend
import com.softwaremill.sttp.circe._

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global

class Zestybot(clientId: String) {
  implicit val backend = AkkaHttpBackend()
  import io.circe.generic.auto._, io.circe.syntax._

  def slackableMessage = {
    for {
      meals <- sttp.get(uri"https://api.zesty.com/portal_api/meals?client_id=${clientId}").response(asJson[Meals]).send().map(_.unsafeBody.right.get)
      todaysMeal = meals.meals.find(_.delivery_date startsWith "2018-04-09").get
      mealDetail <- sttp.get(uri"https://api.zesty.com/portal_api/meals/${todaysMeal.id}").response(asJson[MealDetail]).send().map(_.unsafeBody.right.get)
      dishes <- Future.traverse(mealDetail.meal_items) { mealItem =>
        sttp.get(uri"https://api.zesty.com/portal_api/dishes/${mealItem.dish_id}").response(asJson[Dishes]).send().map(_.unsafeBody.right.get.dishes.head)
      }
    } yield {
      s"""
         |${todaysMeal.restaurant_name}
         |${dishes.map(_.description).mkString(",")}
       """.stripMargin
    }
  }
}

object Zestybot {
  def main(args: Array[String]) = {
    import scala.concurrent.duration._
    println(Await.result(new Zestybot(args(0)).slackableMessage, 10.seconds))
  }
}