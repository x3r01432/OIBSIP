const jwt=require('jsonwebtoken');
exports.auth=(req,res,next)=>{
  const t=(req.headers.authorization||'').replace('Bearer ','');
  if(!t)return res.status(401).json({msg:'No token'});
  try{req.user=jwt.verify(t,process.env.JWT_SECRET||'dev');next();}
  catch{res.status(401).json({msg:'Invalid token'});}
};
exports.admin=(req,res,next)=>req.user?.role==='admin'?next():res.status(403).json({msg:'Admin only'});
