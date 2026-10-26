const r=require('express').Router();const bcrypt=require('bcryptjs');const jwt=require('jsonwebtoken');
const crypto=require('crypto');const User=require('../models/User');const {sendMail}=require('../jobs/mailer');
r.post('/register',async(req,res)=>{
  const{name,email,password}=req.body;
  if(await User.findOne({email}))return res.status(400).json({msg:'User exists'});
  const hash=await bcrypt.hash(password,10);
  const token=crypto.randomBytes(16).toString('hex');
  const u=await User.create({name,email,password:hash});
  await sendMail(email,'Verify your account',`Verify: /api/auth/verify/${u._id}`).catch(()=>{});
  res.json({msg:'Registered. Check email to verify.',id:u._id});
});
r.get('/verify/:id',async(req,res)=>{await User.findByIdAndUpdate(req.params.id,{verified:true});res.json({msg:'Verified'});});
r.post('/login',async(req,res)=>{
  const{email,password}=req.body;const u=await User.findOne({email});
  if(!u||!await bcrypt.compare(password,u.password))return res.status(400).json({msg:'Invalid credentials'});
  const token=jwt.sign({id:u._id,role:u.role},process.env.JWT_SECRET||'dev',{expiresIn:'7d'});
  res.json({token,role:u.role,name:u.name});
});
r.post('/forgot',async(req,res)=>{
  const u=await User.findOne({email:req.body.email});if(!u)return res.json({msg:'If the email exists, a reset link was sent.'});
  u.resetToken=crypto.randomBytes(16).toString('hex');u.resetExpires=Date.now()+3600000;await u.save();
  await sendMail(u.email,'Password reset',`Reset token: ${u.resetToken}`).catch(()=>{});
  res.json({msg:'Reset link sent.'});
});
module.exports=r;
