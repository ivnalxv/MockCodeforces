<template>
    <div>
        <Post v-for="post in posts" :post="post" :comments="postsComments[post.id]" :users="users" :view="false" :key="post.id"/>
    </div>
</template>

<script>

import Post from "./Post";

export default {
    name: "Index",
    props: ["posts", "postsComments", "users"],
    components: {Post}
}
</script>

<style scoped>

</style>