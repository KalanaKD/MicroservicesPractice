import './App.css'
import { BrowserRouter as Router, Navigate , Route, Routes , useLocation } from "react-router";
import Button from '@mui/material/Button';
import { useEffect, useContext, useState } from 'react';
import { AuthContext } from 'react-oauth2-code-pkce';
import { useDispatch } from 'react-redux';
import { setCredentials } from './store/authSlice';

function App() {
  const {token , tokenData, logIn, logOut, isAuthenticated} = useContext(AuthContext);
  const dispatch = useDispatch();
  const [authReady, setAuthReady] = useState(false);

  useEffect(()=>{
    if(token){
      dispatch(setCredentials({token, user:tokenData}));  
      setAuthReady(true);
    }
  },[token, tokenData, dispatch]);


  return (
    <>
      <Router>
        {!token ? (
          <Button variant="contained" color="#dc004e"
        onClick={()=>{
          logIn()
          }}>Login</Button>
        ) : (
          <div>
            <pre>{JSON.stringify(tokenData, null, 2)}</pre>
            <pre>{JSON.stringify(token, null, 2)}</pre>
          </div>
        )}
      </Router>
    </>
  );
}

export default App
