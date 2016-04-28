package controllers

import javax.inject._

import actors.{ChatRoom, UserSocket}
import akka.actor._
import akka.stream.Materializer
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json.JsValue
import play.api.libs.streams.ActorFlow
import play.api.mvc.{Action, Controller, WebSocket}

import scala.concurrent.Future

@Singleton
class Chat @Inject()(val messagesApi: MessagesApi, system: ActorSystem, mat: Materializer) extends Controller with I18nSupport {
  val User = "username"

  implicit val implicitMaterializer: Materializer = mat
  implicit val implicitActorSystem: ActorSystem = system

  val chatRoom = system.actorOf(Props[ChatRoom], "chat-room")

  val nickForm = Form(single("nickname" -> nonEmptyText))





  def leave = Action { implicit request =>
    Redirect(routes.UserController.home()).flashing("success" -> "See you soon!")
  }

  def chat = Action { implicit request =>
    request.session.get(User).map { user =>
      Ok(views.html.chat(user))
    }.getOrElse(Redirect(routes.UserController.home()))
  }

  def socket = WebSocket.acceptOrResult[JsValue, JsValue] { implicit request =>
    Future.successful(request.session.get(User) match {
      case None => Left(Forbidden)
      case Some(uid) =>
        Right(ActorFlow.actorRef(UserSocket.props(uid)))
    })
  }
}
