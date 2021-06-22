const mongoose = require("mongoose")
const Product = require("./Product")
const date = new Date()
const order = mongoose.model("order",{
User:{
    type:mongoose.Schema.Types.ObjectId,
    ref:"User"
},
Product:[
    {

            product:{
                type:mongoose.Schema.Types.ObjectId,
                ref:"Product"
            },
            qty:{
                type:Number
            }

    }
],
PhoneNumber:{
    type:String,
    require:true
},
Latitude:{
    type:String,
    default:""
}
,
Longtitude:{
    type:String,
    default:""

},
Total:{
    type:Number,
    default:0

}
,
orderStatus: {// delivered // cancelled // failed
    type: Boolean,
    default:false
  },
  DeliveredStatus:{
    type:Boolean,
    default:false
  },
  EmployeeId:{

    type:mongoose.Schema.Types.ObjectId,
    ref:'User',
    deault:""
  },
  Address:{
      type:String,
    default:""

  },
  Date:{
      type:String,
      default:`${date.getFullYear()}/${date.getMonth()}/${date.getDay()}`
  }



})

module.exports = order