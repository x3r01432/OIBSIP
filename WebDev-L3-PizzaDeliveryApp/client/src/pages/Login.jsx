import {useState} from 'react';import api from '../api';
export default function Login(){
  const[f,setF]=useState({email:'',password:''});const[m,setM]=useState('');
  const submit=async e=>{e.preventDefault();try{const{data}=await api.post('/auth/login',f);
    localStorage.setItem('token',data.token);localStorage.setItem('role',data.role);location.href='/';}
    catch(err){setM(err.response?.data?.msg||'Error');}};
  return(<form onSubmit={submit} style={{padding:20}}><h2>Login</h2>
  <input placeholder="Email" onChange={e=>setF({...f,email:e.target.value})}/><br/>
  <input type="password" placeholder="Password" onChange={e=>setF({...f,password:e.target.value})}/><br/>
  <button>Login</button><p>{m}</p></form>);
}
