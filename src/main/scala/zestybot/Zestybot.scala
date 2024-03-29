package zestybot

import java.net.URI
import java.time.LocalDate

import com.softwaremill.sttp._
import com.softwaremill.sttp.akkahttp.AkkaHttpBackend
import com.softwaremill.sttp.circe._

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global

case class SlackMessage(text: String, attachments: Seq[Attachment])
case class Attachment(title: String, text: String, image_url: String)

class Zestybot(clientId: String, slackWebhookUri: String) {
  implicit val backend = AkkaHttpBackend()
  import io.circe.generic.auto._, io.circe.java8.time._

  def doIt() = {
    try {
      import scala.concurrent.duration._
      Await.result(slackableMessage.flatMap(postToSlackWebhook), 10.seconds)
    } finally {
      backend.close()
    }
  }

  def postToSlackWebhook(slackMessage: SlackMessage) = {
    sttp.post(Uri(new URI(slackWebhookUri))).body(slackMessage).send()
  }

  def slackableMessage = {
    for {
      meals <- sttp.get(uri"https://api.zesty.com/portal_api/meals?client_id=${clientId}").response(asJson[Meals]).send().map(_.unsafeBody.right.get)
      todaysMeal = meals.meals.find(_.delivery_date.toLocalDate.isEqual(LocalDate.now())).get // TODO: handle more than one meal
      mealDetail <- sttp.get(uri"https://api.zesty.com/portal_api/meals/${todaysMeal.id}").response(asJson[MealDetail]).send().map(_.unsafeBody.right.get)
      dishes <- Future.traverse(mealDetail.meal_items) { mealItem =>
        sttp.get(uri"https://api.zesty.com/portal_api/dishes/${mealItem.dish_id}").response(asJson[Dishes]).send().map(_.unsafeBody.right.get.dishes.head)
      }
    } yield {
      SlackMessage(todaysMeal.restaurant_name, dishes.map { dish => Attachment(dish.name, dish.description, dish.full_image_path) })
    }
  }
}

object Zestybot {
  def main(args: Array[String]): Unit = {
    val zestybot = new Zestybot(args(0), args(1))
    zestybot.doIt()
  }
}