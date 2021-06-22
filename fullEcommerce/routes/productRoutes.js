const express = require('express')
const { verifyUser, verifyAdmin } = require('../middleware/auth')
const upload = require("../middleware/upload")
const router = express.Router()
const Product = require('../models/Product')
const route = require('./UserRoutes')

router.post("/insertProduct",(req,res)=>{
    const name = req.body.Name
    const Price = req.body.Price
    const category = req.body.Category
    const description = req.body.Description
    const stock = req.body.Stock
    const product = new Product({Name:name,Price:parseInt(Price),Category:category,
    Description:description,Stock:parseInt(stock),Images:[],Rating:[],Comment:[]})
    product.save().then((data)=>{
        return res.status(200).json({success:true, message:product._id})
    })

})

router.put("/updateProduct/:id",(req,res)=>{
    const name = req.body.Name
    const Price = req.body.Price
    const category = req.body.Category
    const description = req.body.Description
    const stock = req.body.Stock
     Product.findOneAndUpdate({_id:req.params.id},{Name:name,Price:parseInt(Price),Category:category,
    Description:description,Stock:parseInt(stock),Images:[],Rating:[],Comment:[]})
    .then((data)=>{
        return res.status(200).json({success:true, message:product._id})
    })

})



router.put("/upload/image/:id",upload.array('image'),(req,res)=>{

// 1 file
// 3 ta purano aauxa re 

    let newImage =[]
    console.log(req.files)
    req.files.forEach(data => {
        console.log(data)
        newImage.push({image:data.filename})  
    });
   Product.findByIdAndUpdate({_id:req.params.id},{
       Images:newImage
   }).then((data)=>{
       return res.status(200).json({success:true,message:"Done"})
   })
})

route.delete("/deleteProduct/:id",(req,res)=>{
Product.findByIdAndDelete({_id:req.params.id}).then((Data)=>{
return res.status(200).json({success:true,message:"Deleted"})
})
})


router.post('/showAll',(req,res)=>{
    console.log(req.body.category)
    {Category:req.body.category}
Product.find().populate('Comment.user').populate('Rating.user').then((data)=>{
    console.log("All")
    return res.status(200).json({success:true,data:data})
})
})

// router.get('/search/:term',(req,res)=>{
// const term = req.params.term
// Product.find({Name:{$regex:term,$options:'$i'}}).then((data)=>{
//     return res.status(200).json({success:true,data:data})
// })
// })

// router.post("/filter",(req,res)=>{
//     const price = req.body.Price
//     const prices = req.body.Prices
//     const category = req.body.Category
//     console.log(req.body)
//     Product.find({ Category:category,Price:{$gt:parseInt(price),$lt:parseInt(prices)}  }).then((data)=>{
//         return res.status(200).json({success:true,data:data})
//     })
// })






router.put("/comment/:id",verifyUser,(req,res)=>{
    Product.findByIdAndUpdate({_id:req.params.id},{
$push:{Comment:{user:req.user._id,comment:req.body.comment}}
    }).then((data)=>{
        console.log(data)
        return res.status(200).json({success:true,message:""})

    })
})

router.put("/deleteComment/:pid/:oid",(req,res)=>{

    Product.findByIdAndUpdate({_id:req.params.pid},{
        $pull:{Comment:{_id:req.params.oid}}
            }).then((data)=>{
                return res.status(200).json({success:true,message:"Done"})
        
            })


})
router.put("/updateComment/:pid/:oid",verifyUser,(req,res)=>{

    Product.update({'Comment._id':req.params.oid},{
        $set:{'Comment.$._id':req.params.oid,'Comment.$.user':req.user._id,'Comment.$.comment':req.body.comment}
            }).then((data)=>{
                return res.status(200).json({success:true,message:"Done"})
        
            })


})


//comment edit ra delete


   
router.put('/rate/:id',verifyUser,(req,res)=>{
    console.log(req.body.Rating)
    Product.findOne({_id:req.params.id,'Rating.user':req.user._id}).then((data)=>{
        if(data)
    {
        console.log(data)
        Product.update({'Rating.user':req.user._id},{
            
                $set:{'Rating.$.rating':parseInt(req.body.Rating),'Rating.$.user':req.user._id}
            
                }).then((datas)=>{
                    return res.status(200).json({success:true,message:"Done"})
                }) 

    }
    else{
        Product.findByIdAndUpdate({_id:req.params.id},{
            $push:{Rating:{user:req.user._id,rating:parseInt(req.body.Rating)}}
                }).then((data)=>{
                    return res.status(200).json({success:true,message:"data xaina"})
                })   
    }
        

    })
    
                
})         













module.exports =router