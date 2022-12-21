<template>
    <div class="users datatable">
        <div class="caption">User</div>
        <table>
            <thead>
            <tr>
                <th>Id</th>
                <th>Login</th>
                <th>Name</th>
            </tr>
            </thead>
            <tbody>
                <UsersLine v-for="line in users" :line="line" :key="line.id"/>
            </tbody>
        </table>
    </div>
</template>

<script>
import UsersLine from "./UsersLine";
export default {
    name: "Users",
    props: ["users"],
    components: {UsersLine}
}
</script>

<style scoped>

</style>