<template>
    <tr>
        <td class="id">{{line.id}}</td>
        <td class="login">{{line.login}}</td>
        <td class="login">{{line.name}}</td>
    </tr>
</template>

<script>
export default {
    name: "UsersLine",
    props: ["line"]
}
</script>

<style scoped>

</style>