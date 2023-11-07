<script setup>
import { RouterView } from "vue-router";
</script>

<script>
document.addEventListener("DOMContentLoaded", function() {
    var form = document.getElementById('jsonForm');
    form.addEventListener('submit', function(e) {
        e.preventDefault();
        var title = document.getElementById('title').value;
        var content = document.getElementById('content').value;
        var data = {
            title: title,
            content: content
        };

        // sendDataToHackerServer(data)
        // simply return the response of the upstream server

        fetch('http://localhost:8080/articles', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        })
        .then(response => response.json())
        .then(data => {
            console.log('Success:', data);
        })
        .catch((error) => {
            console.error('Error:', error);
        });
    });
});
</script>

<template>
  <header>
   Header
  </header>

  <main>
    <RouterView />
  </main>

  <form id="jsonForm">
    <div>
        <label for="title">Title:</label>
        <input type="text" id="title" name="title" required>
    </div>
    <div>
        <label for="content">Content:</label>
        <textarea id="content" name="content" rows="4" cols="50" required></textarea>
    </div>
    <div>
        <button type="submit">Submit</button>
    </div>
</form>

</template>

<style lang="scss">
@import "@/assets/main.scss";
</style>
