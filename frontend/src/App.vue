<script setup>
import { RouterView } from "vue-router";
</script>

<template>
  <header class="mainHeader">
    <div class="mainHeader__inner">
      <h1 class="mainHeader__logo">RUNO</h1>
      <nav class="mainHeader__nav">
        <ul class="mainHeader__nav-list">
          <li class="mainHeader__nav-item">
            <a href="#" class="mainHeader__nav-link mainHeader__nav-link--active">Home</a>
          </li>
          <li class="mainHeader__nav-item">
            <a href="#" class="mainHeader__nav-link">About</a>
          </li>
          <li class="mainHeader__nav-item">
            <a href="#" class="mainHeader__nav-link">Articles</a>
          </li>
          <li class="mainHeader__nav-item">
            <a href="#" class="mainHeader__nav-link">Sign in</a>
          </li>
        </ul>
      </nav>
    </div>
  </header>

  <main>
    <RouterView />
  </main>

  <footer class="mainFooter">
    <div class="mainFooter__inner">
      <div class="mainFooter__row">

        <div class="mainFooter__column">
          <h3 class="mainFooter__column-heading">Contact the Publisher</h3>
          <ul class="mainFooter__column-list">
            <li class="mainFooter__column-listItem">
              <a class="mainFooter__column-listItem-link" href="#">mike@runo.com</a>
            </li>
            <li class="mainFooter__column-listItem">
              <a class="mainFooter__column-listItem-link" href="#">+944 450 904 505</a>
            </li>
          </ul>
        </div>

        <div class="mainFooter__column">
          <h3 class="mainFooter__column-heading">Explorate</h3>
          <ul class="mainFooter__column-list">
            <li class="mainFooter__column-listItem">
              <a class="mainFooter__column-listItem-link" href="#">About</a>
            </li>
            <li class="mainFooter__column-listItem">
              <a class="mainFooter__column-listItem-link" href="#">Partners</a>
            </li>
            <li class="mainFooter__column-listItem">
              <a class="mainFooter__column-listItem-link" href="#">Job Opportunities</a>
            </li>
            <li class="mainFooter__column-listItem">
              <a class="mainFooter__column-listItem-link" href="#">Advertise</a>
            </li>
            <li class="mainFooter__column-listItem">
              <a class="mainFooter__column-listItem-link" href="#">Membership</a>
            </li>
          </ul>
        </div>

        <div class="mainFooter__column">
          <h3 class="mainFooter__column-heading">Headquarter</h3>
          <p class="mainFooter__column-text">191 Middleville Road, NY 1001, Sydney Australia</p>
        </div>

        <div class="mainFooter__column">
          <h3 class="mainFooter__column-heading">Headquarter</h3>
          <p class="mainFooter__column-text">191 Middleville Road, NY 1001, Sydney Australia</p>
        </div>

      </div>
    </div>

    <div class="mainFooter__bottom">
      <div class="mainFooter__bottom__inner">
        <p class="mainFooter__bottom-text">2021 | RUNO Publisher Studio</p>
        <p class="mainFooter__bottom-text">Subscribe Now</p>
      </div>
    </div>
  </footer>
</template>

<style lang="scss">
@import "@/assets/main.scss";
</style>
