const btn=document.getElementById('convertBtn');
const err=document.getElementById('error');
const out=document.getElementById('results');
function toCelsius(v,u){if(u==='C')return v;if(u==='F')return (v-32)*5/9;return v-273.15;}
btn.addEventListener('click',()=>{
  err.textContent='';out.innerHTML='';
  const raw=document.getElementById('value').value.trim();
  const unit=document.getElementById('unit').value;
  if(raw===''||isNaN(Number(raw))){err.textContent='Please enter a valid number.';return;}
  const v=Number(raw);
  const c=toCelsius(v,unit);
  if(c< -273.15){err.textContent='Below absolute zero (-273.15°C) is not possible.';return;}
  const f=c*9/5+32;const k=c+273.15;
  out.innerHTML=`<div>Celsius: ${c.toFixed(2)} °C</div><div>Fahrenheit: ${f.toFixed(2)} °F</div><div>Kelvin: ${k.toFixed(2)} K</div>`;
});
