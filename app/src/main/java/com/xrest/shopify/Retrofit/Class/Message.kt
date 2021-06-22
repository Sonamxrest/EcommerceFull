package com.xrest.shopify.Retrofit.Class

class Message(
    var _id:String?=null,
    var User:User?=null,
    var Message:MutableList<InnerMessage>?=null



)

class InnerMessage(
var _id:String?=null,
var sender:User?=null,
var message:String?=null,
var type:String?=null

)