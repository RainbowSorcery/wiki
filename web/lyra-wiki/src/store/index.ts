import { createStore } from 'vuex'

declare let SessionStorageUtils: any

const USER = "USER"

export default createStore({
    state: {
        user:SessionStorageUtils.get(USER) || {}
    },
    mutations: {
        setUser(state, user) {
            state.user = user
            SessionStorageUtils.set(USER, user)
        }
    },
    actions: {

    },
    modules: {

    }
})