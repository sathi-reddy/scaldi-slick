package com.implicitinnovations.eservice.identity.boot

import scala.concurrent.Future
import scaldi.Injector
import scaldi.Injectable

trait UserService extends Injectable {
  
   def getAllUsers: List[User]

}

class UserServiceImpl(implicit val inj: Injector) extends UserService {
  lazy val userDao = inject[UserDao]
  override def getAllUsers: List[User] = {
    userDao.getAllUsers
  }
  
}