const jwt = require('jsonwebtoken')
const User = require('../models/User')


module.exports.verifyUser =(req,res,next)=>{
const rawToken = req.headers.authorization.split(" ")[1];

const data = jwt.verify(rawToken,'secretkey')
User.findOne({_id:data.id}).then((user)=>{
    req.user = user
    console.log(req.user)
    next()
})
}
module.exports.verifyAdmin =(req,res,next)=>{

if(req.user.UserType==="Admin")
{
    next()
}
else{
    res.status(200).json({message:"Admin Routes"})
}


}
