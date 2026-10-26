const mongoose=require('mongoose');
module.exports=mongoose.model('Inventory',new mongoose.Schema({
  type:{type:String,enum:['base','sauce','cheese','veggie']},
  name:String,stock:Number,threshold:{type:Number,default:20}
}));
