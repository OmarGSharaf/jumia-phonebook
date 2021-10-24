import axios from 'axios';

const API_URL = process.env.VUE_APP_API_URL || 'http://localhost:8080/';
let axiosInstance = axios.create({
    baseURL: API_URL,
    headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json'
    }
});

axiosInstance.interceptors.request.use(request => {
    request.params = request.params || {};
    return request;
});

export default axiosInstance;
