import { defineStore } from 'pinia';

export const useNavigatorStore = defineStore('navigator', {
    state: () => ({
        prev: null,
    }),
    actions: {
        setPrev(route){
            this.prev = route;
            console.log("setPrev", route);
        },
    },
});