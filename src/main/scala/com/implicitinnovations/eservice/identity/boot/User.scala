package com.implicitinnovations.eservice.identity.boot

case class User(
  isActive: Boolean,
  email: String,
  firstName: String,
  lastName: String,
  userId: Option[Long])