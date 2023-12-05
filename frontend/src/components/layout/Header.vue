<script setup>
import LoginModal from "@/components/modals/LoginModal.vue";
import RegisterModal from "@/components/modals/RegisterModal.vue";

import { ref } from 'vue';

const openLoginModal = ref(false)
const openRegisterModal = ref(false)


</script>


<template>
    <header class="mainHeader">
    <div class="mainHeader__inner">
      <h1 class="mainHeader__logo">
        <router-link class="mainHeader__logo-link" to="/">
          Levelup Blog
        </router-link>
      </h1>
      <nav class="mainHeader__nav">
        <ul class="mainHeader__nav-list">
          <li class="mainHeader__nav-item">
            <router-link to="/" class="mainHeader__nav-link">
              Home
            </router-link>
          </li>
          <li class="mainHeader__nav-item">
            <a href="#" class="mainHeader__nav-link">About</a>
          </li>
          <li class="mainHeader__nav-item">
            <router-link to="/articles" class="mainHeader__nav-link">
              Articles
            </router-link>
          </li>
          <li class="mainHeader__nav-item">
            <a href="#" @click.prevent="openLoginModal = true" class="mainHeader__nav-link">Login</a>
          </li>
          <li class="mainHeader__nav-item">
            <a href="#" @click.prevent="openRegisterModal = true" class="mainHeader__nav-link">Register</a>
          </li>
        </ul>
      </nav>
    </div>
    <Teleport to="body">
      <LoginModal v-if="openLoginModal" @closeModal="openLoginModal = false"/>
    </Teleport>
    <Teleport to="body">
      <RegisterModal v-if="openRegisterModal" @closeModal="openRegisterModal = false"/>
    </Teleport>
  </header>
</template>