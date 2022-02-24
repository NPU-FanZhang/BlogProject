// import Vue from 'vue'
import { createStore } from 'vuex'
import { register } from '@/api/register'


// Create a new store instance.
const store = createStore({
    state() {
        return {
            id: '',
            account: '',
            name: '',
            avatar: '',
            token: localStorage.token
        }
    },
    mutations: {
        SET_TOKEN: (state, token) => {
            state.token = token;
        },
        SET_ACCOUNT: (state, account) => {
            state.account = account;
        },
        SET_NAME: (state, name) => {
            state.name = name;
        },
        SET_AVATAR: (state, avatar) => {
            state.avatar = avatar;
        },
        SET_ID: (state, id) => {
            state.id = id;
        }
    }, actions: {
        register({ commit }, user) {
            return new Promise((resolve, reject) => {
                // 异步处理
                // 成功调用 resolve 失败调用 reject
                register(user.account, user.nickname, user.password).then(res => {
                    if (res.data.success) {
                        commit('SET_TOKEN', res.data.data);
                        localStorage.token = res.data.data
                        resolve()
                    } else {
                        reject(res.data.msg)
                    }
                }).catch(error => {
                    reject(error)
                })
            })
        }
    }
})

export default store;
