const express = require('express')
const { verifyUser } = require('../middleware/auth')
const route = express.Router()
const Booking = require('../models/booking')

route.post('/book/:id',verifyUser,(req,res)=>{
const pid = req.params.id
const qty = parseInt(req.body.qty)

var flag =false
var index
var qtys
Booking.findOne({User:req.user._id}).then((data)=>{
    console.log(data)
if(data)
{
for( i =0;i<data.Product.length;i++)
{
   
if(data.Product[i].product == pid)
{  
    flag = true
    index = data.Product[i]._id
    qtys = data.Product[i].qty
    break
}
else{
    flag = false
}
}
if(flag===true)
{
    Booking.update({'Product._id':index},{
    $set:{'Product.$.qty':qtys+qty}
    }).then((data)=>{
        return res.status(200).json({success:true,message:"Added Qty One Product"})
    })
}
else{
    Booking.findOneAndUpdate({User:req.user._id},{
        $push:{Product:{product:pid,qty:qty}}
    }).then((result)=>{
        return res.status(200).json({success:true,message:"aDDED One Product"})
    })
}

}

else{

    const booking  = {product:pid,qty:qty}
    const data = new Booking({User:req.user._id,Product:[booking]})
    data.save().then((result)=>{
    return res.status(200).json({success:true,message:"Booked One Product"})
})
}

})
})




route.get('/showBooking',verifyUser,(req,res)=>{
    let total =0
Booking.findOne({User:req.user._id}).populate('Product.product').then((data)=>{
if(data)
{
    data.Product.forEach(element => {
        total += parseInt(element.product.Price) * element.qty
        
    })
    return res.status(200).json({success:true,data:data,total:total.toString()})

}
else{
return res.status(200).json({success:true,data:null,total:total.toString()})

}

   
})

route.put('/updateBooking/:oid',(req,res)=>{
    const oid = req.params.oid
    const qty = parseInt(req.body.qty)
    Booking.update({'Product._id':oid},{
        $set:{'Product.$.qty':qty}
    }).then((data)=>{
        return res.status(200).json({success:true,message:"Done"})
    })
})

route.put('/deleteBooking/:oid',verifyUser,(req,res)=>{
Booking.findOneAndUpdate({User:req.user._id},{
    $pull:{Product:{_id:req.params.oid}}
}).then((data)=>{
    return res.status(200).json({success:true,message:"One Deleed"})

})


})


})




module.exports = route