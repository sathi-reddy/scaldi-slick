package com.implicitinnovations.eservice.identity.boot

import scaldi.Module
import scaldi.akka.AkkaInjectable
import com.typesafe.config.ConfigFactory


class Daos extends Module
{
 bind[UserDao] to new UserDaoImpl

}

class Services extends Module
{
 bind[UserService] to new UserServiceImpl

}

class Configs extends Module {
  private val config = ConfigFactory.load()

  bind[String] as 'URL to config.getString("db.url")
  bind[String] as 'USERNAME to config.getString("db.user")
  bind[String] as 'PASSWORD to config.getString("db.password")
}



object Boot extends App with AkkaInjectable {
  
   implicit val appModule =  new Daos ::new Services:: new Configs
     lazy val userDao = inject[UserService]
     val users=userDao.getAllUsers
   //users.foreach { println }
   

}