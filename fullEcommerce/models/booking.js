const mongoose = require('mongoose')
const Booking = mongoose.model('Booking',{

Date:{
    type:Number,
    default:Date.now()
},
Product:[
    {
        product:{
            type:mongoose.Schema.Types.ObjectId,
            ref:'Product'
        },
        qty:{
            type:Number,
            default:1
        }
       
    }
],
User:{
    type:mongoose.Schema.Types.ObjectId,
    ref:'User'
}



})
module.exports = Booking