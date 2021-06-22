const mongoose = require('mongoose')

const Product =mongoose.model('Product',{

Name:{
    type:String
},
Category:{
    type:String,
    enum:["Clothes","Electronics","Furniture","Glasses"]
},
Price:{
    type:Number
},
Description:{
    type:String
}
,
Images:[
{
   image:{ type:String}
}
],
Stock:{
    type:Number,
    default:1
},
Comment:[
    {
        user:{
            type:mongoose.Schema.Types.ObjectId,
            ref:'User'
        },
        comment:{
            type:String
        }
    }
],
Rating:[
    {
        user:{
            type:mongoose.Schema.Types.ObjectId,
            ref:'User'
        },
        rating:{
            type:Number
        }
    }  
]


})
module.exports =Product