import request from "@/request"
export function getArticles(params){
    return request({
        method:'post',
        url:'/articles',
        data:params
    })
}
// 可以同时抛出其他接口，同一类的接口写在一个文件下
// export function getArticlesById(params){
//     return request({
//         method:'post',
//         url:'/articles',
//         data:params // 这里的params是需要向后端传递的参数
//     })
// }