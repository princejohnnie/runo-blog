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
</template>