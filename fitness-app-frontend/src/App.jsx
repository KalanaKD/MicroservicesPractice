import './App.css'
import { BrowserRouter as Router, Navigate , Route, Routes , useLocation } from "react-router";
import Button from '@mui/material/Button';

function App() {

  return (
    <>
      <Router>
        <Button variant="contained" color="#dc004e">Login</Button>
      </Router>
    </>
  )
}

export default App
