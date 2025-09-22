import React from 'react'
import Box from '@mui/material/Box';
import { useState } from 'react';
import FormControl from '@mui/material/FormControl';
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import Select from '@mui/material/Select';
import { addActivity } from '../services/api';

const ActivityForm = ({ onActivityAdded }) => {

  const [activity, setActivity] = useState({
    type: "RUNNING",
    duration: '',
    caloriesBurned: '',
    additionalMetrics: {}
  });
  
  const handleSubmit = async (event) => {
  event.preventDefault();
  try{
    await addActivity(activity);
    onActivityAdded();
    setActivity({type:"RUNNING", duration:'', caloriesBurned:'', });
    }
  catch(error){
    console.error("Failed to add activity", error);
    }
  }

  return (
      <Box component="form" onSubmit={handleSubmit} sx={{ mb:4 }} className="surface p-4">
        <FormControl fullWidth sx={{mb:2}}>
          <InputLabel>Activity Type</InputLabel>
          <Select value={activity.type}
                  onChange={(e)=>{setActivity({...activity, type: e.target.value})}}>
                    <MenuItem value="RUNNING" > Running </MenuItem>
                    <MenuItem value="WALKING" > Walking </MenuItem>
                    <MenuItem value="CYCLING" > Cycling </MenuItem>
          </Select>
        </FormControl>
        <TextField fullWidth 
                    label="Duration(minutes)"
                    type="number"
                    sx={{mb:2}}
                    value={activity.duration}
                    onChange={(e)=>{setActivity({...activity, duration: e.target.value})}}/>

        <TextField fullWidth 
                    label="Calories(burned)"
                    type="number"
                    sx={{mb:2}}
                    value={activity.caloriesBurned}
                    onChange={(e)=>{setActivity({...activity, caloriesBurned: e.target.value})}}/>

        <Button type="submit" variant="contained" className="primary-btn">Add Activity</Button>
      </Box>
  )
}

export default ActivityForm