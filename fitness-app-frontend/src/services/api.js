import axios from 'axios';

const API_URL = 'http://localhost:8084/api';

const api = axios.create({
    baseURL: API_URL
});

api.interceptors.request.use((config) => {
    const userId = localStorage.getItem('userId');
    const token = localStorage.getItem('token');
    if (token) {
        config.headers['Authorization'] = `Bearer ${token}`;
    }
    // unify header name exactly as backend expects
    if (userId) {
        config.headers['X-User-ID'] = userId;
    }
    return config;
});

export const getActivities = () => {
    return api.get('/activities');   // return the promise
}

export const addActivity = (activity) => {
    return api.post('/activities', activity);
}

export const getActivityDetail = (id) => {
    return api.get(`/recommendations/activity/${id}`); // use GET for detail
}
