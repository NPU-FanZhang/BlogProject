import axios from "axios";

const service = axios.create({
    baseURL: "http://localhost:8888",
    timeout: 5000,
    headers: { "content-type": "application/json;charset=UTF-8" }
})
export default service