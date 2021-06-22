const mongoose = require("mongoose")
const user =mongoose.model('User',{

Name:{
    type:String,
    require:true
},
Username:{
    type:String,
    require:true,
    unique:true
},
Password:{
    type:String,
    require:true,
    
},
Profile:{
    type:String,
    default:"no-image.jpg"
},
UserType:{
type:String,
default:"Customer",
enum:["Admin","Customer","Employee"]
}

})
module.exports =user