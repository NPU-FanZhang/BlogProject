import request from "@/request"
export function register(user){
    return request({
        method:'post',
        url:'/register',
        data:user
    })
}
export function login(user){
    return request({
        method:'post',
        url:'/login',
        "data":{
            "account":user.email,
            "password":user.pass
        }
    })
}