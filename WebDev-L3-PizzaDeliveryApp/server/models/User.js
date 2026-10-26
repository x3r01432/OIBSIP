const mongoose=require('mongoose');
module.exports=mongoose.model('User',new mongoose.Schema({
  name:String,email:{type:String,unique:true},password:String,
  role:{type:String,enum:['user','admin'],default:'user'},
  verified:{type:Boolean,default:false},
  resetToken:String,resetExpires:Date
},{timestamps:true}));
