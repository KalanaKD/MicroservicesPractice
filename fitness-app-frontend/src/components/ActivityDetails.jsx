import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import Typography from '@mui/material/Typography';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Box from '@mui/material/Box';
import Divider from '@mui/material/Divider';
import { getActivityDetail } from '../services/api';

const ActivityDetails = () => {
  const { id } = useParams();
  const [activity, setActivity] = useState(null);

  useEffect(() => {
    const fetchActivityDetails = async () => {
      try {
        const response = await getActivityDetail(id);
        setActivity(response.data);
      } catch (error) {
        console.error("Failed to fetch activity details", error);
      }
    };
    fetchActivityDetails();
  }, [id]);

  if (!activity) {
    return <Typography>Loading...</Typography>;
  }

  return (
    <Box sx={{ maxWidth: 800, mx: 'auto', p: 2 }}>
      {/* Activity Card */}
      <Card sx={{ mb: 2 }}>
        <CardContent>
          <Typography variant="h5" gutterBottom>
            Activity Details
          </Typography>
          <Typography variant="h6">{activity.type}</Typography>
          <Typography>Duration: {activity.duration}</Typography>
          <Typography>Calories Burned: {activity.caloriesBurned}</Typography>
          <Typography>
            Date: {new Date(activity.date).toLocaleDateString()}
          </Typography>
        </CardContent>
      </Card>

      {/* Recommendations Card */}
      {activity.recommendations && (
        <Card>
          <CardContent>
            <Typography variant="h5" gutterBottom>
              AI Recommendations
            </Typography>

            {/* Analysis */}
            <Typography variant="h6">Analysis</Typography>
            <Typography paragraph>{activity.recommendations}</Typography>

            <Divider sx={{ my: 2 }} />

            {/* Improvements */}
            <Typography variant="h6">Improvements</Typography>
            {activity?.improvements?.map((improvement, index) => (
              <Typography key={index} paragraph>
                {improvement}
              </Typography>
            ))}

            <Divider sx={{ my: 2 }} />

            {/* Suggestions */}
            <Typography variant="h6">Suggestions</Typography>
            {activity?.suggestions?.map((suggestion, index) => (
              <Typography key={index} paragraph>
                {suggestion}
              </Typography>
            ))}

            <Divider sx={{ my: 2 }} />

            {/* Safety */}
            <Typography variant="h6">Safety Guidelines</Typography>
            {activity?.safety?.map((safety, index) => (
              <Typography key={index} paragraph>
                {safety}
              </Typography>
            ))}
          </CardContent>
        </Card>
      )}
    </Box>
  );
};

export default ActivityDetails;
