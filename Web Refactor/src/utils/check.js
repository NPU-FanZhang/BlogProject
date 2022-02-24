
/* 特殊字符过滤 */
export function strpscript(str) {
    var patter = new RegExp(
        "[`~!@#$%^&*()_\\-+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]"
    );
    var rs = "";
    for (let i = 0; i < str.length; i++) {
        rs = rs + str.sub(i, 1).replace(patter, "");
    }
    return rs;
}
/* 验证邮箱 */
export function validateVEmail(value) {
    let reg = /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
    return !reg.test(value) ? true : false;
}

/* 验证密码 6-15位，包含大小写字母和数字*/
export function validateVPassword(value) {
    let reg = /^[A-Za-z0-9]{6,15}$/;   
    return !reg.test(value) ? true : false;
}
