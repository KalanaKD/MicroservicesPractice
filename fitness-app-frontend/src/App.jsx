import './App.css'
import { BrowserRouter as Router, Navigate , Route, Routes , useLocation } from "react-router";
import Button from '@mui/material/Button';
import { useEffect, useContext, useState } from 'react';
import { AuthContext } from 'react-oauth2-code-pkce';
import { useDispatch } from 'react-redux';
import { setCredentials } from './store/authSlice';
import Box from '@mui/material/Box';
import ActivityForm from './components/ActivityForm';
import ActivityDetails from './components/ActivityDetails';
import ActivityList from './components/ActivityList';
import { addActivity } from './services/api';

const ActivitiesPage = () => {
  const handleActivityAdded = async (activity) => {
    try {
      await addActivity(activity);
      window.location.reload();
    } catch (error) {
      console.error("Failed to add activity", error);
    }
  };

  return (
    <Box component="section" sx={{ p: 2, border: '1px dashed grey' }}>
      <ActivityForm onActivitiesAdded={handleActivityAdded} />
      <ActivityList />
    </Box>
  )
}


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
          // <div>
          //   <pre>{JSON.stringify(tokenData, null, 2)}</pre>
          //   <pre>{JSON.stringify(token, null, 2)}</pre>
          // </div>
          <Box component="section" sx={{ p: 2, border: '1px dashed grey' }}>
            <Routes>
              <Route path="/activities" element={<ActivitiesPage />} />
              <Route path="/activities/:id" element={<ActivityDetails />} />
              <Route path="/" element={token ? <Navigate to="/activities" replace/>: <div>Welcome! Please Login...</div>} />
            </Routes>
          </Box>
        )}
      </Router>
    </>
  );
}

export default App
