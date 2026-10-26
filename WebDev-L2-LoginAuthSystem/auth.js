// Client-side auth demo. Passwords hashed with SHA-256 (Web Crypto) — never stored plain.
async function sha256(txt){
  const buf=await crypto.subtle.digest('SHA-256',new TextEncoder().encode(txt));
  return [...new Uint8Array(buf)].map(b=>b.toString(16).padStart(2,'0')).join('');
}
const users=()=>JSON.parse(localStorage.getItem('users')||'{}');
const saveUsers=u=>localStorage.setItem('users',JSON.stringify(u));
const tabLogin=document.getElementById('tabLogin'),tabReg=document.getElementById('tabReg');
const loginForm=document.getElementById('loginForm'),regForm=document.getElementById('regForm');
tabLogin.onclick=()=>{loginForm.classList.remove('hidden');regForm.classList.add('hidden');tabLogin.classList.add('active');tabReg.classList.remove('active');};
tabReg.onclick=()=>{regForm.classList.remove('hidden');loginForm.classList.add('hidden');tabReg.classList.add('active');tabLogin.classList.remove('active');};

regForm.addEventListener('submit',async e=>{
  e.preventDefault();const msg=document.getElementById('rMsg');msg.className='msg';
  const u=document.getElementById('rUser').value.trim();
  const p=document.getElementById('rPass').value;
  if(!u||!p){msg.textContent='All fields required.';return;}
  if(p.length<8||!/\d/.test(p)){msg.textContent='Password needs 8+ chars and a number.';return;}
  const db=users();
  if(db[u]){msg.textContent='User already exists.';return;}
  db[u]={hash:await sha256(p)};saveUsers(db);
  msg.className='msg ok';msg.textContent='Registered! You can now log in.';
});
loginForm.addEventListener('submit',async e=>{
  e.preventDefault();const msg=document.getElementById('lMsg');msg.className='msg';
  const u=document.getElementById('lUser').value.trim();
  const p=document.getElementById('lPass').value;
  if(!u||!p){msg.textContent='All fields required.';return;}
  const db=users();
  if(!db[u]||db[u].hash!==await sha256(p)){msg.textContent='Invalid credentials.';return;}
  sessionStorage.setItem('authUser',u);location.href='dashboard.html';
});
