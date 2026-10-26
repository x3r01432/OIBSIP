const nodemailer=require('nodemailer');
let t=null;
function tx(){if(t)return t;t=nodemailer.createTransport({host:process.env.SMTP_HOST,port:+(process.env.SMTP_PORT||587),
  auth:{user:process.env.SMTP_USER,pass:process.env.SMTP_PASS}});return t;}
exports.sendMail=(to,subject,text)=>tx().sendMail({from:process.env.SMTP_USER,to,subject,text});
