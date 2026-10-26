const display=document.getElementById('display');
let current='0',previous=null,operator=null,resetNext=false;
function update(){display.textContent=current;}
function inputNum(n){
  if(resetNext){current='0';resetNext=false;}
  if(n==='.'&&current.includes('.'))return;
  current=(current==='0'&&n!=='.')?n:current+n;update();
}
function compute(a,b,op){
  switch(op){case '+':return a+b;case '-':return a-b;case '*':return a*b;
  case '/':return b===0?'ERR':a/b;}
}
function setOp(op){
  if(operator&&!resetNext){doEquals();}
  previous=parseFloat(current);operator=op;resetNext=true;
}
function doEquals(){
  if(operator===null||previous===null)return;
  const r=compute(previous,parseFloat(current),operator);
  if(r==='ERR'){current='Error: div by 0';operator=null;previous=null;resetNext=true;update();return;}
  current=String(parseFloat(r.toFixed(10)));operator=null;previous=null;resetNext=true;update();
}
document.querySelector('.buttons').addEventListener('click',e=>{
  const b=e.target.closest('button');if(!b)return;
  if(b.dataset.num!==undefined)inputNum(b.dataset.num);
  else if(b.dataset.op)setOp(b.dataset.op);
  else if(b.dataset.action==='clear'){current='0';previous=null;operator=null;resetNext=false;update();}
  else if(b.dataset.action==='back'){current=current.length>1?current.slice(0,-1):'0';update();}
  else if(b.dataset.action==='equals')doEquals();
});
update();
