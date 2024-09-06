import { defineStore } from "pinia";

export const useUserStore = defineStore("user", {
    state: () => ({
        user: null,
    }),
    getters: {
        isLogIn: (state) => !!state.user,
    },
    actions: {
        setUser(user) {
        this.user = user;
        },
        logout() {
        this.user = null;
        },
        updateUser(updatedUser) {
        if (this.user) {
            this.user = { ...this.user, ...updatedUser };
        }
        },
    },
    });
