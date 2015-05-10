package com.implicitinnovations.eservice.identity.boot

import scaldi.Injectable
import scaldi.Injector
import demo.Tables._
import demo.Tables.profile._
import scala.collection.immutable.List
import slick.driver.PostgresDriver.simple._




trait UserDao extends Injectable {
  def getAllUsers: List[User]
}

class UserDaoImpl(implicit inj: Injector) extends UserDao  {
  //protected lazy val db = inject[DatabaseDef]
  
  val URL = inject[String](identified by 'URL)
   val USERNAME = inject[String](identified by 'USERNAME)
   val PASSWORD = inject[String](identified by 'PASSWORD)
   // val url = "jdbc:postgresql://localhost:5432/satya?user=postgres&password=abiram06"
    val db = Database.forURL(URL,user=USERNAME,password=PASSWORD,driver="org.postgresql.Driver")


  override def getAllUsers: List[User] = {     
    db.withSession { implicit session ⇒   
      val userList = Users.list
      userList.foreach { println }           
    }
      val u=new User(true,"d@implicitinnovations.com","d","r",Some(1));
     return List(u)
    // return l; 
  }

}