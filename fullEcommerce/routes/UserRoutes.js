const express = require('express')
const route = express.Router()
const User = require('../models/User')
const { verifyUser} = require('../middleware/auth')
const crypt = require('bcryptjs')
const jwt = require('jsonwebtoken')
const upload = require('../middleware/upload')
const { router } = require('websocket')

route.post("/login",(req,res)=>{

const username = req.body['username']
const password = req.body['password']
console.log(req.body)
User.findOne({Username:username}).then((data)=>{

if(data)
{

crypt.compare(password,data.Password).then((result)=>{
console.log(data)
if(result==true)
{

    const token = jwt.sign({id:data._id},'secretkey')
    return res.status(200).json({success:true,token:token,data:data})
}

})



}
else{
    return res.status(200).json({success:false,message:"No User Found"})
}


})
})


route.post("/register",(req,res)=>{
const name = req.body.Name
const username = req.body.Username
const password = req.body.Password,
usertype = req.body.UserType
console.log(req.body)

crypt.hash(password,10).then((hash)=>{

    const user = new User({

        Name:name,
        Username:username,
        Password:hash,
        UserType:usertype
    })
    user.save().then((data)=>{
        return res.status(200).json({success:true,message:user._id})
    })
})

})



route.put("/update/profile/:id",upload.single('profile'),(req,res)=>{
const id = req.params.id
User.findByIdAndUpdate({_id:id},{
    Profile:req.file.filename
}).then((data)=>{
    return res.status(200).json({success:true,message:"Success"})
})
})


route.put('/update/user',verifyUser,(req,res)=>{

    const username = req.body.Username
    const name = req.body.Name
User.findByIdAndUpdate({_id:req.user._id},{
Name:name,
Username:username
}).then((data)=>{
    return res.status(200).json({success:true,message:"Success"})
})
})


route.put('/updatePassword',verifyUser,(req,res)=>{
    crypt.hash(req.body.Password,10).then((hash)=>{
    User.findByIdAndUpdate({_id:req.user._id},{
    Password:hash
    }).then((data)=>{
        return res.status(200).json({success:true,message:"Success"})
    })
})
})

route.post("/verifyPassword",verifyUser,(req,res)=>{
const password = req.body["Password"]
User.findById({_id:req.params.id}).then((data)=>{
crypt.compare(password,data.Password).then((result)=>{

    if(result ==true)
    {
        return res.status(200).json({successs:true,message:"Verified"})
    }
})
})

})



module.exports = route