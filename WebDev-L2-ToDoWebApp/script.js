let tasks=JSON.parse(localStorage.getItem('tasks')||'[]');
const pl=document.getElementById('pendingList'),cl=document.getElementById('completedList');
function save(){localStorage.setItem('tasks',JSON.stringify(tasks));}
function fmt(t){return t?new Date(t).toLocaleString():'';}
function render(){
  pl.innerHTML='';cl.innerHTML='';
  let p=0,c=0;
  tasks.forEach((t,i)=>{
    const li=document.createElement('li');if(t.done)li.className='done';
    li.innerHTML=`<span class="text">${t.text}</span>
    <span class="ts">added ${fmt(t.added)}${t.done?' · done '+fmt(t.completed):''}</span>
    <button class="toggle">${t.done?'Undo':'Done'}</button>
    <button class="edit">Edit</button><button class="del">Delete</button>`;
    li.querySelector('.toggle').onclick=()=>{t.done=!t.done;t.completed=t.done?Date.now():null;save();render();};
    li.querySelector('.edit').onclick=()=>{const n=prompt('Edit task:',t.text);if(n!==null&&n.trim()){t.text=n.trim();save();render();}};
    li.querySelector('.del').onclick=()=>{tasks.splice(i,1);save();render();};
    if(t.done){cl.appendChild(li);c++;}else{pl.appendChild(li);p++;}
  });
  document.getElementById('pendingCount').textContent=p;
  document.getElementById('completedCount').textContent=c;
  document.getElementById('pendingEmpty').style.display=p?'none':'block';
  document.getElementById('completedEmpty').style.display=c?'none':'block';
}
document.getElementById('addForm').addEventListener('submit',e=>{
  e.preventDefault();const inp=document.getElementById('taskInput');
  const v=inp.value.trim();if(!v)return;
  tasks.push({text:v,done:false,added:Date.now(),completed:null});
  inp.value='';save();render();
});
render();
