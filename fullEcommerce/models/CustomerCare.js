const mongoose = require('mongoose')
const Care = mongoose.model('CustomerCare',{
User:{
    type:mongoose.Schema.Types.ObjectId,
    ref:'User'
},
Message:[
    {
        sender:{
            type:mongoose.Schema.Types.ObjectId,
            ref:'User'
        },
        message:{
            type:String
        },
        format:{
            type:String,
            default:"Message"
        }
    }
]

})
module.exports = Care