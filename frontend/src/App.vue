<template>
    <div id="app">
        <Header :user="user"/>
        <Middle :posts="posts" :users="users" :postsComments="postsComments"/>
        <Footer/>
    </div>
</template>

<script>
import Header from "./components/Header";
import Middle from "./components/Middle";
import Footer from "./components/Footer";
import axios from "axios"


function validateLength(token, minLength, maxLength, tokenName, errorName, emiter) {
    if (!token || token.length > maxLength || token.length < minLength) {
        emiter.$emit(errorName, tokenName + " should be between " + minLength + " and " + maxLength + " symbols long");
        return false;
    }
    return true;
}

function validateLengthRegister(token, minLength, maxLength, tokenName, emiter) {
    return validateLength(token, minLength, maxLength, tokenName, "onRegisterValidationError", emiter);
}

function validateRegister(login, name, password, emiter) {
    if (!validateLengthRegister(login, 3, 16, "Login", emiter)) return false;
    if (!validateLengthRegister(name, 3, 32, "Name", emiter)) return false;
    if (!validateLengthRegister(password, 8, 32, "Password", emiter)) return false;
    if (!login.match(/^[a-z]+$/)) {
        emiter.$emit("onRegisterValidationError", "Login should use only lowercase latin letters");
        return false;
    }
    return true;
}

export default {
    name: 'App',
    components: {
        Footer,
        Middle,
        Header
    },
    data: function () {
        return {
            user: null,
            posts: [],
            users: [],
            postsComments: []
        }
    },
    beforeMount() {
        if (localStorage.getItem("jwt") && !this.user) {
            this.$root.$emit("onJwt", localStorage.getItem("jwt"));
        }

        axios.get("/api/1/posts").then(response => {
            this.posts = response.data;
        });

        axios.get("/api/1/users").then(response => {
            this.users = response.data;
        });
    },
    beforeCreate() {
        this.$root.$on("onEnter", (login, password) => {
            if (password === "") {
                this.$root.$emit("onEnterValidationError", "Password is required");
                return;
            }

            axios.post("/api/1/jwt", {
                    login, password
            }).then(response => {
                localStorage.setItem("jwt", response.data);
                this.$root.$emit("onJwt", response.data);
            }).catch(error => {
                this.$root.$emit("onEnterValidationError", error.response.data);
            });
        });

        this.$root.$on("onRegister", (login, name, password) => {
            if (validateRegister(login, name, password, this.$root)) {
                axios.post("/api/1/users", {
                    login, name, password
                }).then(response => {
                    localStorage.setItem("jwt", response.data);
                    this.$root.$emit("onJwt", response.data);
                }).catch(error => {
                    this.$root.$emit("onRegisterValidationError", error.response.data);
                });
            }
        });

        this.$root.$on("onJwt", (jwt) => {
            localStorage.setItem("jwt", jwt);

            axios.get("/api/1/users/auth", {
                params: {
                    jwt
                }
            }).then(response => {
                this.user = response.data;
                this.$root.$emit("onChangePage", "Index");
            }).catch(() => this.$root.$emit("onLogout"))
        });

        this.$root.$on("onLogout", () => {
            localStorage.removeItem("jwt");
            this.user = null;
        });

    }
}
</script>

<style>
#app {

}
</style>
